package ckEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class ColorChooser extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2552694122520808626L;

	JPanel rgb_panel;
	JPanel hsb_panel;
	JPanel alpha_panel;
	//TODO preview
	//TODO hex?
	
	private JColorChooser chooser=new JColorChooser();
	AbstractColorChooserPanel[] colorPanel_array =chooser.getChooserPanels();
	
	private JSlider alpha_slider;
	private JSpinner alpha_spinner;
	
	/*constructor*/
	public ColorChooser()
	{
		
		//importatnt!
		setPreferredSize(new Dimension(445,500));
		setBorder(BorderFactory.createTitledBorder("Color Options"));
		
		//init components
		initRGB();
		initHSB();
		initAlpha();
		//TODO initPreview();
		
		//add components
		add(hsb_panel);
		add(rgb_panel);
		add(alpha_panel);
	}
	
	/*--Begin init()'s and listeners--*/
	private void initRGB()
	{
		rgb_panel=new JPanel();
		rgb_panel.add(colorPanel_array[2]);
		rgb_panel.setVisible(true);
	}
	
	private void initHSB()
	{
		hsb_panel=new JPanel();
		hsb_panel.add(colorPanel_array[1]);
		hsb_panel.setVisible(true);
	}
	

	private void initAlpha() 
	{
		alpha_panel=new JPanel();
		alpha_panel.setPreferredSize(new Dimension(350,160));
		
		alpha_slider = new JSlider(JSlider.HORIZONTAL,0,255,255);
		alpha_slider.setMinorTickSpacing(17);
		alpha_slider.setMajorTickSpacing(85);
		alpha_slider.setPaintTicks(true);
		alpha_slider.setPaintLabels(true);
		alpha_slider.addChangeListener(new SliderListener());
		
		alpha_spinner= new JSpinner(new SpinnerNumberModel(255,0,255,1));
		alpha_spinner.setValue(255);
		alpha_spinner.setInheritsPopupMenu(true);
		alpha_spinner.addChangeListener(new SpinnerListener());
		
		alpha_panel.add(new JLabel("Alpha"),BorderLayout.WEST);
		alpha_panel.add(alpha_slider,BorderLayout.CENTER);
		alpha_panel.add(alpha_spinner,BorderLayout.EAST);
	}
	
	class SliderListener implements ChangeListener 
	{
	    public void stateChanged(ChangeEvent e) 
	    {
	    	alpha_spinner.setValue( alpha_slider.getValue());
	    	recalcColor();	
	    }
	}	
	
	class SpinnerListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e) 
		{
			//cast it because getValue() from a text filed isnt an int
			int val = ((Integer) alpha_spinner.getValue()).intValue();
			alpha_slider.setValue(val);			
		}
	
	}
	/*--END init()'s and Listeners--*/
	
	/*--Begin mutators and accessors--*/
	
	public Color getColor()
	{
		int r=chooser.getColor().getRed();
		int g=chooser.getColor().getGreen();
		int b=chooser.getColor().getBlue();
		return new Color(r,g,b,getAlpha());
	}
	
	public int getColorIntRGB()
		{return chooser.getColor().getRGB();}
	
	public int getColorIntARGB()
		{
			int r=chooser.getColor().getRed();
			int g=chooser.getColor().getGreen();
			int b=chooser.getColor().getBlue();
			int a=getAlpha();
			return r&b&g&a;
		}
	
	public void setColor(int r, int g, int b, int a)
	{
		chooser.setColor(new Color(r,g,b,a));
		alpha_slider.setValue(a);
		repaint();
	}
	
	public int getAlpha()
		{return alpha_slider.getValue();}
	
	
	
	/*--End mutators and accessors--*/
	
	/*--Other methods--*/
	public void recalcColor()
	{
		int r=chooser.getColor().getRed();
		int g=chooser.getColor().getGreen();
		int b=chooser.getColor().getBlue();
		chooser.setColor(new Color(r,g,b,getAlpha()));
		
	}
	/*-- End other methods --*/
}

