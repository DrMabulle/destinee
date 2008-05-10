/**
 * 
 */
package destinee.algorithmes.voisinages.utils;

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
	private static GestionnaireOrdresAttaquants instance = new GestionnaireOrdresAttaquants();
	private static int TAILLE_MAX = 100;

	private List<ChaineAttaquesV> chaines = new ArrayList<ChaineAttaquesV>(TAILLE_MAX);
	private List<List<Perso>> ordresATraiter = new ArrayList<List<Perso>>(400);
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
		return instance;
	}

	public synchronized void ajouterChaineTraitee(ChaineAttaquesV aChaine) throws TechnicalException
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
		notifyAll();
	}

	/**
	 * Méthode permettant d'ajouter une chaine d'attaques dans la liste des chaines d'attaque à traiter
	 * 
	 * @param aScenario un scénario à traiter
	 */
	public synchronized void ajouterOrdreATraiter(List<Perso> aOrdre)
	{
		notifyAll();
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
		notifyAll();
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
	 * @return true s'il reste des ordres d'attaques à traiter
	 */
	public synchronized boolean hasNextOrdreATraiter()
	{
		notifyAll();
		return !ordresATraiter.isEmpty();
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
	public void retirerChaineAttaques(ChaineAttaquesV aChaine)
	{
		chaines.remove(aChaine);
	}

	/**
	 * @param aListChainesAttaques
	 */
	public void retirerChainesAttaques(List<ChaineAttaquesV> aListChainesAttaques)
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
}
