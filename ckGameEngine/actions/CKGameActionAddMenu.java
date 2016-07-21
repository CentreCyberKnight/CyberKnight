package ckGameEngine.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;

import ckEditor.treegui.CKTreeGui;
import ckGameEngine.QuestData;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.CKTriggerList;

public class CKGameActionAddMenu
{
	
	public static JMenu getMenu(CKTreeGui tree,QuestData quest)
	{
		return getMenu(tree,quest,"Add Actions",0,false);
	}
	
	public static JMenu getMenu(CKTreeGui tree,QuestData quest, String text,int pos,boolean replace)
	{
		JMenu addActions = new JMenu(text);

			/*        Add Actions --should these be based on the item?*/
			
			
			/* 
			 * ACTION TYPES
			 * 
			 */
			JMenu addActionType = new JMenu("Action Types:");
			addActions.add(addActionType);
			
			JMenuItem addSeq = new JMenuItem("Add Sequential Action");
			addSeq.addActionListener(new TreeAddActionListener("SEQUENTIAL",tree,pos,replace));
			addActionType.add(addSeq);

			JMenuItem addConn = new JMenuItem("Add Concurrent Action");
			addConn.addActionListener(new TreeAddActionListener("CONCURRENT",tree,pos,replace));
			addActionType.add(addConn);
			
			JMenuItem addRand = new JMenuItem("Add Random Action");
			addRand.addActionListener(new TreeAddActionListener("RANDOM",tree,pos,replace));
			addActionType.add(addRand);
			
			JMenuItem addNULL= new JMenuItem("Add NULL Action");
			addNULL.addActionListener(new TreeAddActionListener("NULL_ACTION",tree,pos,replace));
			addActionType.add(addNULL);


			JMenuItem addTL = new JMenuItem("Add TriggerList");
			addTL.addActionListener(new TreeAddActionListener("TRIGGERLIST",tree,pos,replace));
			addActions.add(addTL);

			
			JMenuItem addSTL = new JMenuItem("Add Shared TriggerList");
			addSTL.addActionListener(new TreeAddActionListener("SHAREDTRIGGER",tree,pos,replace));
			addActions.add(addSTL);

			
			addActions.add(new JSeparator());
			
			/*
			 * 
			 * DIALOG TYPES
			 * 
			 */
			
			
			JMenu addDialogue = new JMenu("Dialogue Options:");
			addActions.add(addDialogue);
			

			JMenuItem addGUI= new JMenuItem("Add Simple Dialog");
			addGUI.addActionListener(new TreeAddActionListener("SIMPLE_DIALOG",tree,pos,replace));
			addDialogue.add(addGUI);
			
			addGUI= new JMenuItem("Add Dialogue Action");
			addGUI.addActionListener(new TreeAddActionListener("DIALOGUE_ACTION",tree,pos,replace));
			addDialogue.add(addGUI);
			if(quest==null)
			{
				addGUI.setEnabled(false);
			}
			
			
			JMenuItem addActorSel= new JMenuItem("Add Actor/Artifact Selection Action");
			addActorSel.addActionListener(new TreeAddActionListener("SELECTION_ACTION",tree,pos,replace));
			addDialogue.add(addActorSel);
			if(quest==null)
			{
				addActorSel.setEnabled(false);
			}

			
			
			/*
			 * 
			 * MOVEMENT OPTIONS
			 * 
			 * 
			 */
			
			JMenu addMove = new JMenu("Movement Options:");
			addActions.add(addMove);
			
			JMenuItem addAMove= new JMenuItem("Absolute Move");
			addAMove.addActionListener(new TreeAddActionListener("PC_ABSOLUTE_MOVE",tree,pos,replace));
			addMove.add(addAMove);
			
			//Teleport
			JMenuItem addTAction= new JMenuItem("Teleport Action");
			addTAction.addActionListener(new TreeAddActionListener("PC_TELEPORT_ACTION",tree,pos,replace));
			addMove.add(addTAction);
			
			JMenuItem addRMove= new JMenuItem("Relative Move");
			addRMove.addActionListener(new TreeAddActionListener("PC_RELATIVE_MOVE",tree,pos,replace));
			addMove.add(addRMove);
			
			JMenuItem addTurn= new JMenuItem("PC Turn");
			addTurn.addActionListener(new TreeAddActionListener("PC_TURN",tree,pos,replace));
			addMove.add(addTurn);

			JMenuItem addMoveSpell= new JMenuItem("Move Actor Command");
			addMoveSpell.addActionListener(new TreeAddActionListener("PC_MOVE_SPELL",tree,pos,replace));
			addMove.add(addMoveSpell);

			
			/*
			 * 
			 * ACTOR OPTIONS
			 * 
			 * 
			 */
			JMenu addActorCmd = new JMenu("Actor Options:");
			addActions.add(addActorCmd);
			
			JMenuItem addGridActor= new JMenuItem("Add Grid Actor");
			addGridActor.addActionListener(new TreeAddActionListener("Add Grid Actor",tree,pos,replace));
			addActorCmd.add(addGridActor);
			
			JMenuItem removeGridActor= new JMenuItem("Remove Grid Actor");
			removeGridActor.addActionListener(new TreeAddActionListener("Remove Grid Actor",tree,pos,replace));
			addActorCmd.add(removeGridActor);
			
			JMenuItem addMarkGridActor= new JMenuItem("Mark Grid Actor");
			addMarkGridActor.addActionListener(new TreeAddActionListener("Mark Grid Actor",tree,pos,replace));
			addActorCmd.add(addMarkGridActor);
			
			JMenuItem removeActorPage= new JMenuItem("Remove Actor Page");
			removeActorPage.addActionListener(new TreeAddActionListener("Remove Actor Page",tree,pos,replace));
			addActorCmd.add(removeActorPage);
			
			/*
			 * 
			 * SPELL MECHANICS
			 * 
			 */
			
			JMenu addSpellMech = new JMenu("Spell Mechanics:");
			addActions.add(addSpellMech);
			
			JMenuItem addSpell= new JMenuItem("Spell");
			addSpell.addActionListener(new TreeAddActionListener("Spell",tree,pos,replace));
			addSpellMech.add(addSpell);	

			JMenuItem addReactiveSpell = new JMenuItem("Reactive Spell Action");
			addReactiveSpell.addActionListener(new TreeAddActionListener("Reactive Spell Action",tree,pos,replace));
			addSpellMech.add(addReactiveSpell);
			
			JMenu addSpellFX = new JMenu("Spell Effect Options:");
			addActions.add(addSpellFX);
			
			JMenuItem alterCP= new JMenuItem("Alter Cyber Points");
			alterCP.addActionListener(new TreeAddActionListener("Alter Cyber Points",tree,pos,replace));
			addSpellFX.add(alterCP);
			
			JMenuItem contestedAlterCP= new JMenuItem("Contested Alter Cyber Points");
			contestedAlterCP.addActionListener(new TreeAddActionListener("Contested Alter Cyber Points",tree,pos,replace));
			addSpellFX.add(contestedAlterCP);
			
			
			
			JMenuItem alterSpellCP = new JMenuItem("Alter Spell Points");
			alterSpellCP.addActionListener(new TreeAddActionListener("Alter Spell Points",tree,pos,replace));
			addSpellFX.add(alterSpellCP);
			
			JMenuItem ShovedActor= new JMenuItem("Shoved");
			ShovedActor.addActionListener(new TreeAddActionListener("Shoved",tree,pos,replace));
			addSpellFX.add(ShovedActor);
			
			JMenuItem voiceAction = new JMenuItem("Voice Action");
			voiceAction.addActionListener(new TreeAddActionListener("Voice Action",tree,pos,replace));
			addSpellFX.add(voiceAction);

			JMenuItem scryAction = new JMenuItem("Scry Action");
			scryAction.addActionListener(new TreeAddActionListener("Scry Action",tree,pos,replace));
			addSpellFX.add(scryAction);
			
			JMenuItem soundAction = new JMenuItem("Sound Action");
			soundAction.addActionListener(new TreeAddActionListener("Sound Action",tree,pos,replace));
			addSpellFX.add(soundAction);
			
			
			
			
			addActions.add(new JSeparator());


			JMenuItem addPCFocus= new JMenuItem("PC Focus");
			addPCFocus.addActionListener(new TreeAddActionListener("PC_CAMERA_FOCUS",tree,pos,replace));
			addActions.add(addPCFocus);


			JMenuItem addWait= new JMenuItem("Wait");
			addWait.addActionListener(new TreeAddActionListener("WAIT",tree,pos,replace));
			addActions.add(addWait);
			
			addMenuItem("End Quest","ENDQUEST",addActions,tree,pos,replace);
			
			
			return addActions;
		}
	
	
	private static void addMenuItem(String name, String tag, JComponent parent,
			CKTreeGui tree,int pos,boolean replace)
	
	{
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(new TreeAddActionListener(tag,tree,pos,replace));
		parent.add(item);	
		
	}
	
	
	
}

	
class TreeAddActionListener implements ActionListener
{
		String name;
		String hero = "HERO";
		CKTreeGui tree;
		int position;
		boolean replace;
	
		
		
		public TreeAddActionListener(String n,CKTreeGui t,int pos,boolean replace)
		{
			name = n;
			tree=t;
			position = pos;
			this.replace=replace;	
		}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)
			{
			//	node = (DefaultMutableTreeNode) treeModel.getRoot();
				return;
			}
			CKGameAction action=new CKNullAction();
			if(name.compareTo("CONCURRENT")==0)
			{
				action = new CKConcurrentAction();
			}
			else if(name.compareTo("SEQUENTIAL")==0)
			{
				action = new CKSequentialAction();
			}			
			else if(name.compareTo("RANDOM")==0){
				action = new CKRandomAction();
			}			
			else if(name.compareTo("TRIGGERLIST")==0)
			{
				action = new CKTriggerList();
			}
			else if(name.compareTo("SHAREDTRIGGER")==0)
			{
				action = new CKSharedTriggerList();
			}
			else if(name.compareTo("DIALOGUE_ACTION")==0)
			{				
				action = new CKDialogAction();
			}
			else if(name.compareTo("SIMPLE_DIALOG")==0)
			{				
				action = new CKSimpleGUIAction();
			}
			else if(name.compareTo("PC_ABSOLUTE_MOVE")==0)
			{
				action = new CKPCAbsoluteMoveCmd();
			}
			//teleport
			else if(name.compareTo("PC_TELEPORT_ACTION")==0)
			{
				action = new TeleportAction(); 
			}
					
			else if(name.compareTo("PC_CAMERA_FOCUS")==0)
			{
				action = new CKPCFocusCameraCmd();
			}
			else if(name.compareTo("PC_RELATIVE_MOVE")==0)
			{
				action = new CKPCRelativeMoveCmd();
			}
			else if(name.compareTo("PC_TURN")==0)
			{
				action = new CKPCTurnCmd();
			}
			else if(name.compareTo("WAIT")==0)
			{
				action = new CKWaitCmd();
			}
			else if(name.compareTo("NULL_ACTION")==0)
			{
				action = new CKNullAction();
			}
			else if(name.compareTo("Spell")==0)
			{
				action = new CKSpellAction();
			}
			else if(name.compareTo("Mark Grid Actor")==0)
			{
				action = new CKMarkGridActor();
			}
			else if(name.compareTo("Remove Actor Page")==0)
			{
				action = new CKRemovePage();
			}
			else if(name.compareTo("Reactive Spell Action")==0)
			{
				action = new CKReactiveSpellAction();
			}
			else if(name.compareTo("Add Grid Actor")==0)
			{
				action = new CKAddActorCmd();
			}
			else if(name.compareTo("Remove Grid Actor")==0)
			{
				action = new CKRemoveActorCmd();
			}
			else if(name.compareTo("Shoved")==0)
			{
				action = new CKShoveActorCmd();
			}
			else if(name.compareTo("Alter Cyber Points")==0)
			{
				action = new CKAlterCPCmd();
			}
			else if(name.compareTo("Contested Alter Cyber Points")==0)
			{
				action = new CKContestedAlterCP();
			}
			else if(name.compareTo("Alter Spell Points")==0)
			{
				action = new CKAlterSpellCPCmd();
			}
			
			else if(name.compareTo("Voice Action")==0)
			{
				action = new VoiceAction();
			}
			
			else if(name.compareTo("Scry Action")==0)
			{
				action = new ScryAction();
			}
			
			else if(name.compareTo("Sound Action")==0)
			{
				action = new CKSoundEffect();
			}
			
			else if(name.compareTo("PC_MOVE_SPELL")==0)
			{
				action = new CKMoveActorCmd();
			}
			else if(name.compareTo("ENDQUEST")==0)
			{
				action = new CKEndGameAction();
			}
			else if(name.compareTo("SELECTION_ACTION")==0)
			{
				action = new CKSelectActorArtifactAction();
			}
			
			if(replace)
			{
				node.remove(position);
			}
			tree.addNode(node,action,position);		
		}
			
	
}

