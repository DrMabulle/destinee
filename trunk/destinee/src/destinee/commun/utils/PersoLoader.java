/**
 * 
 */
package destinee.commun.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import logic.gateways.DestineeToLogicGateway;
import destinee.commun.constantes.ConstantesAttaques;
import destinee.commun.data.Perso;
import destinee.core.exception.TechnicalException;
import destinee.core.utils.ConversionUtil;
import destinee.logic.gateways.DestineeToLogicGatewayImpl;

/**
 * @author Bubulle
 * 
 */
public class PersoLoader
{
	private static final String CHEMIN = "./properties/destinee/properties/persos.properties";
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
	private static Properties props = null;

	/**
	 * M�thode permettant de charger les persos d�finis dans le fichiers persos.properties
	 * 
	 * @throws TechnicalException e
	 */
	public static void chargerPersos() throws TechnicalException
	{
		DestineeToLogicGateway prolog = DestineeToLogicGatewayImpl.getDefaultInstance();

		// Charger les donn�es du fichier
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
			precise = recupererMaitriseAttaque(base, ATT_PRECISE, "pr�cise");
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

			// Ajouter le perso nouvellement cr�� au cache
			CachePersos.getInstance().addPerso(identifiant, perso);

			// D�clarer le perso � Prolog
			prolog.ajouterPerso(perso.getIdentifiant(), perso.getNombreDeDesAttaque(), perso.getBonusAttaque(), perso.getNombreDeDesDegats(), perso
					.getBonusDegats(), pa1, pa2);
			declarerAttaquesProlog(prolog, identifiant, maitrises);
		}
	}

	/**
	 * @param aBase la premi�re partie de la propri�t�
	 * @return l'identifiant du perso
	 * @throws TechnicalException e
	 */
	private static String recupererIdentifiant(String aBase) throws TechnicalException
	{
		String id = props.getProperty(aBase + IDENTIFIANT);
		if (id == null || "".equals(id.trim()))
		{
			throw new TechnicalException("Erreur : l'identifiant du " + aBase + " est mal renseign�.");
		}
		return id;
	}

	/**
	 * @param aBase la premi�re partie de la propri�t�
	 * @return un tableau d'entiers contenant les caracs du perso
	 * @throws TechnicalException
	 */
	private static int[] recupererCaracs(String aBase) throws TechnicalException
	{
		int[] caracsPhysiques;
		String caracs = props.getProperty(aBase + CARACS);

		if (caracs == null || "".equals(caracs.trim()))
		{
			throw new TechnicalException("Erreur : les caract�ristiques physiques du " + aBase + " ne sont pas renseign�es.");
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
			throw new TechnicalException("Erreur : les caract�ristiques physiques du " + aBase + " sont mal renseign�es : caract�res num�riques attendus.");
		}

		if (caracsPhysiques.length != 7)
		{
			throw new TechnicalException("Erreur : les caract�ristiques physiques du " + aBase + " sont mal renseign�es : caracs manquantes.");
		}

		return caracsPhysiques;
	}

	/**
	 * @param aBase la premi�re partie de la propri�t�
	 * @param numeroDuCycle cycle 1 ou 2
	 * @return le nombre de PA dans le cycle indiqu�
	 * @throws TechnicalException e
	 */
	private static int recupererNombreDePA(String aBase, int numeroDuCycle) throws TechnicalException
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
			throw new TechnicalException("Erreur : le nombre de PA pour le cycle " + numeroDuCycle + " et le " + aBase + " est mal renseign�.");
		}
		return pa;
	}

	/**
	 * @param aBase la premi�re partie de la propri�t�
	 * @param aAttaque la seconde partie de la propri�t�
	 * @param aNomAttaque le nom de l'attaque r�cup�r�e
	 * @return la maitrise de l'attaque en question
	 * @throws TechnicalException e
	 */
	private static double recupererMaitriseAttaque(String aBase, String aAttaque, String aNomAttaque) throws TechnicalException
	{
		double result = 0.0;
		String maitrise = props.getProperty(aBase + aAttaque);

		// Cette propri�t� n'est pas obligatoire.
		if (maitrise != null && !"".equals(maitrise.trim()))
		{
			// Convertir en double
			try
			{
				result = ConversionUtil.stringVersDouble(maitrise, null).doubleValue();
			}
			catch (Exception e)
			{
				throw new TechnicalException("Erreur : la maitrise de l'attaque " + aNomAttaque + " du " + aBase + " est mal renseign�e.");
			}
		}

		return result;
	}

	/**
	 * @param aPrologGateway une gateway Prolog
	 * @param aIdentifiant un identifiant
	 * @param aMaitrises des donn�es relatives � la maitrise des comp�tences d'attaque
	 */
	private static void declarerAttaquesProlog(DestineeToLogicGateway aPrologGateway, String aIdentifiant, Map<String, Double> aMaitrises)
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
		init(CHEMIN);
	}

	/**
	 * Initialise les donn�es du Properties avec les donn�es d'un fichier
	 * 
	 * @param aFileName un fichier
	 * @throws TechnicalException e
	 */
	private static void init(String aFileName) throws TechnicalException
	{
		props = new Properties();
		try
		{
			props.load(new FileInputStream(aFileName));
		}
		catch (FileNotFoundException e)
		{
			throw new TechnicalException("Fichier des propriet�s non trouv�", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException("Lecture du fichier impossible", e);
		}
	}
}
