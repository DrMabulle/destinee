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
public class LogFactory
{
	private static final String LOG_LEVEL = "destinee.log.logLevel";
	private static final String chemin = "./properties/destinee/properties/application.properties";

	private static final String DEBUG = "debug";
	private static final String INFO = "info";
	private static final String WARN = "warn";
	private static final String ERROR = "error";

	private static boolean isLogDebugEnabled = true;
	private static boolean isLogInfoEnabled = true;
	private static boolean isLogWarnEnabled = true;
	private static boolean isLogErrorEnabled = true;

	/**
	 * 
	 */
	static
	{
		String logLevel;
		Properties props = new Properties();
		try
		{
			props.load(FileAssistant.getInputStream(chemin));
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
	 * @return the isLogDebugEnabled
	 */
	public static boolean isLogDebugEnabled()
	{
		return isLogDebugEnabled;
	}

	/**
	 * @return the isLogInfoEnabled
	 */
	public static boolean isLogInfoEnabled()
	{
		return isLogInfoEnabled;
	}

	/**
	 * @return the isLogWarnEnabled
	 */
	public static boolean isLogWarnEnabled()
	{
		return isLogWarnEnabled;
	}

	/**
	 * @return the isLogErrorEnabled
	 */
	public static boolean isLogErrorEnabled()
	{
		return isLogErrorEnabled;
	}

	public static void logDebug(Object log)
	{
		if (isLogDebugEnabled())
		{
			System.out.println(log.toString());
		}
	}

	public static void logInfo(Object log)
	{
		if (isLogInfoEnabled())
		{
			System.out.println(log.toString());
		}
	}

	public static void logWarn(Object log)
	{
		if (isLogWarnEnabled())
		{
			System.out.println(log.toString());
		}
	}

	public static void logError(Object log)
	{
		if (isLogErrorEnabled())
		{
			System.err.println(log.toString());
		}
	}

}
