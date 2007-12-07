package logic.gateways;

import java.util.List;
import java.util.Map;
import java.util.Vector;

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
	public List<Map<String, Vector<String>>> query(String aQuery);

	/**
	 * Permet d'ex�cuter une Query Prolog mais en ne r�cup�rant que le premier r�sultat
	 * 
	 * @param aQuery une Query
	 * @return le premier r�sultat
	 */
	public Map<String, Vector<String>> queryOnce(String aQuery);

	/**
	 * Permet d'ajouter un personnage et ses caract�ristiques
	 * 
	 * @param aNomPerso un nom de personnage
	 * @param aNbDesAtt nombre de d�s d'attaque du personnage
	 * @param aBonusAtt bonus d'attaque du personnage
	 * @param aNbDesDeg nombre de d�s de d�gat du personnage
	 * @param aBonusDeg bonus de d�gat du personnage
	 * @param aNbPaCyc1 nombre de Points d'Action du personnage lors du premier cycle (attaques en cumul)
	 * @param aNbPaCyc2 nombre de Points d'Action du personnage lors du second cycle (attaques en cumul) (0 si pas de cumul)
	 */
	public void ajouterPerso(String aNomPerso, int aNbDesAtt, int aBonusAtt, int aNbDesDeg, int aBonusDeg, int aNbPaCyc1, int aNbPaCyc2);

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
