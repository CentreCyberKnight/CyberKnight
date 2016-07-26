package ckPythonInterpreter;

import static ckCommonUtils.CKPropertyStrings.CH_AIM;
import static ckCommonUtils.CKPropertyStrings.CH_EARTH;
import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_SCRY;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
import static ckCommonUtils.CKPropertyStrings.CH_WATER;
import static ckCommonUtils.CKPropertyStrings.CH_WIND;
import static ckCommonUtils.CKPropertyStrings.P_END_TURN;
import static ckCommonUtils.CKPropertyStrings.P_FRONT;
import static ckCommonUtils.CKPropertyStrings.P_INT;
import static ckCommonUtils.CKPropertyStrings.P_MOVETO;
import static ckCommonUtils.CKPropertyStrings.P_SELF;
import static ckCommonUtils.CKPropertyStrings.P_STAR;
import static ckCommonUtils.CKPropertyStrings.P_STRING;
import static ckCommonUtils.CKPropertyStrings.P_TARGET;

import java.util.ArrayList;
import java.util.Collection;

import ckCommonUtils.CKAreaPositions;
import ckCommonUtils.CKPosition;
import ckCommonUtils.Command;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGrid.GridNode;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItemSet;
import ckGameEngine.CKSpellCast;
import ckGameEngine.CKSpellResult;
import ckGameEngine.Direction;
import ckGraphicsEngine.CKSelection;

public class CKEditorPCController
{

	private static CKGridActor getCharacter()
	{
		return CKGameObjectsFacade.getCurrentPlayer();
	}

	@SuppressWarnings("unused")
	private static CKGridActor getActorAt(CKPosition pos)
	{
		CKAbstractGridItem item = getItemAt(pos);
		if (item instanceof CKGridActor)
		{
			return (CKGridActor) item;
		}
		return null;
	}

	private static CKAbstractGridItem getItemAt(CKPosition pos)
	{

		if (pos instanceof CKAreaPositions)
		{
			return new CKGridItemSet((CKAreaPositions) pos, CKGameObjectsFacade
					.getQuest().getGrid(), CKGameObjectsFacade.getQuest());
		}
		return CKGameObjectsFacade.getQuest().getGrid().getTopPosition(pos);
	}

	public static CKBook getAbilties()
	{
		CKArtifact art = CKPlayerObjectsFacade.getArtifact();
		if (art != null)
		{
			return art.getUsageLimits();
		}

		return getCharacter().getAbilities();
	}

	public static void unknownSpell(String s)
	{
		System.err.println("Unknown Spell:" + s);
	}

	public static void CPShortage(String s)
	{
		System.err.println("CPShortage:" + s);
	}

	/*
	 * checks the limits to determine if the spell can be cast.
	 */
	public static boolean attemptSpell(String chapter, int val, String modifier)
	{
		CKBook limits = getAbilties();
		//System.out.println(limits.treeString());
		if(CKGameObjectsFacade.isPrediction())
		{
			//System.out.println("Prediciton:"+modifier+"  "+val);
		}
		else
		{
			System.out.println(modifier+"  "+val);
		}

		if (!limits.meetsRequirements(chapter, val, modifier))
		{
			unknownSpell("you don't know that spell " + chapter + ":"
					+ modifier + "-" + val);
			return false;
		}

		// now test for enough CP - spell fizzle
		if(! CKGameObjectsFacade.isPrediction())
		{
			int ap = CKPlayerObjectsFacade.getCPTurnRemaining();
			// System.out.println("points left?" + ap);
			CKPlayerObjectsFacade.alterCPTurnRemaining(-val);
			getCharacter()
					.setCyberPoints(getCharacter().getCyberPoints() - val);

			if (ap >= val)
			{
				return true;
			} else
			{
				CPShortage("You don't have enough CP");
				return false;
			}
		}
		return true;


	}

	public static boolean voice(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_VOICE, CP, modifier))
		{
			
				CKSpellCast cast = new CKSpellCast(getItemAt(target),
						getCharacter(), CH_VOICE, modifier, CP, key);
				cast.castSpell();
			

			return true;

		}
		return false;
	}

	
	
	
	public static boolean voice(String modifier, int CP, CKPosition target)
	{
		return voice(modifier, CP, target, "");
	}

	
	public static boolean fire(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_FIRE, CP, modifier))
		{
			CKSpellCast cast = new CKSpellCast(getItemAt(target),
					getCharacter(), CH_FIRE, modifier, CP, key);
			cast.castSpell();
			return true;

		}
		return false;
	}

	public static boolean fire(String modifier, int CP, CKPosition target)
	{
		return fire(modifier, CP, target, "");
	}

	public static CKPosition aim(String modifier, int CP)
	{
		System.out.println("Aiming");
		if (attemptSpell(CH_AIM, CP, modifier))
		{
			if (modifier.equalsIgnoreCase(P_SELF))
			{
				return getCharacter().getPos();
			}
			if (modifier.equalsIgnoreCase(P_FRONT))
			{
				return getCharacter().getNextPosition();
			} else if (modifier.equalsIgnoreCase(P_TARGET))
			{
				CKPosition quick = getCharacter().getTurnController()
						.useStoredAim();
				if (quick != null)
				{
					return quick;
				}

				CKSelection sel = new CKSelection();
				CKPosition pos = sel.SelectTarget(getCharacter().getPos(), 0,
						CP);

				getCharacter().getTurnController().fireAimLogEvent(pos);
				return pos;
			} else if (modifier.equalsIgnoreCase(P_STAR))
			{
				CKPosition quick = getCharacter().getTurnController()
						.useStoredAim();
				if (quick != null)
				{
					return quick;
				}

				CKSelection sel = new CKSelection();
				ArrayList<CKPosition> offsets = new ArrayList<CKPosition>();
				offsets.add(new CKPosition(1, 1));
				offsets.add(new CKPosition(1, -1));
				offsets.add(new CKPosition(-1, 1));
				offsets.add(new CKPosition(-1, -1));
				CKPosition pos = sel.SelectTargetArea(getCharacter().getPos(),
						0, CP, offsets);

				getCharacter().getTurnController().fireAimLogEvent(pos);
				return pos;
			}
		}
		return null;

	}

	public static boolean move(String modifier, int CP)
	{
		if (attemptSpell(CH_MOVE, CP, modifier))
		{
			/*
			 * if(modifier.equalsIgnoreCase(P_FORWARD)) {
			 */
			// FIXME I do not work yet!!! getCharacter().move(CP);
			CKSpellCast cast = new CKSpellCast(getCharacter(), getCharacter(),
					CH_MOVE, modifier, CP, "");
			cast.castSpell();
			return true;
		}
		return false;

	}
	
	public static boolean moveTo()
	{
		CKBook book = getAbilties();
		int ap = 0;
		if(book.hasChapter(CH_MOVE))
		{
			ap = book.getChapter(CH_MOVE).getValue();
		}
				
				
		if(attemptSpell(CH_MOVE,ap,P_MOVETO))
		{
			
			int cp = (int) (ap *.75);
			//get data
			CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
			GridNode[][][][] nodes= grid.allPositionsReachable(getCharacter(), cp, 1);	
			//first aim at the areas
			Collection<CKPosition> possibles = grid.getReachablePositions(nodes,1);
			
			ArrayList<CKPosition>offsets = new ArrayList<>(1);
			offsets.add(new CKPosition(0,0));
			CKSelection sel = new CKSelection();
			CKPosition pos = sel.SelectTargetArea(getCharacter().getPos(),
					possibles,offsets);
			
			//now move to that position
			for(Direction d:Direction.values())
			{
				GridNode node = nodes[(int) pos.getX()][(int) pos.getY()][d.ordinal()][0];
				if(node.isVisited())
				{	
					moveTo(node);
					break;
				}
			}
			return true;
			
		}
		return false;
		
	}
	
	public static void moveTo(GridNode node)//,CKPosition destination)
	{
		//need to do the last thing first!
		GridNode parent = node.getParentNode(); 
		if(parent!=null) { moveTo(parent); }
		//now do my action
		String action = node.getAction();
		int cp = node.getActionCost();
		
		if(action.equals("") || action.equals(P_END_TURN))
		{
			return; //we are done
		}
		System.err.println("MoveTO "+action+" "+cp);
		CKSpellCast cast = new CKSpellCast(getCharacter(), getCharacter(),
				CH_MOVE, action, cp, "");
		cast.castSpell();
	}

	public static boolean earth(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_EARTH, CP, modifier))
		{
			CKSpellCast cast = new CKSpellCast(getItemAt(target),
					getCharacter(), CH_EARTH, modifier, CP, key);
			cast.castSpell();
			return true;

		}
		return false;
	}

	public static boolean earth(String modifier, int CP, CKPosition target)
	{
		return earth(modifier, CP, target, "");
	}

	public static boolean water(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_WATER, CP, modifier))
		{
			CKSpellCast cast = new CKSpellCast(getItemAt(target),
					getCharacter(), CH_WATER, modifier, CP, key);
			cast.castSpell();
			return true;

		}
		return false;
	}

	public static boolean water(String modifier, int CP, CKPosition target)
	{
		return water(modifier, CP, target, "");
	}

	public static boolean wind(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_WIND, CP, modifier))
		{
			CKSpellCast cast = new CKSpellCast(getItemAt(target),
					getCharacter(), CH_WIND, modifier, CP, key);
			System.out.println("Before Spell: "
					+ getCharacter().getCyberPoints());
			cast.castSpell();
			System.out.println("After Spell: "
					+ getCharacter().getCyberPoints());
			return true;

		}
		return false;
	}

	public static boolean wind(String modifier, int CP, CKPosition target)
	{
		return wind(modifier, CP, target, "");
	}

	@SuppressWarnings("deprecation")
	public static String scryString(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_SCRY, CP, modifier))
		{
			CKAbstractGridItem item = getItemAt(target);
			CKSpellCast cast = new CKSpellCast(item,
					getCharacter(), CH_SCRY, modifier, CP, key);
			cast.castSpell();
//			System.out.println(cast.getResult());
			return cast.getResult().getResult(item, "scry:"+key);

		}
		return "";
	}

	@SuppressWarnings("deprecation")
	public static int scryInt(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_SCRY, CP, modifier))
		{
			CKAbstractGridItem item = getItemAt(target);
			CKSpellCast cast = new CKSpellCast(item,
					getCharacter(), CH_SCRY, modifier, CP, key);
			cast.castSpell();
			System.out.println(cast.getResult());
			return Integer.parseInt(cast.getResult().getResult(item,"scry:"+key));

		}
		return 10000;
	}

	public static Object scry(String modifier, int CP, CKPosition target,
			String key)
	{
		if (modifier.compareToIgnoreCase(P_INT) == 0)
		{
			return scryInt(modifier, CP, target, key);
		} else if (modifier.compareToIgnoreCase(P_STRING) == 0)
		{
			return scryString(modifier, CP, target, key);
		}
		return null;
	}
	
	public static CKSpellResult predictCasting(Command cmd,int iterations)
	{
		
		CKGameObjectsFacade.startPrediction();
		for(int i=0;i<iterations;i++)
		{
			CKGameObjectsFacade.iteratePrediction();
			cmd.call();
		}
		return CKGameObjectsFacade.endPrediction();
	}

}
