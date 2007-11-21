/**
 * 
 */
package destinee.data;

import destinee.probas.ResolutionAttaque;
import junit.framework.TestCase;

/**
 * @author Bubulle
 *
 */
public class ChaineAttaquesTest extends TestCase
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
	 * L'attaque effectuée en premier
	 */
	Attaque attaque1;
	/**
	 * L'attaque effectuée en second
	 */
	Attaque attaque2;
	/**
	 * La chaine d'attaque testée
	 */
	ChaineAttaques chaineAtt;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		cible = new Cible(10, 5, 5);
		attaquant = new Perso(15, 15, 10, 5, 5, 0, 0, "Attaquant");
		attaque1 = new AttaqueNormale(attaquant);
		ScenarioElement scenarElmt1 = new ScenarioElement(attaque1, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		attaque2 = new AttaqueNormale(attaquant);
		ScenarioElement scenarElmt2 = new ScenarioElement(attaque2, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		scenar = new Scenario(cible);
		scenar.ajouterElement(scenarElmt1);
		scenar.ajouterElement(scenarElmt2);
	}

	/**
	 * Test method for {@link destinee.data.ChaineAttaques#ajouterScenario(destinee.data.Scenario)}.
	 */
	public void testAjouterScenario()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link destinee.data.ChaineAttaques#getEsperanceDegatCumulee()}.
	 */
	public void testGetEsperanceDegatCumulee()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link destinee.data.ChaineAttaques#getProbaRealisationCumulee()}.
	 */
	public void testGetProbaRealisationCumulee()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link destinee.data.ChaineAttaques#getIdentifiant()}.
	 */
	public void testGetIdentifiant()
	{
		fail("Not yet implemented");
	}

}
