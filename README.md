<div>

# <span class="c27 c81">M1 Informatique - TP IA  2020-2021</span>

</div>


## <span class="c27 c38"> AKOTO Yao Arnaud P  </span> 


<span class="c4"></span>

<span class="c4"></span>

## <span class="c12">Compte rendu du  TP1 : Dames Anglaises avec MCTS</span>

<span class="c4"></span>

### <span class="c27 c42">Objectif du TP</span>

<span class="c5">L’objectif de ce TP était d’implémenter une intelligence artificielle à l’aide de l’algorithme de Monte-Carlo (</span><span class="c5">MCTS</span><span class="c5">) dans un jeu dont nous avons codé les règles (</span><span class="c5">Dames Anglaises</span><span class="c5">) pour en suite analyser les performances avec différents temps de calculer.</span>

<span class="c4"></span>

### <span class="c42 c27">Processus</span>

<span class="c4">Pour réaliser notre tâche nous avons eu deux grandes étapes qui sont :</span>

*   <span class="c4">L’implémentation</span>

*   <span class="c4">Implémentation des règles du jeu</span>
*   <span class="c4">Implémentation du MCTS</span>

*   <span class="c4">L’analyse des résultats</span>

<span class="c4"></span>

#### 1.  <span class="c5 c67">Implémentations</span><span class="c12"> </span>

1.  <span class="c12"> Implémentation des règles du je</span>

<span class="c12"></span>

<span class="c4">Pour commencer, il était important avant un tour de connaitre le statut des différents cases de notre jeu de dame (vide, pion adverse, notre pion). À l’aide des fonctions :</span>

<a id="t.9c010cb5b70a005f8fb361bdcfec3af5a0dfb198"></a><a id="t.0"></a>

<table class="c85 c93">

<tbody>

<tr class="c14">

<td class="c30" colspan="1" rowspan="1">

<span class="c16">boolean</span><span class="c16"> </span><span class="c15">isEmpty</span><span class="c16">(</span><span class="c16">int</span><span class="c16"> square)  
</span><span class="c16">boolean</span><span class="c16"> </span><span class="c15">isAdversary</span><span class="c16">(</span><span class="c16">int</span><span class="c16"> square)  
</span><span class="c16">boolean</span><span class="c16"> </span><span class="c15">isMine</span><span class="c16">(</span><span class="c16">int</span><span class="c16">square)  
ArrayList<Integer></span> <span class="c15">myPawns</span><span class="c16">()</span>

</td>

</tr>

</tbody>

</table>

<span class="c5">Pour</span> <span class="c15">isEmpty</span><span class="c5">, on a juste utiliser la fonction portant le même non dans la classe</span> <span class="c5">CheckerBoard</span> <span class="c4">retournant la valeur souhaitée.</span>

<span class="c15">isAdversary et isMine</span><span class="c5">sont implémenté quasiment de la même manière: Si la valeur du joueur courant est</span> <span class="c5">ONE</span><span class="c5">et  la fonction</span> <span class="c15">isBlack</span><span class="c5">du</span> <span class="c5">CheckerBoard</span><span class="c5">ou si la valeur du joueur courant est</span> <span class="c5">TWO</span><span class="c5">et  la fonction</span> <span class="c15">isWhite</span><span class="c4"> on a alors une case contenant un adversaire. On fait l’inverse pour celle contenant notre pion.  </span>

<span class="c5">Concernant</span> <span class="c15">myPawns,</span><span class="c4"> on retourne la liste contenant les pions noirs ou celle contenant les pions blancs en fonction de la valeur de notre joueur.</span>

<span class="c4"></span>

<span class="c4"></span>

<span class="c5">Ensuite, il fallait implémenter les mouvements possibles du joueur courent</span> <span class="c5 c18 c45">(l’étape la plus difficile de cette première partie)</span><span class="c4">.</span>

<span class="c4">Pour ce faire, on a dû passer par six(6) fonctions auxiliaires dont une pas très utile mais laissée pour la compréhension du code:</span>

<a id="t.5f02427bdbfbcfb25edb12797d495c88ceab40b9"></a><a id="t.1"></a>

<table class="c62">

<tbody>

<tr class="c14">

<td class="c57" colspan="1" rowspan="1">

<span class="c16">ArrayList<Integer></span> <span class="c43">calculMov</span><span class="c16">(Integer from,</span> <span class="c7">boolean</span><span class="c16">dame,</span> <span class="c7">boolean</span><span class="c16 c27 c50"> blanc,CheckerBoard board1)</span>

<span class="c16">List<Move></span> <span class="c43">deplacementPossible</span><span class="c16">()  
List<Move></span> <span class="c43">moveSansCapture</span><span class="c16 c27 c50">()</span>

<span class="c16">List<Move></span> <span class="c43">movAvecCaptureForEach</span><span class="c16">(</span><span class="c7">int</span><span class="c16"> origin,</span><span class="c7">int</span><span class="c16">from,</span> <span class="c7">boolean</span><span class="c16">blanc ,</span> <span class="c7">boolean</span><span class="c16 c27 c50"> dame,CheckerBoard board1 ,DraughtsMove drMove , ArrayList<Integer> prise)</span>

<span class="c16">List<Move></span> <span class="c43">movAvecCaptureForAll</span><span class="c16">()  
List<Move></span> <span class="c43">moveAvecCapture</span><span class="c16">()  
List<Move></span> <span class="c43">possibleMoves</span><span class="c16">()</span>

</td>

</tr>

</tbody>

</table>

<span class="c15">calculMov</span><span class="c5">prend en paramètre : un</span> <span class="c5">pion</span><span class="c5">, son</span> <span class="c5">type</span><span class="c5">, sa</span> <span class="c5">couleur</span><span class="c5">et un</span> <span class="c5">CheckerBoard.</span><span class="c4"> On commence par vérifier si le pion est positionné sur une extrémité du plateau puis en fonction de son type et de sa couleur, calcule les destinations que pourrait atteindre le pion s’il y avait aucun obstacle pour l’y en empêcher.    </span>

<span class="c15">deplacementPossible</span><span class="c5">utilise</span> <span class="c15">calculMov</span><span class="c5">et</span> <span class="c15">myPawns</span><span class="c5">et calcule pour chaque pion les déplacements possibles toujours sans contrainte comme si chaque pion était seul sur le damier puis forme des objets</span> <span class="c15">Move</span><span class="c5">de sous la forme</span> <span class="c5 c10 c94">(</span><span class="c5 c10 c36">from-t1xt2xt3x.... tn)</span><span class="c46"> </span><span class="c4">où from représente la position de départ et t, la position d’arrivée.</span>

<span class="c4">Exemple :</span>

<a id="t.2b4e72d66f7a9755ae22af4597c5fe341baa9e8d"></a><a id="t.2"></a>

<table class="c80">

<tbody>

<tr class="c14">

<td class="c83" colspan="1" rowspan="1">

<span class="c13">-------------     -------------  
|</span> <span class="c19">1</span><span class="c13">   </span><span class="c19">2</span><span class="c13">   </span><span class="c19">3</span><span class="c13">|   |   x   x   x |  
|</span> <span class="c19">4</span><span class="c13">   </span><span class="c19">5</span><span class="c13">   </span><span class="c19">6</span><span class="c13">|   | x   x   x   |  
|</span> <span class="c19">7</span><span class="c13">   </span><span class="c19">8</span><span class="c13">   </span><span class="c19">9</span><span class="c13"> |   |   .   .   . |  
|</span><span class="c19">10</span><span class="c13"></span> <span class="c19">11</span><span class="c13"></span> <span class="c19">12</span><span class="c13">   |   | .   .   .   |  
|  </span><span class="c19">13</span><span class="c13"></span> <span class="c19">14</span><span class="c13"></span> <span class="c19">15</span><span class="c13"> |   |   o   o   o |  
|</span><span class="c19">16</span><span class="c13"></span> <span class="c19">17</span><span class="c13"></span> <span class="c19">18</span><span class="c13 c27 c50">   |   | o   o   o   |  
-------------     -------------</span>

<span class="c13 c27 c50">calculMov (14, !estDame, esBblanc, board) retrurne (11,12)</span>

<span class="c13 c27 c50">calculMov (16, !estDame, estBlanc, board) retourne (13)</span>

<span class="c13 c27 c50">calculMov (13, estDame, esBblanc, board) retourne (10,11,16,17)</span>

<span class="c27 c50 c75">//on considère ici que chaque pion est seul sur le damier</span>

<span class="c13 c27 c50">deplacementPossible() retourne donc dans notre cas:</span>

<span class="c13 c27 c50"> 13-10x11</span>

<span class="c13 c27 c50"> 14-11x12</span>

<span class="c13 c27 c50"> 15-12</span>

<span class="c13 c27 c50"> 16-13</span>

<span class="c13 c27 c50"> 17-13x14</span>

<span class="c13 c27 c50"> 18-14x15</span>

<span class="c13 c27 c50"></span>

</td>

</tr>

</tbody>

</table>

<span class="c4"></span>

<span class="c15">moveSansCapture</span><span class="c5">récupère la liste retournée par</span> <span class="c15">deplacementPossible,</span><span class="c5">pour chaque sous-liste, isole le premier élément qui est la case de départ puis pour chaque destination vide, forme un doublet</span> <span class="c5 c36 c10">(from-to)</span><span class="c4"> et l’ajoute à laliste des mouvements sans captures.</span>

<span class="c4">on a donc une double itération.</span>

<span class="c4"></span>

<span class="c5">Pour un pion donné, on calcule toutes ses prises possible avec</span> <span class="c15">movAvecCaptureForEach</span><span class="c4">.</span>

<span class="c5">Ici aussi on utilise</span> <span class="c15">calculMov</span><span class="c4">  et pour chaque destination du pion de départ, on vérifie si c’est un adversaire puis si elle n’est pas déjà dans la liste des pions déjà capturés. Ensuite si la destination n’est pas une extrémité de notre damier. Une les trois conditions précédentes réunis, on peut envisager une prise pour notre pion.</span>

<span class="c5">Pour savoir s’il y a</span> <span class="c5">prise</span><span class="c5">, on doit détecter</span> <span class="c5 c59">le sens du voisinage</span><span class="c5">entre le</span> <span class="c5">pion</span><span class="c5">et la</span> <span class="c5">destination</span><span class="c5">et si la</span> <span class="c5 c54">case suivante dans le même sens de voisinage est vide</span><span class="c4"> on a effectivement une prise.        </span>

<span class="c5">Pour les prises multiples, on appelle récursivement</span> <span class="c15">movAvecCaptureForEach</span><span class="c4">.    </span>

<span class="c5">On remarque la présence de trois paramètres particuliers (</span><span class="c5 c54">drMove, prise et origin</span><span class="c5">):</span>

*   <span class="c5 c54">drMove :</span><span class="c4">À chaque récursion, on mémorise la trace de déplacement pour l’ajoute à la liste de déplacement avec captures du pion seulement quand on arrive à la destination finale.</span>
*   <span class="c5 c54">prise :</span><span class="c4">Pour s’assurer de ne pas boucler infiniment sur une prise, il faut à chaque fois ajouter la prise à la liste de pions capturés.</span>
*   <span class="c5 c54">origin :</span><span class="c5">  Si lors d’une rafle, les adversaires sons disposés de tel sorte que notre pion a la possibilité de repasser par sa position d’origine, il est évident que vérifier uniquement que la case est vide ne le perpétrait pas alors nous avons ajouté la condition suivante : (</span><span class="c16">isEmpty(to) || to == origin)</span><span class="c4"> </span>

<span class="c5">Illustration de l’importance d’</span><span class="c5 c54">origin</span><span class="c4"> :</span>

<a id="t.c2dbcb74274cb364441fb375c79453845c7cde1d"></a><a id="t.3"></a>

<table class="c17 c85">

<tbody>

<tr class="c14">

<td class="c64" colspan="1" rowspan="1">

<span class="c2">-----------------     -----------------  
|</span> <span class="c1">1</span><span class="c2">   </span><span class="c1">2</span><span class="c2">   </span><span class="c1">3</span><span class="c2">   </span><span class="c1">4</span><span class="c2">|   |   .   O   .   . |  
|</span> <span class="c1">5</span><span class="c2">   </span><span class="c1">6</span><span class="c2">   </span><span class="c1">7</span><span class="c2">   </span><span class="c1">8</span><span class="c2">|   | .   .   .   x   |  
|</span> <span class="c1">9</span><span class="c2"></span> <span class="c1">10</span><span class="c2"></span> <span class="c1">11</span><span class="c2"></span> <span class="c1">12</span><span class="c2"> |   |   .   x   x   . |  
|</span><span class="c1">13</span><span class="c2"></span> <span class="c1">14</span><span class="c2"></span> <span class="c1">15</span><span class="c2"></span> <span class="c1">16</span><span class="c2">   |   | .   x   .   .   |  
|  </span><span class="c1">17</span><span class="c2"></span> <span class="c1">18</span><span class="c2"></span> <span class="c1">19</span><span class="c2"></span> <span class="c1">20</span><span class="c2"> |   |   .   x   x   . |  
|</span><span class="c1">21</span><span class="c2"></span> <span class="c1">22</span><span class="c2"></span> <span class="c1">23</span><span class="c2"></span> <span class="c1">24</span><span class="c2">   |   | .   .   .   .   |  
|  </span><span class="c1">25</span><span class="c2"></span> <span class="c1">26</span><span class="c2"></span> <span class="c1">27</span><span class="c2"></span> <span class="c1">28</span><span class="c2"> |   |   .   .   o   . |  
|</span><span class="c1">29</span><span class="c2"></span> <span class="c1">30</span><span class="c2"></span> <span class="c1">31</span><span class="c2"></span> <span class="c1">32</span><span class="c2 c27">   |   | X   o   o   o   |  
-----------------     -----------------</span>

<span class="c13 c27 c37">  ||</span>

<span class="c13 c27 c37">  \/</span>

<span class="c2">-----------------     -----------------  
|</span> <span class="c1">1</span><span class="c2">   </span><span class="c1">2</span><span class="c2">   </span><span class="c1">3</span><span class="c2">   </span><span class="c1">4</span><span class="c2">|   |   .   .   .   . |  
|</span> <span class="c1">5</span><span class="c2">   </span><span class="c1">6</span><span class="c2">   </span><span class="c1">7</span><span class="c2">   </span><span class="c1">8</span><span class="c2">|   | .   .   O   x   |  
|</span> <span class="c1">9</span><span class="c2"></span> <span class="c1">10</span><span class="c2"></span> <span class="c1">11</span><span class="c2"></span> <span class="c1">12</span><span class="c2"> |   |   .   x   x   . |  
|</span><span class="c1">13</span><span class="c2"></span> <span class="c1">14</span><span class="c2"></span> <span class="c1">15</span><span class="c2"></span> <span class="c1">16</span><span class="c2">   |   | .   x   .   .   |  
|  </span><span class="c1">17</span><span class="c2"></span> <span class="c1">18</span><span class="c2"></span> <span class="c1">19</span><span class="c2"></span> <span class="c1">20</span><span class="c2"> |   |   .   X   x   . |  
|</span><span class="c1">21</span><span class="c2"></span> <span class="c1">22</span><span class="c2"></span> <span class="c1">23</span><span class="c2"></span> <span class="c1">24</span><span class="c2">   |   | .   .   .   .   |  
|  </span><span class="c1">25</span><span class="c2"></span> <span class="c1">26</span><span class="c2"></span> <span class="c1">27</span><span class="c2"></span> <span class="c1">28</span><span class="c2"> |   |   .   .   o   . |  
|</span><span class="c1">29</span><span class="c2"></span> <span class="c1">30</span><span class="c2"></span> <span class="c1">31</span><span class="c2"></span> <span class="c1">32</span><span class="c2 c27">   |   | X   o   o   o   |  
-----------------     -----------------</span>

<span class="c13 c27 c37">  ||</span>

<span class="c13 c27 c37">  \/</span>

<span class="c2">-----------------     -----------------  
|</span> <span class="c1">1</span><span class="c2">   </span><span class="c1">2</span><span class="c2">   </span><span class="c1">3</span><span class="c2">   </span><span class="c1">4</span><span class="c2">|   |   .   .   .   . |    
|</span> <span class="c1">5</span><span class="c2">   </span><span class="c1">6</span><span class="c2">   </span><span class="c1">7</span><span class="c2">   </span><span class="c1">8</span><span class="c2">|   | .   .   O   x   |  
|</span> <span class="c1">9</span><span class="c2"></span> <span class="c1">10</span><span class="c2"></span> <span class="c1">11</span><span class="c2"></span> <span class="c1">12</span><span class="c2"> |   |   .   x   x   . |  
|</span><span class="c1">13</span><span class="c2"></span> <span class="c1">14</span><span class="c2"></span> <span class="c1">15</span><span class="c2"></span> <span class="c1">16</span><span class="c2">   |   | .   .   .   .   |  
|  </span><span class="c1">17</span><span class="c2"></span> <span class="c1">18</span><span class="c2"></span> <span class="c1">19</span><span class="c2"></span> <span class="c1">20</span><span class="c2"> |   |   x   x   x   . |  
|</span><span class="c1">21</span><span class="c2"></span> <span class="c1">22</span><span class="c2"></span> <span class="c1">23</span><span class="c2"></span> <span class="c1">24</span><span class="c2">   |   | .   .   .   .   |  
|  </span><span class="c1">25</span><span class="c2"></span> <span class="c1">26</span><span class="c2"></span> <span class="c1">27</span><span class="c2"></span> <span class="c1">28</span><span class="c2"> |   |   .   .   o   . |  
|</span><span class="c1">29</span><span class="c2"></span> <span class="c1">30</span><span class="c2"></span> <span class="c1">31</span><span class="c2"></span> <span class="c1">32</span><span class="c2">   |   | X   o   o   o   |  
-----------------     -----------------</span><span class="c13">  
</span><span class="c2">Ici</span> <span class="c73 c89">possibleMove</span><span class="c2">retourne :  
avec</span> <span class="c29">origin :</span><span class="c2 c27">{7x16x23x14x7 ; 7x14x21 ; 7x16x23x14x21 ; 7x14x23x16x7}  </span>

<span class="c2">sans</span> <span class="c29">origin</span><span class="c60 c73"> :</span><span class="c2">{7x16x23x14 ; 7x14x21 ; 7x16x23x14x21 ; 7x14x23x16}</span>

</td>

</tr>

</tbody>

</table>

<span class="c5">Par la suite, avec</span> <span class="c15">movAvecCaptureForAll</span><span class="c5">on récupère juste liste des déplacements avec prise pour tous les pions en itérant sur</span> <span class="c15">myPawns.</span><span class="c4"> </span>

<span class="c15">moveAvecCapture</span><span class="c5">est la fonction un peu inutile car retourne simplement</span> <span class="c15">movAvecCaptureForAll</span><span class="c4"> mais pur une question compréhension du code on l’a laissée.</span>

<span class="c5">Pour finir avec la partie des déplacements, dans</span> <span class="c15">possibleMoves</span> <span class="c5">on retourne</span> <span class="c15">moveAvecCapture</span><span class="c5">si elle n’est pas vide sinon on retourne</span> <span class="c15">moveSansCapture</span><span class="c4"> </span>

<span class="c4"></span>

<span class="c4"></span>

<span class="c4">Pour finir nous avons réussi à implémenter les Méthodes plus facilement:</span>

<a id="t.9437be57a1a8e64a98781e1b1535146f310ce254"></a><a id="t.4"></a>

<table class="c82">

<tbody>

<tr class="c14">

<td class="c71" colspan="1" rowspan="1">

<span class="c7">void</span><span class="c16"> </span><span class="c43">play</span><span class="c16">(Move aMove)  
PlayerId</span> <span class="c43">winner</span><span class="c16">()</span>

</td>

</tr>

</tbody>

</table>

<span class="c4"></span>

<span class="c5">Avec</span> <span class="c15">play</span><span class="c5">,</span><span class="c4"> on parcourt le mouvement en paramètre en déplaçant le pion au fur et à mesure qu’on avance. Si entre le couple départ-arrivée, il y a un pion, on le capture en le supprimant du damier.</span>

<span class="c5">Si on a un mouvement avec prise ou un mouvement d’un pion simple, on réinitialise la variable</span> <span class="c15">nbKingMovesWithoutCapture</span><span class="c4">  sinon elle est incrémentée.</span>

<span class="c5">Si un pion arrive à l’extrémité adverse et que ce n’est pas une dame il est immédiatement</span> <span class="c5">promu en dame</span><span class="c4"> et passe son tour.</span>

<span class="c4">À la fin du tour, on incrémente le nombre de tours  et on donne la main à l’adversaire.</span>

<span class="c4"></span>

<span class="c15">winner</span><span class="c5">retourne</span> <span class="c5">NONE</span><span class="c5"> si on a seulement des mouvements de dames durant 25 tours (</span><span class="c15">nbKingMovesWithoutCapture ==25</span><span class="c4">). Si nous n’avons plus la possibilité de bouger ou si on n’a plus de pion sur le damier alors c’est l’adversaire qui a gagné.</span>

* * *

<span class="c4"></span>

<span class="c4"></span>

1.  <span class="c12"> Implémentation du MCTS</span>

<span class="c4">Dans cette deuxième partie, il s’agissait pour nous d’implémenter l’algorithme de Monte-Carlo. Pour ce faire nous avions à notre disposition la structure suivante que nous avons modifié pour l’adapter à notre implémentation:</span>

<span class="c4"></span>

<a id="t.ed348dcb9e4a6447aba22856aca60807453a2e2d"></a><a id="t.5"></a>

<table class="c69">

<tbody>

<tr class="c14">

<td class="c79" colspan="1" rowspan="1">

<span class="c7 c18">public</span><span class="c16 c18"> </span><span class="c7 c18">class</span><span class="c16 c18"> </span><span class="c9">MonteCarloTreeSearch</span><span class="c16 c18"> {  
        </span><span class="c7 c18">class</span><span class="c16 c18"> </span><span class="c9">EvalNode</span><span class="c16 c18">{  

               Move move_node ;</span> <span class="c18 c28">//Ajouté mouvement associer au nœud</span><span class="c16 c18">  
                EvalNode parent;</span><span class="c28 c18">//Ajouté : un pointeur vers le père</span><span class="c16 c18">  

                </span><span class="c7 c18">double</span><span class="c16 c18"> </span><span class="c43 c18">uct</span><span class="c16 c18">()  
                </span><span class="c7 c18">double</span><span class="c16 c18"> </span><span class="c43 c18">score</span><span class="c16 c18">()  
                </span><span class="c7 c18">void</span><span class="c16 c18"> </span><span class="c18 c43">updateStats</span><span class="c16 c18">(RolloutResults res)  
                </span><span class="c7 c18">void</span><span class="c16 c18"> </span><span class="c43 c18">genererFils</span><span class="c16 c18">()  </span><span class="c28 c18">//Ajouté</span><span class="c16 c18">EvalNode</span> <span class="c43 c18">meilleurFeuille</span><span class="c16 c18">()</span> <span class="c28 c18">//Ajouté</span><span class="c16 c18">EvalNode</span> <span class="c43 c18">meilleurFils</span><span class="c16 c18">()</span> <span class="c28 c18">//Ajouté</span><span class="c16 c18">  
        }  

        </span><span class="c7 c18">static</span><span class="c16 c18">class</span> <span class="c43 c18">RolloutResults</span><span class="c16 c18">()</span> <span class="c7 c18">static</span><span class="c16 c18">PlayerId</span> <span class="c43 c18">playRandomlyToEnd</span><span class="c16 c18">(Game game)</span> <span class="c7 c18">static</span><span class="c16 c18">RolloutResults</span> <span class="c43 c18">rollOut</span><span class="c16 c18">(</span><span class="c7 c18">final</span><span class="c16 c18">Game game,</span> <span class="c7 c18">int</span><span class="c16 c18"> nbRuns)  
        </span><span class="c7 c18">public</span><span class="c16 c18"> </span><span class="c7 c18">void</span><span class="c16 c18"> </span><span class="c43 c18">evaluateTreeWithTimeLimit</span><span class="c16 c18">(</span><span class="c7 c18">int</span><span class="c16 c18"> timeLimitMillis)  
        </span><span class="c7 c18">public</span><span class="c16 c18"> </span><span class="c7 c18">boolean</span><span class="c16 c18"> </span><span class="c43 c18">evaluateTreeOnce</span><span class="c16 c18">()  
        </span><span class="c7 c18">public</span><span class="c16 c18">Move</span> <span class="c43 c18">getBestMove</span><span class="c16 c18">()</span> <span class="c7 c18">public</span><span class="c16 c18">String</span> <span class="c43 c18">stats</span><span class="c16 c27 c18">()  
}</span>

</td>

</tr>

</tbody>

</table>

<span class="c4"></span>

<span class="c5">La première des choses qu’on avait  à faire était de nous assurer que notre algorithme parcourait toutes les possibilités dans un état donné du jeu avant de passer à l’exploration des niveaux suivants. Pour ce faire nous avons utilisé la formule</span> <span style="overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 84.70px; height: 16.81px;">![](images/image3.png)</span><span class="c5">que nous avons implémenté dans la méthode</span> <span class="c15 c18">uct.</span><span class="c4"> </span>

<span class="c5">Pour commencer, on génère immédiatement les fils du</span> <span class="c5">root</span> <span class="c5">avec la méthode</span> <span class="c15 c18">genererFils</span><span class="c5">qui itère sur les mouvements possibles de l’état courent du jeu en associant à chaque mouvement, un nœud dont le père est le nœud appelant. D'où l’ajout des attributs</span> <span class="c16 c18">Move et EvalNode.</span><span class="c5">C’est seulement avec la fonction</span> <span class="c15 c18">evaluateTreeOnce</span><span class="c4"> que nous implémentons concrètement le comportement de notre MCTS.</span>

<span class="c5">Avant de parcourir notre arbre, on commence par sélectionner le meilleur nœud  l’aide de</span> <span class="c15 c18">meilleurFeuille</span><span class="c5">qui parcoure l’arbre et retourne la feuille la plus prometteuse de l’arbre à l’aide notamment l’utilisation de</span> <span class="c5">l’uct</span><span class="c4"> de chaque fils.</span>

<span class="c5">Une fois la meilleure feuille de l’arborescence sélectionnée, on génère aussi ses fils et on l’exécute aléatoirement avec</span> <span class="c15 c18">RolloutResults</span><span class="c4"> durant une période donnée en paramètre d’exécution et retourne à chaque fin de partie aléatoire, des résultats que nous remontons aux différents nœuds parcouru.      </span>

<span class="c5">La petite subtilité qui à pendant un moment bouleversé nos résultats est la méthode</span> <span class="c15 c18">updateStats</span><span class="c4"> qui permet de remonter le nombre de défaites du nœud courent (et non le nombre de victoires).</span>

<span class="c5">On stop l’itération si notre root n’a qu’un seul fils ou quand on a un fils pour qui on a une victoire imminente</span> <span class="c5 c18 c55">(ça ne sert à rien de calculer)</span><span class="c4">.</span>

<span class="c5">Après les calcules, on récupère le meilleur coup avec</span> <span class="c15 c18">getBestMove</span><span class="c5">qui appel la fonction</span> <span class="c15 c18">meilleurFils</span><span class="c5">retournant le nœud où notre probabilité de gagner est plus forte (plus grand</span> <span class="c15 c18">score</span><span class="c4">).    </span>

<span style="overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 594.00px; height: 209.86px;">![](images/image1.png)</span>

<span class="c4"></span>

<span class="c4"></span>

<span class="c5"> </span>

1.  <span class="c27 c67 c90 c92">Analyse des résultats</span>

<span class="c4">Après implémentation de l’algorithme, nous l’avons testé sur les différentes versions des jeux à notre disposition (Tic-tac-toe, Dames 8x8, Dames 10x10, Dames 6x6) avec les différents temps de calcul.</span>

<span class="c4">Ce qui nous retourne les résultats suivants :</span>

<span class="c4"></span>

<span class="c22">Tic-Tac-Toe</span>

<span class="c4">Ici peu importe leurs différents temps de calcul, les IA ont tous la même performance. Cela est dû au fait que l’espace jeux est relativement petit et donc ne nécessite pas beaucoup d'effort de calcul.  </span>

<span class="c4"></span>

<span class="c5">IA vs IA</span>

<span class="c5">100%</span><span class="c5">des parties confrontant deux IA peu importe leurs différents temps de calcul se soldent sur des</span> <span class="c5">matchs nul</span><span class="c4">.</span>

<span class="c4"></span>

<span class="c27 c47">Humain vs IA</span>

<span class="c4">Ici les résultats divergent en fonction de l’adversaire que l’IA a en face d’elle.</span>

<span class="c5">C'est en gros cette partie qui nous a compliquée la tâche on pensait que c'était une erreur de notre implémentation alors que non. C’est juste que</span> <span class="c5">MCTS</span><span class="c4"> n’est pas l’algorithme le plus adapté pour ce genre de jeux où le nombre de coups possible est assez réduit.</span>

<span class="c4">Dans notre cas, l’algorithme cherche à maximiser ses chances de gagner sans forcément minimiser celles de perdre.</span>

<span class="c4">Illustrations :</span>

<a id="t.6bb9eaac4754da2b192d635d0e5c8b140ee3af9c"></a><a id="t.6"></a>

<table class="c77">

<tbody>

<tr class="c86">

<td class="c34" colspan="1" rowspan="1">

<span class="c35">//IA avec 'o' et Humain avec 'x'</span><span class="c44">  
        board      board     board     board     board     board     board  
0 1 2    . . .      . . x     o . x     o . x     o . x     o. x      o . x  
3 4 5    . o .      . o .     . o .     . o .     . o .     . o .     . o .  
6 7 8    . . .      . . .     . . .     . . x     . . x     . . x     . . x</span>

</td>

</tr>

</tbody>

</table>

<span class="c4"></span>

<span class="c5">À ce niveau de la partie de notre exemple, on constate que le meilleur coup ici pour éviter la défaite est</span> <span class="c5">“</span><span class="c5 c49">5</span><span class="c5">”</span><span class="c5">alors que notre algorithme privilégiera l’option</span> <span class="c5">“</span><span class="c5 c49">3</span><span class="c5">”</span><span class="c5">qui lui garantirait une victoire imminante au coup suivant. L’option</span> <span class="c5">“</span><span class="c5 c49">5</span><span class="c5">”</span> <span class="c4">n’est pas choix car même si elle lui évite la défaite sur le coup on a aucune garantis de gagner la partie.</span>

<span class="c5">Pour résoudre ce problème, nous avons ajouté une</span> <span class="c5 c18">pseudo heuristique</span><span class="c4"> permettant de savoir si après notre tour l’adversaire aurait un déplacement qui ferait immédiatement perdre :</span>

<span class="c4"></span>

<a id="t.2e145ab91627b399db34a9899e15bddb74e00e58"></a><a id="t.7"></a>

<table class="c74">

<tbody>

<tr class="c14">

<td class="c87" colspan="1" rowspan="1">

<span class="c7 c10">boolean</span><span class="c16 c10"> </span><span class="c43 c10">verfDefaiteImediate</span><span class="c16 c10">(EvalNode node){  
                        EvalNode nodeDeVerificationDeDefaite =</span> <span class="c7 c10">new</span><span class="c16 c10"> EvalNode(node.game.clone());</span><span class="c16 c73">  
</span><span class="c16 c10">                        nodeDeVerificationDeDefaite.genererFils();  
                        </span><span class="c7 c10">for</span><span class="c16 c10"> (EvalNode n : nodeDeVerificationDeDefaite.children){  
                                </span><span class="c7 c10">if</span><span class="c16 c10"> (n.game.winner() !=</span><span class="c7 c10">null</span><span class="c16 c10">)  
                                </span><span class="c7 c10">return</span><span class="c16 c10"> </span><span class="c7 c10">true</span><span class="c16 c10">;  
                        }  
                        </span><span class="c7 c10">return</span><span class="c16 c10"> </span><span class="c7 c10">false</span><span class="c16 c27 c10">;  
                }</span>

</td>

</tr>

</tbody>

</table>

<span class="c16 c27 c50"></span>

<span class="c5">Dans la même lancée, nous topons l’itération s’il y a qu’un seul fils qui peut nous sortie de la situation de défaite (</span> <span class="c7">int</span><span class="c16"> </span><span class="c43">seSauverAvec</span><span class="c16">(EvalNode node) ==</span> <span class="c88">1</span><span class="c4">).</span>

<span class="c4"></span>

<span class="c5">Les modifications apportées améliorent largement les l’efficacité de notre IA.  
les parties entre IA sont plus élaborées et celle entre IA et humain se soldent au mieux par un nul pour l’humain.</span> <span class="c5 c78">(sur la base de trois dizaines de parties)</span><span class="c4"> </span>

<span class="c4"></span>

<span class="c4"></span>

<span class="c5 c51">Dames Anglaise</span>

<span class="c5">Avec le jeu  de Dames, la version améliorée n’apporte pas grand changement dans les résultats qu’on avait déjà dans la première version de notre</span> <span class="c5">MCTS</span><span class="c4"> sauf souvent en fin de partie.</span>

<span class="c4"></span>

<span class="c4">Dans cette partie, les résultats varient en fonction de l'espace de jeu et du temps de calcul.</span>

<span class="c5">Après les parties entre différents temps de jeu et espace</span><span class="c5 c10 c41">(résultats en annexe page 9)</span><span class="c4">, on constate généralement que les débuts de parties ne sont pas très déterminants.</span>

<span class="c5">La différence se fait vers environ le</span> <span class="c5">10ᵉ tour</span><span class="c4"> où le joueur avec le plus de temps de calcul commence à prendre l’avantage sur l’autre. C'est seulement vers la fin de la partie que le jeu devient un peu équilibré en termes de choix  de déplacement.</span>

<span class="c4"></span>

<span class="c5">Petite illustration pour revenir sur l’amélioration de notre</span> <span class="c5">MCTS</span><span class="c4"> en fin de partie</span>

<a id="t.bceafbb14406dfa47eed93e4a07a1c63b98aafdd"></a><a id="t.8"></a>

<table class="c58">

<tbody>

<tr class="c14">

<td class="c66" colspan="1" rowspan="1">

<span class="c13">-----------------     -----------------  
|</span> <span class="c19">1</span><span class="c13">   </span><span class="c19">2</span><span class="c13">   </span><span class="c19">3</span><span class="c13">   </span><span class="c19">4</span><span class="c13">|   |   x   O   .   . |  
|</span> <span class="c19">5</span><span class="c13">   </span><span class="c19">6</span><span class="c13">   </span><span class="c19">7</span><span class="c13">   </span><span class="c19">8</span><span class="c13">|   | o   .   .   O   |  
|</span> <span class="c19">9</span><span class="c13"></span> <span class="c19">10</span><span class="c13"></span> <span class="c19">11</span><span class="c13"></span> <span class="c19">12</span><span class="c13"> |   |   .   .   .   . |  
|</span><span class="c19">13</span><span class="c13"></span> <span class="c19">14</span><span class="c13"></span> <span class="c19">15</span><span class="c13"></span> <span class="c19">16</span><span class="c13">   |   | .   x   .   .   |  
|  </span><span class="c19">17</span><span class="c13"></span> <span class="c19">18</span><span class="c13"></span> <span class="c19">19</span><span class="c13"></span> <span class="c19">20</span><span class="c13"> |   |   .   .   .   . |  
|</span><span class="c19">21</span><span class="c13"></span> <span class="c19">22</span><span class="c13"></span> <span class="c19">23</span><span class="c13"></span> <span class="c19">24</span><span class="c13">   |   | o   .   .   .   |  
|  </span><span class="c19">25</span><span class="c13"></span> <span class="c19">26</span><span class="c13"></span> <span class="c19">27</span><span class="c13"></span> <span class="c19">28</span><span class="c13"> |   |   .   O   o   . |  
|</span><span class="c19">29</span><span class="c13"></span> <span class="c19">30</span><span class="c13"></span> <span class="c19">31</span><span class="c13"></span> <span class="c19">32</span><span class="c13">   |   | .   .   .   .   |  
-----------------     -----------------</span>

</td>

</tr>

</tbody>

</table>

<span class="c4"></span>

<span class="c5">À ce stade de la partie, le joueur “</span><span class="c5">x</span><span class="c5">”</span><span class="c5">à 100% de chance de perdre la partie et il a la possibilité d’aller en :</span> <span class="c5">1-6</span><span class="c5">;</span> <span class="c5">14-17</span><span class="c5">et</span> <span class="c5">14-18.</span><span class="c4"> Dans la première version, il aurait choisi un mouvement aléatoire entre ces trois qui ont la même probabilité. Mais avec la version améliorée, on privilégiera le mouvement avec lequel on aura droit à des tours supplémentaire même si la défaite est imminente.</span>

<span class="c4"></span>

<span class="c4">En somme on peut dire qu’en début de partie l’impression d’équilibre entre deux IA avec des temps différents n’est qu’une illusion car celle avec le plus de temps de calcul effectue logiquement plus de partie aléatoires donc à plusieurs coups d’avances sur l’autre. Constat qui se confirme en milieu de partie où le nombre de déplacements possible croit largement. Ce qui signifie que l’arborescence est plus grande et les choix deviennent de plus en plus critiques.</span>

<span class="c5">Cependant, en fin de partie, où les coups possibles sont largement réduits, le temps de calcul perd plus ou moins son importance comme au</span> <span class="c5">tic-tac-toe</span><span class="c4"> ce qui fait qu’on a des parties très longues. Et le résultat est déterminé par l’avance pris en milieu de partie.</span>

<span class="c4">Aussi, les temps les plus longs ne concèdent aucunes victoires (dans nos tests) au temps les plus courts.</span>

<span class="c4"></span>

<span class="c4"> </span>

<span class="c4"></span>

<span class="c4">   </span>

<span class="c5"> </span>

<span class="c5"> </span>

<span class="c12">Annexe: Les résultats des parties de Dames Anglaise</span>

<span class="c12"></span>

<a id="t.f2782291412da7e001852aa804cc9322e2c164fa"></a><a id="t.9"></a>

<table class="c68">

<tbody>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c70" colspan="3" rowspan="1">

<span class="c12">1s vs  2s</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c65" colspan="3" rowspan="1">

<span class="c12">1s vs 5s</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c65" colspan="3" rowspan="1">

<span class="c12">1s vs 10s</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">8x8</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">2</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">4</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">10x10</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">2</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">6x6</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">2</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

</tr>

</tbody>

</table>

<span class="c12"></span>

<span class="c12"></span>

<a id="t.31d87c38d7e2f9a4ec3aa8249398413f48dac29f"></a><a id="t.10"></a>

<table class="c68">

<tbody>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c70" colspan="3" rowspan="1">

<span class="c12">2s vs 5s</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c65" colspan="3" rowspan="1">

<span class="c12">2s  vs 10s</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">8x8</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">10x10</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">6x6</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c20" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

</tr>

</tbody>

</table>

<span class="c12"></span>

<span class="c12"></span>

<a id="t.a193cbbdc4b2b676a4c306ca1f30f2bf92d3487f"></a><a id="t.11"></a>

<table class="c68">

<tbody>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12"></span>

</td>

<td class="c63" colspan="3" rowspan="1">

<span class="c12">5s vs 10s</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">8x8</span>

</td>

<td class="c52" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c48" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c48" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">10x10</span>

</td>

<td class="c52" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c48" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c48" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

</tr>

<tr class="c0">

<td class="c21" colspan="1" rowspan="1">

<span class="c12">6x6</span>

</td>

<td class="c52" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

<td class="c48" colspan="1" rowspan="1">

<span class="c12">1</span>

</td>

<td class="c48" colspan="1" rowspan="1">

<span class="c12">0</span>

</td>

</tr>

</tbody>

</table>

<span class="c4"></span>

<div>

<span class="c27 c50 c61"></span>

</div>