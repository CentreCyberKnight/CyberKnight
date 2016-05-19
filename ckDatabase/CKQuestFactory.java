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
	/*

	public static String generateUniqueQuestName()
	{
		try
		{
			String path = new CKURL(XMLDirectories.QUEST_DIR).getURL().getFile();
			File uniqueFile = File.createTempFile("asset", ".xml", new File(path));
			String name =uniqueFile.getName(); 
			return name.substring(0, name.lastIndexOf(".xml"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return "OOPS";

	}
	

	public static void writeQuestToXMLDirectory(QuestData q)
	{
		try
		{
			if(q.getQid().length()==0)
			{
				q.setQid(generateUniqueQuestName());
			}
			CKURL u = new CKURL(XMLDirectories.QUEST_DIR+q.getQid()+".xml");
			q.writeToStream(u.getOutputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public static QuestData readQuestFromXMLDirectory(String questID)
	{
		try
		{
			CKURL u = new CKURL(XMLDirectories.QUEST_DIR+questID+".xml");
			return QuestData.readFromStream(u.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	/**
	 * Returns the layer stored in the database with id lid.
	 * Layers are not unique, so there is no need to keep them unique.
	 * @param aid
	 * @return
	 */
	/*public QuestData getQuest(String qid)
	{
		return readQuestFromXMLDirectory(qid);
	}

	
	public Iterator<QuestData> getAllQuests()
	{
		File folder;
		Vector<QuestData> vec=new Vector<QuestData>();
		
		try
		{
			folder = new File (new CKURL(XMLDirectories.QUEST_DIR).getURL().getFile());
		
			for (File f : folder.listFiles())
			{
				vec.add(getQuest(f.getName().replaceFirst("[.][^.]+$","") )  );
			}
		}	catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	
		return vec.iterator();
		
	
	}

	public static void createTestDB()
	{
	
			//QuestData q = Quest.creation(3);
			//writeQuestToXMLDirectory(q);
	}

	
	*/
	
	
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
