package db;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;

public class Database {
    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
                                COMMA = "\\s*,\\s*",
                                AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
                                 LOAD_CMD   = Pattern.compile("load " + REST),
                                 STORE_CMD  = Pattern.compile("store " + REST),
                                 DROP_CMD   = Pattern.compile("drop table " + REST),
                                 INSERT_CMD = Pattern.compile("insert into " + REST),
                                 PRINT_CMD  = Pattern.compile("print " + REST),
                                 SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" + "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
                                 SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                                         "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                                         "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                                         "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
                                 CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" + SELECT_CLS.pattern()),
                                 INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" + "\\s*(?:,\\s*.+?\\s*)*)");


    private String name = "xx";
    private HashMap<String, Table> tables;

    public Database() {
        // YOUR CODE HERE
        tables = new HashMap<>();
        In in = new In("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\Database_" + name + ".tbl");

        while (!in.isEmpty()) {
            String eachTableName = in.readLine();
            tables.put(eachTableName, new Table(eachTableName));
        }
    }

    private void CreateTable (String query) throws RuntimeException{
        if(CREATE_NEW.matcher(query).matches()) {
            String[] nameAndOthers = query.split("\\(|\\)");
            String tableName = nameAndOthers[0].trim();

            if(tables.containsKey(tableName))
                throw new RuntimeException("ERROR: " + tableName + " is already exist in Database.");

            tables.put(tableName, new Table(tableName));
            getTable(tableName).initializeColumns(nameAndOthers[1]);
        } else if(CREATE_SEL.matcher(query).matches()){
            String[] nameAndOthers = query.split("\\s+as select\\s+");
            Table selectResult = Select(nameAndOthers[1]);
            selectResult.setName(nameAndOthers[0].trim());
            CreateTable(selectResult.getName(), selectResult);
        } else {
            throw new RuntimeException("ERROR: Invalid CREATE_COMMAND: create table " + query);
        }

    }

    private void CreateTable(String tableName, Table selectResult) throws RuntimeException{
        tableName = tableName.trim();

        if(tables.containsKey(tableName))
            throw new RuntimeException("ERROR: " + tableName + " is already exist in Database.");

        selectResult.setName(tableName);
        tables.put(selectResult.getName(), selectResult);
    }

    private Table getTable(String tableName) throws RuntimeException {
        tableName = tableName.trim();

        if(!tables.containsKey(tableName))
            throw new RuntimeException("ERROR: " + tableName + " is not in Database.");

        return tables.get(tableName);
    }

    private void Load(String tableName) throws RuntimeException {
        tableName = tableName.trim();

        Table tableToLoad = getTable(tableName);
        tableToLoad.loadTable(tableName);
    }

    private void Print(String tableName) throws RuntimeException {
        tableName = tableName.trim();

        if(getTable(tableName).isEmpty())
            throw new RuntimeException("ERROR: " + tableName + " is not loaded.");
        
        getTable(tableName).printTable();
    }

    private void Store(String tableName) throws RuntimeException {
        tableName = tableName.trim();
        Table newTable;

        //要保证newTable在当前内存里是存在的，即loaded；
        try {
            newTable = getTable(tableName);
        } catch (RuntimeException e) {
            throw new RuntimeException("ERROR: " + tableName + " is not exist.");
        }

        //写成.tbl文件;
        newTable.storeTable();

        //在Database_xx.tbl中更新上tableName；
        In in = new In("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\Database_" + name + ".tbl");
        String TablesName = in.readAll();
        TablesName = TablesName + "\n" + tableName;
        Out out = new Out("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\Database_" + name + ".tbl");
        out.print(TablesName);
    }

    private void Drop(String tableName) throws RuntimeException {
        In in = new In("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\Database_" + name + ".tbl");
        String TablesName = "";
        String eachTableName = in.readLine();
        tableName = tableName.trim();

        if(!tables.containsKey(tableName))
            throw new RuntimeException("ERROR: " + tableName + " is not in Database.");

        while(eachTableName != null) {
            if(eachTableName.equals(tableName)) {
                eachTableName = in.readLine();
            }
            else {
                TablesName = TablesName + eachTableName + "\n";
                eachTableName = in.readLine();
            }
        }
        Out out = new Out("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\Database_" + name + ".tbl");
        out.print(TablesName);
    }

    private void insertInto(String tableNameAndLiterals) throws RuntimeException {
        if(!INSERT_CLS.matcher(tableNameAndLiterals).matches())
            throw new RuntimeException("ERROR: Invalid INSERT_INTO_COMMAND: insert into " + tableNameAndLiterals);
        String[] tableNameAndValues = tableNameAndLiterals.split("(\\s{1}values\\s{1})");
        String tableName = tableNameAndValues[0].trim();
        String[] literals = tableNameAndValues[1].split(COMMA);
        if(!getTable(tableName).isEmpty()) {
            getTable(tableName).insertTable(literals);
        } else {
            throw new RuntimeException("ERROR: " + tableName + " is not loaded.");
        }
    }

    private Table Joins(String[] tablesName) throws RuntimeException{
        //循环赋值
        Table[] ts = new Table[tablesName.length];
        for(int i = 0; i < tablesName.length; i++) {
            tablesName[i] = tablesName[i].trim();
        }
        for(int i = 0; i < tablesName.length; i ++) {
            if (getTable(tablesName[i]).isEmpty()){
                throw new RuntimeException("ERROR: "+ tablesName[i] + " is not loaded.");
            } else {
                ts[i] = getTable(tablesName[i]);
            }
        }

        //循环积
        Table result = new Table("");
        if(ts.length == 1) {
            result = ts[0];
        } else {
            for(Table table : ts) {
                result = Table.tableJoins(result , table);
            }
        }

        return result;
    }

    private Table Select (String s) throws RuntimeException {
        String[] strings = s.split("(\\s{1}from\\s{1}|\\s{1}where\\s{1})");
        String tableNames = strings[1];
        String columnExpression = strings[0];

        String[] tablesName = tableNames.split(COMMA);
        Table joinResult = Joins(tablesName);

        String[] colExpressions = columnExpression.split(COMMA);
        columnExpr[] colExprs = new columnExpr[colExpressions.length];
        for(int i = 0; i < colExprs.length; i ++) {
            colExprs[i] = new columnExpr(colExpressions[i]);
        }

        if(strings.length == 3) {
            String conStates = strings[2];
            String[] conditionalStatements = conStates.split(AND);
            conState[] conStatesInstance = new conState[conditionalStatements.length];
            for(int i = 0; i < conStatesInstance.length; i ++) {
                conStatesInstance[i] = new conState(conditionalStatements[i]);
            }
            return Table.select(colExprs, joinResult, conStatesInstance);
        } else {
            return Table.select(colExprs, joinResult, null);
        }
    }

    private void eval(String query) throws RuntimeException{
        query = query.replaceAll(" +", " ");
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            CreateTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            Load(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            Store(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            Drop(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            insertInto(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            Print(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            Table selectResult = Select(m.group(1));
            selectResult.printTable();
        } else {
            throw new RuntimeException("Malformed query: "+ query);
        }
    }

    public String transact(String query) {
        if (query.isEmpty()) {
            System.err.println("Expected a single query argument");
            return "ERROR";
        }

        String queryResult = "";
        try {
            eval(query);
        } catch (RuntimeException e) {
            queryResult = e.getMessage();
        }
        return queryResult;
    }
}
