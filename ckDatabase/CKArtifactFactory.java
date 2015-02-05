package ckDatabase;

import static ckCommonUtils.CKPropertyStrings.CH_AIM;
import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;
import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_EARTH;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
import static ckCommonUtils.CKPropertyStrings.P_BASH;
import static ckCommonUtils.CKPropertyStrings.P_FORWARD;
import static ckCommonUtils.CKPropertyStrings.P_ILLUMINATE;
import static ckCommonUtils.CKPropertyStrings.P_LEFT;
import static ckCommonUtils.CKPropertyStrings.P_MOUTH;
import static ckCommonUtils.CKPropertyStrings.P_OFFHAND_WEAPON;
import static ckCommonUtils.CKPropertyStrings.P_RIGHT;
import static ckCommonUtils.CKPropertyStrings.P_SHOES;
import static ckCommonUtils.CKPropertyStrings.P_SLASH;
import static ckCommonUtils.CKPropertyStrings.P_STAR;
import static ckCommonUtils.CKPropertyStrings.P_SWORD;
import static ckCommonUtils.CKPropertyStrings.P_TALK;
import static ckCommonUtils.CKPropertyStrings.P_TARGET;
import static ckCommonUtils.CKPropertyStrings.SPEED;
import ckEditor.treegui.BookList;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKSpell;

public class CKArtifactFactory extends CKXMLFactory<CKArtifact>
{

	@Override
	public String getBaseDir()
	{
	
		return XMLDirectories.ARTIFACTS_DIR;
	}

	
	private static CKArtifactFactory factory= null;
	 
	
	 

	public static CKArtifactFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKArtifactFactory();
		}
		return factory;
	}	
	
	
	
	private static void createTestDB()
	{
		
		CKArtifactFactory factory = CKArtifactFactory.getInstance();
		
		/*
		String functions =
				"def moveAndTurn(right):\n"+
				"	if(right):\n" +
				"		move('right', 1)\n" +
				"	else:\n" +
				"		move('left', 1)\n" +
				"	move('forward',1)\n\n"+
				"def talkTo():\n"+
				"      pc = aim('target',5)\n" +
				"      voice('talk',1,pc)\n\n" +
				"def lightUp():\n"+
				"      pc = aim('target',5)\n"+
				"      fire('"+P_ILLUMINATE+"',1,pc)\n\n"+
				"def lightAll():\n" +
				"      pc = aim('star',5)\n" +
				"      fire('"+P_ILLUMINATE+"',2,pc)\n\n";
		
		
		*/
		
		//boots
		CKBook limits = new CKBook();
		String[] pages = {P_FORWARD,P_LEFT,P_RIGHT};
		limits.addChapter(new CKChapter(CH_MOVE,2,pages ) );
		CKBook abilities=new CKBook("Abilties",CH_VOICE,1,P_TALK);
		CKBook []reqs = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact combatBoots = new CKArtifact("Combat Boots","Given to you by your grandmother",
				"boots", abilities,limits,new BookList(reqs),2);
		
		// need some spells
		CKSpell spell = new CKSpell("Go Forth", "moves forward by 2",
				"move('forward',2)", "upArrow");
		combatBoots.addSpell(spell);
		spell = new CKSpell("LeftTurn", "turns left and moves forward",
				"moveAndTurn(False)", "leftArrow");
		combatBoots.addSpell(spell);
		spell = new CKSpell("UTurn", "turns around", "move('left',2)", "uTurn");
		combatBoots.addSpell(spell);
		combatBoots.setAID("combatBoots");
		factory.writeAssetToXMLDirectory(combatBoots);
		
		
		
		// sword of awesome
		CKBook L2 = new CKBook("Limits", CH_EARTH, 5, P_SLASH);
		CKBook A2 = new CKBook("abilties", CH_EQUIP_SLOTS, 0, P_OFFHAND_WEAPON);
		CKBook[] R2 = {
				new CKBook("Requirements", CH_EQUIP_SLOTS, 0, P_OFFHAND_WEAPON),
				new CKBook("Requirements", CH_EQUIP_SLOTS, 0, P_SWORD) };
		CKArtifact coolSword = new CKArtifact("Dual Strike", "Adds a bonus",
				"axe", A2, L2, new BookList(R2), 1);
		// spells?
		CKSpell slash = new CKSpell("Slash", "cuts enemy by 2",
				"physical('slash',2)", "uTurn");
		coolSword.addSpell(slash);
		coolSword.setAID("coolSword");
		factory.writeAssetToXMLDirectory(coolSword);

		// spoon of awesome
		CKBook SL1 = new CKBook("Limits", CH_EARTH, 5, P_BASH);
		CKBook SA2 = new CKBook("abilties", CH_EQUIP_SLOTS, 0, P_OFFHAND_WEAPON);
		CKBook[] SR2 = { new CKBook("Requirements", CH_EQUIP_SLOTS, 0,
				P_OFFHAND_WEAPON) };
		CKArtifact coolSpoon = new CKArtifact("Mom's Spoon", "Home Loving",
				"woodenSpoon", SL1, SA2, new BookList(SR2), 1);
		// spells?
		CKSpell bash = new CKSpell("Bash", "hits enemy by 2",
				"physical('bash',2)", "sparkles");
		coolSpoon.setAID("coolSpoon");
		coolSpoon.addSpell(bash);
		factory.writeAssetToXMLDirectory(coolSpoon);

		// mouth of talking
		CKBook ML1 = new CKBook("mouth limits");
		ML1.addChapter(new CKChapter(CH_VOICE, 3, P_TALK));
		ML1.addChapter(new CKChapter(CH_FIRE, 3, P_ILLUMINATE));
		String[] aims = { P_TARGET, P_STAR };
		ML1.addChapter(new CKChapter(CH_AIM, 10, aims));
		System.out.println(ML1.treeString());
		// CKBook ML1 = new CKBook("Limits",CH_VOICE,5,P_TALK);
		CKBook MA2 = new CKBook("abilties", CH_VOICE, 1, P_TALK);
		CKBook[] MR2 = { new CKBook("Requirements", CH_EQUIP_SLOTS, 0, P_MOUTH) };
		CKArtifact mouth = new CKArtifact("Mouth", "talking aaway", "lips",
				MA2, ML1,new BookList( MR2), 1);
		// spells?
		CKSpell talk = new CKSpell("Talk", "Speaks to person in front",
				"talkTo()", "sparkles");
		mouth.addSpell(talk);
		CKSpell light = new CKSpell("Light", "illuminating", "lightUp()",
				"sparkles");
		mouth.addSpell(light);
		CKSpell light2 = new CKSpell("Light Everythin", "illuminating",
				"lightAll()", "uTurn");
		mouth.addSpell(light2);
		
		mouth.setAID("motorMouth");
		factory.writeAssetToXMLDirectory(mouth);
		
		
		
		//ballet shoes
		CKBook limitsballet = new CKBook();
		String[] pagesb = {P_FORWARD,P_LEFT,P_RIGHT};
		limitsballet.addChapter(new CKChapter(CH_MOVE,3,pagesb ) );
		
		
		//String[] pages = {P_FORWARD,P_LEFT,P_RIGHT};
		CKBook ABS = new CKBook("shoe abilities",SPEED ,2);
		limitsballet.addChapter(new CKChapter(CH_MOVE,3,pages ) );
		CKBook []reqsballer = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact balletShoes = new CKArtifact("Ballet Shoes","Worn by all the premire dancers!!",
				"balletShoes", ABS,limitsballet,new BookList(reqsballer),2);
		
		//need some spells
		spell = new CKSpell("Forward","moves forward", "move('forward',1)","upArrow");
		balletShoes.addSpell(spell);
		spell = new CKSpell("LeftTurn","turns left", "move('left',1)","leftArrow");
		balletShoes.addSpell(spell);
		spell = new CKSpell("Right","turns right", "move('left',3)","rightArrow");
		balletShoes.addSpell(spell);
		balletShoes.setAID("balletShoes");
		factory.writeAssetToXMLDirectory(balletShoes);
		
		
		
		CKBook limitsshoes = new CKBook();
		
		limitsshoes.addChapter(new CKChapter(CH_MOVE,1,pages ) );
		CKBook []reqsShoes = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact babyShoes = new CKArtifact("Bare Feet","When you can't afford shoes",
				"bareFeet", new CKBook(),limitsshoes,new BookList(reqsShoes),2);




		//need some spells
		spell = new CKSpell("Forward","moves forward", "move('forward',1)","upArrow");
		babyShoes.addSpell(spell);
		
		babyShoes.setAID("bareFeet");
		factory.writeAssetToXMLDirectory(babyShoes);
		
	}
	
	
	public static void main(String [] args)
	{
		createTestDB();
	}



	@Override
	public CKArtifact getAssetInstance()
	{
		return new CKArtifact();
	}
	
	
	

}
