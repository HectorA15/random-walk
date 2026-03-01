package org.example;

import java.util.Random;

public class Robot {

    Random random = new Random();
    private int steps;
    private int battery;
    private RobotStatus status;
    private int oldX;
    private int oldY;
    private int x;
    private int y;
    private final int width = 20;
    private final int height = 20;
    private int stepSize = 40;


    public Robot(int x, int y) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        steps = 0;
        battery = 100;
        status = RobotStatus.SEARCHING;
    }

    public int getSteps() {
        return steps;
    }

    public int getBattery() {
        return battery;
    }

    public RobotStatus getStatus() {
        return status;
    }

    public void setStatus(RobotStatus status) {
        this.status = status;
    }

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int x) {
        oldX = x;
    }

    public int getOldY() {
        return oldY;
    }

    public void setOldY(int y) {
        oldY = y;
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

    public void setOldPosition() {
        this.oldX = this.x;
        this.oldY = this.y;
    }

    public void checkCollision(Arena arenaBounds) {


        if (this.x <= arenaBounds.getLeftEdge()) {
            this.x = arenaBounds.getLeftEdge();
        }

        if (this.x + width >= arenaBounds.getRightEdge()) {
            this.x = arenaBounds.getRightEdge() - width;
        }

        if (this.y <= arenaBounds.getTopEdge()) {
            this.y = arenaBounds.getTopEdge();
        }

        if (this.y + height >= arenaBounds.getBottomEdge()) {
            this.y = arenaBounds.getBottomEdge() - height;
        }
    }

    public void move() {
        steps++;
        battery--;

        this.oldX = this.x;
        this.oldY = this.y;

        if (battery <= 0) {
            status = RobotStatus.FAILURE;
            return;
        }

        int dx = random.nextInt(stepSize*2 + 1) - stepSize;
        int dy = random.nextInt(stepSize*2 + 1) - stepSize;

        this.x += dx;
        this.y += dy;

    }
}
