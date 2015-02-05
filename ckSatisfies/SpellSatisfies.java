package ckSatisfies;

import java.awt.Color;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

import static ckCommonUtils.CKPropertyStrings.*;

public class SpellSatisfies extends Satisfies implements ItemListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3642344006960003239L;
	private String chapter="MOVE";
    private String page="forward";
	private int cost;
	private NumericalCostType t;
	

	public SpellSatisfies(String chapter, String page, int cost,
			NumericalCostType t)
	{
		super();
		this.chapter = chapter;
		this.page = page;
		this.cost = cost;
		this.t = t;
	}

public SpellSatisfies()
{
	this("MOVE","forward",0,NumericalCostType.FALSE);
}


	
	
	
	
	
	/**
 * @return the chapter
 */
public String getChapter()
{
	return chapter;
}

/**
 * @param chapter the chapter to set
 */
public void setChapter(String chapter)
{
	this.chapter = chapter;
}

/**
 * @return the page
 */
public String getPage()
{
	return page;
}

/**
 * @param page the page to set
 */
public void setPage(String page)
{
	this.page = page;
}

/**
 * @return the cost
 */
public int getCost()
{
	return cost;
}

/**
 * @param cost the cost to set
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
 * @param t the t to set
 */
public void setT(NumericalCostType t)
{
	this.t = t;
}

	@Override
	public boolean isSatisfied(CKSpellCast cast)
	{
		return(
				    (    page.compareTo(P_ANY)==0 ||
				         page.compareToIgnoreCase(cast.getPage())==0 
				     )
				     &&
				     (
				    	chapter.compareToIgnoreCase(cast.getChapter())==0 ||
				    	chapter.compareTo(P_ANY)==0
				     )
				     &&
				     NumericalCostType.evaluate(cast.getCp(), t, cost) 
			  );
	}

	
	



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "SpellSatisfies [chapter=" + chapter + ", page=" + page
				+ ", cost=" + cost + ", t=" + t + "]";
	}






	static JPanel []panel;
	static JComboBox<String> [] chapterBox;
	static JComboBox<String> [] pageBox;
	
	static JComboBox<NumericalCostType> [] typeBox;
	static SpinnerNumberModel [] costSpinner;
	

	@SuppressWarnings("unchecked")
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.Y_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.Y_AXIS));

			
			JPanel []top = new JPanel[2]; 
			top[0] = new JPanel();
			top[1] = new JPanel();
			
			
			top[0].add(new JLabel("When "));
			top[1].add(new JLabel("When "));
			
			chapterBox = new JComboBox[2];
			chapterBox[0] = new JComboBox<String>();
			chapterBox[1] = new JComboBox<String>();
			chapterBox[EDIT].setEditable(true);
			top[0].add(chapterBox[0]);		
			top[1].add(chapterBox[1]);		

			
			pageBox = new JComboBox[2];
			pageBox[0] = new JComboBox<String>();
			pageBox[1] = new JComboBox<String>();
			pageBox[EDIT].setEditable(true);
			top[0].add(pageBox[0]);		
			top[1].add(pageBox[1]);		

			/*---*/
			
			
			JPanel []bot = new JPanel[2]; 
			bot[0] = new JPanel();
			bot[1] = new JPanel();

			
			typeBox = new JComboBox[2];
			typeBox[0] = new JComboBox<NumericalCostType>(NumericalCostType.values());
			typeBox[1] = new JComboBox<NumericalCostType>(NumericalCostType.values());
			bot[0].add(typeBox[0]);		
			bot[1].add(typeBox[1]);		

			
			costSpinner=new SpinnerNumberModel[2]; 
			costSpinner = generateNumberModels();

			
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
	
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);
		
		this.initializeEditableChapterBox(chapterBox[index], chapter);
		this.initializeEditablePageBox(pageBox[index], chapter,page);
		typeBox[index].setSelectedItem(t);
		costSpinner[index].setValue(cost);
	 }
	
	
	static ItemListener Chapterlist = null;
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		chapterBox[EDIT].removeItemListener(Chapterlist);
		setPanelValues(EDIT);
		Chapterlist = this;
		chapterBox[EDIT].addItemListener(Chapterlist);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		chapter = (String) chapterBox[EDIT].getEditor().getItem();
		page     = (String) pageBox[EDIT].getEditor().getItem();
		cost =  (Integer) costSpinner[EDIT].getValue();
		t =(NumericalCostType) typeBox[EDIT].getSelectedItem();
		//System.out.println("Stored"+this);
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
			JFrame frame = new JFrame("CyberKnight SAT Editor");
			CKGuiRoot root = new CKGuiRoot();
		
			
			root.add(new SpellSatisfies("Move","forward",13,NumericalCostType.EQ) );
			frame.add(new CKTreeGui(root));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(600,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
	 }

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if(e.getID()==ItemEvent.ITEM_STATE_CHANGED)
		{
			//System.out.println("-------------- with Change");

			if(e.getSource()==chapterBox[EDIT])
			{//a little sneaky here...want to make sure it does not store a page value
				//safe to alter this since the page box does not have a listener
				String temp = "*";
				pageBox[EDIT].addItem(temp);						
				pageBox[EDIT].setSelectedItem(temp);
			}
			
			storeComponentValues();
			//bogus values to force a redraw-don't care about the return value.
			getTreeCellEditorComponent(null,null,true,true,false,0);
		}
		
	

	}
}
