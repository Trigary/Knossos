package hu.trigary.knossos.build.impl;

import hu.trigary.knossos.build.BuildTask;
import hu.trigary.knossos.build.Builder;
import hu.trigary.knossos.data.Arguments;
import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import hu.trigary.knossos.util.FormatterUtils;
import hu.trigary.knossos.util.WeightedSelection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Checks all blocks in the maze (the bounds are specified by {@link Plan})
 * and if the block is a {@link Container} and it contains an item named {@link #LOOT_TABLE_PREFIX}
 * (followed by the name of a registered {@link LootTable}), it will replace all empty slots
 * (and the item specified before) in the inventory with the random items/air from the {@link LootTable}.
 */
public class LootBuilder<T extends CellType> extends Builder<T> {
	public static final String LOOT_TABLE_PREFIX = "$loot=";
	private final Map<String, LootTable> tables = new HashMap<>();
	
	public void setLootTable(String name, LootTable table) {
		if (tables.put(name, table) != null) {
			Bukkit.getLogger().warning("A loot table named " + name + " was overridden.");
		}
	}
	
	
	
	@Override
	public BuildTask build(Plan<T> plan, Location origin, Arguments args) throws KnossosException {
		int minHeight = -1;
		int maxHeight = -1;
		boolean success = false;
		
		try {
			minHeight = Integer.valueOf(args.getValue("min-height"));
			maxHeight = Integer.valueOf(args.getValue("max-height"));
		} catch (NumberFormatException ignored) {}
		
		if (minHeight < 0 || maxHeight < 0 || minHeight > maxHeight ||
				origin.getBlockY() + maxHeight >= origin.getWorld().getMaxHeight()) {
			throw new KnossosException("The 'min-height' and/or 'max-height' parameters were invalid. " +
					"They should specify the offset range in which items containers should be searched for.");
		}
		
		final int finalMinHeight = minHeight;
		final int finalMaxHeight = maxHeight;
		return new BuildTask(plan, origin) {
			@Override
			protected void buildCell(int x, int z, Location corner) {
				for (int ix = 0; ix < plan.getSize(); ix++) {
					for (int iy = finalMinHeight; iy <= finalMaxHeight; iy++) {
						for (int iz = 0; iz < plan.getSize(); iz++) {
							handleBlock(corner.clone().add(ix, 0, iz).getBlock());
						}
					}
				}
			}
		};
	}
	
	private void handleBlock(Block block) {
		BlockState state = block.getState();
		if (!(state instanceof Container)) {
			return;
		}
		
		Inventory inventory = ((Container) state).getInventory();
		String tableName = null;
		for (int i = 0; i < inventory.getSize(); i++) {
			ItemStack item = inventory.getItem(i);
			if (item != null && item.hasItemMeta()) {
				String name = item.getItemMeta().getDisplayName();
				if (name != null && name.length() > LOOT_TABLE_PREFIX.length() && name.startsWith(LOOT_TABLE_PREFIX)) {
					tableName = name.substring(LOOT_TABLE_PREFIX.length());
					inventory.setItem(i, null);
					break;
				}
			}
		}
		
		if (tableName == null) {
			return;
		}
		
		LootTable table = tables.get(tableName);
		if (table == null) {
			Bukkit.getLogger().warning("Container specified non-existent loot table (" +
					tableName + ") at: " + FormatterUtils.locationToString(block.getLocation()));
			return;
		}
		
		for (int i = 0; i < inventory.getSize(); i++) {
			ItemStack current = inventory.getItem(i);
			if (current == null || current.getType() == Material.AIR) {
				inventory.setItem(i, table.getRandom());
			}
		}
	}
	
	
	
	/**
	 * A weighted ItemStack selection.
	 */
	public static class LootTable extends WeightedSelection<ItemStack> {
		/**
		 * @param items the item-chance pairs, the map will be cloned.
		 * Use null or {@link Material#AIR} items to represent chance of nothing.
		 */
		public LootTable(Map<ItemStack, Integer> items) {
			super(items, item -> item == null ? null : item.getType() == Material.AIR ? null : item);
		}
	}
}
