package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import javafx.scene.canvas.GraphicsContext;

public class CKFadeAsset extends CKGraphicsAsset
{

	private CKGraphicsAsset asset;
	private int startFade;
	private int endFade;
	private boolean FadeOut;
	


	
	
	public CKFadeAsset(CKGraphicsAsset asset, int startFade,int endFade)
	{
		super("assetFades", "wrappper to fade other assets");
		this.asset = asset;
		this.startFade = startFade;
		this.endFade=endFade;
		FadeOut=true;
	}
	
	public CKFadeAsset(CKGraphicsAsset asset, int startFade, int endFade, boolean FadeOut){
		super("assetFades", "wrappper to fade other assets");
		this.asset = asset;
		this.startFade = startFade;
		this.endFade=endFade;
		this.FadeOut=FadeOut;
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

		if(frame<startFade&&FadeOut)
		{
			asset.drawToGraphics(g, screenx, screeny, frame, row, observer);
		}
		else if (frame < endFade)
		{

			double alpha = g.getGlobalAlpha();
			
			double percent=0;
			if (FadeOut){
				percent = 1-(frame-startFade)/(double)(endFade-startFade); }
			else{
				percent=(frame-startFade)/(double)(endFade-startFade);
			}
			System.out.println(percent);
			g.setGlobalAlpha(percent);
			asset.drawToGraphics(g, screenx, screeny, frame, row, observer);
			g.setGlobalAlpha(alpha);
		}
		else{
			if(!FadeOut){
				
				asset.drawToGraphics(g, screenx, screeny, frame, row, observer);
			}
				
			}
			
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

}
