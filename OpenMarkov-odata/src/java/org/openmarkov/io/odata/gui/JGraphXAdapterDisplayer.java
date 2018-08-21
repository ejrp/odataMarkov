/*
 * (C) Copyright 2013-2017, by Barak Naveh and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.openmarkov.io.odata.gui;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;

public class JGraphXAdapterDisplayer<V, E> {

//	private static final Dimension DEFAULT_SIZE = new Dimension(640, 480);

	/**
	 * 
	 */
	private static JGraphXAdapterDisplayer<?, ?> adapter;

	private JGraphXAdapter<V, E> jgxAdapter;

	public JGraphXAdapter<V, E> getJgxAdapter() {
		return jgxAdapter;
	}

	public void setJgxAdapter(JGraphXAdapter<V, E> jgxAdapter) {
		this.jgxAdapter = jgxAdapter;
	}

	public JGraphXAdapterDisplayer(ListenableGraph<V, E> graph) {
		init(graph);
		// setTitle("Odata Xcheme");
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// pack();
		// setMaximumSize(DEFAULT_SIZE);
		// setSize(DEFAULT_SIZE);
		// setVisible(true);
	}

	public void init(ListenableGraph<V, E> graph) {
		// create a visualization using JGraph, via an adapter
		jgxAdapter = new JGraphXAdapter<V, E>(graph) {
			/**
			 * Allows expanding tables
			 */
			@Override
			public boolean isCellFoldable(Object cell, boolean collapse) {
				return model.isVertex(cell);
			}
		};

		// mxGraphComponent mxGraphComponent = new mxGraphComponent(jgxAdapter);
		// getContentPane().add(mxGraphComponent);
		// mxGraphComponent.setSize(DEFAULT_SIZE);
		// mxGraphComponent.setMaximumSize(DEFAULT_SIZE);

		// positioning via jgraphx layouts
//		mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
//		jgxAdapter.getModel().beginUpdate();
//		try {
//			layout.execute(jgxAdapter.getDefaultParent());
//		}
//		finally {
//			jgxAdapter.getModel().endUpdate();
//		}
            //mxCompactTreeLayout layout5 = new mxCompactTreeLayout(jgxAdapter);
		// layout5.execute(jgxAdapter.getDefaultParent());
		// layout.setRadius(5);
		// layout.setDisableEdgeStyle(true);
		// // layout.setMoveCircle(true);
		// layout.setX0(0);
		// layout.setY0(0);

		// mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
		// layout.setRadius(5);
		// layout.setDisableEdgeStyle(true);
		// // layout.setMoveCircle(true);
		// layout.setX0(0);
		// layout.setY0(0);
		// layout.execute (jgxAdapter.getDefaultParent());

//		mxOrganicLayout layout2 = new mxOrganicLayout(jgxAdapter, new Rectangle(1500, 1500));
//		layout2.setAverageNodeArea(15000);
//		layout2.setOptimizeBorderLine(true);
//		layout2.setBorderLineCostFactor(100);
//		layout2.setMaxIterations(150000);
//		layout2.setMaxDistanceLimit(1000);
//		layout2.setMinDistanceLimit(10);
//
//		layout2.setOptimizeNodeDistribution(true);
//		// layout2.setApproxNodeDimensions(true);
//		layout2.setNodeDistributionCostFactor(1500000);
//		layout2.setOptimizeEdgeLength(false);
//		layout2.setOptimizeEdgeCrossing(true);
//		layout2.setEdgeCrossingCostFactor(100000);
//		layout2.execute(jgxAdapter.getDefaultParent());

		// mxHierarchicalLayout layout3 = new mxHierarchicalLayout(jgxAdapter);
		// layout3.setOrientation(SwingConstants.NORTH);
		//// layout3.setAverageNodeArea(10000);
		//// layout3.setMaxIterations(2000);
		//// layout3.setMaxDistanceLimit(10000);
		//// layout3.setMinDistanceLimit(1);
		//// layout3.setOptimizeNodeDistribution(true);
		// layout3.execute(jgxAdapter.getDefaultParent());

		// mxOrganicLayout layout4 = new mxOrganicLayout(jgxAdapter);
		// layout3.setOrientation(SwingConstants.NORTH);
		//// layout3.setAverageNodeArea(10000);
		//// layout3.setMaxIterations(2000);
		//// layout3.setMaxDistanceLimit(10000);
		//// layout3.setMinDistanceLimit(1);
		//// layout3.setOptimizeNodeDistribution(true);
		// layout3.execute(jgxAdapter.getDefaultParent());

	}

	@SuppressWarnings("unchecked")
	public static <V, E> JGraphXAdapterDisplayer<V, E> getAdapter(ListenableGraph<V, E> graph) {
		if (adapter == null) {
			adapter = new JGraphXAdapterDisplayer<V, E>(graph);
		}
		return (JGraphXAdapterDisplayer<V, E>) adapter;
	}

	public static <V, E> JGraphXAdapterDisplayer<?, ?> getAdapter() {
		return adapter;
	}
}

// End JGraphXAdapterDemo.java
