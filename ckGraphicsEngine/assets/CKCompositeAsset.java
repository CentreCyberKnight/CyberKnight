package ckGraphicsEngine.assets;

import java.util.Iterator;




/** Composite Assets are decorators that create an asset by possibly using more than 1 other asset.
 * @author Michael K. Bradshaw
 *
 */
public abstract class CKCompositeAsset extends CKGraphicsAsset
{
	
	protected CKCompositeAsset(String aid, String description)
	{
		super(aid, description);
	}


	/**creates an iterator for the assets in this asset
	 * @return an Iterator of CKGraphicsAsset that will cycle through all of the contained CKGrpahicAssests
	 */
	public abstract Iterator<CKGraphicsAsset> iterator();

	
	/**In classes that support ordering, this method will move the asset up in the ordering
	 * @param asset to change the ordering of
	 */
	public void moveUpAsset(CKGraphicsAsset asset) {}

	/**In classes that support ordering, this method will move the asset up in the ordering
	 * @param asset to change the ordering of
	 */
	public void moveDownAsset(CKGraphicsAsset asset) {}

	/** adds the asset from this class
	 * @param asset to be added
	 */
	public abstract void addAsset(CKGraphicsAsset asset);
	
	/** Removes the asset from this class
	 * @param asset to be removed
	 */
	public abstract void removeAsset(CKGraphicsAsset asset);

	/**In classes that support Height, increments the height of the asset.
	 * @param asset   Asset to alter the height of
	 * @param increment how much to change the height.  Use negative increments to decrease the height
	 */
	public void addHeight(CKGraphicsAsset asset, int increment) {}

	/**In classes that support Naming, get the Name of the Asset stored.
	 * @param asset   Asset to get the name of
	 */
	public String getAssetName(CKGraphicsAsset asset) {return "";}
	
	/**In classes that support Naming, changes the name of the asset.
	 * @param asset   Asset to alter the height of
	 * @param newName What is the new name of the asset
	 */
	public void renameAsset(CKGraphicsAsset asset,String newName){}
	
	/**Indicates if the class supports Height methods.  
	 * If it does not support height, unsupported height methods will simply not do anything 
	 * @return true if the class supports height methods
	 */
	public boolean supportsHeight(){ return false; }


	/**Indicates if the class supports Ordering methods.  
	 * If it does not support ordering, unsupported ordering methods will simply not do anything 
	 * @return true if the class supports ordering methods
	 */
	public boolean supportsOrdering() {return false; }

	
	/**Indicates if the class supports Naming methods.  
	 * If it does not support naming, unsupported naming methods will simply not do anything 
	 * @return true if the class supports naming methods
	 */
	public boolean supportsNaming() {return false; }

}