package hu.trigary.knossos.plan;

import hu.trigary.knossos.data.CellCoord;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.data.KnossosException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Dungeon and maze planners implement this class.
 * @param <T> type of plan this class can create
 */
public abstract class Planner<T extends CellType> {
	/**
	 * @param width number of cells along the x axis
	 * @param length number of cells along the z axis
	 * @param size width and length of the cells
	 * @param start entrance or null, if it should be random
	 * @param args additional arguments, which may or may not be optional; can be null
	 * @throws KnossosException if the planning failed, usually due to invalid parameters
	 * @return the created plan, which is never null
	 */
	public abstract Plan<T> plan(int width, int length, int size, CellCoord start, String[] args) throws KnossosException;
	
	
	
	protected boolean randBool() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	protected int randInt(int exclusiveMax) {
		return ThreadLocalRandom.current().nextInt(exclusiveMax);
	}
}
