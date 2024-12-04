package org.pneditor.petrinet.models.antonino;

/** 
 * MissingArcException is a custom exception class used to indicate that an expected arc is missing from the Petri net, usually during remove or access operations.
 */
public class MissingArcException extends Exception {

    /**
     * Constructor for MissingArcException.
     *
     * @param message The error message to display when the exception is thrown.
     */
    public MissingArcException(String message) {
        super(message); // Passes the message to the superclass (Exception) constructor.
    }
}
