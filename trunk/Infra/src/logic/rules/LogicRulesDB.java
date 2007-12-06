package logic.rules;

import java.util.List;

/**
 * @author Benoit Kessler
 * 
 */
public interface LogicRulesDB
{
	/**
	 * @return une liste de règles Prolog, dépendant des besoins
	 */
	public List<String> getRulesList();

}