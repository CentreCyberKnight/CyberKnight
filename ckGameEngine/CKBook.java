package ckGameEngine;

import java.awt.Color;
import java.awt.Component;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKBookFactory;
import ckEditor.CKGUINodePropertiesEditor;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTreeGui;

/**
 * @author dragonlord
 *
 */
public class CKBook extends CKGUINode implements CKXMLAsset<CKBook>
{

	private static final long serialVersionUID = -7796753128865684837L;
	private  LinkedHashMap<String,CKChapter> map;
	private String name;
	private int ID;
	private String AID = "";

	public CKBook()
	{
		map = new LinkedHashMap<String,CKChapter>();
		name = "";
		ID = -1;
		setAllowsChildren(true);
	}
	
	
	public CKBook(String bookName,String chapter,int val,String page)
	{
		this();
		this.name=bookName;
		addChapter(new CKChapter(chapter,val,page));		
	}
	
	
	
	public CKBook(String name, String chap, int val)
	{
		this();
		this.name=name;
		addChapter(new CKChapter(chap,val));
	}


	public CKBook(String name)
	{
		this();
		this.name=name;
	}


	public int getID()
	{
		return this.ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Iterator<CKChapter> getChapters()
	{
		return map.values().iterator();
	}
	
	public CKChapter getChapter(String s)
	{
		CKChapter c = map.get(s.toLowerCase());
		if(c==null)
		{
			System.err.println("Chapter "+s+" does not exist");
			c = new CKChapter();
		}
		return c;
	}
	
	
	
	
	
	
	
	public void addChapter(CKChapter a)
	{
		/*CKChapter ch = (CKChapter) a.clone();
		addIT(a);
		map.put(ch.getName(), ch);
		*/				
		if(! hasChapter(a.getName()))
		{	
			add(a);
		}
		else
		{
			getChapter(a.getName()).addChapter(a);
		}
	}
	
	
	public void removeChapter(String s)
	{
		CKChapter ch = getChapter(s);
		remove(ch);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof CKChapter)
		{
			CKChapter ch = (CKChapter) ((CKChapter) newChild).clone();
			super.insertIT(ch, childIndex);
			map.put(ch.getName().toLowerCase(),ch);
		}		
		
	}


	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#remove(int)
	 */
	@Override
	public void remove(int childIndex)
	{
		CKChapter ch = (CKChapter) this.getChildAt(childIndex);
		super.remove(childIndex);
		map.remove(ch.getName().toLowerCase());
		
	}


	


	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#removeAllChildren()
	 */
	@Override
	public void removeAllChildren()
	{
		map.clear();
		super.removeAllChildren();
	}


	


	public boolean hasChapter(String s)
	{
		return map.containsKey(s.toLowerCase());
	}
	
	public boolean hasPage(String att,String abl)
	{
		if(hasChapter(att))
		{
			return map.get(att.toLowerCase()).hasPage(abl);
		}
		return false;
	}
	
	
	public boolean meetsRequirements(CKBook reqs)
	{
		for(CKChapter ch:reqs.map.values())
		{
			if ( !meetsRequirements(ch)) {return false;}			
		}		
		return true;
	}
	
	
	public boolean meetsRequirements(CKChapter chapter)
	{
		String name = chapter.getName();
		if(meetsRequirements(name, chapter.getValue() ))
		{
			Iterator<CKPage> iter = chapter.getPages();
			while(iter.hasNext())
			{
				if(!hasPage(name,iter.next().getName())) { return false;}
			}			
		}
		else
		{
			return false;
		}
		return true;
	}
	
	
	public boolean meetsRequirements(String chapter,int value,String page)
	{
		
			if (hasPage(chapter,page))
			{
				CKChapter ch = getChapter(chapter);
				return ch.getValue() >=value;
			}
			return false;	
	}
	
	public boolean meetsRequirements(String chapter,int value)
	{
		
			if (hasChapter(chapter))
			{
				CKChapter ch = getChapter(chapter);
				return ch.getValue() >=value;
			}
			return false;	
	}
	
	public boolean meetsRequirements(Iterator<CKBook> requirements)
	{
		while(requirements.hasNext())
		{
			if(meetsRequirements(requirements.next())) { return true; } 
		}
		return false;

	}
	
	
	public static void addToBook(CKBook to, CKBook from)
	{
		Set<Entry<String, CKChapter>> set = from.map.entrySet();
		
		for(Entry<String,CKChapter> entity: set)
		{
			String key = entity.getKey();
			if(to.hasChapter(key))
			{//need to add them
				to.getChapter(key).addChapter(entity.getValue());
			}
			else
			{
				to.addChapter(new CKChapter(entity.getValue()));
			}
		}		
	}
	
	public static CKBook addBooks(CKBook A, CKBook B)
	{
		CKBook set = new CKBook();
		addToBook(set,A);
		addToBook(set,B);		
		return set;
	}
	
	public static CKBook addBooks(Iterator<CKBook> iter)
	{
		CKBook set = new CKBook();
		while(iter.hasNext())
		{
			addToBook(set,iter.next());
		}
		return set;
	}
	
	
	public String treeString()
	{
		String s = "CKBook\n\n";
		
		Set<Entry<String, CKChapter>> set = map.entrySet();
		
		for(Entry<String,CKChapter> entity: set)
		{
			s+=entity.getValue().treeString();
		}		
	
		return s;
	}
	public String toString()
	{
		
		return "Book  "+name;
		
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
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CKBook)
		{
			CKBook s = (CKBook) obj;
			return map.equals(s.map);
		}
		return false;
	}



	/**
	 * Returns a new CKBook which has the minimum Bounds between the two
	 * input CKBooks.
	 * 
	 *  Note: the smaller book should be passes as A for efficiency.
	 * @param A
	 * @param B
	 * @return
	 */
	public static CKBook minBounds(CKBook A,CKBook B)
	{
		CKBook C = new CKBook();
		//System.out.println("Merging"+B.treeString() +A.toString());
		for(String ch : A.map.keySet())
		{
			if(B.hasChapter(ch))
			{
				
				C.addChapter(CKChapter.minBounds(A.getChapter(ch),B.getChapter(ch)));				
			}
		}
		return C;
		
	}
	
	/*
	 * Popup menu
	 * 
	 * 
	 */
	
	/* (non-Javadoc)
	 * @see ckEditor.treegui.CKGUINode#GUIAddNode(ckEditor.treegui.CKTreeGui)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		
		return CKBookFactory.getBookMenu(tree);
	}

	
	
	/*
	 *  GUI editors
	 *
	 */

	static JPanel []panel;
	static JTextField[] nameFields;
	
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
		
			
			//name
			nameFields = new JTextField[2];
			nameFields[0]=new JTextField(12);
			nameFields[1]=new JTextField(12);
			panel[0].add(nameFields[0]);
			panel[1].add(nameFields[1]);
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
		
		nameFields[index].setText(name);
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
		name = nameFields[EDIT].getText();
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
	
	
	
	
	
	
	
		
	
	public static void main(String [] args)
	{
		JFrame frame = new JFrame("Testing CKBook");
		CKGuiRoot root = new CKGuiRoot();
		CKChapter A;
		CKChapter A1;
		CKChapter B;
		//CKChapter B1;
		CKChapter C;
		A=new CKChapter("Levels",1);
		A.addPage(new CKPage("level1"));
		A1=new CKChapter("Levels",1);
		A1.addPage(new CKPage("level2"));

		B=new CKChapter("Move",3);
		B.addPage(new CKPage("forward"));
		B.addPage(new CKPage("turn_left"));
		B.addPage(new CKPage("turn right"));
		
		C=new CKChapter("Fire",0);
		CKBook S1 = new CKBook("Tester");
		S1.addChapter(A);
		S1.addChapter(B);
		S1.addChapter(C);
		
		root.add(S1 );
		XMLEncoder e;
		try
		{
			e = new XMLEncoder(
			        new BufferedOutputStream(
			            new FileOutputStream("Test.xml")));
			e.writeObject(S1);
			e.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		CKBook S2 = new CKBook();
		
		try
		{
			 XMLDecoder d = new XMLDecoder(
                     new BufferedInputStream(
                         new FileInputStream("Test.xml")));
			 S2 = (CKBook) d.readObject();
			 d.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		root.add(S2);
		
		
		frame.add(new CKTreeGui(root));
		//frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	@Override
	public String getAID() 
	{
		return AID;
	}


	@Override
	public void setAID(String a) 
	{
		AID=a;
	}


	@Override
	public JComponent getXMLAssetViewer() 
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}


	@Override
	public JComponent getXMLAssetViewer(ViewEnum v) 
	{
		return new CKTreeGui(this,false);
	}


	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKBook> getXMLPropertiesEditor() 
	{
		return new CKGUINodePropertiesEditor<CKBook>(this);
	}
	
	
}
