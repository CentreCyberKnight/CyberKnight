package ckSnapInterpreter;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import netscape.javascript.JSObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.EventListener;


public class controlPanel extends Application // implements EventHandler<Action
												// Event>
{

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("A Control Panel");

		GridPane grid = new GridPane();
		grid.setHgap(30);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));

		TextField tSlide = new TextField();
		tSlide.setText("500");
		tSlide.setPrefWidth(500);
		//TextField t1 = new TextField();
		//t1.setText("XML Here");
		//t1.setPrefWidth(1000);
		
		//Button button = new Button();
		//button.setText("Click me!");

		WebView browser = new WebView();
		WebEngine webEngine = browser.getEngine();
		webEngine.load(getClass().getResource("snap.html").toExternalForm());
		//button.setOnAction(e -> {webEngine.executeScript("CustomCommandBlockMorph.prototype.exportBlockDefinition()");});
		//JSObject jsobj = (JSObject) webEngine.executeScript("window");
		//jsobj.setMember("javaTextField", t1);
		
		//grid.add(t1, 0, 4, 2, 1);
		//grid.add(button, 0, 2, 2, 1); 
		//grid.add(browser, 0, 6, 4, 1);
		grid.add(browser, 0, 1, 5, 1);
		
		Scene scene = new Scene(grid, 1000, 1000);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
