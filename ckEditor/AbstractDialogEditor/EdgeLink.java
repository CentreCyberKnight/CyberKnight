package ckEditor.AbstractDialogEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.OutputStream;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import org.apache.commons.collections15.Factory;

import ckCommonUtils.CKXMLAsset;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKSingleParent;
import ckGameEngine.CKSpellCast;
import ckSatisfies.Satisfies;
import ckSatisfies.TrueSatisfies;

public class EdgeLink implements Factory<EdgeLink>,CKXMLAsset<EdgeLink>
{

	String description="";
	int weight=1;

	private CKSingleParent singleParent=null;
	
	//needed for XMLEncode to work
	@SuppressWarnings("unused")
	private CKGUINode childNode=null;
	
	private int sourceId=-1;
	private int destinationId=-1;
	
	private AbstractGraph g;
	private String AID="";
	
	public EdgeLink() {

		this.singleParent = new CKSingleParent();
		setChildNode(new TrueSatisfies());
	}
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the weight
	 */
	public int getWeight()
	{
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight)
	{
		this.weight = weight;
	}

	/**
	 * @return the childNode
	 */
	public CKGUINode getChildNode()
	{
		return (CKGUINode) singleParent.getChildAt(0);
	}


	/**
	 * @param childNode the childNode to set
	 */
	public void setChildNode(CKGUINode childNode)
	{
		this.childNode = childNode;
		singleParent.add(childNode);
	}

	/**
	 * @return the singleParent
	 */
	public CKSingleParent getSingleParent()
	{
		return singleParent;
	}

	/**
	 * @param singleParent the singleParent to set
	 */
	public void setSingleParent(CKSingleParent singleParent)
	{
		this.singleParent = singleParent;
	}

	public int getSourceId() 		{   return sourceId; }

	public int getDestinationId() {   return destinationId; }

	
	public void setSourceId(int sourceId) {

		this.sourceId = sourceId;
	}

	public void setDestinationId(int destId) {
		this.destinationId = destId;
	}
	/*
	public VertexNode getSource() {
		return source;
	}

	public void setSource(int i) {
		this.source = i;
		setSourceId(i.getId());
		singleParent.removeFromParent();
		if(i.getG().getHiddenNode()!=null)
		{
			 i.getG().getHiddenNode().addIT(singleParent);
		}
		
	}
	
	public void setDestination(int i) {
		this.destination = i;
		setDestinationId(i.getId());
		
		singleParent.removeFromParent();
		if(i.getG().getHiddenNode()!=null)
		{
			i.getG().getHiddenNode().addIT(singleParent);
		}
	}

	public VertexNode getDestination() {
		return destination;
	}
*/


	public void setGraph(AbstractGraph abstractGraph)
	{
		g = abstractGraph;	
		
		singleParent.removeFromParent();
		if(g.getHiddenNode()!=null)
		{
			 g.getHiddenNode().addIT(singleParent);
		}
	}

	public boolean isSatisfied(CKSpellCast cast) 
	{
		return ((Satisfies) singleParent.getChildAt(0)).isSatisfied(cast);
	}

	@Override
	public EdgeLink create()
	{
		EdgeLink link =new EdgeLink();
		link.setGraph(g);
		return link;
	}
	public JMenuItem getPopUpMenuOptions(final AbstractCKGraphEditor ge)
	{
		JMenuItem item = new JMenuItem("Edit Edge Properties...");
    
		item.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				CKXMLAssetPropertiesEditor<EdgeLink> ed = getXMLPropertiesEditor(); 
				ge.notifyEditListener(ed);
				ed.addChangeListener(ge);
			}

		});
		return item;
	}
	
	
	


	@Override
	public void writeToStream(OutputStream out)
	{
		XMLEncoder xmlEncoder = new XMLEncoder(out);
		xmlEncoder.writeObject(this);
		xmlEncoder.close();
	}



	@Override
	public String getAID()
	{
		return AID;
	}



	@Override
	public void setAID(String a)
	{
		AID=a;
		
	}

	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if(description.length()==0)
		{
			return "Edge from "+g.getSource(this).getAID() +" to "+
					g.getDest(this).getAID();
		}
		return description;

	}



	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}



	@Override
	public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
	{
		return new JLabel(toString());
	}



	@Override
	public CKXMLAssetPropertiesEditor<EdgeLink> getXMLPropertiesEditor()
	{
		return new EdgeLinkPropertiesEditor(this);
	}
	
	

	public AbstractGraph getG()
	{
		return g;
	}

	
	
	
}
