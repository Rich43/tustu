package org.apache.commons.net.telnet;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/InvalidTelnetOptionException.class */
public class InvalidTelnetOptionException extends Exception {
    private static final long serialVersionUID = -2516777155928793597L;
    private final int optionCode;
    private final String msg;

    public InvalidTelnetOptionException(String message, int optcode) {
        this.optionCode = optcode;
        this.msg = message;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.msg + ": " + this.optionCode;
    }
}
