package ckSnapInterpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import ckCommonUtils.CKPropertyStrings;
import ckCommonUtils.GameCompletionListener;
import ckDatabase.CKCampaignNodeFactory;
import ckDatabase.CKConnection;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CampaignNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameApplication extends Application implements GameCompletionListener
{
	
	private CKBook traits;
	private List<CampaignNode> campaign;
	private Stage stage;


	public GameApplication()
	{
		//load traits and campaign data
		
		
		File f = new File(
				CKConnection.getCKSettingsDirectory(),
				"campaign.xml");
		
				try
				{
					traits = (CKBook) CKBook.readFromStream(new FileInputStream(f));
				} catch (FileNotFoundException e)
				{
					traits = new CKBook("Game Progress");
					saveTraits();
				}
		
		//Campaign Data
		campaign = CKCampaignNodeFactory.getInstance().getCampaign("Demo");
		
	}
	
	
	@Override
	public void start(Stage stage) throws Exception
	{
		
		CKGameObjectsFacade.setGameCompletionListener(this);
		this.stage = stage;
		System.out.println("Starting the stage");
		Pane chooseLevel = new LevelChooser(traits,campaign);
		
		Scene scene = new Scene(chooseLevel,760,430);
		scene.getStylesheets().add("ckSnapInterpreter/cyberknight_intro.css");
		stage.setScene(scene);
		stage.show();
		
		
		
	}
	
	public void saveTraits()
	{
		 File f = new File(CKConnection.getCKSettingsDirectory(),"campaign.xml");
		 try
		{
			traits.writeToStream(System.out);
			traits.writeToStream(new FileOutputStream(f));
		
		} catch (FileNotFoundException e)
		{
			
			e.printStackTrace();
		}
		 
		 
		
	}
	
	
	public static void main(String[] args)
	{
		//new GameApplication();
		launch(args);
		
	}


	@Override
	public void endGame(int state,String questID)
	{
		if(state>0)
		{
			campaign.forEach(cn->
			{
				if(cn.getQuestID().compareTo(questID)==0)
				{
					CKBook.addToBook(traits, cn.getPostAdds());
					traits.addChapter(new CKChapter(CKPropertyStrings.CH_LEVELS,
							0,cn.getWinName()));
				}
				saveTraits();
				
			});
		}
		//now restart the chooser
		Pane chooseLevel = new LevelChooser(traits,campaign);
		
		Scene scene = new Scene(chooseLevel,760,430);
		scene.getStylesheets().add("ckSnapInterpreter/cyberknight_intro.css");
		stage.setScene(scene);
		
		
	}

}
