package ckEditor.DialogEditor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.apache.commons.collections15.Transformer;

import ckEditor.DialogEditor.GraphElements.NateEdgeFactory;
import ckEditor.DialogEditor.GraphElements.NateVertexFactory;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;

public class ckDialogUse extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
    
    GraphMLReader2<Graph<NateNode, NateLink>, NateNode,NateLink> graphML;
    Graph<NateNode, NateLink> g;
    NateNode currentNode;
    NateLink[] possibleReplies;
    
    /**
     * creates class
     */
    public ckDialogUse() {
    	setGraphToStart();
    }
    
    /**
     * @return text said to player
     */
    public String getSaidToPlayer() {
    	return currentNode.getReply();
    }
    
    /**
     * gets reply option for display from particular edge
     * @param edge
     * @return reply option
     */
    public String getPlayerReplyOption(NateLink edge) {
    	return edge.getDialogOption();
    }


    /**
     * @return current node
     */
    public NateNode getCurrentNode() {
		return currentNode;
	}

    /**
     * sets current node
     * @param currentNode
     */
	public void setCurrentNode(NateNode currentNode) {
		this.currentNode = currentNode;
	}
	
	public void choosePathByInt(int i)
	{
		choosePathByEdge(possibleReplies[i-1]);
	}

	/**
	 * Allows character to choose dialog path, private
	 * @sets current node to new node
	 * @param edge
	 */
	private void choosePathByEdge(NateLink edge)
    {
    	//check to make sure meet edge requirements
    	NateNode nn = (NateNode) g.getDest(edge);
    	setCurrentNode(nn);
    	//innact any actions that need to take place
    }
    
	/**
	 * Gets outgoing edges from current node
	 * @return Collection of NateLink which may be displayed to allow user to choose path
	 */
    public NateLink[] getPossibleRepliesFromCurrentNode()
    {
    	return getPossibleReplies(getCurrentNode());
    }
    
    /**
     * Gets outgoing edges from vertex, private
     * @param vertex
     * @return Collection of NateLink which may be displayed to allow user to choose path
     */
    private NateLink[] getPossibleReplies(NateNode vertex) {
    	//Add in Check, don't display nodes that don't meet edge requirements
    	NateLink[] nna;
		int possibileReplyCount = g.getOutEdges(vertex).toArray().length;
		nna= new NateLink[possibileReplyCount];
		for(int i = 0; i<possibileReplyCount; i++) 
		{
			nna[i] = (NateLink) g.getOutEdges(vertex).toArray()[i];
			System.out.println(i+1 + ": " + nna[i].getDialogOption());
		}
		possibleReplies = nna;
    	return nna;
    }
    
    /**
     * This call will do all appropriate actions and then return the approriate CKDialogMessage?
     */
    
    
    
    
    /**
     * reloads graph, thus reseting all variables. 
     * WARNING: Does not reset any consequences that may have been innacted by traversing graph
     */
    public void setGraphToStart() {
		
    	loadStuff();
		if(this.getCurrentNode()==null)
		{
			System.out.println("No start node assigned. Assigning one randomly");
			if(!g.getEdges().isEmpty()&&!g.getVertices().isEmpty());
			{
				setCurrentNode((NateNode) g.getVertices().toArray()[0]);
				System.out.println(getCurrentNode().getReply());
			}
		}
		
		//undo any actions? 
    }
    
    /**
     * This will load the graph from the the DialogTest.xml 
     * @assigns currentNode to the start node
     * @assigns g
     * @assigns graphML
     */
    public void loadStuff()
	{
		//JFileChooser j = new JFileChooser();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader("DialogTest.xml"));
			Transformer<GraphMetadata, Graph<NateNode,NateLink>> graphTransformer = new Transformer<GraphMetadata, Graph<NateNode,NateLink>>() {
				public Graph<NateNode, NateLink> 
				 transform(GraphMetadata metadata) {

						return new SparseMultigraph<NateNode,NateLink>();
						
				}
			};
			Transformer<NodeMetadata, NateNode> vertexTransformer = new Transformer<NodeMetadata, NateNode>() {
				
				@Override
				public NateNode transform(NodeMetadata metadata) {
					NateNode nn = NateVertexFactory.getInstance().create();
					nn.setReply(metadata.getProperty("reply"));
					if(metadata.getProperty("startnode").compareTo("T")==0)
					{
						nn.setStartNode(true);
						currentNode=nn;
					}
					else {
						nn.setStartNode(false);
					}
					if(metadata.getProperty("endnode").compareTo("T")==0){
						nn.setEndNode(true);
					}
					else {
						nn.setEndNode(false);
					}
					return nn;
				}

			};
			Transformer<EdgeMetadata, NateLink> edgeTransformer = new Transformer<EdgeMetadata, NateLink>() {
				public NateLink transform(EdgeMetadata metadata) {
					NateLink nl = NateEdgeFactory.getInstance().create();
					nl.setDialogOption(metadata.getProperty("dialogOption"));
					return nl;
				}
			};

			Transformer<HyperEdgeMetadata, NateLink> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, NateLink>() {
				public NateLink transform(HyperEdgeMetadata metadata) {
					NateLink nl = NateEdgeFactory.getInstance().create();
					return nl;
				}
				
			};
			graphML = new GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink>(fileReader, graphTransformer, vertexTransformer, edgeTransformer, hyperEdgeTransformer);
			try {
				g = graphML.readGraph();
				
			} catch (GraphIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("FileNotFound");
		}
		
	}
    
	public static void main(String[] args) {
		ckDialogUse ckD = new ckDialogUse();
		System.out.println("Current Node is " + ckD.getCurrentNode().getReply());
		System.out.println("Possible Replies are ");
		NateLink []nateLinkOptions= ckD.getPossibleRepliesFromCurrentNode();
		System.out.println("following first path");
		ckD.choosePathByEdge(nateLinkOptions[0]);
		System.out.println("Current Node is " + ckD.getCurrentNode().getReply());
		for(int i=0; i < 10; i++)
		{
			nateLinkOptions= ckD.getPossibleRepliesFromCurrentNode();
			System.out.println("following first path");
			ckD.choosePathByEdge(nateLinkOptions[0]);
			System.out.println("Current Node is " + ckD.getCurrentNode().getReply());
		}
		ckD.choosePathByInt(1);
		System.out.println("Current Node is " + ckD.getCurrentNode().getReply());
		for(int i=0; i < 10; i++)
		{
			nateLinkOptions= ckD.getPossibleRepliesFromCurrentNode();
			System.out.println("following first path");
			ckD.choosePathByInt(1);
			System.out.println("Current Node is " + ckD.getCurrentNode().getReply());
		}
		

		
		
		
	}
}
