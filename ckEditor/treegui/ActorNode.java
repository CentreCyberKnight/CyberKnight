package ckEditor.treegui;

import static ckCommonUtils.CKPropertyStrings.*;
import static ckCommonUtils.CKPropertyStrings.P_STAR;
import static ckCommonUtils.CKPropertyStrings.P_TARGET;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckDatabase.CKActorControllerFactory;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.CKGridActorFactory;
import ckDatabase.CKTeamFactory;
import ckEditor.CKPositionSetterListener;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.ActorTurnController;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItem;
import ckGameEngine.CKPage;
import ckGameEngine.Direction;
import ckGameEngine.Grid;
import ckGameEngine.ActorArtifactController;
import ckGameEngine.ActorNullController;
import ckGameEngine.ActorController;
import ckGameEngine.actions.CKMoveActorCmd;
import ckGameEngine.actions.CKNullAction;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckSatisfies.NumericalCostType;
import ckSatisfies.SpellSatisfies;
import ckSatisfies.TrueSatisfies;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.CKTriggerListNode;

public class ActorNode extends CKGUINode implements CKPositionSetterListener
{
	
	private static final long serialVersionUID = -8261330449029307737L;
	String name;
//	String assetID;
	String actorID;
	Direction direction;
	CKPosition pos;
	String controllerID;
	private CKGridActor pc=null;
	//CKCharacter characterData;
	//TriggerList triggers;
	
	
	/**
	 * @return the triggers
	 */
	public CKTriggerList getTriggers()
	{
		return ( (CKTriggerList) this.getChildAt(0));
	}

	/**
	 * @param triggers the triggers to set
	 */
	public void setTriggers(CKTriggerListNode triggers)
	{
		add(triggers);
	}
/*
	public ActorNode(String name, String assetId,
			Direction direction,CKPosition pos, int controllerID,
			CKTriggerListNode triggers)
	{
		super();
		System.out.println("I am making an actor!");
		this.name = name;
		this.assetID = assetId;
		this.direction = direction;
		this.pos = pos;
		this.controllerID = controllerID;
		setChildOrderLocked(true);
		setChildRemoveable(false);
	//	this.triggers = triggers;
		add(triggers);
	}*/

	/**
	 * This constructor should only be called by XMLDecode/encode
	 * call ActorNode(1) instead
	 */
	public ActorNode()
	{
		//(new CKGraphicsAssetFactoryDB()).getGraphicsAsset(11),
		//this("ArtifactTestBaby",	Direction.SOUTHEAST,new CKPosition(),1,new CKTriggerList());
		this("ArtifactTestBaby",	Direction.SOUTHEAST,new CKPosition());
	}
	
	
	/**
	 * completly ignores the parameter
	 * @param i ignored.
	 */
	public ActorNode(int i)
	{
		//(new CKGraphicsAssetFactoryDB()).getGraphicsAsset(11),
		this("ArtifactTestBaby",	Direction.SOUTHEAST,new CKPosition(),new CKTriggerList());
	}

	
	//Called from default constructor, XMLENCODE only.
	public ActorNode(String ActorID, Direction direction, CKPosition pos)
	{
		super();
		//System.out.println("I am making an actor!");
		setActorId(ActorID);

		name=pc.getName();

		this.direction = direction;
		this.pos = pos;
		//this.controllerID = controllerID;
		setChildOrderLocked(true);
		setChildRemoveable(false);
		//this.triggers = triggers;
		//add(triggers);
	}

	
	
	public ActorNode(String ActorID, Direction direction, CKPosition pos, CKTriggerList triggers)
	{
		super();
		//System.out.println("I am making an actor!");
		setActorId(ActorID);

		name=pc.getName();

		this.direction = direction;
		this.pos = pos;
		
		setChildOrderLocked(true);
		setChildRemoveable(false);
		
		add(triggers);
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
		//pc.setName(name);
	}

	private boolean shouldInitPC = true;
	
	
	private CKGridActor initPC(String teamId)
	{
		shouldInitPC=false;
		pc.setDirection(direction);
		pc.setName(name);
		pc.setTreeParent(this);
		pc.setTeam(CKTeamFactory.getInstance().getAsset(teamId));
		pc.setPos(getPos());
		pc.setControllerID(controllerID);
	/*	
		ActorController cont;
		switch(controllerID)
		{
		case 0: //null controller
			cont = new ActorNullController(pc);
			break;
		case 1:
			System.out.println(name+"text controller");
			cont = new ActorArtifactController(pc,ActorController.TEXT_CONTROL);
			break;
		case 2:
			System.out.println(name+"artifact controller");
			cont = new ActorArtifactController(pc,ActorController.ARTIFACT_CONTROL);
			break;
		case 3:
			System.out.println(name+"Both controller");
			cont = new ActorArtifactController(pc,ActorController.BOTH_CONTROL);
			break;
		case 4:
			System.out.println(name+"Turn controller");
			cont = new ActorTurnController(pc);
			break;
		case 5: 
			System.out.println(name+"Debug controller, do nothing");
			return pc; 
		default:
			 cont  = new ActorNullController(pc);					
		}			
		pc.setTurnController(cont);
		*/
		return pc;
	}
	
	
	/**
	 * Add to the grid
	 * @param teamId
	 * @return
	 */
	public CKGridActor startPC(String teamId)
	{
		if(shouldInitPC) { initPC(teamId); }
		
		if(pc.getInstanceID()<0)
		{
			CKGameObjectsFacade.getQuest().getGrid().addToPosition(pc, pos);
		}
		//FIXME - pc.setPersonalTriggers((CKTriggerList) getTriggers());
		pc.setCyberPoints(100);
		
		
		//FIXME -need elegant way to do this...
		//books
		
		CKBook book = new CKBook("Mine");
		book.addChapter(new CKChapter(CKPropertyStrings.CP_PER_ROUND,1000));	
		CKChapter move = new CKChapter("MOVE",10,"Forward");	
		move.addPage(CKPropertyStrings.P_LEFT);
		move.addPage(CKPropertyStrings.P_RIGHT);	
		move.addPage(CKPropertyStrings.P_JUMP_UP);
		book.addChapter(move);
		CKChapter chap3 = new CKChapter(CH_AIM, 7);
		chap3.addPage(new CKPage(P_TARGET));
		chap3.addPage(new CKPage(P_STAR));
		chap3.addPage(new CKPage(P_FRONT));
		book.add(chap3);
		CKChapter earth = new CKChapter(CH_EARTH,100);
		earth.addPage(CKPropertyStrings.P_SLICE);
		earth.addPage(CKPropertyStrings.P_SHOVE);
		book.addChapter(earth);
		//CKChapter water = new CKChapter(CH_WATER,100);
		//water.addPage(CKPropertyStrings.P_ICE);
		//water.addPage(CKPropertyStrings.P_RAIN);
		//book.addChapter(water);

		
		//pc.setQuestAbilities(book);
		pc.addAbilities(book);
		
		//triggers
		//SpellSatisfies mSat = new SpellSatisfies(CKPropertyStrings.CH_MOVE, CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);
		
		//CKTrigger trigger = new CKTrigger(mSat,new CKMoveActorCmd());
		//pc.addTrigger(trigger);
		pc.addTriggers(getTriggers());
	
		//TODO use database to store this stuff
		
		
		
		
		return getPC(teamId);
	}
	
	
	public CKGridActor getPC(String teamId)
	{
		
		if(shouldInitPC) { initPC(teamId); }
			
			
	
		return pc;
		
	}
		
	/**
	 * @return the assetId
	 */
	public String getActorId()
	{
		return actorID;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setActorId(String assetId)
	{
		this.actorID = assetId;
		pc = (CKGridActor) CKGridActorFactory.getInstance().getAsset(assetId,true);
		this.controllerID = pc.getControllerID();
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection()
	{
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	/**
	 * @return the pos
	 */
	public CKPosition getPos()
	{
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(CKPosition pos)
	{
		this.pos = pos;
	}

	/**
	 * @return the controllerID
	 */
	public String getControllerID()
	{
		return controllerID;
	}

	/**
	 * @param controllerID the controllerID to set
	 */
	public void setControllerID(String controllerID)
	{
		this.controllerID = controllerID;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Actor: " + name;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{//this must be setup properly in the constructor for this to work later.
		//will ignore childIndex
		if(newChild instanceof CKTriggerListNode)
		{
			if( getChildCount() ==1)
			{
				System.out.println("Out with the Kid");
				remove(0); 
			}
			super.insert(newChild,0);
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
				node.add( (MutableTreeNode) ((CKTriggerListNode) o ).clone());
			}
		}
		return node;
	}

	
	
	
	
		
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance(); 
		return new ImageIcon(CKGraphicsPreviewGenerator.createAssetPreview(
				factory.getGraphicsAsset(pc.getAssetID()),
				0,0,64,128) );
	}

	
	class AssetPostionSelector implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			getQuest().reqestPosition(ActorNode.this, getPC("NULL"),true);
			
		}
		
	}
	
	class DirectionChangedListener implements ItemListener
	{

		@Override
		public void itemStateChanged(ItemEvent e)
		{
			
			if(e.getStateChange()==ItemEvent.SELECTED)
			{
				//System.out.println(name+" "+e.getItem() + " " + e.getStateChange());
				shouldInitPC=true;
				direction = (Direction) directionBox[EDIT].getSelectedItem();
				getQuest().notifyGraphicsChanged();
			}
		}
		
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
			
			CKXMLFilteredAssetPicker<CKGridItem,CKGridActorFactory> picker = 
					new CKXMLFilteredAssetPicker<CKGridItem,CKGridActorFactory>(CKGridActorFactory.getInstance());
			picker.addSelectedListener(new AssetListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}
		
	}
	
	
	
	class AssetListener implements CKEntitySelectedListener<CKGridItem>
	{
		JFrame frame;
		public AssetListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(CKGridItem a)
		{
			setActorId(a.getAID());
			shouldInitPC=true;
			getQuest().notifyGraphicsChanged();
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}

	class ControllerViewerPopupListener implements ActionListener
	{

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			
			CKXMLFilteredAssetPicker<ActorController,CKActorControllerFactory> picker = 
					new CKXMLFilteredAssetPicker<ActorController,CKActorControllerFactory>(CKActorControllerFactory.getInstance());
			picker.addSelectedListener(new ControllerListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}
		
	}
	
	
	
	class ControllerListener implements CKEntitySelectedListener<ActorController>
	{
		JFrame frame;
		public ControllerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(ActorController a)
		{
			setControllerID(a.getAID());
			controllerName[EDIT].setText(a.getName());
			shouldInitPC=true;
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}

	
	
	static JPanel []panel;
	static JTextField[] nameFields;
	static JButton[] pickPosition;
	static JLabel positionLabel[];
//	static SpinnerNumberModel [][] positionSpinners; //x and y pos
	static JComboBox<Direction> [] directionBox;
	
	static JTextField[] controllerName;
	static JButton[] controllerButton;
	//static JComboBox<String> [] controllerBox;
	//public final static String[] controllerTypes = {"Null","Text","Artifact","Both","Turn","Debug"};
	
	
	static JButton[] pickActor;
	static AssetViewerPopupListener actorListener;
	static AssetPostionSelector positionListener;
	private AssetPostionSelector myPositionListener;
	static  DirectionChangedListener directionListener;
	static ControllerViewerPopupListener controllerListener;
	

	
	@SuppressWarnings("unchecked")
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.PAGE_AXIS));
			panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.PAGE_AXIS));

			panel[0].setBorder(BorderFactory.createEmptyBorder(2,2,2,0));
			panel[1].setBorder(BorderFactory.createEmptyBorder(2,2,2,0));
			
			JPanel []tops=new JPanel[2];
			tops[0]=new JPanel();
			tops[1]=new JPanel();
			
			tops[0].setLayout(new BoxLayout(tops[0], BoxLayout.LINE_AXIS));
			tops[0].setBackground(colors[0]);
			tops[1].setBackground(colors[1]);
			tops[1].setLayout(new BoxLayout(tops[1], BoxLayout.LINE_AXIS));
			
			
			//name
			nameFields = new JTextField[2];
			nameFields[0]=new JTextField(5);
			nameFields[1]=new JTextField(5);
			tops[0].add(nameFields[0]);
			tops[1].add(nameFields[1]);
			
			
			
			pickActor = new JButton[2];
			pickActor[0] = new JButton("pick actor");
			pickActor[1] = new JButton("pick actor");
			tops[0].add(pickActor[0]);
			tops[1].add(pickActor[1]);
			
			
			


			panel[0].add(tops[0]);
			panel[1].add(tops[1]);
			//controllers
			JPanel [] mid = new JPanel[2];
			mid[0] = new JPanel();
			mid[1] = new JPanel();
			
			mid[0].setLayout(new BoxLayout(mid[0], BoxLayout.LINE_AXIS));
			mid[1].setLayout(new BoxLayout(mid[1], BoxLayout.LINE_AXIS));
			mid[0].setBackground(colors[0]);
			mid[1].setBackground(colors[1]);
			
		/*
			mid[0].add(new JLabel(" w/ controller "));
			mid[1].add(new JLabel(" w/ controller "));
			*/
			controllerName = new JTextField[2];
			controllerName[0] = new JTextField(15);
			controllerName[1] = new JTextField(15);
			mid[0].add(controllerName[0]);		
			mid[1].add(controllerName[1]);	
			
			controllerButton = new JButton[2];
			controllerButton[0] = new JButton("Pick Controller");
			controllerButton[1] = new JButton("Pick Controller");
			mid[0].add(controllerButton[0]);
			mid[1].add(controllerButton[1]);
			
			
			panel[0].add(mid[0]);
			panel[1].add(mid[1]);

			JPanel []bot=new JPanel[2];
			bot[0]=new JPanel();
			bot[1]=new JPanel();
			
			bot[0].setLayout(new BoxLayout(bot[0], BoxLayout.LINE_AXIS));
			bot[1].setLayout(new BoxLayout(bot[1], BoxLayout.LINE_AXIS));
			bot[0].setBackground(colors[0]);
			bot[1].setBackground(colors[1]);

			
			//starts at 			
			bot[0].add(new JLabel(" Starts at "));
			bot[1].add(new JLabel(" Starts at "));

			positionLabel = new JLabel[2];
			positionLabel[0] =new JLabel();
			positionLabel[1] = new JLabel();
			bot[0].add(positionLabel[0]);
			bot[1].add(positionLabel[1]);
			
			pickPosition = new JButton[2];
			pickPosition[0] = new JButton("pick Position");
			pickPosition[1] = new JButton("pick Position");
				
			
			bot[0].add(pickPosition[0]);
			bot[1].add(pickPosition[1]);


			panel[0].add(bot[0]);
			panel[1].add(bot[1]);
			
		
			
			JPanel []button=new JPanel[2];
			button[0]=new JPanel();
			button[1]=new JPanel();
			
			button[0].setLayout(new BoxLayout(button[0], BoxLayout.LINE_AXIS));
			button[1].setLayout(new BoxLayout(button[1], BoxLayout.LINE_AXIS));
			button[0].setBackground(colors[0]);
			button[1].setBackground(colors[1]);
			
			button[0].add(new JLabel(" Faces "));
			button[1].add(new JLabel(" Faces "));
			
			directionBox = new JComboBox[2];
			directionBox[0] = new JComboBox<Direction>(Direction.values());
			directionBox[1] = new JComboBox<Direction>(Direction.values());
			button[0].add(directionBox[0]);		
			button[1].add(directionBox[1]);		
		
			
			panel[0].add(button[0]);
			panel[1].add(button[1]);
			
			
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
		
		nameFields[index].setText(name);
		positionLabel[index].setText("At ("+(int)pos.getX()+","+(int)pos.getY()+")");
		directionBox[index].setSelectedItem(direction);
		
		String name = CKActorControllerFactory.getInstance().getAsset(controllerID).getName();
		controllerName[index].setText(name);
	
		panel[index].validate();
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		
		//first get rid of old listeners
		pickActor[EDIT].removeActionListener(actorListener);
		pickPosition[EDIT].removeActionListener(positionListener);
		directionBox[EDIT].removeItemListener(directionListener);
		controllerButton[EDIT].removeActionListener(controllerListener);
		
		//change values
		setPanelValues(EDIT);
		
		//set new listeners
		actorListener = new AssetViewerPopupListener();
		pickActor[EDIT].addActionListener(actorListener);
		
		positionListener = new AssetPostionSelector();
		myPositionListener = positionListener;
		pickPosition[EDIT].addActionListener(positionListener);
		
		directionListener = new DirectionChangedListener();
		directionBox[EDIT].addItemListener(directionListener);
		
		controllerListener = new ControllerViewerPopupListener();
		controllerButton[EDIT].addActionListener(controllerListener);
		
		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		
		name = nameFields[EDIT].getText();
		direction = (Direction) directionBox[EDIT].getSelectedItem();
		
		//controllerID = controllerBox[EDIT].getSelectedIndex();
		/*
		String cont =((String) controllerBox[EDIT].getSelectedItem()  ); 
		
		if( cont.compareToIgnoreCase("NULL")==0     )
		{
			controllerID = 0;
		}
		else if (cont.compareToIgnoreCase("TEXT")==0)
		{
			controllerID = 1;
		}
		else
		{
			controllerID=2;
		}*/
			
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
	
	
	
 public static void main(String[] args)
 {
		JFrame frame = new JFrame("CyberKnight Actor Editor");
		CKGuiRoot root = new CKGuiRoot();

		new Grid(10,10);
		CKTriggerList tl = new CKTriggerList();
		CKTrigger trig = new CKTrigger(new TrueSatisfies(),
									   new CKNullAction());	
		tl.add(trig);
		ActorNode heroAct =	
				new ActorNode("SpellTestDad",Direction.SOUTHWEST, 
				new CKPosition(5,3),tl);
		
		System.out.println("Children"+heroAct.children.size());
		root.add( heroAct);
		frame.add(new CKTreeGui(root));
		//frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	 
		heroAct.writeToStream(System.out);
		//tl.writeToStream(System.out);
	 
	 
	 
 }

 /**
  * This is called from the quest editor.
  */
@Override
public void setPosition(CKPosition pos)
{
	setPos(pos);
	if(positionListener == myPositionListener)
	{
		positionLabel[EDIT].setText("At ("+(int)pos.getX()+","+(int)pos.getY()+")");
	}
	
}

@Override
public CKPosition getPosition()
{
	return getPos();
}
	
}
