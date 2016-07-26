package ckEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKQuestFactory;
import ckDatabase.CKXMLFactory;

public class QuickCKEditor
{

	
	
	static class Popup<T extends CKXMLAsset<T>,F extends CKXMLFactory<T>>  implements ActionListener
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
	
	
	
	public static <T extends CKXMLAsset<T>,F extends CKXMLFactory<T>> void openEditor(T asset,F factory)
	{
		JFrame frame = new JFrame();
		CKXMLAssetEditor<T,F> view= new CKXMLAssetEditor<T,F>(asset,factory);
		
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{/*
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
		
		JButton quest = new JButton("Quest Editor");
		quest.addActionListener(new Popup<QuestData,CKQuestFactory>(CKQuestFactory.getInstance()));
		panel.add(quest);		

		
		
		
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		*/

		//CKQuestTestFactory factory = CKQuestTestFactory.getInstance();
		CKQuestFactory factory = CKQuestFactory.getInstance();
		//openEditor(factory.getAsset("asset7990217181097898597"),factory);
		openEditor(factory.getAsset("asset5480648433123583504"),factory);
		
	}

}
