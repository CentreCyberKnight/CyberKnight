package ckSnapInterpreter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class imageTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

    	Pane menuPane = new Pane();
    	menuPane.setPrefSize(200, 200);
    	
    	Rectangle rectangle = new Rectangle(180,180);
    	rectangle.setFill(Color.RED);
    	Image img = new Image("headshot.png");
    	rectangle.setFill(new ImagePattern(img));    	

    	menuPane.getChildren().addAll(rectangle);
        
        
        Scene scene = new Scene(menuPane,800,800, Color.AQUAMARINE);
        primaryStage.setTitle("Test Image");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
	
	
}