package io.github.seerainer.tetris;

import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class TetrisGame {
	private class GameKeyListener implements KeyListener {
		@Override
		public void keyPressed(final KeyEvent e) {
			if (gameOver) {
				return;
			}

			switch (e.keyCode) {
			case SWT.ARROW_LEFT -> {
				if (canMovePiece(currentPiece, -1, 0)) {
					currentPiece.moveLeft();
					canvas.redraw();
				}
			}
			case SWT.ARROW_RIGHT -> {
				if (canMovePiece(currentPiece, 1, 0)) {
					currentPiece.moveRight();
					canvas.redraw();
				}
			}
			case SWT.ARROW_DOWN -> {
				if (canMovePiece(currentPiece, 0, 1)) {
					currentPiece.moveDown();
					canvas.redraw();
				}
			}
			case SWT.ARROW_UP -> {
				final var rotated = new Tetromino(currentPiece);
				rotated.rotate();
				if (canMovePiece(rotated, 0, 0)) {
					currentPiece.rotate();
					canvas.redraw();
				}
			}
			case 'r', 'R' -> restartGame();
			default -> {
				// Ignore other keys
			}
			}
		}

		@Override
		public void keyReleased(final KeyEvent e) {
			// Not used
		}
	}

	private class GamePaintListener implements PaintListener {
		@Override
		public void paintControl(final PaintEvent e) {
			final var gc = e.gc;
			drawGame(gc);
		}
	}

	private static final int BOARD_WIDTH = 10;
	private static final int BOARD_HEIGHT = 20;

	private static final int BLOCK_SIZE = 30;
	private static final int INITIAL_SPEED = 500;

	public static void main(final String[] args) {
		final var game = new TetrisGame();
		game.run();
	}

	private Display display;
	private Shell shell;

	private Canvas canvas;
	private Label scoreLabel;
	private Label levelLabel;
	private final GameBoard gameBoard;
	private Tetromino currentPiece;
	private Tetromino nextPiece;
	private int score;
	private int level;
	private int linesCleared;

	private boolean gameOver;

	private Timer gameTimer;

	private final SecureRandom random;

	public TetrisGame() {
		this.gameBoard = new GameBoard(BOARD_WIDTH, BOARD_HEIGHT);
		this.random = new SecureRandom();
		this.score = 0;
		this.level = 1;
		this.linesCleared = 0;
		this.gameOver = false;
	}

	private boolean canMovePiece(final Tetromino piece, final int deltaX, final int deltaY) {
		final var newX = piece.getX() + deltaX;
		final var newY = piece.getY() + deltaY;

		final var shape = piece.getShape();
		for (var row = 0; row < shape.length; row++) {
			for (var col = 0; col < shape[row].length; col++) {
				if (shape[row][col] == 1) {
					final var boardX = newX + col;
					final var boardY = newY + row;

					if (boardX < 0 || boardX >= BOARD_WIDTH || boardY >= BOARD_HEIGHT
							|| (boardY >= 0 && gameBoard.getCell(boardX, boardY) != 0)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void createInfoPanel() {
		final var infoPanel = new Composite(shell, SWT.NONE);
		infoPanel.setLayout(new GridLayout(1, false));
		infoPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		scoreLabel = new Label(infoPanel, SWT.NONE);
		scoreLabel.setText("Score: 0");
		scoreLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		levelLabel = new Label(infoPanel, SWT.NONE);
		levelLabel.setText("Level: 1");
		levelLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		final var controlsLabel = new Label(infoPanel, SWT.NONE);
		controlsLabel.setText("\nControls:\n↑ - Rotate\n← → - Move\n↓ - Drop\nR - Restart");
		controlsLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	}

	private Tetromino createRandomTetromino() {
		final var types = TetrominoType.values();
		final var type = types[random.nextInt(types.length)];
		return new Tetromino(type, BOARD_WIDTH / 2 - 1, 0);
	}

	private void createShell() {
		shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE & ~SWT.MAX);
		shell.setText("Tetris Game");
		shell.setLayout(new GridLayout(2, false));

		// Game canvas
		canvas = new Canvas(shell, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		canvas.setLayoutData(new GridData(BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE));
		canvas.addPaintListener(new GamePaintListener());
		canvas.addKeyListener(new GameKeyListener());
		canvas.setFocus();

		// Info panel
		createInfoPanel();

		shell.pack();
		shell.open();
	}

	private void drawBoard(final GC gc) {
		for (var y = 0; y < BOARD_HEIGHT; y++) {
			for (var x = 0; x < BOARD_WIDTH; x++) {
				final var cellValue = gameBoard.getCell(x, y);
				if (cellValue != 0) {
					final var color = getColorForType(cellValue - 1);
					gc.setBackground(color);
					gc.fillRectangle(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

					gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
					gc.drawRectangle(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
	}

	private void drawGame(final GC gc) {
		// Clear background
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		final var bounds = canvas.getBounds();
		gc.fillRectangle(0, 0, bounds.width, bounds.height);

		// Draw board
		drawBoard(gc);

		// Draw current piece
		if (currentPiece != null && !gameOver) {
			drawTetromino(gc, currentPiece);
		}

		// Draw game over overlay
		if (!gameOver) {
			return;
		}
		gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		gc.setAlpha(128);
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
		gc.setAlpha(255);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawText("GAME OVER", bounds.width / 2 - 50, bounds.height / 2, true);
	}

	private void drawTetromino(final GC gc, final Tetromino tetromino) {
		final var shape = tetromino.getShape();
		final var color = getColorForType(tetromino.getType().ordinal());
		gc.setBackground(color);

		for (var row = 0; row < shape.length; row++) {
			for (var col = 0; col < shape[row].length; col++) {
				if (shape[row][col] == 1) {
					final var x = (tetromino.getX() + col) * BLOCK_SIZE;
					final var y = (tetromino.getY() + row) * BLOCK_SIZE;

					if (y >= 0) {
						gc.fillRectangle(x, y, BLOCK_SIZE, BLOCK_SIZE);
						gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
						gc.drawRectangle(x, y, BLOCK_SIZE, BLOCK_SIZE);
					}
				}
			}
		}
	}

	private void gameOver() {
		gameOver = true;
		if (gameTimer != null) {
			gameTimer.cancel();
		}

		display.asyncExec(() -> {
			if (!shell.isDisposed()) {
				final var messageBox = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				messageBox.setText("Game Over");
				messageBox.setMessage("Game Over! Final Score: " + score + "\n\nPlay again?");

				if (messageBox.open() == SWT.YES) {
					restartGame();
				}
			}
		});
	}

	private void gameStep() {
		if (gameOver) {
			return;
		}

		if (canMovePiece(currentPiece, 0, 1)) {
			currentPiece.moveDown();
		} else {
			placePiece();
			final var clearedLines = gameBoard.clearLines();
			if (clearedLines > 0) {
				updateScore(clearedLines);
			}

			currentPiece = nextPiece;
			nextPiece = createRandomTetromino();

			if (!canMovePiece(currentPiece, 0, 0)) {
				gameOver();
			}
		}

		canvas.redraw();
		updateInfoLabels();
	}

	private Color getColorForType(final int typeIndex) {
		return switch (typeIndex) {
		case 0 -> display.getSystemColor(SWT.COLOR_CYAN); // I
		case 1 -> display.getSystemColor(SWT.COLOR_YELLOW); // O
		case 2 -> display.getSystemColor(SWT.COLOR_MAGENTA); // T
		case 3 -> display.getSystemColor(SWT.COLOR_BLUE); // J
		case 4 -> display.getSystemColor(SWT.COLOR_RED); // L
		case 5 -> display.getSystemColor(SWT.COLOR_GREEN); // S
		case 6 -> display.getSystemColor(SWT.COLOR_DARK_RED); // Z
		default -> display.getSystemColor(SWT.COLOR_GRAY);
		};
	}

	private void placePiece() {
		final var shape = currentPiece.getShape();
		for (var row = 0; row < shape.length; row++) {
			for (var col = 0; col < shape[row].length; col++) {
				if (shape[row][col] == 1) {
					final var boardX = currentPiece.getX() + col;
					final var boardY = currentPiece.getY() + row;
					if (boardY >= 0) {
						gameBoard.setCell(boardX, boardY, currentPiece.getType().ordinal() + 1);
					}
				}
			}
		}
	}

	private void restartGame() {
		gameBoard.clear();
		score = 0;
		level = 1;
		linesCleared = 0;
		gameOver = false;
		currentPiece = createRandomTetromino();
		nextPiece = createRandomTetromino();
		startGameLoop();
		canvas.redraw();
		updateInfoLabels();
	}

	public void run() {
		display = new Display();
		createShell();
		startGame();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		if (gameTimer != null) {
			gameTimer.cancel();
		}
		display.dispose();
	}

	private void startGame() {
		currentPiece = createRandomTetromino();
		nextPiece = createRandomTetromino();
		startGameLoop();
	}

	private void startGameLoop() {
		if (gameTimer != null) {
			gameTimer.cancel();
		}

		gameTimer = new Timer();
		final var speed = Math.max(50, INITIAL_SPEED - (level - 1) * 50);

		gameTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!gameOver) {
					display.asyncExec(() -> {
						if (!shell.isDisposed()) {
							gameStep();
						}
					});
				}
			}
		}, speed, speed);
	}

	private void updateInfoLabels() {
		if (!scoreLabel.isDisposed()) {
			scoreLabel.setText("Score: " + score);
		}
		if (!levelLabel.isDisposed()) {
			levelLabel.setText("Level: " + level);
		}
	}

	private void updateScore(final int clearedLines) {
		linesCleared += clearedLines;
		score += clearedLines * 100 * level;

		// Increase level every 10 lines
		final var newLevel = (linesCleared / 10) + 1;
		if (newLevel == level) {
			return;
		}
		level = newLevel;
		startGameLoop(); // Restart timer with new speed
	}
}