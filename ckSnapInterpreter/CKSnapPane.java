package ckSnapInterpreter;

import org.w3c.dom.Document;

import ckGameEngine.CKGameObjectsFacade;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

public class CKSnapPane extends Pane {

	WebView BrowserWindow;
	
	public CKSnapPane(CKData data) {
		
		this.setPrefSize(690, 820);
		this.setPadding(new Insets(5));
		//this.setStyle("-fx-background-color: rgb(217, 210, 240)");
		//this.setOpacity(0.5);
		this.getChildren().add(getSnap(data));
	}
	
	//getters and setters of WebView
	
	
	public Node getSnap(CKData data) {

		BrowserWindow = new WebView();
		CKGameObjectsFacade.setWebEngine(BrowserWindow.getEngine());
		WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		
		
		webEngine.setOnError(e->{System.err.println("JAVASCRIPT ERROR"+e.getMessage());});
		webEngine.getLoadWorker().exceptionProperty()
		.addListener((e,oldVal,newVal)->
		{System.err.println("JAVASCRIPT LOAD ERROR"+e.toString()+oldVal.getMessage()+newVal.getMessage());});
		/*webEngine.getLoadWorker().stateProperty().addListener(
			      new ChangeListener<Worker.State>() {
			      public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {                               
			             System.out.println("webEngine result "+ newState.toString());
			      }
			  });
		*/
		 webEngine.getLoadWorker().stateProperty().addListener(
		            new ChangeListener<State>() {
		              @Override public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, State oldState, State newState) {

		                    System.out.println("called"+newState);
		                  
		                }
		            });
		 
		 
		 
		 
		 
		//engine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}"); 

		
		/*webEngine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
		    @Override
		    public void changed(ObservableValue<? extends Throwable> ov, Throwable t, Throwable t1) {
		        System.out.println("Received exception: "+t1.getMessage());
		    }
		});
		*/
		
		BrowserWindow.setPrefSize(690, 820);
		webEngine.load(getClass().getResource("CyberSnap/snap.html").toExternalForm());
		
		
		webEngine.documentProperty().addListener(new ChangeListener<Document>() {
		      @Override public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
		    	webEngine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}"); 
		      }
		    });
		
		
		
		
		
		
		
		
		
		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		jsobj.setMember("javaMove", new CKSpellObject("move"));
		jsobj.setMember("jsDebug", new CKjsDebugger());

    	//CKDrawerTab snap = new CKDrawerTab(BrowserWindow, DrawerSides.RIGHT, 750.0, 0.0, 690.0, 820.0, "ckSnapInterpreter/text.png");
    	
    	data.registerArtifactObserver((artifact) -> 
    	{ 
    	try {
    		jsobj.setMember("artifact", artifact);
    		webEngine.executeScript("ide.setCyberSnap()");
    	}
    	catch (JSException e) {
    		System.out.println(e.getMessage());
    	}
    	});
    	return BrowserWindow;
	}
	
}

