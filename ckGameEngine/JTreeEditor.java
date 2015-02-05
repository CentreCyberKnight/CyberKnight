/**
 * 
 */
package ckGameEngine;

import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Phillip
 *
 */



public class JTreeEditor {
	
public static void main(String[]args)
{
	CKChapter A=new CKChapter("Levels",1);
	A.addPage(new CKPage("level1"));
	CKChapter A1=new CKChapter("Levels",1);
	A1.addPage(new CKPage("level2"));

	CKChapter B=new CKChapter("Move",3);
	B.addPage(new CKPage("forward"));
	B.addPage(new CKPage("turn_left"));
	B.addPage(new CKPage("turn right"));
	
	CKChapter C = new CKChapter("Fire",0);
	CKBook S1 = new CKBook();
	S1.addChapter(A);
	S1.addChapter(B);
	S1.addChapter(C);
	
	//JTreeEditor editor = new JTreeEditor(S1);
}

public JTreeEditor(CKBook set)
{

JFrame window = new JFrame("Action and Attribute Editor");

DefaultMutableTreeNode top = this.returnTree(set);
JTree tree = new JTree(top);
JScrollPane editorView = new JScrollPane(tree);


window.add(editorView);
window.pack();
window.setVisible(true);
}

public DefaultMutableTreeNode returnTree(CKBook set) 
{
		//Create top tree node
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(set);
	
		Iterator<CKChapter> itr = set.getChapters();
		while(itr.hasNext())
		{
			//Create tree node for each CKAttribute
			CKChapter attribute = itr.next();
			DefaultMutableTreeNode attributeNode = new DefaultMutableTreeNode(attribute);
			top.add(attributeNode);
			
			Iterator<CKPage> abilities = attribute.getPages();
			while(abilities.hasNext())
			{
				CKPage ability = abilities.next();
				DefaultMutableTreeNode abilityNode = new DefaultMutableTreeNode(ability);
				attributeNode.add(abilityNode);
				
				
			}
		}
		return top;
	
	
}

}

