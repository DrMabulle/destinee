package destinee.logic.gateways;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import logic.gateways.DestineeToLogicGateway;
import logic.gateways.LogicToPrologGateway;
import logic.gateways.LogicToPrologGatewayImpl;
import destinee.logic.rules.LogicRulesDBImpl;

/**
 * @author Benoit Kessler
 * 
 */
public class DestineeToLogicGatewayImpl implements DestineeToLogicGateway
{
	private LogicToPrologGateway itsGate;

	private static DestineeToLogicGatewayImpl INSTANCE = new DestineeToLogicGatewayImpl();

	public static DestineeToLogicGatewayImpl getDefaultInstance()
	{
		return INSTANCE;
	}

	/**
	 * Creates and initializes a gateway for communication between Reflex and the Logic engine.
	 */
	private DestineeToLogicGatewayImpl()
	{
		super();
		itsGate = LogicToPrologGatewayImpl.getDefaultGateway();
		for (String rule : LogicRulesDBImpl.getDefaultInstance().getRulesList())
		{
			itsGate.addRule(rule);
		}
	}

	public List<Map<String, Vector<String>>> query(String aQuery)
	{
		try
		{
			return itsGate.queryAll(aQuery);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Vector<String>> queryOnce(String aQuery)
	{
		try
		{
			return itsGate.queryOnce(aQuery);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Vector<String>> next()
	{
		try
		{
			return itsGate.next();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void stop()
	{
		try
		{
			itsGate.stop();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
	}

	public void ajouterPerso(String aNomPerso, int aNbDesAtt, int aBonusAtt, int aNbDesDeg, int aBonusDeg, int aNbPaCyc1, int aNbPaCyc2)
	{
		itsGate.addFact("perso('" + aNomPerso + "', " + aNbDesAtt + ", " + aBonusAtt + ", " + aNbDesDeg + ", " + aBonusDeg + ", " + aNbPaCyc1 + ", "
				+ aNbPaCyc2 + ").");
	}

	public void ajouterAttaquePerso(String aNomPerso, String aNomAttaque)
	{
		itsGate.addFact("attaque('" + aNomPerso + "', '" + aNomAttaque + "').");
	}

	public void flush()
	{
		itsGate.flushFacts();
	}

	/**
	 * Dispose the gateway.
	 */
	public void reinitialize()
	{
		itsGate.dispose();
		itsGate = LogicToPrologGatewayImpl.getDefaultGateway();
		for (String rule : LogicRulesDBImpl.getDefaultInstance().getRulesList())
		{
			itsGate.addRule(rule);
		}
	}
}
