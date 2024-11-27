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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private static final int WINDOW_WIDTH = 400;
    private static final int CELL_WIDTH = 40;

    BoardView boardView;
    int n;
    int m;
    Integer[] cols;
    Font myFont = Font.font("verdana");

    private void buttonStyle(Button btn){
        btn.setFont(myFont);
        btn.setPadding(new Insets(10));
//        btn.setBackground(new Background((new BackgroundFill(Color.LAVENDER,
//               new CornerRadii(3), new Insets(5)))));
        btn.setStyle("-fx-font: 16px \"verdana\";\n" +
                "    -fx-padding: 10;\n" +
                "    -fx-background-color: " +
                "radial-gradient(center 50% 50%, radius 100%, #d86e3a, #E9967A);"
        );
        btn.setMinHeight(50);
//        btn.setBorder(new Border(new BorderStroke(Color.LAVENDER, BorderStrokeStyle.SOLID,
//                new CornerRadii(2), new BorderWidths(2))));
    }

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //AtomicInteger n= new AtomicInteger(5); //строки
        //AtomicInteger m= new AtomicInteger(5); //колонки

        //System.loadLibrary("jniortools");

        Font bigFont = Font.font("verdana", FontWeight.BOLD,15);
        Font reallyBigFont = Font.font("verdana", FontWeight.BOLD,40);
        Image backgroundImage = new Image("file:Back.png", 1400,830, false, true);
        BackgroundImage backgroundImage1 = new BackgroundImage(backgroundImage,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        int winWidth = CELL_WIDTH + 150;
        int winHight = CELL_WIDTH + 150;
        if(winHight < 800) winHight = 830;//830
        if(winWidth < 800) winWidth = 1400;


        HBox mainSceneInsude = new HBox();
        VBox mainScene = new VBox();

        BorderPane root = new BorderPane();
        BorderPane.setMargin(root,new Insets(10,10,10,10));
        //VBox box = new VBox();
        //box.setAlignment(Pos.CENTER);

        Button input = new Button("Solve");
        buttonStyle(input);
        Button play = new Button("Play game");
        buttonStyle(play);
        Button solveGenerated = new Button("Solve");
        buttonStyle(solveGenerated);
        Button checkInput = new Button("Check");
        buttonStyle(checkInput);

        Label mainLabel = new Label("Please, enter size of the field! Size should be less then 20");
        mainLabel.setFont(bigFont);
        //mainLabel.setTextFill(Color.DARKSALMON);
        HBox infoBox = new HBox(mainLabel);
        infoBox.setPadding(new Insets(10,0,0,0));
        infoBox.setAlignment(Pos.CENTER);
        root.setTop(infoBox);

        Label nLabel = new Label("Enter the number of strings");
        nLabel.setFont(myFont);
        TextField nField = new TextField();
        Button show = new Button("Show");
        buttonStyle(show);
        //box.getChildren().addAll(nLabel,nField, show, play);
        //VBox.setMargin(show, new Insets(10,0,10,0));

        mainSceneInsude.getChildren().addAll(show, play);
        Label mainSceneWelcome = new Label("Shikaku");
        mainSceneWelcome.setFont(reallyBigFont);
        nField.setMaxWidth(70);
        //nField.setB
        mainScene.getChildren().addAll(mainSceneWelcome, nField, mainSceneInsude);

        final String[] nText = new String[1];
        show.setOnAction(event -> {
            nText[0] = nField.getText();

            if(!nText[0].equals("")){
                try {
                    BorderPane inputGameRoot = new BorderPane();

                    mainLabel.setText("Enter the pussle and press \"Solve\" ");
                    n= Integer.parseInt(nText[0]);
                    if(n<=0){
                        mainLabel.setText("Number should be >0");
                    }
                    else if(n>20){
                        mainLabel.setText("Number should be <20");
                    }
                    else {
                        m = n;
                        boardView = new BoardView(n, m);
                        VBox boardBox = new VBox(boardView.getBoardPane());
                        boardBox.getChildren().add(input);
                        boardBox.setAlignment(Pos.CENTER);
                        inputGameRoot.setCenter(boardBox);
//                        if (!box.getChildren().contains(input)) {
//                            box.getChildren().add(input);
//                        }
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
                    mainLabel.setText("You should enter only numbers!");
                    System.out.println("Exeption string should be number!");
                }
            }
        });

        //root.setLeft(box);
        root.setPadding(new Insets(10));


        List<CellNode> o = new ArrayList<>();

        int finalWinWidth = winWidth;
        int finalWinHight = winHight;
        input.setOnAction(event -> {
            try {
                Board board = new Board(boardView.getBoard(), n, m);
                //board.printField();
                long startTimeDLX = System.currentTimeMillis();
                if(board.createMatrix()) {

                    DLX.solve(DLX.buildSparseMatrix(board.getMatr(), cols), 0, o);


                    //Cell h = Backtracking.createField(boardView.getBoard());
                    //Backtracking.printField(h);
                    //Backtracking.solve(h.next, board);


                    HBox root2 = new HBox();
                    Node DLXResultPane = boardView.getLabelPane(DLX.getResult(), boardView.getBoard());
                    //Node BactrackingResultPane = boardView.getLabelPane(Backtracking.getResult(),boardView.getBoard());
                    root2.getChildren().addAll(DLXResultPane);

                    root2.setSpacing(50);
                    root2.setBackground(Background.EMPTY);
                    root2.setAlignment(Pos.CENTER);

                    VBox rootHelper = new VBox(root2);
                    rootHelper.setAlignment(Pos.CENTER);
                    rootHelper.setBackground(new Background(backgroundImage1));
                    Scene newScene = new Scene(rootHelper, finalWinWidth, finalWinHight);

                    stage.setScene(newScene);
                    stage.show();
                }
                else {
                    input.setBackground(new Background
                            (new BackgroundFill(Color.RED,
                                    new CornerRadii(3.0), Insets.EMPTY)));
                }
            } catch (NumberFormatException e) {
                mainLabel.setText("You can enter only numbers!");
                System.out.println("Exeption string should be number!");
            }
            catch (NotPositiveNumber e){
                mainLabel.setText("Number should be >0");
            }

        });


        play.setOnAction(event -> {
            nText[0] = nField.getText();
            if(!nText[0].equals("")) {
                try {
                    n = Integer.parseInt(nText[0]);

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
                    Scene playScene = new Scene(playHRoot, finalWinWidth, finalWinHight);
                    playHRoot.setOnDragDetected(
                            eventMouse -> {
                                if (eventMouse.getButton() == MouseButton.PRIMARY) {
                                    eventMouse.consume();
                                    playHRoot.startFullDrag();
                                }
                            });

                    stage.setScene(playScene);
                    stage.show();

                    solveGenerated.setOnAction(event2 -> {
                        Board board = new Board(gameView.gen.field,n,n);

                        // long startTimeDLX = System.currentTimeMillis();
                        board.createMatrix();
                        // long matrTimeDLX = System.currentTimeMillis();
                        DLX.solve(DLX.buildSparseMatrix(board.getMatr(), cols), 0, o);
                        //long endTimeDLX = System.currentTimeMillis();
                        // long timeSveden = matrTimeDLX - startTimeDLX;
                        // long timeElapsedDLX = endTimeDLX - startTimeDLX;
                        // System.out.println("SVEDENIE: "+ timeSveden);
                        // System.out.println("DLX TIME: "+ timeElapsedDLX);


                        HBox root2 = new HBox();
                        boardView = new BoardView(n, n);
                        Node DLXResultPane = boardView.getLabelPane(DLX.getResult(),gameView.gen.field);
                        //Node BactrackingResultPane = boardView.getLabelPane(Backtracking.getResult());
                        root2.getChildren().addAll( DLXResultPane);

                        root2.setSpacing(50);
                        root2.setBackground(Background.EMPTY);
//                        root2.setBackground(new Background((new BackgroundFill(Color.IVORY,
//                                CornerRadii.EMPTY, Insets.EMPTY))));
                        root2.setAlignment(Pos.CENTER);

                        VBox rootHelper = new VBox(root2);
                        rootHelper.setAlignment(Pos.CENTER);
                        rootHelper.setBackground(new Background(backgroundImage1));
                        Scene newScene = new Scene(rootHelper, finalWinWidth, finalWinHight);

                        stage.setScene(newScene);
                        stage.show();
                    });

                    checkInput.setOnAction(checkEvent -> {
                        if (Generation.isSolved(gameView.inputRectangles)) {
                            //System.out.println("YES!");
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
                            //System.out.println("NO");

                            Board board = new Board(gameView.gen.field,n,n);
                            board.createMatrix();
                            DLX.solve(DLX.buildSparseMatrix(board.getMatr(), cols), 0, o);
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
                    });

                } catch (NumberFormatException e) {
                    mainLabel.setText("You can enter only numbers!");
                    System.out.println("Exeption string should be number!");
                }

            }
        });



        //root.setBottom(input);
        root.setBackground(new Background(backgroundImage1)); //new Background((new BackgroundFill(Color.IVORY,
        //CornerRadii.EMPTY, Insets.EMPTY)))




        //Scene scene = new Scene(root, winWidth, winHight);
        mainScene.setBackground(new Background(backgroundImage1));
        mainSceneInsude.setAlignment(Pos.CENTER);
        mainSceneInsude.setPadding(new Insets(10));
        HBox.setMargin(play,new Insets(10));
        mainScene.setAlignment(Pos.CENTER);
        Scene scene = new Scene(mainScene, winWidth, winHight);
        //scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Shikaku");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}