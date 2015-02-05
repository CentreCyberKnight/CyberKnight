package ckEditor.DialogEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.io.GraphMLWriter;

public class DialogSaveMenu implements ActionListener {


	static Layout<NateNode,NateLink> layout;
	//int acc;
	//Layout<NateNode,NateLink> layout;
	public DialogSaveMenu(	Layout<NateNode,NateLink> g)
	{
		//this.layout=layout;
		DialogSaveMenu.layout=g;
		//acc=0;
	}
	
	

	public Layout<NateNode, NateLink> getGraph() {
		return layout;
	}



	public static void setGraph(Layout<NateNode, NateLink> graph) {
		layout = graph;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//layout.getGraph()
        CKDialogGraph g = (CKDialogGraph)layout.getGraph();

		for(NateNode nn: layout.getGraph().getVertices()) {
        	nn.setG(g);
        	for(NateLink nl: g.getOutEdges((NateNode) nn)) {
        		//if(vv.getGraphLayout().getGraph().getOutEdges((NateNode)nn)).
				//if(((NateLink)nl)==nl//((NateNode)nn).getId())
				//{
					nl.setSource(nn);
					nl.setDestination(g.getOpposite(nn, nl));
				//}
        	}
		}
		GraphMLWriter<NateNode,NateLink> graphMLwriter = new GraphMLWriter<NateNode,NateLink>();
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("DialogTest.xml")));
//			graphMLwriter.addVertexData("x", null, "0", 
//					new Transformer<NateNode,String>() {
//				public String transform(NateNode n) {
//					return Double.toString(((AbstractLayout) vv.getLayout()).getX(n)); 
//				}
//		}
//	);
//	graphMLwriter.addVertexData("y", null, "0", 
//			new Transformer<NateNode,String>() {
//				public String transform(NateNode n) {
//					return Double.toString(((AbstractLayout) vv.getLayout()).getY(n)); 
//				}
//		}
//	);
	graphMLwriter.setVertexIDs(new Transformer<NateNode,String>() {
		public String transform(NateNode n) {
			return n.getSpeaker()+n.getReply();
		}
	});
			
	graphMLwriter.addVertexData("reply", null, "huh", 
					new Transformer<NateNode, String>() {
		public String transform(NateNode n) {
			return n.getReply();
			}
		}
	);
	graphMLwriter.addVertexData("speaker", null, "nobody", 
					new Transformer<NateNode, String>() {
		public String transform(NateNode n) {
			return n.getSpeaker();
			}
		}
	);
	
	graphMLwriter.addVertexData("treedata", null, null, 
			new Transformer<NateNode, String>() {
		@SuppressWarnings("resource")
		public String transform(NateNode n) {
			//ByteArrayInputStream
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLEncoder xmlEncoder = new XMLEncoder(baos);
				xmlEncoder = new XMLEncoder(baos);
				xmlEncoder.writeObject(n.getSingleParent());
				xmlEncoder.close();
				//XMLUtils xmlu= new XMLUtils();
				
				String xml = baos.toString();
//Convert the supplied XML document into a form that is appropriate for passing 
				//in a string. This transforms quotes and < > characters into ", < and >. 
				System.out.println(xml);
			return "<![CDATA[" + xml + "]]>";
			
		}
			}
		);
		
	
	graphMLwriter.addVertexData("startnode", null, "F", 
					new Transformer<NateNode, String>() {
		public String transform(NateNode n) {
			if(n.isStartNode()) {return "T";}
			else {return "F";}
			}
		}
	);

	graphMLwriter.addVertexData("endnode", null, "F", 
					new Transformer<NateNode, String>() {
		public String transform(NateNode n) {
			if(n.isEndNode()) {return "T";}
			else {return "F";}
			}
		}
	);


	graphMLwriter.addVertexData("randomnode", null, "F", 
					new Transformer<NateNode, String>() {
		public String transform(NateNode n) {
			if(n.isRandomNode()) {return "T";}
			else {return "F";}
			}
		}
	);

	graphMLwriter.addVertexData("idNum", null, "0", 
					new Transformer<NateNode, String>() {
		public String transform(NateNode n) {
				return Integer.toString(n.getId());
			}
		}
	);
	graphMLwriter.addEdgeData("dialogOption", null, "well...", 
			new Transformer<NateLink, String> () {
		public String transform(NateLink nl) {
			return nl.getDialogOption();
			}
		}
	);

	graphMLwriter.addEdgeData("source", null, "null", 
			new Transformer<NateLink, String> () {
		public String transform(NateLink nl) {
			return Integer.toString(nl.getSource().getId());
			}
		}
	);

	graphMLwriter.addEdgeData("destination", null, "null", 
			new Transformer<NateLink, String> () {
		public String transform(NateLink nl) {
			return Integer.toString(nl.getDestination().getId());
			}
		}
	);
	
	graphMLwriter.save(layout.getGraph(),out);//(graph,out);
	System.out.println("Done Saving");
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			System.out.println("Didn't save");
			//ex.printStackTrace();
		}  
		
		
		
	}
}


