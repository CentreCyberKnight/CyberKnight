/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
@SuppressWarnings("serial")
public class CKReactiveSpellAction extends CKGameAction implements ItemListener
{
	
	CKSpellCast reactiveCast;
	int percent = 100;
	int stat = 1;
	boolean useCast;
	boolean useBook;
	boolean useStatic = true;
	boolean changeKey;
	boolean editSpell;
	boolean changeSource;
	boolean changeTarget;
	String sor ="hero";
	String tar ="";
	String key = "";
	String chapter = "*";
	String page = "*";
	String bookChapter = "*";
	String bookPage = "*";
	
	public CKReactiveSpellAction()
	{
		this(new CKSpellCast(null, null, "MOVE","*",1,""));
	}
	
	public CKReactiveSpellAction(CKSpellCast spell)
	{
		reactiveCast = spell;
		editSpell=true;
		changeKey=true;

	}
	
	public CKSpellCast getReactiveCast()
	{
		return reactiveCast;
	}
	
	public void setReactiveCast(CKSpellCast spell)
	{
		reactiveCast = spell;
	}
	
	
	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

	public boolean isUseCast() {
		return useCast;
	}

	public void setUseCast(boolean useCast) {
		this.useCast = useCast;
	}

	public boolean isUseBook() {
		return useBook;
	}

	public void setUseBook(boolean useBook) {
		this.useBook = useBook;
	}

	public boolean isUseStatic() {
		return useStatic;
	}

	public void setUseStatic(boolean useStatic) {
		this.useStatic = useStatic;
	}

	public boolean isChangeKey() {
		return changeKey;
	}

	public void setChangeKey(boolean changeKey) {
		this.changeKey = changeKey;
	}

	public boolean isEditSpell() {
		return editSpell;
	}

	public void setEditSpell(boolean editSpell) {
		this.editSpell = editSpell;
	}

	public boolean isChangeSource() {
		return changeSource;
	}

	public void setChangeSource(boolean changeSource) {
		this.changeSource = changeSource;
	}

	public boolean isChangeTarget() {
		return changeTarget;
	}

	public void setChangeTarget(boolean changeTarget) {
		this.changeTarget = changeTarget;
	}

	public String getSor() {
		return sor;
	}

	public void setSor(String sor) {
		this.sor = sor;
	}

	public String getTar() {
		return tar;
	}

	public void setTar(String tar) {
		this.tar = tar;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBookChapter() {
		return bookChapter;
	}

	public void setBookChapter(String bookChapter) {
		this.bookChapter = bookChapter;
	}

	public String getBookPage() {
		return bookPage;
	}

	public void setBookPage(String bookPage) {
		this.bookPage = bookPage;
	}



	/**
 * @return the chapter
 */
public String getChapter()
{
	return reactiveCast.getChapter();
}

/**
 * @param chapter the chapter to set
 */
public void setChapter(String chapter)
{
	reactiveCast.setChapter(chapter);
}

/**
 * @return the page
 */
public String getPage()
{
	return reactiveCast.getPage();
}

/**
 * @param page the page to set
 */
public void setPage(String page)
{
	reactiveCast.setPage(page);
}

/**
 * @return the cost
 */
public int getCost()
{
	return reactiveCast.getCp();
}

/**
 * @param cost the cost to set
 */
public void setCost(int cost)
{
	reactiveCast.setCp(cost);
}

	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Reactive spell:"+reactiveCast.getChapter() + reactiveCast.getPage() + reactiveCast.getCp()
				+ reactiveCast.getKey();
	}


	
//Add documentation
	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		//This c2 is the expected most frequent version, where the user chooses the chapter, page
		//key, the Cyber Points are a static number, and the target and source are flipped from the 
		//param cast
		//tl;dr The user picks everything except target/source, and cp is static
		CKSpellCast c2 = reactiveCast.lateBindCopy(cast.getSource(), cast.getItemTarget());

		replaceListener(L);
		if(useCast)
		{
			c2.setCp((cast.getCp()*percent)/100);
		}
		else if(useBook)
		{
			c2.setCp(cast.getPCSource().getAbilities().getChapter(bookChapter).getValue());
		}
		if(!changeKey)
		{
			c2.setKey(cast.getKey());
		}
		if(!editSpell)
		{
			c2.setChapter(cast.getChapter());
			c2.setPage(cast.getPage());
		}
		if(changeSource)
		{
			c2.setSource(getQuest().getActor(sor));
		}
		if(changeTarget)
		{
			c2.setTarget(getQuest().getActor(tar));
		}
		
		c2.castSpell();//??this);
		notifyListener();
	}




	

	
	static JPanel []panel;
	//static JComboBox []spell; //spell to be reacted to
	static JComboBox<String> []pageBox;
	static JComboBox<String> []chapterBox;
	static JComboBox<String> []pageBox2;
	static JComboBox<String> []chapterBox2;
	static SpinnerNumberModel [] costSpinner;
	
	static SpinnerNumberModel [] percSpinner;
	static JRadioButton [] useCastBox;
	static JRadioButton [] useBookBox;
	static JRadioButton [] useStaticBox;
	static JCheckBox [] useKeyBox;
	static JCheckBox [] editSpellBox;
	static JTextField [] text;
	static JCheckBox [] sourceBox;
	static JCheckBox [] targetBox;
	static JComboBox<String> [] sourceName;
	static JComboBox<String> [] targetName;

	
	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
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

			JPanel [] source = new JPanel[2];
			source[0] = new JPanel();
			source[1] = new JPanel();
			
			JPanel [] target = new JPanel[2];
			target[0] = new JPanel();
			target[1] = new JPanel();
			
			
			sourceBox = new JCheckBox[2];
			sourceBox[0] = new JCheckBox("Change Source");
			sourceBox[1] = new JCheckBox("Change Source");
			source[0].add(sourceBox[0]);
			source[1].add(sourceBox[1]);
			
			sourceName = new JComboBox[2];
			sourceName[0] = new JComboBox<String>();
			sourceName[1] = new JComboBox<String>();
			source[0].add(sourceName[0]);
			source[1].add(sourceName[1]);
			
			targetBox = new JCheckBox[2];
			targetBox[0] = new JCheckBox("Change Target");
			targetBox[1] = new JCheckBox("Change Target");
			target[0].add(targetBox[0]);
			target[1].add(targetBox[1]);
			
			targetName = new JComboBox[2];
			targetName[0] = new JComboBox<String>();
			targetName[1] = new JComboBox<String>();
			target[0].add(targetName[0]);
			target[1].add(targetName[1]);

			
			editSpellBox = new JCheckBox[2];
			editSpellBox[0] = new JCheckBox("Pick Spell Type");
			editSpellBox[1] = new JCheckBox("Pick Spell Type");
			top[0].add(editSpellBox[0]);
			top[1].add(editSpellBox[1]);


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
			
		
			//panel
			JPanel []book = new JPanel[2]; 
			book[0] = new JPanel();
			book[1] = new JPanel();
			
			useBookBox = new JRadioButton[2];
			useBookBox[0] = new JRadioButton("use book");
			useBookBox[1] = new JRadioButton("use book");
			book[0].add(useBookBox[0]);
			book[1].add(useBookBox[1]);
			
			chapterBox2 = new JComboBox[2];
			chapterBox2[0] = new JComboBox<String>();
			chapterBox2[1] = new JComboBox<String>();
			chapterBox2[EDIT].setEditable(true);
			book[0].add(chapterBox2[0]);
			book[1].add(chapterBox2[1]);

			
			pageBox2 = new JComboBox[2];
			pageBox2[0] = new JComboBox<String>();
			pageBox2[1] = new JComboBox<String>();
			pageBox2[EDIT].setEditable(true);
			book[0].add(pageBox2[0]);
			book[1].add(pageBox2[1]);
			
			//panel
			JPanel []stat = new JPanel[2]; 
			stat[0] = new JPanel();
			stat[1] = new JPanel();
			
			useStaticBox = new JRadioButton[2];
			useStaticBox[0] = new JRadioButton("Use Static");
			useStaticBox[1] = new JRadioButton("Use Static");
			stat[0].add(useStaticBox[0]);
			stat[1].add(useStaticBox[1]);

			
			
			
			
			

			
			JPanel []cast = new JPanel[2]; 
			cast[0] = new JPanel();
			cast[1] = new JPanel();
			
			//cast[0].setLayout(new BoxLayout(cast[0],BoxLayout.Y_AXIS));
			//cast[1].setLayout(new BoxLayout(cast[1],BoxLayout.Y_AXIS));
			
			TitledBorder title;
			title = BorderFactory.createTitledBorder("CP Amount");
			//cast[0].setBorder(title);
			//cast[1].setBorder(title);
			
			
			//panel
			JPanel [] buttons = new JPanel[2];
			buttons[0] = new JPanel();
			buttons[0].setLayout(new BoxLayout(buttons[0],BoxLayout.Y_AXIS));
			buttons[0].setBorder(title);
			buttons[1] = new JPanel();
			buttons[1].setLayout(new BoxLayout(buttons[1],BoxLayout.Y_AXIS));
			buttons[1].setBorder(title);
			buttons[0].add(cast[0]);
			buttons[1].add(cast[1]);
			buttons[0].add(book[0]);
			buttons[1].add(book[1]);
			buttons[0].add(stat[0]);
			buttons[1].add(stat[1]);

		
			
			

			
			
			
			costSpinner=new SpinnerNumberModel[2]; 
			costSpinner[0]=new SpinnerNumberModel(1, -1000, 1000,1);
			JSpinner spin = new JSpinner(costSpinner[0]);
			stat[0].add(spin);
			costSpinner[1]=new SpinnerNumberModel(1, -1000, 1000,1);
			spin = new JSpinner(costSpinner[1]);
			stat[1].add(spin);

			useCastBox = new JRadioButton[2];
			useCastBox[0] = new JRadioButton("use cast");
			useCastBox[1] = new JRadioButton("use cast");
			cast[0].add(useCastBox[0]);
			cast[1].add(useCastBox[1]);


			
			JLabel percent = new JLabel("percent:");
			JLabel percent2 = new JLabel("percent:");
			cast[0].add(percent);
			cast[1].add(percent2);
			
			percSpinner=new SpinnerNumberModel[2]; 
			percSpinner[0]=new SpinnerNumberModel(0, 0, 100,1);
			spin = new JSpinner(percSpinner[0]);
			cast[0].add(spin);
			percSpinner[1]=new SpinnerNumberModel(0, 0, 100,1);
			spin = new JSpinner(percSpinner[1]);
			cast[1].add(spin);

			
			
			
			ButtonGroup [] group = new ButtonGroup[2];
			group[0] = new ButtonGroup();
			group[1] = new ButtonGroup();
			group[0].add(useCastBox[0]);
			group[1].add(useCastBox[1]);
			group[0].add(useBookBox[0]);
			group[1].add(useBookBox[1]);
			group[0].add(useStaticBox[0]);
			group[1].add(useStaticBox[1]);

			
			
			
			
			JPanel []key = new JPanel[2];
			key[0] = new JPanel();
			key[1] = new JPanel();

			
			useKeyBox = new JCheckBox[2];
			useKeyBox[0] = new JCheckBox("change key");
			useKeyBox[1] = new JCheckBox("change key");
			key[0].add(useKeyBox[0]);
			key[1].add(useKeyBox[1]);

			
			text = new JTextField[2];
			text[0] = new JTextField(20);
			text[1] = new JTextField(20);
			key[0].add(text[0]);
			key[1].add(text[1]);
			
			panel[0].add(source[0]);
			panel[1].add(source[1]);
			panel[0].add(target[0]);
			panel[1].add(target[1]);
			panel[0].add(top[0]);
			panel[1].add(top[1]);
			panel[0].add(buttons[0]);
			panel[1].add(buttons[1]);
			panel[0].add(key[0]);
			panel[1].add(key[1]);
			
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
			sourceBox[index].setSelected(changeSource);
			sor = initializeActorBox(sourceName[index],sor);
			sourceName[index].setEnabled(true);
			
			targetBox[index].setSelected(changeTarget);
			tar = initializeActorBox(targetName[index],tar);
			targetName[index].setEnabled(true);
		}
		else
		{
			changeSource=false;
			sourceBox[index].setSelected(changeSource);
			sourceBox[index].setEnabled(false);
			sourceName[index].addItem("Not Availible");
			sourceName[index].setEnabled(false);
			
			changeTarget=false;
			targetBox[index].setSelected(changeTarget);
			targetBox[index].setEnabled(false);
			targetName[index].addItem("Not Availible");
			targetName[index].setEnabled(false);
		}
		
		if(editSpell)
		{
			chapterBox[index].setEnabled(true);
			pageBox[index].setEnabled(true);

		}
		else
		{
			chapterBox[index].setEnabled(false);
			pageBox[index].setEnabled(false);
		}
		if(useBook)
		{
			chapterBox2[index].setEnabled(true);
			pageBox2[index].setEnabled(true);
		}
		else
		{
			chapterBox2[index].setEnabled(false);
			pageBox2[index].setEnabled(false);
		}
		
		useCastBox[index].setSelected(useCast);
		useBookBox[index].setSelected(useBook);
		useStaticBox[index].setSelected(useStatic);
		useKeyBox[index].setSelected(changeKey);
		editSpellBox[index].setSelected(editSpell);
		sourceBox[index].setSelected(changeSource);
		targetBox[index].setSelected(changeTarget);


		
		this.initializeEditableChapterBox(chapterBox[index], reactiveCast.getChapter());
		this.initializeEditablePageBox(pageBox[index], reactiveCast.getChapter(),reactiveCast.getPage());
		this.initializeEditableChapterBox(chapterBox2[index], bookChapter);
		this.initializeEditablePageBox(pageBox2[index], bookChapter,bookPage);
		costSpinner[index].setValue(stat);
		percSpinner[index].setValue(percent);
		//percSpinner[index].setValue(percSpinner);
		text[index].setText(reactiveCast.getKey());


	}
	
	static ItemListener Chapterlist = null;
	// (non-Javadoc)
	// * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		chapterBox[EDIT].removeItemListener(Chapterlist);
		chapterBox2[EDIT].removeItemListener(Chapterlist);
		useBookBox[EDIT].removeItemListener(Chapterlist);
		editSpellBox[EDIT].removeItemListener(Chapterlist);
		sourceBox[EDIT].removeItemListener(Chapterlist);
		targetBox[EDIT].removeItemListener(Chapterlist);
		
		
		setPanelValues(EDIT);
		Chapterlist = this;
		chapterBox[EDIT].addItemListener(Chapterlist);
		chapterBox2[EDIT].addItemListener(Chapterlist);
		useBookBox[EDIT].addItemListener(Chapterlist);
		editSpellBox[EDIT].addItemListener(Chapterlist);
		sourceBox[EDIT].addItemListener(Chapterlist);
		targetBox[EDIT].addItemListener(Chapterlist);

		return panel[EDIT];	
	}

	// (non-Javadoc)
	// * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	
	
	@Override
	public void storeComponentValues()
	{
		useCast = useCastBox[EDIT].isSelected();
		useBook = useBookBox[EDIT].isSelected();
		useStatic = useStaticBox[EDIT].isSelected();
		changeKey = useKeyBox[EDIT].isSelected();
		editSpell = editSpellBox[EDIT].isSelected();
		changeSource = sourceBox[EDIT].isSelected();
		changeTarget = targetBox[EDIT].isSelected();
		percent = (Integer)percSpinner[EDIT].getValue();
		stat = (Integer)costSpinner[EDIT].getValue();

		
		if(editSpell)
		{
			reactiveCast.setChapter((String) chapterBox[EDIT].getEditor().getItem());
			reactiveCast.setPage((String) pageBox[EDIT].getEditor().getItem());
		}
		if(useStatic)
		{
			reactiveCast.setCp((Integer) costSpinner[EDIT].getValue());
		}
		else if(useCast)
		{
			reactiveCast.setCp((reactiveCast.getCp()*percent)/100);
		}
		if(changeKey)
		{
			key = (String) text[EDIT].getText();
			reactiveCast.setKey(key);
		}
		
		if(changeSource) //getQuest()!=null){
		{
			sor= (String)sourceName[EDIT].getSelectedItem();
		}
		if(changeTarget) //getQuest()!=null){
		{
			tar = (String)targetName[EDIT].getSelectedItem();
		}		

		if(useBook)
		{
			chapter = (String) chapterBox2[EDIT].getEditor().getItem();
			page     = (String) pageBox2[EDIT].getSelectedItem();
		}
		
		if(useBook)
		{
			bookChapter = (String) chapterBox2[EDIT].getEditor().getItem();
			bookPage     = (String) pageBox2[EDIT].getSelectedItem();
		}

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
			else if(e.getSource()==chapterBox2[EDIT])
			{//a little sneaky here...want to make sure it does not store a page value
				//safe to alter this since the page box does not have a listener
				String temp = "*";
				pageBox2[EDIT].addItem(temp);						
				pageBox2[EDIT].setSelectedItem(temp);
			}
			
			storeComponentValues();
			//bogus values to force a redraw-don't care about the return value.
			getTreeCellEditorComponent(null,null,true,true,false,0);
		}
	}
}
