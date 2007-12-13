%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Méthodes utilitaires
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Le prédicat concat
concat([],X,X).
concat([X|Y],Z,[X|W]):-
    concat(Y,Z,W).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Méthodes propres à DESTINEE
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Méthode permettant d'ajouter les types de résolution aux différentes attaques
ajouterResolution([], []).
ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup critique') | Tail2]) :-
	ajouterResolution(Tail, Tail2).
ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Coup simple') | Tail2]) :-
	ajouterResolution(Tail, Tail2).
ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive simple') | Tail2]) :-
	ajouterResolution(Tail, Tail2).
ajouterResolution([att(Perso,Type) | Tail], [att(Perso,Type,'Esquive parfaite') | Tail2]) :-
	ajouterResolution(Tail, Tail2).

generationScenarios(Result) :-
	generationListeAttaques(Tmp),
	ajouterResolution(Tmp, Result).

generationChainesAttaques(Result) :-
	generationListeAttaques(Result).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
% Méthode permettant de creer une première liste d'attaques (normales) en fonction des persos et de leurs PA
generationListeAttaques(L) :- genererListeAtt(L, []).
genererListeAtt([], Atts) :-
	not(getNextAttaque(Atts, _)).
genererListeAtt(Retour, Atts) :-
	getNextAttaque(Atts, Attaque),
	concat(Atts, [Attaque], Atts2),
	genererListeAtt(Retour2, Atts2),
	concat([Attaque], Retour2, Retour).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Methode permettant de récupérer la prochaine attaque possible
getNextAttaque(Atts, Attaque) :-
	peutAttaquerBerserk(Perso, Atts),
	attaque(Perso, 'Berserk'),
	Attaque = att(Perso, 'Berserk').
getNextAttaque(Atts, Attaque) :-
	peutAttaquer(Perso, Atts),
	attaque(Perso, Type),
	Type \== 'Rapide',
	Type \== 'Berserk',
	Attaque = att(Perso, Type).
getNextAttaque(Atts, Attaque) :-
	peutAttaquerRapide(Perso, Atts),
	attaque(Perso, 'Rapide'),
	Attaque = att(Perso, 'Rapide').

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Méthode permettant de trouver une personne qui peut encore attaquer (possède assez de PA)
peutAttaquer(Perso, Atts) :-
	paRestants(Perso, Atts, PARestants),
	PARestants >= 4.
peutAttaquerBerserk(Perso, Atts) :-
	paRestants(Perso, Atts, PARestants),
	PARestants >= 8.
peutAttaquerRapide(Perso, Atts) :-
	paRestants(Perso, Atts, PARestants),
	PARestants >= 2.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Methode permettant de savoir combien de PA restent à un perso
paRestants(Perso, Atts, PARestants) :-
	perso(Perso, _, _, _, _, PA, _),
	paUtilises(Perso, Atts, PaUtilises),
	PARestants is PA - PaUtilises.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Methode permettant de savoir combien de PA ont été utilisés par un perso
paUtilises(_, [], 0).
paUtilises(Perso1, [att(Perso2, Type) | Suite], PaUtilises) :-
	Perso1 == Perso2,
	coutPA(Type, PA),
	paUtilises(Perso1, Suite, PaUtilisesSuite),
	PaUtilises is PaUtilisesSuite + PA.
paUtilises(Perso1, [att(Perso2, _) | Suite], PaUtilises) :-
	Perso1 \== Perso2,
	paUtilises(Perso1, Suite, PaUtilisesSuite),
	PaUtilises is PaUtilisesSuite.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Données sur les attaques
% caracAttaque(typeAttaque, att, bonAtt, deg, bonDeg, resultAtt, resultBonAtt, resultDeg, resultBonDeg).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

coutPA('Normale', 4).
coutPA('Brutale', 4).
coutPA('Précise', 4).
coutPA('Berserk', 8).
coutPA('Rapide', 2).

attaque('Koumi', 'Normale').
attaque('No-one', 'Normale').

perso('Koumi', 16, 10, 12, 7, 10, 0).
perso('No-one', 12, 0, 8, 0, 10, 0).

