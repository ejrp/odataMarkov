package org.openmarkov.io.odata.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openmarkov.io.odata.ValueElementDiscretization;
import org.openmarkov.io.odata.ValueElementDiscretization.DiscretizationMethod;

public class DiscretizationPane extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7356204174387470981L;
	ValueElementDiscretization methodAlgorithm;

	public DiscretizationPane(DiscretizationMethod aMethod) {
		super();
		switch (aMethod) {
		case VALUE_VARIATION:
			createValueVariationConfiguration(aMethod);
			break;
		case LOGICAL_OP:

			break;
		case CATEGORIES:

			break;

		default:
			break;
		}
		setVisible(true);
	}

	private void createValueVariationConfiguration(DiscretizationMethod aMethod) {
		JTextField textField;

		textField = new JTextField(10) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1021717689263001480L;

			public void processKeyEvent(KeyEvent ev) {
				char c = ev.getKeyChar();
				try {
					// Ignore all non-printable characters. Just check the printable ones.
					if (c > 31 && c < 127) {
						Integer.parseInt(c + "");
					}
					super.processKeyEvent(ev);
				} catch (NumberFormatException nfe) {
					// Do nothing. Character input is not a number, so ignore it.
				}
			}
		};
		textField.setText("%");
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(new JLabel("Max Percentage of variation"));
		panel.add(textField);
		JButton SUBMIT = new JButton("SUBMIT");
		panel.add(SUBMIT);
		add(panel, BorderLayout.CENTER);
		
		SUBMIT.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				methodAlgorithm = ValueElementDiscretization.getValueVariationMethod(textField.getText());				
				setVisible(false);
			}
		});
		setTitle("Value Variation Configuration");
		pack();

	}

	public  ValueElementDiscretization getDiscretizationAlgorithm() {
		return methodAlgorithm;
	}
}
