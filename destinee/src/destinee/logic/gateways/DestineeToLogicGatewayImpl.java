package destinee.logic.gateways;

import java.util.List;
import java.util.Map;

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

	public List<Map<String, List<String>>> query(final String aQuery)
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

	public Map<String, List<String>> queryOnce(final String aQuery)
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

	public Map<String, List<String>> next()
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

	public void ajouterPerso(final String aNomPerso, final int aNbDesAtt, final int aBonusAtt, final int aNbDesDeg, final int aBonusDeg,
			final int aNbPaCyc1, final int aNbPaCyc2)
	{
		StringBuffer fact = new StringBuffer(400);
		fact.append("perso('").append(aNomPerso).append("', ");
		fact.append(aNbDesAtt).append(", ").append(aBonusAtt).append(", ");
		fact.append(aNbDesDeg).append(", ").append(aBonusDeg).append(", ");
		fact.append(aNbPaCyc1).append(", ").append(aNbPaCyc2).append(").");
		itsGate.addFact(fact.toString());
	}

	public void ajouterAttaquePerso(final String aNomPerso, final String aNomAttaque)
	{
		StringBuffer fact = new StringBuffer(200);
		fact.append("attaque('").append(aNomPerso).append("', '").append(aNomAttaque).append("').");
		itsGate.addFact(fact.toString());
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
