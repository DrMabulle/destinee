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
	 * Cr�e une nouvelle instance de FileAssistant
	 */
	private FileAssistant()
	{
		super();
	}

	/**
	 * G�n�re une instance de InputStream permettant de charger la ressource pass�e en param�tre
	 * 
	 * @param aChemin un chemin vers un fichier ou une ressource
	 * @return InputStream permettant de charger la ressource pass�e en param�tre
	 */
	public static final InputStream getInputStream(final String aChemin)
	{
		InputStream is;

		File file = new File(aChemin);
		if (file.exists())
		{
			try
			{
				is = new FileInputStream(file);
			}
			catch (FileNotFoundException e)
			{
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream(aChemin);
			}
		}
		else
		{
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(aChemin);
		}

		return is;
	}
}
