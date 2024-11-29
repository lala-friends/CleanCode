package main.java.chp10.after;

import main.java.chp10.Column;

import java.util.Objects;

abstract public class Sql {
    private final String table;
    private final Column[] columns;

    public Sql(final String table, final Column[] columns) {
        this.table = Objects.requireNonNull(table);
        this.columns = Objects.requireNonNull(columns);
    }

    abstract public String generate();

    private String columnList(Column[] columns) {
        throw new RuntimeException();
    }
}
