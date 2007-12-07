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
	 * Permet d'exécuter une Query Prolog
	 * 
	 * @param aQuery une Query
	 * @return la liste des résultats
	 */
	public List<Map<String, Vector<String>>> query(String aQuery);

	/**
	 * Permet d'exécuter une Query Prolog mais en ne récupérant que le premier résultat
	 * 
	 * @param aQuery une Query
	 * @return le premier résultat
	 */
	public Map<String, Vector<String>> queryOnce(String aQuery);

	/**
	 * Permet d'ajouter un personnage et ses caractéristiques
	 * 
	 * @param aNomPerso un nom de personnage
	 * @param aNbDesAtt nombre de dés d'attaque du personnage
	 * @param aBonusAtt bonus d'attaque du personnage
	 * @param aNbDesDeg nombre de dés de dégat du personnage
	 * @param aBonusDeg bonus de dégat du personnage
	 * @param aNbPaCyc1 nombre de Points d'Action du personnage lors du premier cycle (attaques en cumul)
	 * @param aNbPaCyc2 nombre de Points d'Action du personnage lors du second cycle (attaques en cumul) (0 si pas de cumul)
	 */
	public void ajouterPerso(String aNomPerso, int aNbDesAtt, int aBonusAtt, int aNbDesDeg, int aBonusDeg, int aNbPaCyc1, int aNbPaCyc2);

	/**
	 * Permet de renseigner les différentes attaques connues par le personnage. Exemples : Attaque brtuale, Attaque précise, etc.
	 * 
	 * @param aNomPerso un nom de personnage
	 * @param aNomAttaque un type d'attaque
	 */
	public void ajouterAttaquePerso(String aNomPerso, String aNomAttaque);

	/**
	 * Permet de réinitialiser les faits Prolog précédemment ajoutés
	 */
	public void flush();

	/**
	 * Dispose the gateway.
	 */
	public void reinitialize();

}
