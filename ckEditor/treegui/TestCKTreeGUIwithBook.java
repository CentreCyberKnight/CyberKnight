package ckEditor.treegui;

import javax.swing.JFrame;

import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKPage;

public class TestCKTreeGUIwithBook {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
			
		CKChapter A=new CKChapter("Levels",1);
		A.addPage(new CKPage("level1"));
		CKChapter A1=new CKChapter("Levels",1);
		A1.addPage(new CKPage("level2"));

		CKChapter B=new CKChapter("Move",3);
		B.addPage(new CKPage("forward"));
		B.addPage(new CKPage("turn_left"));
		B.addPage(new CKPage("turn right"));
		
		//CKChapter C=new CKChapter("Fire",0);
		CKBook S1 = new CKBook();
		S1.addChapter(A);
		S1.addChapter(B);

		frame.add(new CKTreeGui());
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

}
