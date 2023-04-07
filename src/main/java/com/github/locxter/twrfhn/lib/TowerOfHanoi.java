package com.github.locxter.twrfhn.lib;

import java.util.ArrayList;

// Tower of Hanoi class
public class TowerOfHanoi {
    // Attributes
    private int diskCount = 1;

    // Constructors
    public TowerOfHanoi() {
    }

    public TowerOfHanoi(int diskCount) {
        this();
        setDiskCount(diskCount);
    }

    // Helper method to clone the rods before saving them
    private ArrayList<ArrayList<Integer>> cloneRods(ArrayList<ArrayList<Integer>> originalRods) {
        ArrayList<Integer> copiedRod0 = new ArrayList<>(originalRods.get(0));
        ArrayList<Integer> copiedRod1 = new ArrayList<>(originalRods.get(1));
        ArrayList<Integer> copiedRod2 = new ArrayList<>(originalRods.get(2));
        ArrayList<ArrayList<Integer>> copiedRods = new ArrayList<>();
        copiedRods.add(copiedRod0);
        copiedRods.add(copiedRod1);
        copiedRods.add(copiedRod2);
        return copiedRods;
    }

    // Getter and setter
    public int getDiskCount() {
        return diskCount;
    }

    public void setDiskCount(int diskCount) {
        if (diskCount > 0) {
            this.diskCount = diskCount;
        }
    }

    // Method to calculate the moves needed to solve the puzzle
    public ArrayList<ArrayList<ArrayList<Integer>>> calculateMoves() {
        // Internal variables
        ArrayList<Integer> rod0 = new ArrayList<>();
        ArrayList<Integer> rod1 = new ArrayList<>();
        ArrayList<Integer> rod2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> rods = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Integer>>> moves = new ArrayList<>();
        ArrayList<Integer> disk0Positions = new ArrayList<>();
        // Set the movement direction for disk 0 following this scheme: [last, current, next]
        if (diskCount % 2 == 0) {
            // Move disk 0 through the rods from A to C when having an even number of disks
            disk0Positions.add(2);
            disk0Positions.add(0);
            disk0Positions.add(1);
        } else {
            // Move disk 0 through the rods from C to A when having an odd number of disks
            disk0Positions.add(1);
            disk0Positions.add(0);
            disk0Positions.add(2);
        }
        // Create the start configuration
        for (int i = 0; i < diskCount; i++) {
            rod0.add(i);
        }
        rods.add(rod0);
        rods.add(rod1);
        rods.add(rod2);
        moves.add(cloneRods(rods));
        // Move disks until all of them are on rod C and in the right order
        while (rods.get(2).size() < diskCount) {
            // Move disk 0 to the next rod
            rods.get(disk0Positions.get(2)).add(0, 0);
            rods.get(disk0Positions.get(1)).remove(0);
            moves.add(cloneRods(rods));
            if (rods.get(2).size() < diskCount) {
                try {
                    if (rods.get(disk0Positions.get(0)).get(0) < rods.get(disk0Positions.get(1)).get(0)) {
                        // Moving another disk from second last to last position of disk 0 if possible
                        rods.get(disk0Positions.get(1)).add(0, rods.get(disk0Positions.get(0)).get(0));
                        rods.get(disk0Positions.get(0)).remove(0);
                        moves.add(cloneRods(rods));
                    } else if (rods.get(disk0Positions.get(1)).get(0) < rods.get(disk0Positions.get(0)).get(0)) {
                        // Moving another disk from second last to last position of disk 0 if possible
                        rods.get(disk0Positions.get(0)).add(0, rods.get(disk0Positions.get(1)).get(0));
                        rods.get(disk0Positions.get(1)).remove(0);
                        moves.add(cloneRods(rods));
                    }
                } catch (Exception exception) {
                    if (rods.get(disk0Positions.get(1)).isEmpty()) {
                        // Moving another disk from second last to last position of disk 0 if possible
                        rods.get(disk0Positions.get(1)).add(0, rods.get(disk0Positions.get(0)).get(0));
                        rods.get(disk0Positions.get(0)).remove(0);
                        moves.add(cloneRods(rods));
                    } else if (rods.get(disk0Positions.get(0)).isEmpty()) {
                        // Moving another disk from second last to last position of disk 0 if possible
                        rods.get(disk0Positions.get(0)).add(0, rods.get(disk0Positions.get(1)).get(0));
                        rods.get(disk0Positions.get(1)).remove(0);
                        moves.add(cloneRods(rods));
                    }
                }
            }
            // Set new positions of disk 0 for the next iteration
            disk0Positions.add(disk0Positions.get(0));
            disk0Positions.remove(0);
        }
        return moves;
    }
}
