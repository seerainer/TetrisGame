package io.github.seerainer.tetris;

public enum TetrominoType {
	I(new int[][][] { { { 1, 1, 1, 1 } }, { { 1 }, { 1 }, { 1 }, { 1 } } }),

	O(new int[][][] { { { 1, 1 }, { 1, 1 } } }),

	T(new int[][][] { { { 0, 1, 0 }, { 1, 1, 1 } }, { { 1, 0 }, { 1, 1 }, { 1, 0 } }, { { 1, 1, 1 }, { 0, 1, 0 } },
			{ { 0, 1 }, { 1, 1 }, { 0, 1 } } }),

	J(new int[][][] { { { 1, 0, 0 }, { 1, 1, 1 } }, { { 1, 1 }, { 1, 0 }, { 1, 0 } }, { { 1, 1, 1 }, { 0, 0, 1 } },
			{ { 0, 1 }, { 0, 1 }, { 1, 1 } } }),

	L(new int[][][] { { { 0, 0, 1 }, { 1, 1, 1 } }, { { 1, 0 }, { 1, 0 }, { 1, 1 } }, { { 1, 1, 1 }, { 1, 0, 0 } },
			{ { 1, 1 }, { 0, 1 }, { 0, 1 } } }),

	S(new int[][][] { { { 0, 1, 1 }, { 1, 1, 0 } }, { { 1, 0 }, { 1, 1 }, { 0, 1 } } }),

	Z(new int[][][] { { { 1, 1, 0 }, { 0, 1, 1 } }, { { 0, 1 }, { 1, 1 }, { 1, 0 } } });

	private final int[][][] shapes;

	TetrominoType(final int[][][] shapes) {
		this.shapes = shapes;
	}

	public int[][][] getShapes() {
		return shapes;
	}
}