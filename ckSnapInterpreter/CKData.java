package ckSnapInterpreter;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpell;

public class CKData {

	
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
		
		observers = new ArrayList<Observer>();
	}
	
	public CKGridActor getPlayer() {
		return player;
	}
	
	public void setPlayer(CKGridActor newPlayer) {
		player = newPlayer;
		notifyObserver();
	}
	
	public CKArtifact getArtifact() {
		return artifact;
	}
	
	public void setArtifact(CKArtifact newArtifact) {
		artifact = newArtifact;
		notifyObserver();
	}
	
	public CKSpell getSpell() {
		return spell;
	}
	
	public void setAbility(CKSpell newSpell) {
		spell = newSpell;
		notifyObserver();
	}

	public ArrayList<Observer> getObservers() {
		return observers;
	}
	
	public void setObservers(ArrayList<Observer> observerList) {
		observers = observerList;
	}
	
	@Override
	public void registerObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}


	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}


	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub
		for(Observer observer : observers) {
			observer.update(artifact, player, spell);
		}
		System.out.println("Notified all registers observers");
	}
	
	//change listeners, object based properties
	//observable
	//observer pattern...call interface for each 
	//

	

}