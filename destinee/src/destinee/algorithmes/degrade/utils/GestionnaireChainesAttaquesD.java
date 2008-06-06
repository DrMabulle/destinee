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
	 * Constructeur par d�faut
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
	 * M�thode permettant d'ajouter un sc�nario dans la liste des sc�narios � traiter
	 * 
	 * @param aScenario un sc�nario � traiter
	 */
	public synchronized void ajouterChaineAttaqueATraiter(final ChaineAttaquesD aChaine)
	{
		chainesAttDATraiter.add(aChaine);
		if (LogFactory.isLogDebugEnabled())
		{
			LogFactory.logDebug(new Object[] { "Ajout d'une Chaine d'Attaques � traiter. ", chainesAttDATraiter.size(),
				" ChaineAttaquesDegradees � traiter." });
		}
		notifyAll();
	}

	/**
	 * M�thode permettant de r�cup�rer la prochaine ChaineAttaquesDegradees � traiter
	 * 
	 * @return la prochaine ChaineAttaquesDegradees � traiter
	 */
	public synchronized ChaineAttaquesD getNextChaineAttaqueATraiter()
	{
		ChaineAttaquesD chaine = null;
		try
		{
			// Tant qu'il n'y a pas de Scenario � traiter, attendre
			while (chainesAttDATraiter.isEmpty())
			{
				wait();
			}

			if (!chainesAttDATraiter.isEmpty())
			{
				chaine = chainesAttDATraiter.remove(0);
				if (LogFactory.isLogDebugEnabled())
				{
					LogFactory.logDebug(new Object[] { "Retrait d'une Chaine d'Attaques � traiter. ", chainesAttDATraiter.size(),
						" ChaineAttaquesDegradees � traiter." });
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
	 * @return true s'il reste des scenarios � traiter
	 */
	public synchronized boolean hasNextChaineAttaqueATraiter()
	{
		return !chainesAttDATraiter.isEmpty();
	}

	/**
	 * M�thode permettant de r�cuperer la liste de toutes les chaines d'attaques, ordonn�e suivant l'esp�rance de d�g�ts d�croissante Algorithme d�grad�
	 * 
	 * @return le liste des chaines d'attaque ordonn�e
	 */
	public List<ChaineAttaquesD> getListeChainesOrdonnee()
	{
		// Cr�er une chaine contenant l'ensemble des chaines d'attaque
		List<ChaineAttaquesD> listeChaines = new ArrayList<ChaineAttaquesD>(chainesDegradees.values());

		// Trier la liste obtenue par ordre d'esp�rances de d�g�ts d�croissant
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

			// On veut l'ordre d�croissant => faire les tests � l'envers
			return espeDeg2.compareTo(espeDeg1);
		}

	}

}
