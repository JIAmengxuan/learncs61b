package db;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by John on 2017/11/18.
 */
public class Table implements Cloneable{
    //换行(\n)，逗号(,)，的正则表达式
    private static final String NEWLINE = "\\s*\\n",
                                  COMMA = "\\s*,\\s*";
    //String, int, float类型的正则表达式
    private static final String STRING_REGEX = "('.+')|(NOVALUE)",
                                INT_REGEX    = "-?\\d+",
                                FLOAT_REGEX  = "(-?\\d+\\.\\d+)|(NaN)";

    //String, int, float类型的Pattern对象
    private static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX),
                                 INT_PATTERN    = Pattern.compile(INT_REGEX),
                                 FLOAT_PATTERN  = Pattern.compile(FLOAT_REGEX);

    //"name string","name int","name float"以及name匹配的类型的Pattern对象
    private static final Pattern STRING_NAME_PATTERN = Pattern.compile(".+\\s{1}(string)"),
                                 INT_NAME_PATTERN    = Pattern.compile(".+\\s{1}(int)"),
                                 FLOAT_NAME_PATTERN  = Pattern.compile(".+\\s{1}(float)");


    private String tableName;
    private LinkedList<Column> columns;
    private Integer rowNum;

    private Table() {
        tableName = "";
        columns = new LinkedList<>();
        rowNum = 0;
    }

    Table(String name) {
        tableName = name;
        columns = new LinkedList<>();
        rowNum = 0;
    }

    public Table(String name, Column col1st) {
        tableName = name;
        rowNum = col1st.size();
        columns = new LinkedList<>();
        columns.addLast(col1st);
    }

    private void addColumn(Column col) {
        if(isEmpty()) {
            rowNum = col.size();
        }
        columns.addLast(col.clone());
    }

    private void addColumns(Table t) {
        for(Column col : t.columns) {
            addColumn(col);
        }
    }

    private void removeAllDatas() {
        for(Column col : columns) {
            col.removeAllDatas();
        }
        rowNum = 0;
    }

    private int getRowNum() {
        return this.rowNum;
    }

    //return the (col:x, row:y) element
    private Object get(int x, int y) {
        return columns.get(x).get(y);
    }

    String getName() {
        return this.tableName;
    }

    LinkedList<Column> getColumns() {
        return columns;
    }

    boolean isEmpty() {
        return this.columns.isEmpty();
    }

    void setName(String s) {
        tableName = s;
    }

    private static Object changeType (String s) throws RuntimeException {
        s = s.trim();
        if(STRING_PATTERN.matcher(s).matches())
            return s;
        if(FLOAT_PATTERN.matcher(s).matches())
            if(s.equals("NaN")) {
            return Float.NaN;
            } else {
                return Float.parseFloat(s);
            }
        if(INT_PATTERN.matcher(s).matches())
            return Integer.parseInt(s);
        else
            throw new RuntimeException("ERROR: Invalid changeType.");
    }

    private boolean matchType (String[] literals) throws RuntimeException {
        boolean result = true;
        Iterator<Column> eachColumn = this.columns.iterator();
        for(int i = 0; i < literals.length; i ++) {
            Class columnGenericType = eachColumn.next().getGenericType();
            Class litClz = Table.changeType(literals[i]).getClass();
            if (!columnGenericType.equals(litClz)) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public Table clone() {
        Table t = null;
        try {
            t = (Table) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        t.columns = new LinkedList<>();
        for(Column clo : columns) {
            t.addColumn(clo.clone());
        }
        return t;
    }


    void initializeColumns (String namesAndTypes) throws RuntimeException{
        namesAndTypes = namesAndTypes.trim();
        namesAndTypes = namesAndTypes.replaceAll(" +"," ");
        Column col;
        String[] tableHeads = Pattern.compile(COMMA).split(namesAndTypes);
        for(String tableHead : tableHeads ) {
            if (STRING_NAME_PATTERN.matcher(tableHead).matches())
                col = new Column(tableHead, String.class);
            else if (INT_NAME_PATTERN.matcher(tableHead).matches())
                col = new Column(tableHead, Integer.class);
            else if (FLOAT_NAME_PATTERN.matcher(tableHead).matches())
                col = new Column(tableHead, Float.class);
            else {
                throw new RuntimeException("ERROR: Invalid Column Name and Type: " + tableHead);
            }
            this.addColumn(col);
        }
    }

    void loadTable (String tn) throws RuntimeException {
        In in = new In("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\" + tn + ".tbl");
        while (!in.isEmpty()) {

            //初始化Columns
            String firstLine = in.readLine();
            this.initializeColumns(firstLine);

            //将数据分别填充在每个列对象中
            Scanner theRestLines = new Scanner(in.readAll()).useDelimiter(COMMA +"|"+ NEWLINE);
            int i = 0;
            while(i < columns.size() && theRestLines.hasNext()) {
                columns.get(i).addLast(Table.changeType(theRestLines.next()));
                i ++;
                if(i == columns.size()) {
                    i =0;
                    rowNum ++;
                }
            }
        }
    }

    public void printTable() {
        Iterator<Column> tableIterator = this.columns.iterator();
        while (tableIterator.hasNext()) {
            tableIterator.next().printColumnNT();
            if (!tableIterator.hasNext()) {
                System.out.println();
                break;
            }
            System.out.print(",");
        }
        for (int i = 0; i < this.getRowNum(); i ++) {
            Iterator<Column> colIterator = this.columns.iterator();
            while (colIterator.hasNext()) {
                System.out.print(colIterator.next().get(i));
                if (!colIterator.hasNext()) {
                    System.out.println();
                    break;
                }
                System.out.print(",");
            }
        }
    }

    void storeTable () {
        Out out = new Out("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\" + tableName + ".tbl");
        Iterator<Column> tableIterator = this.columns.iterator();
        while (tableIterator.hasNext()) {
            out.print(tableIterator.next().getColumnNameType());
            if (!tableIterator.hasNext()) {
                out.println();
                break;
            }
            out.print(",");
        }
        for (int i = 0; i < this.getRowNum(); i ++) {
            Iterator<Column> colIterator = this.columns.iterator();
            while (colIterator.hasNext()) {
               out.print(colIterator.next().get(i));
                if (!colIterator.hasNext()) {
                    out.println();
                    break;
                }
                out.print(",");
            }
        }
    }

    void insertTable(String[] literals) throws RuntimeException {
        if(literals.length != columns.size()||!this.matchType(literals)) {
            throw new RuntimeException("ERROR: Invalid literals or literals'type incompatible");
        }

        Iterator<Column> tableIterator = this.columns.iterator();
        for(int i = 0; tableIterator.hasNext() && i < literals.length; i ++) {
            tableIterator.next().addLast(Table.changeType(literals[i]));
        }
        rowNum ++;
    }

    private Column getColumn(String columnName) throws RuntimeException {
        columnName = columnName.trim();
        Iterator<Column> columnI = this.columns.iterator();
        Column result;
        while(columnI.hasNext()) {
            Pattern NAME_PATTERN = Pattern.compile(columnName + "\\s{1}((string)|(int)|(float)){1}");
            result = columnI.next();
            if(NAME_PATTERN.matcher(result.columnNameType).matches())
                return result;
        }
        throw new RuntimeException("ERROR: Column <" + columnName + ">se not exist.");
    }

    static Table tableJoins(Table t1, Table t2) throws RuntimeException {
        if(t1.isEmpty()) {
            if(t2.isEmpty()) {
                throw new RuntimeException("ERROR: can't joins with two EMPTY TABLE");
            } else {
                return t2;
            }
        } else if(t2.isEmpty()) {
            return t1;
        } else {
            //两非空table取积
            Table result = new Table(t1.getName()+" joins "+t2.getName());
            boolean noSameColumn = true;
            for(Column c1 : t1.columns) {
                for(Column c2 : t2.columns) {
                    if(c1.columnNameType.equals(c2.columnNameType)) {
                        noSameColumn = false;
                        break;
                    }
                }
                if(!noSameColumn) break;
            }
            //没有相同的Column
            if(noSameColumn) {
                Integer nTimesSelf1 = t2.rowNum;
                Integer nTimesSelf2 = t1.rowNum;

                Table newT1 = t1.clone();
                newT1.nTimesSelf(nTimesSelf1);

                Table newT2 = t2.clone();
                newT2.removeAllDatas();
                for (int i = 0; i < t2.rowNum; i++) {
                    for(int j = 0; j < newT2.columns.size(); j ++) {
                        for(int k = 0; k < nTimesSelf2; k ++) {
                            newT2.columns.get(j).addLast(t2.get(j, i));
                        }
                    }
                }
                newT2.rowNum = newT1.rowNum;
                result.addColumns(newT1);
                result.addColumns(newT2);
            } else {
                //存在相同的列
                LinkedList<Column> t1CfColumns = new LinkedList<>();
                LinkedList<Column> t2CfColumns = new LinkedList<>();
                Table newT1 = t1.clone();
                Table newT2WithoutCF = t2.clone();
                //记录相同列
                for(int i = 0; i < newT1.columns.size(); i++) {
                    for(int j = 0; j < newT2WithoutCF.columns.size(); j++) {
                        Column t1CfCol = newT1.columns.get(i).clone();
                        Column t2CfCol = newT2WithoutCF.columns.get(j).clone();
                        if(t1CfCol.getColumnNameType().equals(t2CfCol.getColumnNameType())) {
                            t1CfColumns.add(t1CfCol);
                            t2CfColumns.add(t2CfCol);
                            newT2WithoutCF.columns.remove(j);
                            break;
                        }
                    }
                }
                //记录相同列中相同元素的下标
                LinkedList<Integer> t1CfRows = new LinkedList<>();
                LinkedList<Integer> t2CfRows = new LinkedList<>();

                Column t1CfCol = t1CfColumns.removeFirst();
                Column t2CfCol = t2CfColumns.removeFirst();
                for(Object o : t1CfCol.columnData) {
                    for(Object ob : t2CfCol.columnData) {
                        if(o.equals(ob)) {
                            //t1CfCol.columnData中没有重复元素，故可以使用indexOf()方法
                            t1CfRows.add(t1CfCol.columnData.indexOf(o));
                            //t2CfCol.columnData中没有重复元素，故可以使用indexOf()方法
                            t2CfRows.add(t2CfCol.columnData.indexOf(ob));
                        }
                    }
                }

                while(!t1CfColumns.isEmpty() && !t2CfColumns.isEmpty()) {
                    Column t1col = t1CfColumns.removeFirst();
                    Column t2col = t2CfColumns.removeFirst();
                    for(int i : t1CfRows) {
                        int j = t2CfRows.get(t1CfRows.indexOf(i));
                        if(!t1col.get(i).equals(t2col.get(j))) {
                            t1CfRows.remove(t1CfRows.indexOf(i));
                            t2CfRows.remove(t2CfRows.indexOf(j));
                        }
                    }
                }
                //赋值新的table
                result.addColsByRows(newT1, t1CfRows);
                result.addColsByRows(newT2WithoutCF, t2CfRows);
            }
            return result;
        }
    }

    /**add the specific rows(rowsNumber) of t to this table;
    /*the size of rowsNumber should equals to this.rowNum, or this.rowNum == 0;
     */
    private void addColsByRows(Table t, LinkedList<Integer> rowsNumber) {
        for(Column col1 : t.columns) {
            Column resultCol = new Column(col1.columnNameType, col1.columnType);
            for(Integer i : rowsNumber) {
                resultCol.addLast(col1.get(i));
            }
            this.addColumn(resultCol);
        }
        rowNum = rowsNumber.size();
    }

    private void nTimesSelf(Integer n) {
        Table tmp = this.clone();
        for(int i = 1; i < n; i++) {
            for(Column col : this.columns) {
                col.addLast(tmp.columns.get(this.columns.indexOf(col)));
            }
        }
        rowNum = rowNum * n;
    }

    static Table select(columnExpr[] colExprs, Table joinResult, conState[] conStates) throws RuntimeException {
        Table resultOfSelect = new Table();

        for(columnExpr colExpr : colExprs) {
            switch (colExpr.result) {
                case "ALL":
                    resultOfSelect.addColumns(joinResult);
                    break;

                case "SINGLE":
                    resultOfSelect.addColumn(joinResult.getColumn(colExpr.cols[0]));
                    break;

                default:
                    Column columnToAdd;
                    if(colExpr.cols[1].matches(INT_REGEX+"|"+FLOAT_REGEX)) {
                        columnToAdd = colExpr.colOp.Operator(joinResult.getColumn(colExpr.cols[0]), (Number) Table.changeType(colExpr.cols[1]));
                    } else if(colExpr.cols[1].matches(STRING_REGEX)) {
                        columnToAdd = colExpr.colOp.Operator(joinResult.getColumn(colExpr.cols[0]), (String) Table.changeType(colExpr.cols[1]));
                    } else {
                        columnToAdd = colExpr.colOp.Operator(joinResult.getColumn(colExpr.cols[0]), joinResult.getColumn(colExpr.cols[1]));
                    }
                    columnToAdd.setNameType(colExpr.result);
                    resultOfSelect.addColumn(columnToAdd);
                    break;
            }
        }

        if(conStates != null) {
            LinkedList<Integer> conStatesResult = new LinkedList<>();
            for (conState conState : conStates) {
                switch (conState.result) {
                    case "BINARY":
                        conStatesResult.addAll(conState.colCom.compare(resultOfSelect.getColumn(conState.cols[0]), resultOfSelect.getColumn(conState.cols[1])));
                        break;

                    case "UNARY":
                        LinkedList<Integer> indexToAdd;
                        if (conState.cols[1].matches(INT_REGEX + "|" + FLOAT_REGEX)) {
                            indexToAdd = conState.colCom.compare(resultOfSelect.getColumn(conState.cols[0]), (Number) Table.changeType(conState.cols[1]));
                        } else {
                            indexToAdd = conState.colCom.compare(resultOfSelect.getColumn(conState.cols[0]), (String) Table.changeType(conState.cols[1]));
                        }
                        conStatesResult.addAll(indexToAdd);
                        break;
                }
            }

            //conStatesResult中重复次数为条件个数的元素即为conditional statements选择的结果；
            HashMap<Integer, Integer> hashMap = new HashMap<>();
            for (Integer i : conStatesResult) {
                if (hashMap.containsKey(i)) {
                    Integer value = hashMap.get(i) + 1;
                    hashMap.put(i, value);
                } else {
                    hashMap.put(i, 1);
                }
            }
            conStatesResult.clear();
            for (HashMap.Entry<Integer, Integer> entry : hashMap.entrySet()) {
                if (entry.getValue().equals(conStates.length)) {
                    conStatesResult.add(entry.getKey());
                }
            }

            resultOfSelect.resetTableByRowNums(conStatesResult);
        }
        return resultOfSelect;
    }

    private void resetTableByRowNums(LinkedList<Integer> rowNums) {
        for(Column col : columns) {
            col.resetColByRowNums(rowNums);
        }
        rowNum = rowNums.size();
    }
}
