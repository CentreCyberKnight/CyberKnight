package ckEditor.DataPickers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ckCommonUtils.CKEntitySelectedListener;

public class CKPicker<T> extends JScrollPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8035499304332618932L;
	Vector<CKEntitySelectedListener<T>> listeners;
	JPanel innerPane;
	
	public CKPicker(Iterator<T> iter)
	{
		listeners = new Vector<CKEntitySelectedListener<T>>();
		
		//Lay out the label and scroll pane from top to bottom.
		innerPane = new JPanel();
		innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
		innerPane.setBackground(Color.RED);
		innerPane.setAlignmentX(LEFT_ALIGNMENT);
		
		rePopulateEntities(iter);
		getViewport().add(innerPane);
		getVerticalScrollBar().setUnitIncrement(50);
		
		Dimension big = new Dimension(350,500);
		setPreferredSize(big);
	}
	
	public void addSelectedListener(CKEntitySelectedListener<T> L)
	{
		listeners.add(L);
	}
	
	public void removeSelectedListener(CKEntitySelectedListener<T> L)
	{
		listeners.remove(L);
	}
	
	public void rePopulateEntities(Iterator<T> iter)
	{
		innerPane.removeAll();

		while(iter.hasNext())
		{
			T asset = iter.next();
			//System.out.println("inserting asset "+asset.getDescription());
			JPanel panel = new JPanel();
			
			JButton select = new JButton("Select");
			panel.setLayout(new BorderLayout());
			
			panel.add(select,BorderLayout.WEST);
			
			JComponent comp = assetPanelView(asset);
			panel.add(comp);
			
			select.addMouseListener(new MouseAssetSelector(asset));
			
			innerPane.add(panel);
					
		}
		
		innerPane.updateUI();
		
		
	}
	
	/**
	 * Override by subclasses to present the asset in question
	 * @param asset
	 * @return
	 */
	public JComponent assetPanelView(T asset)
	{
		return new JLabel(asset.toString());
	}

	
	
  class MouseAssetSelector extends MouseAdapter
	{
		T asset;
				
		public MouseAssetSelector(T a)
		{
			asset=a;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent arg0)
		{
			for (CKEntitySelectedListener<T> l : listeners)
			{
				l.entitySelected(asset);
			}
		}
		
	}	
	
	public static void main (String [] argv)
	{
		
		JFrame frame = new JFrame();
		Vector<String> s = new Vector<String>();
		s.add("BACON");
		s.add("CORN");
		s.add("Potatoes");
		
		CKPicker<String> picker = new CKPicker<String>(s.iterator());
		picker.addSelectedListener(new StringPopUpAssetViewer());
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
		
}

class StringPopUpAssetViewer implements CKEntitySelectedListener<String>
{

	
	public StringPopUpAssetViewer() {}
	
	@Override
	public void entitySelected(String asset)
	{
		JFrame frame = new JFrame();

		JLabel l = new JLabel("You choose :"+asset);

		frame.add(l);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
