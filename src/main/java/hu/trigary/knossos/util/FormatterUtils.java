package hu.trigary.knossos.util;

import org.bukkit.Location;

/**
 * General utilities regarding formatting.
 */
public class FormatterUtils {
	public static String locationToString(Location loc) {
		return loc.getWorld().getName() + "#" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
	}
}
