package org.pneditor.petrinet.models.antonino;

/**
 * NotFireableTransitionException is a custom exception class used to indicate that a Transition cannot be fired.
 */
public class NotFireableTransitionException extends Exception {

	/**
     * Constructor for NotFireableTransitionException.
     *
     * @param message The error message to display when the exception is thrown.
     */
	public NotFireableTransitionException(String message) {
		
		super(message);// Passes the message to the superclass (Exception) constructor.
	
	}

}
