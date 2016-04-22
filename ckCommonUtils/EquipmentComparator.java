package ckCommonUtils;
import static ckCommonUtils.CKPropertyStrings.P_ARMOR;
import static ckCommonUtils.CKPropertyStrings.P_MAGIC;
import static ckCommonUtils.CKPropertyStrings.P_MISC1;
import static ckCommonUtils.CKPropertyStrings.P_MISC2;
import static ckCommonUtils.CKPropertyStrings.P_MISC3;
import static ckCommonUtils.CKPropertyStrings.P_MISC4;
import static ckCommonUtils.CKPropertyStrings.P_MISC5;
import static ckCommonUtils.CKPropertyStrings.P_MISC6;
import static ckCommonUtils.CKPropertyStrings.P_OFFHAND_WEAPON;
import static ckCommonUtils.CKPropertyStrings.P_SHIRT;
import static ckCommonUtils.CKPropertyStrings.P_SHOES;
import static ckCommonUtils.CKPropertyStrings.P_SWORD;

import java.util.Comparator;
import java.util.HashMap;


public  class EquipmentComparator implements Comparator<String>
{
	private static HashMap<String,Integer> eMap=null; 

	private static void prepCompareMap()
	{
		eMap=new HashMap<String,Integer>();
		eMap.put(P_SHOES,0);
		eMap.put(P_SWORD,1000);
		eMap.put(P_OFFHAND_WEAPON,2000);
		eMap.put(P_MAGIC,3000);
		eMap.put(P_SHIRT,5000);
		eMap.put(P_ARMOR,6000);
		eMap.put(P_MISC1,90001);
		eMap.put(P_MISC2,90002);
		eMap.put(P_MISC3,90003);
		eMap.put(P_MISC4,90004);
		eMap.put(P_MISC5,90005);
		eMap.put(P_MISC6,90006);		
	}

	private static EquipmentComparator CompInstance;

	public static EquipmentComparator getComparator()
	{
		if(CompInstance==null)
		{
			CompInstance=new EquipmentComparator();
		}
		return CompInstance;
	}

	private EquipmentComparator()
	{
		if(eMap==null)
		{
			prepCompareMap();
		}
	}

	private static int getValue(String key)
	{
		Integer val = eMap.get(key);
		if(val == null) { return 9000; }
		else               { return val.intValue(); }
	}
	@Override
	public int compare(String o1, String o2)
	{
		return getValue(o1) - getValue(o2);
	}		
}