package ckGraphicsEngine;

//TODO rename this to Scene viewer...
import static ckGraphicsEngine.CKGraphicsConstants.FRAME_RATE;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

import ckDatabase.CKSceneFactory;
import ckGameEngine.CKGrid;

public class CKSceneViewer extends CKGamePanelTimer 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5776082859671061634L;
	CKGraphicsSceneInterface scene;
	

	public CKSceneViewer(String desc,double targetfps,Dimension D,Dimension layerD)
	  {
		  super(targetfps,5);
		  scene = new CKGraphicsScene("",desc,new CKGrid(layerD.width,layerD.height));
		  FRAME_RATE=targetfps;
		  setPreferredSize(D);
	} 

	
	//TODO some issues here
	public CKSceneViewer(CKGraphicsSceneInterface s,double targetfps)
	  {
		  super(targetfps,5);
		  scene =  s;
		  FRAME_RATE=targetfps;
		  setPreferredSize(new Dimension(500,500));
		  //setFrameRate
		  //no-helpthis.setMinimumSize(new Dimension(500,500));
		  
		  /*setLayout(new BorderLayout());
		  JButton b = new JButton("Here we go");
		  add(b);
		  b.setVisible(false);*/
		  
	} 
	
	  	  
	@Override
	public void calcState()
	{
		scene.calcState();	
	}
	

	@Override
	public void drawOffScreenBuffer(Graphics g,int width,int height)
	{
		//System.out.println("drawing: and visible"+isVisible()+" and showing"+isShowing());
		scene.drawOffScreenBuffer(g, width,height);							
	}

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
	
		CKGraphicsSceneInterface scene = CKSceneFactory.getInstance().getAsset("Kitchen");
		
		/* Container c = frame.getContentPane();*/    
		CKSceneViewer view=new CKSceneViewer(scene,1);
		//test.setPreferredSize(new Dimension(500,500));
		//test.setVisible(true);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		//Thread animator = new Thread(test);
		//animator.start();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	

}
