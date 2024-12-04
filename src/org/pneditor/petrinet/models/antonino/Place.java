package org.pneditor.petrinet.models.antonino;

import java.util.ArrayList;



/**
 * Place models a location in the PetriNet where tokens are held.
 * Tokens in a Place determine if a Transition can fire, based on arc conditions.
 */
public class Place {

    /**
     *  The number of tokens currently held in this Place.
     */
    private int tokens;
       
    /**
     * Constructor to create a Place with a specified initial number of tokens.
     *
     * @param tokens The initial token count for this Place.
     */
    public Place(int tokens) {
        this.tokens = tokens;
    }

    /**
     * Retrieves the current token count for this Place.
     *
     * @return The current number of tokens in this Place.
     */
    public int getTokens() {
        return this.tokens;
    }

    /**
     * Sets the token count for this Place.
     *
     * @param tokens The new token count to set.
     */
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
    
    /**
     * Adds tokens to this Place.
     * This operation is typically called when the transition fires.
     * 
     * @param w Amount of tokens to add to this place
     */
    public void addTokens(int w) {
        this.setTokens(this.getTokens() + w);
    }
    
    /**
     * Removes tokens from this Places.
     * This operation is typically called when the transition fires.
     * 
     * @param w Amount of tokens to remove from this place
     */
    public void removeTokens(int w) {
        this.setTokens(this.getTokens() - w);
    }
    
    /**
     * Removes all Arcs connected to this Place from a PetriNet
     * 
     * @param pN The PetriNet to remove this place's connected Arcs from
     * @throws MissingArcException Remnant from PetriNet.removeArc methods 
     * 							   Should never be called
     */
    public void removeConnectedArcs(PetriNet pN) throws MissingArcException {
	    for (Transition  t: pN.getTransitions()) {
	    	
	    	ArrayList<ArcTP> arcsTP = t.getArcsTP();
	    	ArrayList<ArcPT> arcsPT = t.getArcsPT();
	    	
	    	for (int i = 0; i < arcsTP.size(); i++) {
	    		ArcTP aTP = arcsTP.get(i);
	    		if (aTP.getPlace() == this) {
	    			pN.removeArcTP(arcsTP.get(i));
	    		}
	    	}
	    	 
	    	for (int i = 0; i < arcsPT.size(); i++) {
	    		ArcPT aPT = arcsPT.get(i);
	    		if (aPT.getPlace() == this) {
	    			pN.removeArcPT(arcsPT.get(i));
	    		}
	    	}
	    }
    }
    
}
