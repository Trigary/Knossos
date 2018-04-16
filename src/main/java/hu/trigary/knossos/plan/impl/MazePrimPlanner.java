package hu.trigary.knossos.plan.impl;

import hu.trigary.knossos.data.Arguments;
import hu.trigary.knossos.data.CellCoord;
import hu.trigary.knossos.data.KnossosException;
import hu.trigary.knossos.data.cell.MazeCellType;
import hu.trigary.knossos.plan.Plan;
import hu.trigary.knossos.plan.Planner;
import hu.trigary.knossos.plan.MutablePlan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MazePrimPlanner extends Planner<MazeCellType> {
	@Override
	public Plan<MazeCellType> plan(int width, int length, int size, CellCoord start, Arguments args) throws KnossosException {
		MutablePlan<MazeCellType> plan = new MutablePlan<>(width, length, size, MazeCellType.WALL);
		if (start == null) {
			start = new CellCoord(randBool() ? 0 : width - 1, randBool() ? 0 : length - 1);
		}
		
		calculatePlan(plan, start);
		configurePlan(plan, start);
		return plan;
	}
	
	
	
	private void calculatePlan(MutablePlan<MazeCellType> plan, CellCoord start) {
		List<CellCoord> frontiers = new ArrayList<>(20);
		plan.setBlock(start, MazeCellType.PATH_START);
		getFrontiers(start.getX(), start.getZ(), frontiers, plan);
		List<CellCoord> allFronts = new ArrayList<>(4);
		
		while (!frontiers.isEmpty()) {
			CellCoord wallFront = frontiers.remove(randInt(frontiers.size()));
			if (plan.getBlock(wallFront) != MazeCellType.WALL) {
				continue;
			}
			
			getFrontiers(wallFront.getX(), wallFront.getZ(), allFronts, plan);
			List<CellCoord> pathFronts = allFronts.stream()
					.filter(cell -> plan.getBlock(cell) != MazeCellType.WALL)
					.collect(Collectors.toList());
			if (pathFronts.isEmpty()) {
				continue;
			}
			
			CellCoord pathFront = pathFronts.get(randInt(pathFronts.size()));
			plan.setBlock(wallFront, MazeCellType.PATH_CORRIDOR);
			plan.setBlock((wallFront.getX() + pathFront.getX()) / 2,
					(wallFront.getZ() + pathFront.getZ()) / 2, MazeCellType.PATH_CORRIDOR);
			
			allFronts.stream()
					.filter(cell -> plan.getBlock(cell) == MazeCellType.WALL)
					.forEach(frontiers::add);
			allFronts.clear();
		}
	}
	
	private void configurePlan(MutablePlan<MazeCellType> plan, CellCoord start) throws KnossosException {
		List<CellCoord> deadEnds = new ArrayList<>();
		for (int x = 0; x < plan.getWidth(); x++) {
			for (int z = 0; z < plan.getLength(); z++) {
				MazeCellType type = plan.getBlock(x, z);
				if (type != MazeCellType.PATH_CORRIDOR) {
					continue;
				}
				
				boolean[] neighbours = new boolean[]{
						isNotWall(x + 1, z, plan),
						isNotWall(x - 1, z, plan),
						isNotWall(x, z + 1, plan),
						isNotWall(x, z - 1, plan),
				};
				
				int count = 0;
				for (boolean neighbour : neighbours) {
					if (neighbour) {
						count++;
					}
				}
				
				if (count == 1) {
					plan.setBlock(x, z, MazeCellType.PATH_DEAD_END);
					deadEnds.add(new CellCoord(x, z));
				} else if (count == 2) {
					if (!((neighbours[0] && neighbours[1]) || (neighbours[2] && neighbours[3]))) {
						plan.setBlock(x, z, MazeCellType.PATH_CORNER);
					}
				} else if (count > 2) {
					plan.setBlock(x, z, MazeCellType.PATH_JUNCTION);
				}
			}
		}
		
		Optional<CellCoord> finish = deadEnds.stream()
				.max(Comparator.comparingInt(cell -> {
					int x = cell.getX() - start.getX();
					int z = cell.getZ() - start.getZ();
					return x * x + z * z;
				}));
		if (finish.isPresent()) {
			plan.setBlock(finish.get(), MazeCellType.PATH_FINISH);
		} else {
			throw new KnossosException("The maze does not contain any dead ends.");
		}
	}
	
	
	
	private void getFrontiers(int x, int z, List<CellCoord> listToUse, Plan<MazeCellType> plan) {
		addIfInside(x + 2, z, listToUse, plan);
		addIfInside(x - 2, z, listToUse, plan);
		addIfInside(x, z + 2, listToUse, plan);
		addIfInside(x, z - 2, listToUse, plan);
	}
	
	private void addIfInside(int x, int z, List<CellCoord> list, Plan<MazeCellType> plan) {
		if (plan.isInside(x, z)) {
			list.add(new CellCoord(x, z));
		}
	}
	
	private boolean isNotWall(int x, int z, Plan<MazeCellType> plan) {
		MazeCellType type = plan.getBlock(x, z);
		return type != null && type != MazeCellType.WALL;
	}
}
