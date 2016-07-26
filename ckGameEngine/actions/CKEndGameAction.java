/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;

import javafx.application.Platform;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
public class CKEndGameAction extends CKQuestAction
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2093475589966167206L;
	int winValue=0; //not a win :)
	
	public CKEndGameAction()
	{
		
	}
	

	
	/**
	 * @return the winValue
	 */
	public int getWinValue()
	{
		return winValue;
	}



	/**
	 * @param winValue the winValue to set
	 */
	public void setWinValue(int winValue)
	{
		this.winValue = winValue;
	}



	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		Platform.runLater(()->
		{
		CKGameObjectsFacade.getGameCompletionListener()
		.endGame(winValue,
				CKGameObjectsFacade.getQuest().getQuestData().getAID());
		});
	}

	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "End Game Action [WinValue:" + winValue+"]";
	}
	
	static JPanel []panel;
	static SpinnerNumberModel []winAmount; ;
	
	
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].add(new JLabel("End Game, 1-3 is a win"));
			panel[1].add(new JLabel("End Game, 1-3 is a win"));

			winAmount=new SpinnerNumberModel[2];			
			winAmount[0] = new SpinnerNumberModel(0, 0, 3,1);
			JSpinner spin = new JSpinner(winAmount[0]);
			panel[0].add(spin);
			winAmount[1] = new SpinnerNumberModel(0, 0, 3,1);
			spin = new JSpinner(winAmount[1]);
			panel[1].add(spin);
			
			panel[0].add(new JLabel("Steps"));
			panel[1].add(new JLabel("Steps"));
			
			
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
		
		winAmount[index].setValue(winValue);	
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
		winValue=(Integer) winAmount[EDIT].getNumber();
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
