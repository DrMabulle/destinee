/**
 * 
 */
package destinee.core.exception;

/**
 * @author Bubulle
 * 
 */
public class DestineeException extends Exception
{

	/**
	 * 
	 */
	public DestineeException()
	{
		super();
	}

	/**
	 * @param aMessage
	 * @param aCause
	 */
	public DestineeException(String aMessage, Throwable aCause)
	{
		super(aMessage, aCause);
	}

	/**
	 * @param aMessage
	 */
	public DestineeException(String aMessage)
	{
		super(aMessage);
	}

	/**
	 * @param aCause
	 */
	public DestineeException(Throwable aCause)
	{
		super(aCause);
	}

	/** */
	private static final long serialVersionUID = 3643088270785046542L;

}
