package app;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {

        String url = "https://boo.vn/looney-tunes";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements pages = doc.select("div.product-item-info");
        JSONArray products = new JSONArray();

        print("\nproduct: (%d)", pages.size());
        for (Element page : pages) {
            JSONObject jsonObject = new JSONObject();
            String href = page.select("a[href]").attr("href");

            print("Fetching %s...", href);
            print("%s", "end");
            try {
                Document temp = Jsoup.connect(href).get();

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
                jsonObject.put("size", sizes);
                jsonObject.put("description", description);
                products.put(jsonObject);
            } catch (Exception e) {
                System.out.println(e);
                continue;
            }

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

        }
        System.out.println(products.toString());

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

}
