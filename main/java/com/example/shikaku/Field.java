package com.example.shikaku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Field implements Cloneable {
    int n;

    final List<List<Integer>> fieldNumbers;

    List<RectangleOnField> inputRectangles = new ArrayList<>();

    List<List<Integer>> result;

    public void setResult(List<List<Integer>> result) {
        this.result = result;
    }

    Field (int n) {
        this.n = n;
        fieldNumbers = generate();
    }

    Field (List<List<Integer>> result, List<List<Integer>> fieldNumbers){
        this.result = result;
        this.fieldNumbers = fieldNumbers;
    }

    public List<List<Integer>> generate(){
        Generation gen = new Generation(n);
        return matrToField(gen.generateField());
        //return gen.getField();
    }

    public List<List<Integer>> getFieldNumbers() {
        return fieldNumbers;
    }

    public Field addInputRectangleToField(final Field field, List<Integer> inputRect, int numCounter, int num) {
        Field fieldCopy = field.clone();
        RectangleOnField r = new RectangleOnField(List.copyOf(inputRect), numCounter, num);
        fieldCopy.inputRectangles.add(r);
        return fieldCopy;
    }


    private static int countOnes(RectangleOnField rect) {
        return (int) rect.inputRectangle.stream().filter(el -> el==1).count();
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
    private static boolean checkRectangles(List<RectangleOnField> listRectangles) {
        return listRectangles.stream().allMatch(el -> el.num == countOnes(el));
    }


    private static boolean isRectChosenOnce(List<RectangleOnField> listRectangles) {
        return listRectangles.stream().allMatch(el -> el.numCounter == 1);
    }

    private static boolean checkAllOnes(int[] ones, int size) {
        return Arrays.stream(ones).allMatch(el -> el == 1);
    }

    public static boolean isSolved(List<RectangleOnField> r) {
        if (!r.isEmpty()) {
            int[] ones = fillOnesArray(r, r.get(0).inputRectangle.size());
            return isRectChosenOnce(r) && checkRectangles(r) && checkAllOnes(ones,r.get(0).inputRectangle.size());
        }
        return false;
    }

    private List<List<Integer>> matrToField(List<RectangleOnField> fieldMatr) {
        List<List<Integer>> field = new ArrayList<>();
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
        return field;
    }

    @Override
    public Field clone() {
        try {
            Field clone = (Field) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
