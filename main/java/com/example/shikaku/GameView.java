package com.example.shikaku;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameView {
    private static final int CELL_WIDTH = 35; //15
    TilePane pane = new TilePane();
    Double chosenAreaXMin = Double.MAX_VALUE;
    Double chosenAreaXMax = -1.0;
    Double chosenAreaYMax = -1.0;
    Double chosenAreaYMin = Double.MAX_VALUE;

    //List<List<Integer>> inputMatr = new ArrayList<>();
    List<RectangleOnField> inputRectangles = new ArrayList<>();

    Integer counterRect =0;

    ObservableList<Node> list;
    List<GameCell> gameCellsList = new ArrayList<>();
    int n;
    Generation gen;
    public static final Color[] colors = new Color[]{Color.ROYALBLUE, Color.YELLOW, Color.AQUAMARINE, Color.LIGHTCORAL,
            Color.FUCHSIA, Color.RED, Color.FORESTGREEN, Color.BISQUE, Color.GOLD, Color.BROWN,
            Color.CRIMSON, Color.YELLOWGREEN, Color.DIMGREY, Color.PERU, Color.SADDLEBROWN, Color.PLUM,
            Color.POWDERBLUE, Color.MEDIUMVIOLETRED, Color.DARKVIOLET, Color.LIGHTSLATEGRAY, Color.DARKSALMON
    };
    int colorsCounter=0;

    GameView(int n){
        this.n = n;
        gen = new Generation(n);
        pane.setPrefColumns(n);
        pane.setPrefTileWidth(CELL_WIDTH); //(400-20)/n
        pane.setPrefTileHeight(CELL_WIDTH);
        pane.setMaxWidth(Region.USE_PREF_SIZE);
        setPaneElements();
    }

    void mouse(MouseDragEvent event){
        event.consume();
        RectangleOnField r = new RectangleOnField();
        List<Integer> inputRect = Stream.generate(() -> 0)
                .limit(n*n)
                .collect(Collectors.toCollection(ArrayList::new));
        r.inputRectangle = inputRect;
        int num =0;
        for(GameCell rectCell: gameCellsList){
            double rectX = rectCell.rectangle.getX();
            double rectY = rectCell.rectangle.getY();
            if(rectX >= chosenAreaXMin && rectX <= chosenAreaXMax
                    && rectY >= chosenAreaYMin && rectY <= chosenAreaYMax){
                rectCell.rectangle.setFill(colors[colorsCounter]);
                inputRect.set((int)(rectY*n+rectX), 1);
                String rectTextStr = rectCell.text.getText();
                if(!Objects.equals(rectTextStr, "")
                ){
                    int sdh = Integer.parseInt(rectCell.text.getText());
                    r.numCounter++;
                    num = sdh;
                    r.num=num;
                }
            }
        }
        inputRectangles.add(r);
        colorsCounter++;
        if(colorsCounter==colors.length){
            colorsCounter =0;
        }
        //inputMatr.add(inputRect);
        chosenAreaXMax = -1.0;
        chosenAreaXMin = Double.MAX_VALUE;
        chosenAreaYMax = -1.0;
        chosenAreaYMin = Double.MAX_VALUE;
    }

    private void mouseEnteredEvent(Event event, Rectangle cell, GameCell gameCell) {
        {
            event.consume();
            cell.setFill(colors[colorsCounter]);
            Double cellX = cell.getX();
            if(cellX<chosenAreaXMin)
                chosenAreaXMin = cellX;
            if(cellX > chosenAreaXMax)
                chosenAreaXMax = cellX;
            Double cellY = cell.getY();
            if(cellY<chosenAreaYMin)
                chosenAreaYMin = cellY;
            if(cellY > chosenAreaYMax)
                chosenAreaYMax = cellY;
            gameCell.recNumber = counterRect;
        }
    }

    private void mouseDraggedEvent(Event event) {
        {
            event.consume();
            RectangleOnField r = new RectangleOnField();
            List<Integer> inputRect = Stream.generate(() -> 0)
                    .limit(n*n)
                    .collect(Collectors.toCollection(ArrayList::new));
            r.inputRectangle = inputRect;
            int num =0;
            for(GameCell rectCell: gameCellsList){
                double rectX = rectCell.rectangle.getX();
                double rectY = rectCell.rectangle.getY();
                if(rectX >= chosenAreaXMin && rectX <= chosenAreaXMax
                        && rectY >= chosenAreaYMin && rectY <= chosenAreaYMax){
                    rectCell.rectangle.setFill(colors[colorsCounter]);
                    inputRect.set((int)(rectY*n+rectX), 1);
                    String rectTextStr = rectCell.text.getText();
                    if(!Objects.equals(rectTextStr, "")
                    ){
                        int sdh = Integer.parseInt(rectCell.text.getText());
                        r.numCounter++;
                        num = sdh;
                        r.num=num;
                    }
                }
            }
            inputRectangles.add(r);
            colorsCounter++;
            if(colorsCounter==colors.length){
                colorsCounter =0;
            }
            //inputMatr.add(inputRect);
            chosenAreaXMax = -1.0;
            chosenAreaXMin = Double.MAX_VALUE;
            chosenAreaYMax = -1.0;
            chosenAreaYMin = Double.MAX_VALUE;
        }
    }

    private void mouseReleasedEvent(Event event) {
        {
            event.consume();
            RectangleOnField r = new RectangleOnField();
            List<Integer> inputRect = Stream.generate(() -> 0)
                    .limit(n*n)
                    .collect(Collectors.toCollection(ArrayList::new));
            r.inputRectangle = inputRect;
            int num =0;
            for(GameCell rectCell: gameCellsList){
                double rectX = rectCell.rectangle.getX();
                double rectY = rectCell.rectangle.getY();
                if(rectX >= chosenAreaXMin && rectX <= chosenAreaXMax
                        && rectY >= chosenAreaYMin && rectY <= chosenAreaYMax){
                    rectCell.rectangle.setFill(colors[colorsCounter]);
                    inputRect.set((int)(rectY*n+rectX), 1);
                    String rectTextStr = rectCell.text.getText();
                    if(!Objects.equals(rectTextStr, "")
                    ){
                        int sdh = Integer.parseInt(rectCell.text.getText());
                        r.numCounter++;
                        num = sdh;
                        r.num=num;
                    }
                }
            }
            inputRectangles.add(r);
            colorsCounter++;
            if(colorsCounter==colors.length){
                colorsCounter =0;
            }
            chosenAreaXMax = -1.0;
            chosenAreaXMin = Double.MAX_VALUE;
            chosenAreaYMax = -1.0;
            chosenAreaYMin = Double.MAX_VALUE;
        }
    }

    void setPaneElements() {
        gen.generateField();
        List<List<Integer>> generatedNumbers = gen.getField();
        list = pane.getChildren();
        for (int i = 0; i < n ; ++i) {
            for(int j=0; j<n; j++) {
                Rectangle cell = new Rectangle();
                cell.setX(j);
                cell.setY(i);
                cell.setFill(Color.LAVENDER);
                cell.setStroke(Color.DARKSALMON);
                GameCell gameCell = new GameCell(cell,generatedNumbers.get(i).get(j));
                cell.setOnMouseDragEntered(
                        event -> mouseEnteredEvent(event, cell, gameCell));
                cell.setOnMouseDragReleased(
                        this::mouseDraggedEvent
                );
                gameCell.text.setOnMouseDragReleased(this::mouseReleasedEvent);
                cell.setWidth(CELL_WIDTH);
                cell.setHeight(CELL_WIDTH);
                list.add(gameCell.stackPane);
                gameCellsList.add(gameCell);
            }
        }
    }

    Node getPlayPane(){
        return pane;
    }

}

class GameCell{
    Rectangle rectangle;
    Integer recNumber = null;
    Text text;
    StackPane stackPane;

    GameCell(Rectangle rectangle, Integer num){
        this.rectangle = rectangle;
        if(num!=0) {
            this.text = new Text(num.toString());
        }
        else this.text = new Text("");
        stackPane = new StackPane(rectangle, text);
        //text.setOnMouseDragReleased();
    }

    @Override
    public String toString(){
        return text.getText();
    }

}

class CheckingRectangle{
    List<Integer> inputRectangle;
    int numCounter = 0;
    int num = 0;
}
