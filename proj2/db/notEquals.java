package db;

import java.util.LinkedList;

public class notEquals implements ColumnComparison {
    final String compName = "!=";

    @Override
    public LinkedList<Integer> compare (Column col, String s) throws RuntimeException {
        LinkedList<Integer> rowIndexOfCol = new LinkedList<>();

        if(!col.columnType.equals(String.class)) {
            throw new RuntimeException("ERROR: Can't compare " + col.columnNameType + " with " + s);

        } else {
            for(int i = 0; i < col.size(); i++ ) {
                if(!col.get(i).equals(s)) {
                    rowIndexOfCol.addLast(i);
                }
            }
        }

        return rowIndexOfCol;
    }

    @Override
    public LinkedList<Integer> compare (Column col, Number num) throws RuntimeException {
        LinkedList<Integer> rowIndexOfCol = new LinkedList<>();

        if(!(col.columnType.equals(Float.class)||col.columnType.equals(Integer.class))) {
            throw new RuntimeException("ERROR: Can't compare " + col.columnNameType + " with " + num.toString());
        } else {
            for(int i = 0; i < col.size(); i++ ) {
                if(!col.get(i).equals(num)) {
                    rowIndexOfCol.addLast(i);
                }
            }
        }

        return rowIndexOfCol;
    }


    @Override
    public LinkedList<Integer> compare (Column col1, Column col2) throws RuntimeException {
        LinkedList<Integer> rowIndexOfCol = new LinkedList<>();

        if((col1.columnType.equals(String.class))&&!(col2.columnType.equals(String.class))) {
            throw new RuntimeException("ERROR: Can't compare " + col1.columnNameType + " with " + col2.columnNameType);
        } else if (!(col1.columnType.equals(String.class))&&(col2.columnType.equals(String.class))){
            throw new RuntimeException("ERROR: Can't compare " + col1.columnNameType + " with " + col2.columnNameType);
        } else {
            for(int i = 0; i < col1.size(); i++) {
                if(!col1.get(i).equals(col2.get(i))) {
                    rowIndexOfCol.addLast(i);
                }
            }
        }

        return rowIndexOfCol;
    }

    @Override
    public String toString() {
        return compName;
    }
}