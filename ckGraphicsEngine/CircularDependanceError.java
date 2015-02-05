package ckGraphicsEngine;

/**
 * CircularDependanceError indicates that the requested Dependence would create a circular chain of dependencies that cannot be resolved.
 * @author Michael K. Bradshaw
 *
 */
public class CircularDependanceError extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8548560002169272733L;

	public CircularDependanceError()
	{
		super("Link will cause a Circular Dependance");
	}

	public CircularDependanceError(String arg0)
	{
		super("Link will cause a Circular Dependance:"+arg0);
	}

	public CircularDependanceError(Throwable arg0)
	{
		super("Link will cause a Circular Dependance",arg0);
	}

	public CircularDependanceError(String arg0, Throwable arg1)
	{
		super("Link will cause a Circular Dependance:"+arg0, arg1);
	}

}
