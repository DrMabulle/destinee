/**
 * 
 */
package destinee.probas;

import java.math.BigDecimal;

import destinee.core.utils.ConversionUtil;
import destinee.data.Attaque;
import destinee.data.AttaqueNormale;
import destinee.data.Cible;
import destinee.data.Perso;

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
		
		perso = new Perso(15, 11, 10, 11, 5, 0, 0, "Attaquant");
		attaque = new AttaqueNormale(perso);
		cible = new Cible(10, 11, 0);
	}

	/**
	 * Test method for {@link destinee.probas.ResolutionAttaque#resoudreAttaque(Attaque, Cible, int)}.
	 */
	public void testResoudreAttaqueMalusDef()
	{
		// Tests mettant en oeuvre les malus de d�fense de la cible
		cible.incrementerMalusDefence(attaque, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		
		// Coup critique
		BigDecimal proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		BigDecimal proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Coup simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive parfaite
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 11, 9, 11, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
	}


	/**
	 * Test method for {@link destinee.probas.ResolutionAttaque#resoudreAttaque(int, int, int, int, int, int, int, int)}.
	 */
	public void testResoudreAttaqueFatigueAttaquant()
	{
		// Tests mettant en oeuvre les malus de fatigue de l'attaquant
		perso = new Perso(15, 11, 10, 11, 5, 0, 5, "Attaquant"); // fatigue initiale � 5
		attaque = new AttaqueNormale(perso);
		
		// Coup critique
		BigDecimal proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		BigDecimal proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Coup simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive simple
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
		
		// Esquive parfaite
		proba1 = ResolutionAttaque.resoudreAttaque(attaque, cible, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		proba2 = ResolutionAttaque.resoudreAttaque(15, 6, 10, 11, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		
		System.out.println(ConversionUtil.bigDecimalVersString(proba1, 12));
		System.out.println(ConversionUtil.bigDecimalVersString(proba2, 12));
		
		assertTrue(proba1.compareTo(proba2) == 0);
	}
}