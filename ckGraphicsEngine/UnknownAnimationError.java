package ckGraphicsEngine;

/**
 * UnknownAnimationError indicates that the instance does not support the animation requested
 * @author Michael K. Bradshaw
 *
 */
public class UnknownAnimationError extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8548560002169272733L;

	public UnknownAnimationError()
	{
		super("Unknown Animation");
	}

	public UnknownAnimationError(String arg0)
	{
		super("Unknown Animation:"+arg0);
	}

	public UnknownAnimationError(Throwable arg0)
	{
		super("Unknown Animation",arg0);
	}

	public UnknownAnimationError(String arg0, Throwable arg1)
	{
		super("Unknown Animation:"+arg0, arg1);
	}

}
