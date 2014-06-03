package services;

import java.util.HashMap;
import java.util.Map;

import models.SiteContent;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;

public class SiteVisitor {

    private Parser parser;
    private PageFetcher pageFetcher;
    
    private PageVisitor pageVisitor;
    
	private Map<String, Boolean> visitedUrls = new HashMap<String, Boolean>();
    
    public SiteVisitor() {
        CrawlConfig config = new CrawlConfig();
        parser = new Parser(config);
        pageFetcher = new PageFetcher(config);
	}

    public void visitSite(WebURL webUrl) {
        
        WebURL movedToWebUrl = getMovedSite(webUrl);
		if (movedToWebUrl != null) {
			webUrl = movedToWebUrl;
			visitedUrls.put(movedToWebUrl.getURL(), true);
		}

        Page page = new Page(webUrl);
        PageFetchResult fetchResult = pageFetcher.fetchHeader(webUrl);

        //fetch the content to the page
		if (!fetchResult.fetchContent(page)) {
			return;
		}
	
		if (!parser.parse(page, webUrl.getURL())) {
			return;
		}

		doVisit(page);
    }

    private WebURL getMovedSite(WebURL webUrl) {

        PageFetchResult fetchResult = pageFetcher.fetchHeader(webUrl);
		int statusCode = fetchResult.getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				String movedToUrl = fetchResult.getMovedToUrl();
				if (movedToUrl == null) {
					return null;
				}
				//already in the list
				else if (visitedUrls.containsKey(movedToUrl)) {
					return null;
				} else {

					WebURL movedToWebUrl = new WebURL();
					movedToWebUrl.setURL(movedToUrl);
					movedToWebUrl.setParentUrl(webUrl.getParentUrl());
					movedToWebUrl.setDepth(webUrl.getDepth());
					return movedToWebUrl;
				}
			}
		}
		return null;
    }
    
    private void doVisit(Page page) {
    	
    	WebURL webUrl = page.getWebURL();
    	pageVisitor = new PageVisitor(webUrl.getURL());

        visit(page);
        visiteLinks();
    }

    private void visiteLinks() {
		
		for (String url : visitedUrls.keySet()) {
			WebURL webUrl = new WebURL();
			webUrl.setURL(url);
	        Page page = new Page(webUrl);
	        
	        visit(page);
		}
	}

	private SiteContent visit(Page page) {

		SiteContent siteContent = null;
    	
        WebURL webUrl = page.getWebURL();
        System.out.println("Visiting: " + webUrl.getURL());

        siteContent = pageVisitor.visit(page);

        if (siteContent != null) {

    		for (String outgoingUrl : siteContent.getLinks()) {
    			if (!visitedUrls.containsKey(outgoingUrl)) {
    				visitedUrls.put(outgoingUrl, true);
    			}
    		}

            SiteContent.save(siteContent);
        }
        
        if (visitedUrls.containsKey(webUrl.getURL())) {
			visitedUrls.put(webUrl.getURL(), false);
        }
        
        return siteContent;
    }
}
