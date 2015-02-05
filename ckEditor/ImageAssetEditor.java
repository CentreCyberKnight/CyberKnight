package ckEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKImageAsset;

/*This class is essentially the container for the editor. It has more functionality added to it though*/
public class ImageAssetEditor extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6970213570171201647L;
	Editor editor;
	JScrollPane scroll_pane;
	JPanel control_panel;
	JPanel frame_row_panel;
	ColorChooser chooser;
	public ImageAssetEditor(ColorChooser c)
	{
		chooser=c;
		
		setPreferredSize(new Dimension(700,600));
		
		initComponents();
		addComponents();
	}
	
	private void initComponents()
	{		
		editor=new Editor(chooser);//TODO get the editor a ColorChooser
		scroll_pane=new JScrollPane(editor);
		scroll_pane.setPreferredSize(editor.getPreferredSize());
		//scroll_pane.setViewportView(editor);
		control_panel=new JPanel();
		control_panel.setPreferredSize(new Dimension(75,200));
		frame_row_panel=new JPanel();
		frame_row_panel.setPreferredSize(new Dimension(576,30));
		
		
		
		final JSpinner frames=new JSpinner();
		final JSpinner rows=new JSpinner();
		
		frames.setValue(0);
		rows.setValue(0);
		
		frames.addChangeListener(new ChangeListener()
			{@Override
				public void stateChanged(ChangeEvent e) 
				{
					if ((int)(Integer)frames.getValue() < 0)
					{frames.setValue(0);}
					editor.setFrame((Integer)frames.getValue());		
			}});
		rows.addChangeListener(new ChangeListener()
			{@Override
			public void stateChanged(ChangeEvent e) 
			{
				if ((int)(Integer)rows.getValue() < 0)
				{rows.setValue(0);}
				editor.setRow((Integer)rows.getValue());		
			}});
		frame_row_panel.add(new JLabel("Frame:"));
		frame_row_panel.add(frames);
		frame_row_panel.add(new JLabel("Row:"));
		frame_row_panel.add(rows);
		
		initControlPanel();
		
	}
	
	private void initControlPanel()
	{
		JButton zoom_in=addButton("ckEditor/images/tools/stock-tool-zoom-in.png");
		JButton zoom_out=addButton("ckEditor/images/tools/stock-tool-zoom-out.png");
		JButton fill=addButton("ckEditor/images/tools/stock-tool-bucket-fill-16.png");
		JButton picker=addButton("ckEditor/images/tools/stock-tool-color-picker-16.png");
		JButton eraser=addButton("ckEditor/images/tools/stock-tool-eraser-16.png");
		JButton save=addButton("ckEditor/images/tools/save.png");
		JButton open=addButton("ckEditor/images/tools/open.png");
		JButton new_asset=addButton("ckEditor/images/tools/new.png");
		
		zoom_in.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.zoomIn();
			}});
		zoom_out.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.zoomOut();
			}});
		fill.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.setCursorMode("fill");
			}});
		picker.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.setCursorMode("color");
			}});
		eraser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}});
		
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
					CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(editor.getImageAsset());
			}
		}
		);
		
		open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					DatabaseBrowser db=new DatabaseBrowser();
					JDialog test=new JDialog();
					test.setModal(true);
					test.setContentPane(db);
					test.pack();
					test.setVisible(true);
					editor.setImageAsset((CKImageAsset)db.getRetrievedObject());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}			
		});

		new_asset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			ImageAssetInformationPanel p=new ImageAssetInformationPanel();
			JDialog test=new JDialog();
			test.setModal(true);
			test.setContentPane(p);
			test.pack();
			test.setVisible(true);
			editor.setImageAsset(p.getImageAsset());
			editor.getImageAsset().setFilename(p.getFileName());
			}});

		
		control_panel.add(save);
		control_panel.add(open);
		control_panel.add(new_asset);
		control_panel.add(zoom_in);
		control_panel.add(zoom_out);
		control_panel.add(fill);
		control_panel.add(picker);
		control_panel.add(eraser);
		
	}
	
	private JButton addButton(String fname)
	{
		ImageIcon icon=new ImageIcon(fname);
		JButton temp=new JButton(icon);
		return temp;
	}
	
	
	
	private void addComponents()
	{
		add(control_panel,BorderLayout.WEST);
		add(scroll_pane,BorderLayout.CENTER);
		add(frame_row_panel,BorderLayout.NORTH);
	}
	
	public Editor getEditor()
	{
		return editor;
	}

	
	public static void main(String[] args)
	{
		JFrame f=new JFrame();
		JFrame c=new JFrame();
		ColorChooser chooser=new ColorChooser();
		c.setContentPane(chooser);
		c.pack();
		c.setVisible(true);
/*
		AssetFactory factory=new AssetFactory();
		
	
		
		final CKImageAsset asset = factory.getImageAssetFromDB(13);
		//Editor e=new Editor(chooser,);
		final ImageAssetEditor a =new ImageAssetEditor(chooser);
		
		f.setContentPane(a);
		JButton button=new JButton("Load From DB");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				a.editor.setImageAsset(asset);
				a.editor.repaint();
			}
			
		});
		*/
		//f.add(button);
	 ImageAssetEditor a =new ImageAssetEditor(chooser);
		
		f.setContentPane(a);
		f.pack();
		f.setVisible(true);
		c.setVisible(true);
	}
	
}
