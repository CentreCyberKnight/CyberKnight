package ckDatabase;

import java.util.Iterator;

import ckGraphicsEngine.assets.CKGraphicsAsset;

abstract public class CKGraphicsAssetFactory
{
	
 abstract public CKGraphicsAsset getGraphicsAsset(String aid);
 	
  
 abstract public Iterator<CKGraphicsAsset> getAllGraphicsAssets();


 abstract public CKGraphicsAsset getPortrait(String assetId);


abstract public Iterator<CKGraphicsAsset> getFilteredGraphicsAssets(String string);


abstract public Iterator<CKGraphicsAsset>getFilteredGraphicsAssets(String[] filters);


abstract public Iterator<String> getAllUsages();


 
}
