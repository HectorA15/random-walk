package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private static final long FRAME_TIME = 16;
    Text probability;

    private long lastFrameTime = 0;

    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        BorderPane borderPane = new BorderPane();
        StackPane game = new StackPane(canvas);

        game.setStyle("-fx-background-color: black;");

        game.widthProperty().addListener((obs, oldVal, newVal) -> {
            double scaleX = newVal.doubleValue() / width;
            double scaleY = game.getHeight() / height;
            double scale = Math.min(scaleX, scaleY);
            canvas.setScaleX(scale);
            canvas.setScaleY(scale);
        });

        game.heightProperty().addListener((obs, oldVal, newVal) -> {
            double scaleX = game.getWidth() / width;
            double scaleY = newVal.doubleValue() / height;
            double scale = Math.min(scaleX, scaleY);
            canvas.setScaleX(scale);
            canvas.setScaleY(scale);
        });

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.TOP_CENTER);
        probability = new Text("Attempts: 0 | Success: 0 | Probability: 0.00%");
        probability.setFill(Color.WHITE);
        hBox.setStyle("-fx-padding: 10; -fx-background-color: black;");
        hBox.getChildren().add(probability);

        borderPane.setTop(hBox);
        borderPane.setCenter(game);

        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("Simulación Montecarlo");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(true);
        primaryStage.show();


        startGameLoop();
    }

    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return;
                }

                long elapsed = (now - lastFrameTime) / 1_000_000;
                if (elapsed >= FRAME_TIME) {
                    gameCore.update(elapsed);

                    probability.setText(String.format("Attempts: %d | Success: %d | Probability: %.2f%%",
                            gameCore.getAttempts(),
                            gameCore.getSuccess(),
                            gameCore.successProbability()));
                    probability.setStyle( "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                    render(gc);
                    lastFrameTime = now;
                }
            }
        };
        gameLoop.start();
    }

    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        int marginThick = 2;
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(marginThick);
        gc.strokeRect(marginThick / 2.0, marginThick / 2.0, width - marginThick, height - marginThick);

        List<Point> trajectory = gameCore.getTrajectory();
        gc.setStroke(Color.color(1.0, 1.0, 0.0, 0.4));
        gc.setLineWidth(2);

        for (int i = 0; i < trajectory.size() - 1; i++) {
            Point p1 = trajectory.get(i);
            Point p2 = trajectory.get(i + 1);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        button = gameCore.getButton();
        gc.setFill(Color.RED);
        gc.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());

        Robot robot = gameCore.getRobot();
        gc.setFill(Color.CYAN);
        double renderX = robot.getX() - (robot.getWidth() / 2.0);
        double renderY = robot.getY() - (robot.getHeight() / 2.0);
        gc.fillRect(renderX, renderY, robot.getWidth(), robot.getHeight());
    }
}