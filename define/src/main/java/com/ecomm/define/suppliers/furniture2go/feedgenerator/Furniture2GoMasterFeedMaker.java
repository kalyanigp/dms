package com.ecomm.define.suppliers.furniture2go.feedgenerator;

import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.opencsv.CSVWriter;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class Furniture2GoMasterFeedMaker {
    private final static Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);

    public static void main(String args[]) {

        Furniture2GoMasterFeedMaker maker = new Furniture2GoMasterFeedMaker();
        maker.processMasterData();
    }

    public void processMasterData() {

        String line = "";
        String cvsSplitBy = ",";

        FileWriter outputImageFile = null;
        try {
            outputImageFile = new FileWriter(new File("/Users/vamshikirangullapelly/Downloads/F2G_Images.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // adding header to csv
      //  String[] avoidCodes = {"IN100", "IN101", "IN102", "IN200", "IN201", "IN202", "IN203", "IN204", "IN205", "IN206", "IN207", "IN208", "IN209", "IN210"};
        String[] header = {"SKU", "ImageURL1", "ImageURL2", "ImageURL3", "ImageURL4", "ImageURL5", "ImageURL6", "ImageURL7", "ImageURL8", "ImageURL9", "ImageURL10", "ImageURL11", "ImageURL12", "ImageURL13", "imageURL14"};
        String sku = "";

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
        //  create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputImageFile);

        String fileName = "/Users/vamshikirangullapelly/Downloads/F2G_Master_20200721.csv";
        writer.writeNext(header);

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName))) {
            BufferedReader br = new BufferedReader(inputStreamReader);
            //ignore Header
            br.readLine();
            while ((line = br.readLine()) != null) {
                boolean avoided = false;
                String[] productCode = line.split(cvsSplitBy);
                sku = productCode[0];

                String productURL = Furniture2GoURLReader.generateProductURL("https://furniture-to-go.co.uk/catalogsearch/result/?q=" + sku);
                    if (productURL.length() > 0) {
                        Document doc = Furniture2GoURLReader.getDocument(productURL);
                        if (doc != null) {

                            imageURL1 = Furniture2GoURLReader.findImageURL(doc, 0);
                            imageURL2 = Furniture2GoURLReader.findImageURL(doc, 1);
                            imageURL3 = Furniture2GoURLReader.findImageURL(doc, 2);
                            imageURL4 = Furniture2GoURLReader.findImageURL(doc, 3);
                            imageURL5 = Furniture2GoURLReader.findImageURL(doc, 4);
                            imageURL6 = Furniture2GoURLReader.findImageURL(doc, 5);
                            imageURL7 = Furniture2GoURLReader.findImageURL(doc, 6);
                            imageURL8 = Furniture2GoURLReader.findImageURL(doc, 7);
                            imageURL9 = Furniture2GoURLReader.findImageURL(doc, 8);
                            imageURL10 = Furniture2GoURLReader.findImageURL(doc, 9);
                            imageURL11 = Furniture2GoURLReader.findImageURL(doc, 10);
                            imageURL12 = Furniture2GoURLReader.findImageURL(doc, 11);
                            imageURL13 = Furniture2GoURLReader.findImageURL(doc, 12);
                            imageURL14 = Furniture2GoURLReader.findImageURL(doc, 13);

                            writer.writeNext(new String[]{sku, imageURL1, imageURL2, imageURL3, imageURL4, imageURL5, imageURL6, imageURL7, imageURL8, imageURL9, imageURL10, imageURL11, imageURL12, imageURL13, imageURL14});
                        }
                    }
                    else {
                        System.out.println("Product Not Found "+sku);
                    }
                writer.flush();
           }
        } catch (IOException ioe) {
            System.out.print("Error while processing Furniture2Go Data from Furniture2Go website" + ioe.getMessage());
        }
    }
}

