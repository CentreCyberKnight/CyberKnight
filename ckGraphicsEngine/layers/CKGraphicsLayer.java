package ckGraphicsEngine.layers;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;

abstract public class CKGraphicsLayer 
		implements Comparable<CKGraphicsLayer>,CKXMLAsset<CKGraphicsLayer>
{
	/*constants*/
	public static int BACKGROUND1_LAYER = 0;
	public static int BACKGROUND2_LAYER = 1000;
	
	public static int BACKGROUND_BOUNDRY=2000;
	
	public static int GROUND_LAYER = 3000;
	public static int REARHIGHLIGHT_LAYER = 4000;
	public static int GROUNDCOVER_LAYER = 5000;
	public static int SPRITE_LAYER = 6000;
	public static int FRONTHIGHLIGHT_LAYER = 7000;
	
	public static int ENVIRNOMENT_BOUNDRY = 8000;
	
	public static int ENVIRONMENT1_LAYER = 9000;
	public static int ENVIRONMENT2_LAYER = 10000;
	
	
	
	
	
	
	/* variables*/
	
	private String description;
	private int layerDepth;
	private String lid;
	/**
	 * @param layerDepth the layerDepth to set
	 */
	public void setLayerDepth(int layerDepth)
	{
		this.layerDepth = layerDepth;
	}

	/**
	 * @return the lid
	 */
	public String getLid()
	{
		return lid;
	}

	/**
	 * @param lid the lid to set
	 */
	public void setLid(String lid)
	{
		this.lid = lid;
	}

	volatile boolean visible;
	
	
	public CKGraphicsLayer(int d,String id, String desc)
	{
		layerDepth=d;
		description = desc;
		setVisible(false);
		lid=id;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean vis)
	{
		visible = vis;
	}
	
	public int getLayerDepth()
	{
		return layerDepth;
	}
	
	public void reset()
	{
		
	}
	
	abstract public void drawLayerToGraphics (Graphics g,int frame,
			ImageObserver observer,CKCoordinateTranslator translator);
	
	abstract public void drawLayerRowToGraphics (Graphics g,int frame, int y,
			ImageObserver observer,CKCoordinateTranslator translator);
	
	
	abstract public void drawLayerTileToGraphics (Graphics g,int frame, 
			int x,int y,
			ImageObserver observer,CKCoordinateTranslator translator);

	
	abstract public void drawLayerToGraphics (GraphicsContext g,int frame,
			ImageObserver observer,CKCoordinateTranslator translator);
	
	abstract public void drawLayerRowToGraphics (GraphicsContext g,int frame, int y,
			ImageObserver observer,CKCoordinateTranslator translator);
	
	
	abstract public void drawLayerTileToGraphics (GraphicsContext g,int frame, 
			int x,int y,
			ImageObserver observer,CKCoordinateTranslator translator);

	
	abstract public void changeHeight(int x,int y, double heightDiff);
	
	@Override
	public int compareTo(CKGraphicsLayer that)
	{
		if(layerDepth==that.layerDepth) { return 0; }
		else if(layerDepth<that.layerDepth){return -1; }
		else {return 1; }
	}

	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String desc)
	{
		description = desc;
	}
	
	
	/**
	 * Sets the parameters to the minimum Map Boundry and the maximum map Boundry.
	 * @param MinPoint will be set to the minimum boundry point.
	 * @param MaxPoint will be set to the maximum boundry point.
	 * @return false if there is nothing interesting in this layer
	 */
	abstract public boolean getLayerBounds(Point minPoint, Point maxPoint); 
	
	public void addAsset(CKPosition pos,String assetID)
	{
		addAsset(pos,CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID));
	}
	abstract public void addAsset(CKPosition pos,CKGraphicsAsset t);
	abstract public void addInstance(CKAssetInstance t);
	abstract public boolean removeInstance(CKAssetInstance t);
	abstract public void removeAllInstances();
	abstract public Iterator<CKAssetInstance> iterator();
	

	//XML Database functions
		


	class LayerPersistenceDelegate extends DefaultPersistenceDelegate
	{
	    protected void initialize(Class<?> type, Object oldInstance,
	                              Object newInstance, Encoder out) 
	    {
	        super.initialize(type, oldInstance,  newInstance, out);

	        CKGraphicsLayer layer = (CKGraphicsLayer) oldInstance;

	        Iterator<CKAssetInstance> iter = layer.iterator();
	        while(iter.hasNext())
	        {
	        	CKAssetInstance inst = iter.next();
	            out.writeStatement(new java.beans.Statement(oldInstance,
	                              "addAsset", // Could also use "addElement" here.
	                              new Object[]{inst.getPosition(),inst.getAsset().getAID()}) );
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
	

	public static CKGraphicsLayer readFromStream(InputStream in)
	{
		XMLDecoder d = new XMLDecoder(in);
		CKGraphicsLayer node = (CKGraphicsLayer) d.readObject();
		d.close();
		return node;
		
	}

	@Override
	public String getAID()
	{
		return getLid();
	}

	@Override
	public void setAID(String a)
	{
		setLid(a);
		
	}

	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}

	@Override
	public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
	{
		switch(v)
		{
		case STATIC:
		case INTERACTIVE:
		case EDITABLE:
			
			BufferedImage img = CKGraphicsPreviewGenerator.createLayerPreview(this, 200, 200);
			JComponent panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(new JLabel(new ImageIcon(img)));
			panel.add(new JLabel(getDescription(),JLabel.CENTER)
								,BorderLayout.NORTH);
			return panel;
		//DONE STATIC
			
		}
		
		return null;
		
	}

	@Override
	public <B extends CKXMLAsset<B>> CKXMLAssetPropertiesEditor<B> getXMLPropertiesEditor()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
		
}
