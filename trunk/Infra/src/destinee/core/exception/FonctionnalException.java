/**
 * 
 */
package destinee.core.exception;

/**
 * @author Bubulle
 * 
 */
public class FonctionnalException extends DestineeException
{

	/** */
	public FonctionnalException()
	{
		super("Exception fonctionnelle");
	}

	/**
	 * @param aMessage description de l'exception
	 * @param aCause Exception liée
	 */
	public FonctionnalException(String aMessage, Throwable aCause)
	{
		super("Exception fonctionnelle : " + aMessage, aCause);
	}

	/**
	 * @param aMessage description de l'exception
	 */
	public FonctionnalException(String aMessage)
	{
		super("Exception fonctionnelle : " + aMessage);
	}

	/**
	 * @param aCause Exception liée
	 */
	public FonctionnalException(Throwable aCause)
	{
		super(aCause);
	}

	/** */
	private static final long serialVersionUID = -1573676643174576719L;

}
