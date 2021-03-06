package fr.istic.ia.tp1;

import java.util.*;
import java.util.concurrent.TimeUnit;

import fr.istic.ia.tp1.Game.Move;
import fr.istic.ia.tp1.Game.PlayerId;

/**
 * A class implementing a Monte-Carlo Tree Search method (MCTS) for playing two-player games ({@link Game}).
 * @author vdrevell
 * @author Yao Arnaud Akoto
 * @author Kassim Kaba
 *
 */
public class MonteCarloTreeSearch {

	/**
	 * A class to represent an evaluation node in the MCTS tree.
	 * This is a member class so that each node can access the global statistics of the owning MCTS.
	 * @author vdrevell
	 *
	 */
	static class EvalNode  {
		/** The number of simulations run through this node */
		int n;
		
		/** The number of winning runs */
		double w;
		
		/** The game state corresponding to this node */
		Game game;
		
		/** The children of the node: the games states accessible by playing a move from this node state */
		ArrayList<EvalNode> children;

		/**
		 * mouvement-noeud
		 */
		Move move_node ;

		/**
		 * parent
		 */
		EvalNode parent;


		/** 
		 * The only constructor of EvalNode.
		 * @param game The game state corresponding to this node.
		 */
		EvalNode(Game game) {

			this.game = game;
			children = new ArrayList<EvalNode>();
			w = 0.0;
			n = 0;
		}
		
		/**
		 * Compute the Upper Confidence Bound for Trees (UCT) value for the node.
		 * @return UCT value for the node
		 */
		double uct() {
			double INFINI = Double.MAX_VALUE;
			double c = Math.sqrt(2);
			double lnN = Math.log(parent.n);
			return n == 0 ? INFINI : this.score() + c * Math.sqrt(lnN/n) ;

		}
		
		/**
		 * "Score" of the node, i.e estimated probability of winning when moving to this node
		 * @return Estimated probability of win for the node
		 */
		double score() {
			return n==0? 0 : w/n ;
		}
		
		/**
		 * Update the stats (n and w) of the node with the provided rollout results
		 * @param res ""
		 */
		void updateStats(RolloutResults res) {
			n += res.nbSimulations();
			w += res.nbWins(Objects.requireNonNull(this.game.player().other())) ;
		}

		/**
		 * verifie si le coup suivant est une défaite
		 * @param node ""
		 * @return ""
		 */
		boolean verfDefaiteImediate(EvalNode node){
			EvalNode nodeDeVerificationDeDefaite = new EvalNode(node.game.clone());
			nodeDeVerificationDeDefaite.genererFils();
			for (EvalNode n : nodeDeVerificationDeDefaite.children){
				if (n.game.winner() !=null)
				return true;
			}
			return false;
		}

		/**
		 * methode qui genere les fils d'un noeud
		 *
		 */
		void genererFils(){

			for (Move deplavementNode : this.game.possibleMoves()) {
				Game gameCurent = this.game.clone();
				gameCurent.play(deplavementNode);
				EvalNode node = new EvalNode(gameCurent);
				node.move_node = deplavementNode;
				this.children.add(node);
				node.parent = this;
			}
		}

		/**
		 * La phase de sélection
		 * @return la feille la plus prometeuse
		 */
		EvalNode meilleurFeuille (){
			EvalNode node = null;
			for(EvalNode nodeCourent : this.children) {

				//si un noeud donne directement un victoire on n'a plus besoin de calculer
				if (nodeCourent.game.winner() != null)
					return nodeCourent;

				//verifier si ya defaite imédiate au prochain tour
				boolean actualise = !verfDefaiteImediate(nodeCourent);
				if (node == null) {
					if (actualise)
						node = nodeCourent;
				}
				else if (nodeCourent.uct() > node.uct() && actualise) {
					node = nodeCourent;
				}
			}
			if(node == null){
				node = children.get(new Random().nextInt(children.size())) ;
			}
			if (!node.children.isEmpty())
				node = node.meilleurFeuille();

			return  node ;
		}

		/**
		 * choix du coup à jouer
		 * @return le meilleur fils
		 */
		EvalNode meilleurFils (){
			if (children.isEmpty()) return null ;
			EvalNode node = null;
			// Selection (with UCT tree policy)
			for(EvalNode nodeCourent : this.children){
				if (node == null)
					node = nodeCourent;
				else{
					if (node.score() < nodeCourent.score()) {
						node = nodeCourent;
					}
					else if (node.score() == nodeCourent.score()){
						if (node.n < nodeCourent.n)
							node =nodeCourent;
					}
				}
			}
			return  node ;
		}

		@Override
		public int hashCode() {
			return Objects.hash(n, w, game, children, move_node);
		}
	}
	
	/**
	 * A class to hold the results of the rollout phase
	 * Keeps the number of wins for each player and the number of simulations.
	 * @author vdrevell
	 *
	 */
	static class RolloutResults {
		/** The number of wins for player 1 {@link PlayerId#ONE}*/
		double win1;
		
		/** The number of wins for player 2 {@link PlayerId#TWO}*/
		double win2;
		
		/** The number of playouts */
		int n;
		
		/**
		 * The constructor
		 */
		public RolloutResults() {
			reset();
		}
		
		/**
		 * Reset results
		 */
		public void reset() {
			n = 0;
			win1 = 0.0;
			win2 = 0.0;
		}
		
		/**
		 * Add other results to this
		 * @param res The results to add
		 */
		public void add(RolloutResults res) {
			win1 += res.win1;
			win2 += res.win2;
			n += res.n;
		}
		
		/**
		 * Update playout statistics with a win of the player <code>winner</code>
		 * Also handles equality if <code>winner</code>={@link PlayerId#NONE}, adding 0.5 wins to each player
		 * @param winner ""
		 */
		public void update(PlayerId winner) {
			if (winner == PlayerId.ONE) {
				win1 = win1+1;
			}
			else if (winner == PlayerId.TWO){
				win2 = win2+1;
			}else {
				win1 = win1 + 0.5;
				win2 = win2 + 0.5;

			}
			n++;
		}
		
		/**
		 * Getter for the number of wins of a player
		 * @param playerId ""
		 * @return The number of wins of player <code>playerId</code>
		 */
		public double nbWins(PlayerId playerId) {
			switch (playerId) {
			case ONE: return win1;
			case TWO: return win2;
			default: return 0.0;
			}
		}
		
		/**
		 * Getter for the number of simulations
		 * @return The number of playouts
		 */
		public int nbSimulations() {
			return n;
		}
	}
	
	/**
	 * The root of the MCTS tree
	 */
	EvalNode root;
	
	/**
	 * The total number of performed simulations (rollouts)
	 */
	int nTotal;

	/**
	 * The constructor
	 * @param game
	 */
	public MonteCarloTreeSearch(Game game) {
		root = new EvalNode(game.clone());
		nTotal = 0;
		root.parent =null ;
		root.genererFils();
	}
	
	/**
	 * Perform a single random playing rollout from the given game state
	 * @param game Initial game state. {@code game} will contain an ended game state when the function returns.
	 * @return The PlayerId of the winner (or NONE if equality or timeout).
	 */
	static PlayerId playRandomlyToEnd(Game game) {
		// Game loop until the end of the game
		Player player = new PlayerRandom() ;
		while (game.winner() == null) {
			game.play(player.play(game));
		}
		return game.winner();
	}
	
	/**
	 * Perform nbRuns rollouts from a game state, and returns the winning statistics for both players.
	 * @param game The initial game state to start with (not modified by the function)
	 * @param nbRuns The number of playouts to perform
	 * @return A RolloutResults object containing the number of wins for each player and the number of simulations
	 */
	static RolloutResults rollOut(final Game game, int nbRuns) {
		RolloutResults rolloutResults = new RolloutResults() ;
		for (int i = 0; i < nbRuns; i++) {
			rolloutResults.update(playRandomlyToEnd(game));
		}
		return rolloutResults;
	}
	
	/**
	 * Apply the MCTS algorithm during at most <code>timeLimitMillis</code> milliseconds to compute
	 * the MCTS tree statistics.
	 * @param timeLimitMillis Computation time limit in milliseconds
	 */
	public void evaluateTreeWithTimeLimit(int timeLimitMillis) {
		// Record function entry time
		long startTime = System.nanoTime();

		// Evaluate the tree until timeout
		while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) < timeLimitMillis) {
			// Perform one MCTS step
			boolean canStop = evaluateTreeOnce();
			// Stop evaluating the tree if there is nothing more to explore
			if (canStop) {
				break;
			}
		}
		
		// Print some statistics
		System.out.println("Stopped search after " 
		       + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + " ms. "
		       + "Root stats is " + root.w + "/" + root.n + String.format(" (%.2f%% loss)", 100.0*root.w/root.n));
	}
	
	/**
	 * Perform one MCTS step (selection, expansion(s), simulation(s), backpropagation
	 * @return <code>true</code> if there is no need for further exploration (to speed up end of games).
	 */
	public boolean evaluateTreeOnce() {
		if(root.children.isEmpty() || root.children.size() ==1) {
			return true ;
		}
		// List of visited nodes
		// Start from the root
		// Selection (with UCT tree policy)
		EvalNode node = root.meilleurFeuille();
		// Expand node
		// Simulate from new node(s)
		//jouer alléatoirement entre 1 et 1000 coups
		node.genererFils();
		RolloutResults res = rollOut(node.game.clone(), 1);

		// Backpropagate results
		node.updateStats(res);
		while (node.parent != null){
			node.parent.updateStats(res);
			node = node.parent ;
		}
		nTotal = root.n;

		//si c'est une victoire on arrête le calcule
		boolean victoireDirecte = root.meilleurFils().game.winner() == root.game.player();
		//si on se sauve d'une situation de féfaites on arête aussi
		boolean protection = seSauverAvec(root) ==1 ;
		// Return false if tree evaluation should continue
		return victoireDirecte || protection;
	}

	/**
	 * retourne le nobre de noeuds avec le quel on peu se sauver d'une défaite imédiate
	 * @param node
	 * @return
	 */
	int seSauverAvec(EvalNode node){
		int nbr =0;
		for (EvalNode n : node.children){
			if (!node.verfDefaiteImediate(n)) nbr++;
		}
		return nbr;
	}
	
	/**
	 * Select the best move to play, given the current MCTS tree playout statistics
	 * @return The best move to play from the current MCTS tree state.
	 */
	public Move getBestMove() {
		EvalNode node = root.meilleurFils();

		System.out.println(stats());
		System.out.println("############ Choix : ");
		System.out.println(node.move_node + " : " + node.score() + " (" + node.w + "/" + node.n + ")");
		System.out.println("############");

		return node.move_node;
	}
	
	
	/**
	 * Get a few stats about the MTS tree and the possible moves scores
	 * @return A string containing MCTS stats
	 */
	public String stats() {
		String str = "MCTS with " + nTotal + " evals\n";
		for (EvalNode node : root.children) {
			double score = node.score();
			str += node.move_node + " : " + score + " (" + node.w + "/" + node.n + ")\n";
		}
		return str;
	}
}
