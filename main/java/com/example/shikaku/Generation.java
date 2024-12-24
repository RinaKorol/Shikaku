package com.example.shikaku;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.shikaku.RectangleOnField.createNewRect;
import static com.example.shikaku.Utils.createFilledList;
import static com.example.shikaku.Utils.setNum;

public class Generation {
    public static int n = 0;
    int iter = 0;

    Generation(int n) {
        this.n = n;
    }




    //действие
    private RectangleOnField[] devisionCols(RectangleOnField currRec, int iter) {
        int devRes1, devRes2;
        devRes1 = currRec.numCol / 2;
        devRes2 = currRec.numCol - devRes1;

        if (iter % 2 == 0) {
            int tmp = devRes1;
            devRes1 = devRes2;
            devRes2 = tmp;
        }

        RectangleOnField first = createNewRect(currRec.numStr, devRes1); //создаю два прямоугольника
        RectangleOnField second = createNewRect(currRec.numStr, devRes2);
        List<Integer> str2 = new ArrayList<>();
        str2 = Utils.addToList(str2, currRec.inputRectangle);
        Object[] recCol = cutRect(forCol, devRes1, currRec, first, str2, 1);
        first = (RectangleOnField) recCol[0];
        second.inputRectangle = (List<Integer>) recCol[1];
        return new RectangleOnField[]{first, second};
    }

    private RectangleOnField[] division(RectangleOnField currRec, int colOrStr) {
        int devRes1, devRes2;
        RectangleOnField first; //создаю два прямоугольника
        RectangleOnField second;
        List<Integer> str2 = new ArrayList<>();
        str2 = Utils.addToList(str2, currRec.inputRectangle);
        Object[] res;
        if (colOrStr == 0) {
            devRes1 = currRec.numCol / 2;
            devRes2 = currRec.numCol - devRes1;
            first = createNewRect(currRec.numStr, devRes1); //создаю два прямоугольника
            second = createNewRect(currRec.numStr, devRes2);
            res = cutRectByCol(devRes1, currRec, first, str2);
        }
        else {
            devRes1 = currRec.numStr / 2;
            devRes2 = currRec.numStr - devRes1;
            first = createNewRect(devRes1, currRec.numCol);
            second = createNewRect(devRes2, currRec.numCol);
            res = cutRect(forStr, devRes1, currRec, first, str2, n);
        }
        first = (RectangleOnField) res[0];
        second.inputRectangle = (List<Integer>) res[1];
        return new RectangleOnField[]{first, second};
    }

    //теперь вычисление
    private Object[] cutRectByCol(int devRes, RectangleOnField currRec, RectangleOnField rect, List<Integer> str2) {
        RectangleOnField newRect = rect.clone();
        List<Integer> str = new ArrayList<>(List.copyOf(str2));
        int chosenCol = currRec.chooseCol();
        for (int i = 0; i < n * n; i++) {     //прохожусь по всем столбцам и забираю значения из нужных
            if (i % n == chosenCol) {
                for (int j = 0; j < devRes; j++) {
                    newRect.inputRectangle.set(i + j, currRec.inputRectangle.get(i));    //беру все столбцы которые мне нужны
                    str.set(i + j, 0);
                }
            }
        }
        return new Object[]{newRect, str};
    }

    BiFunction<Integer, RectangleOnField, Boolean> forCol = (num, rec) -> Objects.equals(num%n, rec.chooseCol());
    BiFunction<Integer, RectangleOnField, Boolean> forStr = (num, rec) -> Objects.equals(num/n, rec.chooseStr());
    private Object[] cutRect(
            BiFunction<Integer, RectangleOnField, Boolean> func,
            int devRes,
            RectangleOnField currRec,
            RectangleOnField rect,
            List<Integer> str2,  int t) {
        RectangleOnField newRect = rect.clone();
        List<Integer> str = new ArrayList<>(List.copyOf(str2));
        for (int i = 0; i < n * n; i++) {     //прохожусь по всем столбцам и забираю значения из нужных
            if (func.apply(i,currRec)) {
                for (int j = 0; j < devRes; j++) {
                    newRect.inputRectangle.set(i + j * t, currRec.inputRectangle.get(i));    //беру все столбцы которые мне нужны
                    str.set(i + j * t, 0);
                }
            }
        }
        return new Object[]{newRect, str};
    }
    //вычисление
    private Object[] cutRectByStr(int devRes, RectangleOnField currRec, RectangleOnField rect, List<Integer> str2) { //RectangleOnField rect2
        RectangleOnField newRect = rect.clone();
        List<Integer> str = new ArrayList<>(List.copyOf(str2));
        int chosenStr = currRec.chooseStr();
        for (int i = 0; i < n * n; i++) {   //прохожусь по всем строкам и забираю значения из нужных
            if (i / n == chosenStr) {
                for (int j = 0; j < devRes; j++) {
                    newRect.inputRectangle.set(i + j * n, currRec.inputRectangle.get(i));
                    str.set(i + j * n, 0);
                }
            }
        }
        return new Object[]{newRect, str};
    }

    //вычисление но слишком большое
    //та самая штука про неявные параметры
    private RectangleOnField[] devisionStr(RectangleOnField currRec) {
        int devRes1, devRes2;
        devRes1 = currRec.numStr / 2;
        devRes2 = currRec.numStr - devRes1;
        List<Integer> str2 = new ArrayList<>();
        str2 = Utils.addToList(str2, currRec.inputRectangle);
        RectangleOnField first = createNewRect(devRes1, currRec.numCol);
        RectangleOnField second = createNewRect(devRes2, currRec.numCol);
        Object[] recStr = cutRect(forStr, devRes1, currRec, first, str2, n);
        first = (RectangleOnField) recStr[0];
        second.inputRectangle = (List<Integer>) recStr[1];
        return new RectangleOnField[]{first, second};
    }


    //сделать чтобы оно не изменяло глобальную переменную, а принимало fieldMatr как аргумент и после этого
    //использовать КПЗ и использовать как пример для КПЗ
    private List<RectangleOnField> divide(RectangleOnField currRec, int colOrStr, List<RectangleOnField> fieldMatr, int iter ) {
        List<RectangleOnField> fieldMatrCopy = new ArrayList<>(List.copyOf(fieldMatr));
        if (currRec.numCol > 1 && currRec.numStr > 1) {
            RectangleOnField[] res;
            if (colOrStr == 0) {
                res = devisionCols(currRec, iter);
            } else {
                res = devisionStr(currRec);
            }
            fieldMatrCopy.remove(currRec);  //удаляю старый и вношу два новых прямоугольника
            fieldMatrCopy.add(res[0]);
            fieldMatrCopy.add(res[1]);
        }
        return fieldMatrCopy;
    }


    //изменяется глобальная переменная iter  fieldMatr
    private List<RectangleOnField> divideField(RectangleOnField currRec, Random rand, List<RectangleOnField> fieldMatr) {
        List<RectangleOnField> fieldMatrCopy = new ArrayList<>(List.copyOf(fieldMatr));
            if (currRec.numStr * currRec.numCol > 3) {
                int colOrStr = rand.nextInt(2);//0 столбец, 1 строка
                fieldMatrCopy = divide(currRec, colOrStr, fieldMatrCopy, iter);
            }
            iter++;
            return fieldMatrCopy;
    }

    //действие
    public List<RectangleOnField> generateField() {
        List<RectangleOnField> fieldMatr = new ArrayList<>();
        RectangleOnField first = new RectangleOnField();
        first.inputRectangle = createFilledList(setNum.apply(1),n);
        first.numCol = n;
        first.numStr = n;
        fieldMatr.add(first);

        while (iter < n * n / 2 +1) {
        Random rand = new Random();
        int chosenStr = rand.nextInt(fieldMatr.size());
        if (iter < 3)   //первые 3 итерации делится самый большой прямоугольник
            chosenStr = 0;

        RectangleOnField currRec = fieldMatr.get(chosenStr);
            fieldMatr = divideField(currRec, rand, fieldMatr);
        }
        return fieldMatr;
    }
}


class RectangleOnField implements Cloneable {
    int numCol;
    int numStr;
    List<Integer> inputRectangle = new ArrayList<>();
    int numCounter = 0;
    int num = 0;

    public RectangleOnField() {
    }

    public RectangleOnField(List<Integer> inputRectangle, int numCounter, int num) {
        this.inputRectangle = inputRectangle;
        this.numCounter = numCounter;
        this.num = num;
    }

    //вычисление
    public static RectangleOnField createNewRect(int numStr, int numCol) {
        RectangleOnField newRect = new RectangleOnField();
        newRect.numStr = numStr;
        newRect.numCol = numCol;
        newRect.inputRectangle = Stream.generate(() -> 0)
                .limit((long) Generation.n * Generation.n)
                .collect(Collectors.toCollection(ArrayList::new));
        return newRect;
    }

    //вычисление
    //выселить в ректангл тк находит колонку прямоугольника
    public int chooseCol() {
        int chosenCol = inputRectangle.size();
        for (int i = 0; i < Generation.n * Generation.n; i++) { //нахожу первую 1 в строке которую делю, чтобы определить в каком столбце
            //начинается прямоугольник
            if (inputRectangle.get(i) == 1) {
                chosenCol = i % Generation.n;
                break;
            }
        }
        return chosenCol;
    }

    //вычисление
    public int chooseStr() {
        int chosenStr = inputRectangle.size();
        //может вынести этот метод в RectangleOnField?
        for (int i = 0; i < Generation.n * Generation.n; i++) { //нахожу первую 1 в строке которую делю, чтобы определить в какой строке
            //начинается прямоугольник
            if (inputRectangle.get(i) == 1) {
                chosenStr = i / Generation.n;
                break;
            }
        }
        return chosenStr;
    }

    public void copyRectStr(int ind, RectangleOnField currRec) {
        List<Integer> str2 = new ArrayList<>();
        for (int i = 0; i < ind; i++) {
            str2.add(currRec.inputRectangle.get(i));
        }
        this.inputRectangle = str2;
    }

    @Override
    public RectangleOnField clone() {
        try {
            RectangleOnField clone = (RectangleOnField) super.clone();
            clone.inputRectangle = this.inputRectangle;
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
