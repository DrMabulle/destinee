/**
 * 
 */
package destinee.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import destinee.data.ChaineAttaques;
import destinee.data.Scenario;

/**
 * @author bkessler
 * 
 */
public class GestionnaireChainesAttaques
{
	private Map<String, ChaineAttaques> chaines = new HashMap<String, ChaineAttaques>(15000);
	private static GestionnaireChainesAttaques instance = new GestionnaireChainesAttaques();

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
	public GestionnaireChainesAttaques getInstance()
	{
		return instance;
	}

	public void ajouterScenario(Scenario aSceanrio)
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
	}

	/**
	 * Méthode permettant de récuperer la liste de toutes les chaines
	 * d'attaques, ordonnée suivant l'espérance de dégâts décroissante
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

			return espeDeg1.compareTo(espeDeg2);
		}

	}

}
