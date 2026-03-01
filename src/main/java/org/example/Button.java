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
        double robotLeft = robot.getX() - (robot.getWidth() / 2.0);
        double robotRight = robot.getX() + (robot.getWidth() / 2.0);
        double robotTop = robot.getY() - (robot.getHeight() / 2.0);
        double robotBottom = robot.getY() + (robot.getHeight() / 2.0);

        double buttonRight = x + width;
        double buttonBottom = y + height;

        return robotLeft < buttonRight &&
                robotRight > x &&
                robotTop < buttonBottom &&
                robotBottom > y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}