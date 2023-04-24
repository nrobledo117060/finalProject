package mongoDB;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoDbPojoExample {
	private static MongoClient mongo;
	private static MongoDatabase database;
	private static MongoCollection<Item> collection;
	
	private static final String URI = "mongodb+srv://nrobledo:2dumb2live@cluster0.od1dgod.mongodb.net/?retryWrites=true&w=majority";
	private static final String DB = "auction";
	private static final String COLLECTION = "items";

	public static void main(String[] args) {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
		
		mongo = MongoClients.create(URI);
		database = mongo.getDatabase(DB).withCodecRegistry(pojoCodecRegistry);
		collection = database.getCollection(COLLECTION, Item.class);
		
		//CREATE
		Item newItem = new Item("Adidas Ultraboost", "this one is also cool", "", 0.0);
		collection.insertOne(newItem);
		
		//FIND AND READ
		Item item = collection.find(Filters.eq("name", "Adidas Ultraboost")).first();
        System.out.println(item);
        
        //UPDATE
        item.setName("Adidas Megaboost");
        collection.findOneAndReplace(Filters.eq("name", "Adidas Ultraboost"), item);
        
        //READ ALL
        MongoCursor cursor = collection.find(Filters.empty()).cursor();
        while(cursor.hasNext()) {
        	System.out.println(((Item)cursor.next()).toString());
        }
        
        //DELETE
        Document filterByItemId = new Document("_id", item.getId());
        collection.deleteOne(filterByItemId);
		
		mongo.close();
	}
	
}
