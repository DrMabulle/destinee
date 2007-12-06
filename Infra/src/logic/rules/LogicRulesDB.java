package logic.rules;

import java.util.List;

/**
 * @author Benoit Kessler
 * 
 */
public interface LogicRulesDB
{
	/**
	 * @return une liste de r�gles Prolog, d�pendant des besoins
	 */
	public List<String> getRulesList();

}