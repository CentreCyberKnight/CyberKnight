package ckGraphicsEngine;



import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.scene.canvas.GraphicsContext;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKWorkSupervisorListener;
import ckCommonUtils.JoinedIterator;
import ckDatabase.CKGraphicsLayerFactoryXML;
import ckDatabase.CKGridFactory;
import ckEditor.CKScenePropertiesEditor;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckGameEngine.CKGrid;
//import ckGraphicsEngine.sceneAction.CKAddInstanceAction;
//import ckGraphicsEngine.sceneAction.CKMoveInstanceAction;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKCameraInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.layers.CKDynamicLayer;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckGraphicsEngine.layers.CKGridGraphicsLayer;
import ckGraphicsEngine.sceneAction.CKSceneAction;


public class CKGraphicsScene implements CKGraphicsSceneInterface, ImageObserver
{
	
	private Vector<CKGraphicsLayer> backgrounds;
	private Vector<CKGraphicsLayer> interactives;
	private Vector<CKGraphicsLayer> environments;
	
	private CKCoordinateTranslator trans;
	private CKCameraInstance camera;
	
	private LinkedList<CKSceneAction> actions = new LinkedList<CKSceneAction>();
	private ConcurrentLinkedQueue<LinkedList<CKSceneAction>> storedActions = new ConcurrentLinkedQueue<LinkedList<CKSceneAction>>();
	private boolean loading = false;
	//private String gridId;
	private CKGrid grid;
	
	
	
	
	public CKGraphicsScene(String id,String desc,String grid)
	  {
		  description =desc;
		  presentFrame=-1;
		  AID=id;
		 
		  backgrounds= new Vector<CKGraphicsLayer>();
		  interactives= new Vector<CKGraphicsLayer>();
		  environments= new Vector<CKGraphicsLayer>();
		  setGrid(grid);
	  } 

	
	public CKGraphicsScene(String id,String desc,CKGrid grid)
	  {
		  description =desc;
		  //FRAME_RATE=targetfps;
		  //setPreferredSize(D);
		  presentFrame=-1;
		  AID=id;
		 
		  backgrounds= new Vector<CKGraphicsLayer>();
		  interactives= new Vector<CKGraphicsLayer>();
		  environments= new Vector<CKGraphicsLayer>();
		  setGrid(grid);
	  } 

	public void setGrid(CKGrid grid)
	{
		//this.gridId = grid.getAID();
		this.grid=grid;
		
		trans = new CKDiamondTranslator(grid.getWidth(),grid.getHeight(),
				200,200);
		camera = new CKCameraInstance(new CKPosition(grid.getWidth()/2,
			 									  grid.getHeight()/2,0,0),
			                       trans); 
	 
		CKGraphicsLayer layer = new CKGridGraphicsLayer(grid);
		addLayer(layer);
	}
	
	public void setGrid(String gridId)
	{
		CKGrid grid = CKGridFactory.getInstance().getAsset(gridId,true);
		setGrid(grid);
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#loadActions(java.util.LinkedList)
	 */
	@Override
	public synchronized void loadActions(LinkedList<CKSceneAction> a)
	{
		storedActions.add(a);//since stored actions is a vector this action is safe
		loading=false;
		/*
		actions=a;
		for (CKSceneAction act:actions)
		{
			act.offsetTimes(presentFrame+15);
		}
		*/
	}
	  	  

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#calcState(ckGraphicsEngine.CKGraphicsLayer)
	 */

	public void calcState()
	{
		
		presentFrame++;
		boolean working = !workCompleted(); //store to see if I need to notify my boss
		 
		loadPendingActions();
		
		Iterator<CKSceneAction> iter=actions.iterator();
		while(iter.hasNext())
		{	
			CKSceneAction action = iter.next();
			//System.out.println("inspecting "+action.getStartTime()+" "+action.getEndTime());
			action.performAction(this, presentFrame);
			if(action.getEndTime()<=presentFrame)
			{
				iter.remove();
			}
		}
		//System.out.println("working?"+working+","+workCompleted()+":"+presentFrame);
		if(working && workCompleted())
		{
			if(boss != null)
			{
				boss.workCompleted(this);
			}
		}
		
	}

	private synchronized void loadPendingActions()
	{
		if(actions.isEmpty() && ! storedActions.isEmpty())
		{ 
			actions = storedActions.poll();	
			for (CKSceneAction act:actions)
			{
				act.offsetTimes(presentFrame);
			}
		}
	}
	
	
	
	public synchronized boolean workCompleted()
	{
		//System.out.println("work completed"+actions.size()+","+storedActions.size()+" bool:"+ (actions.isEmpty() && storedActions.isEmpty()) );
		return actions.isEmpty() && storedActions.isEmpty()&& ! loading;
		
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#getInteractiveLayerIter()
	 */
	@Override
	public Iterator<CKGraphicsLayer> getInteractiveLayerIter()
	{
		
		return interactives.iterator();
	}
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#resetLayers()
	 */
	@Override
	public void resetLayers()
	{
		Iterator<CKGraphicsLayer> iter=backgrounds.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			layer.reset();
		}
		
		iter=interactives.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			layer.reset();
		}
		
		iter=environments.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			layer.reset();
		}
		
	}
	
	final private void drawBackgrounds(Graphics g)
	{
		Iterator<CKGraphicsLayer> iter=backgrounds.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			layer.drawLayerToGraphics(g, presentFrame, this, trans);
			//println("drawing background "+layer.getDescription());
		}
	}

	

	
	final private void drawInteractives(Graphics g)
	{
		//println("drawing interactives"+interactives.size());
		int rowlen = trans.getMapRows();
		for(int row =0;row<rowlen;row++)
		{
			Iterator<CKGraphicsLayer>iter=interactives.iterator();
			while(iter.hasNext())
			{
				CKGraphicsLayer layer = iter.next();
				//System.out.println("drawing row"+row);
				layer.drawLayerRowToGraphics(g, presentFrame, row, this, trans);	
			}
		
		}	
	}
	
	final private void drawEnvironments(Graphics g)
	{
		Iterator<CKGraphicsLayer> iter=environments.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			layer.drawLayerToGraphics(g, presentFrame, this, trans);
		}
	}
	

	final private void drawBackgrounds(GraphicsContext g)
	{
		Iterator<CKGraphicsLayer> iter=backgrounds.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			layer.drawLayerToGraphics(g, presentFrame, this, trans);
			//println("drawing background "+layer.getDescription());
		}
	}
	
	final private void drawInteractives(GraphicsContext g)
	{
		//println("drawing interactives"+interactives.size());
		int rowlen = trans.getMapRows();
		for(int row =0;row<rowlen;row++)
		{
			Iterator<CKGraphicsLayer>iter=interactives.iterator();
			while(iter.hasNext())
			{
				CKGraphicsLayer layer = iter.next();
				//System.out.println("drawing row"+row);
				layer.drawLayerRowToGraphics(g, presentFrame, row, this, trans);	
			}
		
		}	
	}
	
	
	final private void drawEnvironments(GraphicsContext g)
	{
		Iterator<CKGraphicsLayer> iter=environments.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			layer.drawLayerToGraphics(g, presentFrame, this, trans);
		}
	}
	
	
	
	@SuppressWarnings("unused")
	private void drawRay(Graphics g, CKPosition p1,CKPosition p2,int stops)
	{
	double frac= 1.0/stops;
	CKPosition last=p1;
	for(int i=1;i<stops;i++)
	{
		CKPosition pos = CKPosition.interpolate(p1,p2, (float)frac*i);
		//need to trans to screen
		Point lastscreen = trans.convertMapToScreen(last);
		Point nextscreen = trans.convertMapToScreen(pos);
		//System.out.println("draw from "+lastscreen+" to "+nextscreen);
		g.drawLine((int) lastscreen.getX(),(int)lastscreen.getY(),
				(int)nextscreen.getX(),(int)nextscreen.getY());
		last=pos;
	}
		
		
		
		
	}
	
	
	

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#drawOffScreenBuffer(ckGraphicsEngine.CKGraphicsLayer)
	 */
	public void drawOffScreenBuffer(Graphics g,int width,int height)
	{
//		println("Drawing the off screen buffers");
		trans.setScreenDimensions(width,height);
		camera.recenterCamera();
		
		resetLayers();
		//draw backgrounds
		drawBackgrounds(g);
		//draw interactives
		drawInteractives(g);
		//draw environments
		drawEnvironments(g);
								
	}
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#drawOffScreenBuffer(ckGraphicsEngine.CKGraphicsLayer)
	 */
	public void drawOffScreenBuffer(GraphicsContext g,int width,int height)
	{
//		println("Drawing the off screen buffers");
		trans.setScreenDimensions(width,height);
		camera.recenterCamera();
		
		resetLayers();
		//draw backgrounds
		drawBackgrounds(g);
		//draw interactives
		drawInteractives(g);
		//draw environments
		drawEnvironments(g);
								
	}
	public CKCameraInstance getCamera()
	{
		return camera;
	}
	
	public void addLayer(String Lid)
	{
			addLayer(CKGraphicsLayerFactoryXML.getInstance().getGraphicsLayer(Lid));
	}
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#addLayer(ckGraphicsEngine.CKGraphicsLayer)
	 */
	@Override
	public void addLayer(CKGraphicsLayer L)
	{
		
		//first check if a layer of that depth is already present
		CKGraphicsLayer oldLayer = getLayerByDepth(L.getLayerDepth());
		if(oldLayer != null) {removeLayer(oldLayer);}
		//now add new layer
		
		if(L.getLayerDepth() < CKGraphicsLayer.BACKGROUND_BOUNDRY)
		{
			backgrounds.add(L);
			Collections.sort(backgrounds);			
		}
		else if (L.getLayerDepth()<CKGraphicsLayer.ENVIRNOMENT_BOUNDRY)
		{
			interactives.add(L);
			Collections.sort(interactives);	
		}
		else
		{
			environments.add(L);
			Collections.sort(environments);
		}
		
		Point minP = new Point();
		Point maxP= new Point();
		L.getLayerBounds(minP,maxP);
//		println("layer reports min"+minP+" and max"+maxP);
		trans.insureMapBounds(maxP);
		
	}
	
	private CKGraphicsLayer getLayerByDepth(int depth)
	{
		Vector<CKGraphicsLayer> list;
		
		if(depth < CKGraphicsLayer.BACKGROUND_BOUNDRY)
		{
			list = backgrounds;			
		}
		else if (depth <CKGraphicsLayer.ENVIRNOMENT_BOUNDRY)
		{
			list=interactives;
		}
		else
		{
			list=environments;
		}		
		//search through list
		Iterator<CKGraphicsLayer> iter=list.iterator();
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			if(layer.getLayerDepth()==depth)
				{
					return layer;
				}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#addAssetToLayer(ckCommonUtils.CKPosition, ckGraphicsEngine.CKGraphicsAsset, int)
	 */
	@Override
	public void addAssetToLayer(CKPosition pos, CKGraphicsAsset inst, int layerDepth)
	{
		CKGraphicsLayer layer= getLayerByDepth(layerDepth);
	//check if layer exists
		if(layer==null)
		{
			layer = new CKDynamicLayer(layerDepth);
			addLayer(layer);
		}
		//now add
		layer.addAsset(pos,inst);
		layer.reset();
	}
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#addInstanceToLayer(ckGraphicsEngine.CKAssetInstance, int)
	 */
	@Override
	public void addInstanceToLayer(CKAssetInstance inst, int layerDepth)
	{
		CKGraphicsLayer layer= getLayerByDepth(layerDepth);
	//check if layer exists
		if(layer==null)
		{
			layer = new CKDynamicLayer(layerDepth);
			addLayer(layer);
		}
		//now add
		layer.addInstance(inst);
		layer.reset();
	}
	
	
	
	private boolean removeInstanceFromLayerSet(Iterator<CKGraphicsLayer> iter,
												CKAssetInstance inst)
	{
		while(iter.hasNext())
		{	
			CKGraphicsLayer layer = iter.next();
			if( layer.removeInstance(inst))
			{
				return true;
			}
		}
		return false;
		
	}
	
	public boolean removeInstanceFromScene(CKAssetInstance inst)
	{
		//don't know what layer it is on, need to go through all of them.
		//most likely it is on interactives layer...
		
		if( removeInstanceFromLayerSet(interactives.iterator(),inst) )
		{
			return true;
		}
		else if( removeInstanceFromLayerSet(backgrounds.iterator(),inst) )
		{
			return true;
		}
		else if( removeInstanceFromLayerSet(environments.iterator(),inst) )
		{
			return true;
		}
		return false;
				
		
	}
	


	@Override
	public boolean imageUpdate(Image image, int flags, int x, int y, 
    int width, int height)
	{
		//used for the drawing commands...can add something here in the future
		
		
		// If the image has finished loading, repaint the window. 
        if ((flags & ALLBITS) != 0) 
        { 
            //can we get away with this?
        	//we should not need to repaint the image as it is already loaded
        	
        	
        	//repaint(); 
            return false;  // Return false to say we don't need further notification. 
        } 
        return true;       // Image has not finished loading, need further notification. 
   
	}
	
	
	public void removeLayer(CKGraphicsLayer layer)
	{
		@SuppressWarnings("unused") // just wanted the short circuiting to avoid unnecessary search
		boolean v = backgrounds.remove(layer) ||
		interactives.remove(layer)||
		environments.remove(layer);
		
		
	}


	
	@Override
	public Iterator<CKGraphicsLayer> layerIterator()
	{	
		return new JoinedIterator<CKGraphicsLayer>( (Iterator<CKGraphicsLayer>) backgrounds.iterator(),
				                                    (Iterator<CKGraphicsLayer>) interactives.iterator(),
				                                    (Iterator<CKGraphicsLayer>) environments.iterator());
	}


	@Override
	public void addSingleWorkSupervisorListener(
			CKWorkSupervisorListener<CKGraphicsScene> boss)
	{
		this.boss=boss;
		
	}


	@Override
	public void startLoading()
	{
		loading=true;		
	}


	@Override
	public void clearHighlights()
	{
		this.getLayerByDepth(CKGraphicsLayer.REARHIGHLIGHT_LAYER).removeAllInstances();
		this.getLayerByDepth(CKGraphicsLayer.FRONTHIGHLIGHT_LAYER).removeAllInstances();
		
	}


	public void setAID(String id)
	{
		AID = id;
	}

	
	String AID;
	/**
	 * @return the sID
	 */
	public String getAID()
	{
		return AID;
	}


	int presentFrame;
	String description;
	
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}


	CKWorkSupervisorListener<CKGraphicsScene> boss;
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKGraphicsSceneInterface#getTrans()
	 */
	@Override
	public CKCoordinateTranslator getTrans()
	{
		return trans;
	}


	
	

	//XML Database functions
		


	class LayerPersistenceDelegate extends DefaultPersistenceDelegate
	{
		
		protected Expression instantiate(Object oldInstance,  Encoder out)
		{
			CKGraphicsScene scene = (CKGraphicsScene) oldInstance;
			
			//Dimension M = new Dimension(scene.trans.getMapRows(),scene.trans.getMapColumns());
			//Dimension P = new Dimension(scene.trans.getScreenWidth(),scene.trans.getScreenHeight());

			
			return new Expression(oldInstance,oldInstance.getClass(),
					"new", new Object[]{ scene.AID,scene.description,grid.getAID()});
		}
		
	    @SuppressWarnings("rawtypes")
		protected void initialize(Class type, Object oldInstance,
	                              Object newInstance, Encoder out) 
	    {
	        super.initialize(type, oldInstance,  newInstance, out);

	        CKGraphicsScene scene = (CKGraphicsScene) oldInstance;

	        Iterator<CKGraphicsLayer> iter = scene.layerIterator();
	    
	        while(iter.hasNext())
	        {
	        	CKGraphicsLayer inst = iter.next();
	        	if(! (inst instanceof CKGridGraphicsLayer))
	        	{
	        		out.writeStatement(new java.beans.Statement(oldInstance,
	                              "addLayer", // Could also use "addElement" here.
	                              new Object[]{inst.getLid()}) );
	        	}
	        }
	    }
	}
	
	

	
	/**
	* Stores this object to an OutputString
	 * @throws IOException
	 */
	public void writeToStream(OutputStream out)
	{
			
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.setPersistenceDelegate(getClass(), new LayerPersistenceDelegate());
		e.writeObject(this);
		e.close();
		
	}
	

	public static CKGraphicsSceneInterface readFromStream(InputStream in)
	{
		XMLDecoder d = new XMLDecoder(in);
		CKGraphicsScene node = (CKGraphicsScene) d.readObject();
		d.close();
		return node;
		
	}


	@Override
	public CKGrid getGrid()
	{
		return grid;
	}


	
	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}


	@Override
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
		
		switch(v)
		{
		case STATIC:
			
		BufferedImage img = CKGraphicsPreviewGenerator.createScenePreview(this, 200,200);
		JComponent panel = new JPanel();
		panel.add(new JLabel(new ImageIcon(img)));
		return panel;
		//DONE STATIC
		
		case INTERACTIVE:
			return new CKSceneViewer(this,24);
			
		case EDITABLE:
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
			p.add(getXMLAssetViewer(ViewEnum.STATIC));
			
			CKXMLAssetPicker<CKGraphicsLayer> layerPicker = new CKXMLAssetPicker<CKGraphicsLayer>(layerIterator());
			layerPicker.addSelectedListener(new LayerRemoval(p));
			p.add(layerPicker);
			return p;
			
			
			
		}
		
		return null;
	}

	
	class LayerRemoval implements CKEntitySelectedListener<CKGraphicsLayer>
	{
		
		JPanel panel;
		
		public LayerRemoval(JPanel p)
		{
			panel=p;
		}
		
		@Override
		public void entitySelected(CKGraphicsLayer entity)
		{
			removeLayer(entity);
			panel.removeAll();
			
			//same as the other one...
			panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
			panel.add(getXMLAssetViewer(ViewEnum.STATIC));
		
			CKXMLAssetPicker<CKGraphicsLayer> layerPicker = new CKXMLAssetPicker<CKGraphicsLayer>(layerIterator());
			layerPicker.addSelectedListener(new LayerRemoval(panel));
			panel.add(layerPicker);
			
			panel.revalidate();
			
			
		}

	};
	
	

	@SuppressWarnings("unchecked")
	@Override
	public CKScenePropertiesEditor getXMLPropertiesEditor()
	{
		return new CKScenePropertiesEditor(this);
	}


	
	
	

	
	
	
	
	
	
	
}
