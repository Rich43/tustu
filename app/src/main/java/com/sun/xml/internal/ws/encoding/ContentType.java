package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.encoding.HeaderTokenizer;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/ContentType.class */
public final class ContentType {
    private String primaryType;
    private String subType;
    private ParameterList list;

    public ContentType(String s2) throws WebServiceException {
        HeaderTokenizer h2 = new HeaderTokenizer(s2, com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.MIME);
        HeaderTokenizer.Token tk = h2.next();
        if (tk.getType() != -1) {
            throw new WebServiceException();
        }
        this.primaryType = tk.getValue();
        if (((char) h2.next().getType()) != '/') {
            throw new WebServiceException();
        }
        HeaderTokenizer.Token tk2 = h2.next();
        if (tk2.getType() != -1) {
            throw new WebServiceException();
        }
        this.subType = tk2.getValue();
        String rem = h2.getRemainder();
        if (rem != null) {
            this.list = new ParameterList(rem);
        }
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
}
