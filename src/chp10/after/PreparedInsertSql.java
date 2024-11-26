package chp10.after;

import chp10.Column;

public class PreparedInsertSql extends Sql {
    public PreparedInsertSql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        throw new RuntimeException();
    }

    private String placeholderList(Column[] columns) {
        throw new RuntimeException();
    }
}
