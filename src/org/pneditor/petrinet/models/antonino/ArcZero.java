package org.pneditor.petrinet.models.antonino;


/**
 * ArcZero is a specialized ArcPT that fires only when the connected Place has zero tokens.
 */
public class ArcZero extends ArcPT {

    /**
     * Constructor to create an ArcZero with a specified weight, Place, and Transition.
     *
     * @param weight      The number of tokens required to fire the transition.
     * @param place       The Place connected to this arc.
     * @param transition  The Transition connected to this arc.
     */
    public ArcZero(int weight, Place place, Transition transition) {
        super(weight, place, transition); // Initialize the superclass (ArcPT) with the given parameters.
    }

    /**
     * Checks if the transition connected to this arc can fire.
     * The transition is fireable only if this arc is active (Place has zero tokens)
     * and if it meets the firing conditions defined in ArcPT.
     *
     * @return true if the transition can fire, false otherwise.
     */
    @Override
    public boolean isFireable() {
        if (this.isActive()) { // Ensures Place has zero tokens before checking further conditions.
            return super.isFireable(); // Calls the ArcPT isFireable() method to check standard firing conditions.
        }
        return false;
    }

    /**
     * Checks if this arc is active.
     * An ArcZero is active when the connected Place has zero tokens.
     *
     * @return true if the Place has zero tokens, false otherwise.
     */
    public boolean isActive() {
        Place place = this.getPlace();
        return place.getTokens() == 0;
    }
}
