package ckGameEngine;

import javafx.scene.web.WebEngine;

import javax.swing.JButton;

import ckCommonUtils.CKThreadCompletedListener;
import ckEditor.CKTeamArtifactEditor;
import ckGameEngine.Quest;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKTeamView;
import ckTrigger.CKTriggerListNode;

public class CKGameObjectsFacade
{

	// this will get replaced with a variable to the entity that creates the
	// charactrs
	// for now just store the PC that we want the editor to grab
	private static Quest quest;
	private static CK2dGraphicsEngine engine;
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
			//webEngine = 
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

	public static CK2dGraphicsEngine getEngine()
	{

		// FIXME uncomment
		if (engine == null)
		{
			engine = new CK2dGraphicsEngine(30, 5);
		}
		return engine;
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
		// TODO Auto-generated method stub
		String name = getCurrentPlayer().getName();
		artifactController.enableCharacter(name, false);
	}

	public static void enableArtifactInput()
	{
		// TODO Auto-generated method stub
		String name = getCurrentPlayer().getName();
		System.out.println("I want to have a cookie:" + name);
		artifactController.gotoTab(name);
		artifactController.enableCharacter(name, true);
	}

	public static void runSpell(String code, CKThreadCompletedListener listen)
	{
		if (console != null)
		{
			//System.out.println("Running this code:\n"+code);
			console.runNewCode(code, listen);
		}
	}

}
