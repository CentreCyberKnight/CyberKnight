package ckPythonInterpreter;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeListener;

import ckEditor.treegui.BookList;
import ckGameEngine.ActorController;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKSpell;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;
import static ckCommonUtils.CKPropertyStrings.*;

public class CKTeamView extends JPanel
{
	//HashMap <String,CKCharacterEquippedView> indexMap;
	HashMap <String,CKCharacterView> indexMap;
	JTabbedPane tabs;
	JTabbedPane configTabs;
	
	final static String PLAY = "PLAY";
	final static String CONFIG = "CONFIG";
	
	
	public CKTeamView(List<CKGridActor> actors)
	{
		setLayout(new CardLayout());
		//setLayout(new BorderLayout());
		tabs = new JTabbedPane();
		indexMap = new HashMap<String,CKCharacterView>();
		for (CKGridActor ch: actors)
		{
			System.out.println("Character "+ch.getName());
			CKCharacterView cView = new CKCharacterView(ch);
			tabs.addTab(ch.getName(), null, cView, ch.getName());
			indexMap.put(ch.getName(),cView);
		}
		//add(tabs,BorderLayout.CENTER);		
		add(tabs,PLAY);
		/*
		
		///
		configTabs = new JTabbedPane();
		indexMap = new HashMap<String,CKCharacterView>();
		for (CKGridActor ch: actors)
		{
//			System.out.println("Character "+ch.getName());
			CKCharacterConfigView cView = new CKCharacterConfigView(ch);
			tabs.addTab(ch.getName(), null, cView, ch.getName());
//			indexMap.put(ch.getName(),cView);
		}
		//add(tabs,BorderLayout.CENTER);		
		add(tabs,CONFIG);
		CardLayout cl = (CardLayout)(getLayout());
		cl.show(this, CONFIG);	
		
		*/
		
		
	}
	
	public void gotoTab(String name)
	{
		System.out.println("switching over to "+name);
		tabs.setSelectedComponent( indexMap.get(name));
	}
	
	public CKGridActor getSelectedActor()
	{
		return ((CKCharacterView) tabs.getSelectedComponent()).getActor();
		
	}
	
	public CKCharacterView getSelectedView()
	{
		return (CKCharacterView) tabs.getSelectedComponent();
	}
	
	public void addChangeListener(ChangeListener l)
	{
		tabs.addChangeListener(l);
	}
	
	public void removeChangeListener(ChangeListener l)
	{
		tabs.removeChangeListener(l);
	}
	
	
	public void enableCharacters(boolean enabled)
	{
		//enable here
		System.err.println("enableCharacter not implmented\n");

	}
	public void enableCharacter(String name,boolean enabled)
	{
		CKCharacterView cView= indexMap.get(name);
		//enable here
//		System.err.println("enableCharacter not implmented");
		cView.enableCharacter(enabled);

	}

	
	
	public static void main(String [] args)
	{
		//make a team:)
		CKBook teamplay = new CKBook();
		CKChapter chap = new CKChapter(CH_MOVE, 1);
		chap.addPage(new CKPage(P_FORWARD));
		chap.addPage(new CKPage(P_LEFT));
		chap.addPage(new CKPage(P_RIGHT));
		teamplay.addChapter(chap);
		
		CKTeam team = new CKTeam("heroes");
		team.addToAbilties(teamplay);
		
		
		//add some characters!
		CKGridActor dad = new CKGridActor("heroSprite",Direction.NORTHEAST);
		dad.setName("Dad");
		
		CKChapter dChap = new CKChapter(CH_MOVE,2);
		CKChapter dChap2 = new CKChapter(CH_FIRE,2,P_IGNITE);
		CKChapter dChap3 = new CKChapter(CH_EQUIP_SLOTS,0);
		dChap3.addPage(new CKPage(P_SHOES));
		dChap3.addPage(new CKPage(P_SWORD));
		dChap3.addPage(new CKPage(P_ARMOR));
		CKBook dBook = new CKBook();
		dBook.addChapter(dChap);
		dBook.addChapter(dChap2);
		dBook.addChapter(dChap3);
		dad.addAbilities(dBook);
		
		team.addCharacter(dad);
		dad.setTeam(team);
		
		//combat boots
		CKBook limits = new CKBook();
		String[] pages = {P_FORWARD,P_LEFT,P_RIGHT};
		limits.addChapter(new CKChapter(CH_MOVE,2,pages ) );
		CKBook abilities=new CKBook("Abilties",CH_VOICE,1,P_TALK);
		CKBook []reqs = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact combatBoots = new CKArtifact("Combat Boots","Given to you by your grandmother",
				"boots", abilities,limits,new BookList(reqs),2);
		
		//need some spells
		CKSpell spell = new CKSpell("Go Forth","moves forward by 2", "move('forward',2)","upArrow");
		combatBoots.addSpell(spell);
		spell = new CKSpell("LeftTurn","turns left", "move('left',1)","left" +"Arrow");
		combatBoots.addSpell(spell);
		spell = new CKSpell("UTurn","turns around", "move('left',2)","uTurn");
		combatBoots.addSpell(spell);
		
		dad.equipArtifact(P_SHOES,combatBoots);
		
		//sword of awesome
		CKBook L2 = new CKBook("Limits",CH_EARTH,5,P_SLASH);
		CKBook A2 = new CKBook("abilties",CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON);
		CKBook [] R2 = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON),
									 new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SWORD) };		
		CKArtifact coolSword = new CKArtifact("Dual Strike","Adds a bonus","axe",A2,L2,new BookList(R2),1);
		//spells?
		CKSpell slash= new CKSpell("Slash","cuts enemy by 2","physical('slash',2)","uTurn");
		coolSword.addSpell(slash);
		
		
		
		
		CKGridActor mom = new CKGridActor("momSprite",Direction.NORTHEAST);
		mom.setName("Mom");
		
		dChap = new CKChapter(CH_MOVE,1);
		 dChap2 = new CKChapter(CH_VOICE,3,P_SING);
		String [] pos = { P_SHOES,P_OFFHAND_WEAPON} ;
		dChap3 = new CKChapter(CH_EQUIP_SLOTS,0,pos);
		dBook = new CKBook();
		dBook.addChapter(dChap);
		dBook.addChapter(dChap2);
		dBook.addChapter(dChap3);
		mom.addAbilities(dBook);
		
		CKGridActor baby = new CKGridActor("babySprite",Direction.NORTHEAST);
		baby.setName("Baby");


		baby.addAbilities(new CKBook("abil",CH_EQUIP_SLOTS,0,P_SHOES));
		
		//add them
		
		team.addCharacter(mom);
		team.addCharacter(baby);
		//already did team.addCharacter(dad);
		
		//add artifacts
		team.addArtifact(combatBoots);
		team.addArtifact(coolSword);
		
	    JFrame f = new JFrame("TeamViewTest");
	    final Container c = f.getContentPane();
	    c.setLayout(new BorderLayout());
	    Vector<CKGridActor> actors = new Vector<>();
	    actors.add(dad);
	    //dad.setTeam(team);
	    actors.add(mom);
	    mom.setTeam(team);
	    actors.add(baby);
	    baby.setTeam(team);
	    
	    CKTeamView view = new CKTeamView(actors);
	    view.enableCharacter("Dad",true);
	    c.add(view);
	    f.setSize(400, 600);
	    f.setVisible(true);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
}
