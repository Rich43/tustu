package com.sun.xml.internal.bind.util;

import com.sun.xml.internal.bind.ValidationEventLocatorEx;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/util/ValidationEventLocatorExImpl.class */
public class ValidationEventLocatorExImpl extends ValidationEventLocatorImpl implements ValidationEventLocatorEx {
    private final String fieldName;

    public ValidationEventLocatorExImpl(Object target, String fieldName) {
        super(target);
        this.fieldName = fieldName;
    }

    @Override // com.sun.xml.internal.bind.ValidationEventLocatorEx
    public String getFieldName() {
        return this.fieldName;
    }

    @Override // javax.xml.bind.helpers.ValidationEventLocatorImpl
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[url=");
        buf.append((Object) getURL());
        buf.append(",line=");
        buf.append(getLineNumber());
        buf.append(",column=");
        buf.append(getColumnNumber());
        buf.append(",node=");
        buf.append((Object) getNode());
        buf.append(",object=");
        buf.append(getObject());
        buf.append(",field=");
        buf.append(getFieldName());
        buf.append("]");
        return buf.toString();
    }
}
