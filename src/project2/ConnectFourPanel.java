package project2;

import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class ConnectFourPanel extends JPanel {

	private JLabel[][] board;
	private JButton[] selection;

	private Boolean person = true;
	private JMenuItem gameItem;
	private JMenuItem quitItem;
	private ImageIcon iconBlank;
	private ImageIcon iconPlayer1;
	private ImageIcon iconPlayer2;
	private final int DEFAULT_BOARD_SIZE = 10;
	private final int MIN_BOARD_SIZE = 3;
	private final int MAX_BOARD_SIZE = 30;
	private JPanel buttonPanel;
	private JPanel boardPanel;
	private int boardSize;
	private ButtonListener listener;
	private int totalPlayers;

	private ConnectFourGame game;

	public ConnectFourPanel(JMenuItem pquitItem, JMenuItem pgameItem) {

		boardSize = askForBoardSize();
		totalPlayers = askNumberOfPlayers();
		System.out.println(totalPlayers);
		game = new ConnectFourGame(boardSize, totalPlayers);

		gameItem = pgameItem;
		quitItem = pquitItem;

		setLayout(new GridLayout(boardSize + 1, boardSize)); // room for top row

		iconBlank = createIcon("blank.png");
		iconPlayer1 = createIcon("player1.png");
		iconPlayer2 = createIcon("player2.png");
		

		listener = new ButtonListener();
		quitItem.addActionListener(listener);
		gameItem.addActionListener(listener);

		selection = new JButton[boardSize];
		for (int col = 0; col < boardSize; col++) {
			selection[col] = createJButton(col);
			add(selection[col]);
		}

		board = new JLabel[boardSize][boardSize];

		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				board[row][col] = createJLabel();
				add(board[row][col]);
			}
		}
	}

	private int askNumberOfPlayers() {
		
		int bPlayers;
		
		String[] options = {"Single Player", "2-Player"};
		
		int whoKnows = JOptionPane.showOptionDialog(this, "Select number of players.", "Game Mode", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, null);
		
		if (whoKnows == JOptionPane.YES_OPTION) {
			bPlayers = 1;
		} else 
			bPlayers = 2;
		
		return bPlayers;
	}
	
	private JButton createJButton(int col) {
		JButton button = new JButton();
		button.setText("Select");
		button.addActionListener(listener);
		button.setActionCommand(Integer.toString(col));
		return button;
	}

	private ImageIcon createIcon(String filename) {
		ImageIcon icon = null;
		
		try {
			Image img = ImageIO.read(getClass().getResource(filename));
			icon = new ImageIcon(img);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return icon;
	}

	private JLabel createJLabel() {
		JLabel label = new JLabel();
		label.setIcon(iconBlank);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		label.setBorder(border);

		return label;
	}

	private int askForBoardSize() {

		String message = null;
		int bSize = DEFAULT_BOARD_SIZE;
		String strSize = JOptionPane.showInputDialog(null, "Enter board size (4 - 30)", DEFAULT_BOARD_SIZE);

		try {
			bSize = Integer.parseInt(strSize);
		} catch (Exception e) {
			message = "Invalid Board Size (" + strSize + ")" + " Setting to default";
		}

		if (bSize <= MIN_BOARD_SIZE)
			message = "Board too small, setting to default (" + DEFAULT_BOARD_SIZE + ")";
		else if (bSize > MAX_BOARD_SIZE)
			message = "Board too big, setting to default (" + DEFAULT_BOARD_SIZE + ")";

		if (message != null)
			JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);

		return bSize;
	}

	// *****************************************************************
	// Represents a listener for button push (action) events.
	// *****************************************************************
	private class ButtonListener implements ActionListener {
		// --------------------------------------------------------------
		// Updates the counter and label when the button is pushed.
		// --------------------------------------------------------------
		public void actionPerformed(ActionEvent event) {

			JComponent comp = (JComponent) event.getSource();

			for (int col = 0; col < boardSize; col++) {
				if (selection[col] == comp) {

					int row = game.selectCol(col);
					if (row == -1)
						JOptionPane.showMessageDialog(null, "Col is full!");
					else
						board[row][col].setIcon((game.getCurrentPlayer() == ConnectFourGame.PLAYER) ? iconPlayer1 : iconPlayer2);

					if (game.checkWinner(game.getCurrentPlayer())) {
						if(game.getTotalPlayers() == 2) {
							if(game.getCurrentPlayer() == ConnectFourGame.PLAYER)
								JOptionPane.showMessageDialog(null, "Player 1 wins!");
							else
								JOptionPane.showMessageDialog(null, "Player 2 wins!");
						} else if(game.getCurrentPlayer() == ConnectFourGame.PLAYER)
								JOptionPane.showMessageDialog(null, "You win!");
							else
								JOptionPane.showMessageDialog(null, "The computer wins!");
					}

					game.nextPlayer();
				}
			}

			if (comp == gameItem) {
				game.reset();

				for (int row = 1; row < boardSize; row++)
					for (int col = 0; col < boardSize; col++)
						board[row][col].setIcon(iconBlank);
			}

			if (comp == quitItem)
				System.exit(1);
		}

	}

}