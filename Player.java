import java.util.HashSet;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;

// Plays the solitaire game. Has a Board it's playing, thinks about 
// and makes Moves.
public class Player {

	private Board board;
	private ArrayDeque<Move> moveQueue;
	
	public Player(Board b) {
		board = b;
		moveQueue = new ArrayDeque<Move>();
	}

	// TODO: Return list of moves used to solve the board.
	public void solveBoard() {
		System.out.println("Solving board: ");
		board.print();
		long startTime = System.nanoTime();

		// Set of all board states, used for dynamic programming.
		HashSet<Long> boardsSeen = new HashSet<Long>();
		boardsSeen.add(board.toLong());
		
		// Some debugging metrics.
		int gamesFinished = 0;
		int uniqueBoardsSeen = 0;
		int duplicateBoardsAvoided = 0;

		// Start by looking through the whole board for every possible
		// valid move.
		moveQueue.addAll(getAllPossibleMoves());

		if (moveQueue.isEmpty()) {
			System.out.println("Board didn't have any starting moves.");
			board.print();
			return;
		}

		while (!moveQueue.isEmpty() && !board.isSolved()) {
			// We don't actually want to pop because we need to know
			// how to retrace our steps if we reach a dead end. So leave
			// the moves on the stack and we'll pop() as we backtrack.			
			Move m = moveQueue.peekLast();
			
			if (!m.hasBeenPerformed()) {
				board.doMove(m);
			} else {
				board.revertMove(m);
				// Okay now that the move has been reversed we can forget it.
				moveQueue.removeLast();
				continue;
			}


			if (isUniqueBoardState(board, boardsSeen)) {
				uniqueBoardsSeen++;
				boardsSeen.add(board.toLong());
			} else {
				duplicateBoardsAvoided++;
				// We've already been down this path... so abort.
				revertMove(m);
				moveQueue.removeLast();
				continue;
			}		

			if (board.isSolved()) {
				System.out.println("Solved it!");
				board.print();
				return;
			}

			// Find new moves for the given board state.
			ArrayList<Move> possibleMoves = getAllPossibleMoves();

			if (possibleMoves.size() > 0) {
				Collections.sort(possibleMoves);
				moveQueue.addAll(getAllPossibleMoves());
			} else {
				gamesFinished++;
				// Print some progress info just to monitor things.
				if (gamesFinished % 1000 == 0) {
					double duration = System.nanoTime() - startTime;
					double nanosPerSecond = 1000000000;
					duration /= nanosPerSecond;
					System.out.println("Unique: " + uniqueBoardsSeen + 
						"; Redundant: " + duplicateBoardsAvoided + 
						"; Games finished: " + gamesFinished +
						"; Duration: " + duration);
					board.print();
				}
			}	
		}

	}

	private ArrayList<Move> getAllPossibleMoves() {
		ArrayList<Move> allMoves = new ArrayList<Move>();

		// Detect empty locations on the board.
		ArrayList<Node> empties = new ArrayList<Node>();
		for (int j = 0; j < Board.SIZE; j++) {
			for (int i = 0; i < Board.SIZE; i++) {
				Node n = board.getNode(i, j);
				if (n.isValid() && !n.isFilled()) empties.add(n);
			}
		}

		for (Node n : empties) {
			allMoves.addAll(movesAtLocation(n));
		}

		return allMoves;
	}

	public void doMove(Move m) {
		// Make sure it's still a valid move.
		if (!m.isValid()) {
			if (!m.equals(moveQueue.peekLast())) {
				// This should never happen.
				System.out.println("Uh oh something wrong with the move stack");
			}
			// This should be safe since we're always dealing with the
			// top element.
			moveQueue.removeLast();
			return;
		}

		board.doMove(m);

		// Check if new moves are now available.
		moveQueue.addAll(movesAtLocation(m.n3));
		moveQueue.addAll(movesAtLocation(m.n2));
		moveQueue.addAll(movesAtLocation(m.n1));
	}

	// Return list of all valid moves involving node n.
	private ArrayList<Move> movesAtLocation(Node n) {
		if (n.isFilled()) {
			ArrayList<Move> moves = movesFromLocation(n);
			moves.addAll(movesOverLocation(n));
			return moves;
		} else {
			return movesToLocation(n);
		}
		
	}

	// Return list of all valid moves using location n to jump from.
	// Pre: n is filled.
	private ArrayList<Move> movesFromLocation(Node n) {
		ArrayList<Move> moves = new ArrayList<Move>();
		Node n1, n2; // Neighbours.

		// We want to jump from n over n1 to n2.

		// Up.
		n1 = board.getNode(n.x, n.y-1);
		n2 = board.getNode(n.x, n.y-2);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n, n1, n2));
		}

		// Down.
		n1 = board.getNode(n.x, n.y+1);
		n2 = board.getNode(n.x, n.y+2);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n, n1, n2));
		}

		// Left.
		n1 = board.getNode(n.x-1, n.y);
		n2 = board.getNode(n.x-2, n.y);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n, n1, n2));
		}

		// Right.
		n1 = board.getNode(n.x+1, n.y);
		n2 = board.getNode(n.x+2, n.y);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n, n1, n2));
		}

		return moves;
	}

	// Return list of all valid moves that jump over location n.
	// Pre: n is filled.
	private ArrayList<Move> movesOverLocation(Node n) {
		ArrayList<Move> moves = new ArrayList<Move>();
		Node n1, n2; // Neighbours.

		// We want to jump from n1 over n to n2

		// Up.
		n1 = board.getNode(n.x, n.y+1);
		n2 = board.getNode(n.x, n.y-1);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n1, n, n2));
		}

		// Down.
		n1 = board.getNode(n.x, n.y-1);
		n2 = board.getNode(n.x, n.y+1);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n1, n, n2));
		}

		// Left.
		n1 = board.getNode(n.x+1, n.y);
		n2 = board.getNode(n.x-1, n.y);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n1, n, n2));
		}

		// Right.
		n1 = board.getNode(n.x-1, n.y);
		n2 = board.getNode(n.x+1, n.y);
		if (n1 != null && n2 != null && 
			n1.isFilled() && !n2.isFilled()) {
			moves.add(new Move(n1, n, n2));
		}

		return moves;
	}

	// Return list of all valid moves that jump to location n.
	// Pre: n is empty.
	private ArrayList<Move> movesToLocation(Node n) {
		// Detect valid moves by examining surrounding locations on the board.
		ArrayList<Move> moves = new ArrayList<Move>();
		Node n1, n2; // Neighbours.

		// We want to jump from n2 over n1 to n.

		// Up.
		n1 = board.getNode(n.x, n.y-1);
		n2 = board.getNode(n.x, n.y-2);
		if (n1 != null && n2 != null && 
			n1.isFilled() && n2.isFilled()) {
			moves.add(new Move(n2, n1, n));
		}

		// Down.
		n1 = board.getNode(n.x, n.y+1);
		n2 = board.getNode(n.x, n.y+2);
		if (n1 != null && n2 != null && 
			n1.isFilled() && n2.isFilled()) {
			moves.add(new Move(n2, n1, n));
		}

		// Left.
		n1 = board.getNode(n.x-1, n.y);
		n2 = board.getNode(n.x-2, n.y);
		if (n1 != null && n2 != null && 
			n1.isFilled() && n2.isFilled()) {
			moves.add(new Move(n2, n1, n));
		}

		// Right.
		n1 = board.getNode(n.x+1, n.y);
		n2 = board.getNode(n.x+2, n.y);
		if (n1 != null && n2 != null && 
			n1.isFilled() && n2.isFilled()) {
			moves.add(new Move(n2, n1, n));
		}

		return moves;
	}

	public void revertMove(Move m) {
		board.revertMove(m);
	}

	// Return true if the board doesn't already exist within the set of boards.
	private static boolean isUniqueBoardState(Board b, HashSet<Long> boards) {
		// TODO: This function is really slow. The clone, rotation, mirror, and
		// toLong calls are all O(n).
		Board b2 = new Board(b);

		// Check all rotations of the board.
		for (int i = 0; i < 4; i++) {
			if (boards.contains(b2.toLong())) return false;
			b2.rotate();
		}

		// Now try the mirrored rotations too.
		b2.mirror();
		for (int i = 0; i < 4; i++) {
			if (boards.contains(b2.toLong())) return false;
			b2.rotate();
		}

		return true;
	}

}



