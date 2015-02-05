package ckEditor.AbstractDialogEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections15.Transformer;


import ckCommonUtils.CKEntitySelectedListener;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.ObservableCachingLayout;

public class AbstractCKGraphEditor extends VisualizationViewer<VertexNode, EdgeLink> 
{


	private static final long serialVersionUID = -7730453232623716572L;

	AbstractGraph g;
		
	private VertexNode vFactory;
	private EdgeLink   eFactory;
	private DijkstraDistance<VertexNode, EdgeLink> distances;
	
	
	public AbstractCKGraphEditor(AbstractGraph g,VertexNode v, EdgeLink e) 
	{
		super(new StaticLayout<VertexNode,EdgeLink>(g,locationTransformer),
				new Dimension(800,900));
		
		this.g=g;
		distances = new DijkstraDistance<VertexNode, EdgeLink>(g);
		this.vFactory=v;
		this.eFactory=e;
		vFactory.setGraph(g);
		eFactory.setGraph(g);

		setRenderSettings();
		
		
		setMouseSettings();
		
		this.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				distances.reset();				
			}
		});
	
	

	}

	
	
	private void setRenderSettings()
	{
		setBackground(Color.WHITE);
		
		//draw icon/shape vertexes
				getRenderer().setVertexRenderer(new MultiVertexRenderer<VertexNode,EdgeLink>());
				this.getRenderContext().setVertexIconTransformer(vertexIconTransformer);
		        getRenderContext().setVertexShapeTransformer(new vertexShape());
		        getRenderContext().setVertexFillPaintTransformer(color);

		        //use toString for labels
		        //getRenderContext().setVertexLabelTransformer(new ToStringLabeller<VertexNode>());
		        getRenderer().setEdgeLabelRenderer(new BetterEdgeLabelRenderer<VertexNode,EdgeLink>());
		        getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<EdgeLink>());
		        
				//getRenderContext().setEdgeLabelClosenessTransformer(closeness);
		        //getRenderer().getVertexLabelRenderer().setPosition(Position.NE); 
				
	}
	
	private void setMouseSettings()
	{
		//mouse interaction
		EditingModalGraphMouse<VertexNode, EdgeLink> gm = 
				new EditingModalGraphMouse<VertexNode, EdgeLink>(
						getRenderContext(), 
						vFactory,eFactory); 
		
		
		// Trying out our new popup menu mouse plugin...
		PopupMousePlugin<VertexNode, EdgeLink> myPlugin =
				new PopupMousePlugin<VertexNode, EdgeLink>();
        
        
		gm.remove(gm.getPopupEditingPlugin());  // Removes the existing popup editing plugin
		gm.add(myPlugin);   // Add our new plugin to the mouse

		
		//need an editing mouse that will notify when an edge is potentially created
		EditingNotifyingMousePlugin<VertexNode,EdgeLink> myEditor = new
				EditingNotifyingMousePlugin<VertexNode,EdgeLink>();
		
		gm.add(myEditor);
		
		setGraphMouse(gm);
		
		
		//menu bar
		JMenuBar bar = new JMenuBar();
		JMenu modeMenu = gm.getModeMenu();
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null); // I'm using this in a main menu
		modeMenu.setPreferredSize(new Dimension(200,20)); // Change the size so I can see the text
		gm.setMode(ModalGraphMouse.Mode.EDITING); // Start off in editing mode
		
		JMenuItem refresh = new JMenuItem("Refresh Screen");
		refresh.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent arg0) {  stateChanged(null); 	}	});

				
				
		
		bar.add(modeMenu);
		bar.add(refresh);				
		add(bar);
		
	}
	
	public boolean hasPath(VertexNode s,VertexNode d)
	{
		return distances.getDistance(s,d)!=null;
	}
	
	
	 static Transformer<VertexNode, Point2D> locationTransformer =
			 new Transformer<VertexNode, Point2D>() 
	{
         @Override
         public Point2D transform(VertexNode vertex) { return vertex.getPos();  }
     };
	
     static Transformer<VertexNode,Icon> vertexIconTransformer =
    		 new Transformer<VertexNode,Icon>()
    		 {
				@Override
				public Icon transform(VertexNode v)	{    return v.getIcon(); 	}
    		 };
     
    class vertexShape extends AbstractVertexShapeTransformer<VertexNode>
    						implements Transformer<VertexNode, Shape>
    		 {
    		     public Shape transform(VertexNode v)
    		     {
    		    	 int margin = 10;
    		    	 int width = v.getDim().width;
    		    	 int height = v.getDim().height;
    		    	 
//    		    	 return new Rectangle(-105,-55,210,110);
//      		    	 return new Rectangle(0,0, width+margin,height+margin);
    		    	 return new Rectangle((width+margin)/-2,(height+margin)/-2,
    		    			 								width+margin,height+margin);
  		         
    		         //return factory.getRegularPolygon(v, 4); //Return the Shape, in this case a square, ie. 4 sides.
    		     }
    		 }

    
    
	Transformer<VertexNode,Paint> color = new Transformer<VertexNode,Paint>() 
	{
		@Override
		public Paint transform(VertexNode nn) 
		{
			
			VertexNode source = g.getStartNode();
			if(source==null) {return Color.RED;}
			
			if(distances.getDistance(source, nn)==null)
				{return Color.RED; }
			
			
			
			
			if(nn.isStartNode() && nn.isEndNode()) {return Color.ORANGE;}
			if(nn.isStartNode() && nn.isRandomNode()) {return Color.YELLOW;}
			if(nn.isEndNode()) {return Color.MAGENTA;}
			if(nn.isStartNode()) {return Color.GREEN;}
			if(nn.isRandomNode()) {return Color.CYAN;}
			return Color.WHITE;
		}
		
	};
    
	


	/**
	 * @return the graphLayout
	 */
	public ObservableCachingLayout<VertexNode, EdgeLink> getGraphLayout()
	{
		return (ObservableCachingLayout<VertexNode, EdgeLink>) this.model.getGraphLayout();
		//return graphLayout;
	}

	private Vector<CKEntitySelectedListener<JPanel>> listeners = new Vector<CKEntitySelectedListener<JPanel>>();

	public void addEntitySelectedListener(CKEntitySelectedListener<JPanel> listener) 
	{
		listeners.add(listener);
	}
	

	public void notifyEditListener(JPanel jp) 
	{
		for(CKEntitySelectedListener<JPanel> entitySelectedListenerJPanel: listeners)
		{
			entitySelectedListenerJPanel.entitySelected(jp);
		}
	}

	
	
	public AbstractGraph getGraph()
	{
		return g;
	}
	

	

public static void main(String[] args) 
{
	/*
	JFrame frame = new JFrame();
	frame.setLayout(new BorderLayout());
	AbstractGraph g = new AbstractGraph();
	AbstractCKGraphEditor ed = new AbstractCKGraphEditor(g,new VertexNode(),new EdgeLink());
	frame.add(ed,BorderLayout.CENTER);
	//frame.getContentPane().add(ed);
	frame.pack();
	frame.setVisible(true);*/
	
	TestGraphEditor.main(args);
		
	
}

}
