package ckGameEngine;

import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckDatabase.CKBookFactory;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKTreeGui;

public class CKChapter extends CKGUINode
{
	
	private static final long serialVersionUID = 226734356051749629L;
	private HashMap<String,CKPage> pages;
	private String name;
	private int value;
	
	public CKChapter(String n,int v)
	{
		name = n;
		value = v;
		pages = new HashMap<String,CKPage>();
		setAllowsChildren(true);	
	}

	public CKChapter()
	{
		this("",0);
	}
	
	public CKChapter(String n,int v,String[]p)
	{
		this(n,v);
		for(String s:p)
		{
			addPage(new CKPage(s));
		}		
	}


	public CKChapter(String n,int v,String p)
	{
		this(n,v);
		addPage(new CKPage(p));
	}

	public CKChapter(CKChapter sec)
	{
		this(sec.name,sec.value);
		for (Entry<String,CKPage> p:sec.pages.entrySet())
		{
			addPage(p.getValue());
		}
//		pages.putAll(sec.pages);
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	@Override
	public String toString() {
		return "Chapter: "+name + " " + value;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value)
	{
		this.value = value;
	}

	public void addChapter(CKChapter att)
	{//should be ok but check anyway
		if(name.compareTo(att.name) == 0 && this != att)
		{
			value +=att.value;
			for (Entry<String,CKPage> p:att.pages.entrySet())
			{
				addPage(p.getValue());
			}			
			//pages.putAll(att.pages);
		}
	}
	


	public Iterator<CKPage> getPages()
	{
		return pages.values().iterator();
	}
	//need to talk to dr. b about checking this
	public Collection<CKPage> getVectorPages()
	{
		return pages.values();
		
	}
	
	

	public CKPage getPage(String s)
	{
		return pages.get(s.toLowerCase());
		
	}

	
	public void addPage(CKPage a)
	{
		add(a);

	}
	
	public void addPage(String s)
	{
		addPage(new CKPage(s));
	}
	
	
	public void removePage(String s)
	{
		//privateRemovePage(s);
		if(s.compareToIgnoreCase("*")==0)
		{
			this.removeAllChildren();
		}
		CKPage p = getPage(s);
		if(p !=null)
		{
			pages.remove(s);
			remove(p);
		}
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof CKPage)
		{
			CKPage ch = (CKPage) ((CKPage) newChild).clone();
			if( ! hasPage(ch.getName()))
			{
				super.insertIT(ch, childIndex);
				pages.put(ch.getName().toLowerCase(),ch);
			}
			//System.out.println("insert   children:"+getChildCount());
		}		
		
	}


	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#remove(int)
	 */
	@Override
	public void remove(int childIndex)
	{
		System.out.println("removing node pos"+childIndex);

		CKPage p = (CKPage) this.getChildAt(childIndex);
		if(p !=null)
			{
				super.remove(childIndex);
				pages.remove(p.getName().toLowerCase());
			}
		}
	

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#removeAllChildren()
	 */
	@Override
	public void removeAllChildren()
	{
		pages.clear();
		super.removeAllChildren();
	}


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
	
	
	
	
	
	
	boolean hasPage(CKPage ability)
	{
		return hasPage(ability.getName());
	}
	
	boolean hasPage(String s)
	{
		return pages.containsKey(s.toLowerCase());
	}

	
	public String treeString()
	{
		String s = toString() + "\n";
		Set<Entry<String, CKPage>> set = pages.entrySet();

		for (Entry<String, CKPage> entity : set)
		{
			s += entity.getValue().treeString();
		}
	
		
		return s;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		
		if(obj instanceof CKChapter)
		{
			CKChapter o = (CKChapter) obj;
			if(name.compareTo(o.name)!=0 || value != o.value)
			{
				return false;
			}
			return pages.equals(o.pages);
		
		}
		return false;
	}

	public static CKChapter minBounds(CKChapter A, CKChapter B)
	{
		if(A.getName().compareTo(B.getName())!=0)
		{
			return null;
		}
		CKChapter C = new CKChapter(A.getName(),Math.min(A.getValue(),B.getValue()));
		for (CKPage p:A.pages.values())
		{
			if(B.hasPage(p))
			{
				C.addPage(p);
			}
		}
		return C;
	}

	
	/*
	 * 
	 * Popup actions
	 * 
	 */

	/* (non-Javadoc)
	 * @see ckEditor.treegui.CKGUINode#GUIAddNode(ckEditor.treegui.CKTreeGui)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		return CKBookFactory.getChapterMenu(tree, this);
	}

	
	
	
	/*
	 *  GUI editors
	 *
	 */

	static JPanel []panel;
	
	static JLabel []labels;
/*	static JComboBox[] names;
	static JButton[]  addNewNames;
	*/	
	static SpinnerNumberModel []values; 
	
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.LINE_AXIS));
			panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.LINE_AXIS));

			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
		
			
			labels = new JLabel[2];
			labels[0] = new JLabel();
			labels[1] = new JLabel();
			panel[0].add(labels[0]);
			panel[1].add(labels[1]);
			//name
			/*
			names= new JComboBox[2];
			names[0] = new JComboBox();
			names[1] = new JComboBox();
			//name
		//	nameFields = new JTextField[2];
		//	nameFields[0]=new JTextField(12);
		//	nameFields[1]=new JTextField(12);
			panel[0].add(names[0]);
			panel[1].add(names[1]);
			
			
			*/
			//values
			values=new SpinnerNumberModel[2];			
			values[0] = new SpinnerNumberModel(0, -100, 100,1);
			JSpinner spin = new JSpinner(values[0]);
			panel[0].add(spin);
			values[1] = new SpinnerNumberModel(0, -100, 100,1);
			spin = new JSpinner(values[1]);
			panel[1].add(spin);
			/*
			addNewNames = new JButton[2];
			addNewNames[0] = new JButton("Add New");
			addNewNames[1] =  new JButton("Add New");
			panel[0].add(addNewNames[0]);
			panel[1].add(addNewNames[1]);			
			*/
		}
					
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);
		
//		String[] chapterNames = CKBookFactory.getInstance().getAllChapters();

//		name = initializeJComboBox(names[index],name,chapterNames);
		labels[index].setText("Chapter:"+name);
		
		values[index].setValue(value);	
		
		panel[index].validate();
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
//		name = (String)names[EDIT].getSelectedItem();
		value = values[EDIT].getNumber().intValue();
		
		//if there are children that don't belong they need to be removed....
	/*	
		String [] pages = CKBookFactory.getInstance().getAllPages(this);
		if(children==null) 	{ 	return; 	}

		Iterator<Object> objs = children.iterator();
		while(objs.hasNext())
		{
			CKPage p = (CKPage) objs.next();
			String pName = p.getName();
			boolean found = false;
			for(int i=0;i<pages.length;i++)
			{
				if(pName.compareTo(pages[i])==0)
				{
					found=true;
					break;
				}
			}
			if(! found) { 	objs.remove(); }
		}
		*/
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}
	
}	
