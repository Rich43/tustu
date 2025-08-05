package com.sun.xml.internal.ws.handler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import com.sun.xml.internal.ws.util.HandlerAnnotationInfo;
import com.sun.xml.internal.ws.util.JAXWSUtils;
import com.sun.xml.internal.ws.util.UtilException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.PortInfo;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerChainsModel.class */
public class HandlerChainsModel {
    private Class annotatedClass;
    private List<HandlerChainType> handlerChains;
    private String id;
    public static final String PROTOCOL_SOAP11_TOKEN = "##SOAP11_HTTP";
    public static final String PROTOCOL_SOAP12_TOKEN = "##SOAP12_HTTP";
    public static final String PROTOCOL_XML_TOKEN = "##XML_HTTP";
    private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.util");
    public static final String NS_109 = "http://java.sun.com/xml/ns/javaee";
    public static final QName QNAME_CHAIN_PORT_PATTERN = new QName(NS_109, "port-name-pattern");
    public static final QName QNAME_CHAIN_PROTOCOL_BINDING = new QName(NS_109, "protocol-bindings");
    public static final QName QNAME_CHAIN_SERVICE_PATTERN = new QName(NS_109, "service-name-pattern");
    public static final QName QNAME_HANDLER_CHAIN = new QName(NS_109, "handler-chain");
    public static final QName QNAME_HANDLER_CHAINS = new QName(NS_109, "handler-chains");
    public static final QName QNAME_HANDLER = new QName(NS_109, Constants.TRANSLET_OUTPUT_PNAME);
    public static final QName QNAME_HANDLER_NAME = new QName(NS_109, "handler-name");
    public static final QName QNAME_HANDLER_CLASS = new QName(NS_109, "handler-class");
    public static final QName QNAME_HANDLER_PARAM = new QName(NS_109, "init-param");
    public static final QName QNAME_HANDLER_PARAM_NAME = new QName(NS_109, "param-name");
    public static final QName QNAME_HANDLER_PARAM_VALUE = new QName(NS_109, "param-value");
    public static final QName QNAME_HANDLER_HEADER = new QName(NS_109, "soap-header");
    public static final QName QNAME_HANDLER_ROLE = new QName(NS_109, "soap-role");

    private HandlerChainsModel(Class annotatedClass) {
        this.annotatedClass = annotatedClass;
    }

    private List<HandlerChainType> getHandlerChain() {
        if (this.handlerChains == null) {
            this.handlerChains = new ArrayList();
        }
        return this.handlerChains;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public static HandlerChainsModel parseHandlerConfigFile(Class annotatedClass, XMLStreamReader reader) {
        ensureProperName(reader, QNAME_HANDLER_CHAINS);
        HandlerChainsModel handlerModel = new HandlerChainsModel(annotatedClass);
        List<HandlerChainType> hChains = handlerModel.getHandlerChain();
        XMLStreamReaderUtil.nextElementContent(reader);
        while (reader.getName().equals(QNAME_HANDLER_CHAIN)) {
            HandlerChainType hChain = new HandlerChainType();
            XMLStreamReaderUtil.nextElementContent(reader);
            if (reader.getName().equals(QNAME_CHAIN_PORT_PATTERN)) {
                QName portNamePattern = XMLStreamReaderUtil.getElementQName(reader);
                hChain.setPortNamePattern(portNamePattern);
                XMLStreamReaderUtil.nextElementContent(reader);
            } else if (reader.getName().equals(QNAME_CHAIN_PROTOCOL_BINDING)) {
                String bindingList = XMLStreamReaderUtil.getElementText(reader);
                StringTokenizer stk = new StringTokenizer(bindingList);
                while (stk.hasMoreTokens()) {
                    String token = stk.nextToken();
                    hChain.addProtocolBinding(token);
                }
                XMLStreamReaderUtil.nextElementContent(reader);
            } else if (reader.getName().equals(QNAME_CHAIN_SERVICE_PATTERN)) {
                QName serviceNamepattern = XMLStreamReaderUtil.getElementQName(reader);
                hChain.setServiceNamePattern(serviceNamepattern);
                XMLStreamReaderUtil.nextElementContent(reader);
            }
            List<HandlerType> handlers = hChain.getHandlers();
            while (reader.getName().equals(QNAME_HANDLER)) {
                HandlerType handler = new HandlerType();
                XMLStreamReaderUtil.nextContent(reader);
                if (reader.getName().equals(QNAME_HANDLER_NAME)) {
                    String handlerName = XMLStreamReaderUtil.getElementText(reader).trim();
                    handler.setHandlerName(handlerName);
                    XMLStreamReaderUtil.nextContent(reader);
                }
                ensureProperName(reader, QNAME_HANDLER_CLASS);
                String handlerClass = XMLStreamReaderUtil.getElementText(reader).trim();
                handler.setHandlerClass(handlerClass);
                XMLStreamReaderUtil.nextContent(reader);
                while (reader.getName().equals(QNAME_HANDLER_PARAM)) {
                    skipInitParamElement(reader);
                }
                while (reader.getName().equals(QNAME_HANDLER_HEADER)) {
                    skipTextElement(reader);
                }
                while (reader.getName().equals(QNAME_HANDLER_ROLE)) {
                    List<String> soapRoles = handler.getSoapRoles();
                    soapRoles.add(XMLStreamReaderUtil.getElementText(reader));
                    XMLStreamReaderUtil.nextContent(reader);
                }
                handlers.add(handler);
                ensureProperName(reader, QNAME_HANDLER);
                XMLStreamReaderUtil.nextContent(reader);
            }
            ensureProperName(reader, QNAME_HANDLER_CHAIN);
            hChains.add(hChain);
            XMLStreamReaderUtil.nextContent(reader);
        }
        return handlerModel;
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x021c, code lost:
    
        r0.invoke(r0, new java.lang.Object[0]);
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x014e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.sun.xml.internal.ws.util.HandlerAnnotationInfo parseHandlerFile(javax.xml.stream.XMLStreamReader r4, java.lang.ClassLoader r5, javax.xml.namespace.QName r6, javax.xml.namespace.QName r7, com.sun.xml.internal.ws.api.WSBinding r8) {
        /*
            Method dump skipped, instructions count: 630
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.handler.HandlerChainsModel.parseHandlerFile(javax.xml.stream.XMLStreamReader, java.lang.ClassLoader, javax.xml.namespace.QName, javax.xml.namespace.QName, com.sun.xml.internal.ws.api.WSBinding):com.sun.xml.internal.ws.util.HandlerAnnotationInfo");
    }

    public HandlerAnnotationInfo getHandlersForPortInfo(PortInfo info) {
        HandlerAnnotationInfo handlerInfo = new HandlerAnnotationInfo();
        List<Handler> handlerClassList = new ArrayList<>();
        Set<String> roles = new HashSet<>();
        for (HandlerChainType hchain : this.handlerChains) {
            boolean hchainMatched = false;
            if (!hchain.isConstraintSet() || JAXWSUtils.matchQNames(info.getServiceName(), hchain.getServiceNamePattern()) || JAXWSUtils.matchQNames(info.getPortName(), hchain.getPortNamePattern()) || hchain.getProtocolBindings().contains(info.getBindingID())) {
                hchainMatched = true;
            }
            if (hchainMatched) {
                for (HandlerType handler : hchain.getHandlers()) {
                    try {
                        Handler handlerClass = (Handler) loadClass(this.annotatedClass.getClassLoader(), handler.getHandlerClass()).newInstance();
                        callHandlerPostConstruct(handlerClass);
                        handlerClassList.add(handlerClass);
                        roles.addAll(handler.getSoapRoles());
                    } catch (IllegalAccessException e2) {
                        throw new RuntimeException(e2);
                    } catch (InstantiationException ie) {
                        throw new RuntimeException(ie);
                    }
                }
            }
        }
        handlerInfo.setHandlers(handlerClassList);
        handlerInfo.setRoles(roles);
        return handlerInfo;
    }

    private static Class loadClass(ClassLoader loader, String name) {
        try {
            return Class.forName(name, true, loader);
        } catch (ClassNotFoundException e2) {
            throw new UtilException("util.handler.class.not.found", name);
        }
    }

    private static void callHandlerPostConstruct(Object handlerClass) throws SecurityException {
        for (Method method : handlerClass.getClass().getMethods()) {
            if (method.getAnnotation(PostConstruct.class) != null) {
                try {
                    method.invoke(handlerClass, new Object[0]);
                    return;
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
        }
    }

    private static void skipChain(XMLStreamReader reader) {
        while (true) {
            if (XMLStreamReaderUtil.nextContent(reader) == 2 && reader.getName().equals(QNAME_HANDLER_CHAIN)) {
                XMLStreamReaderUtil.nextElementContent(reader);
                return;
            }
        }
    }

    private static void skipTextElement(XMLStreamReader reader) {
        XMLStreamReaderUtil.nextContent(reader);
        XMLStreamReaderUtil.nextElementContent(reader);
        XMLStreamReaderUtil.nextElementContent(reader);
    }

    private static void skipInitParamElement(XMLStreamReader reader) {
        while (true) {
            int state = XMLStreamReaderUtil.nextContent(reader);
            if (state == 2 && reader.getName().equals(QNAME_HANDLER_PARAM)) {
                XMLStreamReaderUtil.nextElementContent(reader);
                return;
            }
        }
    }

    private static void ensureProperName(XMLStreamReader reader, QName expectedName) {
        if (!reader.getName().equals(expectedName)) {
            failWithLocalName("util.parser.wrong.element", reader, expectedName.getLocalPart());
        }
    }

    static void ensureProperName(XMLStreamReader reader, String expectedName) {
        if (!reader.getLocalName().equals(expectedName)) {
            failWithLocalName("util.parser.wrong.element", reader, expectedName);
        }
    }

    private static void failWithLocalName(String key, XMLStreamReader reader, String arg) {
        throw new UtilException(key, Integer.toString(reader.getLocation().getLineNumber()), reader.getLocalName(), arg);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerChainsModel$HandlerChainType.class */
    static class HandlerChainType {
        QName serviceNamePattern;
        QName portNamePattern;
        List<HandlerType> handlers;
        String id;
        boolean constraintSet = false;
        List<String> protocolBindings = new ArrayList();

        public void setServiceNamePattern(QName value) {
            this.serviceNamePattern = value;
            this.constraintSet = true;
        }

        public QName getServiceNamePattern() {
            return this.serviceNamePattern;
        }

        public void setPortNamePattern(QName value) {
            this.portNamePattern = value;
            this.constraintSet = true;
        }

        public QName getPortNamePattern() {
            return this.portNamePattern;
        }

        public List<String> getProtocolBindings() {
            return this.protocolBindings;
        }

        public void addProtocolBinding(String tokenOrURI) {
            String binding = BindingID.parse(DeploymentDescriptorParser.getBindingIdForToken(tokenOrURI)).toString();
            this.protocolBindings.add(binding);
            this.constraintSet = true;
        }

        public boolean isConstraintSet() {
            return this.constraintSet || !this.protocolBindings.isEmpty();
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public List<HandlerType> getHandlers() {
            if (this.handlers == null) {
                this.handlers = new ArrayList();
            }
            return this.handlers;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/HandlerChainsModel$HandlerType.class */
    static class HandlerType {
        String handlerName;
        String handlerClass;
        List<String> soapRoles;
        String id;

        public String getHandlerName() {
            return this.handlerName;
        }

        public void setHandlerName(String value) {
            this.handlerName = value;
        }

        public String getHandlerClass() {
            return this.handlerClass;
        }

        public void setHandlerClass(String value) {
            this.handlerClass = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public List<String> getSoapRoles() {
            if (this.soapRoles == null) {
                this.soapRoles = new ArrayList();
            }
            return this.soapRoles;
        }
    }
}
