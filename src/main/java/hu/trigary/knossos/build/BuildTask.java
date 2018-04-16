package hu.trigary.knossos.build;

import hu.trigary.knossos.Knossos;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import org.bukkit.Location;

/**
 * A task which can be scheduled in the {@link Knossos} instance for execution.
 */
public abstract class BuildTask {
	private final Plan<? extends CellType> plan;
	private final Location origin;
	private int x = 0;
	private int z = 0;
	
	/**
	 * @param plan plans of the structure
	 * @param origin location of the structure's lowest corner
	 */
	public BuildTask(Plan<? extends CellType> plan, Location origin) {
		this.plan = plan;
		this.origin = origin;
	}
	
	
	
	/**
	 * @return true if the task is not yet finished - whether the tick method should be called more
	 */
	public boolean tick() {
		buildCell(x, z, origin.clone().add(x * plan.getSize(), 0, z * plan.getSize()));
		if (++x == plan.getWidth()) {
			x = 0;
			return ++z != plan.getLength();
		}
		return true;
	}
	
	/**
	 * @param x location of the cell along the width
	 * @param z location of the cell along the length
	 * @param corner location of the cell's lowest corner
	 */
	protected abstract void buildCell(int x, int z, Location corner);
}
