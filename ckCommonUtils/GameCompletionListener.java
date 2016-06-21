package ckCommonUtils;

@FunctionalInterface
public interface GameCompletionListener
{
	public void endGame(int state,String questID);
}
