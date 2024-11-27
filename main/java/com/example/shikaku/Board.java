package com.example.shikaku;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    List<List<Integer>> field;
    List<List<Integer>> matr;
    int n,m;
    int counter = 0;

    public Board(){
        this.field = new ArrayList<>();
    }

    public Board(List<List<Integer>> field, int n, int m) {
        this.field = field;
        this.n = n;
        this.m = m;
    }

    public List<List<Integer>> getMatr(){
        return matr;
    }

    private TreeSet<Integer> getDevisors(int num){
        TreeSet<Integer> devisors = new TreeSet<>();
        for( int i=1; i< Math.sqrt(num); i++){
            if (num % i == 0)
            {
                devisors.add(i);
                devisors.add(num / i);
            }
        }
        int i= (int)Math.sqrt(num);
        if(num%i == 0)
            devisors.add(i);

//        for(Integer j : devisors)
//            System.out.print(j+ " ");
//        System.out.println();
        return devisors;
    }

    //массив списков, колонок n*m строк - сколько угодно - добавляются динамически
    public boolean createMatrix(){
        matr = new ArrayList<>();
        int num;
        //int recHight, recWidth;
        //int counter=0;
//        boolean isNumberMarked = false;
//        List<Integer> matrStr = Stream.generate(() -> 0)
//                .limit(n*m)
//                .collect(Collectors.toCollection(ArrayList::new));
        int numX, numY;
//        Boolean flag = false;
//        TreeSet<Integer> devisors;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                num = field.get(i).get(j);
                numX = j;
                numY = i;
                if(num != 0){
                    List<List<Integer>> oneRecMat = rectOptions(num, numX,numY);
                    if(!oneRecMat.isEmpty()){
                        matr.addAll(oneRecMat);
                    }

//                    isNumberMarked = false;
//                    int[] rect = new int[num];
//                    //System.out.println(num);
//                    devisors = getDevisors(num);
//                    for(int devisor : devisors){
//                        recHight = devisor;
//                        recWidth = num /devisor;
//                        if(recWidth > m || recHight > n) // влезает ли вообще прямоугольник в поле
//                            continue;
//                        //if(numX + recWidth > m || numY + recHight > n) //число в ячейке - не выходит ли прямоугольник за границы
//                        //   break;
//                        for(int k = 0;k< rect.length; k++){
//                            rect[k]=1;
//                            int str = k/recWidth;
//                            int stolb = k%recWidth;
//                            if(numX - stolb < 0 || numX + (recWidth-1 - stolb) > m-1) // влезает по горизонтали в поле
//                                continue;
//                            if (numY - str <0 || numY +(recHight-1 - str) > n-1)
//                                continue;
//                            for( int s = numY - str; s< numY - str +recHight; s++){
//                                for( int t = numX - stolb; t< numX- stolb+recWidth; t++) {
//                                    if (!(s == numY && t == numX))
//                                        if (field.get(s).get(t) != 0) { //&& s!=numY && t!=numX
//                                            flag = true;
//                                            break;
//                                        }
//                                    matrStr.set(s * m + t, 1);
//                                }
//                            }
//                            if(!flag){
//                                matr.add(matrStr);
//                                if(!isNumberMarked){
//                                    counter++;
//                                    isNumberMarked=true;
//                                }
//                            }
//                            matrStr = Stream.generate(() -> 0)
//                                    .limit(n*m)
//                                    .collect(Collectors.toCollection(ArrayList::new));
//                            flag = false;
//                        }
//                    }
                }
            }
        }
        //printMatr();
        System.out.println("Counter: " + counter);
        return checkMatr(counter);
    }

    public List<List<Integer>> rectOptions (int num, int numX, int numY){ //проверку на то что num не 0 добавитьна всякий
        List<List<Integer>> oneRectMatr = new ArrayList<>();
        int recHight, recWidth;
        boolean isNumberMarked = false;
        List<Integer> matrStr = Stream.generate(() -> 0)
                .limit(n*m)
                .collect(Collectors.toCollection(ArrayList::new));
        Boolean flag = false;
        TreeSet<Integer> devisors;

        isNumberMarked = false;
        int[] rect = new int[num];
        //System.out.println(num);
        devisors = getDevisors(num);
        for(int devisor : devisors){
            recHight = devisor;
            recWidth = num /devisor;
            if(recWidth > m || recHight > n) // влезает ли вообще прямоугольник в поле
                continue;
            //if(numX + recWidth > m || numY + recHight > n) //число в ячейке - не выходит ли прямоугольник за границы
            //   break;
            for(int k = 0;k< rect.length; k++){
                rect[k]=1;
                int str = k/recWidth;
                int stolb = k%recWidth;
                if(numX - stolb < 0 || numX + (recWidth-1 - stolb) > m-1) // влезает по горизонтали в поле
                    continue;
                if (numY - str <0 || numY +(recHight-1 - str) > n-1)
                    continue;
                for( int s = numY - str; s< numY - str +recHight; s++){
                    for( int t = numX - stolb; t< numX- stolb+recWidth; t++) {
                        if (!(s == numY && t == numX))
                            if (field.get(s).get(t) != 0) { //&& s!=numY && t!=numX
                                flag = true;
                                break;
                            }
                        matrStr.set(s * m + t, 1);
                    }
                }
                if(!flag){
                    oneRectMatr.add(matrStr);
                    //matr.add(matrStr);
                    if(!isNumberMarked){
                        counter++;
                        isNumberMarked=true;
                    }
                    //return matrStr;
                }
                matrStr = Stream.generate(() -> 0)
                        .limit(n*m)
                        .collect(Collectors.toCollection(ArrayList::new));
                flag = false;
            }
        }
        return oneRectMatr;
    }

    public boolean checkMatr(int counterPossibleRect){
        int num, sum =0, countNumbers = 0;
        boolean isNotEmpty = false;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++) {
                num = field.get(i).get(j);
                if(num!=0){
                    countNumbers++;
                    isNotEmpty = true;
                    sum+=num;
                }
            }
        }
        return ((sum == m*n)&&(isNotEmpty)&&(counterPossibleRect == countNumbers));
    }



    public void printMatr(){
        for(int i=0; i<matr.size(); i++){
            for(int j=0;j<m*n;j++){
                System.out.print(matr.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    public void printField(){
        for(int i=0; i<n; i++){
            for(int j=0;j<m;j++){
                System.out.print(field.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
