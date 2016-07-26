package ckSnapInterpreter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import netscape.javascript.JSObject;
import ckGameEngine.CKGameObjectsFacade;
//git ~ anjackson / SwingFXWebView.java
//creates a JavaFX panel that can be placed in a Swing panel
//used to load CyberSnap
import com.sun.javafx.application.PlatformImpl;
  
/** 
 * SwingFXWebView 
 */  
public class SwingFXWebView extends JPanel {  
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 2657204081846082302L;
	private Stage stage;  
    private WebView browser;  
    private static JFXPanel jfxPanel;  
    private JButton swingButton;  
    private WebEngine webEngine;  
  
    public SwingFXWebView(){  
        initComponents();  
    }  
  
    public static JFXPanel main(String ...args){  
        // Run this later:
        SwingUtilities.invokeLater(new Runnable() {  
            @Override
            public void run() {  
                /*final JFrame frame = new JFrame();  
                 
                frame.getContentPane().add(new SwingFXWebView());  
                 
                frame.setMinimumSize(new Dimension(640, 480));  
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
                frame.setVisible(true);  */
                
            }  
        }); 
        return jfxPanel;
    }  
     
    private void initComponents(){  
         
        jfxPanel = new JFXPanel();  
        createScene();  
         
        setLayout(new BorderLayout());  
        add(jfxPanel, BorderLayout.CENTER);  
         
        swingButton = new JButton();  
        swingButton.addActionListener(new ActionListener() {
 
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
 
                    @Override
                    public void run() {
                        webEngine.reload();
                    }
                });
            }
        });  
        swingButton.setText("Reload");  
         
        //add(swingButton, BorderLayout.SOUTH);  
    }     
     
    /** 
     * createScene 
     * 
     * Note: Key is that Scene needs to be created and run on "FX user thread" 
     *       NOT on the AWT-EventQueue Thread 
     * 
     */  
    public void createScene() {  
        PlatformImpl.startup(new Runnable() {  
            @Override
            public void run() {  
                 
                stage = new Stage();  
                 
                stage.setTitle("CyberSnap");  
                stage.setResizable(true);  
   
                Group root = new Group();  
                Scene scene = new Scene(root,80,20);  
                stage.setScene(scene);  
                 
                // Set up the embedded browser:
                browser = new WebView();
                CKGameObjectsFacade.setWebEngine(browser.getEngine());
                //only changes below
                //load snap and make it executable 
                webEngine = CKGameObjectsFacade.getWebEngine();
                webEngine.load(getClass().getResource("CyberSnap/snap.html").toExternalForm());
        		JSObject jsobj = (JSObject) webEngine.executeScript("window");
    			jsobj.setMember("javaMove", new CKSpellObject("move"));
    			jsobj.setMember("jsDebug", new CKjsDebugger());
 

        		
                ObservableList<Node> children = root.getChildren();
                
                children.add(browser);                     
                 
                jfxPanel.setScene(scene);
                
            }  
        });  
    }
}
