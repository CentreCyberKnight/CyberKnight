/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import ckGameEngine.CKSpellCast;
import ckGameEngine.Quest;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.FX2dGraphicsEngine;

/**
 * @author dragonlord
 *
 */
public class CKWaitCmd extends CKQuestAction
{




	
	private static final long serialVersionUID = 6428447825524450185L;
	int delay;
	
	public CKWaitCmd()
	{
		this(30);
	}
	
	public CKWaitCmd(int delay)
	{
		this.delay=delay;
	
	}
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{		
		Quest w = CKGameObjectsFacade.getQuest();
		FX2dGraphicsEngine e = CKGameObjectsFacade.getEngine();
		e.createNullAction(0, w.getStartTime(), w.getStartTime()+delay);
		//w.setStartTime(w.getStartTime()+delay);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Wait  " + delay ;
	}
	
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Wait:"));
		SpinnerNumberModel model = new SpinnerNumberModel(delay, 0, 1000,1);
		JSpinner spin = new JSpinner(model);
		panel.add(spin);
		menu.add(panel);	

		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(model));
		menu.add(store);
		
		return menu;
	}
	
	
	class EditAction  implements ActionListener
	{
		SpinnerNumberModel spin;
		
		public EditAction(SpinnerNumberModel s)
		{
			spin=s;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			delay=spin.getNumber().intValue();
			
		}
	}
	
	static JPanel []panel;
	static SpinnerNumberModel []frames; //forward;
	
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].add(new JLabel("Wait"));
			panel[1].add(new JLabel("Wait"));

			frames=new SpinnerNumberModel[2];			
			frames[0] = new SpinnerNumberModel(1, 1, 1000,1);
			JSpinner spin = new JSpinner(frames[0]);
			panel[0].add(spin);
			frames[1] = new SpinnerNumberModel(1, 1, 1000,1);
			spin = new JSpinner(frames[1]);
			panel[1].add(spin);
			
			panel[0].add(new JLabel("Frames"));
			panel[1].add(new JLabel("Frames"));
			
			
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
		
		frames[index].setValue(delay);	
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
		delay=(Integer) frames[EDIT].getNumber();
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
