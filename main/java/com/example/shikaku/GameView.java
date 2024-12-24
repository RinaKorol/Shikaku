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

import static com.example.shikaku.Utils.createFilledList;
import static com.example.shikaku.Utils.setNum;

public class GameView {
    private static final int CELL_WIDTH = 35; //15
    TilePane pane = new TilePane();
    Double chosenAreaXMin = Double.MAX_VALUE;
    Double chosenAreaXMax = -1.0;
    Double chosenAreaYMax = -1.0;
    Double chosenAreaYMin = Double.MAX_VALUE;



    Integer counterRect =0;

    ObservableList<Node> list;
    List<GameCell> gameCellsList = new ArrayList<>();
    int n;
    public static final Color[] colors = new Color[]{Color.ROYALBLUE, Color.YELLOW, Color.AQUAMARINE, Color.LIGHTCORAL,
            Color.FUCHSIA, Color.RED, Color.FORESTGREEN, Color.BISQUE, Color.GOLD, Color.BROWN,
            Color.CRIMSON, Color.YELLOWGREEN, Color.DIMGREY, Color.PERU, Color.SADDLEBROWN, Color.PLUM,
            Color.POWDERBLUE, Color.MEDIUMVIOLETRED, Color.DARKVIOLET, Color.LIGHTSLATEGRAY, Color.DARKSALMON
    };
    int colorsCounter=0;

    GameView(int n, final Field field){
        this.n = n;
        pane.setPrefColumns(n);
        pane.setPrefTileWidth(CELL_WIDTH); //(400-20)/n
        pane.setPrefTileHeight(CELL_WIDTH);
        pane.setMaxWidth(Region.USE_PREF_SIZE);
        setPaneElements(field);
    }

    void mouse(MouseDragEvent event){
        event.consume();
        RectangleOnField r = new RectangleOnField();
        List<Integer> inputRect = createFilledList(setNum.apply(0),n);
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

    private void mouseDraggedEvent(Event event, Field field) {
        {
            event.consume();
           savePersonsMove(field);
        }
    }

    private void savePersonsMove(Field field) {
        List<Integer> inputRect = createFilledList(setNum.apply(0),n);;
        int numCounter = 0, num = 0;
        for(GameCell rectCell: gameCellsList){
            double rectX = rectCell.rectangle.getX();
            double rectY = rectCell.rectangle.getY();
            if(isCellInArea(rectX, rectY, chosenAreaXMin, chosenAreaXMax, chosenAreaYMin, chosenAreaYMax)){
                rectCell.rectangle.setFill(colors[colorsCounter]);
                inputRect.set((int)(rectY*n+rectX), 1);
                int[] res =countHowMuchNumers(rectCell, numCounter, num);
                numCounter = res[0];
                num = res[1];
            }
        }
        field = field.addInputRectangleToField(field, inputRect, numCounter, num);
        colorsCounter = newColorsCounter(colorsCounter);
        chosenAreaXMax = -1.0;
        chosenAreaXMin = Double.MAX_VALUE;
        chosenAreaYMax = -1.0;
        chosenAreaYMin = Double.MAX_VALUE;
    }

    private boolean isCellInArea(double rectX, double rectY, double chosenAreaXMin, double chosenAreaXMax,
                                 double chosenAreaYMin, double chosenAreaYMax) {
        return rectX >= chosenAreaXMin && rectX <= chosenAreaXMax
                && rectY >= chosenAreaYMin && rectY <= chosenAreaYMax;
    }

    private int[] countHowMuchNumers(GameCell rectCell, int numCounter, int num) {
        String rectTextStr = rectCell.text.getText();
        if(!Objects.equals(rectTextStr, "")
        ){
            int numCounterNew = numCounter;
            int numNew = num;
            int[] res = numberPassed(rectCell, numCounterNew, numNew);
            numCounterNew = res[0];
            numNew = res[1];
            return new int[]{numCounterNew, numNew};
        }
        return new int[]{numCounter, num};
    }

    private int newColorsCounter( int colorsCounter){
        int c = colorsCounter;
        c++;
        if(c==colors.length){
            c =0;
        }
        return c;
    }

    private int[] numberPassed(GameCell rectCell, int numCounter, int num) {
        int numCounterNew = numCounter;
        int numNew = num;
        int sdh = Integer.parseInt(rectCell.text.getText());
        numCounterNew++;
        numNew = sdh;
        return new int[]{numCounterNew, numNew};
    }


    private void mouseReleasedEvent(Event event, Field field) {
        {
            event.consume();
            savePersonsMove(field);
        }
    }

    private int changeColor(int colorsCounter) {
        colorsCounter++;
        if(colorsCounter==colors.length){
            colorsCounter =0;
        }
        return colorsCounter;
    }

    void setPaneElements(final Field field) {
        List<List<Integer>> generatedNumbers = field.getFieldNumbers();
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
                        event -> mouseDraggedEvent(event, field)
                );
                gameCell.text.setOnMouseDragReleased(event ->  mouseReleasedEvent(event, field));
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
