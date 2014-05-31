package controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import play.*;
import play.mvc.*;

import views.html.*;

import java.io.IOException;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }




}
