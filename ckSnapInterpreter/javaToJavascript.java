package ckSnapInterpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import netscape.javascript.JSObject;

public class javaToJavascript extends Application // implements EventHandler<Action
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
		TextField t1 = new TextField();
		t1.setText("XML Here");
		t1.setPrefWidth(1000);

		WebView browser = new WebView();

		WebEngine webEngine = browser.getEngine();
		// Display a local webpage
		webEngine.load(getClass().getResource("snap.html").toExternalForm());
		JSObject jsobj = (JSObject) webEngine.executeScript("window");

		jsobj.setMember("javaTextField", t1);

		grid.add(t1, 0, 4, 2, 1);

		grid.add(browser, 0, 6, 4, 1);

		Scene scene = new Scene(grid, 1000, 700);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
