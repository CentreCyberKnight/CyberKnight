package ckGameEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKQuestFactory;
import ckEditor.CKGUINodePropertiesEditor;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTreeGui;

public class CampaignNode extends CKGUINode implements CKXMLAsset<CampaignNode>
{
	
	private static final long serialVersionUID = 2838819054698145385L;
	String AID = "";
	String questID = ""; 
	String name = "";
	String campaign = "";
	String winName = "";
	int winValue = 0;
	
	public CampaignNode(String aid,String questID,String name,String campaign,
			String winName,
				CKBook preReqs,CKBook postAdds)
	{
		
		AID = aid;
		this.questID = questID;
		this.name=name;
		this.campaign = campaign;
		this.winName=winName;
		add(preReqs);
		add(postAdds);
		
		
		//this.setChildOrderLocked(true);
		//this.setChildRemoveable(false);
	}
	
	
	public CampaignNode()
	{
		this("","","","","",new CKBook("PreRequisits"),new CKBook("PostAdditions"));
	}
	
	

	public CKBook getPrereqs()
	{
		return ((CKBook) this.getChildAt(0));
	}
	
	public void setPrereqs(CKBook preq)
	{
		insert(preq,0);
	}
	

	public CKBook getPostAdds()
	{
		return ((CKBook) this.getChildAt(1));
	}
	
	public void setPostAdds(CKBook post)
	{
		insert(post,0);
	}
	
	
	
	
	
	
	/**
	 * @return the campaign
	 */
	public String getCampaign()
	{
		return campaign;
	}


	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(String campaign)
	{
		this.campaign = campaign;
	}


	@Override
	public String getAID()
	{
		return AID;
	}

	@Override
	public void setAID(String aid)
	{
		AID = aid;
		
	}

	
	
	/**
	 * @return the questID
	 */
	public String getQuestID()
	{
		return questID;
	}


	/**
	 * @param questID the questID to set
	 */
	public void setQuestID(String questID)
	{
		this.questID = questID;
	}

	
	

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the winName
	 */
	public String getWinName()
	{
		return winName;
	}


	/**
	 * @param winName the winName to set
	 */
	public void setWinName(String winName)
	{
		this.winName = winName;
	}


	

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		CampaignNode cm = new CampaignNode(getAID(),getQuestID(),getName(),
				getCampaign(),getWinName(),
				(CKBook) getPrereqs().clone(),
				(CKBook) getPostAdds().clone());
		return cm;
	}

	

	 public static void main(String[] args)
	 {
			JFrame frame = new JFrame("CyberKnight CampaignNode Editor");
			CKGuiRoot root = new CKGuiRoot();
		
			CampaignNode cm = new CampaignNode("bob","rob","sob","dob","level1",
					new CKBook(),new CKBook());
			root.add(cm );
			
			frame.add(new CKTreeGui(root));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(600,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//cm.writeToStream(System.out);
		 
	 }

	static JPanel []panel;
	static JTextField[] nameText;
	static JLabel [] questLabel;
	static JButton [] questButton;
	
	static JTextField[] levelWinText;
	
	static QuestSelectedListener questListener;
	
	class QuestSelectedListener implements ActionListener
	{

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			
			CKXMLFilteredAssetPicker<QuestData,CKQuestFactory> picker = 
					new CKXMLFilteredAssetPicker<QuestData,CKQuestFactory>(CKQuestFactory.getInstance());
			picker.addSelectedListener(new AssetListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}
	
		
	}
	
	
	class AssetListener implements CKEntitySelectedListener<QuestData>
	{
		JFrame frame;
		public AssetListener(JFrame f) {frame=f;}
		
		
		@Override
		public void entitySelected(QuestData entity)
		{
			setQuestID(entity.getAID());
			setPanelValues(EDIT);
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
			
		}

	}
	
	
	
	static private void initPanel(boolean force,Object caller)
	{
		if(panel==null || force)
		{
			
			panel=new JPanel[2];
			
			JPanel [] descPanel = new JPanel[2];
			JPanel [] qPanel = new JPanel[2];
			
			
			descPanel[0]=new JPanel();
			descPanel[0].setLayout(new BoxLayout(descPanel[0],BoxLayout.LINE_AXIS));
			descPanel[1]=new JPanel();			
			descPanel[1].setLayout(new BoxLayout(descPanel[1],BoxLayout.LINE_AXIS));

			nameText = new JTextField[2];
			nameText[0] = new JTextField();
			nameText[1] = new JTextField();
			
			descPanel[0].add(new JLabel("Description   "));
			descPanel[0].add(nameText[0]);		
			
			descPanel[1].add(new JLabel("Description   "));
			descPanel[1].add(nameText[1]);		


			qPanel[0]=new JPanel();
			qPanel[0].setLayout(new BoxLayout(qPanel[0],BoxLayout.LINE_AXIS));
			qPanel[1]=new JPanel();			
			qPanel[1].setLayout(new BoxLayout(qPanel[1],BoxLayout.LINE_AXIS));
			
			
			questLabel = new JLabel[2];
			questLabel[0] = new JLabel();
			questLabel[1] = new JLabel();
			
			qPanel[0].add(questLabel[0]);
			qPanel[1].add(questLabel[1]);
			
			questButton = new JButton[2];
			questButton[0] = new JButton("Pick Quest");
			questButton[1] = new JButton("Pick Quest");
			
			
			qPanel[0].add(questButton[0]);
			qPanel[1].add(questButton[1]);
			
			
			
			JPanel []winPanel = new JPanel[2];
			winPanel[0] = new JPanel(new FlowLayout());
			winPanel[1] = new JPanel(new FlowLayout());
			
			winPanel[0].add(new JLabel("Win Label"));
			winPanel[1].add(new JLabel("Win Label"));
			
			
			levelWinText = new JTextField[2];
			levelWinText[0] = new JTextField();
			levelWinText[0].setColumns(20);
			levelWinText[1] = new JTextField();
			levelWinText[1].setColumns(20);
			
			winPanel[0].add(levelWinText[0]);
			winPanel[1].add(levelWinText[1]);
			

			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.Y_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.Y_AXIS));
			
			
			panel[0].add(descPanel[0]);
			panel[0].add(qPanel[0]);
			panel[0].add(winPanel[0]);
			
			panel[1].add(descPanel[1]);
			panel[1].add(qPanel[1]);
			panel[1].add(winPanel[1]);

			
			
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true,this); }
		panel[index].setBackground(colors[index]);
		
		nameText[index].setText(getName());
		nameText[index].setColumns(15);
		questLabel[index].setText("Quest:  "+getQuestID());
		levelWinText[index].setText(winName);
		
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		questButton[EDIT].removeActionListener(questListener);
		
		
		setPanelValues(EDIT);
		
		questListener = new QuestSelectedListener();
		questButton[EDIT].addActionListener(questListener);
		
		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		setName( (String)nameText[EDIT].getText() );
		setWinName( levelWinText[EDIT].getText());
		
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CampaignNode:" + name;
	}


	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}

	@Override
	public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
	{
		return new CKTreeGui(this,false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CampaignNode> getXMLPropertiesEditor()
	{
		return new CKGUINodePropertiesEditor<CampaignNode>(this);
	}

}
