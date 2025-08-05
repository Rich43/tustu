package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement(SchemaSymbols.ATTVAL_RESTRICTION)
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/ComplexRestriction.class */
public interface ComplexRestriction extends Annotated, AttrDecls, TypeDefParticle, TypedXmlWriter {
    @XmlAttribute
    ComplexRestriction base(QName qName);
}
