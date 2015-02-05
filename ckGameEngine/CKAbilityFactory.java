package ckGameEngine;
import static ckCommonUtils.CKPropertyStrings.* ;

public class CKAbilityFactory
{

	private static CKBook characterAbilities = null;
	
	public static CKBook characterAbilityFactory()
	{
		if( characterAbilities == null)
		{
			characterAbilities = new CKBook();
		}
		CKBook A = new CKBook();
		return CKBook.addBooks(A,characterAbilities);	
	}
	
	private static CKBook teamAbilities = null;
	
	public static CKBook teamAbilityFactory()
	{
		if( teamAbilities == null)
		{
			teamAbilities = new CKBook();
			teamAbilities.addChapter(new CKChapter(SPEED,1));
			teamAbilities.addChapter(new CKChapter(CP_PER_ROUND,100));
			teamAbilities.addChapter(new  CKChapter(MAX_CP,100));
			teamAbilities.addChapter(new  CKChapter(RECHARGE_CP,1));
			teamAbilities.addChapter(new  CKChapter(CH_EQUIP_SLOTS,0));
			teamAbilities.addChapter(new CKChapter(CH_MOVE,1,P_FORWARD));
			
		}
		CKBook A = new CKBook();

		
	/*	System.out.println("team abilties"+teamAbilities.treeString());
		System.out.println("A"+A.treeString());
		*/
		return CKBook.addBooks(A,teamAbilities);
	}
	
}
