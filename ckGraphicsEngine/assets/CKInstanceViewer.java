package ckGraphicsEngine.assets;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ckCommonUtils.CKPosition;
import ckDatabase.XMLDirectories;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.CKGamePanelTimer;
import ckGraphicsEngine.CKRectangularTranslator;

public class CKInstanceViewer extends CKGamePanelTimer
{


	private static final long serialVersionUID = -7525597334015307021L;
	CKAssetInstance instance;
	CKCoordinateTranslator trans;
	int presentRow;
	int presentFrame;
	
	  public CKInstanceViewer(double targetfps,CKAssetInstance a,
			  Dimension D)
	  {
		  super(targetfps,5);
		  instance=a;
		  setPreferredSize(D);
		  presentRow=0;
		  presentFrame=0;
		  trans = new CKRectangularTranslator(1,1,200,200);
	  } 


	  	  
	@Override
	public void calcState()
	{
		//do nothing..
		//System.out.println("calc state was called");

	}

	@Override
	public void drawOffScreenBuffer(Graphics g,int screenWidth,int screenHeight)
	{
		//change the map coords..
		//TODO - trans is not working...
		trans.setWorldOffset(getWidth()/2,getHeight()/2);
		
		
		//could draw the stats up..
		instance.drawToGraphics(g,presentFrame,presentRow,this,trans);
		
		
		g.setColor(Color.WHITE);
		g.drawString("Present Frame "+ presentFrame, getWidth()-125, 25);
		g.drawString("Present Row  "+ presentRow, getWidth()-125, 50);

		
		//update for next frame
		presentFrame++;
		if(presentFrame>=instance.getFrames(presentRow))
		{
			presentFrame=0;
			presentRow++;
			if(presentRow>=instance.getRows())
			{
				presentRow=0;
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	JFrame frame = new JFrame();
	//create CKImageAsset
	CKImageAsset water=null;
	CKAssetInstance sprite = null;
		water = new CKImageAsset("","person",32,64,6,4,TileType.BASE,
		  XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"sprites_map_claudius.png");
		sprite=new CKAssetInstance(new CKPosition(0,0,0,0), water,0);
	
	
	/* Container c = frame.getContentPane();*/    
	CKInstanceViewer view=new CKInstanceViewer(1,sprite,new Dimension(256,256));
	//test.setPreferredSize(new Dimension(500,500));
	//test.setVisible(true);
	frame.add(view);
	frame.pack();
	frame.setVisible(true);
	//Thread animator = new Thread(test);
	//animator.start();
	   frame.addWindowListener(new WindowAdapter(){
           public void windowClosing(WindowEvent e)
           	{
               	System.exit(0);
           	}
       	}
);

	}

}
