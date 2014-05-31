package controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.IOException;

/**
 * Created by derya on 31/05/14.
 */
public class SeedSites extends Controller {

    public static Result scrape() throws IOException {
        final WebClient webClient = new WebClient();
        final HtmlPage page = (HtmlPage) webClient.getPage("http://www.alexa.com/topsites/countries/SG");

        return ok(index.render(page.getElementsByIdAndOrName("site-listing").toString()));
    }
}
