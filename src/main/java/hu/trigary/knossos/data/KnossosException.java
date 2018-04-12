package hu.trigary.knossos.data;

/**
 * An exception which indicates that something went wrong, possibly due to error in the input.
 * The errors are always worded to be comprehensible by end-users.
 */
public class KnossosException extends Exception {
	public KnossosException(String message) {
		super(message);
	}
}
