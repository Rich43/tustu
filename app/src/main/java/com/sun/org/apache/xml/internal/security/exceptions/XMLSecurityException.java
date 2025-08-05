package com.sun.org.apache.xml.internal.security.exceptions;

import com.sun.org.apache.xml.internal.security.utils.I18n;
import java.text.MessageFormat;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/exceptions/XMLSecurityException.class */
public class XMLSecurityException extends Exception {
    private static final long serialVersionUID = 1;
    protected String msgID;

    public XMLSecurityException() {
        super("Missing message string");
        this.msgID = null;
    }

    public XMLSecurityException(String str) {
        super(I18n.getExceptionMessage(str));
        this.msgID = str;
    }

    public XMLSecurityException(String str, Object[] objArr) {
        super(MessageFormat.format(I18n.getExceptionMessage(str), objArr));
        this.msgID = str;
    }

    public XMLSecurityException(Exception exc) {
        super(exc.getMessage(), exc);
    }

    public XMLSecurityException(Exception exc, String str) {
        super(I18n.getExceptionMessage(str, exc), exc);
        this.msgID = str;
    }

    @Deprecated
    public XMLSecurityException(String str, Exception exc) {
        this(exc, str);
    }

    public XMLSecurityException(Exception exc, String str, Object[] objArr) {
        super(MessageFormat.format(I18n.getExceptionMessage(str), objArr), exc);
        this.msgID = str;
    }

    @Deprecated
    public XMLSecurityException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }

    public String getMsgID() {
        if (this.msgID == null) {
            return "Missing message ID";
        }
        return this.msgID;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String str;
        String name = getClass().getName();
        String localizedMessage = super.getLocalizedMessage();
        if (localizedMessage != null) {
            str = name + ": " + localizedMessage;
        } else {
            str = name;
        }
        if (super.getCause() != null) {
            str = str + "\nOriginal Exception was " + super.getCause().toString();
        }
        return str;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        synchronized (System.err) {
            super.printStackTrace(System.err);
        }
    }

    public Exception getOriginalException() {
        if (getCause() instanceof Exception) {
            return (Exception) getCause();
        }
        return null;
    }
}
