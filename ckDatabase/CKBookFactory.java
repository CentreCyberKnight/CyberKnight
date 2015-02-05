package ckDatabase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import ckCommonUtils.CKPropertyStrings;
import ckCommonUtils.CKURL;
import ckEditor.treegui.CKSingleParent;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKPage;

public class CKBookFactory extends CKXMLFactory<CKBook>
{
	private static CKBookFactory instance;
	private CKBook masterBook;
	
	private CKBookFactory() 
	{
		try
		{
			CKURL u = new CKURL(XMLDirectories.MASTER_BOOK);
			masterBook = (CKBook) CKBook.readFromStream(u.getInputStream());
		} catch (IOException e)
		{
			masterBook=new CKBook("Master Book");
		}
	}
	
	private void saveMasterBook()
	{
		try
		{
			CKURL u = new CKURL(XMLDirectories.MASTER_BOOK);
			masterBook.writeToStream(u.getOutputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	public synchronized static CKBookFactory getInstance()
	{
		if(instance ==null)
		{
			instance = new CKBookFactory();
		}
		return instance;	
	}
	
	
	public String[] getAllPages(String chapterName)
	{
		CKChapter chap = masterBook.getChapter(chapterName);
		Vector<String> names = new Vector<String>();
		names.add(CKPropertyStrings.P_ANY);
		Iterator<CKPage> pages = chap.getPages();
		while(pages.hasNext())
		{
			names.add(pages.next().getName());
		}
		return names.toArray(new String[names.size()]);
	}
	
	public String[] getAllPages(CKChapter chapter)
	{
		return getAllPages(chapter.getName());
		
	}


	public void addPage(String parentName, String childName)
	{
		CKChapter chap = masterBook.getChapter(parentName);
		chap.addPage(new CKPage(childName));
		this.saveMasterBook();
	}


	public String[] getAllChapters()
	{
		Vector<String> names = new Vector<String>();
		names.add(CKPropertyStrings.P_ANY);
		Iterator<CKChapter> chapters = masterBook.getChapters();
		while(chapters.hasNext())
		{
			names.add(chapters.next().getName());
		}
		return names.toArray(new String[names.size()]);
	}


	public void addChapter(String newName)
	{
		masterBook.addChapter(new CKChapter(newName,0));
		saveMasterBook();
	}
	
	


	public static JMenu getBookMenu(CKTreeGui tree)
	{
		JMenu addActions = new JMenu("Add Chapter");
		/*        Add Actions --should these be based on the item?*/
		JMenuItem addSeq = new JMenuItem("Add New Chapter");
		addSeq.addActionListener(new AddNewChapterAction());
		addActions.add(addSeq);
	
		addActions.add(new JSeparator());
		
		String [] chapters = getInstance().getAllChapters();
		for(String s: chapters)
		{
			JMenuItem addChap = new JMenuItem(s);
			addChap.addActionListener(new TreeAddChapterListener(s,tree));
			addActions.add(addChap);
		}	
	
			return addActions;
	}
	
	
static class TreeAddChapterListener implements ActionListener
{
		String name;
		CKTreeGui tree;
		
		public TreeAddChapterListener(String n,CKTreeGui t)
		{
			name = n;
			tree=t;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)
			{
			
				return;
			}
			tree.addNode(node,new CKChapter(name,0) );		
		}
}		




static class AddNewChapterAction implements ActionListener
{
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String newName = (String)JOptionPane.showInputDialog(
		                    null,
		                    "What is the New Chapter Name?  ",
		                    "Create a new Chapter",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    "");
		if(newName != null && newName.length()>0)
		{
			newName = newName.toUpperCase();
			CKBookFactory.getInstance().addChapter(newName);
		}
		else if(newName != null)
		{
			JOptionPane.showMessageDialog(null,
				    "Empty String is not acceptable");
		}		
	}
	

}






public static JMenu getChapterMenu(CKTreeGui tree,CKChapter chapter)
{
	JMenu addActions = new JMenu("Add Page");
	/*        Add Actions --should these be based on the item?*/
	JMenuItem addSeq = new JMenuItem("Add New Page");
	addSeq.addActionListener(new AddNewPageAction(chapter.getName()));
	addActions.add(addSeq);

	addActions.add(new JSeparator());
	
	String [] chapters = getInstance().getAllPages(chapter);
	for(String s: chapters)
	{
		JMenuItem addChap = new JMenuItem(s);
		addChap.addActionListener(new TreeAddPageListener(s,tree));
		addActions.add(addChap);
	}	

		return addActions;
}


static class TreeAddPageListener implements ActionListener
{
	String name;
	CKTreeGui tree;
	
	public TreeAddPageListener(String n,CKTreeGui t)
	{
		name = n;
		tree=t;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		DefaultMutableTreeNode	node	= tree.getSelected();
		
		if(node == null)
		{
		
			return;
		}
		tree.addNode(node,new CKPage(name) );		
	}
}		




	static class AddNewPageAction implements ActionListener
	{
		
		String chapter;

		public AddNewPageAction(String c)
		{
			chapter = c;
		}


		@Override
		public void actionPerformed(ActionEvent e)
		{
			String newName = (String)JOptionPane.showInputDialog(
	                    null,
	                    "What is the New Page Name for Chapter: "+chapter,
	                    "Create a new Page",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    "");
	if(newName != null && newName.length()>0)
	{
		newName = newName.toUpperCase();
		CKBookFactory.getInstance().addPage(chapter,newName);
	}
	else if(newName != null)
	{
		JOptionPane.showMessageDialog(null,
			    "Empty String is not acceptable");
	}		
}


}



	
	
	
	
	
	
	
	

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		
		CKTreeGui tree = new CKTreeGui(new CKSingleParent(new CKBook("Toad")));
		
		frame.add(tree);
		frame.pack();
		frame.setVisible(true);
		
		 frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	@Override
	public String getBaseDir() 
	{
		return XMLDirectories.BOOK_DIR;
	}

	@Override
	public CKBook getAssetInstance() 
	{
		return new CKBook("blank");
	}

}
