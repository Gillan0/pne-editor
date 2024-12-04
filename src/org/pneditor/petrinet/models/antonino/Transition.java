package org.pneditor.petrinet.models.antonino;

import java.util.ArrayList;

/**
 * Transition models a transition in the PetriNet.
 * It manages incoming arcs from Places (ArcPT) and outgoing arcs to Places (ArcTP).
 */
public class Transition {

    /**
     *  List of arcs going from this Transition to connected Places.
     */
    private ArrayList<ArcTP> arcsTP;
    
    /**
     *  List of arcs going from connected Places to this Transition.
     */
    private ArrayList<ArcPT> arcsPT;

    /**
     * Default constructor to initialize the Transition with empty lists of arcs.
     */
    public Transition() {
        this.arcsTP = new ArrayList<ArcTP>();
        this.arcsPT = new ArrayList<ArcPT>();
    }

    /**
     * Adds an ArcTP (Transition to Place arc) to the Transition.
     *
     * @param a The ArcTP to add.
     */
    public void addArcTP(ArcTP a) {
        if (a != null) {
            this.arcsTP.add(a);
        }
    }

    /**
     * Adds an ArcPT (Place to Transition arc) to the Transition.
     *
     * @param a The ArcPT to add.
     */
    public void addArcPT(ArcPT a) {
        if (a != null) {
            this.arcsPT.add(a);
        }
    }

    /**
     * Retrieves the list of ArcTP objects (outgoing arcs) associated with this Transition.
     *
     * @return The list of ArcTP objects.
     */
    public ArrayList<ArcTP> getArcsTP() {
        return this.arcsTP;
    }

    /**
     * Retrieves the list of ArcPT objects (incoming arcs) associated with this Transition.
     *
     * @return The list of ArcPT objects.
     */
    public ArrayList<ArcPT> getArcsPT() {
        return this.arcsPT;
    }

    /**
     * Removes an ArcTP (Transition to Place arc) from the Transition.
     *
     * @param a The ArcTP to remove.
     */
    public void removeArcTP(ArcTP a) {
        if (a != null) {
            this.arcsTP.remove(a);
        }
    }

    /**
     * Removes an ArcPT (Place to Transition arc) from the Transition.
     *
     * @param a The ArcPT to remove.
     */
    public void removeArcPT(ArcPT a) {
        if (a != null) {
            this.arcsPT.remove(a);
        }
    }
    
    /**
     * Checks if the transition is can be fired
     * @return true if the Transition is Fireable, false otherwise.
     */
    public boolean isFireable() {
        for (ArcPT aPT : this.arcsPT) {
            if (!aPT.isFireable()) {
                // Transition is not enabled
                return false;
            }
        }
        return true;
    }
    
    /**
     *  Fires the Transition, taking Tokens from input Places and Adding them to output Places.
     */
    public void fire() {
        // Consume tokens from all input places
        for (ArcPT aPT : this.arcsPT) {
            aPT.removeTokens();
        }

        // Produce tokens to all output places
        for (ArcTP aTP : this.arcsTP) {
            aTP.distributeTokens();
        }

    }

}
