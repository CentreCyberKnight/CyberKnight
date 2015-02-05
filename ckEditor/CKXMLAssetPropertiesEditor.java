package ckEditor;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import ckCommonUtils.CKXMLAsset;

abstract public class CKXMLAssetPropertiesEditor<T extends CKXMLAsset<T>> extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6955624799392299809L;
	
	public CKXMLAssetPropertiesEditor()
	{
		setLayout(new BorderLayout());
	}
	
	abstract public void addChangeListener(ChangeListener l);
	abstract public T getAsset();
	abstract public void storeState();
}
