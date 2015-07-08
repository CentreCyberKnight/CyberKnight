package ckSnapInterpreter;

import netscape.javascript.JSObject;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKSpell;

public class CKArtifactModel {
	
	public static CKArtifact artifact;
	public static CKSpell spell;
	
	public CKArtifactModel(CKArtifact artifact1, CKSpell spell1)
	{
		artifact = artifact1;
		spell = spell1;
	}

	/**
	 * @return the artifact
	 */
	public static CKArtifact getArtifact() {
		return artifact;
	}

	/**
	 * @param artifact the artifact to set
	 */
	public static void setArtifact(CKArtifact artifact) {
		CKArtifactModel.artifact = artifact;
		//JSObject jsobj = (JSObject) controlPanel.getwebEngine().executeScript("window");
		//jsobj.setMember("artifact", artifact);
		//controlPanel.getwebEngine().executeScript("ide.domino()");
	}

	/**
	 * @return the spell
	 */
	public static CKSpell getSpell() {
		return spell;
	}

	/**
	 * @param spell the spell to set
	 */
	public static void setSpell(CKSpell spell) {
		CKArtifactModel.spell = spell;
	}
	
	

}
