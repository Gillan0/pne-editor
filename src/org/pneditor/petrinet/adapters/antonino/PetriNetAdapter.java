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
	
	private HashMap<TransitionAdapter, Transition> adapterToTransitionMap;
	private HashMap<Transition, TransitionAdapter> transitionToAdapterMap;
	
	private HashMap<PlaceAdapter, Place> adapterToPlaceMap;
	private HashMap<Place, PlaceAdapter> placeToAdapterMap;
	
	private HashMap<ArcAdapter, Arc> adapterToArcMap;
	private HashMap<Arc, ArcAdapter> arcToAdapterMap;
	
	
	public PetriNetAdapter() {
		petriNet = new PetriNet();
		
		adapterToTransitionMap = new HashMap<TransitionAdapter, Transition>();
		adapterToPlaceMap = new HashMap<PlaceAdapter, Place>();
		adapterToArcMap = new HashMap<ArcAdapter, Arc>();
		
		transitionToAdapterMap = new HashMap<Transition, TransitionAdapter>();
		placeToAdapterMap = new HashMap<Place, PlaceAdapter>();
		arcToAdapterMap = new HashMap<Arc, ArcAdapter>();

		
	}
	
	
	@Override
	public AbstractPlace addPlace() {
		try {
			Place placeAdaptee = petriNet.addPlace(0);
			PlaceAdapter place = new PlaceAdapter("", placeAdaptee, petriNet);
			adapterToPlaceMap.put(place, placeAdaptee);
			placeToAdapterMap.put(placeAdaptee, place);
			
			return place;
		} catch (NegativeException e) {
			PNEditorLogger.severeLogs(e.getMessage());
		}
		return null;
	}

	@Override
	public AbstractTransition addTransition() {
		TransitionAdapter transition = new TransitionAdapter("");
		Transition transitionAdaptee = this.petriNet.addTransition();
		
		adapterToTransitionMap.put(transition, transitionAdaptee);
		transitionToAdapterMap.put(transitionAdaptee, transition);
		
		return transition;
	}

	@Override
	public AbstractArc addRegularArc(AbstractNode source, AbstractNode destination) throws UnimplementedCaseException {
		try {
			
			if (source instanceof AbstractPlace && destination instanceof AbstractTransition) {
				Place p = adapterToPlaceMap.get((AbstractPlace) source);
				Transition t = adapterToTransitionMap.get((AbstractTransition) destination);
				
				Arc arcAdaptee = (Arc) petriNet.addArcPT(0, p, t);
				ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
				
				arcToAdapterMap.put(arcAdaptee, arc);
				adapterToArcMap.put(arc, arcAdaptee);
				
				return arc;
					
			}
			
			if (source instanceof AbstractTransition && destination instanceof AbstractPlace) {
				Place p = adapterToPlaceMap.get((AbstractPlace) destination);
				Transition t = adapterToTransitionMap.get((AbstractTransition) source);
				
				Arc arcAdaptee = (Arc) petriNet.addArcTP(0, p, t);
				ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
				
				arcToAdapterMap.put(arcAdaptee, arc);
				adapterToArcMap.put(arc, arcAdaptee);
				
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
			Place p = adapterToPlaceMap.get((AbstractPlace) place);
			Transition t = adapterToTransitionMap.get((AbstractTransition) transition);
			
			Arc arcAdaptee = (Arc) petriNet.addArcDrain(0, p, t);
			ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
			
			arcToAdapterMap.put(arcAdaptee, arc);
			adapterToArcMap.put(arc, arcAdaptee);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnimplementedCaseException("");
	}

	@Override
	public AbstractArc addResetArc(AbstractPlace place, AbstractTransition transition)
			throws UnimplementedCaseException {
		try {
			Place p = adapterToPlaceMap.get((AbstractPlace) place);
			Transition t = adapterToTransitionMap.get((AbstractTransition) transition);
			
			Arc arcAdaptee = (Arc) petriNet.addArcZero(0, p, t);
			ArcAdapter arc = new ArcAdapter(arcAdaptee, this);
			
			arcToAdapterMap.put(arcAdaptee, arc);
			adapterToArcMap.put(arc, arcAdaptee);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnimplementedCaseException("");
	}

	@Override
	public void removePlace(AbstractPlace place) {
		Place placeAdaptee = adapterToPlaceMap.get(place);
		try {
			petriNet.removePlace(placeAdaptee);
			
			adapterToPlaceMap.remove(place);
			placeToAdapterMap.remove(placeAdaptee);
			
		} catch (MissingPlaceException | MissingArcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void removeTransition(AbstractTransition transition) {
		Transition transitionAdaptee = adapterToTransitionMap.get(transition); 
		try {
			petriNet.removeTransition(transitionAdaptee);
			
			adapterToTransitionMap.remove(transition);
			transitionToAdapterMap.remove(transitionAdaptee);
			
		} catch (MissingTransitionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeArc(AbstractArc arc) {
		Arc arcAdaptee = adapterToArcMap.get(arc); 
		try {
			
			if (arcAdaptee instanceof ArcTP) {
	 			petriNet.removeArcTP((ArcTP) arcAdaptee);
			}
			
			if (arcAdaptee instanceof ArcPT) {
	 			petriNet.removeArcPT((ArcPT) arcAdaptee);
			}
			
			adapterToArcMap.remove(arc);
			arcToAdapterMap.remove(arcAdaptee);
			
		} catch (MissingArcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isEnabled(AbstractTransition transition) throws ResetArcMultiplicityException {
		Transition transitionAdaptee = adapterToTransitionMap.get(transition); 
		return transitionAdaptee.isFireable();
	}

	@Override
	public void fire(AbstractTransition transition) throws ResetArcMultiplicityException {
		Transition transitionAdaptee = adapterToTransitionMap.get(transition); 
		try {
			petriNet.fireTransition(transitionAdaptee);
		} catch (NotFireableTransitionException e) {
			e.printStackTrace();
		}
	}

	public HashMap<TransitionAdapter, Transition> getAdapterToTransitionMap() {
		return adapterToTransitionMap;
	}
	
	public HashMap<Transition, TransitionAdapter> getTransitionToAdapterMap() {
		return transitionToAdapterMap;
	}

	public HashMap<PlaceAdapter, Place> getAdapterToPlaceMap() {
		return adapterToPlaceMap;
	}

	public HashMap<Place, PlaceAdapter> getPlaceToAdapterMap() {
		return placeToAdapterMap;
	}
	
	public PetriNet getPetriNet() {
		return petriNet;
	}

	public HashMap<ArcAdapter, Arc> getAdapterToArcMap() {
		return adapterToArcMap;
	}

	public HashMap<Arc, ArcAdapter> getArcToAdapterMap() {
		return arcToAdapterMap;
	}

}
