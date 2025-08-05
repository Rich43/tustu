package com.sun.xml.internal.ws.wsdl.writer;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.v2.schemagen.Util;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexType;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Element;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ExplicitGroup;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalElement;
import com.sun.xml.internal.txw2.TXW;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.output.ResultFactory;
import com.sun.xml.internal.txw2.output.TXWResult;
import com.sun.xml.internal.txw2.output.XmlSerializer;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.model.MEP;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.model.WrapperParameter;
import com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLGeneratorExtension;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.BindingHelper;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import com.sun.xml.internal.ws.util.RuntimeVersion;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.writer.document.Binding;
import com.sun.xml.internal.ws.wsdl.writer.document.BindingOperationType;
import com.sun.xml.internal.ws.wsdl.writer.document.Definitions;
import com.sun.xml.internal.ws.wsdl.writer.document.Fault;
import com.sun.xml.internal.ws.wsdl.writer.document.FaultType;
import com.sun.xml.internal.ws.wsdl.writer.document.Import;
import com.sun.xml.internal.ws.wsdl.writer.document.Message;
import com.sun.xml.internal.ws.wsdl.writer.document.Operation;
import com.sun.xml.internal.ws.wsdl.writer.document.ParamType;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import com.sun.xml.internal.ws.wsdl.writer.document.Port;
import com.sun.xml.internal.ws.wsdl.writer.document.PortType;
import com.sun.xml.internal.ws.wsdl.writer.document.Service;
import com.sun.xml.internal.ws.wsdl.writer.document.Types;
import com.sun.xml.internal.ws.wsdl.writer.document.soap.Body;
import com.sun.xml.internal.ws.wsdl.writer.document.soap.BodyType;
import com.sun.xml.internal.ws.wsdl.writer.document.soap.Header;
import com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPFault;
import com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPAddress;
import com.sun.xml.internal.ws.wsdl.writer.document.xsd.Schema;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/WSDLGenerator.class */
public class WSDLGenerator {
    private JAXWSOutputSchemaResolver resolver;
    private com.oracle.webservices.internal.api.databinding.WSDLResolver wsdlResolver;
    private AbstractSEIModelImpl model;
    private Definitions serviceDefinitions;
    private Definitions portDefinitions;
    private Types types;
    private static final String DOT_WSDL = ".wsdl";
    private static final String RESPONSE = "Response";
    private static final String PARAMETERS = "parameters";
    private static final String RESULT = "parameters";
    private static final String UNWRAPPABLE_RESULT = "result";
    private static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
    private static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    private static final String XSD_PREFIX = "xsd";
    private static final String SOAP11_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap/";
    private static final String SOAP12_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap12/";
    private static final String SOAP_PREFIX = "soap";
    private static final String SOAP12_PREFIX = "soap12";
    private static final String TNS_PREFIX = "tns";
    private static final String DOCUMENT = "document";
    private static final String RPC = "rpc";
    private static final String LITERAL = "literal";
    private static final String REPLACE_WITH_ACTUAL_URL = "REPLACE_WITH_ACTUAL_URL";
    private Set<QName> processedExceptions;
    private WSBinding binding;
    private String wsdlLocation;
    private String portWSDLID;
    private String schemaPrefix;
    private WSDLGeneratorExtension extension;
    List<WSDLGeneratorExtension> extensionHandlers;
    private String endpointAddress;
    private Container container;
    private final Class implType;
    private boolean inlineSchemas;
    private final boolean disableXmlSecurity;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WSDLGenerator.class.desiredAssertionStatus();
    }

    public WSDLGenerator(AbstractSEIModelImpl model, com.oracle.webservices.internal.api.databinding.WSDLResolver wsdlResolver, WSBinding binding, Container container, Class implType, boolean inlineSchemas, WSDLGeneratorExtension... extensions) {
        this(model, wsdlResolver, binding, container, implType, inlineSchemas, false, extensions);
    }

    public WSDLGenerator(AbstractSEIModelImpl model, com.oracle.webservices.internal.api.databinding.WSDLResolver wsdlResolver, WSBinding binding, Container container, Class implType, boolean inlineSchemas, boolean disableXmlSecurity, WSDLGeneratorExtension... extensions) {
        WSDLGeneratorExtension[] wsdlGeneratorExtensions;
        this.wsdlResolver = null;
        this.processedExceptions = new HashSet();
        this.endpointAddress = REPLACE_WITH_ACTUAL_URL;
        this.model = model;
        this.resolver = new JAXWSOutputSchemaResolver();
        this.wsdlResolver = wsdlResolver;
        this.binding = binding;
        this.container = container;
        this.implType = implType;
        this.extensionHandlers = new ArrayList();
        this.inlineSchemas = inlineSchemas;
        this.disableXmlSecurity = disableXmlSecurity;
        register(new W3CAddressingWSDLGeneratorExtension());
        register(new W3CAddressingMetadataWSDLGeneratorExtension());
        register(new PolicyWSDLGeneratorExtension());
        if (container != null && (wsdlGeneratorExtensions = (WSDLGeneratorExtension[]) container.getSPI(WSDLGeneratorExtension[].class)) != null) {
            for (WSDLGeneratorExtension wsdlGeneratorExtension : wsdlGeneratorExtensions) {
                register(wsdlGeneratorExtension);
            }
        }
        for (WSDLGeneratorExtension w2 : extensions) {
            register(w2);
        }
        this.extension = new WSDLGeneratorExtensionFacade((WSDLGeneratorExtension[]) this.extensionHandlers.toArray(new WSDLGeneratorExtension[0]));
    }

    public void setEndpointAddress(String address) {
        this.endpointAddress = address;
    }

    protected String mangleName(String name) {
        return BindingHelper.mangleNameToClassName(name);
    }

    /* JADX WARN: Type inference failed for: r1v23, types: [T, java.lang.String] */
    public void doGeneration() throws TransformerFactoryConfigurationError {
        XmlSerializer portWriter = null;
        String fileName = mangleName(this.model.getServiceQName().getLocalPart());
        Result result = this.wsdlResolver.getWSDL(fileName + DOT_WSDL);
        this.wsdlLocation = result.getSystemId();
        XmlSerializer serviceWriter = new CommentFilter(ResultFactory.createSerializer(result));
        if (this.model.getServiceQName().getNamespaceURI().equals(this.model.getTargetNamespace())) {
            portWriter = serviceWriter;
            this.schemaPrefix = fileName + "_";
        } else {
            String wsdlName = mangleName(this.model.getPortTypeName().getLocalPart());
            if (wsdlName.equals(fileName)) {
                wsdlName = wsdlName + MemberSubmissionAddressingConstants.WSA_PORTTYPE_NAME;
            }
            Holder<String> absWSDLName = new Holder<>();
            absWSDLName.value = wsdlName + DOT_WSDL;
            Result result2 = this.wsdlResolver.getAbstractWSDL(absWSDLName);
            if (result2 != null) {
                this.portWSDLID = result2.getSystemId();
                if (this.portWSDLID.equals(this.wsdlLocation)) {
                    portWriter = serviceWriter;
                } else {
                    portWriter = new CommentFilter(ResultFactory.createSerializer(result2));
                }
            } else {
                this.portWSDLID = absWSDLName.value;
            }
            this.schemaPrefix = new File(this.portWSDLID).getName();
            int idx = this.schemaPrefix.lastIndexOf(46);
            if (idx > 0) {
                this.schemaPrefix = this.schemaPrefix.substring(0, idx);
            }
            this.schemaPrefix = mangleName(this.schemaPrefix) + "_";
        }
        generateDocument(serviceWriter, portWriter);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/WSDLGenerator$CommentFilter.class */
    private static class CommentFilter implements XmlSerializer {
        final XmlSerializer serializer;
        private static final String VERSION_COMMENT = " Generated by JAX-WS RI (http://jax-ws.java.net). RI's version is " + ((Object) RuntimeVersion.VERSION) + ". ";

        CommentFilter(XmlSerializer serializer) {
            this.serializer = serializer;
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void startDocument() {
            this.serializer.startDocument();
            comment(new StringBuilder(VERSION_COMMENT));
            text(new StringBuilder("\n"));
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void beginStartTag(String uri, String localName, String prefix) {
            this.serializer.beginStartTag(uri, localName, prefix);
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
            this.serializer.writeAttribute(uri, localName, prefix, value);
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void writeXmlns(String prefix, String uri) {
            this.serializer.writeXmlns(prefix, uri);
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void endStartTag(String uri, String localName, String prefix) {
            this.serializer.endStartTag(uri, localName, prefix);
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void endTag() {
            this.serializer.endTag();
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void text(StringBuilder text) {
            this.serializer.text(text);
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void cdata(StringBuilder text) {
            this.serializer.cdata(text);
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void comment(StringBuilder comment) {
            this.serializer.comment(comment);
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void endDocument() {
            this.serializer.endDocument();
        }

        @Override // com.sun.xml.internal.txw2.output.XmlSerializer
        public void flush() {
            this.serializer.flush();
        }
    }

    private void generateDocument(XmlSerializer serviceStream, XmlSerializer portStream) throws TransformerFactoryConfigurationError {
        this.serviceDefinitions = (Definitions) TXW.create(Definitions.class, serviceStream);
        this.serviceDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/", "");
        this.serviceDefinitions._namespace("http://www.w3.org/2001/XMLSchema", XSD_PREFIX);
        this.serviceDefinitions.targetNamespace(this.model.getServiceQName().getNamespaceURI());
        this.serviceDefinitions._namespace(this.model.getServiceQName().getNamespaceURI(), TNS_PREFIX);
        if (this.binding.getSOAPVersion() == SOAPVersion.SOAP_12) {
            this.serviceDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/soap12/", SOAP12_PREFIX);
        } else {
            this.serviceDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/soap/", SOAP_PREFIX);
        }
        this.serviceDefinitions.name(this.model.getServiceQName().getLocalPart());
        WSDLGenExtnContext serviceCtx = new WSDLGenExtnContext(this.serviceDefinitions, this.model, this.binding, this.container, this.implType);
        this.extension.start(serviceCtx);
        if (serviceStream != portStream && portStream != null) {
            this.portDefinitions = (Definitions) TXW.create(Definitions.class, portStream);
            this.portDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/", "");
            this.portDefinitions._namespace("http://www.w3.org/2001/XMLSchema", XSD_PREFIX);
            if (this.model.getTargetNamespace() != null) {
                this.portDefinitions.targetNamespace(this.model.getTargetNamespace());
                this.portDefinitions._namespace(this.model.getTargetNamespace(), TNS_PREFIX);
            }
            String schemaLoc = relativize(this.portWSDLID, this.wsdlLocation);
            Import _import = this.serviceDefinitions._import().namespace(this.model.getTargetNamespace());
            _import.location(schemaLoc);
        } else if (portStream != null) {
            this.portDefinitions = this.serviceDefinitions;
        } else {
            String schemaLoc2 = relativize(this.portWSDLID, this.wsdlLocation);
            Import _import2 = this.serviceDefinitions._import().namespace(this.model.getTargetNamespace());
            _import2.location(schemaLoc2);
        }
        this.extension.addDefinitionsExtension(this.serviceDefinitions);
        if (this.portDefinitions != null) {
            generateTypes();
            generateMessages();
            generatePortType();
        }
        generateBinding();
        generateService();
        this.extension.end(serviceCtx);
        this.serviceDefinitions.commit();
        if (this.portDefinitions != null && this.portDefinitions != this.serviceDefinitions) {
            this.portDefinitions.commit();
        }
    }

    protected void generateTypes() throws TransformerFactoryConfigurationError {
        this.types = this.portDefinitions.types();
        if (this.model.getBindingContext() != null) {
            if (this.inlineSchemas && this.model.getBindingContext().getClass().getName().indexOf("glassfish") == -1) {
                this.resolver.nonGlassfishSchemas = new ArrayList<>();
            }
            try {
                this.model.getBindingContext().generateSchema(this.resolver);
            } catch (IOException e2) {
                throw new WebServiceException(e2.getMessage());
            }
        }
        if (this.resolver.nonGlassfishSchemas != null) {
            TransformerFactory tf = XmlUtil.newTransformerFactory(!this.disableXmlSecurity);
            try {
                Transformer t2 = tf.newTransformer();
                Iterator<DOMResult> it = this.resolver.nonGlassfishSchemas.iterator();
                while (it.hasNext()) {
                    DOMResult xsd = it.next();
                    Document doc = (Document) xsd.getNode();
                    SAXResult sax = new SAXResult(new TXWContentHandler(this.types));
                    t2.transform(new DOMSource(doc.getDocumentElement()), sax);
                }
            } catch (TransformerConfigurationException e3) {
                throw new WebServiceException(e3.getMessage(), e3);
            } catch (TransformerException e4) {
                throw new WebServiceException(e4.getMessage(), e4);
            }
        }
        generateWrappers();
    }

    void generateWrappers() {
        List<WrapperParameter> wrappers = new ArrayList<>();
        for (JavaMethodImpl method : this.model.getJavaMethods()) {
            if (!method.getBinding().isRpcLit()) {
                for (ParameterImpl p2 : method.getRequestParameters()) {
                    if ((p2 instanceof WrapperParameter) && WrapperComposite.class.equals(((WrapperParameter) p2).getTypeInfo().type)) {
                        wrappers.add((WrapperParameter) p2);
                    }
                }
                for (ParameterImpl p3 : method.getResponseParameters()) {
                    if ((p3 instanceof WrapperParameter) && WrapperComposite.class.equals(((WrapperParameter) p3).getTypeInfo().type)) {
                        wrappers.add((WrapperParameter) p3);
                    }
                }
            }
        }
        if (wrappers.isEmpty()) {
            return;
        }
        HashMap<String, Schema> xsds = new HashMap<>();
        for (WrapperParameter wp : wrappers) {
            String tns = wp.getName().getNamespaceURI();
            Schema xsd = xsds.get(tns);
            if (xsd == null) {
                xsd = this.types.schema();
                xsd.targetNamespace(tns);
                xsds.put(tns, xsd);
            }
            Element e2 = (Element) xsd._element(Element.class);
            e2._attribute("name", wp.getName().getLocalPart());
            e2.type(wp.getName());
            ComplexType ct = (ComplexType) xsd._element(ComplexType.class);
            ct._attribute("name", wp.getName().getLocalPart());
            ExplicitGroup sq = ct.sequence();
            for (ParameterImpl p4 : wp.getWrapperChildren()) {
                if (p4.getBinding().isBody()) {
                    LocalElement le = sq.element();
                    le._attribute("name", p4.getName().getLocalPart());
                    TypeInfo typeInfo = p4.getItemType();
                    boolean repeatedElement = false;
                    if (typeInfo == null) {
                        typeInfo = p4.getTypeInfo();
                    } else {
                        repeatedElement = true;
                    }
                    QName type = this.model.getBindingContext().getTypeName(typeInfo);
                    le.type(type);
                    if (repeatedElement) {
                        le.minOccurs(0);
                        le.maxOccurs(SchemaSymbols.ATTVAL_UNBOUNDED);
                    }
                }
            }
        }
    }

    protected void generateMessages() {
        for (JavaMethodImpl method : this.model.getJavaMethods()) {
            generateSOAPMessages(method, method.getBinding());
        }
    }

    protected void generateSOAPMessages(JavaMethodImpl method, SOAPBinding binding) {
        boolean isDoclit = binding.isDocLit();
        Message message = this.portDefinitions.message().name(method.getRequestMessageName());
        this.extension.addInputMessageExtension(message, method);
        BindingContext jaxbContext = this.model.getBindingContext();
        for (ParameterImpl param : method.getRequestParameters()) {
            if (isDoclit) {
                if (isHeaderParameter(param)) {
                }
                Part part = message.part().name(param.getPartName());
                part.element(param.getName());
            } else if (param.isWrapperStyle()) {
                for (ParameterImpl childParam : ((WrapperParameter) param).getWrapperChildren()) {
                    Part part2 = message.part().name(childParam.getPartName());
                    part2.type(jaxbContext.getTypeName(childParam.getXMLBridge().getTypeInfo()));
                }
            } else {
                Part part3 = message.part().name(param.getPartName());
                part3.element(param.getName());
            }
        }
        if (method.getMEP() != MEP.ONE_WAY) {
            Message message2 = this.portDefinitions.message().name(method.getResponseMessageName());
            this.extension.addOutputMessageExtension(message2, method);
            for (ParameterImpl param2 : method.getResponseParameters()) {
                if (isDoclit) {
                    Part part4 = message2.part().name(param2.getPartName());
                    part4.element(param2.getName());
                } else if (param2.isWrapperStyle()) {
                    for (ParameterImpl childParam2 : ((WrapperParameter) param2).getWrapperChildren()) {
                        Part part5 = message2.part().name(childParam2.getPartName());
                        part5.type(jaxbContext.getTypeName(childParam2.getXMLBridge().getTypeInfo()));
                    }
                } else {
                    Part part6 = message2.part().name(param2.getPartName());
                    part6.element(param2.getName());
                }
            }
        }
        for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
            QName tagName = exception.getDetailType().tagName;
            String messageName = exception.getMessageName();
            QName messageQName = new QName(this.model.getTargetNamespace(), messageName);
            if (!this.processedExceptions.contains(messageQName)) {
                Message message3 = this.portDefinitions.message().name(messageName);
                this.extension.addFaultMessageExtension(message3, method, exception);
                Part part7 = message3.part().name("fault");
                part7.element(tagName);
                this.processedExceptions.add(messageQName);
            }
        }
    }

    protected void generatePortType() {
        PortType portType = this.portDefinitions.portType().name(this.model.getPortTypeName().getLocalPart());
        this.extension.addPortTypeExtension(portType);
        for (JavaMethodImpl method : this.model.getJavaMethods()) {
            Operation operation = portType.operation().name(method.getOperationName());
            generateParameterOrder(operation, method);
            this.extension.addOperationExtension(operation, method);
            switch (method.getMEP()) {
                case REQUEST_RESPONSE:
                    generateInputMessage(operation, method);
                    generateOutputMessage(operation, method);
                    break;
                case ONE_WAY:
                    generateInputMessage(operation, method);
                    break;
            }
            for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
                QName messageName = new QName(this.model.getTargetNamespace(), exception.getMessageName());
                FaultType paramType = operation.fault().message(messageName).name(exception.getMessageName());
                this.extension.addOperationFaultExtension(paramType, method, exception);
            }
        }
    }

    protected boolean isWrapperStyle(JavaMethodImpl method) {
        if (method.getRequestParameters().size() > 0) {
            ParameterImpl param = method.getRequestParameters().iterator().next();
            return param.isWrapperStyle();
        }
        return false;
    }

    protected boolean isRpcLit(JavaMethodImpl method) {
        return method.getBinding().getStyle() == SOAPBinding.Style.RPC;
    }

    protected void generateParameterOrder(Operation operation, JavaMethodImpl method) {
        if (method.getMEP() == MEP.ONE_WAY) {
            return;
        }
        if (isRpcLit(method)) {
            generateRpcParameterOrder(operation, method);
        } else {
            generateDocumentParameterOrder(operation, method);
        }
    }

    protected void generateRpcParameterOrder(Operation operation, JavaMethodImpl method) {
        StringBuilder paramOrder = new StringBuilder();
        Set<String> partNames = new HashSet<>();
        List<ParameterImpl> sortedParams = sortMethodParameters(method);
        int i2 = 0;
        for (ParameterImpl parameter : sortedParams) {
            if (parameter.getIndex() >= 0) {
                String partName = parameter.getPartName();
                if (!partNames.contains(partName)) {
                    int i3 = i2;
                    i2++;
                    if (i3 > 0) {
                        paramOrder.append(' ');
                    }
                    paramOrder.append(partName);
                    partNames.add(partName);
                }
            }
        }
        if (i2 > 1) {
            operation.parameterOrder(paramOrder.toString());
        }
    }

    protected void generateDocumentParameterOrder(Operation operation, JavaMethodImpl method) {
        StringBuilder paramOrder = new StringBuilder();
        Set<String> partNames = new HashSet<>();
        List<ParameterImpl> sortedParams = sortMethodParameters(method);
        int i2 = 0;
        for (ParameterImpl parameter : sortedParams) {
            if (parameter.getIndex() >= 0) {
                String partName = parameter.getPartName();
                if (!partNames.contains(partName)) {
                    int i3 = i2;
                    i2++;
                    if (i3 > 0) {
                        paramOrder.append(' ');
                    }
                    paramOrder.append(partName);
                    partNames.add(partName);
                }
            }
        }
        if (i2 > 1) {
            operation.parameterOrder(paramOrder.toString());
        }
    }

    protected List<ParameterImpl> sortMethodParameters(JavaMethodImpl method) {
        int pos;
        Set<ParameterImpl> paramSet = new HashSet<>();
        List<ParameterImpl> sortedParams = new ArrayList<>();
        if (isRpcLit(method)) {
            for (ParameterImpl param : method.getRequestParameters()) {
                if (param instanceof WrapperParameter) {
                    paramSet.addAll(((WrapperParameter) param).getWrapperChildren());
                } else {
                    paramSet.add(param);
                }
            }
            for (ParameterImpl param2 : method.getResponseParameters()) {
                if (param2 instanceof WrapperParameter) {
                    paramSet.addAll(((WrapperParameter) param2).getWrapperChildren());
                } else {
                    paramSet.add(param2);
                }
            }
        } else {
            paramSet.addAll(method.getRequestParameters());
            paramSet.addAll(method.getResponseParameters());
        }
        Iterator<ParameterImpl> params = paramSet.iterator();
        if (paramSet.isEmpty()) {
            return sortedParams;
        }
        sortedParams.add(params.next());
        for (int i2 = 1; i2 < paramSet.size(); i2++) {
            ParameterImpl param3 = params.next();
            while (pos < i2) {
                ParameterImpl sortedParam = sortedParams.get(pos);
                pos = (!(param3.getIndex() == sortedParam.getIndex() && (param3 instanceof WrapperParameter)) && param3.getIndex() >= sortedParam.getIndex()) ? pos + 1 : 0;
            }
            sortedParams.add(pos, param3);
        }
        return sortedParams;
    }

    protected boolean isBodyParameter(ParameterImpl parameter) {
        ParameterBinding paramBinding = parameter.getBinding();
        return paramBinding.isBody();
    }

    protected boolean isHeaderParameter(ParameterImpl parameter) {
        ParameterBinding paramBinding = parameter.getBinding();
        return paramBinding.isHeader();
    }

    protected boolean isAttachmentParameter(ParameterImpl parameter) {
        ParameterBinding paramBinding = parameter.getBinding();
        return paramBinding.isAttachment();
    }

    protected void generateBinding() {
        Binding newBinding = this.serviceDefinitions.binding().name(this.model.getBoundPortTypeName().getLocalPart());
        this.extension.addBindingExtension(newBinding);
        newBinding.type(this.model.getPortTypeName());
        boolean first = true;
        for (JavaMethodImpl method : this.model.getJavaMethods()) {
            if (first) {
                com.sun.xml.internal.ws.api.model.soap.SOAPBinding sBinding = method.getBinding();
                SOAPVersion soapVersion = sBinding.getSOAPVersion();
                if (soapVersion == SOAPVersion.SOAP_12) {
                    com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPBinding soapBinding = newBinding.soap12Binding();
                    soapBinding.transport(this.binding.getBindingId().getTransport());
                    if (sBinding.getStyle().equals(SOAPBinding.Style.DOCUMENT)) {
                        soapBinding.style("document");
                    } else {
                        soapBinding.style("rpc");
                    }
                } else {
                    com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPBinding soapBinding2 = newBinding.soapBinding();
                    soapBinding2.transport(this.binding.getBindingId().getTransport());
                    if (sBinding.getStyle().equals(SOAPBinding.Style.DOCUMENT)) {
                        soapBinding2.style("document");
                    } else {
                        soapBinding2.style("rpc");
                    }
                }
                first = false;
            }
            if (this.binding.getBindingId().getSOAPVersion() == SOAPVersion.SOAP_12) {
                generateSOAP12BindingOperation(method, newBinding);
            } else {
                generateBindingOperation(method, newBinding);
            }
        }
    }

    protected void generateBindingOperation(JavaMethodImpl method, Binding binding) {
        BindingOperationType operation = binding.operation().name(method.getOperationName());
        this.extension.addBindingOperationExtension(operation, method);
        String targetNamespace = this.model.getTargetNamespace();
        QName requestMessage = new QName(targetNamespace, method.getOperationName());
        List<ParameterImpl> bodyParams = new ArrayList<>();
        List<ParameterImpl> headerParams = new ArrayList<>();
        splitParameters(bodyParams, headerParams, method.getRequestParameters());
        com.sun.xml.internal.ws.api.model.soap.SOAPBinding soapBinding = method.getBinding();
        operation.soapOperation().soapAction(soapBinding.getSOAPAction());
        TypedXmlWriter input = operation.input();
        this.extension.addBindingOperationInputExtension(input, method);
        BodyType body = (BodyType) input._element(Body.class);
        boolean isRpc = soapBinding.getStyle().equals(SOAPBinding.Style.RPC);
        if (soapBinding.getUse() == SOAPBinding.Use.LITERAL) {
            body.use(LITERAL);
            if (headerParams.size() > 0) {
                if (bodyParams.size() > 0) {
                    ParameterImpl param = bodyParams.iterator().next();
                    if (isRpc) {
                        StringBuilder parts = new StringBuilder();
                        int i2 = 0;
                        for (ParameterImpl parameter : ((WrapperParameter) param).getWrapperChildren()) {
                            int i3 = i2;
                            i2++;
                            if (i3 > 0) {
                                parts.append(' ');
                            }
                            parts.append(parameter.getPartName());
                        }
                        body.parts(parts.toString());
                    } else {
                        body.parts(param.getPartName());
                    }
                } else {
                    body.parts("");
                }
                generateSOAPHeaders(input, headerParams, requestMessage);
            }
            if (isRpc) {
                body.namespace(method.getRequestParameters().iterator().next().getName().getNamespaceURI());
            }
            if (method.getMEP() != MEP.ONE_WAY) {
                bodyParams.clear();
                headerParams.clear();
                splitParameters(bodyParams, headerParams, method.getResponseParameters());
                TypedXmlWriter output = operation.output();
                this.extension.addBindingOperationOutputExtension(output, method);
                BodyType body2 = (BodyType) output._element(Body.class);
                body2.use(LITERAL);
                if (headerParams.size() > 0) {
                    StringBuilder parts2 = new StringBuilder();
                    if (bodyParams.size() > 0) {
                        ParameterImpl param2 = bodyParams.iterator().hasNext() ? bodyParams.iterator().next() : null;
                        if (param2 != null) {
                            if (isRpc) {
                                int i4 = 0;
                                for (ParameterImpl parameter2 : ((WrapperParameter) param2).getWrapperChildren()) {
                                    int i5 = i4;
                                    i4++;
                                    if (i5 > 0) {
                                        parts2.append(" ");
                                    }
                                    parts2.append(parameter2.getPartName());
                                }
                            } else {
                                parts2 = new StringBuilder(param2.getPartName());
                            }
                        }
                    }
                    body2.parts(parts2.toString());
                    QName responseMessage = new QName(targetNamespace, method.getResponseMessageName());
                    generateSOAPHeaders(output, headerParams, responseMessage);
                }
                if (isRpc) {
                    body2.namespace(method.getRequestParameters().iterator().next().getName().getNamespaceURI());
                }
            }
            for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
                Fault fault = operation.fault().name(exception.getMessageName());
                this.extension.addBindingOperationFaultExtension(fault, method, exception);
                SOAPFault soapFault = ((SOAPFault) fault._element(SOAPFault.class)).name(exception.getMessageName());
                soapFault.use(LITERAL);
            }
            return;
        }
        throw new WebServiceException("encoded use is not supported");
    }

    protected void generateSOAP12BindingOperation(JavaMethodImpl method, Binding binding) {
        BindingOperationType operation = binding.operation().name(method.getOperationName());
        this.extension.addBindingOperationExtension(operation, method);
        String targetNamespace = this.model.getTargetNamespace();
        QName requestMessage = new QName(targetNamespace, method.getOperationName());
        ArrayList<ParameterImpl> bodyParams = new ArrayList<>();
        ArrayList<ParameterImpl> headerParams = new ArrayList<>();
        splitParameters(bodyParams, headerParams, method.getRequestParameters());
        com.sun.xml.internal.ws.api.model.soap.SOAPBinding soapBinding = method.getBinding();
        String soapAction = soapBinding.getSOAPAction();
        if (soapAction != null) {
            operation.soap12Operation().soapAction(soapAction);
        }
        TypedXmlWriter input = operation.input();
        this.extension.addBindingOperationInputExtension(input, method);
        com.sun.xml.internal.ws.wsdl.writer.document.soap12.BodyType body = (com.sun.xml.internal.ws.wsdl.writer.document.soap12.BodyType) input._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.Body.class);
        boolean isRpc = soapBinding.getStyle().equals(SOAPBinding.Style.RPC);
        if (soapBinding.getUse().equals(SOAPBinding.Use.LITERAL)) {
            body.use(LITERAL);
            if (headerParams.size() > 0) {
                if (bodyParams.size() > 0) {
                    ParameterImpl param = bodyParams.iterator().next();
                    if (isRpc) {
                        StringBuilder parts = new StringBuilder();
                        int i2 = 0;
                        for (ParameterImpl parameter : ((WrapperParameter) param).getWrapperChildren()) {
                            int i3 = i2;
                            i2++;
                            if (i3 > 0) {
                                parts.append(' ');
                            }
                            parts.append(parameter.getPartName());
                        }
                        body.parts(parts.toString());
                    } else {
                        body.parts(param.getPartName());
                    }
                } else {
                    body.parts("");
                }
                generateSOAP12Headers(input, headerParams, requestMessage);
            }
            if (isRpc) {
                body.namespace(method.getRequestParameters().iterator().next().getName().getNamespaceURI());
            }
            if (method.getMEP() != MEP.ONE_WAY) {
                bodyParams.clear();
                headerParams.clear();
                splitParameters(bodyParams, headerParams, method.getResponseParameters());
                TypedXmlWriter output = operation.output();
                this.extension.addBindingOperationOutputExtension(output, method);
                com.sun.xml.internal.ws.wsdl.writer.document.soap12.BodyType body2 = (com.sun.xml.internal.ws.wsdl.writer.document.soap12.BodyType) output._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.Body.class);
                body2.use(LITERAL);
                if (headerParams.size() > 0) {
                    if (bodyParams.size() > 0) {
                        ParameterImpl param2 = bodyParams.iterator().next();
                        if (isRpc) {
                            StringBuilder parts2 = new StringBuilder();
                            int i4 = 0;
                            for (ParameterImpl parameter2 : ((WrapperParameter) param2).getWrapperChildren()) {
                                int i5 = i4;
                                i4++;
                                if (i5 > 0) {
                                    parts2.append(" ");
                                }
                                parts2.append(parameter2.getPartName());
                            }
                            body2.parts(parts2.toString());
                        } else {
                            body2.parts(param2.getPartName());
                        }
                    } else {
                        body2.parts("");
                    }
                    QName responseMessage = new QName(targetNamespace, method.getResponseMessageName());
                    generateSOAP12Headers(output, headerParams, responseMessage);
                }
                if (isRpc) {
                    body2.namespace(method.getRequestParameters().iterator().next().getName().getNamespaceURI());
                }
            }
            for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
                Fault fault = operation.fault().name(exception.getMessageName());
                this.extension.addBindingOperationFaultExtension(fault, method, exception);
                com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPFault soapFault = ((com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPFault) fault._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPFault.class)).name(exception.getMessageName());
                soapFault.use(LITERAL);
            }
            return;
        }
        throw new WebServiceException("encoded use is not supported");
    }

    protected void splitParameters(List<ParameterImpl> bodyParams, List<ParameterImpl> headerParams, List<ParameterImpl> params) {
        for (ParameterImpl parameter : params) {
            if (isBodyParameter(parameter)) {
                bodyParams.add(parameter);
            } else {
                headerParams.add(parameter);
            }
        }
    }

    protected void generateSOAPHeaders(TypedXmlWriter writer, List<ParameterImpl> parameters, QName message) {
        for (ParameterImpl headerParam : parameters) {
            Header header = (Header) writer._element(Header.class);
            header.message(message);
            header.part(headerParam.getPartName());
            header.use(LITERAL);
        }
    }

    protected void generateSOAP12Headers(TypedXmlWriter writer, List<ParameterImpl> parameters, QName message) {
        for (ParameterImpl headerParam : parameters) {
            com.sun.xml.internal.ws.wsdl.writer.document.soap12.Header header = (com.sun.xml.internal.ws.wsdl.writer.document.soap12.Header) writer._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.Header.class);
            header.message(message);
            header.part(headerParam.getPartName());
            header.use(LITERAL);
        }
    }

    protected void generateService() {
        QName portQName = this.model.getPortName();
        QName serviceQName = this.model.getServiceQName();
        Service service = this.serviceDefinitions.service().name(serviceQName.getLocalPart());
        this.extension.addServiceExtension(service);
        Port port = service.port().name(portQName.getLocalPart());
        port.binding(this.model.getBoundPortTypeName());
        this.extension.addPortExtension(port);
        if (this.model.getJavaMethods().isEmpty()) {
            return;
        }
        if (this.binding.getBindingId().getSOAPVersion() == SOAPVersion.SOAP_12) {
            SOAPAddress address = (SOAPAddress) port._element(SOAPAddress.class);
            address.location(this.endpointAddress);
        } else {
            com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPAddress address2 = (com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPAddress) port._element(com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPAddress.class);
            address2.location(this.endpointAddress);
        }
    }

    protected void generateInputMessage(Operation operation, JavaMethodImpl method) {
        ParamType paramType = operation.input();
        this.extension.addOperationInputExtension(paramType, method);
        paramType.message(new QName(this.model.getTargetNamespace(), method.getRequestMessageName()));
    }

    protected void generateOutputMessage(Operation operation, JavaMethodImpl method) {
        ParamType paramType = operation.output();
        this.extension.addOperationOutputExtension(paramType, method);
        paramType.message(new QName(this.model.getTargetNamespace(), method.getResponseMessageName()));
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [T, java.lang.String] */
    public Result createOutputFile(String namespaceUri, String suggestedFileName) throws IOException {
        String schemaLoc;
        if (namespaceUri == null) {
            return null;
        }
        Holder<String> fileNameHolder = new Holder<>();
        fileNameHolder.value = this.schemaPrefix + suggestedFileName;
        Result result = this.wsdlResolver.getSchemaOutput(namespaceUri, fileNameHolder);
        if (result == null) {
            schemaLoc = fileNameHolder.value;
        } else {
            schemaLoc = relativize(result.getSystemId(), this.wsdlLocation);
        }
        boolean isEmptyNs = namespaceUri.trim().equals("");
        if (!isEmptyNs) {
            com.sun.xml.internal.ws.wsdl.writer.document.xsd.Import _import = this.types.schema()._import();
            _import.namespace(namespaceUri);
            _import.schemaLocation(schemaLoc);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Result createInlineSchema(String namespaceUri, String suggestedFileName) throws IOException {
        if (namespaceUri.equals("")) {
            return null;
        }
        Result result = new TXWResult(this.types);
        result.setSystemId("");
        return result;
    }

    protected static String relativize(String uri, String baseUri) {
        try {
            if (!$assertionsDisabled && uri == null) {
                throw new AssertionError();
            }
            if (baseUri == null) {
                return uri;
            }
            URI theUri = new URI(Util.escapeURI(uri));
            URI theBaseUri = new URI(Util.escapeURI(baseUri));
            if (theUri.isOpaque() || theBaseUri.isOpaque()) {
                return uri;
            }
            if (!Util.equalsIgnoreCase(theUri.getScheme(), theBaseUri.getScheme()) || !Util.equal(theUri.getAuthority(), theBaseUri.getAuthority())) {
                return uri;
            }
            String uriPath = theUri.getPath();
            String basePath = theBaseUri.getPath();
            if (!basePath.endsWith("/")) {
                basePath = Util.normalizeUriPath(basePath);
            }
            if (uriPath.equals(basePath)) {
                return ".";
            }
            String relPath = calculateRelativePath(uriPath, basePath);
            if (relPath == null) {
                return uri;
            }
            StringBuilder relUri = new StringBuilder();
            relUri.append(relPath);
            if (theUri.getQuery() != null) {
                relUri.append('?').append(theUri.getQuery());
            }
            if (theUri.getFragment() != null) {
                relUri.append('#').append(theUri.getFragment());
            }
            return relUri.toString();
        } catch (URISyntaxException e2) {
            throw new InternalError("Error escaping one of these uris:\n\t" + uri + "\n\t" + baseUri);
        }
    }

    private static String calculateRelativePath(String uri, String base) {
        if (base == null) {
            return null;
        }
        if (uri.startsWith(base)) {
            return uri.substring(base.length());
        }
        return "../" + calculateRelativePath(uri, Util.getParentUriPath(base));
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/WSDLGenerator$JAXWSOutputSchemaResolver.class */
    protected class JAXWSOutputSchemaResolver extends SchemaOutputResolver {
        ArrayList<DOMResult> nonGlassfishSchemas = null;

        protected JAXWSOutputSchemaResolver() {
        }

        @Override // javax.xml.bind.SchemaOutputResolver
        public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
            if (!WSDLGenerator.this.inlineSchemas) {
                return WSDLGenerator.this.createOutputFile(namespaceUri, suggestedFileName);
            }
            if (this.nonGlassfishSchemas == null) {
                return WSDLGenerator.this.createInlineSchema(namespaceUri, suggestedFileName);
            }
            return nonGlassfishSchemaResult(namespaceUri, suggestedFileName);
        }

        private Result nonGlassfishSchemaResult(String namespaceUri, String suggestedFileName) throws IOException {
            DOMResult result = new DOMResult();
            result.setSystemId("");
            this.nonGlassfishSchemas.add(result);
            return result;
        }
    }

    private void register(WSDLGeneratorExtension h2) {
        this.extensionHandlers.add(h2);
    }
}
