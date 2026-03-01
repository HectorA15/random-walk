package org.example;

import java.util.ArrayList;
import java.util.List;

public class GameCore {

    private static final long MOVE_INTERVAL = 500; // 500 milisegundos
    Arena gameBounds;
    Robot robot;
    Button button;
    private long moveTimer = 0;
    private final List<Point> trajectory = new ArrayList<>(); // ← Nuevo

    private int attempts = 0;
    private int success = 0;

    public GameCore() {
        this.gameBounds = new Arena(0, 0, 800, 600);
        this.robot = new Robot(gameBounds.getWidth() / 2, gameBounds.getHeight() / 2);
        this.button = new Button(10, 10, 50, 50);
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
                        resetRobot();
                        success++;
                    }
                } else {
                    robot.setStatus(RobotStatus.FAILURE);
                }
                moveTimer = 0;
            }
        } else if (robot.getStatus() == RobotStatus.FAILURE) {
            resetRobot();
            attempts++;
        }
    }

    public void resetRobot() {
        trajectory.clear();

        this.robot = new Robot(gameBounds.getWidth() / 2, gameBounds.getHeight() / 2);

        trajectory.add(new Point(robot.getX(), robot.getY()));

        moveTimer = 0;
    }

    public Robot getRobot() {
        return robot;
    }

    public Arena getGameBounds() {
        return gameBounds;
    }

    public Button getButton() {
        return button;
    }

    public List<Point> getTrajectory() {
        return trajectory;
    }

    public double successProbability() {
        return (double) success / attempts * 100;
    }
}
