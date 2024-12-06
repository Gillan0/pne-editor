package org.pneditor.petrinet.adapters.antonino;

import org.pneditor.petrinet.AbstractTransition;
import org.pneditor.petrinet.models.antonino.Transition;

/**
 * Adapter class that bridges {@link AbstractTransition} with the {@link Transition} model.
 * It allows a {@link Transition} to be used as an {@link AbstractTransition}.
 */
public class TransitionAdapter extends AbstractTransition {

    /**
     * The wrapped {@link Transition} instance.
     */
    private Transition transition;

    /**
     * Constructs a {@code TransitionAdapter} with the given label and {@link Transition}.
     *
     * @param label the label of the transition
     * @param t the {@link Transition} instance to wrap
     */
    public TransitionAdapter(String label, Transition t) {
        super(label);
        this.transition = t;
    }

    /**
     * Retrieves the wrapped {@link Transition} instance.
     *
     * @return the wrapped {@link Transition}
     */
    public Transition getTransition() {
        return transition;
    }
}
