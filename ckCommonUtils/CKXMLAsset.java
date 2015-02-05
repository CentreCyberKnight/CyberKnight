package ckCommonUtils;

import java.io.OutputStream;

import javax.swing.JComponent;

import ckEditor.CKXMLAssetPropertiesEditor;

public interface CKXMLAsset<T>
{
	/**
	 * writes the XML form of the object to the stream out
	 * @param out - stream to write to
	 */
	public void writeToStream(OutputStream out);

	
	
	public String getAID();
	public void setAID(String a);
	

	
	public enum ViewEnum {STATIC,INTERACTIVE,EDITABLE}


	/**
	 * creates a way to display the XMLasset on a jcomponent
	 */
	public JComponent getXMLAssetViewer();
	
	/**
	 * creates a way to display the XMLasset on a jcomponent
	 * 
	 * Classes that only return a static viewer do not need to use the parameter
	 */
	public JComponent getXMLAssetViewer(ViewEnum v);
	

	/**
	 * creates a way to edit the properties of an XMLasset.
	 */
	public <B extends CKXMLAsset<B> > CKXMLAssetPropertiesEditor<B> getXMLPropertiesEditor();
	
	
	
	/**
	 * write the XML form of the object from the stream in and returns the object.
	 * @param in
	 * @return
	 */
	//put this in the factory....public static T readFromStream(InputStream in);
}
