package com.ecomm.define.suppliers.lpdfurniture.feedgenerator;

import com.ecomm.define.suppliers.hillinterior.constants.HillInteriorConstants;
import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import com.ecomm.define.suppliers.hillinterior.feedgenerator.HillInteriorURLReader;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class LpdMasterFeedMaker {
    private final static Logger LOGGER = LoggerFactory.getLogger(LpdMasterFeedMaker.class);

    public static List<String> getProductImages(HillInteriorProduct hillInteriorProduct) {

        LOGGER.info("Started Processing Hill Interior Image URLs");
        List<String> images = new ArrayList<>();
        try {
            String productURL = com.ecomm.define.suppliers.hillinterior.feedgenerator.HillInteriorURLReader.generateProductURL(HillInteriorConstants.FEEDMAKER_URL + hillInteriorProduct.getSku());
            if (productURL !=null && !productURL.isEmpty()) {
                Document doc = com.ecomm.define.suppliers.hillinterior.feedgenerator.HillInteriorURLReader.getDocument(productURL);
                images = HillInteriorURLReader.addImages(doc);
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing Hill Interior Data from Hill Interior website");
        }
        LOGGER.info("Finished processing Hill Interior Image URLs for sku {} , images {}", hillInteriorProduct.getSku(), images);
        return images;
    }

}