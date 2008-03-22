/**
 * 
 */
package destinee.algorithmes.normal.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import destinee.algorithmes.normal.data.ChaineAttaques;
import destinee.algorithmes.normal.data.Scenario;
import destinee.core.exception.TechnicalException;

/**
 * @author Bubulle et No-one
 * 
 */
public class GestionnaireChainesAttaques
{
	private Map<String, ChaineAttaques> chaines = new Hashtable<String, ChaineAttaques>(15000);
	private static GestionnaireChainesAttaques instance = new GestionnaireChainesAttaques();

	private List<Scenario> scenariosATraiter = new ArrayList<Scenario>(400);

	/**
	 * Constructeur par défaut
	 */
	private GestionnaireChainesAttaques()
	{
		super();
	}

	/**
	 * @return default instance
	 */
	public static GestionnaireChainesAttaques getInstance()
	{
		return instance;
	}

	/**
	 * 
	 * @param aSceanrio un scénario traité à ajouter
	 * @throws TechnicalException e
	 */
	public synchronized void ajouterScenarioTraite(Scenario aSceanrio) throws TechnicalException
	{
		// Récupérer l'identifiant de la chaine d'attaque du scénario
		String id = aSceanrio.getIdentifiantChaineAttaques();

		// Récuperer la chaine d'attaques correspondant à cet identifiant
		ChaineAttaques chaineAtt = chaines.get(id);

		// Si la chaine d'attaque n'existe pas, la créer
		if (chaineAtt == null)
		{
			chaineAtt = new ChaineAttaques(id);
			chaines.put(id, chaineAtt);
		}

		// Ajouter le scénario à la chaine d'attaque
		chaineAtt.ajouterScenario(aSceanrio);

		notifyAll();
	}

	/**
	 * Méthode permettant d'ajouter un scénario dans la liste des scénarios à traiter
	 * 
	 * @param aScenario un scénario à traiter
	 */
	public synchronized void ajouterScenarioATraiter(Scenario aScenario)
	{
		notifyAll();

		try
		{
			// On limite la taille du buffer à 200 scénarios à traiter
			while (scenariosATraiter.size() >= 200)
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		scenariosATraiter.add(aScenario);
		// System.out.println("Ajout d'un Scenario à traiter. " + scenariosATraiter.size() + " Scenarios à traiter.");
		notifyAll();
	}

	/**
	 * Méthode permettant de récupérer le prochain scénario à traiter
	 * 
	 * @return le prochain scénario à traiter
	 */
	public synchronized Scenario getNextScenarioATraiter()
	{
		notifyAll();

		Scenario scenario = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario à traiter, attendre
			while (scenariosATraiter.isEmpty())
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		if (!scenariosATraiter.isEmpty())
		{
			scenario = scenariosATraiter.remove(0);
			// System.out.println("Retrait d'un Scenario à traiter. " + scenariosATraiter.size() + " Scenarios à traiter.");
		}
		notifyAll();

		return scenario;
	}

	/**
	 * @return true s'il reste des scenarios à traiter
	 */
	public synchronized boolean hasNextScenarioATraiter()
	{
		notifyAll();
		return !scenariosATraiter.isEmpty();
	}

	/**
	 * Méthode permettant de récuperer la liste de toutes les chaines d'attaques, ordonnée suivant l'espérance de dégâts décroissante
	 * 
	 * @return le liste des chaines d'attaque ordonnée
	 */
	public List<ChaineAttaques> getListeChainesOrdonnee()
	{
		// Créer une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaques> listeChaines = new ArrayList<ChaineAttaques>(chaines.values());

		// Trier la liste obtenue par ordre d'espérances de dégâts décroissant
		Collections.sort(listeChaines, new ChaineAttComparator());

		return listeChaines;
	}

	class ChaineAttComparator implements Comparator<ChaineAttaques>
	{
		@Override
		public int compare(ChaineAttaques aO1, ChaineAttaques aO2)
		{
			Double espeDeg1 = new Double(aO1.getEsperanceDegatCumulee());
			Double espeDeg2 = new Double(aO2.getEsperanceDegatCumulee());

			// On veut l'ordre décroissant => faire les tests à l'envers
			return espeDeg2.compareTo(espeDeg1);
		}
	}
}
