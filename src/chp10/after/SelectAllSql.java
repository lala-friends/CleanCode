package chp10.after;

import chp10.Column;

public class SelectAllSql extends Sql{
    public SelectAllSql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        throw new RuntimeException();
    }
}
