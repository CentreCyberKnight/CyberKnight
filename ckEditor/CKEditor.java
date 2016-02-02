package ckEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKArtifactFactory;
import ckDatabase.CKBookFactory;
import ckDatabase.CKGridActorFactory;
import ckDatabase.CKGridItemFactory;
import ckDatabase.CKQuestFactory;
import ckDatabase.CKQuestTestFactory;
import ckDatabase.CKSceneFactory;
import ckDatabase.CKTeamFactory;
import ckDatabase.CKTriggerFactory;
import ckDatabase.CKTriggerListFactory;
import ckDatabase.CKXMLFactory;
import ckEditor.treegui.CKQuestTest;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGridItem;
import ckGameEngine.CKTeam;
import ckGameEngine.QuestData;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckSound.CKSound;
import ckSound.CKSoundFactory;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;

public class CKEditor
{

	
	
	public static class Popup<T extends CKXMLAsset<T>,F extends CKXMLFactory<T>>  implements ActionListener
	{

		
		F factory;
		
		public Popup(F fact)
		{
			factory = fact;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			CKXMLAssetEditor<T,F> view= new CKXMLAssetEditor<T,F>(factory.getAssetInstance(),factory);
			
			frame.add(view);
			frame.pack();
			frame.setVisible(true);
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		}
		
		
		
		
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(300,400));
		
		JButton gridItem = new JButton("Item Editor");
		gridItem.addActionListener(new Popup<CKGridItem,CKGridItemFactory>(CKGridItemFactory.getInstance()));
		panel.add(gridItem);
		
		
		JButton gridActor = new JButton("Actor Editor");
		gridActor.addActionListener(new Popup<CKGridItem,CKGridActorFactory>(CKGridActorFactory.getInstance()));
		panel.add(gridActor);		
		
		JButton sound = new JButton("Sound Editor");
		sound.addActionListener(new Popup<CKSound,CKSoundFactory>(CKSoundFactory.getInstance()));
		panel.add(sound);		

		JButton trigger = new JButton("Trigger Editor");
		trigger.addActionListener(new Popup<CKTrigger,CKTriggerFactory>(CKTriggerFactory.getInstance()));
		panel.add(trigger);		
		
		JButton triggerList = new JButton("TrigerList Editor");
		triggerList.addActionListener(new Popup<CKTriggerList,CKTriggerListFactory>(CKTriggerListFactory.getInstance()));
		panel.add(triggerList);		
		
		JButton scene = new JButton("Scene Editor");
		scene.addActionListener(new Popup<CKGraphicsSceneInterface,CKSceneFactory>(CKSceneFactory.getInstance()));
		panel.add(scene);		
		
		JButton team = new JButton("Team Editor");
		team.addActionListener(new Popup<CKTeam,CKTeamFactory>(CKTeamFactory.getInstance()));
		panel.add(team);
		
		JButton artifact = new JButton("Artifact Editor");
		artifact.addActionListener(new Popup<CKArtifact,CKArtifactFactory>(CKArtifactFactory.getInstance()));
		panel.add(artifact);
		
		JButton book = new JButton("Book Editor");
		book.addActionListener(new Popup<CKBook,CKBookFactory>(CKBookFactory.getInstance()));
		panel.add(book);
		
		
		
		JButton questTest = new JButton("Quest Test Editor");
		questTest.addActionListener(new Popup<CKQuestTest,CKQuestTestFactory>(CKQuestTestFactory.getInstance()));
		panel.add(questTest);	
		
		JButton quest = new JButton("Quest Editor");
		quest.addActionListener(new Popup<QuestData,CKQuestFactory>(CKQuestFactory.getInstance()));
		panel.add(quest);		

		
		
		
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
