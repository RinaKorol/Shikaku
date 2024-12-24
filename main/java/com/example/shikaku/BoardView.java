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

    private static final  Color[] colors = new Color[]{Color.ROYALBLUE, Color.YELLOW, Color.AQUAMARINE, Color.LIGHTCORAL,
            Color.FUCHSIA, Color.RED, Color.FORESTGREEN, Color.BISQUE, Color.GOLD, Color.BROWN,
            Color.CRIMSON, Color.YELLOWGREEN, Color.DIMGREY, Color.PERU, Color.SADDLEBROWN, Color.PLUM,
            Color.POWDERBLUE, Color.MEDIUMVIOLETRED, Color.DARKVIOLET, Color.LIGHTSLATEGRAY
    };
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

    Node getLabelPane(Field field){
        pane.setPadding(new Insets(3));
        return createLabelPane(field);
    }

    TilePane createLabelPane(Field field){
        TilePane labelPane = new TilePane();
        labelPane.setPrefColumns(n);
        labelPane.setPrefTileWidth(CELL_WIDTH); //(400-20)/n
        labelPane.setPrefTileHeight(CELL_WIDTH);
        labelPane.setMaxWidth(Region.USE_PREF_SIZE);

        List<Node> listNodes = labelPane.getChildren();
        listNodes.addAll(createChildren(field));
        return labelPane;
    }

    private List<Node> paintLabels(final List<Node> listNodes, final List<List<Integer>> result) {
        List<Node> listNodesCopy = List.copyOf(listNodes);
        int i=0;
        for(final List<Integer> row : result ){
            int finalI = i;
            row.forEach(el ->{
                ((Label)listNodesCopy.get(el)).setBackground(getBackground(finalI));
            });
            i = changeColor(i);
        }
        return listNodesCopy;
    }

    private Background getBackground(final int finalI) {
        return new Background
                (new BackgroundFill(colors[finalI],
                        CornerRadii.EMPTY, Insets.EMPTY));
    }

    private List<Node> createChildren(Field field) {
        List<Node> listNodes = new ArrayList<>();
        for (int i = 0; i < n * m; ++i) {
            Label view = new Label();//
            view.setMinWidth(30);
            view.setMinHeight(30);
            int numInt = field.fieldNumbers.get(i/n).get(i%n);
            if (numInt != 0){
                String numStr = Integer.toString(numInt);
                view.setText(numStr);
                view.setPadding(new Insets(0,0,0,9));
            }
            else
                view.setText("");
            listNodes.add(view);
        }
        return paintLabels(listNodes, field.result);
    }

    private int changeColor(final int i) {
        if (i == colors.length) {
            return 0;
        }
        return i+1;
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
