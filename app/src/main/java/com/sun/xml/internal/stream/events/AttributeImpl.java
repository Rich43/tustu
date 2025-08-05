package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/AttributeImpl.class */
public class AttributeImpl extends DummyEvent implements Attribute {
    private String fValue;
    private String fNonNormalizedvalue;
    private QName fQName;
    private String fAttributeType;
    private boolean fIsSpecified;

    public AttributeImpl() {
        this.fAttributeType = "CDATA";
        init();
    }

    public AttributeImpl(String name, String value) {
        this.fAttributeType = "CDATA";
        init();
        this.fQName = new QName(name);
        this.fValue = value;
    }

    public AttributeImpl(String prefix, String name, String value) {
        this(prefix, null, name, value, null, null, false);
    }

    public AttributeImpl(String prefix, String uri, String localPart, String value, String type) {
        this(prefix, uri, localPart, value, null, type, false);
    }

    public AttributeImpl(String prefix, String uri, String localPart, String value, String nonNormalizedvalue, String type, boolean isSpecified) {
        this(new QName(uri, localPart, prefix), value, nonNormalizedvalue, type, isSpecified);
    }

    public AttributeImpl(QName qname, String value, String nonNormalizedvalue, String type, boolean isSpecified) {
        this.fAttributeType = "CDATA";
        init();
        this.fQName = qname;
        this.fValue = value;
        if (type != null && !type.equals("")) {
            this.fAttributeType = type;
        }
        this.fNonNormalizedvalue = nonNormalizedvalue;
        this.fIsSpecified = isSpecified;
    }

    public String toString() {
        if (this.fQName.getPrefix() != null && this.fQName.getPrefix().length() > 0) {
            return this.fQName.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + this.fQName.getLocalPart() + "='" + this.fValue + PdfOps.SINGLE_QUOTE_TOKEN;
        }
        return this.fQName.getLocalPart() + "='" + this.fValue + PdfOps.SINGLE_QUOTE_TOKEN;
    }

    public void setName(QName name) {
        this.fQName = name;
    }

    @Override // javax.xml.stream.events.Attribute
    public QName getName() {
        return this.fQName;
    }

    public void setValue(String value) {
        this.fValue = value;
    }

    @Override // javax.xml.stream.events.Attribute
    public String getValue() {
        return this.fValue;
    }

    public void setNonNormalizedValue(String nonNormalizedvalue) {
        this.fNonNormalizedvalue = nonNormalizedvalue;
    }

    public String getNonNormalizedValue() {
        return this.fNonNormalizedvalue;
    }

    public void setAttributeType(String attributeType) {
        this.fAttributeType = attributeType;
    }

    @Override // javax.xml.stream.events.Attribute
    public String getDTDType() {
        return this.fAttributeType;
    }

    public void setSpecified(boolean isSpecified) {
        this.fIsSpecified = isSpecified;
    }

    @Override // javax.xml.stream.events.Attribute
    public boolean isSpecified() {
        return this.fIsSpecified;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(toString());
    }

    protected void init() {
        setEventType(10);
    }
}
