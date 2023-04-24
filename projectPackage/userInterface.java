package projectPackage;



import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class userInterface extends Application {
	
	static Label highestBidder;
	
	@Override
	public void start(Stage primaryStage) {
        primaryStage.setTitle("User Interface");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        
        
        Text scenetitle = new Text("Welcome to the Auction");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        
        Button signBtn = new Button("Sign in");
        Button guestBtn = new Button("Guest");
        Button quitBtn = new Button("Quit");
        HBox hbBtns = new HBox(10);
        hbBtns.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtns.getChildren().add(quitBtn);
        hbBtns.getChildren().add(guestBtn);
        hbBtns.getChildren().add(signBtn);
        
        grid.add(hbBtns, 1, 4);
        
        
        
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        signBtn.setOnAction(new EventHandler<ActionEvent>() { 
            @Override
            public void handle(ActionEvent e) {
            	String username = userTextField.getText();
            	if(!username.isEmpty()) {
            		Scene shopScene = generateShop(username);
            		primaryStage.setScene(shopScene);
            		primaryStage.show();
            	}else{
            		actiontarget.setFill(Color.FIREBRICK);
            		actiontarget.setText("No Username Input");
            	}
            }
        });
        
        guestBtn.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e) {
        		Scene shopScene = generateShop("guest");
        		primaryStage.setScene(shopScene);
        		primaryStage.show();
        	}
        });
        
        quitBtn.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e) {
        		System.exit(0);
        	}
        });
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void setUpInternet() throws Exception{
		/*
		int currentServer = 7777;
		
		@SuppressWarnings("resource")
		Socket server = new Socket(currentServer);
		*/
	}
	
	
	public Scene generateShop(String username){
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
		Scene shop = new Scene(grid, 300, 275);
		
		
		Text scenetitle = new Text("Auction Page");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        highestBidder = new Label("Price: ");
        
        UserBackend currentUser = new UserBackend();
        currentUser.connectUser();
        List<String> auctionData = currentUser.getData();
        
        Label item = new Label("Item 1: " + auctionData.get(0));
        grid.add(item, 0, 1);

        Label info = new Label("Description: " + auctionData.get(1));
        grid.add(info, 0, 2);
        
        Label currentPrice = new Label("Highest Bidder: " + auctionData.get(2));
        grid.add(currentPrice, 0, 3);
        
        highestBidder = new Label("Price: " + auctionData.get(3));
        grid.add(highestBidder, 0, 4);
        
        Button bidBtn = new Button("Bid");
        grid.add(bidBtn, 0, 5);
        
        bidBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		currentUser.updateBid();
        		
        	}
        });
        
        return shop;
	}
	
	public static void updatePrice(String price) {
		highestBidder.setText(price);
	}
}
