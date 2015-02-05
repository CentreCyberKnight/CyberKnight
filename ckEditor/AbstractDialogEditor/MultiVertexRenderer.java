package ckEditor.AbstractDialogEditor;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.Icon;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;


/**
 * A class that renders a shape as well as an icon for the vertexes in the system.
 * 
 *  found at   http://www.game2learn.com/?p=437
 *  
 * @author dragonlord
 *
 * @param <V>
 * @param <E>
 */
public class MultiVertexRenderer<V, E> extends BasicVertexRenderer<V, E>
{

	
	
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer#paintIconForVertex(edu.uci.ics.jung.visualization.RenderContext, java.lang.Object, edu.uci.ics.jung.algorithms.layout.Layout)
	 */
	@Override
	protected void paintIconForVertex(RenderContext<V, E>rc, V v,
			Layout<V, E> layout)
	{
	        GraphicsDecorator g = rc.getGraphicsContext();
	        boolean vertexHit = true;
	        // get the shape to be rendered
	        Shape shape = rc.getVertexShapeTransformer().transform(v);
	        
	        Point2D p = layout.transform(v);
	        p = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p);
	        float x = (float)p.getX();
	        float y = (float)p.getY();
	        // create a transform that translates to the location of
	        // the vertex to be rendered
	        AffineTransform xform = AffineTransform.getTranslateInstance(x,y);
	        // transform the vertex shape with xtransform
	        shape = xform.createTransformedShape(shape);
	        
	        vertexHit = vertexHit(rc, shape);
	            //rc.getViewTransformer().transform(shape).intersects(deviceRectangle);

	        if (vertexHit) {
	        	if(rc.getVertexIconTransformer() != null) {
	        		Icon icon = rc.getVertexIconTransformer().transform(v);
	        		if(icon != null) {
	        			paintShapeForVertex(rc, v, shape); //added
	           			g.draw(icon, rc.getScreenDevice(), shape, (int)x,(int)y);

	        		} else {
	        			paintShapeForVertex(rc, v, shape);
	        		}
	        	} else {
	        		paintShapeForVertex(rc, v, shape);
	        	}
	        }
	    }
	}


