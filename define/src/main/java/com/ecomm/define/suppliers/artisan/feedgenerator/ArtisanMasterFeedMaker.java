package com.ecomm.define.suppliers.artisan.feedgenerator;

import com.ecomm.define.suppliers.artisan.constants.ArtisanConstants;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

    public List<ArtisanProduct> processMasterData(InputStream fileStream) throws FileNotFoundException {
        String line = "";
        String cvsSplitBy = ",";

        String[] avoidCodes = {"IN100", "IN101", "IN102", "IN200", "IN201", "IN202", "IN203", "IN204", "IN205", "IN206", "IN207", "IN208", "IN209", "IN210"};

        String productTitle = "";
        String productDesc = "";

        String width = "";
        String height = "";
        String depth = "";
        String weight = "";
        String imageURL1 = "";
        String imageURL2 = "";
        String imageURL3 = "";
        String imageURL4 = "";
        String imageURL5 = "";
        String imageURL6 = "";
        String imageURL7 = "";
        String imageURL8 = "";
        String imageURL9 = "";
        String imageURL10 = "";
        String imageURL11 = "";
        String imageURL12 = "";
        String imageURL13 = "";
        String imageURL14 = "";

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
                        productDesc = ArtisanURLReader.findProductDescription(doc);

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
                        productDesc = productDesc.concat("Packaged Weight " + ArtisanURLReader.findPackagedWeight(doc) + "\n");
                        productDesc = productDesc.concat("EAN " + ArtisanURLReader.findEAN(doc) + "\n");
                        productDesc = productDesc.concat("Material " + ArtisanURLReader.findMaterial(doc));
                        productDesc = productDesc.concat("Origin " + ArtisanURLReader.findOrigin(doc));

                        productDesc = productDesc.concat("Features \n ");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc, 1) + "\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc, 2) + "\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc, 3) + "\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc, 4) + "\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc, 5) + "\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc, 6) + "\n");

                        artisanProduct.setDescription(productDesc);

                        imageURL1 = ArtisanURLReader.findImageURL(doc, 0);
                        imageURL2 = ArtisanURLReader.findImageURL(doc, 1);
                        imageURL3 = ArtisanURLReader.findImageURL(doc, 2);
                        imageURL4 = ArtisanURLReader.findImageURL(doc, 3);
                        imageURL5 = ArtisanURLReader.findImageURL(doc, 4);
                        imageURL6 = ArtisanURLReader.findImageURL(doc, 5);
                        imageURL7 = ArtisanURLReader.findImageURL(doc, 6);
                        imageURL8 = ArtisanURLReader.findImageURL(doc, 7);
                        imageURL9 = ArtisanURLReader.findImageURL(doc, 8);
                        imageURL10 = ArtisanURLReader.findImageURL(doc, 9);
                        imageURL11 = ArtisanURLReader.findImageURL(doc, 10);
                        imageURL12 = ArtisanURLReader.findImageURL(doc, 11);
                        imageURL13 = ArtisanURLReader.findImageURL(doc, 12);
                        imageURL14 = ArtisanURLReader.findImageURL(doc, 13);
                        List<String> images = new ArrayList<>();
                        images.add(imageURL1);
                        images.add(imageURL2);
                        images.add(imageURL3);
                        images.add(imageURL4);
                        images.add(imageURL5);
                        images.add(imageURL6);
                        images.add(imageURL7);
                        images.add(imageURL8);
                        images.add(imageURL9);
                        images.add(imageURL10);
                        images.add(imageURL11);
                        images.add(imageURL12);
                        images.add(imageURL13);
                        images.add(imageURL14);
                        artisanProduct.setImages(images);

                        // titles.add(new String[]{sku, productTitle, productDesc, material, origin, ean, width, height, depth, weight, packagedWeight, bp1, bp2, bp3, bp4, bp5, bp6, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, imageURL6, imageURL7, imageURL8, imageURL9, imageURL10, imageURL11, imageURL12, imageURL13, imageURL14);
                        if (productTitle.length() == 0) {
                            System.out.println("Product Not Available" + productCode[0]);
                        } else {
                            // writer.writeNext(new String[]{sku, productTitle, productDesc, material, origin, ean, width, height, depth, weight, packagedWeight, bp1, bp2, bp3, bp4, bp5, bp6, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, imageURL6, imageURL7, imageURL8, imageURL9, imageURL10, imageURL11, imageURL12, imageURL13, imageURL14});
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