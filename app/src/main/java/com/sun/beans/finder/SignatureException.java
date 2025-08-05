package com.sun.beans.finder;

/* loaded from: rt.jar:com/sun/beans/finder/SignatureException.class */
final class SignatureException extends RuntimeException {
    SignatureException(Throwable th) {
        super(th);
    }

    NoSuchMethodException toNoSuchMethodException(String str) {
        Throwable cause = getCause();
        if (cause instanceof NoSuchMethodException) {
            return (NoSuchMethodException) cause;
        }
        NoSuchMethodException noSuchMethodException = new NoSuchMethodException(str);
        noSuchMethodException.initCause(cause);
        return noSuchMethodException;
    }
}
