/**
 * 
 */
package ckGameEngine.actions;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;
import ckGraphicsEngine.CKDialogChoice;
import ckGraphicsEngine.CKDialogMessage;
import ckGraphicsEngine.assets.CKGraphicsAsset;

/**
 * @author dragonlord
 *
 */
public class VoiceAction extends CKGameAction implements CKEntitySelectedListener<CKDialogChoice>
{

	private String mess;
	private boolean picture;
	

	
	
	private static final long serialVersionUID = -1112825497910646743L;

	public VoiceAction() 
	{
		this.mess ="";
	}
	
	public VoiceAction(String mess)
	{
		this.mess = mess;
	}
	
	
	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

	public boolean isPicture() {
		return picture;
	}

	public void setPicture(boolean picture) {
		this.picture = picture;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Voice";
	}


	@Override
	public void entitySelected(CKDialogChoice entity)
	{
		notifyListener();
		
	}

	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		replaceListener(L);
		CKDialogMessage message;
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();

		if(getQuest()!=null && (picture==true))
		{//use target
			CKGraphicsAsset portrait = factory.getPortrait(cast.getActorTarget().getAssetID());
			message = new CKDialogMessage(mess, portrait);

		}
		message = new CKDialogMessage(cast.getKey());
		message.replaceEventListener(this);
		CKGameObjectsFacade.getEngine().loadDialogMessage(message);

		
	}
/*
	static JPanel []panel;
	static JTextArea []messBox;
	

	static JCheckBox []isPic;
	
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
			
			
			isPic = new JCheckBox[2];
			isPic[0] = new JCheckBox("Display Portrait?");
			isPic[1] = new JCheckBox("Display Portrait?");
			top[0].add(isPic[0]);
			top[1].add(isPic[1]);
			
			
			JPanel []bot = new JPanel[2]; 
			bot[0] = new JPanel();
			bot[1] = new JPanel();
			

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
		
		isPic[index].setSelected(picture);
		messBox[index].setText(mess);		
	}
	

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}


	@Override
	public void storeComponentValues()
	{

		mess = messBox[EDIT].getText();
		picture = (boolean) isPic[EDIT].isSelected();

	}


	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}

*/
}