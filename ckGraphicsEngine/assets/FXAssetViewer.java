package ckGraphicsEngine.assets;



import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.CKGamePanelTimer;
import ckGraphicsEngine.CKGraphicsConstants;
import ckGraphicsEngine.FXGamePanelTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FXAssetViewer extends FXGamePanelTimer
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4936612248019644031L;
	CKGraphicsAsset asset;
	int presentRow;
	int presentFrame;
	boolean extendedFormat;
	
	  public FXAssetViewer(double targetfps,CKGraphicsAsset a,
			  Dimension D)
	  {
		  this(targetfps,a,D,true);
	  } 


		
	  public FXAssetViewer(double targetfps,CKGraphicsAsset a,
			  Dimension D, boolean extended)
	  {
		  super(targetfps,5);
		  asset=a;
		
		  if(extended && D!=null)
		  {
			  //setPreferredSize(D);
			  setWidth(D.getWidth());
			  setHeight(D.getHeight());
		  }
		  else
		  {
			  Point offset = new Point();
			  Point bound = new Point();
			  asset.getDrawBounds(0, 0, offset, bound);
			  
			  //System.out.println("offset is "+offset.y+" bound is"+bound.y);
			  setWidth(200);
//			  setPrefSize(200,
					  /*Math.max(asset.getHeight(0)+50+CKGraphicsConstants.BASE_HEIGHT,
					  125)));
					  */
			  setHeight(Math.max(bound.y-offset.y+50 +CKGraphicsConstants.BASE_HEIGHT,
							  125));
		  }
		  presentRow=0;
		  presentFrame=0;
		  extendedFormat = extended;
	  } 


	  	  
	@Override
	public void calcState()
	{
		//update for next frame
		presentFrame++;
		if(presentFrame>=asset.getFrames(presentRow))
		{
			presentFrame=0;
			presentRow++;
			if(presentRow>=asset.getRows())
			{
				presentRow=0;
			}
		}

	
	}

	@Override
	public void drawOffScreenBuffer(GraphicsContext g,double screenWidth,double screenHeight)
	{
		//could draw the stats up..
		int leftMargin = 25;
		int topMargin = 25;
		//g.setColor(Color.WHITE);
		//g.draw3DRect(leftMargin, topMargin, asset.getWidth(presentRow)*20
		//		asset.getHeight(presentRow)*2,true);

		Point offset = new Point();
		Point bounds = new Point();
		asset.getDrawBounds(presentFrame, presentRow, offset, bounds);
		
		asset.drawToGraphics(g,leftMargin -offset.x, 
				topMargin-offset.y +CKGraphicsConstants.BASE_HEIGHT/2,
				presentFrame,presentRow, null);//this);
		
		int width = Math.max(asset.getWidth(presentRow),CKGraphicsConstants.BASE_WIDTH);
		int height = asset.getHeight(presentRow)+CKGraphicsConstants.BASE_HEIGHT/2;
		//int width = Math.max(asset.getWidth(presentRow),CKGraphicsConstants.BASE_WIDTH);
		

		
		
		g.setFill(Color.WHITE);
		g.fillText("Description  "+asset.getDescription(), leftMargin*2 +width, topMargin);
		g.fillText("Present Frame"+ presentFrame, leftMargin*2 +width, topMargin+25);
		g.fillText("Present Row  "+ presentRow, leftMargin*2 +width, topMargin+50);
		g.fillText("Width/Height  "+ asset.getWidth(presentRow)+" / "+
				asset.getHeight(presentRow), leftMargin*2 +width, topMargin+75);
		
		//g.drawString("panel height "+ 
		//		Math.max(asset.getHeight(0)+50+CKGraphicsConstants.BASE_HEIGHT/2,
		//				  125),leftMargin*2 +width, topMargin+100);
		

		
		if(extendedFormat)
		{

			asset.drawPreviewToGraphics(g,width+50,Math.max(125,height*3),null);//this);
		}
		
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
	 * @return the presentRow
	 */
	public int getPresentRow()
	{
		return presentRow;
	}



	/**
	 * @param presentRow the presentRow to set
	 */
	public void setPresentRow(int presentRow)
	{
		this.presentRow = presentRow;
	}



	/**
	 * @return the presentFrame
	 */
	public int getAssetFrame()
	{
		return presentFrame;
	}



	/**
	 * @param presentFrame the presentFrame to set
	 */
	public void setAssetFrame(int presentFrame)
	{
		this.presentFrame = presentFrame;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
/*		JFrame frame = new JFrame();
		CKGraphicsAsset water=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("heroSprite");		
		FXAssetViewer view=new FXAssetViewer(1,water,null,true);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
*/
	}

}