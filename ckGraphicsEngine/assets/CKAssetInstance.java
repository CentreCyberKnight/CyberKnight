package ckGraphicsEngine.assets;

import java.awt.Graphics;

import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.LinkedList;

import ckCommonUtils.CKPosition;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.CKGraphicsEngine.RelationalLinkType;
import javafx.scene.canvas.GraphicsContext;
//{PUSH,PULL,RELATIVE,NONE};

public class CKAssetInstance implements Comparable<CKAssetInstance>
{
	
	
	private int iid; //instance id-unique for a board
	protected CKGraphicsAsset asset;
	protected  CKPosition position;
	protected boolean isVisible;
	private double animationSpeed;
	
	
	private CKAssetInstance parent;
	private LinkedList<CKAssetInstance> children;
	private RelationalLinkType relation;
	private CKPosition lastParentPosition;
	private CKPosition lastVector;
	
	
public CKAssetInstance(CKPosition pos,CKGraphicsAsset a,int IID )
{
	iid=IID;
	asset=a;
	position=pos;
	isVisible=true;
	parent=null;
	children=new LinkedList<CKAssetInstance>();
	relation=null;
	lastParentPosition=null;
	animationSpeed=1;
}




/**
 * @return the animationSpeed
 */
public double getAnimationSpeed()
{
	return animationSpeed;
}




/**
 * @param animationSpeed the animationSpeed to set
 */
public void setAnimationSpeed(double animationSpeed)
{
	this.animationSpeed = animationSpeed;
}




public void setParent(CKAssetInstance par,RelationalLinkType t)
{
	//use a lazy removal to get rid of children..only odd case is when it is the same parent
	if(parent != null)
	{
		parent.removeChild(this);
	}
	
	
	parent=par;
	relation=t;
	if(par != null  && t != RelationalLinkType.NONE)
	{
		par.addChild(this);

		lastParentPosition = parent.getPosition();
		lastParentPosition.round();
		lastVector = position.getDifferenceVector(lastParentPosition);
	}
	
	
	
	
}

protected void removeChild(CKAssetInstance child)
{
	children.remove(child);
}


protected void addChild(CKAssetInstance child)
{
	if(children==null)
	{
		children=new LinkedList<CKAssetInstance>();
	}
	children.add(child);
}

protected boolean notifyParentMove(CKAssetInstance par,double dx,double dy,double dz)
{
	if(parent != par) //parent has changed since last move
	{
		return (false);
	}
	//System.out.println("told to move by parent");


	
	if(relation==RelationalLinkType.RELATIVE)
	{
//		System.out.println("told to move by parent");
		moveBy(dx,dy,dz);	
	}
	else if (relation==RelationalLinkType.PULL)
	{
		CKPosition vec = new CKPosition(dx,dy,dz);
		double speed = vec.normalize();
		CKPosition myVec = lastVector.scaleVector(speed);
		moveBy(myVec.getX(),myVec.getY(),myVec.getZ());
		
		
		/*System.out.println("-------------------\nFollowing here "+position+
				"\nto      "+lastParentPosition
				+"\n following     "+parent.getPosition());
	*/
		//now update - Does this really work? TODO
		if(position.almostEqual(lastParentPosition,.0001))
		{
			lastParentPosition = parent.getPosition();
			lastParentPosition.round();
			lastVector = position.getDifferenceVector(lastParentPosition);
			
		}
		
	}
	else if (relation==RelationalLinkType.PUSH)
	{
		System.out.println("PUSH is not implmented yet");
	
	}
	
	
	
	
	return true;
	
}

protected void moveChildren(double dx,double dy,double dz)
{
	if(children != null)
	{
		Iterator<CKAssetInstance> iter=children.iterator();
		while(iter.hasNext())
		{	
			CKAssetInstance child = iter.next();
			boolean active = child.notifyParentMove(this,dx,dy,dz);
			if(! active)
			{
				iter.remove();
			}
		}
	}
}

	
public void drawToGraphics (Graphics g,int frame,
		ImageObserver observer,CKCoordinateTranslator translator)
{
	if(isVisible)
	{
		frame=(int) (frame*animationSpeed);
		Point screenP = translator.convertMapToScreen(position);
		asset.drawToGraphics(g,screenP.x,screenP.y,frame,0,observer);
	}
}


public void drawToGraphics (Graphics g,int frame, int row,
		ImageObserver observer,CKCoordinateTranslator translator)
{
	if(isVisible)
	{
		frame=(int) (frame*animationSpeed);
		Point screenP = translator.convertMapToScreen(position);
		asset.drawToGraphics(g,screenP.x,screenP.y,frame,row,observer);
	}
}


public void drawToGraphics (GraphicsContext g,int frame,
		ImageObserver observer,CKCoordinateTranslator translator)
{
	if(isVisible)
	{
		frame=(int) (frame*animationSpeed);
		Point screenP = translator.convertMapToScreen(position);
		asset.drawToGraphics(g,screenP.x,screenP.y,frame,0,observer);
	}
}


public void drawToGraphics (GraphicsContext g,int frame, int row,
		ImageObserver observer,CKCoordinateTranslator translator)
{
	if(isVisible)
	{
		frame=(int) (frame*animationSpeed);
		Point screenP = translator.convertMapToScreen(position);
		asset.drawToGraphics(g,screenP.x,screenP.y,frame,row,observer);
	}
}
	
	public void setVisible(boolean vis)
	{
		isVisible=vis;	
	}
		
	public int getFrames(int row)
	{
		return asset.getFrames(row);
	}
	
	public int getRows()
	{
		return asset.getRows();
	}
	
	public int getHeight(int row)
	{
		return asset.getHeight(row);	
	}

	public int getWidth(int row)
	{
		return asset.getWidth(row);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	@Override
	public int compareTo(CKAssetInstance that)
	{
		return position.compareTo(that.position);
	
	}

	
	
	final public void moveTo(CKPosition p)
	{
		CKPosition vec = position.getDifferenceVector(p);
		moveBy(vec.getX(),vec.getY(),vec.getZ());
	}
	
	final public void moveTo(double x,double y)
	{
		moveTo(new CKPosition(x,y,position.getZ(),0));
	}
	
	final public void moveTo(double x,double y,double z)
	{
		moveTo(new CKPosition(x,y,z,0));
	}
	
	final public void moveBy(double x,double y)
	{
		moveBy(x,y,0);
	}
	
	final public void moveBy(CKPosition pos)
	{
		moveBy(pos.getX(),pos.getY(),pos.getZ());
	}
	
	
	public void moveBy(double x,double y,double z)
	{

		position.setX(position.getX()+x);
		position.setY(position.getY()+y);
		position.setZ(position.getZ()+z);
		moveChildren(x,y,z);
	
	}

	/**
	 * @return the asset
	 */
	public CKGraphicsAsset getAsset()
	{
		return asset;
	}




	public CKPosition getPosition()
	{
		return (CKPosition) position.clone();
	}
	
	public int getIID()
	{
		return iid;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
