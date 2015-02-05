package ckEditor.AbstractDialogEditor;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;
import ckCommonUtils.CKEntitySelectedListener;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKHiddenNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class AbstractGraph extends DirectedSparseMultigraph<VertexNode, EdgeLink>
{

	
	private static final long serialVersionUID = -674253551462995178L;
	
	private CKHiddenNode hiddenNode;
	private Object startNode;
	

	public AbstractGraph()
	{
		this.hiddenNode=new CKHiddenNode();
		this.startNode=null;
	}
	

	public String getXMLString()    { return getXMLString(null); }
	
	public String getXMLString(Layout<VertexNode,EdgeLink> layout) 
	{
		//store positional data in the nodes.
		if(layout!=null)
		{
			for(VertexNode v:getVertices())
			{
				v.setPos(layout.transform(v));		
			}
		}
		//store link data in edges
		for( EdgeLink e:this.getEdges())
		{
			e.setSourceId(this.getSource(e).getVid());
			e.setDestinationId(this.getDest(e).getVid());
		}

		//time to write out...
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder xmlEncoder = new XMLEncoder(baos);
		
		//System.out.println(this.getVertices());
		Vector<VertexNode> V = new Vector<>();
		V.addAll(getVertices());
		for(VertexNode v: V)
		{
			System.out.println(v.getVid());
		}
		//Nodes first
		xmlEncoder.writeObject(V);
		Vector<EdgeLink> E = new Vector<>();
		E.addAll(getEdges());
		
		
		xmlEncoder.writeObject(E);
		
		xmlEncoder.close();
	
		
		return baos.toString();
	}
	
	public static AbstractGraph readFromXMLString(String s)
	{
		AbstractGraph g = new AbstractGraph();
		ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
		
		//first read in nodes
		XMLDecoder xmldecoder = new XMLDecoder(bais);
		@SuppressWarnings("unchecked")
		Collection<VertexNode> nodes = (Collection<VertexNode>) xmldecoder.readObject();
		
		HashMap<Integer,VertexNode> map=new HashMap<Integer,VertexNode>();
		for(VertexNode v: nodes)
		{
			map.put(v.getVid(),v);
			g.addVertex(v);
			v.setGraph(g);
		}
		
		//then read in edges
		@SuppressWarnings("unchecked")
		Collection<EdgeLink> edges = (Collection<EdgeLink>) xmldecoder.readObject();
		for(EdgeLink e:edges)
		{
			g.addEdge(e, map.get(e.getSourceId()), map.get(e.getDestinationId()));
			e.setGraph(g);
		}
		
		
		
		xmldecoder.close();
		
		
		
		return g;
	}
	
	
		
	public static void main(String[] args)
	{
		TestGraphEditor.main(args);
	}

	public CKHiddenNode getHiddenNode() {
		return this.hiddenNode;
	}
	
	public void setSecretParent(CKGUINode node)
	{
		hiddenNode.secretParent(node);
	}
	
	
	Vector<CKEntitySelectedListener<JPanel>> nodeEditListeners = new Vector<CKEntitySelectedListener<JPanel>>();
	
	public void addNodeEditListener(CKEntitySelectedListener<JPanel> entitySelectedListener) 
	{
		nodeEditListeners.add(entitySelectedListener);
	}
	
	Vector<CKEntitySelectedListener<JPanel>> edgeEditListeners = new Vector<CKEntitySelectedListener<JPanel>>();
	
	public void addEdgeEditListener(CKEntitySelectedListener<JPanel> entitySelectedListener) 
	{
		edgeEditListeners.add(entitySelectedListener);
	}

	public void notifyNodeEditListener(JPanel jp) 
	{
		for(CKEntitySelectedListener<JPanel> entitySelectedListenerJPanel: nodeEditListeners)
		{
			entitySelectedListenerJPanel.entitySelected(jp);
		}
	}
	
	public void notifyEdgeEditListener(JPanel jp) 
	{
		for(CKEntitySelectedListener<JPanel> entitySelectedListenerJPanel: edgeEditListeners)
		{
			entitySelectedListenerJPanel.entitySelected(jp);
		}
	}


	public VertexNode getStartNode()
	{
		for(VertexNode nn: this.getVertices())
		{
			if(nn.isStartNode())
			{return nn;}
		}
	
		return null;
	}


	public Vector<VertexNode> getEndNodes()
	{
		Vector<VertexNode> vec = new Vector<VertexNode>();
		for(VertexNode nn: this.getVertices())
		{
			if(nn.isEndNode())
			{vec.add(nn); }
		}
	
		return vec;
		
		
	}
}
