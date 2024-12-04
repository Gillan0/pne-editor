package org.pneditor.petrinet.adapters.antonino;

import org.pneditor.petrinet.AbstractPlace;
import org.pneditor.petrinet.models.antonino.MissingPlaceException;
import org.pneditor.petrinet.models.antonino.NegativeException;
import org.pneditor.petrinet.models.antonino.PetriNet;
import org.pneditor.petrinet.models.antonino.Place;

public class PlaceAdapter extends AbstractPlace {
	
	private Place place;
	private PetriNet petriNet;
	
	public PlaceAdapter(String label, Place p, PetriNet petriNet) {
		super(label);
		this.place = p;
		this.petriNet = petriNet;
	}

	@Override
	public void addToken() {
		try {
			petriNet.addTokens(place, 1);
		} catch (MissingPlaceException | NegativeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeToken() {
		try {
			petriNet.removeTokens(place, 1);
		} catch (MissingPlaceException | NegativeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getTokens() {
		return place.getTokens();
	}

	@Override
	public void setTokens(int tokens) {
		place.setTokens(tokens);
	}
	
	public Place getPlace() {
		return place;
	}

}
