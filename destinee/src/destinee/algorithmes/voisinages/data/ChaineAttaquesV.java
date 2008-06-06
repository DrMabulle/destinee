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
	 * Constructeur par d�faut
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
	 * @return la probabilit� de r�alisation cumul�e
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
	 * @return l'esp�rance de d�g�ts cumul�e
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
	 * M�thode d'�valuation de la chaine d'attaque suivant les param�tres du fichier application.properties
	 * 
	 * @throws TechnicalException e
	 */
	public void evaluer() throws TechnicalException
	{
		// R�cup�ration de la proba cumul�e cible, � partir du application.properties
		String tmp = PropertiesFactory.getOptionalString(CLE_PROBA_CUMULEE_CIBLE);
		BigDecimal probaCumuleeCible = ConversionUtil.stringVersBigDecimal(tmp, new BigDecimal("0.97"));

		// R�cup�ration de la proba minimum pour un scenario principal, � partir du application.properties
		tmp = PropertiesFactory.getOptionalString(CLE_PROBA_MIN_UNITAIRE);
		BigDecimal probaMinUnitaire = ConversionUtil.stringVersBigDecimal(tmp, new BigDecimal("0.0"));

		// R�cup�ration de la proba minimum pour un scenario principal, � partir du application.properties
		tmp = PropertiesFactory.getOptionalString(CLE_NB_VOISINAGES_MAX);
		int nbVoisinageMax = ConversionUtil.stringVersInteger(tmp, 5);

		evaluer(probaCumuleeCible, probaMinUnitaire, nbVoisinageMax);
	}

	/**
	 * M�thode d'�valuation de la chaine d'attaque suivant certains param�tres donn�s
	 * 
	 * @param aProbaCumuleeCible une probabilit� de r�alisation cumul�e cible
	 * @param aProbaMinUnitaire une probabilit� unitaire minimum pour les sc�narios principaux
	 * @param aNbVoisinages un nombre de voisinage maximum
	 * @throws TechnicalException e
	 */
	public void evaluer(final BigDecimal aProbaCumuleeCible, final BigDecimal aProbaMinUnitaire, final int aNbVoisinages)
			throws TechnicalException
	{
		// On indique que la chaine d'attaque a �t� �valu�e au moins une fois
		isEvaluee = true;

		// Quand on �value, on remet � 0 la conjecture
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
			// r�cup�rer le sc�nario de plus haute probabilit� dans la liste des sc�narios de voisinage
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

				// Mise � jour des probas de r�alisation cumul�e et esp�rance de d�g�ts cumul�e
				calculerEsperanceDegatsCumulee(scenariosPrincipaux, voisinages);
				calculerProbaRealisationCumulee(scenariosPrincipaux, voisinages);

				// R�cup�rer le sc�nario de plus haute probabilit� pour l'it�ration suivante
				Collections.sort(voisinages, new ScenarioVComparator());
				// Si le voisinage calcul� est vide, s'arr�ter : on a d�j� tout calcul�
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
				(voisinages.size() + scenariosPrincipaux.size()), " sc�narios �valu�s. Temps = ", (System.currentTimeMillis() - startTime),
				" ms." });
		}
	}

	/**
	 * M�thode permettant de calculer un tr�s bon candidat pour le sc�nario de plus haute probabilit� de r�alisation
	 * 
	 * @return Le sc�nario initial pour l'�valuation
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
					// La proba de cette r�solution est sup�rieure � 50% => on peut arr�ter l�
					break;
				}
			}
			scenar.changerTypeResolution(i, typeResolPlusProbable);
		}

		return scenar;
	}

	/**
	 * M�thode permettant de r�cup�rer le voisinage direct d'un sc�nario
	 * 
	 * @param aScenar un sc�nario
	 * @return un ensemble de sc�narios repr�sentant le voisinage du sc�nario donn�
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

					// Echec comp�tence
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

					// Echec comp�tence
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

					// Echec comp�tence
					tmp.set(i, ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					break;

				case ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE:

					// Esquive simple
					tmp.set(i, ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
					scenarTmp = new ScenarioV(this, tmp);
					voisinage.add(scenarTmp);

					// Echec comp�tence
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
	 * Permet de conjecturer quelle sera l'esp�rance de d�g�t pour 100% de proba de r�alisation
	 * 
	 * @return l'esp�rance de d�g�t conjectur�e
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
	 * Permet de conjecturer l'esp�rance de d�g�t finale en fonction du voisinage d�j� calcul�. Cette conjecture se base sur l'indice de bourrinisme moyen de ce
	 * qui a �t� calcul� et sur le fait qu'� 100% de proba de r�alisation l'indice de bourrinisme doit �tre � 1.
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
	 * Permet d'�valuer l'indice de bourrinisme moyen pour
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
	 * M�thode permettant d'initiliser les persos et la cible comme s'ils avaient d�j� effectu� les attaques pass�es en param�tre, suivant les r�alisations
	 * pass�es en param�tre �galement
	 * 
	 * @param aListeAttaques une liste d'attaques
	 * @param aListeResolutions une liste de r�alisation d'attaque
	 * @param aIndice position de la derni�re attaque � effectuer
	 */
	private void initPersosCible(final List<Attaque> aListeAttaques, final List<Integer> aListeResolutions, final int aIndice)
	{
		// R�initialiser la cible et les persos
		cible.reinitialiserFatigue();
		cible.reinitialiserMalusDefense();
		for (Attaque att : chaine)
		{
			att.getPerso().reinitialiserFatigue();
		}

		// Appliquer aux persos et � la cible chaque attaque, jusqu'� l'indice - 1
		for (int i = 0; i < aIndice; i++)
		{
			cible.incrementerFatigue();
			cible.incrementerMalusDefense(aListeAttaques.get(i), aListeResolutions.get(i));

			aListeAttaques.get(i).getPerso().incrementerFatigue(aListeAttaques.get(i));
		}
	}

	/**
	 * Calcul de la probabilit� de r�alisation cumul�e en fonction des sc�narios principaux et de leur voisinage
	 * 
	 * @param aScenariosPrincipaux une liste de sc�narios principaux
	 * @param aVoisinages une liste de sc�narios du voisinage
	 * @throws TechnicalException e
	 */
	private void calculerProbaRealisationCumulee(final List<ScenarioV> aScenariosPrincipaux, final List<ScenarioV> aVoisinages)
			throws TechnicalException
	{
		// Une fois la chaine d'attaque vid�e, on ne touche plus au r�sultat si on en a un
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
	 * Calcul de l'esp�rance de d�g�ts cumul�e en fonction des sc�narios principaux et de leur voisinage
	 * 
	 * @param aScenariosPrincipaux une liste de sc�narios principaux
	 * @param aVoisinages une liste de sc�narios du voisinage
	 * @throws TechnicalException e
	 */
	private void calculerEsperanceDegatsCumulee(final List<ScenarioV> aScenariosPrincipaux, final List<ScenarioV> aVoisinages)
			throws TechnicalException
	{
		// Une fois la chaine d'attaque vid�e, on ne touche plus au r�sultat si on en a un
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
		// V�rification de l'�galit� des r�f�rences
		if (this == aObj)
		{
			return true;
		}
		return this.toString().equals(aObj.toString());
		// /*
		// * Deux Chaines d'Attaques sont consid�r�es �gales si chacune de leurs attaques de rang �gal sont �gales
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
				LogFactory.logError("Erreur lors de la comparaison de 2 sc�narios V !");
				return 0;
			}
		}

	}
}
