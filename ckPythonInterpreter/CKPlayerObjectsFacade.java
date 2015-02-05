package ckPythonInterpreter;


import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGameObjectsFacade;
import static ckCommonUtils.CKPropertyStrings.*;

public class CKPlayerObjectsFacade
{
	
		// Note that the constructor is private
		private CKPlayerObjectsFacade() { }
		
		private static CKArtifact artifact = null;
		private static int CPTurnMax =0;
		private static int CPTurnRemaining =0;
		
		
		public static void setArtifact(CKArtifact art)
		{
			artifact = art;
		}
				
		public static CKArtifact getArtifact()
		{
			return artifact; 
		}
		
 		
		public static void calcCPTurnLimit()
		{
			CKBook limits = CKEditorPCController.getAbilties();
			if(limits.hasChapter(CP_PER_ROUND) )
			{
				CPTurnMax = limits.getChapter(CP_PER_ROUND).getValue();
			}
			else
			{
				CPTurnMax=0;
				
			}

			//don't let them go over
			CPTurnMax = Math.min(CPTurnMax,
						CKGameObjectsFacade.getCurrentPlayer().getCyberPoints());
			CPTurnRemaining=CPTurnMax;

			System.out.println("max CP is "+CPTurnMax);
			
		}
		
		public static int getCPTurnRemaining()
		{
			return CPTurnRemaining;
		}
		
		public static void alterCPTurnRemaining(int val)
		{
			CPTurnRemaining += val;
		}

		public static int getCPTurnMax()
		{
			return CPTurnMax;
		}
		
		
		/*
		 * don't need this?
		public static CKEditorPCController getCharacter()
		{
			return new CKEditorPCController(CKGameObjectsFacade.getCurrentPlayer());
		}
			*/	
}
