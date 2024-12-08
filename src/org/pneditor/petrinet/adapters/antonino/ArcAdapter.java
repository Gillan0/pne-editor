package org.pneditor.petrinet.adapters.antonino;

import java.util.HashMap;

import org.pneditor.petrinet.AbstractArc;
import org.pneditor.petrinet.AbstractNode;
import org.pneditor.petrinet.ResetArcMultiplicityException;
import org.pneditor.petrinet.models.antonino.*;

/**
 * Adapter class that bridges {@link AbstractArc} with the {@link Arc} model.
 * It allows various types of {@link Arc} instances to be used as {@link AbstractArc}.
 */
public class ArcAdapter extends AbstractArc {

    /**
     * The wrapped {@link Arc} instance.
     */
    private Arc arc;

    /**
     * The associated {@link PetriNetAdapter} instance.
     */
    private PetriNetAdapter petriNetAdapter;

    /**
     * Constructs an {@code ArcAdapter} with the given {@link Arc} and {@link PetriNetAdapter}.
     *
     * @param arc the {@link Arc} instance to wrap
     * @param petriNetAdapter the associated {@link PetriNetAdapter}
     */
    public ArcAdapter(Arc arc, PetriNetAdapter petriNetAdapter) {
        this.arc = arc;
        this.petriNetAdapter = petriNetAdapter;
    }

    /**
     * Retrieves the wrapped {@link Arc} instance.
     *
     * @return the wrapped {@link Arc}
     */
    public Arc getArc() {
        return arc;
    }
    
    /**
     * Retrieves the destination node of the arc.
     *
     * @return the destination {@link AbstractNode}
     */
    @Override
    public AbstractNode getDestination() {
        if (arc instanceof ArcTP) {
            Place p = arc.getPlace();
            HashMap<Place, PlaceAdapter> placeMap = petriNetAdapter.getPlaceToAdapterMap();
            return placeMap.get(p);
        }
        if (arc instanceof ArcPT) {
            Transition t = arc.getTransition();
            HashMap<Transition, TransitionAdapter> transitionMap = petriNetAdapter.getTransitionToAdapterMap();
            return transitionMap.get(t);
        }
        return null;
    }

    /**
     * Retrieves the multiplicity of the arc.
     *
     * @return the multiplicity of the arc
     * @throws ResetArcMultiplicityException if the arc is a reset arc
     */
    @Override
    public int getMultiplicity() throws ResetArcMultiplicityException {
        return arc.getWeight();
    }

    /**
     * Retrieves the source node of the arc.
     *
     * @return the source {@link AbstractNode}
     */
    @Override
    public AbstractNode getSource() {
        if (arc instanceof ArcTP) {
            Transition t = arc.getTransition();
            HashMap<Transition, TransitionAdapter> transitionMap = petriNetAdapter.getTransitionToAdapterMap();
            return transitionMap.get(t);
        }
        if (arc instanceof ArcPT) {
            Place p = arc.getPlace();
            HashMap<Place, PlaceAdapter> placeMap = petriNetAdapter.getPlaceToAdapterMap();
            return placeMap.get(p);
        }
        return null;
    }

    /**
     * Sets the multiplicity of the arc.
     *
     * @param multiplicity the new multiplicity of the arc
     * @throws ResetArcMultiplicityException if the arc is a reset arc
     */
    @Override
    public void setMultiplicity(int multiplicity) throws ResetArcMultiplicityException {
        PetriNet petriNet = petriNetAdapter.getPetriNet();

        try {
            if (arc instanceof ArcTP) {
                Place place = arc.getPlace();
                Transition transition = arc.getTransition();
                petriNet.removeArcTP((ArcTP) arc);
                arc = petriNet.addArcTP(multiplicity, place, transition);
            }

            if (arc instanceof ArcPT) {
                Place place = arc.getPlace();
                Transition transition = arc.getTransition();
                petriNet.removeArcPT((ArcPT) arc);
                arc = petriNet.addArcPT(multiplicity, place, transition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines if the arc is a reset arc.
     *
     * @return {@code true} if the arc is a reset arc, {@code false} otherwise
     */
    @Override
    public boolean isReset() {
        return arc instanceof ArcZero;
    }

    /**
     * Determines if the arc is a regular arc.
     *
     * @return {@code true} if the arc is regular, {@code false} otherwise
     */
    @Override
    public boolean isRegular() {
        return !isReset() && !isInhibitory();
    }

    /**
     * Determines if the arc is an inhibitory arc.
     *
     * @return {@code true} if the arc is inhibitory, {@code false} otherwise
     */
    @Override
    public boolean isInhibitory() {
        return arc instanceof ArcDrain;
    }
}