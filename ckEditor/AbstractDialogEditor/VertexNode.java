package ckEditor.AbstractDialogEditor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.beans.XMLEncoder;
import java.io.OutputStream;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.border.TitledBorder;
import org.apache.commons.collections15.Factory;

import ckCommonUtils.CKXMLAsset;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKSingleParent;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckGameEngine.actions.CKNullAction;
import ckGameEngine.actions.CKSequentialAction;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;

public class VertexNode  implements Factory<VertexNode>,CKXMLAsset<VertexNode>{

	private boolean startNode=false;
	private boolean endNode=false;
	private boolean randomNode=false;
	private String description="";
	private int vid=-1;
	private String AID="";
	
	private CKSingleParent singleParent=null;
	//This variable is here to make XMLENcode work smoothly.
	@SuppressWarnings("unused")
	private CKGUINode childNode;
	
	private Point2D pos=new Point();
	
	
	
	private AbstractGraph g=null;
	
	//private static int instanceCount=1;
	
	public VertexNode() {
		this("r");
	}
	

	public VertexNode(String r) {
		setDescription(r);
		this.singleParent = new CKSingleParent();
		//this.singleParent.add(new CKNullAction());
		setChildNode(new CKNullAction());
	}
	
	public VertexNode(String r, AbstractGraph g)
	{
		this(r);
		setGraph(g);
//??		setSingleParent(g.getHiddenNode());
	
	}
	
	public Point2D getPos()
	{
		return pos;
	}


	public void setPos(Point2D pos)
	{
		this.pos = pos;
	}


	
	
	


	//named oddly to stop the XML from recording.
	AbstractGraph getG() 
	{
		return this.g;
	}


	/**
	 * @return the startNode
	 */
	public boolean isStartNode() {
		return startNode;
	}
	/**
	 * @param startNode the startNode to set
	 */
	public void setStartNode(boolean startNode) 
	{
		if(startNode==true && this.getG()!=null) //clear existing nodes
		{
			for(VertexNode nn: this.getG().getVertices())
			{
			   nn.setStartNode(false);	
			}
		}
		this.startNode = startNode;
		
	}

	/**
	 * @return the endNode
	 */
	public boolean isEndNode() {
		return endNode;
	}

	/**
	 * @param endNode the endNode to set
	 */
	public void setEndNode(boolean endNode) {
		this.endNode = endNode;
	}
	/**
	 * @return the randomNode
	 */
	public boolean isRandomNode()
	{
		return randomNode;
	}


	/**
	 * @param randomNode the randomNode to set
	 */
	public void setRandomNode(boolean randomNode)
	{
		this.randomNode = randomNode;
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





	public void setGraph(AbstractGraph g2) 
	{
		this.g = g2;
		singleParent.removeFromParent();
		if(g2.getHiddenNode()!=null)
		{
			 g2.getHiddenNode().addIT(singleParent);
		}
	}
	
	public void doAction(CKGameActionListenerInterface listener, CKSpellCast cast)
	{
		((CKGameAction) singleParent.getChildAt(0)).doAction(listener, cast);
	}
	
		


	@Override
	synchronized public VertexNode create()
	{
		int max= vid+1;

		for(VertexNode nn: this.getG().getVertices()){
			if(nn.getVid()>=max) {max=nn.getVid()+1;}
		}
		VertexNode v = new VertexNode("new node "+max);
		v.setGraph(g);
		v.setVid(max);
		v.setDescription("No Description");
		return v;
		
	}

	
	public JMenuItem getPopUpMenuOptions(final AbstractCKGraphEditor ge)
	{
		JMenuItem item = new JMenuItem("Edit Node Properties...");
    
		item.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				CKXMLAssetPropertiesEditor<VertexNode> ed = getXMLPropertiesEditor(); 
				ge.notifyEditListener(ed);
				ed.addChangeListener(ge);
			}

		});
		return item;
	}
	
	

	
	public static void main(String [] args)
	{
		XMLEncoder xmlEncoder = new XMLEncoder(System.out);

		VertexNode v = new VertexNode("r",new AbstractGraph());
		//v.setVid(10);
		//v.startNode=true;
		v.getSingleParent().add(new CKSequentialAction());
		
		//Nodes first
//		xmlEncoder.writeObject(v.getSingleParent());
		xmlEncoder.writeObject(v);
		
		xmlEncoder.close();
	}


	@Override
	public void writeToStream(OutputStream out)
	{
		XMLEncoder xmlEncoder = new XMLEncoder(out);
		xmlEncoder.writeObject(this);
		xmlEncoder.close();
	}


	/**
	 * @return the vid
	 */
	public int getVid()
	{
		return vid;
	}


	/**
	 * @param vid the vid to set
	 */
	public void setVid(int vid)
	{
		this.vid = vid;
		setAID("V"+vid);
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


	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}


	@Override
	public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
	{
	//for now boring...
		JComponent comp = new JLabel(description);
		//Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(AID);
		comp.setBorder(title);
		return comp;
	}


	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<VertexNode> getXMLPropertiesEditor()
	{
		return new VertexNodePropertiesEditor(this);
		
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return AID;
	}
	
	
	private Dimension dim=new Dimension(100,50);
	
	
	

	/**
	 * @return the dim
	 */
	public Dimension getDim()
	{
		return dim;
	}


	/**
	 * @param dim the dim to set
	 */
	public void setDim(Dimension dim)
	{
		this.dim = dim;
	}


	public Icon getIcon()
	{
		Dimension d = getDim();
		return new ImageIcon(CKGraphicsPreviewGenerator.createComponentPreview(
				this.getXMLAssetViewer(),d.width,d.height));
	}


	
	
	

}





