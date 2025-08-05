package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.ws.wsdl.parser.SOAPConstants;
import com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPOperation;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/BindingOperationType.class */
public interface BindingOperationType extends TypedXmlWriter, StartWithExtensionsType {
    @XmlAttribute
    BindingOperationType name(String str);

    @XmlElement(value = "operation", ns = "http://schemas.xmlsoap.org/wsdl/soap/")
    SOAPOperation soapOperation();

    @XmlElement(value = "operation", ns = SOAPConstants.NS_WSDL_SOAP12)
    com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPOperation soap12Operation();

    @XmlElement
    Fault fault();

    @XmlElement
    StartWithExtensionsType output();

    @XmlElement
    StartWithExtensionsType input();
}
