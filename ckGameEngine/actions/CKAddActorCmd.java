/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKPosition;
import ckDatabase.CKActorControllerFactory;
import ckDatabase.CKGridActorFactory;
import ckDatabase.CKTeamFactory;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.ActorController;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItem;
import ckGameEngine.CKSpellCast;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;

/**
 * @author dragonlord
 * 
 */
public class CKAddActorCmd extends CKQuestAction implements ActionListener
{

	private static final long serialVersionUID = -6822390508048805078L;

	String actorID;
	String teamID;
	String controllerID;
	Direction location;
	boolean overlap;

	public CKAddActorCmd()
	{
		this("ArtifactTestBaby", "NULL", Direction.SOUTHWEST, false);
	}

	public CKAddActorCmd(String actorID, String teamID, Direction location,
			boolean overlap)
	{
		
		controllerID = "TURN";
		setActorID(actorID);
		this.teamID = teamID;
		this.location = location;
		this.overlap = overlap;

	}

	/**
	 * @return the controllerID
	 */
	public String getControllerID()
	{
		return controllerID;
	}

	/**
	 * @param controllerID
	 *            the controllerID to set
	 */
	public void setControllerID(String controllerID)
	{
		this.controllerID = controllerID;
	}

	/**
	 * @return the actorID
	 */
	public String getActorID()
	{
		return actorID;
	}

	/**
	 * @param actorID
	 *            the actorID to set
	 */
	public void setActorID(String actorID)
	{
		this.actorID = actorID;
		/*calling this code will result in an infiniate loop
		 * Since this can be inside an actor trigger
		 * Asking for another actor that has the same trigger will cause inf recursion.
		  CKGridActor actor = (CKGridActor) CKGridActorFactory.getInstance()
		 
				.getAsset(actorID);
		if (actor != null)
		{
			controllerID = actor.getControllerID();
		} else
		{
			controllerID = "TURN";
		}
		*/

	}

	/**
	 * @return the teamID
	 */
	public String getTeamID()
	{
		return teamID;
	}

	/**
	 * @param teamID
	 *            the teamID to set
	 */
	public void setTeamID(String teamID)
	{
		this.teamID = teamID;
	}

	/**
	 * @return the location
	 */
	public Direction getLocation()
	{
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Direction location)
	{
		this.location = location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CKAddActorCmd [actor=" + actorID + ", pos=" + location + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		// for now ignore cast, will return later to
		// allow blank values to be utilized by the casting...

		System.err.println("creating actor");
		CKGridActor actor = (CKGridActor) CKGridActorFactory.getInstance()
				.getAsset(actorID);
		// now clean up.
		actor.setTeam(CKTeamFactory.getInstance().getAsset(teamID));
		actor.setTreeParent(this);

		// setPosition
		CKPosition pos = (CKPosition) cast.getTargetPosition().clone();
		pos.setX(pos.getX() + location.dx);
		pos.setY(pos.getY() + location.dy);

		// should check if the space is occupied...
		// FIXME


		// add to grid
		CKGameObjectsFacade.getQuest().getGrid().addToPosition(actor, pos);
		
		//add to eventLoop
		CKGameObjectsFacade.getQuest().enqueueAddedActor(actor);

	}

	static JPanel[] panel;
	// static JComboBox<CKGridActor> []actorBox;
	static JTextField[] actorName;
	static JButton[] actorButton;

	static JComboBox<CKTeam>[] teamBox;
	static JComboBox<Direction>[] directionBox;
	static JTextField[] controllerName;
	static JButton[] controllerButton;

	static JCheckBox[] overlapCheck;

	// will add more check boxes..

	static ActionListener currentListener = null;
	static ControllerViewerPopupListener controllerListener;

	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
	{
		if (panel == null || force)
		{
			panel = new JPanel[2];
			panel[0] = new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.Y_AXIS));
			panel[1] = new JPanel();
			panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.Y_AXIS));

			JPanel top0 = new JPanel();
			JPanel top1 = new JPanel();
			JPanel bot0 = new JPanel();
			JPanel bot1 = new JPanel();

			actorName = new JTextField[2];
			actorName[0] = new JTextField();
			actorName[0].setEditable(false);
			actorName[1] = new JTextField();
			actorName[1].setEditable(false);

			actorButton = new JButton[2];
			actorButton[0] = new JButton("Pick Actor");
			actorButton[1] = new JButton("Pick Actor");

			top0.add(actorName[0]);
			top0.add(actorButton[0]);
			top1.add(actorName[1]);
			top1.add(actorButton[1]);

			JPanel[] mid = new JPanel[2];
			mid[0] = new JPanel();
			mid[1] = new JPanel();

			mid[0].setLayout(new BoxLayout(mid[0], BoxLayout.LINE_AXIS));
			mid[1].setLayout(new BoxLayout(mid[1], BoxLayout.LINE_AXIS));
			mid[0].setBackground(colors[0]);
			mid[1].setBackground(colors[1]);

			/*
			 * mid[0].add(new JLabel(" w/ controller ")); mid[1].add(new
			 * JLabel(" w/ controller "));
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

			teamBox = new JComboBox[2];
			teamBox[0] = new JComboBox<CKTeam>();
			teamBox[1] = new JComboBox<CKTeam>();
			bot0.add(teamBox[0]);
			bot1.add(teamBox[1]);

			directionBox = new JComboBox[2];
			directionBox[0] = new JComboBox<Direction>(Direction.values());
			directionBox[1] = new JComboBox<Direction>(Direction.values());
			bot0.add(directionBox[0]);
			bot1.add(directionBox[1]);

			panel[0].add(top0);
			panel[0].add(mid[0]);
			panel[0].add(bot0);

			panel[1].add(top1);
			panel[1].add(mid[1]);
			panel[1].add(bot1);

		}

	}

	private final static int EDIT = 0;
	private final static int RENDER = 1;
	private final static Color[] colors = { Color.GREEN, Color.WHITE };

	private void setPanelValues(int index)
	{
		// System.out.println("setting panel");
		if (panel == null)
		{
			initPanel(true);
		}
		panel[index].setBackground(colors[index]);

		if (CKGridActorFactory.getInstance().getAsset(actorID) != null)
		{
			actorName[index].setText(CKGridActorFactory.getInstance()
					.getAsset(actorID).getName());
		} else
		{
			actorName[index].setText("null");
		}
		this.initializeJComboBoxByID(teamBox[index], CKTeamFactory
				.getInstance().getAllAssets(), teamID);

		String name = CKActorControllerFactory.getInstance()
				.getAsset(controllerID).getName();
		controllerName[index].setText(name);

		directionBox[index].setSelectedItem(location);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree
	 * , java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		actorButton[EDIT].removeActionListener(currentListener);
		controllerButton[EDIT].removeActionListener(controllerListener);
		
		setPanelValues(EDIT);
		
		
		currentListener = this;
		actorButton[EDIT].addActionListener(currentListener);
		controllerListener = new ControllerViewerPopupListener();
		controllerButton[EDIT].addActionListener(controllerListener);
		
		return panel[EDIT];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		// actorID is set by the button...
		teamID = teamBox[EDIT].getItemAt(teamBox[EDIT].getSelectedIndex())
				.getAID();
		location = directionBox[EDIT].getItemAt(directionBox[EDIT]
				.getSelectedIndex());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing
	 * .JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFrame frame = new JFrame();
		CKXMLFilteredAssetPicker<CKGridItem, CKGridActorFactory> picker = new CKXMLFilteredAssetPicker<>(
				CKGridActorFactory.getInstance());
		;
		picker.addSelectedListener(new PickerListener(frame));
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);

	}

	class PickerListener implements CKEntitySelectedListener<CKGridItem>
	{
		JFrame frame;

		public PickerListener(JFrame f)
		{
			frame = f;
		}

		@Override
		public void entitySelected(CKGridItem a)
		{
			actorName[EDIT].setText(a.getName());
			actorID = a.getAID();
			frame.setVisible(false); // you can't see me!
			frame.dispose(); // Destroy the JFrame object
		}

	}

	class ControllerViewerPopupListener implements ActionListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();

			CKXMLFilteredAssetPicker<ActorController, CKActorControllerFactory> picker = new CKXMLFilteredAssetPicker<ActorController, CKActorControllerFactory>(
					CKActorControllerFactory.getInstance());
			picker.addSelectedListener(new ControllerListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}

	}

	class ControllerListener implements
			CKEntitySelectedListener<ActorController>
	{
		JFrame frame;

		public ControllerListener(JFrame f)
		{
			frame = f;
		}

		@Override
		public void entitySelected(ActorController a)
		{
			setControllerID(a.getAID());
			controllerName[EDIT].setText(a.getName());
			frame.setVisible(false); // you can't see me!
			frame.dispose(); // Destroy the JFrame object
		}

	}

}
