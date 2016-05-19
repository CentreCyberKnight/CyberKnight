package ckEditor.treegui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKScriptTools;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKQuestFactory;
import ckEditor.CKGUINodePropertiesEditor;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.QuestData;
import ckGraphicsEngine.FX2dGraphicsEngine;



public class CKQuestTest extends CKGUINode implements CKXMLAsset<CKQuestTest>//, junit.framework.Test
{

	
	
	

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6884734139951670326L;
	
	private String name="";
	private String questID="asset69633757502583801";
	private int randomSeed=10;
	
	
	private CKHiddenNode hidden=new CKHiddenNode();
	
	
	
	
	public CKQuestTest()
	{
		setParent(hidden);
		setQuestID(questID);
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
		hidden.setParent(CKQuestFactory.getInstance().getAsset(questID));
		
	}









	/**
	 * @return the randomSeed
	 */
	public int getRandomSeed()
	{
		return randomSeed;
	}









	/**
	 * @param randomSeed the randomSeed to set
	 */
	public void setRandomSeed(int randomSeed)
	{
		this.randomSeed = randomSeed;
	}


	public void runBackgroundTest()
	{
		
		QuestData q = initTest();
		CKScriptTools.checkQuest(q);		
		
		
	}
	
	public void runVewingTest()
	{
		QuestData q = initTest();
		CKScriptTools.runQuestInThread(q);		
	}
	
 

	public void runDropDownTest(String runType)
	{
		QuestData q = initTest();
		
		if(runType.compareTo("BACKGROUND")==0)
		{
			CKScriptTools.checkQuest(q);		
		}
		
		else if (runType.compareTo("WATCH")==0)
		{
			CKScriptTools.runQuest(q);
		}

	}
	
	public QuestData initTest()
	{
		//run all of the children
		QuestData q = CKQuestFactory.getInstance().getAsset(questID,true);
		System.out.println("quest data value"+Integer.toHexString(System.identityHashCode(q)));
		CKGameObjectsFacade.killEngine();
		FX2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
		//FIXME q.setRandomSeed(randomSeed);

		
		if(getChildCount()>0)
		{
			ArrayList<ScriptNode> vec = new ArrayList<ScriptNode>(getChildCount());
			for (int i=0;i<getChildCount();i++)
			{	
				vec.add((ScriptNode) getChildAt(i) );
			}
			Collections.sort(vec);
			for(ScriptNode script:vec)
			{
				script.initScript(q, engine);
			}		
		}
		return q;
		
	}
	
	
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof ScriptNode)
		{
			super.insert(newChild, childIndex);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		DefaultMutableTreeNode node= (DefaultMutableTreeNode) super.clone();
		if(children != null)
		{
			for(Object o: children)
			{
				//	Satisfies s = (Satisfies) o;
				node.add( (MutableTreeNode) ((ScriptNode) o ).clone());
			}
		}
		return node;
	}

	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Quest Test "+name;
	}
	
	
	/* (non-Javadoc)
	 * @see ckEditor.treegui.CKGUINode#GUIEdit()
	 */
	@Override
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Run Test");
		
		JMenuItem check = new JMenuItem("Check Test");
		check.addActionListener(new RunTestListener("BACKGROUND"));
		menu.add(check);

		JMenuItem watch = new JMenuItem("Watch Test");
		watch.addActionListener(new RunTestListener("WATCH"));
		menu.add(watch);

		menu.addSeparator();
		
		JMenuItem record = new JMenuItem("Record Test");
		record.addActionListener(new RecordTestListener());
		menu.add(record);
		
		return menu;
	}

	public class RunTestListener implements ActionListener
	{

		String type;
		
		public RunTestListener(String type)
		{
			this.type = type;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			//runType = type;
			//JUnitCore junit = new JUnitCore();
			//junit.run(CKQuestTest.this);
			runDropDownTest(type);
		}

	}

	public class RecordTestListener implements ActionListener
	{

		
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			QuestData q = CKQuestFactory.getInstance().getAsset(questID);
			FX2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
			//FIXME q.setRandomSeed(randomSeed);
			
			
			if(getChildCount()>0)
			{
				ArrayList<ScriptNode> vec = new ArrayList<ScriptNode>(getChildCount());
				for (int i=0;i<getChildCount();i++)
				{	
					vec.add((ScriptNode) getChildAt(i) );
				}
				Collections.sort(vec);
				for(ScriptNode script:vec)
				{
					script.createScript(q, engine);
				}		
			}
/*			//FIXME should run by priority 
			for (int i=0;i<getChildCount();i++)
			{
				ScriptNode script = (ScriptNode) getChildAt(i);
				script.createScript(q, engine);
			}
	*/	//now to run the quest..
			CKScriptTools.runQuest(q);
			
		
		}

	}

	
	

	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		JMenu menu = new JMenu("Add Script");
	
		JMenuItem addActor = new JMenuItem("Add Actor Script");
		addActor.addActionListener(new AddScriptListener(tree,"ACTOR"));
		menu.add(addActor);
		
		JMenuItem addAim = new JMenuItem("Add Aim Script");
		addAim.addActionListener(new AddScriptListener(tree,"AIM"));
		menu.add(addAim);
		
		
		JMenuItem addDialog = new JMenuItem("Add Dialog Script");
		addDialog.addActionListener(new AddScriptListener(tree,"DIALOG"));
		menu.add(addDialog);
		

		
		return menu;
		}
	
	
class AddScriptListener implements ActionListener
{
	
	    CKTreeGui tree;
	    String type;
		
		public AddScriptListener(CKTreeGui t,String type)
		{
			tree=t;
			this.type=type;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)  		{ return; }

			if(type.compareTo("ACTOR")==0)
			{
				tree.addNode(node,new ActorScriptNode());
			}
			else if (type.compareTo("DIALOG")==0)
			{
				tree.addNode(node,new DialogScriptNode());
			}
			else if (type.compareTo("AIM")==0)
			{
				tree.addNode(node,new AimScriptNode());
			}
		}
			
	
}
	
	
	
	
	
	
	
	
	
	
	 JPanel []panel;
	JTextField[] testName;
	//static JComboBox<QuestData> []questBox;
	 JTextField questNames[];
	 SpinnerNumberModel[] seedSpinner;
	
	
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			
			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
			
			
			JPanel [] top = new JPanel[2];
			top[0] = new JPanel();
			top[1] = new JPanel();
			
			testName = new JTextField[2];
			testName[0] = new JTextField(15);
			testName[1] = new JTextField(15);
			top[0].add(testName[0]);
			top[1].add(testName[1]);
			/*
			questBox = new JComboBox[2];
			questBox[0] = new JComboBox<QuestData>();
			questBox[1] = new JComboBox<QuestData>();
			top[0].add(questBox[0]);
			top[1].add(questBox[1]);
*/
			questNames = new JTextField[2];
			questNames[0] = new JTextField(15);
			questNames[1] = new JTextField(15);
			top[0].add(questNames[0]);
			top[1].add(questNames[1]);
			JButton top0 = new JButton("Pick Quest");
			JButton top1 = new JButton("Pick Quest");
			top0.addActionListener(new QuestViewerPopupListener());
			top[0].add(top0);
			top[1].add(top1);
			
			
			panel[0].add(top[0]);
			panel[1].add(top[1]);
			
			
			JPanel[] bottom = new JPanel[2];
			bottom[0] = new JPanel();
			bottom[1] = new JPanel();
			
			
			bottom[0].add(new JLabel("Random Seed"));
			bottom[1].add(new JLabel("Random Seed"));
			
			
			seedSpinner=new SpinnerNumberModel[2]; 
			seedSpinner[0] = new SpinnerNumberModel(1, 0, 10000,1);
			seedSpinner[1] = new SpinnerNumberModel(1, 0, 10000,1);
			JSpinner spin = new JSpinner(seedSpinner[0]);
			bottom[0].add(spin);
			spin = new JSpinner(seedSpinner[1]);
			bottom[1].add(spin);
			
			
			panel[0].add(bottom[0]);
			panel[1].add(bottom[1]);
		}
		
	}

class QuestViewerPopupListener implements ActionListener
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
	public void entitySelected(QuestData a)
	{
		setQuestID(a.getAID());
		frame.setVisible(false); //you can't see me!
		frame.dispose(); //Destroy the JFrame object
	}

}




	private void setPanelValues(int index)
	{
		if(panel==null) { initPanel(true); }
		
		testName[index].setText(name);
		questNames[index].setText(CKQuestFactory.getInstance().getAsset(questID).getName());
		//this.initializeJComboBoxByID(questBox[index], CKQuestFactory.getInstance().getAllAssets(),questID);
		seedSpinner[index].setValue(randomSeed);		
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		//setQuestID(((QuestData)questBox[EDIT].getSelectedItem()).getAID());
		randomSeed = (Integer) seedSpinner[EDIT].getNumber();
		name = testName[EDIT].getText();
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
		
	String AID = "";

	@Override
	public String getAID()
	{
		return AID;
	}


	@Override
	public void setAID(String a)
	{
		AID=a;
	}


	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}


	@Override
	public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
	{
		return new CKTreeGui(this,false) ;
	}


	@SuppressWarnings("unchecked")
	@Override
	public  CKXMLAssetPropertiesEditor<CKQuestTest> getXMLPropertiesEditor()
	{	
		return new CKGUINodePropertiesEditor<CKQuestTest>(this);
		
	}






	
	
	
	
	
	
	
	
	
}
