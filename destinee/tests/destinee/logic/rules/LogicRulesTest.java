/**
 * 
 */
package destinee.logic.rules;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import logic.gateways.DestineeToLogicGateway;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;

/**
 * @author Bubulle
 * 
 */
public class LogicRulesTest extends TestCase
{
	private DestineeToLogicGateway itsGate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		itsGate = DestineeToLogicGatewayImpl.getDefaultInstance();

		itsGate.ajouterPerso("Koumi", 12, 5, 12, -6, 10, 0);
		itsGate.ajouterAttaquePerso("Koumi", ConstantesAttaques.ID_ATTAQUE_NORMALE);

		itsGate.ajouterPerso("Dwarfeater", 12, 5, 12, -6, 10, 0);
		itsGate.ajouterAttaquePerso("Dwarfeater", ConstantesAttaques.ID_ATTAQUE_NORMALE);

		itsGate.ajouterPerso("LaPorte", 12, 5, 12, -6, 10, 0);
		itsGate.ajouterAttaquePerso("LaPorte", ConstantesAttaques.ID_ATTAQUE_NORMALE);
	}

	public void testRulesOrdre()
	{
		List<Map<String, List<String>>> result = itsGate.query("generationListeAttaquants(L).");
		assertNotNull(result);
		assertTrue(!result.isEmpty());
		assertTrue(result.size() == 90);
	}
}
