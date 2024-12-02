package main.java.chp14.code;

import java.text.ParseException;
import java.util.*;

public class Args {
    private String schema;
    private String[] args;

    private Map<Character, ArgumentMarshaler> marshalers = new HashMap<>();

    private boolean valid = true;
    private Set<Character> unexpectedArguments = new TreeSet<>();
    private Set<Character> argsFound = new HashSet<>();

    private int currentArgument = 0;

    private char errorArgument = '\0';
    private ErrorCode errorCode = ErrorCode.OK;

    enum ErrorCode {
        OK, MISSING_INTEGER, INVALID_INTEGER, MISSING_STRING
    }

    public Args(final String schema, final String[] args) throws ParseException, ArgsException {
        this.args = args;
        this.schema = schema;
        this.valid = parse();
    }

    private boolean parse() throws ParseException, ArgsException {
        if (schema.isEmpty() && args.length == 0) {
            return true;
        }

        parseSchema();
        parseArguments();
        return unexpectedArguments.isEmpty();
    }

    private void parseSchema() throws ParseException {
        for (String element : schema.split(",")) {
            parseSchemaElement(element);
        }
    }

    private void parseSchemaElement(final String element) throws ParseException {
        final var elementId = element.charAt(0);
        final var elementTail = element.substring(1);

        validateSchemaElementId(elementId);

        if (isBooleanSchemaElement(elementTail)) {
            parseBooleanSchemaElement(elementId);
        } else if (isStringSchemaElement(elementTail)) {
            parseStringSchemaElement(elementId);
        } else if (isIntegerSchemaElement(elementTail)) {
            parseIntegerSchemaElement(elementId);
        }
    }

    private void validateSchemaElementId(final char elementId) throws ParseException {
        if (!Character.isLetter(elementId)) {
            throw new ParseException("Bad character: " + elementId + " in Args format: " + schema, 0);
        }
    }

    private void parseBooleanSchemaElement(final char elementId) {
        marshalers.put(elementId, new BooleanArgumentMarshaler());
    }

    private boolean isBooleanSchemaElement(final String elementTail) {
        return elementTail.isEmpty();
    }

    private void parseStringSchemaElement(final char elementId) {
        marshalers.put(elementId, new StringArgumentMarshaler());
    }

    private boolean isStringSchemaElement(final String elementTail) {
        return elementTail.equals("*");
    }

    private void parseIntegerSchemaElement(final char elementId) {
        marshalers.put(elementId, new IntegerArgumentMarshaler());
    }

    private boolean isIntegerSchemaElement(String elementTail) {
        return elementTail.equals("#");
    }

    private boolean parseArguments() throws ArgsException {
        for (currentArgument = 0; currentArgument < args.length; currentArgument++) {
            final var arg = args[currentArgument];
            parseArgument(arg);
        }

        return true;
    }

    private void parseArgument(final String arg) throws ArgsException {
        if (arg.startsWith("-")) {
            parseElements(arg);
        }
    }

    private void parseElements(final String arg) throws ArgsException {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(final char argChar) throws ArgsException {
        if (setArgument(argChar)) {
            argsFound.add(argChar);
        } else {
            unexpectedArguments.add(argChar);
            valid = false;
        }
    }

    private boolean setArgument(final char argChar) throws ArgsException {
        boolean set = true;
        if (isBooleanArg(argChar)) {
            setBooleanArg(argChar, true);
        } else if (isStringArg(argChar)) {
            setStringArg(argChar, "");
        } else if (isIntegerArg(argChar)) {
            setIntegerArg(argChar, 0);
        } else {
            set = false;
        }
        return set;
    }

    private void setBooleanArg(char argChar, boolean value) throws ArgsException {
        try {
            marshalers.get(argChar).set("true");
        } catch(ArgsException e) {
            throw e;
        }

    }

    private boolean isBooleanArg(final char argChar) {
        final var m = marshalers.get(argChar);
        return m instanceof BooleanArgumentMarshaler;
    }

    private void setStringArg(final char argChar, final String s) throws ArgsException {
        currentArgument++;

        try {
            marshalers.get(argChar).set(args[currentArgument]);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.MISSING_STRING;
            throw new ArgsException();
        } catch (ArgsException e) {
            throw e;
        }
    }

    private boolean isStringArg(final char argChar) {
        final var m = marshalers.get(argChar);
        return m instanceof StringArgumentMarshaler;
    }

    private void setIntegerArg(char argChar, int i) throws ArgsException {
        currentArgument++;

        try {
            marshalers.get(argChar).set(args[currentArgument]);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.MISSING_INTEGER;
            throw new ArgsException();
        } catch (ArgsException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.INVALID_INTEGER;
            throw e;
        }
    }

    private boolean isIntegerArg(final char argChar) {
        final var m = marshalers.get(argChar);
        return m instanceof IntegerArgumentMarshaler;
    }

    public boolean getBoolean(final char argChar) {
        final var am = marshalers.get(argChar);
        return am != null && (Boolean) am.get();
    }

    public String getString(final char argChar) {
        final var am = marshalers.get(argChar);
        return am == null ? "" : (String) am.get();
    }

    private Integer getInteger(final char argChar) {
        final var am = marshalers.get(argChar);
        return am == null ? 0 : (Integer) am.get();
    }

    public static void main(String[] args) throws ParseException, ArgsException {

        System.out.println(Arrays.toString(args));

        final var arg = new Args("l,p#,d*", args);
        boolean logging = arg.getBoolean('l');
        System.out.println(logging);

        String directory = arg.getString('d');
        System.out.println(directory);

        int port = arg.getInteger('p');
        System.out.println(port);
    }
}
