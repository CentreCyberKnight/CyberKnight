package ckEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitPane;


import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsLayerFactory;
import ckDatabase.CKGraphicsLayerFactoryXML;
import ckDatabase.CKGridFactory;
import ckDatabase.CKSceneFactory;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.CKGrid;
import ckGraphicsEngine.CKGraphicsScene;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.layers.CKGraphicsLayer;


public class CKScenePropertiesEditor extends CKXMLAssetPropertiesEditor<CKGraphicsSceneInterface> implements ChangeListener,DocumentListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8502775289068923594L;
	private CKGraphicsSceneInterface scene;
	private CKGraphicsLayerFactory afactory;

	private CKXMLFilteredAssetPicker<CKGrid,CKGridFactory> gridPicker;
	private CKXMLAssetPicker<CKGraphicsLayer> libraryPicker;
	
	private JTextField description;
	
	
	
		public CKScenePropertiesEditor(CKGraphicsSceneInterface scene)
		{
			this.scene=scene;
			afactory = CKGraphicsLayerFactoryXML.getInstance();
			
			
			//create picker for the grid
			gridPicker = new CKXMLFilteredAssetPicker<CKGrid,CKGridFactory>(CKGridFactory.getInstance());
			gridPicker.addSelectedListener(new ReplaceGrid());
			
			//create picker for the library of elements
			//TODO add a method to decrease the number of Layers shown
			libraryPicker = new CKXMLAssetPicker<CKGraphicsLayer>(afactory.getAllGraphicsLayers());
			libraryPicker.addSelectedListener(new AddLayer());
			
						
			description=new JTextField(15);
			description.getDocument().addDocumentListener(this);
			layoutPanel();
			
		}
		
		
		private void layoutPanel()
		{
			removeAll();
			MultiSplitPane pane = new MultiSplitPane();
			
			String layoutDef = 
					"(COLUMN description (ROW lp gp))";

			MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
			pane.getMultiSplitLayout().setModel(modelRoot);
			
			
			
			JPanel d = new JPanel(new FlowLayout());
			d.add(new JLabel("Description"));
			d.add(description);
			description.setText(((CKGraphicsScene)scene).getDescription()); 
			pane.add(d,"description");
			
			
			
			
			JPanel lp = new JPanel(new BorderLayout());
			lp.add(new JLabel("Add Layers"),BorderLayout.NORTH);
			lp.add(libraryPicker);
			pane.add(lp,"lp");
			
			JPanel rp = new JPanel(new BorderLayout());
			rp.add(new JLabel("Pick Grid"),BorderLayout.NORTH);
			rp.add(gridPicker);
			pane.add(rp,"gp");						
			
			
			add(pane);
	
			
			
/*			setLayout(new BorderLayout());
			
			add(description,BorderLayout.NORTH);
			
			
			
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(150);

			//Provide minimum sizes for the two components in the split pane
			Dimension minimumSize = new Dimension(200, 100);
			
			
			JPanel lp = new JPanel(new BorderLayout());
			lp.add(new JLabel("Add Layers"),BorderLayout.NORTH);
			lp.add(libraryPicker);
			splitPane.setRightComponent(lp);
			
			JPanel rp = new JPanel(new BorderLayout());
			rp.add(new JLabel("Pick Grid"),BorderLayout.NORTH);
			rp.add(gridPicker);
			splitPane.setLeftComponent(rp);						
			
			
			add(splitPane);
	*/		
			revalidate();
		}
		
		
		

		private class AddLayer implements CKEntitySelectedListener<CKGraphicsLayer>
		{

			@Override
			public void entitySelected(CKGraphicsLayer entity)
			{
				scene.addLayer(entity);
				stateChanged(null);		
			}

			
		
		}
		
		private class ReplaceGrid implements CKEntitySelectedListener<CKGrid>
		{

			@Override
			public void entitySelected(CKGrid entity)
			{
				((CKGraphicsScene)scene).setGrid(entity);
				stateChanged(null);
			}		
		
		}
	
		
		public static void main(String[] args)
		{
			JFrame frame = new JFrame();
			CKSceneFactory factory = CKSceneFactory.getInstance(); 
						
			CKGraphicsSceneInterface scene = factory.getAsset("Kitchen");
			scene.setAID("");
			CKScenePropertiesEditor ed =
				new CKScenePropertiesEditor(scene);
			frame.add(ed);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			
			
		}

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			stateChanged(null);
			
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			stateChanged(null);
			
		}

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			stateChanged(null);
			
		}

		

		@Override
		public void stateChanged(ChangeEvent e)
		{
			storeState();
			for(ChangeListener l: listeners)
			{
				l.stateChanged(e);
			}
			
		}

		
		Vector<ChangeListener> listeners = new Vector<ChangeListener>();
		
		@Override
		public void addChangeListener(ChangeListener l)
		{
			listeners.add(l);
			
		}

		@Override
		public CKGraphicsSceneInterface getAsset()
		{
			return scene;
		}

		@Override
		public void storeState()
		{
			((CKGraphicsScene) scene).setDescription(description.getText());
			
		}


	
		
		
		
		
}
