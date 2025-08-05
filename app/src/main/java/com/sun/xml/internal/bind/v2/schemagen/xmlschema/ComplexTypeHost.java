package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/ComplexTypeHost.class */
public interface ComplexTypeHost extends TypeHost, TypedXmlWriter {
    @XmlElement
    ComplexType complexType();
}
