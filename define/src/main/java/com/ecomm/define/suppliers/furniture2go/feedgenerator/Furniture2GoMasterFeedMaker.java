package com.ecomm.define.suppliers.furniture2go.feedgenerator;

import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ecomm.define.suppliers.furniture2go.constants.Furniture2GoConstants.FURNITURE_2_GO_FEED_URL;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class Furniture2GoMasterFeedMaker {
    private final static Logger LOGGER = LoggerFactory.getLogger(Furniture2GoMasterFeedMaker.class);

    public static List<String> getCatalogImages(Furniture2GoProduct product) {
        LOGGER.info("Started Processing Furniture2Go Image URLs");
        List<String> images = new ArrayList<>();
        try {
            String productURL = Furniture2GoURLReader.generateProductURL(FURNITURE_2_GO_FEED_URL + product.getSku());
            if (productURL.length() > 0) {
                Document doc = Furniture2GoURLReader.getDocument(productURL);
                images = Furniture2GoURLReader.findImageURL(doc);
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing Hill Interior Data from Hill Interior website");
        }
        LOGGER.info("Finished processing Hill Interior Image URLs for sku {} , images {}", product.getSku(), images);
        return images;
    }
}

