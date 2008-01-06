/**
 * 
 */
package destinee.algorithmes.voisinages.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import destinee.commun.data.Attaque;
import destinee.commun.data.Cible;
import destinee.commun.probas.ResolutionAttaque;
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
	private BigDecimal probaRealisationCumulee = null;
	

	private static final String CLE_PROBA_MIN_UNITAIRE = "destinee.chaineAttaquesV.evaluation.valeurMinUnitaire";
	private static final String CLE_PROBA_CUMULEE_CIBLE = "destinee.chaineAttaquesV.evaluation.probaCumuleeCible";
	private static final String CLE_NB_VOISINAGES_MAX = "destinee.chaineAttaquesV.evaluation.nombreVoisinagesMax";
	
	public ChaineAttaquesV(Cible aCible, List<Attaque> aListeAttaques)
	{
		super();
		cible = aCible;
		chaine = aListeAttaques;
	}
	
	public int size()
	{
		return chaine.size();
	}
	
	public String getIdentifiant()
	{
		StringBuffer sb = new StringBuffer("");

		for (Attaque att : chaine)
		{
			sb.append(att.getPerso().getIdentifiant());
			sb.append(att.getTypeAttaque());
			sb.append("-");
		}

		return sb.toString();
	}

	public Cible getCible()
	{
		return cible;
	}

	public List<Attaque> getListeAttaques()
	{
		return chaine;
	}
	
	
	protected ScenarioV getScenarioInital()
	{
		ScenarioV scenar = new ScenarioV(this);
		
		List<Integer> typesResol = new ArrayList<Integer>();
		typesResol.add(ResolutionAttaque.RESOLUTION_COUP_CRITIQUE);
		typesResol.add(ResolutionAttaque.RESOLUTION_COUP_SIMPLE);
		typesResol.add(ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
		typesResol.add(ResolutionAttaque.RESOLUTION_ESQUIVE_PARFAITE);
		typesResol.add(ResolutionAttaque.RESOLUTION_ESQUIVE_SIMPLE);
		
		for(int i = 0; i < chaine.size(); i++)
		{
			scenar.ajouterElementResolution(ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE);
			int typeResolPlusProbable = ResolutionAttaque.RESOLUTION_ECHEC_COMPETENCE;
			BigDecimal probaMaxTmp = BigDecimal.ZERO;
			
			for (Integer resolution : typesResol)
			{
				scenar.changerTypeResolution(i, resolution);
				BigDecimal probaTmp = scenar.getProbaRealisation();
				if (probaTmp.compareTo(probaMaxTmp) > 0)
				{
					probaMaxTmp = probaTmp;
					typeResolPlusProbable = resolution;
				}
			}
			scenar.changerTypeResolution(i, typeResolPlusProbable);
		}
		
		return scenar;
	}
	
	public BigDecimal getProbaCumulee()
	{
		if (probaRealisationCumulee == null)
		{
			evaluer();
		}
		return probaRealisationCumulee;
	}
	
	public double getEsperanceDegatsCumules()
	{
		if (probaRealisationCumulee == null)
		{
			evaluer();
		}
		return esperanceDegatCumulee;
	}
	
	protected void evaluer()
	{
		ScenarioV scenarI = getScenarioInital();
		
		List<ScenarioV> scenariosPrincipaux = new ArrayList<ScenarioV>();
		List<ScenarioV> voisinages = new ArrayList<ScenarioV>();
		
		// Récupération de la proba cumulée cible, à partir du application.properties
		String tmp = PropertiesFactory.getOptionalString(CLE_PROBA_CUMULEE_CIBLE);
		BigDecimal probaCumuleeCible = ConversionUtil.stringVersBigDecimal(tmp, new BigDecimal(0.95));

		// Récupération de la proba minimum pour un scenario principal, à partir du application.properties
		tmp = PropertiesFactory.getOptionalString(CLE_PROBA_MIN_UNITAIRE);
		BigDecimal probaMinUnitaire = ConversionUtil.stringVersBigDecimal(tmp, new BigDecimal(0.001));

		// Récupération de la proba minimum pour un scenario principal, à partir du application.properties
		tmp = PropertiesFactory.getOptionalString(CLE_NB_VOISINAGES_MAX);
		int nbVoisinageMax = ConversionUtil.stringVersInteger(tmp, 5);
		
		ScenarioV scenar = scenarI;
		scenariosPrincipaux.add(scenar);
		probaRealisationCumulee = BigDecimal.ZERO;
		
		while(scenariosPrincipaux.size() <= nbVoisinageMax 
				&& probaCumuleeCible.compareTo(probaRealisationCumulee) >= 0
				&& probaMinUnitaire.compareTo(scenar.getProbaRealisation()) <= 0)
		{
			Set<ScenarioV> voisinage = getVoisinage(scenar);
			// Eviter les doublons :
			for (ScenarioV scenarioV : voisinage)
			{
				if (!voisinages.contains(scenarioV))
				{
					voisinages.add(scenarioV);
				}
			}
			voisinages.removeAll(scenariosPrincipaux);
			Collections.sort(voisinages, new ScenarioVComparator());
			// Si le voisinage calculé est vide, c'est que l'on a déjà tout calculé et donc les cas d'arrêts sont atteints
			scenar = voisinages.remove(0);
			scenariosPrincipaux.add(scenar);
			calculerProbaCumulee(scenariosPrincipaux, voisinages);
			calculerEsperanceDegats(scenariosPrincipaux, voisinages);
		}
		
	}

	private void calculerProbaCumulee(List<ScenarioV> aScenariosPrincipaux, List<ScenarioV> aVoisinages)
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

	private void calculerEsperanceDegats(List<ScenarioV> aScenariosPrincipaux, List<ScenarioV> aVoisinages)
	{
		esperanceDegatCumulee = 0;
		
		for (ScenarioV scenarioV : aScenariosPrincipaux)
		{
			esperanceDegatCumulee += scenarioV.getEsperanceDegats() * ConversionUtil.bigdecimalVersDouble(scenarioV.getProbaRealisation(), 20);
		}

		for (ScenarioV scenarioV : aVoisinages)
		{
			esperanceDegatCumulee += scenarioV.getEsperanceDegats() * ConversionUtil.bigdecimalVersDouble(scenarioV.getProbaRealisation(), 20);
		}
	}

	private Set<ScenarioV> getVoisinage(ScenarioV aScenar)
	{
		Set<ScenarioV> voisinage = new HashSet<ScenarioV>();
		List<Integer> listeResolutions = aScenar.getListeTypesResolution();
		
		for(int i = 0; i < listeResolutions.size(); i++)
		{
			int resolution = listeResolutions.get(i);
			List<Integer> tmp = new ArrayList<Integer>(listeResolutions);
			ScenarioV scenarTmp;
			
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aObj)
	{
		/*
		 * Deux Chaines d'Attaques sont considérées égales si chacune de leurs attaques de rang égal sont égales
		 */
		if (aObj != null && aObj instanceof ChaineAttaquesV)
		{
			ChaineAttaquesV chaineTmp = (ChaineAttaquesV) aObj;
			if (chaineTmp.size() == this.size())
			{
				boolean egales = true;
				for (int i = 0; i < chaine.size(); i++)
				{
					egales &= this.chaine.get(i).equals(chaineTmp.chaine.get(i));
				}
				
				if (egales)
				{
					return true;
				}
			}
		}
		return super.equals(aObj);
	}
	
	
	private class ScenarioVComparator implements Comparator<ScenarioV>
	{
		@Override
		public int compare(ScenarioV aArg0, ScenarioV aArg1)
		{
			return aArg1.getProbaRealisation().compareTo(aArg0.getProbaRealisation());
		}
		
	}
}
