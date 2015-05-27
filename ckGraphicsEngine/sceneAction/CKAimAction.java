package ckGraphicsEngine.sceneAction;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Vector;

import ckCommonUtils.CKAreaPositions;
import ckCommonUtils.CKPosition;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.CKGraphicMouseInterface;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.CKSelectedPositionsListeners;
import ckGraphicsEngine.CKGraphicsEngine.SelectAreaType;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKSpriteAsset;
import ckGraphicsEngine.assets.CKSpriteInstance;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class CKAimAction extends CKSceneAction implements CKGraphicMouseInterface
{
	CKPosition origin;
	float min;
	float max;
	CKSelectedPositionsListeners listener;
	SelectAreaType type;
	Point mouseP;
	Point presentPoint;
	//int mouseX=0;
	//int mouseY=0;
	
	public static String rearHightlightID =  "asset4324539093293563502";
	public static String frontHightlightID = "asset1611874609805567554";
	
	public static String rearPossibleID = "yellowRH";
	public static String frontPossibleID = "yellowFH";
	
	
	private static CKGraphicsAssetFactory afactory;
	//private CKAssetInstance rear;
	//private CKAssetInstance front;
	
	private Vector<CKAssetInstance> rearTiles=new Vector<CKAssetInstance>();
	private Vector<CKAssetInstance> frontTiles=new Vector<CKAssetInstance>();
	boolean visible = false;
	CKGraphicsSceneInterface storedScene=null;
	boolean closing = false;
	private Collection<CKPosition> offsets;
	
	public CKAimAction(CKPosition originLocation, float minDistance,
			float maxDistance, CKSelectedPositionsListeners callback,
			SelectAreaType type)
	{
		super(0,1000 );
		
		init(originLocation,minDistance,maxDistance,callback,type);
		
		rearTiles.add(getRearHightLightInstance(origin));
		frontTiles.add(getFrontHightLightInstance(origin));
	
	}

	

	public CKAimAction(CKPosition originLocation, float minDistance,
			float maxDistance, CKSelectedPositionsListeners callback,
			Collection<CKPosition> offsets)
	{
		super(0,1000 );
		
		init(originLocation,minDistance,maxDistance,callback,SelectAreaType.NONE);
		this.offsets=offsets;
		
		for(CKPosition pos:offsets)
		{
			CKPosition newpos= pos.add(origin);
			rearTiles.add(getRearHightLightInstance(newpos));
			frontTiles.add(getFrontHightLightInstance(newpos));
		}
	}


	private void init(CKPosition originLocation, float minDistance,
			float maxDistance, CKSelectedPositionsListeners callback,SelectAreaType type)
	{

		origin = originLocation;//do not change this!
		min    = minDistance;
		max   = maxDistance;
		listener = callback;
		this.type = type;
		mouseP = new Point((int) origin.getX(),(int)origin.getY());
		presentPoint = new Point((int) origin.getX(),(int)origin.getY());
		
		/*
		presentPoint.x=(int)origin.getX();
		presentPoint.y=(int)origin.getY();
		*/
		
		if(afactory ==null)  { afactory = CKGraphicsAssetFactoryXML.getInstance(); }
		
		
	}
	

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.sceneAction.CKSceneAction#performAction(ckGraphicsEngine.CKGraphicsScene, int)
	 */
	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame)
	{
		//System.out.println("entering the Action");
		if(frame == getStartTime())
		{
			for(CKAssetInstance rear:rearTiles)
			{
				scene.addInstanceToLayer(rear, CKGraphicsLayer.REARHIGHLIGHT_LAYER);
			}
			for(CKAssetInstance front:frontTiles)
			{
				scene.addInstanceToLayer(front, CKGraphicsLayer.FRONTHIGHTLIGHT_LAYER);
			}
			storedScene = scene;
		}
		if(closing)
		{
			this.setEndTime(0);
			//need to remove the highlights. 
			storedScene.clearHighlights();
			CKGameObjectsFacade.getEngine().removeMouseListener(this);
		}
		this.endTime++; //keep it from closing
		CKCoordinateTranslator trans = scene.getTrans();
		Point p = trans.convertScreenToMap(mouseP);
		if(presentPoint.x!=p.x || presentPoint.y!=p.y)
		{
			//set visibility
			double distance =Math.sqrt((Math.pow(origin.getX()-p.x,2) + Math.pow(origin.getY()-p.y,2))); 
			if(distance<min || distance > max)   
				{ 
				//System.out.println("don't draw"+p+" "+origin);
					setVisible(false);  
					visible=false;
				}
			else	                                                            
			{  //need to draw stuff
				//System.out.println("I should draw!");
				setVisible(true);
				visible=true;
				moveFromTo(presentPoint,p);
				presentPoint = p;		
			}
		}
	}
	
	protected void moveFromTo(Point from,Point to)
	{
		int dx = to.x - from.x;
		int dy = to.y - from.y;
		for(CKAssetInstance rear:rearTiles) 	{ rear.moveBy(dx, dy); 	}
		for(CKAssetInstance front:frontTiles) { front.moveBy(dx,dy); }
	}
	
	protected void setVisible(boolean vis)
	{
		for(CKAssetInstance rear:rearTiles) { rear.setVisible(vis); }
		for(CKAssetInstance front:frontTiles) { front.setVisible(vis); }
	}
	



	private CKAssetInstance getAssetInstance(CKPosition pos,String assetID)
	{
		CKGraphicsAsset asset = afactory.getGraphicsAsset(assetID);
		
		if(asset instanceof CKSpriteAsset)
		{
			return new CKSpriteInstance((CKPosition) pos.clone(), (CKSpriteAsset)asset, -1);
		}
		else
		{
			return new CKAssetInstance((CKPosition) pos.clone(), asset, -1);
		}
	}		

	protected CKAssetInstance getRearHightLightInstance(CKPosition pos)
	{
		return getAssetInstance(pos,CKAimAction.rearHightlightID);
	}
	

	protected CKAssetInstance getFrontHightLightInstance(CKPosition pos)
	{
		return getAssetInstance(pos,CKAimAction.frontHightlightID);
	}



	@Override
	public void handleMouseClicked(MouseEvent e)
	{
		//TODO handle each side of click and have cancel option?
		System.out.println("Aim Action Mouse clicked"+e);
		if(! visible || closing) {return; }
		
		//		CKPosition [] array = new CKPosition[1];
		//	array[0]=
		
		CKPosition ret;
		if(offsets==null)
		{
			ret = new CKPosition(presentPoint.x,presentPoint.y,0,0);
		}
		else
		{
			CKPosition[]off = new CKPosition[offsets.size()];
			int i =0;
			for(CKPosition pos:offsets)
			{
				off[i]=pos.add(presentPoint);
				i++;
			}
			ret = new CKAreaPositions(presentPoint.x,presentPoint.y,0,0,off);
			
		}
		
		listener.NotifyOfTargets(ret);
		//stop the machine:)
		this.closing=true;
		/*
		this.setEndTime(0);
		//need to remove the highlights.
		//FIXME 
		storedScene.clearHighlights();
		CKGameObjectsFacade.getEngine().removeMouseListener(this);
		*/
		
	}



	@Override
	public void handleMouseMoved(MouseEvent e)
	{
		//System.out.println("Aim Action Mouse Moved"+e);

		mouseP= e.getPoint();		
	}
	
	
	
	
	
}
