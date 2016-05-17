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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerListModel;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Quest;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.CircularDependanceError;

/**
 * @author dragonlord
 *
 */
public class CKPCFocusCameraCmd extends CKQuestAction
{
	
	private static final long serialVersionUID = 3853248008608299397L;
	String name;
	
	public CKPCFocusCameraCmd()
	{
		this("HERO");
	}
	public CKPCFocusCameraCmd(String name)
	{
		this.name = name;

	
	}
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{		
		CKGridActor target;
		if(cast!=null) 	{target = cast.getActorTarget(); }
		else			{target = getPC(name); }	
		
		Quest w = CKGameObjectsFacade.getQuest();
		try
		{
			CKGameObjectsFacade.getEngine().cameraFollowInstance(w.gettId(),
					target.getInstanceID(), 
				w.getStartTime(), 30);
		} catch (BadInstanceIDError e)
		{
			e.printStackTrace();
		} catch (CircularDependanceError e)
		{
			e.printStackTrace();
		}
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Focus Camera on " + name;
	}

	
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Name:"));
		SpinnerListModel model = new SpinnerListModel(getQuest().getActors().getActorNames());
		model.setValue(name);
		JSpinner spin = new JSpinner(model);
		((JSpinner.DefaultEditor)spin.getEditor()).getTextField().setColumns(16);
		panel.add(spin);
		menu.add(panel);	

		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(model));
		menu.add(store);
			
		return menu;
	}
	
	
	class EditAction  implements ActionListener
	{
		SpinnerListModel text;
		
		public EditAction(SpinnerListModel f)
		{
			text= f;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			name = (String) text.getValue();
			
		}
	}
	
	static JPanel []panel;
	static JComboBox<String> []nameBox;
	
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
			panel[0].add(new JLabel("Focus Camera On"));
			panel[0].add(nameBox[0]);		
			
			panel[1].add(new JLabel("Focus Camera On"));
			panel[1].add(nameBox[1]);		
			
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
