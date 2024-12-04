package org.pneditor.petrinet.models.antonino;

/** 
 * MissingTransitionException is a custom exception class used to indicate that an expected Transition is missing from the Petri net, usually during removal or access operations.
 */
public class MissingTransitionException extends Exception {

    /**
     * Constructor for MissingTransitionException.
     *
     * @param message The error message to display when the exception is thrown.
     */
    public MissingTransitionException(String message) {
        super(message); // Passes the message to the superclass (Exception) constructor.
    }
}
