package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/ExtensionType.class */
public interface ExtensionType extends Annotated, TypedXmlWriter {
    @XmlAttribute
    ExtensionType base(QName qName);
}
