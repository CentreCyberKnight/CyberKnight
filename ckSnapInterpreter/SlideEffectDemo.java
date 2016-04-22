package ckSnapInterpreter;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class SlideEffectDemo extends Application {
 
 private Rectangle2D boxBounds = new Rectangle2D(100, 100, 250, 200);
  
 private StackPane basePane;
 private StackPane topPane;
 private Rectangle clipRect;
 private Timeline timelineLeft;
 private Timeline timelineRight;
  
 public static void main(String[] args) {
  Application.launch(args);
 }
  
 @Override
 public void start(Stage stage) throws Exception {
  VBox root = new VBox();
  root.setAlignment(Pos.CENTER);
  root.autosize();
  Scene scene = new Scene(root);
  stage.setTitle("Menu Slide Down/Up");
  stage.setWidth(350);
  stage.setHeight(300);
  stage.setScene(scene);
  stage.show();
      
  configureBox(root);
 }
 
 private void configureBox(VBox root) {
  StackPane container = new StackPane();
  container.setPrefHeight(250);
  container.setPrefSize(boxBounds.getWidth(), boxBounds.getHeight());
  container.setStyle("-fx-border-width:1px;-fx-border-style:solid;-fx-border-color:#999999;");
   
  // BASE PANE 
  basePane = new StackPane();
  Rectangle r = new Rectangle();
  r.prefHeight(boxBounds.getHeight());
  r.prefWidth(boxBounds.getWidth());
 // r.setFill(Color.RED);
  //basePane.setShape(r);
 TextField t1 = new TextField("this would be the game");
  basePane.getChildren().addAll(r);

  // TOP PANE 
 StackPane sp1 = new StackPane();
 TextField t2 = new TextField("this is the menu that slides down");
 Button b2 = new Button("this button does nothing");
 sp1.getChildren().add(b2);
 sp1.getChildren().add(t2);

  topPane = new StackPane();
  Rectangle r2 = new Rectangle();
  r2.prefHeight(boxBounds.getHeight());
  r2.prefWidth(boxBounds.getWidth());
  r.setOpacity(30);
  topPane.getChildren().addAll(r2, sp1);
  
  container.getChildren().addAll(basePane,topPane);
   
  setAnimation();
   
  Group gp = new Group();
  gp.getChildren().add(container);
  root.getChildren().addAll(getActionPane(),gp);
 }
 
 private void setAnimation(){
  // Initially hiding the Top Pane
  clipRect = new Rectangle();
  clipRect.setWidth(boxBounds.getWidth());
  clipRect.setHeight(0);
  clipRect.translateYProperty().set(boxBounds.getHeight());
  topPane.setClip(clipRect);
  topPane.translateYProperty().set(-boxBounds.getHeight());
        
  timelineRight = new Timeline();
  timelineLeft = new Timeline();
         
  // Animation for scroll out.
  timelineRight.setCycleCount(1);
  timelineRight.setAutoReverse(true);
  final KeyValue kvRight1 = new KeyValue(clipRect.widthProperty(), boxBounds.getHeight());
  final KeyValue kvRight2 = new KeyValue(clipRect.translateXProperty(), boxBounds.getHeight());
  final KeyValue kvRight3 = new KeyValue(topPane.translateXProperty(), boxBounds.getHeight());
  final KeyFrame kfDwn = new KeyFrame(Duration.millis(500), kvRight1, kvRight2, kvRight3);
  timelineRight.getKeyFrames().add(kfDwn);
   
  // Animation for scroll up.
  timelineLeft.setCycleCount(1); 
  timelineLeft.setAutoReverse(true);
  final KeyValue kvUp1 = new KeyValue(clipRect.heightProperty(), 0);
  final KeyValue kvUp2 = new KeyValue(clipRect.translateXProperty(), boxBounds.getHeight());
  final KeyValue kvUp3 = new KeyValue(topPane.translateXProperty(), -boxBounds.getHeight());
  final KeyFrame kfUp = new KeyFrame(Duration.millis(500), kvUp1, kvUp2, kvUp3);
  timelineLeft.getKeyFrames().add(kfUp);
 }
  
 private HBox getActionPane(){
  HBox hb = new HBox();
  hb.setAlignment(Pos.CENTER);
  hb.setSpacing(10);
  hb.setPrefHeight(40);
  Button upBtn = new Button("Slide Left");
  upBtn.setOnAction(e -> {
	  timelineLeft.play();
  });
  
  Button downBtn = new Button("Slide Right");
  downBtn.setOnAction(e -> {
    timelineRight.play();
  });
  hb.getChildren().addAll(downBtn,upBtn);
  return hb;
 }
}
