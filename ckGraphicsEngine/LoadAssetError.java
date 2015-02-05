package ckGraphicsEngine;

/**
 * LoadAssetError indicates that the asset could not be loaded into memory
 * @author Michael K. Bradshaw
 *
 */
public class LoadAssetError extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8548560002169272733L;

	public LoadAssetError()
	{
		super("Unable to Load Asset");
	}

	public LoadAssetError(String arg0)
	{
		super("Unable to Load Asset "+arg0);
	}

	public LoadAssetError(Throwable arg0)
	{
		super("Unale to Load Asset",arg0);
	}

	public LoadAssetError(String arg0, Throwable arg1)
	{
		super("Unable to Load Asset"+arg0, arg1);
	}

}
