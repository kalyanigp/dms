package com.ecomm.define.suppliers.hillinterior.feedgenerator;

import com.ecomm.define.suppliers.hillinterior.constants.HillInteriorConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 08/07/2020.
 */
public class HillInteriorURLReader {

    public static String generateProductURL(String url) {
        String productURL = "";
        try {
            Document doc = Jsoup.connect(url).get();
            if (!doc.getElementsByClass("product-link clearfix").isEmpty()) {
                productURL = doc.getElementsByClass("product-link clearfix").get(0).getElementsByAttribute("href").toString();
                productURL = productURL.substring(9, productURL.indexOf("\" class"));
            }
            //System.out.println("Product Link *** "+links.get(81).getElementsByAttribute("href").attr("href").toString());
           // productURL = links.get(81).getElementsByAttribute("href").attr("href").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HillInteriorConstants.BASE_URL+productURL;
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



    public static List<String> addImages(Document doc) {
        String imageURL = "";
        List<String> images = new ArrayList<String>();
            imageURL = doc.select("div[id*=product-album-main").select("a[href]").toString();
        if (imageURL.length()>8) {
            imageURL = imageURL.substring(8, imageURL.indexOf("rel="));
        }
            images.add(imageURL);
         System.out.print(imageURL+ "**");

        // Elements links = doc.getElementsByClass("download-highres");

        findMoreURLs(doc, images, "zoom1");
        findMoreURLs(doc, images, "zoom2");
        findMoreURLs(doc, images, "zoom3");

        return images;
    }

    private static void findMoreURLs(Document doc, List<String> images, String zoom) {
        String fullURL;
        String partURL;
        if (doc.getElementById(zoom) != null){
            fullURL = doc.getElementById(zoom).select("a[href]").toString();
            partURL = fullURL.substring(fullURL.indexOf("smallimage: ")+13,fullURL.indexOf("largeimage: ")-2);
            images.add(partURL);
            System.out.print(partURL + "**");

            partURL = fullURL.substring(fullURL.indexOf("largeimage: ")+13,fullURL.indexOf("}\">")-1);
            images.add(partURL);
            System.out.print(partURL+ "**");

            partURL = fullURL.substring(fullURL.indexOf("<img data-src=")+15,fullURL.indexOf("\" alt="));
            images.add(partURL);
            System.out.println(partURL);

        }
    }
}
