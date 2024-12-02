package main.java.chp14.code;

import main.java.chp14.code.ArgsException.ErrorCode;

import java.text.ParseException;
import java.util.*;

public class Args {
    private String schema;

    private Map<Character, ArgumentMarshaler> marshalers = new HashMap<>();

    private Set<Character> argsFound = new HashSet<>();

    private Iterator<String> currentArgument;

    private char errorArgumentId = '\0';
    private String errorParameter = "TILT";
    private ErrorCode errorCode = ErrorCode.OK;

    private List<String> argsList;

    public Args(final String schema, final String[] args) throws ArgsException {
        this.argsList = Arrays.asList(args);
        this.schema = schema;
        parse();
    }

    private void parse() throws ArgsException {
        parseSchema();
        parseArguments();
    }

    private boolean parseSchema() throws ArgsException {
        for (String element : schema.split(",")) {
            parseSchemaElement(element.trim());
        }

        return true;
    }

    private void parseSchemaElement(final String element) throws ArgsException {
        final var elementId = element.charAt(0);
        final var elementTail = element.substring(1);

        validateSchemaElementId(elementId);

        if (elementTail.isEmpty()) {
            marshalers.put(elementId, new BooleanArgumentMarshaler());
        } else if (elementTail.equals("*")) {
            marshalers.put(elementId, new StringArgumentMarshaler());
        } else if (elementTail.equals("#")) {
            marshalers.put(elementId, new IntegerArgumentMarshaler());
        } else if (elementTail.equals("##")) {
            marshalers.put(elementId, new DoubleArgumentMarshaler());
        } else {
            throw new ArgsException(ErrorCode.INVALID_FORMAT, elementId, elementTail);
        }
    }

    private void validateSchemaElementId(final char elementId) throws ArgsException {
        if (!Character.isLetter(elementId)) {
            throw new ArgsException(ErrorCode.INVALID_ARGUMENT_NAME, elementId, null);
        }
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
            errorCode = ErrorCode.UNEXPECTED_ARGUMENT;
        }
    }

    private boolean setArgument(final char argChar) throws ArgsException {
        final var m = marshalers.get(argChar);

        if (m == null) {
            return false;
        }

        try {
            m.set(currentArgument);
        } catch (ArgsException e) {
            errorArgumentId = argChar;
            throw e;
        }

        return true;
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

    private Double getDouble(final char argChar) {
        final var am = marshalers.get(argChar);

        try {
            return am == null ? 0d : (Double) am.get();
        } catch (ClassCastException _) {
            return 0d;
        }
    }

    public boolean has(char arg) {
        return argsFound.contains(arg);
    }

    public static void main(String[] args) throws ParseException, ArgsException {
        final var arg = new Args("l,p#,d*,a##", args);
        boolean logging = arg.getBoolean('l');
        System.out.println(logging);

        String directory = arg.getString('d');
        System.out.println(directory);

        int port = arg.getInteger('p');
        System.out.println(port);

        double percent = arg.getDouble('a');
        System.out.println(percent);
    }

    public interface ArgumentMarshaler {
        void set(Iterator<String> currentArgument) throws ArgsException;

        Object get();
    }

    public class BooleanArgumentMarshaler implements ArgumentMarshaler {
        private Boolean booleanValue = false;

        @Override
        public void set(final Iterator<String> currentArgument) throws ArgsException {
            booleanValue = true;
        }

        @Override
        public Object get() {
            return booleanValue;
        }
    }

    public class StringArgumentMarshaler implements ArgumentMarshaler {
        private String stringValue = "";

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            try {
                stringValue = currentArgument.next();
            } catch (NoSuchElementException e) {
                errorCode = ErrorCode.MISSING_STRING;
                throw new ArgsException();
            }
        }

        @Override
        public Object get() {
            return stringValue;
        }
    }

    public class IntegerArgumentMarshaler implements ArgumentMarshaler {
        private Integer integerValue = 0;

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            String parameter = null;

            try {
                parameter = currentArgument.next();
                integerValue = Integer.parseInt(parameter);
            } catch (NoSuchElementException e) {
                errorCode = ErrorCode.MISSING_INTEGER;
                throw new ArgsException();
            } catch (NumberFormatException e) {
                errorParameter = parameter;
                errorCode = ErrorCode.INVALID_INTEGER;
                throw new ArgsException();
            }
        }

        @Override
        public Object get() {
            return integerValue;
        }
    }

    public class DoubleArgumentMarshaler implements ArgumentMarshaler {
        private Double doubleValue = 0d;

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            String parameter = null;

            try {
                parameter = currentArgument.next();
                doubleValue = Double.parseDouble(parameter);
            } catch (NoSuchElementException e) {
                errorCode = ErrorCode.MISSING_DOUBLE;
                throw new ArgsException();
            } catch (NumberFormatException e) {
                errorParameter = parameter;
                errorCode = ErrorCode.INVALID_DOUBLE;
                throw new ArgsException();
            }
        }

        @Override
        public Object get() {
            return doubleValue;
        }
    }
}
