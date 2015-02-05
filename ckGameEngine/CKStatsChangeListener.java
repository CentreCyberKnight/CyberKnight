package ckGameEngine;

public interface CKStatsChangeListener
{
	public void equippedChanged();
	public void statsChanged(CKBook stats);
	public void cpChanged(int cp);
	
}
