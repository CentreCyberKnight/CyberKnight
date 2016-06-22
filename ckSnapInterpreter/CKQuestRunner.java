package ckSnapInterpreter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class CKQuestRunner extends Application 
{
	public static String rectColor = "rgb(0,20,28)";
	public static Double rectOpacity = 0.2;
	 
	

	CKQuestSceneBuilder builder;
	

	
	@Override
	public void init() throws Exception
	{
	
		super.init();
		
		Parameters param = getParameters();
		for(String p:param.getRaw())
		{
			System.out.println("Param:"+p);
			
			
		}
		String asset = param.getRaw().get(0);
		builder = new CKQuestSceneBuilder(asset);
		
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Scene scene = builder.getAndStartScene();
		 primaryStage.setTitle("Test Drawer Tabs");
		    primaryStage.setScene(scene);
		    primaryStage.show();
	}
   
    public static void main(String[] args)
    {
    	
    	String input[] = {"asset7364953977011982560"};
    	
        launch(input);
    	//launch(args);
    }


}


