package hu.trigary.knossos.plan;

import hu.trigary.knossos.data.CellCoord;
import hu.trigary.knossos.data.cell.CellType;
import org.apache.commons.lang.Validate;

import java.util.Arrays;

/**
 * Dungeon and maze plans implement this class. Plan means that the cells have been planned out,
 * but the structure is yet to be instantiated in the world.
 *
 * @param <T> the type of cells this plan holds
 */
public abstract class Plan<T extends CellType> {
	protected final int width;
	protected final int length;
	protected final int size;
	protected final T[][] cells;
	
	protected Plan(int width, int length, int size, T defaultCellType) {
		Validate.isTrue(width > 0 && length > 0 && size > 0, "The dimensions must be positive.");
		this.width = width;
		this.length = length;
		this.size = size;
		
		//noinspection unchecked
		cells = (T[][]) new CellType[width][length];
		if (defaultCellType == null) {
			return;
		}
		
		for (int x = 0; x < width; x++) {
			Arrays.fill(cells[x], defaultCellType);
		}
	}
	
	
	
	/**
	 * @return the number of cells along the x axis
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return the number of cells along the z axis
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * @return the width and length of the cells
	 */
	public int getSize() {
		return size;
	}
	
	
	
	public boolean isInside(int x, int z) {
		return x >= 0 && x < width && z >= 0 && z < length;
	}
	
	public boolean isInside(CellCoord coord) {
		return isInside(coord.getX(), coord.getZ());
	}
	
	
	
	/**
	 * @return the type of the block at the specified coordinates or null, if the coordinates are invalid
	 */
	public T getBlock(int x, int z) {
		return x >= 0 && x < width && z >= 0 && z < length ? cells[x][z] : null;
	}
	
	/**
	 * @return the type of the block at the specified coordinates or null, if the coordinates are invalid
	 */
	public T getBlock(CellCoord coord) {
		return getBlock(coord.getX(), coord.getZ());
	}
}
