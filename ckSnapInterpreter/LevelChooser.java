package ckSnapInterpreter;

import java.util.List;
import java.util.Vector;






import ckCommonUtils.CKPropertyStrings;
import ckGameEngine.CKBook;
import ckGameEngine.CampaignNode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LevelChooser extends Pane
{

	List<CampaignNode> completed = new Vector<>();
	List<CampaignNode> open = new Vector<>();
	List<CampaignNode> closed = new Vector<>();
	CKBook traits;
	List<CampaignNode> campaign;
 	
	
	public LevelChooser(CKBook traits,List<CampaignNode> campaign)
	{
		catagorize(traits,campaign);
		doLayout();
		
		
		
	}
	
	
	public void doLayout()
	{
		TreeItem<CampaignNode> root = new TreeItem<CampaignNode>();
		root.setExpanded(true);
		open.forEach(cn->
		{
			TreeItem<CampaignNode> item = new TreeItem<CampaignNode>(cn);
			root.getChildren().add(item);
		});
		TreeView<CampaignNode> tree = new TreeView<CampaignNode>(root);
		getChildren().add(tree);
		
		/*
		 * .getSelectionModel()
		.selectedItemProperty()
		.addListener( e ->(ob,ov,nv)->
		 */
		
		tree.setOnMouseClicked(e->
		{
			if(e.getClickCount()==2)
			{
			
				CampaignNode node = tree.getSelectionModel()
						.getSelectedItem().getValue();
				
				
				Stage stage = (Stage) getScene().getWindow();
				CKQuestSceneBuilder builder = new CKQuestSceneBuilder(node.getQuestID());
				stage.setScene(builder.getAndStartScene());
			}
		});
				

	}
	
	
	
	
	public void catagorize(CKBook traits,List<CampaignNode> campaign)
	{
		this.traits = traits;
		this.campaign = campaign;
		
		completed.clear();
		open.clear();
		closed.clear();
		
		campaign.forEach(cn ->
		{
			
			if(traits.hasPage(CKPropertyStrings.CH_LEVELS,cn.getWinName()))
			{
				completed.add(cn);
			}
			else if ( traits.meetsRequirements(cn.getPrereqs()) )
			{
				open.add(cn);
			}
			else
			{
				closed.add(cn);
			}
			
			
			
			
		});
		
	}
	
	
	
}
