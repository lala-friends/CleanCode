package main.java.chp14.code;

public class StringArgumentMarshaler implements ArgumentMarshaler {
    private String stringValue = "";

    @Override
    public void set(String s) {
        stringValue = s;
    }

    @Override
    public Object get() {
        return stringValue;
    }
}
