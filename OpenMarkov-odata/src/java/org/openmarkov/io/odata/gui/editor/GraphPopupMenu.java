package org.openmarkov.io.odata.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.openmarkov.io.odata.ValueElementDiscretization;
import org.openmarkov.io.odata.ValueElementDiscretization.DiscretizationMethod;
import org.openmarkov.io.odata.gui.DiscretizationPane;

public class GraphPopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7889696740629269145L;
	protected ValueElementDiscretization lastMethodSelected = null;

	/**
	 * 
	 */

	public GraphPopupMenu() {

		super("Discretization Method");
		add(new JLabel("Discretization Method"));
		for (DiscretizationMethod discretizationMethod : ValueElementDiscretization.getDiscretizationMethodsName())
			add(new JMenuItem(discretizationMethod.toString())).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand().equals(DiscretizationMethod.VALUE_VARIATION.toString()))
						lastMethodSelected = (new DiscretizationPane(DiscretizationMethod.VALUE_VARIATION).getDiscretizationAlgorithm());
				}
	        });;

	}


	public ValueElementDiscretization getLastSelectedValueProcessorMethod() {
		return lastMethodSelected;
	}

}
