/**
 * 
 */
package destinee.core.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import destinee.core.exception.TechnicalException;
import destinee.core.utils.FileAssistant;

/**
 * @author Bubulle et No-one
 * 
 */
public final class PropertiesFactory
{
	private static final String CHEMIN = "./properties/";
	private static final String FICHIER = "application.properties";
	private static Properties props;

	/**
	 * Crée une nouvelle instance de PropertiesFactory
	 */
	private PropertiesFactory()
	{
		super();
	}

	private static final void init() throws TechnicalException
	{
		init(CHEMIN, FICHIER);
	}

	private static final void init(final String aPath, final String aFileName) throws TechnicalException
	{
		props = new Properties();
		try
		{
			props.load(FileAssistant.getInputStream(aPath, aFileName));
		}
		catch (FileNotFoundException e)
		{
			throw new TechnicalException("Fichier des proprietés non trouvé", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException("Lecture du fichier impossible", e);
		}

	}

	public static final Boolean getBoolean(final String aCle) throws TechnicalException
	{
		if (props == null)
		{
			init();
		}
		String temp = props.getProperty(aCle);
		if (temp == null)
		{
			throw new TechnicalException("cle inexistante : " + aCle);
		}
		return Boolean.valueOf(temp);
	}

	public static final String getString(final String aCle) throws TechnicalException
	{
		if (props == null)
		{
			init();
		}
		String temp = props.getProperty(aCle);
		if (temp == null)
		{
			throw new TechnicalException("cle inexistante : " + aCle);
		}
		return temp;
	}

	public static final Boolean getOptionalBoolean(final String aCle) throws TechnicalException
	{
		if (props == null)
		{
			init();
		}
		String temp = props.getProperty(aCle, "");
		return Boolean.valueOf(temp);
	}

	public static final String getOptionalString(final String aCle) throws TechnicalException
	{
		if (props == null)
		{
			init();
		}
		String temp = props.getProperty(aCle, "");
		return temp;
	}

}
