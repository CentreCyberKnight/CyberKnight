package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ckCommonUtils.CKURL;
import ckDatabase.XMLDirectories;
import ckGraphicsEngine.UnknownAnimationError;
import javafx.scene.canvas.GraphicsContext;

/**
 * CKGraphicsAsset is a class that encapsulated the drawing of data to a graphics context. Most of these classes ustilize a 
 * decorator pattern to provide sharing, extra functionality, or composite effects on the baseline behavior.
 * 
 * Assets use the idea of rows and frames.  Rows indicate a collection of related images that form an animation.
 * Frames are the individual frames of an animation.  By default all assets should have a minimum of 1 row and 1 frame.
 * 
 * Assets also contain the logic to read and write themselves to a database.
 * 
 * @author dragonlord
 *
 */
public abstract class CKGraphicsAsset 
{
	
	private String AID;
	private String description;
	private boolean dirty;
	
	public static String getUniqueAssetName()
	{
		try
		{
			String path = new CKURL(XMLDirectories.GRAPHIC_ASSET_DIR).getURL().getFile();
			File uniqueFile = File.createTempFile("asset", ".xml", new File(path));
			return uniqueFile.getName();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OOPS";

	}
	
	protected CKGraphicsAsset(String aid,String description)
	{
		this.AID = aid;
		this.description=description;
		dirty=false;
	}
	
	protected CKGraphicsAsset(String description)
	{
		this("",description);	
	}
	
	
	
	/**
	 * Returns the Asset id for this object.
	 * Asset Id's must be unique among other assets
	 *  of the same subclass.
	 */
	final public String getAID() { return AID; };
	/** Only allows subclasses to set asset ids.
	 * @param a - new aid of the asset
	 */
	final public void setAID(String a) { AID = a; }
	
	/**
	 * @return the string description of the asset
	 */
	final public String getDescription() {return description;}
	
	/**
	 * @param newDescription the new description of the asset
	 */
	final public void setDescription(String newDescription) {description = newDescription;}
	
	/*Draws all of the asset to the graphics at screenx,screeny*/
	
	/** Draws a preview of the asset to graphics, this should be used for debugging and building purposes
	 * @param g              graphics to draw the asset to
	 * @param screenx        x coordinate to start drawing at
	 * @param screeny        y coordinate to start drawing at
	 * @param observer       An observer to detect when the image is drawn.
	 */
	abstract public void drawPreviewToGraphics(Graphics g,int screenx,int screeny,ImageObserver observer);
	
	abstract public void drawPreviewToGraphics(GraphicsContext g,int screenx,int screeny,ImageObserver observer);
	
	/**Draws the asset to the graphics.  This is for the actual game
	 * @param g              graphics to draw the asset to
	 * @param screenx        x coordinate to start drawing at
	 * @param screeny        y coordinate to start drawing at
	 * @param observer       An observer to detect when the image is drawn.
	 * @param frame          Which Frame of the asset should be drawn to the graphics
	 * @param row            Which Row of the asset should be drawn to the graphics
	 * @param observer       An observer to detect when the image is drawn
	 */
	abstract public void drawToGraphics (Graphics g,int screenx,int screeny,
            int frame,int row,ImageObserver observer);
	
	
	/**Draws the asset to the graphics.  This is for the actual game
	 * @param g              graphics to draw the asset to
	 * @param screenx        x coordinate to start drawing at
	 * @param screeny        y coordinate to start drawing at
	 * @param observer       An observer to detect when the image is drawn.
	 * @param frame          Which Frame of the asset should be drawn to the graphics
	 * @param row            Which Row of the asset should be drawn to the graphics
	 * @param observer       An observer to detect when the image is drawn
	 */
	abstract public void drawToGraphics (GraphicsContext g,int screenx,int screeny,
            int frame,int row,ImageObserver observer);


	/**Draws a preview of the asset's Row to graphics, this should be used for debugging and building purposes
	 * @param g              graphics to draw the asset to
	 * @param screenx        x coordinate to start drawing at
	 * @param screeny        y coordinate to start drawing at
	 * @param observer       An observer to detect when the image is drawn.
	 * @param row            Which Row of the asset should be drawn to the graphics
	 * @param observer       An observer to detect when the image is drawn
	 */
	abstract public void drawPreviewRowToGraphics (Graphics g,int screenx,int screeny,
            int row,ImageObserver observer);
	abstract public void drawPreviewRowToGraphics (GraphicsContext g,int screenx,int screeny,
            int row,ImageObserver observer);
	

	/**Draws a preview of the asset's Row to graphics, this should be used for debugging and building purposes
	 * @param g              graphics to draw the asset to
	 * @param screenx        x coordinate to start drawing at
	 * @param screeny        y coordinate to start drawing at
	 * @param observer       An observer to detect when the image is drawn.
	 * @param row            Which Row of the asset should be drawn to the graphics
	 * @param observer       An observer to detect when the image is drawn
	 */
	abstract public void drawPreviewFrameToGraphics (Graphics g,int screenx,int screeny,
            int frame,ImageObserver observer);
	abstract public void drawPreviewFrameToGraphics (GraphicsContext g,int screenx,int screeny,
            int frame,ImageObserver observer);
	
	
	
	/**Gets the animation length of the asset as described be the string animation.  Some assets may ignore the input.
	 * @param animation A string that is used to descrine a row
	 * @return
	 * @throws UnknownAnimationError
	 */
	//FIXME should this only go into sprites?
	public int getAnimationLength(String animation) throws UnknownAnimationError { return getFrames(0);} 
	
	
	abstract public int getFrames(int row);
	abstract public int getRows();
	abstract public int getHeight(int row);
	abstract public int getWidth(int row);
	
	public boolean isDirty() { return dirty; }
	protected void setClean() { dirty = false;}
	protected void setDirty() { dirty=true;}
	
	/**Calculates the bounds of the drawn asset if it was asked to draw at point 0,0
	 * @param frame frame of asset to measure
	 * @param row   row of asset to measure
	 * @param off   starting offset of the asset.
	 * @param bounds ending offsets of the asset
	 */
	abstract public void getDrawBounds(int frame,int row,Point off,Point bounds);
	
	
	
	
	
	/**
	* Stores this object to an OutputString
	 * @throws IOException
	 */
	public void writeToStream(OutputStream out)
	{
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.writeObject(this);
		e.close();
		
	}
	
	
	public static CKGraphicsAsset readFromStream(InputStream in)
	{
		XMLDecoder d = new XMLDecoder(in);
		CKGraphicsAsset node = (CKGraphicsAsset) d.readObject();
		d.close();
		return node;
		
	}
	
	
	
	
}