package com.sun.xml.internal.messaging.saaj.packaging.mime;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/MessagingException.class */
public class MessagingException extends Exception {
    private Exception next;

    public MessagingException() {
    }

    public MessagingException(String s2) {
        super(s2);
    }

    public MessagingException(String s2, Exception e2) {
        super(s2);
        this.next = e2;
    }

    public Exception getNextException() {
        return this.next;
    }

    public synchronized boolean setNextException(Exception ex) {
        Exception theEnd;
        Exception exc = this;
        while (true) {
            theEnd = exc;
            if (!(theEnd instanceof MessagingException) || ((MessagingException) theEnd).next == null) {
                break;
            }
            exc = ((MessagingException) theEnd).next;
        }
        if (theEnd instanceof MessagingException) {
            ((MessagingException) theEnd).next = ex;
            return true;
        }
        return false;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.next == null) {
            return super.getMessage();
        }
        Exception n2 = this.next;
        String s2 = super.getMessage();
        StringBuffer sb = new StringBuffer(s2 == null ? "" : s2);
        while (n2 != null) {
            sb.append(";\n  nested exception is:\n\t");
            if (n2 instanceof MessagingException) {
                MessagingException mex = (MessagingException) n2;
                sb.append(n2.getClass().toString());
                String msg = mex.getSuperMessage();
                if (msg != null) {
                    sb.append(": ");
                    sb.append(msg);
                }
                n2 = mex.next;
            } else {
                sb.append(n2.toString());
                n2 = null;
            }
        }
        return sb.toString();
    }

    private String getSuperMessage() {
        return super.getMessage();
    }
}
