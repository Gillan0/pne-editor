package org.pneditor.petrinet.models.antonino;


/**
 * ArcTP models an arc that goes from a Transition to a Place (Arc Transition to Place).
 * It inherits the Arc class and adds specific behavior for token distribution.
 */
public class ArcTP extends Arc {

    /**
     * Constructor to create an ArcTP with a specified weight, Place, and Transition.
     *
     * @param weight      The number of tokens to distribute when the transition fires.
     * @param place       The Place connected to this arc.
     * @param transition  The Transition connected to this arc.
     */
    public ArcTP(int weight, Place place, Transition transition) {
        super(weight, place, transition); // Initialize the superclass (Arc) with the given parameters.
    }

    /**
     * Distributes tokens to the Place connected to this arc based on the weight of the arc.
     * This operation is typically called when the transition fires.
     */
    public void distributeTokens() {
        Place place = this.getPlace();
        place.addTokens(this.getWeight());
    }
}
