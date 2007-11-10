/**
 * 
 */
package logic.rules;

import java.util.ArrayList;
import java.util.List;


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
        
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Données sur les attaques\n" +
        		"% caracAttaque(typeAttaque, att, bonAtt, deg, bonDeg, resultAtt, resultBonAtt, resultDeg, resultBonDeg).\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        rulesList.add("caracAttaque('normale', Att, BonAtt, Deg, BonDeg, ResultAtt, ResultBonAtt, ResultDeg, ResultBonDeg) :-\n" +
        		"	ResultAtt = Att,\n" +
        		"	ResultBonAtt = BonAtt,\n" +
        		"	ResultDeg = Deg,\n" +
        		"	ResultBonDeg = BonDeg.");
        
        rulesList.add("caracAttaque('brutale', Att, BonAtt, Deg, BonDeg, ResultAtt, ResultBonAtt, ResultDeg, ResultBonDeg) :-\n" +
        		"	ResultAtt is round(2 * Att / 3, 0),\n" +
        		"	ResultBonAtt = BonAtt,\n" +
        		"	ResultDeg is round(3 * Deg / 2, 0),\n" +
        		"	ResultBonDeg = BonDeg.");
        

        rulesList.add("caracAttaque('précise', Att, BonAtt, Deg, BonDeg, ResultAtt, ResultBonAtt, ResultDeg, ResultBonDeg) :-\n" +
        		"	ResultAtt is round(3 * Att / 2, 0),\n" +
        		"	ResultBonAtt = BonAtt,\n" +
        		"	ResultDeg is round(2 * Deg / 3, 0),\n" +
        		"	ResultBonDeg = BonDeg.");
        
        rulesList.add("caracAttaque('rapide', Att, BonAtt, Deg, BonDeg, ResultAtt, ResultBonAtt, ResultDeg, ResultBonDeg) :-\n" +
        		"	ResultAtt is round(Att / 2, 0),\n" +
        		"	ResultBonAtt = BonAtt,\n" +
        		"	ResultDeg is round(Deg / 2, 0),\n" +
        		"	ResultBonDeg = BonDeg.");
        
        rulesList.add("caracAttaque('berserk', Att, BonAtt, Deg, BonDeg, ResultAtt, ResultBonAtt, ResultDeg, ResultBonDeg) :-\n" +
        		"	ResultAtt = Att * 2,\n" +
        		"	ResultBonAtt = BonAtt,\n" +
        		"	ResultDeg = Deg * 2,\n" +
        		"	ResultBonDeg = BonDeg.");
        
        rulesList.add("coutPA('normale', 4).\n" +
        		"coutPA('brutale', 4).\n" +
        		"coutPA('précise', 4).\n" +
        		"coutPA('berserk', 8).\n" +
        		"coutPA('rapide', 2).");

        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthodes propres à DESTINEE\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthode permettant de remplacer une attaque à N PA par une autre à N PA\n" +
        		"% changeAtt(ListAtts, NewListAtt).\n" +
        		"changeAtt([], []).\n" +
        		"changeAtt([att(Nom, Type) | SuiteAtts], L) :-\n" +
        		"	changeAtt(SuiteAtts, M),\n" +
        		"	insere(att(Nom, Type), M, L).\n" +
        		"changeAtt([att(Nom, Type) | SuiteAtts], L) :-\n" +
        		"	changeAtt(SuiteAtts, M),\n" +
        		"	attaque(Nom, Type2),\n" +
        		"	Type \\== Type2,\n" +
        		"	insere(att(Nom, Type2), M, L).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	\n" +
        		"% Méthode permettant de creer une première liste d'attaques (normales) en fonction des persos et de leurs PA\n" +
        		"generationListeAttaques(L) :- genererListeAtt(L, []).\n" +
        		"\n" +
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
        		"	Attaque = att(Perso, 'normale').\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	not(peutAttaquer(Perso, Atts)),\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 2,\n" +
        		"	attaque(Perso, 'rapide'),\n" +
        		"	Attaque = att(Perso, 'rapide').");
        
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
    }

}
