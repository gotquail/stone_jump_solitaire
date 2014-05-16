public class Solitaire {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Args: board_filename");
			return;
		}

		Board b = new Board(args[0]);
		b.solve();
	}	
}