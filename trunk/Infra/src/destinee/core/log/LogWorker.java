/**
 * 
 */
package destinee.core.log;

/**
 * @author Bubulle
 * 
 */
public final class LogWorker implements Runnable
{
	private Throwable throwable;
	private String message;
	private String level;

	/**
	 * Crée une nouvelle instance de LogWorker
	 */
	public LogWorker(final String aLevel, final String aMessage)
	{
		super();
		level = aLevel;
		message = aMessage;
	}

	/**
	 * Crée une nouvelle instance de ContextLog
	 */
	public LogWorker(final String aLevel, final String aMsg, final Throwable aThw)
	{
		super();
		throwable = aThw;
		message = aMsg;
		level = aLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		if (LogFactory.ERROR.equals(level))
		{
			System.err.println(message);
			if (throwable != null)
			{
				throwable.printStackTrace();
			}
		}
		else
		{
			System.out.println(message);
		}
	}

}
