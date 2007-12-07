/**
 * 
 */
package logic.gateways;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ubc.cs.JLog.Foundation.jPrologAPI;

/**
 * @author Benoit Kessler
 */
public class LogicToPrologGatewayImpl implements LogicToPrologGateway
{
	// List of facts stored
	private List<String> itsFacts;
	// List of rules stored
	private List<String> itsRules;
	// input stream used for the prolog system
	private InputStream itsInputStream;
	// the prolog session
	private jPrologAPI itsPrologAPI;
	// simple optimisation
	private boolean hasChanged;
	// default gateway
	private static LogicToPrologGatewayImpl itsGateway;
	// storing temp file
	private File itsStoringFile;

	/**
	 * creates its own instance of PrologInterfaceFactory
	 */
	@SuppressWarnings("unchecked")
	private LogicToPrologGatewayImpl()
	{
		super();
		hasChanged = true;
		itsFacts = new ArrayList<String>();
		itsRules = new ArrayList<String>();
	}

	public void addFact(String aFact)
	{
		if (!itsFacts.contains(aFact))
		{
			itsFacts.add(aFact);
			hasChanged = true;
		}
	}

	public void addRule(String aRule)
	{
		itsRules.add(aRule);
		hasChanged = true;
	}

	public Map<String, Vector<String>> queryOnce(String aQuery)
	{
		if (hasChanged)
		{
			// create the file with the rules and facts
			try
			{
				createTheFile();
				hasChanged = false;
				// consult the file
				if (itsPrologAPI == null)
				{
					itsPrologAPI = new jPrologAPI(itsInputStream);
					itsPrologAPI.setFailUnknownPredicate(true);
				}
				else
					itsPrologAPI.consultSource(itsStoringFile.getPath());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		// query
		return itsPrologAPI.queryOnce(aQuery);
	}

	@SuppressWarnings("serial")
	public List<Map<String, Vector<String>>> queryAll(String aQuery)
	{
		if (hasChanged)
		{
			// create the file with the rules and facts
			try
			{
				createTheFile();
				hasChanged = false;
				// consult the file
				if (itsPrologAPI == null)
				{
					itsPrologAPI = new jPrologAPI(itsInputStream);
					itsPrologAPI.setFailUnknownPredicate(true);
				}
				else
					itsPrologAPI.consultSource(itsStoringFile.getPath());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		// query
		List<Map<String, Vector<String>>> theResult = new ArrayList<Map<String, Vector<String>>>();
		Map<String, Vector<String>> theQueryResult;

		theQueryResult = itsPrologAPI.query(aQuery);

		while (theQueryResult != null)
		{
			theResult.add(theQueryResult);
			theQueryResult = itsPrologAPI.retry();
		}
		return theResult;
	}

	private void createTheFile() throws IOException
	{
		try
		{
			itsStoringFile = File.createTempFile("DestineeTemp", ".pl");
			itsInputStream = new FileInputStream(itsStoringFile);
		}
		catch (FileNotFoundException e)
		{
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String theTempContent = "";
		if (itsStoringFile.createNewFile())
		{
			FileWriter theFileWriter = new FileWriter(itsStoringFile, false);
			BufferedWriter theWriter = new BufferedWriter(theFileWriter);

			theTempContent = createContents();

			theWriter.write(theTempContent, 0, theTempContent.length());
			theWriter.close();
			theFileWriter.close();
		}
		else
		{
			if (itsStoringFile.canWrite())
			{
				FileWriter theFileWriter = new FileWriter(itsStoringFile, false);
				BufferedWriter theWriter = new BufferedWriter(theFileWriter);

				theTempContent = createContents();

				theWriter.write(theTempContent, 0, theTempContent.length());
				theWriter.close();
				theFileWriter.close();
			}
			else
			{
				throw new IOException("Impossible d'ouvrir le fichier. \n Vérifiez qu'il n'existe pas déjà ou qu'il n'est pas déjà utilisé.");
			}
		}
	}

	private String createContents()
	{
		String theResult = "";
		for (String rule : itsRules)
		{
			theResult += rule + "\n\n";
		}
		for (String fact : itsFacts)
		{
			theResult += fact + "\n\n";
		}
		return theResult;
	}

	public static LogicToPrologGatewayImpl getDefaultGateway()
	{
		if (itsGateway == null)
		{
			itsGateway = new LogicToPrologGatewayImpl();
		}
		return itsGateway;
	}

	public void dispose()
	{
		if (itsGateway != null)
		{
			if (itsStoringFile != null)
				itsStoringFile.delete();
			if (itsPrologAPI != null)
				itsPrologAPI.stop();
			itsPrologAPI = null;
			itsGateway = null;
		}
	}

	public void flushFacts()
	{
		itsFacts = new ArrayList<String>();
		hasChanged = true;
	}

	public void flushRules()
	{
		itsRules = new ArrayList<String>();
		hasChanged = true;
	}

	public void flush()
	{
		flushFacts();
		flushRules();
	}
}
