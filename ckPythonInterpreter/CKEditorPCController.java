package ckPythonInterpreter;

import java.util.ArrayList;

import ckCommonUtils.CKAreaPositions;
import ckCommonUtils.CKPosition;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItemSet;
import ckGameEngine.CKSpellCast;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.CKSelection;
import static ckCommonUtils.CKPropertyStrings.*;

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
		System.out.println(limits.treeString());

		if (!limits.meetsRequirements(chapter, val, modifier))
		{
			unknownSpell("you don't know that spell " + chapter + ":"
					+ modifier + "-" + val);
			return false;
		}

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

	public static boolean voice(String modifier, int CP, CKPosition target,
			String key)
	{
		if (attemptSpell(CH_VOICE, CP, modifier))
		{
			if (modifier.equalsIgnoreCase(P_TALK))
			{
				// TODO remove PC pc = (PC)target;
				CKSpellCast cast = new CKSpellCast(getItemAt(target),
						getCharacter(), CH_VOICE, modifier, CP, key);
				cast.castSpell();
			}

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
