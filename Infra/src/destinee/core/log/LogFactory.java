/**
 * 
 */
package destinee.core.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private static final int DEFAULT_LOG_SIZE = 256;
	private static final String LOG_LEVEL = "destinee.log.logLevel";
	private static final String CHEMIN = "./properties/";
	private static final String FICHIER = "application.properties";

	protected static final String DEBUG = "debug";
	protected static final String INFO = "info";
	protected static final String WARN = "warn";
	protected static final String ERROR = "error";

	private static final boolean isLogDebugEnabled;
	private static final boolean isLogInfoEnabled;
	private static final boolean isLogWarnEnabled;
	private static final boolean isLogErrorEnabled;

	private static PrintWriter printer = null;

	private static ExecutorService executor = Executors.newSingleThreadExecutor();

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
			executor.execute(new LogWorker(DEBUG, log.toString()));
			// System.out.println(log.toString());
		}
	}

	public static final void logDebug(final Object[] msgArray)
	{
		if (isLogDebugEnabled())
		{
			StringBuilder sb = new StringBuilder(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			executor.execute(new LogWorker(DEBUG, sb.toString()));
			// System.out.println(sb.toString());
		}
	}

	public static final void logInfo(final Object log)
	{
		if (isLogInfoEnabled() && log != null)
		{
			executor.execute(new LogWorker(INFO, log.toString()));
			// System.out.println(log.toString());
		}
	}

	public static final void logInfo(final Object[] msgArray)
	{
		if (isLogInfoEnabled())
		{
			StringBuilder sb = new StringBuilder(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			executor.execute(new LogWorker(INFO, sb.toString()));
			// System.out.println(sb.toString());
		}
	}

	public static final void logWarn(final Object log)
	{
		if (isLogWarnEnabled() && log != null)
		{
			executor.execute(new LogWorker(WARN, log.toString()));
			// System.out.println(log.toString());
		}
	}

	public static final void logWarn(final Object[] msgArray)
	{
		if (isLogWarnEnabled())
		{
			StringBuilder sb = new StringBuilder(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			executor.execute(new LogWorker(WARN, sb.toString()));
			// System.out.println(sb.toString());
		}
	}

	public static final void logError(final Object log)
	{
		if (isLogErrorEnabled() && log != null)
		{
			executor.execute(new LogWorker(ERROR, log.toString()));
			// System.err.println(log.toString());
		}
	}

	public static final void logError(final Object[] msgArray)
	{
		if (isLogErrorEnabled())
		{
			StringBuilder sb = new StringBuilder(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			executor.execute(new LogWorker(ERROR, sb.toString()));
			// System.err.println(sb.toString());
		}
	}

	public static final void logError(final Object log, final Throwable aThrowable)
	{
		if (isLogErrorEnabled() && log != null)
		{
			executor.execute(new LogWorker(ERROR, log.toString(), aThrowable));
			// System.err.println(log.toString());
		}
	}

	public static final void logError(final Object[] msgArray, final Throwable aThrowable)
	{
		if (isLogErrorEnabled())
		{
			StringBuilder sb = new StringBuilder(DEFAULT_LOG_SIZE);
			for (Object theObject : msgArray)
			{
				sb.append(theObject);
			}
			executor.execute(new LogWorker(ERROR, sb.toString(), aThrowable));
			// System.err.println(sb.toString());
		}
	}

	private static final void logToFile(final String filename, final String msg)
	{
		try
		{
			if (printer == null)
			{
				FileWriter fileWriter = new FileWriter(filename, true);
				printer = new PrintWriter(fileWriter);
			}
			printer.println(msg);
			printer.flush();
		}
		catch (IOException e)
		{
			logError(e);
		}
	}

	public static final void logToFile(final String msg)
	{
		logToFile("./output.log", msg);

		if (isLogInfoEnabled())
		{
			logInfo(msg);
		}
	}

	public static final void logToFile(final Object[] msg)
	{
		StringBuilder sb = new StringBuilder(65536);
		for (Object theObject : msg)
		{
			sb.append(theObject);
		}
		logToFile(sb.toString());
	}

	public static final void stopper()
	{
		executor.shutdown();
	}
}
