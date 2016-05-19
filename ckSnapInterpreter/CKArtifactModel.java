package ckSnapInterpreter;

import ckGameEngine.CKArtifact;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpell;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class CKArtifactModel {
	
	public static CKArtifact artifact;
	//public static CKSpell spell;
	
	public CKArtifactModel(CKArtifact artifact1, CKSpell spell1)
	{
		artifact = artifact1;
		//spell = spell1;
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
		WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		jsobj.setMember("artifact", artifact);
		webEngine.executeScript("ide.setCyberSnap()");
	}
	
	public static void generateXML()
	{
		WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		webEngine.executeScript("ide.ckExportXML('helloWorld')");
	}

	public static void loadXML()
	{
		WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		webEngine.executeScript("ide.ckImportXML()");
	}
	
	/**
	 * @return the spell
	 */
	/*
	public static CKSpell getSpell() {
		return spell;
	}
	*/

	/**
	 * @param spell the spell to set
	 *//*
	public static void setSpell(CKSpell spell) {
		CKArtifactModel.spell = spell;
	}
	*/
	
	

}
