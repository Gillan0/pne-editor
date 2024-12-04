package org.pneditor.petrinet.models.antonino;

import java.util.ArrayList;

/**
 * PetriNet implements the IPetriNet interface, providing methods to manage places, transitions, and various arcs in a PetriNet, including adding, removing, and retrieving components.
 */
public class PetriNet implements IPetriNet {

    /**
     *  List of transitions in the PetriNet.
     */
    private ArrayList<Transition> transitions;
    
    /**
     *  List of places in the PetriNet.
     */
    private ArrayList<Place> places;

    /**
     * Default constructor to initialize the PetriNet with empty lists of places and transitions.
     */
    public PetriNet() {
        this.transitions = new ArrayList<Transition>();
        this.places = new ArrayList<Place>();
    }

    /**
     * Retrieves all Places in the PetriNet.
     *
     * @return List of Place objects.
     */
    @Override
    public ArrayList<Place> getPlaces() {
        return this.places;
    }

    /**
     * Retrieves all ArcTP arcs in the PetriNet by iterating over each Transition and collecting its outgoing arcs.
     *
     * @return List of ArcTP objects.
     */
    @Override
    public ArrayList<ArcTP> getArcsTP() {
        ArrayList<ArcTP> arcsTP = new ArrayList<ArcTP>();
        for (Transition transition : this.transitions) {
            arcsTP.addAll(transition.getArcsTP());
        }
        return arcsTP;
    }

    /**
     * Retrieves all ArcPT arcs in the PetriNet by iterating over each Transition and collecting its incoming arcs.
     * Beware ! Includes ArcDrain and ArcZero
     *
     * @return List of ArcPT objects.
     */
    @Override
    public ArrayList<ArcPT> getArcsPT() {
        ArrayList<ArcPT> arcsPT = new ArrayList<ArcPT>();
        for (Transition transition : this.transitions) {
        	arcsPT.addAll(transition.getArcsPT());
        }
        return arcsPT;
    }

	/**
	 * Retrieves all ArcZero objects in the PetriNet.
	 *
	 * @return A list of ArcZero objects.
	 */
	@Override
	public ArrayList<ArcZero> getArcsZero() {
	    ArrayList<ArcZero> arcsZero = new ArrayList<>();
	    for (Transition transition : transitions) {
	        for (ArcPT arc : transition.getArcsPT()) {
	            if (arc instanceof ArcZero) {
	                arcsZero.add((ArcZero) arc);
	            }
	        }
	    }
	    return arcsZero;
	}
	
	/**
	 * Retrieves all ArcDrain objects in the PetriNet.
	 *
	 * @return A list of ArcDrain objects.
	 */
	@Override
	public ArrayList<ArcDrain> getArcsDrain() {
	    ArrayList<ArcDrain> arcsDrain = new ArrayList<>();
	    for (Transition transition : transitions) {
	        for (ArcPT arc : transition.getArcsPT()) {
	            if (arc instanceof ArcDrain) {
	                arcsDrain.add((ArcDrain) arc);
	            }
	        }
	    }
	    return arcsDrain;
	}
	
	/**
	 * Retrieves all Transitions in the PetriNet.
	 *
	 * @return List of Transition objects.
	 */
	@Override
	public ArrayList<Transition> getTransitions() {
	    return this.transitions;
	}
	
    /**
     * Adds a new Place with a specified number of tokens to the PetriNet.
     *
     * @param tokens Initial token count for the Place.
     * @return The created Place object.
     * @throws NegativeException If the token count is negative.
     */
    @Override
    public Place addPlace(int tokens) throws NegativeException {
        if (tokens < 0) {
            throw new NegativeException("Place can't have a negative amount of tokens");
        }
        Place p = new Place(tokens);
        this.places.add(p);
        return p;
    }

    /**
     * Adds tokens to a specified Place already existing in the PetriNet
     * 
     * @param p The Place to add tokens to
     * @param tokens The number of tokens to add
     * @throws MissingPlaceException If the specified Place is not in PetriNet
     * @throws NegativeException If the amount of tokens is negative
     */
    public void addTokens(Place p, int tokens) throws MissingPlaceException, NegativeException {
    	checkModifyTokens(p, tokens);
    	
    	p.addTokens(tokens);
   
    }
    
    /**
     * Adds an ArcTP (Transition to Place) to the PetriNet.
     *
     * @param w Weight of the arc.
     * @param p Place involved in the arc.
     * @param t Transition involved in the arc.
     * @return The created ArcTP object.
     */
    @Override
    public ArcTP addArcTP(int w, Place p, Transition t) throws NegativeException, MissingPlaceException, MissingTransitionException, DuplicateArcException {
        checkAddArcExceptions(w, p, t);
        checkDuplicateArcTP(p, t);
        
        ArcTP arc = new ArcTP(w, p, t);
        t.addArcTP(arc);
        return arc;
    }

    /**
     * Adds an ArcPT (Place to Transition) to the PetriNet.
     *
     * @param w Weight of the arc.
     * @param p Place involved in the arc.
     * @param t Transition involved in the arc.
     * @return The created ArcPT object.
     */
    @Override
    public ArcPT addArcPT(int w, Place p, Transition t) throws NegativeException, MissingPlaceException, MissingTransitionException, DuplicateArcException {
        checkAddArcExceptions(w, p, t);
        checkDuplicateArcPT(p, t);
        
        ArcPT arc = new ArcPT(w, p, t);
        t.addArcPT(arc);
        return arc;
    }

    // Other methods like addArcZero, addArcDrain, addTransition, removePlace, and more follow similar patterns
    // for adding, removing, and managing arcs and transitions in the PetriNet.
	
	/**
	 * Adds a new ArcZero to the PetriNet.
	 *
	 * @param w Weight of the arc.
	 * @param p Place involved in the arc.
	 * @param t Transition involved in the arc.
	 * @return The created ArcZero object.
	 */
	@Override
	public ArcZero addArcZero(int w, Place p, Transition t) throws NegativeException, MissingPlaceException, MissingTransitionException, DuplicateArcException {
	    checkAddArcExceptions(w, p, t);
	    checkDuplicateArcPT(p, t);
	    
	    ArcZero arc = new ArcZero(w, p, t);
	    t.addArcPT(arc);  // ArcZero is treated as an ArcPT
	    return arc;
	}
	
	/**
	 * Adds a new ArcDrain to the PetriNet.
	 *
	 * @param w Weight of the arc.
	 * @param p Place involved in the arc.
	 * @param t Transition involved in the arc.
	 * @return The created ArcDrain object.
	 */
	@Override
	public ArcDrain addArcDrain(int w, Place p, Transition t) throws NegativeException, MissingPlaceException, MissingTransitionException, DuplicateArcException {
	    checkAddArcExceptions(w, p, t);
	    checkDuplicateArcPT(p, t);
	    
	    ArcDrain arc = new ArcDrain(w, p, t);
	    t.addArcPT(arc);  // ArcDrain is treated as an ArcPT
	    return arc;
	}
	
	/**
	 * Adds a new Transition to the PetriNet.
	 *
	 * @return The created Transition object.
	 */
	@Override
	public Transition addTransition() {
	    Transition t = new Transition();
	    this.transitions.add(t);
	    return t;
	}
	
	/**
	 * Removes a Place from the PetriNet. If linked to any Arc, removes said Arc from the PetriNet.
	 *
	 * @param p The Place to remove.
	 * @throws MissingPlaceException If the Place is not found in the net
	 * @throws MissingArcException Remnant from removal of all Arcs linked to the Place. By construction it shouldn't be called.
	 */
	@Override
	public void removePlace(Place p) throws MissingPlaceException, MissingArcException {
	    if (p == null || !this.places.contains(p)) {
	        throw new MissingPlaceException("Place not in PetriNet");
	    }
	    
	    p.removeConnectedArcs(this);
	    this.places.remove(p);
	}
	
    /**
     * Removes tokens to a specified Place already existing in the PetriNet
     * 
     * @param p The Place to remove tokens to
     * @param tokens The number of tokens to remove
     * @throws MissingPlaceException If the specified Place is not in PetriNet
     * @throws NegativeException If the amount of tokens removed is negative or 
     * 		   				     if by removing said amount of token, the place has a negative amount of tokens 
     */
    public void removeTokens(Place p, int tokens) throws MissingPlaceException, NegativeException {
    	checkModifyTokens(p, tokens);
    	if (p.getTokens() - tokens < 0) {
    		throw new NegativeException("Place cannot have a negative amount of tokens");
    	}
    	
    	p.removeTokens(tokens);
    	
    }
		
	/**
	 * Removes an ArcTP from the PetriNet.
	 *
	 * @param a The ArcTP to remove.
	 * @throws MissingArcException If the ArcTP is not found in the net.
	 */
	@Override
	public void removeArcTP(ArcTP a) throws MissingArcException {
	    if (a == null || !getArcsTP().contains(a)) {
	        throw new MissingArcException("Arc not in PetriNet");
	    }
	    a.getTransition().removeArcTP(a);
	}
	
	/**
	 * Removes an ArcPT from the PetriNet.
	 *
	 * @param a The ArcPT to remove.
	 * @throws MissingArcException If the ArcPT is not found in the net.
	 */
	@Override
	public void removeArcPT(ArcPT a) throws MissingArcException {
	    if (a == null || !getArcsPT().contains(a)) {
	        throw new MissingArcException("Arc not in PetriNet");
	    }
	    a.getTransition().removeArcPT(a);
	}
	
	/**
	 * Removes an ArcZero from the PetriNet.
	 *
	 * @param a The ArcZero to remove.
	 * @throws MissingArcException If the ArcZero is not found in the net.
	 */
	@Override
	public void removeArcZero(ArcZero a) throws MissingArcException {
	    if (a == null || !getArcsZero().contains(a)) {
	        throw new MissingArcException("Arc not in PetriNet");
	    }
	    a.getTransition().removeArcPT(a);
	}
	
	/**
	 * Removes an ArcDrain from the PetriNet.
	 *
	 * @param a The ArcDrain to remove.
	 * @throws MissingArcException If the ArcDrain is not found in the net.
	 */
	@Override
	public void removeArcDrain(ArcDrain a) throws MissingArcException {
	    if (a == null || !getArcsDrain().contains(a)) {
	        throw new MissingArcException("Arc not in PetriNet");
	    }
	    a.getTransition().removeArcPT(a);
	}
	
	/**
	 * Removes a Transition from the PetriNet. If linked to any Arc, removes said Arc from the PetriNet.
	 *
	 * @param t The Transition to remove.
	 * @throws MissingTransitionException If the Transition is not found in the net.
	 */
	@Override
	public void removeTransition(Transition t) throws MissingTransitionException {
	    if (t == null || !this.transitions.contains(t)) {
	        throw new MissingTransitionException("Transition not in PetriNet");
	    }
	    
	    this.transitions.remove(t);
	}
	
	/**
	 *  Fires a Transition.
	 * @param t The Transition to fire.
	 * @throws NotFireableTransitionException Transition is not fireable
	 */
	public void fireTransition(Transition t) throws NotFireableTransitionException {
		if (!t.isFireable()) {
			throw new NotFireableTransitionException("Transition cannot be fired");
		}
		
		t.fire();
	}
	
	/**
	 * Returns a description of the PetriNet
	 */
	public String toString() {
		String description = "";
		description += "Content of PetriNet : \n";

		ArrayList<ArcPT> arcsPT = this.getArcsPT();
		ArrayList<ArcTP> arcsTP = this.getArcsTP();

		// Amount of each element of the PetriNet
		description += "  • " +  this.places.size() + " places\n"; 		
		description += "  • " +  this.transitions.size() + " transitions\n"; 
		description += "  • " + arcsPT.size() + " ArcPT\n";
		description += "  • " + arcsTP.size() + " ArcTP\n";
		
		String finalDescription = description + this.getDescriptionDetails(arcsTP, arcsPT);
		
		return finalDescription;
			
	}
	
	/**
	 * Shows content of the PetriNet
	 */
	public void show() {
		System.out.print(this.toString());	
	}
	
	/**
     * Checks common exceptions when adding an arc, such as negative weights or null references.
     *
     * @param w Weight of the arc.
     * @param p Place involved in the arc.
     * @param t Transition involved in the arc.
     * @throws NegativeException If the arc weight is negative.
     * @throws MissingPlaceException If the Place is null.
     * @throws MissingTransitionException If the Transition is null.
     */
    private void checkAddArcExceptions(int w, Place p, Transition t) throws NegativeException, MissingPlaceException, MissingTransitionException {
        if (w < 0) {
            throw new NegativeException("Arc weight can't be negative");
        }
        if (p == null) {
            throw new MissingPlaceException("Place can't be null");
        }
        if (t == null) {
            throw new MissingTransitionException("Transition can't be null");
        }
        if (!this.places.contains(p)) {
            throw new MissingPlaceException("Place not in PetriNet");
        }
        if (!this.transitions.contains(t)) {
            throw new MissingTransitionException("Transition not in PetriNet");
        }
    }

    /**
     * Checks if an ArcTP already exists between the given Place and Transition.
     *
     * @param p The Place involved in the Arc.
     * @param t The Transition involved in the Arc.
     * @throws DuplicateArcException If an identical ArcTP already exists.
     */
    private void checkDuplicateArcTP(Place p, Transition t) throws DuplicateArcException {
        ArrayList<ArcTP> arcs = t.getArcsTP();
        for (ArcTP arc : arcs) {
            if (arc.getPlace() == p) {
                throw new DuplicateArcException("An Arc already exists between this place and this transition");
            }
        }
    }

    /**
     * Checks if an ArcPT already exists between the given Place and Transition.
     *
     * @param p The Place involved in the Arc.
     * @param t The Transition involved in the Arc.
     * @throws DuplicateArcException If an identical ArcPT already exists.
     */
    private void checkDuplicateArcPT(Place p, Transition t) throws DuplicateArcException {
        ArrayList<ArcPT> arcs = t.getArcsPT();
        for (ArcPT arc : arcs) {
            if (arc.getPlace() == p) {
                throw new DuplicateArcException("An Arc already exists between this place and this transition");
            }
        }
    }

    /*
     * Checks for common exceptions when modifying a Place's tokens
     *
     * @param p The Place involved.
     * @param tokens The amount of tokens to be added / removed.
     * @throws MissingPlaceException If the Place is not part of the PetriNet.
     * @throws NegativeException If the amount of tokens to be added / removed is negative.
     */
    private void checkModifyTokens(Place p, int tokens) throws MissingPlaceException, NegativeException {
    	if (p == null) {
    		throw new MissingPlaceException("Place cannot be null");
    	}
    	
    	if (!this.places.contains(p)) {
    		throw new MissingPlaceException("Place not in PetriNet");
    	}
    	
    	if (tokens < 0) {
    		throw new NegativeException("Amount of tokens negative");
    	}
    }
    
    /*
     * Returns the details of elements of the PetriNet
     * 
     * @params arcsTP Array of all ArcTP of this PetriNet
     * @params arcsPT Array of all ArcPT of this PetriNet
     */
    private String getDescriptionDetails(ArrayList<ArcTP> arcsTP, ArrayList<ArcPT> arcsPT) {
    	String description = "";
    	
    	// Details for places
		if (this.places.size() > 0)	 {
			description += "Places : \n";
			for (int i =0 ; i < this.places.size(); i++) {
				description += "  • " +  i + " : place with " + this.places.get(i).getTokens() + " tokens\n";
			}
		}
		// Details for transitions
		if (this.transitions.size() > 0)	 {
			description += "Transitions : \n";
			for (int i =0 ; i < this.transitions.size(); i++) {
				description += "  • " +  i + " : transition with " + this.transitions.get(i).getArcsPT().size() + " entering arcs and "+ this.transitions.get(i).getArcsTP().size() + " exiting arcs\n";
			}
		}	
		// Details for ArcTP
		if (arcsTP.size() > 0) {
			description += "ArcTP : \n";
			for (int i =0 ; i < arcsTP.size(); i++) {
				description += "  • " + i + " : ArcTP with a weight of " + arcsTP.get(i).getWeight() + " and connected place with "+ arcsTP.get(i).getPlace().getTokens() + " tokens\n";
			}
		}
		// Details for ArcPT
		if (arcsPT.size() > 0) {
			description += "ArcPT : \n";
			for (int i =0 ; i < arcsPT.size(); i++) {
				description += "  • " + i + " : "+ arcsPT.get(i).getClass().getSimpleName() +" with a weight of " + arcsPT.get(i).getWeight() + " and connected place with "+ arcsPT.get(i).getPlace().getTokens() + " tokens\n";
			}
		}
		
		return description;
    }

}