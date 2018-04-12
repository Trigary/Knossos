package hu.trigary.knossos.plan;

import hu.trigary.knossos.data.CellCoord;
import hu.trigary.knossos.data.cell.CellType;

public class MutablePlan<T extends CellType> extends Plan<T> {
	/**
	 * @param width number of cells along the x axis
	 * @param length number of cells along the z axis
	 * @param size width and length of the cells
	 * @param defaultCellType default value for all of the cells, can be null
	 */
	public MutablePlan(int width, int length, int size, T defaultCellType) {
		super(width, length, size, defaultCellType);
	}
	
	
	
	/**
	 * @param type the new type
	 * @return whether the coordinates are valid
	 */
	public boolean setBlock(int x, int z, T type) {
		if (x >= 0 && x < width && z >= 0 && z < length) {
			cells[x][z] = type;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @param type the new type
	 * @return whether the coordinates are valid
	 */
	public boolean setBlock(CellCoord coord, T type) {
		return setBlock(coord.getX(), coord.getZ(), type);
	}
}
