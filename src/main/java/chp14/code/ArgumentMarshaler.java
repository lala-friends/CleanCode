package main.java.chp14.code;

public interface ArgumentMarshaler {
    void set(String s) throws ArgsException;

    Object get();
}
