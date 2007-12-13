/**
 * 
 */
package destinee.logic.rules;

import java.util.ArrayList;
import java.util.List;

import destinee.commun.constantes.ConstantesAttaques;

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
        		"% M�thodes utilitaires\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        rulesList.add("% Le pr�dicat concat/3\n" +
        		"concat([],X,X).\n" +
        		"concat([X|Y],Z,[X|W]):-\n" +
        		"    concat(Y,Z,W).");
        
//        rulesList.add("% Le pr�dicat member/2\n" +
//        		"member(X,[X|_]).\n" +
//        		"member(X,[_|Xs]) :- member(X,Xs).");
        
//        rulesList.add("% Le pr�dicat insere/3\n" +
//        		"insere(X, [], [X]).\n" +
//        		"insere(X, [Y|Z], L) :- concat([X], [Y|Z], L).\n" +
//        		"insere(X, [Y|Z], L) :- insere(X,Z,M), concat([Y], M, L).");
        
//        rulesList.add("% Le pr�dicat insereAtt/3\n" +
//        		"insereAtt(att(Perso,Type,Resolution), [], [att(Perso,Type,Resolution)]).\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
//        		"	Perso1 \\== Perso2,\n" +
//        		"	Type1 \\== Type2,\n" +
//        		"	Resolution1 \\== Resolution2.\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
//        		"	Perso1 == Perso2,\n" +
//        		"	Type1 \\== Type2,\n" +
//        		"	Resolution1 \\== Resolution2.\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
//        		"	Perso1 \\== Perso2,\n" +
//        		"	Type1 == Type2,\n" +
//        		"	Resolution1 \\== Resolution2.\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
//        		"	Perso1 == Perso2,\n" +
//        		"	Type1 == Type2,\n" +
//        		"	Resolution1 \\== Resolution2.\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
//        		"	Perso1 \\== Perso2,\n" +
//        		"	Type1 \\== Type2,\n" +
//        		"	Resolution1 == Resolution2.\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
//        		"	Perso1 == Perso2,\n" +
//        		"	Type1 \\== Type2,\n" +
//        		"	Resolution1 == Resolution2.\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	concat([att(Perso1,Type1,Resolution1)], [att(Perso2,Type2,Resolution2) | Tail], Result),\n" +
//        		"	Perso1 \\== Perso2,\n" +
//        		"	Type1 == Type2,\n" +
//        		"	Resolution1 == Resolution2.\n" +
//        		"insereAtt(att(Perso1,Type1,Resolution1), [att(Perso2,Type2,Resolution2) | Tail], Result) :-\n" +
//        		"	insere(att(Perso1,Type1,Resolution1), Tail, Tmp),\n" +
//        		"	concat([att(Perso2,Type2,Resolution2)], Tmp, Result).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% M�thodes propres � DESTINEE\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	");
        
//        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//        		"% M�thode permettant de g�n�rer une liste de sc�narios � partir d'une liste d'attaques normales initiale\n" +
//        		"genererScenarios(ListeAttaques, ListeScenarios) :-\n" +
//        		"	ajouterResolution(ListeAttaques, Tmp),\n" +
//        		"	changeAtt(Tmp, ListeScenarios).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
    			"% M�thode permettant d'ajouter les types de r�solution aux diff�rentes attaques\n" +
    			"ajouterResolution([], []).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup critique') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup simple') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive simple') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive parfaite') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).");
        
//        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//        		"% M�thode permettant de remplacer une attaque � N PA par une autre � N PA\n" +
//        		"% changeAtt(ListAtts, NewListAtt).\n" +
//        		"changeAtt([], []).\n" +
//        		"changeAtt(Entree, Result) :-\n" +
//        		"	changerNormaleBerserk(Entree, Result1),\n" +
//        		"	changerNormaleRapide(Result1, Result2),\n" +
//        		"	permutterAttaques(Result2, Result3),\n" +
//        		"	modifierType(Result3, Result).");
        
//        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//        		"% M�thode permettant de permutter les attaques d'une chaine d'attaque\n" +
//        		"permutterAttaques([], []).\n" +
//        		"permutterAttaques([att(Nom, Type, Resolution) | SuiteAtts], Result) :-\n" +
//        		"	permutterAttaques(SuiteAtts, Result1),\n" +
//        		"	insereAtt(att(Nom, Type, Resolution), Result1, Result).");

//        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//        		"% M�thode permettant de modifier le type d'une attaque\n" +
//        		"modifierType([], []).\n" +
//        		"modifierType([att(Nom, Type, Resolution) | SuiteAtts], Result) :-\n" +
//        		"	modifierType(SuiteAtts, Result1),\n" +
//        		"	concat([att(Nom, Type, Resolution)], Result1, Result).\n" +
//        		"modifierType([att(Nom, Type, Resolution) | SuiteAtts], Result) :-\n" +
//        		"	attaque(Nom, Type2),\n" +
//        		"	Type \\== Type2,\n" +
//        		"	Type2 \\== '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "',\n" +
//        		"	Type2 \\== '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "',\n" +
//        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "',\n" +
//        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "',\n" +
//        		"	modifierType(SuiteAtts, Result1),\n" +
//        		"	concat([att(Nom, Type2, Resolution)], Result1, Result).");

//        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//        		"% M�thode permettant de changer une attaque normale en deux attaques rapides\n" +
//        		"changerNormaleRapide([], []).\n" +
//        		"changerNormaleRapide([att(Nom, Type, Resolution) | SuiteAtts], Result) :-\n" +
//        		"	changerNormaleRapide(SuiteAtts, Result1),\n" +
//        		"	concat([att(Nom, Type, Resolution)], Result1, Result).\n" +
//        		"changerNormaleRapide([att(Nom, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', Resolution) | SuiteAtts], Result) :-\n" +
//        		"	attaque(Nom, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "'),\n" +
//        		"	changerNormaleRapide(SuiteAtts, Result1),\n" +
//        		"	concat([att(Nom, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "', Resolution), att(Nom, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "', Resolution)], Result1, Result).");
        
//        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//        		"% M�thode permettant de changer deux attaques normales en une berserk\n" +
//        		"changerNormaleBerserk([], []).\n" +
//         		"changerNormaleBerserk([att(Nom, Type, Resolution) | SuiteAtts], Result) :-\n" +
//        		"	changerNormaleBerserk(SuiteAtts, Result1),\n" +
//        		"	concat([att(Nom, Type, Resolution)], Result1, Result).\n" +
//        		"changerNormaleBerserk([att(Nom1, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', Resolution1), att(Nom2, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', Resolution2) | SuiteAtts], Result) :-\n" +
//        		"	Nom1 == Nom2,\n" +
//        		"	Resolution1 == Resolution2,\n" +
//        		"	attaque(Nom1, '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "'),\n" +
//        		"	changerNormaleBerserk(SuiteAtts, Result1),\n" +
//        		"	concat([att(Nom1, '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "', Resolution1)], Result1, Result).");

        rulesList.add("generationScenarios(Result) :-\n" +
        		"	generationListeAttaques(Tmp),\n" +
        		"	ajouterResolution(Tmp, Result).");
        
        rulesList.add("generationChainesAttaques(Result) :-\n" +
    		"	generationListeAttaques(Result).");
        		
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	\n" +
        		"% M�thode permettant de creer une premi�re liste d'attaques (normales) en fonction des persos et de leurs PA\n" +
        		"generationListeAttaques(L) :- genererListeAtt(L, []).\n" +
        		"genererListeAtt([], Atts) :-\n" +
        		"	not(getNextAttaque(Atts, _)).\n" +
        		"genererListeAtt(Retour, Atts) :-\n" +
        		"	getNextAttaque(Atts, Attaque),\n" +
        		"	concat(Atts, [Attaque], Atts2),\n" +
        		"	genererListeAtt(Retour2, Atts2),\n" +
        		"	concat([Attaque], Retour2, Retour).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Methode permettant de r�cup�rer la prochaine attaque possible\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	peutAttaquerBerserk(Perso, Atts),\n" +
        		"	attaque(Perso, 'Berserk'),\n" +
        		"	Attaque = att(Perso, 'Berserk').\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	peutAttaquer(Perso, Atts),\n" +
        		"	attaque(Perso, Type),\n" +
        		"	Type \\== 'Rapide',\n" +
        		"	Type \\== 'Berserk',\n" +
        		"	Attaque = att(Perso, Type).\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	peutAttaquerRapide(Perso, Atts),\n" +
        		"	attaque(Perso, 'Rapide'),\n" +
        		"	Attaque = att(Perso, 'Rapide').");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% M�thode permettant de trouver une personne qui peut encore attaquer (poss�de assez de PA)\n" +
        		"peutAttaquer(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 4.\n" +
        		"peutAttaquerBerserk(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 8.\n" +
        		"peutAttaquerRapide(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 2.");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Methode permettant de savoir combien de PA restent � un perso\n" +
        		"paRestants(Perso, Atts, PARestants) :-\n" +
        		"	perso(Perso, _, _, _, _, PA, _),\n" +
        		"	paUtilises(Perso, Atts, PaUtilises),\n" +
        		"	PARestants is PA - PaUtilises.");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Methode permettant de savoir combien de PA ont �t� utilis�s par un perso\n" +
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
        		"% Donn�es sur les attaques\n" +
        		"% caracAttaque(typeAttaque, att, bonAtt, deg, bonDeg, resultAtt, resultBonAtt, resultDeg, resultBonDeg).\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        rulesList.add("coutPA('" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_BRUTALE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_PRECISE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "', 8).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "', 2).");
    }

}