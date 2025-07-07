package io.github.seerainer.tetris;

public class GameBoard {
	private final int[][] board;
	private final int width;
	private final int height;

	public GameBoard(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.board = new int[height][width];
		clear();
	}

	public void clear() {
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				board[y][x] = 0;
			}
		}
	}

	public int clearLines() {
		var linesCleared = 0;

		for (var y = height - 1; y >= 0; y--) {
			if (isLineFull(y)) {
				removeLine(y);
				linesCleared++;
				y++; // Check the same line again since everything shifted down
			}
		}

		return linesCleared;
	}

	public int getCell(final int x, final int y) {
		return x >= 0 && x < width && y >= 0 && y < height ? board[y][x] : 0;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	private boolean isLineFull(final int y) {
		for (var x = 0; x < width; x++) {
			if (board[y][x] == 0) {
				return false;
			}
		}
		return true;
	}

	private void removeLine(final int lineY) {
		// Move all lines above down by one
		for (var y = lineY; y > 0; y--) {
			for (var x = 0; x < width; x++) {
				board[y][x] = board[y - 1][x];
			}
		}

		// Clear the top line
		for (var x = 0; x < width; x++) {
			board[0][x] = 0;
		}
	}

	public void setCell(final int x, final int y, final int value) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			board[y][x] = value;
		}
	}
}