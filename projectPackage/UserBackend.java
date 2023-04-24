package projectPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;

public class UserBackend {
	
	
	Socket socket = null;
	ArrayList<String> data = new ArrayList<String>();
	Thread changeHandler = new Thread() {
		@Override
		public void run() {
			try {
				DataInputStream input = new DataInputStream(socket.getInputStream());
				while(true) {
					if(input.available() > 0) {
						String changedData = input.readUTF();
						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								userInterface.updatePrice(changedData);
							}
						});
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
	
	public void connectUser() {
		
		try {
			socket = new Socket("localhost", 7777);
			
		        // create a data output stream from the output stream so we can send data through it
		    
		    System.out.println("Connected!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

        // get the output stream from the socket.
       
	}
	
	
	public List<String> getData(){
		
		try {
			
			InputStream input = socket.getInputStream();
			DataInputStream dataInput = new DataInputStream(input);
	        System.out.println("Recieving string from the ServerSocket");
	        
	        // write the message we want to send
	       
	        data.add(dataInput.readUTF());
	        
	        while(input.available() > 0) {
	        	data.add(dataInput.readUTF());
	        }
	       
	        
	         // close the output stream when we're done.

	        System.out.println("Closing socket and terminating program.");
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		changeHandler.setDaemon(true);
		changeHandler.start();
		return data;
	}
	
	public void updateBid() {
		try {
			
			OutputStream output = socket.getOutputStream();
			DataOutputStream dataOutput = new DataOutputStream(output);
			dataOutput.writeDouble(1.00);
			dataOutput.flush();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
