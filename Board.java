import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.ArrayDeque;

/**
 * Representation of the 7x7 solitaire game. The 3 locations in each corner
 * are out of bounds. Leap-frog stones (remove jumped stones) to try to have 
 * one stone remaining.
 */
public class Board {

	// Boards are square, with SIZExSIZE dimensions.
	public static final int SIZE = 7;

	// For reading boards from txt files.
	public static final char CHAR_WALL = '=';
	public static final char CHAR_STONE = 'o';
	public static final char CHAR_EMPTY = '.';

	protected Node[][] nodes;
	protected int numStones;

	public Board(String fn) {
		initMembers();

		readBoardFromFile(fn);
	}

	/**
	 * For cloning boards.
	 */
	public Board(Board b) {
		initMembers();

		for (int j = 0; j < SIZE; j++) {
			for (int i = 0; i < SIZE; i++) {
				Node n = new Node(i, j);
				Node bn = b.getNode(i, j);
				n.setValid(bn.isValid());
				n.setFilled(bn.isFilled());
				nodes[i][j] = n;  
			}
		}
	}

	private void initMembers() {
		numStones = 0;
		nodes = new Node[SIZE][SIZE];
	}

	public Node getNode(int x, int y) {
		if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return null;
		return nodes[x][y];
	}

	public void doMove(Move m) {
		m.n1.setFilled(false);
		m.n2.setFilled(false);
		m.n3.setFilled(true);
		m.setPerformed();
		numStones--;
	}

	public void revertMove(Move m) {
		m.n1.setFilled(true);
		m.n2.setFilled(true);
		m.n3.setFilled(false);
		numStones++;
	}

	public boolean isSolved() {
		if (numStones == 1) return true;
		return false;
	}

	/**
	 * Rotate the nodes of the board clockwise 90 degrees.
	 */
	public void rotate() {
		for (int layer = 0; layer < SIZE / 2; layer++) {
			int first = layer;
			int last = SIZE - layer - 1;
			for (int i = layer; i < last; i++) {
				Node temp = nodes[i][layer];
				nodes[i][layer] = nodes[layer][SIZE-i-1];
				nodes[layer][SIZE-i-1] = nodes[SIZE-i-1][SIZE-layer-1];
				nodes[SIZE-i-1][SIZE-layer-1] = nodes[SIZE-layer-1][i];
				nodes[SIZE-layer-1][i] = temp;
			}
		}
	}

	/**
	 * Mirror the board (swap nodes around centred horizontal line of symmetry).
	 */
	public void mirror() {
		for (int j = 0; j < SIZE; j++) {
			for (int i = 0; i < SIZE / 2; i++) {
				Node temp = nodes[i][j];
				nodes[i][j] = nodes[SIZE-i-1][j];
				nodes[SIZE-i-1][j] = temp;
			}
		}
	}

	/**
	 *	Return board state compressed to a long.
	 */
	public long toLong() {
		long n = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int val = 0;
				if (nodes[j][i].isValid() && nodes[j][i].isFilled()) {
					val = 1;
				}
				n = n + (val << j + i*SIZE);
			}
		}
		return n;
	}

	protected void readBoardFromFile(String fn) {
		BufferedReader inputStream = null;
		try {
			inputStream = new BufferedReader(new FileReader(fn));
			
			int row = 0;
			String l;
			while ((l = inputStream.readLine()) != null) {
				for (int col = 0; col < l.length(); col++) {
					char c = l.charAt(col);
					Node n = new Node(col, row);
					switch (c) {
						case CHAR_WALL:
							n.setValid(false);
							break;
						case CHAR_STONE:
							n.setValid(true);
							n.setFilled(true);
							numStones++;
							break;
						case CHAR_EMPTY:
							n.setValid(true);
							n.setFilled(false);
							break;
						default:
							break;
					}
					nodes[col][row] = n;
				}

				row++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());				
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		} finally {
			try {
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {
				System.err.println("IOException: " + e.getMessage());
			}
		}
	}

	/**
	 * Write a visual representation of the board to stdout.
	 */
	public void print() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Node n = nodes[j][i];
				if (!n.isValid()) {
					System.out.print(CHAR_WALL);
				} else if (n.isFilled()) {
					System.out.print(CHAR_STONE);
				} else {
					System.out.print(CHAR_EMPTY);
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public int getNumStones() {
		return numStones;
	}
}



