package ckGraphicsEngine;

import java.awt.Point;

import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.layers.CKGraphicsLayer;


public class CKTileHighlighter extends CKSceneMouseListener
{
	
	
	private CKAssetInstance instance_back;
	private CKAssetInstance instance_front;

	public CKTileHighlighter(CKGraphicsSceneInterface s,CKAssetInstance back)
	{
		super(s);
		instance_back=back;
		back.setVisible(false);
		instance_front=null;
		getScene().addInstanceToLayer(instance_back,CKGraphicsLayer.REARHIGHLIGHT_LAYER);
	}

	public CKTileHighlighter(CKGraphicsSceneInterface s,CKAssetInstance front,CKAssetInstance back)
	{
		this(s,front,back,CKGraphicsLayer.FRONTHIGHLIGHT_LAYER,CKGraphicsLayer.REARHIGHLIGHT_LAYER);
	}
	
	public CKTileHighlighter(CKGraphicsSceneInterface s,CKAssetInstance front,CKAssetInstance back,
			int f_layer,int b_layer)
	{
		super(s);
		instance_back=back;
		instance_front=front;
		back.setVisible(false);
		front.setVisible(false);
		getScene().addInstanceToLayer(instance_back,b_layer);
		getScene().addInstanceToLayer(instance_front,f_layer);
	}

		@Override
		public void handleMouseMoved(javafx.scene.input.MouseEvent e)
		{
		
		//TODO is there a synchonization problem
		
    	Point p = getPoint(e);
    	    	
    	Point mapCoords = getScene().getTrans().convertScreenToMap(p);

    	//System.out.println("Mouse is at point"+mapCoords);
    	//check if it is within bounds
    	if(mapCoords.x<0 || mapCoords.y<0  
    			||mapCoords.x>=getScene().getTrans().getMapColumns()
    			||mapCoords.y>=getScene().getTrans().getMapRows())
    	{
    		instance_back.setVisible(false);
    		if(instance_front!=null)
    		{
    			instance_front.setVisible(false);
    		}
    	}
    	else
    	{
    		double Z = getScene().getGrid().getTopPosition((int) mapCoords.x, (int)mapCoords.y).getPos().getZ();

    		instance_back.moveTo(mapCoords.x,mapCoords.y, Z);
    		instance_back.setVisible(true);
    		if(instance_front!=null)
    		{
    			instance_front.moveTo(mapCoords.x,mapCoords.y, Z);
    			instance_front.setVisible(true);
    		}
    	}
  	}

	@Override
	public void handleMouseClicked(javafx.scene.input.MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	


	@Override
	public void handleMouseExited(javafx.scene.input.MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
