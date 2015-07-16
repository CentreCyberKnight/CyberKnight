package ckGameEngine;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKSpellListener;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckSnapInterpreter.CKFXImage;

public class CKSpell
{
	String name;
	String description;
	String functionCall;
	String iconID;
	Image fximage;
	String snapImage;
	
	
	int timeTilRecharge;
	Vector<CKSpellListener> listeners;
	
	public CKSpell()
	{
		this("","","","sparkles");
		this.fximage = null;
	}
	
	public CKSpell(String name,String description,String fcn, String iconID)
	{
		this.name=name;
		this.description=description;
		this.functionCall=fcn;
		this.iconID=iconID;
		this.fximage = null;
		
		listeners=new Vector<CKSpellListener>();
		timeTilRecharge = 0;
	}
	
	public void turnPasses()
	{
		if(timeTilRecharge >0)
		{
			timeTilRecharge--;
			notifyRechargeChange();
		}		
	}
	
	/*
	public void runSpell()
	{
		//need to run the spell...
		
		
		
		//
		timeTilRecharge = rechargeTime;
		notifyRechargeChange();
	}
	*/
	
	//FIXME do I need these?
	public void addListener(CKSpellListener lis)
	{
		listeners.add(lis);
	}
	
	public void removeListener(CKSpellListener lis)
	{
		listeners.remove(lis);
	}
	
	
	protected void notifyRechargeChange()
	{
		for (CKSpellListener lis: listeners)
		{
			lis.rechargeChanged();
		}
	}
	
	protected void notifySpellChanged()
	{
		for (CKSpellListener lis: listeners)
		{
			lis.spellChanged();
		}
		
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the functionCall
	 */
	public String getFunctionCall()
	{
		return functionCall;
	}

	/**
	 * @param functionCall the functionCall to set
	 */
	public void setFunctionCall(String functionCall)
	{
		this.functionCall = functionCall;
	}

	/**
	 * @return the iconID
	 */
	public String getIconID()
	{
		return iconID;
	}

	/**
	 * @param iconID the iconID to set
	 */
	public void setIconID(String iconID)
	{
		this.iconID = iconID;
	}

	
	
	
	/**
	 * @return the timeTilRecharge
	 */
	public int getTimeTilRecharge()
	{
		return timeTilRecharge;
	}

	/**
	 * @param timeTilRecharge the timeTilRecharge to set
	 */
	public void setTimeTilRecharge(int timeTilRecharge)
	{
		this.timeTilRecharge = timeTilRecharge;
	}
	
	
	
	public Image getFXImage() 
	{
		if(this.fximage != null) {	
			return fximage;
		}
		else {
			try {
				System.out.println("The image for " + iconID + " was not found. It is now being created.");	
    			CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(iconID);
    			Image image = CKGraphicsPreviewGenerator.createAssetPreviewFX(asset, 0, 0, 80, 90);
    			this.fximage = image;
			}
			catch (NullPointerException n) {
				System.out.println("The asset for " + iconID + " was not found." );
			}
			return fximage;	
		}
	}
	
	//returns a base64 string of the artifact image
	public String getSnapImage()
		{
		if(this.snapImage != null && this.fximage != null) {	
			return this.snapImage;
		}
		else {
			try {
				//needed a smaller image for the artifacts
				CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(iconID);
    			Image image = CKGraphicsPreviewGenerator.createAssetPreviewFX(asset, 0, 0, 35, 40);
    			BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
    			ByteArrayOutputStream s = new ByteArrayOutputStream();
    			try {
					ImageIO.write(bImage, "png", s);
					} 
    			catch (IOException e) {
					e.printStackTrace();
					}
    			this.snapImage  = Base64.encode(s.toByteArray());
			}
			catch (NullPointerException n) {
				System.out.println("The asset for " + iconID + " was not found." );
			}
			return this.snapImage;	
		}
	}
}