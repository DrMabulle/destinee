/**
 * 
 */
package destinee.data;

import java.math.BigDecimal;

import destinee.probas.ResolutionAttaque;
import junit.framework.TestCase;

/**
 * @author Bubulle et No-one
 *
 */
public class ScenarioTest extends TestCase
{
	
	/**
	 * La cible des attaques
	 */
	Cible cible;
	/**
	 * Le scénario testé
	 */
	Scenario scenar;
	/**
	 * L'attaquant
	 */
	Perso attaquant;
	/**
	 * L'attaque effectuée
	 */
	Attaque attaque;
	
	
	

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		cible = new Cible(10, 5, 5);
		attaquant = new Perso(15, 15, 10, 5, 5, 0, 0, "Attaquant");
		attaque = new AttaqueNormale(attaquant);
		ScenarioElement scenarElmt = new ScenarioElement(attaque, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		scenar = new Scenario(cible);
		scenar.ajouterElement(scenarElmt);
	}

	/**
	 * Test method for {@link destinee.data.Scenario#getEsperanceDegats()}.
	 */
	public void testGetEsperanceDegats()
	{
		double esperanceDeg = ResolutionAttaque.esperanceDeDegats(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		double esperanceDegScenar = scenar.getEsperanceDegats();
		
		assertEquals(esperanceDeg, esperanceDegScenar, 0);
	}

	/**
	 * Test method for {@link destinee.data.Scenario#getProbaRealisation()}.
	 */
	public void testGetProbaRealisation()
	{
		BigDecimal proba = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		BigDecimal probaScenar = scenar.getProbaRealisation();
		
		assertTrue(proba.compareTo(probaScenar) == 0);
	}

}
