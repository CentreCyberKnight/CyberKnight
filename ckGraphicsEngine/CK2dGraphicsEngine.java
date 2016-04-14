package ckGraphicsEngine;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JFrame;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKWorkSupervisorListener;
import ckCommonUtils.LogListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.CKGraphicsLayerFactory;
import ckDatabase.CKGraphicsLayerFactoryXML;
import ckDatabase.CKSceneFactory;
import ckGameEngine.CKGrid;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKSpriteAsset;
import ckGraphicsEngine.assets.CKSpriteInstance;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckGraphicsEngine.sceneAction.CKAddAssetAction;
import ckGraphicsEngine.sceneAction.CKAddInstanceAction;
import ckGraphicsEngine.sceneAction.CKAimAction;
import ckGraphicsEngine.sceneAction.CKChangeAnimationAction;
import ckGraphicsEngine.sceneAction.CKInstanceVisibleAction;
import ckGraphicsEngine.sceneAction.CKLinkInstanceAction;
import ckGraphicsEngine.sceneAction.CKMoveInstanceAction;
import ckGraphicsEngine.sceneAction.CKNullAction;
import ckGraphicsEngine.sceneAction.CKRemoveInstanceAction;
import ckGraphicsEngine.sceneAction.CKSceneAction;
import ckGraphicsEngine.test.CKfpsTest;
import ckSound.CKSound;
import ckSound.CKSoundFactory;
import ckSound.CKSoundLoopAction;
import ckSound.CKSoundPlayAction;
import ckSound.CKSoundStopAction;

//public class CK2dGraphicsEngine extends CKGamePanelTimer implements
//CKGraphicsEngine

public class CK2dGraphicsEngine extends CKfpsTest implements
CKGraphicsEngine,CKWorkSupervisorListener<CKGraphicsScene>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4537172638873370471L;
	CKGraphicsAssetFactory afactory;
	CKGraphicsLayerFactory lfactory;
	CKSceneFactory sfactory;
	CKSoundFactory soundfactory;
	HashMap<Integer,CKAssetInstance> instances;
	HashMap<Integer,CKSound> sounds;
	
	private static int cameraIID = 1;
	private static int soundIID = 1;
	
	
	
	int instanceCount;
	int soundCount;
	CKGraphicsSceneInterface scene;
	LinkedList<CKSceneAction> actions;

	CKGUI gui = new CKGUI();
	
	
	
	public CK2dGraphicsEngine()
	{
		this(30,5);
	}

	

	public CK2dGraphicsEngine(double framesPerSecond, int maxDropped)
	{
		super(framesPerSecond, maxDropped);
		afactory = CKGraphicsAssetFactoryXML.getInstance();
		lfactory = CKGraphicsLayerFactoryXML.getInstance();
		sfactory = CKSceneFactory.getInstance();
		soundfactory = CKSoundFactory.getInstance();
		
		instances = new HashMap<Integer,CKAssetInstance>();
		instanceCount=cameraIID+1;
		
		sounds = new HashMap<Integer,CKSound>();
		soundCount = soundIID+1;
		
		
		scene=null;
		MouseMessengerListener lis = new MouseMessengerListener();
		addMouseListener(lis);
		addMouseMotionListener(lis);
	}

	private CKAssetInstance getInstance(int IID) throws BadInstanceIDError
	{
		CKAssetInstance inst = instances.get(IID);
		if(inst==null)
		{
			throw(new BadInstanceIDError("Cannot find "+IID));
		}
		return inst;
	}
	
	
	
	@Override
	public synchronized void loadScene(String string)
	{
		scene=sfactory.getAsset(string,true);
		if(scene==null)
		{
			System.out.println("load unsuccessful");
		}
		else
		{
			System.out.println("load is successful");
			scene.addSingleWorkSupervisorListener(this);
			instances.put(cameraIID,scene.getCamera());
		}
	}

	@Override
	public int startTransaction(boolean block)
	{
		
		if(actions!= null)
		{
			endTransaction(0,false);
		}
		
        scene.startLoading();
		actions= new LinkedList<CKSceneAction>();
		
		return 1;
	}

	@Override
	public void endTransaction(int tid, boolean block)
	{
		//TODO need to handle state machine for transactions...
		
		if(block)
		{
			this.blockTilActionsComplete();
			
		}
		if(scene!=null && actions!=null)
		{
			scene.loadActions(actions); //this should block for us?
			actions=null;			
		}
//System.out.println("Leaving end Transaction");
	}

	@Override
	public void loadAsset(int tid, String string) throws LoadAssetError
	{
		afactory.getGraphicsAsset(string);
		
	}
	
	

	@Override
	public int createInstance(int tid, String AID, CKPosition pos, int startFrame, int layerDepth)
			throws LoadAssetError
	{
		CKGraphicsAsset asset = afactory.getGraphicsAsset(AID);
		return createUniqueInstance(tid,asset,pos,startFrame,layerDepth);
	}
	
	@Override
	public int createUniqueInstance(int tid, CKGraphicsAsset asset,
			CKPosition pos, int startFrame, int layerDepth)
	{
	
		CKAssetInstance inst=null;
		if(asset instanceof CKSpriteAsset)
		{
			inst = new CKSpriteInstance((CKPosition) pos.clone(), (CKSpriteAsset)asset, instanceCount);
		}
		else
		{
			inst = new CKAssetInstance((CKPosition) pos.clone(), asset, instanceCount);
		}
		
		instances.put(instanceCount,inst);
		instanceCount++;
			
		CKAddInstanceAction action = new CKAddInstanceAction(inst,layerDepth,startFrame);
		actions.add(action);
		
		return inst.getIID();
	}
	
	public int createSoundInstance(int tid, String soundID)
	{
		CKSound soundAsset = soundfactory.getAsset(soundID);
		
		sounds.put(soundCount, soundAsset);
		soundCount ++ ;
		
		return soundCount -1;
	}

	@Override
	public void destroy(int tid, int IID, int startFrame)
			throws BadInstanceIDError
	{
		CKAssetInstance inst = getInstance(IID);
		
		
		CKSceneAction a1=new CKRemoveInstanceAction(inst ,startFrame);
		actions.add(a1);

	}

	@Override
	public void hide(int tid, int IID, int startFrame)
			throws BadInstanceIDError
	{
		CKAssetInstance inst = getInstance(IID);
		
		
		CKInstanceVisibleAction a1=new CKInstanceVisibleAction(inst,false ,startFrame);
		actions.add(a1);
		
	}

	@Override
	public void reveal(int tid, int IID, int startFrame)
			throws BadInstanceIDError
	{
		
		CKAssetInstance inst = getInstance(IID);
		
		
		CKInstanceVisibleAction a1=new CKInstanceVisibleAction(inst,true ,startFrame);
		actions.add(a1);
	}

	@Override
	public int move(int tid, int IID, int startFrame, 
			CKPosition orgin,CKPosition destination,
			int speed) throws BadInstanceIDError
	{ //do not need to clone positions as they will be cloned in ckmoveInstanceAction
		CKAssetInstance inst = getInstance(IID);
		
		//need to calc ending time
		int frames = CKMoveInstanceAction.calcTravelTime(orgin, destination, speed);
		//System.out.println("I need "+frames+"to complete this action");
		CKMoveInstanceAction a1=new CKMoveInstanceAction(orgin,destination,inst,
											startFrame,startFrame+frames);
		actions.add(a1);
		
		
		return startFrame+frames;
	}

	
	
	@Override
	public int moveTo(int tid, int IID, int startFrame, 
			          CKPosition destination,int duration) throws BadInstanceIDError
	{//do not need to clone positions as they will be cloned in ckmoveInstanceAction
		CKAssetInstance inst = getInstance(IID);
		
	
	
		CKMoveInstanceAction a1=new CKMoveInstanceAction(null,destination,inst,
											startFrame,startFrame+duration);
		actions.add(a1);
		
		
		return startFrame+duration;
	}

	
	
	
	@Override
	public int getAnimationLength(int tid, String AID, String Animation)
			throws LoadAssetError, BadInstanceIDError, UnknownAnimationError
	{
		CKGraphicsAsset asset = afactory.getGraphicsAsset(AID); 
		if(asset == null)
		{
			throw (new LoadAssetError("Unable to load asset "+AID));
		}
		return asset.getAnimationLength(Animation);
	}

	@Override
	public int getInstanceAnimationLength(int tid, int IID, String animation)
			throws BadInstanceIDError, UnknownAnimationError
	{
		CKAssetInstance inst = getInstance(IID);
		
		return inst.getAsset().getAnimationLength(animation);
	}

	@Override
	public void setAnimation(int tid, int IID, String animation, int startFrame)
			throws BadInstanceIDError, UnknownAnimationError
	{
		
		CKSpriteInstance inst=null;
		try
		{
			inst = (CKSpriteInstance) getInstance(IID);
		}
		catch (Exception e)
		{
			throw new UnknownAnimationError("Not a Sprite Instance");
			
		}
		
		//check for the animation string-a cheat, it will throw if it cannot find.
		if (inst.getAnimationLength(animation) < 0)
		{
			throw new UnknownAnimationError("Unknown animation "+animation);
		}
		//
		actions.add(new CKChangeAnimationAction(inst,animation,startFrame));
		

	}

	@Override
	public void setAnimationChain(int tid, int IID, String[] animations,
			int[] start_times) throws BadInstanceIDError,
			UnknownAnimationError, IndexOutOfBoundsException
	{
		// TODO Auto-generated method stub
		
		
		
	}


	@Override
	public void replaceTile(int tid, String AID, CKPosition position,
			int layerDepth,int startFrame) throws LoadAssetError
	{
	
		CKGraphicsAsset asset = afactory.getGraphicsAsset(AID);
		if( asset == null)
		{
			throw new LoadAssetError("unable to find asset"+AID);
		}
			
		CKAddAssetAction action = new CKAddAssetAction((CKPosition)position.clone(),asset,layerDepth,startFrame);
		actions.add(action);
		
		return;
	}

	@Override
	public void linkGraphics(int tid, int IID_Child, int IID_Parent,
			RelationalLinkType rtype, int startFrame) throws BadInstanceIDError,
			CircularDependanceError
	{
		
		CKAssetInstance inst_child = getInstance(IID_Child);
		CKAssetInstance inst_parent = getInstance(IID_Parent);
		actions.add(new CKLinkInstanceAction(inst_child,inst_parent,rtype,startFrame));
		
	}

	@Override
	public void unLink(int tid, int IID, int startFrame)
			throws BadInstanceIDError
	{
		CKAssetInstance inst_child = getInstance(IID);
		actions.add(new CKLinkInstanceAction(inst_child,startFrame));
	}

	@Override
	public int cameraFollowInstance(int tid, int IID, int startFrame,
			int transition_duration) throws BadInstanceIDError, CircularDependanceError
	{
		//if I'm not already there I need to transition to that position
		CKAssetInstance inst_child = getInstance(IID);
		CKAssetInstance camera = getInstance(cameraIID);
		System.out.println("camera at"+camera.getPosition()+" inst at"+inst_child.getPosition());
		int arrives = cameraPointAt(tid,inst_child.getPosition(),startFrame,transition_duration);
		
		//now link to the parent
		linkGraphics(tid,cameraIID,IID,RelationalLinkType.RELATIVE,arrives);
		return arrives;

	}

	@Override
	public int cameraPointAt(int tid, CKPosition position, int startFrame,
			int transition_duration) throws BadInstanceIDError
	{
		//remove any parents
		unLink(tid,cameraIID,startFrame);
		//move to new location
		return moveTo(tid,cameraIID,startFrame,position,transition_duration);		
	}

	
	@Override
	public void calcState()
	{
		if(scene != null)
		{
			scene.calcState();
		}
		gui.calcState();

	}

	@Override
	public void drawOffScreenBuffer(Graphics g,int width,int height)
	{
		if(scene!=null)
			{
				scene.drawOffScreenBuffer(g, width, height);
			}
		else
		{
			System.out.println("scene is null");
			
		}
		//drawDebugToOffScreenBuffer(g,width,height);
		gui.drawOffScreenBuffer(g, width, height);
		
	}
	private Vector<CKGraphicMouseInterface>mouseListeners=new Vector<CKGraphicMouseInterface>();

	public void addMouseListener(CKGraphicMouseInterface list)
	{
		mouseListeners.add(list);
	}
	
	public void removeMouseListener(CKGraphicMouseInterface list)
	{
		mouseListeners.remove(list);
	}
	
	public void playSound(int stime, int etime,int SIID)
	{
		CKSound soundAsset = sounds.get(SIID);
		playSound(stime,etime,SIID,soundAsset.getPreferredVolume());
	}
	
		
	public void playSound(int stime, int etime, int SIID, double vol)
	{

		CKSound soundAsset = sounds.get(SIID);
		
		
		
		CKSoundLoopAction ckSoundLoopAction = new CKSoundLoopAction(stime, soundAsset, vol);
		CKSoundStopAction ckSoundStopAction = new CKSoundStopAction(etime, soundAsset);
		
		
		actions.add(ckSoundLoopAction);
		actions.add(ckSoundStopAction);	
	}
	
	
	
	public void playSound(int stime, int SIID, double vol)
	{
		CKSound soundAsset = sounds.get(SIID);
		
		
		CKSoundLoopAction ckSoundLoopAction = new CKSoundLoopAction(stime, soundAsset, vol);
		
		actions.add(ckSoundLoopAction);
	}
	

	public void playSound(int stime, int SIID)
	{
		CKSound soundAsset = sounds.get(SIID);
		//System.out.println("Playing sound at "+soundAsset.getPreferredVolume());
		playSound(stime,SIID,soundAsset.getPreferredVolume());
	}
	

	
	public void stopSound(int stime, int SIID)
	{
		CKSound soundAsset = sounds.get(SIID);
		
		CKSoundStopAction ckSoundStopAction = new CKSoundStopAction(stime, soundAsset);
		
		actions.add(ckSoundStopAction);	
	}
	
	
	
	
	private class MouseMessengerListener extends MouseAdapter
	{

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e)
		{
			System.out.println("Recieved Mouse click event");
			if(gui.handleMouseEvent(e)) 	{ return;		}
				 //check this to see if I should continue
			for(CKGraphicMouseInterface listener:mouseListeners)
			{
				listener.handleMouseClicked(e);
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseMoved(MouseEvent e)
		{
			//System.out.println("Mouse Moved");
			//nothing yet
			// TODO Auto-generated method stub
//			super.mouseMoved(e);
			for(CKGraphicMouseInterface listener:mouseListeners)
			{
				listener.handleMouseMoved(e);
			}

		}
		
	
		
	}
	
	
	
	
	
	
	public static void main(String argv[]) 
	{
		
		
		JFrame frame = new JFrame();
		
		CK2dGraphicsEngine engine = new CK2dGraphicsEngine(30,5);
		engine.setPreferredSize(new Dimension(600,600));
		
		//get scene
		engine.loadScene("Kitchen");

		
		String personAssetId = "babySprite";
		String spinAssetID = "Swirl";
		String slowSpinAsset="SlowSwirl";
		try
		{
			
			
			int tid =  engine.startTransaction(true);
			

			//int tid = 0;
			
			engine.loadAsset(tid, personAssetId);
			engine.loadAsset(tid, spinAssetID);
			engine.loadAsset(tid, slowSpinAsset);
			
			
			CKPosition pos1 = new CKPosition(5,5,0,0);
			CKPosition pos2 = new CKPosition(1,1,0,0);
			CKPosition pos3 = new CKPosition(8,8,0,-1);
			
			//sprite
			int spriteID = engine.createInstance(tid,personAssetId,pos1,30,CKGraphicsLayer.SPRITE_LAYER);

			//move camera
			CKPosition cam1 = new CKPosition(10,1,0,0);
			CKPosition cam2 = new CKPosition(1,10,0,0);
			
			//int arrives = engine.cameraPointAt(tid, cam1, 30,30);
			int arrives =30;
			
			//arrives = engine.cameraPointAt(tid, cam2, arrives,30);
			//spin			
			int fastspin = engine.createInstance(tid,spinAssetID,pos2,arrives,CKGraphicsLayer.SPRITE_LAYER);
			//regulated spin
			int spinIID = engine.createInstance(tid,slowSpinAsset,pos3,arrives+30,CKGraphicsLayer.SPRITE_LAYER);
			//walkforward
			arrives = arrives+30;
			engine.cameraFollowInstance(tid, spriteID, arrives+30, 0);
			engine.setAnimation(tid,spriteID,"NORTHWEST",arrives);
			
			CKPosition p1 = new CKPosition(3,2,0,0);
			CKPosition p2=new CKPosition(3,8,0,0);
			int endtime=engine.move(tid,spriteID,arrives+30,p1,p2,10);
			int SIID = engine.createSoundInstance(0,"Buzz");
			int SIID2 = engine.createSoundInstance(0,"Rainforest");
			engine.playSound(arrives+30,endtime,SIID,0.90);
			engine.playSound(0, SIID2, 0.90);
			
			/*//link swirl to person, move into place
			CKPosition p2SOUTHEAST = new CKPosition(3,8,0,-1);
			System.out.println("calcing spin");
			endtime = engine.move(tid, spinIID, endtime, pos3, p2SOUTHEAST, 90);
			engine.linkGraphics(tid, spinIID, spriteID, RelationalLinkType.RELATIVE, endtime);
			
			//now we move again
			
			engine.setAnimation(tid,spriteID,"NORTHEAST",endtime);
			
			CKPosition p3 = new CKPosition(8,8,0,0);
			endtime=engine.move(tid,spriteID,endtime,p2,p3,10);
			
			
			//add follow me sprite

			CKPosition pfollow = new CKPosition(7,8,0,0);
			int spriteID2 = engine.createInstance(tid,personAssetId,pfollow,endtime,
					CKGraphicsLayer.SPRITE_LAYER);
			engine.linkGraphics(tid, spriteID2, spriteID, RelationalLinkType.PULL, endtime);
			
			//spiral turns off and on
			for(int i=0;i<10;i++)
			{
				int time = endtime+i*60;
			
				engine.hide(tid,spinIID,time);
				engine.reveal(tid,spinIID,time+30);
			}
			//move across screen
			p1 = new CKPosition(8,8,0,0);
			p2=new CKPosition(8,1,0,0);
			engine.setAnimation(tid,spriteID,"SOUTHEAST", endtime);
			endtime=engine.move(tid,spriteID,endtime,p1,p2,10);

			//destroy entity
			engine.destroy(tid,fastspin,endtime);
			
			//move across screen
			p1 = new CKPosition(8,1,0,0);
			p2=new CKPosition(4,1,0,0);
			engine.setAnimation(tid,spriteID,"SOUTHWEST", endtime);
			endtime=engine.move(tid,spriteID,endtime,p1,p2,10);

			
			//move across screen
			p1 = new CKPosition(4,1,0,0);
			p2=new CKPosition(4,6,0,0);			
			engine.setAnimation(tid,spriteID,"NORTHWEST", endtime);
			endtime=engine.move(tid,spriteID,endtime,p1,p2,10);

			
			//move across screen
			p1 = new CKPosition(4,6,0,0);
			p2=new CKPosition(9,6,0,0);
			engine.setAnimation(tid,spriteID,"NORTHEAST", endtime);
			endtime=engine.move(tid,spriteID,endtime,p1,p2,10);*/
			
	
			
			
			engine.endTransaction(0, false);
		} catch (LoadAssetError e)
		{
			
			e.printStackTrace();
		} catch (BadInstanceIDError e)
		{
			
			e.printStackTrace();
		} catch (UnknownAnimationError e)
		{
			
			e.printStackTrace();
		} catch (CircularDependanceError e)
		{
			
			e.printStackTrace();
		}
		
		
		//get a person
		
		//move person across the screen
		 
		 
		
		frame.add(engine);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	
	}



	@Override
	public void loadDialogMessage(CKDialogMessage mess)
	{
		//should this be in a transaction TODO
		gui.addDialogMessage(mess);
		
	}

	
	public void loadDialogDebugScript(String script, int frames)
	{
		gui.loadSatisfyScript(script, frames);
		
	}
	
	public void loadDialogLogListener(LogListener l)
	{
		gui.addListener(l);
	}


	@Override
	public  synchronized void blockTilActionsComplete()
	{
		//System.out.println("                                        entering blocking");
		if (scene == null) { return; }
		if(scene.workCompleted()) { return;}
		//System.out.println("                                        past first hurdleentering blocking");
		
		if(EventQueue.isDispatchThread())
		{
			throw new SecurityException("Attempted to block on an Event Thread");
			//return;
			
		}
		
		
		while(scene!=null && !scene.workCompleted() )
		{//need a while loop since there could be multiple waiters
			try
			{
				//System.out.println("Going to sleep!!");
				wait(); //TODO need to figure out who will call this...
				//System.out.println("Waking UP!!");
			}
			catch (InterruptedException e) {}
		}
		return;
	}



	@Override
	public synchronized void workCompleted(CKGraphicsScene entity)
	{
		notifyAll();		
	}



	@Override
	public void selectArea(CKPosition originLocation, double minDistance,
			double maxDistance, CKSelectedPositionsListeners callSOUTHEAST,
			SelectAreaType type)
	{
		//start new transaction
		int t=this.startTransaction(true);
		//create the action
		CKAimAction a = new CKAimAction(originLocation,minDistance,
				maxDistance,callSOUTHEAST,type);
		actions.add(a);
		//add to listeners list
		this.addMouseListener(a);
		//end transaction
		this.endTransaction(t,false);
		
	}



	@Override
	public void selectAreaOffsets(CKPosition originLocation, double minDistance,
			double maxDistance, CKSelectedPositionsListeners callSOUTHEAST,
			Collection<CKPosition> offsets)
	{
		//start new transaction
		int t=this.startTransaction(true);
		//create the action
		CKAimAction a = new CKAimAction(originLocation,minDistance,
				maxDistance,callSOUTHEAST,offsets);
		actions.add(a);
		//add to listeners list
		this.addMouseListener(a);
		//end transaction
		this.endTransaction(t,false);
		
		
		
	}

	@Override
	public void selectAreaOffsets(CKPosition originLocation, Collection<CKPosition> possibles,
			CKSelectedPositionsListeners callback,
			Collection<CKPosition> offsets)
	{
		//start new transaction
		int t=this.startTransaction(true);
		//create the action
		CKAimAction a = new CKAimAction(originLocation,possibles,callback,offsets);
		actions.add(a);
		//add to listeners list
		this.addMouseListener(a);
		//end transaction
		this.endTransaction(t,false);
		
		
		
	}

	@Override
	public void highlightArea(CKPosition originLocation, int startTime,
			Iterable<CKPosition> offsets)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public void unhighlight(int startTime)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public CKGrid getGrid()
	{
		if(scene==null) {return null;}
		return scene.getGrid();
	}

	public CKGraphicsSceneInterface getCKScene()
	{
		return scene;
	}

	@Override
	public void createNullAction(int tid, int startTime, int endTime)
	{
		actions.add(new CKNullAction(startTime,endTime));
		
	}



	
	
	
	
	
	
	
	
	
	
	
	
	
}
