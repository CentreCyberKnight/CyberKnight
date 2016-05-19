package ckSnapInterpreter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



public class ckGui extends Application // implements EventHandler<Action
												// Event>
{

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("CyberKnight");

		GridPane root = new GridPane();
		root.setHgap(30);
		root.setVgap(10);
		root.setPadding(new Insets(0, 10, 0, 10));

		
	    Button m = new Button("Slidey Tab");
	    m.setPrefWidth(100);
	    m.setId("myButton");
	    m.setOnAction(e -> {
	    });
	    

	   
		//WebView browser = new WebView();
	    //browser.setPrefSize(800,600);
		//WebEngine webEngine = browser.getEngine();
		//webEngine.load(getClass().getResource("snap.html").toExternalForm());
		//button.setOnAction(e -> {webEngine.executeScript("CustomCommandBlockMorph.prototype.exportBlockDefinition()");});
		//JSObject jsobj = (JSObject) webEngine.executeScript("window");
		//jsobj.setMember("javaTextField", t1);
		//root.add(browser, 0, 1, 5, 1);
	    
	    root.add(m, 0, 1);

		Scene scene = new Scene(root, 1000, 1000);
		//scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	  	
	
}
