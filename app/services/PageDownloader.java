package services;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;

public class PageDownloader {

    public Page download(String url) {
        System.out.println("Downloading: " + url);

        String canonicalUrl = URLCanonicalizer.getCanonicalURL(url);
        System.out
                .println("downloading canonicalUrl : " + canonicalUrl + " \n");

        WebURL curURL = new WebURL();
        curURL.setURL(canonicalUrl);
        PageFetchResult fetchResult = null;
        try {
            CrawlConfig config = new CrawlConfig();

            Parser parser = new Parser(config);
            PageFetcher pageFetcher = new PageFetcher(config);

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
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
        System.out.println("Downloaded failed: " + url);

        return null;
    }
}
