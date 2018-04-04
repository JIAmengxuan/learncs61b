package db;

import java.util.LinkedList;

public class greaterThanOrEquals implements ColumnComparison {
    final String compName = ">=";

    @Override
    public LinkedList<Integer> compare (Column col, String s) throws RuntimeException {
        LinkedList<Integer> rowIndexOfCol = new LinkedList<>();

        if(!col.columnType.equals(String.class)) {
            throw new RuntimeException("ERROR: Can't compare " + col.columnNameType + " with " + s);
        } else {
            for(int i = 0; i < col.size(); i++ ) {
                if(((String)col.get(i)).compareTo(s) >= 0) {
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
                Object data = col.get(i);
                Float numData;
                Float fNum;
                if(col.columnType.equals(Integer.class)) {
                    numData = ((Integer) data).floatValue();
                } else {
                    numData = (Float) data;
                }
                if(num.getClass().equals(Integer.class)) {
                    fNum = num.floatValue();
                } else {
                    fNum = (Float) num;
                }
                if(numData.compareTo(fNum) >= 0) {
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
            if(col1.columnType.equals(String.class)) {
                for(int i = 0; i < col1.size(); i++) {
                    if(((String)col1.get(i)).compareTo(((String)col2.get(i))) >= 0)
                        rowIndexOfCol.addLast(i);
                }
            } else {
                for(int i = 0; i < col1.size(); i++) {
                    Float col1Data;
                    Float col2Data;
                    if(col1.columnType.equals(Integer.class)) {
                        col1Data = ((Integer) col1.get(i)).floatValue();
                    } else {
                        col1Data = (Float) col1.get(i);
                    }
                    if(col2.columnType.equals(Integer.class)) {
                        col2Data = ((Integer) col2.get(i)).floatValue();
                    } else {
                        col2Data = (Float) col2.get(i);
                    }
                    if(col1Data.compareTo(col2Data) >= 0) {
                        rowIndexOfCol.addLast(i);
                    }
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
