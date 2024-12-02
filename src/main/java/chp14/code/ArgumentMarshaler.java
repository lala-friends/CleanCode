package main.java.chp14.code;

public class ArgumentMarshaler {
    private boolean booleanValue = false;
    private String stringValue = "";
    private Integer integerValue = 0;

    public boolean getBoolean() {
        return booleanValue;
    }

    public void setBoolean(final boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getString() {
        return stringValue;
    }

    public void setString(final String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getInteger() {
        return integerValue;
    }

    public void setInteger(final Integer integerValue) {
        this.integerValue = integerValue;
    }

    private class BooleanArgumentMarshaler extends ArgumentMarshaler {}

    private class StringArgumentMarshaler extends ArgumentMarshaler {}

    private class IntegerArgumentMarshaler extends ArgumentMarshaler {}
}
