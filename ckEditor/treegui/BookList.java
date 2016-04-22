package ckEditor.treegui;

import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;
import static ckCommonUtils.CKPropertyStrings.P_SHOES;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

//import com.sun.xml.internal.fastinfoset.stax.events.EmptyIterator;
import org.apache.commons.collections15.iterators.EmptyIterator;

import ckGameEngine.CKBook;


public class BookList extends CKGUINode implements Iterable<CKBook>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8232016573022629841L;

	public BookList()
	{
	}
	
	public BookList(Iterator<CKBook> iter)
	{
		while(iter.hasNext())
		{
			add(iter.next());
		}
	}

	public BookList(CKBook[] array)
	{
		for(int i=0;i<array.length;i++)
		{
			add(array[i]);
		}
	}



	public BookList(CKBook requirements)
	{
		add(requirements);
	}

	public CKBook getBook(String name)
	{
		for (int i=0;i<getChildCount();i++)
		{
			CKBook book = (CKBook) getChildAt(i);
			if(book.getName().compareTo(name)==0)
			{
				return book;
			}			
		}
		return null;
		
	}
	
	public List<String> getBookList()
	{
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0;i<getChildCount();i++)
		{
			CKBook book = (CKBook) getChildAt(i);
			list.add(book.getName());
		}
		return list;		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof CKBook)
		{
			super.insert(newChild, childIndex);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		DefaultMutableTreeNode node= (DefaultMutableTreeNode) super.clone();
		if(children != null)
		{
			for(Object o: children)
			{
				//	Satisfies s = (Satisfies) o;
				node.add( (MutableTreeNode) ((CKBook) o ).clone());
			}
		}
		return node;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Book List ";
	}
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#GUIAddNode(ckGraphics.treegui.CKTreeGui)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
			
			JMenuItem addActor = new JMenuItem("Add Book");
			addActor.addActionListener(new BookAddTriggerListener(tree));
			
			return addActor;
		}
	
	
	@SuppressWarnings("unchecked")
	public Iterator<CKBook> iterator()
	{
		if(children!=null)
		{
			return children.iterator();
		}
		return EmptyIterator.getInstance();
	}

	
class BookAddTriggerListener implements ActionListener
{
	
	    CKTreeGui tree;
		
		public BookAddTriggerListener(CKTreeGui t)
		{
			tree=t;
		}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)
			{
			//	node = (DefaultMutableTreeNode) treeModel.getRoot();
				return;
			}	
			tree.addNode(node,new CKBook("Unnamed"));		
		}
			
}

	public static void main(String[] args)
	{

	
		CKBook[] reqs = { new CKBook("Requirements", CH_EQUIP_SLOTS, 0, P_SHOES) };
		BookList list = new BookList(reqs);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		list.writeToStream(baos);

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		System.out.println(new String(baos.toByteArray()));

		XMLDecoder xmldecoder = new XMLDecoder(bais);
		//@SuppressWarnings("unchecked")
		//BookList NewBoots = (BookList) xmldecoder.readObject();

		xmldecoder.close();
	}
}
