package controllers;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class SiteCrawlerTest {

	private SiteCrawler siteCrawler;
	
	@Before
	public void before() throws Exception {
		siteCrawler = new SiteCrawler();
	}
	
	@Test
	public void testCrawl() throws IOException {
		
    	String url = "http://www.ics.uci.edu/";

    	siteCrawler.visit(url);
	}
}
