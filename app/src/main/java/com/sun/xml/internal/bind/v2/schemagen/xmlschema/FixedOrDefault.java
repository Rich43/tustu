package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/FixedOrDefault.class */
public interface FixedOrDefault extends TypedXmlWriter {
    @XmlAttribute("default")
    FixedOrDefault _default(String str);

    @XmlAttribute
    FixedOrDefault fixed(String str);
}
