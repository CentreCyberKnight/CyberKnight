package ckPipeline;
public class Action {
	private String name,file;
	public Action(String namen,String fileNamen){
		//This class is the action and its associated file
		name=namen;
		file=fileNamen;
	}
	public void setFile(String namen){
		//This sets the file to the given name
		file=namen;
	}
	public String getFile(){
		//This returns the file associated with the action
		return file;
	}
	public String getName(){
		//This returns the name of the action
		return name;
	}
}
