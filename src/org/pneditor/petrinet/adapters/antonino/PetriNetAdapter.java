package org.pneditor.petrinet.adapters.antonino;

import java.util.HashMap;

import org.pneditor.petrinet.*;
import org.pneditor.petrinet.models.antonino.*;

import logger.PNEditorLogger;

/**
 * Adapter class that bridges the {@link PetriNetInterface} with the {@link PetriNet} model.
 * It manages mappings between the PNEditor framework's abstractions and the internal Petri net implementation.
 */
public class PetriNetAdapter extends PetriNetInterface {

    /**
     * The internal {@link PetriNet} instance being adapted.
     */
    private PetriNet petriNet;

    /**
     * Maps {@link Transition} instances to their corresponding {@link TransitionAdapter}.
     */
    private HashMap<Transition, TransitionAdapter> transitionToAdapterMap;

    /**
     * Maps {@link Place} instances to their corresponding {@link PlaceAdapter}.
     */
    private HashMap<Place, PlaceAdapter> placeToAdapterMap;

    /**
     * Maps {@link Arc} instances to their corresponding {@link ArcAdapter}.
     */
    private HashMap<Arc, ArcAdapter> arcToAdapterMap;

    /**
     * Constructs a new {@code PetriNetAdapter} and initializes the internal data structures.
     */
    public PetriNetAdapter() {
        petriNet = new PetriNet();
        transitionToAdapterMap = new HashMap<>();
        placeToAdapterMap = new HashMap<>();
        arcToAdapterMap = new HashMap<>();
    }
	
	
    /**
     * Adds a new place to the Petri net with an initial token count of 0.
     * The place is wrapped in a {@link PlaceAdapter} to bridge the internal model with the PNEditor framework.
     *
     * @return the {@link AbstractPlace} adapter representing the newly added place, 
     *         or {@code null} if the operation fails due to an exception.
     */
    @Override
    public AbstractPlace addPlace() {
        try {
            // Create a new Place with an initial token count of 0
            Place placeAdaptee = petriNet.addPlace(0);
            
            // Wrap the Place in a PlaceAdapter
            PlaceAdapter place = new PlaceAdapter("", placeAdaptee, petriNet);
            
            // Store the mapping from the internal Place to its adapter
            placeToAdapterMap.put(placeAdaptee, place);
            
            return place;
        } catch (NegativeException e) {
            // Log any exception related to invalid token count
            PNEditorLogger.severeLogs(e.getMessage());
        }
        return null; // Return null if the operation fails
    }


    /**
     * Adds a new transition to the Petri net.
     * The transition is wrapped in a {@link TransitionAdapter} to bridge the internal model with the PNEditor framework.
     *
     * @return the {@link AbstractTransition} adapter representing the newly added transition.
     */
    @Override
    public AbstractTransition addTransition() {
        // Create a new Transition in the internal PetriNet model
        Transition transitionAdaptee = this.petriNet.addTransition();
        
        // Wrap the Transition in a TransitionAdapter
        TransitionAdapter transition = new TransitionAdapter("", transitionAdaptee);
        
        // Store the mapping from the internal Transition to its adapter
        transitionToAdapterMap.put(transitionAdaptee, transition);
        
        return transition;
    }


    /**
     * Adds a regular arc to the Petri net, linking a source node to a destination node.
     * This method supports arcs between a place and a transition in either direction.
     *
     * @param source      the source {@link AbstractNode} of the arc, which can be a {@link AbstractPlace} or {@link AbstractTransition}.
     * @param destination the destination {@link AbstractNode} of the arc, which can be a {@link AbstractTransition} or {@link AbstractPlace}.
     * @return the {@link AbstractArc} adapter representing the newly added arc.
     * @throws UnimplementedCaseException if the arc does not link a place to a transition or if an unexpected error occurs.
     */
    @Override
    public AbstractArc addRegularArc(AbstractNode source, AbstractNode destination) throws UnimplementedCaseException {
        try {
            // Check if the arc goes from a place to a transition
            if (source instanceof AbstractPlace && destination instanceof AbstractTransition) {
                PlaceAdapter placeAdapter = (PlaceAdapter) source;
                TransitionAdapter transitionAdapter = (TransitionAdapter) destination;

                Place p = placeAdapter.getPlace();
                Transition t = transitionAdapter.getTransition();

                // Add the arc to the internal PetriNet model
                Arc arcAdaptee = (Arc) petriNet.addArcPT(0, p, t);
                ArcAdapter arc = new ArcAdapter(arcAdaptee, this);

                // Store the mapping and return the adapter
                arcToAdapterMap.put(arcAdaptee, arc);
                return arc;
            }

            // Check if the arc goes from a transition to a place
            if (source instanceof AbstractTransition && destination instanceof AbstractPlace) {
                PlaceAdapter placeAdapter = (PlaceAdapter) destination;
                TransitionAdapter transitionAdapter = (TransitionAdapter) source;

                Place p = placeAdapter.getPlace();
                Transition t = transitionAdapter.getTransition();

                // Add the arc to the internal PetriNet model
                Arc arcAdaptee = (Arc) petriNet.addArcTP(0, p, t);
                ArcAdapter arc = new ArcAdapter(arcAdaptee, this);

                // Store the mapping and return the adapter
                arcToAdapterMap.put(arcAdaptee, arc);
                return arc;
            }

            // Throw an exception if the arc does not link a place to a transition
            throw new UnimplementedCaseException("Arc must link a place to a transition");
        } catch (NegativeException |
                 MissingPlaceException |
                 MissingTransitionException |
                 DuplicateArcException e) {
            // Log unexpected exceptions (should never happen)
            e.printStackTrace();
        }

        // Throw an exception for any other unexpected failure
        throw new UnimplementedCaseException("");
    }


    /**
     * Adds an inhibitory arc between a place and a transition in the Petri net.
     * Inhibitory arcs are used to prevent a transition from firing if there are tokens in the place.
     *
     * @param place      the {@link AbstractPlace} where the inhibitory arc originates.
     * @param transition the {@link AbstractTransition} where the inhibitory arc points to.
     * @return the {@link AbstractArc} adapter representing the newly added inhibitory arc.
     * @throws UnimplementedCaseException if the arc cannot be created or linked properly.
     */
    @Override
    public AbstractArc addInhibitoryArc(AbstractPlace place, AbstractTransition transition) throws UnimplementedCaseException {
        try {
            // Cast place and transition to their respective adapters
            PlaceAdapter placeAdapter = (PlaceAdapter) place;
            TransitionAdapter transitionAdapter = (TransitionAdapter) transition;

            // Get the corresponding Place and Transition objects
            Place p = placeAdapter.getPlace();
            Transition t = transitionAdapter.getTransition();

            // Add the inhibitory arc to the internal PetriNet model
            Arc arcAdaptee = (Arc) petriNet.addArcDrain(0, p, t);
            ArcAdapter arc = new ArcAdapter(arcAdaptee, this);

            // Store the mapping and return the adapter
            arcToAdapterMap.put(arcAdaptee, arc);

        } catch (Exception e) {
            // Log any exception encountered during the process
            e.printStackTrace();
        }

        // Throw an exception if the arc cannot be created
        throw new UnimplementedCaseException("");
    }


    /**
     * Adds a reset arc between a place and a transition in the Petri net.
     * Reset arcs are used to reset the tokens in a place when the associated transition fires.
     *
     * @param place      the {@link AbstractPlace} where the reset arc originates.
     * @param transition the {@link AbstractTransition} where the reset arc points to.
     * @return the {@link AbstractArc} adapter representing the newly added reset arc.
     * @throws UnimplementedCaseException if the arc cannot be created or linked properly.
     */
    @Override
    public AbstractArc addResetArc(AbstractPlace place, AbstractTransition transition) throws UnimplementedCaseException {
        try {
            // Cast place and transition to their respective adapters
            PlaceAdapter placeAdapter = (PlaceAdapter) place;
            TransitionAdapter transitionAdapter = (TransitionAdapter) transition;

            // Get the corresponding Place and Transition objects
            Place p = placeAdapter.getPlace();
            Transition t = transitionAdapter.getTransition();

            // Add the reset arc to the internal PetriNet model
            Arc arcAdaptee = (Arc) petriNet.addArcZero(0, p, t);
            ArcAdapter arc = new ArcAdapter(arcAdaptee, this);

            // Store the mapping and return the adapter
            arcToAdapterMap.put(arcAdaptee, arc);

        } catch (Exception e) {
            // Log any exception encountered during the process
            e.printStackTrace();
        }

        // Throw an exception if the arc cannot be created
        throw new UnimplementedCaseException("");
    }


    /**
     * Removes a place from the Petri net and updates the associated adapter mappings.
     * This method removes the place both from the internal PetriNet model and from the adapter map.
     *
     * @param place the {@link AbstractPlace} to be removed from the Petri net.
     * @throws MissingPlaceException if the specified place does not exist in the Petri net.
     * @throws MissingArcException if there are any missing arcs associated with the place during removal.
     */
    @Override
    public void removePlace(AbstractPlace place) {
        // Cast the AbstractPlace to a PlaceAdapter to access the adaptee
        PlaceAdapter placeAdapter = (PlaceAdapter) place;
        Place placeAdaptee = placeAdapter.getPlace();
        try {
            // Remove the place from the PetriNet
            petriNet.removePlace(placeAdaptee);

            // Remove the place from the adapter map
            placeToAdapterMap.remove(placeAdaptee);

        } catch (MissingPlaceException | MissingArcException e) {
            // Log any exceptions encountered during the removal process
            e.printStackTrace();
        }
    }


    /**
     * Removes a transition from the Petri net and updates the associated adapter mappings.
     * This method removes the transition both from the internal PetriNet model and from the adapter map.
     *
     * @param transition the {@link AbstractTransition} to be removed from the Petri net.
     * @throws MissingTransitionException if the specified transition does not exist in the Petri net.
     */
    @Override
    public void removeTransition(AbstractTransition transition) {
        // Cast the AbstractTransition to a TransitionAdapter to access the adaptee
        TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
        Transition transitionAdaptee = transitionAdapter.getTransition(); 
        try {
            // Remove the transition from the PetriNet
            petriNet.removeTransition(transitionAdaptee);

            // Remove the transition from the adapter map
            transitionToAdapterMap.remove(transitionAdaptee);

        } catch (MissingTransitionException e) {
            // Log any exceptions encountered during the removal process
            e.printStackTrace();
        }
    }


    /**
     * Removes an arc from the Petri net and updates the associated adapter mappings.
     * This method removes the arc from the internal PetriNet model based on its type (ArcTP or ArcPT)
     * and from the adapter map.
     *
     * @param arc the {@link AbstractArc} to be removed from the Petri net.
     * @throws MissingArcException if the specified arc does not exist in the Petri net.
     */
    @Override
    public void removeArc(AbstractArc arc) {
        // Cast the AbstractArc to an ArcAdapter to access the adaptee
        ArcAdapter arcAdapter = (ArcAdapter) arc;
        Arc arcAdaptee = arcAdapter.getArc(); 
        try {
            // Check the type of arc and remove it accordingly from the PetriNet
            if (arcAdaptee instanceof ArcTP) {
                petriNet.removeArcTP((ArcTP) arcAdaptee);
            }

            if (arcAdaptee instanceof ArcPT) {
                petriNet.removeArcPT((ArcPT) arcAdaptee);
            }

            // Remove the arc from the adapter map
            arcToAdapterMap.remove(arcAdaptee);

        } catch (MissingArcException e) {
            // Log any exceptions encountered during the removal process
            e.printStackTrace();
        }
    }


    /**
     * Checks if the specified transition is enabled (i.e., fireable) in the Petri net.
     * This method determines whether the transition can be fired based on its current state in the Petri net.
     * A transition is considered enabled if it has the necessary tokens in its input places.
     *
     * @param transition the {@link AbstractTransition} to be checked for enablement.
     * @return true if the transition is fireable (enabled), false otherwise.
     * @throws ResetArcMultiplicityException if an error occurs while checking the multiplicity of reset arcs.
     */
    @Override
    public boolean isEnabled(AbstractTransition transition) throws ResetArcMultiplicityException {
        // Cast the AbstractTransition to a TransitionAdapter to access the adaptee
        TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
        Transition transitionAdaptee = transitionAdapter.getTransition();
        
        // Check if the transition is fireable (enabled)
        return transitionAdaptee.isFireable();
    }


    /**
     * Fires the specified transition in the Petri net, causing it to execute its action and update the net's state.
     * This method will only fire the transition if it is enabled (fireable). If the transition is not fireable,
     * it will throw a {@link NotFireableTransitionException}.
     *
     * @param transition the {@link AbstractTransition} to be fired.
     * @throws ResetArcMultiplicityException if an error occurs while checking the multiplicity of reset arcs.
     */
    @Override
    public void fire(AbstractTransition transition) throws ResetArcMultiplicityException {
        // Cast the AbstractTransition to a TransitionAdapter to access the adaptee
        TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
        Transition transitionAdaptee = transitionAdapter.getTransition();
        
        try {
            // Fire the transition in the Petri net
            petriNet.fireTransition(transitionAdaptee);
        } catch (NotFireableTransitionException e) {
            // Handle the case where the transition is not fireable
            e.printStackTrace();
        }
    }


    /**
     * Retrieves the map of transitions to their corresponding adapters.
     * This map is used to associate each transition in the Petri net with its adapter.
     *
     * @return a {@link HashMap} that maps each {@link Transition} to its {@link TransitionAdapter}.
     */
    public HashMap<Transition, TransitionAdapter> getTransitionToAdapterMap() {
        return transitionToAdapterMap;
    }

    /**
     * Retrieves the map of places to their corresponding adapters.
     * This map is used to associate each place in the Petri net with its adapter.
     *
     * @return a {@link HashMap} that maps each {@link Place} to its {@link PlaceAdapter}.
     */
    public HashMap<Place, PlaceAdapter> getPlaceToAdapterMap() {
        return placeToAdapterMap;
    }

    /**
     * Retrieves the underlying Petri net object being adapted.
     * This method provides access to the core functionality of the Petri net.
     *
     * @return the {@link PetriNet} object being adapted.
     */
    public PetriNet getPetriNet() {
        return petriNet;
    }

    /**
     * Retrieves the map of arcs to their corresponding adapters.
     * This map is used to associate each arc in the Petri net with its adapter.
     *
     * @return a {@link HashMap} that maps each {@link Arc} to its {@link ArcAdapter}.
     */
    public HashMap<Arc, ArcAdapter> getArcToAdapterMap() {
        return arcToAdapterMap;
    }


}
