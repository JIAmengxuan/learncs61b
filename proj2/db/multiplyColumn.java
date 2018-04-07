package db;

import java.util.Iterator;

public class multiplyColumn implements ColumnOperator {
    final String opName = "Multiply";

    @Override
    public Column Operator(Column c1, Column c2) throws RuntimeException {
        if (c1.columnType.getGenericSuperclass().equals(Number.class) && c2.columnType.getGenericSuperclass().equals(Number.class)) {
            if (c1.columnType == Float.class || c2.columnType == Float.class) {
                //至少有一个是column<Float>
                Column result = new Column(" float", Float.class);

                Iterator iterator_c1 = c1.columnData.iterator();
                Iterator iterator_c2 = c2.columnData.iterator();
                while (iterator_c1.hasNext() && iterator_c2.hasNext()) {
                    result.addLast((Float) iterator_c1.next() * (Float) iterator_c2.next());
                }
                return result;
            } else {
                //两个都是column<Integer>
                Column result = new Column(" int", Integer.class);

                Iterator iterator_c1 = c1.columnData.iterator();
                Iterator iterator_c2 = c2.columnData.iterator();
                while (iterator_c1.hasNext() && iterator_c2.hasNext()) {
                    result.addLast((Integer) iterator_c1.next() * (Integer) iterator_c2.next());
                }
                return result;
            }
        } else {
            //存在一个是<String>
            throw new RuntimeException("ERROR: " + c1.columnNameType + " x " + c2.columnNameType + " is not allowed!");
        }
    }

    @Override
    public Column Operator(Column col, String s) throws RuntimeException {
        throw new RuntimeException("ERROR: " + col.columnNameType + " x " + s +" is not allowed!");
    }

    @Override
    public Column Operator(Column col, Number n) throws RuntimeException {
        if (col.columnType.getGenericSuperclass().equals(Number.class)) {
            if (col.columnType == Float.class || n.getClass() == Float.class) {
                //至少有一个是column<Float>
                Column result = new Column(" float", Float.class);

                for (Object value: col.columnData){
                    result.addLast((Float) value * (Float) n);
                }
                return result;
            } else {
                //两个都是column<Integer>
                Column result = new Column(" int", Integer.class);

                for (Object value : col.columnData) {
                    result.addLast((Integer) value * (Integer) n);
                }
                return result;
            }
        } else {
            throw new RuntimeException("ERROR: " + col.columnNameType + " x " + " n is not allowed!");
        }
    }

    @Override
    public String toString() {
        return opName;
    }
}
