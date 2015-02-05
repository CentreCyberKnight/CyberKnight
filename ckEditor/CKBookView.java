package ckEditor;

import java.awt.Dimension;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.tree.TreePath;

import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKBook;
import ckGameEngine.CKDeltaBook;
import ckGameEngine.CKDeltaPage;
import ckGameEngine.CKStatsChangeListener;


/**
 * place holder class for now.  This will provide a cleaner way to draw non-editable data
 * 
 * @author bradshaw
 *
 */
public class CKBookView extends CKTreeGui //implements CKStatsChangeListener 
{
	
	private static final long serialVersionUID = 3318955326087095017L;
	CKBook book;
	//JScrollPane areaScrollPane;
	//JTextArea area;
	//FIXME - use treenodes to make this look nicer
	public CKBookView(CKBook book)
	{
		super(book,false);
		this.tree.setRootVisible(false);
		setRoot(book);
		/*
		area =  new JTextArea(book.treeString());
		
		areaScrollPane = new JScrollPane(area);
		areaScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(250, 200));
		add(areaScrollPane);
	*/
		//add(book.getXMLAssetViewer());
	}

	
	
	/* (non-Javadoc)
	 * @see ckEditor.treegui.CKTreeGui#setRoot(ckEditor.treegui.CKGUINode)
	 */
	@Override
	public void setRoot(CKGUINode node)
	{
		setBook((CKBook) node);
	}



	public void setBook(CKBook book)
	{
		this.book = book;
		
		//super(new CKDeltaBook(book),false);
		if(book instanceof CKDeltaBook)
		{
			super.setRoot(book);

		}
		else
		{
			super.setRoot(new CKDeltaBook((CKBook) book));
		}
		
		CKDeltaBook b = (CKDeltaBook) getRoot();
		@SuppressWarnings("unchecked")
		Enumeration<Object> nodes = b.depthFirstEnumeration();
		while(nodes.hasMoreElements())
		{
			Object n = nodes.nextElement();
			if(n instanceof CKDeltaPage)
			{
				CKDeltaPage p = (CKDeltaPage) n;
				if(p.getType() != CKDeltaBook.DELTA_TYPES.NO_CHANGE)
				{
					TreePath path = new TreePath(p.getPath());
					tree.makeVisible(path);
				}
				
				
			}
			
		}

	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	/*@Override
	public void setPreferredSize(Dimension preferredSize)
	{
		areaScrollPane.setPreferredSize(preferredSize);
		super.setPreferredSize(preferredSize);
	}
*/
	/*@Override
	public void equippedChanged() {	} //don't care :)
	@Override
	public void statsChanged(CKBook stats)
	{
		book = stats;
//		removeAll();
	area.setText(book.treeString());
//		add(book.getXMLAssetViewer());
	}
	*/
	
}
