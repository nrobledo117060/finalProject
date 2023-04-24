package projectPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Server {
	
	protected static int portNumber = 7777;
	private static MongoClient mongo;
	private static MongoDatabase database;
	private static MongoCollection<Document> collection;
	
	private static final String URI = "mongodb+srv://nrobledo:2dumb2live@cluster0.od1dgod.mongodb.net/?retryWrites=true&w=majority";
	private static final String DB = "auction";
	private static final String COLLECTION = "items";
	
	private static ArrayList<Item> auctionItems;
	private static Set<ClientHandler> clients;

	
	public static void main(String[] args) {
		new Server().runServer();
	}
	
	private void runServer() {
		try {
		setUpNetworking();
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
/*	
		
	*/
	
	private void setUpNetworking() throws Exception {
	    @SuppressWarnings("resource")
	    ServerSocket serverSock = new ServerSocket(portNumber);
	    mongo = MongoClients.create(URI);
		database = mongo.getDatabase(DB);
		collection = database.getCollection(COLLECTION);
		getServerData();
		clients = new HashSet<ClientHandler>();
	    while (true) {
	      Socket clientSocket = serverSock.accept();
	      System.out.println("Connecting to... " + clientSocket);
	      ClientHandler handler = new ClientHandler(this, clientSocket);
	      clients.add(handler);
	      Thread t = new Thread(handler);
	      t.start();
	    }
	  }
	
	
	
	
	public void getServerData() {
		MongoCursor<Document> cursor = collection.find(
			Filters.empty()
		).cursor();
		ArrayList<String> items = new ArrayList<String>();
		while(cursor.hasNext()) {
			items.add(cursor.next().toJson());
		}
		int startOfString;
		int endOfString;
		String newString;
		
		String name, description, winner;
		String finalPrice;
		ObjectId itemId;
		
		ArrayList<Item> itemArray = new ArrayList<Item>();
		
		for(String itemString: items) {
			
				startOfString = itemString.indexOf("\"name\"") + 9;
				endOfString = itemString.indexOf("\"", startOfString);
				newString = itemString.substring(startOfString, endOfString);
				
				name = newString;
				
				
				startOfString = itemString.indexOf("\"description\"") + 16;
				endOfString = itemString.indexOf("\"", startOfString);
				newString = itemString.substring(startOfString, endOfString);
				
				description = newString;
				
				
				startOfString = itemString.indexOf("\"winner\"") + 11;
				endOfString = itemString.indexOf("\"", startOfString);
				newString = itemString.substring(startOfString, endOfString);
				
				winner = newString;
			
				
				startOfString = itemString.indexOf("\"finalPrice\"") + 13;
				endOfString = itemString.indexOf("}", startOfString);
				newString = itemString.substring(startOfString, endOfString);
				
				finalPrice = newString;
								
				Item currentItem = new Item(name, description, winner, Double.parseDouble(finalPrice));
				
				startOfString = itemString.indexOf("oid") + 7;
				endOfString = itemString.indexOf("\"", startOfString);
				newString = itemString.substring(startOfString, endOfString);
				itemId = new ObjectId(newString);
				
				currentItem.setId(itemId);
				itemArray.add(currentItem);
		}
		
		auctionItems = itemArray;
	}
	
	protected static void updateInfo(double input){
		Item update = auctionItems.get(0);
		update.setFinalPrice(update.getFinalPrice() + input);
		auctionItems.set(0, update);
		for(ClientHandler currentClient: clients) {
			currentClient.updatePrice();
		}
		
	}
	
	public static ArrayList<String> getItemInfo(){
		ArrayList<String> fillerArray = new ArrayList<String>();
		for(Item currentItem: auctionItems) {
			fillerArray.add(currentItem.getName());
			fillerArray.add(currentItem.getDescription());
			fillerArray.add(currentItem.getWinner());
			fillerArray.add("" + currentItem.getFinalPrice());
		}
		return fillerArray;
		
	}
}
