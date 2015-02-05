package ckEditor.AbstractDialogEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckCommonUtils.CKEntitySelectedListener;
import ckGameEngine.QuestData;
import ckPythonInterpreterTest.CKArtifactQuestRunner;

@SuppressWarnings("serial")
public class TestGraphEditor extends JPanel 
implements CKEntitySelectedListener<JPanel>
{

	AbstractCKGraphEditor editor;
	JPanel east;
	JPanel buttonPanel = new JPanel(new FlowLayout());
	AbstractGraph ag;
	String data;
	
	
	TestGraphEditor()
	{
		ag = new AbstractGraph();
		
		//reuse test data...
		QuestData q = CKArtifactQuestRunner.createTestQuest().getQuestData();
		//add the hidden node...
		
		ag.setSecretParent(q);
		
		setLayout(new BorderLayout());
//		editor = new AbstractCKGraphEditor(ag,new VertexNode(),new EdgeLink());
		editor = new AbstractCKGraphEditor(ag,new DialogVertex(),new EdgeLink());
		editor.addEntitySelectedListener(this);
		east = new JPanel();
		east.add(new JLabel("waiting for donuts"));
		add(east,BorderLayout.EAST);
		add(editor,BorderLayout.CENTER);
	
		

		JButton saveButton = new JButton("Save");
		buttonPanel.add(saveButton);
		saveButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				data = ag.getXMLString(editor.getGraphLayout());
				System.out.println(data);				
			}
			
		});
		
		JButton loadButton = new JButton("Load");
		buttonPanel.add(loadButton);
		loadButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ag = AbstractGraph.readFromXMLString(data);
				editor = new AbstractCKGraphEditor(ag,new VertexNode(),new EdgeLink());
				editor.addEntitySelectedListener(TestGraphEditor.this);
				resetViews();
				
			}
			
		});
		
		add(buttonPanel,BorderLayout.SOUTH);
			
	}
	
	
	@Override
	public void entitySelected(JPanel entity)
	{	
		//TODO should set a save right here... 
		east=entity;
		resetViews();
	}

	public void resetViews()
	{
		removeAll();
		add(editor,BorderLayout.CENTER);
		add(east,BorderLayout.EAST);
		add(buttonPanel,BorderLayout.SOUTH);
		revalidate();

	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();

		TestGraphEditor view=new TestGraphEditor();
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
}
