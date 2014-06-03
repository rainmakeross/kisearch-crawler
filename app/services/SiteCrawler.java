package services;

import java.util.List;

import crawler.CrawlControllerFactory;
import crawler.PageCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class SiteCrawler {

    public void crawlSite(String url) throws Exception {

        CrawlController controller = CrawlControllerFactory.createController();
        
        System.out.println("visiting url :" + url);
        controller.addSeed(url);
        
        try {
            controller.start(PageCrawler.class, CrawlControllerFactory.NB_CRAWLERS);
        } finally {
            controller.shutdown();
        }
    }
    
    public void crawlSites(List<String> urls) throws Exception {

        CrawlController controller = CrawlControllerFactory.createController();
        
        for (String url : urls) {
            System.out.println("visiting url :" + url);
            controller.addSeed(url);
        }
        
        try {
            controller.start(PageCrawler.class, CrawlControllerFactory.NB_CRAWLERS);
        } finally {
            controller.shutdown();
        }
    }
}
