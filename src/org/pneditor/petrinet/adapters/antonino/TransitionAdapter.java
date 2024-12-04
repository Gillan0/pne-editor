package org.pneditor.petrinet.adapters.antonino;

import org.pneditor.petrinet.AbstractTransition;
import org.pneditor.petrinet.models.antonino.Transition;

public class TransitionAdapter extends AbstractTransition {

	private Transition transition;
	
	public TransitionAdapter(String label, Transition t) {
		super(label);
		transition = t;
	}

	public Transition getTransition() {
		return transition;
	}

}
