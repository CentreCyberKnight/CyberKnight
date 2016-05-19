package ckEditor.treegui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKQuestFactory;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckGameEngine.QuestData;


public class CKGuiRoot extends CKGUINode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2009482896399359048L;




	public CKGuiRoot()
	{
		
	}
	
	public void saveQuests()
	{
		if(children==null)
			return ;

		for(Object o: children)
		{
			QuestData q = (QuestData) o;
			System.out.println("Saving quest "+q.getName());
			CKQuestFactory.getInstance().writeAssetToXMLDirectory(q);
		}
	}
	
	
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		JMenuItem addNewMenu = new JMenuItem("New Quest");
		addNewMenu.addActionListener(new NewQuestListener(tree,this));
		return addNewMenu;
	}

	public JPopupMenu getPopup(CKTreeGui tree)
	{
			
		JPopupMenu popup=new JPopupMenu();
		JMenuItem editAction= GUIEdit();
		popup.add(editAction);
		popup.add(new JSeparator());
		
		JMenuItem copyAction = new JMenuItem("Copy");
		//can't copy the root node
		copyAction.setEnabled(false);
		popup.add(copyAction);
		
		//can only paste quests
		JMenuItem pasteAction = new JMenuItem("Paste");
		if(tree.getCopyBuffer()!=null && tree.getCopyBuffer()  instanceof QuestData)
		{	
			pasteAction.addActionListener(new TreePasteActionListener(tree));
		}
		else { pasteAction.setEnabled(false);}
		popup.add(pasteAction);

		JMenuItem removeAction = new JMenuItem("Remove");
		removeAction.setEnabled(false);
		popup.add(removeAction);
		
		popup.add(new JSeparator());
		//Load Quest

		JMenuItem loadMenu = new JMenuItem("Load Quest from DB");
		loadMenu.addActionListener(new QuestViewerPopupListener(tree,this,false));
		popup.add(loadMenu);
		

		JMenuItem loadCopyMenu = new JMenuItem("Load Quest as Copy");
		loadCopyMenu.addActionListener(new QuestViewerPopupListener(tree,this,true));
		popup.add(loadCopyMenu);


		JMenuItem loadFileMenu = new JMenuItem("Load Quest from File");
		loadFileMenu.addActionListener(new QuestFileLoader(tree,this));
		popup.add(loadFileMenu);
		

		
		JMenuItem saveAction = new JMenuItem("Save Quests");
		saveAction.addActionListener(new SaveAllListener());
		popup.add(saveAction);
		
		
		
		JMenuItem addAction= GUIAddNode(tree);
		popup.add(addAction);
		
		return popup;
	}
	
	public static void main (String[] argv)
	{
			JFrame frame = new JFrame();
			
			frame.add(new CKTreeGui(new CKGuiRoot()));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(400,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	class SaveAllListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			saveQuests();			
		}		
	}
	
	class NewQuestListener implements ActionListener
	{
		CKTreeGui tree;
		CKGuiRoot root;
		public NewQuestListener(CKTreeGui tree, CKGuiRoot root)
		{
			this.tree = tree;
			this.root=root;
		}

		public void actionPerformed(ActionEvent e)
		{
			tree.addNode(root,new QuestData(5));
		}
	
	
	}
	
	class QuestViewerPopupListener implements ActionListener
	{
		CKTreeGui tree;
		CKGuiRoot root;
		boolean copy;
		public QuestViewerPopupListener(CKTreeGui tree, CKGuiRoot root,boolean copy)
		{
			this.tree = tree;
			this.root=root;
			this.copy=copy;
			
		}

		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			CKQuestFactory factory = CKQuestFactory.getInstance(); 
			
			CKXMLAssetPicker<QuestData> picker = new CKXMLAssetPicker<QuestData>(factory.getAllAssets());
			
			
			picker.addSelectedListener(new AddQuestListener(frame,tree,root,copy));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}
	
	
	}

	
	class AddQuestListener implements CKEntitySelectedListener<QuestData>
	{

		CKTreeGui tree;
		JFrame frame;
		CKGuiRoot root;
		boolean copy;
				
		public AddQuestListener(JFrame frame, CKTreeGui tree,CKGuiRoot root,boolean copy)
		{
			this.root=root;
			this.tree=tree;
			this.frame=frame;
			this.copy=copy;
		}

		@Override
		public void entitySelected(QuestData quest)
		{
			if(copy)
			{
				
				quest.setCopy();
			}
			//add quest to root
			tree.addNode(root, quest);
			//need to delete frame
			frame.setVisible(false);
			frame.dispose(); 
			
		}

	}


	class QuestFileLoader implements ActionListener
	{
		CKTreeGui tree;
		CKGuiRoot root;
		
		public QuestFileLoader(CKTreeGui tree, CKGuiRoot root)
		{
			this.tree = tree;
			this.root=root;
		}
	
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fc =new JFileChooser(); 
			int returnVal = fc.showOpenDialog(null);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            
			            try
						{
							CKGUINode node =  CKGUINode.readFromStream(new FileInputStream(file));
							//!Then attatch
							this.tree.addNode(root,node);
						
						} catch (FileNotFoundException e1)
						{
							System.err.println("Could not file File"+file);
						}
			        } 
			   }

	
		}
	
}
