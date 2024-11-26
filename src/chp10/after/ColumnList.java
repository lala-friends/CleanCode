package chp10.after;

import chp10.Column;

public class ColumnList {
    private final Column[] columns;

    public ColumnList(Column[] columns) {
        this.columns = columns;
    }

    public String generate() {
        throw new RuntimeException();
    }
}
