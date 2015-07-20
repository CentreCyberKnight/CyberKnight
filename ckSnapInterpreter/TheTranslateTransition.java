package ckSnapInterpreter;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class TheTranslateTransition extends Application {
     
    Pane pane;
    Scene scene;
    Rectangle rect;
    TranslateTransition translate;
     
    @Override
    public void start(Stage stage) throws Exception {
        pane = new Pane();
         
        scene = SceneBuilder
                .create()
                .root(pane)
                .width(600)
                .height(300)
                .build();
         
        rect = RectangleBuilder
                .create()
                .width(100)
                .height(50)
                .stroke(Color.SLATEGREY)
                .fill(Color.WHITESMOKE)
                .build();
         
        translate = TranslateTransitionBuilder
                    .create()
                    .duration(new Duration(5 * 1000))
                    .node(rect)
                    .toX(-100)
                    .autoReverse(true)
                    .cycleCount(Timeline.INDEFINITE)
                    .interpolator(Interpolator.EASE_BOTH)
                    .build();
         
        pane.getChildren().addAll(rect);
         
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
         
        translate.play();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}