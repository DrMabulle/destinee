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
        		"% Méthodes utilitaires\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        rulesList.add("% Le prédicat concat/3\n" +
        		"concat([],X,X).\n" +
        		"concat([X|Y],Z,[X|W]):-\n" +
        		"    concat(Y,Z,W).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthodes propres à DESTINEE\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	");
        
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
        		"	ajouterResolution(Tail, Tail2).\n" +
        		"ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Echec competence') | Tail2]) :-\n" +
        		"	ajouterResolution(Tail, Tail2).");

        rulesList.add("generationScenarios(Result) :-\n" +
        		"	generationListeAttaques(Tmp),\n" +
        		"	ajouterResolution(Tmp, Result).");
        
        rulesList.add("generationChainesAttaques(Result) :-\n" +
    		"	generationListeAttaques(Result).");
        		
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
        		"	peutAttaquerBerserk(Perso, Atts),\n" +
        		"	attaque(Perso, '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "'),\n" +
        		"	Attaque = att(Perso, '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "').\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	peutAttaquerKamikaze(Perso, Atts),\n" +
        		"	attaque(Perso, '" + ConstantesAttaques.ID_ATTAQUE_KAMIKAZE + "'),\n" +
        		"	Attaque = att(Perso, '" + ConstantesAttaques.ID_ATTAQUE_KAMIKAZE + "').\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	peutAttaquer(Perso, Atts),\n" +
        		"	attaque(Perso, Type),\n" +
        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "',\n" +
        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "',\n" +
        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_KAMIKAZE + "',\n" +
        		"	Type \\== '" + ConstantesAttaques.ID_ATTAQUE_CHARGE + "',\n" +
        		"	Attaque = att(Perso, Type).\n" +
        		"getNextAttaque(Atts, Attaque) :-\n" +
        		"	peutAttaquerRapide(Perso, Atts),\n" +
        		"	attaque(Perso, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "'),\n" +
        		"	Attaque = att(Perso, '" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "').");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Méthode permettant de trouver une personne qui peut encore attaquer (possède assez de PA)\n" +
        		"peutAttaquer(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 4.\n" +
        		"peutAttaquerKamikaze(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 6.\n" +
        		"peutAttaquerCharge(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 6.\n" +
        		"peutAttaquerBerserk(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 8.\n" +
        		"peutAttaquerRapide(Perso, Atts) :-\n" +
        		"	paRestants(Perso, Atts, PARestants),\n" +
        		"	PARestants >= 2.");
        
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
        	"% Méthode permettant de creer l'ordre d'attaque en fonction des persos et de leurs PA\n" +
        	"generationListeAttaquants(L) :- genererOrdre(L, [], []).\n" +
        	"genererOrdre([], Atts, _) :-\n" +
        	"	not(getNextAttaquant(Atts, _)).\n" +
        	"genererOrdre(Retour, Atts, Ordre) :-\n" +
        	"	getNextAttaquant(Atts, Attaquant),\n" +
        	"	concat(Atts, [att(Attaquant, '" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "')], Atts2),\n" +
        	"	concat(Ordre, [Attaquant], Ordre2),\n" +
        	"	genererOrdre(Retour2, Atts2, Ordre2),\n" +
        	"	concat([Attaquant], Retour2, Retour).");
        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Methode permettant de récupérer le prochain attaquant possible\n" +
        		"getNextAttaquant(Atts, Attaquant) :-\n" +
        		"	peutAttaquer(Attaquant, Atts).");

        
        rulesList.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
        		"% Données sur les attaques\n" +
        		"% caracAttaque(typeAttaque, att, bonAtt, deg, bonDeg, resultAtt, resultBonAtt, resultDeg, resultBonDeg).\n" +
        		"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        
        rulesList.add("coutPA('" + ConstantesAttaques.ID_ATTAQUE_BERSERK + "', 8).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_BRUTALE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_CHARGE + "', 6).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_IMPARABLE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_KAMIKAZE + "', 6).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_MAGIQUE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_NORMALE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_PRECISE + "', 4).\n" +
        		"coutPA('" + ConstantesAttaques.ID_ATTAQUE_RAPIDE + "', 2).");
    }

}
