package com.ecomm.define.suppliers.furniture2go.feedgenerator;

import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import org.apache.commons.collections4.map.HashedMap;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vamshikirangullapelly on 07/07/2020.
 */
public class Furniture2GoMasterFeedMaker {
    public static Map<String, List<String>> getCatalogImages(List<Furniture2GoProduct> furniture2GoProducts) {
        Map<String, List<String>> imagesMap = new HashedMap<>();
        furniture2GoProducts.forEach(product -> {
            String productURL = Furniture2GoURLReader.generateProductURL("https://furniture-to-go.co.uk/catalogsearch/result/?q=" + product.getSku());
            List<String> images = new ArrayList<>();
            if (productURL.length() > 0) {
                Document doc = Furniture2GoURLReader.getDocument(productURL);
                if (doc != null) {
                    for (int i = 0; i <= 13; i++) {
                        images.add(Furniture2GoURLReader.findImageURL(doc, i));
                    }
                    imagesMap.put(product.getSku(), images);
                }
            }
        });
        return imagesMap;
    }
}

