package ckCommonUtils;

@FunctionalInterface
public interface INTERPOLATE{
	
	public CKPosition interpolate(CKPosition spos, CKPosition epos, float frac);
	
}