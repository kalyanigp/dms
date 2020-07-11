package com.ecomm.define.suppliers.artisan.feedgenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by vamshikirangullapelly on 08/07/2020.
 */
public class ArtisanURLReader {

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
        //  Document doc = null;
        String productDesc = "";
        // try {
        //    doc = Jsoup.connect(url).get();
        Elements links = doc.select("div");
        //System.out.println("Product Link *** "+links.get(81).getElementsByAttribute("href").attr("href").toString());
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

        // productDesc =   links.get(174).ownText();
        //  } catch (IOException e) {
        //       e.printStackTrace();
        //   }
        // System.out.println(productDesc);
        return productDesc;
    }

    public static String findProductTitle(Document doc) {
        //  Document doc = null;
        String productTitle = "";
        // try {
        //     doc = Jsoup.connect(url).get();
        Elements links = doc.select("title");
        String titleString = links.toString();
        if (titleString.contains("Wholesale")) {
            productTitle = titleString.substring(7, titleString.indexOf("Wholesale"));
            //System.out.println(productTitle);
        }
        //  } catch (IOException e) {
        //     e.printStackTrace();
        //  }
        return productTitle;
    }

    public static String findMaterial(Document doc) {
        //  Document doc = null;
        String material = "";
        //   try {
        //      doc = Jsoup.connect(url).get();
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 1) {
            material = links.get(0).ownText();
        }
        //    } catch (IOException e) {
        //       e.printStackTrace();
        //   }
        return material;
    }

    public static String findBP1(Document doc) {
        //  Document doc = null;
        String bp1 = "";
        //    try {
        //        doc = Jsoup.connect(url).get();
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 2) {
            bp1 = links.get(1).ownText();
        }
        //   } catch (IOException e) {
        //      e.printStackTrace();
        //  }
        return bp1;
    }

    public static String findBP2(Document doc) {
        String bp2 = "";
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 3) {
            bp2 = links.get(2).ownText();
        }
        return bp2;
    }

    public static String findBP3(Document doc) {
        String bp3 = "";
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 4) {
            bp3 = links.get(3).ownText();
        }
        return bp3;
    }

    public static String findBP4(Document doc) {
        String bp4 = "";
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 5) {
            bp4 = links.get(4).ownText();
        }
        return bp4;
    }

    public static String findBP5(Document doc) {
        String bp5 = "";
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 6) {
            bp5 = links.get(5).ownText();
        }
        return bp5;
    }

    public static String findBP6(Document doc) {
        String bp6 = "";
        Elements links = doc.getElementsByClass("eael-feature-list-content");
        if (links != null && links.size() > 7) {
            bp6 = links.get(6).ownText();
        }
        return bp6;
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
        Elements links =  doc.getElementsByClass("ee-table__cell elementor-repeater-item-e85a72b");
        if (links != null) {
            height = links.text();
        }

        return height;
    }
    public static String findWidth(Document doc) {
        String width = "";
        Elements links =  doc.getElementsByClass("ee-table__cell elementor-repeater-item-3ca4416");
        if (links != null) {
            width = links.text();
        }

        return width;
    }
    public static String findDepth(Document doc) {
        String depth = "";
        Elements links =  doc.getElementsByClass("ee-table__cell elementor-repeater-item-4ae25f3");
        if (links != null) {
            depth = links.text();
        }

        return depth;
    }

    public static String findWeight(Document doc) {
        String weight = "";
        Elements links =  doc.getElementsByClass("ee-table__cell elementor-repeater-item-a15f3fd");
        if (links != null) {
            weight = links.text();
        }

        return weight;
    }

    public static String findPackagedWeight(Document doc) {
        String packWeight = "";
        Elements links =  doc.getElementsByClass("ee-table__cell elementor-repeater-item-91fa573");
        if (links != null) {
            packWeight = links.text();
        }

        return packWeight;
    }
    public static String findImageURL(Document doc, int number) {
        String imageURL = "";
        Elements links =  doc.getElementsByClass("woocommerce-product-gallery__image");

        if (links != null && links.size() > number) {
            if (links.get(number).child(0) != null) {
                imageURL = links.get(number).child(0).absUrl("href");
            }
        }
        return imageURL;
    }
}
