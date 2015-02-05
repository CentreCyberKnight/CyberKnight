package ckEditor;

import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ckCommonUtils.CKXMLAsset;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKTreeGui;
import ckTrigger.CKTrigger;

public class CKGUINodePropertiesEditor<T extends CKGUINode & CKXMLAsset<T>> 
extends CKXMLAssetPropertiesEditor<T> implements ChangeListener

{

	/**
	 * 
	 */
	private static final long serialVersionUID = 826899908289413368L;

	T asset;
	
	
	CKTreeGui   myTree;
	
	
	public CKGUINodePropertiesEditor(T asset)
	{
		this.asset=asset;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		myTree = new CKTreeGui(asset);
		myTree.addChangeListener(this);
		add(myTree);
		
	}

	private Vector<ChangeListener> listeners = new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}



	@Override
	public void storeState()
	{
		//stateChanged(null);
		//if(myTree)
		myTree.saveState();
		
	}
	
	
	@Override
	public void stateChanged(ChangeEvent e)
	{		
		
		//System.out.println("changed GUITree State");
		//storeState();
		
		for(ChangeListener l:listeners)
		{
			l.stateChanged(new ChangeEvent(this));
		}
		
	}

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		//CKSharedAsset water=(CKSharedAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("HeroSW");
		
		CKGUINodePropertiesEditor<CKTrigger> view=new CKGUINodePropertiesEditor<CKTrigger>(new CKTrigger());
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}

	
	@Override
	public T getAsset()
	{
		return (T) myTree.getRoot();
		
	}


}
