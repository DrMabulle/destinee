/**
 * 
 */
package destinee.data;

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

/**
 * @author Bubulle
 * 
 */
public class ChaineAttaquesTest extends TestCase
{
	/**
	 * La cible des attaques
	 */
	private Cible cible;
	/**
	 * Le scénario testé
	 */
	private Scenario scenar;
	/**
	 * L'attaquant
	 */
	private Perso attaquant;
	/**
	 * L'attaque effectuée en premier
	 */
	private Attaque attaque1;
	/**
	 * L'attaque effectuée en second
	 */
	private Attaque attaque2;

	// /**
	// * La chaine d'attaque testée
	// */
	// private ChaineAttaques chaineAtt;

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
		attaque1 = new AttaqueNormale(attaquant);
		ScenarioElement scenarElmt1 = new ScenarioElement(attaque1, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		attaque2 = new AttaqueNormale(attaquant);
		ScenarioElement scenarElmt2 = new ScenarioElement(attaque2, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		scenar = new Scenario(cible);
		scenar.ajouterElement(scenarElmt1);
		scenar.ajouterElement(scenarElmt2);
	}

	/**
	 * Test method for {@link destinee.algorithmes.normal.data.ChaineAttaques#ajouterScenario(destinee.algorithmes.normal.data.Scenario)}.
	 */
	public void testAjouterScenario()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link destinee.algorithmes.normal.data.ChaineAttaques#getEsperanceDegatCumulee()}.
	 */
	public void testGetEsperanceDegatCumulee()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link destinee.algorithmes.normal.data.ChaineAttaques#getProbaRealisationCumulee()}.
	 */
	public void testGetProbaRealisationCumulee()
	{
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link destinee.algorithmes.normal.data.ChaineAttaques#getIdentifiant()}.
	 */
	public void testGetIdentifiant()
	{
		fail("Not yet implemented");
	}

}
