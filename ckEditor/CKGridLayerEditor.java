package ckEditor;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitPane;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKPosition;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.CKGridFactory;
import ckDatabase.CKGridItemFactory;
import ckEditor.DataPickers.CKXMLAssetUsagePicker;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridItem;
import ckGameEngine.CKSharedGridItem;
import ckGraphicsEngine.CKGraphicsScene;
import ckGraphicsEngine.CKSceneSlider;
import ckGraphicsEngine.CKSceneViewer;
import ckGraphicsEngine.CKTileHighlighter;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckGraphicsEngine.layers.CKStaticMatrixLayer;

public class CKGridLayerEditor extends CKXMLAssetPropertiesEditor<CKGrid> 
implements MouseWheelListener,DocumentListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	JPanel controls;
	
	CKSceneViewer sceneViewer;//center
	
	CKXMLFilteredAssetPicker<CKGridItem,CKGridItemFactory> assetPicker;
	
	JPanel card;
	JPanel header;
	
	CKXMLAssetPropertiesEditor<CKGridItem> E=null;
	CKXMLAssetUsagePicker<CKGrid,CKGridFactory> usages;
	CKGraphicsScene scene;
	//CKGridGraphicsLayer layer;
	CKGridItemViewer assetView;
	
	CKGrid grid;
	
	
	int rows=10;
	int cols=10;
	
	CKAbstractGridItem currentlySelectedAsset=new CKGridItem();
	
	public CKGridLayerEditor(CKGrid grid)
	{
		setLayout(new BorderLayout());
		setGrid(grid);
	}
	
	
	private void setGrid(CKGrid grid)
	{
		this.grid=grid;
		rows = grid.getWidth();
		cols= grid.getHeight();
		
		//setPreferredSize(new Dimension(800,600));
		initComponents();
		showMyComponents();
	}
	
	JRadioButton addMode;
	JRadioButton removeMode;
	JRadioButton editMode;
	boolean editing = false;
	
	
	JRadioButton bottomMode;
	JRadioButton allMode;
	JRadioButton topMode;
	
	JRadioButton sharedMode;
	JRadioButton uniqueMode;
	
	
	
	private void createControlPanel()
	{
		controls = new JPanel();
		controls.setPreferredSize(new Dimension(150,400));
		controls.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                controls.getBorder()));

		controls.setLayout(new BoxLayout(controls,BoxLayout.Y_AXIS));
		
		JPanel mainMode = new JPanel();
		mainMode.setLayout(new BoxLayout(mainMode,BoxLayout.Y_AXIS));
		mainMode.add(new JLabel("Mode"));
		addMode = new JRadioButton("Add Item");
		addMode.setSelected(true);
		EditingPanelListener el = new EditingPanelListener();
		addMode.addActionListener(el);

		removeMode = new JRadioButton("Remove Item");
		removeMode.addActionListener(el);
		editMode = new JRadioButton("Edit Mode");
		editMode.addActionListener(el);
		
		ButtonGroup modeBG  = new ButtonGroup();
		modeBG.add(addMode);
		modeBG.add(removeMode);
		modeBG.add(editMode);
		mainMode.add(addMode);
		mainMode.add(removeMode);
		mainMode.add(editMode);
		controls.add(mainMode);
		
		
				
		JPanel baseMode = new JPanel();
		baseMode.setLayout(new BoxLayout(baseMode,BoxLayout.Y_AXIS));
		baseMode.add(new JLabel("Add/Remove"));
		bottomMode = new JRadioButton("Below Base");
		allMode = new JRadioButton("At Base");
		allMode.setSelected(true);
		topMode = new JRadioButton("At Top");
		ButtonGroup mode  = new ButtonGroup();
		mode.add(bottomMode);
		mode.add(allMode);
		mode.add(topMode);
		baseMode.add(bottomMode);
		baseMode.add(allMode);
		baseMode.add(topMode);
		controls.add(baseMode);
		
		
		JPanel replaceMode = new JPanel();
		replaceMode.setLayout(new BoxLayout(replaceMode,BoxLayout.Y_AXIS));
		replaceMode.add(new JLabel("Shared/Unique"));
		sharedMode = new JRadioButton("Shared");
		sharedMode.setSelected(true);
		uniqueMode = new JRadioButton("Unique");
		ButtonGroup replace  = new ButtonGroup();
		replace.add(sharedMode);
		replace.add(uniqueMode);
		replaceMode.add(sharedMode);
		replaceMode.add(uniqueMode);
		//replaceMode.setAlignmentX(LEFT_ALIGNMENT);
		controls.add(replaceMode);
		
		
		usages=new CKXMLAssetUsagePicker<CKGrid,CKGridFactory>(grid,CKGridFactory.getInstance(),true);
		usages.setAlignmentX(LEFT_ALIGNMENT);
		controls.add(usages);
		
		
		
		
		
		
	}
	
	
	private void initComponents()
	{
		
		//buttonPanel=new CKMatrixLayerButtonPanel();
		
		
		
		createControlPanel();
		
		/*scene = new CKGraphicsScene("","example",new Dimension(200,200),new Dimension(rows,cols));
		layer =  new CKGridGraphicsLayer("Testing","hope",grid,CKGraphicsLayer.GROUND_LAYER);
		//initDefaultMatrixLayer();
		
		scene.addLayer(layer);
		*/
		scene = new CKGraphicsScene("","example",grid);
		scene.addLayer(new CKStaticMatrixLayer("","layout", grid.getWidth(),grid.getHeight(),
				CKGraphicsLayer.BACKGROUND2_LAYER));
		
		sceneViewer = new CKSceneViewer(scene, 30);
		sceneViewer.setPreferredSize(new Dimension(640,700));
		
		//layerControlPanel=new CKLayerControlPanel(scene);
		//sceneViewer.addMouseWheelListener(layerControlPanel);
		
		addLayerInteractionInterfaces();
		
		CKGridItemFactory factory = CKGridItemFactory.getInstance();
		assetPicker = new CKXMLFilteredAssetPicker<CKGridItem,CKGridItemFactory>(factory);
		assetPicker.addSelectedListener(new SelectedAssetListener());
		
		card=new JPanel();
		card.add(assetPicker);
		
		
				
		assetView = new CKGridItemViewer(10, currentlySelectedAsset, 
									new Dimension(100,100), false);
		
		createHeader();
	}


	
	JTextField name;
	JTextField description;
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
		
		
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
		idLabel =new JLabel("FileName: "+CKGridFactory.getInstance().getBaseDir()+grid.getAID()); 
		//bottom.add(idLabel);
		
		bottom.add(new JLabel("Name"));
		name = new JTextField(grid.getName());
		name.getDocument().addDocumentListener(this);
		bottom.add(name);
		
		bottom.add(new JLabel("Description"));
		description = new JTextField(grid.getDescription());
		description.getDocument().addDocumentListener(this);
		bottom.add(description);
		header.add(bottom);
		//header.add(idLabel);
		
		
	}

	
	
	private void showMyComponents()
	{
		this.removeAll();
		
		MultiSplitPane plane = new MultiSplitPane();
		//setLayout(new BorderLayout());
		//String layoutDef = 	"(COLUMN (ROW  control assetV) (ROW gridV card))";
		//String layoutDef = 	"(ROW (COLUMN control usages) (LEAF name=gridV weight=1.0) (COLUMN assetV card))";
		//String layoutDef = 	"(ROW (COLUMN header control usages) (LEAF name=gridV weight=1.0) (COLUMN assetV card))";
		String layoutDef = 	"(COLUMN header (ROW (COLUMN control usages) (LEAF name=gridV weight=1.0) (COLUMN assetV card) )footer)";
		
		
		
//				"(ROW (COLUMN (LEAF name=viewer weight=1.0) usages) (COLUMN  properties )) ";
				//"(COLUMN assetid (ROW (COLUMN viewer usages weight=1.0) properties )";
		
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		plane.getMultiSplitLayout().setModel(modelRoot);
		
		
		/*JPanel plane = new JPanel();
		plane.setLayout(new BorderLayout());
		
		plane.add(sceneViewer,BorderLayout.CENTER);
		//add(layerControlPanel,"control");
		plane.add(controls,BorderLayout.WEST);
		//plane.add(usages,"usages");
		
		card.setLayout(new BorderLayout());
		card.add(assetView,BorderLayout.NORTH);
		card.add(assetPicker,BorderLayout.CENTER);
		plane.add(card,BorderLayout.EAST);
		*/
		
		plane.add(sceneViewer,"gridV");
		//add(layerControlPanel,"control");
		//JPanel left = new JPanel();
		//left.setLayout(new BoxLayout())
		plane.add(controls,"control");
		//plane.add(usages,"usages");
		plane.add(assetView,"assetV");
		plane.add(assetPicker,"card");
		plane.add(header,"header");
		plane.add(idLabel,"footer");
		
		
		
		//plane.add(card,"card");
		/*if(editMode.isSelected())
		{
			plane.add( E,"card");
		}
		else
		{

		}
		*/
		
		add(plane);
		this.revalidate();
	}

	
	private void addLayerInteractionInterfaces()
	{
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		CKGraphicsAsset front=factory.getGraphicsAsset("yellowFH");
		CKGraphicsAsset back=factory.getGraphicsAsset("yellowRH");
				
		CKAssetInstance hfront = new CKAssetInstance(new CKPosition(),front,2);
		CKAssetInstance hback = new CKAssetInstance(new CKPosition(),back,3);
	
		sceneViewer.addMouseListener(new CKTileHighlighter(scene,hfront,hback,8002,8003));
		
		CKSceneSlider slider = new CKSceneSlider(scene);
		//sceneViewer.addMouseMotionListener(slider);
		sceneViewer.addMouseListener(slider);
		
		
		
		sceneViewer.addMouseListener(new PlaceSelectedAssetListener());
		sceneViewer.addMouseWheelListener(this);
		sceneViewer.addMouseMotionListener(new ShowCoordsListener());
		
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		Point p = e.getPoint();
		Point mapCoords = scene.getTrans().convertScreenToMap(p);
		//now to see how much to change by.
		int units=e.getWheelRotation();
		
//		layer.changeHeight(mapCoords.x,mapCoords.y,units*-.25);
		
		CKAbstractGridItem item =grid.getPosition(mapCoords.x,mapCoords. y);
		item.changeHeight(units*-.25);
		
	}
	
	
	public void changeSelectedAsset(CKAbstractGridItem entity)
	{
		currentlySelectedAsset=entity;
		assetView.setAsset(entity);
		if(editMode.isSelected())
		{
			editing=true;
			E=((CKGridItem) currentlySelectedAsset).getXMLPropertiesEditor();
			this.showMyComponents();
		}
	}
	
	
	class EditingPanelListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(! editMode.isSelected())
			{
				if(editing)
				{
					editing=false;
					showMyComponents();
				}
			}
			
			
		}
		
	}

	class SelectedAssetListener implements CKEntitySelectedListener<CKGridItem>
	{
		public void entitySelected(CKGridItem entity) 
		{
			changeSelectedAsset(entity);
		}
		
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
				int Z = grid.getTopPosition((int) coords.getX(), (int)coords.getY()).getTotalHeight();
				coord.setText("X: "+coords.getX()+ " Y: "+coords.getY()+" Z: "+Z);

			}
			
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
	
	public static void main(String[] args)
	{
			
			CKGrid grid = new CKGrid(10,10);
			//now to place some tiles.
					
			for(int i =2;i<=8;i++)
				for(int j =2;j<=8;j++)
				{
					CKAbstractGridItem land= new CKGridItem();
					land.setAssetID("blue");
					land.setMoveCost(1);
					grid.setPosition(land, i, j);				
				}
			
			
			for(int i =4;i<=6;i++)
				for(int j =4;j<=6;j++)
				{
					CKAbstractGridItem block= new CKGridItem();
					block.setAssetID("pineBlock");
					block.setMoveCost(2);
					block.setItemHeight(1);
					grid.addToPosition(block, i,j);
				}
			
			CKAbstractGridItem bigBlock=new CKGridItem();
			bigBlock.setAssetID("stoneBlock");
			bigBlock.setMoveCost(1);
			bigBlock.setItemHeight(2);
			grid.addToPosition(bigBlock,5, 5);
			
			
		//grid.writeToStream(System.out);
		
		
		JFrame frame=new JFrame();
		
		CKGridLayerEditor e=new CKGridLayerEditor(grid);
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
	public CKGrid getAsset()
	{
		return grid;
	}


	@Override
	public void storeState()
	{
		grid.setName(name.getText());
		grid.setDescription(description.getText());
	}

	
	class SaveAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(grid!=null)
			{
				CKGridFactory.getInstance().writeAssetToXMLDirectory(grid);
				usages.saveUsages();
			}
			
		}		
	}
	
	
	
	
	
	class SaveAsNewAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(grid!=null)
			{
				grid.setAID("");
				CKGridFactory.getInstance().writeAssetToXMLDirectory(grid);
				usages.saveUsages();
			}
			
		}		
	}
	
	class LoadAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			CKXMLFilteredAssetPicker<CKGrid,CKGridFactory> picker =
					new CKXMLFilteredAssetPicker<CKGrid, CKGridFactory>(CKGridFactory.getInstance());		
			picker.addSelectedListener(new PickerListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	class PickerListener implements CKEntitySelectedListener<CKGrid>
	{
		JFrame frame;
		public PickerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(CKGrid a)
		{
			setGrid(a);
	
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}
	
	class NewCopy implements ActionListener
	{

		public SpinnerNumberModel widthSpin=new SpinnerNumberModel(10,1,1000,1);
		public SpinnerNumberModel heightSpin=new SpinnerNumberModel(10,1,1000,1);
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			JPanel p = new JPanel(new BorderLayout());

			
			JPanel spinnerPanel = new JPanel();
			spinnerPanel.setLayout(new FlowLayout());
			spinnerPanel.add(new JLabel("Width"));
			JSpinner spinW = new JSpinner(widthSpin);
			spinnerPanel.add(spinW);
			
			spinnerPanel.add(new JLabel("Height"));
			JSpinner spinH = new JSpinner(heightSpin);
			spinnerPanel.add(spinH);

			p.add(spinnerPanel);
				
			JButton create = new JButton("Create");
			p.add(create,BorderLayout.SOUTH);
			create.addActionListener(new CopySet(this,frame));
					

			
			frame.add(p);
			frame.pack();
			frame.setVisible(true);
			
		}		
	}
	
	
	class CopySet implements ActionListener
	{

		NewCopy copy;
		JFrame frame;

		
		
		
		
		
		public CopySet(NewCopy newCopy, JFrame frame2)
		{
			copy=newCopy;
			frame=frame2;
		}






		@Override
		public void actionPerformed(ActionEvent e)
		{
			setGrid(new CKGrid(copy.widthSpin.getNumber().intValue(),copy.heightSpin.getNumber().intValue()));
			
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}
		
	}
	

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		storeState();
		
	}


	@Override
	public void removeUpdate(DocumentEvent e)
	{
		storeState();
		
	}


	@Override
	public void changedUpdate(DocumentEvent e)
	{
		storeState();
		
	}
	
	
}


