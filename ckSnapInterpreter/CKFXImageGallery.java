package ckSnapInterpreter;

import java.util.ArrayList;

import javafx.scene.image.Image;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;


public class CKFXImageGallery extends ArrayList<CKFXImage> {
	
	private static final long serialVersionUID = 1L;
	boolean foundById;
	CKFXImage theImage;
	
	public CKFXImageGallery() {
		foundById = false;	
	}
	
	
	public void findImageId(String imageid) {
		for(CKFXImage i : this) {
			if(i.getFXImageId() == imageid) 
				foundById = true;
				theImage = i;
				break; 
		}	
	}
	
	public CKFXImage getFXImage(String fxImageId) {
		findImageId(fxImageId);
		if(!foundById) {	
			try {
				System.out.println("The image for " + fxImageId + " was not found. It is now being created." );	
    			CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(fxImageId);
    			Image image = CKGraphicsPreviewGenerator.createAssetPreviewFX(asset, 0, 0, 80, 90);
    			CKFXImage newImage = new CKFXImage(image, fxImageId);
    			this.add(newImage);
    			theImage = newImage;
			}
			catch (NullPointerException n) {
				System.out.println("The asset for " + fxImageId + " was not found." );
			}
		}
		return theImage;		
	}
	
}
