package org.pneditor.petrinet.models.antonino;

/**
 * The Arc class models an arc in a PetriNet, connecting a Place and a Transition with a specific weight.
 */
public class Arc {

    /**
     *  Weight of the arc, representing the number of tokens transferred upon firing a transition.
     */
    protected final int weight;
    
    /**
     *  The Place linked to this arc.
     */
    private final Place place;
    
    /**
     *  The Transition linked to this arc.
     */
    private final Transition transition;

    /**
     * Constructor to initialize an Arc with a weight, a Place, and a Transition.
     *
     * @param weight      The number of tokens this arc transfers when the transition fires.
     * @param place       The Place connected to this arc.
     * @param transition  The Transition connected to this arc.
     */
    public Arc(int weight, Place place, Transition transition) {
        this.weight = weight;
        this.place = place;
        this.transition = transition;
    }

    /**
     * Retrieves the Place associated with this arc.
     *
     * @return the Place connected to this arc.
     */
    public Place getPlace() {
        return this.place;
    }

    /**
     * Retrieves the weight of this arc.
     *
     * @return the number of tokens this arc transfers.
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Retrieves the Transition associated with this arc.
     *
     * @return the Transition connected to this arc.
     */
    public Transition getTransition() {
        return this.transition;
    }
}
