package org.pneditor.petrinet.adapters.antonino;

import java.util.HashMap;

import org.pneditor.petrinet.AbstractArc;
import org.pneditor.petrinet.AbstractNode;
import org.pneditor.petrinet.ResetArcMultiplicityException;
import org.pneditor.petrinet.models.antonino.Arc;
import org.pneditor.petrinet.models.antonino.ArcDrain;
import org.pneditor.petrinet.models.antonino.ArcPT;
import org.pneditor.petrinet.models.antonino.ArcTP;
import org.pneditor.petrinet.models.antonino.ArcZero;
import org.pneditor.petrinet.models.antonino.PetriNet;
import org.pneditor.petrinet.models.antonino.Place;
import org.pneditor.petrinet.models.antonino.Transition;

public class ArcAdapter extends AbstractArc {

	private Arc arc;
	private PetriNetAdapter petriNetAdapter;
	
	public ArcAdapter(Arc arc, PetriNetAdapter petriNetAdapter) {
		this.arc = arc;
		this.petriNetAdapter = petriNetAdapter;
	}

	@Override
	public AbstractNode getSource() {
		if (arc instanceof ArcTP) {
			Transition t = arc.getTransition();
			HashMap<Transition, TransitionAdapter> transitionMap = petriNetAdapter.getTransitionToAdapterMap();
			return (AbstractNode) transitionMap.get(t);	
		}
		if (arc instanceof ArcPT) {
			Place p = arc.getPlace();
			HashMap<Place, PlaceAdapter> placeMap = petriNetAdapter.getPlaceToAdapterMap();
			return (AbstractNode) placeMap.get(p);
		}
		return null;
	}

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

	@Override
	public boolean isReset() {
		return arc instanceof ArcZero;
	}

	@Override
	public boolean isRegular() {
		return !isReset() && !isInhibitory();
	}

	@Override
	public boolean isInhibitory() {
		return arc instanceof ArcDrain;
	}

	@Override
	public int getMultiplicity() throws ResetArcMultiplicityException {
		return arc.getWeight();
	}

	@Override
	public void setMultiplicity(int multiplicity) throws ResetArcMultiplicityException {
		PetriNet pN = petriNetAdapter.getPetriNet();
		
		try {
			if (arc instanceof ArcTP) {
				Place p = arc.getPlace();
				Transition t = arc.getTransition();
				pN.removeArcTP((ArcTP) arc);
				arc = pN.addArcTP(multiplicity, p, t);
			}
			
			if (arc instanceof ArcPT) {
				Place p = arc.getPlace();
				Transition t = arc.getTransition();
				pN.removeArcPT((ArcPT) arc);
				arc = pN.addArcPT(multiplicity, p, t);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
