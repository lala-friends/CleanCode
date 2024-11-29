package main.java.chp10.before;

import main.java.chp10.Column;
import main.java.chp10.Criteria;

import java.util.Objects;

public class Sql {
    private final String table;
    private final Column[] columns;

    public Sql(final String table, final Column[] columns) {
        this.table = Objects.requireNonNull(table);
        this.columns = Objects.requireNonNull(columns);
    }

    public String create() {
        throw new RuntimeException();
    }

    public String insert(Object[] fields) {
        throw new RuntimeException();
    }

    public String selectAll() {
        throw new RuntimeException();
    }

    public String findByKey(String keyColumn, String keyValue) {
        throw new RuntimeException();
    }

    public String select(Column column, String pattern) {
        throw new RuntimeException();
    }

    public String select(Criteria criteria) {
        throw new RuntimeException();
    }

    public String preparedInsert() {
        throw new RuntimeException();
    }

    private String columnList(Column[] columns) {
        throw new RuntimeException();
    }

    private String valuesList(Object[] values, final Column[] columns) {
        throw new RuntimeException();
    }

    private String selectWithCriteria(String criteria) {
        throw new RuntimeException();
    }

    private String placeholderList(Column[] columns) {
        throw new RuntimeException();
    }

}
