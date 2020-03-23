package com.zombie.data;

/**
 * The creatures (either zombie or uninfected creatures) inhabiting the world (grid)
 */
public class Creature {
    /**
     * The horizontal (x-axis) position of the creature
     */
    private int xPosition;
    /**
     * The vertical (y-axis) position of the creature
     */
    private int yPosition;

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Move the creature up (wrapping around the grid if needed)
     *
     * @param max the vertical height of the grid (zero based - 0 to n - 1)
     */
    public void moveUp(int max) {
        if (yPosition == 0) {
            yPosition = max;
        } else {
            yPosition--;
        }
    }

    /**
     * Move the creature down (wrapping around the grid if needed)
     *
     * @param max the vertical height of the grid (zero based - 0 to n - 1)
     */
    public void moveDown(int max) {
        if (yPosition == max) {
            yPosition = 0;
        } else {
            yPosition++;
        }
    }

    /**
     * Move the creature left (wrapping around the grid if needed)
     *
     * @param max the horizontal length of the grid (zero based - 0 to n - 1)
     */
    public void moveLeft(int max) {
        if (xPosition == 0) {
            xPosition = max;
        } else {
            xPosition--;
        }
    }

    /**
     * Move the creature right (wrapping around the grid if needed)
     *
     * @param max the horizontal length of the grid (zero based - 0 to n - 1)
     */
    public void moveRight(int max) {
        if (xPosition == max) {
            xPosition = 0;
        } else {
            xPosition++;
        }
    }

    /**
     * Output the position of the creature (if it has been turned into a zombie)
     *
     * @return the string containing the grid co-ordinates of the creature/zombie i.e. (3, 2) or (2, 7)
     */
    public String getPosition() {
        return "(" + xPosition + ", " + yPosition + ")";
    }
}
