/**
 * 
 */
package destinee.logic.rules;

import java.util.ArrayList;
import java.util.List;

import destinee.constantes.ConstantesAttaques;

import logic.rules.LogicRulesDB;


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
            DEFAULT = new LogicRulesDBImpl();
        return DEFAULT;
    }
    
    /* (non-Javadoc)
     * @see reflex.core.build.logic.LogicRulesDB#getRulesList()
     */
    public List<String> getRulesList()
    {
        return rulesList;
    }
    
    private void initialiseRules() 
    {
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthodes utilitaires\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        
        rulesList.add("% Le prédicat concat/3\n" +
        		"concat([],X,X).\n" +
        		"concat([X|Y],Z,[X|W]):-\n" +
        		"    concat(Y,Z,W).");
        
        rulesList.add("% Le prédicat member/2\n" +
        		"member(X,[X|_]).\n" +
        		"member(X,[_|Xs]) :- member(X,Xs).");
        
        rulesList.add("% Le prédicat insere/3\n" +
        		"insere(X, [], [X]).\n" +
        		"insere(X, [Y|Z], L) :- concat([X], [Y|Z], L).\n" +
        		"insere(X, [Y|Z], L) :- insere(X,Z,M), concat([Y], M, L).");
        
        rulesList.add("% Le prédicat insereAtt/3\n" +
        		"insereAtt(att(Perso,Type,Resolution), [], [att(Perso,Type,Resolution)]).\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
        		"	Perso1 \\== Perso2,\n" +
        		"	Type1 \\== Type2,\n" +
        		"	Resolution1 \\== Resolution2.\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
        		"	Perso1 == Perso2,\n" +
        		"	Type1 \\== Type2,\n" +
        		"	Resolution1 \\== Resolution2.\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
        		"	Perso1 \\== Perso2,\n" +
        		"	Type1 == Type2,\n" +
        		"	Resolution1 \\== Resolution2.\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
        		"	Perso1 == Perso2,\n" +
        		"	Type1 == Type2,\n" +
        		"	Resolution1 \\== Resolution2.\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
        		"	Perso1 \\== Perso2,\n" +
        		"	Type1 \\== Type2,\n" +
        		"	Resolution1 == Resolution2.\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
        		"	Perso1 == Perso2,\n" +
        		"	Type1 \\== Type2,\n" +
        		"	Resolution1 == Resolution2.\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
        		"	Perso1 \\== Perso2,\n" +
        		"	Type1 == Type2,\n" +
        		"	Resolution1 == Resolution2.\n" +
        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
        		"	insere(att(Perso1,Type1,Resolution1), Tail, Tmp),\n" +
        		"	concat([att(Perso2,Type2,Resolution2)], Tmp, Result).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthodes propres à DESTINEE\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthode permettant de générer une liste de scénarios à partir d'une liste d'attaques normales initiale\n" +
        		"genererScenarios(ListeAttaques, ListeScenarios) :-\n" +
        		"	ajouterResolution(ListeAttaques, Tmp),\n" +
        		"	changeAtt(Tmp, ListeScenarios).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthode permettant de remplacer une attaque à N PA par une autre à N PA\n" +
        		"% changeAtt(ListAtts, NewListAtt).\n" +
        		"changeAtt([], []).\n" +
        		"changeAtt([att(Nom, Type, Resolution) | SuiteAtts], L) :-\n" +
        		"	changeAtt(SuiteAtts, Tmp),\n" +
        		"	insereAtt(att(Nom, Type, Resolution), Tmp, L).\n" +
        		"changeAtt([att(Nom, Type, Resolution) | SuiteAtts], L) :-\n" +
        		"	attaque(Nom, Type2),\n" +
        		"	Type \\== Type2,\n" +
        		"	Type2 \\== '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "',\n" +
        		"	Type2 \\== '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "',\n" +
        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "',\n" +
        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "',\n" +
        		"	changeAtt(SuiteAtts, Tmp),\n" +
        		"	insereAtt(att(Nom, Type2, Resolution), Tmp, L).\n" +
        		"% Changer une attaque normale en deux rapides\n" +
        		"changeAtt([att(Nom, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', Resolution) | SuiteAtts], L) :-\n" +
        		"	changeAtt(SuiteAtts, Tmp),\n" +
        		"	attaque(Nom, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "'),\n" +
        		"	concat([att(Nom, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "', Resolution), att(Nom, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "', Resolution)], Tmp, L).\n" +
        		"% Changer deux normales de même résolution et du même perso en une bersek\n" +
        		"changeAtt([att(Nom1, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', Resolution1), att(Nom2, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', Resolution2) | SuiteAtts], L) :-\n" +
        		"	Nom1 == Nom2,\n" +
        		"	Resolution1 == Resolution2,\n" +
        		"	attaque(Nom1, '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "'),\n" +
        		"	changeAtt(SuiteAtts, Tmp),\n" +
        		"	insereAtt(att(Nom1, '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "', Resolution1), Tmp, L).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
    			"% Méthode permettant d'ajouter les types de résolution aux différentes attaques\n" +
    			"ajouterResolution([], []).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup critique') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup simple') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive simple') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive parfaite') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	\n" +
        		"% Méthode permettant de creer une première liste d'attaques (normales) en fonction des persos et de leurs PA\n" +
        		"generationListeAttaques(L) :- genererListeAtt(L, []).\n" +
        		"genererListeAtt([], Atts) :-\n" +
        		"	not(getNextAttaque(Atts, _)).\n" +
        		"genererListeAtt(Retour, Atts) :-\n" +
        		"	getNextAttaque(Atts, Attaque),\n" +
        		"	concat(Atts, [Attaque], Atts2),\n" +
        		"	genererListeAtt(Retour2, Atts2),\n" +
        		"	concat([Attaque], Retour2, Retour).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Methode permettant de récupérer la prochaine attaque possible\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	peutAttaquer(Perso, Atts),\n" +
        		"	Attaque = att(Perso, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "').\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	not(peutAttaquer(Perso, Atts)),\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 2,\n" +
        		"	attaque(Perso, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "'),\n" +
        		"	Attaque = att(Perso, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "').");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthode permettant de trouver une personne qui peut encore attaquer (possède assez de PA)\n" +
        		"peutAttaquer(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 4.");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Methode permettant de savoir combien de PA restent à un perso\n" +
        		"paRestants(Perso, Atts, PARestants) :-\n" +
        		"	perso(Perso, _, _, _, _, PA, _),\n" +
        		"	paUtilises(Perso, Atts, PaUtilises),\n" +
        		"	PARestants is PA - PaUtilises.");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Methode permettant de savoir combien de PA ont été utilisés par un perso\n" +
        		"paUtilises(_, [], 0).\n" +
        		"paUtilises(Perso1, [att(Perso2, Type) | Suite], PaUtilises) :-\n" +
        		"	Perso1 == Perso2,\n" +
        		"	coutPA(Type, PA),\n" +
        		"	paUtilises(Perso1, Suite, PaUtilisesSuite),\n" +
        		"	PaUtilises is PaUtilisesSuite + PA.\n" +
        		"paUtilises(Perso1, [att(Perso2, _) | Suite], PaUtilises) :-\n" +
        		"	Perso1 \\== Perso2,\n" +
        		"	paUtilises(Perso1, Suite, PaUtilisesSuite),\n" +
        		"	PaUtilises is PaUtilisesSuite.");

        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Données sur les attaques\n" +
        		"% caracAttaque(typeAttaque, att, bonAtt, deg, bonDeg, resultAtt, resultBonAtt, resultDeg, resultBonDeg).\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        rulesList.add("coutPA('" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_BRUTALE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_PRECISE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "', 8).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "', 2).");
    }

}
