package ckEditor;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.BiConsumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ckDatabase.CKCampaignNodeFactory;
import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CampaignNode;

public class CKCampaignEditor extends JPanel
{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090731877475478629L;

	CKGuiRoot root;
	CKTreeGui tree;
	
	public CKCampaignEditor()
	{
		setPreferredSize(new Dimension(800,800));
		initComponents();
	}
	
	
	JTextField campaignLabel = new JTextField();
	
	private void initComponents()
	{
		
		root = new CKGuiRoot();
		tree = new CKTreeGui(root); 
		//tree.setShowsRootHandles(false);
		JPanel strip = new JPanel();
		strip.setLayout(new BoxLayout(strip,BoxLayout.X_AXIS));
		
		
		strip.add(new JLabel("Campaign: "));
		campaignLabel.setColumns(25);
		strip.add(campaignLabel);
		
		
		JButton newB = new JButton("Load Campaign");
		strip.add(newB);
		
		newB.addActionListener(e->
		{
			selectCampaign( (s,v)->
			{
				root.removeAllChildren();
				setCampaign(s);
				v.forEach(node->
				{
					tree.addNode(root,node);		
				});
				
				
			});
			
			
		});
		
		JButton addB = new JButton("Add Node");
		strip.add(addB);
		addB.addActionListener(e->{
			System.out.println("adding node");
			tree.addNode(root,new CampaignNode());			
		});
		
		
		
		
		JButton saveB = new JButton("Save Campaign");
		strip.add(saveB);
		saveB.addActionListener(event->
		{
		
			Enumeration<?> e = root.children();
			while(e.hasMoreElements())
			{	
				CampaignNode node = (CampaignNode) e.nextElement();
				node.setCampaign(getCampaign());
				CKCampaignNodeFactory.getInstance().writeAssetToXMLDirectory(node);
			}
			
		});
		
		setLayout(new BorderLayout());
		add(strip,BorderLayout.NORTH);
		
		
		add(tree);
	}

	
	
	
	public static void selectCampaign(BiConsumer<String,Vector<CampaignNode>> fcn)
	{
		//get all nodes
		Iterator<CampaignNode> iter = CKCampaignNodeFactory.getInstance().getAllAssets();
		
		//sort Nodes with Map
		HashMap <String,Vector<CampaignNode>> map = 
				new HashMap<String, Vector<CampaignNode>>();
		
		while(iter.hasNext())
		{
			CampaignNode node = iter.next();
			if(map.containsKey(node.getCampaign()))
			{
				map.get(node.getCampaign()).add(node);
			}
			else
			{
				Vector<CampaignNode> vec = new Vector<>();
				vec.add(node);
				map.put(node.getCampaign(), vec);
			}
		}
		//have a map of all the things, now do a picker
		
		JFrame frame = new JFrame();
		
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		

		map.forEach( (s,v)->
			{
				JButton b = new JButton(s);
				b.addActionListener(e->
				{
					fcn.accept(s, v);
					frame.setVisible(false);
					frame.dispose();					
				});
				pane.add(b);
			});
		
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	/**
	 * @return the campaign
	 */
	public String getCampaign()
	{
		return campaignLabel.getText();
	}




	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(String campaign)
	{
		this.campaignLabel.setText(campaign);
	}




	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		
		CKCampaignEditor e=new CKCampaignEditor();
		frame.add(e);
		//CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance(); 
		//CKCompositeAssetEditor ae=new CKCompositeAssetEditor((CKCompositeAsset) factory.getGraphicsAsset("heroSprite"));
		//pane.addTab(ae,"Asset Editor");
		//pane.addTab(new CKTreeGui(new QuestData(null)),"Tree GUI");
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	}
		);
		
	}
	
}


