/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.AbstractDialogEditor.AbstractGraph;
import ckEditor.AbstractDialogEditor.DialogEditorFrame;
import ckEditor.AbstractDialogEditor.DialogVertex;
import ckEditor.AbstractDialogEditor.EdgeLink;
import ckEditor.AbstractDialogEditor.VertexNode;
import ckGraphicsEngine.CKDialogChoice;
import ckGraphicsEngine.CKDialogChoiceMessage;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
public class CKDialogAction extends CKGameAction
implements CKEntitySelectedListener<CKDialogChoice>, CKGameActionListenerInterface,
			ChangeListener
{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6541541978080039019L;
	//private int startingId;
	private String xmlGraph;

	private AbstractGraph graph = null;

	private CKSpellCast cast;
	private Vector<EdgeLink> validEdges = new Vector<EdgeLink>();
	static private Random rand =new Random();
	
	
	DialogVertex currentNode=null;
	
	public CKDialogAction()
	{
		this("");
		
	
	}
	
	public CKDialogAction(String xml)
	{
		super();
		xmlGraph = xml;
		
	}
	
	public void initGraph()
	{
		if (graph == null)
		{	
			//string for xml and gui node, actions will be child of that gui node
			if(xmlGraph.length()>0)
			{
				graph = AbstractGraph.readFromXMLString(xmlGraph);
			}
			else
			{
				graph = new AbstractGraph();
			}
			
			graph.setSecretParent(this);
			//Stores this, if this changes report that change
			//graph.addChangeListener(this); - graph will not change without vizualizer...
		}
//		return graph;
	}
	/**
	 * @return the xmlGraph
	 */
	public String getXmlGraph()
	{
		return xmlGraph;
	}

	/**
	 * @param xmlGraph the xmlGraph to set
	 */
	public void setXmlGraph(String xmlGraph)
	{
		this.xmlGraph = xmlGraph;
	}

	

	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameAction#doAction(ckGameEngine.actions.CKGameActionListener)
	 */
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		replaceListener(L);
		 //just to initialize
		this.cast =cast;
		initGraph();
		//return start node
		currentNode = (DialogVertex) graph.getStartNode();
		//pass this to action's (thing put onto hidden node) doAction
		currentNode.doAction(this,cast);
		
	}


	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		if(graph!=null)
		{
			return new CKDialogAction(graph.getXMLString());
		}
		return new CKDialogAction(xmlGraph);
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListener#actionCompleted(ckGameEngine.actions.CKGameAction)
	 */
	@Override
	public void actionCompleted(CKGameAction action)
	{
		//we have returned from any initial actions at a node.
		if(currentNode.isEndNode()) 
		{
			notifyListener();
			return; 
		}
		
		//we need to find viable edges from this node
		calcEdges();
		
		//if there are no edges we need to stop
		if(validEdges.size()==0)
		{
			System.err.println("DialogNode has no outgoing edges");
			notifyListener();
			return;
		}
		
		
		if(currentNode.isRandomNode()) 	
			{   //will randomly move to the next node and run the actions there
				chooseEdge(); 
				return;  
			}
		else //form message and send it 
		{
			sendMessage();
		}

	}

	
	

	@Override
	public void entitySelected(CKDialogChoice entity)
	{
		//we have returned from a dialog choice
		
		chooseEdge(entity.getID());
	}


	protected final void chooseEdge()
	{
		chooseEdge(rand.nextInt(validEdges.size()));
	}
	
	protected final void chooseEdge(int i)
	{
		//currentNode = (NateNode) graph.getDest(validEdges.get(i));
		currentNode =  (DialogVertex) graph.getDest(validEdges.get(i));
		System.out.println(currentNode + " is the current node");
		//System.out.println(currentNode. + " is the current node");
		currentNode.doAction(this,cast);	
	}
	
	
	protected final void sendMessage() { sendMessage(currentNode); }
	
	protected void sendMessage(DialogVertex vertex)
	{
	    CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
	    //TODO protect for no name....
	    CKGridActor actor = getQuest().getActor(vertex.getSpeaker());
	    CKDialogChoiceMessage message;
	    if(actor != null)
	    {
	    	CKGraphicsAsset portrait = factory.getPortrait(actor.getAssetID());
	    	 message = new CKDialogChoiceMessage(vertex.getReply(),portrait);	
	    }
	    else
	    {
	    	 message = new CKDialogChoiceMessage(vertex.getReply());	
	    }
		//now place in the choices...
		int i=0;
		for (EdgeLink n:validEdges)
		{
			message.addChoice(new CKDialogChoice(i,n.getDescription()));
			i++;
		}
			
		message.replaceEventListener(this);
		CKGameObjectsFacade.getEngine().loadDialogMessage(message);
	}
	
	
	
	private void calcEdges() { calcEdges(currentNode); }
	
	private void calcEdges(VertexNode vertex)
	{
		validEdges.clear();

		for (EdgeLink edge :graph.getOutEdges(vertex))
		{
			//like do action, pass to satisfied object
			if(edge.isSatisfied(cast )) { validEdges.add(edge); }			
		}
	}
	//TODO how to trigger the editor..
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Dialog Action";
	}
	

	static JPanel []panel;
	static JButton []button;
	
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			button = new JButton[2];
			button[0] = new JButton("Edit Dialog");
			button[1] = new JButton("Edit Dialog");
			
			panel[0].add(new JLabel("Dialog Action"));
			panel[0].add(button[0]);		
	
			
			
			panel[1].add(new JLabel("Dialog Action"));
			panel[1].add(button[1]);		
			
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);
		
		if(index == EDIT)
		{
			button[index].setAction(new EditorAction());
		}
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
		//no need to store
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
	
	
	DialogEditorFrame editor;
	
	class EditorAction extends AbstractAction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5670692061691185962L;

		public EditorAction()
		{
			super("Edit Dialog");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			initGraph();
			editor=new DialogEditorFrame(graph);
			editor.addChangeListener(CKDialogAction.this);
			
		
			
		}
		
	}

	@Override
	public void runAction(CKGameAction act, CKSpellCast cast)
	{
		act.doAction(this,cast);		
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if(editor!=null)
		{
		
			xmlGraph = graph.getXMLString(editor.getGraphLayout());
		}

	}
	
	
	
	
	
	
	
}
