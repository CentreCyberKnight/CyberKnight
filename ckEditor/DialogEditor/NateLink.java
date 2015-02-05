package ckEditor.DialogEditor;

import ckEditor.treegui.CKSingleParent;
import ckGameEngine.CKSpellCast;
import ckSatisfies.Satisfies;
import ckSatisfies.TrueSatisfies;

public class NateLink {


	private double weight; // should be private for good practice
	//private int id;
	private String DialogOption;
	private CKSingleParent singleParent;
	private NateNode source;
	private NateNode destination; 
	private int sourceId;
	private int destinationId;

	public NateLink(String DialogOption) {
		this.DialogOption=DialogOption;
		this.weight=100;
		//this.treeRoot = new CKGuiRoot();
		this.singleParent = new CKSingleParent();
		this.singleParent.add(new TrueSatisfies());
        //add(new CKTree2(treeRoot), BorderLayout.CENTER);
	}

	

	public int getSourceId() {
		return sourceId;
	}

	public int getDestinationId() {
		return destinationId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}
	public NateNode getSource() {
		return source;
	}

	public void setSource(NateNode source) {
		this.source = source;
		this.sourceId= source.getId();
	}

	public NateNode getDestination() {
		return destination;
	}

	public void setDestination(NateNode destination) {
		this.destination = destination;
		this.destinationId= destination.getId();
	}


	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public NateLink(double weight, String dialogOption) {
		this.setDialogOption(dialogOption);
		this.weight=weight;
		
		//this.treeRoot = new CKGuiRoot();
		
		this.singleParent.add(new TrueSatisfies());
        //add(new CKTree2(treeRoot), BorderLayout.CENTER);
	}
	
	


	public CKSingleParent getSingleParent() {
		return singleParent;
	}



	public void setSingleParent(CKSingleParent singleParent) {
		this.singleParent = singleParent;
	}



	public String toString() { // Always good for debugging
		return getDialogOption();
		//return "E"+id;
	}

	public String getDialogOption() {
		return DialogOption;
	}

	public void setDialogOption(String dialogOption) {
		this.DialogOption = dialogOption;
	}

	public boolean isSatisfied(CKSpellCast cast) {
		return ((Satisfies) singleParent.getChildAt(0)).isSatisfied(cast);
	}
}
