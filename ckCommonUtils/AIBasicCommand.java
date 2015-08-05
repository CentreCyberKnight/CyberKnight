package ckCommonUtils;

import java.util.function.BiFunction;

import ckDatabase.AimDescriptionFactory;
import ckGameEngine.AimDescription;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellResult;
import ckGameEngine.Direction;
import ckGameEngine.DescisionGrid.CharacterActionDescription;
import ckGameEngine.DescisionGrid.DecisionNode;
import ckPythonInterpreter.CKEditorPCController;

public class AIBasicCommand implements AICommand
{
	BasicCommand cmd;
	
	public AIBasicCommand(BasicCommand cmd)
	{
		this.cmd = cmd;
		
	}
	
	
	
	@Override
	public void doCommand(CKPosition origin,Direction dir,
			String targetType, int cp)
	{
		AimDescriptionFactory factory = AimDescriptionFactory.getInstance();
		AimDescription aim = factory.getAsset(targetType);
		CKPosition [] offsets = aim.getOffsets(dir);
		CKPosition target;
		if(offsets.length==1)
		{
			target=origin.add(offsets[0]);//FIXME will not work for chained area of effect stuff
		}
		else
		{
			
			target=new CKAreaPositions(origin,AimDescription.calculateTarget(origin, offsets));
		}
			
		cmd.doAction(target,cp);
	}
	
	//Damage only for now....
	//need team!
	public double [] doPrediction(CharacterActionDescription cad,DecisionNode node)
	{
		assert CKGameObjectsFacade.getCurrentPlayer()!=null;

		final int depth = 5;
		
		double []utils=new double[cad.costs.length];
		for(int i=0;i<cad.costs.length;i++)
		{
			int cost = cad.costs[i]; 
			CKSpellResult res = CKEditorPCController.predictCasting(()->
			{doCommand(node.position,node.direction,cad.targetType ,cost); }, depth);

			//total damage - penalty for damage to support players.

			utils[i] = res.avgResults("Damage",depth) - 3*res.avgResults("Damage",CKGameObjectsFacade.getCurrentPlayer().getTeam(),depth);
			
		}
		System.out.print(cad.action+" "+"node"+node.position+" utils:");
		for(double d: utils)
			System.out.print(d+" ");
		System.out.println("");
		return utils;
	};
	
}
