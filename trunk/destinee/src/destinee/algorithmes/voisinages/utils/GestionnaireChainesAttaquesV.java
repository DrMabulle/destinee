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

	public synchronized void ajouterChaineTraitee(ChaineAttaquesV aChaine)
	{
		chaines.add(aChaine);
		notifyAll();
	}

	/**
	 * M�thode permettant d'ajouter une chaine d'attaques dans la liste des chaines d'attaque � traiter
	 * 
	 * @param aScenario un sc�nario � traiter
	 */
	public synchronized void ajouterChaineATraiter(ChaineAttaquesV aChaine)
	{
		notifyAll();
		try
		{
			// On limite la taille du buffer � 200 sc�narios � traiter
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
		// System.out.println("Ajout d'une chaine d'attaques � traiter. " + chainesATraiter.size() + " chaines � traiter.");
		notifyAll();
	}

	/**
	 * M�thode permettant de r�cup�rer la prochaine chaine d'attaques � traiter
	 * 
	 * @return la prochaine chaine d'attaques � traiter
	 */
	public synchronized ChaineAttaquesV getNextChaineATraiter()
	{
		notifyAll();
		ChaineAttaquesV chaine = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario � traiter, attendre
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
			// System.out.println("Retrait d'une chaine d'attaques � traiter. " + chainesATraiter.size() + " chaines � traiter.");
		}
		notifyAll();

		return chaine;
	}

	/**
	 * @return true s'il reste des chaines d'attaques � traiter
	 */
	public synchronized boolean hasNextChaineATraiter()
	{
		notifyAll();
		return !chainesATraiter.isEmpty();
	}

	/**
	 * M�thode permettant de r�cuperer la liste de toutes les chaines d'attaques, ordonn�e suivant l'esp�rance de d�g�ts d�croissante
	 * 
	 * @return le liste des chaines d'attaque ordonn�e
	 */
	public List<ChaineAttaquesV> getListeChainesOrdonnee()
	{
		// Cr�er une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesV> listeChaines = new ArrayList<ChaineAttaquesV>(chaines);

		// Trier la liste obtenue par ordre d'esp�rances de d�g�ts d�croissant
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

			// On veut l'ordre d�croissant => faire les tests � l'envers
			return espeDeg2.compareTo(espeDeg1);
		}
	}
}
