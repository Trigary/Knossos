package hu.trigary.knossos.build.impl;

import hu.trigary.knossos.build.BuildTask;
import hu.trigary.knossos.build.Builder;
import hu.trigary.knossos.data.Arguments;
import hu.trigary.knossos.data.BlockInfo;
import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps a {@link CellType} to a {@link BlockInfo}: instantiates the {@link Plan}'s structure.
 */
public class LayoutBuilder<T extends CellType> extends Builder<T> {
	private final Map<String, Map<T, BlockInfo>> materials = new HashMap<>();
	
	/**
	 * @param name name of the material table
	 * @param materials materials to use, the map will be cloned
	 */
	public void setMaterials(String name, Map<T, BlockInfo> materials) {
		Validate.notNull(name, "The name mustn't be null.");
		if (this.materials.put(name, materials) != null) {
			Bukkit.getLogger().warning("A material table named " + name + " was overridden.");
		}
	}
	
	
	
	@Override
	public BuildTask build(Plan<T> plan, Location origin, Arguments args) throws KnossosException {
		Map<T, BlockInfo> materials = this.materials.get(args.getValue("layout"));
		if (materials == null) {
			throw new KnossosException("The 'layout' parameter was invalid.");
		}
		
		return new BuildTask(plan, origin) {
			@Override
			protected void buildCell(int x, int z, Location corner) {
				for (int ix = 0; ix < plan.getSize(); ix++) {
					for (int iz = 0; iz < plan.getSize(); iz++) {
						materials.getOrDefault(plan.getBlock(x, z), BlockInfo.AIR)
								.apply(corner.clone().add(ix, 0, iz).getBlock());
					}
				}
			}
		};
	}
}
