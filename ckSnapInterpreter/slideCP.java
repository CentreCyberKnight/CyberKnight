package ckSnapInterpreter;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
 
/** Example of a sidebar that slides in and out of view */
public class slideCP extends Application {

  WebView browser;
  public static void main(String[] args) throws Exception { launch(args); }
  
  public void start(final Stage stage) throws Exception {
 
    // create a WebView to show to the right of the SideBar.
    browser = new WebView();
    browser.setPrefSize(800, 600);
    WebEngine webEngine = browser.getEngine();
	webEngine.load(getClass().getResource("snap.html").toExternalForm());
 
    // create a sidebar with some content in it.
    final Pane menuPane = createSidebarContent();
    SideBar sidebar = new SideBar(250, menuPane);
    VBox.setVgrow(menuPane, Priority.ALWAYS);
 
    // layout the scene.
    final BorderPane layout = new BorderPane();
    Pane mainPane = VBoxBuilder.create().spacing(10)
      .children(
        sidebar.getControlButton(),
        browser
      ).build();
    layout.setLeft(sidebar);
    layout.setCenter(mainPane);
 
    // show the scene.
    Scene scene = new Scene(layout);
    scene.getStylesheets().add(getClass().getResource("slideout.css").toExternalForm());
    stage.setScene(scene);
    stage.show();
  }
 
  private BorderPane createSidebarContent() {// create some content to put in the sidebar.

    final BorderPane menuPane = new BorderPane();
    TextField t1 = new TextField("This is the sidebar");
    menuPane.setCenter(t1);
    return menuPane;
  }
 
  /** Animates a node on and off screen to the left. */
  class SideBar extends VBox {
	  
    /** @return a control button to hide and show the sidebar */
    public Button getControlButton() { 
    	return controlButton; 
    }
    private final Button controlButton;
 
    /** creates a sidebar containing a vertical alignment of the given nodes */
    SideBar(final double expandedWidth, Node... nodes) {
      //getStyleClass().add("sidebar");
      this.setPrefWidth(expandedWidth);
      this.setMinWidth(0);
 
      // create a bar to hide and show.
      setAlignment(Pos.CENTER);
      getChildren().addAll(nodes);
 
      // create a button to hide and show the sidebar.
      controlButton = new Button("Slidey Tab");
      //controlButton.getStyleClass().add("hide-left");
 
      // apply the animations when the button is pressed.
      controlButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
          // create an animation to hide sidebar.
          final Animation hideSidebar = new Transition() {
            { setCycleDuration(Duration.millis(250)); }
            protected void interpolate(double frac) {
              final double curWidth = expandedWidth * (1.0 - frac);
              setPrefWidth(curWidth);
              setTranslateX(-expandedWidth + curWidth);
            }
          };
          hideSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
              setVisible(false);
              controlButton.setText("Show");
              //controlButton.getStyleClass().remove("hide-left");
              //controlButton.getStyleClass().add("show-right");
            }
          });
  
          // create an animation to show a sidebar.
          final Animation showSidebar = new Transition() {
            { setCycleDuration(Duration.millis(250)); }
            protected void interpolate(double frac) {
              final double curWidth = expandedWidth * frac;
              setPrefWidth(curWidth);
              setTranslateX(-expandedWidth + curWidth);
            }
          };
          showSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
              controlButton.setText("Slidey Tab");
              //controlButton.getStyleClass().add("hide-left");
              //controlButton.getStyleClass().remove("show-right");
            }
          });
  
          if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
            if (isVisible()) {
              hideSidebar.play();
            } else {
              setVisible(true);
              showSidebar.play();
            }
          }
        }
      });
    }
  }
  
}