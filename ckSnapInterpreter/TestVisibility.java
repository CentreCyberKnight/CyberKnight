package ckSnapInterpreter;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TestVisibility extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {            


            Pane pane = new Pane();

            HBox hbox = new HBox();

            hbox.setSpacing(12);

            Button button1 = new Button("Button 1");
            Button button2 = new Button("OMG Magic!");
            Button button3 = new Button("Button 3");

            hbox.getChildren().addAll(button1, button2, button3);

            pane.getChildren().add( hbox);


            final Scene scene = new Scene(pane, Color.TRANSPARENT);

//          scene.getRoot().setStyle("-fx-background-color: transparent");
            scene.getRoot().setStyle("-fx-background-color: rgba(1,1,1,0.004)");

            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);

            primaryStage.show();

            // initial opacity of button 2
            button2.setOpacity(0.0);

            // button2: fade-in on mouse scene enter
            scene.addEventHandler(MouseEvent.MOUSE_ENTERED, evt ->  {

                System.out.println("Mouse entered");

                FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), button2);
                fadeTransition.setFromValue(0.0);
                fadeTransition.setToValue(1.0);
                fadeTransition.play();

            });

            // button2: fade-out on mouse scene exit
            scene.addEventHandler(MouseEvent.MOUSE_EXITED, evt ->  {

                System.out.println("Mouse exited");

                FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), button2);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();              
            });   


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}