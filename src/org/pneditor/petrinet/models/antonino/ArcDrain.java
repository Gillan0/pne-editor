package org.pneditor.petrinet.models.antonino;

/**
 * ArcDrain is a specialized ArcPT that removes all tokens from the connected Place when the transition fires.
 */
public class ArcDrain extends ArcPT {

    /**
     * Constructor to create an ArcDrain with a specified weight, Place, and Transition.
     *
     * @param weight      The number of tokens required to fire the transition.
     * @param place       The Place connected to this arc.
     * @param transition  The Transition connected to this arc.
     */
    public ArcDrain(int weight, Place place, Transition transition) {
        super(weight, place, transition); // Initialize the superclass (ArcPT) with the given parameters.
    }

    /**
     * Checks if the transition connected to this arc can fire.
     * The transition is fireable only if this arc is active (Place has at least one token)
     * and if it meets the firing conditions defined in ArcPT.
     *
     * @return true if the transition can fire, false otherwise.
     */
    @Override
    public boolean isFireable() {
        if (this.isActive()) { // Ensures the Place has tokens before checking further conditions.
            return super.isFireable(); // Calls the ArcPT isFireable() method to check standard firing conditions.
        }
        return false;
    }

    /**
     * Checks if this arc is active.
     * An ArcDrain is active when the connected Place has one or more tokens.
     *
     * @return true if the Place has at least one token, false otherwise.
     */
    public boolean isActive() {
        Place place = this.getPlace();
        return place.getTokens() > 0;
    }

    /**
     * Removes all tokens from the connected Place.
     * This method sets the token count of the Place to zero when the transition fires.
     */
    @Override
    public void removeTokens() {
    	Place place = this.getPlace();
    	int tokens = place.getTokens();
        place.removeTokens(tokens); // Sets token count to zero, effectively draining all tokens.
    }
}
