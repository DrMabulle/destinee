package logic.gateways;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author Benoit Kessler
 */
public interface LogicToPrologGateway
{
	/**
	 * Adds a fact to the set of facts already stored.
	 * 
	 * @param aFact The fact to be added.
	 */
	public void addFact(String aFact);

	/**
	 * Adds a rule to the set of rules already stored.
	 * 
	 * @param aRule The rule to be added.
	 */
	public void addRule(String aRule);

	/**
	 * begins a query on the Prolog system. If there was an active query running, further results lost. The resultant Hashtable contains keys equal to the
	 * unbound variables in the query, and values equal to their binding. If null is returned, no bindings could satisfy the query ("no"). If there were no
	 * unbound variables, an empty Hashtable is returned to signify "yes".
	 * 
	 * @param aQuery The query to be done
	 * @return a hashtable, containing the bindings generated.
	 */
	public Map<String, String> queryOnce(String aQuery);

	/**
	 * begins a query on the Prolog system. If there was an active query running, further results lost. The resultant Hashtable contains keys equal to the
	 * unbound variables in the query, and values equal to their binding. If null is returned, no bindings could satisfy the query ("no"). If there were no
	 * unbound variables, an empty Hashtable is returned to signify "yes".
	 * 
	 * @param aQuery The query to be done
	 * @return a hashtable, containing the bindings generated.
	 */
	public List<Map<String, Vector<String>>> queryAll(String aQuery);

	/**
	 * Erase all the facts from the facts base
	 */
	public void flushFacts();

	/**
	 * Erase all the rules from the rules base
	 */
	public void flushRules();

	/**
	 * Erase all the facts and rules from the base
	 */
	public void flush();

	/**
	 * Dispose the gateway.
	 */
	public void dispose();

}
