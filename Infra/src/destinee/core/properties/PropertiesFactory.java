/**
 * 
 */
package destinee.core.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import destinee.core.exception.TechnicalException;

/**
 * @author Bubulle et No-one
 * 
 */
public class PropertiesFactory
{
	private static final String chemin = "./properties/destinee/properties/application.properties";
	private static Properties props = null;

	private static void init()
	{
		init(chemin);
	}

	private static void init(String aFileName)
	{
		props = new Properties();
		try
		{
			props.load(new FileInputStream(chemin));
		}
		catch (FileNotFoundException e)
		{
			new TechnicalException("Fichier des proprietés non trouvé", e);
		}
		catch (IOException e)
		{
			new TechnicalException("Lecture du fichier impossible", e);
		}

	}

	public static Boolean getBoolean(String aCle) throws TechnicalException
	{
		if (props == null)
		{
			init();
		}
		String temp = (String) props.getProperty(aCle);
		if (temp == null)
		{
			throw new TechnicalException("cle inexistante : " + aCle);
		}
		return Boolean.valueOf(temp);
	}

	public static String getString(String aCle) throws TechnicalException
	{
		if (props == null)
		{
			init();
		}
		String temp = (String) props.getProperty(aCle);
		if (temp == null)
		{
			throw new TechnicalException("cle inexistante : " + aCle);
		}
		return temp;
	}

	public static Boolean getOptionalBoolean(String aCle)
	{
		if (props == null)
		{
			init();
		}
		String temp = (String) props.getProperty(aCle, "");
		return Boolean.valueOf(temp);
	}

	public static String getOptionalString(String aCle)
	{
		if (props == null)
		{
			init();
		}
		String temp = (String) props.getProperty(aCle, "");
		return temp;
	}

}
