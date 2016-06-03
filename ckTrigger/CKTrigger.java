package ckTrigger;

import java.awt.Color;
import java.awt.Component;
import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKXMLAsset;
import ckEditor.CKGUINodePropertiesEditor;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKMoveActorCmd;
import ckGameEngine.actions.CKNullAction;
import ckSatisfies.FalseSatisfies;
import ckSatisfies.Satisfies;

public class CKTrigger extends CKTriggerNode implements CKXMLAsset<CKTrigger>
{
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AID="";
	private TriggerResult result;
	
	public CKTrigger(Satisfies sat, CKGameAction action,TriggerResult result)
	{
		this(sat,action,new CKNullAction(),result);
	}
	
	
	public CKTrigger(Satisfies sat, CKGameAction action,CKGameAction failedAction,TriggerResult result)
	
	{//have to be careful with how we initialize.
		add(sat);
		add(action);
		add(failedAction);
		this.result = result;
		setChildOrderLocked(true);
	}
	
	public CKTrigger(Satisfies sat, CKGameAction action)
	{
		this(sat,action,TriggerResult.SATISFIED);
	}
	
	public CKTrigger()
	{
		
		this(new FalseSatisfies(),new CKNullAction(),new CKNullAction(),TriggerResult.SATISFIED);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		CKTrigger trig = new CKTrigger((Satisfies)getSatisfy().clone(),
										(CKGameAction) getAction().clone(),result);
		
		trig.setAID(getAID());
		trig.setName(getName());
		return trig;
	}

	
	
	/* (non-Javadoc)
	 * @see ckTrigger.CKTriggerNode#getSatisfy()
	 */
	public Satisfies getSatisfy()
	{
		return ( (Satisfies) this.getChildAt(0));
	}

	
	/* (non-Javadoc)
	 * @see ckTrigger.CKTriggerNode#setSatisfy(ckSatisfies.Satisfies)
	 */
	public void setSatisfy(Satisfies satisfy)
	{
		insert(satisfy,0);
	}

	/**
	 * @return the action
	 */
	public CKGameAction getAction()
	{
		return ( (CKGameAction) this.getChildAt(1));
	}

	
	
	
	
	@Override
	public CKGameAction getFailedAction() 
	{
		return ( (CKGameAction) this.getChildAt(2));
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(CKGameAction action)
	{
		insert(action,1);
	}
	
	public void setFailedAction(CKGameAction failedaction)
	{
		insert(failedaction,2);
	}

	/**
	 * @return the result
	 */
	public TriggerResult getResult()
	{
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(TriggerResult result)
	{
		this.result = result;
	}
	
	
	
	
	
/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{//this must be setup properly in the constructor for this to work later.
		//will ignore childIndex
		if(newChild instanceof Satisfies)
		{
			if( getChildCount() ==3)
			{	remove(0); }
			super.insert(newChild,0);
		}
		else if(newChild instanceof CKGameAction )
		{
			if(getChildCount() ==3)
				{remove(childIndex);}
			super.insert(newChild,childIndex);
		}
		
	}
	
		
	@Override
	public boolean shouldActNow(CKSpellCast cast)
	{
		return getSatisfy().isSatisfied(cast );
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Trigger:  [result=" + result + "] " +getName();
	}
	
	
	public static void main(String [] s)
	{
		CKTrigger trig = new CKTrigger();
		
		trig.setName("I've been set");
		trig.setAction(new CKMoveActorCmd());
		
		PipedInputStream pipeIn = new PipedInputStream();
		PipedOutputStream pipeOut;
		try
		{
			pipeOut = new PipedOutputStream(pipeIn);
			trig.writeToStream(pipeOut);
			trig.writeToStream(System.out);
		} catch (IOException e)
		{
		
			e.printStackTrace();
		}
		
		
		
		
		@SuppressWarnings("resource")
		XMLDecoder d = new XMLDecoder(pipeIn);
		CKTrigger t2 = (CKTrigger) d.readObject();
		System.out.println("stored name" +t2.getName());
		
		
	}
	
	static JPanel []panel;
	static JTextField[] nameText;
	@SuppressWarnings("rawtypes")
	static JComboBox[] resultBox;

	
	static private void initPanel(boolean force,Object caller)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.LINE_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.LINE_AXIS));

			nameText = new JTextField[2];
			nameText[0] = new JTextField();
			nameText[1] = new JTextField();
			
			panel[0].add(new JLabel("Trigger   "));
			panel[0].add(nameText[0]);		
			
			panel[1].add(new JLabel("Trigger   "));
			panel[1].add(nameText[1]);		

			resultBox = new JComboBox[2];
			resultBox[0]=new JComboBox<TriggerResult>(TriggerResult.values());
			resultBox[1]=new JComboBox<TriggerResult>(TriggerResult.values());
			panel[0].add(resultBox[0]);
			panel[1].add(resultBox[1]);
			

		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true,this); }
		panel[index].setBackground(colors[index]);
		
		nameText[index].setText(getName());
		nameText[index].setColumns(15);
		resultBox[index].setSelectedItem(result);
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		setName( (String)nameText[EDIT].getText() );
		result = (TriggerResult)resultBox[EDIT].getSelectedItem();
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}

	@Override
	public String getAID()
	{

		return AID;
	}

	@Override
	public void setAID(String a)
	{
		AID=a;
		
	}
	
	

	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}
	
	@Override
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
		return new CKTreeGui(this,false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKTrigger> getXMLPropertiesEditor()
	{
		
		return new CKGUINodePropertiesEditor<CKTrigger>(this);
	}
	
}
