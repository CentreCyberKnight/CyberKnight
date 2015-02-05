package ckEditor.treegui;

import ckCommonUtils.LogListener;
import ckGameEngine.QuestData;
import ckGraphicsEngine.CKGraphicsEngine;

public abstract class ScriptNode extends CKGUINode implements LogListener,Comparable<ScriptNode>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7073637163804608596L;
	private String name = "";
	private int priority = 0;
	private String script="";
	
	
	public ScriptNode()
	{
		
	}
	
	public ScriptNode(String name, int priority,String script)
	{
		this.name=name;
		this.priority=priority;
		this.script=script;
		
	}
	
	public abstract void initScript(QuestData quest, CKGraphicsEngine engine);
	
	public abstract void createScript(QuestData quest, CKGraphicsEngine engine);
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
	}
	/**
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority)
	{
		this.priority = priority;
	}
	/**
	 * @return the script
	 */
	public String getScript()
	{
		return script;
	}
	/**
	 * @param script the script to set
	 */
	public void setScript(String script)
	{
		this.script = script;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see ckCommonUtils.LogListener#addText(java.lang.String)
	 */
	@Override
	public void addText(String t)
	{
		script = script+t;
	}


	@Override
	public int compareTo(ScriptNode o)
	{
		//return getPriority() - o.getPriority();
		return o.getPriority() - getPriority();
	}
/*

	private static JPanel []panel;
	private static JTextField[] nameFields;
	private static SpinnerNumberModel[] priorityBox;
	private static JTextArea[] scriptBox;

	

	
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.PAGE_AXIS));
			panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.PAGE_AXIS));

			panel[0].setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
			panel[1].setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
			
			JPanel []tops=new JPanel[2];
			tops[0]=new JPanel();
			tops[1]=new JPanel();
			
			tops[0].setLayout(new BoxLayout(tops[0], BoxLayout.LINE_AXIS));
			tops[0].setBackground(colors[0]);
			tops[1].setBackground(colors[1]);
			tops[1].setLayout(new BoxLayout(tops[1], BoxLayout.LINE_AXIS));
			
			
			//name
			nameFields = new JTextField[2];
			nameFields[0]=new JTextField(15);
			nameFields[1]=new JTextField(15);
			tops[0].add(nameFields[0]);
			tops[1].add(nameFields[1]);

			tops[0].add(new JLabel(" w/ priority "));
			tops[1].add(new JLabel(" w/ priority "));
			

			
			priorityBox=new SpinnerNumberModel[2]; 
			priorityBox[0] = new SpinnerNumberModel(1, 0, 100,1);
			priorityBox[1] = new SpinnerNumberModel(1, 0, 100,1);
			JSpinner spin = new JSpinner(priorityBox[0]);
			tops[0].add(spin);
			spin = new JSpinner(priorityBox[1]);
			tops[1].add(spin);
			
			panel[0].add(tops[0]);
			panel[1].add(tops[1]);
		


			JPanel []bot=new JPanel[2];
			bot[0]=new JPanel();
			bot[1]=new JPanel();
			
			bot[0].setLayout(new BoxLayout(bot[0], BoxLayout.LINE_AXIS));
			bot[1].setLayout(new BoxLayout(bot[1], BoxLayout.LINE_AXIS));
			bot[0].setBackground(colors[0]);
			bot[1].setBackground(colors[1]);

			
			//starts at 			
			
			scriptBox = new JTextArea[2];
			scriptBox[0] = new JTextArea(3,15);
			scriptBox[1] = new JTextArea(3,15);

			
			bot[0].add(scriptBox[0]);
			bot[1].add(scriptBox[1]);
			panel[0].add(bot[0]);
			panel[1].add(bot[1]);
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
		priorityBox[index].setValue(priority);
		scriptBox[index].setText(script);
		
		panel[index].validate();
	}
	*/
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	/*@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}
*/
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	/*@Override
	public void storeComponentValues()
	{
		
		name = nameFields[EDIT].getText();
		priority = (Integer) priorityBox[EDIT].getNumber();
		script = scriptBox[EDIT].getText();
			
	}
*/
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
/*	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}
	*/
	
	
}
