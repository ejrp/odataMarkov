package org.openmarkov.io.odata;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.olingo.client.api.domain.ClientCollectionValue;
import org.apache.olingo.client.api.domain.ClientComplexValue;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.api.domain.ClientEnumValue;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.api.domain.ClientValue;
import org.openmarkov.core.io.database.CaseDatabase;
import org.openmarkov.core.io.database.CaseDatabaseReader;
import org.openmarkov.core.model.network.NodeType;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.ProbNode;
import org.openmarkov.core.model.network.State;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.type.BayesianNetworkType;
import org.openmarkov.io.odata.gui.EdmGraphManager;

public class SchemaDataBaseReader implements CaseDatabaseReader {
	

	private GraphElementsSelector elementSelector;
    /** If the value of this variable is 0 all the Strings are kept as reading,
     * if the value is 1, all the Strings are changed to lower case and if the
     * value is 2, all the Strings are changed to upper case.<p>
     * This variable is useful to avoid transcription problems when the data
     * source has been entered by persons. */
    public static int translateToLowerUpperCase = 0;
    

	public SchemaDataBaseReader(GraphElementsSelector anElementSelector) {
		elementSelector = anElementSelector;
	}

	@Override
	public CaseDatabase load(String filename) throws IOException {
	    ProbNet probNet = new ProbNet ();
	    List<String> variablesNames = new ArrayList<String>();
        
        if (!elementSelector.hasSelections()){
//            variablesNames =elementSelector.getHeaders();;
//        }else{            
            throw new IOException ("No Field selections made from database");
        }
        int numRows = 0;
        ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator  = EdmGraphManager.getEdmGraphManager(null).queryDataBase(elementSelector.getSelectedElements());
        if (iterator == null) {
        	throw new IOException ("ODATA query error. Check URI building");
        }
        String graphName = elementSelector.getSelectedTables().iterator().next().toString();
        String queryReplyString = "";
        boolean gotHeaders = false;
        while (iterator.hasNext()) {
        	ClientEntity ce = iterator.next();
        	if (!gotHeaders) {
        		for (ClientProperty entry : ce.getProperties()) {
        				if (!entry.hasComplexValue()) {
        					 variablesNames.add(entry.getName());
        				}else {
        					for (ClientProperty itemEntry:entry.getComplexValue()) {
        						variablesNames.add(entry.getName().concat(":").concat(itemEntry.getName()));
        					}
        				}
        		}
        		gotHeaders = true;
        	}
        	queryReplyString = queryReplyString.concat(prettyPrint(ce.getProperties()) + "\n");
        }
        
        int numColumns = variablesNames.size ();
        List<List<String>> variablesStatesNames = new ArrayList<>(numColumns);
        for (int i = 0; i < numColumns; i++)
        {
        	variablesStatesNames.add(new ArrayList<String> ());
        }
        List<int[]> data = new ArrayList<int[]> (); // Read data
        List<Map<String, Integer>> variablesStates = new ArrayList<Map<String, Integer>> (); // Variables states
        for (int i = 0; i < numColumns; i++)
        {
        	variablesStates.add (new HashMap<String, Integer> ());
        }
        
        Scanner scanner = new Scanner(queryReplyString);
        while (scanner.hasNextLine()) {       	
            data.add (getDataLine (scanner.nextLine (), variablesStates, variablesStatesNames));
            numRows++;
        }
        scanner.close (); // ensure the underlying stream is always closed
        /*
         * Creation of the probNet. The probNet only contains variables but no
         * links.
         */
        probNet = getBayesNetVariables (graphName, variablesNames,
                                  variablesStatesNames);
        int[][] cases = new int[numRows][numColumns];
        for (int i = 0; i < numRows; i++)
        {
            int[] row = data.get (i);
            for (int j = 0; j < numColumns; j++)
            {
                cases[i][j] = row[j];
            }
        }
        return new CaseDatabase(probNet.getVariables (), cases);
	}
	
	private static String prettyPrint(Map<String, Object> properties) {
		StringBuilder b = new StringBuilder();
		Set<Entry<String, Object>> entries = properties.entrySet();

		for (Entry<String, Object> entry : entries) {
			b.append(entry.getKey()).append(":");
			Object value = entry.getValue();
			if (value instanceof Map) {
				value = prettyPrint((Map<String, Object>) value);
			} else if (value instanceof Calendar) {
				Calendar cal = (Calendar) value;
				value = SimpleDateFormat.getInstance().format(cal.getTime());
			}
		//	b.append(value).append("\n");
			b.append(value).append(",");
		}
		// remove last line break
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}

	private static String prettyPrint(Collection<ClientProperty> properties) {
		StringBuilder b = new StringBuilder();

		for (ClientProperty entry : properties) {
		 ClientValue value = entry.getValue();
			if (value.isCollection()) {
				ClientCollectionValue cclvalue = value.asCollection();
			//	b.append(prettyPrint(cclvalue.asJavaCollection()));
			} else if (value.isComplex()) {
				ClientComplexValue cpxvalue = value.asComplex();
				b.append(prettyPrint(cpxvalue.asJavaMap()));
			} else if (value.isEnum()) {
				ClientEnumValue cnmvalue = value.asEnum();
				//b.append(entry.getName()).append(": ");
				//b.append(cnmvalue.getValue()).append("\n");
				b.append(cnmvalue.getValue()).append(",");
			} else if (value.isPrimitive()) {
				//b.append(entry.getName()).append(": ");
				//b.append(entry.getValue()).append("\n");
				b.append(entry.getValue()).append(",");

			}
		}
		return b.toString();
	}
	
	
	
    /** Reads a data line of a 'csv' file. 
     * @param variablesStatesNames 
     * @param line. Data to be parsed. <code>String</code>
     * @param variablesStates. Current states of variables that can be updated
     *  if appears new ones. <code>ArrayList</code> of <code>HashSet</code> of
     *  <code>String</code>.
     * @return A data line, each cell contains the state number readed. 
     *  <code>Integer[]</code>. */
    private int[] getDataLine (String line,
                               List<Map<String, Integer>> variablesStates,
                               List<List<String>> variablesStatesNames)
    {
        int[] statesLines = new int[variablesStates.size ()];
        Scanner scanner = new Scanner (line);
        scanner.useDelimiter (",|;");
        int numVariable = 0;
        boolean updateNumVariable = true;
        while (scanner.hasNext ())
        {
            String stateVariable = scanner.next ();
            if (stateVariable.contains(":")) {
            	String[] stateVariableSplit = stateVariable.split(":");
            	stateVariable = stateVariableSplit[1];
            	if (!updateNumVariable) {
            		numVariable++;
            	}
            	updateNumVariable = false;
            }
            switch (translateToLowerUpperCase)
            {
                case 1 :
                    stateVariable = stateVariable.toLowerCase ();
                    break;
                case 2 :
                    stateVariable = stateVariable.toUpperCase ();
                    break;
                default :
                    break;
            }
            Map<String, Integer> variableInnerStates = variablesStates.get (numVariable);
            Integer stateNumber = variableInnerStates.get (stateVariable);
            if (stateNumber == null)
            {
                stateNumber = variableInnerStates.size ();
                variableInnerStates.put (stateVariable, stateNumber);
                variablesStatesNames.get (numVariable).add (stateVariable);
            }
            statesLines[numVariable] = stateNumber;
            if (updateNumVariable) numVariable++;
        }
        scanner.close ();
        return statesLines;
    }
    
    /** Returns a bayesian network with a list of variables: 
     *  <code>ProbNet</code> with a
     *  <code>openmarkov.networks.constraints.compound.BNConstraint</code>. */
    public static ProbNet getBayesNetVariables(String fileName, 
            List<String> variablesNames,
            List<List<String>> variablesStatesNames) {
        ProbNet probNet = new ProbNet(BayesianNetworkType.getUniqueInstance ());
        HashMap<String, String> ioNet = new HashMap<String, String>();
        ioNet.put("Name", fileName);
        for (int i = 0; i < variablesNames.size (); ++i){ 
            String variableName = variablesNames.get (i);
            List<String> variableStateNames = variablesStatesNames.get (i);
            Map<String, String> infoNode = new HashMap<String, String>();
            State[] states = new State[variableStateNames.size()];
            
            for (int j=0; j <variableStateNames.size (); ++j){
                states[j] = new State(variableStateNames.get (j));
            }
            infoNode.put("Title", variableName);
            infoNode.put("CoordinateX", "0");
            infoNode.put("CoordinateY", "0");
            infoNode.put("UseDefaultStates", "false");
            Variable variable = new Variable(variableName, states);
            ProbNode probNode = probNet.addProbNode(variable, NodeType.CHANCE);
            probNode.additionalProperties = infoNode;
            infoNode.put("Name", variableName);
        }
        probNet.additionalProperties = ioNet;

        return probNet;
    }    

}
