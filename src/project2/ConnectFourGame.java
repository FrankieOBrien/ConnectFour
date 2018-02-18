package project2;

import java.awt.Point;
import java.util.*;

public class ConnectFourGame {

	private int[][] board;
	private int size;
	private boolean playerTurn;
	public static final int PLAYER = 1;
	public static final int PLAYER2 = 2;
	public static final int COMPUTER = 5;
	public static boolean winner;
	private int totalPlayers;
	private int lastTurn;
	private int totalTurns;
	private int computerLastTurn;

	public ConnectFourGame(int pSize, int players) {
		size = pSize;
		board = new int[pSize][pSize];
		lastTurn = 0;
		totalPlayers = players;
		reset();
	}

	public void reset() {
		winner = false;
		totalTurns = 0;
		playerTurn = true;
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				board[row][col] = 0;
	}

	/*********************************************************
	 * @param pCol
	 * @return
	 ********************************************************/
	private int placeToken(int pCol) {
		for (int row = size - 1; row >= 0; row--)
			if (board[row][pCol] == 0) {
				board[row][pCol] = getCurrentPlayer();
				return row;
			}
		return -1;
	}

	public int selectCol(int pCol) {
		lastTurn = pCol;
		return placeToken(pCol);
	}

	public void nextPlayer() {
		playerTurn = !playerTurn;
	}

	public void testTurn() {
		for (int i = 0; i < (size * size); i++) {
			if (!winner) {
				computerTurn();
				System.out.println("Turn: " + i);
				printBoard();
				System.out.println();
				checkWinner(getCurrentPlayer());
			}
		}
	}

	public void printBoard() {
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					if (col == 0 && row != 0)
						System.out.println();
					System.out.print(board[row][col] + " ");

				}
			}
		System.out.println();
		System.out.println();
		for (int col = 1; col <= size; col++)
			System.out.print(col + " ");	
	}

	public int deselectCol(int pCol) {
		for (int row = 0; row <= size - 1; row++)
			if (board[row][pCol] == COMPUTER || board[row][pCol] == PLAYER) {
				board[row][pCol] = 0;
				return pCol;
			}
		return -1;
	}

	private void setPlayer(int player) {
		if (player == COMPUTER) {
			if (playerTurn == true)
				playerTurn = false;
		} else if (player == PLAYER)
			if (playerTurn == false)
				playerTurn = true;

	}

	public int getTotalPlayers() {
		return totalPlayers;
	}
	
	public int computerTurn() {
		// Select a column to win if possible
		for (int col = size - 1; col >= 0; col--) {
			placeToken(col);
			checkWinner(getCurrentPlayer());
			if (winner) {
				winner = false;
				computerLastTurn = col;
				return col;
			} else
				deselectCol(col);
		}

		// Select a column so computer won't lose
		setPlayer(PLAYER);
		for (int col = size - 1; col >= 0; col--) {
			placeToken(col);
			checkWinner(getCurrentPlayer());
			if (winner) {
				winner = false;
				deselectCol(col);
				setPlayer(COMPUTER);
				selectCol(col);
				computerLastTurn = col;
				return col;
			} else
				deselectCol(col);
		}
		setPlayer(COMPUTER);

		// Blocks specific early-game strategy going to randomly chosen spot x
		// 0 0 0 0 0 5 0 0 0
		// 0 0 0 x 2 2 x 0 0
		for (int col = 1; col < size - 2; col++) {
			if ((board[size - 1][col] == PLAYER) && (board[size - 1][col + 1] == PLAYER)
					&& (board[size - 1][col - 1] == 0) && (board[size - 1][col + 2] == 0)) {
				Random rand = new Random();
				int n = rand.nextInt(2);
				if (n == 1) {
					selectCol(col - 1);
					computerLastTurn = col - 1;
					return col - 1;
				} else {
					selectCol(col + 2);
					computerLastTurn = col + 2;
					return col + 2;
				}
			}
		}

		// Sets up a win for the next turn
		for (int col = 0; col < size - 1; col++) {
			placeToken(col);
			placeToken(col + 1);
			checkWinner(COMPUTER);
			deselectCol(col);
			deselectCol(col + 1);
			if (winner) {
				winner = false;
				selectCol(col);
				computerLastTurn = col;
				return col;
			}
		}
		
		// Selects a column within 1 of where the player just went on the first turn
		// Selects a column within 1 of where the computer went last turn if not the
		// first turn
		if (totalTurns == 0) {
			Random rand = new Random();
			int n = rand.nextInt(3) - 1;
			if (lastTurn == 0)
				n = rand.nextInt(2);
			if (lastTurn == size - 1)
				n = rand.nextInt(2) - 1;
			selectCol(n + lastTurn);
			computerLastTurn = n + lastTurn;
			return n + lastTurn;
		} else {
			Random rand = new Random();
			int n = rand.nextInt(3) - 1;
			if (computerLastTurn == 0)
				n = rand.nextInt(2);
			if (computerLastTurn == size - 1)
				n = rand.nextInt(2) - 1;
			selectCol(n + computerLastTurn);
			computerLastTurn += n;
			return n + computerLastTurn - n;
		}

	}

	public int getCurrentPlayer() {
		if (playerTurn)
			return PLAYER;
		else
			return COMPUTER;
	}

	public void printTile(int pRow, int pCol) {
		System.out.println(board[pRow][pCol]);
	}

	public boolean checkWinner(int person) {
		// Checks horizontal win
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				if ((col + 3) < size)
					if ((board[row][col] == person) && (board[row][col + 1] == person)
							&& (board[row][col + 2] == person) && (board[row][col + 3] == person)) {
						winner = true;
						return true;
					}
		// Checks Vertical win
		for (int row = 3; row < size; row++)
			for (int col = 0; col < size; col++)
				if ((board[row][col] == person) && (board[row - 1][col] == person) && (board[row - 2][col] == person)
						&& (board[row - 3][col] == person)) {
					winner = true;
					return true;
				}
		// Checks Up/Right win
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				if (row >= 3 && col <= size - 4)
					if ((board[row][col] == person) && (board[row - 1][col + 1] == person)
							&& (board[row - 2][col + 2] == person) && (board[row - 3][col + 3] == person)) {
						winner = true;
						return true;
					}

			}
		// Checks Up/Left win
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				if (row >= 3 && col >= 3)
					if ((board[row][col] == person) && (board[row - 1][col - 1] == person)
							&& (board[row - 2][col - 2] == person) && (board[row - 3][col - 3] == person)) {
						winner = true;
						return true;
					}

			}
		// Checks Down/Right win
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				if (row <= size - 4 && col <= size - 4)
					if ((board[row][col] == person) && (board[row + 1][col + 1] == person)
							&& (board[row + 2][col + 2] == person) && (board[row + 3][col + 3] == person)) {
						winner = true;
						return true;
					}

			}
		// Checks Down/Left win
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				if (row <= size - 4 && col >= 3)
					if ((board[row][col] == person) && (board[row + 1][col - 1] == person)
							&& (board[row + 2][col - 2] == person) && (board[row + 3][col - 3] == person)) {
						winner = true;
						return true;
					}
			}
		return false;
	}

	public int twoPlayerGame() {
		System.out.println("Player 1 chip: " + PLAYER);
		System.out.println("Player 2 chip: " + PLAYER2);
		printBoard();
		Scanner scan = new Scanner(System.in);
		int g;
		while (!winner) {
			System.out.println();
			System.out.println("It's player 1's turn!");
			System.out.println();
			System.out.println("Pick a number from 1 - " + size);
			g = scan.nextInt();
			while (g < 1 || g > size) {
				System.out.println("Pick a number from 1 - " + size);
				g = scan.nextInt();
			}
			selectCol(g - 1);
			printBoard();
			turnFinish();
			if (!winner) {
				System.out.println();
				System.out.println("It's player 2's turn!");
				System.out.println();
				System.out.println("Pick a number from 1 - " + size);
				g = scan.nextInt();
				while (g < 1 || g > size) {
					System.out.println("Pick a number from 1 - " + size + ", please!");
					g = scan.nextInt();
				}
				selectCol(g - 1);
				printBoard();
				turnFinish();
				if (winner) {
					System.out.println();
					System.out.println("Player 2 won!");
					System.out.println();
					printBoard();
					playAgain();
					return COMPUTER;
				}
				totalTurns++;
			} else {
				System.out.println();
				System.out.println("Player 1 won!");
				System.out.println();
				printBoard();
				playAgain();
				return PLAYER;
			}
		}
		return -1;
	}

	private void turnFinish() {
		checkWinner(getCurrentPlayer());
		nextPlayer();
	}

	public void startGame() {
		switch (totalPlayers) {
		case 1:
			System.out.println("Good Luck!");
			System.out.println();
			againstComputerGame();
			break;

		case 2:
			System.out.println("HUASHFDA:I");
			twoPlayerGame();

		}
	}

	public int againstComputerGame() {
		System.out.println("Player chip: " + PLAYER);
		System.out.println("Computer chip: " + COMPUTER);
		printBoard();
		while (!winner) {
			Scanner scan = new Scanner(System.in);
			System.out.println();
			System.out.println("Pick a number from 1 - " + size);
			int g = scan.nextInt();
			while (g < 1 || g > size) {
				System.out.println("Pick a number from 1 - " + size + ", please!");
				g = scan.nextInt();
			}
			selectCol(g - 1);
			turnFinish();
			if (!winner) {
				computerTurn();
				System.out.println("Next Turn:");
				printBoard();
				System.out.println();
				turnFinish();
				if (winner) {
					System.out.println("The Computer Won!");
					System.out.println();
					printBoard();
					playAgain();
					return COMPUTER;
				}
				totalTurns++;
			} else {
				System.out.println("You Won!");
				printBoard();
				playAgain();
				return PLAYER;
			}
		}
		return -1;
	}

	public void playAgain() {
		reset();
		Scanner scan = new Scanner(System.in);
		System.out.println();
		System.out.println("Enter 1 to play again");
		int g = scan.nextInt();
		while (g != 1) {
			g = scan.nextInt();
		}
		if (g == 1) {
			System.out.println("Enter a proper board size (9 recomended)");
			g = scan.nextInt();
			while (g < 4 || g > 20) {
				System.out.println("That's not a proper board size, try again!");
				g = scan.nextInt();
			}
			size = g;
			startGame();
		} else
			System.exit(0);
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a proper board size (9 recomended)");
		int g = scan.nextInt();
		while (g < 4 || g > 10) {
			System.out.println("That's not a proper board size, try again!");
			g = scan.nextInt();
		}
		System.out.println();
		System.out.println("Enter the number of players");
		int f = scan.nextInt();
		while (g < 1 || g > 2) {
			System.out.println("You can only have one or two players, try again");
			g = scan.nextInt();
		}
		ConnectFourGame game = new ConnectFourGame(g, f);
		game.startGame();
	}
}