package controllers;

import java.io.IOException;
import java.util.List;

import models.SeedSite;
import play.mvc.Controller;
import play.mvc.Result;
import services.PageDownloader;
import services.PageVisitor;
import services.SiteVisitor;
import views.html.index;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Created by kong on 2014-06-01.
 */
public class SiteCrawler extends Controller {

    public static Result crawlOne() throws IOException {
        StringBuilder result = new StringBuilder("start crawling one site");

        String url = "http://www.ics.uci.edu/";
//        crawl(url);

        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        new SiteVisitor().visitSite(webUrl);

        return ok(index.render(result.toString() + " done"));

    }

    public static Result crawl() throws IOException {
        StringBuilder result = new StringBuilder("start crawl ");

        List<SeedSite> sites = SeedSite.all();
        for (SeedSite site : sites) {
            String url = "http://www." + site.uri + "/";
            System.out.println("visiting url :" + url);
            result.append("visiting url :" + url + " ");

//            crawl(url);
            
            WebURL webUrl = new WebURL();
            webUrl.setURL(url);
            new SiteVisitor().visitSite(webUrl);

        }
        
        return ok(index.render(result.toString() + " done"));
    }

    // this is an alternative to crawl to download and then visit a page
    // to be deleted
    private static void crawl(String url) throws IOException {
        System.out.println("visiting url :" + url);

        try {
            Page page = new PageDownloader().download(url);

            if (page != null) {
                new PageVisitor(url).visit(page);
            } else {
                System.out.println("Couldn't fetch the content of the page.");
            }
        } catch (Exception ex) {
            System.out.println("exception caught : " + ex.getStackTrace()
                    + " \n");
        }

    }

}
