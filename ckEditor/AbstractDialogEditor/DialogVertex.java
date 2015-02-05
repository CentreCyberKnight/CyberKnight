package ckEditor.AbstractDialogEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import ckEditor.CKXMLAssetPropertiesEditor;
import ckGameEngine.actions.CKSimpleGUIAction;

public class DialogVertex extends VertexNode
{

	
	CKSimpleGUIAction question = new CKSimpleGUIAction();
	
	
	
	
	
	
	
	/**
	 * @return the question
	 */
	public CKSimpleGUIAction getQuestion()
	{
		return question;
	}







	/**
	 * @param question the question to set
	 */
	public void setQuestion(CKSimpleGUIAction question)
	{
		this.question = question;
	}


	public String getSpeaker()
	{
		return question.getPc();
	}


	public String getReply()
	{
		return question.getMess();
	}




	/* (non-Javadoc)
	 * @see ckEditor.AbstractDialogEditor.VertexNode#setGraph(ckEditor.AbstractDialogEditor.AbstractGraph)
	 */
	@Override
	public void setGraph(AbstractGraph g2)
	{
		super.setGraph(g2);
		
		question.removeFromParent();
		if(g2.getHiddenNode()!=null)
		{
			 g2.getHiddenNode().addIT(question);
		}
	}




	public boolean showNewIcon()
	{
		if(question.getQuest()==null) {return false; } //messes up on XMLsave
	
		String pc = question.getQuest().getPCAssetId(question.getPc());
		return ! (isRandomNode() || isEndNode() ||  pc==null || pc=="");
		
	}


	/* (non-Javadoc)
	 * @see ckEditor.AbstractDialogEditor.VertexNode#getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum)
	 */
	@Override
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
	
		if(! showNewIcon())
		{
			return super.getXMLAssetViewer(v);
		}
		else
		{
			JPanel panel = new JPanel(new BorderLayout());
			panel.setOpaque(false);
			TitledBorder title = BorderFactory.createTitledBorder(getAID());
			panel.setBorder(title);
			
			String text = question.getPc()+" Asks \n\n"+question.getMess()+"\n\n("+getDescription()+")";
			JLabel label = new JLabel(question.getTreeIcon(true, true));
				
			panel.add(label,BorderLayout.LINE_START);

			JTextArea messBox=new JTextArea(text,2,15);
			messBox.setMargin(new Insets(5, 20, 5, 5));
			messBox.setOpaque(false);
			messBox.setEditable(false);
			//messBox.setFont()
			messBox.setLineWrap(true);
			messBox.setWrapStyleWord(true);
			//tPanel.add(messBox,BorderLayout.CENTER);
			//tPanel.doLayout();
			panel.add(messBox,BorderLayout.CENTER);
	
			
			
			//panel.add(question.getTreeCellRendererComponent(null,null,true,true,true, 0,true),BorderLayout.CENTER);
			//panel.add(new JLabel(getDescription(),JLabel.CENTER),BorderLayout.PAGE_END);
			
			
			return panel;	
		}
	}


	/* (non-Javadoc)
	 * @see ckEditor.AbstractDialogEditor.VertexNode#getDim()
	 */
	@Override
	public Dimension getDim()
	{
		if(showNewIcon())
		{
			return new Dimension(250,120);
		}
		else
		{
			return super.getDim();
		}
	}







	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<VertexNode> getXMLPropertiesEditor()
	{
		return new DialogVertexPropertiesEditor(this);
	}







	/* (non-Javadoc)
	 * @see ckEditor.AbstractDialogEditor.VertexNode#create()
	 */
	@Override
	synchronized public VertexNode create()
	{
		DialogVertex vert = new DialogVertex();
		int max= getVid()+1;

		for(VertexNode nn: this.getG().getVertices()){
			if(nn.getVid()>=max) {max=nn.getVid()+1;}
		}
		vert.setVid(max);
		vert.setDescription("No Description");
		vert.setGraph(getG());
		return vert;
	}
	
	
	
	
	
	
	
	
	

}
