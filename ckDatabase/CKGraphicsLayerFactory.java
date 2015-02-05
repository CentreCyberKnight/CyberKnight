package ckDatabase;

import java.util.Iterator;

import ckGraphicsEngine.layers.CKGraphicsLayer;

public interface CKGraphicsLayerFactory
{
	
 public CKGraphicsLayer getGraphicsLayer(String aid);

 public Iterator<CKGraphicsLayer> getAllGraphicsLayers();
 
}
