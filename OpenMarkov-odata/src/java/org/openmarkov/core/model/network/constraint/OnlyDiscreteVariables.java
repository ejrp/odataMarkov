/*
* Copyright 2011 CISIAD, UNED, Spain
*
* Licensed under the European Union Public Licence, version 1.1 (EUPL)
*
* Unless required by applicable law, this code is distributed
* on an "AS IS" basis, WITHOUT WARRANTIES OF ANY KIND.
*/

package org.openmarkov.core.model.network.constraint;

import java.util.List;

import org.openmarkov.core.action.AddProbNodeEdit;
import org.openmarkov.core.action.PNEdit;
import org.openmarkov.core.action.VariableTypeEdit;
import org.openmarkov.core.exception.NonProjectablePotentialException;
import org.openmarkov.core.exception.WrongCriterionException;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.VariableType;
import org.openmarkov.core.model.network.constraint.annotation.Constraint;


@Constraint (name = "OnlyDiscreteVariables", defaultBehavior = ConstraintBehavior.OPTIONAL)
public class OnlyDiscreteVariables extends PNConstraint {
	
	@Override
	public boolean checkEdit(ProbNet probNet, PNEdit edit)
	throws NonProjectablePotentialException, 
	WrongCriterionException {
	    List<PNEdit> edits = 
			UtilConstraints.getSimpleEditsByType(edit, AddProbNodeEdit.class);
		for (PNEdit simpleEdit : edits) {
			Variable variable = ((AddProbNodeEdit)simpleEdit).getVariable(); 
			if (variable.getVariableType() != VariableType.FINITE_STATES) {
				return false;
			}
		}
		edits = 
			UtilConstraints.getSimpleEditsByType(edit, VariableTypeEdit.class);
		for (PNEdit simpleEdit : edits) {
			VariableType newType = ((VariableTypeEdit)simpleEdit).getNewVariableType(); 
			if (newType != VariableType.FINITE_STATES && newType !=
				VariableType.DISCRETIZED) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean checkProbNet(ProbNet probNet) {
	    List<Variable> variables = probNet.getVariables();
		for (Variable variable : variables) {
			if (variable.getVariableType() != VariableType.DISCRETIZED) {
				return false;
			}
		}
		return true;
	}

    @Override
    protected String getMessage ()
    {
        return "all variables must be discrete";
    }

}
