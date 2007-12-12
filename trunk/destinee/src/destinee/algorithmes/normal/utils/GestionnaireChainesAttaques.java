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

/**
 * @author Bubulle et No-one
 * 
 */
public class GestionnaireChainesAttaques
{
	private Map<String, ChaineAttaques> chaines = new Hashtable<String, ChaineAttaques>(15000);
	private static GestionnaireChainesAttaques instance = new GestionnaireChainesAttaques();

	private List<Scenario> scenariosATraiter = new ArrayList<Scenario>();

	/**
	 * Constructeur par d�faut
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

	public synchronized void ajouterScenarioTraite(Scenario aSceanrio)
	{
		// R�cup�rer l'identifiant de la chaine d'attaque du sc�nario
		String id = aSceanrio.getIdentifiantChaineAttaques();

		// R�cuperer la chaine d'attaques correspondant � cet identifiant
		ChaineAttaques chaineAtt = chaines.get(id);

		// Si la chaine d'attaque n'existe pas, la cr�er
		if (chaineAtt == null)
		{
			chaineAtt = new ChaineAttaques(id);
			chaines.put(id, chaineAtt);
		}

		// Ajouter le sc�nario � la chaine d'attaque
		chaineAtt.ajouterScenario(aSceanrio);
	}

	/**
	 * M�thode permettant d'ajouter un sc�nario dans la liste des sc�narios � traiter
	 * 
	 * @param aScenario un sc�nario � traiter
	 */
	public synchronized void ajouterScenarioATraiter(Scenario aScenario)
	{
		scenariosATraiter.add(aScenario);
//		System.out.println("Ajout d'un Scenario � traiter. " + scenariosATraiter.size() + " Scenarios � traiter.");
		notifyAll();
	}

	/**
	 * M�thode permettant de r�cup�rer le prochain sc�nario � traiter
	 * 
	 * @return le prochain sc�nario � traiter
	 */
	public synchronized Scenario getNextScenarioATraiter()
	{
		Scenario scenario = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario � traiter, attendre
			while (scenariosATraiter.isEmpty())
			{
				wait();
			}

			if (!scenariosATraiter.isEmpty())
			{
				scenario = scenariosATraiter.remove(0);
//				System.out.println("Retrait d'un Scenario � traiter. " + scenariosATraiter.size() + " Scenarios � traiter.");
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return scenario;
	}

	/**
	 * @return true s'il reste des scenarios � traiter
	 */
	public synchronized boolean hasNextScenarioATraiter()
	{
		return !scenariosATraiter.isEmpty();
	}

	/**
	 * M�thode permettant de r�cuperer la liste de toutes les chaines d'attaques, ordonn�e suivant l'esp�rance de d�g�ts d�croissante
	 * 
	 * @return le liste des chaines d'attaque ordonn�e
	 */
	public List<ChaineAttaques> getListeChainesOrdonnee()
	{
		// Cr�er une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaques> listeChaines = new ArrayList<ChaineAttaques>(chaines.values());

		// Trier la liste obtenue par ordre d'esp�rances de d�g�ts d�croissant
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

			// On veut l'ordre d�croissant => faire les tests � l'envers
			return espeDeg2.compareTo(espeDeg1);
		}
	}
}
