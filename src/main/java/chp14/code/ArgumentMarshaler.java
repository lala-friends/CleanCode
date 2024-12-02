package main.java.chp14.code;

public class ArgumentMarshaler {
    private boolean booleanValue = false;

    public boolean getBoolean() {
        return booleanValue;
    }

    public void setBoolean(final boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    private class BooleanArgumentMarshaler extends ArgumentMarshaler {}

    private class StringArgumentMarshaler extends ArgumentMarshaler {}

    private class IntegerArgumentMarshaler extends ArgumentMarshaler {}
}
