package ckEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ckGraphicsEngine.layers.CKStaticMatrixLayer;

public class CKStaticMatrixLayerInformationPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7216888773534473447L;
	JTextField description;
	JSpinner width_spinner;
	JSpinner height_spinner;
	JSpinner layer_depth_spinner;
	JButton create_button;
	JLabel desc_lbl;
	JLabel width_lbl;
	JLabel height_lbl;
	JLabel depth_lbl;
	
	CKStaticMatrixLayer layer=null;
	
	public CKStaticMatrixLayerInformationPanel()
	{
		setLayout(null);
		setPreferredSize(new Dimension(300,200));
		initComponents();
		addComponents();
	}

	private void initComponents()
	{
		desc_lbl=new JLabel("Description:");
		desc_lbl.setBounds(5,25,100,20);
		description=new JTextField();
		description.setBounds(90,25,200,20);
		
		
		width_lbl=new JLabel("Width:");
		width_lbl.setBounds(5,50,100,20);
		width_spinner=new JSpinner();
		width_spinner.setValue(1);
		width_spinner.setBounds(90, 50, 50, 20);
		
		
		height_lbl=new JLabel("Height:");
		height_lbl.setBounds(5,75,100,20);
		height_spinner=new JSpinner();
		height_spinner.setValue(1);
		height_spinner.setBounds(90, 75, 50, 20);
		
		depth_lbl=new JLabel("Depth:");
		depth_lbl.setBounds(5,100,100,20);
		layer_depth_spinner=new JSpinner();
		layer_depth_spinner.setValue(1);
		layer_depth_spinner.setBounds(90, 100, 50, 20);
		
		create_button=new JButton("Create");
		create_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				String d=description.getText();
				int w=(Integer)width_spinner.getValue();
				int h=(Integer)height_spinner.getValue();
				int ld=(Integer)layer_depth_spinner.getValue();
				
				layer=new CKStaticMatrixLayer("",d,w,h,ld);
				dispose();				
			}
			
		});
		create_button.setBounds(125,175,50,20);
	}
	
	private void addComponents()
	{
		add(description);
		add(desc_lbl);
		add(width_lbl);
		add(height_lbl);
		add(depth_lbl);
		add(width_spinner);
		add(height_spinner);
		add(layer_depth_spinner);
		add(create_button);
	}
	
	private void dispose()
	{
		SwingUtilities.getWindowAncestor(this).dispose();
	}
	
	public CKStaticMatrixLayer getCKStaticMatrixLayer()
	{
	
		return layer;
	}
	
	public static void main(String[] args)
	{
		JFrame f=new JFrame();
		f.setContentPane(new CKStaticMatrixLayerInformationPanel());
		//f.setResizable(false);
		f.pack();
		f.setVisible(true);
	}
	
}
