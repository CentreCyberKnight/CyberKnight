package ckEditor.DialogEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

import ckEditor.DialogEditor.GraphElements.NateEdgeFactory;
import ckEditor.DialogEditor.GraphElements.NateVertexFactory;
import ckEditor.treegui.CKHiddenNode;
import ckEditor.treegui.CKSingleParent;
import ckGameEngine.actions.CKDialogAction;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class CKDialogGraph extends DirectedSparseMultigraph<NateNode, NateLink> {

	
	private static final long serialVersionUID = 8633270045622743212L;
	
	
	CKHiddenNode hiddenNode;
	//SparseMultigraph<NateNode, NateLink> g; 
	NateNode startNode;
	
	private CKDialogGraph()
	{
		this.hiddenNode=new CKHiddenNode();
		this.startNode=null;
	}
	
	
	private CKDialogGraph(String xmlgraph)
	{
		this(xmlgraph, new CKHiddenNode());
	}
	
	private CKDialogGraph(String xmlGraph, CKHiddenNode hidden) {
		//GraphCreation gc = new GraphCreation(xmlGraph);
		try {
			loadFromString(xmlGraph);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	public NateNode getStartNode() {
		if(startNode==null) {
			for(NateNode nn: this.getVertices())
			{
				if(nn.isStartNode())
				{
					startNode=nn;
				}
			}
		}
		if(startNode==null) {
			System.out.println("No assigned StartNode, assigning random");
			if(this.getVertices().toArray().length==0)
			{
				System.out.println("No Nodes to start from");

			}
			startNode=(NateNode) this.getVertices().toArray()[0];
		}
		return startNode;
	}
	
	
	
	

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.graph.DirectedSparseMultigraph#addEdge(java.lang.Object, edu.uci.ics.jung.graph.util.Pair, edu.uci.ics.jung.graph.util.EdgeType)
	 */
	@Override
	public boolean addEdge(NateLink edge, Pair<? extends NateNode> endpoints,
			EdgeType edgeType) {
		// TODO Auto-generated method stub
		return super.addEdge(edge, endpoints, EdgeType.DIRECTED);
	}


	public static CKDialogGraph loadFromString(String xml) throws UnsupportedEncodingException
	{
		if(xml==null || xml.length()==0)
		{
			//SparseMultigraph<NateNode,NateLink> g = 
				//new SparseMultigraph<NateNode,NateLink>();
			return new CKDialogGraph();
		}
			
			//BufferedReader fileReader = new BufferedReader(new FileReader("DialogTest.xml"));
			//InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(xml.getBytes()));
			//InputStream is = new InputStream();
			InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			InputStreamReader reader = new InputStreamReader(new BufferedInputStream(is));//new StringReader(xml));
			//InputStreamReader reader = new InputStreamReader(is);
			System.out.println(xml);
			Transformer<GraphMetadata, CKDialogGraph> graphTransformer = new Transformer<GraphMetadata, CKDialogGraph>() 
			{
				public CKDialogGraph  transform(GraphMetadata metadata) 
				{
					return new CKDialogGraph();
				}
			};

			Transformer<NodeMetadata, NateNode> vertexTransformer = new Transformer<NodeMetadata, NateNode>() {
				
				@Override
				public NateNode transform(NodeMetadata metadata) {
					NateNode nn = NateVertexFactory.getInstance().create();
					nn.setReply(metadata.getProperty("reply"));
					nn.setSpeaker(metadata.getProperty("speaker"));
					nn.setId(Integer.parseInt(metadata.getProperty("idNum")));
					nn.setStartNode(metadata.getProperty("startnode").compareTo("T")==0);
					nn.setEndNode(metadata.getProperty("endnode").compareTo("T")==0);
					nn.setRandomNode(metadata.getProperty("randomnode").compareTo("T")==0);
					
					String metaxmlstring = metadata.getProperty("treedata");
					
					// convert String into InputStream
					InputStream is = new ByteArrayInputStream(metaxmlstring.getBytes());
					
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
			GraphMLReader2<CKDialogGraph, NateNode, NateLink> graphReader = new GraphMLReader2<CKDialogGraph, NateNode, NateLink>(reader, graphTransformer, vertexTransformer, edgeTransformer, hyperEdgeTransformer);
			//graphReader;
			//GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink> graphReader = loadStuff();//new GraphMLReader2<Graph<NateNode, NateLink>, NateNode, NateLink>(fileReader, graphTransformer, vertexTransformer, edgeTransformer, hyperEdgeTransformer);
			
			CKDialogGraph g=null;
			try {
					g = graphReader.readGraph();
					//graphReader.
				for(NateNode nn: g.getVertices()) {
					nn.setG(g);
					//g.hiddenNode.add(nn.getNodeAction());
					if(nn.isStartNode()) 
						{g.startNode=nn;}
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
					//return g;
				}
			} catch (GraphIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			System.out.println("VERTEX COUNT" + g.getVertexCount());
			return g;
				
		
		
		
				
	}

	public String getXMLString() {
		// TODO Auto-generated method stub
				//layout.getGraph()
		//FIXME when nodes are directed 
		for(NateNode nn:this.getVertices()) {
        	nn.setG(this);
        	for(NateLink nl: this.getOutEdges((NateNode) nn)) {
					nl.setSource(nn);
					nl.setDestination(this.getOpposite(nn, nl));
        	}
		}
				GraphMLWriter<NateNode,NateLink> graphMLwriter = new GraphMLWriter<NateNode,NateLink>();
				
				try {
					//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("DialogTest.xml")));
					StringWriter out = new StringWriter();

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
				public String transform(NateNode n) {
					//ByteArrayInputStream
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					XMLEncoder xmlEncoder = new XMLEncoder(baos);
						
						//TODO most likely place for this to break
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
			
			graphMLwriter.save(this,out);//(graph,out);
			return out.toString();
			//System.out.println("Done Saving");
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					return "Didn't save";
					//ex.printStackTrace();
				}  
	}
	
	public static void main(String[] args) 
	{
		String output = null;
		try
		{
			output = new Scanner(new File("DialogTest.xml")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		///CKDialogGraph cool = new CKDialogGraph(output, new CKHiddenNode());
		CKDialogGraph cool;
		try {
			cool = loadFromString(output);
			System.out.println(cool.getVertexCount());
			
			cool.visualizeGraph();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NateNode getDest(NateLink nateLink, NateNode nateNode) {
		if(nateLink.getDestination()==nateNode) {
			return nateLink.getSource();
		}
		return nateLink.getDestination();
	}

	public Collection<NateLink> getOutEdges(NateNode vertex) {
		return super.getOutEdges(vertex);
	}

	
	public Vector<CKDialogAction> changeListeners = new Vector<CKDialogAction>();
	public void addChangeListener(CKDialogAction ckDialogAction)
	{
		changeListeners.add(ckDialogAction);
	}
	
	public void notifyChanges()
	{
		for(CKDialogAction a:changeListeners)
		{
			a.stateChanged(null);//reportChange(this);
		}
	}
	
	
	public void attachToHiddenNode(CKHiddenNode hiddenNode) {
		this.hiddenNode=hiddenNode;
		for(NateNode nn: this.getVertices()) {
			this.hiddenNode.addIT(nn.getSingleParent());
			//TODO add link actions
		}
		for(NateLink nl: this.getEdges()) {
			this.hiddenNode.addIT(nl.getSingleParent());
			//TODO add link actions
		}
	}
	
	public void visualizeGraph() {

		JFrame frame = new JFrame("Nate's Kick-Ass Dialog Editor");
		//g = new SparseMultigraph<NateNode,NateLink>();
		Layout<NateNode,NateLink> layout = new CircleLayout<NateNode,NateLink>(this);
		//layout.s
		layout.setSize(new Dimension(800,900));
		final VisualizationViewer<NateNode,NateLink> vv = 
				new VisualizationViewer<NateNode,NateLink>(layout);
		vv.setPreferredSize(new Dimension(900,900));
		// Show vertex and edge labels
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<NateNode>());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<NateLink>());
		// Create a graph mouse and add it to the visualization viewer
				EditingModalGraphMouse<NateNode, NateLink> gm = new EditingModalGraphMouse<NateNode, NateLink>(vv.getRenderContext(), 
						GraphElements.NateVertexFactory.getInstance(),
						GraphElements.NateEdgeFactory.getInstance()); 
				//gm.getEditingPlugin().

				// Set some defaults for the Edges...
				GraphElements.NateEdgeFactory.setDefaultWeight(1);
				// Trying out our new popup menu mouse plugin...
				PopupVertexEdgeMenuMousePlugin<NateNode,NateLink> myPlugin = new PopupVertexEdgeMenuMousePlugin<NateNode,NateLink>();
				// Add some popup menus for the edges and vertices to our mouse plugin.
		        
		        
				JPopupMenu edgeMenu = new MouseMenues.EdgeMenu(frame);
				JPopupMenu vertexMenu = new MouseMenues.VertexMenu(frame);
				myPlugin.setEdgePopup(edgeMenu);
				myPlugin.setVertexPopup(vertexMenu);
				gm.remove(gm.getPopupEditingPlugin());  // Removes the existing popup editing plugin

				gm.add(myPlugin);   // Add our new plugin to the mouse

				vv.setGraphMouse(gm);
				vv.setBackground(Color.MAGENTA);

				//JFrame frame = new JFrame("Editing and Mouse Menu Demo");
				// MKB do not so this will close other stuff --frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(vv);

				// Let's add a menu for changing mouse modes
				JMenuBar menuBar = new JMenuBar();
				JMenu modeMenu = gm.getModeMenu();
				modeMenu.setText("Mouse Mode");
				modeMenu.setIcon(null); // I'm using this in a main menu
				modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size so I can see the text

				menuBar.add(modeMenu);
				frame.setJMenuBar(menuBar);
				gm.setMode(ModalGraphMouse.Mode.EDITING); // Start off in editing mode
				JMenu saveMenu = new JMenu("File System");
				JMenuItem saveG = new JMenuItem("Save 1");
				saveG.setAction(new SaveGraphAction());
				JMenuItem save = new JMenuItem("Save");
				save.addActionListener(new DialogSaveMenu(layout));
				JMenuItem load = new JMenuItem("Load");
				load.addActionListener(new DialogLoadMenu(vv));
				
				//Create a file chooser
				saveMenu.add(saveG);
				saveMenu.add(save);
				saveMenu.add(load);
				menuBar.add(saveMenu);
				frame.pack();
				frame.setVisible(true);    
				
				

		        Transformer<NateNode,Shape> vertexSize = new Transformer<NateNode,Shape>(){
		            public Shape transform(NateNode node){
		                //Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
		            	Rectangle rectangle = new Rectangle(-15,-15, 30, 30);
		            	
		                // in this case, the vertex is twice as large
		            	if(node.toString().length() > 20) return AffineTransform.getScaleInstance(4, 1).createTransformedShape(rectangle);
		                else if(node.toString().length() > 10) return AffineTransform.getScaleInstance(2, 1).createTransformedShape(rectangle);
		                else return rectangle;
		            }

		        };
		        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
				//Color color = new Color(255, 255, 255);
				Transformer<NateNode,Paint> color = new Transformer<NateNode,Paint>() {
					@Override
					public Paint transform(NateNode nn) {
						if(nn.isStartNode() && nn.isEndNode()) {return Color.BLUE;}
						if(nn.isEndNode()) {return Color.GRAY;}
						if(nn.isStartNode()) {return Color.PINK;}
						return Color.WHITE;
					}
					
				};
				vv.getRenderContext().setVertexFillPaintTransformer(color);
		        //vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
				//vv.getRenderer().setVertexRenderer(new MyRenderer());

				 //VertexLabelAsShapeRenderer<NateNode,NateLink> vlasr = new VertexLabelAsShapeRenderer<NateNode,NateLink>(vv.getRenderContext());
				 
				 
				 vv.getRenderContext().setVertexLabelTransformer(
						 new ChainedTransformer<NateNode,String>(new Transformer[]{
								 new ToStringLabeller<String>(),
								 new Transformer<String,String>() {
									 @Override
									 public String transform(String somestring) {
										 return "<html><center>" + somestring;//+ nn.getSpeaker() + "<p>"+nn.getReply();
									 }}}));
				// vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.red));
				//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
				vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<NateLink>());
				vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR); 
	}

	public CKHiddenNode getHiddenNode()
	{
		return hiddenNode;
	}
	
	class SaveGraphAction extends AbstractAction
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8968029770606877826L;


		public SaveGraphAction()
		{
			super("Save Graph");
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			notifyChanges();			
		}
		
	}

}
