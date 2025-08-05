package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser;
import java.io.IOException;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLModel.class */
public interface WSDLModel extends WSDLExtensible {
    WSDLPortType getPortType(@NotNull QName qName);

    WSDLBoundPortType getBinding(@NotNull QName qName);

    WSDLBoundPortType getBinding(@NotNull QName qName, @NotNull QName qName2);

    WSDLService getService(@NotNull QName qName);

    @NotNull
    Map<QName, ? extends WSDLPortType> getPortTypes();

    @NotNull
    Map<QName, ? extends WSDLBoundPortType> getBindings();

    @NotNull
    Map<QName, ? extends WSDLService> getServices();

    QName getFirstServiceName();

    WSDLMessage getMessage(QName qName);

    @NotNull
    Map<QName, ? extends WSDLMessage> getMessages();

    PolicyMap getPolicyMap();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLModel$WSDLParser.class */
    public static class WSDLParser {
        @NotNull
        public static WSDLModel parse(XMLEntityResolver.Parser wsdlEntityParser, XMLEntityResolver resolver, boolean isClientSide, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
            return parse(wsdlEntityParser, resolver, isClientSide, Container.NONE, extensions);
        }

        @NotNull
        public static WSDLModel parse(XMLEntityResolver.Parser wsdlEntityParser, XMLEntityResolver resolver, boolean isClientSide, @NotNull Container container, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
            return parse(wsdlEntityParser, resolver, isClientSide, container, PolicyResolverFactory.create(), extensions);
        }

        @NotNull
        public static WSDLModel parse(XMLEntityResolver.Parser wsdlEntityParser, XMLEntityResolver resolver, boolean isClientSide, @NotNull Container container, PolicyResolver policyResolver, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
            return RuntimeWSDLParser.parse(wsdlEntityParser, resolver, isClientSide, container, policyResolver, extensions);
        }
    }
}
