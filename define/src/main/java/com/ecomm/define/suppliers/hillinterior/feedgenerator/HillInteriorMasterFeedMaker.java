package com.ecomm.define.suppliers.hillinterior.feedgenerator;

import com.ecomm.define.suppliers.artisan.constants.ArtisanConstants;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader;
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
public class HillInteriorMasterFeedMaker {
    private final static Logger LOGGER = LoggerFactory.getLogger(HillInteriorMasterFeedMaker.class);

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
                    String productURL = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.generateProductURL(ArtisanConstants.FEEDMAKER_URL + productCode[0]);
                    artisanProduct.setSku(productCode[0]);
                    Document doc = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.getDocument(productURL);
                    if (doc != null) {
                        productTitle = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findProductTitle(doc);
                        artisanProduct.setProductName(productTitle);
                        productDesc = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findProductDescription(doc);

                        height = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findHeight(doc).replace("cm", "").replace("CM", "").replace(" ", "").replace(".", "");
                        width = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findWidth(doc).replace("cm", "").replace("CM", "").replace(" ", "").replace(".", "");
                        depth = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findDepth(doc).replace("cm", "").replace("CM", "").replace(" ", "").replace(".", "");
                        weight = com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findWeight(doc).replace("kg", "").replace("KG", "").replace("Kg", "").replace(" ", "").replace(".", "");
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
                        productDesc = productDesc.concat("Packaged Weight " + com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findPackagedWeight(doc) + "\n");
                        productDesc = productDesc.concat("EAN " + com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findEAN(doc) + "\n");
                        productDesc = productDesc.concat("Material " + com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findMaterial(doc));
                        productDesc = productDesc.concat("Origin " + com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findOrigin(doc));

                        productDesc = productDesc.concat("Features \n ");
                        for (int i = 1; i <= 6; i++) {
                            productDesc = productDesc.concat(com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanURLReader.findBulletPoint(doc, i) + "\n");
                        }
                        artisanProduct.setDescription(productDesc);
                        List<String> images = new ArrayList<>();

                        for (int i = 0; i <= 13; i++) {
                            images.add(ArtisanURLReader.findImageURL(doc, i));
                        }
                        artisanProduct.setImages(images);

                        if (productTitle.length() == 0) {
                            System.out.println("Product Not Available" + productCode[0]);
                        } else {
                            artisanProductList.add(artisanProduct);
                            System.out.println("added" + artisanProduct.getSku());
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