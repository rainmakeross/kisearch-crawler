package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import models.SiteContent;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;

public class WebCrawler {

    private final static Pattern SITE_URL_FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private String rootUrl;
    
    private Parser parser;
    private PageFetcher pageFetcher;
    
	private Map<WebURL, Boolean> urlSet = new HashMap<WebURL, Boolean>();
    
	
    public WebCrawler(String rootUrl) {
        this.rootUrl = rootUrl;
        
        CrawlConfig config = new CrawlConfig();
        parser = new Parser(config);
        pageFetcher = new PageFetcher(config);

	}

    private boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !SITE_URL_FILTERS.matcher(href).matches() && href.startsWith(rootUrl);
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
				else if (urlSet.containsKey(movedToUrl)) {
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
    
    public void visitSite(WebURL webUrl) {
    	
        if (!shouldVisit(webUrl))  {
            System.out.println("This url should not be visited : " + webUrl.getURL());
            return;
        }

        WebURL movedToWebUrl = getMovedSite(webUrl);
		if (movedToWebUrl != null) {
			webUrl = movedToWebUrl;
			urlSet.put(movedToWebUrl, true);
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
	
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

			for (WebURL outgoingUrl : htmlParseData.getOutgoingUrls()) {
				urlSet.put(outgoingUrl, true);
			}
        }
        
        visit(page);
        visiteSite();
    }

    private void visiteSite() {
		
		for (WebURL webUrl : urlSet.keySet()) {
	        Page page = new Page(webUrl);
	        visit(page);
		}
	}

	private SiteContent visit(Page page) {

		SiteContent siteContent = null;
    	
        WebURL webUrl = page.getWebURL();
        String url = webUrl.getURL();
        System.out.println("Visiting: " + url);
        
        if (urlSet.get(webUrl)) {
			urlSet.put(webUrl, false);

	        if (page.getParseData() instanceof HtmlParseData) {
	            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	            String siteTitle = htmlParseData.getTitle();
	            String siteText = htmlParseData.getText();
	            String siteHtml = htmlParseData.getHtml();
	            List<String> links = convertToStringList(htmlParseData.getOutgoingUrls());
	
	            System.out.println("Title : " + siteTitle);
	            System.out.println("Text length: " + siteText.length());
	            System.out.println("Html length: " + siteHtml.length());
	            System.out.println("Number of outgoing links: " + links.size());
	
	            siteContent = new SiteContent(url, siteTitle, siteHtml, webUrl.getParentUrl(), links);
	        }
        }
        
        return siteContent;
    }

    private List<String> convertToStringList(List<WebURL> outgoingUrls) {
        List<String> links = new ArrayList<>();
        for (WebURL webUrl : outgoingUrls) {
            links.add(webUrl.getURL());
        }
        return links;
    }
}
