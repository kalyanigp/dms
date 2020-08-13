package com.ecomm.define.suppliers.furniture2go.feedgenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 08/07/2020.
 */
public class Furniture2GoURLReader {

    public static String generateProductURL(String url) {
        String productURL = "";
        try {
            Document doc = Jsoup.connect(url).get();
            String href = doc.getElementsByClass("product-item-wrap").toString();
            if (href.length() > 0) {
                productURL = href.substring(href.indexOf("<a href=") + 9, href.indexOf(" title=") - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productURL;
    }

    public static Document getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }


    public static List<String> findImageURL(Document doc) {
        String imageURL = "";
        List<String> images = new ArrayList<>();

        Elements links = doc.getElementsByClass("more-views");
        if (links != null && links.size() > 0 && links.get(0).child(1) != null) {
            for (int i = 0; i <= 13; i++) {
                if (links.get(0).child(1).children().size() > i) {
                    String fullText = links.get(0).child(1).child(i).toString();
                    if (fullText.contains("<a href=") && fullText.contains("rel=\"popupWin:")) {
                        imageURL = fullText.substring(fullText.indexOf("<a href=") + 9, fullText.indexOf("rel=\"popupWin:") - 2);
                        if (imageURL != null && !imageURL.isEmpty()) {
                            images.add(imageURL);
                        }
                    }
                }

            }

        }
        return images;
    }
}
