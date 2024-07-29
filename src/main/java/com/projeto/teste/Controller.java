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
    private TextField inputField;

    private static int NUM_RECTANGLES;
    private static int RECTANGLE_WIDTH = 49;
    private static int RECTANGLE_HEIGHT_FACTOR = 20;

    public static void setNumRectangles(int numRectangles) {
        NUM_RECTANGLES = numRectangles;
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void start() {
        hbox.getChildren().clear();

        int[] heights;
        if (inputField.getText().isEmpty()) {
            setNumRectangles(13);
            heights = new int[NUM_RECTANGLES];
            for (int i = 0; i < NUM_RECTANGLES; i++) {
                heights[i] = (int) (Math.random() * RECTANGLE_HEIGHT_FACTOR) + 1;
            }
            List<StackPane> stackPanes = createStackPanes(heights);
            if (bubble.isSelected()) {
                bubbleSortAnimation(stackPanes);
            } else if (merge.isSelected()) {
                selectionSortAnimation(stackPanes);
            }
        } else {
            String input = inputField.getText();
            String[] numbers = input.split(",");
            heights = new int[numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                heights[i] = Integer.parseInt(numbers[i].trim());
            }
            setNumRectangles(numbers.length);
            List<StackPane> stackPanes = createStackPanes(heights);
            if (bubble.isSelected()) {
                bubbleSortAnimation(stackPanes);
            } else if (merge.isSelected()) {
                selectionSortAnimation(stackPanes);
            }
        }

    }

    private List<StackPane> createStackPanes(int[] heights) {
        List<StackPane> stackPanes = new ArrayList<>();
        for (int i = 0; i < NUM_RECTANGLES; i++) {
            StackPane stackPane = new StackPane();
            Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, heights[i] * RECTANGLE_HEIGHT_FACTOR);
            rectangle.setFill(Color.BLUE);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(1);

            Text text = new Text(String.valueOf(heights[i]));
            text.setFill(Color.WHITE);

            stackPane.getChildren().addAll(rectangle, text);
            hbox.getChildren().add(stackPane);

            stackPanes.add(stackPane);
        }
        return stackPanes;
    }

    private void bubbleSortAnimation(List<StackPane> stackPanes) {
        SequentialTransition seqTransition = new SequentialTransition();

        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < stackPanes.size() - 1; i++) {
                Text text1 = (Text) stackPanes.get(i).getChildren().get(1);
                Text text2 = (Text) stackPanes.get(i + 1).getChildren().get(1);
                if (Integer.parseInt(text1.getText()) > Integer.parseInt(text2.getText())) {
                    swapWithAnimation(stackPanes.get(i), stackPanes.get(i + 1), seqTransition);
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

        seqTransition.setOnFinished(event -> pauseTransition.play());
        seqTransition.play();
    }

    private void swapWithAnimation(StackPane pane1, StackPane pane2, SequentialTransition seqTransition) {
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(0.5), pane1);
        transition1.setByX(50);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), pane2);
        transition2.setByX(-50);

        seqTransition.getChildren().addAll(transition1, transition2);
    }

    private void selectionSortAnimation(List<StackPane> stackPanes) {
        SequentialTransition seqTransition = new SequentialTransition();

        for (int i = 0; i < stackPanes.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < stackPanes.size(); j++) {
                Text text1 = (Text) stackPanes.get(j).getChildren().get(1);
                Text text2 = (Text) stackPanes.get(minIndex).getChildren().get(1);
                if (Integer.parseInt(text1.getText()) < Integer.parseInt(text2.getText())) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swapWithAnimation(stackPanes, i, minIndex, seqTransition);
                Collections.swap(stackPanes, i, minIndex);
            }
        }

        seqTransition.play();
    }

    private void swapWithAnimation(List<StackPane> stackPanes, int index1, int index2, SequentialTransition seqTransition) {
        StackPane pane1 = stackPanes.get(index1);
        StackPane pane2 = stackPanes.get(index2);

        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(0.5), pane1);
        transition1.setByX(50 * (index2 - index1));
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), pane2);
        transition2.setByX(-50 * (index2 - index1));

        seqTransition.getChildren().addAll(transition1, transition2);
    }
}
