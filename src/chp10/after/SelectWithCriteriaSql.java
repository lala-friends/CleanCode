package chp10.after;

import chp10.Column;

public class SelectWithCriteriaSql extends Sql {
    public SelectWithCriteriaSql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        throw new RuntimeException();
    }

    private String selectWithCriteria(String criteria) {
        throw new RuntimeException();
    }
}
