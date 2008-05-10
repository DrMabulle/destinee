/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueNormale;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;

/**
 * @author Bubulle
 * 
 */
public class TraitementOrdreAttaquesTest extends TestCase
{
	private ChaineAttaquesV chaine;
	private List<Perso> ordre;
	private TraitementOrdreAttaques thread;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();

		Cible cible = new Cible(10, 0, 0);

		TraitementOrdreAttaques.arreterTraitements();
		thread = new TraitementOrdreAttaques(cible);

		Map<String, Double> maitrise = new Hashtable<String, Double>();
		maitrise.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, 0.8);
		maitrise.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, 0.8);
		maitrise.put(ConstantesAttaques.ID_ATTAQUE_IMPARABLE, 0.8);
		maitrise.put(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE, 0.8);
		maitrise.put(ConstantesAttaques.ID_ATTAQUE_MAGIQUE, 0.8);
		maitrise.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, 0.8);
		maitrise.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, 0.8);

		Perso attaquant1 = new Perso(10, 0, 10, 0, 10, 0, 0, "Attaquant1", maitrise);
		Perso attaquant2 = new Perso(10, 0, 10, 0, 10, 0, 0, "Attaquant2", maitrise);
		Perso attaquant3 = new Perso(10, 0, 10, 0, 10, 0, 0, "Attaquant3", maitrise);

		ordre = new ArrayList<Perso>();
		ordre.add(attaquant1);
		ordre.add(attaquant2);
		ordre.add(attaquant1);
		ordre.add(attaquant2);
		ordre.add(attaquant3);
		ordre.add(attaquant3);

		List<Attaque> listeAttaques = new ArrayList<Attaque>();
		listeAttaques.add(new AttaqueNormale(attaquant1));
		listeAttaques.add(new AttaqueNormale(attaquant2));
		listeAttaques.add(new AttaqueNormale(attaquant1));
		listeAttaques.add(new AttaqueNormale(attaquant2));
		listeAttaques.add(new AttaqueNormale(attaquant3));
		listeAttaques.add(new AttaqueNormale(attaquant3));
		chaine = new ChaineAttaquesV(cible, listeAttaques);

	}

	/**
	 * Test method for {@link destinee.algorithmes.voisinages.cm.threads.TraitementOrdreAttaques#genererChaineInitiale(java.util.List)}.
	 */
	public void testGenererChaineInitiale()
	{
		ChaineAttaquesV theChain = thread.genererChaineInitiale(ordre);
		assertNotNull(theChain);
		assertEquals(chaine, theChain);
	}

	/**
	 * Test method for
	 * {@link destinee.algorithmes.voisinages.cm.threads.TraitementOrdreAttaques#genererVoisinage(destinee.algorithmes.voisinages.data.ChaineAttaquesV)}.
	 */
	public void testGenererVoisinage()
	{
		List<ChaineAttaquesV> voisinage = thread.genererVoisinage(chaine);
		assertNotNull(voisinage);
	}

}
