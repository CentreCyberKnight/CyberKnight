/**
 * 
 */
package ckGameEngine.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKDialogMessageFactory;
import ckDatabase.CKDialogMessageFactoryExample;
import ckGraphicsEngine.CKDialogChoice;
import ckGraphicsEngine.CKDialogMessage;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
public class CKGUIAction extends CKGameAction implements CKEntitySelectedListener<CKDialogChoice>
{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6541541978080039019L;
	private int startingId;
	private CKDialogMessageFactory factory;
	
	public CKGUIAction()
	{
		super();
		startingId=0;
		factory = new CKDialogMessageFactoryExample();
	}
	
	/**
	 * @return the startingId
	 */
	public int getStartingId()
	{
		return startingId;
	}

	/**
	 * @param startingId the startingId to set
	 */
	public void setStartingId(int startingId)
	{
		this.startingId = startingId;
	}

	/**
	 * @return the factory
	 */
	public CKDialogMessageFactory getFactory()
	{
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	public void setFactory(CKDialogMessageFactory factory)
	{
		this.factory = factory;
	}

	public CKGUIAction(CKDialogMessageFactory factory, int messID)
	{
		super();
		this.factory = factory;
		this.startingId = messID;
	}
	
	

	
	

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameAction#doAction(ckGameEngine.actions.CKGameActionListener)
	 */
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		replaceListener(L);
		sendNextMessage(startingId);
	}


	@Override
	public void entitySelected(CKDialogChoice entity)
	{
		sendNextMessage(entity.getID());
		
	}

	private void sendNextMessage(int mess)
	{
		CKDialogMessage message = factory.getDialogMessage(mess);
		if(message == null)
		{
			notifyListener(); //we are done!
			return;
		}
		else
		{
		message.replaceEventListener(this);
		CKGameObjectsFacade.getEngine().loadDialogMessage(message);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "GUI Action "+ startingId;
	}
	
	
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("FIX Edit Action");
		
		JPanel panel = new JPanel();
		
		panel.add(new JLabel("ID:"));
		SpinnerNumberModel model = new SpinnerNumberModel(startingId, 0, 10000,1);
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
			startingId=spin.getNumber().intValue();
			
		}
	}


	
	
	
	
	
}
