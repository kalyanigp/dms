package com.ecomm.define.suppliers.coreproducts.feedgenerator;

import com.ecomm.define.suppliers.coreproducts.domain.CoreProduct;
import org.apache.commons.collections4.map.HashedMap;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ecomm.define.suppliers.markharris.constants.MarkHarrisConstants.FEEDMAKER_URL;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class CoreProductsFeedMaker {
    private final static Logger LOGGER = LoggerFactory.getLogger(CoreProductsFeedMaker.class);

    /*public static Map<String, List<String>> getCatalogImages(List<MarkHarrisProduct> furniture2GoProducts) {
        LOGGER.info("Started Processing Mark Harris Image URLs");
        Map<String, List<String>> imagesMap = new HashedMap<>();
        furniture2GoProducts.forEach(product -> {
            String productURL = MarkHarrisURLReader.generateProductURL(FEEDMAKER_URL + product.getSku());
            if (productURL.length() > 0) {
                Document doc = MarkHarrisURLReader.getDocument(productURL);
                if (doc != null) {
                    imagesMap.put(product.getSku(), MarkHarrisURLReader.findImageURLList(doc));
                }
            }
        });
        LOGGER.info("Finished Processing Mark Harris Image URLs");
        return imagesMap;
    }*/


    public static List<String> getCatalogImages(CoreProduct coreProduct) {
        LOGGER.info("Started Processing Mark Harris Image URLs");
        List<String> images = new ArrayList<>();
        String productURL = CoreProductsURLReader.generateProductURL(FEEDMAKER_URL + coreProduct.getSku());
        if (productURL.length() > 0) {
            Document doc = CoreProductsURLReader.getDocument(productURL);
            images = CoreProductsURLReader.findImageURLList(doc);
        }
        return images;
    }


    public static Map<String, List<String>> getCatalogImages(List<CoreProduct> furniture2GoProducts) {
        Map<String, List<String>> imagesMap = new HashedMap<>();
        furniture2GoProducts.forEach(product -> {
            String productURL = CoreProductsURLReader.generateProductURL(FEEDMAKER_URL + product.getSku());
            if (productURL.length() > 0) {
                Document doc = CoreProductsURLReader.getDocument(productURL);
                if (doc != null) {
                    List<String> imageURLList = CoreProductsURLReader.findImageURLList(doc);
                    LOGGER.info("Images for SKU {}, and Images {}", product.getSku(), imageURLList);
                    imagesMap.put(product.getSku(), imageURLList);
                }
            }
        });
        return imagesMap;
    }
}

