package Crawler.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class RecInfCrawler {

    private HashSet<String> links;

    public RecInfCrawler() {
        links = new HashSet<String>();
    }

    public void getPages(String URL) throws Exception {
        //4. Check if you have already crawled the URLs 
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {

                if (links.add(URL)) {
                    System.out.println(URL);
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements elementos = document.select("a[href]");
                Element lenguaje = document.select("html[lang]").first();
                Element titulo = document.select("title").first();

                for(Element el : elementos) {
                    if(lenguaje.attributes().get("lang").equals("es") && !el.attributes().get("href").contains("?") &&
                     !el.attributes().get("href").contains(":") && !el.attributes().get("href").contains("#") && el.attributes().get("href").contains("wiki")) {

                        File f = new File("./././HTML/" + titulo.text() + ".html");
                        FileWriter writer = new FileWriter(f);
                        writer.write(document.outerHtml());
                        writer.close();

                        getPages(el.attr("abs:href"));

                    }
                }

            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //1. Pick a URL from the frontier
        new RecInfCrawler().getPages("https://es.wikipedia.org/wiki/Wikipedia:Portada");
    }

}
