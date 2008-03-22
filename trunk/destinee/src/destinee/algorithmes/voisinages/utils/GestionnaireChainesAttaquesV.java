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
	 * Méthode permettant de récuperer la liste de toutes les chaines d'attaques, ordonnée suivant un indicateur composé, basé sur l'espérance de dégâts et
	 * l'indice de bourrinisme
	 * 
	 * @return le liste des chaines d'attaques ordonnée
	 */
	public List<ChaineAttaquesV> getListeChainesOrdonneeComp()
	{
		// Créer une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesV> listeChaines = new ArrayList<ChaineAttaquesV>(chaines);

		// Trier la liste obtenue par ordre d'espérances de dégâts décroissant
		Collections.sort(listeChaines, new ChaineAttVComparatorComp());

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
	 * @author Bubulle
	 * 
	 * Comparateur
	 */
	class ChaineAttVComparator implements Comparator<ChaineAttaquesV>
	{
		@Override
		public int compare(ChaineAttaquesV aO1, ChaineAttaquesV aO2)
		{
			try
			{
				Double espeDeg1 = new Double(aO1.getEsperanceDegatCumulee());
				Double espeDeg2 = new Double(aO2.getEsperanceDegatCumulee());

				// On veut l'ordre décroissant => faire les tests à l'envers
				return espeDeg2.compareTo(espeDeg1);
			}
			catch (Exception e)
			{
				System.err.println("Erreur lors de la comparaison de deux ChaineAttaquesV. Méthode ChaineAttVComparator.compare()");
				return 0;
			}
		}
	}

	/**
	 * @author Bubulle
	 * 
	 * Comparateur de chaines d'attaques se basant sur l'espérance de dégâts conjecturée
	 */
	class ChaineAttVComparatorConj implements Comparator<ChaineAttaquesV>
	{
		@Override
		public int compare(ChaineAttaquesV aO1, ChaineAttaquesV aO2)
		{
			try
			{
				Double espeDeg1 = new Double(aO1.getEsperanceDegatConjecturee());
				Double espeDeg2 = new Double(aO2.getEsperanceDegatConjecturee());

				// On veut l'ordre décroissant => faire les tests à l'envers
				return espeDeg2.compareTo(espeDeg1);
			}
			catch (Exception e)
			{
				System.err.println("Erreur lors de la comparaison de deux ChaineAttaquesV. Méthode ChaineAttVComparatorConj.compare()");
				return 0;
			}
		}
	}

	/**
	 * @author Bubulle
	 * 
	 * Comparateur de chaines d'attaques se basant sur plusieurs critères dont l'espérance de dégât cumulée et l'indice de bourrinisme
	 */
	class ChaineAttVComparatorComp implements Comparator<ChaineAttaquesV>
	{
		@Override
		public int compare(ChaineAttaquesV aO1, ChaineAttaquesV aO2)
		{
			try
			{
				Double espeDeg1 = new Double(aO1.getEsperanceDegatConjecturee() * aO1.getIndiceBourrinisme());
				Double espeDeg2 = new Double(aO2.getEsperanceDegatConjecturee() * aO2.getIndiceBourrinisme());

				// On veut l'ordre décroissant => faire les tests à l'envers
				return espeDeg2.compareTo(espeDeg1);
			}
			catch (Exception e)
			{
				System.err.println("Erreur lors de la comparaison de deux ChaineAttaquesV. Méthode ChaineAttVComparatorComp.compare()");
				return 0;
			}
		}
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
}
