///*
// * NodePropertyDialog.java
// *
// * Created on March 22, 2007, 2:23 PM
// * Copyright 2007 Grotto Networking
// */
//
package ckEditor.AbstractDialogEditor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKSingleParent;
import ckEditor.treegui.CKTreeGui;

//
///**
// *
// * @author  Greg
// */
public class VertexNodePropertiesEditor extends
		CKXMLAssetPropertiesEditor<VertexNode> implements ChangeListener,
		DocumentListener
{

	private static final long serialVersionUID = -1238586618687841329L;

	private VertexNode vertex;

	public VertexNodePropertiesEditor(VertexNode vert)
	{

		vertex = vert;
		initComponents();

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	//private JFormattedTextField capFormattedTextField;

	// End of variables declaration//GEN-END:variables
	private JTextArea description = new JTextArea();

	private JCheckBox startNode = new JCheckBox("Start Node");
	private JCheckBox endNode = new JCheckBox("End Node");
	private JCheckBox randomNode = new JCheckBox("Random Node");
	private CKTreeGui tree;

	private void initComponents()
	{

		// Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(vertex.getAID());
		setBorder(title);

		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		description.setText(vertex.getDescription());
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.getDocument().addDocumentListener(this);
		topPanel.add(description, BorderLayout.PAGE_START);

		JPanel BPanel = new JPanel(new GridLayout(1, 3));
		ButtonGroup cbg = new ButtonGroup()
		{
			private static final long serialVersionUID = 1L;

			public void setSelected(ButtonModel model, boolean selected)
			{
				if (selected)
				{
					super.setSelected(model, selected);
				} else
				{
					clearSelection();
				}
			}
		};
		startNode.setSelected(vertex.isStartNode());
		endNode.setSelected(vertex.isEndNode());
		randomNode.setSelected(vertex.isRandomNode());
		
		// do not add this to group cbg.add(startNode);
		cbg.add(endNode);
		cbg.add(randomNode);

		BPanel.add(startNode);
		BPanel.add(endNode);
		BPanel.add(randomNode);
		
		startNode.addChangeListener(this);
		endNode.addChangeListener(this);
		randomNode.addChangeListener(this);

		topPanel.add(BPanel, BorderLayout.CENTER);
		add(topPanel, BorderLayout.PAGE_START);

		tree = new CKTreeGui(vertex.getSingleParent());
		add(tree, BorderLayout.CENTER); 

	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		stateChanged(null);
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		stateChanged(null);
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		stateChanged(null);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		privateStoreState();

		for (ChangeListener l : listeners)
		{
			l.stateChanged(e);
		}

	}

	Vector<ChangeListener> listeners = new Vector<>();

	@Override
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}

	@Override
	public VertexNode getAsset()
	{
		return vertex;
	}

	@Override
	public void storeState()
	{
		tree.stopEditing();
		privateStoreState();
		
	}
	
	private void privateStoreState()
	{
		vertex.setDescription(description.getText());
		vertex.setStartNode(startNode.isSelected());
		vertex.setEndNode(endNode.isSelected());
		vertex.setRandomNode(randomNode.isSelected());
		vertex.setSingleParent((CKSingleParent) tree.getRoot());
	}

}
