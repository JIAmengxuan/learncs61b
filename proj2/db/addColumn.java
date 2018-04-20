package db;

import jh61b.junit.In;

import java.util.Iterator;

/**
 * Created by John on 2018/2/5.
 */
public class addColumn implements ColumnOperator {
    final String opName = "ADD";

    @Override
    public Column Operator(Column c1, Column c2) throws RuntimeException {
        if(c1.columnType.getGenericSuperclass().equals(Number.class)) {
            if(c2.columnType.getGenericSuperclass().equals(Number.class)) {
                if(c1.columnType == Float.class || c2.columnType == Float.class) {
                    //至少有一个是column<Float>
                    Column result = new Column(" float", Float.class);

                    Iterator iterator_c1 = c1.columnData.iterator();
                    Iterator iterator_c2 = c2.columnData.iterator();
                    while(iterator_c1.hasNext() && iterator_c2.hasNext()) {
                        //子类是可以cast成父类的，只不过会丢掉一些属性。
                        Float num1 = ((Number) iterator_c1.next()).floatValue();
                        Float num2 = ((Number) iterator_c2.next()).floatValue();
                        result.addLast( num1 + num2);
                    }
                    return result;
                } else {
                    //两个都是column<Integer>
                    Column result = new Column(" int", Integer.class);

                    Iterator iterator_c1 = c1.columnData.iterator();
                    Iterator iterator_c2 = c2.columnData.iterator();
                    while(iterator_c1.hasNext() && iterator_c2.hasNext()) {
                        result.addLast((Integer) iterator_c1.next() + (Integer) iterator_c2.next());
                    }
                    return result;
                }
            } else {
                //一个是<Number>一个是<String>
                throw new RuntimeException("ERROR: " + c1.columnNameType + " + " + c2.columnNameType + " is not allowed!");
            }
        } else if(c2.columnType == String.class) {
            //两个都是column<String>
            Column result = new Column(" string", String.class);

            Iterator iterator_c1 = c1.columnData.iterator();
            Iterator iterator_c2 = c2.columnData.iterator();
            while(iterator_c1.hasNext() && iterator_c2.hasNext()) {
                String tmp1 = (String) iterator_c1.next();
                String tmp2 = (String) iterator_c2.next();
                if(tmp1.equals("NOVALUE")) {
                    if(tmp2.equals("NOVALUE")) {
                        result.addLast("");
                    } else {
                        result.addLast(tmp1);
                    }
                } else if(tmp2.equals("NOVALUE")) {
                    result.addLast(tmp1);
                } else {
                    result.addLast(tmp1 + tmp2);
                }
            }
            return result;
        } else {
            throw new RuntimeException("ERROR: " + c1.columnNameType + " + " + c2.columnNameType + " is not allowed!");
        }
    }

    @Override
    public Column Operator(Column col, String s) throws RuntimeException {
        if(col.columnType == String.class) {
            Column result = new Column(col.columnNameType, String.class);
            Iterator iterator = col.columnData.iterator();

            while(iterator.hasNext()) {
                if(s.equals("NOVALUE")) {
                    result.addLast(iterator.next() + "");
                } else {
                    result.addLast(iterator.next() + s);
                }
            }
            return result;
        } else {
            throw new RuntimeException("ERROR: " + col.columnNameType + " + " + s +" is not allowed!");
        }
    }

    @Override
    public Column Operator(Column col, Number n) throws RuntimeException {
        if (col.columnType.getGenericSuperclass().equals(Number.class)) {
            if (col.columnType.equals(Float.class) || n.getClass() == Float.class) {
                //至少有一个是column<Float>
                Column result = new Column(" float", Float.class);
                for(Object value : col.columnData) {
                    if(value.getClass().equals(Integer.class)) {
                        value = ((Integer) value).floatValue();
                    }
                    result.addLast((Float) value + n.floatValue());
                }
                return result;
            } else {
                //两个都是column<Integer>
                Column result = new Column(" int", Integer.class);

                for (Object value : col.columnData){
                    result.addLast((Integer) value + (Integer) n);
                }
                return result;
            }
        } else {
            throw new RuntimeException("ERROR: " + col.columnNameType + " + " + " n is not allowed!");
        }
    }

    @Override
    public String toString() {
        return opName;
    }
}
