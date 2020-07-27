package com.ecomm.define.suppliers.furniture2go.feedgenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

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

               // Elements links = doc.select("a[href]");
                //System.out.println("Product Link *** "+links.get(81).getElementsByAttribute("href").attr("href").toString());
                //   productURL = links.get(81).getElementsByAttribute("href").attr("href").toString();
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


    public static String findImageURL(Document doc, int number) {
        String imageURL = "";
        Elements links = doc.getElementsByClass("more-views");

        if (links != null && links.size() > 0 && links.get(0).child(1) != null) {
            if (links.get(0).child(1).children().size() > number) {
                String fullText = links.get(0).child(1).child(number).toString();
                if (fullText.contains("<a href=") && fullText.contains("rel=\"popupWin:")) {
                    imageURL = fullText.substring(fullText.indexOf("<a href=") + 9, fullText.indexOf("rel=\"popupWin:") - 2);
                }
            }
        }
        return imageURL;
    }
}
