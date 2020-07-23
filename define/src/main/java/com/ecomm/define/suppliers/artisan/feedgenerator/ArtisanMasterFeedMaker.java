package com.ecomm.define.suppliers.artisan.feedgenerator;

import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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
    private final static Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);

    public List<ArtisanProduct> processMasterData(InputStream fileStream) {

      //  String eanCSV = "/Users/vamshikirangullapelly/Downloads/Artisan EAN codes Copy.CSV";

        String line = "";
        String cvsSplitBy = ",";

        // create FileWriter object with file as parameter
        File titleFile = new File("/Users/vamshikirangullapelly/Downloads/ArtisanTitles.csv");
        FileWriter outputTitlefile = null;
        try {
            outputTitlefile = new FileWriter(titleFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // adding header to csv
        String[] avoidCodes = {"IN100", "IN101", "IN102", "IN200", "IN201", "IN202", "IN203", "IN204", "IN205", "IN206", "IN207", "IN208", "IN209", "IN210"};
      //  String[] header = {"SKU", "Product Title", "Description", "Material", "Origin", "EAN", "Width", "Height", "Depth", "Weight", "packagedWeight", "BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "ImageURL1", "ImageURL2", "ImageURL3", "ImageURL4", "ImageURL5", "ImageURL6", "ImageURL7", "ImageURL8", "ImageURL9", "ImageURL10", "ImageURL11", "ImageURL12", "ImageURL13", "imageURL14"};
      //  String sku = "";
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

        String origin = "";
        String[] header = {"SKU", "Product Title", "Description", "Material", "Origin", "EAN", "Width", "Height", "Depth", "Weight", "packagedWeight", "BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "ImageURL1", "ImageURL2", "ImageURL3", "ImageURL4", "ImageURL5", "ImageURL6", "ImageURL7", "ImageURL8", "ImageURL9", "ImageURL10", "ImageURL11", "ImageURL12", "ImageURL13", "imageURL14"};

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
                    String productURL = ArtisanURLReader.generateProductURL("https://www.artisanfurniture.net/?s=" + productCode[0]);
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
                           // Double doubleWeight = Double.parseDouble(weight);
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
                        artisanProduct.setImageURL1(imageURL1);
                        artisanProduct.setImageURL2(imageURL2);
                        artisanProduct.setImageURL3(imageURL3);
                        artisanProduct.setImageURL4(imageURL4);
                        artisanProduct.setImageURL5(imageURL5);
                        artisanProduct.setImageURL6(imageURL6);
                        artisanProduct.setImageURL7(imageURL7);
                        artisanProduct.setImageURL8(imageURL8);
                        artisanProduct.setImageURL9(imageURL9);
                        artisanProduct.setImageURL10(imageURL10);
                        artisanProduct.setImageURL11(imageURL11);
                        artisanProduct.setImageURL12(imageURL12);
                        artisanProduct.setImageURL13(imageURL13);
                        artisanProduct.setImageURL14(imageURL14);

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
            logger.error("Error while processing Artisan Data from Artisan website");
        }
        return artisanProductList;
    }
}

