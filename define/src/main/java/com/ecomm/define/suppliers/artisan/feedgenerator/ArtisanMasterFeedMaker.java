package com.ecomm.define.suppliers.artisan.feedgenerator;

import com.opencsv.CSVWriter;
import org.jsoup.nodes.Document;

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
    public static void main(String[] args) {

        String eanCSV = "/Users/vamshikirangullapelly/Downloads/Artisan EAN codes.CSV";

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

        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputTitlefile);

        // adding header to csv
        String[] avoidCodes = {"IN100", "IN101", "IN102"};
        String[] header = {"SKU", "Product Title", "Description", "Material", "Origin", "EAN", "Width", "Height", "Depth", "Weight", "packagedWeight", "BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "ImageURL1", "ImageURL2", "ImageURL3", "ImageURL4", "ImageURL5", "ImageURL6", "ImageURL7", "ImageURL8", "ImageURL9", "ImageURL10", "ImageURL11", "ImageURL12", "ImageURL13", "imageURL14"};
        writer.writeNext(header);
        String sku = "";
        String productTitle = "";
        String productDesc = "";
        String material = "";
        String ean = "";
        String width = "";
        String height = "";
        String depth = "";
        String weight = "";
        String packagedWeight = "";
        String bp1 = "";
        String bp2 = "";
        String bp3 = "";
        String bp4 = "";
        String bp5 = "";
        String bp6 = "";
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
        try (BufferedReader br = new BufferedReader(new FileReader(eanCSV))) {
            List<String[]> titles = new ArrayList<String[]>();

            while ((line = br.readLine()) != null) {
                boolean avoided = false;
                String[] productCode = line.split(cvsSplitBy);
                for (int index = 0; index < avoidCodes.length; index++) {
                    if (avoidCodes[index].equals(productCode[0])) {
                        System.out.println("Product Avoided to write " + productCode[0]);
                        avoided = true;
                    }
                }
                if (!avoided) {
                    String productURL = ArtisanURLReader.generateProductURL("https://www.artisanfurniture.net/?s=" + productCode[0]);
                    sku =  productCode[0];
                    Document doc = ArtisanURLReader.getDocument(productURL);
                    if (doc != null) {
                        productTitle = ArtisanURLReader.findProductTitle(doc);


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

                        height = ArtisanURLReader.findHeight(doc);
                        width = ArtisanURLReader.findWidth(doc);
                        depth = ArtisanURLReader.findDepth(doc);
                        weight = ArtisanURLReader.findWeight(doc);
                        packagedWeight = ArtisanURLReader.findPackagedWeight(doc);

                        origin = ArtisanURLReader.findOrigin(doc);
                        ean = ArtisanURLReader.findEAN(doc);
                        material = ArtisanURLReader.findMaterial(doc);
                        bp1 = ArtisanURLReader.findBP1(doc);
                        bp2 = ArtisanURLReader.findBP2(doc);
                        bp3 = ArtisanURLReader.findBP3(doc);
                        bp4 = ArtisanURLReader.findBP4(doc);
                        bp5 = ArtisanURLReader.findBP5(doc);
                        bp6 = ArtisanURLReader.findBP6(doc);

                        productDesc = ArtisanURLReader.findProductDescription(doc);

                       // titles.add(new String[]{sku, productTitle, productDesc, material, origin, ean, width, height, depth, weight, packagedWeight, bp1, bp2, bp3, bp4, bp5, bp6, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, imageURL6, imageURL7, imageURL8, imageURL9, imageURL10, imageURL11, imageURL12, imageURL13, imageURL14);
                        if (productTitle.length() == 0)
                        {
                            System.out.println("Product Not Available" + productCode[0]);
                        } else {
                            writer.writeNext(new String[]{sku, productTitle, productDesc, material, origin, ean, width, height, depth, weight, packagedWeight, bp1, bp2, bp3, bp4, bp5, bp6, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, imageURL6, imageURL7, imageURL8, imageURL9, imageURL10, imageURL11, imageURL12, imageURL13, imageURL14});
                            writer.flush();
                        }
                    }
                }
            }
         //   writer.writeAll(titles);
         //   writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

