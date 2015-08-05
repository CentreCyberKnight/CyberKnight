package ckCommonUtils;

import ckGameEngine.Direction;

@FunctionalInterface
public interface AICommand
{
	public void doCommand(CKPosition origin,Direction dir,
			String targetType,int cp);
}
