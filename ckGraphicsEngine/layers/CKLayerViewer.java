package ckGraphicsEngine.layers;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JFrame;

import ckCommonUtils.CKPosition;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.CKDiamondTranslator;
import ckGraphicsEngine.CKGamePanelTimer;
import ckGraphicsEngine.assets.CKImageAsset;

public class CKLayerViewer extends CKGamePanelTimer
{


	private static final long serialVersionUID = -7525597334015307021L;
	
	
	CKGraphicsLayer layer;
	CKCoordinateTranslator trans;
	int presentRow;
	int presentFrame;
	
	  public CKLayerViewer(double targetfps,CKGraphicsLayer a,
			  Dimension D,Dimension layerD)
	  {
		  super(targetfps,5);
		  layer=a;
		  setPreferredSize(D);
		  presentRow=0;
		  presentFrame=0;
		  trans = new CKDiamondTranslator(layerD.width,layerD.height,
					D.width,D.height);
	//	  trans = new CKDiamondTranslator(layerD.width,layerD.height,
	//				D.width,D.height);
	  } 

	  public CKLayerViewer(double targetfps,CKGraphicsLayer a,
			  Dimension D)
	  {
		  super(targetfps,5);
		  layer=a;
		  setPreferredSize(D);
		  presentRow=0;
		  presentFrame=0;
		  Point minP=new Point();
		  Point maxP=new Point();
		  layer.getLayerBounds(minP, maxP);
		  
		  trans = new CKDiamondTranslator(maxP.x,maxP.y,
					D.width,D.height);
	//	  trans = new CKDiamondTranslator(layerD.width,layerD.height,
	//				D.width,D.height);
	  } 


	  	  
	@Override
	public void calcState()
	{
		//do nothing..
		//System.out.println("calc state was called");

	}

	@Override
	public void drawOffScreenBuffer(Graphics g, int width,int height)
	{
		//change the map coords..
		
		trans.setScreenDimensions(width,height);
		
		//could draw the stats up..
		layer.drawLayerToGraphics (g,presentFrame,this,trans);
				
		
		g.setColor(Color.WHITE);
		g.drawString("Layer Name "+layer.getDescription(), width-125, 25);
		g.drawString("Present Frame "+ presentFrame, width-125, 50);
		g.drawString("Present Row  "+ presentRow, width-125, 75);

		presentFrame++;
						
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	JFrame frame = new JFrame();
	//create CKImageAsset
	CKImageAsset water=null;
	CKImageAsset grass=null;
	//CKAssetInstance sprite = null;
	/*
	try
	{
		water = CKImageAsset.readImage("water",64,32,4,1,TileType.BASE,
		  "images/animatedWater_baseTile.png");
		
		grass = CKImageAsset.readImage("grass",64,32,1,1,TileType.BASE,
		  "images/grass_baseTile.png");
		
		
		
	} catch (IOException e1)
	{
		
		e1.printStackTrace();
		return;
	}*/
	
	int cols = 40;
	int rows = 40;
	CKStaticMatrixLayer layer=
		new CKStaticMatrixLayer("row"," ",cols,rows,CKGraphicsLayer.GROUND_LAYER);
	
	
	for (int i=0;i<cols;i++)
	{
		for(int j=0;j<rows;j++)
		{
			CKPosition pos=new CKPosition(i,j,0,0);
			layer.addAsset(pos,water);		
		}
	}
	for(int i=0;i<rows;i++)
	{
		CKPosition pos=new CKPosition(i,0,0,0);
		layer.addAsset(pos,grass);		
	}
	
	CKLayerViewer view=new CKLayerViewer(1,layer,new Dimension(256,256),new Dimension(20,20));
	frame.add(view);
	frame.pack();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
