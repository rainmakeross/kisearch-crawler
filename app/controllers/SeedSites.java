package controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.SeedSite;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by derya on 31/05/14.
 */
public class SeedSites extends Controller {

    public static Result scrape() throws IOException {
        final WebClient webClient = new WebClient();
        List<String> UrlSeed = new ArrayList<String>();
        SeedSite.removeAll();
        for(int i=0; i<20;i++){
            String Uri = "http://www.alexa.com/topsites/countries;"+i+"/SG";
            final HtmlPage page = webClient.getPage(Uri);
            Iterator<HtmlAnchor> allAnchors = page.getAnchors().iterator();
            while(allAnchors.hasNext()){
                HtmlAnchor anchor = allAnchors.next();
                if(anchor.asText().contains(".sg")){
                    UrlSeed.add(anchor.asText());
                    SeedSite seedSite = new SeedSite(anchor.asText());
                    SeedSite.create(seedSite);
                }

            }
        }

        System.out.println(UrlSeed.toString());
        return ok(index.render("done"));
    }
}
