/**
 * 
 */
package destinee.core.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Classe utilitaire offrant des méthodes de conversion d'objet à objet.
 */
public class ConversionUtil
{
	/**
	 * Conversion d'un bigDecimal en String, avec formatage correct
	 * 
	 * @param decimal le BigDecimal à convertir
	 * @return le BigDecimal formaté
	 */
	public static String bigDecimalVersString(final BigDecimal decimal)
	{
		return bigDecimalVersString(decimal, 2);
	}

	/**
	 * Conversion d'un bigDecimal en String, avec formatage correct
	 * 
	 * @param decimal le BigDecimal à convertir
	 * @return le BigDecimal formaté
	 */
	public static String bigDecimalVersString(final BigDecimal decimal, final int scale)
	{
		String str = "";
		int idxVirgule = -1;
		char[] buf = null;
		StringBuilder sb = null;

		if (decimal != null)
		{
			// On distingue 2 cas :
			// - decimal est très petit (proche de 0)
			// - decimal n'est pas très petit (cas 'normal')
			BigDecimal abs = decimal.abs();
			BigDecimal zero = BigDecimal.ZERO;
			BigDecimal un = BigDecimal.ONE;

			if (zero.equals(zero.min(abs)) && un.equals(un.max(abs)))
			{
				// decimal est compris entre -1 et 1
				// Représentation non scientifique : pas de puissance de 10
				str = decimal.toPlainString();

				// , au lieu de .
				str = str.replaceAll("\\.", ",");
				if (str.length() >= scale + 2)
				{
					str = str.substring(0, scale + 2);
				}
			}
			else
			{
				// Cas 'normal'
				// Arrondi à 2 chiffres après la virgule
				str = decimal.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
				// , au lieu de .
				str = str.replaceAll("\\.", ",");

				// Ajout des espaces tous les 3 chiffres
				idxVirgule = str.indexOf(',');
				buf = new char[idxVirgule];
				str.getChars(0, idxVirgule, buf, 0);
				sb = new StringBuilder(32);

				for (int n = 0; n < idxVirgule; n++)
				{
					int reste = idxVirgule - n;

					// Le reste est multiple de 3 et ce n'est pas ni le premier
					// ni le dernier caractère
					if (reste % 3 == 0 && n > 0 && reste > 0)
					{
						sb.append(' ');
					}

					sb.append(str.charAt(n));
				}

				sb.append(str.substring(idxVirgule));

				str = sb.toString();
			}

		}

		return str;
	}

	/**
	 * Convertit une Date en chaîne de caractère au format spécifié par pModele
	 * 
	 * @param pDate Date à convertir
	 * @param pModele chaîne spécifiant le format de la date à convertir (ex: "dd.MM.yyyy")
	 * @return String chaîne formatée au format spécifié par pModele
	 */
	public static String dateVersString(final Date pDate, final String pModele)
	{
		DateFormat dateFormat = new SimpleDateFormat(pModele);
		String resultat = "";

		if (pDate != null)
		{
			resultat = dateFormat.format(pDate);
		}

		return resultat;
	}

	/**
	 * Convertit une chaîne de caractères en un Float.
	 * 
	 * @param pChaine Chaîne à convertir
	 * @return Float correspondant à la chaîne ou bien Float(0.0f) si la chaîne est vide
	 */
	public static BigDecimal stringVersBigDecimal(final String pChaine)
	{
		return stringVersBigDecimal(pChaine, null);
	}

	/**
	 * Convertit une chaîne de caractères en un Float.
	 * 
	 * @param pChaine Chaîne à convertir
	 * @return Float correspondant à la chaîne ou bien Float(0.0f) si la chaîne est vide
	 */
	public static BigDecimal stringVersBigDecimal(final String pChaine, final BigDecimal defaultValue)
	{

		BigDecimal retour = defaultValue;

		if (pChaine != null)
		{

			String chaineSansEspaces = pChaine.replaceAll("&nbsp;", "");

			if (!"".equals(chaineSansEspaces))
			{
				try
				{
					String chaineAvecPoint = chaineSansEspaces.replace(',', '.');
					retour = new BigDecimal(chaineAvecPoint.replaceAll("\\ ", ""));
				}
				catch (NumberFormatException nfe)
				{
					retour = null;
				}
			}
		}

		return retour;
	}

	/**
	 * Convertit une chaîne au format spécifié par pModele en Date
	 * 
	 * @param pChaine chaîne à convertir en timestamp
	 * @param pModele chaîne spécifiant le format de la date utilisée (ex: dd.MM.yyyy)
	 * @return Date date correspondant à la chaîne
	 */
	public static Date stringVersDate(final String pChaine, final String pModele)
	{
		Date resultat = null;

		if (pChaine != null)
		{
			try
			{
				DateFormat dateFormat = new SimpleDateFormat(pModele);
				resultat = dateFormat.parse(pChaine);
			}
			catch (Exception e)
			{
				resultat = null;
			}
		}

		return resultat;
	}

	/**
	 * Convertit une Date en chaîne de caractère au format "JJ/MM/AAAA hh:mm:ss"
	 * 
	 * @param pDate Date à convertir
	 * @return String chaîne formatée au format "JJ/MM/AAAA hh:mm:ss"
	 */
	public static String dateHeureVersString(final Date pDate)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String resultat = "";

		if (pDate != null)
		{
			resultat = dateFormat.format(pDate);
		}

		return resultat;
	}

	/**
	 * Convertit une Date en chaîne de caractère au format "JJ/MM/AAAA hh:mm"
	 * 
	 * @param pDate Date à convertir
	 * @return String chaîne formatée au format "JJ/MM/AAAA hh:mm"
	 */
	public static String dateHeureVersStringSansSecondes(final Date pDate)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		String resultat = "";
		int heure = 0;
		int minute = 0;

		if (pDate != null)
		{

			Calendar gc = new GregorianCalendar();
			gc.setTime(pDate);

			heure = gc.get(Calendar.HOUR_OF_DAY);
			minute = gc.get(Calendar.MINUTE);

			if (heure == 0 && minute == 0)
			{
				resultat = dateFormat2.format(pDate);
			}
			else
			{
				resultat = dateFormat.format(pDate);
			}
		}

		return resultat;
	}

	/**
	 * Convertit une Date en chaîne de caractère au format défini dans beanUtils.date.pattern
	 * 
	 * @param pDate Date à convertir
	 * @return String chaîne formatée au format défini dans beanUtils.date.pattern
	 */
	public static String dateVersString(final Date pDate)
	{
		String pattern;
		String resultat = "";

		pattern = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		if (pDate != null)
		{
			resultat = dateFormat.format(pDate);
		}

		return resultat;
	}

	/**
	 * Convertit un Float en Integer, en multipliant au préalable le multiplicateur (10.0f , 100.0f ...)
	 * 
	 * @param pFloat flottant à convertir
	 * @param pMultiplicateur le multiplicateur
	 * @return Integer correpondant au flottant multiplié par 10
	 * @throws Exception en cas d'erreur
	 */
	public static Integer floatVersInteger(final Float pFloat, final float pMultiplicateur) throws Exception
	{

		Integer retour = null;

		try
		{
			if (pFloat != null)
			{
				retour = Integer.valueOf((int) (pFloat.floatValue() * pMultiplicateur));
			}
		}
		catch (NumberFormatException nfe)
		{
			retour = null;
		}

		return retour;
	}

	/**
	 * Convertit un Float en Long, en multipliant au préalable le flottant par 10.
	 * 
	 * @param pFloat flottant à convertir
	 * @return Long correpondant au flottant multiplié par 10
	 * @throws Exception en cas d'erreur
	 */
	public static Long floatVersLong(final Float pFloat) throws Exception
	{

		Long retour = null;

		try
		{
			if (pFloat != null)
			{
				retour = new Long((long) (pFloat.floatValue() * 10.0f));
			}
		}
		catch (NumberFormatException nfe)
		{
			retour = null;
		}

		return retour;
	}

	/**
	 * Méthode de conversion d'un float en String avec gestion de pointeur null et d'exception. Cette méthode permet d'éviter à avoir à tester si un objet est à
	 * null avant de la convertir en String.
	 * 
	 * @param pFloat Le float à convertir en String
	 * @return String correspondant à l'objet
	 * @throws Exception en cas d'erreur
	 */
	public static String floatVersString(final Object pFloat) throws Exception
	{

		String resultat = null;

		if (pFloat != null)
		{
			resultat = pFloat.toString();
			resultat = resultat.replace('.', ',');
		}

		return resultat;
	}

	/**
	 * Convertit un Integer en Float, en divisant le diviseur (10.0f , 100.0f , ...).
	 * 
	 * @param pEntier entier à convertir
	 * @param pDiviseur le diviseur
	 * @return Float correpondant à l'entier divisé par 10
	 */
	public static Float integerVersFloat(final Integer pEntier, final float pDiviseur)
	{

		Float retour = null;

		try
		{
			if (pEntier != null)
			{
				retour = new Float(pEntier.floatValue() / pDiviseur);
			}
		}
		catch (NumberFormatException nbf)
		{
			retour = null;
		}

		return retour;
	}

	/**
	 * Méthode de conversion d'un integer en String avec gestion de pointeur null et d'exception. Cette méthode permet d'éviter à avoir à tester si un objet est
	 * à null avant de la convertir en String.
	 * 
	 * @param pInteger Le integer à convertir en String
	 * @return String correspondant à l'objet
	 */
	public static String integerVersString(final Object pInteger)
	{

		return integerVersString(pInteger, null);
	}

	/**
	 * Méthode de conversion d'un integer en String avec gestion de pointeur null et d'exception. Cette méthode permet d'éviter à avoir à tester si un objet est
	 * à null avant de la convertir en String.
	 * 
	 * @param pInteger Le integer à convertir en String
	 * @param valeurDefaut la valeur par défaut
	 * @return String correspondant à l'objet
	 */
	public static String integerVersString(final Object pInteger, final String valeurDefaut)
	{

		String resultat = valeurDefaut;

		if (pInteger != null)
		{
			resultat = pInteger.toString();
		}

		return resultat;
	}

	/**
	 * Convertit un Long en Float, en divisant le nombre par 10.
	 * 
	 * @param pEntier entier à convertir
	 * @return Float correpondant à l'entier divisé par 10
	 */
	public static Float longVersFloat(final Long pEntier)
	{

		Float retour = null;

		try
		{

			if (pEntier != null)
			{
				retour = new Float(pEntier.floatValue() / 10.0f);
			}
		}
		catch (NumberFormatException nbf)
		{
			retour = null;
		}

		return retour;
	}

	/**
	 * Méthode de conversion d'un long en String avec gestion de pointeur null et d'exception. Cette méthode permet d'éviter à avoir à tester si un objet est à
	 * null avant de la convertir en String.
	 * 
	 * @param pLong Le long à convertir en String
	 * @return String correspondant à l'objet
	 * @throws Exception en cas d'erreur
	 */
	public static String longVersString(final Object pLong) throws Exception
	{

		String resultat = null;

		if (pLong != null)
		{
			resultat = pLong.toString();
		}

		return resultat;
	}

	/**
	 * Méthode de conversion d'un objet en String avec gestion de pointeur null et d'exception. Cette méthode permet d'éviter à avoir à tester si un objet est à
	 * null avant de la convertir en String.
	 * 
	 * @param pObjet L'objet à convertir en String
	 * @return String correspondant à l'objet
	 */
	public static String objectVersString(final Object pObjet)
	{
		String resultat = "";

		try
		{
			if (pObjet != null)
			{
				resultat = pObjet.toString();
			}
		}
		catch (Exception e)
		{
			resultat = "";
		}

		return resultat;
	}

	/**
	 * Retourne une valeur décimale sous la forme d'une string. C'est à dire que la valeur sera affichée avec un virgule et non un point
	 * 
	 * @param pValeur la valeur à formater
	 * @param pNbDec Le nombre de chiffre après la virgule. Cela permet uniquement de complèter avec des zeros et non couper ou arrondir
	 * @param pEspace booleen indiquant s'il faut insérer des espaces par paquet de trois chiffres
	 * @return String.
	 */
	public static String pointVersVirgule(final String pValeur, final int pNbDec, final boolean pEspace)
	{

		StringTokenizer st = null;
		String ent = null;
		String dec = null;
		String result = "";

		if (!pValeur.equals(""))
		{

			// Mise en forme du résultat
			st = new StringTokenizer(pValeur, ".");

			if (st.countTokens() >= 1 && st.countTokens() <= 2)
			{
				ent = st.nextToken();

				if (st.hasMoreTokens())
				{
					dec = st.nextToken();
				}

				if (dec == null)
				{
					dec = "";
				}

				// Ajout du zéro à la fin
				if (dec.length() < pNbDec)
				{
					for (int i = dec.length(); i < pNbDec; i++)
					{
						dec += "0";
					}
				}

				// Ajout de blanc par paquet de 3 chiffres
				if (pEspace)
				{
					for (int i = 0; i < ent.length(); i++)
					{
						if ((ent.length() - i) % 3 == 0)
						{
							result += " " + ent.substring(i, i + 3);
						}
					}

					result = ent.substring(0, ent.length() % 3) + result;

					if (result.charAt(0) == ' ')
					{
						result = result.substring(1);
					}

					result += "," + dec;
				}
				else
				{
					result = ent + "," + dec;
				}
			}
		}
		else
		{
			result = "";
		}

		return result;
	}

	/**
	 * Convertit une chaîne au format défini dans beanUtils.date.pattern
	 * 
	 * @param pChaine chaîne à convertir en timestamp
	 * @return Date date correspondant à la chaîne
	 */
	public static Date stringVersDate(final String pChaine)
	{
		Date resultat = null;

		if (pChaine != null)
		{
			try
			{
				String pattern = "dd/MM/yyyy";
				DateFormat dateFormat = new SimpleDateFormat(pattern);
				resultat = dateFormat.parse(pChaine);
			}
			catch (Exception e)
			{
				resultat = null;
			}
		}

		return resultat;
	}

	/**
	 * Convertit une chaîne au format "JJ/MM/AAAA hh:mm:ss" en Date
	 * 
	 * @param pChaine chaîne à convertir en timestamp
	 * @return Date date correspondant à la chaîne
	 */
	public static Date stringVersDateHeure(final String pChaine)
	{
		Date resultat = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		if (pChaine != null)
		{
			try
			{
				resultat = dateFormat.parse(pChaine);
			}
			catch (ParseException e)
			{
				resultat = null;
			}
		}

		return resultat;
	}

	/**
	 * Convertit une chaîne au format "JJ/MM/AAAA hh:mm" en Date
	 * 
	 * @param pChaine chaîne à convertir en timestamp
	 * @return Date date correspondant à la chaîne
	 */
	public static Date stringVersDateHeureSansSecondes(final String pChaine)
	{
		Date resultat = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

		if (pChaine != null)
		{
			try
			{
				resultat = dateFormat.parse(pChaine);
			}
			catch (ParseException e)
			{
				try
				{
					resultat = dateFormat2.parse(pChaine);
				}
				catch (ParseException e2)
				{
					resultat = null;
				}
			}
		}

		return resultat;
	}

	/**
	 * Convertit une chaîne de caractères en un Float.
	 * 
	 * @param pChaine Chaîne à convertir
	 * @return Float correspondant à la chaîne ou bien Float(0.0f) si la chaîne est vide
	 */
	public static Float stringVersFloat(final String pChaine)
	{

		Float retour = null;

		if (pChaine != null && !pChaine.equals(""))
		{
			try
			{
				String chaineAvecPoint = pChaine.replace(',', '.');
				retour = new Float(chaineAvecPoint.replaceAll("\\ ", ""));
			}
			catch (NumberFormatException nfe)
			{
				retour = null;
			}
		}

		return retour;
	}

	/**
	 * Méthode de conversion d'une chaîne en Integer. L'exception levée par Java en cas de chaîne vide ou non convertible en entier est attrapée et null est
	 * renvoyé dans ce cas.
	 * 
	 * @param pString chaîne à convertir
	 * @return Integer correspondant à la chaîne ou null si la chaîne n'est pas un entier
	 */
	public static Integer stringVersInteger(final String pString)
	{
		return stringVersInteger(pString, new Integer(0));
	}

	/**
	 * Méthode de conversion d'une chaîne en Integer. L'exception levée par Java en cas de chaîne vide ou non convertible en entier est attrapée et null est
	 * renvoyé dans ce cas.
	 * 
	 * @param pString chaîne à convertir
	 * @param pDefaut valeur par défaut si la conversion échoue
	 * @return Integer correspondant à la chaîne ou null si la chaîne n'est pas un entier
	 */
	public static Integer stringVersInteger(final String pString, final Integer pDefaut)
	{
		Integer resultat = pDefaut;

		try
		{
			resultat = Integer.valueOf(pString.replaceAll("\\ ", ""));
		}
		catch (Exception e)
		{
			resultat = pDefaut;
		}

		return resultat;
	}

	/**
	 * Méthode de conversion d'une chaîne en Double. L'exception levée par Java en cas de chaîne vide ou non convertible en entier est attrapée et null est
	 * renvoyé dans ce cas.
	 * 
	 * 
	 * 
	 * @param pString chaîne à convertir
	 * @param pDefaut valeur par défaut si la conversion échoue
	 * @return Integer correspondant à la chaîne ou null si la chaîne n'est pas un entier
	 */
	public static Double stringVersDouble(final String pString, final Double pDefaut)
	{
		Double resultat = pDefaut;

		try
		{
			resultat = new Double(pString.replaceAll("\\ ", "").replace(',', '.'));
		}
		catch (Exception e)
		{
			resultat = pDefaut;
		}

		return resultat;
	}

	/**
	 * Méthode de conversion d'une chaîne en Double. L'exception levée par Java en cas de chaîne vide ou non convertible en entier est attrapée et null est
	 * renvoyé dans ce cas.
	 * 
	 * 
	 * 
	 * @param pString chaîne à convertir
	 * @return Integer correspondant à la chaîne ou null si la chaîne n'est pas un entier
	 */
	public static Double stringVersDouble(final String pString)
	{
		Double resultat = null;

		resultat = stringVersDouble(pString, new Double(0.0));

		return resultat;
	}

	/**
	 * Méthode de conversion d'une chaîne en Long. L'exception levée par Java en cas de chaîne vide ou non convertible en entier est attrapée et null est
	 * renvoyé dans ce cas.
	 * 
	 * @param pString chaîne à convertir
	 * @return Integer correspondant à la chaîne ou null si la chaîne n'est pas un entier
	 */
	public static Long stringVersLong(final String pString)
	{
		return stringVersLong(pString, new Long(0));
	}

	/**
	 * Méthode de conversion d'une chaîne en Long. L'exception levée par Java en cas de chaîne vide ou non convertible en entier est attrapée et null est
	 * renvoyé dans ce cas.
	 * 
	 * @param pString chaîne à convertir
	 * @param pDefaut valeur par défaut si la conversion échoue
	 * @return Integer correspondant à la chaîne ou null si la chaîne n'est pas un entier
	 */
	public static Long stringVersLong(final String pString, final Long pDefaut)
	{
		Long resultat = pDefaut;

		try
		{
			resultat = Long.valueOf(pString.replaceAll("\\ ", ""));
		}
		catch (NumberFormatException e)
		{
			resultat = pDefaut;
		}

		return resultat;
	}

	/**
	 * Transforme une chaîne de caractères représentant un décimal - virgule vers point - suppresqion des espace
	 * 
	 * 
	 * @param pChaine Chaîne à convertir
	 * @return String
	 */
	public static String virguleVersPoint(final String pChaine)
	{

		String retour = null;

		if (pChaine != null && !pChaine.equals(""))
		{
			retour = pChaine.replace(',', '.');
			retour = retour.replaceAll("\\ ", "");
		}

		return retour;
	}

	/**
	 * Transforme un Double en BigDecimal
	 * 
	 * @param aDouble un Double
	 * @return le BigDecimal équivalent au Double, null si le paramètre est null
	 */
	public static BigDecimal doubleVersBigDecimal(final Double aDouble)
	{

		String theDoubleValue = "";
		if (aDouble != null)
		{
			theDoubleValue = aDouble.toString();
		}

		BigDecimal theResult = stringVersBigDecimal(theDoubleValue);
		if (theResult != null)
		{
			theResult = theResult.setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		return theResult;
	}

	/**
	 * Transforme un double en BigDecimal
	 * 
	 * @param aDouble un double
	 * @return le BigDecimal équivalent au Double
	 */
	public static BigDecimal doubleVersBigDecimal(final double aDouble)
	{

		String theDoubleValue = String.valueOf(aDouble);

		return stringVersBigDecimal(theDoubleValue).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Transforme un BigDecimal en Double
	 * 
	 * @param aBigDecimal un BigDecimal
	 * @return le Double équivalent au BigDecimal en paramètre, null si le paramètre est null
	 */
	public static Double bigdecimalVersDouble(final BigDecimal aBigDecimal)
	{

		Double dble = null;

		if (aBigDecimal != null)
		{
			dble = bigdecimalVersDouble(aBigDecimal, 2);
		}

		return dble;
	}

	/**
	 * Transforme un BigDecimal en Double
	 * 
	 * @param aBigDecimal un BigDecimal
	 * @param scale scale
	 * @return le Double équivalent au BigDecimal en paramètre, null si le paramètre est null
	 */
	public static Double bigdecimalVersDouble(final BigDecimal aBigDecimal, final int scale)
	{

		Double dble = null;

		if (aBigDecimal != null)
		{
			dble = new Double(aBigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
		}

		return dble;
	}

	/**
	 * Transforme un BigDecimal en Long
	 * 
	 * @param aBigDecimal un BigDecimal
	 * @return le Long équivalent au BigDecimal en paramètre, null si le paramètre est null
	 */
	public static Long bigDecimalVersLong(final BigDecimal aBigDecimal)
	{

		Long lng = null;

		if (aBigDecimal != null)
		{
			lng = new Long(aBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).longValue());
		}

		return lng;
	}

	/**
	 * Conversion d'un Double en String, avec formatage correct
	 * 
	 * @param decimal le Double à convertir
	 * @return le Double formaté
	 */
	public static String doubleVersString(final Double decimal)
	{
		String str = "";
		int idxVirgule = -1;
		char[] buf = null;
		StringBuilder sb = null;

		if (decimal != null)
		{
			// Arrondi à 2 chiffres après la virgule
			str = new BigDecimal("" + decimal.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			// , au lieu de .
			str = str.replaceAll("\\.", ",");

			// Ajout des espaces tous les 3 chiffres
			idxVirgule = str.indexOf(',');
			buf = new char[idxVirgule];
			str.getChars(0, idxVirgule, buf, 0);
			sb = new StringBuilder(24);

			for (int n = 0; n < idxVirgule; n++)
			{
				int reste = idxVirgule - n;

				// Le reste est multiple de 3 et ce n'est pas ni le premier ni
				// le dernier caractère
				if (reste % 3 == 0 && n > 0 && reste > 0)
				{
					sb.append(' ');
				}

				sb.append(str.charAt(n));
			}

			sb.append(str.substring(idxVirgule));

			str = sb.toString();
		}

		return str;
	}

	/**
	 * Conversion d'un Double en String, avec formatage correct et précision ajustable
	 * 
	 * @param decimal le Double à convertir
	 * @param scale précision du double
	 * @return le Double formaté
	 */
	public static String doubleVersString(final Double decimal, final Integer scale)
	{
		String str = "";
		int idxVirgule = -1;
		char[] buf = null;
		StringBuilder sb = null;

		if (decimal != null)
		{
			// Arrondi à 2 chiffres après la virgule
			if (scale == null || scale.intValue() < 0)
			{
				str = new BigDecimal(decimal.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			}
			else
			{
				str = new BigDecimal(decimal.toString()).setScale(scale.intValue(), BigDecimal.ROUND_HALF_UP).toString();
			}
			// , au lieu de .
			str = str.replaceAll("\\.", ",");

			// Ajout des espaces tous les 3 chiffres
			idxVirgule = str.indexOf(',');
			buf = new char[idxVirgule];
			str.getChars(0, idxVirgule, buf, 0);
			sb = new StringBuilder(24);

			for (int n = 0; n < idxVirgule; n++)
			{
				int reste = idxVirgule - n;

				// Le reste est multiple de 3 et ce n'est pas ni le premier ni
				// le dernier caractère
				if (reste % 3 == 0 && n > 0 && reste > 0)
				{
					sb.append(' ');
				}

				sb.append(str.charAt(n));
			}

			sb.append(str.substring(idxVirgule));

			str = sb.toString();
		}

		return str;
	}

	/**
	 * Complète a gauche avec le caractères fourni jusqu'a la taille indiqué
	 * 
	 * @param numero le numéro a complèter
	 * @param taille la taille finale
	 * @param caractere le caractères a ajouter
	 * @return String
	 * @throws InfraTechnicalException l'exception technique
	 */
	public static String completerAGauche(final Integer numero, final int taille, final String caractere)
	{

		String complete = null;

		complete = String.valueOf(numero);

		while (complete.length() < taille)
		{
			complete = caractere + complete;
		}

		return complete;
	}

	/**
	 * Formatage de l'heure en Integer
	 * 
	 * Ex:
	 * 
	 * 00:00:01 => 1 01:00:45 => 10045 00:42:01 => 4201 12:12:12 => 121212
	 * 
	 * @param date la date
	 * @return Integer
	 */
	public static Integer heureVersInteger(final Date date)
	{

		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

		Integer heure = null;
		String string = null;

		string = sdf.format(date);

		try
		{
			heure = Integer.valueOf(string);
		}
		catch (NumberFormatException nfe)
		{
			heure = null;
		}

		return heure;
	}

	/**
	 * Formatage de l'Integer en date
	 * 
	 * Ex:
	 * 
	 * 100 => 00 H 01 10045 => 01 H 00
	 * 
	 * @param aInteger l'heure
	 * @return Integer
	 */
	public static String heureIntegerVersHeureStringH(final Integer aInteger)
	{
		String heure = null;
		StringBuilder sb = null;

		if (aInteger != null)
		{
			heure = aInteger.toString();
			while (heure.length() < 6)
			{
				heure = "0" + heure;
			}
			sb = new StringBuilder(8);
			sb.append(heure.substring(0, 2));
			sb.append('H');
			sb.append(heure.substring(2, 4));
		}

		return sb.toString();
	}

	/**
	 * Formatage de l'Integer en heure
	 * 
	 * Ex:
	 * 
	 * 1 => 00:00:01 10045 => 01:00:45
	 * 
	 * @param heure l'heure
	 * @return Integer
	 */
	public static String integerVersHeure2(final Integer heure)
	{
		String hhmmss = paddingString(integerVersString(heure, "000000"), 6, '0', true);
		StringBuilder str = new StringBuilder(hhmmss);
		str.insert(2, ':');
		str.insert(5, ':');
		return str.toString();
	}

	/**
	 * Formatage de l'Integer en heure
	 * 
	 * Ex:
	 * 
	 * 1 => 0:00 10045 => 1:00 174500 => 17:45
	 * 
	 * @param heure l'heure
	 * @return Integer
	 */
	public static String integerVersHeure3(final Integer heure)
	{

		int heureMinute = 0;

		heureMinute = heure.intValue() / 100;
		String hhmm = paddingString(integerVersString(new Integer(heureMinute), "000"), 3, '0', true);
		StringBuilder str = new StringBuilder(hhmm);
		str.insert(str.length() - 2, ':');
		return str.toString();
	}

	/**
	 * Récupération de l'Integer à partir d'une heure
	 * 
	 * Ex:
	 * 
	 * 00:00:01 => 1 01:00:45 => 10045
	 * 
	 * @param heure l'heure
	 * @return Integer
	 */
	public static Integer heureVersInteger2(final String heure)
	{

		String str = heure.replaceAll(":", "");
		return stringVersInteger(str, new Integer(0));

	}

	/**
	 * pad a string S with a size of N with char C on the left (True) or on the right(flase)
	 * 
	 * @param s the string
	 * @param n the size
	 * @param c the char
	 * @param paddingLeft boolean
	 * @return String
	 */
	private static String paddingString(final String s, final int n, final char c, final boolean paddingLeft)
	{
		StringBuilder str = new StringBuilder(s);
		int strLength = str.length();
		if (n > 0 && n > strLength)
		{
			for (int i = 0; i <= n; i++)
			{
				if (paddingLeft)
				{
					if (i < n - strLength)
					{
						str.insert(0, c);
					}
				}
				else
				{
					if (i > strLength)
					{
						str.append(c);
					}
				}
			}
		}
		return str.toString();
	}

	/**
	 * Convertit un Long en String, en ajoutant le séparateur de miliers
	 * 
	 * @param val le long
	 * @return la valeur correctement formatée
	 */
	public static String longVersStringFormat(final Long val)
	{
		String longFormat = "";

		if (val != null)
		{
			String longSimple = val.toString();
			StringBuilder sbLong = new StringBuilder(24);

			int nbCar = longSimple.length();
			int numCar = 0;
			for (int idx = nbCar - 1; idx >= 0; idx--)
			{
				char car = longSimple.charAt(idx);
				sbLong.append(car);
				numCar++;
				if (numCar % 3 == 0 && idx > 0)
				{
					sbLong.append(' ');
				}
			}

			longFormat = sbLong.reverse().toString();
		}

		return longFormat;
	}

	/**
	 * Permet de générer une liste au format Prolog à partir d'une Collection Java de String
	 * 
	 * @param aCollection une collection
	 * @return une liste au format Prolog
	 */
	public static String generatePrologListFromSringCollection(final Collection<String> aCollection)
	{
		if (aCollection == null || aCollection.size() == 0)
		{
			return "[]";
		}

		StringBuilder theResult = new StringBuilder(156);
		theResult.append("['");

		for (Iterator<String> iter = aCollection.iterator(); iter.hasNext();)
		{
			theResult.append(iter.next());

			if (iter.hasNext())
			{
				theResult.append("','");
			}
		}
		theResult.append("']");

		return theResult.toString();
	}
}
