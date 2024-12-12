package com.example.shikaku;

import java.util.ArrayList;
import java.util.List;

public class Backtracking {
    private static List<List<Integer>> result = new ArrayList<>();
    private static List<List<Integer>> solvingMatr = new ArrayList<>();
    static int numRect = 0;

    public static List<List<Integer>> getResult() {
        return result;
    }

    public static Cell createField(List<List<Integer>> matr){
        Cell h = new Cell();
        h.next=h;
        Cell prev = null;
        boolean firstCell = true;

        for (int j = 0; j < matr.size() ; ++j) {
            for (int i = 0; i < matr.get(j).size() ; ++i) {
                if(matr.get(j).get(i)!=0){
                    Cell cell = new Cell();
                    numRect++;
                    cell.num = matr.get(j).get(i);
                    cell.numX = i;
                    cell.numY = j;
                    if(firstCell){
                        h.next = cell;
                        firstCell=false;
                    }
                    cell.next = h;
                    if(prev!=null)
                        prev.next = cell;
                    prev = cell;
                }

            }
        }
        int max = 0;
        Cell maxCell = null;
        Cell curr = h.next;
        while (curr.next!=h){
            if(curr.num>max){
                max = curr.num;
                maxCell = curr;
            }
            curr = curr.next;
        }
        if(maxCell!=null) {
            curr.next = h.next;
            h.next = maxCell;
        }
        return h;
    }

    static private boolean processResult(){
        for (int j = 0; j < solvingMatr.size(); ++j) {
            final List<Integer> resultRow = new ArrayList<>();
            for (int i = 0; i < solvingMatr.get(j).size(); ++i) {
                if (solvingMatr.get(j).get(i) == 1) {
                    resultRow.add(i);
                }
            }
            result.add(resultRow);
        }
        return true;
    }

    static public void printField(Cell h){
        Cell curr = h;
        while(curr.next!=h){
            curr = curr.next;
            System.out.print(curr.num+" ");
        }
        System.out.println();
    }

//    private static List<List<Integer>> createMatrForNumber(Cell cell){
//        List<List<Integer>> options = new ArrayList<>();
//
//    }

    public static boolean solve(Cell currNum, Matrix matrix){
        if(isSolved()){
            return processResult();
        }
        boolean solutionFound = false;
        Cell curr = currNum;
        List<List<Integer>> currRectOptions = matrix.rectOptions(curr.num,curr.numX,curr.numY);  //все возможные расположения прямоугольников
        if(solvingMatr.isEmpty()){  //если в списке ответов нет ничего, то просто добавить прямоугольник
            for (int i=0; i<currRectOptions.size();i++) {   //проходим по всем возможным прямоугольникам для текущего числа
                if(!solutionFound) {
                    solvingMatr.add(currRectOptions.get(i));    //добавить в ответы
                    solutionFound = solve(curr.next, matrix);    //рекурсивный вызов
                    solvingMatr.remove(solvingMatr.size() - 1);
                }
                else return solutionFound;  //если найдено решение, то прекратить работу
            }
        }
        else {
            for(int i=0;i<currRectOptions.size();i++){   //проходим по всем возможным прямоугольникам для текущего числа
                if(!solutionFound) {
                    if (isPossible(currRectOptions.get(i))) {
                        solvingMatr.add(currRectOptions.get(i));    //добавить в ответы
                        solutionFound = solve(curr.next, matrix);    //рекурсивный вызов
                        solvingMatr.remove(solvingMatr.size() - 1);
                    }
                }
                else return solutionFound;
            }
        }
        return solutionFound;
    }
    public static void printResult(List<List<Integer>> mat){
        System.out.println("Bactracking result");
        for (int j = 0; j < mat.size() ; ++j) {
            for (int i = 0; i < mat.get(j).size(); ++i) {
                System.out.print(mat.get(j).get(i)+" ");
            }
            System.out.println();
        }
    }
    private static boolean isPossible(List<Integer> newStr) {   //проверка возможно ли расположение прямоугольников
        boolean isPossibleFlag = true;
        int[] ones = new int[solvingMatr.get(0).size()];    //количество единиц в каждом столбе матрицы решения
        for (int j = 0; j < solvingMatr.size(); ++j) {
            for (int i = 0; i < solvingMatr.get(j).size(); ++i) {
                if (solvingMatr.get(j).get(i) == 1) {
                    ones[i]++;   //считаем 1 в уже выбранных ответах
                }
            }
        }
        for(int i=0;i<newStr.size();i++)    //считаем 1 для текущего ответа
            if(newStr.get(i) == 1)
                ones[i]++;
        for (int i = 0; i < solvingMatr.get(0).size(); i++) {
            if (ones[i] > 1) {      //если 1 в столбце больше 1, значит прямоугольники заходят друг на друга - решение неверно
                isPossibleFlag = false;
                break;
            }
        }
        return isPossibleFlag;
    }
    private static boolean isSolved() { //проверка найдено ли решение
        boolean isSolve = false;
        boolean checkoneOnes = false;

        if(solvingMatr.size() == numRect && !solvingMatr.isEmpty()){    //для всех чисел выбран прямоугольник
            int[] ones = new int[solvingMatr.get(0).size()];    //количество единиц в каждом столбе матрицы решения
            checkoneOnes = true;
            for (int j = 0; j < solvingMatr.size(); ++j) {
                for (int i = 0; i < solvingMatr.get(j).size(); ++i) {
                    if (solvingMatr.get(j).get(i) == 1) {
                        ones[i]++;  //считаем 1 в уже выбранных ответах
                    }
                }
            }
            for (int i = 0; i < solvingMatr.get(0).size(); i++) {
                if (ones[i] != 1) { //в каждом стоблце должна быть ровно одна 1
                    checkoneOnes = false;
                    break;
                }
            }
        }
        if(checkoneOnes)
            isSolve = true;

        return isSolve;
    }

}

class Cell {
    int num = 0;
    int numX =0, numY = 0;
    Cell next = null;

}
