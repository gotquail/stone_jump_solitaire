public class Move {
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

	public void setPerformed() {
		isPerformed = true;
	}

	public boolean hasBeenPerformed() {
		return isPerformed;
	}

}