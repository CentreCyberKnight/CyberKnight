package ckSnapInterpreter;

import java.util.ArrayList;
import ckDatabase.CKArtifactFactory;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpell;
import ckGameEngine.CKTeam;

public class CKDataModel {

	CKArtifactFactory aFactory;	
	CKBook teamplay;
	CKChapter chap;
	CKTeam team;
	
	 //player chosen to view
	CKGridActor player;
	
	//player with permission to take a turn.
	private Observable<CKGridActor> activePlayer = new Observable<CKGridActor>();
	
	//artifact
	CKArtifact artifact;
	//control move
	CKSpell spell; 
	
	ArrayList<Observer<CKGridActor>> playerObservers;

	ArrayList<Observer<CKArtifact>> artifactObservers;
	ArrayList<Observer<CKSpell>> spellObservers;
	
	
	public CKDataModel(CKGridActor player, CKArtifact artifact, CKSpell spell){
		this.player = player;
		this.artifact = artifact;
		this.spell = spell;
		
		playerObservers = new ArrayList<Observer<CKGridActor>>();
		
		artifactObservers = new ArrayList<Observer<CKArtifact>>();
		spellObservers = new ArrayList<Observer<CKSpell>>();
	}
	
	public Observable<CKGridActor> getActivePlayerProperty()
	{
		return activePlayer;
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
	
	
}