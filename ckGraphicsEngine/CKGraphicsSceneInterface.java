package ckGraphicsEngine;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKWorkSupervisorListener;
import ckCommonUtils.CKXMLAsset;
import ckGameEngine.CKGrid;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckGraphicsEngine.sceneAction.CKSceneAction;

public interface CKGraphicsSceneInterface extends CKXMLAsset<CKGraphicsSceneInterface>
{

	
	public abstract void calcState();
	public abstract void drawOffScreenBuffer(Graphics g,int width,int height);
	public abstract void drawOffScreenBuffer(GraphicsContext g,int width,int height);
	
	
	/**
	 * @return the trans
	 */
	public abstract CKCoordinateTranslator getTrans();

	public abstract void loadActions(LinkedList<CKSceneAction> a);

	public abstract Iterator<CKGraphicsLayer> getInteractiveLayerIter();

	public abstract void resetLayers();

	public abstract void startLoading();
	

	public abstract void addLayer(CKGraphicsLayer L);

	public abstract void addAssetToLayer(CKPosition pos, CKGraphicsAsset inst,
			int layerDepth);

	public abstract void addInstanceToLayer(CKAssetInstance inst, int layerDepth);
	
	public abstract boolean removeInstanceFromScene(CKAssetInstance asset);
	public abstract CKAssetInstance getCamera();
	public abstract Iterator<CKGraphicsLayer> layerIterator();
	public void removeLayer(CKGraphicsLayer layer);
	

	public  boolean workCompleted();
	public void addSingleWorkSupervisorListener(CKWorkSupervisorListener<CKGraphicsScene> boss);
	public abstract void clearHighlights();
	public abstract CKGrid getGrid();
	
}