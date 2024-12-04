package org.pneditor.petrinet.models.antonino;

/**
 * DuplicateArcException is a custom exception class used to indicate that an attempt was made to add an arc that already exists between a given Place and Transition.
 */
public class DuplicateArcException extends Exception {

    /**
     * Constructor for DuplicateArcException.
     *
     * @param message The error message to display when the exception is thrown.
     */
    public DuplicateArcException(String message) {
        super(message); // Passes the message to the superclass (Exception) constructor.
    }
}
