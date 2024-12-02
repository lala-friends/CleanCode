package main.java.chp14.code;

import java.util.Objects;

public class BooleanArgumentMarshaler implements ArgumentMarshaler {
    private Boolean booleanValue = false;

    @Override
    public void set(String s) {
        booleanValue = true;
    }

    @Override
    public Object get() {
        return booleanValue;
    }
}