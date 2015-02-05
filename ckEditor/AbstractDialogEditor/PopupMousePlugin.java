/*
 * PopupVertexEdgeMenuMousePlugin.java
 *
 * Created on March 21, 2007, 12:56 PM; Updated May 29, 2007
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */

package ckEditor.AbstractDialogEditor;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;



public class PopupMousePlugin<V extends VertexNode, E extends EdgeLink> 
	extends AbstractPopupGraphMousePlugin 
{
    //private JPopupMenu edgePopup, vertexPopup;
    
    /** Creates a new instance of PopupVertexEdgeMenuMousePlugin */
    public PopupMousePlugin() {
        this(MouseEvent.BUTTON3_MASK);
    }
    
    /**
     * Creates a new instance of PopupVertexEdgeMenuMousePlugin
     * @param modifiers mouse event modifiers see the jung visualization Event class.
     */
    public PopupMousePlugin(int modifiers) {
        super(modifiers);
    }
    
    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where the 
     * work gets done. You shouldn't have to modify unless you really want to...
     * @param e 
     */  
	protected void handlePopup(MouseEvent e) 
	{
		Point2D p = e.getPoint();
		
		
		@SuppressWarnings("unchecked")
		final VisualizationViewer<V,E> vv =
                (VisualizationViewer<V,E>)e.getSource();
      
		GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        
		if(pickSupport != null) 
		{
            final V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            if(v != null) 
            {
            	JPopupMenu menu = new JPopupMenu("Vertex Options");
            	menu.add(new JMenuItem("Vertex Options"));
            	menu.add(new JSeparator());
            	JMenuItem delVertex = new JMenuItem("delete vertex");
            	delVertex.addActionListener(new DelVertex(v,vv));
            	menu.add(delVertex);
            	
            	menu.add(v.getPopUpMenuOptions((AbstractCKGraphEditor)vv));
            	
            	//updateVertexMenu(v, vv, p);

                menu.show(vv, e.getX(), e.getY());
            } else {
                final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
                if(edge != null) 
                {
                	JPopupMenu menu = new JPopupMenu("Edge Options");
                	menu.add(new JMenuItem("Edge Options"));
                	menu.add(new JSeparator());
                	JMenuItem delEdge = new JMenuItem("delete edge");
                	delEdge.addActionListener(new DelEdge(edge,vv));
                	menu.add(delEdge);
                	
                	menu.add(edge.getPopUpMenuOptions((AbstractCKGraphEditor)vv));
                	           	
                	
                	//updateEdgeMenu(edge, vv, p);
                    menu.show(vv, e.getX(), e.getY());
                  
                }
            }
        }
    }
    
   class DelVertex implements ActionListener
   {

	   VertexNode v;
	   VisualizationViewer<V, E> vv;
	   
	   public DelVertex(VertexNode v, VisualizationViewer<V, E> vv)
	   {
		   this.v=v;
		   this.vv=vv;
	   }
	   
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		v.getG().removeVertex(v);
		//vv.repaint();
		vv.stateChanged(new ChangeEvent(this));

	}
	   
   }
   
   class DelEdge implements ActionListener
   {

	   EdgeLink e;
	   VisualizationViewer<V, E> vv;
	   
	   public DelEdge(EdgeLink e, VisualizationViewer<V, E> vv)
	   {
		   this.e=e;
		   this.vv=vv;
	   }
	   
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		e.getG().removeEdge(e);
		vv.stateChanged(new ChangeEvent(this));
//		vv.repaint();

	}
	   
   }
   
   

}
