package com.sun.xml.internal.ws.encoding.xml;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.PropertySet;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLPropertyBag.class */
public class XMLPropertyBag extends BasePropertySet {
    private String contentType;
    private static final BasePropertySet.PropertyMap model = parse(XMLPropertyBag.class);

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return model;
    }

    @PropertySet.Property({XMLConstants.OUTPUT_XML_CHARACTER_ENCODING})
    public String getXMLContentType() {
        return this.contentType;
    }

    public void setXMLContentType(String content) {
        this.contentType = content;
    }
}
