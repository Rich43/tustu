package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/SchemaTop.class */
public interface SchemaTop extends Redefinable, TypedXmlWriter {
    @XmlElement
    TopLevelAttribute attribute();

    @XmlElement
    TopLevelElement element();
}
