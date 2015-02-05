package ckGraphicsEngine;

/**
 * BadInstanceIDErrro indicates that the instance Id could not be found
 * @author Michael K. Bradshaw
 *
 */
public class BadInstanceIDError extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1256507814478533387L;

	public BadInstanceIDError()
	{
		super("Unknown Instance ID");
	}

	public BadInstanceIDError(String arg0)
	{
		super("Unknown Instance ID "+arg0);
	}

	public BadInstanceIDError(Throwable arg0)
	{
		super("Unknown Instance ID",arg0);
	}

	public BadInstanceIDError(String arg0, Throwable arg1)
	{
		super("Unknown Instance ID"+arg0, arg1);
	}

}
