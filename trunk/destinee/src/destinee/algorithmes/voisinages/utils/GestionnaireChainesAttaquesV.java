/**
 * 
 */
package destinee.algorithmes.voisinages.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private Set<ChaineAttaquesV> chaines = new HashSet<ChaineAttaquesV>(15000);
	private static GestionnaireChainesAttaquesV instance = new GestionnaireChainesAttaquesV();

	private List<ChaineAttaquesV> chainesATraiter = new ArrayList<ChaineAttaquesV>(400);

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

	public synchronized void ajouterChaineTraitee(ChaineAttaquesV aChaine)
	{
		chaines.add(aChaine);
		notifyAll();
	}

	/**
	 * Méthode permettant d'ajouter une chaine d'attaques dans la liste des chaines d'attaque à traiter
	 * 
	 * @param aScenario un scénario à traiter
	 */
	public synchronized void ajouterChaineATraiter(ChaineAttaquesV aChaine)
	{
		notifyAll();
		try
		{
			// On limite la taille du buffer à 200 scénarios à traiter
			while (chainesATraiter.size() >= 200)
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		chainesATraiter.add(aChaine);
		// System.out.println("Ajout d'une chaine d'attaques à traiter. " + chainesATraiter.size() + " chaines à traiter.");
		notifyAll();
	}

	/**
	 * Méthode permettant de récupérer la prochaine chaine d'attaques à traiter
	 * 
	 * @return la prochaine chaine d'attaques à traiter
	 */
	public synchronized ChaineAttaquesV getNextChaineATraiter()
	{
		notifyAll();
		ChaineAttaquesV chaine = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario à traiter, attendre
			while (chainesATraiter.isEmpty())
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
			// System.out.println("Retrait d'une chaine d'attaques à traiter. " + chainesATraiter.size() + " chaines à traiter.");
		}
		notifyAll();

		return chaine;
	}

	/**
	 * @return true s'il reste des chaines d'attaques à traiter
	 */
	public synchronized boolean hasNextChaineATraiter()
	{
		notifyAll();
		return !chainesATraiter.isEmpty();
	}

	/**
	 * Méthode permettant de récuperer la liste de toutes les chaines d'attaques, ordonnée suivant l'espérance de dégâts décroissante
	 * 
	 * @return le liste des chaines d'attaque ordonnée
	 */
	public List<ChaineAttaquesV> getListeChainesOrdonnee()
	{
		// Créer une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesV> listeChaines = new ArrayList<ChaineAttaquesV>(chaines);

		// Trier la liste obtenue par ordre d'espérances de dégâts décroissant
		Collections.sort(listeChaines, new ChaineAttVComparator());

		return listeChaines;
	}

	class ChaineAttVComparator implements Comparator<ChaineAttaquesV>
	{
		@Override
		public int compare(ChaineAttaquesV aO1, ChaineAttaquesV aO2)
		{
			Double espeDeg1 = new Double(aO1.getEsperanceDegatCumulee());
			Double espeDeg2 = new Double(aO2.getEsperanceDegatCumulee());

			// On veut l'ordre décroissant => faire les tests à l'envers
			return espeDeg2.compareTo(espeDeg1);
		}
	}
}
