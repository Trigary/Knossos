package hu.trigary.knossos.util;

import java.util.HashMap;

public class ChainedHashMap<K, V> extends HashMap<K, V> {
	public ChainedHashMap add(K key, V value) {
		put(key, value);
		return this;
	}
}
