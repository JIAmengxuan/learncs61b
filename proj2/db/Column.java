package db;

import java.util.LinkedList;

/**
 * Created by John on 2017/11/27.
 */
class Column implements Cloneable{
    String columnNameType;
    Class columnType;//泛型补偿
    LinkedList columnData;

    Column(String n, Class t) {
        columnNameType = n.trim();
        columnType = t;
        columnData = new LinkedList();
    }

    @Override
    public Column clone() {
        Column result =  null;
        try {
            result = (Column) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        /*elements of columnData is immutable（String,Integer, or Float）,
         * so just use the Linkedlist.clone() to get the shallow copy:
         * return a new linkedlist instance contains references of the former linkedlist's elements.
         */
        result.columnData = (LinkedList) columnData.clone();
        return result;
    }

    String getColumnNameType() {
        return columnNameType;
    }

    Object get(int i) {
        return columnData.get(i);
    }

/**
 * Returns the index of the first occurrence of the specified element
 * in this list, or -1 if this list does not contain the element.
 */
    /*Integer indexOf(Object o) {
        return columnData.indexOf(o);
    }*/

    void addLast(Object value) {
        if(value instanceof Column) {
            for(Object v : ((Column) value).columnData) {
                columnData.addLast(v);
            }
        } else {
            columnData.addLast(value);
        }
    }

    void removeAllDatas() {
        columnData.clear();
    }


    Integer size() {
        return columnData.size();
    }

    Class getGenericType() {
        return columnType;
    }

    void printColumnNT() {
        System.out.print(columnNameType);
    }

    void setNameType(String columnName) {
        columnNameType = columnName + " ";
        if(columnType.equals(String.class)) {
            columnNameType = columnNameType + "string";
        } else if(columnType.equals(Integer.class)) {
            columnNameType = columnNameType + "int";
        } else {
            columnNameType = columnNameType + "float";
        }
    }

    void resetColByRowNums(LinkedList<Integer> rowNums) {
        LinkedList llist = new LinkedList();
        for(Integer i : rowNums) {
            llist.addLast(columnData.get(i));
        }

        removeAllDatas();
        for(Object o : llist) {
            addLast(o);
        }
    }
}