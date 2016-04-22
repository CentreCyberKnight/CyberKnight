package ckGameEngine;

import static ckGameEngine.CKGameObjectsFacade.getConsole;
import static ckGameEngine.CKGameObjectsFacade.getQuest;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Vector;

import ckCommonUtils.CKScriptTools;
import ckPythonInterpreter.CKPlayerObjectsFacade;

public class ActorScriptController extends ActorController
{

	
	String text;
	Vector<String> snippets=new Vector<String>();
	Iterator<String> iterator;
	
	
	public ActorScriptController(CKGridActor pc,String text)
	{
		super(pc);
		setText(text);
	}
	
	
	

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}




	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
		
/*
		int lastIndex = 0; 
		int nextIndex = text.indexOf("---", lastIndex);
		while(nextIndex!=-1)
		{
			int len = nextIndex-lastIndex;
			if(len>0)
			{
				snippets.add(text.substring(lastIndex, nextIndex));
			}
			//now to reposition the lastIndex to end of line
			lastIndex = text.indexOf('\n',nextIndex);
			if(lastIndex == -1) { break;} //file ends without newline.
			lastIndex++; //move it past the newline.
			nextIndex = text.indexOf("---", lastIndex);		
		}
		//might be a last snippet here
		
		if(lastIndex!=-1 && lastIndex < text.length()-1)
		{
			String last = text.substring(lastIndex);
			//just auto add a \n in case they forgot
			snippets.add(last+'\n');
		}*/
		snippets = CKScriptTools.pullOutActions(text);
		iterator = snippets.iterator();
		
		for(String s:snippets)
		{
			System.out.println(s);
			System.out.println("+++++++++++++++++++++++");
		}
	}




	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#takeMyTurn()
	 */
	@Override
	protected void takeMyTurn()
	{
		CKGameObjectsFacade.setCurrentPlayer(getActor());
		CKPlayerObjectsFacade.calcCPTurnLimit();

		if (iterator.hasNext())
		{

			Quest w = getQuest();

			w.startTransaction();


			String code = "from ckPythonInterpreter.CKEditorPCController import * \n\n"+
							getActor().getTeam().getFunctions()+"\n"+
							iterator.next();

			getConsole().runNewCode(code);

			getConsole().waitForCompletion();

			w.endTransaction();
			if(! CKGameObjectsFacade.unitTest)
			{ CKGameObjectsFacade.getEngine().blockTilActionsComplete(); }
		}
		else
		{
			fail("Script has no more actions");
		}
		
		CKGameObjectsFacade.setCurrentPlayer(null);
		//main thread will run through here.
	}

	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#getInitialTurnEvent()
	 */
	@Override
	public Event getInitialTurnEvent(double start)
	{
		
		
		return new ActorEvent(getActor(),getActor().calcTimeToNextTurn(start));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PCArtifactController for "+getActor().getName();
	}

	
	
	public static void main(String [] args)
	{
		
		String s = 
				"-----start-----\n" +
				"I like to eat\n" +
				"pizza and stuff\n" +
				"------next----\n" +
				"but I like to drink\n" +
				"------root-----\n" +
				"------beer-----\n" +
				"with guack";
		new ActorScriptController(null,s);
		
		
		String s2 = 
				"I like to eat\n" +
				"pizza and stuff\n" +
				"------next----\n" +
				"but I like to drink\n" +
				"------root-----\n" +
				"------beer-----\n" +
				"with guack\n";
		new ActorScriptController(null,s2);

		
		String s3 = 
				"I like to eat\n" +
				"pizza and stuff\n" +
				"------next----\n" +
				"but I like to drink\n" +
				"------root-----\n" +
				"------beer-----\n" +
				"with guack\n" +
				"-------end-------";
		new ActorScriptController(null,s3);

		
		
		
		
		
		
		
	}
	
}
