package db;

import java.util.regex.Pattern;

public class columnExpr {
    //<operand0> <arithmetic operator> <operand1> as <column alias>匹配的类型的Pattern对象
    //正则中将[]中的所有理解为需要匹配的字符，所以[]里面的要写成java中的字符格式，即\n表示换行，不需要额外转义；
    // 因为java中\本身就是转义符，对java的解释器来说，在反斜线字符(\)后的字符有特殊的含义，例如：\n表示一个转义字符：换行。
    // 在java中，与regex有关的包，并不都能理解和识别反斜线字符(\)为regex的转义符，
    // 即当(\任意字符）在java字符解释时没有特殊含义，即\没有被java解释器识别为字符的转义符，才可进而被正则识别为转义符，所以
    // 为避免这一点，即为了让反斜线字符(\)在模式对象中被完全地传递，应该用双反斜线字符(\\)。
    // 可以理解为，为了让\d准确的转换为正则中的\d，即代表数字[0-9]，需要先对字符串形式的\d进行转义，即将用\将\d中的\转义为普通反斜线，
    // 则(普通\)d变不会被java字符串识别为转义，则传递到正则识别时即为\d，此时将\d识别为正则中的特殊符号，达到目的。
    private static final Pattern operator_PATTERN      = Pattern.compile("([+]|[-]|[*]|[/])"),
                                 alias_PATTERN         = Pattern.compile("(as){1}"),
                                 ALL_COLUMN_PATTERN    = Pattern.compile("[*]\\s+"),
                                 singleOperand_PATTERN = Pattern.compile("([^*]+\\s?){1}"),
                                 twoOperand_PATTERN    = Pattern.compile(".+\\s?([+]|[-]|[*]|[/])\\s?.+\\s?(as)\\s?.+");

    ColumnOperator colOp;
    String[] cols = new String[2];//储存col的name
    String result;

    columnExpr(String colExpr) {
        colExpr = colExpr.replaceAll( " +"," ");
        colExpr = colExpr.trim();
        if(twoOperand_PATTERN.matcher(colExpr).matches()) {
            colExpr = colExpr.replace(" ", "");
            cols = operator_PATTERN.split(colExpr, 2);
            String[] temp = alias_PATTERN.split(cols[1], 2);
            cols[1] = temp[0];
            result = temp[1];
            if(colExpr.matches(".+\\+.+")) {
                colOp = new addColumn();
            } else if(colExpr.matches(".+\\-.+")) {
                colOp = new minusColumn();
            } else if(colExpr.matches(".+\\*.+")) {
                colOp = new multiplyColumn();
            } else if(colExpr.matches(".+\\/.+")){
                colOp = new devideColumn();
            } else {
                throw new RuntimeException("ERROR: Invalid COLUMN_EXPRESSION: " +colExpr);
            }
        } else if (singleOperand_PATTERN.matcher(colExpr).matches()) {
            colOp = null;
            cols[0] = colExpr.replace(" ", "");
            result = "SINGLE";
        } else if (ALL_COLUMN_PATTERN.matcher(colExpr).matches()){
            colOp = null;
            cols = null;
            result = "ALL";
        } else {
            throw new RuntimeException("ERROR: Invalid COLUMN_EXPRESSION: " + colExpr);
        }
    }
}
