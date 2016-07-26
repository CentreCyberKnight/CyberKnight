package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Shadow;
import ckDatabase.CKGraphicsAssetFactoryXML;

public class CKShadowAsset extends CKGraphicsAsset
{

	private CKGraphicsAsset asset;
	private String assetID;
	
	public CKShadowAsset(String aid, String assetID)
	{
		this(aid,CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID));
		
	}

	public CKShadowAsset(String aid,CKGraphicsAsset asset)
	{
		super("assetShadow"+asset.getAID(), "shadow of"+asset.getDescription());
	
		this.asset = asset;
		this.assetID = asset.getAID();
		
	}

	/**
	 * @return the asset
	 */
	public CKGraphicsAsset getAsset()
	{
		return asset;
	}

	/**
	 * @param asset the asset to set
	 */
	public void setAsset(CKGraphicsAsset asset)
	{
		this.asset = asset;
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
	}

	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawPreviewToGraphics(GraphicsContext g, int screenx,
			int screeny, ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawToGraphics(Graphics g, int screenx, int screeny, int frame,
			int row, ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawToGraphics(GraphicsContext g, int screenx, int screeny,
			int frame, int row, ImageObserver observer)
	{
		
		g.setEffect(new Shadow());
		asset.drawToGraphics(g, screenx,screeny,frame,row,observer);
		g.setEffect(null);
		
		

	}

	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawPreviewRowToGraphics(GraphicsContext g, int screenx,
			int screeny, int row, ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawPreviewFrameToGraphics(GraphicsContext g, int screenx,
			int screeny, int frame, ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getFrames(int row)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRows()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight(int row)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth(int row)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		// TODO Auto-generated method stub

	}

}
