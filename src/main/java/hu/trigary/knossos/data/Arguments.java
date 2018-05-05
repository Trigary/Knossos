package hu.trigary.knossos.data;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of arguments which can either be key-value pairs or valueless.
 */
public class Arguments {
	private final Map<String, String> content = new HashMap<>();
	
	public Arguments() {}
	
	/**
	 * @param input {@link String} instances to process
	 * @param startIndex index of the first element which should be processed
	 */
	public Arguments(String[] input, int startIndex) {
		for (int i = startIndex; i < input.length; i++) {
			String raw = input[i];
			int separator = raw.indexOf('=');
			if (separator == -1) {
				content.put(raw, null);
			} else {
				content.put(raw.substring(0, separator), raw.substring(separator + 1));
			}
		}
	}
	
	
	
	/**
	 * @param key argument's identifier
	 * @return the value of the specified argument or null, if it doesn't exist or if it's valueless
	 */
	public String getValue(String key) {
		return content.get(key);
	}
	
	/**
	 * @param key argument's identifier
	 * @return true if an argument with the specified identifier is present, false otherwise
	 */
	public boolean isSet(String key) {
		return content.containsKey(key);
	}
	
	/**
	 * @param key argument's identifier
	 * @param value value or null, if the argument should be valueless
	 * @return the current {@link Arguments} instance for chaining
	 */
	public Arguments setValue(String key, String value) {
		content.put(key, value);
		return this;
	}
}
