package main.java.chp14.code;

import java.text.ParseException;
import java.util.*;

public class Args {
    private String schema;
    private String[] args;

    private boolean vaild;

    private Map<Character, ArgumentMarshaler> booleanArgs = new HashMap<>();
    private Map<Character, String> stringArgs = new HashMap<>();
    private Map<Character, Integer> intArgs = new HashMap<>();

    private boolean valid = true;
    private Set<Character> unexpectedArguments = new TreeSet<>();
    private Set<Character> argsFound = new HashSet<>();

    private int currentArgument = 0;

    private char errorArgument = '\0';
    private ErrorCode errorCode = ErrorCode.OK;

    enum ErrorCode {
        OK, MISSING_INTEGER, INVALID_INTEGER, MISSING_STRING
    }

    public Args(final String schema, final String[] args) throws ParseException {
        this.args = args;
        this.schema = schema;
        this.vaild = parse();
    }

    private boolean parse() throws ParseException {
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
        } else if(isIntegerSchemaElement(elementTail)) {
            parseIntegerSchemaElement(elementId);
        }
    }

    private void validateSchemaElementId(final char elementId) throws ParseException {
        if (!Character.isLetter(elementId)) {
            throw new ParseException("Bad character: " + elementId + " in Args format: " + schema, 0);
        }
    }

    private void parseBooleanSchemaElement(final char elementId) {
        booleanArgs.put(elementId, new ArgumentMarshaler());
    }

    private boolean isBooleanSchemaElement(final String elementTail) {
        return elementTail.isEmpty();
    }

    private void parseStringSchemaElement(final char elementId) {
        stringArgs.put(elementId, "");
    }

    private boolean isStringSchemaElement(final String elementTail) {
        return elementTail.equals("*");
    }

    private void parseIntegerSchemaElement(final char elementId) {
        intArgs.put(elementId, 0);
    }

    private boolean isIntegerSchemaElement(String elementTail) {
        return elementTail.equals("#");
    }

    private boolean parseArguments() {
        for (currentArgument = 0; currentArgument < args.length; currentArgument++) {
            final var arg = args[currentArgument];
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
        if (setArgument(argChar)) {
            argsFound.add(argChar);
        } else {
            unexpectedArguments.add(argChar);
            valid = false;
        }
    }

    private boolean setArgument(final char argChar) {
        boolean set = true;
        if (isBoolean(argChar)) {
            setBooleanArg(argChar, true);
        } else if (isString(argChar)) {
            setStringArg(argChar, "");
        } else if(isInteger(argChar)) {
            setIntegerArg(argChar, 0);
        }else {
            set = false;
        }
        return set;
    }

    private void setBooleanArg(char argChar, boolean value) {
        booleanArgs.get(argChar).setBoolean(value);
    }

    private boolean isBoolean(final char argChar) {
        return booleanArgs.containsKey(argChar);
    }

    private void setStringArg(final char argChar, final String s) {
        currentArgument++;

        try {
            stringArgs.put(argChar, args[currentArgument]);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.MISSING_STRING;
        }
    }

    private boolean isString(final char argChar) {
        return stringArgs.containsKey(argChar);
    }

    private void setIntegerArg(char argChar, int i) {
        currentArgument++;

        try {
            intArgs.put(argChar, Integer.parseInt(args[currentArgument]));
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.MISSING_INTEGER;
        } catch (NumberFormatException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.INVALID_INTEGER;
        }
    }

    private boolean isInteger(final char argChar) {
        return intArgs.containsKey(argChar);
    }

    public boolean getBoolean(final char argChar) {
        final var am = booleanArgs.get(argChar);
        return am != null && am.getBoolean();
    }

    public String getString(final char argChar) {
        return falseIfNull(stringArgs.get(argChar));
    }

    private String falseIfNull(final String s) {
        return s == null ? "" : s;
    }

    private Integer getInteger(final char argChar) {
        return falseIfNull(intArgs.get(argChar));
    }

    private Integer falseIfNull(final Integer i) {
        return i == null ? 0 : i;
    }

    public static void main(String[] args) throws ParseException {
        final var arg = new Args("l,p#,d*", args);
        boolean logging = arg.getBoolean('l');
        System.out.println(logging);

        String directory = arg.getString('d');
        System.out.println(directory);

        int port = arg.getInteger('p');
        System.out.println(port);

//        System.out.println(Arrays.toString(args));
    }
}
