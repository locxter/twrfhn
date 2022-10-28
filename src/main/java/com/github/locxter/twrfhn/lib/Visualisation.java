package com.github.locxter.twrfhn.lib;

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
    private ArrayList<ArrayList<Integer>> rods = new ArrayList<>();

    // Helper method to transform an unscaled value to a scaled one
    private int getScaledValue(int unscaledValue) {
        return (int) Math.round(unscaledValue * scalingRatio);
    }

    // Method to draw the component
    @Override
    protected void paintComponent(Graphics context) {
        int width = getWidth();
        int height = getHeight();
        // Clear the component
        super.paintComponent(context);
        context.setColor(new Color(60, 63, 65));
        context.fillRect(0, 0, width, height);
        // Calculate the scaling ratio and center the canvas
        if ((double) width / height > 16.0 / 9) {
            scalingRatio = height / 1080.0;
            context.translate((int) Math.round((width - (scalingRatio * 1920)) / 2), 0);
        } else {
            scalingRatio = width / 1920.0;
            context.translate(0, (int) Math.round((height - (scalingRatio * 1080)) / 2));
        }
        if (showStartScreen) {
            // Draw a start screen if no visualisation has been requested yet
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, getScaledValue(64));
            FontMetrics metrics = context.getFontMetrics(font);
            String message = "Press \"Calculate\" to see the visualisation.";
            context.setFont(font);
            context.setColor(new Color(255, 255, 255));
            context.drawString(message, (int) Math.round(((double) getScaledValue(1920) - metrics.stringWidth(message)) / 2), (int) Math.round((((double) getScaledValue(1080) - metrics.getHeight()) / 2) + metrics.getAscent()));
        } else {
            int diskCount = rods.get(0).size() + rods.get(1).size() + rods.get(2).size();
            for (int i = 0; i < 3; i++) {
                // Drawing a rod
                context.setColor(new Color(255, 255, 255));
                context.fillRect(getScaledValue(304 + (640 * i)), 0, getScaledValue(32), getScaledValue(1080));
                // Drawing the disks on the rod
                context.setColor(new Color(0, 255, 0));
                for (int j = 0; j < rods.get(i).size(); j++) {
                    int diskWidth = (int) Math.round((640.0 / diskCount) * (rods.get(i).get(j) + 1));
                    context.fillRect(getScaledValue(((320 - (diskWidth / 2)) + (640 * i))), getScaledValue((int) Math.round(1080 - (108 * (rods.get(i).size() - j)))), getScaledValue(diskWidth), getScaledValue(96));
                }
            }
        }
    }

    // Constructor
    public Visualisation() {
        super();
    }

    // Method to update the visualisation
    public void showVisualisation(ArrayList<ArrayList<Integer>> rods) {
        if (showStartScreen) {
            showStartScreen = false;
        }
        this.rods = rods;
        repaint();
    }
}
