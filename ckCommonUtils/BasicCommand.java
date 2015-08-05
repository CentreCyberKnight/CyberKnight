package ckCommonUtils;

@FunctionalInterface
public interface BasicCommand
{
	public void doAction(CKPosition target, int cp);
	
}
