package com.ecomm.define.suppliers.markharris.feedgenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Created by vamshikirangullapelly on 08/07/2020.
 */
public class MarkHarrisURLReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(MarkHarrisURLReader.class);

    public static String generateProductURL(String url) {
        String productURL = "";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("product-item-link");
            if (elements.size() > 0) {
                String href = elements.get(0).getElementsByAttribute("href").toString();
                if (href.length() > 0) {
                    productURL = href.substring(href.indexOf("href=") + 6, href.indexOf("> <h3 class=") - 1);
                }
            } else {
                LOGGER.info("Unable to process images from url {}", url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productURL;
    }

    public static Document getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).timeout(60000).get();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }


    public static List<String> findImageURLList(Document doc) {

        String wholeData = doc.getElementsByTag("script").get(17).dataNodes().get(0).getWholeData().toString();

        List<String> imageURLList = new ArrayList<String>();
        List<String> urlTokens = getTokens(wholeData);
        for (String token:urlTokens){
            if (token.startsWith("\"full\"")){
                imageURLList.add(token.substring(8, token.length() - 1).replaceAll("\\\\",""));
            }
        }
        return imageURLList;
    }

    public static List<String> findVideoURLList(Document doc) {

        String wholeData = doc.getElementsByTag("script").get(17).dataNodes().get(0).getWholeData().toString();

        List<String> imageURLList = new ArrayList<String>();
        List<String> urlTokens = getTokens(wholeData);
        for (String token:urlTokens){
            if (token.startsWith("\"videoUrl\"")){
                imageURLList.add(token.substring(11,token.length()-1));
                System.out.println(token.substring(11,token.length()-1));
            }
        }
        return imageURLList;
    }

    public static List<String> getTokens(String str) {
        return Collections.list(new StringTokenizer(str, ",")).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }
}
