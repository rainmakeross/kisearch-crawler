package services;

import java.util.List;

import crawler.CrawlControllerFactory;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;


/**
 * This SiteCrawler execute the injected WebCrawler implementation to crawl one or mutliple sites,
 * using the built-in mutli-threading of Craw4J's CrawController.
 * 
 * @author kong on 03/06/14.
 *
 */
public class SiteCrawler {
	
	private Class<? extends WebCrawler> clazz;
	
	public SiteCrawler(Class<? extends WebCrawler> clazz) {
		this.clazz = clazz;
	}

    public void crawlSite(String url) throws Exception {

        CrawlController controller = CrawlControllerFactory.createController();
        
        System.out.println("visiting url: " + url);
        controller.addSeed(url);
        
        try {
            controller.start(clazz, CrawlControllerFactory.NB_CRAWLERS);
        } finally {
            controller.shutdown();
        }
    }
    
    public void crawlSites(List<String> urls) throws Exception {

        CrawlController controller = CrawlControllerFactory.createController();
        
        for (String url : urls) {
            System.out.println("visiting url: " + url);
            controller.addSeed(url);
        }
        
        try {
            controller.start(clazz, CrawlControllerFactory.NB_CRAWLERS);
        } finally {
            controller.shutdown();
        }
    }
}
