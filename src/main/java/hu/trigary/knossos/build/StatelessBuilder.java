package hu.trigary.knossos.build;

import hu.trigary.knossos.data.Arguments;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import org.bukkit.Location;

/**
 * A {@link Builder} which internally creates the {@link BuildTask} instance,
 * disallowing you to save state information, but making the code cleaner.
 */
public abstract class StatelessBuilder<T extends CellType> extends Builder<T> {
	@Override
	public BuildTask build(Plan<T> plan, Location origin, Arguments args) {
		return new BuildTask(plan, origin) {
			@Override
			protected void buildCell(int x, int z, Location corner) {
				StatelessBuilder.this.buildCell(plan, x, z, corner);
			}
		};
	}
	
	/**
	 * @param plan plans of the structure
	 * @param x location of the cell along the width
	 * @param z location of the cell along the length
	 * @param corner location of the cell's lowest corner
	 */
	protected abstract void buildCell(Plan<T> plan, int x, int z, Location corner);
}
