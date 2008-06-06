/**
 * 
 */
package destinee.algorithmes.voisinages.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import destinee.commun.data.Attaque;
import destinee.commun.data.Cible;
import destinee.commun.probas.ResolutionAttaque;
import destinee.core.exception.TechnicalException;
import destinee.core.log.LogFactory;
import destinee.core.properties.PropertiesFactory;
import destinee.core.utils.ConversionUtil;

/**
 * @author Bubulle et No-one
 * 
 */
public class ChaineAttaquesV
{
	private List<Attaque> chaine;
	private Cible cible;
	private double esperanceDegatCumulee = 0;
	private BigDecimal probaRealisationCumulee = BigDecimal.ZERO;
	private double esperanceDegatConjecturee = -1;
	private boolean isEvalTerminee = false;
	private boolean isEvaluee = false;

	private String toString = null;
	private String identifiant = null;

	protected final List<ScenarioV> scenariosPrincipaux = new ArrayList<ScenarioV>();
	protected final List<ScenarioV> voisinages = new ArrayList<ScenarioV>();

	private static final String CLE_PROBA_MIN_UNITAIRE = "destinee.chaineAttaquesV.evaluation.valeurMinUnitaire";
	private static final String CLE_PROBA_CUMULEE_CIBLE = "destinee.chaineAttaquesV.evaluation.probaCumuleeCible";
	private static final String CLE_NB_VOISINAGES_MAX = "destinee.chaineAttaquesV.evaluation.nombreVoisinagesMax";

	/**
	 * Constructeur par défaut
	 * 
	 * @param aCible une cible
	 * @param aListeAttaques une liste d'attaques
	 */
	public ChaineAttaquesV(final Cible aCible, final List<Attaque> aListeAttaques)
	{
		super();
		cible = aCible.clone();
		chaine = aListeAttaques;
	}

	/**
	 * @return la longueur de la chaine d'attaque
	 */
	public int size()
	{
		return chaine.size();
	}

	/**
	 * @return l'identifiant de la chaine d'attaque
	 */
	public String getIdentifiant()
	{
		if (identifiant == null)
		{
			StringBuffer sb = new StringBuffer("");

			for (Attaque att : chaine)
			{
				sb.append(att.toString());
				sb.append(" - ");
			}

			identifiant = sb.substring(0, sb.lastIndexOf(" - "));
		}
		return identifiant;
	}

	/**
	 * @return la cible
	 */
	public Cible getCible()
	{
		return cible;
	}

	/**
	 * @return la liste des attaques de cette chaine d'attaques
	 */
	public List<Attaque> getListeAttaques()
	{
		return chaine;
	}

	/**
	 * @return la probabilité de réalisation cumulée
	 * @throws TechnicalException e
	 */
	public BigDecimal getProbaRealisationCumulee() throws TechnicalException
	{
		if (!isEvaluee)
		{
			evaluer();
		}
		return probaRealisationCumulee;
	}

	/**
	 * @return l'espérance de dégâts cumulée
	 * @throws TechnicalException e
	 */
	public double getEsperanceDegatCumulee() throws TechnicalException
	{
		if (!isEvaluee)
		{
			evaluer();
		}
		return esperanceDegatCumulee;
	}

	/**
	 * Méthode d'évaluation de la chaine d'attaque suivant les paramètres du fichier application.properties
	 * 
	 * @throws TechnicalException e
	 */
	public void evaluer() throws TechnicalException
	{
		// Récupération de la proba cumulée cible, à partir du application.properties
		String tmp = PropertiesFactory.getOptionalString(CLE_PROBA_CUMULEE_CIBLE);
		BigDecimal probaCumuleeCible = ConversionUtil.stringVersBigDecimal(tmp, new BigDecimal("0.97"));

		// Récupération de la proba minimum pour un scenario principal, à partir du application.properties
		tmp = PropertiesFactory.getOptionalString(CLE_PROBA_MIN_UNITAIRE);
		BigDecimal probaMinUnitaire = ConversionUtil.stringVersBigDecimal(tmp, new BigDecimal("0.0"));

		// Récupération de la proba minimum pour un scenario principal, à partir du application.properties
		tmp = PropertiesFactory.getOptionalString(CLE_NB_VOISINAGES_MAX);
		int nbVoisinageMax = ConversionUtil.stringVersInteger(tmp, 5);

		evaluer(probaCumuleeCible, probaMinUnitaire, nbVoisinageMax);
	}

	/**
	 * Méthode d'évaluation de la chaine d'attaque suivant certains paramètres donnés
	 * 
	 * @param aProbaCumuleeCible une probabilité de réalisation cumulée cible
	 * @param aProbaMinUnitaire une probabilité unitaire minimum pour les scénarios principaux
	 * @param aNbVoisinages un nombre de voisinage maximum
	 * @throws TechnicalException e
	 */
	public void evaluer(final BigDecimal aProbaCumuleeCible, final BigDecimal aProbaMinUnitaire, final int aNbVoisinages)
			throws TechnicalException
	{
		// On indique que la chaine d'attaque a été évaluée au moins une fois
		isEvaluee = true;

		// Quand on évalue, on remet à 0 la conjecture
		isEvalTerminee = false;
		esperanceDegatConjecturee = -1;

		if ((scenariosPrincipaux == null || scenariosPrincipaux.isEmpty()) && (voisinages == null || voisinages.isEmpty()))
		{
			ScenarioV scenarI = getScenarioInital();
			voisinages.add(scenarI);
		}

		long startTime = System.currentTimeMillis();

		if (!voisinages.isEmpty())
		{
			// récupérer le scénario de plus haute probabilité dans la liste des scénarios de voisinage
			Collections.sort(voisinages, new ScenarioVComparator());
			ScenarioV scenar = voisinages.remove(0);
			scenariosPrincipaux.add(scenar);

			// Variables temporaires
			Set<ScenarioV> voisinage;

			while (scenariosPrincipaux.size() <= aNbVoisinages && aProbaCumuleeCible.compareTo(probaRealisationCumulee) >= 0
					&& aProbaMinUnitaire.compareTo(scenar.getProbaRealisation()) <= 0)
			{
				voisinage = getVoisinage(scenar);
				// Eviter les doublons :
				for (ScenarioV scenarioV : voisinage)
				{
					if (!voisinages.contains(scenarioV))
					{
						voisinages.add(scenarioV);
					}
				}
				voisinages.removeAll(scenariosPrincipaux);

				// Mise à jour des probas de réalisation cumulée et espérance de dégâts cumulée
				calculerEsperanceDegatsCumulee(scenariosPrincipaux, voisinages);
				calculerProbaRealisationCumulee(scenariosPrincipaux, voisinages);

				// Récupérer le scénario de plus haute probabilité pour l'itération suivante
				Collections.sort(voisinages, new ScenarioVComparator());
				// Si le voisinage calculé est vide, s'arrêter : on a déjà tout calculé
				if (voisinages.isEmpty())
				{
					break;
				}
				scenar = voisinages.remove(0);
				scenariosPrincipaux.add(scenar);
			}
		}
		if (LogFactory.isLogDebugEnabled())
		{
			LogFactory.logDebug(new Object[] { "Chaine d'attaques ", getIdentifiant(), ": ",
				ConversionUtil.bigDecimalVersString(getProbaRealisationCumulee(), 15), ", ",
				(voisinages.size() + scenariosPrincipaux.size()), " scénarios évalués. Temps = ", (System.currentTimeMillis() - startTime),
				" ms." });
		}
	}

	/**
	 * Méthode permettant de calculer un très bon candidat pour le scénario de plus haute probabilité de réalisation
	 * 
	 * @return Le scénario initial pour l'évaluation
	 */
	protected ScenarioV getScenarioInital()
	{
		int[] typesResol = new int[] { ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE, ResolutionAttaque.RESOLUTION_COUP_SIMPLE,
			ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE,
			ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE };

		ScenarioV scenar = new ScenarioV(this);
		int chaineSize = chaine.size();

		// Variables temporaires
		int typeResolPlusProbable;
		BigDecimal probaMaxTmp = BigDecimal.ZERO;
		BigDecimal probaTmp;

		for (int i = 0; i < chaineSize; i++)
		{
			// Remettre les persos et la cible en situation
			initPersosCible(chaine, scenar.getListeTypesResolution(), i);

			scenar.ajouterElementResolution(ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
			typeResolPlusProbable = ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE;
			probaMaxTmp = BigDecimal.ZERO;
			probaTmp = null;

			for (int resolution : typesResol)
			{
				scenar.changerTypeResolution(i, resolution);
				probaTmp = ResolutionAttaque.resoudreAttaque(chaine.get(i), cible, resolution);
				if (probaTmp.compareTo(probaMaxTmp) > 0)
				{
					probaMaxTmp = probaTmp;
					typeResolPlusProbable = resolution;
				}

				if (probaTmp.compareTo(new BigDecimal("0.5")) >= 0)
				{
					// La proba de cette résolution est supérieure à 50% => on peut arrêter là
					break;
				}
			}
			scenar.changerTypeResolution(i, typeResolPlusProbable);
		}

		return scenar;
	}

	/**
	 * Méthode permettant de récupérer le voisinage direct d'un scénario
	 * 
	 * @param aScenar un scénario
	 * @return un ensemble de scénarios représentant le voisinage du scénario donné
	 */
	protected Set<ScenarioV> getVoisinage(final ScenarioV aScenar)
	{
		Set<ScenarioV> voisinage = new HashSet<ScenarioV>();
		List<Integer> listeResolutions = aScenar.getListeTypesResolution();
		int nbResoltions = listeResolutions.size();

		// Variables temporaires
		List<Integer> tmp;
		ScenarioV scenarTmp;

		for (int i = 0; i < nbResoltions; i++)
		{
			int resolution = listeResolutions.get(i);
			tmp = new ArrayList<Integer>(listeResolutions);

			switch (resolution)
			{
				case ResolutionAttaque.RESOLUTION_COUP_CRITIQUE:

					// Coup simple
					tmp.set(i, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Echec compétence
					tmp.set(i, ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					break;

				case ResolutionAttaque.RESOLUTION_COUP_SIMPLE:

					// Coup critique
					tmp.set(i, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Esquive simple
					tmp.set(i, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Echec compétence
					tmp.set(i, ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					break;

				case ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE:

					// Coup simple
					tmp.set(i, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Esquive parfaite
					tmp.set(i, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Echec compétence
					tmp.set(i, ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					break;

				case ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE:

					// Esquive simple
					tmp.set(i, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Echec compétence
					tmp.set(i, ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					break;

				case ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE:

					// Coup critique
					tmp.set(i, ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Coup simple
					tmp.set(i, ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Esquive simple
					tmp.set(i, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Esquive parfaite
					tmp.set(i, ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					break;

				default:
					break;
			}
		}

		return voisinage;
	}

	/**
	 * Permet de conjecturer quelle sera l'espérance de dégât pour 100% de proba de réalisation
	 * 
	 * @return l'espérance de dégât conjecturée
	 * @throws TechnicalException e
	 */
	public double getEsperanceDegatConjecturee() throws TechnicalException
	{
		if (esperanceDegatConjecturee == -1)
		{
			conjecturerResultatFinal();
		}
		return esperanceDegatConjecturee;
	}

	/**
	 * Permet de conjecturer l'espérance de dégât finale en fonction du voisinage déjà calculé. Cette conjecture se base sur l'indice de bourrinisme moyen de ce
	 * qui a été calculé et sur le fait qu'à 100% de proba de réalisation l'indice de bourrinisme doit être à 1.
	 * 
	 * @throws TechnicalException e
	 */
	public void conjecturerResultatFinal() throws TechnicalException
	{
		if (esperanceDegatConjecturee == -1 && !isEvalTerminee)
		{
			int nbScenariosActuel = scenariosPrincipaux.size() + voisinages.size();
			int nbScenariosTotal = (int) Math.pow(5, size());
			int nbScenariosRestants = nbScenariosTotal - nbScenariosActuel;

			double indBourrinismeActuel = getIndiceBourrinisme();

			double probaActuelle = ConversionUtil.bigdecimalVersDouble(getProbaRealisationCumulee(), 10);
			double probaRestante = 1.0 - probaActuelle;
			double esperanceDeg = getEsperanceDegatCumulee();

			double indBourrinismeRestant;
			if (nbScenariosRestants == 0)
			{
				indBourrinismeRestant = 1;
			}
			else
			{
				indBourrinismeRestant = (nbScenariosTotal - (indBourrinismeActuel * nbScenariosActuel)) / nbScenariosRestants;
			}

			esperanceDegatConjecturee = (esperanceDeg)
					+ (esperanceDeg / probaActuelle * probaRestante / indBourrinismeActuel * indBourrinismeRestant);
		}
	}

	/**
	 * Permet d'évaluer l'indice de bourrinisme moyen pour
	 * 
	 * @param aScenarioCollection
	 * @return
	 */
	protected double evaluerIndiceBourrinisme(final Collection<ScenarioV> aScenarioCollection)
	{
		double indiceBourrinismeMoyen = 0;
		if (aScenarioCollection != null && aScenarioCollection.size() != 0)
		{
			ScenarioV theScenarioV;
			for (Iterator<ScenarioV> theIterator = aScenarioCollection.iterator(); theIterator.hasNext();)
			{
				theScenarioV = theIterator.next();
				indiceBourrinismeMoyen += theScenarioV.getIndiceBourrinisme();
			}
			indiceBourrinismeMoyen /= aScenarioCollection.size();
		}

		return indiceBourrinismeMoyen;
	}

	/**
	 * Méthode permettant d'initiliser les persos et la cible comme s'ils avaient déjà effectué les attaques passées en paramètre, suivant les réalisations
	 * passées en paramètre également
	 * 
	 * @param aListeAttaques une liste d'attaques
	 * @param aListeResolutions une liste de réalisation d'attaque
	 * @param aIndice position de la dernière attaque à effectuer
	 */
	private void initPersosCible(final List<Attaque> aListeAttaques, final List<Integer> aListeResolutions, final int aIndice)
	{
		// Réinitialiser la cible et les persos
		cible.reinitialiserFatigue();
		cible.reinitialiserMalusDefense();
		for (Attaque att : chaine)
		{
			att.getPerso().reinitialiserFatigue();
		}

		// Appliquer aux persos et à la cible chaque attaque, jusqu'à l'indice - 1
		for (int i = 0; i < aIndice; i++)
		{
			cible.incrementerFatigue();
			cible.incrementerMalusDefense(aListeAttaques.get(i), aListeResolutions.get(i));

			aListeAttaques.get(i).getPerso().incrementerFatigue(aListeAttaques.get(i));
		}
	}

	/**
	 * Calcul de la probabilité de réalisation cumulée en fonction des scénarios principaux et de leur voisinage
	 * 
	 * @param aScenariosPrincipaux une liste de scénarios principaux
	 * @param aVoisinages une liste de scénarios du voisinage
	 * @throws TechnicalException e
	 */
	private void calculerProbaRealisationCumulee(final List<ScenarioV> aScenariosPrincipaux, final List<ScenarioV> aVoisinages)
			throws TechnicalException
	{
		// Une fois la chaine d'attaque vidée, on ne touche plus au résultat si on en a un
		if (!isEvalTerminee)
		{
			probaRealisationCumulee = BigDecimal.ZERO;

			for (ScenarioV scenarioV : aScenariosPrincipaux)
			{
				probaRealisationCumulee = probaRealisationCumulee.add(scenarioV.getProbaRealisation());
			}

			for (ScenarioV scenarioV : aVoisinages)
			{
				probaRealisationCumulee = probaRealisationCumulee.add(scenarioV.getProbaRealisation());
			}
		}
	}

	/**
	 * Calcul de l'espérance de dégâts cumulée en fonction des scénarios principaux et de leur voisinage
	 * 
	 * @param aScenariosPrincipaux une liste de scénarios principaux
	 * @param aVoisinages une liste de scénarios du voisinage
	 * @throws TechnicalException e
	 */
	private void calculerEsperanceDegatsCumulee(final List<ScenarioV> aScenariosPrincipaux, final List<ScenarioV> aVoisinages)
			throws TechnicalException
	{
		// Une fois la chaine d'attaque vidée, on ne touche plus au résultat si on en a un
		if (!isEvalTerminee)
		{
			esperanceDegatCumulee = 0;

			for (ScenarioV scenarioV : aScenariosPrincipaux)
			{
				esperanceDegatCumulee += scenarioV.getEsperanceDegats()
						* ConversionUtil.bigdecimalVersDouble(scenarioV.getProbaRealisation(), 20);
			}

			for (ScenarioV scenarioV : aVoisinages)
			{
				esperanceDegatCumulee += scenarioV.getEsperanceDegats()
						* ConversionUtil.bigdecimalVersDouble(scenarioV.getProbaRealisation(), 20);
			}
		}
	}

	public double getIndiceBourrinisme()
	{
		int nbScenariosActuel = scenariosPrincipaux.size() + voisinages.size();
		Set<ScenarioV> scenarioCollection = new HashSet<ScenarioV>(((nbScenariosActuel * 3 / 2)) + 1);
		scenarioCollection.addAll(scenariosPrincipaux);
		scenarioCollection.addAll(voisinages);
		return evaluerIndiceBourrinisme(scenarioCollection);
	}

	public int getNbScenariosTraites()
	{
		return scenariosPrincipaux.size() + voisinages.size();
	}

	public void vider()
	{
		// scenariosPrincipaux = null;
		// voisinages = null;
		isEvalTerminee = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object aObj)
	{
		// Vérification de l'égalité des références
		if (this == aObj)
		{
			return true;
		}
		return this.toString().equals(aObj.toString());
		// /*
		// * Deux Chaines d'Attaques sont considérées égales si chacune de leurs attaques de rang égal sont égales
		// */
		// if (aObj != null && aObj instanceof ChaineAttaquesV)
		// {
		// ChaineAttaquesV chaineTmp = (ChaineAttaquesV) aObj;
		// if (chaineTmp.size() == this.size())
		// {
		// boolean egales = true;
		// int chaineSize = chaine.size();
		//
		// for (int i = 0; i < chaineSize && egales; i++)
		// {
		// egales &= this.chaine.get(i).equals(chaineTmp.chaine.get(i));
		// }
		//
		// if (egales)
		// {
		// return true;
		// }
		// }
		// }
		// return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hashcode = 5;
		for (Attaque attaque : chaine)
		{
			hashcode = hashcode * 3 + attaque.hashCode();
		}
		return hashcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if (toString == null)
		{
			toString = new StringBuffer(getIdentifiant()).append(" || ").append(cible.toString()).toString();
		}
		return toString;
	}

	private class ScenarioVComparator implements Comparator<ScenarioV>
	{
		@Override
		public int compare(final ScenarioV aArg0, final ScenarioV aArg1)
		{
			try
			{
				return aArg1.getProbaRealisation().compareTo(aArg0.getProbaRealisation());
			}
			catch (TechnicalException e)
			{
				LogFactory.logError("Erreur lors de la comparaison de 2 scénarios V !");
				return 0;
			}
		}

	}
}
