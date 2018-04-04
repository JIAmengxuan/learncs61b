package db;

import java.util.regex.Pattern;

public class conState {

    //String, int, float类型的正则表达式
    private static final String STRING_REGEX = "('.+')|(NOVALUE)",
                                INT_REGEX    = "-?\\d+",
                                FLOAT_REGEX  = "(-?\\d+\\.\\d+)|(NaN)",
                              LITERALS_REGEX = STRING_REGEX +"|"+ INT_REGEX +"|"+ FLOAT_REGEX;


    //<column0 name> <comparison> <column1 name> and <column name> <comparison> <literal>匹配的类型的Pattern对象
    //要求 <comparison> 前后都必须有空格
    private static final Pattern Comparison_PATTERN  = Pattern.compile("\\s([=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=])\\s"),
                                   twoColumn_PATTERN = Pattern.compile(".+\\s([=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=])\\s.+\\s"),
                                singleColumn_PATTERN = Pattern.compile(".+\\s([=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=])\\s(" + LITERALS_REGEX +")\\s?");

    ColumnComparison colCom;
    String[] cols;//储存col的name
    String result = "";

    conState (String conStatement) throws RuntimeException {
        conStatement = conStatement.replaceAll( " +"," ");
        if(singleColumn_PATTERN.matcher(conStatement).matches()) {
            cols = Comparison_PATTERN.split(conStatement);
            result = "UNARY";
        } else if (twoColumn_PATTERN.matcher(conStatement).matches()){
            cols = Comparison_PATTERN.split(conStatement);
            result = "BINARY";
        } else {
            throw new RuntimeException("ERROR: Invalid CONDITIONAL_STATEMENT: " + conStatement);
        }

        for(int i = 0; i < cols.length; i++) {
            cols[i] = cols[i].trim();
        }
        if (conStatement.matches(".+[=]{2}.+")) {
            colCom = new doubleEquals();
        } else if (conStatement.matches(".+(\\!\\=){1}.+")) {
            colCom = new notEquals();
        } else if (conStatement.matches(".+(\\<[^=]){1}.+")) {
            colCom = new lessThan();
        } else if (conStatement.matches(".+(\\<\\=){1}.+")) {
            colCom = new lessThanOrEquals();
        } else if (conStatement.matches(".+(\\>[^=]){1}.+")) {
            colCom = new greaterThan();
        } else if (conStatement.matches(".+(\\>\\=){1}.+")) {
            colCom = new greaterThanOrEquals();
        } else {
            throw new RuntimeException("ERROR: Invalid CONDITIONAL_STATEMENT: " + conStatement);
        }
    }
}
