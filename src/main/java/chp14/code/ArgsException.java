package main.java.chp14.code;

public class ArgsException extends Exception{
    private char errorArgumentId = '\0';
    private String errorParameter = null;
    private ErrorCode errorCode = ErrorCode.OK;

    public ArgsException() {
    }

    public ArgsException(String message) {
        super(message);
    }

    public ArgsException(ErrorCode errorCode, char errorArgumentId, String errorParameter) {
        this.errorCode = errorCode;
        this.errorArgumentId = errorArgumentId;
        this.errorParameter = errorParameter;
    }

    enum ErrorCode {
        OK, MISSING_STRING, MISSING_INTEGER, INVALID_INTEGER, MISSING_DOUBLE, INVALID_DOUBLE, INVALID_FORMAT, INVALID_ARGUMENT_NAME, UNEXPECTED_ARGUMENT
    }
}
