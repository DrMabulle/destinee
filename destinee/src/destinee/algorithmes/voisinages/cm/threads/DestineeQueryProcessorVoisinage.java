/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;
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
import destinee.core.exception.DestineeException;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;
import destinee.core.properties.PropertiesFactory;

/**
 * @author Bubulle et No-one
 * 
 * Class permettant d'analyser les résultats de la Query Prolog effectuée afin de créer les scénarios retournés par Prolog
 * 
 * Producer
 */
public class DestineeQueryProcessorVoisinage extends Thread
{

	private static List<Thread> listeThreads = new ArrayList<Thread>(16);
	private Cible cible;
	private DestineeToLogicGateway prologGateway;

	private static final String QUERY = "generationChainesAttaques(Result).";
	private static final String CLE_UTILISATION_HEURISTIQUE = "destinee.voisinage.utiliser.heuristique";

	/**
	 * Constructeur par defaut
	 * 
	 * @param aGateway une DestineeToLogicGateway
	 * @param aScenarioVariableName nom de la variable Prolog utilisée
	 * @param aCible Cible des attaques
	 */
	public DestineeQueryProcessorVoisinage(final DestineeToLogicGateway aGateway, final Cible aCible)
	{
		prologGateway = aGateway;
		cible = aCible;

		start();
	}

	public static void processQuery(final DestineeToLogicGateway aGateway, final Cible aCible) throws DestineeException
	{
		Boolean utiliseHeuristique = PropertiesFactory.getOptionalBoolean(CLE_UTILISATION_HEURISTIQUE);

		if (Boolean.TRUE.equals(utiliseHeuristique))
		{
			traitementHeuristique(aGateway, aCible);
		}
		else
		{
			traitementNormal(aGateway, aCible);
		}
	}

	/**
	 * Traitement par heuristique : seuls les chaines d'attaques les plus intéressantes sont traitées
	 * 
	 * @param aGateway une DestineeToLogicGateway
	 * @param aScenarioVariableName nom de la variable Prolog utilisée
	 */
	private static void traitementHeuristique(final DestineeToLogicGateway aGateway, final Cible aCible) throws DestineeException
	{
		Thread processor = new DestineeQueryProcessorVoisinage(aGateway, aCible);
		int nbVoisinages = 1;
		int nbPersos = CachePersos.getNombrePersos();
		int facteurIncrement = nbPersos;
		int nbIterations = 3 * nbPersos;
		BigDecimal probaCible = new BigDecimal("0.5");
		BigDecimal increment = new BigDecimal("" + (0.5 / nbIterations)).setScale(2, BigDecimal.ROUND_HALF_UP);
		probaCible = new BigDecimal("0.98").subtract(increment.multiply(new BigDecimal(nbIterations)));
		BigDecimal probaMin = BigDecimal.ZERO; // new BigDecimal("0.1");

		// Traiter le premier voisinage
		traiterXVoisinages(processor, nbVoisinages, probaCible, probaMin, true);

		List<ChaineAttaquesV> chainesAtt;
		List<ChaineAttaquesV> premiereMoitie;

		// variables temporaires
		ChaineAttaquesV chaine;
		Iterator<ChaineAttaquesV> iter1;
		// Iterator<ChaineAttaquesV> iter2;
		int indDiscard;
		double taillePremierePartie = 1.0;
		int tailleListeChainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesAttaques().size();

		for (int j = 0; j < nbIterations; j++)
		{
			// Récupérer la liste des Chaines d'attaques et conjecturer le résultat
			chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesAttaques();
			for (iter1 = chainesAtt.iterator(); iter1.hasNext();)
			{
				chaine = iter1.next();
				chaine.conjecturerResultatFinal();
			}
			// Récupérer la liste des chaines d'attaques ordonnées suivant les résultats de la conjecture
			chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesOrdonneeConj();
			// Ne garder que la première moitié. La seconde moitié doit être vidée.
			taillePremierePartie *= 0.55;
			indDiscard = (int) (chainesAtt.size() * taillePremierePartie);
			if (indDiscard <= 50)
			{
				indDiscard = 50;
			}

			for (int i = tailleListeChainesAtt - 1; i >= indDiscard; i--)
			{
				chaine = chainesAtt.remove(i);
				chaine.vider();
			}
			premiereMoitie = chainesAtt;

			GestionnaireChainesAttaquesV.getInstance().ajouterChainesATraiter(premiereMoitie);
			GestionnaireChainesAttaquesV.getInstance().retirerChainesAttaques(premiereMoitie);

			// Traiter les nouvelles chaines avec un nombre d'étape plus grand
			nbVoisinages *= facteurIncrement;
			probaCible = probaCible.add(increment);
			probaMin = probaMin.multiply(probaMin);
			traiterXVoisinages(processor, nbVoisinages, probaCible, probaMin, true);
		}

	}

	private static void traiterXVoisinages(final Thread aProcessor, final int aNbVoisinages, final BigDecimal aProbaCible,
			final BigDecimal aProbaMin, final boolean utiliserHeuristique) throws DestineeException
	{
		GestionnaireChainesAttaquesV.getInstance().declarerDebutTraitement();

		// Démarrer X Threads de traitement des scénarios
		instancierThreadsTraitement(aNbVoisinages, aProbaCible, aProbaMin, utiliserHeuristique);

		try
		{
			aProcessor.join();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			GestionnaireChainesAttaquesV.getInstance().declarerFinTraitement();
			// Dire aux Threads de traitement de scénario qu'ils peuvent s'arrêter une fois les traitements effectués (pas de nouveaux scénarios)
			TraitementChainesAttaquesV.arreterTraitements();
		}

		joinTreadsTraitement();

		if (LogFactory.isLogDebugEnabled())
		{
			LogFactory.logDebug("====================================================================");
			LogFactory.logDebug("====================================================================");
		}
	}

	/**
	 * Instancie les Threads de traitement des Chaines d'attaques.
	 * 
	 * @param aNbVoisinages un nombre de voisinages souhaité
	 * @param aProbaCible une proba cumulée cible souhaitée
	 * @param aProbaMin une proba min unitaire souhaitée
	 */
	private static void instancierThreadsTraitement(final int aNbVoisinages, final BigDecimal aProbaCible, final BigDecimal aProbaMin,
			final boolean utiliserHeuristique)
	{
		int nbCores = Runtime.getRuntime().availableProcessors();
		int nbThreads = nbCores * 4;
		for (int i = 0; i < nbThreads; i++)
		{
			listeThreads.add(new TraitementChainesAttaquesV(aNbVoisinages, aProbaCible, aProbaMin, utiliserHeuristique));
		}
	}

	/**
	 * Join threads traitement
	 * 
	 * @throws TechnicalException e
	 */
	private static void joinTreadsTraitement() throws TechnicalException
	{
		try
		{
			for (Thread thread : listeThreads)
			{
				thread.join();
			}
		}
		catch (InterruptedException e)
		{
			throw new TechnicalException("erreur lors du traitements des chaines d'attaques", e);
		}
	}

	/**
	 * Traitement normal de l'ensemble des chaines d'attaques
	 * 
	 * @param aGateway une DestineeToLogicGateway
	 * @param aScenarioVariableName nom de la variable Prolog utilisée
	 */
	private static void traitementNormal(final DestineeToLogicGateway aGateway, final Cible aCible) throws DestineeException
	{
		Thread processor = new DestineeQueryProcessorVoisinage(aGateway, aCible);
		traiterXVoisinages(processor, 0, BigDecimal.ONE, BigDecimal.ZERO, false);
	}

	public void run()
	{
		Map<String, List<String>> result = prologGateway.queryOnce(QUERY);

		// Variables temporaires
		List<String> vector;
		String[] details;
		String persoId, typeAttaque;
		Perso attaquant;
		Attaque attaque;
		List<Attaque> listeAttaques;
		ChaineAttaquesV chaine;

		while (result != null && !result.isEmpty())
		{
			vector = result.get("Result");
			/*
			 * On a une String de la forme [att(Perso1,Type1), ..., att(PersoN,TypeN)]
			 */

			CachePersos.getNouvellesInstances();
			listeAttaques = new ArrayList<Attaque>();
			chaine = null;

			try
			{
				/*
				 * Parser la String représentant le scénario
				 */
				// Découper les attaques une à une
				for (String attaqueS : vector)
				{
					// attaqueS est du type att(Perso,Type)
					attaqueS = attaqueS.substring("att(".length(), attaqueS.length() - 1);

					// Il ne reste plus que Perso,Type
					details = attaqueS.split(",");
					persoId = details[0];
					typeAttaque = details[1];

					attaquant = CachePersos.getPerso(persoId);
					attaque = getNouvelleAttaque(typeAttaque, attaquant);

					// Construire un ScenarioElement avec ces données et l'ajouté au scénario
					listeAttaques.add(attaque);
				}
				chaine = new ChaineAttaquesV(cible, listeAttaques);

			}
			catch (Exception e)
			{
				// throw new TechnicalException("erreur lors du parsing des résultats de la query Prolog", e);
				e.printStackTrace();
			}

			// Le scénario étant complet, l'ajouté au gestionnaire de chaines d'attaques
			GestionnaireChainesAttaquesV.getInstance().ajouterChaineATraiter(chaine);

			// préparer la prochaine itération
			result = prologGateway.next();
		}

		// Une fois tous les résultats récupérés, fermer la Query Prolog
		prologGateway.stop();
	}

	/**
	 * @param aTypeAttaque un type d'attaque
	 * @param aAttaquant un attaquant
	 * @return l'instance d'attaque correspondante
	 * @throws TechnicalException si le type d'attaque est incorrect
	 */
	private static Attaque getNouvelleAttaque(final String aTypeAttaque, final Perso aAttaquant) throws TechnicalException
	{
		Attaque attaque = null;
		if (ConstantesAttaques.ID_ATTAQUE_BERSERK.equals(aTypeAttaque))
		{
			attaque = new AttaqueBerserk(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_BRUTALE.equals(aTypeAttaque))
		{
			attaque = new AttaqueBrutale(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_IMPARABLE.equals(aTypeAttaque))
		{
			attaque = new AttaqueImparable(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_KAMIKAZE.equals(aTypeAttaque))
		{
			attaque = new AttaqueKamikaze(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_MAGIQUE.equals(aTypeAttaque))
		{
			attaque = new AttaqueMagique(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_NORMALE.equals(aTypeAttaque))
		{
			attaque = new AttaqueNormale(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_PRECISE.equals(aTypeAttaque))
		{
			attaque = new AttaquePrecise(aAttaquant);
		}
		else if (ConstantesAttaques.ID_ATTAQUE_RAPIDE.equals(aTypeAttaque))
		{
			attaque = new AttaqueRapide(aAttaquant);
		}
		else
		{
			throw new TechnicalException(new StringBuffer("Type d'attaque incorrect : ").append(aTypeAttaque).toString());
		}

		return attaque;
	}
}
