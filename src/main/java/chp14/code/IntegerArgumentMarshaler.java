package main.java.chp14.code;

public class IntegerArgumentMarshaler implements ArgumentMarshaler {
    private Integer integerValue = 0;

    @Override
    public void set(String s) throws ArgsException {
        try {
            integerValue = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new ArgsException();
        }
    }

    @Override
    public Object get() {
        return integerValue;
    }
}