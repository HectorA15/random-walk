package org.example;

import java.util.ArrayList;
import java.util.List;

public class GameCore {

    private static final long MOVE_INTERVAL = 1;

    Arena gameBounds;
    Button button;
    private long moveTimer = 0;

    private final List<Robot> robots = new ArrayList<>();
    private final List<List<Point>> trajectories = new ArrayList<>();

    private int attempts = 0;
    private int success = 0;
    private int centerX;
    private int centerY;

    public GameCore() {
        this.gameBounds = new Arena(0, 0, 1280, 720);
        this.centerX = gameBounds.getWidth() / 2;
        this.centerY = gameBounds.getHeight() / 2;
        this.button = new Button(centerX + 200, centerY, 75, 75);
        addRobots(1);
    }

    public void addRobots(int count) {
        for (int i = 0; i < count; i++) {
            Robot r = new Robot(centerX, centerY);
            robots.add(r);
            List<Point> traj = new ArrayList<>();
            traj.add(new Point(r.getX(), r.getY()));
            trajectories.add(traj);
        }
    }

    public void removeRobots(int count) {
        for (int i = 0; i < count; i++) {
            if (robots.size() > 1) {
                robots.remove(robots.size() - 1);
                trajectories.remove(trajectories.size() - 1);
            }
        }
    }

    public void update(long elapsed) {
        if (gameBounds == null) {
            return;
        }

        moveTimer += elapsed;

        if (moveTimer >= MOVE_INTERVAL) {
            for (int i = 0; i < robots.size(); i++) {
                Robot robot = robots.get(i);

                if (robot.getStatus() == RobotStatus.SEARCHING) {
                    if (robot.getBattery() > 0) {
                        robot.setOldPosition();
                        robot.move();
                        robot.checkCollision(gameBounds);

                        trajectories.get(i).add(new Point(robot.getX(), robot.getY()));

                        if (button.isClicked(robot)) {
                            robot.setStatus(RobotStatus.SUCCESS);
                            success++;
                        }
                    } else {
                        robot.setStatus(RobotStatus.FAILURE);
                    }
                } else {
                    attempts++;
                    resetRobot(i);
                }
            }
            moveTimer = 0;
        }
    }

    public void resetRobot(int index) {
        Robot newRobot = new Robot(centerX, centerY);
        robots.set(index, newRobot);
        trajectories.get(index).clear();
        trajectories.get(index).add(new Point(newRobot.getX(), newRobot.getY()));
    }

    public int getAttempts() { return attempts; }
    public int getSuccess() { return success; }
    public Robot getRobot() { return robots.isEmpty() ? null : robots.get(0); }
    public List<Robot> getRobots() { return robots; }
    public Arena getGameBounds() { return gameBounds; }
    public Button getButton() { return button; }
    public List<List<Point>> getTrajectories() { return trajectories; }

    public double successProbability() {
        if (attempts == 0) return 0.0;
        return (double) success / attempts * 100;
    }
}