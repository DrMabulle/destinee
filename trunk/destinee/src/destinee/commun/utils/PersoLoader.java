/**
 * 
 */
package destinee.commun.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import logic.gateways.DestineeToLogicGateway;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Cible;
import destinee.commun.data.Perso;
import destinee.core.exception.TechnicalException;
import destinee.core.utils.ConversionUtil;
import destinee.core.utils.FileAssistant;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;

/**
 * @author Bubulle
 * 
 */
public final class PersoLoader
{
	private static final String CHEMIN = "./properties/";
	private static final String FICHIER = "persos.properties";
	private static final String NB_PERSOS = "nombre.de.persos";
	private static final String PERSO = "perso";
	private static final String CARACS = ".caracs";
	private static final String IDENTIFIANT = ".identifiant";
	private static final String PA_CYCLE1 = ".PA.cycle1";
	private static final String PA_CYCLE2 = ".PA.cycle2";
	private static final String ATT_RAPIDE = ".attaque.rapide";
	private static final String ATT_BRUTALE = ".attaque.brutale";
	private static final String ATT_PRECISE = ".attaque.precise";
	private static final String ATT_BERSERK = ".attaque.berserk";
	private static final String ATT_KAMIKAZE = ".attaque.kamikaze";
	private static final String ATT_IMPARABLE = ".attaque.imparable";
	private static final String ATT_MAGIQUE = ".attaque.magique";
	private static final String SACRIFICE_ATT = ".kamikaze.sacrifice.attaque";
	private static final String SACRIFICE_DEG = ".kamikaze.sacrifice.degat";
	private static final String SACRIFICE_MAX = ".kamikaze.sacrifice.max";

	private static final String CIBLE = "cible.";
	private static final String NB_DES_DEF = "dés.def";
	private static final String NB_DES_DM = "dés.dm";
	private static final String BONUS_DEF = "bonus.def";
	private static final String BONUS_DM = "bonus.dm";
	private static final String ARMURE = "armure";
	private static final String FATIGUE = "fatigue";

	private static Properties props = null;

	/**
	 * Crée une nouvelle instance de PersoLoader
	 */
	private PersoLoader()
	{
		super();
	}

	/**
	 * Méthode permettant de charger les persos définis dans le fichiers persos.properties
	 * 
	 * @throws TechnicalException e
	 */
	public static void chargerPersos() throws TechnicalException
	{
		DestineeToLogicGateway prolog = DestineeToLogicGatewayImpl.getDefaultInstance();

		// Charger les données du fichier
		init();

		String nbPersosTmp = props.getProperty(NB_PERSOS);
		int nbPersos = ConversionUtil.stringVersInteger(nbPersosTmp);

		int pa1, pa2;
		int[] caracs;
		String base, identifiant;
		double rapide, precise, brutale, berserk, kamikaze, magique, imparable;
		Perso perso;
		Map<String, Double> maitrises;

		for (int i = 0; i < nbPersos; i++)
		{
			base = PERSO + (i + 1);
			identifiant = recupererIdentifiant(base);
			caracs = recupererCaracs(base);
			pa1 = recupererNombreDePA(base, 1);
			pa2 = recupererNombreDePA(base, 2);

			berserk = recupererMaitriseAttaque(base, ATT_BERSERK, "berserk");
			brutale = recupererMaitriseAttaque(base, ATT_BRUTALE, "brutale");
			imparable = recupererMaitriseAttaque(base, ATT_IMPARABLE, "imparable");
			kamikaze = recupererMaitriseAttaque(base, ATT_KAMIKAZE, "kamikaze");
			magique = recupererMaitriseAttaque(base, ATT_MAGIQUE, "magique");
			precise = recupererMaitriseAttaque(base, ATT_PRECISE, "précise");
			rapide = recupererMaitriseAttaque(base, ATT_RAPIDE, "rapide");

			maitrises = new HashMap<String, Double>();
			maitrises.put(ConstantesAttaques.ID_ATTAQUE_BERSERK, berserk);
			maitrises.put(ConstantesAttaques.ID_ATTAQUE_BRUTALE, brutale);
			maitrises.put(ConstantesAttaques.ID_ATTAQUE_IMPARABLE, imparable);
			maitrises.put(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE, kamikaze);
			maitrises.put(ConstantesAttaques.ID_ATTAQUE_MAGIQUE, magique);
			maitrises.put(ConstantesAttaques.ID_ATTAQUE_PRECISE, precise);
			maitrises.put(ConstantesAttaques.ID_ATTAQUE_RAPIDE, rapide);

			perso = new Perso(caracs[0], caracs[1], caracs[2], caracs[3], caracs[4], caracs[5], caracs[6], identifiant, maitrises);

			recupererSacrificesPourKamikaze(perso, base);

			perso.setPaCycle1(pa1);
			perso.setPaCycle2(pa2);

			// Ajouter le perso nouvellement créé au cache
			CachePersos.addPerso(identifiant, perso);

			// Déclarer le perso à Prolog
			prolog.ajouterPerso(perso.getIdentifiant(), pa1, pa2);
			declarerAttaquesProlog(prolog, identifiant, maitrises);
		}

		// Charger la cible
		Cible cible = chargerCible();
		CachePersos.setCible(cible);
	}

	/**
	 * @param aBase la première partie de la propriété
	 * @return l'identifiant du perso
	 * @throws TechnicalException e
	 */
	private static String recupererIdentifiant(final String aBase) throws TechnicalException
	{
		String id = props.getProperty(aBase + IDENTIFIANT);
		if (id == null || "".equals(id.trim()))
		{
			throw new TechnicalException("Erreur : l'identifiant du " + aBase + " est mal renseigné.");
		}
		return id;
	}

	/**
	 * @param aBase la première partie de la propriété
	 * @return un tableau d'entiers contenant les caracs du perso
	 * @throws TechnicalException
	 */
	private static int[] recupererCaracs(final String aBase) throws TechnicalException
	{
		int[] caracsPhysiques;
		String caracs = props.getProperty(aBase + CARACS);

		if (caracs == null || "".equals(caracs.trim()))
		{
			throw new TechnicalException("Erreur : les caractéristiques physiques du " + aBase + " ne sont pas renseignées.");
		}

		String[] splitted = caracs.split(",");
		caracsPhysiques = new int[splitted.length];

		try
		{
			for (int i = 0; i < splitted.length; i++)
			{
				caracsPhysiques[i] = ConversionUtil.stringVersInteger(splitted[i].trim(), null).intValue();
			}
		}
		catch (Exception e)
		{
			throw new TechnicalException("Erreur : les caractéristiques physiques du " + aBase
					+ " sont mal renseignées : caractères numériques attendus.");
		}

		if (caracsPhysiques.length != 7)
		{
			throw new TechnicalException("Erreur : les caractéristiques physiques du " + aBase
					+ " sont mal renseignées : caracs manquantes.");
		}

		return caracsPhysiques;
	}

	/**
	 * @param aBase la première partie de la propriété
	 * @param numeroDuCycle cycle 1 ou 2
	 * @return le nombre de PA dans le cycle indiqué
	 * @throws TechnicalException e
	 */
	private static int recupererNombreDePA(final String aBase, final int numeroDuCycle) throws TechnicalException
	{
		int pa;
		try
		{
			if (numeroDuCycle == 1)
			{
				pa = ConversionUtil.stringVersInteger(props.getProperty(aBase + PA_CYCLE1), null).intValue();
			}
			else
			{
				pa = ConversionUtil.stringVersInteger(props.getProperty(aBase + PA_CYCLE2), null).intValue();
			}
		}
		catch (Exception e)
		{
			throw new TechnicalException("Erreur : le nombre de PA pour le cycle " + numeroDuCycle + " et le " + aBase
					+ " est mal renseigné.");
		}
		return pa;
	}

	/**
	 * @param aBase la première partie de la propriété
	 * @param aAttaque la seconde partie de la propriété
	 * @param aNomAttaque le nom de l'attaque récupérée
	 * @return la maitrise de l'attaque en question
	 * @throws TechnicalException e
	 */
	private static double recupererMaitriseAttaque(final String aBase, final String aAttaque, final String aNomAttaque)
			throws TechnicalException
	{
		double result = 0.0;
		String maitrise = props.getProperty(aBase + aAttaque);

		// Cette propriété n'est pas obligatoire.
		if (maitrise != null && !"".equals(maitrise.trim()))
		{
			// Convertir en double
			try
			{
				result = ConversionUtil.stringVersDouble(maitrise, null).doubleValue();
			}
			catch (Exception e)
			{
				throw new TechnicalException("Erreur : la maitrise de l'attaque " + aNomAttaque + " du " + aBase + " est mal renseignée.");
			}
		}

		return result;
	}

	/**
	 * @param aPerso un perso
	 * @param aBase
	 * @throws TechnicalException
	 */
	private static void recupererSacrificesPourKamikaze(final Perso aPerso, final String aBase) throws TechnicalException
	{
		String sacroAttaque = props.getProperty(aBase + SACRIFICE_ATT);
		String sacroDegat = props.getProperty(aBase + SACRIFICE_DEG);
		String sacroMax = props.getProperty(aBase + SACRIFICE_MAX);
		int temp;

		// Sacrifice pour booster l'attaque
		if (sacroAttaque != null && !"".equals(sacroAttaque.trim()))
		{
			// Convertir en int
			try
			{
				temp = ConversionUtil.stringVersInteger(sacroAttaque, null).intValue();
				aPerso.setSacrificePourAttaque(temp);
			}
			catch (Exception e)
			{
				throw new TechnicalException("Erreur : le sacrifice pour l'attaque du " + aBase + " est mal renseigné.");
			}
		}
		// Sacrifice pour booster les dégâts
		if (sacroDegat != null && !"".equals(sacroDegat.trim()))
		{
			// Convertir en int
			try
			{
				temp = ConversionUtil.stringVersInteger(sacroDegat, null).intValue();
				aPerso.setSacrificePourDegat(temp);
			}
			catch (Exception e)
			{
				throw new TechnicalException("Erreur : le sacrifice pour les degats du " + aBase + " est mal renseigné.");
			}
		}
		// Sacrifice maximal autorisé, pour booster attaque et/ou dégâts
		if (sacroMax != null && !"".equals(sacroMax.trim()))
		{
			// Convertir en int
			try
			{
				temp = ConversionUtil.stringVersInteger(sacroMax, null).intValue();
				aPerso.setSacrificeMax(temp);
			}
			catch (Exception e)
			{
				throw new TechnicalException("Erreur : le sacrifice maximal du " + aBase + " est mal renseigné.");
			}
		}
	}

	/**
	 * @param aPrologGateway une gateway Prolog
	 * @param aIdentifiant un identifiant
	 * @param aMaitrises des données relatives à la maitrise des compétences d'attaque
	 */
	private static void declarerAttaquesProlog(final DestineeToLogicGateway aPrologGateway, final String aIdentifiant,
			final Map<String, Double> aMaitrises)
	{
		// Un perso a toujours l'attaque normale
		aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_NORMALE);

		if (aMaitrises.get(ConstantesAttaques.ID_ATTAQUE_BERSERK) > 0.0)
		{
			aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_BERSERK);
		}
		if (aMaitrises.get(ConstantesAttaques.ID_ATTAQUE_BRUTALE) > 0.0)
		{
			aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_BRUTALE);
		}
		if (aMaitrises.get(ConstantesAttaques.ID_ATTAQUE_IMPARABLE) > 0.0)
		{
			aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_IMPARABLE);
		}
		if (aMaitrises.get(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE) > 0.0)
		{
			aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_KAMIKAZE);
		}
		if (aMaitrises.get(ConstantesAttaques.ID_ATTAQUE_MAGIQUE) > 0.0)
		{
			aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_MAGIQUE);
		}
		if (aMaitrises.get(ConstantesAttaques.ID_ATTAQUE_PRECISE) > 0.0)
		{
			aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_PRECISE);
		}
		if (aMaitrises.get(ConstantesAttaques.ID_ATTAQUE_RAPIDE) > 0.0)
		{
			aPrologGateway.ajouterAttaquePerso(aIdentifiant, ConstantesAttaques.ID_ATTAQUE_RAPIDE);
		}
	}

	/**
	 * Initialisation des properties
	 * 
	 * @throws TechnicalException e
	 */
	private static void init() throws TechnicalException
	{
		init(CHEMIN, FICHIER);
	}

	/**
	 * Initialise les données du Properties avec les données d'un fichier
	 * 
	 * @param aPath un dossier
	 * @param aFileName un fichier
	 * @throws TechnicalException e
	 */
	private static void init(final String aPath, final String aFileName) throws TechnicalException
	{
		props = new Properties();
		try
		{
			props.load(FileAssistant.getInputStream(aPath, aFileName));
		}
		catch (FileNotFoundException e)
		{
			throw new TechnicalException("Fichier des proprietés non trouvé", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException("Lecture du fichier impossible", e);
		}
	}

	/**
	 * Crée et initialise la cible
	 * 
	 * @return une cible
	 */
	private static Cible chargerCible() throws TechnicalException
	{
		Cible cible;

		try
		{
			int nbDesDef = Integer.valueOf(props.getProperty(CIBLE + NB_DES_DEF));
			int nbDesDM = Integer.valueOf(props.getProperty(CIBLE + NB_DES_DM));
			int bonusDef = Integer.valueOf(props.getProperty(CIBLE + BONUS_DEF));
			int bonusDM = Integer.valueOf(props.getProperty(CIBLE + BONUS_DM));
			int armure = Integer.valueOf(props.getProperty(CIBLE + ARMURE));
			int fatigue = Integer.valueOf(props.getProperty(CIBLE + FATIGUE, "0"));

			cible = new Cible(nbDesDef, bonusDef, nbDesDM, bonusDM, armure, fatigue);
		}
		catch (NumberFormatException e)
		{
			throw new TechnicalException("Propriétés de la cible mal renseignées.", e);
		}

		return cible;
	}

}
