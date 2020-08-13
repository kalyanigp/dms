package com.ecomm.define.suppliers.hillinterior.feedgenerator;

import com.ecomm.define.suppliers.hillinterior.constants.HillInteriorConstants;
import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class HillInteriorMasterFeedMaker {
    private final static Logger LOGGER = LoggerFactory.getLogger(HillInteriorMasterFeedMaker.class);

    public static void main(String args[]) {

        HillInteriorMasterFeedMaker maker = new HillInteriorMasterFeedMaker();
        File hillInteriorsCode = new File("/Users/vamshikirangullapelly/Downloads/Uploads_110820/HillInteriors110820.csv");

        try {
            maker.processMasterData(new FileInputStream(hillInteriorsCode));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<HillInteriorProduct> processMasterData(InputStream fileStream) {
        String line = "";
        String cvsSplitBy = ",";


        List<HillInteriorProduct> hillInteriorProducts = new ArrayList<HillInteriorProduct>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                HillInteriorProduct hillInteriorProduct = new HillInteriorProduct();
                String[] productCode = line.split(cvsSplitBy);

                String code = productCode[0].replaceAll("\"", "");
                String productURL = HillInteriorURLReader.generateProductURL(HillInteriorConstants.FEEDMAKER_URL + code);
                hillInteriorProduct.setSku(productCode[0]);
                Document doc = HillInteriorURLReader.getDocument(productURL);
                if (doc != null) {
                    System.out.print("Finding for " + code);
                    List<String> images = HillInteriorURLReader.addImages(doc);
                    hillInteriorProduct.setImages(images);
                }

            }
        } catch (IOException e) {
            LOGGER.error("Error while processing Hill Interior Data from Hill Interior website");
        }
        return hillInteriorProducts;
    }
}