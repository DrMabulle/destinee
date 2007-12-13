/**
 * 
 */
package destinee.algorithmes.degrade.cm.threads;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import destinee.algorithmes.degrade.data.ChaineAttaquesD;
import destinee.algorithmes.degrade.utils.GestionnaireChainesAttaquesD;
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

/**
 * @author Bubulle et No-one
 * 
 * Class permettant d'analyser les r�sultats de la Query Prolog effectu�e afin de cr�er les sc�narios retourn�s par Prolog
 * 
 * Producer
 */
public class DestineeQueryProcessorDegrade extends Thread
{

	private List<Map<String, Vector<String>>> queryResults;
	private String scenarioVariableName;
	private Cible cible;

	/**
	 * Constructeur par defaut
	 * 
	 * @param results R�sultats de la query Prolog � traiter
	 * @param aScenarioVariableName nom de la variable Prolog utilis�e
	 * @param aCible Cible des attaques
	 */
	public DestineeQueryProcessorDegrade(List<Map<String, Vector<String>>> results, String aScenarioVariableName, Cible aCible)
	{
		queryResults = results;
		scenarioVariableName = aScenarioVariableName;
		cible = aCible;

		start();
	}

	public static void processQuery(List<Map<String, Vector<String>>> results, String scenarioVariableName, Cible aCible) throws DestineeException
	{

		Thread processor = new DestineeQueryProcessorDegrade(results, scenarioVariableName, aCible);

		// D�marrer 2 Threads de traitement des sc�narios
		Thread t1 = new TraitementChainesAttaques();
		Thread t2 = new TraitementChainesAttaques();
		Thread t3 = new TraitementChainesAttaques();
		Thread t4 = new TraitementChainesAttaques();
		Thread t5 = new TraitementChainesAttaques();
		Thread t6 = new TraitementChainesAttaques();
		Thread t7 = new TraitementChainesAttaques();
		Thread t8 = new TraitementChainesAttaques();

		try
		{
			processor.join();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			// Dire aux Threads de traitement de sc�nario qu'ils peuvent s'arr�ter une fois les traitements effectu�s (pas de nouveaux sc�narios)
			TraitementChainesAttaques.arreterTraitements();
		}

		try
		{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
		}
		catch (InterruptedException e)
		{
			throw new TechnicalException("erreur lors du traitements des sc�narios", e);
		}
	}

	public void run()
	{
		if (queryResults != null)
		{
			for (Map<String, Vector<String>> theMap : queryResults)
			{
				Vector<String> vector = theMap.get(scenarioVariableName);
				/*
				 * On a une String de la forme [att(Perso1,Type1), ..., att(PersoN,TypeN)]
				 */

				if (vector == null)
				{
					// throw new FonctionnalException("Mauvais identifiant de variable Prolog.");
				}
				CachePersos.getInstance().getNouvellesInstances();
				ChaineAttaquesD chaineAtt = new ChaineAttaquesD(cible.clone());

				try
				{
					/*
					 * Parser la String repr�sentant le sc�nario
					 */
					// D�couper les attaques une � une
					for (String attaqueS : vector)
					{
						// attaqueS est du type att(Perso,Type,Resolution)
						attaqueS = attaqueS.substring("att(".length(), attaqueS.length() - 1);

						// Il ne reste plus que Perso,Type,Resolution
						String[] details = attaqueS.split(",");
						String persoId = details[0];
						String typeAttaque = details[1];

						Perso attaquant = CachePersos.getInstance().getPerso(persoId);
						Attaque attaque = getNouvelleAttaque(typeAttaque, attaquant);

						// Construire une chaineAtt avec ces donn�es
						chaineAtt.ajouterAttaque(attaque);
					}
				}
				catch (Exception e)
				{
					// throw new TechnicalException("erreur lors du parsing des r�sultats de la query Prolog", e);
					e.printStackTrace();
				}

				// Le sc�nario �tant complet, l'ajout� au gestionnaire de chaines d'attaques
				GestionnaireChainesAttaquesD.getInstance().ajouterChaineAttaqueATraiter(chaineAtt);
			}

		}
	}

	/**
	 * @param aTypeAttaque un type d'attaque
	 * @param aAttaquant un attaquant
	 * @return l'instance d'attaque correspondante
	 * @throws TechnicalException si le type d'attaque est incorrect
	 */
	private static Attaque getNouvelleAttaque(String aTypeAttaque, Perso aAttaquant) throws TechnicalException
	{
		Attaque attaque;
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
