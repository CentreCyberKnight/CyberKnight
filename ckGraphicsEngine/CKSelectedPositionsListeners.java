package ckGraphicsEngine;

import ckCommonUtils.CKPosition;

public interface CKSelectedPositionsListeners
{

	/**
	 * Listener for Selected positions
	 * @param pos-array of CKPositions 
	 */
	public void NotifyOfTargets(CKPosition pos);
	
	
}
