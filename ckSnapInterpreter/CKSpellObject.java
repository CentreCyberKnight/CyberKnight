package ckSnapInterpreter;

import static ckCommonUtils.CKPropertyStrings.CH_AIM;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_SCRY;
import static ckCommonUtils.CKPropertyStrings.P_END_TURN;
import static ckCommonUtils.CKPropertyStrings.P_INT;
import static ckCommonUtils.CKPropertyStrings.P_MOVETO;
import static ckCommonUtils.CKPropertyStrings.P_STRING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import ckCommonUtils.CKAreaPositions;
import ckCommonUtils.CKDelayedObject;
import ckCommonUtils.CKPosition;
import ckDatabase.AimDescriptionFactory;
import ckGameEngine.AimDescription;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGrid.GridNode;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItemSet;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGraphicsEngine.CKSelection;
import ckPythonInterpreter.CKPlayerObjectsFacade;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

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

	public static CKAbstractGridItem getItemAt(CKPosition pos)
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
		//System.out.println(modifier+"  "+val);

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
	
	public CKDelayedObject<Boolean> selfSpell(String chapter, String page, int CP, 
			//Object protoTarget, //CKPosition target or CKDelayedObject<CKPosition>,
			String key)
	{
		
		return spell(chapter,page,CP,  getCharacter().getPos() ,key);
	}

	
	public CKDelayedObject<Boolean> spell(String chapter, String page, int CP, 
			Object protoTarget, //CKPosition target or CKDelayedObject<CKPosition>,
			String key)
	{
		//System.out.println("You are in Spell");

		CKPosition target =null;
		if(protoTarget instanceof CKPosition     )
		{
			target = (CKPosition) protoTarget;
		}
		else if (protoTarget instanceof CKDelayedObject)
		{
			//System.out.println("You are using the delayedSpell");
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
						//System.err.println("Casting spell:"+cast+" FX thread?"+Platform.isFxApplicationThread());
						
						//Quest w =CKGameObjectsFacade.getQuest();
						//w.startTransaction();
						
						cast.castSpell();
						completed.setValue(true);
						//w.endTransaction();

						
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

	

	

	public CKDelayedObject<CKPosition> aim(String modifier, int CP)
	{
		System.out.println("Aiming: "+modifier);
		if (attemptSpell(CH_AIM, CP, modifier))
		{

			CKPosition quick = getCharacter().getTurnController()
					.useStoredAim();
			if (quick != null)
			{
				return new CKDelayedObject<CKPosition>(quick);
			}

			
			
			//check DB
			
			AimDescription aim = AimDescriptionFactory.getInstance()
					.getAsset(modifier);

			CKGridActor actor = getCharacter();
			CKPosition origin = actor.getPos(); 
			Direction dir = actor.getDirection();
			CKPosition [] offsets = AimDescription.calculateTarget(origin,
											aim.getOffsets(dir));

			CKDelayedObject<CKPosition> target = new CKDelayedObject<>();
			
			if(aim.isAutoResolve())
			{
				if(offsets.length==1) 
				{
					target.setValue(offsets[0]);
				}
				else
				{
					target.setValue(new CKAreaPositions(origin,offsets));
				}
				return target;
			}
			else
			{

				Thread t = new Thread()
				{
					
					public void run()
					{
						CKSelection sel = new CKSelection();
						//blocking call
						CKPosition pos;
						if(offsets.length==1)
						{
							pos = sel.SelectTarget(origin, 
									aim.getMinDistance(),aim.getMaxDistance());
						}
						else
						{
						pos = sel.SelectTargetArea(origin,
								aim.getMinDistance(),aim.getMaxDistance(),
								//new ArrayList<CKPosition>(Arrays.asList(offsets)));//these will be offset in the underlying aim selector
								new ArrayList<CKPosition>(Arrays.asList(aim.getOffsets(dir))));
						}
						getCharacter().getTurnController().fireAimLogEvent(pos);

						target.setValue(pos);
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
				return target;	
				
				
			}
		}
		return null;
			
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
