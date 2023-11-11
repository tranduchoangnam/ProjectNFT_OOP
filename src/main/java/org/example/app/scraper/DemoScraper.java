package org.example.app.scraper;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

public class DemoScraper {
    private final String URL = "https://boo.vn/looney-tunes";
    private JSONArray danhSachSanPham;

    public DemoScraper() {
        this.danhSachSanPham = new JSONArray();
        scrap();
    }

    public JSONArray getDanhSachSanPham() {
        return this.danhSachSanPham;
    }

    private JSONObject scrapInSubUrl(String subUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            Document temp = Jsoup.connect(subUrl).get();

            String name = temp.select("span[itemprop=name]").text();
            String sku = temp.select("div[itemprop=sku]").text();
            Number finalPrice = Integer
                    .parseInt(temp.select("span[data-price-type=finalPrice]").attr("data-price-amount"));
            Number oldPrice = Integer
                    .parseInt(temp.select("span[data-price-type=oldPrice]").attr("data-price-amount"));
            String discount = temp.select("span.discount_percent").first().text();

            Elements swatchOptions = temp.select("script[type=text/x-magento-init]");

            ArrayList<String> images = new ArrayList<>();
            for (Element swatchOption : swatchOptions) {
                if (swatchOption.html().contains("data-gallery-role=gallery-placeholder")) {
                    JSONObject json = new JSONObject(swatchOption.html())
                            .getJSONObject("[data-gallery-role=gallery-placeholder]");
                    try {
                        JSONArray data = new JSONArray(json
                                .getJSONObject("mage/gallery/gallery").getJSONArray("data"));
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String img = item.getString("img");
                            images.add(img);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        continue;
                    }
                }
            }
            swatchOptions = temp.select("div.swatch-option.text");
            List<String> sizes = new ArrayList<>();
            for (Element swatchOption : swatchOptions) {
                String size = swatchOption.attr("data-option-tooltip-value");
                sizes.add(size);
            }
            String description = temp.select("div[itemprop=description]").text();
            jsonObject.put("name", name);
            jsonObject.put("sku", sku);
            jsonObject.put("price", finalPrice);
            jsonObject.put("old_price", oldPrice);
            jsonObject.put("discount", discount);
            jsonObject.put("image", images);
            // jsonObject.put("size", sizes);
            jsonObject.put("description", description);
            return jsonObject;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private void scrap() {
        print("Fetching %s...", URL);
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements pages = doc.select("div.product-item-info");
            print("\nproduct: (%d)", pages.size());
            for (Element page : pages) {
                String href = page.select("a[href]").attr("href");
                print("Fetching %s...", href);
                print("%s", "end");
                this.danhSachSanPham.put(scrapInSubUrl(href));

            }
            System.out.println(this.danhSachSanPham.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
}
// demo crawl map location

// jsonObject.put("map", hrefs.get(1).attr("href"));

// String locationName = product.select(".location-name").text();
// jsonObject.put("name", locationName);

// // Extract and print the location address
// String locationAddress = product.select(".location-address").text();
// jsonObject.put("address", locationAddress);

// // Extract and print the location hours open
// String locationHoursOpen = product.select(".location-hours-open").text();
// jsonObject.put("hoursOpen", locationHoursOpen);

// Elements hrefs = product.select("a[href]");
// jsonObject.put("tel", hrefs.get(0).text());
// jsonObject.put("map", hrefs.get(1).attr("href"));
// System.out.println(jsonObject.toString() + ',');