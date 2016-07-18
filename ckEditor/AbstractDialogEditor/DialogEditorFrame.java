package ckEditor.AbstractDialogEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckGameEngine.QuestData;
import ckPythonInterpreterTest.CKArtifactQuestRunner;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class DialogEditorFrame extends JFrame
implements CKEntitySelectedListener<JPanel>,ChangeListener,WindowListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AbstractCKGraphEditor editor;
	JPanel east;
	JPanel buttonPanel = new JPanel(new FlowLayout());
	JPanel p = new JPanel(new BorderLayout());
	AbstractGraph ag;
	String data;
	
	
	public DialogEditorFrame(AbstractGraph g)
	{
		
		
		ag = g;

		p.setPreferredSize(new Dimension(800,700));

		editor = new AbstractCKGraphEditor(ag,new DialogVertex(),new EdgeLink());
		editor.addEntitySelectedListener(this);
		editor.addChangeListener(this);
		east = new JPanel();
		east.add(new JLabel("waiting for donuts"));
		p.add(east,BorderLayout.EAST);
		p.add(editor,BorderLayout.CENTER);
	
		

		JButton saveButton = new JButton("Print Graph");
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
		
		JButton loadButton = new JButton("Test Load");
		buttonPanel.add(loadButton);
		loadButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				ag = AbstractGraph.readFromXMLString(data);
				editor = new AbstractCKGraphEditor(ag,new VertexNode(),new EdgeLink());
				editor.addEntitySelectedListener(DialogEditorFrame.this);
				resetViews();
				
			}
			
		});
		
		p.add(buttonPanel,BorderLayout.SOUTH);
		add(p);
		pack();
		setVisible(true);
		this.addWindowListener(this);
			
	}
	
	
	@Override
	public void entitySelected(JPanel entity)
	{	
		
		east=entity;
		resetViews();
	}

	public void resetViews()
	{
		p.removeAll();
		p.add(editor,BorderLayout.CENTER);
		p.add(east,BorderLayout.EAST);
		p.add(buttonPanel,BorderLayout.SOUTH);
		p.revalidate();

	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		AbstractGraph ag = new AbstractGraph();
		
		//reuse test data...
		QuestData q = CKArtifactQuestRunner.createTestQuest().getQuestData();
		//add the hidden node...
		
		ag.setSecretParent(q);
		
		JFrame frame = new DialogEditorFrame(ag);
		frame.pack();
		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}


	Vector<ChangeListener> listeners=new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		//Propogate the change up..
		for(ChangeListener l:listeners)
		{
			l.stateChanged(e);
		}
		
	}


	public Layout<VertexNode, EdgeLink> getGraphLayout()
	{
		return editor.getGraphLayout();
	}

	
	/* window events */

	@Override
	public void windowOpened(WindowEvent e)
	{
		//do nothing
		
	}


	@Override
	public void windowClosing(WindowEvent event)
	{
		//check that there is a startnode
		VertexNode start = editor.getGraph().getStartNode();
		
		if(start==null)
		{
			String Q = "This Graph does not contain a Start Node"; 
			if(popup(Q)!=JOptionPane.YES_OPTION) { return; }
		}
		
		Vector<VertexNode> ends= editor.getGraph().getEndNodes();
		if(ends.size()==0)
		{
			String Q = "This Graph does not contain any End Nodes"; 
			if(popup(Q)!=JOptionPane.YES_OPTION) { return; }			
		}
		
		if (start != null && ends.size() > 0)
		{
			// check for orphans and deadends
			Collection<VertexNode> nodes = editor.getGraph().getVertices();
			// orphans
			for (VertexNode n : nodes)
			{
				if (!editor.hasPath(start, n))
				{
					String Q = "Node " + n.getAID() + " is Unreachable";
					if (popup(Q) != JOptionPane.YES_OPTION) { return; }
				}

				boolean deadend = true;
				for (VertexNode e : ends)
				{
					if (editor.hasPath(n, e))
					{
						deadend = false;
						break;
					}
				}
				if (deadend)
				{
					String Q = "Node " + n.getAID() + " cannot reach the end";
					if (popup(Q) != JOptionPane.YES_OPTION) { return; }
				}
			}

		}
		
				
		//triggerchange
		stateChanged(null);
		this.dispose();
	}

	private int popup(String Q)
	{
		return JOptionPane.showConfirmDialog(
			    this,
			    Q+", Continue Anyway?", "Sanity Check",
			    JOptionPane.YES_NO_CANCEL_OPTION);
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		//do nothing
	}


	@Override
	public void windowIconified(WindowEvent e)
	{
		//do nothing
	}


	@Override
	public void windowDeiconified(WindowEvent e)
	{
		//do nothing
	}


	@Override
	public void windowActivated(WindowEvent e)
	{
		//do nothing
	}


	@Override
	public void windowDeactivated(WindowEvent e)
	{
		//do nothing
	}

	
}
