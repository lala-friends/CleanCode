package main.java.chp14.code;

import java.text.ParseException;
import java.util.*;

public class Args {
    private String schema;

    private boolean valid = true;

    private Map<Character, ArgumentMarshaler> marshalers = new HashMap<>();

    private Set<Character> unexpectedArguments = new TreeSet<>();
    private Set<Character> argsFound = new HashSet<>();

    private Iterator<String> currentArgument;

    private char errorArgumentId = '\0';
    private String errorParameter = "TILT";
    private ErrorCode errorCode = ErrorCode.OK;

    private List<String> argsList;

    enum ErrorCode {
        OK, MISSING_STRING, MISSING_INTEGER, INVALID_INTEGER, UNEXPECTED_ARGUMENT
    }

    public Args(final String schema, final String[] args) throws ParseException, ArgsException {
        this.argsList = Arrays.asList(args);
        this.schema = schema;
        this.valid = parse();
    }

    private boolean parse() throws ParseException, ArgsException {
        if (schema.isEmpty() && argsList.isEmpty()) {
            return true;
        }

        parseSchema();
        parseArguments();

        return valid;
    }

    private boolean parseSchema() throws ParseException {
        for (String element : schema.split(",")) {
            final var trimmedElement = element.trim();
            parseSchemaElement(trimmedElement);
        }

        return true;
    }

    private void parseSchemaElement(final String element) throws ParseException {
        final var elementId = element.charAt(0);
        final var elementTail = element.substring(1);

        validateSchemaElementId(elementId);

        if (isBooleanSchemaElement(elementTail)) {
            marshalers.put(elementId, new BooleanArgumentMarshaler());
        } else if (isStringSchemaElement(elementTail)) {
            marshalers.put(elementId, new StringArgumentMarshaler());
        } else if (isIntegerSchemaElement(elementTail)) {
            marshalers.put(elementId, new IntegerArgumentMarshaler());
        } else {
            throw new ParseException(String.format("Argument: %c has invalid format: %s.", elementId, elementTail), 0);
        }
    }

    private void validateSchemaElementId(final char elementId) throws ParseException {
        if (!Character.isLetter(elementId)) {
            throw new ParseException("Bad character: " + elementId + " in Args format: " + schema, 0);
        }
    }

    private boolean isBooleanSchemaElement(final String elementTail) {
        return elementTail.isEmpty();
    }

    private boolean isStringSchemaElement(final String elementTail) {
        return elementTail.equals("*");
    }

    private boolean isIntegerSchemaElement(String elementTail) {
        return elementTail.equals("#");
    }

    private boolean parseArguments() throws ArgsException {
        for (currentArgument = argsList.listIterator(); currentArgument.hasNext(); ) {
            final var arg = currentArgument.next();
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
            errorCode = ErrorCode.UNEXPECTED_ARGUMENT;
            valid = false;
        }
    }

    private boolean setArgument(final char argChar) throws ArgsException {
        final var m = marshalers.get(argChar);

        try {
            if (m instanceof BooleanArgumentMarshaler) {
                setBooleanArg(m, currentArgument);
            } else if (m instanceof StringArgumentMarshaler) {
                setStringArg(m);
            } else if (m instanceof IntegerArgumentMarshaler) {
                setIntegerArg(m);
            } else {
                return false;
            }
        } catch (ArgsException e) {
            valid = false;
            errorArgumentId = argChar;
            throw e;
        }

        return true;
    }

    private void setBooleanArg(final ArgumentMarshaler m, Iterator<String> currentArgument) throws ArgsException {
        m.set("true");
    }

    private void setStringArg(final ArgumentMarshaler m) throws ArgsException {
        try {
            m.set(currentArgument.next());
        } catch (NoSuchElementException e) {
            errorCode = ErrorCode.MISSING_STRING;
            throw new ArgsException();
        }
    }

    private void setIntegerArg(final ArgumentMarshaler m) throws ArgsException {
        String parameter = null;

        try {
            parameter = currentArgument.next();
            m.set(parameter);
        } catch (NoSuchElementException e) {
            errorCode = ErrorCode.MISSING_INTEGER;
            throw new ArgsException();
        } catch (ArgsException e) {
            errorParameter = parameter;
            errorCode = ErrorCode.INVALID_INTEGER;
            throw e;
        }
    }

    public int cardinality() {
        return argsFound.size();
    }

    public String usage() {
        if (!schema.isEmpty()) {
            return "-[" + schema + "]";
        } else {
            return "";
        }
    }

    public String errorMessage() throws Exception {
        return switch (errorCode) {
            case OK -> throw new Exception("TILT: Should not get here.");
            case UNEXPECTED_ARGUMENT -> unexpectedArgumentMessage();
            case MISSING_STRING -> String.format("Could not find string parameter for -%c.", errorArgumentId);
            case INVALID_INTEGER ->
                    String.format("Argument -%c expects an integer but was '$s'.", errorArgumentId, errorParameter);
            case MISSING_INTEGER -> String.format("Could not find integer parameter for -%c.", errorArgumentId);
        };

    }

    private String unexpectedArgumentMessage() {
        final var message = new StringBuilder("Argument(s) -");

        for (char c : unexpectedArguments) {
            message.append(c);
        }

        return message.toString();
    }

    public boolean getBoolean(final char argChar) {
        final var am = marshalers.get(argChar);
        boolean b = false;

        try {
            b = am != null && (Boolean) am.get();
        } catch (ClassCastException _) {
        }

        return b;
    }

    public String getString(final char argChar) {
        final var am = marshalers.get(argChar);
        try {
            return am == null ? "" : (String) am.get();
        } catch (ClassCastException _) {
            return "";
        }

    }

    private Integer getInteger(final char argChar) {
        final var am = marshalers.get(argChar);

        try {
            return am == null ? 0 : (Integer) am.get();
        } catch (ClassCastException _) {
            return 0;
        }
    }

    public boolean has(char arg) {
        return argsFound.contains(arg);
    }

    public boolean isValid() {
        return valid;
    }

    public static void main(String[] args) throws ParseException, ArgsException {
        final var arg = new Args("l,p#,d*", args);
        boolean logging = arg.getBoolean('l');
        System.out.println(logging);

        String directory = arg.getString('d');
        System.out.println(directory);

        int port = arg.getInteger('p');
        System.out.println(port);
    }
}
