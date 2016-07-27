package ckDatabase;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ckEditor.treegui.CKTreeGui;
import ckGameEngine.QuestData;

/**A CKSatisfiesFactory that uses a DB to solve the problems.
 * 
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKQuestFactory extends CKXMLFactory<QuestData>
{
	private static CKQuestFactory instance;
	private CKQuestFactory()
	{
		setShouldReload(true);
	}

	public static CKQuestFactory getInstance()
	{
		if(instance==null)
		{
			instance = new CKQuestFactory();
		}
		return instance;
	}
	
	
	
	public static void main(String[] args)
	{
		//createTestDB();
			
		
		JFrame frame = new JFrame();
		
		QuestData q = getInstance().getAsset("level1-db");
		CKTreeGui tree = new CKTreeGui(q);
		//CKTreeGui tree = new CKTreeGui(new CKBook());
		
		frame.add(tree);
		frame.pack();
		frame.setVisible(true);
		
		
		   frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	});
		
		
		
		System.out.println("YEA!");
	}

	@Override
	public String getBaseDir()
	{
		return XMLDirectories.QUEST_DIR;
	}

	@Override
	public QuestData getAssetInstance()
	{
		
		return new QuestData(5);
	}
	
}
