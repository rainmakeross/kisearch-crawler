package models;

import net.vz.mongodb.jackson.JacksonDBCollection;
import play.modules.mongodb.jackson.MongoDB;

import java.util.List;

/**
 * Created by kong on 2014-06-01.
 */
public abstract class MongoModel {

    public String id;

    public JacksonDBCollection<MongoModel, String> dataCollection;

    protected MongoModel(String index, Class modelClass) {
        dataCollection = MongoDB.getCollection(index, modelClass, String.class);
    }

    public List<? extends MongoModel> all() {
        return dataCollection.find().toArray();
    }

    public void save() {
        dataCollection.save(this);
    }

    public void delete() {
        MongoModel model = dataCollection.findOneById(this.id);
        if (model != null)
            dataCollection.remove(model);
    }

    public void removeAll(){
        dataCollection.drop();
    }

}
