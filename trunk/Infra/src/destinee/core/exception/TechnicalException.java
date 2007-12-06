/**
 * 
 */
package destinee.core.exception;

/**
 * @author Bubulle
 * 
 */
public class TechnicalException extends DestineeException
{
	/** */
	public TechnicalException()
	{
		super("Exception technique");
	}

	/**
	 * @param aArg0 description de l'exception
	 * @param aArg1 Exception liée
	 */
	public TechnicalException(String aArg0, Throwable aArg1)
	{
		super("Exception technique : " + aArg0, aArg1);
	}

	/**
	 * @param aArg0 description de l'exception
	 */
	public TechnicalException(String aArg0)
	{
		super("Exception technique : " + aArg0);
	}

	/**
	 * @param aArg0 Exception liée
	 */
	public TechnicalException(Throwable aArg0)
	{
		super(aArg0);
	}

	/** */
	private static final long serialVersionUID = -9136219271378885582L;

}
