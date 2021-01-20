package fr.istic.ia.tp1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.istic.ia.tp1.Game.Move;
import fr.istic.ia.tp1.Game.PlayerId;

/**
 * A class implementing a Monte-Carlo Tree Search method (MCTS) for playing two-player games ({@link Game}).
 * @author vdrevell
 *
 */
public class MonteCarloTreeSearch {

	/**
	 * A class to represent an evaluation node in the MCTS tree.
	 * This is a member class so that each node can access the global statistics of the owning MCTS.
	 * @author vdrevell
	 *
	 */
	class EvalNode {
		/** The number of simulations run through this node */
		int n;
		
		/** The number of winning runs */
		double w;
		
		/** The game state corresponding to this node */
		Game game;
		
		/** The children of the node: the games states accessible by playing a move from this node state */
		ArrayList<EvalNode> children;
		
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
			//
			// TODO implement the UCT function (Upper Confidence Bound for Trees)
			//
			return 0.0;
		}
		
		/**
		 * "Score" of the node, i.e estimated probability of winning when moving to this node
		 * @return Estimated probability of win for the node
		 */
		double score() {
			//
			// TODO implement the score function for a node
			//
			return 0.0;
		}
		
		/**
		 * Update the stats (n and w) of the node with the provided rollout results
		 * @param res
		 */
		void updateStats(RolloutResults res) {
			//
			// TODO implement updateStats for a node
			//
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
		 * @param winner
		 */
		public void update(PlayerId winner) {
			//
			// TODO implement the update of RolloutResults
			//
		}
		
		/**
		 * Getter for the number of wins of a player
		 * @param playerId
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
	}
	
	/**
	 * Perform a single random playing rollout from the given game state
	 * @param game Initial game state. {@code game} will contain an ended game state when the function returns.
	 * @return The PlayerId of the winner (or NONE if equality or timeout).
	 */
	static PlayerId playRandomlyToEnd(Game game) {
		//
		// TODO implement playRandomlyToEnd
		//
		return null;
	}
	
	/**
	 * Perform nbRuns rollouts from a game state, and returns the winning statistics for both players.
	 * @param game The initial game state to start with (not modified by the function)
	 * @param nbRuns The number of playouts to perform
	 * @return A RolloutResults object containing the number of wins for each player and the number of simulations
	 */
	static RolloutResults rollOut(final Game game, int nbRuns) {
		//
		// TODO implement rollOut
		//
		return null;
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
		//
		// TODO implement MCTS evaluateTreeOnce
		//
		
		// List of visited nodes
		
		// Start from the root
		
		// Selection (with UCT tree policy)
		
		// Expand node
		
		// Simulate from new node(s)
		
		// Backpropagate results
		
		// Return false if tree evaluation should continue
		return false;
	}
	
	/**
	 * Select the best move to play, given the current MCTS tree playout statistics
	 * @return The best move to play from the current MCTS tree state.
	 */
	public Move getBestMove() {
		// 
		// TODO Implement MCTS getBestMove
		//
		return null;
	}
	
	
	/**
	 * Get a few stats about the MTS tree and the possible moves scores
	 * @return A string containing MCTS stats
	 */
	public String stats() {
		String str = "MCTS with " + nTotal + " evals\n";
		Iterator<Move> itMove = root.game.possibleMoves().iterator();
		for (EvalNode node : root.children) {
			Move move = itMove.next();
			double score = node.score();
			str += move + " : " + score + " (" + node.w + "/" + node.n + ")\n";
		}
		return str;
	}
}
