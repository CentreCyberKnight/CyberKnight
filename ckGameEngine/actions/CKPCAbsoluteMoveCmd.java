/**
 * 
 */
package ckGameEngine.actions;

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

import ckCommonUtils.CKGridPosition;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.CKGameObjectsFacade;

/**
 * @author dragonlord
 *
 */
public class CKPCAbsoluteMoveCmd extends CKQuestAction
{
	


	
	private static final long serialVersionUID = 6459487720903022323L;
	
	String name;
	static CKGridPosition pos;
	int xVal;
	int yVal;
	int zVal;
	
	public CKPCAbsoluteMoveCmd()
	{
		this("HERO",new CKGridPosition());
	}
	
	public CKPCAbsoluteMoveCmd(String name,CKGridPosition pos)
	{
		this.name = name;
		CKPCAbsoluteMoveCmd.pos=pos;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CKPCAbsoluteMoveCmd [name=" + name + ", pos=" + pos + "]";
	}

	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		CKGridActor target;
		if(cast!=null) 	{target = cast.getActorTarget(); }
		else					{target = getPC(name); }
		
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		CKGameObjectsFacade.getQuest().setStartTime(grid.move(target,pos)); 
	}

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
	 * @return the pos
	 */
	public CKGridPosition getPos()
	{
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(CKGridPosition pos)
	{
		CKPCAbsoluteMoveCmd.pos = pos;
	}
	
	/*
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Actor:"));
		SpinnerListModel model = new SpinnerListModel(getQuest().getActors().getActorNames());
		model.setValue(name);
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
			name=(String) text.getValue();
		}
	}
	
	
	//I added everything below from Relative
	
	
	static JPanel []panel;
	static JComboBox<String> []nameBox;
	static SpinnerNumberModel []X; 
	static SpinnerNumberModel []Y;
	static SpinnerNumberModel []Z;
	
	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			nameBox[1] = new JComboBox<String>();
			panel[0].add(nameBox[0]);		
			panel[1].add(nameBox[1]);		

			X=new SpinnerNumberModel[2];			
			X[0] = new SpinnerNumberModel(1, 0, 1000,1);
			JSpinner spin = new JSpinner(X[0]);
			panel[0].add(spin);
			X[1] = new SpinnerNumberModel(1, 0, 1000,1);
			spin = new JSpinner(X[1]);
			panel[1].add(spin);
			
			panel[0].add(new JLabel("X "));
			panel[1].add(new JLabel("X "));
			
			Y=new SpinnerNumberModel[2];			
			Y[0] = new SpinnerNumberModel(1, 0, 1000,1);
			JSpinner spin2 = new JSpinner(Y[0]);
			panel[0].add(spin2);
			Y[1] = new SpinnerNumberModel(1, 0, 1000,1);
			spin2 = new JSpinner(Y[1]);
			panel[1].add(spin2);
			
			panel[0].add(new JLabel("Y "));
			panel[1].add(new JLabel("Y "));
			
			/*
			Z=new SpinnerNumberModel[2];			
			Z[0] = new SpinnerNumberModel(1, 0, 1000,1);
			JSpinner spin3 = new JSpinner(Z[0]);
			panel[0].add(spin3);
			Z[1] = new SpinnerNumberModel(1, 0, 1000,1);
			spin3 = new JSpinner(Z[1]);
			panel[1].add(spin3);
			
			panel[0].add(new JLabel("Z "));
			panel[1].add(new JLabel("Z "));
			*/
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
			name = initializeActorBox(nameBox[index],name);
		}
		else
		{

			nameBox[index].addItem("Not Availible");
			nameBox[index].setEnabled(false);
		}
		
		X[index].setValue(xVal);
		Y[index].setValue(yVal);
		//Z[index].setValue(zVal);
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
		name = (String)nameBox[EDIT].getSelectedItem();
		xVal=(Integer) X[EDIT].getNumber();
		yVal=(Integer) Y[EDIT].getNumber();
		//zVal=(Integer) Z[EDIT].getNumber();
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


	
	