package db;

import java.util.Iterator;

import static java.lang.Float.*;
public class devideColumn implements ColumnOperator {
    final String opName = "DEVIDE";

    @Override
    public Column Operator(Column c1, Column c2) throws RuntimeException {
        if (c1.columnType.getGenericSuperclass().equals(Number.class) && c2.columnType.getGenericSuperclass().equals(Number.class)) {
            if (c1.columnType == Float.class || c2.columnType == Float.class) {
                //至少有一个是column<Float>
                Column result = new Column(" float", Float.class);

                Iterator iterator_c1 = c1.columnData.iterator();
                Iterator iterator_c2 = c2.columnData.iterator();
                while (iterator_c1.hasNext() && iterator_c2.hasNext()) {
                    Number tmp1 = (Number) iterator_c1.next();
                    Number tmp2 = (Number) iterator_c2.next();
                    if(tmp2.equals(0.0f) || tmp2.equals(0)) {
                        if(tmp1.equals(0.0f) || tmp1.equals(0)) {
                            result.addLast(0.0f);
                        } else {
                            result.addLast(NaN);
                        }
                    } else {
                        result.addLast((Float) tmp1 / (Float) tmp2);
                    }
                }
                return result;
            } else {
                //两个都是column<Integer>
                Column result = new Column(" int", Integer.class);

                Iterator iterator_c1 = c1.columnData.iterator();
                Iterator iterator_c2 = c2.columnData.iterator();
                while (iterator_c1.hasNext() && iterator_c2.hasNext()) {
                    Integer tmp1 = (Integer) iterator_c1.next();
                    Integer tmp2 = (Integer) iterator_c2.next();
                    if(tmp2.equals(0)) {
                        if(tmp1.equals(0)) {
                            result.addLast(0);
                        } else {
                            result.addLast(NaN);
                        }
                    } else {
                        result.addLast(tmp1/tmp2);
                    }
                }
                return result;
            }
        } else {
            //存在一个是<String>
            throw new RuntimeException("ERROR: " + c1.columnNameType + " / " + c2.columnNameType + " is not allowed!");
        }
    }

    @Override
    public Column Operator(Column col, String s) throws RuntimeException {
        throw new RuntimeException("ERROR: " + col.columnNameType + " / " + s +" is not allowed!");
    }

    @Override
    public Column Operator(Column col, Number n) throws RuntimeException {
        if (col.columnType.getGenericSuperclass().equals(Number.class)) {
            if (col.columnType == Float.class || n.getClass() == Float.class) {
                //至少有一个操作数是<Float>
                Column result = new Column(" float", Float.class);

                Iterator iterator = col.columnData.iterator();
                while (iterator.hasNext()) {
                    Number tmp = (Number) iterator.next();
                    if(n.equals(0) || n.equals(0.0f)) {
                        if(tmp.equals(0) || tmp.equals(0)) {
                            result.addLast(0.0f);
                        } else {
                            result.addLast(NaN);
                        }
                    } else {
                        result.addLast((Float) tmp / (Float) n);
                    }
                }
                return result;
            } else {
                //两个都是column<Integer>
                Column result = new Column(" int", Integer.class);

                Iterator iterator = col.columnData.iterator();
                while (iterator.hasNext()) {
                    Integer tmp = (Integer) iterator.next();
                    if(n.equals(0)) {
                        if(tmp.equals(0)) {
                            result.addLast(0);
                        } else {
                            result.addLast(NaN);
                        }
                    } else {
                        result.addLast((Integer) tmp / (Integer) n);
                    }
                }
                return result;
            }
        } else {
            throw new RuntimeException("ERROR: " + col.columnNameType + " / " + " n is not allowed!");
        }
    }

    @Override
    public String toString() {
        return opName;
    }
}
