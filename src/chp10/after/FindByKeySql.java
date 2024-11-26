package chp10.after;

import chp10.Column;

public class FindByKeySql extends Sql {
    public FindByKeySql(String table, Column[] columns) {
        super(table, columns);
    }

    @Override
    public String generate() {
        return "";
    }
}
