package ckSound;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import ckDatabase.CKXMLFactory;
import ckDatabase.XMLDirectories;

public class CKSoundFactory extends CKXMLFactory<CKSound>
{

	static List<Clip> clips= new ArrayList<Clip>();
	
	@Override
	public String getBaseDir()
	{
	
		return XMLDirectories.SOUND_DIR;
	}

	
	private static CKSoundFactory factory= null;

	public static CKSoundFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKSoundFactory();
		}
		return factory;
	}	
	
	//public void get

	

	
	private static void createTestDB()
	{

		
		CKSoundFactory factory = CKSoundFactory.getInstance();
	
		CKSound test1 = new CKSound("/rainforest_ambience-GlorySunz-1938133500.wav", "public domain", "Rainforest", "sound-bible.com");//use full url
		factory.writeAssetToXMLDirectory(test1);
		

		CKSound test2 =  new CKSound("/Buzz-SoundBible.com-1790490578.wav", "atribution 2.0", "Buzz", "sound-bible");	
		factory.writeAssetToXMLDirectory(test2);
		
		
	}
	
	
	
	
	
	public static void main(String [] args)
	{

	
		//EVERYTHING MUST BE IN THIS ORDER
		//creates frame
		JFrame frame = new JFrame();
		frame.setSize(480,400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		
		createTestDB();
		CKSoundFactory factory = CKSoundFactory.getInstance();
		CKSound cktest = factory.getAsset("Rainforest");//read from database
		
		
		CKSoundFactory factory2 = CKSoundFactory.getInstance();
		CKSound cktest2 = factory2.getAsset("Buzz");//read from database
		
		//play clip from database
		//cktest.play();
		//cktest.playAtPercent(0.10);

		
	}

	@Override
	public CKSound getAssetInstance()
	{
		return new CKSound();
	}
		
	
	
	
	

}