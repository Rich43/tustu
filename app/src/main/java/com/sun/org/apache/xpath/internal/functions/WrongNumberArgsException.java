package com.sun.org.apache.xpath.internal.functions;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/WrongNumberArgsException.class */
public class WrongNumberArgsException extends Exception {
    static final long serialVersionUID = -4551577097576242432L;

    public WrongNumberArgsException(String argsExpected) {
        super(argsExpected);
    }
}
