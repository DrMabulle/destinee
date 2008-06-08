/**
 * 
 */
package destinee.core.log;

import java.io.IOException;
import java.util.Properties;

import destinee.core.utils.FileAssistant;

/**
 * @author Bubulle
 * 
 */
public final class LogFactory
{
	/**
	 * 
	 */
	private static final int DEFAULT_LOG_SIZE = 200;
	private static final String LOG_LEVEL = "destinee.log.logLevel";
	private static final String CHEMIN = "./properties/";
	private static final String FICHIER = "application.properties";

	private static final String DEBUG = "debug";
	private static final String INFO = "info";
	private static final String WARN = "warn";
	private static final String ERROR = "error";

	private static final boolean isLogDebugEnabled;
	private static final boolean isLogInfoEnabled;
	private static final boolean isLogWarnEnabled;
	private static final boolean isLogErrorEnabled;

	/**
	 * 
	 */
	static
	{
		String logLevel;
		Properties props = new Properties();
		try
		{
			props.load(FileAssistant.getInputStream(CHEMIN, FICHIER));
			logLevel = props.getProperty(LOG_LEVEL);
		}
		catch (IOException e)
		{
			logLevel = DEBUG;
			System.err.println("La propriété " + LOG_LEVEL + " est mal renseignée.");
		}

		if (ERROR.equals(logLevel))
		{
			isLogDebugEnabled = false;
			isLogInfoEnabled = false;
			isLogWarnEnabled = false;
			isLogErrorEnabled = true;
		}
		else if (WARN.equals(logLevel))
		{
			isLogDebugEnabled = false;
			isLogInfoEnabled = false;
			isLogWarnEnabled = true;
			isLogErrorEnabled = true;
		}
		else if (INFO.equals(logLevel))
		{
			isLogDebugEnabled = false;
			isLogInfoEnabled = true;
			isLogWarnEnabled = true;
			isLogErrorEnabled = true;
		}
		else if (DEBUG.equals(logLevel))
		{
			isLogDebugEnabled = true;
			isLogInfoEnabled = true;
			isLogWarnEnabled = true;
			isLogErrorEnabled = true;
		}
		else
		{
			isLogDebugEnabled = true;
			isLogInfoEnabled = true;
			isLogWarnEnabled = true;
			isLogErrorEnabled = true;
			System.err.println("La propriété " + LOG_LEVEL + " est mal renseignée.");
		}
	}

	/**
	 * Crée une nouvelle instance de LogFactory
	 */
	private LogFactory()
	{
		super();
	}

	/**
	 * @return the isLogDebugEnabled
	 */
	public static final boolean isLogDebugEnabled()
	{
		return isLogDebugEnabled;
	}

	/**
	 * @return the isLogInfoEnabled
	 */
	public static final boolean isLogInfoEnabled()
	{
		return isLogInfoEnabled;
	}

	/**
	 * @return the isLogWarnEnabled
	 */
	public static final boolean isLogWarnEnabled()
	{
		return isLogWarnEnabled;
	}

	/**
	 * @return the isLogErrorEnabled
	 */
	public static final boolean isLogErrorEnabled()
	{
		return isLogErrorEnabled;
	}

	public static final void logDebug(final Object log)
	{
		if (isLogDebugEnabled() && log != null)
		{
			System.out.println(log.toString());
		}
	}

	public static final void logDebug(final Object[] msgArray)
	{
		if (isLogDebugEnabled())
		{
			StringBuffer sb = new StringBuffer(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			System.out.println(sb.toString());
		}
	}

	public static final void logInfo(final Object log)
	{
		if (isLogInfoEnabled() && log != null)
		{
			System.out.println(log.toString());
		}
	}

	public static final void logInfo(final Object[] msgArray)
	{
		if (isLogInfoEnabled())
		{
			StringBuffer sb = new StringBuffer(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			System.out.println(sb.toString());
		}
	}

	public static final void logWarn(final Object log)
	{
		if (isLogWarnEnabled() && log != null)
		{
			System.out.println(log.toString());
		}
	}

	public static final void logWarn(final Object[] msgArray)
	{
		if (isLogWarnEnabled())
		{
			StringBuffer sb = new StringBuffer(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			System.out.println(sb.toString());
		}
	}

	public static final void logError(final Object log)
	{
		if (isLogErrorEnabled() && log != null)
		{
			System.err.println(log.toString());
		}
	}

	public static final void logError(final Object[] msgArray)
	{
		if (isLogErrorEnabled())
		{
			StringBuffer sb = new StringBuffer(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			System.err.println(sb.toString());
		}
	}

}
