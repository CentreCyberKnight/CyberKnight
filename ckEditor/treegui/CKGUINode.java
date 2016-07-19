package ckEditor.treegui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKBookFactory;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.QuestData;

/**
 * @author dragonlord
 *
 */
public class CKGUINode extends DefaultMutableTreeNode implements CKGUIEditable
{
	
	private static final long serialVersionUID = 4529450570130666205L;
	private boolean childOrderLocked = false;
	private boolean childRemoveable = true;
	private boolean pastableChildren = true;
	protected boolean visible        	        = true;
	
	
	public boolean isVisible() { return visible; }
	
	
	
	/**
	 * Returns the index-th Visible child, ignores invisible children
	 * Calls getChildAt() to retrieve the child
	 * @param index - visible child to get
	 * @return
	 */
	public TreeNode getVisibleChildAt(int index)
	{
		
		if (children == null) 
		{
			throw new ArrayIndexOutOfBoundsException("node has no children");
		}

		int realIndex = -1;
		int visibleIndex = -1;
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = children.elements();
		while (e.hasMoreElements()) 
		{
			CKGUINode node = (CKGUINode) e.nextElement();
				if (node.isVisible()) 
				{
					visibleIndex++;
				}
				realIndex++;
				if (visibleIndex == index) 
				{
					return (TreeNode) children.elementAt(realIndex);
			    }
		}
		return null;
	}
	
	
	/**
	 * Used by the TreeModel to avoid drawing invisible children
	 * replacement for getChildCount() 
	 * @return number of visible children
	 */
	public int getVisibleChildCount()
	{
		  if (children == null)                 {  return 0;     }

		    int count = 0;
		    @SuppressWarnings("unchecked")
			Enumeration<CKGUINode> e = children.elements();
		    while (e.hasMoreElements()) 
		    {
		    	CKGUINode node = e.nextElement();
		    	if (node.isVisible())            { count++;    }
		    }

		    return count;
		  }
	
	
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#add(javax.swing.tree.MutableTreeNode)
	 * Overridden here to make insure that the behavoir stays the same.
	 */
	@Override
	public final void add(MutableTreeNode newChild)
	{
		insert(newChild,getChildCount());
	}
	
	/**
	 * Adds without cloning the childnode.
	 * Used when links to the node must be kept intact.
	 * @param newChild
	 */
	public final void addIT(MutableTreeNode newChild)
	{
		//bypass the cloning
		super.insert(newChild, getChildCount());
	}
	
	
	
	/**
	 * Adds without cloning the childnode.
	 * Used when links to the node must be kept intact.
	 * @param newChild
	 */
	public final void insertIT(MutableTreeNode newChild,int index)
	{
		//bypass the cloning
		super.insert(newChild, index);
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#remove(javax.swing.tree.MutableTreeNode)
	 */
	@Override
	public final void remove(MutableTreeNode aChild)
	{
		remove(this.getIndex(aChild));
	}
	
		
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		// should ensure that we are always working with copies...
		MutableTreeNode tn = (MutableTreeNode) ((DefaultMutableTreeNode) newChild).clone() ;
		if(! isVisible())
		{
			((CKGUINode) tn).visible=false; 
		}
		super.insert(tn, childIndex); 
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
/*	@Override
	public Object clone()
	{
		DefaultMutableTreeNode node= (DefaultMutableTreeNode) super.clone();
		node.removeAllChildren();
		if(children != null)
		{
			for(Object o: children)
			{
				node.add( (MutableTreeNode) ((DefaultMutableTreeNode) o ).clone());
			}
		}
		return node;
	}
*/
	
	/**
	 * @return the pastableChildren
	 */
	public boolean isPastableChildren()
	{
		return pastableChildren;
	}



	/**
	 * @param pastableChildren the pastableChildren to set
	 */
	public void setPastableChildren(boolean pastableChildren)
	{
		this.pastableChildren = pastableChildren;
	}

	
	
	/**
	 * @return the childOrderLocked
	 */
	public boolean isChildOrderLocked()
	{
		return childOrderLocked;
	}

	


	


	/**
	 * @param childOrderLocked the childOrderLocked to set
	 */
	public void setChildOrderLocked(boolean childOrderLocked)
	{
		this.childOrderLocked = childOrderLocked;
	}

	/**
	 * @return the childRemoveable
	 */
	public boolean isChildRemoveable()
	{
		return childRemoveable;
	}

	/**
	 * @param childRemoveable the childRemoveable to set
	 */
	public void setChildRemoveable(boolean childRemoveable)
	{
		this.childRemoveable = childRemoveable;
	}
	
	/**
	 * Saves an XML representation of the object to the output stream
	 * @param out
	 */
	public void writeToStream(OutputStream out) 
	{
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.writeObject(this);
		e.close();
		
	}
	
	
	
	
	public QuestData getQuest()
	{
		if(this instanceof QuestData)
		{
			return (QuestData)this;
		}
		if(parent ==null) { 
				return CKGameObjectsFacade.getQuest().getQuestData(); 
			}
		
		return  ((CKGUINode) parent).getQuest();
	}

	@Override
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit");
		menu.setEnabled(false);
		return menu;
	}

	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		JMenu menu = new JMenu("Add");
		menu.setEnabled(false);
		return menu;
	}

	public JPopupMenu getPopup(CKTreeGui tree)
	{
		CKGUINode myparent;
		if(parent==null) 	{ myparent=null; }
		else { myparent = (CKGUINode)parent; }
		
		JPopupMenu popup=new JPopupMenu();
		JMenuItem editAction= GUIEdit();
		popup.add(editAction);
		popup.add(new JSeparator());


		JMenuItem e2Action = new JMenuItem("Edit");
		e2Action.addActionListener(new TreeEditActionListener(tree));
		popup.add(e2Action);
		
		
		JMenuItem copyAction = new JMenuItem("Copy");
		copyAction.addActionListener(new TreeCopyActionListener(tree));
		popup.add(copyAction);
		
		JMenuItem pasteAction = new JMenuItem("Paste");
		if(tree.getCopyBuffer()!=null && this.pastableChildren ) { 	pasteAction.addActionListener(new TreePasteActionListener(tree));}
		else { pasteAction.setEnabled(false);}
		popup.add(pasteAction);

		JMenuItem removeAction = new JMenuItem("Remove");
		if(myparent!=null &&!myparent.childOrderLocked && myparent.childRemoveable)
		{removeAction.addActionListener(new TreeRemoveActionListener(tree));}
		else {removeAction.setEnabled(false); }
		popup.add(removeAction);
		
		JMenuItem moveUpAction = new JMenuItem("Move Action Up");
		if(myparent!=null &&!myparent.childOrderLocked && 
				myparent.childRemoveable && myparent.children!=null &&myparent.children.size()>0
				&& myparent.getFirstChild()!=null && myparent.getFirstChild() !=this)
		{moveUpAction.addActionListener(new TreeMoveUpActionListener(tree));}
		else {moveUpAction.setEnabled(false); }
		popup.add(moveUpAction);

		JMenuItem moveDownAction = new JMenuItem("Move Action Down");
		if(myparent!=null &&!myparent.childOrderLocked &&  
				myparent.children!=null &&myparent.children.size()>0 &&
				myparent.childRemoveable && myparent.getLastChild() !=this)
		{moveDownAction.addActionListener(new TreeMoveDownActionListener(tree));}
		else { moveDownAction.setEnabled(false); }
		popup.add(moveDownAction);

		popup.add(new JSeparator());
		JMenuItem addAction= GUIAddNode(tree);
		popup.add(addAction);
		
		return popup;
	}
	
	
	

	public class TreeRemoveActionListener implements ActionListener
	{
		public TreeRemoveActionListener(CKTreeGui tree)
		{
			super();
			this.tree = tree;
		}


		CKTreeGui tree;
	
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode node = tree.getSelected();
			tree.removeNode(node);
		}
	}
	
	public class TreeCopyActionListener implements ActionListener
	{
		public TreeCopyActionListener(CKTreeGui tree)
		{
			super();
			this.tree = tree;
		}

		CKTreeGui tree;
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			tree.setCopyBuffer(CKTreeGui.copyTree( tree.getSelected() ) );
		}

	}
	public class TreeEditActionListener implements ActionListener
	{

		CKTreeGui tree;
				
		public TreeEditActionListener(CKTreeGui tree)
		{
			super();
			this.tree = tree;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			tree.startEditing();
		}

	}
	
	public class TreePasteActionListener implements ActionListener
	{
		CKTreeGui tree;
		
		public TreePasteActionListener(CKTreeGui tree)
		{
			super();
			this.tree = tree;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(tree.getCopyBuffer() != null)
			{
			DefaultMutableTreeNode	parent	= tree.getSelected();
			tree.addNode(parent, CKTreeGui.copyTree(tree.getCopyBuffer()));
			}
		}
	}

	public class TreeMoveUpActionListener implements ActionListener
	{
		CKTreeGui tree;
		
		public TreeMoveUpActionListener(CKTreeGui tree)
		{
			super();
			this.tree = tree;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			tree.moveNode(node,true);			
		}
			
	}


	public class TreeMoveDownActionListener implements ActionListener
	{
		CKTreeGui tree;
		
		public TreeMoveDownActionListener(CKTreeGui tree)
		{
			super();
			this.tree = tree;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			tree.moveNode(node,false);			
		}
			
	}

	
	
	//put the class here for the containers..
	
	private  final static JLabel editComponent=new JLabel();
	private  final static JLabel renderComponent=new JLabel();
	
	
	/**Returns the icon that should be used with this GUINode
	 * @param leaf - is the node a leaf
	 * @param expanded - is the node expanded
	 * @return an Icon to use.
	 */
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		 if (leaf)
		 {
			 return UIManager.getIcon("Tree.leafIcon");
		 }
		 else if (expanded)
		 {
			 return UIManager.getIcon("Tree.openIcon");
		 }
		 else
		 {
			 return UIManager.getIcon("Tree.closedIcon");
		 }
	}

	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		editComponent.setText(value.toString());
		editComponent.setBackground(Color.green);
		editComponent.setForeground(Color.RED);
		return editComponent;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		renderComponent.setText(value.toString());
		return renderComponent;
		
	}

	public void storeComponentValues()
	{
	//default is to do nothing.	
		
	}


	protected final static int EDIT=0;
	protected final static int RENDER=1;
	protected final static Color[] colors={Color.GREEN,Color.WHITE};
	
	
	protected String initializeSpellBox(JComboBox<Object> box,String chapter,String page)
	{
		String safeName = "";
		String name = chapter+" : "+page;
		//fill this wit hPhilips code..
		String[] spells = 
			{
			"Fire : bolt","Fire : Blaze	" ,
			"Voice : Talk", "Voice : Hack","Voice : Lie"					
		};
		
		box.removeAllItems();
		for(String s:spells)
		{ 		//need to be careful about the name, it might not equal the values.
			box.addItem(s);
			//System.out.println("Comparing ("+s+") ("+name+")");
			if(s.compareTo(name)==0)
			{
				//System.out.println("MAtch");
				safeName = s;
				box.setSelectedItem(safeName);
			}
		}
		return safeName;
	}
	
	/*
	protected String initializeReactiveSpellBox(JComboBox box, String spell)
	{
		return initializeJComboBox(box, spell, null);
	}
	*/
	protected String initializeChapterBox(JComboBox<String> box,String chapter)
	{
		return initializeJComboBox(box,chapter,CKBookFactory.getInstance().getAllChapters());
	}
	
	
	protected String initializeEditableChapterBox(JComboBox<String> box,String chapter)
	{
		return initializeEditableJComboBox(box,chapter,CKBookFactory.getInstance().getAllChapters());
	}

	
	protected String initializePageBox(JComboBox<String> box,String chapter,String page)
	{
		return initializeJComboBox(box,page,CKBookFactory.getInstance().getAllPages(chapter));
	}
	
	protected String initializeEditablePageBox(JComboBox<String> box,String chapter,String page)
	{
		return initializeEditableJComboBox(box,page,CKBookFactory.getInstance().getAllPages(chapter));
	}

	
	protected <T extends CKXMLAsset<T>> void initializeJComboBoxByID(JComboBox<T> box,Iterator<T> iter,String ID)
	{
		box.removeAllItems();
		
		while(iter.hasNext())
		{
			T s = iter.next();
			box.addItem(s);
			if(s.getAID().compareTo(ID)==0)
			{
				box.setSelectedItem(s);
			}
		}
	
	}
	
	protected String initializeJComboBox(JComboBox<String> box,String name,String[] catagories)
	{
		String safeName = "";
	
		if(catagories.length>0)
		{
			safeName = catagories[0];
		}
		//String[] actors = getQuest().getActors().getActorNames();
		//nameModel.setList(actors);
		box.removeAllItems();
		for(String s:catagories)
		{ 		//need to be careful about the name, it might not equal the values.
			box.addItem(s);
			if(s.compareToIgnoreCase(name)==0)
			{
				safeName = s;
				box.setSelectedItem(safeName);
			}
		}
		return safeName;
	}
	
	protected String initializeEditableJComboBox(JComboBox<String> box,String name,String[] catagories)
	{
		//String safeName = catagories[0];
		//String[] actors = getQuest().getActors().getActorNames();
		//nameModel.setList(actors);
		box.removeAllItems();
		box.addItem(name);
		box.setSelectedItem(name);
		for(String s:catagories)
		{ 		//need to be careful about the name, it might not equal the values.
			if(s.compareToIgnoreCase(name)!=0)
			{
				box.addItem(s);
			}
			if(s.compareToIgnoreCase(name)==0)
			{
				//safeName = s;
				box.setSelectedItem(s);
			}
		}
		return name;
	}
	

	
	protected String initializeActorBox(JComboBox<String> box,String name)
	{
		String[] actors = getQuest().getActors().getActorNames();

		return initializeJComboBox(box,name,actors);
	}
	
	protected String initializeArtifactBox(JComboBox<String> box,
			String actor,String artifact)
	{
		String [] artifacts = getQuest().getActor(actor).getTeam().getArtifacts()
				.stream().map(a->a.getName()).toArray(size->new String[size]);
		
		return initializeJComboBox(box,artifact,artifacts);
	}
	
	protected SpinnerNumberModel[] generatePositionModels()
	{
		SpinnerNumberModel[] model = new SpinnerNumberModel[2];
		model[0] = new SpinnerNumberModel(0,0,100,1);
		model[1] = new SpinnerNumberModel(0,0,100,1);
		return model;		
	}
	
	protected SpinnerNumberModel[] generateNumberModels()
	{
		SpinnerNumberModel[] model = new SpinnerNumberModel[2];
		model[0] = new SpinnerNumberModel(0,-100,100,1);
		model[1] = new SpinnerNumberModel(0,-100,100,1);
		return model;		
	}

	protected SpinnerNumberModel[] generateDoubleNumberModels()
	{
		SpinnerNumberModel[] model = new SpinnerNumberModel[2];
		model[0] = new SpinnerNumberModel(0,-100,100,.1);
		model[1] = new SpinnerNumberModel(0,-100,100,.1);
		return model;		
	}

	protected void initializePositionModels(SpinnerNumberModel[] spin,CKPosition pos)
	{
		spin[0].setValue(pos.getX());
		spin[1].setValue(pos.getY());
	}
	
	protected void readPositionModels(SpinnerNumberModel[] spin,CKPosition pos)
	{		
		pos.setX(spin[0].getNumber().doubleValue());
		pos.setY( spin[1].getNumber().doubleValue());
	}



	public static CKGUINode readFromStream(InputStream in)
	{
		XMLDecoder d = new XMLDecoder(in);
		CKGUINode node = (CKGUINode) d.readObject();
		d.close();
		return node;
		
	}


	
}
