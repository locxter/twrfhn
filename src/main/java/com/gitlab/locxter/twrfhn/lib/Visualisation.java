package com.gitlab.locxter.twrfhn.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JComponent;

// Visualisation class
public class Visualisation extends JComponent {
    // Attributes
    private double scalingRatio;
    private boolean showStartScreen = true;
    private ArrayList<ArrayList<Integer>> rods;

    // Function to draw the component
    @Override
    protected void paintComponent(Graphics canvas) {
        // Clear the component
        super.paintComponent(canvas);
        canvas.setColor(new Color(255, 255, 255));
        canvas.fillRect(0, 0, getWidth(), getHeight());
        // Calculate the scaling ratio and center the canvas
        if ((double) getWidth() / getHeight() > 16.0 / 9) {
            scalingRatio = getHeight() / 1080.0;
            canvas.translate((int) Math.round((getWidth() - scalingRatio * 1920) / 2), 0);
        } else {
            scalingRatio = getWidth() / 1920.0;
            canvas.translate(0, (int) Math.round((getHeight() - scalingRatio * 1080) / 2));
        }
        if (showStartScreen) {
            // Draw a start screen if no visualisation has been requested yet
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, getScaledValue(64));
            FontMetrics metrics = canvas.getFontMetrics(font);
            String message = "Press \"Calculate\" to see the visualisation.";
            canvas.setFont(font);
            canvas.setColor(new Color(0, 0, 0));
            canvas.drawString(message, (int) Math.round(((double) getScaledValue(1920) - metrics.stringWidth(message)) / 2), (int) Math.round((((double) getScaledValue(1080) - metrics.getHeight()) / 2) + metrics.getAscent()));
        } else {
            int diskCount = rods.get(0).size() + rods.get(1).size() + rods.get(2).size();
            for (int i = 0; i < 3; i++) {
                // Drawing a rod
                canvas.setColor(new Color(0, 0, 0));
                canvas.fillRect(getScaledValue(304 + (640 * i)), 0, getScaledValue(32), getScaledValue(1080));
                // Drawing the disks on the rod
                canvas.setColor(new Color(0, 0, 255));
                for (int j = 0; j < rods.get(i).size(); j++) {
                    int diskWidth = (int) Math.round((640.0 / diskCount) * (rods.get(i).get(j) + 1));
                    canvas.fillRect(getScaledValue(((320 - (diskWidth / 2)) + (640 * i))), getScaledValue((int) Math.round(1080 - (108 * (rods.get(i).size() - j)))), getScaledValue(diskWidth), getScaledValue(96));
                }
            }
        }
    }

    // Helper function to transform an unscaled value to a scaled one
    private int getScaledValue(int unscaledValue) {
        return (int) Math.round(unscaledValue * scalingRatio);
    }

    // Helper function to update the visualisation
    public void showVisualisation(ArrayList<ArrayList<Integer>> rods) {
        if (showStartScreen) {
            showStartScreen = false;
        }
        this.rods = rods;
        repaint();
    }
}
