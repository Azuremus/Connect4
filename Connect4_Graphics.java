import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class Connect4_Graphics {

	public static void main(String[] args) {

		// initialize token tracker and playing rack
		int[][] rack = new int[6][7];
		int[] count = new int[7];
		// setup for placement into next empty index within the column.
		for (int j = 0; j < count.length; j++) {
			count[j] = rack.length - 1;
		}
		// creates starting graphical rack
		int player = 1;
		gameRack(rack, count, player);

	}

	// introduces the graphical component
	public static void gameRack(int[][] rack, int[] count, int player) {

		JFrame window = new JFrame("Graphics window");
		window.setLocationByPlatform(true);
		final JLabel coords = new JLabel(" ");
		@SuppressWarnings("serial")
		final JPanel panel = new JPanel() {

			protected void paintComponent(Graphics gx) {
				coords.setText(" ");
				Graphics2D g = (Graphics2D) gx;
				int width = getWidth();
				int height = getHeight();
				g.clearRect(0, 0, width, height);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setBackground(Color.WHITE);
				g.setColor(Color.BLACK);
				drawRack(g, width, player, rack);
			}

		};
		window.setLayout(new BorderLayout());
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		window.setBounds(d.width / 4, 0, d.width / 2, d.width / 2);
		window.setBackground(Color.WHITE);
		panel.setBackground(Color.BLACK);
		window.add(panel, BorderLayout.CENTER);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		// setup for only altering the graphics upon a MouseEvent
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// Mouse Released
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// limits player actions to mouse press, and only while no winner exists
				if (winner(rack) == 0) {
					int column = e.getX() * 7 / 943;
					
					// ensures the selected column is not full
					if (rack[0][column] == 0) {
						window.dispose();
						canPlayerMove(rack, count, column, player);
						gameRack(rack, count, changePlayer(player));
						changePlayer(player);
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// Mouse Exit
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// Mouse Enter
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// Mouse clicked
			}
		});
	}

	//helper method for calling player within the graphics loop
	public static int changePlayer(int player) {
		player = 3 - player;
		return player;
	}


	// for graphical implementation
	public static void drawRack(java.awt.Graphics g, int size, int player, int[][] rack) {
		
		int tokenSize = size / 8;
		
		// recreates game rack that has been updated with player input
		g.setColor(Color.BLUE);
		g.fillRect(0, size / 8, size, size * 8 / 7);
		g.setColor(Color.WHITE);
		for (int j = 0; j < rack.length; j++) {
			for (int i = 0; i <= rack.length; i++) {

				if (rack[j][i] == 0) {
					g.setColor(Color.WHITE);
				} else if (rack[j][i] == 1) {
					g.setColor(Color.RED);
				} else if (rack[j][i] == 2) {
					g.setColor(Color.BLACK);
				}
				g.fillOval(tokenSize / 8 + ((i) * (tokenSize + tokenSize / 8)),
						(tokenSize + tokenSize / 40) + (j * (tokenSize + tokenSize / 8)), tokenSize, tokenSize);
			}
		}
		
		// creates situational fonts
		Font large = new Font("Arial", 1, 160);
		Font medium = new Font("Arial", 1, 110);
		Font small = new Font("Arial", 1, 75);

		// statements to draw prompts depending on situation
		if (winner(rack) != 0) {
			if (player == 1) {
				g.setColor(Color.RED);
			}else {
				g.setColor(Color.BLACK);
			}
			// declares the winning player
			g.setFont(medium);
			g.drawString("Player " + winner(rack), tokenSize / 16, tokenSize*4/5); 
			g.setColor(Color.GREEN);
			g.drawString(" has won!", size *5/11, tokenSize*4/5);
		
		} else {
			// announces if there are no winners
			if (!(Arrays.toString(rack[0]).contains("0") && winner(rack) == 0)) {
				g.setColor(Color.GREEN);
				g.setFont(small);
				g.drawString("Game over! Nobody won!", tokenSize / 16, tokenSize/2);
			
				// sets up "title screen" image
			} else if (!(Arrays.toString(rack[5]).contains("1")) && !(Arrays.toString(rack[5]).contains("2"))) {
				g.setColor(Color.GREEN);
				g.setFont(large);
				g.drawString("CONNECT 4", tokenSize / 16, tokenSize);
			
				//alternates colors between player prompt
			} else {
				if (player == 1) {
					g.setColor(Color.RED);
				}else {
					g.setColor(Color.BLACK);
				}
				g.setFont(small);
				g.drawString("Player " + player + ", it's your turn! ", tokenSize*2/3, tokenSize/2);
			} 
		}
	}

	public static boolean canPlayerMove(int[][] rack, int[] count, int column, int player) {

		// verifies that player's selection is not within a full column
		if (column >= 0 && column <= 6) {
			if (rack[count[column]][column] == 0) {
				rack[count[column]][column] = player;
				count[column]--;
			}
		}
		return false;
	}

	// sets the winning conditions to alignments of 4 vertical, 4 descending
	// diagonal, 4 horizontal, or 4 descending diagonal
	public static int winner(int[][] rack) {

		// checks for 4 descending diagonal alignment
		for (int i = 0; i < rack.length - 3 && i >= 0; i++) {
			for (int j = 0; j < rack[i].length - 3; j++) {
				if (rack[i][j] != 0 && rack[i][j] == rack[i + 1][j + 1] && rack[i][j] == rack[i + 2][j + 2]
						&& rack[i][j] == rack[i + 3][j + 3]) {
					return rack[i][j];
				}
			}
		}

		// checks for 4 vertical alignment
		for (int i = rack.length - 4; i >= 0; i--) {
			for (int j = 0; j <= rack[i].length - 1; j++) {
				if (rack[i][j] != 0 && rack[i][j] == rack[i + 1][j] && rack[i][j] == rack[i + 2][j]
						&& rack[i][j] == rack[i + 3][j]) {
					return rack[i][j];
				}
			}
		}

		// checks for 4 horizontal alignment
		for (int i = rack.length - 1; i >= 0; i--) {
			for (int j = 0; j < rack[i].length - 3; j++) {
				if (rack[i][j] != 0 && rack[i][j] == rack[i][j + 1] && rack[i][j] == rack[i][j + 2]
						&& rack[i][j] == rack[i][j + 3]) {
					return rack[i][j];
				}
			}
		}

		// checks for 4 ascending diagonal alignment
		for (int i = rack.length - 1; i >= 3; i--) {
			for (int j = 0; j < rack[i].length - 3; j++) {
				if (rack[i][j] != 0 && rack[i][j] == rack[i - 1][j + 1] && rack[i][j] == rack[i - 2][j + 2]
						&& rack[i][j] == rack[i - 3][j + 3]) {
					return rack[i][j];
				}
			}
		}
		return 0;
	}
}