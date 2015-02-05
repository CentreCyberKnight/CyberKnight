package ckCommonUtils;



/** Classes that implement this interface will return a shallow copy of themselves in type T
 * @author dragonlord
 *
 * @param <T> The type of object that will retrurn a shallow copy.
 */
public interface ShallowCopyInterface<T>
{

	
	/**
	 * @return a copy of Type T
	 */
	public T createShallowCopy();
}
