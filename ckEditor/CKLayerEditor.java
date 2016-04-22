package ckEditor;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKTabPane;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.CKGraphicsLayerFactory;
import ckDatabase.CKGraphicsLayerFactoryXML;
import ckEditor.DataPickers.CKAssetPicker;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckGameEngine.CKGrid;
import ckGraphicsEngine.CKGraphicsScene;
import ckGraphicsEngine.CKLayerControlPanel;
import ckGraphicsEngine.CKSceneSlider;
import ckGraphicsEngine.CKSceneViewer;
import ckGraphicsEngine.CKTileHighlighter;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckGraphicsEngine.layers.CKStaticMatrixLayer;

public class CKLayerEditor extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CKLayerControlPanel layerControlPanel;
	CKMatrixLayerButtonPanel buttonPanel;//west
	CKSceneViewer sceneViewer;//center
	CKAssetPicker assetPicker;
	CKGraphicsScene scene;
	CKStaticMatrixLayer layer;
	int rows=10;
	int cols=10;
	
	CKGraphicsAsset currentlySelectedAsset=null;
	
	public CKLayerEditor()
	{
		setPreferredSize(new Dimension(800,800));
		initComponents();
		addComponents();
	}
	
	private void initComponents()
	{
		buttonPanel=new CKMatrixLayerButtonPanel();
		scene = new CKGraphicsScene("","example",new CKGrid(rows,cols));
		layer = (CKStaticMatrixLayer) CKGraphicsLayerFactoryXML.getInstance().getGraphicsLayer("Kitchen");
		//initDefaultMatrixLayer();
		
		scene.addLayer(layer);
		
		sceneViewer = new CKSceneViewer(scene, 30);
		
		layerControlPanel=new CKLayerControlPanel(scene);
		sceneViewer.addMouseWheelListener(layerControlPanel);
		
		addLayerInteractionInterfaces();
		
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		assetPicker = new CKAssetPicker(factory.getAllGraphicsAssets());
		
		assetPicker.addSelectedListener(new SelectedAssetListener());
	}

	/*
	private void initDefaultMatrixLayer()
	{
		CKGraphicsAssetFactory afactory = CKGraphicsAssetFactoryXML.getInstance();
		CKGraphicsAsset gray =afactory.getGraphicsAsset("kTile");
		CKGraphicsAsset cliff=afactory.getGraphicsAsset("cliffFace");
		
		CKLayeredAsset defaultTile=new CKLayeredAsset("default");
		defaultTile.addAsset(gray);
		defaultTile.addAsset(cliff);
		
		for (int i=0;i<rows;i++)
		{
			for(int j=0;j<cols;j++)
			{
				CKPosition ckp=new CKPosition(i,j,0,0);
				layer.addAsset(ckp, defaultTile);
			}
			
		}
		//counters. . .
		
		CKLayeredAsset counter=new CKLayeredAsset("counter");
		CKImageAsset c=null;
		try {
			c = CKImageAsset.readImage("counter",64,64,1,1,TileType.OVER,
			  "images/counter.png");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		counter.addAsset(c);
		counter.addAsset(gray);
		counter.addAsset(cliff);
		//layer.addAsset(new CKPosition(), counter);
		
		for (int i=0; i<rows;i++)
		{
			layer.addAsset(new CKPosition(i,0,0,0), counter);
			layer.addAsset(new CKPosition(0,i,0,0), counter);
		}
		

		//fridge and stove
		CKLayeredAsset fridge=new CKLayeredAsset("fridge");
		CKImageAsset f=null;		
		CKLayeredAsset stove=new CKLayeredAsset("stove");		
		CKImageAsset s=null;
		try {
			f = CKImageAsset.readImage("fridge ",64,100,1,1,TileType.OVER,
			  "images/fridge.png");
			s = CKImageAsset.readImage("stove ",64,70,1,1,TileType.OVER,
					  "images/stove2.png");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		stove.addAsset(s);
		fridge.addAsset(f);
		stove.addAsset(gray);
		fridge.addAsset(gray);
		stove.addAsset(cliff);
		
		fridge.addAsset(cliff);
	

		layer.addAsset(new CKPosition(4,0,0,0), fridge);
		layer.addAsset(new CKPosition(0,4,0,0), stove);
		
	}
	*/	
	private void addComponents()
	{
		setLayout(new BorderLayout());
		add(sceneViewer,BorderLayout.CENTER);
		add(layerControlPanel, BorderLayout.NORTH);
		add(buttonPanel,BorderLayout.WEST);
		add(assetPicker,BorderLayout.EAST);
	}
	
	public void setLayer(CKStaticMatrixLayer _layer)
	{
		scene.removeLayer(layer);
		layer=_layer;
		scene.addLayer(layer);
		addLayerInteractionInterfaces();
		//TODO leave out old stuff --> make a new scene
	}
	
	private void addLayerInteractionInterfaces()
	{
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		CKGraphicsAsset front=factory.getGraphicsAsset("yellowFH");
		CKGraphicsAsset back=factory.getGraphicsAsset("yellowRH");
				
		CKAssetInstance hfront = new CKAssetInstance(new CKPosition(),front,2);
		CKAssetInstance hback = new CKAssetInstance(new CKPosition(),back,3);
	
		sceneViewer.addMouseListener(new CKTileHighlighter(scene,hfront,hback));
		sceneViewer.addMouseListener(new CKSceneSlider(scene));
		sceneViewer.addMouseListener(new PlaceSelectedAssetListener());
	}

	class CKMatrixLayerButtonPanel extends JPanel
	{
		
		private static final long serialVersionUID = 1L;

		public CKMatrixLayerButtonPanel()
		{
			setPreferredSize(new Dimension(100,500));
			initButtons();
		}
		
		private void initButtons()
		{
			JButton save=addButton("ckEditor/images/tools/save.png");
			JButton open=addButton("ckEditor/images/tools/open.png");
			JButton newLayer=addButton("ckEditor/images/tools/new.png");
			
			save.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
						CKGraphicsLayerFactoryXML.writeLayerToXMLDirectory(layer);
			}});
			
			open.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						//DatabaseBrowser db=new DatabaseBrowser();
						JDialog test=new JDialog();
						test.setModal(true);
						
						CKGraphicsLayerFactory factory = CKGraphicsLayerFactoryXML.getInstance(); 
						
					
						CKXMLAssetPicker<CKGraphicsLayer> picker = new CKXMLAssetPicker<CKGraphicsLayer>(factory.getAllGraphicsLayers());
						//CKGraphicsLayer tmp=null;
						picker.addSelectedListener(new CKEntitySelectedListener<CKGraphicsLayer>()
						{

							@Override
							public void entitySelected(CKGraphicsLayer entity) 
							{
								System.out.println("it might be working. . .");
								setLayer((CKStaticMatrixLayer)entity);
								
							}
							
						});
						
						test.setContentPane(picker);
						test.pack();
						test.setVisible(true);
						
						
						//setLayer();
						addLayerInteractionInterfaces();
						
						//layer=(CKStaticMatrixLayer)db.getRetrievedObject();
						//scene.addLayer(layer);
						
						//setLayer();
						
						//layerControlPanel=new CKLayerControlPanel(scene);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}			
			});

			newLayer.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
				CKStaticMatrixLayerInformationPanel p=new CKStaticMatrixLayerInformationPanel ();
				JDialog test=new JDialog();
				test.setModal(true);
				test.setContentPane(p);
				test.pack();
				test.setVisible(true);
				setLayer(p.getCKStaticMatrixLayer());
				//TODO build a brand new scene
				}});

			
			add(save);
			add(open);
			add(newLayer);
			
			
		}
		
		private JButton addButton(String fname)
		{
			ImageIcon icon=new ImageIcon(fname);
			JButton temp=new JButton(icon);
			return temp;
		}
		
	}
		
	class SelectedAssetListener implements CKEntitySelectedListener<CKGraphicsAsset>
	{
		public void entitySelected(CKGraphicsAsset entity) 
		{
			currentlySelectedAsset=entity;
		}
		
	}
	
	class PlaceSelectedAssetListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if (currentlySelectedAsset != null)
			{
				Point p=e.getPoint();
				Point coords=scene.getTrans().convertScreenToMap(p);
				
				layer.addAsset(new CKPosition(coords.x,coords.y,0,layer.getLayerDepth()), currentlySelectedAsset);
			}
			//scene.addAssetToLayer(new CKPosition(coords.x,coords.y,0,layer.getLayerDepth()), currentlySelectedAsset, layer.getLayerDepth());
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		CKTabPane pane=new CKTabPane();
		frame.add(pane);
		CKLayerEditor e=new CKLayerEditor();
		pane.addTab(e,"Layer Editor");
		//CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance(); 
		//CKCompositeAssetEditor ae=new CKCompositeAssetEditor((CKCompositeAsset) factory.getGraphicsAsset("heroSprite"));
		//pane.addTab(ae,"Asset Editor");
		//pane.addTab(new CKTreeGui(new QuestData(null)),"Tree GUI");
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	}
		);
		
	}
	
}


