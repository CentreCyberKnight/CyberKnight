package ckCommonUtils;

public interface CKThreadCompletedListener
{
	/**
	 * is called when the thread completes its actions
	 * @param error true if an error occurred
	 */
	public void threadFinishes(boolean error);
}
