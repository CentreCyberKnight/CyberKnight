/*package ckGameEngine;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.PriorityQueue;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKGridPosition;
import ckDatabase.CKConnection;
import ckDatabase.CKDialogMessageFactoryExample;
import ckDatabase.CKGraphicsSceneFactoryDB;
import ckDatabase.CKQuestFactory;
import ckGameEngine.actions.CKConcurrentAction;
import ckGameEngine.actions.CKGUIAction;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKGameActionListener;
import ckGameEngine.actions.CKPCFocusCameraCmd;
import ckGameEngine.actions.CKPCRelativeMoveCmd;
import ckGameEngine.actions.CKPCTurnCmd;
import ckGameEngine.actions.CKQuestAction;
import ckGameEngine.actions.CKSequentialAction;
import ckGameEngine.actions.CKWaitCmd;
import ckGraphics.src.CKScenePicker;
import ckGraphics.treegui.ActorList;
import ckGraphics.treegui.ActorNode;
import ckGraphics.treegui.CKGUINode;
import ckGraphics.treegui.CKTreeGui;

import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.CKGraphicsScene;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.LoadAssetError;
import ckPythonInterpreterTest.CKQuestRunner2;
import ckSatisfies.DeadSatisfies;
import ckSatisfies.NotSatisfies;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.TrueSatisfies;
import ckTrigger.Trigger;
import ckTrigger.TriggerList;
import ckTrigger.TriggerResult;


public class Quest extends CKGUINode implements CKGameActionListener 
{

	private static final long serialVersionUID = 686701630906485254L;
	int tId;
	int startTime;
	
	Grid grid;
	PriorityQueue<Event> eQueue;

	TriggerList triggerLoop=new TriggerList();
	ActorList actors = new ActorList();
	String name;
	int sceneID;
	
	int qid;
	
	


	volatile boolean inputOK;
	public Quest()
	{
		this(null);
	}
	
	private void init(Grid g)
	{
		qid=-1;
		tId=0;
		this.grid=g;
		this.startTime=0;
		inputOK=false;
		sceneID=1;
		name="cool quest";
		setChildOrderLocked(true);
		setChildRemoveable(false);
	
	}
	
	public Quest(Grid grid)
	{
		init(grid);
		add(actors);
		add(triggerLoop);
	}
	
	public Quest(int id,Connection conn)
	{
		init(new Grid(10,10));
		qid = id;
		String q = "SELECT * FROM "+CKQuestFactory.DBQT+
		" WHERE quest_id="+id+";";
		
		
			Statement stmt;
			try
			{
				stmt = conn.createStatement();
		
			ResultSet set = stmt.executeQuery(q);

			while(set.next())
			{	
				qid = id;
				sceneID = set.getInt("scene_id");
				name = set.getString("quest_description");
			
				//actors
				Clob clob = set.getClob("quest_actors");				
				XMLDecoder d = new XMLDecoder(clob.getAsciiStream());
				actors = (ActorList) d.readObject();
				d.close();
				add(actors);
				//get triggers
				clob = set.getClob("quest_triggers");				
				d = new XMLDecoder(clob.getAsciiStream());
				triggerLoop = (TriggerList) d.readObject();
				d.close();
				add(triggerLoop);
				return;

			}
		
			} catch (SQLException e)
			{
				e.printStackTrace();
				//just in case...
				add(triggerLoop);
				add(actors);
			
			}		
	}
	
	*//**
	 * @return the sceneID
	 *//*
	public int getSceneID()
	{
		return sceneID;
	}

	*//**
	 * @param sceneID the sceneID to set
	 *//*
	public void setSceneID(int sceneID)
	{
		this.sceneID = sceneID;
	}

	
	public void storeToDB(Statement stmt) throws SQLException
	{
		Connection conn = stmt.getConnection();
		PreparedStatement prep;
	
		
		if(qid == -1)
		{
			
				prep = conn.prepareStatement(
						"INSERT INTO "+CKQuestFactory.DBQT+ 
						"(quest_description,scene_id, quest_triggers, quest_actors) "+
						" VALUES (?,?,?,?);",
						Statement.RETURN_GENERATED_KEYS
						);
		
		}
		else
		{
			prep = conn.prepareStatement(
					"UPDATE "+CKQuestFactory.DBQT+ 
					" SET quest_description=?, scene_id=?,"+
							"quest_triggers=?,quest_actors=? "+
					"WHERE quest_id = ?");
			prep.setInt(5,qid);
		}
		prep.setString(1,name);
		prep.setInt(2,sceneID );
	
		File tempFile;
		try
		{
			//triggers
			tempFile = File.createTempFile("xmlencode","tmp");
			tempFile.deleteOnExit();

			XMLEncoder e = new XMLEncoder(
					new BufferedOutputStream(new FileOutputStream(tempFile)));
			e.writeObject(triggerLoop);
			e.close();
			
			e = new XMLEncoder(
					new BufferedOutputStream(System.out));
			e.writeObject(triggerLoop);
			e.close();
			prep.setCharacterStream(3,new InputStreamReader(new FileInputStream(tempFile)  )  );
			//actors
			tempFile = File.createTempFile("xmlencode","tmp");
			tempFile.deleteOnExit();

			e = new XMLEncoder(
					new BufferedOutputStream(new FileOutputStream(tempFile)));
			e.writeObject(actors);
			e.close();

			prep.setCharacterStream(4,new InputStreamReader(new FileInputStream(tempFile)  )  );

		
		} catch (IOException e1)
		{
			e1.printStackTrace();
			throw new SQLException("unable to create temporary file");
		}
		
		
		prep.execute();
		if(qid == -1)
		{
			//now get the generated key
			ResultSet keys = prep.getGeneratedKeys();
			if(keys.next())
			{
				qid = keys.getInt(1);
				//System.out.println("My new id is:"+getId());
			}
			else
			{
				throw new SQLException("unable to create new asset");
			}
		}
		}
	
	
	
	*//**
	 * @return the name
	 *//*
	public String getName()
	{
		return name;
	}


	*//**
	 * @param name the name to set
	 *//*
	public void setName(String name)
	{
		this.name = name;
	}


	*//**
	 * @return the actors
	 *//*
	public ActorList getActors()
	{
		return actors;
	}


	 (non-Javadoc)
	 * @see java.lang.Object#toString()
	 
	@Override
	public String toString()
	{
		return "Quest:" + name ;
	}
	
	*//**
	 * @return the tId
	 *//*
	public int gettId() {
		return tId;
	}
	
	public PC getActor(String actor)
	{
		return actors.getPC(actor);
	}

public void addActor(String actor, PC pc)
{
	actorMap.put(actor,pc);
}

	*//**
	 * @param tId the tId to set
	 *//*
	public void settId(int tId) {
		this.tId = tId;
	}

	*//**
	 * @return the grid
	 *//*
	public Grid getGrid() {
		return grid;
	}

	*//**
	 * @param grid the grid to set
	 *//*
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	*//**
	 * @return the StartTime
	 *//*
	public int getStartTime() {
		return startTime;
	}

	*//**
	 * @param StartTime the StartTime to set
	 *//*
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
	
	public synchronized void waitForInput()
	{
		try {
			System.out.println("The Quest waits! ");
			inputOK=true;
			wait();
			inputOK=false;
			System.out.println("Quest stopped waiting here");
			
			
		} catch (InterruptedException e) {
			//THIS SHOULD BE HOW THE PROGRAM WAKES UP SO IT IS GOOD THIS SHOULD HAPPEN
			//e.printStackTrace();
		}
			
	}
	
	*//**
	 * @return the inputOK
	 *//*
	public boolean isInputOK()
	{
		return inputOK;
	}


	public synchronized void notifyOfInput()
	{
		notify();
	}
	
	public synchronized void doAction(CKGameAction act)
	{
		act.doAction(this);
		waitForInput();
	}
	
	public void doEvent(Event currentEvent)
	{
		currentEvent.doEvent();
		Iterator<Event> e = currentEvent.getNewEvents();
		while(e.hasNext()) 	{ eQueue.add(e.next()); }
	}
	
	
	*//**
	 * Loops through the trigger list and checks and performs each trigger
	 * @return  true if the quest is over, false otherwise
	 *//*
	protected boolean doTriggers(boolean init)
	{
		return triggerLoop.doTriggers(this, init);
	}
	

	public void enqueueActors()
	{
		eQueue = new PriorityQueue<Event>();
		startTransaction();
		actors.enqueueActors(eQueue);
		endTransaction();
		
	}
	
	public void setCopy()
	{
		qid=-1;
	}
	
	public void setupScene()
	{
		CKGameObjectsFacade.getEngine().setPreferredSize(new Dimension(600,600));
		CKGameObjectsFacade.getEngine().loadScene(sceneID);
	}
	
	public void registerQuest()
	{
		CKGameObjectsFacade.setQuest(this);
	}
	public void gameLoop()
	{
		registerQuest();
		setupScene();
		enqueueActors();
		
		if(doTriggers(true)) { return; }
		
		while(true) 
		{
			Event currentEvent = eQueue.poll();
			System.out.println(currentEvent);
			
			if(currentEvent instanceof EndEvent) { return; }
			
			doEvent(currentEvent);
			
			if( doTriggers(false)) { return; }	
		}
	}
		
	
	*//**
	 * @param args
	 * @throws LoadAssetError 
	 *//*
	public static void main(String[] args) throws LoadAssetError {
		
		Quest quest = CKGameObjectsFacade.getQuest();
		JFrame frame = new JFrame();
		quest.creation(2, frame);
		frame.add(CKGameObjectsFacade.getEngine());
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//put this in creation
		//		quest.startTransaction();
//		PC hero = quest.addHeroToQuest(quest.getGrid().getPositionFromList(5,1), 18);
//		PC enemy = quest.addNullControllerCharacterToQuest(quest.getGrid().getPositionFromList(4,8), 20);

		//		frame.addKeyListener(new KeyInputHandler(quest));
		//quest.endTransaction();
		quest.gameLoop();
	//	quest.endTransaction();
	}

	public void addActor(ActorNode act)
	{
		actors.add(act);
	}
	private CKTeam team;
	
	public void setTeam(CKTeam t)
	{
		team = t;
	}
	
	public CKTeam getTeam()
	{
		return team;
	}
	
	public void addTrigger(Trigger t)
	{
		triggerLoop.add(t);
	}
	
	override to create different levels
	public void creation(int scene, JFrame frame)
	{
		sceneID =scene;
		grid = new Grid(10,10);
		if(frame != null)
		{
			frame.addKeyListener(new KeyInputHandler(this));
		}
	//	CKGameObjectsFacade.getEngine().setPreferredSize(new Dimension(600,600));
	//	CKGameObjectsFacade.getEngine().loadScene(scene);
		
		ActorNode heroAct =	new ActorNode("HERO",25,Direction.SOUTHWEST, 
				getGrid().getPositionFromList(5,3),1,new CKCharacter(25,"HERO"));
		ActorNode momAct =	new ActorNode("mom",31,Direction.NORTHEAST, 
				getGrid().getPositionFromList(5,4),0,new CKCharacter(31,"mom"));
		actors.add(heroAct);
		actors.add(momAct);
		
		Satisfies winSatisfies = new PositionReachedSatisfies("HERO", grid.getPositionFromList(5, 4));
				
		CKDialogMessageFactoryExample dFactory = new CKDialogMessageFactoryExample();
		CKQuestAction forward = new CKPCRelativeMoveCmd("mom", 1);
		CKQuestAction right = new CKPCTurnCmd("mom", true, 15);
		
		//Start/Lose/Win actions
		triggerLoop.add(new Trigger(winSatisfies,
				new CKGUIAction(dFactory, 1300),TriggerResult.SATISFIED_END_QUEST));	
		triggerLoop.add(new Trigger(new DeadSatisfies("HERO"),
				new CKGUIAction(dFactory, 1350),TriggerResult.SATISFIED_END_QUEST));
		
		CKSequentialAction comfort = new CKSequentialAction();
		comfort.add(forward);
		comfort.add(new CKGUIAction(dFactory,1000));
		
		
		
		CKSequentialAction ready =  new CKSequentialAction();
		ready.add(new CKGUIAction(dFactory,1100));
		ready.add((CKGameAction)right.clone());
		ready.add((CKGameAction)right.clone());
		ready.add((CKGameAction)forward.clone());
		ready.add((CKGameAction)forward.clone());
		ready.add((CKGameAction)right.clone());
		ready.add((CKGameAction)right.clone());
		
		CKSequentialAction ready0 =  new CKSequentialAction();
		ready0.add(new CKGUIAction(dFactory,1100));
		ready0.add((CKGameAction)right.clone());
		ready0.add((CKGameAction)right.clone());
		ready0.add((CKGameAction)forward.clone());
		ready0.add((CKGameAction)forward.clone());
		ready0.add((CKGameAction)right.clone());
		ready0.add((CKGameAction)right.clone());

		CKSequentialAction doIt = new CKSequentialAction();
		doIt.add(new CKGUIAction(dFactory,1200)); //watch me
		
		//is not doing these concurrently...
		CKConcurrentAction moveAndSpeak = new CKConcurrentAction();
		moveAndSpeak.add(new CKGUIAction(dFactory,1250));
		
		CKSequentialAction act = new CKSequentialAction();
		act.add(new CKWaitCmd(45) );
		act.add((CKGameAction)forward.clone());
		
		moveAndSpeak.add(act);
		
		doIt.add(moveAndSpeak);
		
		CKSequentialAction doIt0 = new CKSequentialAction();
		doIt0.add(new CKGUIAction(dFactory,1200)); //watch me
		
		
		CKConcurrentAction moveAndSpeak0 = new CKConcurrentAction();
		moveAndSpeak0.add(new CKGUIAction(dFactory,1250));
		
		CKSequentialAction act0 = new CKSequentialAction();
		act0.add(new CKWaitCmd(45) );
		act0.add((CKGameAction)forward.clone());
		
		moveAndSpeak0.add(act0);
		
		doIt0.add(moveAndSpeak0);
		
		CKSequentialAction momAction = new CKSequentialAction();
		momAction.add(new CKPCFocusCameraCmd("mom"));
		
		momAction.add(comfort);
		momAction.add(ready);
		momAction.add(doIt);
		
		
		CKSequentialAction start = new CKSequentialAction();
		start.add(new CKPCFocusCameraCmd("mom"));
		start.add(new CKGUIAction(dFactory,1400));
		
		start.add(ready);
		start.add(doIt);

		Trigger startTrigger = new Trigger(new TrueSatisfies(), start,TriggerResult.INIT_ONLY);
		triggerLoop.add(startTrigger);
		Trigger loopTutorialTrigger = new Trigger(new NotSatisfies(winSatisfies), momAction);
		triggerLoop.add(loopTutorialTrigger);

	}


	@Override
	public void actionCompleted(CKGameAction action)
	{
		notifyOfInput();
		
	}
	 Node functions
	
	 (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#add(javax.swing.tree.MutableTreeNode)
	 
	@Override
	public void add(MutableTreeNode newChild)
	{
		if(getChildCount()<2)
		{
			super.insert(newChild,getChildCount());
		}
	}

	
	 (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		add(newChild);
	}
	
	
	 (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 
	@Override
	public Object clone()
	{
		Quest node= (Quest) super.clone();
		node.setGrid(new Grid(10,10));
		for(Object o: children)
		{
		//	Satisfies s = (Satisfies) o;
			node.add( (MutableTreeNode) ((CKGUINode) o ).clone());
		}
		node.qid=-1;
		node.actors = (ActorList) node.children.get(0);
		node.triggerLoop = (TriggerList) node.children.get(1);
		
		return node;
	}
	
	 (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#GUIEdit()
	 
	@Override
	public JMenuItem GUIEdit()
	{
		
		JMenu menu = new JMenu("Edit Quest");
		
		
		JMenu assetmenu = new JMenu("Pick Asset");
		CKGraphicsSceneInterface s = (new CKGraphicsSceneFactoryDB()).getGraphicsScene(sceneID);
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
			CKGraphicsSceneFactoryDB factory = new CKGraphicsSceneFactoryDB(); 
			
			CKScenePicker picker = new CKScenePicker(factory.getAllGraphicsScenes());
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
			sceneID=((CKGraphicsScene) a).getSID();
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


	 (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#GUIAddNode(ckGraphics.treegui.CKTreeGui)
	 
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		JMenu menu = new JMenu("Quest Commands");
		JMenuItem run = new JMenuItem("Run Quest");
		run.addActionListener(new RunAction( (Quest) this.clone()));
		menu.add(run);
		
		JMenuItem save = new JMenuItem("Save Quest");
		save.addActionListener(new SaveQuest());
		menu.add(save);
		
		return menu; 
	}	
	

	class SaveQuest  implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			Connection conn = CKConnection.getConnection();
			
			try
			{
				storeToDB(conn.createStatement());
			} catch (SQLException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	
	}
	class RunAction  implements ActionListener
	{
		Quest q;
		
		
		public RunAction(Quest q)
		{
			this.q=q;

	
		}

		public void actionPerformed(ActionEvent evt) 
		{
			new CKQuestRunner2(q);
		}
	}
	
	 (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		CKGraphicsSceneFactoryDB factory = new CKGraphicsSceneFactoryDB(); 
		return new ImageIcon(CKGraphicsPreviewGenerator.createScenePreview(
				factory.getGraphicsScene(sceneID),100,100));
				
	}
	
	
	static JPanel []panel;
	static JTextField[] nameText;
	static JComboBox[] resultBox;

	
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
			

		}
		
	}
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		
		nameText[index].setText(name);
		nameText[index].setColumns(15);
	}
	
	
	 (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}

	 (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 
	@Override
	public void storeComponentValues()
	{
		name = (String)nameText[EDIT].getText();
	}

	 (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}
	
	
	
	

	

}

*/
package ckGameEngine;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.swing.JFrame;

import ckDatabase.CKQuestFactory;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckTrigger.TriggerResult;


public class Quest  //implements CKGameActionListenerInterface 
{
	QuestData qData;
	
	int tId;
	int startTime;
	PriorityQueue<Event> eQueue;

	/*TriggerList triggerLoop=new TriggerList();
	ActorList actors = new ActorList();
*/
	int qid;
	

	volatile boolean inputOK;
	public Quest()
	{
		this(null);
	}
	
	private void init(QuestData qData)
	{
		this.qData = qData;
		qid=-1;
		tId=0;
		this.startTime=0;
		inputOK=false;
		
		
	}
	
	public Quest(QuestData qData)
	{
		init(qData);
	}
	
	
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return qData.getName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Quest:" + getName();
	}
	
	/**
	 * @return the tId
	 */
	public int gettId() {
		return tId;
	}
	
	public CKGridActor getActor(String actor)
	{
		return qData.getActor(actor);
	}

	public Vector<CKGridActor> getActorsFromTeam(String teamID)
	{
		return qData.getActorsFromTeam(teamID);
	}
	
	public List<CKGridActor> getActors()
	{
		return qData.getActors().getActors();
	}	
	
	/**
	 * @param tId the tId to set
	 */
	public void settId(int tId) {
		this.tId = tId;
	}

	/**
	 * @return the grid
	 */
	public CKGrid getGrid() {
		return CKGameObjectsFacade.getGrid();
	}


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
		//if(CKGameObjectsFacade.unitTest) {return;}
		tId =  CKGameObjectsFacade.getEngine().startTransaction(false);
	}

	public void endTransaction()
	{
		//if(CKGameObjectsFacade.unitTest) {return;}
		CKGameObjectsFacade.getEngine().endTransaction(tId, false);
	}
	
	public void holdPose(int waitTime)
	{
		startTime +=waitTime;
	}
	
	
	
	/**
	 * @return the inputOK
	 */
	public boolean isInputOK()
	{
		return inputOK;
	}


	public synchronized void notifyOfInput()
	{
			notifyAll();
	}


	public synchronized void waitForInput()
	{
		try {
			System.out.println("The Quest waits! ");
			inputOK=true;
			wait();
			inputOK=false;
			System.out.println("Quest stopped waiting here");
			
			
		} catch (InterruptedException e) {
			//THIS SHOULD BE HOW THE PROGRAM WAKES UP SO IT IS GOOD THIS SHOULD HAPPEN
			//e.printStackTrace();
		}
			
	}
	
	
	
	

	public double eventTime = 0.0;
	
	public void doEvent(Event currentEvent)
	{
		eventTime = currentEvent.getPriority();
		currentEvent.doEvent();
		Iterator<Event> e = currentEvent.getNewEvents();
		while(e.hasNext()) 	{ eQueue.add(e.next()); }
	}
	
	
	/**
	 * Loops through the trigger list and checks and performs each trigger
	 * @return  true if the quest is over, false otherwise
	 */
	protected boolean doTriggers(boolean init)
	{
		return qData.getTriggerLoop().doTriggers(init,null)==TriggerResult.SATISFIED_END_QUEST;
	}
	

	public void enqueueActors()
	{
		eQueue = new PriorityQueue<Event>();
		startTransaction();
		qData.getActors().enqueueActors(eQueue);
		endTransaction();
		
	}
	
	public void enqueueAddedActor(CKGridActor actor)
	{
		assert(eQueue!=null);
		startTransaction();

		actor.getTurnController().onLoad();
		Event e = actor.getTurnController().getInitialTurnEvent(eventTime);
		if(e!=null)
		{
			eQueue.add(e);
		}	
		
		
		endTransaction();
		
		
		
	}
	
	public void setCopy()
	{
		qid=-1;
	}
	
	public void setupScene()
	{
		CKGameObjectsFacade.getEngine().prefWidth(600);
		CKGameObjectsFacade.getEngine().prefHeight(600);

		
		CKGameObjectsFacade.getEngine().loadScene(qData.getSceneID());
		
	}
	
	public void registerQuest()
	{
		CKGameObjectsFacade.setQuest(this);
	}
	public void gameLoop()
	{
		registerQuest();
		setupScene();
		enqueueActors();
		
		if(doTriggers(true)) { return; }
		
		while(true) 
		{
			Event currentEvent = eQueue.poll();
			System.out.println(currentEvent);
			
			if(currentEvent instanceof EndEvent) { return; }
			
			if(currentEvent == null) {
				return;
			}
			doEvent(currentEvent);
			
			if( doTriggers(false)) { return; }	
		}
	}

	
	public String getTeam()
	{
		return qData.getTeam();
		
	}
	

	/**
	 * @param args
	 * @throws LoadAssetError 
	 */
	public static void main(String[] args) throws LoadAssetError 
	{
		

		JFrame frame = new JFrame();

		//Quest quest = new Quest(creation(2));
		Quest quest = new Quest(CKQuestFactory.getInstance()
				.getAsset("asset1851556717277832909"));
	
		 CKGameObjectsFacade.setQuest(quest);
		 
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(CKGameObjectsFacade.getJPanelEngine());
		CKGameObjectsFacade.setQuest(quest);
		quest.gameLoop();
	}

	
	
	
/* old...
	
	//override to create different levels
	public static QuestData creation(int scene)
	{

		CKGrid grid = new CKGrid(10,10);
		QuestData qData = new QuestData(5);
		qData.setQid("level1-proto");
		qData.setSceneID("Kitchen");
		
		qData.addBook(new CKBook("fredo","bongo",1 ));
		qData.addBook(new CKBook("sally","knife",10 ));

		// MKB refactoring for CKGrid and GridItems...
		ActorNode heroAct =	new ActorNode("HERO","babySprite",Direction.SOUTHWEST, 
				new CKPosition(5,3),1,new CKCharacter("babySprite","HERO"),new CKTriggerList());
		ActorNode momAct =	new ActorNode("mom","momSprite",Direction.NORTHEAST, 
				new CKPosition(5,4),0,new CKCharacter("momSprite","mom"),new CKTriggerList());
		qData.addActor(heroAct);
		qData.addActor(momAct);
		
		Satisfies winSatisfies = new PositionReachedSatisfies("HERO", new CKPosition(5, 4));
				
		CKDialogMessageFactoryExample dFactory = new CKDialogMessageFactoryExample();
		CKQuestAction forward = new CKPCRelativeMoveCmd("mom", 1);
		CKQuestAction right = new CKPCTurnCmd("mom", true, 15);
		
		//Start/Lose/Win actions
		qData.addTrigger(new CKTrigger(winSatisfies,
				new CKGUIAction(dFactory, 1300),TriggerResult.SATISFIED_END_QUEST));	
		qData.addTrigger(new CKTrigger(new DeadSatisfies("HERO"),
				new CKGUIAction(dFactory, 1350),TriggerResult.SATISFIED_END_QUEST));
		
		CKSequentialAction comfort = new CKSequentialAction();
		comfort.add(forward);
		comfort.add(new CKGUIAction(dFactory,1000));
		
		
		
		CKSequentialAction ready =  new CKSequentialAction();
		ready.add(new CKGUIAction(dFactory,1100));
		ready.add((CKGameAction)right.clone());
		ready.add((CKGameAction)right.clone());
		ready.add((CKGameAction)forward.clone());
		ready.add((CKGameAction)forward.clone());
		ready.add((CKGameAction)right.clone());
		ready.add((CKGameAction)right.clone());
		
		CKSequentialAction ready0 =  new CKSequentialAction();
		ready0.add(new CKGUIAction(dFactory,1100));
		ready0.add((CKGameAction)right.clone());
		ready0.add((CKGameAction)right.clone());
		ready0.add((CKGameAction)forward.clone());
		ready0.add((CKGameAction)forward.clone());
		ready0.add((CKGameAction)right.clone());
		ready0.add((CKGameAction)right.clone());

		CKSequentialAction doIt = new CKSequentialAction();
		doIt.add(new CKGUIAction(dFactory,1200)); //watch me
		
		//is not doing these concurrently...
		CKConcurrentAction moveAndSpeak = new CKConcurrentAction();
		moveAndSpeak.add(new CKGUIAction(dFactory,1250));
		
		CKSequentialAction act = new CKSequentialAction();
		act.add(new CKWaitCmd(45) );
		act.add((CKGameAction)forward.clone());
		
		moveAndSpeak.add(act);
		
		doIt.add(moveAndSpeak);
		
		CKSequentialAction doIt0 = new CKSequentialAction();
		doIt0.add(new CKGUIAction(dFactory,1200)); //watch me
		
		
		CKConcurrentAction moveAndSpeak0 = new CKConcurrentAction();
		moveAndSpeak0.add(new CKGUIAction(dFactory,1250));
		
		CKSequentialAction act0 = new CKSequentialAction();
		act0.add(new CKWaitCmd(45) );
		act0.add((CKGameAction)forward.clone());
		
		moveAndSpeak0.add(act0);
		
		doIt0.add(moveAndSpeak0);
		
		CKSequentialAction momAction = new CKSequentialAction();
		momAction.add(new CKPCFocusCameraCmd("mom"));
		
		momAction.add(comfort);
		momAction.add(ready);
		momAction.add(doIt);
		
		
		CKSequentialAction start = new CKSequentialAction();
		start.add(new CKPCFocusCameraCmd("mom"));
		start.add(new CKGUIAction(dFactory,1400));
		
		start.add(ready);
		start.add(doIt);

		CKTriggerNode startTrigger = new CKTrigger(new TrueSatisfies(), start,TriggerResult.INIT_ONLY);
		qData.addTrigger(startTrigger);
		CKTriggerNode loopTutorialTrigger = new CKTrigger(new NotSatisfies(winSatisfies), momAction);
		qData.addTrigger(loopTutorialTrigger);

		return qData;
	}
*/
	public QuestData getQuestData()
	{
		return qData;
	}

	public void applyWorldFilters(CKGameActionListenerInterface boss, CKSpellCast cast)
	{
		qData.getSpellFilter().doTriggers(boss, false, cast);			
	}

	
	private HashMap<String,Integer>bgSounds = new HashMap<String,Integer>(); 
	
	public int getBackGroundSoundSID(String soundAID)
	{
		if(! bgSounds.containsKey(soundAID))
		{
			FX2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
			int SID = engine.createSoundInstance(0, soundAID);
			bgSounds.put(soundAID, SID);
		}
		return bgSounds.get(soundAID);
	}

	

	
	
	
}



