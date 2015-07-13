package ckSnapInterpreter;

import javafx.scene.image.Image;

public class CKFXImage {
	
	Image image;
	//String imageType;
	String fximageid;
	
	
	public CKFXImage(Image image, String fximageid) {
		this.image = image;
		//this.imageType = imageType;
		this.fximageid = fximageid;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the imageName
	 */
	public String getFXImageId() {
		return fximageid;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setFXImageId(String fximageid) {
		this.fximageid = fximageid;
	}
	
	
}