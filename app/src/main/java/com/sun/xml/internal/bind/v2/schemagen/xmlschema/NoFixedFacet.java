package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/NoFixedFacet.class */
public interface NoFixedFacet extends Annotated, TypedXmlWriter {
    @XmlAttribute
    NoFixedFacet value(String str);
}
