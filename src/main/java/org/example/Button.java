package org.example;

public class Button {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isClicked(Robot robot) {
        return robot.getX() >= x && robot.getX() <= x + width &&
               robot.getY() >= y && robot.getY() <= y + height;
    }


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
