package ckEditor.AbstractDialogEditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;


/**
 * This subclass adds the ability to tell a vizualizer that a change has occurred
 *  
 * @author bradshaw
 *
 * @param <V>
 * @param <E>
 */
public class EditingNotifyingMousePlugin<V,E>
extends AbstractGraphMousePlugin implements
     MouseListener, MouseMotionListener
     

/*extends
		EditingGraphMousePlugin<V, E>
		*/
{
public EditingNotifyingMousePlugin()
	{
		super(MouseEvent.BUTTON1_MASK);
		// TODO Auto-generated constructor stub
	}

	/*
	public EditingNotifyingMousePlugin(Factory<V> vertexFactory,
			Factory<E> edgeFactory)
	{
		//super(vertexFactory, edgeFactory);
	}
*/
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		//super.mouseReleased(e);
		@SuppressWarnings("unchecked")
		final VisualizationViewer<V,E> vv = 
				(VisualizationViewer<V,E>)e.getSource();
		vv.stateChanged(null);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		@SuppressWarnings("unchecked")
		final VisualizationViewer<V,E> vv = 
				(VisualizationViewer<V,E>)e.getSource();
		vv.stateChanged(null);
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	
}
