package services;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;

public class PageDownloader {

	private Parser parser;
	private PageFetcher pageFetcher;

	public PageDownloader() {
		CrawlConfig config = new CrawlConfig();
		parser = new Parser(config);
		pageFetcher = new PageFetcher(config);
	}

	public PageFetchResult fetchHeader(WebURL webUrl) {
		return pageFetcher.fetchHeader(webUrl);
	}

	public Page download(WebURL webUrl) {
		System.out.println("Downloading: " + webUrl.getURL());

		PageFetchResult fetchResult = null;
		try {
			fetchResult = pageFetcher.fetchHeader(webUrl);
			if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
				Page page = new Page(webUrl);
				fetchResult.fetchContent(page);
				if (parser.parse(page, webUrl.getURL())) {
					return page;
				}
			}
		} finally {
			if (fetchResult != null) {
				fetchResult.discardContentIfNotConsumed();
			}
		}
		System.out.println("Downloaded failed: " + webUrl.getURL());

		return null;
	}
}
