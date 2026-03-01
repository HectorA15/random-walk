package org.example;

import java.util.ArrayList;
import java.util.List;

public class GameCore {

    private static final long MOVE_INTERVAL = 1;

    Arena gameBounds;
    Robot robot;
    Button button;
    private long moveTimer = 0;
    private final List<Point> trajectory = new ArrayList<>();

    private int attempts = 0;
    private int success = 0;
    private int centerX;
    private int centerY;
    public GameCore() {
        this.gameBounds = new Arena(0, 0, 1280, 720);
        this.centerX = gameBounds.getWidth() / 2;
        this.centerY = gameBounds.getHeight() / 2;
        this.robot = new Robot(centerX, centerY);
        this.button = new Button(centerX+200, centerY, 75, 75);
        trajectory.add(new Point(robot.getX(), robot.getY()));
    }

    public void update(long elapsed) {
        if (gameBounds == null) {
            return;
        }

        if (robot.getStatus() == RobotStatus.SEARCHING) {
            moveTimer += elapsed;

            if (moveTimer >= MOVE_INTERVAL) {
                if (robot.getBattery() > 0) {
                    robot.setOldPosition();
                    robot.move();
                    robot.checkCollision(gameBounds);

                    trajectory.add(new Point(robot.getX(), robot.getY()));

                    if (button.isClicked(robot)) {
                        robot.setStatus(RobotStatus.SUCCESS);
                        success++;
                    }
                } else {
                    robot.setStatus(RobotStatus.FAILURE);
                }
                moveTimer = 0;
            }
        } else {
            attempts++;
            resetRobot();
        }
    }

    public void resetRobot() {
        trajectory.clear();
        this.robot = new Robot(gameBounds.getWidth() / 2, gameBounds.getHeight() / 2);
        trajectory.add(new Point(robot.getX(), robot.getY()));
        moveTimer = 0;
    }

    public int getAttempts() { return attempts; }
    public int getSuccess() { return success; }
    public Robot getRobot() { return robot; }
    public Arena getGameBounds() { return gameBounds; }
    public Button getButton() { return button; }
    public List<Point> getTrajectory() { return trajectory; }

    public double successProbability() {
        if (attempts == 0) return 0.0;
        return (double) success / attempts * 100;
    }
}