package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/ContentType.class */
public final class ContentType {
    private String primaryType;
    private String subType;
    private ParameterList list;

    public ContentType() {
    }

    public ContentType(String primaryType, String subType, ParameterList list) {
        this.primaryType = primaryType;
        this.subType = subType;
        this.list = list == null ? new ParameterList() : list;
    }

    public ContentType(String s2) throws ParseException {
        HeaderTokenizer h2 = new HeaderTokenizer(s2, HeaderTokenizer.MIME);
        HeaderTokenizer.Token tk = h2.next();
        if (tk.getType() != -1) {
            throw new ParseException();
        }
        this.primaryType = tk.getValue();
        if (((char) h2.next().getType()) != '/') {
            throw new ParseException();
        }
        HeaderTokenizer.Token tk2 = h2.next();
        if (tk2.getType() != -1) {
            throw new ParseException();
        }
        this.subType = tk2.getValue();
        String rem = h2.getRemainder();
        if (rem != null) {
            this.list = new ParameterList(rem);
        }
    }

    public ContentType copy() {
        return new ContentType(this.primaryType, this.subType, this.list.copy());
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public String getSubType() {
        return this.subType;
    }

    public String getBaseType() {
        return this.primaryType + '/' + this.subType;
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

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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
        if (this.primaryType == null || this.subType == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(this.primaryType).append('/').append(this.subType);
        if (this.list != null) {
            sb.append(this.list.toString());
        }
        return sb.toString();
    }

    public boolean match(ContentType cType) {
        if (!this.primaryType.equalsIgnoreCase(cType.getPrimaryType())) {
            return false;
        }
        String sType = cType.getSubType();
        if (this.subType.charAt(0) != '*' && sType.charAt(0) != '*' && !this.subType.equalsIgnoreCase(sType)) {
            return false;
        }
        return true;
    }

    public boolean match(String s2) {
        try {
            return match(new ContentType(s2));
        } catch (ParseException e2) {
            return false;
        }
    }
}
