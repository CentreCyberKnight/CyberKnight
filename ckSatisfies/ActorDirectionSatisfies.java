package ckSatisfies;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;

public class ActorDirectionSatisfies extends Satisfies 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3642344006960003239L;
	private String actor = "HERO";
	private Direction direction = Direction.NONE;
	private boolean victim = false;

	public ActorDirectionSatisfies(String actor, Direction direction)
	{
		super();
		this.actor = actor;
		this.direction = direction;
	}

	public ActorDirectionSatisfies()
	{
		this("HERO", Direction.NONE);
	}

	/**
	 * @return the actor
	 */
	public String getActor()
	{
		return actor;
	}

	/**
	 * @param actor
	 *            the actor to set
	 */
	public void setActor(String actor)
	{
		this.actor = actor;
	}

	

	@Override
	public boolean isSatisfied(CKSpellCast cast)
	{
		Direction d;
		
		if(victim)
		{
			d = cast.getActorTarget().getDirection();
		}
		else
		{
			d = getQuest().getActor(actor).getDirection();
		}
			
		return d==direction;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Actor Direction Satisfies [actor=" + actor
				+ ", directiont=" + direction +  "]";
	}

	static JPanel[] panel;
	static JComboBox<String>[] actorBox;
	static JComboBox<Direction>[] directionBox;


	@SuppressWarnings("unchecked")
	private void initPanel(boolean force)
	{
		if (panel == null || force)
		{
			panel = new JPanel[2];
			panel[0] = new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.Y_AXIS));
			panel[1] = new JPanel();
			panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.Y_AXIS));

			JPanel[] top = new JPanel[2];
			top[0] = new JPanel();
			top[1] = new JPanel();

			top[0].add(new JLabel("When "));
			top[1].add(new JLabel("When "));

			actorBox = new JComboBox[2];
			actorBox[0] = new JComboBox<String>();
			actorBox[1] = new JComboBox<String>();
			actorBox[EDIT].setEditable(true);
			top[0].add(actorBox[0]);
			top[1].add(actorBox[1]);

			top[0].add(new JLabel("Faces"));
			top[1].add(new JLabel("Faces"));
			
			
			/*---*/

			JPanel[] bot = new JPanel[2];
			bot[0] = new JPanel();
			bot[1] = new JPanel();

			directionBox = new JComboBox[2];
			directionBox[0] = new JComboBox<Direction>(
					Direction.values());
			directionBox[1] = new JComboBox<Direction>(
					Direction.values());
			bot[0].add(directionBox[0]);
			bot[1].add(directionBox[1]);

		

			panel[0].add(top[0]);
			panel[1].add(top[1]);
			panel[0].add(bot[0]);
			panel[1].add(bot[1]);

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

		if(getQuest()!=null)
		{
			actor = this.initializeActorBox(actorBox[index], actor);
		}
		directionBox[index].setSelectedItem(direction);
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
		setPanelValues(EDIT);
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
		actor = (String) actorBox[EDIT].getEditor().getItem();
		
		direction = (Direction) directionBox[EDIT].getSelectedItem();
		// System.out.println("Stored"+this);
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

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("CyberKnight SAT Editor");
		CKGuiRoot root = new CKGuiRoot();

		root.add(new ActorDirectionSatisfies());
		frame.add(new CKTreeGui(root));
		// frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
