package ckGameEngine;
import static ckCommonUtils.CKPropertyStrings.CH_ACCURACY;
import static ckCommonUtils.CKPropertyStrings.CH_ATTACK_BONUS;
import static ckCommonUtils.CKPropertyStrings.CH_DEFENSE;
import static ckCommonUtils.CKPropertyStrings.CH_DEFENSE_EFFECTIVENESS;
import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;
import static ckCommonUtils.CKPropertyStrings.CH_EVADE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CP_PER_ROUND;
import static ckCommonUtils.CKPropertyStrings.MAX_CP;
import static ckCommonUtils.CKPropertyStrings.MAX_DAMAGE;
import static ckCommonUtils.CKPropertyStrings.P_FORWARD;
import static ckCommonUtils.CKPropertyStrings.RECHARGE_CP;
import static ckCommonUtils.CKPropertyStrings.SPEED;

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
			teamAbilities.addChapter(new CKChapter(CH_DEFENSE,0));
			teamAbilities.addChapter(new CKChapter(CH_EVADE,0));
			teamAbilities.addChapter(new CKChapter(CH_ATTACK_BONUS,0));
			teamAbilities.addChapter(new CKChapter(MAX_DAMAGE,40));
			teamAbilities.addChapter(new CKChapter(CH_DEFENSE_EFFECTIVENESS,70));
			teamAbilities.addChapter(new CKChapter(CH_ACCURACY,0));
			
			
		}
		CKBook A = new CKBook();

		
	/*	System.out.println("team abilties"+teamAbilities.treeString());
		System.out.println("A"+A.treeString());
		*/
		return CKBook.addBooks(A,teamAbilities);
	}
	
}
