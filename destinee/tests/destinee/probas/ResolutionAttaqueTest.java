/**
 * 
 */
package destinee.probas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueNormale;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.utils.ConversionUtil;

import junit.framework.TestCase;

/**
 * @author Bubulle et No-one
 *
 */
public class ResolutionAttaqueTest extends TestCase
{
	
	private Attaque attaque;
	private Perso perso;
	private Cible cible;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();

		Map<String, Double> maitrises = new HashMap<String, Double>();
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);
		
		perso = new Perso(15, 11, 10, 11, 5, 0, 0, "Attaquant", maitrises);
		attaque = new AttaqueNormale(perso);
		cible = new Cible(10, 11, 0);
	}

	/**
	 * Test method for {@link destinee.commun.probas.ResolutionAttaque#resoudreAttaque(Attaque, Cible, int)}.
	 */
	public void testResoudreAttaqueMalusDef()
	{
		// Tests mettant en oeuvre les malus de défense de la cible
		cible.incrementerMalusDefense(attaque, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		
		// Coup critique
		BigDecimal proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		BigDecimal proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, 1.0, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Coup simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, 1.0, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, 1.0, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive parfaite
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, 1.0, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
	}


	/**
	 * Test method for {@link destinee.commun.probas.ResolutionAttaque#resoudreAttaque(int, int, int, int, int, int, int, int)}.
	 */
	public void testResoudreAttaqueFatigueAttaquant()
	{
		// Tests mettant en oeuvre les malus de fatigue de l'attaquant
		Map<String, Double> maitrises = new HashMap<String, Double>();
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrises.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);
		perso = new Perso(15, 11, 10, 11, 5, 0, 5, "Attaquant", maitrises); // fatigue initiale à 5
		attaque = new AttaqueNormale(perso);
		
		// Coup critique
		BigDecimal proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		BigDecimal proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, 1.0, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Coup simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, 1.0, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, 1.0, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive parfaite
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, 1.0, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
	}
}
