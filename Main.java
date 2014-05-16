import java.util.HashSet;
import java.util.ArrayList;

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

		if (b.isDone()) {
			System.out.println("Solved.");
			return;
		}

		HashSet<Long> boards = new HashSet<Long>();
		boards.add(b.toLong());
		
		int uniqueBoards = 0;
		int redundantBoards = 0;

		ArrayList<Move> moves = b.getMoves();
		while (!moves.isEmpty()) {
			Move m = moves.get(moves.size() - 1);
			if (m.isPerformed()) {
				b.doReversal(m);
				moves.remove(moves.size() - 1);
				continue;
			}
			b.doMove(m);

			if (b.isDone()) {
				System.out.println("Found one.");
				b.print();
				return;
			}

			if (!isUnseenState(b, boards)) {
				redundantBoards++;
			} else {
				uniqueBoards++;
				boards.add(b.toLong());
				ArrayList<Move> newMoves = b.getMoves();
				if (newMoves.isEmpty()) {
					System.out.println("Unique: " + uniqueBoards + "; Redundant: " + redundantBoards);
					b.print();
				}
				moves.addAll(b.getMoves());
			}
//			if (uniqueBoards % 10000 == 0) {
//				System.out.println("Unique: " + uniqueBoards + "; Redundant: " + redundantBoards);
//				b.print();
//			}
			
		}

		System.out.println("Done.");
	}

	private static boolean isUnseenState(Board b, HashSet<Long> boards) {
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