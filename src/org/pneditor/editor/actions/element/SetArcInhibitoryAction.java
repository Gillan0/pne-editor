/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pneditor.editor.actions.element;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import org.pneditor.editor.Root;
import org.pneditor.editor.commands.SetArcInhibitCommand;
import org.pneditor.editor.gpetrinet.GraphicArc;
import org.pneditor.util.GraphicsTools;
import org.pneditor.PNEConstantsConfiguration;

/**
 *
 * @author Amodez
 */
@SuppressWarnings("serial")
public class SetArcInhibitoryAction extends AbstractAction {

	private final Root root;

	public SetArcInhibitoryAction(final Root root) {
		super();
		this.root = root;
		String name = "Set inhibitor arc type";
		putValue(NAME, name);
		putValue(SMALL_ICON, GraphicsTools.getIcon(PNEConstantsConfiguration.CONVERTTRANSITIONTOSUBNET));
		putValue(SHORT_DESCRIPTION, name);
		putValue(MNEMONIC_KEY, KeyEvent.VK_I);
		setEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (this.root.getClickedElement() != null && !this.root.getClickedElement().isNode()) {
			final GraphicArc arc = (GraphicArc) this.root.getClickedElement();
			this.root.getUndoManager().executeCommand(new SetArcInhibitCommand(arc, this.root.getGraphicPetriNet()));
		}

	}
}
