/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckSatisfies.NumericalCostType;

/**
 * @author dragonlord
 *
 */
public class CKMarkGridActor extends CKGameAction implements ItemListener
{
	private String chapter = "MOVE";
    private String page = "Forward";
	private int cost = 0;
	//private NumericalCostType t = NumericalCostType.FALSE;
	private String actorName="";
	
	private boolean useCast = true;
	private boolean pickActor = false;

	
	
	private static final long serialVersionUID = -1112825497910646743L;

	public CKMarkGridActor() {}


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
	 * @return the actorName
	 */
	public String getActorName()
	{
		return actorName;
	}


	/**
	 * @param actorName the actorName to set
	 */
	public void setActorName(String actorName)
	{
		this.actorName = actorName;
	}


	/**
	 * @return the useCast
	 */
	public boolean isUseCast()
	{
		return useCast;
	}


	/**
	 * @param useCast the useCast to set
	 */
	public void setUseCast(boolean useCast)
	{
		this.useCast = useCast;
	}


	/**
	 * @return the pickActor
	 */
	public boolean isPickActor()
	{
		return pickActor;
	}


	/**
	 * @param pickActor the pickActor to set
	 */
	public void setPickActor(boolean pickActor)
	{
		this.pickActor = pickActor;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Mark Target";
	}




	



	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		replaceListener(L);

		CKGridActor actor;
		
		if(pickActor)
		{
			actor = getQuest().getActor(actorName);
		}
		else
		{
			actor= cast.getActorTarget();
		}
		
		if(useCast)
		{
			chapter = cast.getPage();
			page = cast.getKey();
			cost = cast.getCp();
		}//else use the values set here.
		

		if(actor != null)
		{
			if(page.length()==0)
			{	
				actor.addChapter(chapter,cost);
			}
			else
			{
				actor.addPage(chapter,cost,page);
			}
		}
		notifyListener();
		
	}



	
	static JPanel []panel;
	static JComboBox<String> [] chapterBox;
	static JComboBox<String> [] pageBox;
	static JComboBox<String> []nameBox;
	static JComboBox<NumericalCostType> [] typeBox;
	static SpinnerNumberModel [] costSpinner;
	
	static JCheckBox [] useCastBox;
	static JCheckBox [] pickActorBox;
	

	
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
			
			
			pickActorBox = new JCheckBox[2];
			pickActorBox[0] = new JCheckBox("Pick Actor");
			pickActorBox[1] = new JCheckBox("Pick Actor");
			top[0].add(pickActorBox[0]);
			top[1].add(pickActorBox[1]);
			
			
			
			
			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			nameBox[1] = new JComboBox<String>();
			top[0].add(nameBox[0]);
			top[1].add(nameBox[1]);
			top[0].add(new JLabel("is marked with"));
			top[1].add(new JLabel("is marked with"));
			
	

			/*---*/
			
			
			JPanel []bot = new JPanel[2]; 
			bot[0] = new JPanel();
			bot[1] = new JPanel();
			
			
			useCastBox = new JCheckBox[2];
			useCastBox[0] = new JCheckBox("use cast");
			useCastBox[1] = new JCheckBox("use cast");
			bot[0].add(useCastBox[0]);
			bot[1].add(useCastBox[1]);
			
			
			chapterBox = new JComboBox[2];
			chapterBox[0] = new JComboBox<String>();
			chapterBox[1] = new JComboBox<String>();
			chapterBox[EDIT].setEditable(true);
			bot[0].add(chapterBox[0]);		
			bot[1].add(chapterBox[1]);		

			
			pageBox = new JComboBox[2];
			pageBox[0] = new JComboBox<String>();
			pageBox[1] = new JComboBox<String>();
			bot[0].add(pageBox[0]);		
			bot[1].add(pageBox[1]);	

					
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

		if(getQuest()!=null)
		{
			pickActorBox[index].setSelected(pickActor);
			actorName = initializeActorBox(nameBox[index],actorName);
			nameBox[index].setEnabled(pickActor);
		}
		else
		{
			pickActor=false;
			pickActorBox[index].setSelected(pickActor);
			pickActorBox[index].setEnabled(false);
			nameBox[index].addItem("Not Availible");
			nameBox[index].setEnabled(false);
		}
			

		useCastBox[index].setSelected(useCast);
		this.initializeEditableChapterBox(chapterBox[index], chapter);
		this.initializePageBox(pageBox[index], chapter,page);
		costSpinner[index].setValue(cost);
		
		chapterBox[index].setEnabled(!useCast);
		pageBox[index].setEnabled(!useCast);
		//can't enable a number model, will need to keep the spinner
		
	 }
	
	
	ItemListener Chapterlist = null;
	
	
	
	
	//protected void resetValues()
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		chapterBox[EDIT].removeItemListener(Chapterlist);
		pickActorBox[EDIT].removeItemListener(Chapterlist);
		useCastBox[EDIT].removeItemListener(Chapterlist);

		setPanelValues(EDIT);

		Chapterlist = this;
		chapterBox[EDIT].addItemListener(Chapterlist);
		pickActorBox[EDIT].addItemListener(Chapterlist);
		useCastBox[EDIT].addItemListener(Chapterlist);

		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		pickActor = pickActorBox[EDIT].isSelected();
		if(pickActor) //getQuest()!=null){
		{
			actorName = (String)nameBox[EDIT].getSelectedItem();
		}
		
		useCast = useCastBox[EDIT].isSelected();

		if(!useCast)
		{
			chapter = (String) chapterBox[EDIT].getEditor().getItem();
			page     = (String) pageBox[EDIT].getSelectedItem();
			cost =  (Integer) costSpinner[EDIT].getValue();
		}

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
	

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		//String temp2 = (String)chapterBox[EDIT].getEditor().getItem();
		//System.out.println(temp2);
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



	 public static void main(String[] args)
	 {
			JFrame frame = new JFrame("CyberKnight SAT Editor");
			CKGuiRoot root = new CKGuiRoot();
		
			
			//root.add(new BookSatisfies("Fire","bolt",13,NumericalCostType.EQ) );
			root.add(new CKMarkGridActor());
			frame.add(new CKTreeGui(root));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(600,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
	 }
}
