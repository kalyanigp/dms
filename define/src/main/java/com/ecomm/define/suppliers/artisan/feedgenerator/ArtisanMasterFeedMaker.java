package com.ecomm.define.suppliers.artisan.feedgenerator;

import com.ecomm.define.suppliers.artisan.constants.ArtisanConstants;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class ArtisanMasterFeedMaker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ArtisanMasterFeedMaker.class);

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
                ArtisanProduct artisanProduct = new ArtisanProduct();
                String[] productCode = line.split(cvsSplitBy);
                for (int index = 0; index < avoidCodes.length; index++) {
                    if (avoidCodes[index].equals(productCode[0])) {
                        System.out.println("Product Avoided to write " + productCode[0]);
                        avoided = true;
                        break;
                    }
                }
                if (!avoided) {
                    String productURL = ArtisanURLReader.generateProductURL(ArtisanConstants.FEEDMAKER_URL + productCode[0]);
                    artisanProduct.setSku(productCode[0]);
                    Document doc = ArtisanURLReader.getDocument(productURL);
                    if (doc != null) {
                        productTitle = ArtisanURLReader.findProductTitle(doc);
                        artisanProduct.setProductName(productTitle);
                        productDesc = ArtisanURLReader.findProductDescription(doc).concat("<br>");

                        height = ArtisanURLReader.findHeight(doc).replace("cm", "").replace("CM", "").replace(" ", "").replace(".", "");
                        width = ArtisanURLReader.findWidth(doc).replace("cm", "").replace("CM", "").replace(" ", "").replace(".", "");
                        depth = ArtisanURLReader.findDepth(doc).replace("cm", "").replace("CM", "").replace(" ", "").replace(".", "");
                        weight = ArtisanURLReader.findWeight(doc).replace("kg", "").replace("KG", "").replace("Kg", "").replace(" ", "").replace(".", "");
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
                        artisanProduct.setArrivalDate(ArtisanURLReader.findBulletPoint(doc, 10).replaceAll(("Next Container Arrives:"),""));
                        artisanProduct.setDescription(productDesc);
                        List<String> images = new ArrayList<>();

                        for (int i = 0; i <= 13; i++) {
                            images.add(ArtisanURLReader.findImageURL(doc, i));
                        }
                        artisanProduct.setImages(images);

                        if (images.size() == 0) {
                            System.out.println("Product Not Available" + productCode[0]);
                        } else {
                            artisanProductList.add(artisanProduct);
                            System.out.println("added" + artisanProduct.getSku() + artisanProduct.getArrivalDate() + artisanProduct.getAvailablityMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while processing Artisan Data from Artisan website");
        }
        return artisanProductList;
    }
}