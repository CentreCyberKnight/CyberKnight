package ckGameEngine;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKSceneFactory;
import ckDatabase.CKQuestFactory;
import ckEditor.CKGUINodePropertiesEditor;
import ckEditor.CKGraphicsChangedListener;
import ckEditor.CKPositionSetter;
import ckEditor.CKPositionSetterListener;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.DataPickers.CKScenePicker;
import ckEditor.treegui.CKTeamList;
import ckEditor.treegui.ActorNode;
import ckEditor.treegui.BookList;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKTeamNode;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.CKGraphicsScene;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckPythonInterpreterTest.CKArtifactQuestRunner;
import ckTrigger.CKTriggerList;
import ckTrigger.CKTriggerNode;
import ckTrigger.CKTriggerListNode;


public class QuestData extends CKGUINode implements CKXMLAsset<QuestData>
{

	private static final long serialVersionUID = 686701630906485254L;

	
	private static final int TEAMS_INDEX=0;
	private static final int BOOK_INDEX=1;
	private static final int  SPELL_FILTER_INDEX=2;
	private static final int TRIGGER_INDEX=3;
	
	int tId;
	int startTime;
	
	//CKGrid grid;
	PriorityQueue<Event> eQueue;
	
	String name;
	String sceneID;
	
	String qid;
	
	


	/**
	 * @return the qid
	 */
	//TODO switch to AID
	public String getQid()
	{
		return qid;
	}
	volatile boolean inputOK;
	
	/**
	 * This constructor is only for the XMLEncoder-Do NOT call it
	 * 
	 */
	public QuestData()
	{
		//this(null);
		init("");
	}
	
	private void init(String id)
	{
		qid=id;
		tId=0;
		//this.grid=g;
		this.startTime=0;
		inputOK=false;
		sceneID="Kitchen";
		name="cool quest";
		setChildOrderLocked(true);
		setChildRemoveable(false);
		setPastableChildren(false);
	
	}
	
	/**
	 * parameter doesn't matter....
	 */
	public QuestData(int i)
	{
		init("");
		//addIT(new CKTeamNode());
		addIT(new CKTeamList());
		addIT(new BookList());
		addIT(new CKTriggerList("World Spell Filter"));
		addIT(new CKTriggerList("World Triggers"));

	}
	
	
	Vector<CKPositionSetter> pSetters= new Vector<CKPositionSetter>();
	
	public void addPositionSetter(CKPositionSetter l)
	{
		pSetters.add(l);
	}
	
	
	public void reqestPosition(CKPositionSetterListener l,CKAbstractGridItem item,boolean leave)
	{
		if(pSetters.size()>0)
		{
			for(CKPositionSetter s:pSetters)
			{
				s.setPosition(l, item, leave);
			}
		}
		else
		{
			System.err.println("No Position Setters are availible");
		}
	}
	
	Vector<CKGraphicsChangedListener> graphicsChangedListeners= new Vector<>();
	
	public void addGraphicsChangedListener(CKGraphicsChangedListener l)
	{
		graphicsChangedListeners.add(l);
	}
	
	public void notifyGraphicsChanged()
	{
		for(CKGraphicsChangedListener l: graphicsChangedListeners)
		{
			l.graphicsChanged();
		}
	}
	
	/**
	 * @return the sceneID
	 */
	public String getSceneID()
	{
		return sceneID;
	}

	/**
	 * @param sceneID the sceneID to set
	 */
	public void setSceneID(String sceneID)
	{
		this.sceneID = sceneID;
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
	 * @return the actors
	 */
	public CKTeamList getActors()
	{
		return (CKTeamList)children.get(TEAMS_INDEX);
	}

	public CKTriggerListNode getTriggerLoop()
	{
		return (CKTriggerListNode) children.get(TRIGGER_INDEX);
	}
	
	public BookList getBooks()
	{
		return (BookList) children.get(BOOK_INDEX);
	}
	

	public CKTriggerListNode getSpellFilter()
	{
		return (CKTriggerListNode) children.get(SPELL_FILTER_INDEX);
	}
	
	
	public void addBook(CKBook ckBook)
	{
		getBooks().add(ckBook);
	}

	public void addTrigger(CKTriggerNode t)
	{
		getTriggerLoop().add(t);
	}
	
	
	public void addActor(ActorNode act,String teamId)
	{
		getActors().addActor(act,teamId);
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Quest:" + name ;
	}
	
	/**
	 * @return the tId
	 */
	public int gettId() {
		return tId;
	}
	
	public CKGridActor getActor(String actor)
	{
		return getActors().getPC(actor);
	}
	
	public String getPCAssetId(String actor)
	{
		return getActors().getPCAssetId(actor);
	}

	
	public Vector<CKGridActor> getActorsFromTeam(String teamID)
	{
		return getActors().getActorsFromTeam(teamID);
	}
	
	

	
	public CKBook getBook(String b)
	{
		return getBooks().getBook(b);
	}
	/**
	 * @param tId the tId to set
	 * 	 */
	public void settId(int tId) {
		this.tId = tId;
	}

	
	
	/**
	 * @return the grid
	 */
	/*public CKGrid getGrid()
	{
		return 
	}

	/*
	 * now part of the scene, do not allow it to be set seperatly.... 
	 */
/*	public void setGrid(CKGrid grid) {
		this.grid = grid;
	}
*/
	/**
	 * @return the StartTime
	 */
	public int getStartTime() {
		return startTime;
	}

	/**
	 * @param StartTime the StartTime to set
	 */
	public void setStartTime(int StartTime) {
		this.startTime = StartTime;
	}

	
	public void startTransaction()
	{
		startTime=0;
		tId =  CKGameObjectsFacade.getEngine().startTransaction(false);
	}

	public void endTransaction()
	{
		CKGameObjectsFacade.getEngine().endTransaction(tId, false);
	}
	
	public void holdPose(int waitTime)
	{
		startTime +=waitTime;
	}
	
	
	public void setCopy()
	{
		qid="";
	}
	
	private String team="";
	
	public void setTeam(String string)
	{
		team = string;
	}
	
	public String getTeam()
	{
		if(team.length()>0)
			{ return team; }
		
		CKTeamNode node = (CKTeamNode) this.getActors().getChildAt(0);
		return node.getTeamId();
	}
	
	
	


	
	/* Node functions*/
	
	

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(childIndex < 4)
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
		QuestData node= (QuestData) super.clone();
		//node.setGrid(new CKGrid(10,10));
		for(Object o: children)
		{
		//	Satisfies s = (Satisfies) o;
			node.add( (MutableTreeNode) ((CKGUINode) o ).clone());
		}
		// qid should be set in clone  --  ! node.qid=-1;
/*		node.actors = (ActorList) node.children.get(0);
		node.books = (BookList) node.children.get(1);
		node.triggerLoop = (TriggerList) node.children.get(2);
	*/	
		return node;
	}
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#GUIEdit()
	 */
	/*
	@Override
	public JMenuItem GUIEdit()
	{
		
		JMenu menu = new JMenu("Edit Quest");
		
		
		JMenu assetmenu = new JMenu("Pick Asset");
		CKGraphicsSceneInterface s =  CKSceneFactory.getInstance().getAsset(sceneID);
		BufferedImage img = CKGraphicsPreviewGenerator.createScenePreview(s, 200,200);
		JComponent panel = new JPanel();
		panel.add(new JLabel(new ImageIcon(img)));
		panel.addMouseListener(new SceneViewerPopupListener());
		assetmenu.add(panel);
		menu.add(assetmenu);
		
		
		//name
		JPanel p = new JPanel();
		p.add(new JLabel("Quest"));
		JTextField text = new JTextField(name,20); 
		p.add(text);
		menu.add(p);
		
				
		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(text) );
		menu.add(store);
		

		
		return menu;
	}
	
	
	class SceneViewerPopupListener extends MouseAdapter
	{

		public void mouseClicked(MouseEvent e)
		{
			JFrame frame = new JFrame();
			CKSceneFactory factory =CKSceneFactory.getInstance(); 
			
			CKScenePicker picker = new CKScenePicker(factory.getAllAssets());
			picker.addSelectedListener(new SceneListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}	
	}
	*/
	class ScenePickerListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JFrame frame = new JFrame();
			CKSceneFactory factory =CKSceneFactory.getInstance(); 
			
			CKScenePicker picker = new CKScenePicker(factory.getAllAssets());
			picker.addSelectedListener(new SceneListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	
	class SceneListener implements CKEntitySelectedListener<CKGraphicsSceneInterface>
	{
		JFrame frame;
		public SceneListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(CKGraphicsSceneInterface a)
		{
			sceneID=((CKGraphicsScene) a).getAID();
			notifyGraphicsChanged();
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}
	
	
	class EditAction  implements ActionListener
	{
		JTextField text2;
		
		
		public EditAction(JTextField text2)
		{
			this.text2 = text2;

	
		}

		public void actionPerformed(ActionEvent evt) 
		{
			name = text2.getText();
		
		}
	}


	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#GUIAddNode(ckGraphics.treegui.CKTreeGui)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		JMenu menu = new JMenu("Quest Commands");
		JMenuItem run = new JMenuItem("Run Quest");
		run.addActionListener(new RunAction( (QuestData) this.clone()));
		menu.add(run);
		
		JMenuItem save = new JMenuItem("Save Quest to DB");
		save.addActionListener(new SaveQuest());
		menu.add(save);
		
		JMenuItem saveCopy = new JMenuItem("Save New Quest to DB");
		saveCopy.addActionListener(new SaveCopyQuest());
		menu.add(saveCopy);

		JMenuItem saveF = new JMenuItem("Save Quest to File");
		saveF.addActionListener(new SaveFileQuest());
		menu.add(saveF);

		return menu; 
	}	
	

	class SaveQuest  implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			CKQuestFactory.getInstance().writeAssetToXMLDirectory(QuestData.this);
		}
	
	}

	class SaveCopyQuest  implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			QuestData.this.setQid("");
			CKQuestFactory.getInstance().writeAssetToXMLDirectory(QuestData.this);
		}
	
	}
	
	 
	
	class SaveFileQuest  implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fc =new JFileChooser(); 
			int returnVal = fc.showSaveDialog(null);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            
			            try
						{
							QuestData.this.writeToStream(new FileOutputStream(file));
						} catch (FileNotFoundException e1)
						{
							System.err.println("Could not file File"+file);
						}
			        } 
			   }


	}

	
	class RunAction  implements ActionListener
	{
		QuestData q;
		
		
		public RunAction(QuestData q)
		{
			this.q=q;

	
		}

		public void actionPerformed(ActionEvent evt) 
		{
//			new CKQuestRunner2(q);
			new CKArtifactQuestRunner(new Quest(q));
			
		}
	}
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		CKSceneFactory factory = CKSceneFactory.getInstance(); 
		return new ImageIcon(CKGraphicsPreviewGenerator.createScenePreview(
				factory.getAsset(sceneID),100,100));
				
	}
	
	
	static JPanel []panel;
	static JTextField[] nameText;
	//static JComboBox[] resultBox;
	static JButton[]  ScenePicker;
	static ScenePickerListener SPListener;

	
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.LINE_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.LINE_AXIS));

			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
			
			
			nameText = new JTextField[2];
			nameText[0] = new JTextField();
			nameText[1] = new JTextField();
			
			panel[0].add(new JLabel("Quest:   "));
			panel[0].add(nameText[0]);		
			
			panel[1].add(new JLabel("Quest:   "));
			panel[1].add(nameText[1]);	
			ScenePicker = new JButton[2];
			ScenePicker[0] = new JButton("Pick Scene");
			ScenePicker[1] = new JButton("Pick Scene");
			panel[1].add(ScenePicker[1]);
			panel[0].add(ScenePicker[0]);
		}
		
	}
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		
		nameText[index].setText(name);
		nameText[index].setColumns(15);
		if(index == EDIT){
		ScenePicker[EDIT].removeActionListener(SPListener);
		SPListener = new ScenePickerListener();
		ScenePicker[EDIT].addActionListener(SPListener);
		}
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
		name = (String)nameText[EDIT].getText();
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

	public void setQid(String id)
	{
		qid=id;
	}

	/**
	* Stores this object to an OutputString
	 * @throws IOException
	 */
	/*
	public void writeToStream(OutputStream out)
	{
			
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.writeObject(this);
		e.close();
		
	}
	*/

	public static QuestData readFromStream(InputStream in)
	{
		XMLDecoder d = new XMLDecoder(in);
		QuestData node = (QuestData) d.readObject();
		d.close();
		return node;
		
	}

	@Override
	public String getAID()
	{
		return qid;
	}

	@Override
	public void setAID(String a)
	{
		qid=a;
		
	}

	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}

	@Override
	public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
	{
		//all are same for now
		
		CKSceneFactory factory = CKSceneFactory.getInstance();
		BufferedImage img = CKGraphicsPreviewGenerator.createScenePreview(factory.getAsset(getSceneID())  , 200,200);
		JComponent panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(new ImageIcon(img)));
		panel.add(new JLabel(getName()+"   "+getAID()),BorderLayout.SOUTH);
		return panel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<QuestData> getXMLPropertiesEditor()
	{
		return new CKGUINodePropertiesEditor<QuestData>(this);
	}

	
	private Random randGenerator = null;
	
	public double getRandom()
	{
		if(randGenerator==null)
		{
			randGenerator = new Random();
		}
		return randGenerator.nextDouble();
	}

	
	
	
	
	

}

