package com.example.shikaku;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generation {
    int n;
    int iter = 0;
    List<RectangleOnField> fieldMatr = new ArrayList<>();

    List<List<Integer>> field = new ArrayList<>();

    Generation(int n) {
        this.n = n;
        RectangleOnField first = new RectangleOnField();
        List<Integer> str = new ArrayList<>();
        for (int i = 0; i < n * n; i++) {
            str.add(1);
        }
        first.str = str;
        first.numCol = n;
        first.numStr = n;
        fieldMatr.add(first);
    }

    public List<List<Integer>> getField() {
        matrToField();
        //printFields();
        return field;
    }

    private void matrToField() {
        Random rand = new Random();
        int space = 0;
        int place = 0;
        int counter = -1;

        for (int i = 0; i < n; i++) {      //создали поле нужного размера, заполненное нулями
            List<Integer> tmp = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                tmp.add(0);
            }
            field.add(tmp);
        }

        for (RectangleOnField rectangleOnField : fieldMatr) {
            space = rectangleOnField.numCol * rectangleOnField.numStr;
            place = rand.nextInt(space);
            for (int j = 0; j < rectangleOnField.str.size(); j++) {
                if (rectangleOnField.str.get(j) == 1)
                    counter++;
                if (counter == place) {
                    field.get(j / n).set(j % n, space);
                    break;
                }
            }
            counter = -1;
        }
    }

    private void printFields() {
        System.out.println("Generation field:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(field.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println("Generation fieldMatr");
        for (RectangleOnField rectangleOnField : fieldMatr) {
            for (int j = 0; j < rectangleOnField.str.size(); j++) {
                System.out.print(rectangleOnField.str.get(j) + " ");
            }
            System.out.println();
        }
    }

    private void chooseCols(RectangleOnField currRec, int devRes, RectangleOnField rect, RectangleOnField rect2) {
        int first1 = currRec.str.size();
        List<Integer> str2 = new ArrayList<>();
        for (int i = 0; i < first1; i++) {
            str2.add(currRec.str.get(i));
        }
        for (int i = 0; i < n * n; i++) { //нахожу первую 1 в строке которую делю, чтобы определить в каком столбце
            //начинается прямоугольник
            if (currRec.str.get(i) == 1) {
                first1 = i % n;
                break;
            }
        }
        for (int i = 0; i < n * n; i++) {     //прохожусь по всем столбцам и забираю значения из нужных
            if (i % n == first1) {
                for (int j = 0; j < devRes; j++) {
                    rect.str.set(i + j, currRec.str.get(i));    //беру все столбцы которые мне нужны
                    str2.set(i + j, 0);
                }
            }
        }
        rect2.str = str2;
    }

    private void chooseStr(RectangleOnField currRec, int devRes, RectangleOnField rect, RectangleOnField rect2) {
        int first1 = currRec.str.size();
        List<Integer> str2 = new ArrayList<>();
        for (int i = 0; i < first1; i++) {
            str2.add(currRec.str.get(i));
        }
        for (int i = 0; i < n * n; i++) { //нахожу первую 1 в строке которую делю, чтобы определить в какой строке
            //начинается прямоугольник
            if (currRec.str.get(i) == 1) {
                first1 = i / n;
                break;
            }
        }
        for (int i = 0; i < n * n; i++) {   //прохожусь по всем строкам и забираю значения из нужных
            if (i / n == first1) {
                for (int j = 0; j < devRes; j++) {
                    rect.str.set(i + j * n, currRec.str.get(i));
                    str2.set(i + j * n, 0);
                }
            }
        }
        rect2.str = str2;
    }

    private void divideColumns(RectangleOnField currRec) {
        int devRes1, devRes2;
        if (currRec.numCol > 1) {
            devRes1 = currRec.numCol / 2;
            devRes2 = currRec.numCol - devRes1;
            RectangleOnField first = new RectangleOnField(); //создаю два прямоугольника
            first.numStr = currRec.numStr;
            first.numCol = devRes1;
            first.str = Stream.generate(() -> 0)
                    .limit(n * n)
                    .collect(Collectors.toCollection(ArrayList::new));
            RectangleOnField second = new RectangleOnField();
            second.numStr = currRec.numStr;
            second.numCol = devRes2;
            second.str = Stream.generate(() -> 0)
                    .limit(n * n)
                    .collect(Collectors.toCollection(ArrayList::new));

            chooseCols(currRec, devRes1, first, second);
            fieldMatr.remove(currRec);  //удаляю старый и вношу два новых прямоугольника
            fieldMatr.add(first);
            fieldMatr.add(second);
        }
    }

    private void divideStrings(RectangleOnField currRec) {
        int devRes1, devRes2;
        if (currRec.numStr > 1) {
            devRes1 = currRec.numStr / 2;
            devRes2 = currRec.numStr - devRes1;

            if (iter % 2 == 0) {
                int tmp = devRes1;
                devRes1 = devRes2;
                devRes2 = tmp;
            }

            RectangleOnField first = new RectangleOnField();
            first.numStr = devRes1;
            first.numCol = currRec.numCol;
            first.str = Stream.generate(() -> 0)
                    .limit(n * n)
                    .collect(Collectors.toCollection(ArrayList::new));
            RectangleOnField second = new RectangleOnField();
            second.numStr = devRes2;
            second.numCol = currRec.numCol;
            second.str = Stream.generate(() -> 0)
                    .limit(n * n)
                    .collect(Collectors.toCollection(ArrayList::new));

            chooseStr(currRec, devRes1, first, second);
            fieldMatr.remove(currRec);
            fieldMatr.add(first);
            fieldMatr.add(second);
        }
    }

    public void generateField() {
        Random rand = new Random();
        int chosenStr = rand.nextInt(fieldMatr.size());
        if (iter < 3)   //первые 3 итерации делится самый большой прямоугольник
            chosenStr = 0;

        int colOrStr = rand.nextInt(2);//0 столбец, 1 строка
        RectangleOnField currRec = fieldMatr.get(chosenStr);

        while (iter < n * n / 3 +1) {   //делю пока не достигнуто нужное количество прямоугольников
            if (currRec.numStr * currRec.numCol > 3) {
                if (colOrStr == 0) {
                    divideColumns(currRec);
                } else {
                    divideStrings(currRec);
                }
            }
            iter++;
            generateField();
        }
    }

    public static boolean isSolved(List<CheckingRectangle> r) {
        boolean checkoneOnes = false;
        boolean isSolved = true;

        if (!r.isEmpty()) {
            for (int j = 0; j < r.size(); ++j) {
                if (r.get(j).numCounter != 1)
                    isSolved = false;
            }
            int[] ones = new int[r.get(0).inputRectangle.size()];
            checkoneOnes = true;
            int counter = 0;
            for (int j = 0; j < r.size(); ++j) {
                for (int i = 0; i < r.get(j).inputRectangle.size(); ++i) {
                    if (r.get(j).inputRectangle.get(i) == 1) {
                        ones[i]++;
                        counter++;
                    }
                }
                if(counter!=r.get(j).num) {
                    isSolved = false;
                }
                counter = 0;
            }
            for (int i = 0; i < r.get(0).inputRectangle.size(); i++) {
                if (ones[i] != 1) {
                    checkoneOnes = false;
                    break;
                }
            }

        }
        if(isSolved && checkoneOnes)
            return true;
        return false;

    }
}


class RectangleOnField {
    int numCol;
    int numStr;
    List<Integer> str= new ArrayList<>();
}
