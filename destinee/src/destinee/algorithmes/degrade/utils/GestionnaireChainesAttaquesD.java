/**
 * 
 */
package destinee.algorithmes.degrade.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import destinee.algorithmes.degrade.data.ChaineAttaquesD;
import destinee.core.log.LogFactory;

/**
 * @author Bubulle et No-one
 * 
 */
public class GestionnaireChainesAttaquesD
{
	private Map<String, ChaineAttaquesD> chainesDegradees = new Hashtable<String, ChaineAttaquesD>(6000);
	private static GestionnaireChainesAttaquesD instance = new GestionnaireChainesAttaquesD();

	private List<ChaineAttaquesD> chainesAttDATraiter = new ArrayList<ChaineAttaquesD>();

	/**
	 * Constructeur par défaut
	 */
	private GestionnaireChainesAttaquesD()
	{
		super();
	}

	/**
	 * @return default instance
	 */
	public static GestionnaireChainesAttaquesD getInstance()
	{
		return instance;
	}

	public synchronized void ajouterChaineAttaqueTraitee(final ChaineAttaquesD aChaine)
	{
		chainesDegradees.put(aChaine.getIdentifiant(), aChaine);
	}

	/**
	 * Méthode permettant d'ajouter un scénario dans la liste des scénarios à traiter
	 * 
	 * @param aScenario un scénario à traiter
	 */
	public synchronized void ajouterChaineAttaqueATraiter(final ChaineAttaquesD aChaine)
	{
		chainesAttDATraiter.add(aChaine);
		if (LogFactory.isLogDebugEnabled())
		{
			LogFactory.logDebug(new Object[] { "Ajout d'une Chaine d'Attaques à traiter. ", chainesAttDATraiter.size(),
				" ChaineAttaquesDegradees à traiter." });
		}
		notifyAll();
	}

	/**
	 * Méthode permettant de récupérer la prochaine ChaineAttaquesDegradees à traiter
	 * 
	 * @return la prochaine ChaineAttaquesDegradees à traiter
	 */
	public synchronized ChaineAttaquesD getNextChaineAttaqueATraiter()
	{
		ChaineAttaquesD chaine = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario à traiter, attendre
			while (chainesAttDATraiter.isEmpty())
			{
				wait();
			}

			if (!chainesAttDATraiter.isEmpty())
			{
				chaine = chainesAttDATraiter.remove(0);
				if (LogFactory.isLogDebugEnabled())
				{
					LogFactory.logDebug(new Object[] { "Retrait d'une Chaine d'Attaques à traiter. ", chainesAttDATraiter.size(),
						" ChaineAttaquesDegradees à traiter." });
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return chaine;
	}

	/**
	 * @return true s'il reste des scenarios à traiter
	 */
	public synchronized boolean hasNextChaineAttaqueATraiter()
	{
		return !chainesAttDATraiter.isEmpty();
	}

	/**
	 * Méthode permettant de récuperer la liste de toutes les chaines d'attaques, ordonnée suivant l'espérance de dégâts décroissante Algorithme dégradé
	 * 
	 * @return le liste des chaines d'attaque ordonnée
	 */
	public List<ChaineAttaquesD> getListeChainesOrdonnee()
	{
		// Créer une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesD> listeChaines = new ArrayList<ChaineAttaquesD>(chainesDegradees.values());

		// Trier la liste obtenue par ordre d'espérances de dégâts décroissant
		Collections.sort(listeChaines, new ChaineAttDegradeesComparator());

		return listeChaines;
	}

	class ChaineAttDegradeesComparator implements Comparator<ChaineAttaquesD>
	{
		@Override
		public int compare(final ChaineAttaquesD aO1, final ChaineAttaquesD aO2)
		{
			Double espeDeg1 = new Double(aO1.getEsperanceDegatCumulee());
			Double espeDeg2 = new Double(aO2.getEsperanceDegatCumulee());

			// On veut l'ordre décroissant => faire les tests à l'envers
			return espeDeg2.compareTo(espeDeg1);
		}

	}

}
