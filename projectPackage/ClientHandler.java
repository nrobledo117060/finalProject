package projectPackage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class ClientHandler  implements Runnable{
	
	
		private static MongoClient mongo;
		private static MongoDatabase database;
		private static MongoCollection<Document> collection;
		
		private static final String URI = "mongodb+srv://nrobledo:2dumb2live@cluster0.od1dgod.mongodb.net/?retryWrites=true&w=majority";
		private static final String DB = "auction";
		private static final String COLLECTION = "items";
	
		private Server mainServer;
		private Socket clientSocket;
		private DataInputStream inputStream;
		private DataOutputStream outputStream;
		
		protected ClientHandler(Server mainServer, Socket clientSocket) {
			this.mainServer = mainServer;
			this.clientSocket = clientSocket;
			try {
				inputStream = new DataInputStream(clientSocket.getInputStream());
				outputStream = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mongo = MongoClients.create(URI);
			database = mongo.getDatabase(DB);
			collection = database.getCollection(COLLECTION);
		}
		
		@Override
		public void run() {
			
			Scanner auctionData = null;
			ArrayList<String> items = Server.getItemInfo();
			
			try {
				
				System.out.println("Server starting in port: 7777");
				System.out.println("Connected");
				sendData(items);
				
				while(true){
					if(inputStream.available() > 0) {	
						double updateNumber = inputStream.readDouble();
						Server.updateInfo(updateNumber);
					}
				}
				
				
			
		//		System.out.println("Server Closed");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		public void updatePrice() {
			ArrayList <String> items = Server.getItemInfo();
			try {
				outputStream.writeUTF("" + items.get(3));
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		public void sendData(ArrayList<String> items) {
			
			 for(String currentItem: items){
				try {
					outputStream.writeUTF(currentItem);
					outputStream.flush();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

		
	

}
