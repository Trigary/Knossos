package hu.trigary.knossos.build.impl;

import hu.trigary.knossos.build.BuildTask;
import hu.trigary.knossos.build.Builder;
import hu.trigary.knossos.data.Arguments;
import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import hu.trigary.knossos.util.WeightedSelection;
import javafx.util.Pair;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Instantiates a random (suitable) schematic for each cell, depending on the cell's type, size.
 */
public class CellSelectorBuilder<T extends CellType> extends Builder<T> {
	private final Map<String, CompleteSelection<T>> selections = new HashMap<>();
	
	/**
	 * @param name name of the selection
	 * @param cell type of cell the selection is valid for
	 * @param size size of the schematic ({@link Plan#getSize()})
	 * @param selection selection to use, the map will be cloned
	 */
	public void setSelection(String name, T cell, int size, Map<Schematic, Integer> selection) {
		Validate.notNull(name, "The selection name mustn't be null.");
		Validate.notNull(cell, "The cell type mustn't be null.");
		Validate.isTrue(size > 0, "The size must be positive.");
		selections.computeIfAbsent(name, n -> new CompleteSelection<>())
				.setSelection(new Pair<>(cell, size), selection);
	}
	
	
	
	@Override
	public BuildTask build(Plan<T> plan, Location origin, Arguments args) throws KnossosException {
		CompleteSelection<T> selection = selections.get(args.getValue("selection"));
		if (selection == null) {
			throw new KnossosException("The 'selection' parameter was invalid.");
		}
		
		return new BuildTask(plan, origin) {
			@Override
			protected void buildCell(int x, int z, Location corner) {
				Schematic schematic = selection.getRandom(new Pair<>(plan.getBlock(x, z), plan.getSize()));
				if (schematic == null) {
					return;
				}
				
				//TODO instantiate the schematic
			}
		};
	}
	
	
	
	private static class CompleteSelection<T extends CellType> {
		private final Map<Pair<T, Integer>, WeightedSelection<Schematic>> selections = new HashMap<>();
		
		/**
		 * @param type cell type and size of the schematics
		 * @param selection {@link Schematic}-chance pairs, the map will be cloned.
		 */
		private void setSelection(Pair<T, Integer> type, Map<Schematic, Integer> selection) {
			WeightedSelection<Schematic> previous = selections.put(type, new WeightedSelection<>(selection, schematic -> {
				Validate.notNull(schematic, "Schematics mustn't be null.");
				return schematic;
			}));
			if (previous != null) {
				Bukkit.getLogger().warning("The cell type " + type.getKey() +
						", size " + type.getValue() + " selection was overridden.");
			}
		}
		
		/**
		 * @param type cell type and size of the schematics
		 * @return the random schematic or null, if none were registered
		 */
		private Schematic getRandom(Pair<T, Integer> type) {
			WeightedSelection<Schematic> selection = selections.get(type);
			return selection == null ? null : selection.getRandom();
		}
	}
	
	public static class Schematic { //TODO Use WorldEdit's schematics maybe
	
	}
}
