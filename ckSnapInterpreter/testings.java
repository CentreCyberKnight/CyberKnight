package ckSnapInterpreter;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class testings {
	
	public static void main(String[] args) {
		CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("sparkles");
		System.out.println(asset.getDescription());
	}
}