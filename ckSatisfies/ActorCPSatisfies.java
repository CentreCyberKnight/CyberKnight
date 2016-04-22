package ckSatisfies;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;

public class ActorCPSatisfies extends Satisfies 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3642344006960003239L;
	private String actor = "HERO";
	private int cost = 0;
	private NumericalCostType t = NumericalCostType.FALSE;
	private boolean victim = false;

	public ActorCPSatisfies(String actor, int cost, NumericalCostType t)
	{
		super();
		this.actor = actor;
		this.cost = cost;
		this.t = t;
	}

	public ActorCPSatisfies()
	{
		this("HERO", 0, NumericalCostType.FALSE);
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

	/**
	 * @return the cost
	 */
	public int getCost()
	{
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(int cost)
	{
		this.cost = cost;
	}

	/**
	 * @return the t
	 */
	public NumericalCostType getT()
	{
		return t;
	}

	/**
	 * @param t
	 *            the t to set
	 */
	public void setT(NumericalCostType t)
	{
		this.t = t;
	}

	@Override
	public boolean isSatisfied(CKSpellCast cast)
	{
		int cp = 0;
		
		if(victim)
		{
			cp = cast.getActorTarget().getCyberPoints();
		}
		else
		{
			cp = getQuest().getActor(actor).getCyberPoints();
		}
			
		return NumericalCostType.evaluate(cp,t, cost);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Actor CP Satisfies [actor=" + actor
				+ ", cost=" + cost + ", t=" + t + "]";
	}

	static JPanel[] panel;
	static JComboBox<String>[] actorBox;
	static JComboBox<NumericalCostType>[] typeBox;
	static SpinnerNumberModel[] costSpinner;

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

			top[0].add(new JLabel("has CP "));
			top[1].add(new JLabel("has CP "));
			
			
			/*---*/

			JPanel[] bot = new JPanel[2];
			bot[0] = new JPanel();
			bot[1] = new JPanel();

			typeBox = new JComboBox[2];
			typeBox[0] = new JComboBox<NumericalCostType>(
					NumericalCostType.values());
			typeBox[1] = new JComboBox<NumericalCostType>(
					NumericalCostType.values());
			bot[0].add(typeBox[0]);
			bot[1].add(typeBox[1]);

			costSpinner = new SpinnerNumberModel[2];
			costSpinner[0] = new SpinnerNumberModel(1,0,1000,1);  
			costSpinner[1] = new SpinnerNumberModel(1,0,1000,1);  
					
			JSpinner spin = new JSpinner(costSpinner[0]);
			bot[0].add(spin);

			spin = new JSpinner(costSpinner[1]);
			bot[1].add(spin);

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
		typeBox[index].setSelectedItem(t);
		costSpinner[index].setValue(cost);
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
		cost = (Integer) costSpinner[EDIT].getValue();
		t = (NumericalCostType) typeBox[EDIT].getSelectedItem();
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

		root.add(new ActorCPSatisfies());
		frame.add(new CKTreeGui(root));
		// frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
