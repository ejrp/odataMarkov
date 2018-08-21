package org.openmarkov.io.odata.gui;

 
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.openmarkov.io.odata.gui.editor.SchemaEditor;
import org.openmarkov.io.odata.gui.editor.SchemaEditorMenuBar;

public class GraphHandler {

	public static SchemaEditor displayXchema(OdataClientHandler odataHandler, WindowListener aWindowListener, JFrame aParentFrame) {
		try {
			Edm edm = odataHandler.readEdm();
			EdmGraphManager edmManager = EdmGraphManager.getEdmGraphManager(edm);
			DefaultListenableGraph<EdmEntityType, DefaultEdge> graph = edmManager.createEdmGraph();
					JGraphXAdapterDisplayer<EdmEntityType, DefaultEdge> adapter = JGraphXAdapterDisplayer.getAdapter(graph);
					SchemaEditor editor = new SchemaEditor(adapter.getJgxAdapter());
					JDialog editorFrame = editor.createFrame(new SchemaEditorMenuBar(editor),aParentFrame);
					editorFrame.addWindowListener(aWindowListener);
					editorFrame.setVisible(true);
					editorFrame.setAlwaysOnTop(true);
					editorFrame.setModalityType(ModalityType.APPLICATION_MODAL);
					return editor;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
				
	}

}
