/*Author: Chadwick Carter
 *Holds information about a future spritesheet and
 *performs actions with this information to create CKGraphicsAssets
 *Has functions to:
 * 1. Generate a spritesheet
 *		generateSpritesheet()
 * 2. Move the spritesheet into CK_DATA
 *		moveSpritesheet()
 * 3. Create a CKSpriteAsset with the spritesheet and information about actions
 *		createAsset()
 *All 3 of these are done with the function pipeline().
 * Will take information from text file from DAZ portion 
 */
package ckGraphicsEngine.assets;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.XMLDirectories;
//import javax.swing.JFrame;


public class BlenderSpritesheetAsset {
	public static final String IMG_EXTENSION=".png";
	//Change PATH_TO_CK_DATA depending on the computer 
	//Make sure to go into both CK_DATA folders
	public static final String PATH_TO_CK_DATA = "C:/Users/Test/git/CK_DATA/CK_DATA/";
	ArrayList<CKSpritesheetActionNode> actionList;
	String characterName; //Name of character
	String sheetFileName; //Name of spritesheet image
	int totalFrames; //Total number of frames in spritesheet
	int imgWidth; //Pixel width of one image
	int imgHeight; //Pixel height of one image
	int frameRate; //Frames/second of action/spritesheet
	String basePath; //Path to Output folder
	int numColumns; //Calculated in constructor, number of columns in spritesheet
	int numRows; //Calculated in constructor, number of rows in spritesheet
	
	//Constructor parses a text file from DAZ script/java
	//Need full path to text file
	public BlenderSpritesheetAsset(String txtFileName) throws FileNotFoundException{ 
		FileReader fin = new FileReader(txtFileName);
		Scanner scan = new Scanner(fin);
		String spritesheetInfo = scan.nextLine();
		scan.close();
		String[] spritesheetData = spritesheetInfo.split(";");
		this.characterName = spritesheetData[0];
		this.basePath = spritesheetData[1];
		this.totalFrames = Integer.parseInt(spritesheetData[2]);
		calculateTiles();
		this.imgWidth = Integer.parseInt(spritesheetData[3]);
		this.imgHeight = Integer.parseInt(spritesheetData[4]);
		this.frameRate = Integer.parseInt(spritesheetData[5]);
		this.sheetFileName = spritesheetData[0]+IMG_EXTENSION;
	}
	//Previous constructor used in testing
	public BlenderSpritesheetAsset(String charName,String filename,int numFrames, int width,int height,int framerate){ 
		this.actionList = new ArrayList<CKSpritesheetActionNode>();
		this.characterName = charName;	
		this.sheetFileName = filename;
		this.totalFrames = numFrames;
		this.imgWidth = width;
		this.imgHeight = height;
		this.frameRate = framerate;
		this.basePath = "";
		calculateTiles();
		this.sheetFileName = charName+IMG_EXTENSION;
	}
	//Previous constructor used in testing
	public BlenderSpritesheetAsset(String charName,String filename,int numFrames,int frames_Row,int num_Rows, int width,int height,int framerate){ 
		this.actionList = new ArrayList<CKSpritesheetActionNode>();
		this.characterName = charName;	
		this.sheetFileName = filename;
		this.totalFrames = numFrames;
		this.imgWidth = width;
		this.imgHeight = height;
		this.frameRate = framerate;
		this.numRows = num_Rows;
		this.numColumns = frames_Row;
		this.basePath = "";
		calculateTiles();
		this.sheetFileName = charName+IMG_EXTENSION;
	}
	
	//Getters and setters block
	public ArrayList<CKSpritesheetActionNode> getActionList() {
		return actionList;
	}

	public void setActionList(ArrayList<CKSpritesheetActionNode> actionList) {
		this.actionList = actionList;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public static String getImgExtension() {
		return IMG_EXTENSION;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getSheetFileName() {
		return sheetFileName;
	}

	public void setSheetFileName(String fileName) {
		this.sheetFileName = fileName;
	}

	public int getTotalFrames() {
		return totalFrames;
	}

	public void setTotalFrames(int totalFrames) {
		this.totalFrames = totalFrames;
	}
	
	public int getNumColumns() {
		return numColumns;
	}

	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}
	//Creates a new CKSpritesheetActionNode from parameters and adds it to actionList
	//Parameters: name of action, number of frames in action
	public void addAction(String name,int frames){
		/* name = name of action
		 * frames = number of frames in the action
		 */
		CKSpritesheetActionNode newNode = new CKSpritesheetActionNode(name,frames);
		actionList.add(newNode); 
	}
	/*Calculates ideal tile size considering number of frames
	 * And sets framesEachRow and numRows to reflect this
	 * Close to square, fulfills minimum number of frames
	 */
	public void calculateTiles(){
		double base = Math.sqrt((double)totalFrames);
		if ((base - Math.floor(base))<0.5){
			this.numColumns = (int)Math.ceil(base);
			this.numRows = (int)Math.floor(base);
		}
		else{
			this.numColumns = (int)Math.ceil(base+1);
			this.numRows = (int)Math.floor(base);
			}
	}
	//Uses class information and ImageMagick montage command to combine a series of images into a spritesheet
	public void generateSpritesheet(){
		File dir = new File(this.basePath+"/"+this.characterName);
		String[] imgFileList = dir.list(new FilenameFilter(){
			public boolean accept(File directory,String fileName){
				return fileName.endsWith(IMG_EXTENSION);}});
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add("blender");
		
		System.out.println("imgFileList.length: " + imgFileList.length);
		
		for (int i=1;i<imgFileList.length+1;i++){
			commandList.add(imgFileList[i-1]);
		}
		if (commandList.contains(this.sheetFileName)){
			int index = commandList.indexOf(this.sheetFileName);
			commandList.remove(index);
		}
		commandList.add("-tile");
		commandList.add(this.numColumns+"x"+this.numRows);
		commandList.add("-geometry");
		commandList.add(this.imgWidth+"x"+this.imgHeight+"+0+0");
		commandList.add("-background");
		commandList.add("none");
		commandList.add(this.characterName+IMG_EXTENSION);
		
		for (int i = 0; i < commandList.size(); i ++)
		{
			System.out.println(commandList.get(i));
		}
		
		try{
			ProcessBuilder pb = new ProcessBuilder(commandList);
			pb.directory(new File(this.basePath+"/"+this.characterName));
			Process p = pb.start();
			p.waitFor();
			}
			catch(IOException e1){System.out.println(e1.getMessage());} 
			catch (InterruptedException e) {e.printStackTrace();}
	}
	
	//Moves the spritesheet to the ASSET_IMAGES directory in CK_DATA
	public void moveSpritesheet(){
		String curString = this.basePath+"/"+this.characterName+"/"+this.sheetFileName;
		Path currentPath = FileSystems.getDefault().getPath(curString);
		//System.out.println(Files.exists(currentPath));
		String tarString = PATH_TO_CK_DATA+XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+this.sheetFileName;
		Path targetPath = FileSystems.getDefault().getPath(tarString);
		//System.out.println(Files.exists(targetPath));
		try {
			Files.copy(currentPath,targetPath,StandardCopyOption.REPLACE_EXISTING);} 
		catch (IOException e) {e.printStackTrace();}
	}
	//Executes the decorator pattern ImageAsset --> RegulatedAsset --> SelectAsset --> SpriteAsset
	//to make a spritesheet's actions usable by CyberKnight
	public CKGraphicsAsset createAsset(){
		//System.out.println(this.basePath+"/"+this.sheetFileName);
		//Currently must upload images to CK_DATA before using with wrapper
		CKImageAsset imgAst = new CKImageAsset(this.characterName+"_Image",this.characterName+"_spritesheet_"+this.totalFrames,this.imgWidth,this.imgHeight,this.numColumns,this.numRows,TileType.SPRITE,XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+this.sheetFileName);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(imgAst);
		
		String regAssetID = this.characterName+"_Regulated";
		CKRegulatedAsset regAst = new CKRegulatedAsset(regAssetID,this.characterName+"_Regulated_"+this.frameRate+"fps",imgAst,this.frameRate);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(regAst);
		
		int startFrame = 0;
		String selectAssetID = this.characterName+"_Select";
		int num_Frames = this.getTotalFrames();
		CKSelectAsset selectAst = new CKSelectAsset(selectAssetID,this.characterName,regAssetID,num_Frames*3,startFrame*3);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(selectAst);
		
		return selectAst;
	}
	//Runs all 3 major programs in correct sequence for simplified use
	public CKGraphicsAsset pipeline(){
		this.generateSpritesheet();
		this.moveSpritesheet();
		return this.createAsset();
	}
	//Returns String of all information about spritesheet
	public String toString(){
		String toReturn = "***"+this.characterName+"***\n"+this.sheetFileName+"\n"
				+this.basePath+"\n"
		+this.numColumns+"x"+this.numRows+" tiles | "
		+this.totalFrames+" frames\n"
		+this.imgWidth+"x"+this.imgHeight+"p | "
		+this.frameRate+"fps\n";
		return toReturn;
	}
	//Returns the number of actions in the spritesheet
	public int size(){
		return actionList.size();
	}
	public CKSpritesheetActionNode get(int index){
		return actionList.get(index);
	}
	public int indexIs(String nameAction){
		CKSpritesheetActionNode searchAction = new CKSpritesheetActionNode(nameAction,0);
		return this.actionList.indexOf(searchAction);
	}
	public boolean removeAction(String nameAction){
		int index = indexIs(nameAction);
		if (index!=-1){
			this.actionList.remove(indexIs(nameAction));
			return true;
		}
		else{
			return false;
		}
	}
	//Simple node class for holding information about actions
	protected class CKSpritesheetActionNode{
		String actionName;
		int actionFrames;
		public CKSpritesheetActionNode(String name, int frames){
			this.actionName = name;
			this.actionFrames = frames;
		}
		public String getActionName() {
			return actionName;
		}
		public void setActionName(String actionName) {
			this.actionName = actionName;
		}
		public int getActionFrames() {
			return actionFrames;
		}
		public void setActionFrames(int actionFrames) {
			this.actionFrames = actionFrames;
		}
		public boolean equals(Object o){
			CKSpritesheetActionNode ckNode = (CKSpritesheetActionNode) o;
			return (this.actionName.compareToIgnoreCase(ckNode.getActionName())==0);
		}
	}

	public static void main(String[] args) {
		try{
			BlenderSpritesheetAsset test_ = new BlenderSpritesheetAsset("D:/BlenderPipeline/Output/teleport2/teleport2.txt");
			System.out.println(test_);
			CKGraphicsAsset newAsset = test_.pipeline();
			CKGraphicsAsset A1=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(newAsset.getAID());	
			JFrame frame = new JFrame();
			CKAssetViewer view=new CKAssetViewer(1,A1,null,true);
			frame.add(view);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		catch(FileNotFoundException e){System.out.println(e.getMessage());}
	}
}
