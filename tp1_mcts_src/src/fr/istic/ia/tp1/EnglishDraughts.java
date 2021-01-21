package fr.istic.ia.tp1;

import java.util.*;
import java.util.function.Function;

import fr.istic.ia.tp1.Game.PlayerId;

/**
 * Implementation of the English Draughts game.
 * @author vdrevell
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
		return board.isEmpty(square) ;
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
		int tailleDamier1 = board1.size;
		boolean lignePaire = board1.lineOfSquare(from) == 0 ;
		int mouvement  = lignePaire? tailleDamier1/2-1 : tailleDamier1/2  ;
		ArrayList<Integer> result = new ArrayList<>();

	 if(dame){
		if (!bas) {
			if (!gauche)  result.add(from+mouvement);
			if(!droite) result.add(from+mouvement+1) ;
		}
		if (!haut){
			if (!gauche) result.add(from-mouvement);
			if(!droite) result.add(from-mouvement-1);
		}
		return result ;
	 }
	 if (blanc){
		 if (!haut){
			 if (!gauche) result.add(from-mouvement);
			 if(!droite) result.add(from-mouvement-1);
		 }
		 return result ;
	 }

		if (!bas) {
			if (!gauche)  result.add(from+mouvement);
			if(!droite) result.add(from+mouvement+1) ;
		}
		return result ;

	}

	/**
	 * fonction auxciliaire qui retourne les deplacements possibles sans captures
	 * @return
	 */
	private List<Move> moveSansCapture(){
		ArrayList<Move> mouvementSansPrise = new ArrayList<>();
		Iterator<Integer> it = myPawns().iterator() ;
		Integer from = it.next();
		boolean estBlanc = playerId == PlayerId.ONE;
		boolean estDame = board.isKing(from);
		while (it.hasNext()){
			DraughtsMove draughtsMove = new DraughtsMove() ;
			ArrayList<Integer> movCourant = calculMov(from , estDame ,estBlanc , board);
			if (movCourant.size() >=1){
				draughtsMove.add(from);
				draughtsMove.addAll(movCourant);
				mouvementSansPrise.add(draughtsMove);
			}
			from = it.next() ;
		}

		return mouvementSansPrise;
	}

	/**
	 * le nombre de cases pour le deplacement
	 */
	private int nombreDecases(int from , CheckerBoard board1){
		int tailleDamier = board1.size;
		boolean lignePaire = board1.lineOfSquare(from) == 0 ;
		int mouvement  = lignePaire? tailleDamier/2-1 : tailleDamier/2  ;
		return mouvement ;
	}
	/**
	 * deplacement en prenant en compte les autre pions du jeu
	 * @return
	 */
	private  List<Move> moveAvecCapture(){
		ArrayList<Move> mouvementAvecCapture = new ArrayList<>();
		List<Move> mouvementsansCapture = moveSansCapture();
		Iterator<Move> it = mouvementsansCapture.iterator();
		DraughtsMove move = (DraughtsMove) it.next();
		while (it.hasNext()){
			DraughtsMove draughtsMove = new DraughtsMove() ;
			Iterator<Integer> moveIt = move.iterator() ;
			Integer from = moveIt.next();
			while (moveIt.hasNext()){
			Integer current =	moveIt.next();
			boolean stop = false ;
			while (!stop){
				int mouv1 = nombreDecases(from , board);
				int mouv2 = nombreDecases(current , board);
				 if (isAdversary(current)) {
					if(from < current){
						if (current-from == mouv1){
							if (isEmpty(mouv2)) draughtsMove.add(mouv2);
						}else{
							if(isEmpty(mouv2+1)) draughtsMove.add(mouv2+1);
						}
					}
					else{
						if (from-current == mouv1){
							if (isEmpty(mouv2)) draughtsMove.add(mouv2);
						}
						else {
							if (isEmpty(mouv2-1)) draughtsMove.add(mouv2-1);

						}
					}stop = !stop ;
				}
				 else if (isEmpty(current)) {
					 draughtsMove.add(current);
					 stop = !stop ;
				 }
				 //TODO REFELCHIR À LA RECURSIVITÉ
			}



			}
			mouvementAvecCapture.add(draughtsMove);
		}

		return mouvementAvecCapture ;

	}
	/**
	 * Generate the list of possible moves
	 * - first check moves with captures
	 * - if no capture possible, return displacement moves
	 */
	@Override
	public List<Move> possibleMoves() {
		//
		// TODO generate the list of possible moves
		//
		// Advice: 
		// create two auxiliary functions :
		// - one for jump moves from a given position, with capture (and multi-capture).
		//    Use recursive calls to explore all multiple capture possibilities
		// - one function that returns the displacement moves from a given position (without capture)
		//
		ArrayList<Move> moves = new ArrayList<Move>();
		return moves;
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
		
		
		//
		// TODO implement play
		//
		
		// Move pawn and capture opponents
		
		// Promote to king if the pawn ends on the opposite of the board
		
		// Next player
		
		// Update nbTurn
		
		// Keep track of successive moves with kings wthout capture

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
		return null;
	}
}
