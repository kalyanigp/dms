package com.ecomm.define.suppliers.hillinterior.feedgenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by vamshikirangullapelly on 08/07/2020.
 */
public class HillInteriorURLReader {

    public static String generateProductURL(String url) {
        String productURL = "";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            //System.out.println("Product Link *** "+links.get(81).getElementsByAttribute("href").attr("href").toString());
            productURL = links.get(81).getElementsByAttribute("href").attr("href").toString();
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

    public static String findProductDescription(Document doc) {
        String productDesc = "";

        Elements links = doc.select("div");
        int index = 178;
        while (index > 160) {
            if (links.get(index).getElementsByClass("clearfix inactive").size() > 0) {
                if (links.get(index).ownText() != null && links.get(index).ownText().length() != 0) {
                    productDesc = links.get(index).ownText();
                } else {
                    if (links.get(index).getElementsByTag("div") != null && links.get(index).getElementsByTag("div").size() > 2) {
                        productDesc = links.get(index).getElementsByTag("div").get(3).ownText();
                    }
                }
                if (productDesc.length() == 0) {
                    productDesc = links.get(index).getElementsByClass("clearfix inactive").toString();
                } else {
                    break;
                }
            }
            index--;
        }
        return productDesc;
    }

    public static String findProductTitle(Document doc) {
        String productTitle = "";
        Elements links = doc.select("title");
        String titleString = links.toString();
        if (titleString.contains("Wholesale")) {
            productTitle = titleString.substring(7, titleString.indexOf("Wholesale"));
        }
        return productTitle;
    }

    public static String findMaterial(Document doc) {
        String material = "";
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 1) {
            material = links.get(0).ownText();
        }
        return material;
    }

    public static String findBulletPoint(Document doc, int index) {
        String bulletPoint = "";

        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > index + 1) {
            bulletPoint = links.get(index).ownText();
        }

        return bulletPoint;
    }


    public static String findEAN(Document doc) {
        String ean = "";
        Elements links = doc.getElementsByClass("elementor-text-editor elementor-clearfix");
        if (links != null && links.size() > 8) {
            ean = links.get(7).ownText();
        }

        return ean;
    }

    public static String findOrigin(Document doc) {
        String origin = "";
        Elements links = doc.getElementsByClass("elementor-text-editor elementor-clearfix");
        if (links != null && links.size() > 7) {
            origin = links.get(6).ownText();
        }

        return origin;
    }

    public static String findHeight(Document doc) {
        String height = "";
        Elements links = doc.getElementsByClass("ee-table__cell elementor-repeater-item-e85a72b");
        if (links != null) {
            height = links.text();
        }

        return height;
    }

    public static String findWidth(Document doc) {
        String width = "";
        Elements links = doc.getElementsByClass("ee-table__cell elementor-repeater-item-3ca4416");
        if (links != null) {
            width = links.text();
        }

        return width;
    }

    public static String findDepth(Document doc) {
        String depth = "";
        Elements links = doc.getElementsByClass("ee-table__cell elementor-repeater-item-4ae25f3");
        if (links != null) {
            depth = links.text();
        }

        return depth;
    }

    public static String findWeight(Document doc) {
        String weight = "";
        Elements links = doc.getElementsByClass("ee-table__cell elementor-repeater-item-a15f3fd");
        if (links != null) {
            weight = links.text();
        }

        return weight;
    }

    public static String findPackagedWeight(Document doc) {
        String packWeight = "";
        Elements links = doc.getElementsByClass("ee-table__cell elementor-repeater-item-91fa573");
        if (links != null) {
            packWeight = links.text();
        }

        return packWeight;
    }

    public static String findImageURL(Document doc, int number) {
        String imageURL = "";
        Elements links = doc.getElementsByClass("woocommerce-product-gallery__image");

        if (links != null && links.size() > number) {
            if (links.get(number).child(0) != null) {
                imageURL = links.get(number).child(0).absUrl("href");
            }
        }
        return imageURL;
    }
}
