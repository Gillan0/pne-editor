package org.pneditor.petrinet.models.antonino;

/**
 * NegativeException is a custom exception class used to indicate that a negative value has been provided where it is not permitted, such as for token counts or arc weights.
 */
public class NegativeException extends Exception {

    /**
     * Constructor for NegativeException.
     *
     * @param message The error message to display when the exception is thrown.
     */
    public NegativeException(String message) {
        super(message); // Passes the message to the superclass (Exception) constructor.
    }
}
