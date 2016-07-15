package ckEditor.treegui;

import ckGameEngine.CKSpellCast;

public interface CKActorInterface{
	
	int doActorEffect(CKSpellCast cast,boolean source, int startTime);

	Object clone();
	
}