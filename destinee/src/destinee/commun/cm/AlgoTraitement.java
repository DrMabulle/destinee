/**
 * 
 */
package destinee.commun.cm;

import destinee.core.exception.DestineeException;

/**
 * @author Bubulle
 * 
 */
public interface AlgoTraitement
{
	/**
	 * Exécute le traitement, en fonction de l'algorithme choisi
	 * 
	 * @throws DestineeException DestineeException
	 */
	public void executerTraitement() throws DestineeException;
}
