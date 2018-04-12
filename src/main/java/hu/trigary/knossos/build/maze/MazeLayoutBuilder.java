package hu.trigary.knossos.build.maze;

import hu.trigary.knossos.build.BuildTask;
import hu.trigary.knossos.build.Builder;
import hu.trigary.knossos.plan.Plan;
import hu.trigary.knossos.data.cell.MazeCellType;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MazeLayoutBuilder extends Builder<MazeCellType> {
	private static final Map<MazeCellType, Material> MATERIALS = new HashMap<>();
	
	static {
		MATERIALS.put(MazeCellType.WALL, Material.COAL_BLOCK);
		
		MATERIALS.put(MazeCellType.PATH_START, Material.IRON_BLOCK);
		MATERIALS.put(MazeCellType.PATH_FINISH, Material.GOLD_BLOCK);
		MATERIALS.put(MazeCellType.PATH_DEAD_END, Material.REDSTONE_BLOCK);
		
		MATERIALS.put(MazeCellType.PATH_JUNCTION, Material.COBBLESTONE);
		MATERIALS.put(MazeCellType.PATH_CORRIDOR, Material.STONE);
		MATERIALS.put(MazeCellType.PATH_CORNER, Material.SMOOTH_BRICK);
	}
	
	@Override
	public BuildTask build(Plan<MazeCellType> plan, Location origin) {
		return () -> {
			for (int x = 0; x < plan.getWidth(); x++) {
				for (int z = 0; z < plan.getLength(); z++) {
					origin.clone()
							.add(x, 0, z)
							.getBlock()
							.setType(MATERIALS.get(plan.getBlock(x, z)));
				}
			}
			return false;
		};
	}
}
