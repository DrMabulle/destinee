/**
 * 
 */
package logic.gateways;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Bubulle et No-one
 *
 */
public class DestineeToPrologTest extends TestCase
{
	private DestineeToLogicGateway itsGate;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		itsGate = DestineeToLogicGatewayImpl.getDefaultInstance();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link logic.gateways.DestineeToLogicGatewayImpl#query(java.lang.String)}.
	 */
	@SuppressWarnings("unchecked")
	public void testQuery()
	{
		itsGate.ajouterPerso("Koumi", 12, 5, 12, -6, 10, 0);
		itsGate.ajouterAttaquePerso("Koumi", "normale");
		itsGate.ajouterAttaquePerso("Koumi", "brutale");
		List<Map<String, String>> retour = itsGate.query("peutAttaquer(Perso, []).");
		assertNotNull(retour);
		assertTrue(!retour.isEmpty());
		Map binding = retour.get(0);
		String perso = (String) binding.get("Perso");
		assertEquals("'Koumi'", perso);
	}

	/**
	 * Test method for {@link logic.gateways.DestineeToLogicGatewayImpl#ajouterPerso(java.lang.String, int, int, int, int, int, int)}.
	 */
	public void testAjouterPerso()
	{
		itsGate.ajouterPerso("Koumi", 12, 5, 12, -6, 10, 0);
	}

	/**
	 * Test method for {@link logic.gateways.DestineeToLogicGatewayImpl#ajouterAttaquePerso(java.lang.String, java.lang.String)}.
	 */
	public void testAjouterAttaquePerso()
	{
		itsGate.ajouterAttaquePerso("Koumi", "normale");
		itsGate.ajouterAttaquePerso("Koumi", "brutale");
	}

}
