package main.java.chp10.after;

import main.java.chp10.Column;

public class CreateSql extends Sql {
    public CreateSql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        throw new RuntimeException();
    }
}
