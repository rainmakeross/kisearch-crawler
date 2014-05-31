package models;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.util.List;

/**
 * Created by derya on 31/05/14.
 */
public class SeedSite {

    @Id
    @ObjectId
    public String id;

    public String uri;

    private static JacksonDBCollection<SeedSite, String> collection = MongoDB.getCollection("seedsites", SeedSite.class, String.class);

    public SeedSite(){

    }

    public SeedSite(String uri){
        this.uri = uri;
    }

    public static List<SeedSite> all(){
        return SeedSite.collection.find().toArray();
    }

    public static void create(SeedSite seedSite){
        SeedSite.collection.save(seedSite);
    }

    public static void create(String uri){
        create(new SeedSite(uri));
    }

    public static void delete(String id){
        SeedSite seedSite = SeedSite.collection.findOneById(id);
        if(seedSite != null){
            SeedSite.collection.remove(seedSite);
        }
    }

    public static void removeAll(){
        SeedSite.collection.drop();
    }
}
