package org.pneditor.petrinet.adapters.antonino;

import java.util.HashMap;

import org.pneditor.petrinet.AbstractArc;
import org.pneditor.petrinet.AbstractNode;
import org.pneditor.petrinet.AbstractPlace;
import org.pneditor.petrinet.AbstractTransition;
import org.pneditor.petrinet.PetriNetInterface;
import org.pneditor.petrinet.ResetArcMultiplicityException;
import org.pneditor.petrinet.UnimplementedCaseException;
import org.pneditor.petrinet.models.antonino.Arc;
import org.pneditor.petrinet.models.antonino.ArcPT;
import org.pneditor.petrinet.models.antonino.ArcTP;
import org.pneditor.petrinet.models.antonino.DuplicateArcException;
import org.pneditor.petrinet.models.antonino.MissingArcException;
import org.pneditor.petrinet.models.antonino.MissingPlaceException;
import org.pneditor.petrinet.models.antonino.MissingTransitionException;
import org.pneditor.petrinet.models.antonino.NegativeException;
import org.pneditor.petrinet.models.antonino.NotFireableTransitionException;
import org.pneditor.petrinet.models.antonino.PetriNet;
import org.pneditor.petrinet.models.antonino.Place;
import org.pneditor.petrinet.models.antonino.Transition;

import logger.PNEditorLogger;

public class PetriNetAdapter extends PetriNetInterface {

	private PetriNet petriNet;
	
	private HashMap<Transition, TransitionAdapter> transitionToAdapterMap;
	private HashMap<Place, PlaceAdapter> placeToAdapterMap;
	private HashMap<Arc, ArcAdapter> arcToAdapterMap;
	
	
	public PetriNetAdapter() {
		petriNet = new PetriNet();
		
		transitionToAdapterMap = new HashMap<Transition, TransitionAdapter>();
		placeToAdapterMap = new HashMap<Place, PlaceAdapter>();
		arcToAdapterMap = new HashMap<Arc, ArcAdapter>();

	}
	
	
	@Override
	public AbstractPlace addPlace() {
		try {
			Place placeAdaptee = petriNet.addPlace(0);
			PlaceAdapter place = new PlaceAdapter("", placeAdaptee, petriNet);
			placeToAdapterMap.put(placeAdaptee, place);
			
			return place;
		} catch (NegativeException e) {
			PNEditorLogger.severeLogs(e.getMessage());
		}
		return null;
	}

	@Override
	public AbstractTransition addTransition() {
		Transition transitionAdaptee = this.petriNet.addTransition();
		TransitionAdapter transition = new TransitionAdapter("", transitionAdaptee);
		
		transitionToAdapterMap.put(transitionAdaptee, transition);
		
		return transition;
	}

	@Override
	public AbstractArc addRegularArc(AbstractNode source, AbstractNode destination) throws UnimplementedCaseException {
		try {
			
			if (source instanceof AbstractPlace && destination instanceof AbstractTransition) {
				PlaceAdapter placeAdapter = (PlaceAdapter) source;
				TransitionAdapter transitionAdapter = (TransitionAdapter) destination;

				Place p = placeAdapter.getPlace();
				Transition t = transitionAdapter.getTransition();
				
				Arc arcAdaptee = (Arc) petriNet.addArcPT(0, p, t);
				ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
				
				arcToAdapterMap.put(arcAdaptee, arc);
				
				return arc;
					
			}
			
			if (source instanceof AbstractTransition && destination instanceof AbstractPlace) {
				PlaceAdapter placeAdapter = (PlaceAdapter) destination;
				TransitionAdapter transitionAdapter = (TransitionAdapter) source;

				Place p = placeAdapter.getPlace();
				Transition t = transitionAdapter.getTransition();
				
				Arc arcAdaptee = (Arc) petriNet.addArcTP(0, p, t);
				ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
				
				arcToAdapterMap.put(arcAdaptee, arc);
				
				return arc;
			}
			
			throw new UnimplementedCaseException("Arc must link a place to a transition");
		
		} catch (NegativeException  |
				MissingPlaceException | 
				MissingTransitionException |
				DuplicateArcException e) {
			// Should never happen
			e.printStackTrace();
		}
	
		throw new UnimplementedCaseException("");

	}

	@Override
	public AbstractArc addInhibitoryArc(AbstractPlace place, AbstractTransition transition)
			throws UnimplementedCaseException {
		
		try {
			PlaceAdapter placeAdapter = (PlaceAdapter) place;
			TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
			
			Place p = placeAdapter.getPlace();
			Transition t = transitionAdapter.getTransition();
			
			Arc arcAdaptee = (Arc) petriNet.addArcDrain(0, p, t);
			ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
			
			arcToAdapterMap.put(arcAdaptee, arc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnimplementedCaseException("");
	}

	@Override
	public AbstractArc addResetArc(AbstractPlace place, AbstractTransition transition)
			throws UnimplementedCaseException {
		try {
			PlaceAdapter placeAdapter = (PlaceAdapter) place;
			TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
			
			Place p = placeAdapter.getPlace();
			Transition t = transitionAdapter.getTransition();
			
			Arc arcAdaptee = (Arc) petriNet.addArcZero(0, p, t);
			ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
			
			arcToAdapterMap.put(arcAdaptee, arc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnimplementedCaseException("");
	}

	@Override
	public void removePlace(AbstractPlace place) {
		PlaceAdapter placeAdapter = (PlaceAdapter) place;
		Place placeAdaptee = placeAdapter.getPlace();
		try {
			petriNet.removePlace(placeAdaptee);
			
			placeToAdapterMap.remove(placeAdaptee);
			
		} catch (MissingPlaceException | MissingArcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void removeTransition(AbstractTransition transition) {
		TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
		Transition transitionAdaptee = transitionAdapter.getTransition(); 
		try {
			petriNet.removeTransition(transitionAdaptee);
			
			transitionToAdapterMap.remove(transitionAdaptee);
			
		} catch (MissingTransitionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeArc(AbstractArc arc) {
		ArcAdapter arcAdapter = (ArcAdapter) arc;
		Arc arcAdaptee = arcAdapter.getArc(); 
		try {
			
			if (arcAdaptee instanceof ArcTP) {
	 			petriNet.removeArcTP((ArcTP) arcAdaptee);
			}
			
			if (arcAdaptee instanceof ArcPT) {
	 			petriNet.removeArcPT((ArcPT) arcAdaptee);
			}
			
			arcToAdapterMap.remove(arcAdaptee);
			
		} catch (MissingArcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isEnabled(AbstractTransition transition) throws ResetArcMultiplicityException {
		TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
		Transition transitionAdaptee = transitionAdapter.getTransition(); 
		return transitionAdaptee.isFireable();
	}

	@Override
	public void fire(AbstractTransition transition) throws ResetArcMultiplicityException {
		TransitionAdapter transitionAdapter = (TransitionAdapter) transition;
		Transition transitionAdaptee = transitionAdapter.getTransition(); 
		try {
			petriNet.fireTransition(transitionAdaptee);
		} catch (NotFireableTransitionException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Transition, TransitionAdapter> getTransitionToAdapterMap() {
		return transitionToAdapterMap;
	}

	public HashMap<Place, PlaceAdapter> getPlaceToAdapterMap() {
		return placeToAdapterMap;
	}
	
	public PetriNet getPetriNet() {
		return petriNet;
	}

	public HashMap<Arc, ArcAdapter> getArcToAdapterMap() {
		return arcToAdapterMap;
	}

}
