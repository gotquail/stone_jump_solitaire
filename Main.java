import java.util.HashSet;
import java.util.ArrayList;
import java.util.Stack;

public class Main {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Args: board_filename");
			return;
		}

		Board b1 = new Board(args[0]);

		solve(b1);
	}

	public static void solve(Board b) {
		System.out.println("Solving board: ");
		b.print();
		long startTime = System.nanoTime();

		// Set of all board states, used for dynamic programming.
		HashSet<Long> boards = new HashSet<Long>();
		boards.add(b.toLong());
		
		// Some debugging metrics.
		int gamesFinished = 0;
		int uniqueBoards = 0;
		int redundantBoards = 0;
		
		Stack<Move> moves = new Stack();
		for (Move m : b.getMoves()) moves.push(m);

		if (moves.empty()) {
			System.out.println("Uhh board had no moves...");
			b.print();
			return;
		}

		while (!moves.empty()) {
			// We don't actually want to pop because we need to know
			// how to retrace our steps if we reach a dead end. So leave
			// the moves on the stack and we'll pop() as we backtrack.			
			Move m = moves.peek();

			if (m.hasBeenPerformed()) {
				b.revertMove(m);
				// Okay now that the move has been reversed we can forget it.
				moves.pop();
				continue;
			}

			b.doMove(m);

			if (b.isSolved()) {
				System.out.println("Solved it!");
				b.print();
				return;
			}

			if (isUniqueBoardState(b, boards)) {
				uniqueBoards++;
				boards.add(b.toLong());
				ArrayList<Move> newMoves = b.getMoves();
				if (newMoves.isEmpty()) {
					gamesFinished++;
					// Print some progress info just to monitor things.
					if (gamesFinished % 1000 == 0) {
						double duration = System.nanoTime() - startTime;
						double nanosPerSecond = 1000000000;
						duration /= nanosPerSecond;
						System.out.println("Unique: " + uniqueBoards + 
							"; Redundant: " + redundantBoards + 
							"; Games finished: " + gamesFinished +
							"; Duration: " + duration);
						b.print();
					}
				}
				moves.addAll(newMoves);
			} else {
				redundantBoards++;
			}			
		}

		System.out.println("Done.");
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