/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
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
import ckGameEngine.CKBook;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.CKSpellResult;
import ckSatisfies.ContestedActionSatisfies;
import static ckCommonUtils.CKPropertyStrings.*;


/**
 * @author dragonlord
 *
 */
@SuppressWarnings("serial")
public class CKContestedAlterCP extends CKGameAction
{
	
	String maxDamage;
	String defense;
	String defense_Effectiveness;
	String attackBonus;
	
	double multiplier = -3;
	Boolean randomize=false;
	
	
	
	
	
	
	public CKContestedAlterCP(String maxDamage, String defense,
			String defense_Effectiveness, 
			String attackBonus,
			double multiplier, Boolean randomize)
	{
		super();
		this.maxDamage = maxDamage;
		this.defense = defense;
		this.defense_Effectiveness = defense_Effectiveness;
		this.attackBonus =attackBonus; 
		this.multiplier = multiplier;
		this.randomize = randomize;
	}
	
	public CKContestedAlterCP()
	{
		this(MAX_DAMAGE,CH_DEFENSE,CH_DEFENSE_EFFECTIVENESS,CH_ATTACK_BONUS,3,false);
	}

	
	

	
	/**
	 * @return the maxDamage
	 */
	public String getMaxDamage()
	{
		return maxDamage;
	}

	/**
	 * @param maxDamage the maxDamage to set
	 */
	public void setMaxDamage(String maxDamage)
	{
		this.maxDamage = maxDamage;
	}

	/**
	 * @return the defense
	 */
	public String getDefense()
	{
		return defense;
	}

	/**
	 * @param defense the defense to set
	 */
	public void setDefense(String defense)
	{
		this.defense = defense;
	}

	/**
	 * @return the defense_Effectiveness
	 */
	public String getDefense_Effectiveness()
	{
		return defense_Effectiveness;
	}

	/**
	 * @param defense_Effectiveness the defense_Effectiveness to set
	 */
	public void setDefense_Effectiveness(String defense_Effectiveness)
	{
		this.defense_Effectiveness = defense_Effectiveness;
	}

	/**
	 * @return the attackBonus
	 */
	public String getAttackBonus()
	{
		return attackBonus;
	}

	/**
	 * @param attackBonus the attackBonus to set
	 */
	public void setAttackBonus(String attackBonus)
	{
		this.attackBonus = attackBonus;
	}

	/**
	 * @return the multiplier
	 */
	public double getMultiplier()
	{
		return multiplier;
	}

	/**
	 * @param multiplier the multiplier to set
	 */
	public void setMultiplier(double multiplier)
	{
		this.multiplier = multiplier;
	}

	/**
	 * @return the randomize
	 */
	public Boolean getRandomize()
	{
		return randomize;
	}

	/**
	 * @param randomize the randomize to set
	 */
	public void setRandomize(Boolean randomize)
	{
		this.randomize = randomize;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Contested Change CP:";
	}


	
//Add documentation
	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		int def =0;
		double max = 100.0;
		double portion = 1.0;
		int attack=0;
		
		
		try
		{ 
			CKBook tBook = ((CKGridActor) cast.getItemTarget()).getAbilities();
			def = tBook.getChapter(this.defense).getValue();
			portion = tBook.getChapter(this.defense_Effectiveness).getValue()/100.0;
			max = (double) tBook.getChapter(this.maxDamage).getValue();
		}
		catch (ClassCastException e ) { }
		
		
		try
		{ 
			CKBook sBook = ((CKGridActor) cast.getSource()).getAbilities();
			attack= sBook.getChapter(this.attackBonus).getValue();
			
		}
		catch (ClassCastException e ) { }
		
		
		//now for math
		double a = (1-portion-def/max);
		double b = portion-a*def;
		int cp = cast.getCp();
		double base = a*cp*cp+b*cp;
		//TODO work with randomization?		
		int damage = - (int) Math.ceil(base* getMultiplier()+attack);

		//TODO graphical printout of this effect...
		
			
		if(! CKGameObjectsFacade.isPrediction())
			System.err.println("         altering "+cast.getItemTarget().getName()+" CP by:"+damage);
		CKGridActor actor = cast.getActorTarget();
		if(actor != null)
		{
			actor.setCyberPoints(actor.getCyberPoints() +damage);
			cast.addResult(actor, cast.getPage(), CKSpellResult.DAMAGE, damage);
		}
		else
		{
			cast.addResult(cast.getItemTarget(), cast.getPage(), CKSpellResult.DAMAGE, 0);
		}
		
		notifyListener();
	}




	

	
	static JPanel []panel;
	//static JComboBox []spell; //spell to be reacted to
	static JComboBox<String>[] defenseChapterBox;
	static JComboBox<String>[] defEffChapterBox;
	static JComboBox<String>[] attackBonusChapterBox;
	static JComboBox<String>[] maxChapterBox;


	static SpinnerNumberModel[] baseSpinner;	

	
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

			panel[0].setBorder(BorderFactory.createTitledBorder("Contested Alter CP"));
			panel[1].setBorder(BorderFactory.createTitledBorder("Contested Alter CP"));
			
			
			JPanel[] sky = new JPanel[2];
			sky[0] = new JPanel();
			sky[1] = new JPanel();

			sky[0].add(new JLabel("Damage Multiplier"));
			sky[1].add(new JLabel("Damage Multiplier"));
			
			baseSpinner = new SpinnerNumberModel[2];
			baseSpinner = this.generateDoubleNumberModels();

			JSpinner spin = new JSpinner(baseSpinner[0]);
			sky[0].add(spin);

			spin = new JSpinner(baseSpinner[1]);
			sky[1].add(spin); //ok since I don't have to keep the reference

			JPanel[] a = new JPanel[2];
			a[0] = new JPanel();
			a[1] = new JPanel();

			a[0].add(new JLabel("Defense "));
			a[1].add(new JLabel("Defense "));

			defenseChapterBox = new JComboBox[2];
			defenseChapterBox[0] = new JComboBox<String>();
			defenseChapterBox[1] = new JComboBox<String>();
			defenseChapterBox[EDIT].setEditable(true);
			a[0].add(defenseChapterBox[0]);
			a[1].add(defenseChapterBox[1]);

			/*---*/
			JPanel[] b = new JPanel[2];
			b[0] = new JPanel();
			b[1] = new JPanel();

			b[0].add(new JLabel("Defense Effectivness "));
			b[1].add(new JLabel("Defense Effectivness "));

			defEffChapterBox = new JComboBox[2];
			defEffChapterBox[0] = new JComboBox<String>();
			defEffChapterBox[1] = new JComboBox<String>();
			defEffChapterBox[EDIT].setEditable(true);
			b[0].add(defEffChapterBox[0]);
			b[1].add(defEffChapterBox[1]);

			
		//----------	
			
			
			JPanel[] top = new JPanel[2];
			top[0] = new JPanel();
			top[1] = new JPanel();

			top[0].add(new JLabel("Attack Bonus "));
			top[1].add(new JLabel("Attack Bonus "));

			attackBonusChapterBox = new JComboBox[2];
			attackBonusChapterBox[0] = new JComboBox<String>();
			attackBonusChapterBox[1] = new JComboBox<String>();
			attackBonusChapterBox[EDIT].setEditable(true);
			top[0].add(attackBonusChapterBox[0]);
			top[1].add(attackBonusChapterBox[1]);

			/*---*/
			JPanel[] bot = new JPanel[2];
			bot[0] = new JPanel();
			bot[1] = new JPanel();

			bot[0].add(new JLabel("Max Damage "));
			bot[1].add(new JLabel("Max Damage "));

			maxChapterBox = new JComboBox[2];
			maxChapterBox[0] = new JComboBox<String>();
			maxChapterBox[1] = new JComboBox<String>();
			maxChapterBox[EDIT].setEditable(true);
			bot[0].add(maxChapterBox[0]);
			bot[1].add(maxChapterBox[1]);

			
			
			
			panel[0].add(sky[0]);
			panel[1].add(sky[1]);
			panel[0].add(a[0]);
			panel[1].add(a[1]);
			panel[0].add(b[0]);
			panel[1].add(b[1]);
			panel[0].add(top[0]);
			panel[1].add(top[1]);
			panel[0].add(bot[0]);
			panel[1].add(bot[1]);

		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);


		
		this.initializeEditableChapterBox(defenseChapterBox[index], defense);
		this.initializeEditableChapterBox(defEffChapterBox[index], this.defense_Effectiveness);
		this.initializeEditableChapterBox(attackBonusChapterBox[index], this.attackBonus);
		this.initializeEditableChapterBox(maxChapterBox[index],this.maxDamage);
		baseSpinner[index].setValue(multiplier);

	}
	
	static ItemListener Chapterlist = null;
	// (non-Javadoc)
	// * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
	
		return panel[EDIT];	
	}

	// (non-Javadoc)
	// * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	
	
	@Override
	public void storeComponentValues()
	{
		
		defense 					= (String) defenseChapterBox[EDIT].getEditor().getItem();
		this.defense_Effectiveness 	= (String) defEffChapterBox[EDIT].getEditor().getItem();
		attackBonus 				= (String) attackBonusChapterBox[EDIT].getEditor().getItem();
		this.maxDamage			 	= (String) maxChapterBox[EDIT].getEditor().getItem();
		
		this.multiplier =  (double) baseSpinner[EDIT].getValue();
		
	}
	

	// (non-Javadoc)
	 //* @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 
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

		root.add(new CKContestedAlterCP());//"Fire", "bolt", 13));
		frame.add(new CKTreeGui(root));
		// frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	
	
	
	
	
	
	
}
