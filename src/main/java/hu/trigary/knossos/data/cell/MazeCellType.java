package hu.trigary.knossos.data.cell;

public enum MazeCellType implements CellType {
	/**
	 * The start of the maze - the place where players are starting from.
	 */
	PATH_START,
	
	/**
	 * A block on the way between {@link MazeCellType#PATH_START} and
	 * {@link MazeCellType#PATH_FINISH} or {@link MazeCellType#PATH_DEAD_END}.
	 */
	PATH_CORRIDOR,
	
	/**
	 * A path which is turning.
	 */
	PATH_CORNER,
	
	/**
	 * A place where {@link MazeCellType#PATH_CORRIDOR} branches.
	 */
	PATH_JUNCTION,
	
	/**
	 * The end of the maze - the place where players are trying to get.
	 */
	PATH_FINISH,
	
	/**
	 * The end of a {@link MazeCellType#PATH_CORRIDOR}, which isn't {@link MazeCellType#PATH_FINISH}.
	 */
	PATH_DEAD_END,
	
	/**
	 * A block where the player can't be.
	 */
	WALL
}
