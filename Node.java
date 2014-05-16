public class Node {

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