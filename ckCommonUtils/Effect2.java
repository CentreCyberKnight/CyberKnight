package ckCommonUtils;



import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Effect2 extends Application{
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.BLACK);
		primaryStage.setScene(scene);
		Circle c = new Circle(150,Color.web("white",0.2));
		c.setCenterX(200);
		c.setCenterY(300);
		c.setStrokeType(StrokeType.OUTSIDE);
		c.setStroke(Color.web("white", 0.1));
        c.setStrokeWidth(10);
        c.centerXProperty().bind(scene.widthProperty().divide(2));
        c.centerYProperty().bind(scene.heightProperty().divide(2));
        //c.setEffect(new Glow(0.5));
        c.setEffect(new BoxBlur(5, 5, 3));
        /*Circle c1=new Circle(140,Color.web("white",0.2));
        c1.setCenterX(400);
        c1.setCenterY(300);
        c1.setEffect(new BoxBlur(5,5,3));
        Group circles=new Group(c1,c);*/
        
        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
       	     new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new 
       	         Stop[]{
       	            new Stop(0, Color.web("#f8bd55")),
       	            new Stop(0.14, Color.web("#c0fe56")),
       	            new Stop(0.28, Color.web("#5dfbc1")),
       	            new Stop(0.43, Color.web("#64c2f8")),
       	            new Stop(0.57, Color.web("#be4af7")),
       	            new Stop(0.71, Color.web("#ed5fc2")),
       	            new Stop(0.85, Color.web("#ef504c")),
       	            new Stop(1, Color.web("#f2660f")),}));
       	            /*new Stop(0, Color.web("#be4af7")),
       	            new Stop(1,Color.CORAL)}));*/
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());
        //root.getChildren().add(colors);
		//root.getChildren().add(c);
        Group blendModeGroup = new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(), Color.BLACK), c), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
		root.getChildren().add(blendModeGroup);
		 /*Timeline timeline = new Timeline();
         timeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(0), // set start position at 0
                    new KeyValue(c.translateXProperty(), random() * 800),
                    new KeyValue(c.translateYProperty(),random()*600)
                ),
                new KeyFrame(new Duration(30000), // set end position at 40s
                    new KeyValue(c.translateXProperty(), random()*800),
                    new KeyValue(c.translateYProperty(), random()*600)
                )
            );
            timeline.play();*/
		Timeline timeline=new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		//create a keyValue with factory: scaling the circle 2times
        KeyValue keyValueX = new KeyValue(c.scaleXProperty(), 3);
        KeyValue keyValueY = new KeyValue(c.scaleYProperty(), 3);
 
        //create a keyFrame, the keyValue is reached at time 2s
        Duration duration = Duration.millis(2000);
        //one can add a specific action when the keyframe is reached
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                 root.setTranslateX(java.lang.Math.random()*200-100);
                 
            }
        };
 
  KeyFrame keyFrame = new KeyFrame(duration, onFinished , keyValueX, keyValueY);
 
        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);
        //timeline.play();
 
		primaryStage.show();
	}
}