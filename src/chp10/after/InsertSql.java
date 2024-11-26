package chp10.after;

import chp10.Column;

public class InsertSql extends Sql{
    public InsertSql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        throw new RuntimeException();
    }

    private String valuesList(Object[] values, final Column[] columns) {
        throw new RuntimeException();
    }
}
