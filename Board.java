import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;

public class Board {

	public static final char CHAR_WALL = '=';
	public static final char CHAR_STONE = 'o';
	public static final char CHAR_EMPTY = '.';

	protected int width;
	protected int height;
	protected ArrayList<ArrayList<Node>> nodes;
	protected int numStones;

	public Board(String fn) {

		readBoardFromFile(fn);
		this.print();
	}

	public Node getNode(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return null;
		return nodes.get(y).get(x);
	}



	public boolean solve() {
		if (numStones == 1) {
			System.out.println("Found a solution.");
			this.print();
			return true;
		}

		// Detect empty locations on the board.
		ArrayList<Node> empties = new ArrayList<Node>();
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				Node n = getNode(i, j);
				if (n.isValid() && !n.isFilled()) empties.add(n);
			}
		}

		// Detect valid moves by examining empty locations.
		ArrayList<Jump> moves = new ArrayList<Jump>();
		for (Node n : empties) {
			Node n1, n2;

			// Up.
			n1 = getNode(n.x, n.y-1);
			n2 = getNode(n.x, n.y-2);
			if (n1 != null && n2 != null && 
				n1.isFilled() && n2.isFilled()) {
				moves.add(new Jump(n2, n1, n));
			}

			// Down.
			n1 = getNode(n.x, n.y+1);
			n2 = getNode(n.x, n.y+2);
			if (n1 != null && n2 != null && 
				n1.isFilled() && n2.isFilled()) {
				moves.add(new Jump(n2, n1, n));
			}

			// Left.
			n1 = getNode(n.x-1, n.y);
			n2 = getNode(n.x-2, n.y);
			if (n1 != null && n2 != null && 
				n1.isFilled() && n2.isFilled()) {
				moves.add(new Jump(n2, n1, n));
			}

			// Right.
			n1 = getNode(n.x+1, n.y);
			n2 = getNode(n.x+2, n.y);
			if (n1 != null && n2 != null && 
				n1.isFilled() && n2.isFilled()) {
				moves.add(new Jump(n2, n1, n));
			}
		}

		// Explore all the valid jumps recursively.
		for (Jump j : moves) {
			j.s1.setFilled(false);
			j.s2.setFilled(false);
			j.t.setFilled(true);
			numStones--;
			
			this.solve();

			j.s1.setFilled(true);
			j.s2.setFilled(true);
			j.t.setFilled(false);
			numStones++;
		}

		return false;
	}

	protected void readBoardFromFile(String fn) {
		numStones = 0;
		nodes = new ArrayList<ArrayList<Node>>();
		width = 0;
		height = 0;

		BufferedReader inputStream = null;
		try {
			inputStream = new BufferedReader(new FileReader(fn));
			String l;
			while ((l = inputStream.readLine()) != null) {
				processLine(l);
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

	private void processLine(String l) {
		ArrayList<Node> row = new ArrayList<Node>();
		for (int i = 0; i < l.length(); i++) {
			char c = l.charAt(i);
			Node n = new Node(i, height);
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
			row.add(n);
		}

		height++;
		if (row.size() > width) width = row.size();
		nodes.add(row);
	}

	private void print() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Node n = nodes.get(j).get(i);
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
}