package hu.trigary.knossos.command;

import hu.trigary.knossos.Knossos;
import hu.trigary.knossos.build.BuildTask;
import hu.trigary.knossos.build.Builder;
import hu.trigary.knossos.build.impl.LayoutBuilder;
import hu.trigary.knossos.data.Arguments;
import hu.trigary.knossos.data.BlockInfo;
import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.MazeCellType;
import hu.trigary.knossos.plan.Plan;
import hu.trigary.knossos.plan.Planner;
import hu.trigary.knossos.plan.impl.MazePrimPlanner;
import hu.trigary.knossos.util.MapItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

import java.util.HashMap;
import java.util.Map;

public class KnossosCommand implements CommandExecutor {
	public KnossosCommand() { //TODO do this somewhere else
		Knossos knossos = Knossos.getInstance();
		knossos.registerPlanner("maze-prim", new MazePrimPlanner());
		
		LayoutBuilder<MazeCellType> layout = new LayoutBuilder<>();
		Map<MazeCellType, BlockInfo> materials = new HashMap<>();
		materials.put(MazeCellType.WALL, new BlockInfo(Material.COAL_BLOCK));
		materials.put(MazeCellType.PATH_START, new BlockInfo(Material.LAPIS_BLOCK));
		materials.put(MazeCellType.PATH_FINISH, new BlockInfo(Material.REDSTONE_BLOCK));
		materials.put(MazeCellType.PATH_DEAD_END, new BlockInfo(Material.CLAY));
		materials.put(MazeCellType.PATH_JUNCTION, new BlockInfo(Material.COBBLESTONE));
		materials.put(MazeCellType.PATH_CORRIDOR, new BlockInfo(Material.STONE));
		materials.put(MazeCellType.PATH_CORNER, new BlockInfo(Material.SMOOTH_BRICK));
		layout.setMaterials("default", materials);
		knossos.registerBuilder("maze-layout", layout);
	}
	
	
	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp() || !(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		Knossos knossos = Knossos.getInstance();
		String message;
		try {
			Arguments arguments = new Arguments();
			arguments.setValue("layout", "default");
			
			Planner<MazeCellType> planner = (Planner<MazeCellType>) knossos.getPlanner("maze-prim");
			Plan<MazeCellType> plan = planner.plan(11, 11, 1, null, arguments);
			
			Builder<MazeCellType> builder = (Builder<MazeCellType>) knossos.getBuilder("maze-layout");
			BuildTask task = builder.build(plan, player.getLocation().add(1, 0, 1), arguments);
			knossos.scheduleBuild(task);
			
			message = ChatColor.YELLOW + "Maze creation successfully scheduled.";
			player.getInventory().addItem(MapItem.get(player.getLocation(), plan, cell -> {
				switch (cell) {
					case WALL:
						return MapPalette.DARK_BROWN;
					case PATH_START:
						return MapPalette.BLUE;
					case PATH_FINISH:
						return MapPalette.RED;
					case PATH_DEAD_END:
					case PATH_JUNCTION:
					case PATH_CORRIDOR:
					case PATH_CORNER:
						return MapPalette.LIGHT_GRAY;
					default:
						return MapPalette.WHITE;
				}
			}, MapPalette.WHITE)).values().forEach(item -> player.getWorld().dropItem(player.getLocation(), item));
		} catch (KnossosException e) {
			message = ChatColor.RED + "Error while creating maze: " + ChatColor.GRAY + e.getMessage();
		} catch (ClassCastException e) {
			message = ChatColor.RED + "The planner and the builder are incompatible.";
		}
		
		sender.sendMessage(message);
		return true;
	}
}
