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


import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.*;
import java.lang.ProcessBuilder;
import java.io.*;
import java.util.*;
import java.lang.System;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.XMLDirectories;
//import javax.swing.JFrame;


public class CKSpritesheetAsset {
	public static final String IMG_EXTENSION=".png";
	ArrayList<CKSpritesheetActionNode> actionList;
	String characterName;
	String sheetFileName;
	int totalFrames;
	int numColumns;
	int imgWidth;
	int imgHeight;
	int numRows;
	int frameRate;
	String basePath;
	//Need full path to text file
	//Constructor used with a text file from DAZ script/java
	public CKSpritesheetAsset(String txtFileName) throws FileNotFoundException{ 
		this.actionList = new ArrayList<CKSpritesheetActionNode>();
		FileReader fin = new FileReader(txtFileName);
		Scanner scan = new Scanner(fin);
		String spritesheetInfo = scan.nextLine();
		scan.close();
		String[] inTwo = spritesheetInfo.split("@");
		String[] spritesheetData = inTwo[0].split(";");
		this.characterName = spritesheetData[0];
		this.basePath = spritesheetData[1];
		this.totalFrames = Integer.parseInt(spritesheetData[2]);
		calculateTiles();
		this.imgWidth = Integer.parseInt(spritesheetData[3]);
		this.imgHeight = Integer.parseInt(spritesheetData[4]);
		this.frameRate = Integer.parseInt(spritesheetData[5]);
		this.sheetFileName = spritesheetData[0]+IMG_EXTENSION;
		String[] actionData = inTwo[1].split(";");
		for (int j=0;j<actionData.length;j++){
			String actData = actionData[j];
			String[] action = actData.split(",");
			int nFrm = Integer.parseInt(action[1]);
			addAction(action[0],nFrm);
		}
	}
	
	public CKSpritesheetAsset(String charName,String filename,int numFrames, int width,int height,int framerate){ 
		this.actionList = new ArrayList<CKSpritesheetActionNode>();
		this.characterName = charName;	
		this.sheetFileName = filename;
		this.totalFrames = numFrames;
		this.imgWidth = width;
		this.imgHeight = height;
		this.frameRate = framerate;
		this.basePath = "";
		calculateTiles();
	}
	
	public CKSpritesheetAsset(String charName,String filename,int numFrames,int frames_Row,int num_Rows, int width,int height,int framerate){ 
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
	}
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
		commandList.add("montage");
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
		String tarString = "C:/Users/Test/git/CK_DATA/CK_DATA/"+XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+this.sheetFileName;
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
		String spriteAssetID = this.characterName+"_Sprite";
		CKSpriteAsset characterSprite = new CKSpriteAsset(spriteAssetID,this.characterName);
		for(int i=0;i<this.size();i++){
			CKSpritesheetActionNode action = this.actionList.get(i);
			String actionName = action.getActionName();
			int num_Frames = action.getActionFrames();
			CKSelectAsset newAction = new CKSelectAsset(this.characterName+"_"+actionName+"ID",this.characterName+"_"+actionName,regAssetID,num_Frames,startFrame);
			CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(newAction);
			characterSprite.addAnimation(actionName, newAction);
			startFrame = startFrame + num_Frames;
			
			}
		if ((startFrame+1)==this.totalFrames){
			System.out.println("Frames aligned: "+this.totalFrames+" frames");
		}
		else{
			System.out.println("Frames don't match: "+(startFrame+1)+ "--"+this.totalFrames);
			
		}
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(characterSprite);
		((CKGraphicsAssetFactoryXML) CKGraphicsAssetFactoryXML.getInstance()).assignUsageTypeToAsset(spriteAssetID,"sprite");
		return characterSprite;
	}
	//Runs all 3 major programs in correct sequence for simplified use
	public CKGraphicsAsset pipeline(){
		this.generateSpritesheet();
		this.moveSpritesheet();
		return this.createAsset();
	}
	public String toString(){
		String toReturn = "***"+this.characterName+"***\n"+this.sheetFileName+"\n"
				+this.basePath+"\n"
		+this.numColumns+"x"+this.numRows+" tiles | "
		+this.totalFrames+" frames\n"
		+this.imgWidth+"x"+this.imgHeight+"p | "
		+this.frameRate+"fps\n"+"Actions\n";
		for (int i = 0;i<actionList.size();i++){
			CKSpritesheetActionNode element = actionList.get(i);
			toReturn = toReturn+element.getActionName()+": "+element.getActionFrames()+"\n";
		}
		return toReturn;
	}
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
		/*try{
			CKSpritesheetAsset test_ = new CKSpritesheetAsset("C:/Users/Chadwick/Desktop/CK_Pipeline/Images/elf/elf.txt");
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
		catch(FileNotFoundException e){System.out.println(e.getMessage());}*/
	}
}
