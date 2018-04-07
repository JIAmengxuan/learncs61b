package db;

import java.util.LinkedList;

public interface ColumnComparison {

    LinkedList<Integer> compare(Column col, String s) throws RuntimeException;

    LinkedList<Integer> compare(Column col, Number n) throws RuntimeException;

    LinkedList<Integer> compare(Column col1, Column col2) throws RuntimeException;
}
