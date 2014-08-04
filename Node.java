public class Node {

	// A valid node is a node that can contain a stone. The game board isn't 
	// square but we want to use a 2d array to represent it, so the playable
	// space is outlined using these isValid flags.
	private boolean isValid;
	private boolean isFilled;
	public int x;
	public int y;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setValid(boolean v) {
		isValid = v;
	}

	public void setFilled(boolean f) {
		isFilled = f;
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isFilled() {
		return isFilled;
	}
}