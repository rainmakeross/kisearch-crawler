package models;

import edu.uci.ics.crawler4j.url.WebURL;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.db.ebean.Model;
import play.modules.mongodb.jackson.MongoDB;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by kong on 2014-06-01.
 */
public class SiteContent extends MongoModel {

    @Id
    @ObjectId
    public String id;

    private String url;
    private String title;
    private String content;
    List<WebURL> links;
    private SiteContent parent;
    private List<SiteContent> children;

    public SiteContent() {
        super("sitecontent", SiteContent.class);
    }

    public SiteContent(String url, String title, String content, List<WebURL> links) {
        super("sitecontent", SiteContent.class);
        this.url = url;
        this.title = title;
        this.content = content;
        this.links = links;
    }

    public SiteContent getParent() {
        return parent;
    }

    public void setParent(SiteContent parent) {
        this.parent = parent;
    }

    public List<SiteContent> getChildren() {
        return children;
    }

    public void setChildren(List<SiteContent> children) {
        this.children = children;
    }
}
