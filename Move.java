import java.lang.Math;

public class Move implements Comparable {
	// Moves jump a stone from n1 to n3, removing the stone from n2.

	public Node n1;
	public Node n2;
	public Node n3;
	private boolean isPerformed;

	public Move(Node n1, Node n2, Node n3) {
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.isPerformed = false;
	}

	public boolean isValid() {
		return n1.isFilled() && n2.isFilled() && !n3.isFilled();
	}

	public void setPerformed() {
		isPerformed = true;
	}

	public boolean hasBeenPerformed() {
		return isPerformed;
	}

	public int compareTo(Object m) {
		Move move = (Move) m;

		// We'll say a move is greater if its start location (n1) is farther
		// from the centre of the board.
		int CENTRE = Board.SIZE / 2;
		float d1 = Math.abs(n1.x - CENTRE) + Math.abs(n1.y - CENTRE);
		float d2 = Math.abs(move.n1.x - CENTRE) + Math.abs(move.n1.y - CENTRE);
		
		if (d1 < d2) {
			return 1;
		} else {
			return -1;
		}
	}

}