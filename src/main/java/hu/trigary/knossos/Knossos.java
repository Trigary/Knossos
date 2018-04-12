package hu.trigary.knossos;

import hu.trigary.knossos.build.BuildTask;
import hu.trigary.knossos.build.Builder;
import hu.trigary.knossos.build.maze.MazeLayoutBuilder;
import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Planner;
import hu.trigary.knossos.plan.maze.MazePrimPlanner;
import hu.trigary.knossos.command.KnossosCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Knossos extends JavaPlugin {
	private static Knossos instance;
	private final Map<String, Planner<? extends CellType>> planners = new HashMap<>();
	private final Map<String, Builder<? extends CellType>> builders = new HashMap<>();
	private final Queue<BuildTask> buildTasks = new ArrayDeque<>();
	private BukkitTask currentBuild = null;
	private int segmentPerTick = 1;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		segmentPerTick = getConfig().getInt("segment-per-tick");
		
		registerPlanner("maze-prim", new MazePrimPlanner());
		registerBuilder("maze-layout", new MazeLayoutBuilder());
		
		getCommand("knossos").setExecutor(new KnossosCommand());
	}
	
	
	
	public static Knossos getInstance() {
		return instance;
	}
	
	
	
	public void registerPlanner(String name, Planner<? extends CellType> planner) {
		Planner previous = planners.get(name);
		if (previous != null) {
			throw new IllegalStateException("A planner with the specified name ("
					+ name + ") is already registered: " + previous.getClass().getName());
		}
		planners.put(name, planner);
	}
	
	public Planner<? extends CellType> getPlanner(String name) {
		return planners.get(name);
	}
	
	
	
	public void registerBuilder(String name, Builder<? extends CellType> builder) {
		Builder previous = builders.get(name);
		if (previous != null) {
			throw new IllegalStateException("A builder with the specified name ("
					+ name + ") is already registered: " + previous.getClass().getName());
		}
		builders.put(name, builder);
	}
	
	public Builder<? extends CellType> getBuilder(String name) {
		return builders.get(name);
	}
	
	
	
	/**
	 * @param task the task to put into the to-build queue
	 */
	public void scheduleBuild(BuildTask task) {
		if (currentBuild != null) {
			buildTasks.add(task);
		} else {
			startBuild(task);
		}
	}
	
	/**
	 * @return whether a build was actually happening - whether something was actually cancelled
	 */
	public boolean cancelBuild() {
		if (currentBuild != null) {
			currentBuild.cancel();
			return true;
		} else {
			return false;
		}
	}
	
	private void startBuild(BuildTask task) {
		currentBuild = new BukkitRunnable() {
			@Override
			public void run() {
				boolean done = false;
				for (int i = 0; i < segmentPerTick; i++) {
					if (!task.tick()) {
						done = true;
						break;
					}
				}
				
				if (!done) {
					return;
				}
				
				cancel();
				BuildTask newTask = buildTasks.poll();
				if (newTask != null) {
					startBuild(newTask);
				} else {
					currentBuild = null;
				}
			}
		}.runTaskTimer(this, 0, 1);
	}
}
