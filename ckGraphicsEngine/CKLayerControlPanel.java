package ckGraphicsEngine;

import static ckCommonUtils.CKDebugPrinting.println;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckGraphicsEngine.layers.CKGraphicsLayer;

/**
 * 
 * Make this should be part of the graphicsScene?
 * 
 */
public class CKLayerControlPanel extends JPanel implements MouseWheelListener
{
/**
	 * 
	 */
	private static final long serialVersionUID = -5225890244199979166L;

/**
 * Layer Panel
 * 
 * This is panel is to create controls for one layer
 */
	
	private class LayerControl extends JPanel implements ItemListener
	{
		
		/**
	 * 
	 */
	private static final long serialVersionUID = -4934686467231925613L;
		CKGraphicsLayer layer;
		JCheckBox visible;
		JCheckBox affected;
		
		
		public LayerControl(CKGraphicsLayer l)
		{
			setLayout(new GridLayout(3,1));
			layer=l;
			add(new JLabel(""+layer.getLayerDepth()));
			visible=new JCheckBox();
			visible.setSelected(layer.isVisible());
			println("Layer is visible"+layer.isVisible());
			add(visible);
			visible.addItemListener(this);
			
			affected=new JCheckBox();
			add(affected);
			
		}
		
		public CKGraphicsLayer getLayer()
		{
			return layer;
		}
		
		public boolean isAffected()
		{
			return affected.isSelected();
		}

		@Override
		public void itemStateChanged(ItemEvent e)
		{
			Object source = e.getItemSelectable();
			if(source == visible)
			{
				layer.setVisible(visible.isSelected());
			}
		}
		
		
	}
	/**
	 * displays the labels
	 */
	private class LabelControl extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7624343683551061988L;

		public LabelControl()
		{
			setLayout(new GridLayout(3,1));
			add(new JLabel("Layers"));
			add(new JLabel("Visible"));
			add(new JLabel("Affected"));
		}
		
		
		
	}
	
	
	
	CKGraphicsScene scene;
	ArrayList<LayerControl> layers; 
	
	public CKLayerControlPanel(CKGraphicsScene scene2)
	{
		scene = scene2;
		layers=new ArrayList<LayerControl>();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(new LabelControl());
		Iterator<CKGraphicsLayer> iter = scene.getInteractiveLayerIter();
		while(iter.hasNext())
		{
			LayerControl l =new LayerControl(iter.next()); 
			layers.add(l);
			add(l);
		}	
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		Point p = e.getPoint();
		Point mapCoords = scene.getTrans().convertScreenToMap(p);
		//now to see how much to change by.
		int units=e.getWheelRotation();
		
		
		//then how to do the changes
		Iterator<LayerControl> iter = layers.iterator();
		while(iter.hasNext())
		{
			LayerControl lc = iter.next();
			if(lc.isAffected())
			{
				println("Altering layer "+lc.getLayer().getLayerDepth()+" by "+units);
				//println( );
				lc.getLayer().changeHeight(mapCoords.x,mapCoords.y,units*-.25);
			}
			
			
		}
		
		
	}
	
}
