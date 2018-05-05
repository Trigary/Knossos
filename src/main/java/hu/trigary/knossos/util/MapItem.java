package hu.trigary.knossos.util;

import hu.trigary.knossos.data.cell.CellType;
import hu.trigary.knossos.plan.Plan;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.Map;

/**
 * A utility class for creating in-game map items with a maze or dungeon {@link Plan} drawn on them.
 */
public class MapItem {
	/**
	 * @param plan {@link Plan} to draw, must be at most 128x128
	 * @param colorMap maps cell types to colors
	 * @param defaultColor color of all non-plan pixels
	 * @param <T> the type of plan which should be applied
	 * @return the creates map item
	 */
	@SuppressWarnings("deprecation")
	public static <T extends CellType> ItemStack get(Location center, Plan<T> plan, Map<T, Byte> colorMap, byte defaultColor) {
		Validate.isTrue(plan.getLength() <= 128 && plan.getWidth() <= 128, "The plan must be at most 128x128.");
		MapView map = Bukkit.createMap(center.getWorld());
		ItemStack item = new ItemStack(Material.MAP, 1, map.getId());
		
		map.setUnlimitedTracking(false);
		map.setScale(MapView.Scale.CLOSEST);
		map.getRenderers().clear();
		map.addRenderer(new MapRenderer() {
			@Override
			public void render(MapView map, MapCanvas canvas, Player player) {
				int scaleX = 128 / plan.getWidth();
				int scaleY = 128 / plan.getLength();
				int scale = scaleX < scaleY ? scaleX : scaleY;
				
				int maxX = scale * plan.getWidth();
				int maxY = scale * plan.getLength();
				
				for (int x = 0; x < 128; x++) {
					for (int y = 0; y < 128; y++) {
						if (x < maxX && y < maxY) {
							canvas.setPixel(x, y, colorMap.get(plan.getBlock(x / scale, y / scale)));
						} else {
							canvas.setPixel(x, y, defaultColor);
						}
					}
				}
			}
		});
		return item;
	}
}
