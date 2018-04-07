package Test;

import db.*;

/**
 * Created by John on 2017/12/7.
 */
public class Test {
    //pass
    public static void testLoad(String s) {
        Database db = new Database();
        db.Load(s);
    }
    //pass
    public static void testPrint(String s) {
        Database db = new Database();
        db.Load(s);
        db.Print(s);
    }
    //pass
    public static void testStore(String s) {
        Database db = new Database();
        db.Load(s);
        db.Store(s);
    }
    //pass
    public static void testDrop(String s) {
        Database db = new Database();
        db.Drop(s);
    }
    //pass
    public static void testInsert(String s) {
        Database db = new Database();
        db.Load(s);
        db.Print(s);
        String literals = "'Whale xx' ,2018 , 7 ,7, 7";
        db.insertInto(s,literals);
        System.out.println("_______________插入后_________________");
        db.Print(s);
    }

    //pass
    public static void testOp() {
        Database db = new Database();
        Table records = db.Load("records");
        //ColumnOperator colOp = new addColumn();
        ColumnOperator colOp = new devideColumn();
        Table tmp = new Table("tmp", colOp.Operator(records.getColumn("Losses"), records.getColumn("Ties")));
        db.CreateTable("tmp", tmp);
        db.Print("tmp");
    }

    //pass
    public static void testJoins() {
        Database db = new Database();
        Table records = db.Load("t1");
        Table teams = db.Load("t3");
        String[] tablesNameTojoins = {"t1","t3"};
        Table ReJoinsTe = db.Joins(tablesNameTojoins);
        db.Print("t1");
        db.Print("t3");
        System.out.println("_______________Joins result_________________");
        ReJoinsTe.printTable();
    }

    //pass
    public static void testColExpr(String s) {
        columnExpr colExp = new columnExpr(s);
        colExp.printCE();
    }

    //pass
    public static void testConState(String s) {
        conState conSt = new conState(s);
        conSt.printCC();
    }

    public static void testSelect(String s) {
    //select City,Season,Wins/Losses as Ratio from teams,records where Ratio > 0, City > 'abnormal';
        Database db = new Database();
        db.Load("records");
        db.Load("teams");
        db.Print("records");
        db.Print("teams");
        String[] tablesNameTojoins = {"teams","records"};
        Table ReJoinsTe = db.Joins(tablesNameTojoins);
        ReJoinsTe.printTable();
        System.out.println("______________执行以下select语句_______________");
        System.out.println(s);
        s.replaceAll(" +", " ");
        db.CreateTable("select result",s);
        db.Print("select result");
        db.Store("select result");
    }

    public static void main (String[] args) {
        //Test.testLoad("teams");
        //Test.testPrint("t3");
        //Test.testStore("team");
        //Test.testDrop("teams");
        //Test.testInsert("records");
        //Test.testOp();
        //Test.testJoins();
        //Test.testColExpr("wins");
        //Test.testConState("last  >=    'dad' ");
        //Test.testSelect("select   City  ,  Season  ,  Wins  /  Losses   as   Ratio   from   teams  ,  records");
    }
}
