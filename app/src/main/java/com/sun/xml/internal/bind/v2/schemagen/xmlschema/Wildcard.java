package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Wildcard.class */
public interface Wildcard extends Annotated, TypedXmlWriter {
    @XmlAttribute
    Wildcard processContents(String str);

    @XmlAttribute
    Wildcard namespace(String[] strArr);

    @XmlAttribute
    Wildcard namespace(String str);
}
