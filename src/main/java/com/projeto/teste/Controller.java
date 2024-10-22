package com.projeto.teste;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

public class Controller {
    @FXML
    private HBox hbox;
    @FXML
    private RadioButton bubble;
    @FXML
    private RadioButton merge;
    @FXML
    private TextField textField;

    private static final int RECTANGLE_WIDTH = 49;
    private static final int RECTANGLE_STROKE = 1;
    private static final int RECTANGLE_HEIGHT_FACTOR = 20;
    private int numRectangles;

    private SequentialTransition sequentialTransition;
    private boolean isPaused = false;

    @FXML
    private void initialize() {

    }

    @FXML
    private void start() {
        hbox.getChildren().clear();
        int[] heights = getHeights();

        List<StackPane> stackPanes = createStackPanes(heights);
        hbox.getChildren().addAll(stackPanes);
        if (bubble.isSelected()) {
            bubbleSortAnimation(stackPanes);
        }
    }
    @FXML
    private void pause(){
        if (isPaused) {
            sequentialTransition.play(); // Retorna a animação ao estado normal
            isPaused = false;
        } else {
            sequentialTransition.pause(); // Pausa a animação
            isPaused = true;
        }
    }

    private int[] getHeights() {
        if (textField.getText().isEmpty()) {
            numRectangles = 13;
            return generateRandomHeights(numRectangles);
        } else {
            return parseHeightsFromInput(textField.getText());
        }
    }

    private int[] generateRandomHeights(int numRectangles) {
        int[] heights = new int[numRectangles];
        for (int i = 0; i < numRectangles; i++) {
            heights[i] = (int) (Math.random() * RECTANGLE_HEIGHT_FACTOR) + 1;
        }
        return heights;
    }

    private int[] parseHeightsFromInput(String input) {
        String[] numbers = input.split(",");
        numRectangles = numbers.length;
        int[] heights = new int[numRectangles];
        for (int i = 0; i < numRectangles; i++) {
            heights[i] = Integer.parseInt(numbers[i].trim());
        }
        return heights;
    }

    private List<StackPane> createStackPanes(int[] heights) {
        List<StackPane> stackPanes = new ArrayList<>();
        for (int height : heights) {
            StackPane stackPane = new StackPane();
            Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, height * RECTANGLE_HEIGHT_FACTOR);
            rectangle.setFill(Color.BLUE);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(1);

            Text text = new Text(String.valueOf(height));
            text.setFill(Color.WHITE);

            stackPane.getChildren().addAll(rectangle, text);
            stackPanes.add(stackPane);
        }
        return stackPanes;
    }
    private void bubbleSortAnimation(List<StackPane> stackPanes) {
        this.sequentialTransition = new SequentialTransition();

        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < stackPanes.size() - 1; i++) {
                Text text1 = (Text) stackPanes.get(i).getChildren().get(1);
                Text text2 = (Text) stackPanes.get(i + 1).getChildren().get(1);
                if (Integer.parseInt(text1.getText()) > Integer.parseInt(text2.getText())) {
                    swapWithAnimation(stackPanes, i, i + 1);
                    Collections.swap(stackPanes, i, i + 1);
                    swapped = true;
                }
            }
        } while (swapped);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(event -> {
            hbox.getChildren().clear();
            hbox.getChildren().addAll(stackPanes);
            stackPanes.forEach(stackPane -> {
                stackPane.setTranslateX(0);
                stackPane.setTranslateY(0);
            });
        });

        sequentialTransition.setOnFinished(event -> pauseTransition.play());
        sequentialTransition.play();
    }

    private void swapWithAnimation(List<StackPane> stackPanes, int index1, int index2) {
        StackPane pane1 = stackPanes.get(index1);
        StackPane pane2 = stackPanes.get(index2);

        // Define as transições
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(0.5), pane1);
        transition1.setByX(RECTANGLE_WIDTH + 1);

        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), pane2);
        transition2.setByX(-(RECTANGLE_WIDTH + 1));

        // Adiciona ao seqTransition
        ParallelTransition parallelTransition = new ParallelTransition(transition1,transition2);
        sequentialTransition.getChildren().addAll(parallelTransition);
    }

}

