package Crawler.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class RecInfCrawler {

    private ArrayList<String> links;
    private HashSet<String> linksVisitados = new HashSet<String>();

    public RecInfCrawler() {
        links = new ArrayList<String>();
    }

    public void getPages(String URL) throws Exception {
        links.add(URL);
        int i = 0;
       try {
        while(true) {

            Document document = Jsoup.connect(links.get(i)).get();
            Elements elementos = document.select("a[href]");
            Element lenguaje = document.select("html[lang]").first();
            Element titulo = document.select("title").first();

            System.out.println(links.get(links.size() -1));
            for (Element el : elementos) {

                links.add(links.size() -1 , el.attr("abs:href"));
            }
               
            if(lenguaje.attributes().get("lang").equals("es") && !links.get(i).contains("?") 
            && !links.get(i).contains(":") && !links.get(i).contains("#") && links.get(i).contains("wiki")) {
                File f = new File("./././HTML/" + titulo.text() + ".html");
                FileWriter writer = new FileWriter(f);
                writer.write(document.outerHtml());
                writer.close();
            }
            ++i;
        }
       } catch(IOException e) {
           e.printStackTrace();
       }
    }

    public static void main(String[] args) throws Exception {
        //1. Pick a URL from the frontier
        new RecInfCrawler().getPages("https://es.wikipedia.org/wiki/Wikipedia:Portada");
    }

}
