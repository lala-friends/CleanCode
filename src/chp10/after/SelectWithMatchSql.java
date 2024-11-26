package chp10.after;

import chp10.Column;

public class SelectWithMatchSql extends Sql {
    public SelectWithMatchSql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        throw new RuntimeException();
    }
}
