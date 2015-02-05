package ckCommonUtils;

public interface CKWorkSupervisorListener<T>
{
	/**
	 * Observer will call this function when work has been completed
	 * @param entity
	 */
public void workCompleted(T entity);

}
