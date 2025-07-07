package io.github.seerainer.tetris;

public class Tetromino {
	private final TetrominoType type;
	private int x;
	private int y;
	private int rotation;
	private final int[][][] shapes;

	public Tetromino(final Tetromino other) {
		this.type = other.type;
		this.x = other.x;
		this.y = other.y;
		this.rotation = other.rotation;
		this.shapes = other.shapes;
	}

	public Tetromino(final TetrominoType type, final int x, final int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.rotation = 0;
		this.shapes = type.getShapes();
	}

	public int getRotation() {
		return rotation;
	}

	public int[][] getShape() {
		return shapes[rotation];
	}

	public TetrominoType getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void moveDown() {
		y++;
	}

	public void moveLeft() {
		x--;
	}

	public void moveRight() {
		x++;
	}

	public void rotate() {
		rotation = (rotation + 1) % shapes.length;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setY(final int y) {
		this.y = y;
	}
}