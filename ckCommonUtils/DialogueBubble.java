package ckCommonUtils;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DialogueBubble extends Application{
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("DialogueBubble");
		Label chat=new Label("I'm a bubble!");
		chat.getStyleClass().add("chat-bubble");
		chat.setContentDisplay(ContentDisplay.CENTER);
		
		GridPane root=new GridPane();
		root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		root.setAlignment(Pos.CENTER);
		
	    root.getChildren().addAll(chat);
	    Scene scene = new Scene(root, 500, 500);
	    scene.getStylesheets().add(getClass().getResource("Bubble.css").toExternalForm());
	    primaryStage.setScene(scene);
	    primaryStage.show();
		
		
		
		
		
		/*GridPane chat = new GridPane();
	    chat.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

	    ColumnConstraints c1 = new ColumnConstraints();
	    c1.setPercentWidth(100);
	    chat.getColumnConstraints().add(c1);

	    for (int i = 0; i < 20; i++) {
	        Label chatMessage = new Label("Hi " + i);
	        chatMessage.getStyleClass().add("chat-bubble");
	        GridPane.setHalignment(chatMessage, i % 2 == 0 ? HPos.LEFT
	                : HPos.RIGHT);
	        chat.addRow(i, chatMessage);
	    }

	    ScrollPane scroll = new ScrollPane(chat);
	    scroll.setFitToWidth(true);

	    Scene scene = new Scene(scroll, 500, 500);
	    scene.getStylesheets().add(getClass().getResource("Bubble.css").toExternalForm());
	    primaryStage.setScene(scene);
	    primaryStage.show();*/
	}
	
	public static void main(String args){
		launch(args);
	}
}