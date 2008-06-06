/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.ChaineAttVComparatorConj;
import destinee.algorithmes.voisinages.utils.GestionnaireOrdresAttaquants;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueBerserk;
import destinee.commun.data.AttaqueBrutale;
import destinee.commun.data.AttaqueImparable;
import destinee.commun.data.AttaqueKamikaze;
import destinee.commun.data.AttaqueMagique;
import destinee.commun.data.AttaqueNormale;
import destinee.commun.data.AttaquePrecise;
import destinee.commun.data.AttaqueRapide;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.utils.CachePersos;
import destinee.commun.utils.KamikazeOptimiser;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;
import destinee.core.properties.PropertiesFactory;
import destinee.core.utils.ConversionUtil;

/**
 * @author Bubulle
 * 
 * Consumer
 */
public class TraitementOrdreAttaques extends Thread
{
	private static boolean traitementsTermines = false;
	private static final String CLE_PROBA_CUMULEE_CIBLE = "destinee.chaineAttaquesV.evaluation.probaCumuleeCible";

	private int id = 0;
	private static int compteur = 0;
	private Cible cible;

	public TraitementOrdreAttaques(final Cible aCible)
	{
		super();
		id = compteur++;
		cible = aCible;

		start();
	}

	public void run()
	{
		try
		{
			LogFactory.logInfo(new Object[] { "Thread ", id, " : début des activités de traitement des chaines d'attaque." });
			List<Perso> ordre;
			boolean continuer = true;

			// On continue les traitements tant qu'on ne nous dit pas de s'arrêter et tant qu'il reste des traitements à faire
			while (!traitementsTermines || continuer)
			{
				// Récupérer le Scenario suivant pour le traiter
				ordre = GestionnaireOrdresAttaquants.getInstance().getNextOrdreATraiter();
				if (ordre != null)
				{
					traiterOrdreAttaques(ordre);
					ordre = null;
				}
				else
				{
					// Si on reçoit null, c'est qu'il n'y a pas d'autres éléments à traiter
					continuer = false;
				}
			}

			LogFactory.logInfo(new Object[] { "Thread ", id, " : fin des activités de traitement des ordres d'attaque." });
		}
		catch (TechnicalException e)
		{
			LogFactory.logError("Erreur lors du traitement des ordres d'attaques, méthode run()");
			e.printStackTrace();
		}
	}

	/**
	 * @param aOrdre
	 * @throws TechnicalException
	 */
	protected void traiterOrdreAttaques(final List<Perso> aOrdre) throws TechnicalException
	{
		// Variables temporaires
		ChaineAttaquesV chaine;
		List<ChaineAttaquesV> chainesPrincipales = new ArrayList<ChaineAttaquesV>(20);
		List<ChaineAttaquesV> chainesSecondaires = new ArrayList<ChaineAttaquesV>(500);
		List<ChaineAttaquesV> voisinage;
		boolean isProgressing = true;
		double bestResult = 0.0;
		int etapesSecurite = 0;

		// Générer la chaine d'attaque initiale : faite uniquement d'attaques normales
		chaine = genererChaineInitiale(aOrdre);
		// Evaluer cette première chaine d'attaque
		chaine.evaluer();
		chainesPrincipales.add(chaine);
		bestResult = chaine.getEsperanceDegatConjecturee();

		do
		{
			// Générer le voisinage
			voisinage = genererVoisinage(chaine);
			// Effacer les éléments qui pourraient être en double :
			voisinage.removeAll(chainesPrincipales);
			voisinage.removeAll(chainesSecondaires);
			// Evaluer le voisinage
			evaluerVoisinage(voisinage);

			chainesSecondaires.addAll(voisinage);

			if (!chainesSecondaires.isEmpty())
			{
				Collections.sort(chainesSecondaires, new ChaineAttVComparatorConj());
				chaine = chainesSecondaires.remove(0);
				chainesPrincipales.add(chaine);

				if (chaine.getEsperanceDegatConjecturee() > bestResult)
				{
					bestResult = chaine.getEsperanceDegatConjecturee();
					isProgressing = true;
					etapesSecurite = 0;
				}
				else
				{
					isProgressing = false;
					etapesSecurite++;
				}
			}
			else
			{
				chaine = null;
			}
		} while (chaine != null && (isProgressing || (!isProgressing && etapesSecurite < 3)));

		// Ajouter les chaines d'attaques une fois traitées
		for (ChaineAttaquesV theChaineAttaquesV : chainesPrincipales)
		{
			GestionnaireOrdresAttaquants.getInstance().ajouterChaineTraitee(theChaineAttaquesV);
		}
		for (ChaineAttaquesV theChaineAttaquesV : chainesSecondaires)
		{
			GestionnaireOrdresAttaquants.getInstance().ajouterChaineTraitee(theChaineAttaquesV);
		}
	}

	/**
	 * @param aOrdre un ordre d'attaques
	 * @return la Chaine d'attaques initiale pour cet ordre d'attaque
	 */
	protected ChaineAttaquesV genererChaineInitiale(final List<Perso> aOrdre)
	{
		ChaineAttaquesV chaineInit = null;
		List<Attaque> listeAttaques = new ArrayList<Attaque>(aOrdre.size());
		for (Perso thePerso : aOrdre)
		{
			listeAttaques.add(new AttaqueNormale(thePerso));
		}
		chaineInit = new ChaineAttaquesV(cible, listeAttaques);

		return chaineInit;
	}

	/**
	 * Génère le "voisinage" d'une chaine d'attaque
	 * 
	 * @param aChaine une chaine d'attaques
	 * @return le voisinage de la chaine d'attaques
	 */
	protected List<ChaineAttaquesV> genererVoisinage(final ChaineAttaquesV aChaine)
	{
		List<ChaineAttaquesV> voisinage = new ArrayList<ChaineAttaquesV>(aChaine.size() * 5);

		List<Attaque> listeAttaques = aChaine.getListeAttaques();
		List<Attaque> temp;
		Cible cible = aChaine.getCible();
		int nbAttaques = listeAttaques.size();
		Attaque attaque, attaqueSuivante;
		Perso perso;

		for (int i = 0; i < nbAttaques; i++)
		{
			attaque = listeAttaques.get(i);
			perso = attaque.getPerso();

			// On ne fait de changement que si l'attaque est une attaque normale
			if (attaque instanceof AttaqueNormale)
			{
				// Attaque Berserk
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_BERSERK) > 0 && i < nbAttaques - 1)
				{
					attaqueSuivante = listeAttaques.get(i + 1);
					if (attaqueSuivante instanceof AttaqueNormale && perso.equals(attaqueSuivante.getPerso()))
					{
						temp = new ArrayList<Attaque>(listeAttaques);
						temp.set(i, new AttaqueBerserk(perso));
						temp.remove(i + 1);
						voisinage.add(new ChaineAttaquesV(cible, temp));
					}
				}
				// Attaque Rapide
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_RAPIDE) > 0)
				{
					temp = new ArrayList<Attaque>(listeAttaques);
					temp.set(i, new AttaqueRapide(perso));
					temp.add(i, new AttaqueRapide(perso));
					voisinage.add(new ChaineAttaquesV(cible, temp));
				}
				// Attaque Brutale
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_BRUTALE) > 0)
				{
					temp = new ArrayList<Attaque>(listeAttaques);
					temp.set(i, new AttaqueBrutale(perso));
					voisinage.add(new ChaineAttaquesV(cible, temp));
				}
				// Attaque Kamikaze
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE) > 0 && controlerPA(perso, listeAttaques, i))
				{
					temp = new ArrayList<Attaque>(listeAttaques);
					AttaqueKamikaze kami = new AttaqueKamikaze(perso);
					KamikazeOptimiser.optimiserAttaqueKamikaze(kami, cible);
					temp.set(i, kami);
					voisinage.add(new ChaineAttaquesV(cible, temp));
				}
				// Attaque Précise
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_PRECISE) > 0)
				{
					temp = new ArrayList<Attaque>(listeAttaques);
					temp.set(i, new AttaquePrecise(perso));
					voisinage.add(new ChaineAttaquesV(cible, temp));
				}
				// Attaque Imparable
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_IMPARABLE) > 0)
				{
					temp = new ArrayList<Attaque>(listeAttaques);
					temp.set(i, new AttaqueImparable(perso));
					voisinage.add(new ChaineAttaquesV(cible, temp));
				}
				// Attaque Magique
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_MAGIQUE) > 0)
				{
					temp = new ArrayList<Attaque>(listeAttaques);
					temp.set(i, new AttaqueMagique(perso));
					voisinage.add(new ChaineAttaquesV(cible, temp));
				}
				if (perso.getMaitriseAttaque(ConstantesAttaques.ID_ATTAQUE_MAGIQUE) > 0)
				{
					temp = new ArrayList<Attaque>(listeAttaques);
					temp.set(i, new AttaqueMagique(perso));
					voisinage.add(new ChaineAttaquesV(cible, temp));
				}
			}
		}

		return voisinage;
	}

	/**
	 * @param aPerso un Perso
	 * @param aListeAttaques une liste d'attaque
	 * @param aIndice l'indice de la dernière attaque
	 * @return true si le perso a suffisament de PA pour faire une attaque Kamikaze
	 */
	private boolean controlerPA(final Perso aPerso, final List<Attaque> aListeAttaques, final int aIndice)
	{
		int paDepenses = 0;
		Attaque attaque;
		Perso perso;

		List<Attaque> listeAttaques = new ArrayList<Attaque>(aListeAttaques);
		listeAttaques.remove(aIndice);

		for (int i = 0; i < listeAttaques.size(); i++)
		{
			attaque = listeAttaques.get(i);
			perso = attaque.getPerso();
			if (aPerso.equals(perso))
			{
				paDepenses += attaque.getCoutEnPA();
			}
		}
		return aPerso.getPaCycle1() - paDepenses >= 6;
	}

	/**
	 * Permet d'évaluer l'ensemble des Chaines d'Attaques d'un voisinage
	 * 
	 * @param aVoisinage un voisinage à évaluer
	 * @throws TechnicalException e
	 */
	private void evaluerVoisinage(final List<ChaineAttaquesV> aVoisinage) throws TechnicalException
	{
		String tmp = PropertiesFactory.getOptionalString(CLE_PROBA_CUMULEE_CIBLE);
		BigDecimal probaCumuleeCible = ConversionUtil.stringVersBigDecimal(tmp, new BigDecimal("0.97"));

		int nbVoisinages = 2;
		int nbPersos = CachePersos.getNombrePersos();
		int facteurIncrement = nbPersos * 5;
		int nbIterations = nbPersos;
		int tailleEval = aVoisinage.size();
		BigDecimal probaCible = new BigDecimal("0.65");
		BigDecimal increment = new BigDecimal(0.6 / nbIterations).setScale(2, BigDecimal.ROUND_HALF_UP);
		probaCible = probaCumuleeCible.subtract(increment.multiply(new BigDecimal(nbIterations)));
		BigDecimal probaMin = BigDecimal.ZERO;

		for (int iter = 0; iter < nbIterations + 1; iter++)
		{
			for (int i = 0; i < tailleEval; i++)
			{
				aVoisinage.get(i).evaluer(probaCible, probaMin, nbVoisinages);
				aVoisinage.get(i).conjecturerResultatFinal();
			}

			nbVoisinages *= facteurIncrement;
			probaCible = probaCible.add(increment);
			probaMin = probaMin.multiply(probaMin);
			tailleEval *= 0.66;

			// Sécurité
			if (tailleEval < 3)
			{
				tailleEval = 3;
			}
			if (tailleEval > aVoisinage.size())
			{
				tailleEval = aVoisinage.size();
			}
		}
	}

	public static void arreterTraitements()
	{
		traitementsTermines = true;
	}
}
