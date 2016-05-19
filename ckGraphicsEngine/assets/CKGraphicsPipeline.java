/*Author: Chadwick Carter
 * 7-10-15
 * Pipeline for motion-capture generated spritesheets into the CK Graphics Engine
 * */
package ckGraphicsEngine.assets;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.XMLDirectories;
import ckGraphicsEngine.assets.CKSpritesheetAsset.CKSpritesheetActionNode;

public class CKGraphicsPipeline {
	public static final String IMG_EX = ".png";
	public String[] imgFileList;
	public CKGraphicsPipeline(){
	}
	public static CKGraphicsAsset createAssetOld(int numRows){
		CKImageAsset test_ImgAst = new CKImageAsset("Swordsman","SwordPersonImage",128,128,12,7,TileType.SPRITE,XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"spritesheet12.png");
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(test_ImgAst);
		System.out.println("Sup");
		CKRegulatedAsset test_RegAst = new CKRegulatedAsset("SwordsmanRegulated","RegulatedSwordPerson",test_ImgAst,12);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(test_RegAst);
		System.out.println("Done");
		//Array of shared assets by row
		//Can assign tags to array indices to describe actions?
		CKGraphicsAsset [] rowList = new CKGraphicsAsset [numRows];
		for(int i = 0;i<numRows;i++){
			rowList[i] = new CKSharedAsset("SwordsmanShared"+i,"SwordShared"+i,test_RegAst,i);
			CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(rowList[i]);
		}
		CKSpriteAsset Sword_sprite = new CKSpriteAsset("SwordsmanSprite","SwordSpriteAsset");
		for (int j = 0;j<numRows;j++){
			CKSpriteAsset.SpriteNode newAction = new CKSpriteAsset.SpriteNode(rowList[j],"action"+j);
			Sword_sprite.add(newAction);
		}
		System.out.println(Sword_sprite);
		return Sword_sprite;
	}
		public static CKGraphicsAsset createAsset_Test(){
			CKImageAsset test_ImgAst = new CKImageAsset("Swordsman","SwordPersonImage",128,128,12,7,TileType.SPRITE,XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"spritesheet12.png");
			CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(test_ImgAst);
			CKRegulatedAsset test_RegAst = new CKRegulatedAsset("SwordsmanRegulated","RegulatedSwordPerson",test_ImgAst,12);
			CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(test_RegAst);
			CKSelectAsset test_SelAst = new CKSelectAsset("SwordsmanSelect", "SwordSelect", "SwordsmanRegulated",37, 30);
			CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(test_SelAst);
			//Array of shared assets by row
			//Can assign tags to array indices to describe actions?
			/*CKGraphicsAsset [] rowList = new CKGraphicsAsset [numRows];
			for(int i = 0;i<numRows;i++){
				rowList[i] = new CKSharedAsset("SwordsmanShared"+i,"SwordShared"+i,test_RegAst,i);
				CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(rowList[i]);
			}
			CKSpriteAsset Sword_sprite = new CKSpriteAsset("SwordsmanSprite","SwordSpriteAsset");
			for (int j = 0;j<numRows;j++){
				CKSpriteAsset.SpriteNode newAction = new CKSpriteAsset.SpriteNode(rowList[j],"action"+j);
				Sword_sprite.add(newAction);
			}
			System.out.println(Sword_sprite);
			return Sword_sprite;*/
			return test_SelAst;
	}
	public static CKGraphicsAsset createAsset_v1(String characterName, String actionName, int imgWidth, int imgHeight, int framesPerRow, int numRows, String fileName){ //CKActionInfo actionList){
		/* characterName = name of sprite character
		 * actionName = name of action
		 * imgWidth = width of one image in spritesheet
		 * imgHeight = height of one image in spritesheet
		 * framesPerRow = number of frames in one row of spritesheet
		 * numRows = number of rows in spritesheet
		 * fileName = .png file name -- file must be in CKDATA/GRAPHICS/ASSET_IMAGES
		 * actionList = list of action names and lengths
		 */
		CKImageAsset imgAst = new CKImageAsset(characterName,actionName,imgWidth,imgHeight,framesPerRow,numRows,TileType.SPRITE,XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+fileName);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(imgAst);
		System.out.println("Sup");
		CKRegulatedAsset regAst = new CKRegulatedAsset(characterName+"_"+actionName+"_Regulated",actionName+"_Regulated",imgAst,12);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(regAst);
		System.out.println("Done");
		//Array of shared assets by row
		//Can assign tags to array indices to describe actions?
		CKGraphicsAsset [] rowList = new CKGraphicsAsset [numRows];
		for(int i = 0;i<numRows;i++){
			rowList[i] = new CKSharedAsset(characterName+"_"+actionName+"_Shared"+i,actionName+"_Shared"+i,regAst,i);
			CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(rowList[i]);
		}
		//This next line will need to be separate -- only create SpriteAsset first time uploading character
		//Need to create nodes for actions and add to existing CKSpriteAsset for character
		CKSpriteAsset spriteAst = new CKSpriteAsset(characterName+"_Sprite",actionName);
		for (int j = 0;j<numRows;j++){
			CKSpriteAsset.SpriteNode newAction = new CKSpriteAsset.SpriteNode(rowList[j],"action"+j);
			spriteAst.add(newAction);
		}
		System.out.println(spriteAst);
		return spriteAst;
	}
	public static void getImageLengths(String charName){
		//will get lengths of actions
	}

	public static void createSpritesheet(int imgWidth, int imgHeight,int numImages,int numColumns,int numRows,String basePath,String charName){
		File dir = new File(basePath);
		String[] imgFileList = dir.list(new FilenameFilter(){
			public boolean accept(File directory,String fileName){
				return fileName.endsWith(IMG_EX);}});
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add("montage");
		for (int i=1;i<imgFileList.length+1;i++){
			commandList.add(imgFileList[i-1]);
		}
		commandList.add("-tile");
		commandList.add(numColumns+"x"+numRows);
		commandList.add("-geometry");
		commandList.add(imgWidth+"x"+imgHeight+"+0+0");
		commandList.add("-background");
		commandList.add("none");
		commandList.add(charName+IMG_EX);
		try{
			ProcessBuilder pb = new ProcessBuilder(commandList);
			pb.directory(new File(basePath));
			pb.start();
			}
			catch(IOException e1){System.out.println(e1.getMessage());}
	}
	
	public static CKGraphicsAsset createAsset(CKSpritesheetAsset spritesheet_Ast){
		/* spritesheet_Info is created after spritesheet generation
		 */
		String characterName = spritesheet_Ast.getCharacterName();
		int allFrames = spritesheet_Ast.getTotalFrames();
		String fileName = spritesheet_Ast.getSheetFileName();
		CKImageAsset imgAst = new CKImageAsset(characterName+"_Image",characterName+"_spritesheet_"+allFrames,spritesheet_Ast.getImgWidth(),spritesheet_Ast.getImgHeight(),spritesheet_Ast.getNumColumns(),spritesheet_Ast.getNumRows(),TileType.SPRITE,XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+fileName);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(imgAst);
		String regAssetID = characterName+"_Regulated";
		int frameRate = spritesheet_Ast.getFrameRate();
		CKRegulatedAsset regAst = new CKRegulatedAsset(regAssetID,characterName+"_Regulated_"+frameRate+"fps",imgAst,frameRate);
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(regAst);
		int startFrame = 0;
		CKSpriteAsset characterSprite = new CKSpriteAsset(characterName+"_Sprite",characterName);
		for(int i=0;i<spritesheet_Ast.size();i++){
			CKSpritesheetActionNode action = spritesheet_Ast.get(i);
			String actionName = action.getActionName();
			int num_Frames = action.getActionFrames();
			CKSelectAsset newAction = new CKSelectAsset(characterName+"_"+actionName+"ID",characterName+"_"+actionName,regAssetID,num_Frames,startFrame);
			CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(newAction);
			characterSprite.addAnimation(actionName, newAction);
			startFrame = startFrame + num_Frames;
			
			}
		if (startFrame==allFrames){
			System.out.println("Frames aligned: "+allFrames+" frames");
		}
		else{
			System.out.println("Frames don't match: "+startFrame+ "--"+allFrames);
			
		}
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(characterSprite);
		return characterSprite;
	}
	public static void main(String[] args) {
		CKSpritesheetAsset actionTest = new CKSpritesheetAsset("Little_swordsman","Swordsman0111.png",70,10,7,32,64,10);
		actionTest.addAction("Mostly resting",10);
		actionTest.addAction("booo", 10);
		actionTest.addAction("Gearing up", 20);
		actionTest.addAction("Sword swipe",12);
		actionTest.addAction("cry", 18);
		CKGraphicsAsset newAsset = createAsset(actionTest);
		//CKGraphicsAsset newAsset = createAsset("Slasher","slash",128,128,12,7,"spritesheet12.png");
		//CKGraphicsAsset newAsset1 = createAsset_Test();
		//CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(newAsset1);
		CKGraphicsAsset A1=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(newAsset.getAID());	
		
		JFrame frame = new JFrame();

		CKAssetViewer view=new CKAssetViewer(1,A1,null,true);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		createSpritesheet(32,64,70,10,7,"C:/Users/Chadwick/Desktop/Swordsman 10 fps 32x64","Swordsman0111");

	}
		
}
