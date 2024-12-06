package org.pneditor.petrinet.adapters.antonino;

import org.pneditor.petrinet.AbstractPlace;
import org.pneditor.petrinet.models.antonino.MissingPlaceException;
import org.pneditor.petrinet.models.antonino.NegativeException;
import org.pneditor.petrinet.models.antonino.PetriNet;
import org.pneditor.petrinet.models.antonino.Place;

/**
 * Adapter class that bridges {@link AbstractPlace} with the {@link Place} model.
 * It allows a {@link Place} to be used as an {@link AbstractPlace}.
 */
public class PlaceAdapter extends AbstractPlace {
    
    /**
     * The wrapped {@link Place} instance.
     */
    private Place place;
    
    /**
     * The associated {@link PetriNet} instance.
     */
    private PetriNet petriNet;
    
    /**
     * Constructs a {@code PlaceAdapter} with the given label, {@link Place}, and {@link PetriNet}.
     *
     * @param label the label of the place
     * @param p the {@link Place} instance to wrap
     * @param petriNet the {@link PetriNet} instance associated with this place
     */
    public PlaceAdapter(String label, Place p, PetriNet petriNet) {
        super(label);
        this.place = p;
        this.petriNet = petriNet;
    }

    /**
     * Adds a single token to the place.
     * Delegates to the {@link PetriNet#addTokens(Place, int)} method.
     */
    @Override
    public void addToken() {
        try {
            petriNet.addTokens(place, 1);
        } catch (MissingPlaceException | NegativeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a single token from the place.
     * Delegates to the {@link PetriNet#removeTokens(Place, int)} method.
     */
    @Override
    public void removeToken() {
        try {
            petriNet.removeTokens(place, 1);
        } catch (MissingPlaceException | NegativeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the number of tokens in the place.
     *
     * @return the number of tokens in the place
     */
    @Override
    public int getTokens() {
        return place.getTokens();
    }

    /**
     * Sets the number of tokens in the place.
     *
     * @param tokens the number of tokens to set in the place
     */
    @Override
    public void setTokens(int tokens) {
        place.setTokens(tokens);
    }

    /**
     * Retrieves the wrapped {@link Place} instance.
     *
     * @return the wrapped {@link Place}
     */
    public Place getPlace() {
        return place;
    }
}
