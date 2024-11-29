package main.java.chp10.after;

import main.java.chp10.Column;

public class SelectWithMatchSql extends Sql {
    public SelectWithMatchSql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        throw new RuntimeException();
    }
}
