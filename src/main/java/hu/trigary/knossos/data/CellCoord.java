package hu.trigary.knossos.data;

/**
 * An immutable pair of X and Z coordinates of a maze cell.
 */
public class CellCoord {
	private final int x;
	private final int z;
	
	/**
	 * @param x location along the width
	 * @param z location along the length
	 */
	public CellCoord(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	
	
	/**
	 * @return the location along the width
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return the location along the length
	 */
	public int getZ() {
		return z;
	}
}
