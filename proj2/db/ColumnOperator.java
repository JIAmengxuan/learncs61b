package db;

/**
 * Created by John on 2018/2/5.
 */
public interface ColumnOperator {

    Column Operator(Column c1, Column c2) throws RuntimeException;

    Column Operator(Column col, String s) throws RuntimeException;

    Column Operator(Column col, Number n) throws RuntimeException;
}
