package hu.trigary.knossos.build;

import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import org.bukkit.Location;

/**
 * Instantiates a {@link Plan} in the world while also populating it.
 * @param <T> type of plan this class can instantiate
 */
public abstract class Builder<T extends CellType> {
	/**
	 * @param plan plans of the structure
	 * @param origin location of the structure's lowest corner
	 * @throws KnossosException if the plan is incompatible with the builder
	 * @return the task whose ticking causes new maze segments to be placed
	 */
	public abstract BuildTask build(Plan<T> plan, Location origin) throws KnossosException;
}
