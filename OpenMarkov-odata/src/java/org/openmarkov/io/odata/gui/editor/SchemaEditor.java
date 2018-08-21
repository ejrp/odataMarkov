package org.openmarkov.io.odata.gui.editor;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.openmarkov.io.odata.gui.LoginWindow;

import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class SchemaEditor extends BasicGraphEditor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7007225006753337933L;
	private WindowListener callbackListener;


	public SchemaEditor(JGraphXAdapter<EdmEntityType, DefaultEdge> graphAdapter)
	{
		super("Odata Explorer", new SchemaGraphComponent(graphAdapter));
		mxIGraphLayout layout = createLayout("verticalHierarchical", true);
		
		final mxGraph graph = graphComponent.getGraph();
		Object cell = graph.getSelectionCell();

		if (cell == null
				|| graph.getModel().getChildCount(cell) == 0)
		{
			cell = graph.getDefaultParent();
		}

		graph.getModel().beginUpdate();
		try
		{
			long t0 = System.currentTimeMillis();
			layout.execute(cell);
			status("Layout: " + (System.currentTimeMillis() - t0)
					+ " ms");
		}
		finally
		{
			mxMorphing morph = new mxMorphing(graphComponent, 20,
					1.2, 20);

			morph.addListener(mxEvent.DONE, new mxIEventListener()
			{

				public void invoke(Object sender, mxEventObject evt)
				{
					graph.getModel().endUpdate();
				}

			});

			morph.startAnimation();
		}
	}
	
	/**
	 * 
	 */
	public SchemaEditor()
	{
		super("Marcov Analysis tool", new SchemaGraphComponent(new mxGraph()
		{
			/**
			 * Allows expanding tables
			 */
			public boolean isCellFoldable(Object cell, boolean collapse)
			{
				return model.isVertex(cell);
			}
		})

		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -1194463455177427496L;

			/**
			 * Disables folding icons.
			 */
			public ImageIcon getFoldingIcon(mxCellState state)
			{
				return null;
			}

		});

		// Creates a single shapes palette
		EditorPalette shapesPalette = insertPalette("Schema");
		graphOutline.setVisible(false);

		mxCell tableTemplate = new mxCell("New Table", new mxGeometry(0, 0,
				200, 280), null);
		tableTemplate.getGeometry().setAlternateBounds(
				new mxRectangle(0, 0, 140, 25));
		tableTemplate.setVertex(true);

		shapesPalette
				.addTemplate(
						"Table",
						new ImageIcon(
								GraphEditor.class
										.getResource("org/openmarkov/io/odata/gui/editor/images/rectangle.png")),
						tableTemplate);

		getGraphComponent().getGraph().setCellsResizable(true);
		getGraphComponent().setConnectable(true);
		getGraphComponent().getGraphHandler().setCloneEnabled(false);
		getGraphComponent().getGraphHandler().setImagePreview(false);

		updateModel(null);
	}

	public void updateModel(DefaultListenableGraph<EdmEntityType,DefaultEdge> newGraph) {
		if (newGraph != null) {
			mxGraph graph = getGraphComponent().getGraph();
			Object parent = graph.getDefaultParent();
			graph.getModel().beginUpdate();
			try
			{
				Object allCells = graph.getSelectionCell();
				mxCell v1 = (mxCell) graph.insertVertex(parent, null, "Customers",
						20, 20, 200, 280);
				v1.getGeometry().setAlternateBounds(new mxRectangle(0, 0, 140, 25));
				mxCell v2 = (mxCell) graph.insertVertex(parent, null, "Orders",
						280, 20, 200, 280);
				v2.getGeometry().setAlternateBounds(new mxRectangle(0, 0, 140, 25));
			}
			finally
			{
				graph.getModel().endUpdate();
			}
		}
	}

	/**
	 * 
	 */
	protected void installToolBar()
	{
		add(new SchemaEditorToolBar(this, JToolBar.HORIZONTAL),
				BorderLayout.NORTH);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		try {
			LoginWindow frame = new LoginWindow();
			frame.setSize(300, 150);
			frame.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}


//	@Override
//	public void exit() {
//		if (callbackListener != null) {
//			callbackListener.handleEvent(new Instance() {
//			})
//		}
//		super.exit();
//	}

	
}
