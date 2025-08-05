package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement(SchemaSymbols.ATTVAL_LIST)
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/List.class */
public interface List extends Annotated, SimpleTypeHost, TypedXmlWriter {
    @XmlAttribute
    List itemType(QName qName);
}
