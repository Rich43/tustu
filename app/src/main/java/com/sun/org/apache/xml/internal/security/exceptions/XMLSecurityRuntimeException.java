package com.sun.org.apache.xml.internal.security.exceptions;

import com.sun.org.apache.xml.internal.security.utils.I18n;
import java.text.MessageFormat;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/exceptions/XMLSecurityRuntimeException.class */
public class XMLSecurityRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1;
    protected String msgID;

    public XMLSecurityRuntimeException() {
        super("Missing message string");
        this.msgID = null;
    }

    public XMLSecurityRuntimeException(String str) {
        super(I18n.getExceptionMessage(str));
        this.msgID = str;
    }

    public XMLSecurityRuntimeException(String str, Object[] objArr) {
        super(MessageFormat.format(I18n.getExceptionMessage(str), objArr));
        this.msgID = str;
    }

    public XMLSecurityRuntimeException(Exception exc) {
        super("Missing message ID to locate message string in resource bundle \"com.sun.org.apache.xml.internal.security/resource/xmlsecurity\". Original Exception was a " + exc.getClass().getName() + " and message " + exc.getMessage(), exc);
    }

    public XMLSecurityRuntimeException(String str, Exception exc) {
        super(I18n.getExceptionMessage(str, exc), exc);
        this.msgID = str;
    }

    public XMLSecurityRuntimeException(String str, Object[] objArr, Exception exc) {
        super(MessageFormat.format(I18n.getExceptionMessage(str), objArr), exc);
        this.msgID = str;
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
        if (getCause() != null) {
            str = str + "\nOriginal Exception was " + getCause().toString();
        }
        return str;
    }

    public Exception getOriginalException() {
        if (getCause() instanceof Exception) {
            return (Exception) getCause();
        }
        return null;
    }
}
