package org.pneditor.petrinet.models.antonino;


/**
 * ArcPT models an arc that goes from a Place to a Transition (Arc Place to Transition).
 * It inherits the Arc class, adding specific behaviors for firing and token removal.
 */
public class ArcPT extends Arc {

    /**
     * Constructor to create an ArcPT with a specified weight, Place, and Transition.
     *
     * @param weight      The number of tokens required to fire the transition.
     * @param place       The Place connected to this arc.
     * @param transition  The Transition connected to this arc.
     */
    public ArcPT(int weight, Place place, Transition transition) {
        super(weight, place, transition); // Initialize the superclass (Arc) with the given parameters.
    }

    /**
     * Determines if the transition connected to this arc can fire.
     * The transition is fireable if the Place has enough tokens (>= weight of the arc).
     *
     * @return true if the transition can fire, false otherwise.
     */
    public boolean isFireable() {
        Place place = this.getPlace();
        return place.getTokens() >= this.getWeight();
    }

    /**
     * Removes tokens from the Place connected to this arc, based on the weight of the arc.
     * This operation is typically called when the transition fires.
     */
    public void removeTokens() {
        Place place = this.getPlace();
        place.removeTokens(this.getWeight());
    }
}
