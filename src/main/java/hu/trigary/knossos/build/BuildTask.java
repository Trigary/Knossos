package hu.trigary.knossos.build;

import hu.trigary.knossos.Knossos;

/**
 * A task which can be scheduled in the {@link Knossos} instance for execution.
 */
public interface BuildTask {
	/**
	 * @return true if the task is not yet finished - whether the tick method should be called more
	 */
	boolean tick();
}
