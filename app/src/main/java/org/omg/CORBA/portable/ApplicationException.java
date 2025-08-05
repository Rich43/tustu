package org.omg.CORBA.portable;

/* loaded from: rt.jar:org/omg/CORBA/portable/ApplicationException.class */
public class ApplicationException extends Exception {
    private String id;
    private InputStream ins;

    public ApplicationException(String str, InputStream inputStream) {
        this.id = str;
        this.ins = inputStream;
    }

    public String getId() {
        return this.id;
    }

    public InputStream getInputStream() {
        return this.ins;
    }
}
