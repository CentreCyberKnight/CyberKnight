package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import ckDatabase.CKGraphicsAssetFactoryXML;
import javafx.scene.canvas.GraphicsContext;

/**
 * Shared Asset allows us to create an asset out of a single row of another asset.  This class is useful in creating composite assets
 * @author dragonlord
 *
 */
public class CKSharedAsset extends CKGraphicsAsset
{
	CKGraphicsAsset asset=CKNullAsset.getNullAsset();
	String assetID;
	int row;
	
	
	public CKSharedAsset()
	{
		super("","");
	}
	
	public CKSharedAsset(String aid,String desc,String assetName, int row)
	{
		this(aid,desc,
				CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetName),row);
	}
	protected CKSharedAsset(String aid,String desc,CKGraphicsAsset img, int row)
	{
		super(aid,desc);
		asset=img;
		assetID=img.getAID();
		this.row = row;
	}
	
	
	

	/**
	 * @return the assetID
	 */
	public String getAssetID()
	{
		return assetID;
	}

	/**
	 * @param assetID the assetID to set
	 */
	public void setAssetID(String assetID)
	{
		this.assetID = assetID;
		asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID);
	}

	/**
	 * @return the row
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	@Override
	public void drawToGraphics(Graphics g, int screenx, int screeny,
			int frame,int row, ImageObserver observer)
	{
		//ignores row parameter
		asset.drawToGraphics(g,screenx,screeny,frame,this.row,observer);
	}
	
	
	@Override
	public void drawToGraphics(GraphicsContext g, int screenx, int screeny,
			int frame,int row, ImageObserver observer)
	{
		//ignores row parameter
		asset.drawToGraphics(g,screenx,screeny,frame,this.row,observer);
	}



	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		asset.drawPreviewRowToGraphics(g, screenx, screeny,this.row, observer);
	}



	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		asset.drawPreviewRowToGraphics(g, screenx, screeny,this.row, observer);
	}



	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		asset.drawToGraphics(g,screenx,screeny,frame,row,observer);	
	}



	@Override
	public void drawPreviewToGraphics(GraphicsContext g, int screenx, int screeny,
			ImageObserver observer)
	{
		asset.drawPreviewRowToGraphics(g, screenx, screeny,this.row, observer);
	}



	@Override
	public void drawPreviewRowToGraphics(GraphicsContext g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		asset.drawPreviewRowToGraphics(g, screenx, screeny,this.row, observer);
	}



	@Override
	public void drawPreviewFrameToGraphics(GraphicsContext g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		asset.drawToGraphics(g,screenx,screeny,frame,row,observer);	
	}



	@Override
	public int getFrames(int row)
	{
		return asset.getFrames(row);
	}



	@Override
	public int getRows()
	{
		return asset.getRows();
	}



	@Override
	public int getHeight(int row)
	{
		return asset.getHeight(row);
	}



	@Override
	public int getWidth(int row)
	{
		return asset.getWidth(row);
	}

	
	
	
	
	

	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		asset.getDrawBounds(frame, row, off, bounds);
		
	}

	
	
	
	
	
}
