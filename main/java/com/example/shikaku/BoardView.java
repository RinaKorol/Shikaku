package com.example.shikaku;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class BoardView {
    private static final int CELL_WIDTH = 35;
    TilePane pane = new TilePane();

    ObservableList<Node> list;
    int n,m;

    BoardView(int n, int m){
        this.n = n;
        this.m = m;
        pane.setPrefColumns(n);
        pane.setPrefTileWidth(CELL_WIDTH); 
        pane.setPrefTileHeight(CELL_WIDTH);
        pane.setMaxWidth(Region.USE_PREF_SIZE);
        setPaneElements();
    }
    void setPaneElements() {
        list = pane.getChildren();
        for (int i = 0; i < n * m; ++i) {
            TextField view = new TextField();
            view.setMaxWidth(CELL_WIDTH-7);
            view.setText("");
            list.add(view);
        }
    }

    Node getLabelPane(List<List<Integer>> result, List<List<Integer>> numbersField){
        TilePane labelPane = new TilePane();
        labelPane.setPrefColumns(n);
        pane.setPadding(new Insets(3));
        labelPane.setPrefTileWidth(CELL_WIDTH); //(400-20)/n
        labelPane.setPrefTileHeight(CELL_WIDTH);
        labelPane.setMaxWidth(Region.USE_PREF_SIZE);
        createLabelPane(result, labelPane, numbersField);
        return labelPane;
    }

    //теоретически тоже можно сделать вычислением если возсращать результат работы listNodes
    void createLabelPane(List<List<Integer>> result, TilePane labelPane, List<List<Integer>> numbersField){
        ObservableList<Node> listNodes;
        listNodes = labelPane.getChildren();
        for (int i = 0; i < n * m; ++i) {
            Label view = new Label();
            view.setMinWidth(30);
            view.setMinHeight(30);
            if (numbersField.get(i/n).get(i%n) != 0){
                int numInt = numbersField.get(i/n).get(i%n);
                String numStr = Integer.toString(numInt);
                view.setText(numStr);
                view.setPadding(new Insets(0,0,0,9));
            }
            else
                view.setText("");
            listNodes.add(view);
        }
        paintLabels(result, listNodes);
    }

    //можно сделать вычислением, если сделать кпз на listNodes
    void paintLabels(List<List<Integer>> result, ObservableList<Node> listNodes){
        Color[] colors = new Color[]{Color.ROYALBLUE, Color.YELLOW, Color.AQUAMARINE, Color.LIGHTCORAL,
                Color.FUCHSIA, Color.RED, Color.FORESTGREEN, Color.BISQUE, Color.GOLD, Color.BROWN,
                Color.CRIMSON, Color.YELLOWGREEN, Color.DIMGREY, Color.PERU, Color.SADDLEBROWN, Color.PLUM,
                Color.POWDERBLUE, Color.MEDIUMVIOLETRED, Color.DARKVIOLET, Color.LIGHTSLATEGRAY
        };
        int i=0;
        for (final List<Integer> row : result) {
            for (final Object label : row) {
                if (i == colors.length) {
                    i=0;
                }
                Color color = colors[i];
                int c = (Integer) label;
                ((Label)listNodes.get(c)).setBackground(new Background
                        (new BackgroundFill(color,
                                CornerRadii.EMPTY, Insets.EMPTY)));
            }
            i++;
        }
    }

    //вычисление
    List<List<Integer>> getBoard() throws NotPositiveNumber {
        List<List<Integer>> board = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        String numStr;
        int num = 0;
        for( int i=0;i<n;i++){
            for (int j =0; j<m;j++){
                numStr = ((TextField)list.get(i*m + j)).getText();
                if(!numStr.equals("")) {
                    num = Integer.parseInt(numStr);
                    if(num <= 0)
                        throw new NotPositiveNumber();
                }
                tmp.add(num);
                num = 0;
            }
            board.add(tmp);
            tmp = new ArrayList<>();
        }
        return board;
    }

    Node getBoardPane(){
        return pane;
    }
}

class NotPositiveNumber extends Exception{
}
