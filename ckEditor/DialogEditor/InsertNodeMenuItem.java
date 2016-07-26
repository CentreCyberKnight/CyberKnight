package ckEditor.DialogEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ckEditor.DialogEditor.GraphElements.NateEdgeFactory;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class InsertNodeMenuItem extends JMenuItem implements EdgeMenuListener<NateLink> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4342197092931105145L;
	private NateLink edge;
    private VisualizationViewer<NateNode, NateLink> visComp;
    
    /** Creates a new instance of DeleteEdgeMenuItem */
    public InsertNodeMenuItem() {
        super("Add Node");
        this.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                visComp.getPickedEdgeState().pick(edge, false);
                NateNode n1 = createNateNode("I'm an inserted Node");
                visComp.getGraphLayout().getGraph().addEdge(createNateLink("New Edge", n1, edge.getDestination()),n1, edge.getDestination(),EdgeType.DIRECTED);
                visComp.getGraphLayout().getGraph().addEdge(createNateLink(edge.getDialogOption(),edge.getSource(), n1),edge.getSource(), n1, EdgeType.DIRECTED);
                //Collection edgeCollection = visComp.getGraphLayout().getGraph().getVertices();
                //basically, you have a new vertex, hook the edge into it AFTER you hook an new edge from that new vertex to
                //the ending vertex
                //for(NateNode nn : edgeCollection) {
                
                //}
                visComp.getGraphLayout().getGraph().removeEdge(edge);
                visComp.repaint();
                //TODO n1.setLocation to middle 
            }
        });
    }

    /**
     * Implements the EdgeMenuListener interface to update the menu item with info
     * on the currently chosen edge.
     * @param edge 
     * @param visComp 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void setEdgeAndView(NateLink edge, VisualizationViewer visComp) {
        this.edge = edge;
        this.visComp = visComp;
        this.setText("Insert Node After " + edge.toString());
    }

    private NateNode createNateNode(String s) {
		NateNode genNateNode= new NateNode(s); 
		genNateNode.setG((CKDialogGraph) visComp.getGraphLayout().getGraph());
		genNateNode.setSpeaker("New Node Says");
		int max= 0;
		for(NateNode nn: genNateNode.getG().getVertices()){
			if(nn.getId()>=max) {max=nn.getId()+1;}
		}
		genNateNode.setId(max);
		return genNateNode;
	}

	private NateLink createNateLink(String s, NateNode source, NateNode destination) {
		NateLink nl = NateEdgeFactory.getInstance().create();
		nl.setDialogOption(s);
		nl.setSource(source);
		nl.setDestination(destination);
		return nl;
	}
	
}
