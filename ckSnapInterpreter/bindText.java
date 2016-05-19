package ckSnapInterpreter;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class bindText extends Application // implements EventHandler<Action
												// Event>
{

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("Label Binding");
		
		TextField userInput = new TextField();
		userInput.setMaxWidth(200);
		Label firstLabel = new Label("My name is ");
		Label blankLabel = new Label();
		
		HBox bottomText = new HBox(firstLabel, blankLabel);
		bottomText.setAlignment(Pos.CENTER);
	    
	   	VBox vBox = new VBox(10, userInput, bottomText);
	  	 vBox.setAlignment(Pos.CENTER);
	  	 
	  	 blankLabel.textProperty().bind(userInput.textProperty());

		
		Scene scene = new Scene(vBox, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	  	
	
}
