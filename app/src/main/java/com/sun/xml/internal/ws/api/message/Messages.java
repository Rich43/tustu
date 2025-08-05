package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.message.saaj.SAAJFactory;
import com.sun.xml.internal.ws.api.pipe.Codecs;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.message.AttachmentSetImpl;
import com.sun.xml.internal.ws.message.DOMMessage;
import com.sun.xml.internal.ws.message.EmptyMessageImpl;
import com.sun.xml.internal.ws.message.ProblemActionHeader;
import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
import com.sun.xml.internal.ws.message.source.ProtocolSourceMessage;
import com.sun.xml.internal.ws.message.stream.PayloadStreamReaderMessage;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderException;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.util.DOMUtil;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/Messages.class */
public abstract class Messages {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Messages.class.desiredAssertionStatus();
    }

    private Messages() {
    }

    public static Message create(JAXBContext context, Object jaxbObject, SOAPVersion soapVersion) {
        return JAXBMessage.create(context, jaxbObject, soapVersion);
    }

    public static Message createRaw(JAXBContext context, Object jaxbObject, SOAPVersion soapVersion) {
        return JAXBMessage.createRaw(context, jaxbObject, soapVersion);
    }

    public static Message create(Marshaller marshaller, Object jaxbObject, SOAPVersion soapVersion) {
        return create(BindingContextFactory.getBindingContext(marshaller).getJAXBContext(), jaxbObject, soapVersion);
    }

    public static Message create(SOAPMessage saaj) {
        return SAAJFactory.create(saaj);
    }

    public static Message createUsingPayload(Source payload, SOAPVersion ver) {
        if (payload instanceof DOMSource) {
            if (((DOMSource) payload).getNode() == null) {
                return new EmptyMessageImpl(ver);
            }
        } else if (payload instanceof StreamSource) {
            StreamSource ss = (StreamSource) payload;
            if (ss.getInputStream() == null && ss.getReader() == null && ss.getSystemId() == null) {
                return new EmptyMessageImpl(ver);
            }
        } else if (payload instanceof SAXSource) {
            SAXSource ss2 = (SAXSource) payload;
            if (ss2.getInputSource() == null && ss2.getXMLReader() == null) {
                return new EmptyMessageImpl(ver);
            }
        }
        return new PayloadSourceMessage(payload, ver);
    }

    public static Message createUsingPayload(XMLStreamReader payload, SOAPVersion ver) {
        return new PayloadStreamReaderMessage(payload, ver);
    }

    public static Message createUsingPayload(Element payload, SOAPVersion ver) {
        return new DOMMessage(ver, payload);
    }

    public static Message create(Element soapEnvelope) {
        SOAPVersion ver = SOAPVersion.fromNsUri(soapEnvelope.getNamespaceURI());
        Element header = DOMUtil.getFirstChild(soapEnvelope, ver.nsUri, "Header");
        HeaderList headers = null;
        if (header != null) {
            Node firstChild = header.getFirstChild();
            while (true) {
                Node n2 = firstChild;
                if (n2 == null) {
                    break;
                }
                if (n2.getNodeType() == 1) {
                    if (headers == null) {
                        headers = new HeaderList(ver);
                    }
                    headers.add(Headers.create((Element) n2));
                }
                firstChild = n2.getNextSibling();
            }
        }
        Element body = DOMUtil.getFirstChild(soapEnvelope, ver.nsUri, "Body");
        if (body == null) {
            throw new WebServiceException("Message doesn't have <S:Body> " + ((Object) soapEnvelope));
        }
        Element payload = DOMUtil.getFirstChild(soapEnvelope, ver.nsUri, "Body");
        if (payload == null) {
            return new EmptyMessageImpl(headers, new AttachmentSetImpl(), ver);
        }
        return new DOMMessage(ver, headers, payload);
    }

    public static Message create(Source envelope, SOAPVersion soapVersion) {
        return new ProtocolSourceMessage(envelope, soapVersion);
    }

    public static Message createEmpty(SOAPVersion soapVersion) {
        return new EmptyMessageImpl(soapVersion);
    }

    @NotNull
    public static Message create(@NotNull XMLStreamReader reader) {
        if (reader.getEventType() != 1) {
            XMLStreamReaderUtil.nextElementContent(reader);
        }
        if (!$assertionsDisabled && reader.getEventType() != 1) {
            throw new AssertionError(reader.getEventType());
        }
        SOAPVersion ver = SOAPVersion.fromNsUri(reader.getNamespaceURI());
        return Codecs.createSOAPEnvelopeXmlCodec(ver).decode(reader);
    }

    @NotNull
    public static Message create(@NotNull XMLStreamBuffer xsb) {
        try {
            return create(xsb.readAsXMLStreamReader());
        } catch (XMLStreamException e2) {
            throw new XMLStreamReaderException(e2);
        }
    }

    public static Message create(Throwable t2, SOAPVersion soapVersion) {
        return SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, (CheckedExceptionImpl) null, t2);
    }

    public static Message create(SOAPFault fault) {
        SOAPVersion ver = SOAPVersion.fromNsUri(fault.getNamespaceURI());
        return new DOMMessage(ver, fault);
    }

    public static Message createAddressingFaultMessage(WSBinding binding, QName missingHeader) {
        return createAddressingFaultMessage(binding, null, missingHeader);
    }

    public static Message createAddressingFaultMessage(WSBinding binding, Packet p2, QName missingHeader) {
        AddressingVersion av2 = binding.getAddressingVersion();
        if (av2 == null) {
            throw new WebServiceException(AddressingMessages.ADDRESSING_SHOULD_BE_ENABLED());
        }
        WsaTubeHelper helper = av2.getWsaHelper(null, null, binding);
        return create(helper.newMapRequiredFault(new MissingAddressingHeaderException(missingHeader, p2)));
    }

    public static Message create(@NotNull String unsupportedAction, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        SOAPFault fault;
        QName subcode = av2.actionNotSupportedTag;
        String faultstring = String.format(av2.actionNotSupportedText, unsupportedAction);
        try {
            if (sv == SOAPVersion.SOAP_12) {
                fault = SOAPVersion.SOAP_12.getSOAPFactory().createFault();
                fault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
                fault.appendFaultSubcode(subcode);
                Detail detail = fault.addDetail();
                SOAPElement se = detail.addChildElement(av2.problemActionTag);
                se.addChildElement(av2.actionTag).addTextNode(unsupportedAction);
            } else {
                fault = SOAPVersion.SOAP_11.getSOAPFactory().createFault();
                fault.setFaultCode(subcode);
            }
            fault.setFaultString(faultstring);
            Message faultMessage = SOAPFaultBuilder.createSOAPFaultMessage(sv, fault);
            if (sv == SOAPVersion.SOAP_11) {
                faultMessage.getHeaders().add(new ProblemActionHeader(unsupportedAction, av2));
            }
            return faultMessage;
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }

    @NotNull
    public static Message create(@NotNull SOAPVersion soapVersion, @NotNull ProtocolException pex, @Nullable QName faultcode) {
        return SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, pex, faultcode);
    }
}
