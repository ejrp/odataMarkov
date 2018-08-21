package org.openmarkov.io.odata.gui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import org.openmarkov.io.odata.GraphElementsSelector;
import org.openmarkov.io.odata.gui.editor.SchemaGraphComponent;

public class OdataExplorerCanvas {
	
	private GraphElementsSelector elementSelector = null;

		
	public GraphElementsSelector getElementSelector() {
		return elementSelector;
	}



	public void setElementSelector(GraphElementsSelector elementSelector) {
		this.elementSelector = elementSelector;
	}



	public GraphElementsSelector accessOdataBase(Component parent, WindowListener aWindowsListeener) {

		try {
			LoginWindow frame = new LoginWindow();
			frame.setSize(500, 150);
			frame.setModalityType(ModalityType.APPLICATION_MODAL);
			if (parent != null) {
				frame.setLocationRelativeTo(parent);
			}
			if (aWindowsListeener != null)
				frame.setWindowListener(aWindowsListeener);
			frame.setVisible(true);
			if (frame.isSucceeded()) {
				elementSelector = ((SchemaGraphComponent)frame.getElementExplorer().getGraphComponent()).getElementSelector();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return elementSelector;

	}
	
	public static void main (String Arg[]) {
		OdataExplorerCanvas frame = new OdataExplorerCanvas();
		frame.accessOdataBase(null,null);
	}

}
