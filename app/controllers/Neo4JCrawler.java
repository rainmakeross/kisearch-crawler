package controllers;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.rest.graphdb.RestGraphDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by derya on 03/06/14.
 */
public class Neo4JCrawler extends WebCrawler {

    //private RestAPI graphDb;
    private final GraphDatabaseService graphDb;

    public Neo4JCrawler(){

        this.graphDb = new RestGraphDatabase("app25869497.sb02.stations.graphenedb.com:24789", "app25869497", "RQMsA1NDgfePPrzZXPc7");
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(final Page page) {

        final String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        final Index<Node> nodeIndex = graphDb.index().forNodes("PAGE_INDEX");

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            //String html = htmlParseData.getHtml();
            List<WebURL> links = htmlParseData.getOutgoingUrls();
            Transaction tx = graphDb.beginTx();
            try {

                final Node pageNode = graphDb.createNode();
                pageNode.setProperty("URL", url);
                nodeIndex.add(pageNode, "URL", url);

                //get all the words
                final List<String> words = cleanAndSplitString(text);
                int index = 0;
                for (final String word : words) {
                    final Node wordNode = graphDb.createNode();
                    wordNode.setProperty("WORD", word);
                    wordNode.setProperty("INDEX", index++);
                    RelationshipType relationShipType = DynamicRelationshipType.withName("CONTAINS");
                    final Relationship relationship = pageNode.createRelationshipTo(wordNode, relationShipType);
                    relationship.setProperty("SOURCE", url);
                }

                for (final WebURL webURL : links) {
                    System.out.println("Linking to " + webURL);
                    final Node linkNode = graphDb.createNode();
                    linkNode.setProperty("URL", webURL.getURL());
                    RelationshipType relationShipType = DynamicRelationshipType.withName("LINK_TO");
                    final Relationship relationship = pageNode.createRelationshipTo(linkNode, relationShipType);
                    relationship.setProperty("SOURCE", url);
                    relationship.setProperty("DESTINATION", webURL.getURL());
                }

                tx.success();
            } finally {
                tx.finish();
            }

        }
    }

    private static List<String> cleanAndSplitString(final String input) {
        if (input != null) {
            final String[] dic = input.toLowerCase().replaceAll("\\p{Punct}", "").replaceAll("\\p{Digit}", "").split("\\s+");
            return Arrays.asList(dic);
        }
        return new ArrayList<>();
    }




}
