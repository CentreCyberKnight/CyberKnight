package ckSnapInterpreter;

import java.util.List;
import java.util.Vector;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ckCommonUtils.CKPropertyStrings;
import ckGameEngine.CKBook;
import ckGameEngine.CampaignNode;

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
		//resize(1000,650);
		
		//HBox top = new HBox();
		
		BorderPane top = new BorderPane();
		
		Background back = new Background(new BackgroundFill(Color.BLACK, null, null));
		Color frontColor = Color.web("c8e6ff");
		
		Font f= Font.font("Serif",FontWeight.BOLD ,FontPosture.REGULAR, 18);
		
		//javafx.scene.text.Font.getFamilies().forEach(s->System.out.println("FONT"+s));
		
		//Background front = new Background(new BackgroundFill(frontColor, null, null));
		//top.setFill(Color.BLACK);
		top.setBackground(back);
		
		BorderPane left = new BorderPane();
		BorderPane.setMargin(left,new Insets(10,10,10,10));
		
		BorderPane right = new BorderPane();
		BorderPane.setMargin(right,new Insets(10,10,10,0));
		HBox.setHgrow(right,Priority.ALWAYS);
		
		
		
		//Image mom = new Image("ckSnapInterpreter/sword.png");
		Image mom = new Image("ckSnapInterpreter/mom1_portrait.png");
		Node momNode = new ImageView(mom); 
		BorderPane.setMargin(momNode,new Insets(10,10,10,10));
		left.setTop(momNode);
		
		
		

		String message = "Welcome to CyberKnight. \n\n"
				+ "Double click any level to play.";
		Text text = new Text(message);
		text.setFill(frontColor);
//		text.setBackground(front);
		text.setFont(f);
		text.setWrappingWidth(200);
		left.setLeft(text);
		
		Node tree = doLayout();
		
		//ScrollPane scrollPane  = new ScrollPane(tree);
		BorderPane.setMargin(tree,new Insets(10,10,10,10));
		right.setCenter(tree);
		
		//Image mom = new Image("ckSnapInterpreter/sword.png");
		Image title = new Image("ckSnapInterpreter/Title.png");
		Node titleNode = new ImageView(title); 
		BorderPane.setMargin(titleNode,new Insets(10,10,10,10));
		right.setTop(titleNode);

		
		//top.getChildren().addAll(left,right);
		
		top.setLeft(left);
		top.setCenter(right);
		getChildren().add(top);
		
		
	}
	
	
	public TreeView<Object> doLayout()
	{
		TreeItem<Object> root = new TreeItem<Object>(new String("Double Click to Play"));
		root.setExpanded(true);
		
		
		
		//TreeItem<Object> todo = new TreeItem<Object>(new String("New Games"));
		//todo.setExpanded(true);
		open.stream().sorted((a,b)->a.getName().compareTo(b.getName())).forEach(cn->
		{
			TreeItem<Object> item = new TreeItem<Object>(cn);
			root.getChildren().add(item);
		});
		
		if(open.isEmpty())
		{
			root.getChildren().add(new TreeItem<Object>("Congradulations! You completed the Game!"));
			
		}

		if(!completed.isEmpty())
		{
			TreeItem<Object> old = new TreeItem<Object>(new String(
					"Completed Levels"));

			completed.stream().sorted((a,b)->a.getName().compareTo(b.getName()))
			.forEach(cn -> {
				TreeItem<Object> item = new TreeItem<Object>(cn);
				old.getChildren().add(item);
			});

			root.getChildren().add(old);
		}
		
		
		
		
		
		
		
		
		
		TreeView<Object> tree = new TreeView<Object>(root);
		tree.setShowRoot(false);
		
		
		/*
		 * .getSelectionModel()
		.selectedItemProperty()
		.addListener( e ->(ob,ov,nv)->
		 */
		
		tree.setOnMouseClicked(e->
		{
			if(e.getClickCount()==2)
			{
			
				Object obj = tree.getSelectionModel()
						.getSelectedItem().getValue();
				
				if(obj instanceof CampaignNode)
				{
					CampaignNode node = (CampaignNode) obj; 
				
					Stage stage = (Stage) getScene().getWindow();
					CKQuestSceneBuilder builder = new CKQuestSceneBuilder(node.getQuestID(),false);
					stage.setScene(builder.getAndStartScene());
				}
			}
		});
				

		//getChildren().add(tree);
		tree.setPrefWidth(500);
		tree.setPrefHeight(310);
		tree.setMaxWidth(Double.MAX_VALUE);
		
		return tree;
		
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
