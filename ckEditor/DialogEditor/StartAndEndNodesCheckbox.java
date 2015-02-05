package ckEditor.DialogEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.*;

public class StartAndEndNodesCheckbox extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3179635740180262234L;
	
	private JCheckBox startNode;
	private JCheckBox endNode;
	private JCheckBox randomNode;
	private NateNode nn;
	
	
	public StartAndEndNodesCheckbox() {
	    //CheckboxGroup cbg = new CheckboxGroup();

		this.setLayout(new GridLayout(1,2));
	    startNode = new JCheckBox("Dailog begins here");
	    endNode = new JCheckBox("Dialog ends here");
	    this.add(startNode);
	    this.add(endNode);
	    
	}
	

	public StartAndEndNodesCheckbox(NateNode nn, JButton jb) {
		this.setLayout(new GridLayout(2,4));
	    ButtonGroup cbg = new ButtonGroup() 
	    {
	    private static final long serialVersionUID = 1L;

			public void setSelected(ButtonModel model, boolean selected) 
	    	{
	    		if (selected) {    super.setSelected(model, selected);  }
	    		else { clearSelection(); }
	        }
	     };
	    

	    startNode = new JCheckBox("Dailog begins here");
	    endNode = new JCheckBox("Dialog ends here");
	    randomNode = new JCheckBox("Random Dialog");

	    cbg.add(endNode);
	    cbg.add(randomNode);
	    startNode.addActionListener(this);
	    endNode.addActionListener(this);
	    randomNode.addActionListener(this);
	    this.add(startNode);
	    this.add(endNode);
	    this.add(randomNode);
	    this.add(jb);
	    this.nn=nn;

	    startNode.setSelected(this.nn.isStartNode());
	    endNode.setSelected(this.nn.isEndNode());
	    randomNode.setSelected(this.nn.isRandomNode());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setSize(200, 200);
		frame.add(new StartAndEndNodesCheckbox());		
		frame.setVisible(true);

	}


	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		
    	if(startNode.isSelected()) 
    	{
    		Collection<NateNode> arrayOfVert = nn.getG().getVertices();
    		for(NateNode i: arrayOfVert) { i.setStartNode(false); 		}
    	}	
    		
    	nn.setStartNode(startNode.isSelected());
    	nn.setEndNode(endNode.isSelected());
    	nn.setRandomNode(randomNode.isSelected());
	}

}
