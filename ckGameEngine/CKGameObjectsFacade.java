package ckGameEngine;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import ckCommonUtils.CKThreadCompletedListener;
import ckEditor.CKTeamArtifactEditor;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKTeamView;
import ckTrigger.CKTriggerListNode;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CKGameObjectsFacade
{

	// this will get replaced with a variable to the entity that creates the
	// charactrs
	// for now just store the PC that we want the editor to grab
	private static Quest quest;
	private static FX2dGraphicsEngine engine;
	private static CKTeamView artifactController;
	private static WebEngine webEngine;
	/**
	 * @return the artifactController
	 */
	public static CKTeamView getArtifactController()
	{
		return artifactController;
	}

	private static CKPythonConsoleExtended console;
	private static CKTriggerListNode spells;
	
	private static CKTeamArtifactEditor aEditor;

	public static boolean unitTest = false;

	/**
	 * @return the spells
	 */
	public static CKTriggerListNode getSpells()
	{
		return spells;
	}


	public static void setWebEngine(WebEngine newWebEngine)
	{
		webEngine = newWebEngine;	
	}
	
	public static WebEngine getWebEngine()
	{
		if (webEngine == null)
		{
			WebView browser = new WebView();
			webEngine = browser.getEngine();
		}
		return webEngine;
	}
	
	/**
	 * @param spells
	 *            the spells to set
	 */
	public static void setSpells(CKTriggerListNode spells)
	{
		CKGameObjectsFacade.spells = spells;
	}

	public static void targetSpell(CKGameActionListenerInterface boss,
			CKSpellCast cast)
	{
		// TODO need a way to do world filter, then do defaults
		spells.doTriggers(boss, false, cast);
	}

	// Note that the constructor is private
	private CKGameObjectsFacade()
	{
	}

	public static Quest getQuest()
	{
		if (quest == null)
		{
			quest = new Quest();
		}
		return quest;
	}

	public static void setQuest(Quest q)
	{
		quest = q;
	}

	
	public static void setArtifactEditor(CKTeamArtifactEditor con)
	{
		aEditor = con;
	}

	public static CKTeamArtifactEditor getArtifactEditor()
	{
		return aEditor;
	}
	
	
	
	public static void setConsole(CKPythonConsoleExtended con)
	{
		console = con;
	}

	public static CKPythonConsoleExtended getConsole()
	{
		if (console == null)
		{
			console = new CKPythonConsoleExtended();
		}
		return console;
	}

	public static void setArtifactController(CKTeamView cont)
	{
		artifactController = cont;
	}

	public static FX2dGraphicsEngine getEngine()
	{

		// FIXME uncomment
		if (engine == null)
		{
			engine = new FX2dGraphicsEngine(30, 5);
		}
		return engine;
	}

	
	public static JPanel getJPanelEngine()
	{
		final JFXPanel panel = new JFXPanel();
		
		JPanel jpanel = new JPanel();
		jpanel.add(panel);
		 
		Platform.runLater(new Runnable()
		{
			/* (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run()
			{
				HBox root = new HBox();
				//root.setMaxHeight(Double.MAX_VALUE);
				//root.setMaxWidth(Double.MAX_VALUE);
				FX2dGraphicsEngine e = CKGameObjectsFacade.getEngine();
				e.maxHeight(Double.MAX_VALUE);
				e.maxWidth(Double.MAX_VALUE);
				e.prefHeight(600);
				e.prefWidth(600);
				e.widthProperty().bind(root.widthProperty());
				e.heightProperty().bind(root.heightProperty());
				root.getChildren().add(e);
				Scene scene = new Scene(root,600,600);
				panel.setScene(scene);
			}
		});
		 
		return jpanel;
		
	}
	
	public static void killEngine()
	{
		engine = null;
		System.out.println("Engine is Dead");
	}

	public static CKGrid getGrid()
	{
		return getEngine().getGrid();
	}

	private static CKPythonEditorPane editor;

	// Note that the constructor is private

	public static CKPythonEditorPane getUniqueEditor()
	{
		if (editor == null)
		{
			editor = new CKPythonEditorPane();
		}
		return editor;
	}

	private static JButton runButton;

	// Note that the constructor is private

	public static void setRunButton(JButton run)
	{
		runButton = run;
	}

	public static void enableTextInput()
	{
		runButton.setEnabled(true);
	}

	public static void disableTextInput()
	{
		runButton.setEnabled(false);
	}

	private static CKGridActor currentPC;

	public static void setCurrentPlayer(CKGridActor ckGridActor)
	{
		// TODO Auto-generated method stub
		currentPC = ckGridActor;
	}

	public static CKGridActor getCurrentPlayer()
	{
		return currentPC;
	}

	public static void disableArtifactInput()
	{
		
		String name = getCurrentPlayer().getName();
		artifactController.enableCharacter(name, false);
	}

	public static void enableArtifactInput()
	{
		// TODO Auto-generated method stub
		String name = getCurrentPlayer().getName();

		///* FIXME MKB link to Michelle's code!!
		artifactController.gotoTab(name);
		artifactController.enableCharacter(name, true);
		//*/
	}

	public static void runSpell(String code, CKThreadCompletedListener listen)
	{
		if (console != null)
		{
			//System.out.println("Running this code:\n"+code);
			console.runNewCode(code, listen);
		}
	}

	
	private static boolean predictionMode = false;
	private static CKSpellResult predictionResult = null;
	private static HashMap<CKAbstractGridItem,CKAbstractGridItem> items = new HashMap<>();
	
	public static void startPrediction()
	{
		predictionMode=true;
		predictionResult=new CKSpellResult();
		iteratePrediction();
		//note that cyberpoints need to be stored somewhere as well....
	}

	public static void iteratePrediction()
	{
		items.clear();
	}
	
	public static CKSpellResult endPrediction()
	{
		predictionMode=false;
		items.clear();
		CKSpellResult res = predictionResult;
		predictionResult=null;
		return res;
	}
	
	
	public static boolean isPrediction()
	{
		return predictionMode;
	}


	public static CKSpellResult getPredictionResult()
	{
		return predictionResult;
	}


	public static CKAbstractGridItem replaceTargets(CKAbstractGridItem target)
	{
		if(!items.containsKey(target))
		{
			items.put(target, target.makeCopy());
		}
		return items.get(target);
	}
	
}
