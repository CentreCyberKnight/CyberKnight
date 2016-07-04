package ckGraphicsEngine.layers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import ckCommonUtils.CKPosition;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import javafx.scene.canvas.GraphicsContext;

public class CKDynamicLayer extends CKGraphicsLayer
{

	LinkedList<CKAssetInstance> sprites; 
	
	
	public CKDynamicLayer(int d)
	{
		super(d,"","");
		sprites=new LinkedList<CKAssetInstance>();
		setVisible(true);
	}
	
	
	public void reset()
	{
		Collections.sort(sprites);
	}
	
	
	public void addAsset(CKPosition pos,CKGraphicsAsset t)
	{
		addInstance(new CKAssetInstance(pos,t,-1));
	}
	
	
	
	public void addInstance(CKAssetInstance instance)
	{
		sprites.add(instance);
	}
	
	@Override
	public void drawLayerToGraphics(Graphics g, int frame,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		if(!isVisible()) return;
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance instance = iter.next();
			instance.drawToGraphics(g, frame,observer,translator);
		}

	}

	@Override
	public void drawLayerRowToGraphics(Graphics g, int frame, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		if(!isVisible()) return;
		//System.out.println("looking for sprite"+sprites.size());

		sprites.stream()
			.filter(instance->{
				return y == (int) Math.ceil(instance.getPosition().getY());
			})
			.sorted((i1,i2)->
			Double.compare(i1.getPosition().getX(),
					i2.getPosition().getX()) )
			.forEach(instance->{
				instance.drawToGraphics(g, frame, observer,translator);
				//System.out.println("I am drawing"+instance.getAsset().getAID()+" at"+
				//		instance.getPosition().toString());
			});
		
/*		
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance instance = iter.next();
			int spriteY = (int) Math.ceil(instance.getPosition().getY());
			//System.out.println("looking at sprite at "+spriteY+ "for"+y);
			if(spriteY==y)
			{
				//System.out.println("looking at sprite at "+spriteY+ "for"+y);
				instance.drawToGraphics(g, frame, observer,translator);
			}
		}
		
*/
	}

	@Override
	public void drawLayerTileToGraphics(Graphics g, int frame, int x, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		if(!isVisible()) return;
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance instance = iter.next();
			int spriteY = (int) Math.ceil(instance.getPosition().getY());
			int spriteX = (int) instance.getPosition().getX();
			if(spriteY==y && spriteX==x)
			{
				instance.drawToGraphics(g, frame,observer,translator);
			}
		}


	}

	
	@Override
	public void drawLayerToGraphics(GraphicsContext g, int frame,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		if(!isVisible()) return;
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance instance = iter.next();
			instance.drawToGraphics(g, frame,observer,translator);
		}

	}

	@Override
	public void drawLayerRowToGraphics(GraphicsContext g, int frame, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		if(!isVisible()) return;
		//System.out.println("looking for sprite"+sprites.size());
		sprites.stream()
		.filter(instance->{
			return y == (int) Math.ceil(instance.getPosition().getY());
		})
		.sorted((i1,i2)->
		Double.compare(i1.getPosition().getX(),
				i2.getPosition().getX()) )
		.forEach(instance->{
			instance.drawToGraphics(g, frame, observer,translator);
			//System.out.println("I am drawing"+instance.getAsset().getAID()+" at"+
			//		instance.getPosition().toString());
		});
		/*
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance instance = iter.next();
			int spriteY = (int) Math.ceil(instance.getPosition().getY());
			//System.out.println("looking at sprite at "+spriteY+ "for"+y);
			if(spriteY==y)
			{
				//System.out.println("looking at sprite at "+spriteY+ "for"+y);
				instance.drawToGraphics(g, frame, observer,translator);
			}
		}
		*/

	}

	@Override
	public void drawLayerTileToGraphics(GraphicsContext g, int frame, int x, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		if(!isVisible()) return;
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance instance = iter.next();
			int spriteY = (int) Math.ceil(instance.getPosition().getY());
			int spriteX = (int) instance.getPosition().getX();
			if(spriteY==y && spriteX==x)
			{
				instance.drawToGraphics(g, frame,observer,translator);
			}
		}


	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		

	}


	@Override
	public void changeHeight(int x, int y, double heightDiff)
	{
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance instance = iter.next();
			CKPosition pos =instance.getPosition();
			
			if((int)(pos.getX()) == x && (int)(pos.getY())==y)
			{
				pos.setZ(pos.getZ()+heightDiff);
				instance.moveBy(0,0,heightDiff);
			}
		}
	}

	
	/*
	 * Starting DATABASE FUNCTIONS
	 * 
	 */
	

	@Override
	public boolean removeInstance(CKAssetInstance t)
	{
		Iterator<CKAssetInstance> iter=sprites.iterator();
		while(iter.hasNext())
		{
			if(iter.next() == t)
			{
				iter.remove();
				return true;
			}
		}
		
		return false;
	}


	@Override
	public boolean getLayerBounds(Point minPoint, Point maxPoint)
	{
		
		minPoint.x=0;
		minPoint.y=0;
		maxPoint.x=1;
		maxPoint.y=1;
		
		Iterator<CKAssetInstance> iter = sprites.iterator();
		while(iter.hasNext())
		{
			CKAssetInstance asset = iter.next();
			CKPosition pos = asset.getPosition();
			if(pos.getX() < minPoint.x) { minPoint.x=(int) Math.floor(pos.getX()) ; }
			if(pos.getX() > maxPoint.x) { maxPoint.x=(int) Math.ceil(pos.getX()); }
			if(pos.getY() < minPoint.y) { minPoint.y=(int)pos.getY(); }
			if(pos.getY() > maxPoint.y) { maxPoint.y=(int)Math.ceil(pos.getY()); }			
		}
				
		return true;
	}


	@Override
	public void removeAllInstances()
	{
		sprites.clear();
	}


	@Override
	public Iterator<CKAssetInstance> iterator()
	{
		return sprites.iterator();
	}


	
}
