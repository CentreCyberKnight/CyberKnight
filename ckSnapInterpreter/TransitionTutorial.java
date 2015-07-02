package ckSnapInterpreter;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class TransitionTutorial extends Application {
 
    @Override
    public void start(Stage primaryStage) {

    	Pane menuPane = new Pane();
    	menuPane.setPrefSize(200, 200);
    	menuPane.setId("menuPane");
    		     
    	Rectangle rectangle = new Rectangle(250,400,Color.web("rgb(0,20,28)", 0.2));
    	rectangle.relocate(-250, 0);
    	
    	Button close1 = new Button("Close");
    	close1.relocate(50, 400);
    	close1.relocate(-50, 0);
    		     
    	menuPane.getChildren().addAll(rectangle, close1);
    	TranslateTransition slideOut = new TranslateTransition(Duration.seconds(2), rectangle);
    	slideOut.setByX(250);
    	
    	TranslateTransition slideIn = new TranslateTransition(Duration.seconds(2), rectangle);
    	slideIn.setByX(-250);
    	
    	TranslateTransition buttonSlide = new TranslateTransition(Duration.seconds(2), close1);
    	buttonSlide.setByX(250);
    	
    	TranslateTransition buttonClose = new TranslateTransition(Duration.seconds(2), close1);
    	buttonClose.setByX(-250);

        Button openBtn= new Button("Open"); 
        menuPane.getChildren().addAll(openBtn);
        openBtn.relocate(280,10);
        
        openBtn.setOnAction(e ->  {
        	slideOut.play();
        	buttonSlide.play();
        });
        
        close1.setOnAction(e -> {
        	buttonClose.play();
        	slideIn.play();
        	
        });
        
        Scene scene = new Scene(menuPane,800,550,Color.DARKOLIVEGREEN);
        scene.getStylesheets().add(getClass().getResource("ckSlideStyle.css").toExternalForm());
        primaryStage.setTitle("Slide Out");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}