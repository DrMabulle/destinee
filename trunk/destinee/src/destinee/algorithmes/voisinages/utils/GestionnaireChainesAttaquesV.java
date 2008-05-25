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
	 * Constructeur par d�faut
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
	 * M�thode permettant d'ajouter une chaine d'attaques dans la liste des chaines d'attaque � traiter
	 * 
	 * @param aScenario un sc�nario � traiter
	 */
	public synchronized void ajouterChaineATraiter(final ChaineAttaquesV aChaine)
	{
		// notifyAll();
		try
		{
			// On limite la taille du buffer � 400 sc�narios � traiter
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
	 * M�thode permettant de r�cup�rer la prochaine chaine d'attaques � traiter
	 * 
	 * @return la prochaine chaine d'attaques � traiter
	 */
	public synchronized ChaineAttaquesV getNextChaineATraiter()
	{
		// notifyAll();
		ChaineAttaquesV chaine = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario � traiter, attendre
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

}
