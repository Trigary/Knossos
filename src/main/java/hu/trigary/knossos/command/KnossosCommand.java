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
import hu.trigary.knossos.util.ChainedHashMap;
import hu.trigary.knossos.util.MapItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
		materials.put(MazeCellType.PATH_START, new BlockInfo(Material.IRON_BLOCK));
		materials.put(MazeCellType.PATH_FINISH, new BlockInfo(Material.GOLD_BLOCK));
		materials.put(MazeCellType.PATH_DEAD_END, new BlockInfo(Material.REDSTONE_BLOCK));
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
			Plan<MazeCellType> plan = planner.plan(31, 31, 1, null, arguments);
			
			Builder<MazeCellType> builder = (Builder<MazeCellType>) knossos.getBuilder("maze-layout");
			BuildTask task = builder.build(plan, player.getLocation().add(1, 0, 1), arguments);
			knossos.scheduleBuild(task);
			message = ChatColor.YELLOW + "Maze creation successfully scheduled.";
			
			player.getInventory().addItem(MapItem.get(player.getLocation(), plan, new ChainedHashMap<MazeCellType, Byte>()
							.add(MazeCellType.WALL, MapPalette.DARK_BROWN)
							.add(MazeCellType.PATH_START, MapPalette.BLUE)
							.add(MazeCellType.PATH_FINISH, MapPalette.RED)
							.add(MazeCellType.PATH_DEAD_END, MapPalette.LIGHT_GRAY)
							.add(MazeCellType.PATH_JUNCTION, MapPalette.LIGHT_GRAY)
							.add(MazeCellType.PATH_CORRIDOR, MapPalette.LIGHT_GRAY)
							.add(MazeCellType.PATH_CORNER, MapPalette.LIGHT_GRAY),
					MapPalette.WHITE)).values().forEach(item -> player.getWorld().dropItem(player.getLocation(), (ItemStack) item));
		} catch (KnossosException e) {
			message = ChatColor.RED + "Error while creating maze: " + ChatColor.GRAY + e.getMessage();
		} catch (ClassCastException e) {
			message = ChatColor.RED + "The planner and the builder are incompatible.";
		}
		
		sender.sendMessage(message);
		return true;
	}
}
