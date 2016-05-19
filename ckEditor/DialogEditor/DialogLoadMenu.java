package ckEditor.DialogEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

import org.apache.commons.collections15.Transformer;

import ckEditor.DialogEditor.GraphElements.NateEdgeFactory;
import ckEditor.DialogEditor.GraphElements.NateVertexFactory;
import ckEditor.treegui.CKSingleParent;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class DialogLoadMenu implements ActionListener{


	VisualizationViewer<NateNode,NateLink> vv;
	
	public GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink> loadStuff()
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
					nn.setSpeaker(metadata.getProperty("speaker"));
					nn.setId(Integer.parseInt(metadata.getProperty("idNum")));
					if(metadata.getProperty("startnode").compareTo("T")==0)
					{
						nn.setStartNode(true);
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
					nn.setRandomNode(metadata.getProperty("randomNode").compareTo("T")==0);

					
					String metaxmlstring = metadata.getProperty("treedata");
					
					// convert String into InputStream
					InputStream is = new ByteArrayInputStream(metaxmlstring.getBytes());
					//ObjectInputStream ois = new ObjectInputStream(is);
					XMLDecoder xmldecoder = new XMLDecoder(is);
					CKSingleParent result = (CKSingleParent) xmldecoder.readObject();
					nn.setSingleParent(result);
					xmldecoder.close();
					
					return nn;
					
					
				}

			};
			Transformer<EdgeMetadata, NateLink> edgeTransformer = new Transformer<EdgeMetadata, NateLink>() {
				public NateLink transform(EdgeMetadata metadata) {
					NateLink nl = NateEdgeFactory.getInstance().create();
					nl.setDialogOption(metadata.getProperty("dialogOption"));
					nl.setDestinationId(Integer.parseInt(metadata.getProperty("destination")));
					nl.setSourceId(Integer.parseInt(metadata.getProperty("source")));

					//nl.setDestination(metadata.getProperty("destination")))
					return nl;
				}
			};

			Transformer<HyperEdgeMetadata, NateLink> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, NateLink>() {
				public NateLink transform(HyperEdgeMetadata metadata) {
					NateLink nl = NateEdgeFactory.getInstance().create();
					return nl;
				}
				
			};
			GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink> graphReader = new GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink>(fileReader, graphTransformer, vertexTransformer, edgeTransformer, hyperEdgeTransformer);
			return graphReader;
		} catch (FileNotFoundException e1) {
			System.out.println("FileNotFound");
			return null;
		}
	}
			
	@SuppressWarnings("unchecked")
	public DialogLoadMenu(@SuppressWarnings("rawtypes") VisualizationViewer vv)
	{
		this.vv=vv;
	}
		
		public void actionPerformed(ActionEvent e) {
			
				GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink> graphReader = loadStuff();//new GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink>(fileReader, graphTransformer, vertexTransformer, edgeTransformer, hyperEdgeTransformer);
				try {
					CKDialogGraph g = (CKDialogGraph) graphReader.readGraph();
					@SuppressWarnings({ "unchecked", "rawtypes" })
					Layout<NateNode, NateLink> layoutNew = new CircleLayout(g);
					layoutNew.setGraph(g);
					
					for(NateNode nn: g.getVertices()) {
						nn.setG(g);
						for(NateLink nl: g.getEdges()) {
							if(nl.getSourceId()==nn.getId())
							{
								nl.setSource(nn);
							}
							if(nl.getDestinationId()==nn.getId())
							{
								nl.setDestination(nn);
							}
						}
					}
					//layout=layoutNew;
					vv.setGraphLayout(layoutNew);
					DialogSaveMenu.setGraph(layoutNew);
					
			        // Show vertex and edge labels
				} catch (GraphIOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	

}
