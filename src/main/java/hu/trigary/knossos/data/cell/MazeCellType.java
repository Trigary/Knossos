package hu.trigary.knossos.data.cell;

/**
 * Cell types for mazes.
 */
public enum MazeCellType implements CellType {
	/**
	 * The start of the maze - the place where players are starting from.
	 */
	PATH_START,
	
	/**
	 * A block on the way between {@link #PATH_START} and
	 * {@link #PATH_FINISH} or {@link #PATH_DEAD_END}.
	 */
	PATH_CORRIDOR,
	
	/**
	 * A path which is turning.
	 */
	PATH_CORNER,
	
	/**
	 * A place where {@link #PATH_CORRIDOR} branches.
	 */
	PATH_JUNCTION,
	
	/**
	 * The end of the maze - the place where players are trying to get.
	 */
	PATH_FINISH,
	
	/**
	 * The end of a {@link #PATH_CORRIDOR}, which isn't {@link #PATH_FINISH}.
	 */
	PATH_DEAD_END,
	
	/**
	 * A block where the player can't be.
	 */
	WALL
}
