package ckSatisfies;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import ckCommonUtils.CKPosition;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;


public class PositionReachedSatisfies extends Satisfies {


	private static final long serialVersionUID = -1828314511354415387L;

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		
		return new PositionReachedSatisfies(pc,(CKPosition) pos.clone());
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
	 * @return the pc
	 */
	public String getPc()
	{
		return pc;
	}
	/**
	 * @param pc the pc to set
	 */
	public void setPc(String pc)
	{
		this.pc = pc;
	}





	String pc;
	CKPosition pos;
	
	public PositionReachedSatisfies(String pc, CKPosition pos)
	{
		super();
		this.pc = pc;
		this.pos = pos;
	}
	public PositionReachedSatisfies()
	{
		this("HERO",new CKPosition());
	}
	
	@Override
	public boolean isSatisfied(CKSpellCast cast) 
	{//
		//TODO if you walk over the position does not trigger with current implemenation
	//	System.out.println("character:"+pos);
	//	System.out.println("character:"+pc+" "+getPC(pc).getPos());
		CKGridActor tar=null;
		if(cast != null) 	{ tar = cast.getActorTarget();}
		else						{ tar = getPC(pc); } 
		
		
		if(pos.equals(tar.getPos()) )
		{
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Satstisfied when " + pc + " reaches " + pos ;
	}
	/*
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Actor:"));
		SpinnerListModel model = new SpinnerListModel(getQuest().getActors().getActorNames());
		model.setValue(pc);
		JSpinner spinN = new JSpinner(model);
		((JSpinner.DefaultEditor)spinN.getEditor()).getTextField().setColumns(16);
		panel.add(spinN);
		menu.add(panel);	
		
		panel = new JPanel();
		panel.add(new JLabel("X Pos:"));
		SpinnerNumberModel modelX = new SpinnerNumberModel(pos.getX(), 0, 1000,1);
		JSpinner spin = new JSpinner(modelX);
		panel.add(spin);
		menu.add(panel);	

		panel = new JPanel();
		panel.add(new JLabel("Y Pos:"));
		SpinnerNumberModel modelY = new SpinnerNumberModel(pos.getY(), 0, 1000,1);
		spin = new JSpinner(modelY);
		panel.add(spin);
		menu.add(panel);	

		panel = new JPanel();
		panel.add(new JLabel("Z Pos:"));
		SpinnerNumberModel modelZ = new SpinnerNumberModel(pos.getZ(), 0, 1000,1);
		spin = new JSpinner(modelZ);
		panel.add(spin);
		menu.add(panel);	

		panel = new JPanel();
		panel.add(new JLabel("Depth:"));
		SpinnerNumberModel modelDepth = new SpinnerNumberModel(pos.getDepth(), 0, 1000,1);
		spin = new JSpinner(modelDepth);
		panel.add(spin);
		menu.add(panel);	

		
		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(model,modelX,modelY,modelZ,modelDepth));
		menu.add(store);
		
		return menu;
	}
	
	*/
	


	class EditAction  implements ActionListener
	{
		SpinnerNumberModel x;
		SpinnerNumberModel y;
		SpinnerNumberModel z;
		SpinnerNumberModel depth;
		SpinnerListModel text;
		
		public EditAction(SpinnerListModel model,SpinnerNumberModel xm,SpinnerNumberModel ym,
				SpinnerNumberModel zm,SpinnerNumberModel dm)
		{
			text=model;
			x=xm;
			y=ym;
			z=zm;
			depth=dm;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			pos.setX(x.getNumber().intValue());
			pos.setY(y.getNumber().intValue());
			pos.setZ(z.getNumber().intValue());
			pos.setDepth(depth.getNumber().intValue());
			pc=(String) text.getValue();
		}
	}
	
	
	static JPanel []panel;
	static JComboBox<String> []nameBox;
	static SpinnerNumberModel [][] position; //x and y pos
	
	@SuppressWarnings("unchecked")
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].add(new JLabel("When "));
			panel[1].add(new JLabel("When "));
			
			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			nameBox[1] = new JComboBox<String>();
			panel[0].add(nameBox[0]);		
			panel[1].add(nameBox[1]);		
			
			panel[0].add(new JLabel("Reaches"));
			panel[1].add(new JLabel("Reaches"));

			position=new SpinnerNumberModel[2][2]; 
			position[0] = generatePositionModels();
			position[1] = generatePositionModels();
			
			for(int i =0;i<2;i++)
			{
				for(SpinnerNumberModel s:position[i])
				{
					JSpinner spin = new JSpinner(s);
					panel[i].add(spin);
				}
			}
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
		pc = initializeActorBox(nameBox[index],pc);
		}
		initializePositionModels(position[index],pos);		
		
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		pc = (String)nameBox[EDIT].getSelectedItem();
		readPositionModels(position[EDIT],pos);
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
	
	
}
