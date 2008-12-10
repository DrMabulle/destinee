/**
 * 
 */
package destinee.logic.rules;

import java.util.ArrayList;
import java.util.List;

import logic.rules.LogicRulesDB;
import destinee.commun.constantes.ConstantesAttaques;

/**
 * @author Benoit Kessler
 * 
 */
public class LogicRulesDBImpl implements LogicRulesDB
{
	private List<String> rulesList;

	private static LogicRulesDB DEFAULT = new LogicRulesDBImpl();

	private LogicRulesDBImpl()
	{
		super();
		rulesList = new ArrayList<String>();
		initialiseRules();
	}

	public static LogicRulesDB getDefaultInstance()
	{
		if (DEFAULT == null)
		{
			DEFAULT = new LogicRulesDBImpl();
		}
		return DEFAULT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see reflex.core.build.logic.LogicRulesDB#getRulesList()
	 */
	public List<String> getRulesList()
	{
		return rulesList;
	}

	private void initialiseRules()
	{
		StringBuilder rule = new StringBuilder(2048);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Méthodes utilitaires\n");
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("% Le prédicat concat/3\n");
		rule.append("concat([],X,X).\n");
		rule.append("concat([X|Y],Z,[X|W]):-\n");
		rule.append("    concat(Y,Z,W).");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Méthodes propres à DESTINEE\n");
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Méthode permettant d'ajouter les types de résolution aux différentes attaques\n");
		rule.append("ajouterResolution([], []).\n");
		rule.append("ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup critique') | Tail2]) :-\n");
		rule.append("	ajouterResolution(Tail, Tail2).\n");
		rule.append("ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup simple') | Tail2]) :-\n");
		rule.append("	ajouterResolution(Tail, Tail2).\n");
		rule.append("ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive simple') | Tail2]) :-\n");
		rule.append("	ajouterResolution(Tail, Tail2).\n");
		rule.append("ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive parfaite') | Tail2]) :-\n");
		rule.append("	ajouterResolution(Tail, Tail2).\n");
		rule.append("ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Echec competence') | Tail2]) :-\n");
		rule.append("	ajouterResolution(Tail, Tail2).");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("generationScenarios(Result) :-\n");
		rule.append("	generationListeAttaques(Tmp),\n");
		rule.append("	ajouterResolution(Tmp, Result).");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("generationChainesAttaques(Result) :-\n");
		rule.append("	generationListeAttaques(Result).");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	\n");
		rule.append("% Méthode permettant de creer une première liste d'attaques (normales) en fonction des persos et de leurs PA\n");
		rule.append("generationListeAttaques(L) :- genererListeAtt(L, []).\n");
		rule.append("genererListeAtt([], Atts) :-\n");
		rule.append("	not(getNextAttaque(Atts, _)).\n");
		rule.append("genererListeAtt(Retour, Atts) :-\n");
		rule.append("	getNextAttaque(Atts, Attaque),\n");
		rule.append("	concat(Atts, [Attaque], Atts2),\n");
		rule.append("	genererListeAtt(Retour2, Atts2),\n");
		rule.append("	concat([Attaque], Retour2, Retour).");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Methode permettant de récupérer la prochaine attaque possible\n");
		rule.append("getNextAttaque(Atts, Attaque) :-\n");
		rule.append("	peutAttaquerBerserk(Perso, Atts),\n");
		rule.append("	attaque(Perso, '").append(ConstantesAttaques.ID_ATTAQUE_BERSERK).append("'),\n");
		rule.append("	Attaque = att(Perso, '").append(ConstantesAttaques.ID_ATTAQUE_BERSERK).append("').\n");
		rule.append("getNextAttaque(Atts, Attaque) :-\n");
		rule.append("	peutAttaquerKamikaze(Perso, Atts),\n");
		rule.append("	attaque(Perso, '").append(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE).append("'),\n");
		rule.append("	Attaque = att(Perso, '").append(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE).append("').\n");
		rule.append("getNextAttaque(Atts, Attaque) :-\n");
		rule.append("	peutAttaquer(Perso, Atts),\n");
		rule.append("	attaque(Perso, Type),\n");
		rule.append("	Type \\== '").append(ConstantesAttaques.ID_ATTAQUE_RAPIDE).append("',\n");
		rule.append("	Type \\== '").append(ConstantesAttaques.ID_ATTAQUE_BERSERK).append("',\n");
		rule.append("	Type \\== '").append(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE).append("',\n");
		rule.append("	Type \\== '").append(ConstantesAttaques.ID_ATTAQUE_CHARGE).append("',\n");
		rule.append("	Attaque = att(Perso, Type).\n");
		rule.append("getNextAttaque(Atts, Attaque) :-\n");
		rule.append("	peutAttaquerRapide(Perso, Atts),\n");
		rule.append("	attaque(Perso, '").append(ConstantesAttaques.ID_ATTAQUE_RAPIDE).append("'),\n");
		rule.append("	Attaque = att(Perso, '").append(ConstantesAttaques.ID_ATTAQUE_RAPIDE).append("').");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Méthode permettant de trouver une personne qui peut encore attaquer (possède assez de PA)\n");
		rule.append("peutAttaquer(Perso, Atts) :-\n");
		rule.append("	paRestants(Perso, Atts, PARestants),\n");
		rule.append("	PARestants >= 4.\n");
		rule.append("peutAttaquerKamikaze(Perso, Atts) :-\n");
		rule.append("	paRestants(Perso, Atts, PARestants),\n");
		rule.append("	PARestants >= 6.\n");
		rule.append("peutAttaquerCharge(Perso, Atts) :-\n");
		rule.append("	paRestants(Perso, Atts, PARestants),\n");
		rule.append("	PARestants >= 6.\n");
		rule.append("peutAttaquerBerserk(Perso, Atts) :-\n");
		rule.append("	paRestants(Perso, Atts, PARestants),\n");
		rule.append("	PARestants >= 8.\n");
		rule.append("peutAttaquerRapide(Perso, Atts) :-\n");
		rule.append("	paRestants(Perso, Atts, PARestants),\n");
		rule.append("	PARestants >= 2.");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Methode permettant de savoir combien de PA restent à un perso\n");
		rule.append("paRestants(Perso, Atts, PARestants) :-\n");
		rule.append("	perso(Perso, PA, _),\n");
		rule.append("	paUtilises(Perso, Atts, PaUtilises),\n");
		rule.append("	PARestants is PA - PaUtilises.");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Methode permettant de savoir combien de PA ont été utilisés par un perso\n");
		rule.append("paUtilises(_, [], 0).\n");
		rule.append("paUtilises(Perso1, [att(Perso2, Type) | Suite], PaUtilises) :-\n");
		rule.append("	Perso1 == Perso2,\n");
		rule.append("	coutPA(Type, PA),\n");
		rule.append("	paUtilises(Perso1, Suite, PaUtilisesSuite),\n");
		rule.append("	PaUtilises is PaUtilisesSuite + PA.\n");
		rule.append("paUtilises(Perso1, [att(Perso2, _) | Suite], PaUtilises) :-\n");
		rule.append("	Perso1 \\== Perso2,\n");
		rule.append("	paUtilises(Perso1, Suite, PaUtilisesSuite),\n");
		rule.append("	PaUtilises is PaUtilisesSuite.");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Méthode permettant de creer l'ordre d'attaque en fonction des persos et de leurs PA\n");
		rule.append("generationListeAttaquants(L) :- genererOrdre(L, [], []).\n");
		rule.append("genererOrdre([], Atts, _) :-\n");
		rule.append("	not(getNextAttaquant(Atts, _)).\n");
		rule.append("genererOrdre(Retour, Atts, Ordre) :-\n");
		rule.append("	getNextAttaquant(Atts, Attaquant),\n");
		rule.append("	concat(Atts, [att(Attaquant, '").append(ConstantesAttaques.ID_ATTAQUE_NORMALE).append("')], Atts2),\n");
		rule.append("	concat(Ordre, [Attaquant], Ordre2),\n");
		rule.append("	genererOrdre(Retour2, Atts2, Ordre2),\n");
		rule.append("	concat([Attaquant], Retour2, Retour).");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Methode permettant de récupérer le prochain attaquant possible\n");
		rule.append("getNextAttaquant(Atts, Attaquant) :-\n");
		rule.append("	peutAttaquer(Attaquant, Atts).");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		rule.append("% Données sur les attaques\n");
		rule.append("% caracAttaque(typeAttaque, att, bonAtt, deg, bonDeg, resultAtt, resultBonAtt, resultDeg, resultBonDeg).\n");
		rule.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		rulesList.add(rule.toString());

		rule.setLength(0);
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_BERSERK).append("', 8).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_BRUTALE).append("', 4).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_CHARGE).append("', 6).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_IMPARABLE).append("', 4).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_KAMIKAZE).append("', 6).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_MAGIQUE).append("', 4).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_NORMALE).append("', 4).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_PRECISE).append("', 4).\n");
		rule.append("coutPA('").append(ConstantesAttaques.ID_ATTAQUE_RAPIDE).append("', 2).");
		rulesList.add(rule.toString());
	}

}
