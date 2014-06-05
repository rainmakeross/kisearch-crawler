package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.SeedSite;
import play.mvc.Controller;
import play.mvc.Result;
import services.SiteCrawler;
import services.SiteVisitor;
import views.html.index;
import crawler.Neo4JWebCrawler;
import crawler.PageCrawler;

/**
 * Created by kong on 2014-06-01.
 */
public class SitesCrawler extends Controller {


    public static Result crawlOne() throws IOException {
    	Date startTime = new Date();
    	System.out.println("Start: " + startTime);
        
    	StringBuilder result = new StringBuilder("start crawling one site");

        String url = "www.tourisme-montreal.org/";

        new SiteVisitor().visitSite(url);

    	Date endTime = new Date();
    	long duration = (endTime.getTime() - startTime.getTime())/1000;
        System.out.println("End: " + endTime);
        System.out.println("Duration: " + duration );

        return ok(index.render(result.toString() + " done within " + duration + "seconds"));
    }
    
    public static Result crawlOnes() throws IOException {
    	Date startTime = new Date();
    	System.out.println("Start: " + startTime);

        StringBuilder result = new StringBuilder("start crawling one site");
        
        //String url = "www.tourisme-montreal.org";
        List<SeedSite> sites = SeedSite.all();
        
        List<String> urls = convertToStringList(sites);

        new SiteVisitor().visitSite(urls);
        
    	Date endTime = new Date();
    	long duration = (endTime.getTime() - startTime.getTime())/1000;
        System.out.println("End: " + endTime);
        System.out.println("Duration: " + duration );

        return ok(index.render(result.toString() + " done"));

    }
    
    public static Result crawlTwo() throws Exception {
    	Date startTime = new Date();
    	System.out.println("Start: " + startTime);

        StringBuilder result = new StringBuilder("start crawling one site");

        String url = "www.tourisme-montreal.org/";

        new SiteCrawler(PageCrawler.class).crawlSite(url); 

    	Date endTime = new Date();
    	long duration = (endTime.getTime() - startTime.getTime())/1000;
        System.out.println("End: " + endTime);
        System.out.println("Duration: " + duration );

        return ok(index.render(result.toString() + " done within " + duration + "seconds"));
    }

    public static Result crawlTwos() throws Exception {
    	Date startTime = new Date();
    	System.out.println("Start: " + startTime);

        StringBuilder result = new StringBuilder("start crawl ");

        List<SeedSite> sites = SeedSite.all();
        
        List<String> urls = convertToStringList(sites);
        
        new SiteCrawler(PageCrawler.class).crawlSites(urls); 
        
    	Date endTime = new Date();
    	long duration = (endTime.getTime() - startTime.getTime())/1000;
        System.out.println("End: " + endTime);
        System.out.println("Duration: " + duration );

        return ok(index.render(result.toString() + " done"));
    }
    
    public static Result crawlNeo() throws Exception {
    	Date startTime = new Date();
    	System.out.println("Start: " + startTime);

        StringBuilder result = new StringBuilder("start crawling one site");

        String url = "www.tourisme-montreal.org/";

        new SiteCrawler(Neo4JWebCrawler.class).crawlSite(url); 

    	Date endTime = new Date();
    	long duration = (endTime.getTime() - startTime.getTime())/1000;
        System.out.println("End: " + endTime);
        System.out.println("Duration: " + duration );

        return ok(index.render(result.toString() + " done within " + duration + "seconds"));

    }
    
    public static Result crawlNeos() throws Exception {
    	Date startTime = new Date();
    	System.out.println("Start: " + startTime);

        StringBuilder result = new StringBuilder("start crawling one site");

        List<SeedSite> sites = SeedSite.all();
        
        List<String> urls = convertToStringList(sites);
        
        new SiteCrawler(Neo4JWebCrawler.class).crawlSites(urls); 

    	Date endTime = new Date();
    	long duration = (endTime.getTime() - startTime.getTime())/1000;
        System.out.println("End: " + endTime);
        System.out.println("Duration: " + duration );

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
