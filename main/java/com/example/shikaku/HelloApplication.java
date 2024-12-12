package com.example.shikaku;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private static final int WINDOW_WIDTH = 400;
    private static final int CELL_WIDTH = 40;
    private static final Image backgroundImage = new Image("file:Back.png", 1400,830, false, true);

    private static final BackgroundImage backgroundImage1 = new BackgroundImage(backgroundImage,
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

    BoardView boardView;
    int n;
    int m;
    Integer[] cols;//неизменяемые как задались так и используются
    private static final Font myFont = Font.font("verdana");
    private static final Font reallyBigFont = Font.font("verdana", FontWeight.BOLD,40);
    private static final Font bigFont = Font.font("verdana", FontWeight.BOLD,15);

    private static final int windowWidth = 1400;
    private static final int windowHight = 830;

    private void buttonStyle(Button btn){
        btn.setFont(myFont);
        btn.setPadding(new Insets(10));
        btn.setStyle("-fx-font: 16px \"verdana\";\n" +
                "    -fx-padding: 10;\n" +
                "    -fx-background-color: " +
                "radial-gradient(center 50% 50%, radius 100%, #d86e3a, #E9967A);"
        );
        btn.setMinHeight(50);
    }

    //действие
    @Override
    public void start(Stage stage) throws IOException {
        HBox mainSceneInsude = new HBox();
        mainSceneInsude.setAlignment(Pos.CENTER);
        mainSceneInsude.setPadding(new Insets(10));

        VBox mainScene = new VBox();
        mainScene.setBackground(new Background(backgroundImage1));
        mainScene.setAlignment(Pos.CENTER);

        Button input = new Button("Solve");
        buttonStyle(input);
        Button play = new Button("Play game");
        buttonStyle(play);
        Button solveGenerated = new Button("Solve");
        buttonStyle(solveGenerated);
        Button checkInput = new Button("Check");
        buttonStyle(checkInput);
        Button show = new Button("Show");
        buttonStyle(show);

        Label mainLabel = new Label("Please, enter size of the field! Size should be less then 20");
        mainLabel.setFont(myFont);
        Label mainSceneWelcome = new Label("Shikaku");
        mainSceneWelcome.setFont(reallyBigFont);

        TextField nField = new TextField();
        nField.setMaxWidth(70);

        mainSceneInsude.getChildren().addAll(show, play);
        mainScene.getChildren().addAll(mainSceneWelcome,mainLabel, nField, mainSceneInsude);

        show.setOnAction(event -> showAction(nField,input,stage));

        List<CellNode> o = new ArrayList<>();
        input.setOnAction(event -> inputAction(stage, input, o));

        play.setOnAction(event -> playAction(nField, o, stage, solveGenerated, checkInput));
        HBox.setMargin(play,new Insets(10));

        Scene scene = new Scene(mainScene, windowWidth, windowHight);

        stage.setTitle("Shikaku");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void playAction(TextField nField, List<CellNode> o, Stage stage, Button solveGenerated, Button checkInput) {
        final String nText = nField.getText();
        if(!nText.equals("")) {
            try {
                n = Integer.parseInt(nText);

                cols = new Integer[n * n];
                for (int i = 0; i < n * n; i++) {
                    cols[i] = i;
                }

                VBox playRoot = new VBox();
                playRoot.setAlignment(Pos.CENTER);
                HBox playHRoot = new HBox(playRoot);
                playHRoot.setAlignment(Pos.CENTER);
                GameView gameView = new GameView(n);
                playRoot.getChildren().add(gameView.getPlayPane());
                playRoot.getChildren().add(solveGenerated);
                playRoot.getChildren().add(checkInput);
                playRoot.setSpacing(10);
                playHRoot.setBackground(new Background(backgroundImage1));
                Scene playScene = new Scene(playHRoot, windowWidth, windowHight);
                playHRoot.setOnDragDetected(
                        eventMouse -> {
                            if (eventMouse.getButton() == MouseButton.PRIMARY) {
                                eventMouse.consume();
                                playHRoot.startFullDrag();
                            }
                        });

                stage.setScene(playScene);
                stage.show();

                solveGenerated.setOnAction(event2 -> solveGeneratedAction(gameView,stage,o));

                checkInput.setOnAction(checkEvent -> checkInputAction(gameView, stage, o));

            } catch (NumberFormatException e) {
                //mainLabel.setText("You can enter only numbers!");
                System.out.println("Exeption string should be number!");
            }

        }
    }

    private void inputAction(Stage stage, Button input, List<CellNode> o) {
        try {
            Matrix matrix = new Matrix(boardView.getBoard(), n, m);
            //if(board.createMatrix()) {
            matrix.createMatrix();
                DLX.solve(DLX.buildSparseMatrix(matrix.getMatr(), cols), 0, o);
                HBox root2 = new HBox();
                Node DLXResultPane = boardView.getLabelPane(DLX.getResult(), boardView.getBoard());
                root2.getChildren().addAll(DLXResultPane);

                root2.setSpacing(50);
                root2.setBackground(Background.EMPTY);
                root2.setAlignment(Pos.CENTER);

                VBox rootHelper = new VBox(root2);
                rootHelper.setAlignment(Pos.CENTER);
                rootHelper.setBackground(new Background(backgroundImage1));
                Scene newScene = new Scene(rootHelper, windowWidth, windowHight);

                stage.setScene(newScene);
                stage.show();
            //}
//            else {
//                input.setBackground(new Background
//                        (new BackgroundFill(Color.RED,
//                                new CornerRadii(3.0), Insets.EMPTY)));
//            }
        } catch (NumberFormatException e) {
            //mainLabel.setText("You can enter only numbers!");
            System.out.println("Exeption string should be number!");
        }
        catch (NotPositiveNumber e){
            //mainLabel.setText("Number should be >0");
        }

    }

    private void solveGeneratedAction(GameView gameView, Stage stage, List<CellNode> o) {
        //Board board = new Board(gameView.gen.field,n,n);
        //board.createMatrix();
        //DLX.solve(DLX.buildSparseMatrix(board.createMatrix(), cols), 0, o);
        DLX.solvePuzzle(gameView, n, cols, o);

        HBox root2 = new HBox();
        boardView = new BoardView(n, n);
        Node DLXResultPane = boardView.getLabelPane(DLX.getResult(),gameView.gen.field);
        root2.getChildren().addAll( DLXResultPane);

        root2.setSpacing(50);
        root2.setBackground(Background.EMPTY);
        root2.setAlignment(Pos.CENTER);

        VBox rootHelper = new VBox(root2);
        rootHelper.setAlignment(Pos.CENTER);
        rootHelper.setBackground(new Background(backgroundImage1));
        Scene newScene = new Scene(rootHelper, windowWidth, windowHight);

        stage.setScene(newScene);
        stage.show();
    }

    private void showAction(TextField nField, Button input, Stage stage) {
        final String nText = nField.getText();

        if(!nText.equals("")){
            try {
                BorderPane inputGameRoot = new BorderPane();
                //mainLabel.setText("Enter the pussle and press \"Solve\" ");
                n= Integer.parseInt(nText);
                if(n<=0){
                    //mainLabel.setText("Number should be >0");
                }
                else if(n>20){
                    //mainLabel.setText("Number should be <20");
                }
                else {
                    m = n;
                    boardView = new BoardView(n, m);
                    VBox boardBox = new VBox(boardView.getBoardPane());
                    boardBox.getChildren().add(input);
                    boardBox.setAlignment(Pos.CENTER);
                    inputGameRoot.setCenter(boardBox);
                    cols = new Integer[m * n];
                    for (int i = 0; i < m * n; i++) {
                        cols[i] = i;
                    }
                    Scene playScene = new Scene(inputGameRoot, 1400, 830);
                    inputGameRoot.setBackground(new Background(backgroundImage1));
                    stage.setScene(playScene);
                    stage.show();
                }
            } catch (NumberFormatException e) {
                //mainLabel.setText("You should enter only numbers!");
                System.out.println("Exeption string should be number!");
            }
        }
    }

    private void checkInputAction(GameView gameView, Stage stage, List<CellNode> o) {
        if (Generation.isSolved(gameView.inputRectangles)) {
            Label win = new Label("You won!");
            win.setFont(reallyBigFont);
            VBox winBox = new VBox(win);
            winBox.setBackground(new Background(backgroundImage1));
            winBox.setAlignment(Pos.CENTER);
            Scene wonScene = new Scene(winBox, 1400,830);
            stage.setScene(wonScene);
            stage.show();
        }
        else {
            //наверняка дублирование с solveGenerated
            Matrix matrix = new Matrix(gameView.gen.field,n,n);
            //board.createMatrix();
            DLX.solve(DLX.buildSparseMatrix(matrix.createMatrix(), cols), 0, o);
            boardView = new BoardView(n, n);
            Node DLXResultPane = boardView.getLabelPane(DLX.getResult(),gameView.gen.field);

            Label lose = new Label("You lose!");
            lose.setFont(reallyBigFont);
            VBox loseBox = new VBox(lose);

            loseBox.getChildren().add(DLXResultPane);
            loseBox.setBackground(new Background(backgroundImage1));
            loseBox.setAlignment(Pos.CENTER);
            Scene loseScene = new Scene(loseBox, 1400,830);
            stage.setScene(loseScene);
            stage.show();
        }
    }
}