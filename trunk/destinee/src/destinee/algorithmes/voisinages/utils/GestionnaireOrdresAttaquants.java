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
	 * Constructeur par défaut
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
			// Si le résultat conjecturé est supérieur au résusltat conjecturé de la chaine la plus faible stockée
			// alors on enlève la dernière et on stocke celle-ci
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
	 * Méthode permettant d'ajouter une chaine d'attaques dans la liste des chaines d'attaque à traiter
	 * 
	 * @param aScenario un scénario à traiter
	 */
	public synchronized void ajouterOrdreATraiter(final List<Perso> aOrdre)
	{
		// notifyAll();
		try
		{
			// On limite la taille du buffer à 400 scénarios à traiter
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
	 * Méthode permettant de récupérer le prochain ordre d'attaques à traiter
	 * 
	 * @return la prochaine chaine d'attaques à traiter
	 */
	public synchronized List<Perso> getNextOrdreATraiter()
	{
		// notifyAll();
		List<Perso> ordre = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario à traiter, attendre
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
	 * Méthode permettant de récuperer la liste de toutes les chaines d'attaques, ordonnée suivant l'espérance de dégâts décroissante
	 * 
	 * @return le liste des chaines d'attaques ordonnée
	 */
	public List<ChaineAttaquesV> getListeChainesOrdonnee()
	{
		// Créer une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesV> listeChaines = new ArrayList<ChaineAttaquesV>(chaines);

		// Trier la liste obtenue par ordre d'espérances de dégâts décroissant
		Collections.sort(listeChaines, new ChaineAttVComparator());

		return listeChaines;
	}

	/**
	 * Méthode permettant de récuperer la liste de toutes les chaines d'attaques, ordonnée suivant l'espérance de dégâts conjecturée décroissante
	 * 
	 * @return le liste des chaines d'attaques ordonnée
	 */
	public List<ChaineAttaquesV> getListeChainesOrdonneeConj()
	{
		// Créer une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesV> listeChaines = new ArrayList<ChaineAttaquesV>(chaines);

		// Trier la liste obtenue par ordre d'espérances de dégâts décroissant
		Collections.sort(listeChaines, new ChaineAttVComparatorConj());

		return listeChaines;
	}

	/**
	 * Méthode permettant de récuperer la liste de toutes les chaines d'attaques, non ordonnée
	 * 
	 * @return le liste des chaines d'attaques non ordonnée
	 */
	public List<ChaineAttaquesV> getListeChainesAttaques()
	{
		return new ArrayList<ChaineAttaquesV>(chaines);
	}

	/**
	 * Retire une chaine d'attaque de la liste des chaines d'attaques gérées
	 * 
	 * @param aChaine une chaine à retirer
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
	 * Finalise l'évaluation des chaines d'attaques en poussant l'évaluation jusqu'à 99,5% pour les chaines d'attaque retenues.
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
