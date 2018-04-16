package hu.trigary.knossos.build;

import hu.trigary.knossos.data.Arguments;
import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import org.bukkit.Location;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Instantiates a {@link Plan} in the world while also populating it.
 *
 * @param <T> type of plan this class can instantiate
 */
public abstract class Builder<T extends CellType> {
	/**
	 * @param plan plans of the structure
	 * @param origin location of the structure's lowest corner
	 * @param args additional arguments, which may or may not be optional
	 * @return the task whose ticking causes new maze segments to be placed
	 * @throws KnossosException if the plan is incompatible with the builder
	 */
	public abstract BuildTask build(Plan<T> plan, Location origin, Arguments args) throws KnossosException;
	
	
	
	protected static boolean randBool() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	protected static int randInt(int exclusiveMax) {
		return ThreadLocalRandom.current().nextInt(exclusiveMax);
	}
}
