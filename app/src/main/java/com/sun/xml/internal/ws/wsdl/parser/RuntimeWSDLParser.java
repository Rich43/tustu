package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferMark;
import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.BindingIDFactory;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSDLLocator;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLDescriptorKind;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.api.wsdl.parser.MetaDataResolver;
import com.sun.xml.internal.ws.api.wsdl.parser.MetadataResolverFactory;
import com.sun.xml.internal.ws.api.wsdl.parser.PolicyWSDLParserExtension;
import com.sun.xml.internal.ws.api.wsdl.parser.ServiceDescriptor;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
import com.sun.xml.internal.ws.model.wsdl.WSDLBoundFaultImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLBoundOperationImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLFaultImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLInputImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLMessageImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLOperationImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLOutputImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLPartDescriptorImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLPartImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLPortTypeImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.resources.WsdlmodelMessages;
import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import com.sun.xml.internal.ws.util.ServiceFinder;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/RuntimeWSDLParser.class */
public class RuntimeWSDLParser {
    private final EditableWSDLModel wsdlDoc;
    private String targetNamespace;
    private final XMLEntityResolver resolver;
    private final PolicyResolver policyResolver;
    private final WSDLParserExtension extensionFacade;
    private final WSDLParserExtensionContextImpl context;
    List<WSDLParserExtension> extensions;
    private static final Logger LOGGER;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Set<String> importedWSDLs = new HashSet();
    Map<String, String> wsdldef_nsdecl = new HashMap();
    Map<String, String> service_nsdecl = new HashMap();
    Map<String, String> port_nsdecl = new HashMap();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/RuntimeWSDLParser$BindingMode.class */
    private enum BindingMode {
        INPUT,
        OUTPUT,
        FAULT
    }

    static {
        $assertionsDisabled = !RuntimeWSDLParser.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(RuntimeWSDLParser.class.getName());
    }

    public static WSDLModel parse(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, @NotNull EntityResolver resolver, boolean isClientSide, Container container, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
        return parse(wsdlLoc, wsdlSource, resolver, isClientSide, container, Service.class, PolicyResolverFactory.create(), extensions);
    }

    public static WSDLModel parse(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, @NotNull EntityResolver resolver, boolean isClientSide, Container container, Class serviceClass, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
        return parse(wsdlLoc, wsdlSource, resolver, isClientSide, container, serviceClass, PolicyResolverFactory.create(), extensions);
    }

    public static WSDLModel parse(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, @NotNull EntityResolver resolver, boolean isClientSide, Container container, @NotNull PolicyResolver policyResolver, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
        return parse(wsdlLoc, wsdlSource, resolver, isClientSide, container, Service.class, policyResolver, extensions);
    }

    public static WSDLModel parse(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, @NotNull EntityResolver resolver, boolean isClientSide, Container container, Class serviceClass, @NotNull PolicyResolver policyResolver, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
        return parse(wsdlLoc, wsdlSource, resolver, isClientSide, container, serviceClass, policyResolver, false, extensions);
    }

    public static WSDLModel parse(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, @NotNull EntityResolver resolver, boolean isClientSide, Container container, Class serviceClass, @NotNull PolicyResolver policyResolver, boolean isUseStreamFromEntityResolverWrapper, WSDLParserExtension... extensions) throws SAXException, XMLStreamException, IOException {
        if (!$assertionsDisabled && resolver == null) {
            throw new AssertionError();
        }
        RuntimeWSDLParser wsdlParser = new RuntimeWSDLParser(wsdlSource.getSystemId(), new EntityResolverWrapper(resolver, isUseStreamFromEntityResolverWrapper), isClientSide, container, policyResolver, extensions);
        try {
            XMLEntityResolver.Parser parser = wsdlParser.resolveWSDL(wsdlLoc, wsdlSource, serviceClass);
            if (!hasWSDLDefinitions(parser.parser)) {
                throw new XMLStreamException(ClientMessages.RUNTIME_WSDLPARSER_INVALID_WSDL(parser.systemId, WSDLConstants.QNAME_DEFINITIONS, parser.parser.getName(), parser.parser.getLocation()));
            }
            wsdlParser.extensionFacade.start(wsdlParser.context);
            wsdlParser.parseWSDL(parser, false);
            wsdlParser.wsdlDoc.freeze();
            wsdlParser.extensionFacade.finished(wsdlParser.context);
            wsdlParser.extensionFacade.postFinished(wsdlParser.context);
            if (wsdlParser.wsdlDoc.getServices().isEmpty()) {
                throw new WebServiceException(ClientMessages.WSDL_CONTAINS_NO_SERVICE(wsdlLoc));
            }
            return wsdlParser.wsdlDoc;
        } catch (IOException e2) {
            if (wsdlLoc == null) {
                throw e2;
            }
            return tryWithMex(wsdlParser, wsdlLoc, resolver, isClientSide, container, e2, serviceClass, policyResolver, extensions);
        } catch (XMLStreamException e3) {
            if (wsdlLoc == null) {
                throw e3;
            }
            return tryWithMex(wsdlParser, wsdlLoc, resolver, isClientSide, container, e3, serviceClass, policyResolver, extensions);
        }
    }

    private static WSDLModel tryWithMex(@NotNull RuntimeWSDLParser wsdlParser, @NotNull URL wsdlLoc, @NotNull EntityResolver resolver, boolean isClientSide, Container container, Throwable e2, Class serviceClass, PolicyResolver policyResolver, WSDLParserExtension... extensions) throws SAXException, XMLStreamException {
        ArrayList<Throwable> exceptions = new ArrayList<>();
        try {
            WSDLModel wsdlModel = wsdlParser.parseUsingMex(wsdlLoc, resolver, isClientSide, container, serviceClass, policyResolver, extensions);
            if (wsdlModel == null) {
                throw new WebServiceException(ClientMessages.FAILED_TO_PARSE(wsdlLoc.toExternalForm(), e2.getMessage()), e2);
            }
            return wsdlModel;
        } catch (IOException e1) {
            exceptions.add(e2);
            exceptions.add(e1);
            throw new InaccessibleWSDLException(exceptions);
        } catch (URISyntaxException e12) {
            exceptions.add(e2);
            exceptions.add(e12);
            throw new InaccessibleWSDLException(exceptions);
        }
    }

    private WSDLModel parseUsingMex(@NotNull URL wsdlLoc, @NotNull EntityResolver resolver, boolean isClientSide, Container container, Class serviceClass, PolicyResolver policyResolver, WSDLParserExtension[] extensions) throws SAXException, XMLStreamException, URISyntaxException, IOException {
        MetaDataResolver mdResolver = null;
        ServiceDescriptor serviceDescriptor = null;
        RuntimeWSDLParser wsdlParser = null;
        Iterator it = ServiceFinder.find(MetadataResolverFactory.class).iterator();
        while (it.hasNext()) {
            MetadataResolverFactory resolverFactory = (MetadataResolverFactory) it.next();
            mdResolver = resolverFactory.metadataResolver(resolver);
            serviceDescriptor = mdResolver.resolve(wsdlLoc.toURI());
            if (serviceDescriptor != null) {
                break;
            }
        }
        if (serviceDescriptor != null) {
            List<? extends Source> wsdls = serviceDescriptor.getWSDLs();
            wsdlParser = new RuntimeWSDLParser(wsdlLoc.toExternalForm(), new MexEntityResolver(wsdls), isClientSide, container, policyResolver, extensions);
            wsdlParser.extensionFacade.start(wsdlParser.context);
            for (Source src : wsdls) {
                String systemId = src.getSystemId();
                XMLEntityResolver.Parser parser = wsdlParser.resolver.resolveEntity(null, systemId);
                wsdlParser.parseWSDL(parser, false);
            }
        }
        if ((mdResolver == null || serviceDescriptor == null) && ((wsdlLoc.getProtocol().equals("http") || wsdlLoc.getProtocol().equals("https")) && wsdlLoc.getQuery() == null)) {
            String urlString = wsdlLoc.toExternalForm();
            URL wsdlLoc2 = new URL(urlString + "?wsdl");
            wsdlParser = new RuntimeWSDLParser(wsdlLoc2.toExternalForm(), new EntityResolverWrapper(resolver), isClientSide, container, policyResolver, extensions);
            wsdlParser.extensionFacade.start(wsdlParser.context);
            XMLEntityResolver.Parser parser2 = resolveWSDL(wsdlLoc2, new StreamSource(wsdlLoc2.toExternalForm()), serviceClass);
            wsdlParser.parseWSDL(parser2, false);
        }
        if (wsdlParser == null) {
            return null;
        }
        wsdlParser.wsdlDoc.freeze();
        wsdlParser.extensionFacade.finished(wsdlParser.context);
        wsdlParser.extensionFacade.postFinished(wsdlParser.context);
        return wsdlParser.wsdlDoc;
    }

    private static boolean hasWSDLDefinitions(XMLStreamReader reader) {
        XMLStreamReaderUtil.nextElementContent(reader);
        return reader.getName().equals(WSDLConstants.QNAME_DEFINITIONS);
    }

    public static WSDLModel parse(XMLEntityResolver.Parser wsdl, XMLEntityResolver resolver, boolean isClientSide, Container container, PolicyResolver policyResolver, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
        if (!$assertionsDisabled && resolver == null) {
            throw new AssertionError();
        }
        RuntimeWSDLParser parser = new RuntimeWSDLParser(wsdl.systemId.toExternalForm(), resolver, isClientSide, container, policyResolver, extensions);
        parser.extensionFacade.start(parser.context);
        parser.parseWSDL(wsdl, false);
        parser.wsdlDoc.freeze();
        parser.extensionFacade.finished(parser.context);
        parser.extensionFacade.postFinished(parser.context);
        return parser.wsdlDoc;
    }

    public static WSDLModel parse(XMLEntityResolver.Parser wsdl, XMLEntityResolver resolver, boolean isClientSide, Container container, WSDLParserExtension... extensions) throws XMLStreamException, SAXException, IOException {
        if (!$assertionsDisabled && resolver == null) {
            throw new AssertionError();
        }
        RuntimeWSDLParser parser = new RuntimeWSDLParser(wsdl.systemId.toExternalForm(), resolver, isClientSide, container, PolicyResolverFactory.create(), extensions);
        parser.extensionFacade.start(parser.context);
        parser.parseWSDL(wsdl, false);
        parser.wsdlDoc.freeze();
        parser.extensionFacade.finished(parser.context);
        parser.extensionFacade.postFinished(parser.context);
        return parser.wsdlDoc;
    }

    private RuntimeWSDLParser(@NotNull String sourceLocation, XMLEntityResolver resolver, boolean isClientSide, Container container, PolicyResolver policyResolver, WSDLParserExtension... extensions) {
        this.wsdlDoc = sourceLocation != null ? new WSDLModelImpl(sourceLocation) : new WSDLModelImpl();
        this.resolver = resolver;
        this.policyResolver = policyResolver;
        this.extensions = new ArrayList();
        this.context = new WSDLParserExtensionContextImpl(this.wsdlDoc, isClientSide, container, policyResolver);
        boolean isPolicyExtensionFound = false;
        for (WSDLParserExtension e2 : extensions) {
            if (e2 instanceof PolicyWSDLParserExtension) {
                isPolicyExtensionFound = true;
            }
            register(e2);
        }
        if (!isPolicyExtensionFound) {
            register(new com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLParserExtension());
        }
        register(new MemberSubmissionAddressingWSDLParserExtension());
        register(new W3CAddressingWSDLParserExtension());
        register(new W3CAddressingMetadataWSDLParserExtension());
        this.extensionFacade = new WSDLParserExtensionFacade((WSDLParserExtension[]) this.extensions.toArray(new WSDLParserExtension[0]));
    }

    private XMLEntityResolver.Parser resolveWSDL(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, Class serviceClass) throws SAXException, XMLStreamException, IOException {
        URL ru;
        String systemId = wsdlSource.getSystemId();
        XMLEntityResolver.Parser parser = this.resolver.resolveEntity(null, systemId);
        if (parser == null && wsdlLoc != null) {
            String exForm = wsdlLoc.toExternalForm();
            parser = this.resolver.resolveEntity(null, exForm);
            if (parser == null && serviceClass != null && (ru = serviceClass.getResource(".")) != null) {
                String ruExForm = ru.toExternalForm();
                if (exForm.startsWith(ruExForm)) {
                    parser = this.resolver.resolveEntity(null, exForm.substring(ruExForm.length()));
                }
            }
        }
        if (parser == null) {
            if (isKnownReadableSource(wsdlSource)) {
                parser = new XMLEntityResolver.Parser(wsdlLoc, createReader(wsdlSource));
            } else if (wsdlLoc != null) {
                parser = new XMLEntityResolver.Parser(wsdlLoc, createReader(wsdlLoc, serviceClass));
            }
            if (parser == null) {
                parser = new XMLEntityResolver.Parser(wsdlLoc, createReader(wsdlSource));
            }
        }
        return parser;
    }

    private boolean isKnownReadableSource(Source wsdlSource) {
        if (wsdlSource instanceof StreamSource) {
            return (((StreamSource) wsdlSource).getInputStream() == null && ((StreamSource) wsdlSource).getReader() == null) ? false : true;
        }
        return false;
    }

    private XMLStreamReader createReader(@NotNull Source src) throws XMLStreamException {
        return new TidyXMLStreamReader(SourceReaderFactory.createSourceReader(src, true), null);
    }

    private void parseImport(@NotNull URL wsdlLoc) throws SAXException, XMLStreamException, IOException {
        String systemId = wsdlLoc.toExternalForm();
        XMLEntityResolver.Parser parser = this.resolver.resolveEntity(null, systemId);
        if (parser == null) {
            parser = new XMLEntityResolver.Parser(wsdlLoc, createReader(wsdlLoc));
        }
        parseWSDL(parser, true);
    }

    private void parseWSDL(XMLEntityResolver.Parser parser, boolean imported) throws XMLStreamException, SAXException, IOException {
        XMLStreamReader reader = parser.parser;
        try {
            if (parser.systemId == null || this.importedWSDLs.add(parser.systemId.toExternalForm())) {
                if (reader.getEventType() == 7) {
                    XMLStreamReaderUtil.nextElementContent(reader);
                }
                if (WSDLConstants.QNAME_DEFINITIONS.equals(reader.getName())) {
                    readNSDecl(this.wsdldef_nsdecl, reader);
                }
                if (reader.getEventType() != 8 && reader.getName().equals(WSDLConstants.QNAME_SCHEMA) && imported) {
                    LOGGER.warning(WsdlmodelMessages.WSDL_IMPORT_SHOULD_BE_WSDL(parser.systemId));
                    this.wsdldef_nsdecl = new HashMap();
                    reader.close();
                    return;
                }
                String tns = ParserUtil.getMandatoryNonEmptyAttribute(reader, WSDLConstants.ATTR_TNS);
                String oldTargetNamespace = this.targetNamespace;
                this.targetNamespace = tns;
                while (XMLStreamReaderUtil.nextElementContent(reader) != 2 && reader.getEventType() != 8) {
                    QName name = reader.getName();
                    if (WSDLConstants.QNAME_IMPORT.equals(name)) {
                        parseImport(parser.systemId, reader);
                    } else if (WSDLConstants.QNAME_MESSAGE.equals(name)) {
                        parseMessage(reader);
                    } else if (WSDLConstants.QNAME_PORT_TYPE.equals(name)) {
                        parsePortType(reader);
                    } else if (WSDLConstants.QNAME_BINDING.equals(name)) {
                        parseBinding(reader);
                    } else if (WSDLConstants.QNAME_SERVICE.equals(name)) {
                        parseService(reader);
                    } else {
                        this.extensionFacade.definitionsElements(reader);
                    }
                }
                this.targetNamespace = oldTargetNamespace;
                this.wsdldef_nsdecl = new HashMap();
                reader.close();
            }
        } finally {
            this.wsdldef_nsdecl = new HashMap();
            reader.close();
        }
    }

    private void parseService(XMLStreamReader reader) {
        this.service_nsdecl.putAll(this.wsdldef_nsdecl);
        readNSDecl(this.service_nsdecl, reader);
        String serviceName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        EditableWSDLService service = new WSDLServiceImpl(reader, this.wsdlDoc, new QName(this.targetNamespace, serviceName));
        this.extensionFacade.serviceAttributes(service, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (WSDLConstants.QNAME_PORT.equals(name)) {
                parsePort(reader, service);
                if (reader.getEventType() != 2) {
                    XMLStreamReaderUtil.next(reader);
                }
            } else {
                this.extensionFacade.serviceElements(service, reader);
            }
        }
        this.wsdlDoc.addService(service);
        this.service_nsdecl = new HashMap();
    }

    private void parsePort(XMLStreamReader reader, EditableWSDLService service) {
        this.port_nsdecl.putAll(this.service_nsdecl);
        readNSDecl(this.port_nsdecl, reader);
        String portName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        String binding = ParserUtil.getMandatoryNonEmptyAttribute(reader, DeploymentDescriptorParser.ATTR_BINDING);
        QName bindingName = ParserUtil.getQName(reader, binding);
        QName portQName = new QName(service.getName().getNamespaceURI(), portName);
        EditableWSDLPort port = new WSDLPortImpl(reader, service, portQName, bindingName);
        this.extensionFacade.portAttributes(port, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (SOAPConstants.QNAME_ADDRESS.equals(name) || SOAPConstants.QNAME_SOAP12ADDRESS.equals(name)) {
                String location = ParserUtil.getMandatoryNonEmptyAttribute(reader, "location");
                if (location != null) {
                    try {
                        port.setAddress(new EndpointAddress(location));
                    } catch (URISyntaxException e2) {
                    }
                }
                XMLStreamReaderUtil.next(reader);
            } else if (AddressingVersion.W3C.nsUri.equals(name.getNamespaceURI()) && "EndpointReference".equals(name.getLocalPart())) {
                try {
                    StreamReaderBufferCreator creator = new StreamReaderBufferCreator(new MutableXMLStreamBuffer());
                    XMLStreamBuffer eprbuffer = new XMLStreamBufferMark(this.port_nsdecl, creator);
                    creator.createElementFragment(reader, false);
                    WSEndpointReference wsepr = new WSEndpointReference(eprbuffer, AddressingVersion.W3C);
                    port.setEPR(wsepr);
                    if (reader.getEventType() == 2 && reader.getName().equals(WSDLConstants.QNAME_PORT)) {
                        break;
                    }
                } catch (XMLStreamException e3) {
                    throw new WebServiceException(e3);
                }
            } else {
                this.extensionFacade.portElements(port, reader);
            }
        }
        if (port.getAddress() == null) {
            try {
                port.setAddress(new EndpointAddress(""));
            } catch (URISyntaxException e4) {
            }
        }
        service.put(portQName, port);
        this.port_nsdecl = new HashMap();
    }

    private void parseBinding(XMLStreamReader reader) {
        String bindingName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        String portTypeName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "type");
        if (bindingName == null || portTypeName == null) {
            XMLStreamReaderUtil.skipElement(reader);
            return;
        }
        EditableWSDLBoundPortType binding = new WSDLBoundPortTypeImpl(reader, this.wsdlDoc, new QName(this.targetNamespace, bindingName), ParserUtil.getQName(reader, portTypeName));
        this.extensionFacade.bindingAttributes(binding, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (WSDLConstants.NS_SOAP_BINDING.equals(name)) {
                String transport = reader.getAttributeValue(null, WSDLConstants.ATTR_TRANSPORT);
                binding.setBindingId(createBindingId(transport, SOAPVersion.SOAP_11));
                String style = reader.getAttributeValue(null, Constants.ATTRNAME_STYLE);
                if (style != null && style.equals(CORBALogDomains.RPC)) {
                    binding.setStyle(SOAPBinding.Style.RPC);
                } else {
                    binding.setStyle(SOAPBinding.Style.DOCUMENT);
                }
                goToEnd(reader);
            } else if (WSDLConstants.NS_SOAP12_BINDING.equals(name)) {
                String transport2 = reader.getAttributeValue(null, WSDLConstants.ATTR_TRANSPORT);
                binding.setBindingId(createBindingId(transport2, SOAPVersion.SOAP_12));
                String style2 = reader.getAttributeValue(null, Constants.ATTRNAME_STYLE);
                if (style2 != null && style2.equals(CORBALogDomains.RPC)) {
                    binding.setStyle(SOAPBinding.Style.RPC);
                } else {
                    binding.setStyle(SOAPBinding.Style.DOCUMENT);
                }
                goToEnd(reader);
            } else if (WSDLConstants.QNAME_OPERATION.equals(name)) {
                parseBindingOperation(reader, binding);
            } else {
                this.extensionFacade.bindingElements(binding, reader);
            }
        }
    }

    private static BindingID createBindingId(String transport, SOAPVersion soapVersion) throws WebServiceException {
        if (!transport.equals("http://schemas.xmlsoap.org/soap/http")) {
            Iterator it = ServiceFinder.find(BindingIDFactory.class).iterator();
            while (it.hasNext()) {
                BindingIDFactory f2 = (BindingIDFactory) it.next();
                BindingID bindingId = f2.create(transport, soapVersion);
                if (bindingId != null) {
                    return bindingId;
                }
            }
        }
        return soapVersion.equals(SOAPVersion.SOAP_11) ? BindingID.SOAP11_HTTP : BindingID.SOAP12_HTTP;
    }

    private void parseBindingOperation(XMLStreamReader reader, EditableWSDLBoundPortType binding) {
        String bindingOpName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        if (bindingOpName == null) {
            XMLStreamReaderUtil.skipElement(reader);
            return;
        }
        QName opName = new QName(binding.getPortTypeName().getNamespaceURI(), bindingOpName);
        EditableWSDLBoundOperation bindingOp = new WSDLBoundOperationImpl(reader, binding, opName);
        binding.put(opName, bindingOp);
        this.extensionFacade.bindingOperationAttributes(bindingOp, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            String style = null;
            if (WSDLConstants.QNAME_INPUT.equals(name)) {
                parseInputBinding(reader, bindingOp);
            } else if (WSDLConstants.QNAME_OUTPUT.equals(name)) {
                parseOutputBinding(reader, bindingOp);
            } else if (WSDLConstants.QNAME_FAULT.equals(name)) {
                parseFaultBinding(reader, bindingOp);
            } else if (SOAPConstants.QNAME_OPERATION.equals(name) || SOAPConstants.QNAME_SOAP12OPERATION.equals(name)) {
                style = reader.getAttributeValue(null, Constants.ATTRNAME_STYLE);
                String soapAction = reader.getAttributeValue(null, "soapAction");
                if (soapAction != null) {
                    bindingOp.setSoapAction(soapAction);
                }
                goToEnd(reader);
            } else {
                this.extensionFacade.bindingOperationElements(bindingOp, reader);
            }
            if (style != null) {
                if (style.equals(CORBALogDomains.RPC)) {
                    bindingOp.setStyle(SOAPBinding.Style.RPC);
                } else {
                    bindingOp.setStyle(SOAPBinding.Style.DOCUMENT);
                }
            } else {
                bindingOp.setStyle(binding.getStyle());
            }
        }
    }

    private void parseInputBinding(XMLStreamReader reader, EditableWSDLBoundOperation bindingOp) {
        boolean bodyFound = false;
        this.extensionFacade.bindingOperationInputAttributes(bindingOp, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if ((SOAPConstants.QNAME_BODY.equals(name) || SOAPConstants.QNAME_SOAP12BODY.equals(name)) && !bodyFound) {
                bodyFound = true;
                bindingOp.setInputExplicitBodyParts(parseSOAPBodyBinding(reader, bindingOp, BindingMode.INPUT));
                goToEnd(reader);
            } else if (SOAPConstants.QNAME_HEADER.equals(name) || SOAPConstants.QNAME_SOAP12HEADER.equals(name)) {
                parseSOAPHeaderBinding(reader, bindingOp.getInputParts());
            } else if (MIMEConstants.QNAME_MULTIPART_RELATED.equals(name)) {
                parseMimeMultipartBinding(reader, bindingOp, BindingMode.INPUT);
            } else {
                this.extensionFacade.bindingOperationInputElements(bindingOp, reader);
            }
        }
    }

    private void parseOutputBinding(XMLStreamReader reader, EditableWSDLBoundOperation bindingOp) {
        boolean bodyFound = false;
        this.extensionFacade.bindingOperationOutputAttributes(bindingOp, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if ((SOAPConstants.QNAME_BODY.equals(name) || SOAPConstants.QNAME_SOAP12BODY.equals(name)) && !bodyFound) {
                bodyFound = true;
                bindingOp.setOutputExplicitBodyParts(parseSOAPBodyBinding(reader, bindingOp, BindingMode.OUTPUT));
                goToEnd(reader);
            } else if (SOAPConstants.QNAME_HEADER.equals(name) || SOAPConstants.QNAME_SOAP12HEADER.equals(name)) {
                parseSOAPHeaderBinding(reader, bindingOp.getOutputParts());
            } else if (MIMEConstants.QNAME_MULTIPART_RELATED.equals(name)) {
                parseMimeMultipartBinding(reader, bindingOp, BindingMode.OUTPUT);
            } else {
                this.extensionFacade.bindingOperationOutputElements(bindingOp, reader);
            }
        }
    }

    private void parseFaultBinding(XMLStreamReader reader, EditableWSDLBoundOperation bindingOp) {
        String faultName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        EditableWSDLBoundFault wsdlBoundFault = new WSDLBoundFaultImpl(reader, faultName, bindingOp);
        bindingOp.addFault(wsdlBoundFault);
        this.extensionFacade.bindingOperationFaultAttributes(wsdlBoundFault, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            this.extensionFacade.bindingOperationFaultElements(wsdlBoundFault, reader);
        }
    }

    private static boolean parseSOAPBodyBinding(XMLStreamReader reader, EditableWSDLBoundOperation op, BindingMode mode) {
        String namespace = reader.getAttributeValue(null, Constants.ATTRNAME_NAMESPACE);
        if (mode == BindingMode.INPUT) {
            op.setRequestNamespace(namespace);
            return parseSOAPBodyBinding(reader, op.getInputParts());
        }
        op.setResponseNamespace(namespace);
        return parseSOAPBodyBinding(reader, op.getOutputParts());
    }

    private static boolean parseSOAPBodyBinding(XMLStreamReader reader, Map<String, ParameterBinding> parts) {
        String partsString = reader.getAttributeValue(null, "parts");
        if (partsString != null) {
            List<String> partsList = XmlUtil.parseTokenList(partsString);
            if (partsList.isEmpty()) {
                parts.put(" ", ParameterBinding.BODY);
                return true;
            }
            for (String part : partsList) {
                parts.put(part, ParameterBinding.BODY);
            }
            return true;
        }
        return false;
    }

    private static void parseSOAPHeaderBinding(XMLStreamReader reader, Map<String, ParameterBinding> parts) {
        String part = reader.getAttributeValue(null, "part");
        if (part == null || part.equals("")) {
            return;
        }
        parts.put(part, ParameterBinding.HEADER);
        goToEnd(reader);
    }

    private static void parseMimeMultipartBinding(XMLStreamReader reader, EditableWSDLBoundOperation op, BindingMode mode) {
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (MIMEConstants.QNAME_PART.equals(name)) {
                parseMIMEPart(reader, op, mode);
            } else {
                XMLStreamReaderUtil.skipElement(reader);
            }
        }
    }

    private static void parseMIMEPart(XMLStreamReader reader, EditableWSDLBoundOperation op, BindingMode mode) {
        boolean bodyFound = false;
        Map<String, ParameterBinding> parts = null;
        if (mode == BindingMode.INPUT) {
            parts = op.getInputParts();
        } else if (mode == BindingMode.OUTPUT) {
            parts = op.getOutputParts();
        } else if (mode == BindingMode.FAULT) {
            parts = op.getFaultParts();
        }
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (SOAPConstants.QNAME_BODY.equals(name) && !bodyFound) {
                bodyFound = true;
                parseSOAPBodyBinding(reader, op, mode);
                XMLStreamReaderUtil.next(reader);
            } else if (SOAPConstants.QNAME_HEADER.equals(name)) {
                bodyFound = true;
                parseSOAPHeaderBinding(reader, parts);
                XMLStreamReaderUtil.next(reader);
            } else if (MIMEConstants.QNAME_CONTENT.equals(name)) {
                String part = reader.getAttributeValue(null, "part");
                String type = reader.getAttributeValue(null, "type");
                if (part == null || type == null) {
                    XMLStreamReaderUtil.skipElement(reader);
                } else {
                    ParameterBinding sb = ParameterBinding.createAttachment(type);
                    if (parts != null && sb != null && part != null) {
                        parts.put(part, sb);
                    }
                    XMLStreamReaderUtil.next(reader);
                }
            } else {
                XMLStreamReaderUtil.skipElement(reader);
            }
        }
    }

    protected void parseImport(@Nullable URL baseURL, XMLStreamReader reader) throws SAXException, XMLStreamException, IOException {
        URL importURL;
        String importLocation = ParserUtil.getMandatoryNonEmptyAttribute(reader, "location");
        if (baseURL != null) {
            importURL = new URL(baseURL, importLocation);
        } else {
            importURL = new URL(importLocation);
        }
        parseImport(importURL);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            XMLStreamReaderUtil.skipElement(reader);
        }
    }

    private void parsePortType(XMLStreamReader reader) {
        String portTypeName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        if (portTypeName == null) {
            XMLStreamReaderUtil.skipElement(reader);
            return;
        }
        EditableWSDLPortType portType = new WSDLPortTypeImpl(reader, this.wsdlDoc, new QName(this.targetNamespace, portTypeName));
        this.extensionFacade.portTypeAttributes(portType, reader);
        this.wsdlDoc.addPortType(portType);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (WSDLConstants.QNAME_OPERATION.equals(name)) {
                parsePortTypeOperation(reader, portType);
            } else {
                this.extensionFacade.portTypeElements(portType, reader);
            }
        }
    }

    private void parsePortTypeOperation(XMLStreamReader reader, EditableWSDLPortType portType) {
        String operationName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        if (operationName == null) {
            XMLStreamReaderUtil.skipElement(reader);
            return;
        }
        QName operationQName = new QName(portType.getName().getNamespaceURI(), operationName);
        EditableWSDLOperation operation = new WSDLOperationImpl(reader, portType, operationQName);
        this.extensionFacade.portTypeOperationAttributes(operation, reader);
        String parameterOrder = ParserUtil.getAttribute(reader, "parameterOrder");
        operation.setParameterOrder(parameterOrder);
        portType.put(operationName, operation);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (name.equals(WSDLConstants.QNAME_INPUT)) {
                parsePortTypeOperationInput(reader, operation);
            } else if (name.equals(WSDLConstants.QNAME_OUTPUT)) {
                parsePortTypeOperationOutput(reader, operation);
            } else if (name.equals(WSDLConstants.QNAME_FAULT)) {
                parsePortTypeOperationFault(reader, operation);
            } else {
                this.extensionFacade.portTypeOperationElements(operation, reader);
            }
        }
    }

    private void parsePortTypeOperationFault(XMLStreamReader reader, EditableWSDLOperation operation) {
        String msg = ParserUtil.getMandatoryNonEmptyAttribute(reader, "message");
        QName msgName = ParserUtil.getQName(reader, msg);
        String name = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        EditableWSDLFault fault = new WSDLFaultImpl(reader, name, msgName, operation);
        operation.addFault(fault);
        this.extensionFacade.portTypeOperationFaultAttributes(fault, reader);
        this.extensionFacade.portTypeOperationFault(operation, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            this.extensionFacade.portTypeOperationFaultElements(fault, reader);
        }
    }

    private void parsePortTypeOperationInput(XMLStreamReader reader, EditableWSDLOperation operation) {
        String msg = ParserUtil.getMandatoryNonEmptyAttribute(reader, "message");
        QName msgName = ParserUtil.getQName(reader, msg);
        String name = ParserUtil.getAttribute(reader, "name");
        EditableWSDLInput input = new WSDLInputImpl(reader, name, msgName, operation);
        operation.setInput(input);
        this.extensionFacade.portTypeOperationInputAttributes(input, reader);
        this.extensionFacade.portTypeOperationInput(operation, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            this.extensionFacade.portTypeOperationInputElements(input, reader);
        }
    }

    private void parsePortTypeOperationOutput(XMLStreamReader reader, EditableWSDLOperation operation) {
        String msg = ParserUtil.getAttribute(reader, "message");
        QName msgName = ParserUtil.getQName(reader, msg);
        String name = ParserUtil.getAttribute(reader, "name");
        EditableWSDLOutput output = new WSDLOutputImpl(reader, name, msgName, operation);
        operation.setOutput(output);
        this.extensionFacade.portTypeOperationOutputAttributes(output, reader);
        this.extensionFacade.portTypeOperationOutput(operation, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            this.extensionFacade.portTypeOperationOutputElements(output, reader);
        }
    }

    private void parseMessage(XMLStreamReader reader) {
        String msgName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
        EditableWSDLMessage msg = new WSDLMessageImpl(reader, new QName(this.targetNamespace, msgName));
        this.extensionFacade.messageAttributes(msg, reader);
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            QName name = reader.getName();
            if (WSDLConstants.QNAME_PART.equals(name)) {
                String part = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
                String desc = null;
                int index = reader.getAttributeCount();
                WSDLDescriptorKind kind = WSDLDescriptorKind.ELEMENT;
                for (int i2 = 0; i2 < index; i2++) {
                    QName descName = reader.getAttributeName(i2);
                    if (descName.getLocalPart().equals("element")) {
                        kind = WSDLDescriptorKind.ELEMENT;
                    } else if (descName.getLocalPart().equals("type")) {
                        kind = WSDLDescriptorKind.TYPE;
                    }
                    if (descName.getLocalPart().equals("element") || descName.getLocalPart().equals("type")) {
                        desc = reader.getAttributeValue(i2);
                        break;
                    }
                }
                if (desc != null) {
                    EditableWSDLPart wsdlPart = new WSDLPartImpl(reader, part, 0, new WSDLPartDescriptorImpl(reader, ParserUtil.getQName(reader, desc), kind));
                    msg.add(wsdlPart);
                }
                if (reader.getEventType() != 2) {
                    goToEnd(reader);
                }
            } else {
                this.extensionFacade.messageElements(msg, reader);
            }
        }
        this.wsdlDoc.addMessage(msg);
        if (reader.getEventType() != 2) {
            goToEnd(reader);
        }
    }

    private static void goToEnd(XMLStreamReader reader) {
        while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
            XMLStreamReaderUtil.skipElement(reader);
        }
    }

    private static XMLStreamReader createReader(URL wsdlLoc) throws XMLStreamException, IOException {
        return createReader(wsdlLoc, null);
    }

    private static XMLStreamReader createReader(URL wsdlLoc, Class<Service> serviceClass) throws XMLStreamException, IOException {
        WSDLLocator locator;
        InputStream stream;
        try {
            stream = wsdlLoc.openStream();
        } catch (IOException io) {
            if (serviceClass != null && (locator = (WSDLLocator) ContainerResolver.getInstance().getContainer().getSPI(WSDLLocator.class)) != null) {
                String exForm = wsdlLoc.toExternalForm();
                URL ru = serviceClass.getResource(".");
                String loc = wsdlLoc.getPath();
                if (ru != null) {
                    String ruExForm = ru.toExternalForm();
                    if (exForm.startsWith(ruExForm)) {
                        loc = exForm.substring(ruExForm.length());
                    }
                }
                wsdlLoc = locator.locateWSDL(serviceClass, loc);
                if (wsdlLoc != null) {
                    stream = new FilterInputStream(wsdlLoc.openStream()) { // from class: com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.1
                        boolean closed;

                        @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
                        public void close() throws IOException {
                            if (!this.closed) {
                                this.closed = true;
                                byte[] buf = new byte[8192];
                                while (read(buf) != -1) {
                                }
                                super.close();
                            }
                        }
                    };
                }
            }
            throw io;
        }
        return new TidyXMLStreamReader(XMLStreamReaderFactory.create(wsdlLoc.toExternalForm(), stream, false), stream);
    }

    private void register(WSDLParserExtension e2) {
        this.extensions.add(new FoolProofParserExtension(e2));
    }

    private static void readNSDecl(Map<String, String> ns_map, XMLStreamReader reader) {
        if (reader.getNamespaceCount() > 0) {
            for (int i2 = 0; i2 < reader.getNamespaceCount(); i2++) {
                ns_map.put(reader.getNamespacePrefix(i2), reader.getNamespaceURI(i2));
            }
        }
    }
}
