package main.java.chp14.code;

public class ArgsException extends Exception{
    private char errorArgumentId = '\0';
    private String errorParameter = null;
    private Args.ErrorCode errorCode = Args.ErrorCode.OK;

    public ArgsException() {
    }

    public ArgsException(String message) {
        super(message);
    }

    public ArgsException(Args.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ArgsException(Args.ErrorCode errorCode, String errorParameter) {
        this.errorCode = errorCode;
        this.errorParameter = errorParameter;
    }

    public ArgsException(Args.ErrorCode errorCode, char errorArgumentId, String errorParameter) {
        this.errorCode = errorCode;
        this.errorArgumentId = errorArgumentId;
        this.errorParameter = errorParameter;
    }

    public char getErrorArgumentId() {
        return errorArgumentId;
    }

    public void setErrorArgumentId(char errorArgumentId) {
        this.errorArgumentId = errorArgumentId;
    }

    public String getErrorParameter() {
        return errorParameter;
    }

    public void setErrorParameter(String errorParameter) {
        this.errorParameter = errorParameter;
    }

    public Args.ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Args.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
