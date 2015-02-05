package ckEditor.treegui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.DataPickers.CKFilteredAssetPicker;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKGraphicsAssetPickerNode extends CKGUINode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1703639546241072687L;
	String AID = "null";
	String description;
	
	
	
	
	public CKGraphicsAssetPickerNode()
	{
		this("Graphic Asset Picker");
	}
	public CKGraphicsAssetPickerNode(String description)
	{
		this.description = description;
		this.setAllowsChildren(false);
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
	}


	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return the aID
	 */
	public String getAID()
	{
		return AID;
	}


	/**
	 * @param aID the aID to set
	 */
	public void setAID(String aID)
	{
		AID = aID;
	}
	
	
	static private JPanel[] panel;
	static JLabel[] descLabel;
	static JTextField[] nameFields;
	static JButton[] pickAsset;
	

	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.Y_AXIS));
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.Y_AXIS));
			
			JPanel [] top = new JPanel[2];
			top[0]=new JPanel();
			top[1]=new JPanel();

			//description
			descLabel = new JLabel[2];
			descLabel[0]=new JLabel();
			descLabel[1]=new JLabel();
			
			
			top[0].add(descLabel[0]);
			top[1].add(descLabel[1]);
			
			
			
			
			JPanel [] bottom = new JPanel[2];
			bottom[0]=new JPanel();
			bottom[1]=new JPanel();
			
			
			//name
			nameFields = new JTextField[2];
			nameFields[0]=new JTextField(15);
			nameFields[1]=new JTextField(15);
			bottom[0].add(nameFields[0]);
			bottom[1].add(nameFields[1]);
			
			pickAsset = new JButton[2];
			pickAsset[0] = new JButton("Pick Asset");
			pickAsset[1] = new JButton("Pick Asset");
			bottom[0].add(pickAsset[0]);
			bottom[1].add(pickAsset[1]);
			
			panel[0].add(top[0]);
			panel[0].add(bottom[0]);
			
			panel[1].add(top[1]);
			panel[1].add(bottom[1]);
			
			
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.ORANGE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);
		descLabel[index].setText(description);
		
		CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(AID);
		
		nameFields[index].setText(asset.getDescription());
	}
	
	class AssetViewerPopupListener implements ActionListener
	{

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			
			CKFilteredAssetPicker picker = 
					new CKFilteredAssetPicker();
			picker.addSelectedListener(new AssetListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}
		
	}
	
	
	
	class AssetListener implements CKEntitySelectedListener<CKGraphicsAsset>
	{
		JFrame frame;
		public AssetListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(CKGraphicsAsset a)
		{
			setAID(a.getAID());
			nameFields[EDIT].setText(a.getDescription());
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}
	
	static private AssetViewerPopupListener assetListener;
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		
		//first get rid of old listeners
		pickAsset[EDIT].removeActionListener(assetListener);
		
		
		//change values
		setPanelValues(EDIT);
		
		//set new listeners
		assetListener = new AssetViewerPopupListener();
		
		pickAsset[EDIT].addActionListener(assetListener);
		
		
		
		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		
		//nothing to store here...
		
		
			
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance(); 
		return new ImageIcon(CKGraphicsPreviewGenerator.createAssetPreview(
				factory.getGraphicsAsset(AID),0,0,64,128) );
	}
	
	
	public static void main(String[] args)
	 {
			JFrame frame = new JFrame("CyberKnight Actor Editor");
			CKGuiRoot root = new CKGuiRoot();
			
			CKGraphicsAssetPickerNode asset = new CKGraphicsAssetPickerNode();
			root.add(asset);
			frame.add(new CKTreeGui(root));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(600,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		 
			//heroAct.writeToStream(System.out);
			//tl.writeToStream(System.out);
		 
		 
		 
	 }
	
	
	
	
}
