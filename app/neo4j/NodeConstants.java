package neo4j;

import java.util.regex.Pattern;

public class NodeConstants {

	public final static String SOURCE = "SOURCE";
	public final static String DESTINATION = "DESTINATION";
	public static final String PAGE_INDEX = "PAGE_INDEX";
	
	public static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	public static final String URL = "URL";
	public static final String WORD = "WORD";
	public static final String INDEX = "INDEX";

}
