package controllers;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class SiteCrawlerTest {

	@Before
	public void before() throws Exception {
	}
	
	@Test
	public void testCrawl() throws IOException {
	    SitesCrawler.crawlOne();
	}
}
