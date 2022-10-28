package com.github.locxter.twrfhn;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.FlatDarkLaf;
import com.github.locxter.twrfhn.lib.TowerOfHanoi;
import com.github.locxter.twrfhn.lib.Visualisation;

// Main class
public class Main {
    // Attributes
    static private TowerOfHanoi towerOfHanoi = new TowerOfHanoi(5);
    static private ArrayList<ArrayList<ArrayList<Integer>>> moves = new ArrayList<>();
    static private int currentMove = 0;
    static private int moveCount = 0;

    // Main method
    public static void main(String[] args) {
        // Set a pleasing LaF
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception exception) {
            System.out.println("Failed to initialize LaF.");
        }
        // UI components
        JFrame frame = new JFrame("twrfhn");
        JPanel panel = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel diskCountLabel = new JLabel("Disk count:");
        JSpinner diskCountInput = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        JButton calculateButton = new JButton("Calculate");
        JLabel moveLabel = new JLabel("Move:");
        JLabel moveData = new JLabel();
        JButton previousMoveButton = new JButton("Previous move");
        JButton nextMoveButton = new JButton("Next move");
        Visualisation visualisation = new Visualisation();
        JLabel aboutLabel = new JLabel("2022 locxter");
        // Add functions to the buttons
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                towerOfHanoi.setDiskCount((int) diskCountInput.getValue());
                moves = towerOfHanoi.calculateMoves();
                currentMove = 0;
                moveCount = moves.size() - 1;
                moveData.setText(currentMove + " / " + moveCount);
                visualisation.showVisualisation(moves.get(currentMove));
            }
        });
        previousMoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (currentMove > 0 && !moves.isEmpty()) {
                    currentMove--;
                    moveData.setText(currentMove + " / " + moveCount);
                    visualisation.showVisualisation(moves.get(currentMove));
                }
            }
        });
        nextMoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (currentMove < moveCount && !moves.isEmpty()) {
                    currentMove++;
                    moveData.setText(currentMove + " / " + moveCount);
                    visualisation.showVisualisation(moves.get(currentMove));
                }
            }
        });
        // Create the main panel
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.RELATIVE;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(diskCountLabel, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(diskCountInput, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(calculateButton, constraints);
        constraints.fill = GridBagConstraints.RELATIVE;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        panel.add(moveLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(moveData, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(previousMoveButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(nextMoveButton, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(visualisation, constraints);
        constraints.fill = GridBagConstraints.RELATIVE;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(aboutLabel, constraints);
        // Create the main window
        frame.setSize(new Dimension(640, 640));
        frame.setMinimumSize(new Dimension(480, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
    }
}
