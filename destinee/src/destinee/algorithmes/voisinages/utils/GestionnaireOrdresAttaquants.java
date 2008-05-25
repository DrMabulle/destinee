/**
 * 
 */
package destinee.algorithmes.voisinages.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.commun.data.Perso;
import destinee.core.exception.TechnicalException;

/**
 * @author Bubulle et No-one
 * 
 */
public class GestionnaireOrdresAttaquants
{
	private static final GestionnaireOrdresAttaquants INSTANCE = new GestionnaireOrdresAttaquants();
	private static final int TAILLE_MAX = 40;

	private final List<ChaineAttaquesV> chaines = new ArrayList<ChaineAttaquesV>(TAILLE_MAX);
	private final List<List<Perso>> ordresATraiter = new ArrayList<List<Perso>>(400);
	private boolean traitementEnCours = true;

	/**
	 * Constructeur par d�faut
	 */
	private GestionnaireOrdresAttaquants()
	{
		super();
	}

	/**
	 * @return default instance
	 */
	public static GestionnaireOrdresAttaquants getInstance()
	{
		return INSTANCE;
	}

	public synchronized void ajouterChaineTraitee(final ChaineAttaquesV aChaine) throws TechnicalException
	{
		if (chaines.size() < TAILLE_MAX)
		{
			chaines.add(aChaine);
			Collections.sort(chaines, new ChaineAttVComparatorConj());
		}
		else
		{
			// Si le r�sultat conjectur� est sup�rieur au r�susltat conjectur� de la chaine la plus faible stock�e
			// alors on enl�ve la derni�re et on stocke celle-ci
			// Sinon, on ne fait rien
			ChaineAttaquesV chaineLast = chaines.get(TAILLE_MAX - 1);
			if (aChaine.getEsperanceDegatConjecturee() > chaineLast.getEsperanceDegatConjecturee())
			{
				chaines.remove(TAILLE_MAX - 1);
				chaines.add(aChaine);
				Collections.sort(chaines, new ChaineAttVComparatorConj());
			}
		}
		// notifyAll();
	}

	/**
	 * M�thode permettant d'ajouter une chaine d'attaques dans la liste des chaines d'attaque � traiter
	 * 
	 * @param aScenario un sc�nario � traiter
	 */
	public synchronized void ajouterOrdreATraiter(final List<Perso> aOrdre)
	{
		// notifyAll();
		try
		{
			// On limite la taille du buffer � 400 sc�narios � traiter
			while (ordresATraiter.size() >= 400)
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		ordresATraiter.add(aOrdre);
		notifyAll();
	}

	/**
	 * M�thode permettant de r�cup�rer le prochain ordre d'attaques � traiter
	 * 
	 * @return la prochaine chaine d'attaques � traiter
	 */
	public synchronized List<Perso> getNextOrdreATraiter()
	{
		// notifyAll();
		List<Perso> ordre = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario � traiter, attendre
			while (ordresATraiter.isEmpty() && traitementEnCours)
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		if (!ordresATraiter.isEmpty())
		{
			ordre = ordresATraiter.remove(0);
		}
		notifyAll();

		return ordre;
	}

	/**
	 * M�thode permettant de r�cuperer la liste de toutes les chaines d'attaques, ordonn�e suivant l'esp�rance de d�g�ts d�croissante
	 * 
	 * @return le liste des chaines d'attaques ordonn�e
	 */
	public List<ChaineAttaquesV> getListeChainesOrdonnee()
	{
		// Cr�er une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesV> listeChaines = new ArrayList<ChaineAttaquesV>(chaines);

		// Trier la liste obtenue par ordre d'esp�rances de d�g�ts d�croissant
		Collections.sort(listeChaines, new ChaineAttVComparator());

		return listeChaines;
	}

	/**
	 * M�thode permettant de r�cuperer la liste de toutes les chaines d'attaques, ordonn�e suivant l'esp�rance de d�g�ts conjectur�e d�croissante
	 * 
	 * @return le liste des chaines d'attaques ordonn�e
	 */
	public List<ChaineAttaquesV> getListeChainesOrdonneeConj()
	{
		// Cr�er une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesV> listeChaines = new ArrayList<ChaineAttaquesV>(chaines);

		// Trier la liste obtenue par ordre d'esp�rances de d�g�ts d�croissant
		Collections.sort(listeChaines, new ChaineAttVComparatorConj());

		return listeChaines;
	}

	/**
	 * M�thode permettant de r�cuperer la liste de toutes les chaines d'attaques, non ordonn�e
	 * 
	 * @return le liste des chaines d'attaques non ordonn�e
	 */
	public List<ChaineAttaquesV> getListeChainesAttaques()
	{
		return new ArrayList<ChaineAttaquesV>(chaines);
	}

	/**
	 * Retire une chaine d'attaque de la liste des chaines d'attaques g�r�es
	 * 
	 * @param aChaine une chaine � retirer
	 */
	public void retirerChaineAttaques(final ChaineAttaquesV aChaine)
	{
		chaines.remove(aChaine);
	}

	/**
	 * @param aListChainesAttaques
	 */
	public void retirerChainesAttaques(final List<ChaineAttaquesV> aListChainesAttaques)
	{
		chaines.removeAll(aListChainesAttaques);
	}

	/**
	 * 
	 */
	public void declarerFinTraitement()
	{
		traitementEnCours = false;
	}

	/**
	 * 
	 */
	public void declarerDebutTraitement()
	{
		traitementEnCours = true;
	}

	/**
	 * Finalise l'�valuation des chaines d'attaques en poussant l'�valuation jusqu'� 99,5% pour les chaines d'attaque retenues.
	 * 
	 * @throws TechnicalException TechnicalException
	 */
	public void finaliserEvaluation() throws TechnicalException
	{
		for (ChaineAttaquesV chaine : chaines)
		{
			chaine.evaluer(new BigDecimal("0.999"), BigDecimal.ZERO, 2000);
		}
	}
}
