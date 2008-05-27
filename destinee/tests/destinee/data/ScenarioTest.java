/**
 * 
 */
package destinee.data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import destinee.algorithmes.normal.data.Scenario;
import destinee.algorithmes.normal.data.ScenarioElement;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueNormale;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.exception.TechnicalException;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		cible = new Cible(10, 5, 5, 0, 5);
		Map<String, Double> maitrises = new HashMap<String, Double>();
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);
		attaquant = new Perso(15, 15, 10, 5, 5, 0, 0, "Attaquant", maitrises);
		attaque = new AttaqueNormale(attaquant);
		ScenarioElement scenarElmt = new ScenarioElement(attaque, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		scenar = new Scenario(cible);
		scenar.ajouterElement(scenarElmt);
	}

	/**
	 * Test method for {@link destinee.algorithmes.normal.data.Scenario#getEsperanceDegats()}.
	 * 
	 * @throws TechnicalException e
	 */
	public void testGetEsperanceDegats() throws TechnicalException
	{
		double esperanceDeg = ResolutionAttaque.esperanceDeDegats(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		double esperanceDegScenar = scenar.getEsperanceDegats();

		assertEquals(esperanceDeg, esperanceDegScenar, 0);
	}

	/**
	 * Test method for {@link destinee.algorithmes.normal.data.Scenario#getProbaRealisation()}.
	 * 
	 * @throws TechnicalException e
	 */
	public void testGetProbaRealisation() throws TechnicalException
	{
		BigDecimal proba = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		BigDecimal probaScenar = scenar.getProbaRealisation();

		assertTrue(proba.compareTo(probaScenar) == 0);
	}

	public void testGetIdentifiant()
	{
		String id1 = scenar.getIdentifiantChaineAttaques();

		assertEquals("AttaquantNormale-", id1);
	}

}
