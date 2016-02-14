package ckSnapInterpreter;

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
import static ckCommonUtils.CKPropertyStrings.P_TALK;
import static ckCommonUtils.CKPropertyStrings.P_TARGET;
import static ckCommonUtils.CKPropertyStrings.P_SHORT_TARGET;

import java.util.ArrayList;
import java.util.Collection;

import org.python.modules.time.Time;

import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import ckCommonUtils.CKAreaPositions;
import ckCommonUtils.CKDelayedObject;
import ckCommonUtils.CKPosition;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItemSet;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGameEngine.CKGrid.GridNode;
import ckGraphicsEngine.CKSelection;
import ckPythonInterpreter.CKPlayerObjectsFacade;

public class CKSpellObject {
	public String name;
	
	public CKSpellObject (String called) {
		name = called;
	}
	
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
		System.out.println(limits.treeString());
		System.out.println(modifier+"  "+val);

		/*
		 * 
		 * FIXME We will need to uncomment this in the future.
		if (!limits.meetsRequirements(chapter, val, modifier))
		{
			unknownSpell("you don't know that spell " + chapter + ":"
					+ modifier + "-" + val);
			return false;
		}
*/
		// now test for enough CP - spell fizzle
		int ap = CKPlayerObjectsFacade.getCPTurnRemaining();
		System.out.println("points left?" + ap);
		CKPlayerObjectsFacade.alterCPTurnRemaining(-val);
		getCharacter().setCyberPoints(getCharacter().getCyberPoints() - val);
		

		
		if (ap >= val)
		{
			return true;
		} else
		{
			CPShortage("You don't have enough CP");

			return false;
		}


	}
	
/*	public boolean spell(String chapter, String page, int CP, 
			CKDelayedObject<CKPosition> target,
			String key)
	{
		System.out.println("You are in the delayedSpell");
		if (! target.isSet())
		{
			throw new NullPointerException("CKDelayedObject has not been set");
		}
		return spell(chapter,page,CP,target.getValue(),key);
	}
	*/
	
	public CKDelayedObject<Boolean> spell(String chapter, String page, int CP, 
			Object protoTarget, //CKPosition target or CKDelayedObject<CKPosition>,
			String key)
	{
		System.out.println("You are in Spell");

	//	public boolean spell(String chapter, String page, int CP, 
	//			CKDelayedObject<CKPosition> target,
	//			String key)
		
		CKPosition target =null;
		if(protoTarget instanceof CKPosition     )
		{
			target = (CKPosition) protoTarget;
		}
		else if (protoTarget instanceof CKDelayedObject)
		{
			System.out.println("You are using the delayedSpell");
			target = ((CKDelayedObject<CKPosition>) protoTarget ).getValue();
			if (target==null )
			{
				throw new NullPointerException("CKDelayedObject has not been set");
			}
		}
		
		
		if (attemptSpell(chapter, CP, page))
		{
			
			CKDelayedObject<Boolean> completed = new CKDelayedObject<>();
			CKSpellCast cast = new CKSpellCast(getItemAt(target),
					getCharacter(), chapter,page, CP, key);
					
			Thread t = new Thread()
					{
			
					public void run()
					{

						cast.castSpell();
						completed.setValue(true);

						Platform.runLater(new Runnable() {
								public void run()
								{
									WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
									webEngine.executeScript("ide.stage.threads.resumeAll(ide.stage)");
								}
						});

					}
					};
			t.start();
			return completed;
		}
		return null;
	}

	

	
	
	public CKDelayedObject<CKPosition> delayAim(String modifier, int CP)
	{
		System.out.println("DELAY Aiming:" +modifier);
		if (attemptSpell(CH_AIM, CP, modifier))
		{
			if (modifier.equalsIgnoreCase(P_SHORT_TARGET))
			{
				CKPosition quick = getCharacter().getTurnController()
						.useStoredAim();
				if (quick != null)
				{
					
					return new CKDelayedObject<CKPosition>(quick);
				}

				CKDelayedObject<CKPosition> delPos = new CKDelayedObject<>();
				Thread t = new Thread()
						{
				
						public void run()
						{
							CKSelection sel = new CKSelection();
							CKPosition pos = sel.SelectTarget(getCharacter().getPos(), 0,
									CP);

							getCharacter().getTurnController().fireAimLogEvent(pos);
							delPos.setValue(pos);
							Platform.runLater(new Runnable() {
									public void run()
									{
										WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
										webEngine.executeScript("ide.stage.threads.resumeAll(ide.stage)");
									}
							});
							//do call back here
						}			
						};
						t.start();
				return delPos;
			} else if (modifier.equalsIgnoreCase(P_STAR))
			{
				CKPosition quick = getCharacter().getTurnController()
						.useStoredAim();
				if (quick != null)
				{
					return new CKDelayedObject<CKPosition>(quick);
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
				
				return new CKDelayedObject<CKPosition>(pos);
				//return pos;
			}
		}
		return null;

	}
	
	
	public CKPosition aim(String modifier, int CP)
	{
		System.out.println("Aiming: "+modifier);
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

				
				Thread t = new Thread()
						{
				
						public void run()
						{
							CKSelection sel = new CKSelection();
							CKPosition pos = sel.SelectTarget(getCharacter().getPos(), 0,
									CP);

							getCharacter().getTurnController().fireAimLogEvent(pos);
							//do call back here
						}			

						};
						t.start();
				return null;
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
		}		return null;

	}
	

	
	public boolean move2(String modifier, int CP)
	{
		System.out.println("Inside of move CKSpellObject move2");
		
		Quest w =CKGameObjectsFacade.getQuest();
		w.startTransaction();
		boolean ret = move(modifier, CP);
		w.endTransaction();
		return ret;
	}
	
	public static boolean move(String modifier, int CP)
	{
		System.out.println("Inside of move CKSpellObject");
		if (attemptSpell(CH_MOVE, CP, modifier))
		{
			/*
			 * if(modifier.equalsIgnoreCase(P_FORWARD)) {
			 */
			// FIXME I do not work yet!!! getCharacter().move(CP);
			CKSpellCast cast = new CKSpellCast(getCharacter(), getCharacter(),
					CH_MOVE, modifier, CP, "");
			cast.castSpell();

			WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
			JSObject jsobj = (JSObject) webEngine.executeScript("window");
			webEngine.executeScript("ide.stage.threads.resumeAll(ide.stage)");
			/*
			int num = 0;
			jsobj.setMember("javaNum", num); //set something to indicate that we are don
			*/
			return true;
		}
		WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		/*
		int num = 0;
		jsobj.setMember("javaNum", num); //set something to indicate that we are don
		*/
		webEngine.executeScript("ide.stage.threads.resumeAll(ide.stage)");
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
	
	private static void moveTo(GridNode node)//,CKPosition destination)
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

}
