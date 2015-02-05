package ckEditor.DialogEditor;

/*
 * GraphElements.java
 *
 * Created on March 21, 2007, 9:57 AM
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */


import org.apache.commons.collections15.Factory;

/**
 *
 * @author Dr. Greg M. Bernstein
 */
public class GraphElements {
    
    /** Creates a new instance of GraphElements */
    public GraphElements() {
    }
    
    

	
	
	 // Singleton factory for creating Edges...
    public static class NateEdgeFactory implements Factory<NateLink> {
        private static int linkCount = 0;
        private static double defaultWeight = 0;

        private static NateEdgeFactory instance = new NateEdgeFactory();
        
        private NateEdgeFactory() {            
        }
        
        public static NateEdgeFactory getInstance() {
            return instance;
        }
        
        public NateLink create() {
            String name = "Reply" + linkCount++;
            NateLink link = new NateLink(name);
            //link.setWeight(defaultWeight);
            return link;
        }    

        public static double getDefaultWeight() {
            return defaultWeight;
        }

        public static void setDefaultWeight(double aDefaultWeight) {
            defaultWeight = aDefaultWeight;
        }

        
    }
    
    // Single factory for creating Vertices...
    public static class NateVertexFactory implements Factory<NateNode> {
        private static int nodeCount = 0;
        private static NateVertexFactory instance = new NateVertexFactory();
        //private static CKDialogGraph g;
        
       
        
        private NateVertexFactory() {
        	
        }
        
        public static NateVertexFactory getInstance() {
            return instance;
        }
        
        public NateNode create() {
            String name = "Dialog " + nodeCount++;
            
            NateNode node = new NateNode(name);
            node.setId(nodeCount);
            return node;
            //return  NateNode(name);
        }        
    }
    
   

}
