package com.ecomm.define.suppliers.artisan.feedgenerator;

import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.bigcommerce.service.impl.GenerateBCArtisanDataServiceImpl;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class ArtisanMasterFeedMaker {
    private final static Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);

    public static void main(String[] args) {

        String eanCSV = "/Users/vamshikirangullapelly/Downloads/Artisan EAN codes Copy.CSV";

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
        String[] header = {"SKU", "Product Title", "Description", "Material", "Origin", "EAN", "Width", "Height", "Depth", "Weight", "packagedWeight", "BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "ImageURL1", "ImageURL2", "ImageURL3", "ImageURL4", "ImageURL5", "ImageURL6", "ImageURL7", "ImageURL8", "ImageURL9", "ImageURL10", "ImageURL11", "ImageURL12", "ImageURL13", "imageURL14"};
        String sku = "";
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
        List<BcProductData> bcProdcutList = new ArrayList<BcProductData>();

        try (BufferedReader br = new BufferedReader(new FileReader(eanCSV))) {

            while ((line = br.readLine()) != null) {
                boolean avoided = false;
                BcProductData bcProductData = new BcProductData();
                String[] productCode = line.split(cvsSplitBy);
                for (int index = 0; index < avoidCodes.length; index++) {
                    if (avoidCodes[index].equals(productCode[0])) {
                        System.out.println("Product Avoided to write " + productCode[0]);
                        avoided = true;
                    }
                }
                if (!avoided) {
                    String productURL = ArtisanURLReader.generateProductURL("https://www.artisanfurniture.net/?s=" + productCode[0]);
                    bcProductData.setSku(productCode[0]);
                    Document doc = ArtisanURLReader.getDocument(productURL);
                    if (doc != null) {
                        productTitle = ArtisanURLReader.findProductTitle(doc);
                        bcProductData.setPageTitle(productTitle);
                        bcProductData.setName(productTitle);
                        productDesc = ArtisanURLReader.findProductDescription(doc);

                        height = ArtisanURLReader.findHeight(doc).replace("cm","").replace("CM","").replace(" ","").replace(".","");
                        width = ArtisanURLReader.findWidth(doc).replace("cm","").replace("CM","").replace(" ","").replace(".","");
                        depth = ArtisanURLReader.findDepth(doc).replace("cm","").replace("CM","").replace(" ","").replace(".","");
                        weight = ArtisanURLReader.findWeight(doc).replace("kg","").replace("KG","").replace("Kg","").replace(" ","").replace(".","");
                        if (height != null && height.length()>0) {
                            bcProductData.setHeight(Integer.parseInt(height));
                        }
                        if (width != null && width.length()>0) {
                            bcProductData.setWidth(Integer.parseInt(width));
                        }

                        if (depth != null && depth.length()>0) {
                            bcProductData.setDepth(Integer.parseInt(depth));
                        }
                        if (weight != null && weight.length()>0) {
                            Double doubleWeight = Double.parseDouble(weight);
                            bcProductData.setWeight((int)doubleWeight.intValue());
                        }
                        productDesc = productDesc.concat("Packaged Weight "+ArtisanURLReader.findPackagedWeight(doc)+"\n");
                        productDesc = productDesc.concat("EAN "+ArtisanURLReader.findEAN(doc)+"\n");
                        productDesc = productDesc.concat("Material "+ ArtisanURLReader.findMaterial(doc));
                        productDesc = productDesc.concat("Origin "+ ArtisanURLReader.findOrigin(doc));

                        productDesc = productDesc.concat("Features \n ");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc,1)+"\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc,2)+"\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc,3)+"\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc,4)+"\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc,5)+"\n");
                        productDesc = productDesc.concat(ArtisanURLReader.findBulletPoint(doc,6)+"\n");

                        bcProductData.setDescription(productDesc);

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

                        // titles.add(new String[]{sku, productTitle, productDesc, material, origin, ean, width, height, depth, weight, packagedWeight, bp1, bp2, bp3, bp4, bp5, bp6, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, imageURL6, imageURL7, imageURL8, imageURL9, imageURL10, imageURL11, imageURL12, imageURL13, imageURL14);
                        if (productTitle.length() == 0)
                        {
                            System.out.println("Product Not Available" + productCode[0]);
                        } else {
                           // writer.writeNext(new String[]{sku, productTitle, productDesc, material, origin, ean, width, height, depth, weight, packagedWeight, bp1, bp2, bp3, bp4, bp5, bp6, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, imageURL6, imageURL7, imageURL8, imageURL9, imageURL10, imageURL11, imageURL12, imageURL13, imageURL14});
                            bcProdcutList.add(bcProductData);
                            System.out.println("added"+bcProductData.getSku());
                        }
                    }
                }
            }


        } catch (IOException e) {
            logger.error("Error while processing Artisan Data from Artisan website");
        }

        GenerateBCDataService impl = new GenerateBCArtisanDataServiceImpl();
        try {
            impl.generateBcProductsFromSupplier(bcProdcutList);
        } catch (Exception e) {
            logger.error("Error while uploading Artisan Data to Big Commerce");

        }
    }
}

