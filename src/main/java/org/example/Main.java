package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    GraphicsContext gc;
    GameCore gameCore = new GameCore();
    Arena arena = gameCore.getGameBounds();
    Button button = gameCore.getButton();

    private final int width = arena.getWidth();
    private final int height = arena.getHeight();
    private static final long MOVE_INTERVAL = 500; // 500 milisegundos
    private static final long FRAME_TIME = 16; // ~60 FPS en milisegundos
    Text probability;

    private long lastFrameTime = 0;

    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        BorderPane borderPane = new BorderPane();

        StackPane game = new StackPane(canvas);

        HBox hBox = new HBox(10);
        probability = new Text("Success: " + gameCore.successProbability() + "%");
        probability.setFill(Color.WHITE);
        HBox.setHgrow(probability, Priority.ALWAYS);
        hBox.setStyle("-fx-padding: 10; -fx-background-color: #171717;");
        hBox.getChildren().add(probability);

        borderPane.setTop(hBox);
        borderPane.setCenter(game);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("JavaFX Canvas");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.sizeToScene();

        startGameLoop();



    }

    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            private long moveTimer = 0;

            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return;
                }

                long elapsed = (now - lastFrameTime) / 1_000_000;
                if (elapsed >= FRAME_TIME) {
                    moveTimer += elapsed;

                    // Actualizar posición cada MOVE_INTERVAL
                    if (moveTimer >= MOVE_INTERVAL) {
                        gameCore.update(elapsed);
                        moveTimer = 0;
                    }

                    probability.setText("Success: " + gameCore.successProbability() + "%");
                    render(gc);
                    lastFrameTime = now;
                }
            }
        };
        gameLoop.start();
    }

    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);

        // Background
        gc.setFill(Color.rgb(26, 26, 28));
        gc.fillRect(0, 0, width, height);

        // Trajectory
        List<Point> trajectory = gameCore.getTrajectory();
        gc.setStroke(Color.color(1.0, 1.0, 0.0, 0.2)); // R, G, B, Alpha (0.0-1.0)
        gc.setLineWidth(2);

        for (int i = 0; i < trajectory.size() - 1; i++) {
            Point p1 = trajectory.get(i);
            Point p2 = trajectory.get(i + 1);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        // Robot
        Robot robot = gameCore.getRobot();
        gc.setFill(Color.CYAN);
        gc.fillRect(robot.getX(), robot.getY(), robot.getWidth(), robot.getHeight());

        // Button
        button = gameCore.getButton();
        gc.setFill(Color.RED);
        gc.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
    }
}
