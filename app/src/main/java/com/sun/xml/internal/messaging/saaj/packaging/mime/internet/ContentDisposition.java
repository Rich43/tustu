package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/ContentDisposition.class */
public class ContentDisposition {
    private String disposition;
    private ParameterList list;

    public ContentDisposition() {
    }

    public ContentDisposition(String disposition, ParameterList list) {
        this.disposition = disposition;
        this.list = list;
    }

    public ContentDisposition(String s2) throws ParseException {
        HeaderTokenizer h2 = new HeaderTokenizer(s2, HeaderTokenizer.MIME);
        HeaderTokenizer.Token tk = h2.next();
        if (tk.getType() != -1) {
            throw new ParseException();
        }
        this.disposition = tk.getValue();
        String rem = h2.getRemainder();
        if (rem != null) {
            this.list = new ParameterList(rem);
        }
    }

    public String getDisposition() {
        return this.disposition;
    }

    public String getParameter(String name) {
        if (this.list == null) {
            return null;
        }
        return this.list.get(name);
    }

    public ParameterList getParameterList() {
        return this.list;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public void setParameter(String name, String value) {
        if (this.list == null) {
            this.list = new ParameterList();
        }
        this.list.set(name, value);
    }

    public void setParameterList(ParameterList list) {
        this.list = list;
    }

    public String toString() {
        if (this.disposition == null) {
            return null;
        }
        if (this.list == null) {
            return this.disposition;
        }
        StringBuffer sb = new StringBuffer(this.disposition);
        sb.append(this.list.toString(sb.length() + 21));
        return sb.toString();
    }
}
