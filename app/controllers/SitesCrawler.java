package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.SeedSite;
import play.mvc.Controller;
import play.mvc.Result;
import services.SiteCrawler;
import services.SiteVisitor;
import views.html.index;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Created by kong on 2014-06-01.
 */
public class SitesCrawler extends Controller {


    public static Result crawlOne() throws IOException {
        StringBuilder result = new StringBuilder("start crawling one site");

        String url = "";

        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        new SiteVisitor().visitSite(webUrl);

        return ok(index.render(result.toString() + " done"));

    }
    
    public static Result crawlTwo() throws Exception {
        StringBuilder result = new StringBuilder("start crawling one site");

        String url = "";

        new SiteCrawler().crawlSite(url); 

        return ok(index.render(result.toString() + " done"));

    }

    public static Result crawl() throws Exception {
        StringBuilder result = new StringBuilder("start crawl ");

        List<SeedSite> sites = SeedSite.all();
        
        List<String> urls = convertToStringList(sites);
        
        new SiteCrawler().crawlSites(urls); 
        
        return ok(index.render(result.toString() + " done"));
    }
    
    private static List<String> convertToStringList(List<SeedSite> sites) {
        List<String> urls = new ArrayList<>();
        for (SeedSite site : sites) {
            String url = "http://" + site.uri + "/";
            urls.add(url);
        }
        return urls;
    }

}
