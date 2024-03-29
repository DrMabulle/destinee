package logic.gateways;

import java.util.List;
import java.util.Map;

/**
 * @author Bubulle et No-one
 * 
 */
public interface DestineeToLogicGateway
{
	/**
	 * Permet d'ex�cuter une Query Prolog
	 * 
	 * @param aQuery une Query
	 * @return la liste des r�sultats
	 */
	public List<Map<String, List<String>>> query(String aQuery);

	/**
	 * Permet d'ex�cuter une Query Prolog mais en ne r�cup�rant que le premier r�sultat
	 * 
	 * @param aQuery une Query
	 * @return le premier r�sultat
	 */
	public Map<String, List<String>> queryOnce(String aQuery);

	/**
	 * Permet de r�cup�rer le r�sultat suivant apr�s un premier appel � queryOnce()
	 * 
	 * @return le r�sultat suivant
	 */
	public Map<String, List<String>> next();

	/**
	 * Permet de fermer la Query une fois les r�sultats voulus obtenus. A appeler apr�s l'utilisation d'un queryOnce
	 */
	public void stop();

	/**
	 * Permet d'ajouter un personnage et ses caract�ristiques
	 * 
	 * @param aNomPerso un nom de personnage
	 * @param aNbPaCyc1 nombre de Points d'Action du personnage lors du premier cycle (attaques en cumul)
	 * @param aNbPaCyc2 nombre de Points d'Action du personnage lors du second cycle (attaques en cumul) (0 si pas de cumul)
	 */
	public void ajouterPerso(String aNomPerso, int aNbPaCyc1, int aNbPaCyc2);

	/**
	 * Permet de renseigner les diff�rentes attaques connues par le personnage. Exemples : Attaque brtuale, Attaque pr�cise, etc.
	 * 
	 * @param aNomPerso un nom de personnage
	 * @param aNomAttaque un type d'attaque
	 */
	public void ajouterAttaquePerso(String aNomPerso, String aNomAttaque);

	/**
	 * Permet de r�initialiser les faits Prolog pr�c�demment ajout�s
	 */
	public void flush();

	/**
	 * Dispose the gateway.
	 */
	public void reinitialize();

}
