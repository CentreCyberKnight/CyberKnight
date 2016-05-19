package ckEditor.DialogEditor;
/*
 * MyMouseMenus.java
 *
 * Created on March 21, 2007, 3:34 PM; Updated May 29, 2007
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A collection of classes used to assemble popup mouse menus for the custom
 * edges and vertices developed in this example.
 * @author Dr. Greg M. Bernstein
 */
public class MouseMenues {
    
    public static class EdgeMenu extends JPopupMenu {        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// private JFrame frame; 
        public EdgeMenu(final JFrame frame) {
            super("Edge Menu");
            // this.frame = frame;
            this.add(new DeleteEdgeMenuItem<NateLink>());
            this.add(new InsertNodeMenuItem());
            //this.addSeparator();
            //this.add(new CapacityDisplay());
            this.addSeparator();
            this.add(new EdgePropItem(frame));           
        }
        
    }
    
    public static class EdgePropItem extends JMenuItem implements EdgeMenuListener<NateLink>,
        MenuPointListener {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		NateLink edge;
        @SuppressWarnings("rawtypes")
		VisualizationViewer visComp;
        Point2D point;
        
        public void setEdgeAndView(NateLink edge, @SuppressWarnings("rawtypes") VisualizationViewer visComp) {
        	System.out.println("edge called");

            this.edge = edge;
            this.visComp = visComp;
        }

        public void setPoint(Point2D point) {
            this.point = point;
        }
        
        public EdgePropItem(final JFrame frame) {            
            super("Edit Edge Properties...");
            this.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    EdgePropertyDialog dialog = new EdgePropertyDialog(frame, edge);
                    dialog.setLocation((int)point.getX()+ frame.getX(), (int)point.getY()+ frame.getY());
                    dialog.setVisible(true);
                }
                
            });
        }
        
    }
    
    public static class VertexMenu extends JPopupMenu {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public VertexMenu(final JFrame frame) {
            super("Vertex Menu");
            this.add(new DeleteVertexMenuItem<NateNode>());
            this.addSeparator();
            this.add(new NodePropItem(frame)); 
        }
        
    }
    
    public static class NodePropItem extends JMenuItem implements VertexMenuListener<NateNode>,
    MenuPointListener {

    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	NateNode node;
    @SuppressWarnings("rawtypes")
	VisualizationViewer visComp;
    Point2D point;
    public void setVertexAndView(NateNode node, @SuppressWarnings("rawtypes") VisualizationViewer visComp) {
    	System.out.println("node called");
        this.node = node;
        this.visComp = visComp;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }
    
    public NodePropItem(final JFrame frame) {
        super("Edit Node Properties...");
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NodePropertyDialog dialog = new NodePropertyDialog(frame, node);
                dialog.setLocation((int)point.getX()+ frame.getX(), (int)point.getY()+ frame.getY());
                dialog.setVisible(true);
                frame.repaint();
            }
            
        });
    }


    

    
}
    
}
