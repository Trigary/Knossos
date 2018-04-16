package hu.trigary.knossos.util;

import org.bukkit.Location;

public class FormatterUtils {
	public static String locationToString(Location loc) {
		return loc.getWorld().getName() + "#" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
	}
}
