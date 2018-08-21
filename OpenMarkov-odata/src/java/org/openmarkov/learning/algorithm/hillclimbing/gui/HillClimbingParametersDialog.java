/*
 * Copyright 2011 CISIAD, UNED, Spain Licensed under the European Union Public
 * Licence, version 1.1 (EUPL) Unless required by applicable law, this code is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OF ANY KIND.
 */

package org.openmarkov.learning.algorithm.hillclimbing.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.openmarkov.core.io.database.CaseDatabase;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.type.MarkovNetworkType;
import org.openmarkov.learning.algorithm.hillclimbing.HillClimbingAlgorithm;
import org.openmarkov.learning.algorithm.scoreAndSearch.metric.Metric;
import org.openmarkov.learning.algorithm.scoreAndSearch.metric.annotation.MetricManager;
import org.openmarkov.learning.core.algorithm.LearningAlgorithm;
import org.openmarkov.learning.gui.AlgorithmConfiguration;
import org.openmarkov.learning.gui.AlgorithmParametersDialog;

/**
 * This abstract class represents the dialog that shows the user the options and
 * parameters of the hill climber algorithm.
 * @author joliva
 */
@SuppressWarnings("serial")
@AlgorithmConfiguration(algorithm = HillClimbingAlgorithm.class)
public class HillClimbingParametersDialog extends AlgorithmParametersDialog
{
    // Default values for Metric and alpha parameter
    private MetricManager metricManager  = null;
    private String        metric         = "";
    private String        alphaParameter = "0.5";

    /** Creates new form HillClimberOptionsGUI */
    public HillClimbingParametersDialog (JFrame parent, boolean modal)
    {
        super (parent, modal);
        setLocationRelativeTo (parent);
        metricManager = new MetricManager ();
        initComponents ();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void initComponents ()
    {
        jPanel1 = new javax.swing.JPanel ();
        MetricLabel = new javax.swing.JLabel ();
        metricComboBox = new javax.swing.JComboBox<> ();
        alphaText = new javax.swing.JTextField ();
        jLabel7 = new javax.swing.JLabel ();
        AceptButton = new javax.swing.JButton ();
        setDefaultCloseOperation (javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle (stringDatabase.getString ("Learning.HillClimbing.Title"));
        setResizable (false);
        jPanel1.setBorder (javax.swing.BorderFactory.createTitledBorder (stringDatabase.getString ("Learning.HillClimbing.Title")));
        MetricLabel.setText (stringDatabase.getString ("Learning.HillClimbing.Metric"));
        metricComboBox.setModel (new javax.swing.DefaultComboBoxModel (
                                                                       metricManager.getAllMetricNames ().toArray ()));
        metric = metricComboBox.getSelectedItem ().toString ();
        alphaText.setText (alphaParameter);
        jLabel7.setText (stringDatabase.getString ("Learning.Alpha"));
        jLabel7.setToolTipText (stringDatabase.getString ("Learning.Alpha.Tooltip"));
        AceptButton.setText (stringDatabase.getString ("Learning.Ok"));
        AceptButton.addActionListener (new java.awt.event.ActionListener ()
            {
                public void actionPerformed (java.awt.event.ActionEvent evt)
                {
                    AceptButtonActionPerformed (evt);
                }
            });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout (jPanel1);
        jPanel1.setLayout (jPanel1Layout);
        jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING).addGroup (jPanel1Layout.createSequentialGroup ().addGroup (jPanel1Layout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING).addGroup (jPanel1Layout.createSequentialGroup ().addContainerGap ().addComponent (jLabel7).addPreferredGap (javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent (alphaText,
                                                                                                                                                                                                                                                                                                                                                                                                                                               javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                               40,
                                                                                                                                                                                                                                                                                                                                                                                                                                               javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup (jPanel1Layout.createSequentialGroup ().addGap (102,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 102,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 102).addComponent (AceptButton)).addGroup (jPanel1Layout.createSequentialGroup ().addContainerGap ().addComponent (MetricLabel).addPreferredGap (javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent (metricComboBox,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    193,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap (javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING).addGroup (jPanel1Layout.createSequentialGroup ().addGap (18,
                                                                                                                                                                               18,
                                                                                                                                                                               18).addGroup (jPanel1Layout.createParallelGroup (javax.swing.GroupLayout.Alignment.BASELINE).addComponent (metricComboBox,
                                                                                                                                                                                                                                                                                          javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                          javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                          javax.swing.GroupLayout.PREFERRED_SIZE).addComponent (MetricLabel)).addGap (18,
                                                                                                                                                                                                                                                                                                                                                                      18,
                                                                                                                                                                                                                                                                                                                                                                      18).addGroup (jPanel1Layout.createParallelGroup (javax.swing.GroupLayout.Alignment.BASELINE).addComponent (alphaText,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 javax.swing.GroupLayout.PREFERRED_SIZE).addComponent (jLabel7)).addPreferredGap (javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  39,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Short.MAX_VALUE).addComponent (AceptButton).addContainerGap ()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout (getContentPane ());
        getContentPane ().setLayout (layout);
        layout.setHorizontalGroup (layout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING).addGroup (layout.createSequentialGroup ().addContainerGap ().addComponent (jPanel1,
                                                                                                                                                                                     javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                     javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                     javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap (javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                              Short.MAX_VALUE)));
        layout.setVerticalGroup (layout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING).addGroup (layout.createSequentialGroup ().addContainerGap ().addComponent (jPanel1,
                                                                                                                                                                                   javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                   javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                   javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap (javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                            Short.MAX_VALUE)));
        pack ();
    }// </editor-fold>//GEN-END:initComponents

    private void AceptButtonActionPerformed (java.awt.event.ActionEvent evt)
    {// GEN-FIRST:event_AceptButtonActionPerformed
        try
        {
            double alpha = Double.parseDouble (alphaText.getText ());
            if ((alpha < 0) || (alpha > 1))
            {
                JOptionPane.showMessageDialog (null,
                                               stringDatabase.getString ("Learning.Alpha.Error"),
                                               stringDatabase.getString ("ErrorWindow.Title.Label"),
                                               JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog (null, stringDatabase.getString ("Learning.Alpha.Error"),
                                           stringDatabase.getString ("ErrorWindow.Title.Label"),
                                           JOptionPane.ERROR_MESSAGE);
            return;
        }
        metric = (String) metricComboBox.getSelectedItem ();
        alphaParameter = alphaText.getText ();
        this.setVisible (false);
    }// GEN-LAST:event_AceptButtonActionPerformed

    public String getDescription ()
    {
        return stringDatabase.getString ("Learning.HillClimbing.Metric") + ": " + metric + "\r\n"
               + stringDatabase.getString ("Learning.Alpha") + ": " + alphaParameter;
    }

    public String getMetric ()
    {
        return metric;
    }

    public String getAlphaParameter ()
    {
        return alphaParameter;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton           AceptButton;
    private javax.swing.JLabel            MetricLabel;
    private javax.swing.JTextField        alphaText;
    private javax.swing.JLabel            jLabel7;
    private javax.swing.JPanel            jPanel1;
    private javax.swing.JComboBox<String> metricComboBox;

    // End of variables declaration//GEN-END:variables

    @Override
    public LearningAlgorithm getInstance (ProbNet probNet, CaseDatabase database)
    {
        Metric metricInstance = null;
        try
        {
            Constructor<?>[] constructors = metricManager.getMetricByName (metric).getConstructors ();
            for (Constructor<?> constructor : constructors)
            {
                Class<?>[] parameterTypes = constructor.getParameterTypes ();
                if (parameterTypes.length == 1 && parameterTypes[0] == double.class) metricInstance = (Metric) constructor.newInstance (Double.parseDouble (alphaParameter));
                else if (parameterTypes.length == 0) metricInstance = (Metric) constructor.newInstance ();
            }
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e)
        {
            e.printStackTrace ();
        }        
        return new HillClimbingAlgorithm (probNet, database, Double.parseDouble (alphaParameter), metricInstance,!(probNet.getNetworkType() instanceof MarkovNetworkType));
    }
}
