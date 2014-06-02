package controllers;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;
import models.SeedSite;
import models.SiteContent;

import org.apache.http.HttpStatus;

import play.mvc.Controller;
import play.mvc.Result;
import services.SiteVisitor;
import services.WebCrawler;
import views.html.index;

import java.io.IOException;
import java.util.List;

/**
 * Created by kong on 2014-06-01.
 */
public class SiteCrawler extends Controller {

    private static final int NB_CRAWLERS  = 1;

    private static Parser parser;
    private static PageFetcher pageFetcher;
    
    public static Result crawl() throws IOException {
        StringBuilder result =  new StringBuilder("start crawling ");
        
//        List<SeedSite> sites = new SeedSite().all();
//        for (SeedSite site : sites) {
//        	String url = "http://www." + site.uri + "/";
//            result.append("visiting url :" + url + " ");
//
//        	visit(url);
//        }

        String url = "http://www.ics.uci.edu/";
        visit(url);
        
        return ok(index.render(result.toString() + " done"));

    }
    
    public static void visit(String url) throws IOException {
    	System.out.println("visiting url :" + url);
        
        try {
        	Page page = download(url);

            if (page != null) {
//                new SiteVisitor().visit(page);
				WebURL webUrl = new WebURL();
				webUrl.setURL(url);

                new WebCrawler().visitSite(webUrl);
            } else {
                System.out.println("Couldn't fetch the content of the page.");
            }
        } catch (Exception ex) {
        	System.out.println("exception caught : " + ex.getStackTrace() + " \n");
        }

    }

    private static Page download(String url) {
        System.out.println("Downloading: " + url);
        
    	String canonicalUrl = URLCanonicalizer.getCanonicalURL(url);
        System.out.println("downloading canonicalUrl : " + canonicalUrl + " \n");

        WebURL curURL = new WebURL();
        curURL.setURL(canonicalUrl);
        PageFetchResult fetchResult = null;
        try {
            CrawlConfig config = new CrawlConfig();

            parser = new Parser(config);
            pageFetcher = new PageFetcher(config);

            fetchResult = pageFetcher.fetchHeader(curURL);
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
                try {
                    Page page = new Page(curURL);
                    fetchResult.fetchContent(page);
                    if (parser.parse(page, curURL.getURL())) {
                        return page;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Downloaded failed: " + url);
                }
            }
        } finally {
            if (fetchResult != null)
            {
                fetchResult.discardContentIfNotConsumed();
            }
        }
        System.out.println("Downloaded failed: " + url);

        return null;
    }
}
