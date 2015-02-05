package ckEditor.AbstractDialogEditor;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckCommonUtils.DisabledPanel;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.actions.CKSimpleGUIAction;

public class DialogVertexPropertiesEditor extends
		CKXMLAssetPropertiesEditor<VertexNode> implements ChangeListener
{
	private static final long serialVersionUID = -9131880157871860814L;

	private VertexNodePropertiesEditor ed;
	private DialogVertex asset;
	private CKTreeGui question;
	private DisabledPanel dPanel;
	
	
	public DialogVertexPropertiesEditor(DialogVertex asset)
	{
		this.asset=asset;
		ed = new VertexNodePropertiesEditor(asset);
		ed.addChangeListener(this);
		
		question = new CKTreeGui(asset.question);
		question.addChangeListener(this);
		
		dPanel = new DisabledPanel(question);
		dPanel.setEnabled(!(asset.isEndNode() || asset.isRandomNode()));
		
		TitledBorder title = BorderFactory.createTitledBorder("Concluding Question");
		dPanel.setBorder(title);
		
		setLayout(new BorderLayout());
		add(ed,BorderLayout.PAGE_START);
		add(dPanel,BorderLayout.CENTER);
		
		
		
		
	}
	
	Vector<ChangeListener> listeners=new Vector<>();
	
	@Override
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);

	}

	@Override
	public VertexNode getAsset()
	{
		return asset;
	}

	@Override
	public void storeState()
	{
		ed.storeState();
		question.stopEditing();
		myStoreState();

	}

	
	private void myStoreState()
	{
		dPanel.setEnabled(!(asset.isEndNode() || asset.isRandomNode()));
		asset.setQuestion( (CKSimpleGUIAction) question.getRoot());
	}
	

	@Override
	public void stateChanged(ChangeEvent e)
	{
		myStoreState();
		
		for(ChangeListener l:listeners)
		{
			l.stateChanged(e);
		}
		
	}

}
