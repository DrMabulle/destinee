/**
 * 
 */
package destinee.algorithmes.voisinages.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;

/**
 * @author Bubulle et No-one
 * 
 */
public class GestionnaireChainesAttaquesV
{
	private final Set<ChaineAttaquesV> chaines = new HashSet<ChaineAttaquesV>(15000);
	private static final GestionnaireChainesAttaquesV instance = new GestionnaireChainesAttaquesV();

	private final List<ChaineAttaquesV> chainesATraiter = new ArrayList<ChaineAttaquesV>(400);
	private boolean traitementEnCours = true;

	/**
	 * Constructeur par défaut
	 */
	private GestionnaireChainesAttaquesV()
	{
		super();
	}

	/**
	 * @return default instance
	 */
	public static GestionnaireChainesAttaquesV getInstance()
	{
		return instance;
	}

	public synchronized void ajouterChaineTraitee(final ChaineAttaquesV aChaine)
	{
		chaines.add(aChaine);
		// notifyAll();
	}

	/**
	 * Méthode permettant d'ajouter une chaine d'attaques dans la liste des chaines d'attaque à traiter
	 * 
	 * @param aScenario un scénario à traiter
	 */
	public synchronized void ajouterChaineATraiter(final ChaineAttaquesV aChaine)
	{
		// notifyAll();
		try
		{
			// On limite la taille du buffer à 400 scénarios à traiter
			while (chainesATraiter.size() >= 400)
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		chainesATraiter.add(aChaine);
		notifyAll();
	}

	/**
	 * @param aListChainesAttaques
	 */
	public synchronized void ajouterChainesATraiter(final List<ChaineAttaquesV> aListChainesAttaques)
	{
		chainesATraiter.addAll(aListChainesAttaques);
	}

	/**
	 * Méthode permettant de récupérer la prochaine chaine d'attaques à traiter
	 * 
	 * @return la prochaine chaine d'attaques à traiter
	 */
	public synchronized ChaineAttaquesV getNextChaineATraiter()
	{
		// notifyAll();
		ChaineAttaquesV chaine = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario à traiter, attendre
			while (chainesATraiter.isEmpty() && traitementEnCours)
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		if (!chainesATraiter.isEmpty())
		{
			chaine = chainesATraiter.remove(0);
		}
		notifyAll();

		return chaine;
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

}
