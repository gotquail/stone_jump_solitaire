public class Main {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Args: board_filename");
			return;
		}

		Board b1 = new Board(args[0]);
		Player p = new Player(b1);
		p.solveBoard();
	}
}