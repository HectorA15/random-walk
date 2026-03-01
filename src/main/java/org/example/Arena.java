package org.example;

public class Arena {
    private final int initX;
    private final int initY;
    private final int width;
    private final int height;

    public Arena(int initX, int initY, int width, int height) {
        this.initX = initX;
        this.initY = initY;
        this.width = width;
        this.height = height;
    }

    public int getLeftEdge() {
        return initX;
    }
    public int getRightEdge() {
        return initX + width;
    }
    public int getTopEdge() {
        return initY;
    }
    public int getBottomEdge() {
        return initY + height;
    }


    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
