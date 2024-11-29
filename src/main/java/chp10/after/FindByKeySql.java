package main.java.chp10.after;

import main.java.chp10.Column;

public class FindByKeySql extends Sql {
    public FindByKeySql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        return "";
    }
}
