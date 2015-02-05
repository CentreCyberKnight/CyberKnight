package ckGraphicsEngine;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.CKGraphicsLayerFactoryXML;
import ckGameEngine.CKGrid;
import ckGraphicsEngine.assets.CKCameraInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;

/**
 * This class will take various large graphics entities, like scene and layers and create an smaller static or animated image for the scene.

 * @author Michael K. Bradshaw
 *
 */
public class CKGraphicsPreviewGenerator
{


	public static BufferedImage createAssetPreview(CKGraphicsAsset asset,
													int row,int frame,
													int previewWidth,int previewHeight)
	{
		
		//get the bounds
		Point off = new Point(0,0);
		Point bounds = new Point(0,0);
		asset.getDrawBounds(frame, row, off, bounds);
			
		Point screenMax = new Point (bounds.x-off.x,bounds.y-off.y);
		//get graphics context of an image
		BufferedImage drawImg = new BufferedImage(Math.max(screenMax.x, 1), Math.max(screenMax.y, 1),
																						BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = drawImg.createGraphics();
		g.setColor(new Color(0,true));
		//g.setColor(Color.GREEN);
		g.fillRect(0, 0, screenMax.x, screenMax.y);
		asset.drawToGraphics(g, -1*off.x, -1*off.y, frame, row,null);
		g.drawRect(20,20, 10,10);
		
		//now draw to previewImg
		BufferedImage previewImg = new BufferedImage(previewWidth, previewHeight, BufferedImage.TYPE_INT_ARGB);

		g = previewImg.createGraphics();

		//add some rendering hints to increase the quality of the previews
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


		
		//I want to retain the scaling of the new image
		double xscale =previewWidth / (double)screenMax.x; 
		double yscale =previewHeight / (double) screenMax.y; 
		double scale = Math.min(xscale, yscale);
		
		int xUsage = (int) (scale*screenMax.x); 
		int yUsage = (int) (scale*screenMax.y);
		int xMargin = (previewWidth - xUsage)/2;
		int yMargin = (previewHeight - yUsage)/2;
		
		g.setColor(new Color(0,true));
		g.fillRect(0, 0, previewWidth, previewHeight);
		//g.drawRect(xMargin, yMargin, xUsage, yUsage);
		g.drawImage(drawImg,				
				xMargin, yMargin, xMargin+xUsage, yMargin+yUsage,
				 0,0, screenMax.x, screenMax.y,
				null);
				
		return previewImg;
		
	}

	
	
	public static BufferedImage createScenePreview(CKGraphicsSceneInterface scene,
													int previewWidth,int previewHeight)
	{
		CKCoordinateTranslator trans = scene.getTrans();
		
		//get the bounds
		Point screenMin = new Point(0,0);
		Point screenMax = new Point(0,0);
		trans.getScreenCoordsForBounds(screenMin,screenMax);
		//System.out.println("Mincoords"+screenMin+" and max coords"+screenMax);
			
		//need to adjust the image to start at 0,0
		trans.shiftWorldOffset(-1*screenMin.x, -1*screenMin.y);
		//screen max should be how large the image is
		screenMax.x -= screenMin.x;
		screenMax.y -= screenMin.y;

		//magins at 10% of max?
		Point margin = new Point((int) (screenMax.x*.15), (int) (screenMax.y*.15));		
		trans.shiftWorldOffset( margin.x, margin.y);
		
		screenMax.x+=margin.x*2;
		screenMax.y+=margin.y*2;
		
		//get graphics context of an image
		BufferedImage drawImg = new BufferedImage(screenMax.x, screenMax.y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = drawImg.createGraphics();
		g.setColor(new Color(0,true));
		g.fillRect(0, 0, screenMax.x, screenMax.y);

		((CKCameraInstance)scene.getCamera()).turnOffCamera();
		scene.drawOffScreenBuffer(g, screenMax.x,screenMax.y);
		((CKCameraInstance)scene.getCamera()).turnOnCamera();
		

		
		//now draw to previewImg
		BufferedImage previewImg = new BufferedImage(previewWidth, previewHeight, BufferedImage.TYPE_INT_ARGB);

		g = previewImg.createGraphics();

		//add some rendering hints to increase the quality of the previews
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


		
		//I want to retain the scaling of the new image
		double xscale =previewWidth / (double)screenMax.x; 
		double yscale =previewHeight / (double) screenMax.y; 
		double scale = Math.min(xscale, yscale);
		
		int xUsage = (int) (scale*screenMax.x); 
		int yUsage = (int) (scale*screenMax.y);
		int xMargin = (previewWidth - xUsage)/2;
		int yMargin = (previewHeight - yUsage)/2;
		
		g.setColor(new Color(0,true));
		g.fillRect(0, 0, previewWidth, previewHeight);
		//g.drawRect(xMargin, yMargin, xUsage, yUsage);
		g.drawImage(drawImg,				
				xMargin, yMargin, xMargin+xUsage, yMargin+yUsage,
				 0,0, screenMax.x, screenMax.y,
				null);

		//restore the scene
		trans.shiftWorldOffset(screenMin.x, screenMin.y);		
		trans.shiftWorldOffset(-1*margin.x, -1*margin.y);
		
		return previewImg;
		
	}
	
	
	public static BufferedImage createLayerPreview(CKGraphicsLayer layer,
			int previewWidth,int previewHeight)
	{
		CKGraphicsScene scene=new CKGraphicsScene("",layer.getDescription(),new CKGrid(10,10));
		
		scene.addLayer(layer);
		return createScenePreview(scene,previewWidth,previewHeight);	
	}
	
	
	public static BufferedImage createComponentPreview(JComponent comp, int width,int height)
	{
		
		BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		comp.setSize(width, height);
		comp.doLayout();
		comp.validate();
		comp.setDoubleBuffered(false); //removes the error, but does not show..
		
		Graphics g = b.createGraphics();
		comp.paint(g); //paints JLAbels, but not JPanels..
		
		
		return b;

		
	}

	
	public static void main(String [] args)
	{
		JFrame frame = new JFrame();
		
		frame.setLayout(new FlowLayout());
		frame.setBackground(Color.red);
		//CKGraphicsSceneInterface scene = (CKGraphicsSceneFactoryXML.getInstance().getGraphicsScene("Kitchen"));
		//ImageIcon icon = new ImageIcon(createScenePreview(scene,200,200));
		//layers is not working as well
		CKGraphicsLayer layer = (CKGraphicsLayerFactoryXML.getInstance().getGraphicsLayer("Kitchen"));
		ImageIcon icon2 = new ImageIcon(createLayerPreview(layer,200,200));
		//imageicon
		CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("heroSprite");
		ImageIcon icon3 = new ImageIcon(createAssetPreview(asset,0,0,64,64));
		//jcomponent addon
		JLabel label = new JLabel("hi there I'm new");
		ImageIcon icon4 = new ImageIcon(createComponentPreview(label,100,100));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GREEN);
		panel.add(new JLabel("here is some text"));
		ImageIcon icon5 = new ImageIcon(createComponentPreview(panel,100,100));
		
		//frame.add(new JLabel(icon));
		frame.add(new JLabel(icon2));
		frame.add(new JLabel(icon3));
		frame.add(new JLabel(icon4));
		frame.add(new JLabel(icon5));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		//Thread animator = new Thread(test);
		//animator.start();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}




	
	
}
