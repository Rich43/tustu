package com.sun.xml.internal.ws.model;

import com.oracle.webservices.internal.api.databinding.DatabindingModeFeature;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.databinding.Databinding;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPart;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.db.DatabindingImpl;
import com.sun.xml.internal.ws.developer.JAXBContextFactory;
import com.sun.xml.internal.ws.developer.UsesJAXBContextFeature;
import com.sun.xml.internal.ws.resources.ModelerMessages;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import com.sun.xml.internal.ws.spi.db.BindingInfo;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.util.Pool;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebParam;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/AbstractSEIModelImpl.class */
public abstract class AbstractSEIModelImpl implements SEIModel {
    private Pool.Marshaller marshallers;
    protected JAXBRIContext jaxbContext;
    protected BindingContext bindingContext;
    private String wsdlLocation;
    private QName serviceName;
    private QName portName;
    private QName portTypeName;
    private WSDLPort port;
    private final WebServiceFeatureList features;
    private Databinding databinding;
    BindingID bindingId;
    protected Class contractClass;
    protected Class endpointClass;
    protected WSBinding wsBinding;
    protected String defaultSchemaNamespaceSuffix;
    private static final Logger LOGGER;
    static final /* synthetic */ boolean $assertionsDisabled;
    private List<Class> additionalClasses = new ArrayList();
    private Map<Method, JavaMethodImpl> methodToJM = new HashMap();
    private Map<QName, JavaMethodImpl> nameToJM = new HashMap();
    private Map<QName, JavaMethodImpl> wsdlOpToJM = new HashMap();
    private List<JavaMethodImpl> javaMethods = new ArrayList();
    private final Map<TypeReference, Bridge> bridgeMap = new HashMap();
    private final Map<TypeInfo, XMLBridge> xmlBridgeMap = new HashMap();
    protected final QName emptyBodyName = new QName("");
    private String targetNamespace = "";
    private List<String> knownNamespaceURIs = null;
    protected ClassLoader classLoader = null;
    protected BindingInfo databindingInfo = new BindingInfo();

    protected abstract void populateMaps();

    static {
        $assertionsDisabled = !AbstractSEIModelImpl.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(AbstractSEIModelImpl.class.getName());
    }

    protected AbstractSEIModelImpl(WebServiceFeatureList features) {
        this.features = features;
        this.databindingInfo.setSEIModel(this);
    }

    void postProcess() {
        if (this.jaxbContext != null) {
            return;
        }
        populateMaps();
        createJAXBContext();
    }

    public void freeze(WSDLPort port) {
        this.port = port;
        for (JavaMethodImpl m2 : this.javaMethods) {
            m2.freeze(port);
            putOp(m2.getOperationQName(), m2);
        }
        if (this.databinding != null) {
            ((DatabindingImpl) this.databinding).freeze(port);
        }
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public Pool.Marshaller getMarshallerPool() {
        return this.marshallers;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public JAXBContext getJAXBContext() {
        JAXBContext jc = this.bindingContext.getJAXBContext();
        if (jc != null) {
            return jc;
        }
        return this.jaxbContext;
    }

    public BindingContext getBindingContext() {
        return this.bindingContext;
    }

    public List<String> getKnownNamespaceURIs() {
        return this.knownNamespaceURIs;
    }

    public final Bridge getBridge(TypeReference type) {
        Bridge b2 = this.bridgeMap.get(type);
        if ($assertionsDisabled || b2 != null) {
            return b2;
        }
        throw new AssertionError();
    }

    public final XMLBridge getXMLBridge(TypeInfo type) {
        XMLBridge b2 = this.xmlBridgeMap.get(type);
        if ($assertionsDisabled || b2 != null) {
            return b2;
        }
        throw new AssertionError();
    }

    private void createJAXBContext() {
        final List<TypeInfo> types = getAllTypeInfos();
        final List<Class> cls = new ArrayList<>(types.size() + this.additionalClasses.size());
        cls.addAll(this.additionalClasses);
        for (TypeInfo type : types) {
            cls.add((Class) type.type);
        }
        try {
            this.bindingContext = (BindingContext) AccessController.doPrivileged(new PrivilegedExceptionAction<BindingContext>() { // from class: com.sun.xml.internal.ws.model.AbstractSEIModelImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public BindingContext run() throws Exception {
                    if (AbstractSEIModelImpl.LOGGER.isLoggable(Level.FINEST)) {
                        AbstractSEIModelImpl.LOGGER.log(Level.FINEST, "Creating JAXBContext with classes={0} and types={1}", new Object[]{cls, types});
                    }
                    UsesJAXBContextFeature f2 = (UsesJAXBContextFeature) AbstractSEIModelImpl.this.features.get(UsesJAXBContextFeature.class);
                    DatabindingModeFeature dmf = (DatabindingModeFeature) AbstractSEIModelImpl.this.features.get(DatabindingModeFeature.class);
                    JAXBContextFactory factory = f2 != null ? f2.getFactory() : null;
                    if (factory == null) {
                        factory = JAXBContextFactory.DEFAULT;
                    }
                    AbstractSEIModelImpl.this.databindingInfo.properties().put(JAXBContextFactory.class.getName(), factory);
                    if (dmf != null) {
                        if (AbstractSEIModelImpl.LOGGER.isLoggable(Level.FINE)) {
                            AbstractSEIModelImpl.LOGGER.log(Level.FINE, "DatabindingModeFeature in SEI specifies mode: {0}", dmf.getMode());
                        }
                        AbstractSEIModelImpl.this.databindingInfo.setDatabindingMode(dmf.getMode());
                    }
                    if (f2 != null) {
                        AbstractSEIModelImpl.this.databindingInfo.setDatabindingMode("glassfish.jaxb");
                    }
                    AbstractSEIModelImpl.this.databindingInfo.setClassLoader(AbstractSEIModelImpl.this.classLoader);
                    AbstractSEIModelImpl.this.databindingInfo.contentClasses().addAll(cls);
                    AbstractSEIModelImpl.this.databindingInfo.typeInfos().addAll(types);
                    AbstractSEIModelImpl.this.databindingInfo.properties().put("c14nSupport", Boolean.FALSE);
                    AbstractSEIModelImpl.this.databindingInfo.setDefaultNamespace(AbstractSEIModelImpl.this.getDefaultSchemaNamespace());
                    BindingContext bc2 = BindingContextFactory.create(AbstractSEIModelImpl.this.databindingInfo);
                    if (AbstractSEIModelImpl.LOGGER.isLoggable(Level.FINE)) {
                        AbstractSEIModelImpl.LOGGER.log(Level.FINE, "Created binding context: " + bc2.getClass().getName());
                    }
                    return bc2;
                }
            });
            createBondMap(types);
            this.knownNamespaceURIs = new ArrayList();
            for (String namespace : this.bindingContext.getKnownNamespaceURIs()) {
                if (namespace.length() > 0 && !namespace.equals("http://www.w3.org/2001/XMLSchema") && !namespace.equals("http://www.w3.org/XML/1998/namespace")) {
                    this.knownNamespaceURIs.add(namespace);
                }
            }
            this.marshallers = new Pool.Marshaller(this.jaxbContext);
        } catch (PrivilegedActionException e2) {
            throw new WebServiceException(ModelerMessages.UNABLE_TO_CREATE_JAXB_CONTEXT(), e2);
        }
    }

    private List<TypeInfo> getAllTypeInfos() {
        List<TypeInfo> types = new ArrayList<>();
        Collection<JavaMethodImpl> methods = this.methodToJM.values();
        for (JavaMethodImpl m2 : methods) {
            m2.fillTypes(types);
        }
        return types;
    }

    private void createBridgeMap(List<TypeReference> types) {
        for (TypeReference type : types) {
            Bridge bridge = this.jaxbContext.createBridge(type);
            this.bridgeMap.put(type, bridge);
        }
    }

    private void createBondMap(List<TypeInfo> types) {
        for (TypeInfo type : types) {
            XMLBridge binding = this.bindingContext.createBridge(type);
            this.xmlBridgeMap.put(type, binding);
        }
    }

    public boolean isKnownFault(QName name, Method method) {
        JavaMethodImpl m2 = getJavaMethod(method);
        for (CheckedExceptionImpl ce : m2.getCheckedExceptions()) {
            if (ce.getDetailType().tagName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCheckedException(Method m2, Class ex) {
        JavaMethodImpl jm = getJavaMethod(m2);
        for (CheckedExceptionImpl ce : jm.getCheckedExceptions()) {
            if (ce.getExceptionClass().equals(ex)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public JavaMethodImpl getJavaMethod(Method method) {
        return this.methodToJM.get(method);
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public JavaMethodImpl getJavaMethod(QName name) {
        return this.nameToJM.get(name);
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public JavaMethod getJavaMethodForWsdlOperation(QName operationName) {
        return this.wsdlOpToJM.get(operationName);
    }

    public QName getQNameForJM(JavaMethodImpl jm) {
        for (QName key : this.nameToJM.keySet()) {
            JavaMethodImpl jmethod = this.nameToJM.get(key);
            if (jmethod.getOperationName().equals(jm.getOperationName())) {
                return key;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public final Collection<JavaMethodImpl> getJavaMethods() {
        return Collections.unmodifiableList(this.javaMethods);
    }

    void addJavaMethod(JavaMethodImpl jm) {
        if (jm != null) {
            this.javaMethods.add(jm);
        }
    }

    private List<ParameterImpl> applyRpcLitParamBinding(JavaMethodImpl method, WrapperParameter wrapperParameter, WSDLBoundPortType boundPortType, WebParam.Mode mode) {
        ParameterBinding paramBinding;
        QName opName = new QName(boundPortType.getPortTypeName().getNamespaceURI(), method.getOperationName());
        WSDLBoundOperation bo2 = boundPortType.get(opName);
        Map<Integer, ParameterImpl> bodyParams = new HashMap<>();
        List<ParameterImpl> unboundParams = new ArrayList<>();
        List<ParameterImpl> attachParams = new ArrayList<>();
        for (ParameterImpl param : wrapperParameter.wrapperChildren) {
            String partName = param.getPartName();
            if (partName != null && (paramBinding = boundPortType.getBinding(opName, partName, mode)) != null) {
                if (mode == WebParam.Mode.IN) {
                    param.setInBinding(paramBinding);
                } else if (mode == WebParam.Mode.OUT || mode == WebParam.Mode.INOUT) {
                    param.setOutBinding(paramBinding);
                }
                if (paramBinding.isUnbound()) {
                    unboundParams.add(param);
                } else if (paramBinding.isAttachment()) {
                    attachParams.add(param);
                } else if (paramBinding.isBody()) {
                    if (bo2 != null) {
                        WSDLPart p2 = bo2.getPart(param.getPartName(), mode);
                        if (p2 != null) {
                            bodyParams.put(Integer.valueOf(p2.getIndex()), param);
                        } else {
                            bodyParams.put(Integer.valueOf(bodyParams.size()), param);
                        }
                    } else {
                        bodyParams.put(Integer.valueOf(bodyParams.size()), param);
                    }
                }
            }
        }
        wrapperParameter.clear();
        for (int i2 = 0; i2 < bodyParams.size(); i2++) {
            ParameterImpl p3 = bodyParams.get(Integer.valueOf(i2));
            wrapperParameter.addWrapperChild(p3);
        }
        for (ParameterImpl p4 : unboundParams) {
            wrapperParameter.addWrapperChild(p4);
        }
        return attachParams;
    }

    void put(QName name, JavaMethodImpl jm) {
        this.nameToJM.put(name, jm);
    }

    void put(Method method, JavaMethodImpl jm) {
        this.methodToJM.put(method, jm);
    }

    void putOp(QName opName, JavaMethodImpl jm) {
        this.wsdlOpToJM.put(opName, jm);
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public String getWSDLLocation() {
        return this.wsdlLocation;
    }

    void setWSDLLocation(String location) {
        this.wsdlLocation = location;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public QName getServiceQName() {
        return this.serviceName;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public WSDLPort getPort() {
        return this.port;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public QName getPortName() {
        return this.portName;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public QName getPortTypeName() {
        return this.portTypeName;
    }

    void setServiceQName(QName name) {
        this.serviceName = name;
    }

    void setPortName(QName name) {
        this.portName = name;
    }

    void setPortTypeName(QName name) {
        this.portTypeName = name;
    }

    void setTargetNamespace(String namespace) {
        this.targetNamespace = namespace;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    public String getTargetNamespace() {
        return this.targetNamespace;
    }

    String getDefaultSchemaNamespace() {
        String defaultNamespace = getTargetNamespace();
        if (this.defaultSchemaNamespaceSuffix == null) {
            return defaultNamespace;
        }
        if (!defaultNamespace.endsWith("/")) {
            defaultNamespace = defaultNamespace + "/";
        }
        return defaultNamespace + this.defaultSchemaNamespaceSuffix;
    }

    @Override // com.sun.xml.internal.ws.api.model.SEIModel
    @NotNull
    public QName getBoundPortTypeName() {
        if ($assertionsDisabled || this.portName != null) {
            return new QName(this.portName.getNamespaceURI(), this.portName.getLocalPart() + "Binding");
        }
        throw new AssertionError();
    }

    public void addAdditionalClasses(Class... additionalClasses) {
        for (Class cls : additionalClasses) {
            this.additionalClasses.add(cls);
        }
    }

    public Databinding getDatabinding() {
        return this.databinding;
    }

    public void setDatabinding(Databinding wsRuntime) {
        this.databinding = wsRuntime;
    }

    public WSBinding getWSBinding() {
        return this.wsBinding;
    }

    public Class getContractClass() {
        return this.contractClass;
    }

    public Class getEndpointClass() {
        return this.endpointClass;
    }
}
