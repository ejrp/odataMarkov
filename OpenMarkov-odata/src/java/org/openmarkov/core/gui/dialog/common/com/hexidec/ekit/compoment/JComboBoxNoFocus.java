/*
* Copyright 2011 CISIAD, UNED, Spain
*
* Licensed under the European Union Public Licence, version 1.1 (EUPL)
*
* Unless required by applicable law, this code is distributed
* on an "AS IS" basis, WITHOUT WARRANTIES OF ANY KIND.
*/

/*
 * GNU Lesser General Public License JComboBoxNoFocus Copyright (C) 2000 Howard
 * Kistler This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */

package org.openmarkov.core.gui.dialog.common.com.hexidec.ekit.compoment;


import java.util.Vector;

import javax.swing.JComboBox;


/**
 * Class for providing a JComboBox that does not obtain focus
 */
public class JComboBoxNoFocus extends JComboBox {

	public JComboBoxNoFocus(Vector vc) {

		super(vc);
		this.setRequestFocusEnabled(false);
	}

	public JComboBoxNoFocus() {

		super();
		this.setRequestFocusEnabled(false);
	}

	public boolean isFocusable() {

		return false;
	}

	// deprecated in 1.4 with isFocusable(), left in for 1.3 compatibility
	public boolean isFocusTraversable() {

		return false;
	}
}