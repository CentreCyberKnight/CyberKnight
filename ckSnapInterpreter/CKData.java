package ckSnapInterpreter;

import static ckCommonUtils.CKPropertyStrings.CH_EARTH;
import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;
import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
import static ckCommonUtils.CKPropertyStrings.MAX_CP;
import static ckCommonUtils.CKPropertyStrings.P_ARMOR;
import static ckCommonUtils.CKPropertyStrings.P_FORWARD;
import static ckCommonUtils.CKPropertyStrings.P_IGNITE;
import static ckCommonUtils.CKPropertyStrings.P_LEFT;
import static ckCommonUtils.CKPropertyStrings.P_OFFHAND_WEAPON;
import static ckCommonUtils.CKPropertyStrings.P_RIGHT;
import static ckCommonUtils.CKPropertyStrings.P_SHOES;
import static ckCommonUtils.CKPropertyStrings.P_SING;
import static ckCommonUtils.CKPropertyStrings.P_SLASH;
import static ckCommonUtils.CKPropertyStrings.P_SWORD;
import static ckCommonUtils.CKPropertyStrings.P_TALK;
import static ckCommonUtils.CKPropertyStrings.SPEED;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ckCommonUtils.CKPosition;
import ckDatabase.CKArtifactFactory;
import ckDatabase.CKGridActorFactory;
import ckDatabase.CKTeamFactory;
import ckEditor.treegui.ActorNode;
import ckEditor.treegui.BookList;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKSpell;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckPythonInterpreter.CKTeamView;
import ckPythonInterpreterTest.CKArtifactQuestRunner;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.TrueSatisfies;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.TriggerResult;

public class CKData {

	CKArtifactFactory aFactory;	
	CKBook teamplay;
	CKChapter chap;
	CKTeam team;
	
	 //player chosen
	CKGridActor player;
	//artifact
	CKArtifact artifact;
	//control move
	CKSpell spell; 
	
	ArrayList<Observer<CKGridActor>> playerObservers;
	ArrayList<Observer<CKArtifact>> artifactObservers;
	ArrayList<Observer<CKSpell>> spellObservers;
	
	
	public CKData(CKGridActor player, CKArtifact artifact, CKSpell spell){
		this.player = player;
		this.artifact = artifact;
		this.spell = spell;
		
		playerObservers = new ArrayList<Observer<CKGridActor>>();
		artifactObservers = new ArrayList<Observer<CKArtifact>>();
		spellObservers = new ArrayList<Observer<CKSpell>>();
	}
	
	
//	public CKData(CKGridActor player, CKArtifact artifact, CKSpell spell){
//		this.player = player;
//		this.artifact = artifact;     
//		this.spell = spell;
//		
//		playerObservers = new ArrayList<Observer<CKGridActor>>();
//		artifactObservers = new ArrayList<Observer<CKArtifact>>();
//		spellObservers = new ArrayList<Observer<CKSpell>>();
//	}
	
	//make observables for this
	public  CKTeam getTeam() {
		if(team == null)
			System.out.println("no team found");
		return team;
	}
	
	public void setTeam(CKTeam c) {
		team = c;
	}
	
	
	//Data for the current Player
	public CKGridActor getPlayer() {
		return player;
	}
	
	public void setPlayer(CKGridActor newPlayer) {
		player = newPlayer;
		notifyPlayerObserver();
	}
	
	public ArrayList<Observer<CKGridActor>> getPlayerObservers() {
		return playerObservers;
	}
	
	public void setPlayerObservers(ArrayList<Observer<CKGridActor>> playerObserverList) {
		playerObservers = playerObserverList;
	}
	
	public void registerPlayerObserver(Observer<CKGridActor> o) {
		// TODO Auto-generated method stub
		playerObservers.add(o);
	}

	public void removePlayerObserver(Observer<CKGridActor> o) {
		// TODO Auto-generated method stub
		playerObservers.remove(o);
	}

	public void notifyPlayerObserver() {
		// TODO Auto-generated method stub
		for(Observer<CKGridActor> observer : playerObservers) {
			observer.update(player);
		}
		System.out.println("Notifying all registered Player observers");
	}
	
	
	//Data for the current Artifact
	public CKArtifact getArtifact() {
		return artifact;
	}
	
	public void setArtifact(CKArtifact newArtifact) {
		artifact = newArtifact;
		notifyArtifactObserver();
	}
	
	public ArrayList<Observer<CKArtifact>> getArtifactObservers() {
		return artifactObservers;
	}
	
	public void setObservers(ArrayList<Observer<CKArtifact>> artifactObserverList) {
		artifactObservers = artifactObserverList;
	}
	
	public void registerArtifactObserver(Observer<CKArtifact> o) {
		// TODO Auto-generated method stub
		artifactObservers.add(o);
	}

	public void removeArtifactObserver(Observer<CKArtifact> o) {
		// TODO Auto-generated method stub
		artifactObservers.remove(o);
	}

	public void notifyArtifactObserver() {
		// TODO Auto-generated method stub
		for(Observer<CKArtifact> observer : artifactObservers) {
			observer.update(artifact);
		}
		System.out.println("Notified all registers observers");
		
		
	}
	
	
	//Data for the current spell
	public CKSpell getSpell() {
		return spell;
	}
	
	public void setSpell(CKSpell newSpell) {
		spell = newSpell;
		notifySpellObserver();
	}

	public ArrayList<Observer<CKSpell>> getSpellObservers() {
		return spellObservers;
	}
	
	public void setSpellObservers(ArrayList<Observer<CKSpell>> spellObserverList) {
		spellObservers = spellObserverList;
	}
	
	public void registerSpellObserver(Observer<CKSpell> o) {
		// TODO Auto-generated method stub
		spellObservers.add(o);
	}

	public void removeSpellObserver(Observer<CKSpell> o) {
		// TODO Auto-generated method stub
		spellObservers.remove(o);
	}

	public void notifySpellObserver() {
		// TODO Auto-generated method stub
		for(Observer<CKSpell> observer : spellObservers) {
			observer.update(spell);
		}
		System.out.println("Notifying all registered Spell observers");
	}
	
	
	
	
	public void start(Stage primaryStage) {
	{
		
		aFactory = CKArtifactFactory.getInstance();
		

		//make a team:)
		teamplay = new CKBook();
		chap = new CKChapter(CH_MOVE, 1);
		chap.addPage(new CKPage(P_FORWARD));
		chap.addPage(new CKPage(P_LEFT));
		chap.addPage(new CKPage(P_RIGHT));
		teamplay.addChapter(chap);
		
		team = new CKTeam("heroes");
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
		
//		Pane pane = new Pane();
//		Scene scene = new Scene(pane,1500,820);
//	    primaryStage.setTitle("Test Drawer Tabs");
//	    primaryStage.setScene(scene);
//	    primaryStage.show();
//	    JFrame f = new JFrame("TeamViewTest");
//	    final Container c = f.getContentPane();
//	    c.setLayout(new BorderLayout());
	    Vector<CKGridActor> actors = new Vector<>();
	    actors.add(dad);
	    //dad.setTeam(team);
	    actors.add(mom);
	    mom.setTeam(team);
	    actors.add(baby);
	    baby.setTeam(team);
	    
	    CKTeamView view = new CKTeamView(actors);
//	    view.enableCharacter("Dad",true);
//	    c.add(view);
//	    f.setSize(400, 600);
//	    f.setVisible(true);
//		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
//	public static void main(String[] args) {
//		launch(args);
//	}
}
}