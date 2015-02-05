package ckEditor.AbstractDialogEditor;



import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.AbstractDialogEditor.EdgeLink;
import ckEditor.treegui.CKSingleParent;
import ckEditor.treegui.CKTreeGui;


public class EdgeLinkPropertiesEditor extends 
CKXMLAssetPropertiesEditor<EdgeLink> 
implements ChangeListener, DocumentListener
{

	
	private static final long serialVersionUID = 1202254432116300185L;


	EdgeLink edge;

    
    public EdgeLinkPropertiesEditor( EdgeLink edge) 
   {
       this.edge = edge;
	   initComponents();
   }
    
    private JTextArea 	description=new JTextArea();

  
    SpinnerNumberModel weight;
    
    
    private CKTreeGui satisfies;
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() 
    {
    	setLayout(new BorderLayout());
    	
    	TitledBorder title = BorderFactory.createTitledBorder(edge.toString());
		setBorder(title);
    	
    	JPanel panel = new JPanel(new FlowLayout());
    	description.setText(edge.getDescription());
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.getDocument().addDocumentListener(this);
    	panel.add(description);
    	
    	weight = new SpinnerNumberModel(1,edge.getWeight(),100,1);
    	JSpinner wSpin = new JSpinner(weight);
    	wSpin.addChangeListener(this);
    	panel.add(new JLabel("Weight:"));
    	panel.add(wSpin);
    	
    	add(panel, BorderLayout.NORTH);

    	satisfies =new CKTreeGui(edge.getSingleParent());
        add(satisfies, BorderLayout.CENTER); 
    }


	@Override
	public void changedUpdate(DocumentEvent e)
	{
		stateChanged(null);		
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		stateChanged(null);		
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		stateChanged(null);		
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		privateStoreState();
		
		for(ChangeListener l:listeners)
		{
			l.stateChanged(null);
		}
		
	}

	
	Vector<ChangeListener> listeners = new Vector<>();
	
	@Override
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}

	@Override
	public EdgeLink getAsset()
	{
		return edge;
	}

	@Override
	public void storeState()
	{
		satisfies.stopEditing();
		
		privateStoreState();
	}
	
	private void privateStoreState()
	{
		edge.setDescription(description.getText());
		edge.setWeight((Integer)weight.getNumber());
		edge.setSingleParent((CKSingleParent) satisfies.getRoot());
		
		
	}
    
    
   
    
}
