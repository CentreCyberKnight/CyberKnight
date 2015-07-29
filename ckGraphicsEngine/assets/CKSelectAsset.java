/*Author: Chadwick Carter
 * 7-17-15
 *Select Asset is for an action that uses a specific sequence of images in a spritesheet.
 *Should be used in between RegulatedAsset and SpriteAsset
 *Instead of actions being rows like with SharedAsset, SelectAsset creates an abstract "row"
 *for drawToGraphics that can be anywhere from 1 image to the entire spritesheet 
 */
//Can't wrap composite asset
package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import ckDatabase.CKGraphicsAssetFactoryXML;

public class CKSelectAsset extends CKGraphicsAsset {
	CKGraphicsAsset asset = CKNullAsset.getNullAsset();
	String assetID = "";
	int framesPerRow;//get from superclass
	int numFramesAction;
	int initialFrame;
//create class for 
	public CKSelectAsset(){
		super("","");
	}
	public CKSelectAsset(String aid, String description) {
		super(aid, description);
		// TODO Auto-generated constructor stub
	}

	public CKSelectAsset(String description) {
		super(description);
		// TODO Auto-generated constructor stub
	}
	protected CKSelectAsset(String aid, String desc,String innerAID,int numFrames, int startingFrame)
	{
		super(aid,desc);
		this.setAssetID(innerAID);
		this.numFramesAction = numFrames;
		this.initialFrame = startingFrame;
	}
	protected class coordinateNode{
		int theRow;
		int theFrame;
		protected coordinateNode(int row, int frame){
			this.theRow = row;
			this.theFrame = frame;
			
		}
		/**
		 * @return the theRow
		 */
		public int getTheRow() {
			return theRow;
		}
		/**
		 * @param theRow the theRow to set
		 */
		public void setTheRow(int theRow) {
			this.theRow = theRow;
		}
		/**
		 * @return the theFrame
		 */
		public int getTheFrame() {
			return theFrame;
		}
		/**
		 * @param theFrame the theFrame to set
		 */
		public void setTheFrame(int theFrame) {
			this.theFrame = theFrame;
		}
		
		
	
	}

	/**
	 * @return the initialFrame
	 */
	public int getInitialFrame() {
		return initialFrame;
	}
	/**
	 * @return the numFramesAction
	 */
	public int getNumFramesAction() {
		return numFramesAction;
	}
	/**
	 * @param numFramesAction the numFramesAction to set
	 */
	public void setNumFramesAction(int numFramesAction) {
		this.numFramesAction = numFramesAction;
	}
	/**
	 * @param initialFrame the initialFrame to set
	 */
	public void setInitialFrame(int initialFrame) {
		this.initialFrame = initialFrame;
	}
	
	/**
	 * @return the assetID
	 */
	public String getAssetID() {
		return assetID;
	}
	/**
	 * @param assetID the assetID to set
	 */
	public void setAssetID(String assetID) {
		this.assetID = assetID;
		asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID);
		this.framesPerRow = asset.getFrames(0);
	}
	//Sets the values of the class variables drawFrame and drawRow according to the current overall game frame
	public coordinateNode calculateRow_Frame(int gameFrame){
		int finalFrame = initialFrame + numFramesAction-1;
		int firstFrameNewRow = finalFrame - (finalFrame-framesPerRow);
		int curFrame = (((gameFrame%numFramesAction)%framesPerRow)+initialFrame)%firstFrameNewRow;
		int sequenceFrame = (gameFrame%numFramesAction)+initialFrame;
		int curRow = (sequenceFrame-curFrame)/framesPerRow;
		return new coordinateNode(curRow,curFrame);
	}
	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny, ImageObserver observer) {
		// TODO Auto-generated method stub
		//draw whole animation
		for (int i=0;i<numFramesAction;i++){
			coordinateNode cNode = calculateRow_Frame(i);
			asset.drawToGraphics(g,screenx,screeny,cNode.getTheFrame(),cNode.getTheRow(),observer);
			screenx = screenx + asset.getWidth(0);
			
			}
	}

	@Override
	public void drawToGraphics(Graphics g, int screenx, int screeny, int frame, int row, ImageObserver observer) {
		// TODO Auto-generated method stub
		coordinateNode cNode = calculateRow_Frame(frame);
		asset.drawToGraphics(g,screenx,screeny,cNode.getTheFrame(),cNode.getTheRow(),observer);
		

	}

	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny, int row, ImageObserver observer) {
		// TODO Auto-generated method stub
		//is this one useless for this type of asset?
		//draw whole asset one at a time
		//also no frame
		for (int i=0;i<numFramesAction;i++){
			coordinateNode cNode = calculateRow_Frame(i);
			asset.drawToGraphics(g,screenx,screeny,cNode.getTheFrame(),cNode.getTheRow(),observer);
			screenx = screenx + asset.getWidth(0);
			}

	}

	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny, int frame, ImageObserver observer) {
		// TODO Auto-generated method stub
		coordinateNode cNode = calculateRow_Frame(frame);
		asset.drawToGraphics(g, screenx, screeny, cNode.getTheFrame(),cNode.getTheRow(), observer);

	}

	@Override
	public int getFrames(int row) {
		// TODO Auto-generated method stub
		return numFramesAction;
	}

	@Override
	public int getRows() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getHeight(int row) {
		// TODO Auto-generated method stub
		//return height of current row
		return asset.getHeight(row);
	}

	@Override
	public int getWidth(int row) {
		// TODO Auto-generated method stub
		return asset.getWidth(row);
	}

	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds) {
		// TODO Auto-generated method stub
		coordinateNode cNode = calculateRow_Frame(frame);
		asset.getDrawBounds(cNode.getTheFrame(),cNode.getTheRow(), off, bounds);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*CKSelectAsset test_Select = new CKSelectAsset("Test","test",7,6,3);
		for (int i=0;i<60;i++){
			test_Select.calculateRow_Frame(i);
			System.out.println(test_Select.drawRow+", "+test_Select.drawFrame);*/
			
	}
}



