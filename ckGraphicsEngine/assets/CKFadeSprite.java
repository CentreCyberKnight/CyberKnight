package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;

import ckGraphicsEngine.UnknownAnimationError;
import ckGraphicsEngine.assets.CKSpriteAsset.SpriteIterator;
import ckGraphicsEngine.assets.CKSpriteAsset.SpriteNode;
import javafx.scene.canvas.GraphicsContext;

//actually a CKTransparentSprite

public class CKFadeSprite extends CKSpriteAsset implements TransAsset{
	protected CKSpriteAsset sprite;
	private double percent;
	
	public CKFadeSprite(CKGraphicsAsset asset){
		super("assetFades", "wrappper to fade other assets");
		this.sprite = (CKSpriteAsset) asset;

		//this.percent=percent;
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
		//TODO AUTO-GEN
	
	
	}

	@Override
	public void drawToGraphics(GraphicsContext g, int screenx, int screeny,
			int frame, int row, ImageObserver observer)
	{

			double alpha = g.getGlobalAlpha();
			g.setGlobalAlpha(percent);
			sprite.drawToGraphics(g, screenx, screeny, frame, row, observer);
			g.setGlobalAlpha(alpha);
		
			
		}
			
			
	public void setPercent(double percent){
		this.percent=percent;
	}
		
	

	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		//TODO

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
		// FIXME UNSUPPORTED

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
	
	@Override
	public void addAnimation(String animation,CKGraphicsAsset aset)
	{
		sprite.addAnimation(animation,aset);
	}
	
	@Override
	public void add(SpriteNode n)
	{
		sprite.add(n);
	}
	
	@Override
	public ArrayList<SpriteNode> getList()
	{
		return sprite.getList();
	}
	
	@Override
	public void setList(ArrayList<SpriteNode> list)
	{
		this.list = list;
	}

	@Override
	public int getRowIndex(String animation) throws UnknownAnimationError 
	{
		return sprite.getRowIndex(animation);
	}
	
	@Override
	public int getAnimationLength(String animation) throws UnknownAnimationError
	{
		
		return sprite.getAnimationLength(animation);	
	}
	




	
	@Override
	public int getFrames(String animation) throws UnknownAnimationError
	{
		return sprite.getFrames(animation);
		
	}
	
	
	

		


	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKCompositeAsset#iterator()
	 */
	@Override
	public Iterator<CKGraphicsAsset> iterator()
	{
		return new SpriteIterator(list.iterator());
	}



	@Override
	public void addAsset(CKGraphicsAsset asset)
	{
		
		addAnimation("Please Name Me"+unique++,asset);
		
	}



	@Override
	public void removeAsset(CKGraphicsAsset asset)
	{
		sprite.removeAsset(asset);
	}

		
	@Override
	public boolean supportsNaming() { return sprite.supportsNaming(); }
	
	
	@Override
	public String getAssetName(CKGraphicsAsset asset)
	{
		return sprite.getAssetName(asset);
	}

	
	@Override
	public void renameAsset(CKGraphicsAsset asset,String newName)
	{
		sprite.renameAsset(asset, newName);
	}
	
	
}

	
	
	
