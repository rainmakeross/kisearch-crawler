package models;

import java.util.List;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

/**
 * Created by kong on 2014-06-01.
 */
public class SiteContent {

    @Id
    @ObjectId
    public String id;

    private String url;
    private String title;
    private String content;
    private String parentUrl;
    private List<String> links;
    
    public static JacksonDBCollection<SiteContent, String> dataCollection = MongoDB.getCollection("sitecontent", SiteContent.class, String.class);

    public SiteContent() {
    }
    
    public SiteContent(String url, String title, String content, String parentUrl, List<String> links) {
        this.setUrl(url);
        this.setTitle(title);
        this.setContent(content);
        this.setParent(parentUrl);
        this.setLinks(links);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParent(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    
    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public static List<SiteContent> all() {
        return dataCollection.find().toArray();
    }

    public static void save(SiteContent content) {
        dataCollection.save(content);
    }

    public static void delete(String id ) {
        SiteContent model = dataCollection.findOneById(id);
        if (model != null)
            dataCollection.remove(model);
    }

    public static void removeAll(){
        dataCollection.drop();
    }
}
