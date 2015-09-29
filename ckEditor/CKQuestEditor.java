package ckEditor;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitPane;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKPosition;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.CKGridFactory;
import ckDatabase.CKQuestFactory;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.QuestData;
import ckGraphicsEngine.*;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKQuestEditor extends CKXMLAssetPropertiesEditor<QuestData> implements CKGraphicsChangedListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	FX2dGraphicsEngine sceneViewer;//center
	JPanel sceneViewerPanel;
	JPanel header;	
	CKXMLAssetPropertiesEditor<QuestData> E=null;
	//CKXMLAssetUsagePicker<QuestData,CKQuestFactory> usages;
	CKGraphicsSceneInterface scene;
	QuestData quest;
	
	public CKQuestEditor(QuestData quest)
	{
		setQuest(quest);
	}
	
	
	private void setQuest(QuestData quest)
	{
		this.quest=quest;
		quest.addPositionSetter(pHelper);
		quest.addGraphicsChangedListener(this);
		
		E = quest.getXMLPropertiesEditor();
		initGraphicsEngine();
		createHeader();
		
		showMyComponents();
	}

	

	private void initGraphicsEngine()
	{
		CKGameObjectsFacade.killEngine();
		sceneViewer = CKGameObjectsFacade.getEngine();
		sceneViewer.loadScene(quest.getSceneID());
		scene = sceneViewer.getCKScene();
		sceneViewer.prefWidth(640);
		sceneViewer.prefHeight(700);
		
		CKGameObjectsFacade.getQuest().startTransaction();
		for(CKGridActor a:quest.getActors().getGridActors())
		{
			a.setInstanceID(-100);
			scene.getGrid().addToPosition(a, a.getPos());
		}
		CKGameObjectsFacade.getQuest().endTransaction();
		
		addLayerInteractionInterfaces();
		sceneViewerPanel =CKGameObjectsFacade.getJPanelEngine();
	}
	
	
	JLabel idLabel;
	JLabel coord;
	
	private void createHeader()
	{
		header = new JPanel(new GridLayout(1,2));
		
		JPanel top = new JPanel();
		
		
		JButton save = new JButton("Save");
		JButton saveCopy = new JButton("Save as Copy");
		JButton load = new JButton("Load");	
		JButton newCopy = new JButton("New Asset");	
		coord = new JLabel("X: 0 Y: 0 Z: 0");
		
		
		top.add(save);
		save.addActionListener(new SaveAsset());
		
		top.add(saveCopy);
		saveCopy.addActionListener(new SaveAsNewAsset());
		
		top.add(load);
		load.addActionListener(new LoadAsset());
		
		
		top.add(newCopy);
		newCopy.addActionListener(new NewCopy());
		
		top.add(coord);
		
		
		
		header.add(top);
		
		idLabel =new JLabel("FileName: "+CKGridFactory.getInstance().getBaseDir()+quest.getAID()); 
	}

	
	private MultiSplitPane plane;
	
	private void showMyComponents()
	{
		this.removeAll();
		
		plane = new MultiSplitPane();
		String layoutDef = 	"(COLUMN footer (LEAF name=header weight=1.0) (ROW (LEAF name=gridV weight=1.0) editor ))";
		
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		plane.getMultiSplitLayout().setModel(modelRoot);
		
		

		//plane.add(sceneviewer,"gridV");

		plane.add(sceneViewerPanel,"gridV");
	
		plane.add(E,"editor");
		//plane.add(usages,"usages");
		
		plane.add(header,"header");
		plane.add(idLabel,"footer");
		

		add(plane);
		this.revalidate();
	}
	private PickPositionHelper pHelper = new PickPositionHelper();
	
	private void addLayerInteractionInterfaces()
	{
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		CKGraphicsAsset front=factory.getGraphicsAsset("yellowFH");
		CKGraphicsAsset back=factory.getGraphicsAsset("yellowRH");
				
		CKAssetInstance hfront = new CKAssetInstance(new CKPosition(),front,2);
		CKAssetInstance hback = new CKAssetInstance(new CKPosition(),back,3);
	
		sceneViewer.addMouseMotionListener(new CKTileHighlighter(scene,hfront,hback,8002,8003));
		
		CKSceneSlider slider = new CKSceneSlider(scene);
		sceneViewer.addMouseMotionListener(slider);
		sceneViewer.addMouseListener(slider);
		//sceneViewer.addMouseListener(new PlaceSelectedAssetListener());
		
		
		sceneViewer.addMouseMotionListener(pHelper);
		sceneViewer.addMouseListener(pHelper);
		
		sceneViewer.addMouseMotionListener(new ShowCoordsListener());
		
	}
	
	
	

	
	
	class ShowCoordsListener extends MouseAdapter
	{

		@Override
		public void mouseMoved(MouseEvent e) {
			Point p=e.getPoint();
			Point coords=scene.getTrans().convertScreenToMap(p);
			if( scene.getTrans().confineToMapCoords(coords))
			{
				coord.setText("Off the Grid");
			}
			else
			{
				int Z = scene.getGrid().getTopPosition((int) coords.getX(), (int)coords.getY()).getTotalHeight();
				coord.setText("X: "+coords.getX()+ " Y: "+coords.getY()+" Z: "+Z);

			}
			
		}
		
	}
	
	class PickPositionHelper extends MouseAdapter implements CKPositionSetter
	{

		CKPositionSetterListener listener = null;
		CKAbstractGridItem placeItem=null;
		boolean leaveAfterDone = false;
		
		protected void clearItem()
		{
			//not a perfect check, but only here for speed
			CKAbstractGridItem bottom = scene.getGrid().getPosition(placeItem.getPos()); 
			if(bottom!=null && bottom.findItemWithAsset(placeItem.getAsset().getAID())!=null )
			{
				CKGameObjectsFacade.getQuest().startTransaction();
				placeItem.removeItemFromGrid(scene.getGrid());
				CKGameObjectsFacade.getQuest().endTransaction();
			}
		}
		
		protected void placeItem(Point p)
		{
			Point coords=scene.getTrans().convertScreenToMap(p);
			//System.err.println("mouse at "+coords.x+","+coords.y);
			
			if( scene.getTrans().confineToMapCoords(coords))
			{ //off grid
				clearItem();
			}
			else
			{
				//only do it if there is a change in position
				if( placeItem.getPos().getX() != coords.x || placeItem.getPos().getY()!=coords.y)
				{
					CKGameObjectsFacade.getQuest().startTransaction();
					scene.getGrid().addToPosition(placeItem, coords.x,coords.y);
					CKGameObjectsFacade.getQuest().endTransaction();
				}
			}	
			
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if(listener ==null) { return; }

			Point p = e.getPoint();
			Point coords=scene.getTrans().convertScreenToMap(p);
			if( scene.getTrans().confineToMapCoords(coords))
			{ //clicked off of grid - abort to previous value?
				//clearItem();
				coords.x=(int) listener.getPosition().getX();
				coords.y=(int) listener.getPosition().getY();

				if(leaveAfterDone)
				{
					CKGameObjectsFacade.getQuest().startTransaction();
					scene.getGrid().addToPosition(placeItem, coords.x,coords.y);
					CKGameObjectsFacade.getQuest().endTransaction();
				}
				else
				{
					clearItem();
				}
					
			}						
			else if(leaveAfterDone)
			{
				placeItem(p);
			}
			else
			{
				clearItem();
			}
	
			listener.setPosition(new CKPosition(coords.x,coords.y));
			listener=null;
		
		}



		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseMoved(MouseEvent e)
		{
			if(listener!=null)
			{
				placeItem(e.getPoint());
			}
		}



		@Override
		public void setPosition(CKPositionSetterListener l,
				CKAbstractGridItem item, boolean leave)
		{
			System.err.println("set position initialied");
			if(listener!=null) //clean up
			{ //no notification since they never clicked down
				clearItem();
			}
			
			
			listener=l;
			placeItem = item;
			leaveAfterDone = leave;			
		}
		
		
	
		
	}
	/*
	class PlaceSelectedAssetListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if (currentlySelectedAsset != null)
			{
				Point p=e.getPoint();
				Point coords=scene.getTrans().convertScreenToMap(p);
				
				if(addMode.isSelected())
				{
					CKAbstractGridItem item;
					if(sharedMode.isSelected() && currentlySelectedAsset instanceof CKGridItem
							&& ((CKGridItem) currentlySelectedAsset).getAID().length()>0)
					{
						item = (CKAbstractGridItem) 
								new CKSharedGridItem(((CKGridItem) currentlySelectedAsset).getAID() );
						
					}
					else //cannot be shared 
					{
						item = (CKAbstractGridItem) currentlySelectedAsset.makeCopy();
					}
					
					if(topMode.isSelected())
					{
						grid.addToPosition(item, coords.x, coords.y);
					}
					else if(allMode.isSelected())
					{
						grid.setPosition(item, coords.x, coords.y);
					}
					else //bottommode
					{
						CKAbstractGridItem topItems= grid.getPosition(coords.x,coords.y);
						grid.setPosition(item, coords.x, coords.y);
						grid.addToPosition(topItems, coords.x, coords.y);
					}
					
					System.out.println("item position is"+item.getPos());
				}
				else if (removeMode.isSelected())
				{
					
					if(allMode.isSelected())
					{	
						grid.setPosition(new CKGridItem(), coords.x, coords.y);
					}
					else
					{
						CKAbstractGridItem top = grid.getTopPosition(coords.x, coords.y);
						CKAbstractGridItem bottom = grid.getPosition(coords.x, coords.y);
						if(top == bottom) //only one thing there
						{
							grid.setPosition(new CKGridItem(), coords.x, coords.y);
						}
						else if(topMode.isSelected())
						{
							top.removeItemFromGrid(grid);
						}
						else //bottom mode
						{
							bottom.removeItemFromGrid(grid);
						}
						
					}
				}
				else if (editMode.isSelected())
				{
					if(topMode.isSelected())
					{
						changeSelectedAsset(grid.getTopPosition(coords.x, coords.y));
					}
					else
					{
						changeSelectedAsset(grid.getPosition(coords.x, coords.y));
					}
						
				}
				
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
	*/
	public static void main(String[] args)
	{
			
			
			
			
		//grid.writeToStream(System.out);
		
		
		JFrame frame=new JFrame();
		
		//CKQuestEditor e=new CKQuestEditor(CKQuestFactory.getInstance().getAssetInstance());
		CKQuestEditor e=new CKQuestEditor(CKQuestFactory.getInstance().getAsset("asset7364953977011982560"));
		frame.add(e);
		frame.pack();
		frame.setVisible(true);
		
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	}
		);
		
	}
	private Vector<ChangeListener> listeners = new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	


	@Override
	public QuestData getAsset()
	{
		return quest;
	}


	@Override
	public void storeState()
	{
		E.storeState();
//		E.setName(name.getText());
//		grid.setDescription(description.getText());
	}

	
	class SaveAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(quest!=null)
			{
				
				CKQuestFactory.getInstance().writeAssetToXMLDirectory(quest);
				//TODO usages.saveUsages();
			}
			
		}		
	}
	
	
	
	
	
	class SaveAsNewAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(quest!=null)
			{
				quest.setAID("");
				CKQuestFactory.getInstance().writeAssetToXMLDirectory(quest);
				//TODO usages.saveUsages();
			}
			
		}		
	}
	
	class LoadAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			CKXMLFilteredAssetPicker<QuestData,CKQuestFactory> picker =
					new CKXMLFilteredAssetPicker<QuestData, CKQuestFactory>(CKQuestFactory.getInstance());		
			picker.addSelectedListener(new PickerListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	class PickerListener implements CKEntitySelectedListener<QuestData>
	{
		JFrame frame;
		public PickerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(QuestData a)
		{
			setQuest(a);
	
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}
	
	class NewCopy implements ActionListener
	{

		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			setQuest(CKQuestFactory.getInstance().getAssetInstance());			
		}		
	}
	
	
	@Override
	public void graphicsChanged()
	{
		plane.remove(sceneViewerPanel);
		this.initGraphicsEngine();
		plane.add(sceneViewerPanel,"gridV");
		plane.revalidate();
	}
	
}


