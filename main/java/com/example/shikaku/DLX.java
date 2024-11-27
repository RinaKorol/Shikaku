package com.example.shikaku;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DLX {
    private static List<List<Integer>> result;

    public static List<List<Integer>> getResult() {
        return result;
    }

    public static final ColumnNode buildSparseMatrix(List<List<Integer>> matr,   //byte[][] input,
                                                     Integer[] labels ) { //

        // Make root node and column headers
        final ColumnNode h = new ColumnNode();
        h.L = h; h.R = h; h.U = h; h.D = h;

        for (int j = 0; j < matr.get(0).size(); ++j) { //input[0].length
            final ColumnNode newColHdr = new ColumnNode();
            newColHdr.U = newColHdr;
            newColHdr.D = newColHdr;
            newColHdr.R = h;
            newColHdr.L = h.L;
            h.L.R = newColHdr;
            h.L = newColHdr;
            if ((labels != null) && (j < labels.length) && (labels[j] != null))
                newColHdr.col = labels[j];
        }

        // For each row, build a list and stitch it onto the column headers
        long rowSequenceNumber = 0;
        final List<CellNode> thisRowObjects = new LinkedList<CellNode>();
        for (int j = 0; j < matr.size() ; ++j) { //input.length
            thisRowObjects.clear();
            ColumnNode currentCol = (ColumnNode)h.R;
            for (int i = 0; i < matr.get(j).size() ; ++i) { //input[j].length
                // Build data objects, attach to column objects
                if (matr.get(j).get(i) != 0) { //input[j][i]
                    CellNode obj = null;
                    obj = new CellNode();
                    obj.U = currentCol.U;
                    obj.D = currentCol;
                    obj.L = obj;
                    obj.R = obj;
                    obj.C = currentCol;
                    currentCol.U.D = obj;
                    currentCol.U = obj;
                    currentCol.size++;
                    thisRowObjects.add(obj);
                }
                currentCol = (ColumnNode)currentCol.R;
            }
            // Link all data objects built for this row horizontally
            if (thisRowObjects.size() > 0) {
                final Iterator<CellNode> iter = thisRowObjects.iterator();
                final CellNode first = iter.next();
                while (iter.hasNext()) {
                    final CellNode thisObj = iter.next();
                    thisObj.L = first.L;
                    thisObj.R = first;
                    first.L.R = thisObj;
                    first.L = thisObj;
                }
            }
            rowSequenceNumber++;
        }

        return h;
    }

    private static boolean processResult(
            List<CellNode> o) {
        final List<List<Integer>> resultSet = new LinkedList<List<Integer>>();
        for (final CellNode oK : o) {
            final List<Integer> resultRow = new LinkedList<Integer>();
            CellNode node = oK;
            do {
                resultRow.add(((ColumnNode)node.C).col);
                node = node.R;
            } while (node != oK);

            resultSet.add(resultRow);
        }
        result = printResult(resultSet);
        //final DLXResult result = new DLXResult(resultSet);
        //return processor.processResult(result);
        return true;
    }

    private static List<List<Integer>> printResult(List<List<Integer>> result){
        final StringBuffer buffer = new StringBuffer();
        for (final List<Integer> row : result) {
            for (final Object label : row) {
                buffer.append(label.toString());
                buffer.append(' ');
            }
            buffer.append('\n');
        }
        //System.out.println(buffer);
        return result;
    }

    static boolean solve(ColumnNode h, int k, List<CellNode> solving) {
        if (h.R == h) { //если нет столбцов, то найдено решение
            return processResult(solving);
        }

        ColumnNode c = (ColumnNode)h.R;

        ColumnNode i = ((ColumnNode)h.R);   //выбрать столбец с наименьшим кол-вом 1
        int s = i.size;
        while (i != h) {
            if (i.size < s) {
                s = i.size;
                c = i;
            }
            i = (ColumnNode)i.R;
        }

        boolean solutionFound = false;
        //покрываем выбранный столбец
        cover(h, c);
        CellNode d = c.D;
        while ((!solutionFound) && (d != c)) {
            grow(solving, k+1);
            //добавляем в рещение
            solving.set(k, d);
            CellNode j = d.R;
            //покрываем остальные нужные столбцы
            while (j != d) {
                cover(h, (ColumnNode)j.C);
                j = j.R;
            }
            //рекурсия
            solutionFound = solve(h,k+1, solving);

            j = d.L;
            //если вернулись назад, то возвращаем столбцы
            while (j != d) {
                uncover(h, (ColumnNode)j.C);
                j = j.L;
            }

            d = d.D;
        }
        uncover(h, c);

        return solutionFound;
    }

    private static void grow(List<CellNode> o, int k) {
        if (o.size() < k) {
            while (o.size() < k) {
                o.add(null);
            }
        } else if (o.size() > k) {
            while (o.size() > k) {
                o.remove(o.size() - 1);
            }
        }
    }

    static void  cover(ColumnNode h, ColumnNode columnObj) {
        columnObj.R.L = columnObj.L;
        columnObj.L.R = columnObj.R;
        CellNode i = columnObj.D;
        //проходим по всему столбцу
        while (i != columnObj) {
            CellNode j = i.R;
            //проходим по всей строке
            while (j != i) {
                //удаляем
                j.D.U = j.U;
                j.U.D = j.D;
                ((ColumnNode)j.C).size--;
                j = j.R;
            }
            i = i.D;
        }
    }

    static void uncover(ColumnNode h, ColumnNode columnObj) {
        CellNode i = columnObj.U;
        //проходим по столбцу
        while (i != columnObj) {
            CellNode j = i.L;
            //проходим по строке
            while (j != i) {
                ((ColumnNode)j.C).size++;
                //возвращаем узлы в матрицу
                j.D.U = j;
                j.U.D = j;
                j = j.L;
            }
            i = i.U;
        }
        columnObj.R.L = columnObj;
        columnObj.L.R = columnObj;
    }

}



class CellNode {
    public CellNode L, R, U, D, C;
}

class ColumnNode extends CellNode {

    public int size = 0;
    public Integer col =0;


}