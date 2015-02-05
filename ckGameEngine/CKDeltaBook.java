package ckGameEngine;

import java.util.Iterator;

import javax.swing.JFrame;

import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKTreeGui;



public class CKDeltaBook extends CKBook
{


	private static final long serialVersionUID = 7770164083311623775L;

	public enum DELTA_TYPES {ADDED,REMOVED,INCREASED,DECREASED,NO_CHANGE};
	
	
	public CKDeltaBook( CKBook before,CKBook after)
	{
		Iterator<CKChapter> iter=before.getChapters();
		while(iter.hasNext())
		{
			CKChapter chap = iter.next();
			
			CKDeltaChapter dChap = 
					new CKDeltaChapter(chap.getName(),chap.getValue(),
							DELTA_TYPES.REMOVED);
			
			Iterator<CKPage> pIter = chap.getPages();
			while(pIter.hasNext())
			{
				CKPage page = pIter.next();
				dChap.addPage(new CKDeltaPage(page.getName(),DELTA_TYPES.REMOVED));
			}
			addChapter(dChap);
		}
		//now to see what comes after
		iter=after.getChapters();
		while(iter.hasNext())
		{
			CKChapter chap = iter.next();
			CKDeltaChapter dChap;
			
			if(hasChapter(chap.getName()))
			{
				dChap = (CKDeltaChapter) getChapter(chap.getName());
				int diff = chap.getValue() - dChap.getValue(); 
				dChap.setAmount(diff);
				dChap.setValue(chap.getValue());
				if(diff==0)       { dChap.setType(DELTA_TYPES.NO_CHANGE); }
				else if(diff<0) { dChap.setType(DELTA_TYPES.DECREASED);}
				else				   { dChap.setType(DELTA_TYPES.INCREASED); }
			}
			else //add it
			{
				dChap = new CKDeltaChapter(chap.getName(),chap.getValue(),DELTA_TYPES.ADDED);
				addChapter(dChap);
			}
			
			Iterator<CKPage> pIter = chap.getPages();
			while(pIter.hasNext())
			{
				CKPage page = pIter.next();
				if(dChap.hasPage(page.getName()))
				{
					((CKDeltaPage) dChap.getPage(page.getName())).setType(DELTA_TYPES.NO_CHANGE);
				}
				else
				{
					dChap = (CKDeltaChapter) getChapter(chap.getName());
					dChap.addPage(new CKDeltaPage(page.getName(),DELTA_TYPES.ADDED));
				}
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	//just does a normal book
	public CKDeltaBook(CKBook book)
	{
		Iterator<CKChapter> iter=book.getChapters();
		while(iter.hasNext())
		{
			CKChapter chap = iter.next();
			
			CKDeltaChapter dChap = 
					new CKDeltaChapter(chap.getName(),chap.getValue(),
							DELTA_TYPES.NO_CHANGE);
			
			Iterator<CKPage> pIter = chap.getPages();
			while(pIter.hasNext())
			{
				CKPage page = pIter.next();
				dChap.addPage(new CKDeltaPage(page.getName(),DELTA_TYPES.NO_CHANGE));
			}
			addChapter(dChap);
		}
	
	
	}



	
	
	
	
	
	
	
	public static void main(String[] args)
	{
		CKChapter A;
		CKChapter A1;
		CKChapter B;
		// CKChapter B1;
		CKChapter C;
		A = new CKChapter("Levels", 1);
		A.addPage(new CKPage("level1"));
		A1 = new CKChapter("Levels", 4);
		A1.addPage(new CKPage("level2"));

		B = new CKChapter("Move", 3);
		B.addPage(new CKPage("forward"));
		B.addPage(new CKPage("turn_left"));
		B.addPage(new CKPage("turn right"));

		C = new CKChapter("Fire", 1);
		C.addPage(new CKPage("burn"));
		CKBook S1 = new CKBook("Before");
		S1.addChapter(A);
		S1.addChapter(C);
		
		
		System.out.println(S1.treeString());
		
		CKBook S2 = new CKBook("After");
		S2.addChapter(A1);
		S2.addChapter(B);
		
		System.out.println(S2.treeString());
		
		CKDeltaBook d = new CKDeltaBook(S1,S2);
		
//		System.out.println(d.treeString());
		
		JFrame frame = new JFrame();
		frame.add(new CKTreeGui(d));
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		System.out.println(d.treeString());
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKBook> getXMLPropertiesEditor()
	{
		return super.getXMLPropertiesEditor();
	}

}
