/**
 * 
 */
package destinee.logic.gateways;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Hashtable;

import junit.framework.TestCase;
import ubc.cs.JLog.Foundation.iPrologFileServices;
import ubc.cs.JLog.Foundation.jPrologAPI;
import ubc.cs.JLog.Foundation.jPrologFileServices;

/**
 * @author Bubulle
 * 
 */
public class LogicToPrologGatewayTest extends TestCase
{
	private jPrologAPI api;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();

		iPrologFileServices fs = new jPrologFileServices();
		PrintWriter out = new PrintWriter(System.out);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		InputStream is = new FileInputStream(new File("./tests/destinee/logic/gateways/Destinee.pl"));
		api = new jPrologAPI(is, fs, out, in, null);
	}

	/**
	 * Test method for {@link logic.gateways.LogicToPrologGatewayImpl#queryOnce(java.lang.String)}.
	 */
	public void testQueryOnce()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link logic.gateways.LogicToPrologGatewayImpl#next()}.
	 */
	public void testNext()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link logic.gateways.LogicToPrologGatewayImpl#queryAll(java.lang.String)}.
	 */
	public void testQueryAll()
	{
		fail("Not yet implemented");
	}

	@SuppressWarnings("unchecked")
	public void testAPI()
	{
		Hashtable bindings = new Hashtable();
		Hashtable result;

		result = api.query("generationScenarios(Result).", bindings);

		while (result != null)
		{
			System.out.println(result.get("Result").toString());

			result = api.retry();
		}

		System.out.println("That's Every Solution!");
	}

}
