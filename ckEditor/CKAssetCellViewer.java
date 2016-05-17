package ckEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKStillAssetViewer;

public class CKAssetCellViewer extends JPanel implements ChangeListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6906571879625333208L;
	SpinnerNumberModel rowSpin;
	SpinnerNumberModel frameSpin;
	CKStillAssetViewer view;
	CKGraphicsAsset asset;
	
	
	
	public CKAssetCellViewer(CKGraphicsAsset asset)
	{
		super(new BorderLayout());
		this.asset=asset;
		//create spinner panel
		JPanel spinnerPanel = new JPanel();
		spinnerPanel.setLayout(new FlowLayout());
		
		spinnerPanel.add(new JLabel("Now Showing             Row"));
		int tRows = asset.getRows()-1;
		if(tRows<0) { tRows=0; }

		rowSpin = new SpinnerNumberModel(0, 0,tRows,1);
		JSpinner spinR = new JSpinner(rowSpin);
		spinR.addChangeListener(this);
		spinnerPanel.add(spinR);

		spinnerPanel.add(new JLabel("Frame"));
		int tframes = asset.getFrames(0)-1;
		if(tframes<0) { tframes=0; }
		frameSpin = new SpinnerNumberModel(0, 0,tframes,1);
		JSpinner spinF = new JSpinner(frameSpin);
		spinF.addChangeListener(this);
		spinnerPanel.add(spinF);

		add(spinnerPanel,BorderLayout.NORTH);
		//now draw asset
		view =new CKStillAssetViewer(30,asset,null); 
		add(view,BorderLayout.CENTER);
	}

	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		CKGraphicsAsset water=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("heroSprite");		
		CKAssetCellViewer view=new CKAssetCellViewer(water);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		
	}

	
	//FIXME - need a detect when the asset changes and alter the spinners accordingly. 




	@Override
	public void stateChanged(ChangeEvent e)
	{
		rowSpin.setMaximum(asset.getRows()-1);
		int row = rowSpin.getNumber().intValue();
		frameSpin.setMaximum(asset.getFrames(row)-1);
		
		
		view.setPresentRow(rowSpin.getNumber().intValue());
		view.setAssetFrame(frameSpin.getNumber().intValue());		
	}

}
