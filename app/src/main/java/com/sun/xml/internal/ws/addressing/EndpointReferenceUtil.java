package com.sun.xml.internal.ws.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferSource;
import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
import com.sun.xml.internal.ws.util.DOMUtil;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/EndpointReferenceUtil.class */
public class EndpointReferenceUtil {
    private static boolean w3cMetadataWritten;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EndpointReferenceUtil.class.desiredAssertionStatus();
        w3cMetadataWritten = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends EndpointReference> T transform(Class<T> clazz, @NotNull EndpointReference endpointReference) {
        if (!$assertionsDisabled && endpointReference == 0) {
            throw new AssertionError();
        }
        if (clazz.isAssignableFrom(W3CEndpointReference.class)) {
            if (endpointReference instanceof W3CEndpointReference) {
                return endpointReference;
            }
            if (endpointReference instanceof MemberSubmissionEndpointReference) {
                return toW3CEpr((MemberSubmissionEndpointReference) endpointReference);
            }
        } else if (clazz.isAssignableFrom(MemberSubmissionEndpointReference.class)) {
            if (endpointReference instanceof W3CEndpointReference) {
                return toMSEpr((W3CEndpointReference) endpointReference);
            }
            if (endpointReference instanceof MemberSubmissionEndpointReference) {
                return endpointReference;
            }
        }
        throw new WebServiceException("Unknwon EndpointReference: " + ((Object) endpointReference.getClass()));
    }

    private static W3CEndpointReference toW3CEpr(MemberSubmissionEndpointReference msEpr) {
        NodeList nl;
        StreamWriterBufferCreator writer = new StreamWriterBufferCreator();
        w3cMetadataWritten = false;
        try {
            writer.writeStartDocument();
            writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "EndpointReference", AddressingVersion.W3C.nsUri);
            writer.writeNamespace(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.nsUri);
            writer.writeStartElement(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.eprType.address, AddressingVersion.W3C.nsUri);
            writer.writeCharacters(msEpr.addr.uri);
            writer.writeEndElement();
            if ((msEpr.referenceProperties != null && msEpr.referenceProperties.elements.size() > 0) || (msEpr.referenceParameters != null && msEpr.referenceParameters.elements.size() > 0)) {
                writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "ReferenceParameters", AddressingVersion.W3C.nsUri);
                if (msEpr.referenceProperties != null) {
                    for (Element e2 : msEpr.referenceProperties.elements) {
                        DOMUtil.serializeNode(e2, writer);
                    }
                }
                if (msEpr.referenceParameters != null) {
                    for (Element e3 : msEpr.referenceParameters.elements) {
                        DOMUtil.serializeNode(e3, writer);
                    }
                }
                writer.writeEndElement();
            }
            Element wsdlElement = null;
            if (msEpr.elements != null && msEpr.elements.size() > 0) {
                for (Element e4 : msEpr.elements) {
                    if (e4.getNamespaceURI().equals(MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI()) && e4.getLocalName().equals(MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart()) && (nl = e4.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) != null) {
                        wsdlElement = (Element) nl.item(0);
                    }
                }
            }
            if (wsdlElement != null) {
                DOMUtil.serializeNode(wsdlElement, writer);
            }
            if (w3cMetadataWritten) {
                writer.writeEndElement();
            }
            if (msEpr.elements != null && msEpr.elements.size() > 0) {
                for (Element e5 : msEpr.elements) {
                    if (!e5.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/") || e5.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) {
                    }
                    DOMUtil.serializeNode(e5, writer);
                }
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            return new W3CEndpointReference(new XMLStreamBufferSource(writer.getXMLStreamBuffer()));
        } catch (XMLStreamException e6) {
            throw new WebServiceException(e6);
        }
    }

    private static MemberSubmissionEndpointReference toMSEpr(W3CEndpointReference w3cEpr) throws DOMException {
        DOMResult result = new DOMResult();
        w3cEpr.writeTo(result);
        Node eprNode = result.getNode();
        Element e2 = DOMUtil.getFirstElementChild(eprNode);
        if (e2 == null) {
            return null;
        }
        MemberSubmissionEndpointReference msEpr = new MemberSubmissionEndpointReference();
        NodeList nodes = e2.getChildNodes();
        for (int i2 = 0; i2 < nodes.getLength(); i2++) {
            if (nodes.item(i2).getNodeType() == 1) {
                Element child = (Element) nodes.item(i2);
                if (child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri) && child.getLocalName().equals(AddressingVersion.W3C.eprType.address)) {
                    if (msEpr.addr == null) {
                        msEpr.addr = new MemberSubmissionEndpointReference.Address();
                    }
                    msEpr.addr.uri = XmlUtil.getTextForNode(child);
                } else if (child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri) && child.getLocalName().equals("ReferenceParameters")) {
                    NodeList refParams = child.getChildNodes();
                    for (int j2 = 0; j2 < refParams.getLength(); j2++) {
                        if (refParams.item(j2).getNodeType() == 1) {
                            if (msEpr.referenceParameters == null) {
                                msEpr.referenceParameters = new MemberSubmissionEndpointReference.Elements();
                                msEpr.referenceParameters.elements = new ArrayList();
                            }
                            msEpr.referenceParameters.elements.add((Element) refParams.item(j2));
                        }
                    }
                } else if (child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri) && child.getLocalName().equals(AddressingVersion.W3C.eprType.wsdlMetadata.getLocalPart())) {
                    NodeList metadata = child.getChildNodes();
                    String wsdlLocation = child.getAttributeNS(W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_NAMESPACE, W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_LOCALNAME);
                    Element wsdlDefinitions = null;
                    for (int j3 = 0; j3 < metadata.getLength(); j3++) {
                        Node node = metadata.item(j3);
                        if (node.getNodeType() == 1) {
                            Element elm = (Element) node;
                            if ((elm.getNamespaceURI().equals(AddressingVersion.W3C.wsdlNsUri) || elm.getNamespaceURI().equals(W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME)) && elm.getLocalName().equals(AddressingVersion.W3C.eprType.serviceName)) {
                                msEpr.serviceName = new MemberSubmissionEndpointReference.ServiceNameType();
                                msEpr.serviceName.portName = elm.getAttribute(AddressingVersion.W3C.eprType.portName);
                                String service = elm.getTextContent();
                                String prefix = XmlUtil.getPrefix(service);
                                String name = XmlUtil.getLocalPart(service);
                                if (name != null) {
                                    if (prefix != null) {
                                        String ns = elm.lookupNamespaceURI(prefix);
                                        if (ns != null) {
                                            msEpr.serviceName.name = new QName(ns, name, prefix);
                                        }
                                    } else {
                                        msEpr.serviceName.name = new QName(null, name);
                                    }
                                    msEpr.serviceName.attributes = getAttributes(elm);
                                }
                            } else if ((elm.getNamespaceURI().equals(AddressingVersion.W3C.wsdlNsUri) || elm.getNamespaceURI().equals(W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME)) && elm.getLocalName().equals(AddressingVersion.W3C.eprType.portTypeName)) {
                                msEpr.portTypeName = new MemberSubmissionEndpointReference.AttributedQName();
                                String portType = elm.getTextContent();
                                String prefix2 = XmlUtil.getPrefix(portType);
                                String name2 = XmlUtil.getLocalPart(portType);
                                if (name2 != null) {
                                    if (prefix2 != null) {
                                        String ns2 = elm.lookupNamespaceURI(prefix2);
                                        if (ns2 != null) {
                                            msEpr.portTypeName.name = new QName(ns2, name2, prefix2);
                                        }
                                    } else {
                                        msEpr.portTypeName.name = new QName(null, name2);
                                    }
                                    msEpr.portTypeName.attributes = getAttributes(elm);
                                }
                            } else if (elm.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/") && elm.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) {
                                wsdlDefinitions = elm;
                            } else {
                                if (msEpr.elements == null) {
                                    msEpr.elements = new ArrayList();
                                }
                                msEpr.elements.add(elm);
                            }
                        }
                    }
                    Document doc = DOMUtil.createDom();
                    Element mexEl = doc.createElementNS(MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI(), MemberSubmissionAddressingConstants.MEX_METADATA.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart());
                    Element metadataEl = doc.createElementNS(MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getNamespaceURI(), MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getLocalPart());
                    metadataEl.setAttribute(MemberSubmissionAddressingConstants.MEX_METADATA_DIALECT_ATTRIBUTE, "http://schemas.xmlsoap.org/wsdl/");
                    if (wsdlDefinitions == null && wsdlLocation != null && !wsdlLocation.equals("")) {
                        String wsdlLocation2 = wsdlLocation.trim();
                        String wsdlTns = wsdlLocation2.substring(0, wsdlLocation2.indexOf(32));
                        String wsdlLocation3 = wsdlLocation2.substring(wsdlLocation2.indexOf(32) + 1);
                        Element wsdlEl = doc.createElementNS("http://schemas.xmlsoap.org/wsdl/", "wsdl:" + WSDLConstants.QNAME_DEFINITIONS.getLocalPart());
                        Element wsdlImportEl = doc.createElementNS("http://schemas.xmlsoap.org/wsdl/", "wsdl:" + WSDLConstants.QNAME_IMPORT.getLocalPart());
                        wsdlImportEl.setAttribute(Constants.ATTRNAME_NAMESPACE, wsdlTns);
                        wsdlImportEl.setAttribute("location", wsdlLocation3);
                        wsdlEl.appendChild(wsdlImportEl);
                        metadataEl.appendChild(wsdlEl);
                    } else if (wsdlDefinitions != null) {
                        metadataEl.appendChild(wsdlDefinitions);
                    }
                    mexEl.appendChild(metadataEl);
                    if (msEpr.elements == null) {
                        msEpr.elements = new ArrayList();
                    }
                    msEpr.elements.add(mexEl);
                } else {
                    if (msEpr.elements == null) {
                        msEpr.elements = new ArrayList();
                    }
                    msEpr.elements.add(child);
                }
            } else if (nodes.item(i2).getNodeType() == 2) {
                Node n2 = nodes.item(i2);
                if (msEpr.attributes == null) {
                    msEpr.attributes = new HashMap();
                    String prefix3 = fixNull(n2.getPrefix());
                    String ns3 = fixNull(n2.getNamespaceURI());
                    String localName = n2.getLocalName();
                    msEpr.attributes.put(new QName(ns3, localName, prefix3), n2.getNodeValue());
                }
            }
        }
        return msEpr;
    }

    private static Map<QName, String> getAttributes(Node node) {
        Map<QName, String> attribs = null;
        NamedNodeMap nm = node.getAttributes();
        for (int i2 = 0; i2 < nm.getLength(); i2++) {
            if (attribs == null) {
                attribs = new HashMap<>();
            }
            Node n2 = nm.item(i2);
            String prefix = fixNull(n2.getPrefix());
            String ns = fixNull(n2.getNamespaceURI());
            String localName = n2.getLocalName();
            if (!prefix.equals("xmlns") && ((prefix.length() != 0 || !localName.equals("xmlns")) && !localName.equals(AddressingVersion.W3C.eprType.portName))) {
                attribs.put(new QName(ns, localName, prefix), n2.getNodeValue());
            }
        }
        return attribs;
    }

    @NotNull
    private static String fixNull(@Nullable String s2) {
        if (s2 == null) {
            return "";
        }
        return s2;
    }
}
