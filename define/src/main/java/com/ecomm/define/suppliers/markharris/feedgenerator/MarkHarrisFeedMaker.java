package com.ecomm.define.suppliers.markharris.feedgenerator;

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
import java.util.List;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class MarkHarrisFeedMaker {
    private final static Logger logger = LoggerFactory.getLogger(MarkHarrisFeedMaker.class);

    public static void main(String args[]) {

        MarkHarrisFeedMaker maker = new MarkHarrisFeedMaker();
        maker.processMasterData();
    }

    private void processMasterData() {

        String line = "";
        String cvsSplitBy = ",";

        FileWriter outputImageFile = null;
        try {
            outputImageFile = new FileWriter(new File("/Users/vamshikirangullapelly/Downloads/MarkHarris_Images.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // adding header to csv
        //  String[] avoidCodes = {"IN100", "IN101", "IN102", "IN200", "IN201", "IN202", "IN203", "IN204", "IN205", "IN206", "IN207", "IN208", "IN209", "IN210"};
        String[] header = {"SKU", "ImageURL1", "ImageURL2", "ImageURL3", "ImageURL4", "ImageURL5", "ImageURL6", "ImageURL7", "ImageURL8", "ImageURL9", "ImageURL10", "ImageURL11", "ImageURL12", "ImageURL13", "imageURL14"};
        String sku = "";


        //  create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputImageFile);

        String fileName = "/Users/vamshikirangullapelly/Downloads/MarkHarris EAN Codes.csv";
        writer.writeNext(header);

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName))) {
            BufferedReader br = new BufferedReader(inputStreamReader);
            //ignore Header
            br.readLine();
            while ((line = br.readLine()) != null) {
                boolean avoided = false;
                String[] productCode = line.split(cvsSplitBy);
                sku = productCode[0];
                System.out.println("SKU"+sku);


                String productURL = MarkHarrisURLReader.generateProductURL("http://markharrisfurniture.co.uk/catalogsearch/result/?q=" + sku);
                if (productURL.length() > 0) {
                    Document doc = MarkHarrisURLReader.getDocument(productURL);
                    List<String> imageURLList;
                    MarkHarrisURLReader.findVideoURLList(doc);

                   if (doc != null) {
                        imageURLList = MarkHarrisURLReader.findImageURLList(doc);

                        switch (imageURLList.size()) {
                            case 17:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9), imageURLList.get(10), imageURLList.get(11), imageURLList.get(12), imageURLList.get(13), imageURLList.get(14), imageURLList.get(15), imageURLList.get(16)});
                                break;
                            case 16:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9), imageURLList.get(10), imageURLList.get(11), imageURLList.get(12), imageURLList.get(13), imageURLList.get(14), imageURLList.get(15)});
                                break;
                            case 15:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9), imageURLList.get(10), imageURLList.get(11), imageURLList.get(12), imageURLList.get(13), imageURLList.get(14)});
                                break;
                            case 14:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9), imageURLList.get(10), imageURLList.get(11), imageURLList.get(12), imageURLList.get(13)});
                                break;
                            case 13:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9), imageURLList.get(10), imageURLList.get(11), imageURLList.get(12)});
                                break;
                            case 12:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9), imageURLList.get(10), imageURLList.get(11)});
                                break;
                            case 11:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9), imageURLList.get(10)});
                                break;
                            case 10:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8), imageURLList.get(9)});
                                break;
                            case 9:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7), imageURLList.get(8)});
                                break;
                            case 8:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6), imageURLList.get(7)});
                                break;
                            case 7:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5), imageURLList.get(6)});
                                break;
                            case 6:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4), imageURLList.get(5)});
                                break;
                            case 5:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3), imageURLList.get(4)});
                                break;
                            case 4:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2), imageURLList.get(3)});
                                break;
                            case 3:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1), imageURLList.get(2)});
                                break;
                            case 2:
                                writer.writeNext(new String[]{sku, imageURLList.get(0), imageURLList.get(1)});
                                break;
                            case 1:
                                writer.writeNext(new String[]{sku, imageURLList.get(0)});
                                break;

                            default:
                                writer.writeNext(new String[]{sku});
                                break;
                            }
                    }
                } else {
                    System.out.println("Product Not Found " + sku);
                }
                writer.flush();
            }
        } catch (IOException ioe) {
            System.out.print("Error while processing Mark Harris Data from Mark Harris website" + ioe.getMessage());
        }
    }
}

