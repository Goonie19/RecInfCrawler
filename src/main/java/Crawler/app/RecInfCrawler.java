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
import java.util.LinkedList;
import java.util.Queue;

public class RecInfCrawler {

    private final Queue<String> links;
    private final HashSet<String> linksVisitados = new HashSet<String>();

    public RecInfCrawler() {
        links = new LinkedList<>();
    }

    public void getPages(final String URL) throws Exception {
        links.add(URL);
       try {
        while(true) {

            final Document document = Jsoup.connect(links.peek()).get();
            final Elements elementos = document.select("a[href]");
            final Element lenguaje = document.select("html[lang]").first();
            final Element titulo = document.select("title").first();

            System.out.println(links.peek());
            for (final Element el : elementos) {
                if(lenguaje.attributes().get("lang").equals("es") && !el.attributes().get("href").contains("?") &&
                !el.attributes().get("href").contains(":") && !el.attributes().get("href").contains("#") &&
                 el.attributes().get("href").contains("wiki") && !linksVisitados.contains(el.attr("abs:href"))) {
                    links.add(el.attr("abs:href"));
                }
            }
               
            final File f = new File("./././HTML/" + titulo.text() + ".html");
            final FileWriter writer = new FileWriter(f);
            writer.write(document.outerHtml());
            writer.close();
            linksVisitados.add(links.poll());

        }
       } catch(final IOException e) {
           e.printStackTrace();
       }
    }

    public static void main(final String[] args) throws Exception {
        //1. Pick a URL from the frontier
        new RecInfCrawler().getPages("https://es.wikipedia.org/wiki/Wikipedia:Portada");
    }

}
