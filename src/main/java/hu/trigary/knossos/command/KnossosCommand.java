package hu.trigary.knossos.command;

import hu.trigary.knossos.Knossos;
import hu.trigary.knossos.build.BuildTask;
import hu.trigary.knossos.build.Builder;
import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import hu.trigary.knossos.plan.Planner;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KnossosCommand implements CommandExecutor {
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp() || !(sender instanceof Player)) {
			return false;
		}
		
		Knossos knossos = Knossos.getInstance();
		String message;
		try {
			Planner<CellType> planner = (Planner<CellType>) knossos.getPlanner("maze-prim");
			Plan<CellType> plan = planner.plan(11, 11, 1, null, null); //TODO create a map ItemStack from the layout
			Builder<CellType> builder = (Builder<CellType>) knossos.getBuilder("maze-layout");
			BuildTask task = builder.build(plan, ((Player) sender).getLocation().add(1, 0, 1));
			knossos.scheduleBuild(task);
			message = ChatColor.YELLOW + "Maze creation successfully started.";
		} catch (KnossosException e) {
			message = ChatColor.RED + "Error while creating maze: " + ChatColor.GRAY + e.getMessage();
		}
		
		sender.sendMessage(message);
		return true;
	}
}
