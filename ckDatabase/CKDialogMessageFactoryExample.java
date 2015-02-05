package ckDatabase;

import java.util.HashMap;

import ckGraphicsEngine.CKDialogChoice;
import ckGraphicsEngine.CKDialogChoiceMessage;
import ckGraphicsEngine.CKDialogMessage;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKDialogMessageFactoryExample extends CKDialogMessageFactory
{
	private static HashMap<Integer,CKDialogMessage> messageMap;
	
	public CKDialogMessageFactoryExample()
	{
	
		messageMap = new HashMap<Integer,CKDialogMessage>();
		
		CKDialogChoiceMessage m1;
		CKDialogChoiceMessage m2;
		CKDialogChoiceMessage m3;
		CKDialogChoiceMessage m4;
		
		m1 = new CKDialogChoiceMessage("Please make a selection squirrl?");
		m2 = new CKDialogChoiceMessage("You have chosen choice 1");
		m3 = new CKDialogChoiceMessage("You have chosen choice 2");
		m4 = new CKDialogChoiceMessage("You have chosen choice 3");
		CKDialogChoiceMessage[] M = {m1,m2,m3,m4};
		for (CKDialogChoiceMessage m:M)
		{	
			m.addChoice(new CKDialogChoice(1,"Would you like choice 1?"));
			m.addChoice(new CKDialogChoice(2,"Would you like choice 2?"));
			m.addChoice(new CKDialogChoice(3,"Would you like choice 3?"));
		}
		m4.addChoice(new CKDialogChoice(0,"Let's Leave this town!"));
		messageMap.put(5,m1);	
		messageMap.put(1,m2);	
		messageMap.put(2,m3);	
		messageMap.put(3,m4);
		messageMap.put(10, new CKDialogMessage("I'm going to get you!"));
		messageMap.put(0, null);
		storeLevelOne();
	}
	
	
	public void storeLevelOne()
	{
		
		String momAssetID = "momSprite";
		String babyAssetId = "babySprite";
		
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		CKGraphicsAsset momAsset = factory.getPortrait(momAssetID);//factory.getGraphicsAsset(momAssetID);
		CKGraphicsAsset babyAsset = factory.getPortrait(babyAssetId);//factory.getGraphicsAsset(babyAssetId);
		
		//comfort - 1000
		messageMap.put(1000, new CKDialogMessage("That's OK Tiger, It takes time to get this.",momAsset,0));
		
		
		//ready - 1100
		CKDialogChoiceMessage ready = new CKDialogChoiceMessage("Are you ready to try it again?",momAsset); 
		ready.addChoice(new CKDialogChoice(0,"Let's do it!"));
		ready.addChoice(new CKDialogChoice(1110,"Can you go over it again?"));
		messageMap.put(1100,ready);
		
		//Explanation
		messageMap.put(1110, new CKDialogMessage("In order to do anything, you must use the correct functions.",
				momAsset,1111));
		messageMap.put(1111, new CKDialogMessage("For instance if you want to move, you need to call the 'move()' function.",
				momAsset,1112));
		
		messageMap.put(1112, new CKDialogMessage("The move funciton requires two parameters, how to move and how far.",
				momAsset,1113));
		messageMap.put(1113, new CKDialogMessage("Like This: \n\n move('forward',1)",
				momAsset,1114));
		messageMap.put(1114, new CKDialogMessage("It seems complicated, but before too long you'll be controlling the cyber like it was a second language!",
				momAsset,1100));
		
		//Do
		messageMap.put(1200, new CKDialogMessage("Watch Me",momAsset,0));
		messageMap.put(1250, new CKDialogMessage("move('forward',1)",momAsset,1251));
		messageMap.put(1251, new CKDialogMessage("Now you try it, only use baby for your variable name",momAsset,0));
		
		//Win Message
		messageMap.put(1300, new CKDialogMessage("There is no one as good at this as you",momAsset,0));
		
		//Lose Message
		messageMap.put(1350, new CKDialogMessage("LoSe", babyAsset, 0));
		
		
		//intro text
		CKDialogChoiceMessage intro = new CKDialogChoiceMessage("My, you are getting to be a big baby." ,momAsset); 
		intro.addChoice(new CKDialogChoice(0,"skip intro?"));
		intro.addChoice(new CKDialogChoice(1401,"Continue"));
		messageMap.put(1400,intro);
		

		messageMap.put(1401, new CKDialogMessage("gaga"
				,babyAsset,1402));
		messageMap.put(1402, new CKDialogMessage("I guess it is about time for you to learn how to walk."
				,momAsset,1403));
		messageMap.put(1403, new CKDialogMessage("It may not sound like much, but it will be the first time you access the Cyber and control the world around you."
				,momAsset,1404));
		messageMap.put(1404, new CKDialogMessage("gaga?"
				,babyAsset,1405));
		messageMap.put(1405, new CKDialogMessage("Cyber is the force that permeates the world of Aedifex.  In the ancient language it means 'to control'."
				,momAsset,1406));
		messageMap.put(1406, new CKDialogMessage("gaga?"
				,babyAsset,1407));
		messageMap.put(1407, new CKDialogMessage("Yes, I know that I’m talking to a baby, but maybe some of this will sink in."
				,momAsset,1408));
		messageMap.put(1408, new CKDialogMessage("All creatures on Aedifex, from the smallest mouse to the mightiest tiger, from the simplest bit farmer to the mightiest of the CyberKnights use the Cyber to control all that they do."
				,momAsset,1409));
		messageMap.put(1409, new CKDialogMessage("gaga!"
				,babyAsset,1410));
		messageMap.put(1410, new CKDialogMessage("What was that?\nYou want to be a tiger!\nWhy not a CyberKnight like Mommy?"
				,momAsset,1411));
		messageMap.put(1411, new CKDialogMessage("GAGA!!"
				,babyAsset,1412));
		messageMap.put(1412, new CKDialogMessage("Oh well, Tiger it is.  The first lesson for my little tiger is to walk."
				,momAsset,1110));
		
	}
	
	
	@Override
	public CKDialogMessage getDialogMessage(int aid)
	{
		return messageMap.get(aid);
	}



}
