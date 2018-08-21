package org.openmarkov.io.odata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultListenableGraph;
import org.openmarkov.io.odata.gui.EdmGraphManager;


public class GraphElementsSelector {

	public class GraphElement {

		HashMap<Object,LinkedList<String>> vertex = new HashMap<>(); 
		//	LinkedList<ValueElementDiscretization> valueProcessors = new LinkedList<>();
		GraphElement expand;

		public GraphElement getExpand() {
			return expand;
		}

		public GraphElement(Object source, String property) {						
			addVertex(source, property);
			//	valueProcessors.add(valueProcessor);
		}

		public Object getSource() {
			//so far only one instance
			return vertex.keySet().iterator().next();
		}

		public LinkedList<String> getProperties(Object key) {
			return vertex.get(key);
		}

		//		public LinkedList<ValueElementDiscretization> getValueProcessors() {
		//			return valueProcessors;
		//		}

		public GraphElement addVertex(Object value, Object aProperty) {
			LinkedList<String> elementProperty = null;
			if (!vertex.isEmpty()) {
				elementProperty = vertex.get(value);
			}
			//		ValueElementDiscretization processor = valueProcessor;
			if (elementProperty == null){
				elementProperty = new LinkedList<>();
				vertex.put(value, elementProperty);
			}
			elementProperty.add((String) aProperty);					
			return this;
		}

		public GraphElement addVertexExtend(Object value, Object property) {

			GraphElement extendedElement = expand;	
			if (extendedElement == null) {
				expand = new GraphElement(value, (String) property);
			}			
			else {
				extendedElement.addVertex(value, property);
				//element.getValueProcessors().add(processor);			
			}			
			return expand;
		}

		public void removeVertex(Object value, Object property) {	
			LinkedList<String> propertyList = vertex.get(value);
			if (propertyList != null) {		
				Iterator<String> itr = propertyList.iterator();
				//	Iterator<ValueElementDiscretization> itr2 = graphElement.getValueProcessors().iterator();
				while (itr.hasNext()) {
					//		ValueElementDiscretization processor = itr2.next();
					if (itr.next().equals(property)) {
						propertyList.remove(property);
						//		graphElement.getValueProcessors().remove(processor);
						break;
					}
				}
				if(propertyList.isEmpty()) {				
					vertex.remove(value);
				}
			}
		}

		public  HashMap<Object,LinkedList<String>> getAllVertex() {
			return vertex;
		}

		public LinkedList<String> getVertex(Object element) {
			return vertex.get(element);
		}
	}
	
	GraphElement elements;
	private DefaultListenableGraph graph;

	public <V, E> ListenableGraph<V, E> commit(){		
		return (ListenableGraph<V, E>) EdmGraphManager.getEdmInitialGraph(elements);	
	}
	
	public void addVertex(Object value, String aProperty) {
		if (elements == null){
			elements = new GraphElement(value, aProperty);			
		}else {
			if (elements.getVertex(value)!= null) {
				elements.addVertex(value, aProperty);
			}
			else {//only one table query supported, only extends allowed
				elements.addVertexExtend(value, aProperty);
			}
		}
	}

	public boolean hasSelections() {
		return !elements.vertex.isEmpty();
	}

	public List<String> getHeaders(Object aValue) {
		if (elements.vertex.containsKey(aValue))
			return elements.vertex.get(aValue);
		return null;
	}
	
	public  Set<Object> getSelectedTables() {
		return elements.vertex.keySet();
	}

	public GraphElement getSelectedElements() {
		return elements;
	}
}

