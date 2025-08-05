package com.sun.xml.internal.ws.wsdl.writer.document;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import com.sun.xml.internal.ws.wsdl.parser.SOAPConstants;
import com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPBinding;
import javax.xml.namespace.QName;

@XmlElement(DeploymentDescriptorParser.ATTR_BINDING)
/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/document/Binding.class */
public interface Binding extends TypedXmlWriter, StartWithExtensionsType {
    @XmlAttribute
    Binding type(QName qName);

    @XmlAttribute
    Binding name(String str);

    @XmlElement
    BindingOperationType operation();

    @XmlElement(value = DeploymentDescriptorParser.ATTR_BINDING, ns = "http://schemas.xmlsoap.org/wsdl/soap/")
    SOAPBinding soapBinding();

    @XmlElement(value = DeploymentDescriptorParser.ATTR_BINDING, ns = SOAPConstants.NS_WSDL_SOAP12)
    com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPBinding soap12Binding();
}
