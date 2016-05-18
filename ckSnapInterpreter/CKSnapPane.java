package ckSnapInterpreter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.w3c.dom.Document;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import ckDatabase.CKConnection;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKTeam;

public class CKSnapPane extends Pane {

	WebView BrowserWindow;

	public CKSnapPane(CKDataModel data) {

		this.setPrefSize(690, 820);
		this.setPadding(new Insets(5));
		// this.setStyle("-fx-background-color: rgb(217, 210, 240)");
		// this.setOpacity(0.5);
		this.getChildren().add(getSnap(data));
	}

	// getters and setters of WebView

	public Node getSnap(CKDataModel data) {

		BrowserWindow = new WebView();
		CKGameObjectsFacade.setWebEngine(BrowserWindow.getEngine());
		WebEngine webEngine = CKGameObjectsFacade.getWebEngine();

		webEngine.setOnError(e -> {
			System.err.println("JAVASCRIPT ERROR" + e.getMessage());
		});
		
		
		webEngine.setOnAlert(e -> {
			System.err.println("JAVASCRIPT ERROR" + e.getData());
		});
		
		
		
		webEngine
				.getLoadWorker()
				.exceptionProperty()
				.addListener(
						(e, oldVal, newVal) -> {
							System.err.println("JAVASCRIPT LOAD ERROR"
									+ e.toString() + oldVal.getMessage()
									+ newVal.getMessage());
						});
		/*
		 * webEngine.getLoadWorker().stateProperty().addListener( new
		 * ChangeListener<Worker.State>() { public void changed(ObservableValue
		 * ov, Worker.State oldState, Worker.State newState) {
		 * System.out.println("webEngine result "+ newState.toString()); } });
		 */
		/*
		 * webEngine.getLoadWorker().stateProperty().addListener( new
		 * ChangeListener<State>() {
		 * 
		 * @Override public void changed(ObservableValue ov, State oldState,
		 * State newState) {
		 * 
		 * System.out.println("called"+newState);
		 * 
		 * } });
		 */
		webEngine
				.getLoadWorker()
				.stateProperty()
				.addListener((obs, oldValue, newValue) -> {
					if (newValue == State.SUCCEEDED) {

						
						//remote
						//webEngine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
						//local
//						URL firebug = getClass().getResource("firebug-lite.js");
						String firebug = "../firebug-lite.js";
//						System.out.println(firebug);
						//webEngine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src','"+firebug +"' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
						
						// Load the last SNAP configuration
						File f = new File(
								CKConnection.getCKSettingsDirectory(),
								"snapConfig.xml");
						if (f.exists()) {
							byte[] encoded;
							try {

								
								encoded = Files.readAllBytes(Paths.get(f
										.toURI()));
								String xml = new String(encoded);
								// Load to snap!!!
								JSObject jsobj = (JSObject) webEngine
										.executeScript("window");
								jsobj.setMember("importXML", xml);
								//System.out.println("Loading XML"+xml);
								webEngine.executeScript("ide.ckImportXML()");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});

		// engine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");

		/*
		 * webEngine.getLoadWorker().exceptionProperty().addListener(new
		 * ChangeListener<Throwable>() {
		 * 
		 * @Override public void changed(ObservableValue<? extends Throwable>
		 * ov, Throwable t, Throwable t1) {
		 * System.out.println("Received exception: "+t1.getMessage()); } });
		 */

		BrowserWindow.setPrefSize(690, 820);
		webEngine.load(getClass().getResource("CyberSnap/snap.html")
				.toExternalForm());

		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		jsobj.setMember("javaMove", new CKSpellObject("move"));
		jsobj.setMember("jsDebug", new CKjsDebugger());

		data.registerArtifactObserver((artifact) -> {
			if (artifact == null) {
				return;
			}
			try {
				System.out.println("artifact+"+artifact);
				jsobj.setMember("artifact", artifact);
				webEngine.executeScript("ide.setCyberSnap()");
			} catch (JSException e) {
				System.err.println(e.getMessage());
			}
		});
		return BrowserWindow;
	}

}
