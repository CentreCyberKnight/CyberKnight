package ckCommonUtils;

public interface CKEntitySelectedListener<T>
{
	/**
	 * Observer will call this function when an entity has been selected
	 * @param asset
	 */
public void entitySelected(T entity);

}
