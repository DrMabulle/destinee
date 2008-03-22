/**
 * 
 */
package destinee.algorithmes.voisinages.cm.threads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import logic.gateways.DestineeToLogicGateway;
import destinee.algorithmes.voisinages.data.ChaineAttaquesV;
import destinee.algorithmes.voisinages.utils.GestionnaireChainesAttaquesV;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Attaque;
import destinee.commun.data.AttaqueBerserk;
import destinee.commun.data.AttaqueBrutale;
import destinee.commun.data.AttaqueImparable;
import destinee.commun.data.AttaqueMagique;
import destinee.commun.data.AttaqueNormale;
import destinee.commun.data.AttaquePrecise;
import destinee.commun.data.AttaqueRapide;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.commun.utils.CachePersos;
import destinee.core.exception.DestineeException;
import destinee.core.exception.TechnicalException;
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
	public DestineeQueryProcessorVoisinage(DestineeToLogicGateway aGateway, Cible aCible)
	{
		prologGateway = aGateway;
		cible = aCible;

		start();
	}

	public static void processQuery(DestineeToLogicGateway aGateway, Cible aCible) throws DestineeException
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
	private static void traitementHeuristique(DestineeToLogicGateway aGateway, Cible aCible) throws DestineeException
	{
		Thread processor = new DestineeQueryProcessorVoisinage(aGateway, aCible);
		int nbVoisinages = 1;
		int nbPersos = CachePersos.getInstance().getNombrePersos();
		int facteurIncrement = nbPersos * 3;

		// Traiter le premier voisinage
		traiterXVoisinages(processor, nbVoisinages);

		List<ChaineAttaquesV> chainesAtt;
		List<ChaineAttaquesV> premiereMoitie;
		List<ChaineAttaquesV> secondeMoitie;

		for (int j = 0; j < 5 * nbPersos; j++)
		{
			// Récupérer la liste des Chaines d'attaques et conjecturer le résultat
			chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesAttaques();
			for (Iterator<ChaineAttaquesV> iter = chainesAtt.iterator(); iter.hasNext();)
			{
				ChaineAttaquesV theChaineAttaquesV = iter.next();
				theChaineAttaquesV.conjecturerResultatFinal();
			}
			// Récupérer la liste des chaines d'attaques ordonnées suivant les résultats de la conjecture
			chainesAtt = GestionnaireChainesAttaquesV.getInstance().getListeChainesOrdonneeConj();
			// Ne garder que la première moitié. La seconde moitié doit être vidée.
			premiereMoitie = new ArrayList<ChaineAttaquesV>((int) (chainesAtt.size() * 2 / 3) + 1);
			secondeMoitie = new ArrayList<ChaineAttaquesV>((int) (chainesAtt.size() * 1 / 3) + 1);
			int indDiscard = (int) chainesAtt.size() * 2 / 3;

			for (int i = chainesAtt.size() - 1; i >= indDiscard; i--)
			{
				secondeMoitie.add(chainesAtt.remove(i));
			}
			premiereMoitie = chainesAtt;

			// Ajouter la première moitié aux chaines d'attaque à traiter
			for (Iterator<ChaineAttaquesV> iter = premiereMoitie.iterator(); iter.hasNext();)
			{
				ChaineAttaquesV chaine = iter.next();
				GestionnaireChainesAttaquesV.getInstance().ajouterChaineATraiter(chaine);
				GestionnaireChainesAttaquesV.getInstance().retirerChaineAttaques(chaine);
			}

			// Traiter les nouvelles chaines avec un nombre d'étape plus grand
			traiterXVoisinages(processor, nbVoisinages * facteurIncrement);
		}

	}

	private static void traiterXVoisinages(Thread aProcessor, int aNbVoisinages) throws DestineeException
	{
		// Démarrer X Threads de traitement des scénarios
		instancierThreadsTraitement(aNbVoisinages);

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
			// Dire aux Threads de traitement de scénario qu'ils peuvent s'arrêter une fois les traitements effectués (pas de nouveaux scénarios)
			TraitementChainesAttaquesV.arreterTraitements();
		}

		joinTreadsTraitement();

		System.out.println("====================================================================");
		System.out.println("====================================================================");
	}

	/**
	 * Instancie les Threads de traitement des Chaines d'attaques.
	 */
	private static void instancierThreadsTraitement(int aNbVoisinages)
	{
		int nbCores = Runtime.getRuntime().availableProcessors();
		int nbThreads = nbCores * 2;
		for (int i = 0; i < nbThreads; i++)
		{
			listeThreads.add(new TraitementChainesAttaquesV(aNbVoisinages));
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
	private static void traitementNormal(DestineeToLogicGateway aGateway, Cible aCible) throws DestineeException
	{
		Thread processor = new DestineeQueryProcessorVoisinage(aGateway, aCible);
		traiterXVoisinages(processor, 0);
	}

	public void run()
	{
		Map<String, Vector<String>> result = prologGateway.queryOnce(QUERY);

		while (result != null && !result.isEmpty())
		{
			Vector<String> vector = result.get("Result");
			/*
			 * On a une String de la forme [att(Perso1,Type1), ..., att(PersoN,TypeN)]
			 */

			CachePersos.getInstance().getNouvellesInstances();
			List<Attaque> listeAttaques = new ArrayList<Attaque>();
			ChaineAttaquesV chaine = null;

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
					String[] details = attaqueS.split(",");
					String persoId = details[0];
					String typeAttaque = details[1];

					Perso attaquant = CachePersos.getInstance().getPerso(persoId);
					Attaque attaque = getNouvelleAttaque(typeAttaque, attaquant);

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
	private static Attaque getNouvelleAttaque(String aTypeAttaque, Perso aAttaquant) throws TechnicalException
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
			throw new TechnicalException("Type d'attaque incorrect : " + aTypeAttaque);
		}

		return attaque;
	}
}
