package services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import models.SiteContent;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Created by kong on 2014-06-01.
 */
public class SiteVisitor {

    private final static Pattern SITE_URL_FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    
    private String rootUrl;
    
    public SiteVisitor(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !SITE_URL_FILTERS.matcher(href).matches() && href.startsWith(rootUrl);
    }

    public SiteContent visit(Page page) {
    	SiteContent siteContent = null;
    	
        WebURL webUrl = page.getWebURL();
        String url = webUrl.getURL();
        System.out.println("Visiting: " + url);

        if (!shouldVisit(webUrl))  {
            System.out.println("This url should not be visited : " + url);
            return siteContent;
        }

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
