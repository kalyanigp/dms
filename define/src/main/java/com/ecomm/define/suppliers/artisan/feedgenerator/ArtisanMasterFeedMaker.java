package com.ecomm.define.suppliers.artisan.feedgenerator;

import com.ecomm.define.suppliers.artisan.constants.ArtisanConstants;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.artisan.repository.ArtisanProductRepository;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class ArtisanMasterFeedMaker {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArtisanMasterFeedMaker.class);
    private final ArtisanProductRepository repository;

    public ArtisanMasterFeedMaker(ArtisanProductRepository repository) {
        this.repository = repository;
    }


    public List<ArtisanProduct> processMasterData(InputStream fileStream) {
        String line = "";
        String cvsSplitBy = ",";

        String[] avoidCodes = {"IN100", "IN101", "IN102", "IN200", "IN201", "IN202", "IN203", "IN204", "IN205", "IN206", "IN207", "IN208", "IN209", "IN210"};

        String productTitle = "";
        String productDesc = "";

        String width = "";
        String height = "";
        String depth = "";
        String weight = "";

        List<ArtisanProduct> artisanProductList = new ArrayList<ArtisanProduct>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {

            while ((line = br.readLine()) != null) {
                boolean avoided = false;
                String[] productCode = line.split(cvsSplitBy);
                for (int index = 0; index < avoidCodes.length; index++) {
                    if (avoidCodes[index].equals(productCode[0])) {
                        LOGGER.info("Product Avoided to write  {} " , productCode[0]);
                        avoided = true;
                        break;
                    }
                }
                if (!avoided) {
                    String productURL = ArtisanURLReader.generateProductURL(ArtisanConstants.FEEDMAKER_URL + productCode[0]);
                    Optional<ArtisanProduct> byProductSku = repository.findByProductSku(productCode[0]);

                        if (!byProductSku.isPresent()) {
                            Document doc = ArtisanURLReader.getDocument(productURL);
                            if (doc != null) {
                                ArtisanProduct artisanProduct = new ArtisanProduct();
                                artisanProduct.setSku(productCode[0]);

                                productTitle = ArtisanURLReader.findProductTitle(doc);
                                artisanProduct.setProductName(productTitle);
                                productDesc = ArtisanURLReader.findProductDescription(doc).concat("<br>");

                                height = ArtisanURLReader.findHeight(doc).replaceAll("[^\\d.]", "").trim();
                                width = ArtisanURLReader.findWidth(doc).replaceAll("[^\\d.]", "").trim();
                                depth = ArtisanURLReader.findDepth(doc).replaceAll("[^\\d.]", "").trim();
                                weight = ArtisanURLReader.findWeight(doc).replaceAll("[^\\d.]", "").trim();
                                if (height != null && height.length() > 0) {
                                    artisanProduct.setHeight(new BigDecimal(height));
                                }
                                if (width != null && width.length() > 0) {
                                    artisanProduct.setWidth(new BigDecimal(width));
                                }

                                if (depth != null && depth.length() > 0) {
                                    artisanProduct.setDepth(new BigDecimal(depth));
                                }
                                if (weight != null && weight.length() > 0) {
                                    artisanProduct.setWeight(new BigDecimal(weight));
                                }
                                productDesc = productDesc.concat("Packaged Weight: " + ArtisanURLReader.findPackagedWeight(doc) + "<br>");
                                productDesc = productDesc.concat("Material: " + ArtisanURLReader.findMaterial(doc) + "<br>");
                                productDesc = productDesc.concat("Origin: " + ArtisanURLReader.findOrigin(doc) + "<br>");

                                productDesc = productDesc.concat("Features: <br> ");
                                artisanProduct.setBp1(ArtisanURLReader.findBulletPoint(doc, 1));
                                artisanProduct.setBp2(ArtisanURLReader.findBulletPoint(doc, 2));
                                artisanProduct.setBp3(ArtisanURLReader.findBulletPoint(doc, 3));
                                artisanProduct.setBp4(ArtisanURLReader.findBulletPoint(doc, 4));
                                artisanProduct.setBp5(ArtisanURLReader.findBulletPoint(doc, 5));
                                artisanProduct.setBp6(ArtisanURLReader.findBulletPoint(doc, 6));
                                artisanProduct.setEan(ArtisanURLReader.findEAN(doc));

                                artisanProduct.setAvailablityMessage(ArtisanURLReader.findBulletPoint(doc, 9));
                                artisanProduct.setArrivalDate(ArtisanURLReader.findBulletPoint(doc, 10).replaceAll(("Next Container Arrives:"), ""));
                                artisanProduct.setDescription(productDesc);
                                List<String> images = new ArrayList<>();

                                for (int i = 0; i <= 13; i++) {
                                    String imgURL = ArtisanURLReader.findImageURL(doc, i);
                                    if (!StringUtils.isEmpty(imgURL)) {
                                        images.add(imgURL);
                                    }
                                }
                                artisanProduct.setImages(images);

                                if (images.size() == 0) {
                                    LOGGER.info("Product Not Available {} ", productCode[0]);
                                } else {
                                    artisanProductList.add(artisanProduct);
                                    LOGGER.info("added {}" + artisanProduct.getSku());
                                }
                            }
                    } else {
                            ArtisanProduct artisanProduct = byProductSku.get();
                            Document doc = ArtisanURLReader.getDocument(productURL);
                            if (doc != null) {
                                artisanProduct.setAvailablityMessage(ArtisanURLReader.findBulletPoint(doc, 9));
                                artisanProduct.setArrivalDate(ArtisanURLReader.findBulletPoint(doc, 10).replaceAll(("Next Container Arrives:"), ""));
                                artisanProductList.add(artisanProduct);
                            }
                        LOGGER.info("Updated Arrival Dates for {}" , artisanProduct.getSku());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while processing Artisan Data from Artisan website");
            e.printStackTrace();
        }
        return artisanProductList;
    }
}