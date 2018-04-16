package hu.trigary.knossos.util;

import org.apache.commons.lang.Validate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * A selection from which random elements can be retrieved based on their weight.
 *
 * @param <T> type of selection to create
 */
public class WeightedSelection<T> {
	private final LinkedHashMap<Integer, T> selection = new LinkedHashMap<>();
	private final int chanceSum;
	
	/**
	 * @param selection object-chance pairs, the map will be cloned
	 * @param mapper maps the keys in the input array to something else
	 */
	public WeightedSelection(Map<T, Integer> selection, Function<T, T> mapper) {
		Validate.notEmpty(selection, "The selection cannot be empty.");
		int counter = 0;
		for (Map.Entry<T, Integer> entry : selection.entrySet()) {
			Validate.isTrue(entry.getValue() != null && entry.getValue() > 0, "The chances in the map must be positive.");
			counter += entry.getValue();
			this.selection.put(counter, mapper.apply(entry.getKey()));
		}
		chanceSum = counter;
	}
	
	
	
	
	/**
	 * @return the randomly selected element, which can be null
	 */
	public T getRandom() {
		int random = ThreadLocalRandom.current().nextInt(chanceSum);
		for (Map.Entry<Integer, T> entry : selection.entrySet()) {
			if (entry.getKey() < random) {
				return entry.getValue();
			}
		}
		return null;
	}
}
