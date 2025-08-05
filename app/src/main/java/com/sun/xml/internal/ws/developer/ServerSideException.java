package com.sun.xml.internal.ws.developer;

/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/ServerSideException.class */
public class ServerSideException extends Exception {
    private final String className;

    public ServerSideException(String className, String message) {
        super(message);
        this.className = className;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "Client received an exception from server: " + super.getMessage() + " Please see the server log to find more detail regarding exact cause of the failure.";
    }

    @Override // java.lang.Throwable
    public String toString() {
        String s2 = this.className;
        String message = getLocalizedMessage();
        return message != null ? s2 + ": " + message : s2;
    }
}
