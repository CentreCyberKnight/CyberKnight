package ckSatisfies;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
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
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.QuestData;
import static ckCommonUtils.CKPropertyStrings.*;

public class ContestedActionSatisfies extends Satisfies
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3642344006960003239L;
	private String targetChapter;
	private String targetPage;
	private String sourceChapter;
	private String sourcePage;
	private double base;

	public ContestedActionSatisfies(String tChapter,
			String sChapter, double base)
	{
		super();
		this.targetChapter = tChapter;

		this.sourceChapter = sChapter;

		this.base = base;
	}

	public ContestedActionSatisfies()
	{
		this( CH_EVADE,  CH_ACCURACY, 70);
	}

	/**
	 * @return the targetChapter
	 */
	public String getTargetChapter()
	{
		return targetChapter;
	}

	/**
	 * @param targetChapter
	 *            the targetChapter to set
	 */
	public void setTargetChapter(String targetChapter)
	{
		this.targetChapter = targetChapter;
	}

	

	/**
	 * @return the sourceChapter
	 */
	public String getSourceChapter()
	{
		return sourceChapter;
	}

	/**
	 * @param sourceChapter
	 *            the sourceChapter to set
	 */
	public void setSourceChapter(String sourceChapter)
	{
		this.sourceChapter = sourceChapter;
	}


	/**
	 * @return the base
	 */
	public double getBase()
	{
		return base;
	}

	/**
	 * @param base
	 *            the base to set
	 */
	public void setBase(double base)
	{
		this.base = base;
	}

	@Override
	public boolean isSatisfied(CKSpellCast cast)
	{
		int tval = 0;
		int sval = 0;
		
/*		if(cast.getActorTarget()==null)
		{
			
		}
		CKBook tBook = cast.getActorTarget().getAbilities();
		if(tBook != null)
		{

		}
	*/	try
		{
			CKBook tBook = ((CKGridActor)cast.getItemTarget()).getAbilities();
			tval = tBook.getChapter(targetChapter).getValue();			
			
		}
		catch (ClassCastException e ) { }
		
			
		try
		{ 
			CKBook sBook = ((CKGridActor) cast.getSource()).getAbilities();
			sval = sBook.getChapter(sourceChapter).getValue();
			
		}
		catch (ClassCastException e ) { }
		
		double base2 = base + tval - sval;
		
		QuestData data = getQuest();
		return data.getRandom() <base2;
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ContestedActionSatisfies [tChapter=" + targetChapter +
				", sChapter=" + sourceChapter
				+ ", base=" + base + "]";
	}

	static JPanel[] panel;
	static JComboBox<String>[] sourceChapterBox;
	static JComboBox<String>[] targetChapterBox;
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

			panel[0].setBorder(BorderFactory.createTitledBorder("Contested Action"));
			panel[1].setBorder(BorderFactory.createTitledBorder("Contested Action"));
			
			
			JPanel[] sky = new JPanel[2];
			sky[0] = new JPanel();
			sky[1] = new JPanel();

			sky[0].add(new JLabel("Base Success"));
			sky[1].add(new JLabel("Base Success"));
			
			baseSpinner = new SpinnerNumberModel[2];
			baseSpinner = generateNumberModels();

			JSpinner spin = new JSpinner(baseSpinner[0]);
			sky[0].add(spin);

			spin = new JSpinner(baseSpinner[1]);
			sky[1].add(spin); //ok since I don't have to keep the reference

			JPanel[] top = new JPanel[2];
			top[0] = new JPanel();
			top[1] = new JPanel();

			top[0].add(new JLabel("Add "));
			top[1].add(new JLabel("Add "));

			sourceChapterBox = new JComboBox[2];
			sourceChapterBox[0] = new JComboBox<String>();
			sourceChapterBox[1] = new JComboBox<String>();
			sourceChapterBox[EDIT].setEditable(true);
			top[0].add(sourceChapterBox[0]);
			top[1].add(sourceChapterBox[1]);

			/*---*/
			JPanel[] bot = new JPanel[2];
			bot[0] = new JPanel();
			bot[1] = new JPanel();

			bot[0].add(new JLabel("Sub "));
			bot[1].add(new JLabel("Sub "));

			targetChapterBox = new JComboBox[2];
			targetChapterBox[0] = new JComboBox<String>();
			targetChapterBox[1] = new JComboBox<String>();
			targetChapterBox[EDIT].setEditable(true);
			bot[0].add(targetChapterBox[0]);
			bot[1].add(targetChapterBox[1]);

			
			panel[0].add(sky[0]);
			panel[1].add(sky[1]);
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

		this.initializeEditableChapterBox(targetChapterBox[index], targetChapter);
		this.initializeEditableChapterBox(sourceChapterBox[index], sourceChapter);

		baseSpinner[index].setValue(base);
	}

	static ItemListener Chapterlist = null;

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
		//targetChapterBox[EDIT].removeItemListener(Chapterlist);
		//sourceChapterBox[EDIT].removeItemListener(Chapterlist);

		setPanelValues(EDIT);

		//Chapterlist = this;
		//targetChapterBox[EDIT].addItemListener(Chapterlist);
		//pickActorBox[EDIT].addItemListener(Chapterlist);

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
		// chapter = (String) chapterBox[EDIT].getSelectedItem();
		targetChapter = (String) targetChapterBox[EDIT].getEditor().getItem();
		sourceChapter = (String) sourceChapterBox[EDIT].getEditor().getItem();

		base =  (double) baseSpinner[EDIT].getValue();

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

		root.add(new ContestedActionSatisfies());//"Fire", "bolt", 13));
		frame.add(new CKTreeGui(root));
		// frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
