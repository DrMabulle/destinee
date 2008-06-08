/**
 * 
 */
package destinee.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Bubulle
 * 
 */
public final class FileAssistant
{
	/**
	 * Crée une nouvelle instance de FileAssistant
	 */
	private FileAssistant()
	{
		super();
	}

	/**
	 * Génère une instance de InputStream permettant de charger la ressource passée en paramètre
	 * 
	 * @param aPath un chemin (dossier)
	 * @param aFileName un fichier ou une ressource
	 * @param aClassLoader un ClassLoader
	 */
	public static final InputStream getInputStream(final String aPath, final String aFileName)
	{
		return getInputStream(aPath, aFileName, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Génère une instance de InputStream permettant de charger la ressource passée en paramètre
	 * 
	 * @param aPath un chemin (dossier)
	 * @param aFileName un fichier ou une ressource
	 * @param aClassLoader un ClassLoader
	 * 
	 * @return InputStream permettant de charger la ressource passée en paramètre
	 */
	public static final InputStream getInputStream(final String aPath, final String aFileName, final ClassLoader aClassLoader)
	{
		InputStream is;

		File file = new File(aPath + aFileName);
		if (file.exists())
		{
			try
			{
				is = new FileInputStream(file);
			}
			catch (FileNotFoundException e)
			{
				is = loadWithClassLoader(aPath, aFileName, aClassLoader);
			}
		}
		else
		{
			is = loadWithClassLoader(aPath, aFileName, aClassLoader);
		}

		return is;
	}

	private static final InputStream loadWithClassLoader(final String aPath, final String aFileName, final ClassLoader aClassLoader)
	{
		InputStream is = aClassLoader.getResourceAsStream(aFileName);

		if (is == null)
		{
			is = aClassLoader.getResourceAsStream(aPath + aFileName);
		}
		if (is == null)
		{
			is = aClassLoader.getResourceAsStream("properties/" + aFileName);
		}
		return is;
	}
}
