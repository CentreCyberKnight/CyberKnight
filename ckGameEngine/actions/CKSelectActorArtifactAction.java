/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javafx.application.Platform;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGraphicsEngine.CKDialogChoice;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckSnapInterpreter.CKDataModel;

/**
 * @author dragonlord
 *
 */
public class CKSelectActorArtifactAction extends CKGameAction 
implements CKEntitySelectedListener<CKDialogChoice>, ItemListener
{
	
	

	
	private static final long serialVersionUID = 2767116909885114617L;
	
	private String actor = "";
	private String artifact="";
	
	private boolean selectArtifact;
	
	public CKSelectActorArtifactAction()
	{

		this("HERO", "");
		
	}
	
	
	public CKSelectActorArtifactAction(String pc, String artifact)
	{
		super();
		this.actor=pc;
		this.artifact=artifact;

		selectArtifact= artifact.length()>0;
	}
	


	




	
	

	/**
	 * @return the actor
	 */
	public String getActor()
	{
		return actor;
	}


	/**
	 * @param actor the actor to set
	 */
	public void setActor(String actor)
	{
		this.actor = actor;
	}


	/**
	 * @return the artifact
	 */
	public String getArtifact()
	{
		return artifact;
	}


	/**
	 * @param artifact the artifact to set
	 */
	public void setArtifact(String artifact)
	{
		this.artifact = artifact;
	}


	/**
	 * @return the selectArtifact
	 */
	public boolean isSelectArtifact()
	{
		return selectArtifact;
	}


	/**
	 * @param selectArtifact the selectArtifact to set
	 */
	public void setSelectArtifact(boolean selectArtifact)
	{
		this.selectArtifact = selectArtifact;
	}


	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameAction#doAction(ckGameEngine.actions.CKGameActionListener)
	 */
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		
		if(actor.length()>0)
		{
			CKDataModel model = CKGameObjectsFacade.getDataModel();
			if(model ==null) return;
			
			Platform.runLater(()->
			{
			
				CKGridActor pc = getQuest().getActor(actor);
			
				model.setPlayer(pc);
			
				if(this.selectArtifact && this.artifact.length()>0)
				{
					
					CKArtifact [] arts=getQuest().getActor(actor).getTeam().getArtifacts()
					.stream().filter(a->{return a.getName().compareTo(this.artifact)==0;})
					.toArray(size->new CKArtifact[size]);
					
					if(arts.length > 0)
					{
						model.setArtifact(arts[0]);				
					}
					//model.setArtifact(pc.getTeam()..getArtifact(this.artifact));				
				}
			}
			);
			
		}
		
		L.actionCompleted(this);
		
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
		
		String ret = "select "+actor;
		if(this.selectArtifact==true)
		{
			return ret+" "+artifact;
		}
		else { return ret; }
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		//if(getQuest()!=null && (usePicture && ! useSpellTarget))
		if(actor.length()>0)
		{
			String assetID = getQuest().getPCAssetId(actor);
		
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
	static JComboBox<String> []artifactBox;
	static JCheckBox []setArtifact;
	
	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();	
			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
			
			panel[0].add(new JLabel("Select"));
			panel[1].add(new JLabel("Select"));

			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			panel[0].add(nameBox[0]);		
			nameBox[1] = new JComboBox<String>();
			panel[1].add(nameBox[1]);		
			
					
			setArtifact = new JCheckBox[2];
			setArtifact[0] = new JCheckBox("set Artifact");
			setArtifact[1] = new JCheckBox("set Artifact");
			panel[0].add(setArtifact[0]);
			panel[1].add(setArtifact[1]);

			
			artifactBox = new JComboBox[2];
			artifactBox[0] = new JComboBox<String>();
			panel[0].add(artifactBox[0]);		
			artifactBox[1] = new JComboBox<String>();
			panel[1].add(artifactBox[1]);		

			
			
		}
		
	}


	private void setPanelValues(int index)
	{
		if(panel==null) { initPanel(true); }
		
		setArtifact[index].setSelected(selectArtifact);

		if(getQuest()!=null)
		{
			actor= initializeActorBox(nameBox[index],actor);			
		}
		if (selectArtifact)
		{
			artifactBox[index].setEnabled(true);
			if(actor.length()>0)
			{
				artifact = initializeArtifactBox(artifactBox[index],actor,artifact);
			}
		}
		else
		{
			artifactBox[index].setEnabled(false);
		}
	}
	
	
	static ItemListener guiListener=null;
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setArtifact[EDIT].removeItemListener(guiListener);
		guiListener = this;		
		setPanelValues(EDIT);
		setArtifact[EDIT].addItemListener(guiListener);
		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		actor = (String)nameBox[EDIT].getSelectedItem();
		selectArtifact = (boolean) setArtifact[EDIT].isSelected();
		
		if(!selectArtifact)
		{
			artifact = (String)artifactBox[EDIT].getSelectedItem();
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
