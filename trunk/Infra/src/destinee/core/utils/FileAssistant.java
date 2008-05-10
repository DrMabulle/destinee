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
public class FileAssistant
{
	public static InputStream getInputStream(String aChemin)
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
