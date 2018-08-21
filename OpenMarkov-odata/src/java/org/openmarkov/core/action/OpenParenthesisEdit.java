/*
* Copyright 2011 CISIAD, UNED, Spain
*
* Licensed under the European Union Public Licence, version 1.1 (EUPL)
*
* Unless required by applicable law, this code is distributed
* on an "AS IS" basis, WITHOUT WARRANTIES OF ANY KIND.
*/

package org.openmarkov.core.action;

import org.openmarkov.core.exception.DoEditException;

@SuppressWarnings("serial")
/** @author Manuel Arias
 * @see openmarkov.networks.edit.CloseParenthesisEdit */
public class OpenParenthesisEdit extends SimplePNEdit {

	// Constant
	public static final String description = "(";
	
	// Constructor
	/** Singleton pattern */
	public OpenParenthesisEdit() {
	    super(null);
	}

	// Methods
	@Override
	public void doEdit() throws DoEditException {
		//super.addEdit(this);
	}
	
	public void undo() {
		super.undo();
	}
	
	public String getUndoPresentationName() {
		return description + " " + getPresentationName();
	}
	
	public String getRedoPresentationName() {
		return description + " " + getPresentationName();
	}
	
	public String toString() {
		return description;
	}
	
}
