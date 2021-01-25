package fr.istic.ia.tp1;

import java.util.*;
import java.util.function.Function;

import fr.istic.ia.tp1.Game.PlayerId;

import javax.swing.*;

/**
 * Implementation of the English Draughts game.
 * @author vdrevell
 * @author Yao Arnaud Akoto
 * @author kassim kaba
 *
 */
public class EnglishDraughts extends Game {
	/** 
	 * The checker board
	 */
	CheckerBoard board;
	
	/** 
	 * The {@link PlayerId} of the current player
	 * {@link PlayerId#ONE} corresponds to the whites
	 * {@link PlayerId#TWO} corresponds to the blacks
	 */
	PlayerId playerId;
	
	/**
	 * The current game turn (incremented each time the whites play)
	 */
	int nbTurn;
	
	/**
	 * The number of consecutive moves played only with kings and without capture
	 * (used to decide equality)
	 */
	int nbKingMovesWithoutCapture;

	/**
	 * map pour les prise
	 * (move , ArrayList)
	 */
	private Map<Move,ArrayList<Integer>> lesPrisesPossibles = new HashMap<>();
	
	/**
	 * Class representing a move in the English draughts game
	 * A move is an ArrayList of Integers, corresponding to the successive tile numbers (Manouri notation)
	 * toString is overrided to provide Manouri notation output.
	 * @author vdrevell
	 *
	 */
	class DraughtsMove extends ArrayList<Integer> implements Game.Move {

		private static final long serialVersionUID = -8215846964873293714L;

		@Override
		public String toString() {
			Iterator<Integer> it = this.iterator();
			Integer from = it.next();
			StringBuffer sb = new StringBuffer();
			sb.append(from);
			while (it.hasNext()) {
				Integer to = it.next();
				if (board.neighborDownLeft(from)==to || board.neighborUpLeft(from)==to
						|| board.neighborDownRight(from)==to || board.neighborUpRight(from)==to) {
					sb.append('-');
				}
				else {
					sb.append('x');
				}
				sb.append(to);
				from = to;
			}
			return sb.toString();
		}
	}
	
	/**
	 * The default constructor: initializes a game on the standard 8x8 board.
	 */
	public EnglishDraughts() {
		this(8);
	}
	
	/**
	 * Constructor with custom boardSize (to play on a boardSize x boardSize checkerBoard).
	 * @param boardSize See {@link CheckerBoard#CheckerBoard(int)} for valid board sizes. 
	 */
	public EnglishDraughts(int boardSize) {
		this.board = new CheckerBoard(boardSize);
		this.playerId = PlayerId.ONE;
		this.nbTurn = 1;
		this.nbKingMovesWithoutCapture = 0;
	}
	
	/**
	 * Copy constructor
	 * @param d The game to copy
	 */
	EnglishDraughts(EnglishDraughts d) {
		this.board = d.board.clone();
		this.playerId = d.playerId;
		this.nbTurn = d.nbTurn;
		this.nbKingMovesWithoutCapture = d.nbKingMovesWithoutCapture;
	}
	
	@Override
	public EnglishDraughts clone() {
		return new EnglishDraughts(this);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(nbTurn);
		sb.append(". ");
		sb.append(this.playerId==PlayerId.ONE?"W":"B");
		sb.append(":");
		sb.append(board.toString());
		return sb.toString();
	}
	
	@Override
	public String playerName(PlayerId playerId) {
		switch (playerId) {
		case ONE:
			return "Player with the whites";
		case TWO:
			return "Player with the blacks";
		case NONE:
		default:
			return "Nobody";
		}
	}
	
	@Override
	public String view() {
		return board.boardView() + "Turn #" + nbTurn + ". " + playerName(playerId) + " plays.\n";
	}
	
	/**
	 * Check if a tile is empty
	 * @param square Tile number
	 * @return
	 */
	boolean isEmpty(int square) {
		try {
			return board.isEmpty(square);
		} catch (Exception e) {
			return false;
		}
	}
	
	/** 
	 * Check if a tile is owned by adversary
	 * @param square Tile number
	 * @return
	 */
	boolean isAdversary(int square) {
		return ((this.playerId==PlayerId.ONE && board.isBlack(square))||
				(this.playerId==PlayerId.TWO && board.isWhite(square)));
	}
	
	/** 
	 * Check if a tile is owned by the current player
	 * @param square Tile number
	 * @return
	 */
	boolean isMine(int square) {
		return ((this.playerId==PlayerId.ONE && board.isWhite(square))||
				(this.playerId==PlayerId.TWO && board.isBlack(square)));
	}
	
	/** 
	 * Retrieve the list of positions of the pawns owned by the current player
	 * @return The list of current player pawn positions
	 */
	ArrayList<Integer> myPawns() {
		return (this.playerId == PlayerId.ONE)
				?board.getWhitePawns()
				:(this.playerId == PlayerId.TWO)
					?board.getBlackPawns()
					: new ArrayList<>();
	}

	/**
	 * fonction de calcule du déplacement
	 */
	private ArrayList<Integer> calculMov (Integer from, boolean dame , boolean blanc,
											  CheckerBoard board1){
		boolean bas , haut , gauche , droite ;
		gauche = board1.inLeftRow(from);
		droite = board1.inRightRow(from);
		haut = board1.inTopRow(from);
		bas = board1.inBottomRow(from);
		ArrayList<Integer> result = new ArrayList<>();

		//si c'est un king

		if (!bas) {
			if(dame || !blanc) {
				if (!gauche) {
					int to = board1.neighborDownLeft(from);
					if (to!=0) result.add(to);
				}
				if (!droite) {
					int to = board1.neighborDownRight(from);
					if (to!=0) result.add(to);
				}
			}
		}
		if (!haut){
			if (dame || blanc) {
				if (!gauche) {
					int to = board1.neighborUpLeft(from);
					if (to!=0) result.add(to);
				}
				if (!droite) {
					int to = board1.neighborUpRight(from);
					if (to!=0) result.add(to);
				}
			}
		}
		return result ;

	}

	/**
	 * fonction auxciliaire qui retourne les deplacements possibles sans captures et sans contrintes
	 * @return
	 */
	private List<Move> deplacementPossible(){
		ArrayList<Move> deplacementPossible = new ArrayList<>();
		Iterator<Integer> it = myPawns().iterator() ;
		while (it.hasNext()){
			Integer from = it.next();
			boolean estBlanc = playerId == PlayerId.ONE;
			boolean estDame = board.isKing(from);
			DraughtsMove draughtsMove = new DraughtsMove() ;
			ArrayList<Integer> movCourant = calculMov(from , estDame ,estBlanc , board);
			if (movCourant.size() >=1){
				draughtsMove.add(from);
				draughtsMove.addAll(movCourant);
				deplacementPossible.add(draughtsMove);
			}
		}

		return deplacementPossible;
	}

	/**
	 * mouvements sans prises
	 * @return
	 */
	private  List<Move> moveSansCapture(){
		List<Move> mouvementsansCapture = new ArrayList<>();
		List<Move> deplacementpossible = deplacementPossible();
		Iterator<Move> it = deplacementpossible.iterator();
		while (it.hasNext()){
			DraughtsMove move = (DraughtsMove) it.next();
			DraughtsMove draughtsMove = new DraughtsMove() ;
			Iterator<Integer> moveIt = move.iterator() ;
			Integer from = moveIt.next();
			draughtsMove.add(from);
			while (moveIt.hasNext()){
				Integer current =	moveIt.next();
				DraughtsMove moveCurent = new DraughtsMove();
				moveCurent.addAll(draughtsMove);
				if (isEmpty(current)) {
					moveCurent.add(current);
					mouvementsansCapture.add(moveCurent);

				}
			}
		}

		return mouvementsansCapture;
	}

	/**
	 * mov avec capture pour un pion
	 * @return
	 */
	private List<Move> movAvecCaptureForEach(int from, boolean blanc , boolean dame , CheckerBoard board1 ,
											 DraughtsMove drMove , ArrayList<Integer> prise){
		ArrayList<Integer> deplacementPossibleForEach = calculMov(from,dame,blanc,board1);
		List<Move> mouvements = new ArrayList<>();
		Iterator<Integer> it  = deplacementPossibleForEach.iterator();
		boolean continuer = false;
		while (it.hasNext()){
			ArrayList<Integer> priseCurent = new ArrayList<>();
			priseCurent.addAll(prise);
			boolean continuer1 = false;
			DraughtsMove draughtsMove = new DraughtsMove();
			draughtsMove.addAll(drMove);
			draughtsMove.add(from);
			Integer current = it.next();
			//si pion pas deja capturé
			if(!prise.contains(current)){
				if(!board1.inBottomRow(current)
						&& !board1.inTopRow(current)
						&& !board1.inLeftRow(current)
						&& !board1.inRightRow(current)){
					//si un adversaire
					if (isAdversary(current)){
						//ajouter le current dans les pions boffés
						//chercher le sens de deplacement
						int to  ;
						//haut-gauche
						if(board1.neighborUpLeft(from)==current) {
							to = board1.neighborUpLeft(current);

						}
						//haut-droite
						else if(board1.neighborUpRight(from)==current) {
							to =board1.neighborUpRight(current);
						}
						//bas-gauche
						else if(board1.neighborDownLeft(from)==current) {
							to = board1.neighborDownLeft(current);
						}
						//bas-droite
						else  {
							to = board1.neighborDownRight(current);
						}

						if (isEmpty(to)) {

							if (board1.inTopRow(to)||board1.inBottomRow(to)){
								draughtsMove.add(to);
								priseCurent.add(current);
							}else {
								ArrayList<Integer> prises = new ArrayList<>();
								prises.addAll(priseCurent);
								prises.add(current);
								mouvements.addAll(movAvecCaptureForEach(to, blanc, dame, board1, draughtsMove, prises));
								continuer1 = true;

							}

						}

					}
				}
			}
			continuer = continuer1 || continuer ;

			if ( !it.hasNext() && !continuer && (draughtsMove.size() >=2)){
				mouvements.add(draughtsMove);
				lesPrisesPossibles.put(draughtsMove,priseCurent);

			}

		}


		return mouvements;
	}

	/**
	 * Mouvement avec prise multiple
	 */
	private List<Move> movAvecCaptureForAll(){
		List<Move> mouvements = new ArrayList<>();
		List<Move> deplacementpossible = deplacementPossible();
		Iterator<Move> it = deplacementpossible.iterator();
		while (it.hasNext()){
			DraughtsMove move = (DraughtsMove) it.next();
			DraughtsMove draughtsMove = new DraughtsMove();
			Iterator<Integer> moveIt = move.iterator() ;
			Integer from = moveIt.next();
			boolean estBlanc = playerId ==PlayerId.ONE ;
			boolean estDame = board.isKing(from);
			ArrayList<Integer> prise = new ArrayList<>() ;
			List<Move> deplacementForEach = movAvecCaptureForEach(from,estBlanc,estDame,board,draughtsMove, prise);
			if (!deplacementForEach.isEmpty()){
				mouvements.addAll(deplacementForEach);
			}

		}

		return mouvements;
}

	/**
	 * deplacement en prenant en compte les pour un pion
	 * @return
	 */
	private  List<Move> moveAvecCapture(){
		return movAvecCaptureForAll() ;

	}
	/**
	 * Generate the list of possible moves
	 * - first check moves with captures
	 * - if no capture possible, return displacement moves
	 */
	@Override
	public List<Move> possibleMoves() {
		ArrayList<Move> moves = new ArrayList<>();
		moves.addAll(moveAvecCapture());
		return !moves.isEmpty()?moves : moveSansCapture();
	}

	@Override
	public void play(Move aMove) {
		// Player should be valid
		if (playerId == PlayerId.NONE)
			return;
		// We will cast Move to DraughtsMove (kind of ArrayList<Integer>
		if (!(aMove instanceof DraughtsMove))
			return;
		// Cast and apply the move
		DraughtsMove move = (DraughtsMove) aMove;

		// Move pawn and capture opponents
			//ajoter sur la dernier case et en fonction des sauts enlever les enemis
			Integer to = move.get(move.size()-1);
			Integer from = move.get(0);
			board.movePawn(from,to);
			if (lesPrisesPossibles.containsKey(move)){
				ArrayList<Integer> prises = lesPrisesPossibles.get(move);
				Iterator<Integer> it = prises.iterator() ;
				while (it.hasNext()){
					Integer pionCapture= it.next() ;
					board.removePawn(pionCapture);
				}
				lesPrisesPossibles.clear();
				nbKingMovesWithoutCapture =0;
			}else{
				// Keep track of successive moves with kings wthout capture
				nbKingMovesWithoutCapture ++;

			}

			// Promote to king if the pawn ends on the opposite of the board
				if (!board.isKing(to)){
					if (board.inBottomRow(to) || board.inTopRow(to)){
						board.crownPawn(to);
					}
				}

			// Next player
			playerId = playerId.other();

			// Update nbTurn
			nbTurn++;
		


	}

	@Override
	public PlayerId player() {
		return playerId;
	}

	/**
	 * Get the winner (or null if the game is still going)
	 * Victory conditions are :
	 * - adversary with no more pawns or no move possibilities
	 * Null game condition (return PlayerId.NONE) is
	 * - more than 25 successive moves of only kings and without any capture
	 */
	@Override
	public PlayerId winner() {
		//
		// TODO implement winner
		//
		
		// return the winner ID if possible
		
		// return PlayerId.NONE if the game is null
		
		// Return null is the game has not ended yet
		if (nbKingMovesWithoutCapture >= 25) return  PlayerId.NONE;
		if (board.getBlackPawns().size() <= 0 ) return PlayerId.ONE;
		if (board.getWhitePawns().size() <= 0) return  PlayerId.TWO;
		if (possibleMoves().isEmpty()) return  playerId.other();
		return null;
	}
}
