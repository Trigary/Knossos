package hu.trigary.knossos.data;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Stores {@link Material} - data {@link Byte} pair.
 */
public class BlockInfo {
	public static final BlockInfo AIR = new BlockInfo(Material.AIR);
	private final Material material;
	private final byte data;
	
	public BlockInfo(Material material, int data) {
		this.material = material;
		this.data = (byte) data;
	}
	
	public BlockInfo(Material material) {
		this(material, 0);
	}
	
	
	
	public Material getMaterial() {
		return material;
	}
	
	public byte getData() {
		return data;
	}
	
	@SuppressWarnings("deprecation")
	public void apply(Block block) {
		block.setTypeIdAndData(material.getId(), data, false);
	}
}
