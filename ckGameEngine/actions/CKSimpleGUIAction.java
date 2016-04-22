/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;
import ckGraphicsEngine.CKDialogChoice;
import ckGraphicsEngine.CKDialogMessage;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;

/**
 * @author dragonlord
 *
 */
public class CKSimpleGUIAction extends CKGameAction 
implements CKEntitySelectedListener<CKDialogChoice>, ItemListener
{
	
	

	
	private static final long serialVersionUID = 2767116909885114617L;
	
	private String pc = "";
	private String mess;
	private boolean useSpellTarget;
	private boolean usePicture;
	private boolean useKeyMessage;
	
	public CKSimpleGUIAction()
	{

		this("HERO", "Let's Chase Bad Guys!");
		
	}
	
	
	public CKSimpleGUIAction(String pc, String mess)
	{
		super();
		this.pc=pc;
		this.mess=mess;
		useKeyMessage=false;
		if(pc!=null && pc.length()>0)
		{
			useSpellTarget = true;
			usePicture=true;
		}
		
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


	/**
	 * @return the useKeyMessage
	 */
	public boolean isUseKeyMessage()
	{
		return useKeyMessage;
	}


	/**
	 * @param useKeyMessage the useKeyMessage to set
	 */
	public void setUseKeyMessage(boolean useKeyMessage)
	{
		this.useKeyMessage = useKeyMessage;
	}


	/**
	 * @return the mess
	 */
	public String getMess()
	{
		return mess;
	}


	/**
	 * @param mess the mess to set
	 */
	public void setMess(String mess)
	{
		this.mess = mess;
	}
	
	public boolean getUseSpellTarget()
	{
		return useSpellTarget;
	}
	
	public void setUseSpellTarget(boolean target)
	{
		this.useSpellTarget = target;
	}
	
	public boolean getUsePicture()
	{
		return usePicture;
	}
	
	public void setUsePicture(boolean picture)
	{
		this.usePicture = picture;
	}


	

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameAction#doAction(ckGameEngine.actions.CKGameActionListener)
	 */
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		if(CKGameObjectsFacade.isPrediction()) 
		{
			L.actionCompleted(this);
			return; 
		}
		replaceListener(L);
		CKDialogMessage message;

		
		
		
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		String mText = mess;
		if(useKeyMessage)
		{
			mText = cast.getKey();
		}
		
		if(usePicture)
		{
			CKGraphicsAsset portrait;
			if(useSpellTarget && cast !=null) //use target of spell
			{
					portrait = factory.getPortrait(cast.getItemTarget().getAssetID());
			}
			else //use stored value
			{
				portrait = factory.getPortrait(getQuest().getActor(pc).getAssetID());
			}
			message = new CKDialogMessage(mText, portrait);
		}
		else //no picture
		{
			message = new CKDialogMessage(mText);			
		}
		message.replaceEventListener(this);
		CKGameObjectsFacade.getEngine().loadDialogMessage(message);

	
		
		
		
		
	}


	@Override
	public void entitySelected(CKDialogChoice entity)
	{
		notifyListener();
		
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return pc+"says"+ mess;
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		//if(getQuest()!=null && (usePicture && ! useSpellTarget))
		if(usePicture && ! useSpellTarget)
		{
			String assetID = getQuest().getPCAssetId(pc);
		
			if(assetID != "")
			{
				CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		
				CKGraphicsAsset portrait = factory.getPortrait(assetID);
		 
				return new ImageIcon(CKGraphicsPreviewGenerator.createAssetPreview(portrait,
						0,0,64,128) );
			}
		}
			return super.getTreeIcon(leaf,expanded);
		
	}
	
	

	static JPanel []panel;
	static JComboBox<String> []nameBox;
	static JTextArea []messBox;
	static JCheckBox []isTarget;
	static JCheckBox []isPic;
	static JCheckBox []isKeyMessage;
	
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.Y_AXIS));
			panel[1]=new JPanel();	
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.Y_AXIS));
			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
			
			JPanel []top = new JPanel[2]; 
			top[0] = new JPanel();
			top[1] = new JPanel();
			
			isTarget = new JCheckBox[2];
			isTarget[0] = new JCheckBox("Use Spell Target");
			isTarget[1] = new JCheckBox("Use Spell Target");
			top[0].add(isTarget[0]);
			top[1].add(isTarget[1]);
			
			isPic = new JCheckBox[2];
			isPic[0] = new JCheckBox("Display Portrait?");
			isPic[1] = new JCheckBox("Display Portrait?");
			top[0].add(isPic[0]);
			top[1].add(isPic[1]);
			
			isKeyMessage = new JCheckBox[2];
			isKeyMessage[0] = new JCheckBox("Use Key for Message");
			isKeyMessage[1] = new JCheckBox("Use Key for Message");
			top[0].add(isKeyMessage[0]);
			top[1].add(isKeyMessage[1]);

			
			
			JPanel []bot = new JPanel[2]; 
			bot[0] = new JPanel();
			bot[1] = new JPanel();
			
			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			bot[0].add(nameBox[0]);		
			bot[0].add(new JLabel("says"));
			nameBox[1] = new JComboBox<String>();
			bot[1].add(nameBox[1]);		
			bot[1].add(new JLabel("says"));

			messBox=new JTextArea[2];
			messBox[0]=new JTextArea(1,15);
			messBox[1]=new JTextArea(1,15);
			bot[0].add(messBox[0]);
			bot[1].add(messBox[1]);
			
			messBox[0].setLineWrap(true);
			messBox[0].setWrapStyleWord(true);
			messBox[1].setLineWrap(true);
			messBox[1].setWrapStyleWord(true);
			
			panel[0].add(top[0]);
			panel[1].add(top[1]);
			panel[0].add(bot[0]);
			panel[1].add(bot[1]);
			
		}
		
	}


	private void setPanelValues(int index)
	{
		if(panel==null) { initPanel(true); }
		
		isTarget[index].setSelected(useSpellTarget);
		isPic[index].setSelected(usePicture);
		isKeyMessage[index].setSelected(useKeyMessage);

		if(getQuest()!=null)
		{
			pc= initializeActorBox(nameBox[index],pc);			
		}
		if (useSpellTarget)
		{
			nameBox[index].setEnabled(false);
		}
		else
		{
			nameBox[index].setEnabled(true);
		}
		isPic[index].setSelected(usePicture);
		messBox[index].setText(mess);
		messBox[index].setEnabled(!useKeyMessage);

	}
	
	
	static ItemListener guiListener=null;
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		isTarget[EDIT].removeItemListener(guiListener);
		isPic[EDIT].removeItemListener(guiListener);
		isKeyMessage[EDIT].removeItemListener(guiListener);
		guiListener = this;		
		setPanelValues(EDIT);
		isTarget[EDIT].addItemListener(guiListener);
		isPic[EDIT].addItemListener(guiListener);
		isKeyMessage[EDIT].addItemListener(guiListener);
		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		mess = messBox[EDIT].getText();
		useSpellTarget = (boolean) isTarget[EDIT].isSelected();
		usePicture = (boolean) isPic[EDIT].isSelected();
		useKeyMessage = (boolean) isKeyMessage[EDIT].isSelected();
		
		if(!useSpellTarget)
		{
			pc = (String)nameBox[EDIT].getSelectedItem();
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
		if(e.getID()==ItemEvent.ITEM_STATE_CHANGED)
		{
			
			storeComponentValues();
			//bogus values to force a redraw-don't care about the return value.
			getTreeCellEditorComponent(null,null,true,true,false,0);
		}
	}
}
