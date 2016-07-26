package ckGraphicsEngine.assets;

import static ckGraphicsEngine.CKGraphicsConstants.FRAME_RATE;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import javafx.scene.canvas.GraphicsContext;
import ckDatabase.CKGraphicsAssetFactoryXML;

/**
 * This is a decorator class to insure that the asset is played at 
 * a maximum frame rate.
 * @author Michael K. Bradshaw
 *
 */
public class CKRegulatedAsset extends CKGraphicsAsset
{
	CKGraphicsAsset asset=CKNullAsset.getNullAsset();
	String assetID="";
	int maxRate=1;
	int adjust=1;
	
	/**
	 *  Default Constructor forXMLEncode
	 */
	public CKRegulatedAsset()
	{
		super("","");
	}
	
	public CKRegulatedAsset(String aID,String desc,String assetName, int maxRate)
	{
		this(aID,desc,
				CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetName),maxRate);
	}
	
	public CKRegulatedAsset(String aID,String desc,CKGraphicsAsset img, int maxRate)
	{
		super(aID,desc);
		asset=img;
		assetID = asset.getAID();
		setMaxRate(maxRate);
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
		this.asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID);
	}

	/**
	 * @return the maxRate
	 */
	public int getMaxRate()
	{
		return maxRate;
	}

	/**
	 * @param maxRate the maxRate to set
	 */
	public void setMaxRate(int maxRate)
	{
		this.maxRate=maxRate;
		
		if(maxRate>0 && maxRate<FRAME_RATE)
		{
		
			this.adjust=(int) (FRAME_RATE/maxRate);
		}
		else
		{
			this.adjust=1;
		}

	}

	private int recalcFrame(int f)
	{
		return f/adjust;
		
	}
	
	
	@Override
	public void drawToGraphics(Graphics g, int screenx, int screeny,
			int frame,int row, ImageObserver observer)
	{
		frame=recalcFrame(frame);
		asset.drawToGraphics(g,screenx,screeny,frame,row,observer);
	}

	
	@Override
	public void drawToGraphics(GraphicsContext g, int screenx, int screeny,
			int frame,int row, ImageObserver observer)
	{
		frame=recalcFrame(frame);
		asset.drawToGraphics(g,screenx,screeny,frame,row,observer);
	}



	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		
		asset.drawPreviewToGraphics(g, screenx, screeny,observer);
	}



	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		asset.drawPreviewRowToGraphics(g, screenx, screeny,row, observer);
	}



	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		frame=recalcFrame(frame);
		asset.drawPreviewFrameToGraphics(g,screenx,screeny,frame,observer);	
	}

	@Override
	public void drawPreviewToGraphics(GraphicsContext g, int screenx, int screeny,
			ImageObserver observer)
	{
		
		asset.drawPreviewToGraphics(g, screenx, screeny,observer);
	}



	@Override
	public void drawPreviewRowToGraphics(GraphicsContext g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		asset.drawPreviewRowToGraphics(g, screenx, screeny,row, observer);
	}



	@Override
	public void drawPreviewFrameToGraphics(GraphicsContext g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		frame=recalcFrame(frame);
		asset.drawPreviewFrameToGraphics(g,screenx,screeny,frame,observer);	
	}



	@Override
	public int getFrames(int row)
	{
		return asset.getFrames(row) * adjust;
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
