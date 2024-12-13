package com.example.shikaku;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.shikaku.RectangleOnField.createNewRect;

public class Generation {
    public static int n = 0;
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
        first.inputRectangle = str;
        first.numCol = n;
        first.numStr = n;
        fieldMatr.add(first);
    }

    public List<List<Integer>> getField() {
        matrToField();
        return field;
    }

    //действие
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

        //можно вынести в ф-ю изменяет глобальную переменную
        for (RectangleOnField rectangleOnField : fieldMatr) {
            space = rectangleOnField.numCol * rectangleOnField.numStr;
            place = rand.nextInt(space);
            for (int j = 0; j < rectangleOnField.inputRectangle.size(); j++) {
                if (rectangleOnField.inputRectangle.get(j) == 1)
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
            for (int j = 0; j < rectangleOnField.inputRectangle.size(); j++) {
                System.out.print(rectangleOnField.inputRectangle.get(j) + " ");
            }
            System.out.println();
        }
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
        Object[] recCol = cutRectByCol(devRes1, currRec, first, str2);
        first = (RectangleOnField) recCol[0];
        second.inputRectangle = (List<Integer>) recCol[1];
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

    //вычисление
    private Object[] cutRectByStr(int devRes, RectangleOnField currRec, RectangleOnField rect, List<Integer> str2) { //RectangleOnField rect2
        RectangleOnField newRect = rect.clone();
        int chosenStr = currRec.chooseStr();
        List<Integer> str = new ArrayList<>(List.copyOf(str2));
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
        RectangleOnField first = createNewRect(devRes1, currRec.numCol);
        RectangleOnField second = createNewRect(devRes2, currRec.numCol);
        List<Integer> str2 = new ArrayList<>();
        str2 = Utils.addToList(str2, currRec.inputRectangle);
        Object[] recStr = cutRectByStr(devRes1, currRec, first, str2);
        first = (RectangleOnField) recStr[0];
        second.inputRectangle = (List<Integer>) recStr[1];
        return new RectangleOnField[]{first, second};
    }

    //действие
    private void divideColumns(RectangleOnField currRec) {
        if (currRec.numCol > 1) {
            RectangleOnField[] res = devisionCols(currRec, iter);
            fieldMatr.remove(currRec);  //удаляю старый и вношу два новых прямоугольника
            fieldMatr.add(res[0]);
            fieldMatr.add(res[1]);
        }
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



    //действие - изменяется глобальная переменная fieldmatr
    //можно вынести часть со строки 170-180 и слить с делением cols
    private void divideStrings(RectangleOnField currRec) {
        if (currRec.numStr > 1) {
            RectangleOnField[] res = devisionStr(currRec);
            fieldMatr.remove(currRec);
            fieldMatr.add(res[0]);
            fieldMatr.add(res[1]);
        }
    }

    //изменяется глобальная переменная iter  fieldMatr
    private void divideRect(RectangleOnField currRec, int colOrStr) {
        while (iter < n * n / 2 +1) {   //делю пока не достигнуто нужное количество прямоугольников
            if (currRec.numStr * currRec.numCol > 3) {
                fieldMatr = divide(currRec, colOrStr, fieldMatr, iter);
            }
            iter++;
            generateField();
        }
    }

    //действие
    public void generateField() {
        Random rand = new Random();
        int chosenStr = rand.nextInt(fieldMatr.size());
        if (iter < 3)   //первые 3 итерации делится самый большой прямоугольник
            chosenStr = 0;

        int colOrStr = rand.nextInt(2);//0 столбец, 1 строка
        RectangleOnField currRec = fieldMatr.get(chosenStr);

        divideRect(currRec, colOrStr);
    }

    //подсчет единиц, //действие, тк меняется аргумент, но можно сделать вычислением
    //в идеале сделать ones через новую но не разобралась как вернуть пару значений сразу
    private static int countOnes(RectangleOnField rect) {
        int counter = 0;
        for (int i = 0; i < rect.inputRectangle.size(); ++i) {
            if (rect.inputRectangle.get(i) == 1) {
                //ones[i]++;
                counter++;
            }
        }
        return counter;
    }

    private static int[] fillOnesArray(List<RectangleOnField> listRectangles, int size){
        int[] ones = new int[size];
        for (RectangleOnField rect : listRectangles) {
            for (int i = 0; i < size; ++i) {
                if (rect.inputRectangle.get(i) == 1) {
                    ones[i]++;
                }
            }
        }
        return ones;
    }

    //проверка каждого прямоугольника
    //добавить копирование isSolved
    //действие, тк меняется аргумент, но можно сделать вычислением
    private static boolean checkRectangles(List<RectangleOnField> listRectangles) {
        int counter;
        for (RectangleOnField checkingRectangle : listRectangles) {
            counter = countOnes(checkingRectangle);
            if (counter != checkingRectangle.num) {
                return false;
            }
        }
        return true;
    }

    //добавить копирование isSolved
    //действие, тк меняется аргумент, но можно сделать вычислением
    private static boolean isRectChosenOnce(List<RectangleOnField> listRectangles) {
        for (RectangleOnField checkingRectangle : listRectangles) {
            if (checkingRectangle.numCounter != 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkAllOnes(int[] ones, int size) {
        for (int i = 0; i < size; i++) {
            if (ones[i] != 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSolved(List<RectangleOnField> r) {
        if (!r.isEmpty()) {
            int[] ones = fillOnesArray(r, r.get(0).inputRectangle.size());
            //new int[r.get(0).inputRectangle.size()];
              return isRectChosenOnce(r) && checkRectangles(r) && checkAllOnes(ones,r.get(0).inputRectangle.size());
        }
        return false;
    }
}


class RectangleOnField implements Cloneable {
    int numCol;
    int numStr;
    List<Integer> inputRectangle = new ArrayList<>();
    int numCounter = 0;
    int num = 0;

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
