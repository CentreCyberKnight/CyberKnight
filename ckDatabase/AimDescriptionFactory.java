package ckDatabase;
import java.util.HashMap;
import ckCommonUtils.CKPosition;
import ckGameEngine.AimDescription;
import ckGameEngine.Direction;
import static ckCommonUtils.CKPropertyStrings.*;

/**A CK GraphicsAsset fFactory that uses XML files store data
 * 
 * @author Michael K. Bradshaw
 *
 */
public class AimDescriptionFactory
{

	
	private static AimDescriptionFactory factory= null;
  
 
 

	public static AimDescriptionFactory getInstance()
	{
		if(factory==null)
		{
			factory = new AimDescriptionFactory();
		}
		return factory;
	}


	
	private HashMap<String,AimDescription> assetMap = null;
		
	private AimDescriptionFactory()
	{
		assetMap = new HashMap<>();
		loadAssets();
	
	}


	
	private void loadAssets()
	{
	
		CKPosition [] selfO = {new CKPosition(0,0)};
		AimDescription self =
				new AimDescription(0,selfO,Direction.NONE,true,0,.25);
		assetMap.put(P_SELF, self);
		
		CKPosition [] swipeO = {new CKPosition(1,0),new CKPosition(1,1),new CKPosition(1,-1)};
		AimDescription swipe =
				new AimDescription(2,swipeO,Direction.SOUTHEAST,true,0,.25);
		assetMap.put(P_SWIPE,swipe);
		
		CKPosition [] frontO = {new CKPosition(1,0)};
		AimDescription front =
				new AimDescription(0,frontO,Direction.SOUTHEAST,true,0,.25);
		assetMap.put(P_FRONT, front);
		
		CKPosition [] shortTargetO = {new CKPosition(0,0)};
		AimDescription shortTarget =
				new AimDescription(1,shortTargetO,Direction.NONE,false,0,4);
		assetMap.put(P_SHORT_TARGET, shortTarget);
		AimDescription medTarget =
				new AimDescription(2,shortTargetO,Direction.NONE,false,1.5,8);
		assetMap.put(P_MID_TARGET, medTarget);
		AimDescription distantTarget =
				new AimDescription(3,shortTargetO,Direction.NONE,false,1.5,20);
		assetMap.put(P_FAR_TARGET, distantTarget);
		
		CKPosition[] starO = {new CKPosition(1, 1), new CKPosition(1, -1),
				new CKPosition(-1, 1), new CKPosition(-1, -1)};
		AimDescription star =
				new AimDescription(3,starO,Direction.NONE,false,1,5);
		assetMap.put(P_STAR, star);		
		
		
		
		
		
		
		
		
		
		
	}









	public  AimDescription getAsset(String assetID)
	{
		return assetMap.get(assetID);
	}

	
	
	

}
