package ckEditor.DataPickers;

import java.util.Iterator;

import javax.swing.JComponent;

import ckEditor.CKArtifactShortView;
import ckGameEngine.CKArtifact;

@Deprecated
public class CKArtifactPicker extends CKPicker<CKArtifact>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2724889568962939380L;


	public CKArtifactPicker(Iterator<CKArtifact> iter)
	{
		super(iter);
	}

	/* (non-Javadoc)
	 * @see ckGraphics.src.CKPicker#assetPanelView(java.lang.Object)
	 */
	@Override
	public JComponent assetPanelView(CKArtifact asset)
	{
		CKArtifactShortView view = new CKArtifactShortView();
		view.setArtifact(asset);
		return view;
	}
	
}