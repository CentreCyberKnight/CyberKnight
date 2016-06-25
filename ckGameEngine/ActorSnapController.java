package ckGameEngine;


import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import ckGameEngine.CKGameObjectsFacade;

import ckPythonInterpreter.CKPlayerObjectsFacade;

public class ActorSnapController extends ActorController
{

	
	
	public ActorSnapController()
	{
		this(null);
	}
	
	
	
	public ActorSnapController(CKGridActor pc)
	{
		super(pc);
	}


	volatile boolean snapDone = false;
	volatile boolean engineDone = false;
		
	public synchronized void waitForSnap()
	{
			snapDone = false;
			engineDone = false;
			SnapRunner runner = new SnapRunner(this);
			Platform.runLater(runner);
			
			
			while((!snapDone) || (!engineDone)) //need this in case snap completes before we can wait for it.
			{	try {wait();}
				catch (InterruptedException e) {
					System.err.println(e);
				}
			}
			//now wrap it up.
			System.out.print("after thread");
		}
		
		public synchronized void snapCompletes()
		{
			snapDone=true;
			System.out.println("SNAP COMPLETES");
			
			notify();
		}
		public synchronized void engineCompletes(){
			engineDone=true;
			System.out.println("ENGINE COMPLETES");
			
			notify();
		}
		
		public synchronized void engineStart(){
			engineDone=false;
		}
	
	public class SnapRunner extends Thread
	{
		protected ActorSnapController runner;
		
		public SnapRunner(ActorSnapController codeRunner)
		{
			runner = codeRunner;
		}
		
		public void run()
		{

				
				WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
				JSObject jsobj = (JSObject) webEngine.executeScript("window");
				jsobj.setMember("completionListener", runner);				
				System.out.println("in thread");
				//webEngine.executeScript("ide.fireTEST()");	
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#takeMyTurn()
	 */
	@Override
	protected void takeMyTurn()
	{
		
		System.out.println("in thread with" +getActor());
		CKGameObjectsFacade.setCurrentPlayer(getActor());
		//update GUI...
		//System.out.println("Se")
		CKGameObjectsFacade.getDataModel()
			.getActivePlayerProperty()
			.setData(getActor());
/*
		
		CKPlayerObjectsFacade.calcCPTurnLimit();
		//FIXME need to some how set the current actor in the model as well.
		
		Quest w = getQuest();
		
		w.startTransaction();
		//enableArtifactInput(); - should be handled by the GUI now.

//		w.waitForInput();//this is satisfied by the completion of the python code running
	
		getConsole().waitForCompletion();
		//FIXME

		
		
		w.endTransaction();
		CKGameObjectsFacade.getEngine().blockTilActionsComplete();	
	*/
		
		this.waitForSnap();
		
		CKGameObjectsFacade.getEngine().blockTilActionsComplete();	
		
		getActor().setCPConsumedLastRound(CKPlayerObjectsFacade.getCPTurnMax() - CKPlayerObjectsFacade.getCPTurnRemaining());
		
		CKGameObjectsFacade.setCurrentPlayer(null);
		CKGameObjectsFacade.getDataModel()
		.getActivePlayerProperty()
		.setData(null);
		//main thread will run through here.
	}

	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#getInitialTurnEvent()
	 */
	@Override
	public Event getInitialTurnEvent(double time)
	{
		
		//FIXME need to evaluate the 2 based on speed
		return new ActorEvent(getActor(),getActor().calcTimeToNextTurn(time));
	}

	

	
}