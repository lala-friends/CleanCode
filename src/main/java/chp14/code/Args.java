package main.java.chp14.code;

import java.util.*;

public class Args {
    private String schema;
    private String[] args;

    private boolean vaild;

    private Set<Character> unexpectedArguments = new TreeSet<>();
    private Map<Character, Boolean> booleanArgs = new HashMap<>();

    private int numberOfArguments = 0;

    public Args(final String schema, final String[] args) {
        this.args = args;
        this.schema = schema;
        this.vaild = parse();
    }

    private boolean parse() {
        if (schema.isEmpty() && args.length == 0) {
            return true;
        }

        parseSchema();
        parseArguments();
        return unexpectedArguments.isEmpty();
    }

    private void parseSchema() {
        for (String element : schema.split(",")) {
            parseSchemaElement(element);
        }
    }

    private void parseSchemaElement(final String element) {
        if (element.length() == 1) {
            parseBooleanSchemaElement(element);
        }
    }

    private void parseBooleanSchemaElement(final String element) {
        final var c = element.charAt(0);
        if (Character.isLetter(c)) {
            booleanArgs.put(c, false);
        }
    }

    private boolean parseArguments() {
        for (String arg : args) {
            parseArgument(arg);
        }

        return true;
    }

    private void parseArgument(final String arg) {
        if (arg.startsWith("-")) {
            parseElements(arg);
        }
    }

    private void parseElements(final String arg) {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(final char argChar) {
        if (isBoolean(argChar)) {
            numberOfArguments++;
            setBooleanArg(argChar, true);
        }
    }

    private void setBooleanArg(char argChar, boolean value) {
        booleanArgs.put(argChar, value);
    }

    private boolean isBoolean(final char argChar) {
        return booleanArgs.containsKey(argChar);
    }

    public boolean getBoolean(final char arg) {
        return booleanArgs.get(arg);
    }

    public static void main(String[] args) {
        final var arg = new Args("l,p#,d*", args);
        boolean logging = arg.getBoolean('l');
        System.out.println(logging);
        System.out.println(Arrays.toString(args));
    }
}
