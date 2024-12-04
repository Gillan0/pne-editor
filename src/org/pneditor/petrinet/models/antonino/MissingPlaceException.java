package org.pneditor.petrinet.models.antonino;

/** 
 * MissingPlaceException is a custom exception class used to indicate that an expected Place is missing from the Petri net, usually during removal or access operations.
 */
public class MissingPlaceException extends Exception {

    /**
     * Constructor for MissingPlaceException.
     *
     * @param message The error message to display when the exception is thrown.
     */
    public MissingPlaceException(String message) {
        super(message); // Passes the message to the superclass (Exception) constructor.
    }
}
