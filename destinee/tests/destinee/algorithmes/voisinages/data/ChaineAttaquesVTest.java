/**
 * 
 */
package destinee.algorithmes.voisinages.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueBrutale;
import destinee.commun.data.AttaqueNormale;
import destinee.commun.data.AttaquePrecise;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.core.exception.TechnicalException;
import destinee.core.utils.ConversionUtil;

/**
 * @author bkessler
 * 
 */
public class ChaineAttaquesVTest extends TestCase
{

	private ChaineAttaquesV chaine;
	private Cible cible;
	private Perso attaquant;
	private Attaque attaque1;
	private Attaque attaque2;
	private Attaque attaque3;
	private Attaque attaque4;

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
		attaquant = new Perso(15, 0, 10, 0, 5, 0, 0, "Attaquant", maitrises);
		attaque1 = new AttaquePrecise(attaquant);
		attaque2 = new AttaqueNormale(attaquant);
		attaque3 = new AttaqueNormale(attaquant);
		attaque4 = new AttaqueBrutale(attaquant);
		List<Attaque> listeAtt = new ArrayList<Attaque>();
		listeAtt.add(attaque1);
		listeAtt.add(attaque2);
		listeAtt.add(attaque3);
		listeAtt.add(attaque4);

		chaine = new ChaineAttaquesV(cible, listeAtt);
	}

	/**
	 * Test method for {@link destinee.algorithmes.voisinages.data.ChaineAttaquesV#getScenarioInital()}.
	 * 
	 * @throws TechnicalException e
	 */
	public void testGetScenarioInital() throws TechnicalException
	{
		ScenarioV scenar = chaine.getScenarioInital();

		List<Integer> result = scenar.getListeTypesResolution();
		assertTrue(result != null && !result.isEmpty());
		assertTrue(result.size() == 2);
		assertTrue(ConversionUtil.bigdecimalVersDouble(scenar.getProbaRealisation(), 4) > 0);
	}

	public void testEvaluer() throws TechnicalException
	{
		BigDecimal result = chaine.getProbaRealisationCumulee();

		assertTrue(result != null);
		assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
	}

	public void testIndiceBourrinisme1()
	{
		ScenarioV scenar = chaine.getScenarioInital();
		chaine.scenariosPrincipaux.add(scenar);
		double indBourrScenar = scenar.getIndiceBourrinisme();
		double indBourrChaine = chaine.getIndiceBourrinisme();
		assertEquals(indBourrChaine, indBourrScenar);
	}

	public void testIndiceBourrinisme2() throws TechnicalException
	{
		List<Attaque> listeAtt = new ArrayList<Attaque>();
		listeAtt.add(attaque2);
		listeAtt.add(attaque3);
		chaine = new ChaineAttaquesV(cible, listeAtt);

		chaine.evaluer(BigDecimal.ONE, BigDecimal.ZERO, 25);
		double indBourrChaine = chaine.getIndiceBourrinisme();
		assertEquals(1.0, indBourrChaine);
	}
}
