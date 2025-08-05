package com.sun.xml.internal.ws.model;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import com.oracle.webservices.internal.api.EnvelopeStyleFeature;
import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.localization.Localizable;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.model.ExceptionType;
import com.sun.xml.internal.ws.api.model.MEP;
import com.sun.xml.internal.ws.api.model.Parameter;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPart;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.model.soap.SOAPBindingImpl;
import com.sun.xml.internal.ws.resources.ModelerMessages;
import com.sun.xml.internal.ws.resources.ServerMessages;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;
import javax.xml.ws.Action;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingType;
import javax.xml.ws.FaultAction;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.Response;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebFault;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.MTOMFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/RuntimeModeler.class */
public class RuntimeModeler {
    private final WebServiceFeatureList features;
    private BindingID bindingId;
    private WSBinding wsBinding;
    private final Class portClass;
    private AbstractSEIModelImpl model;
    private SOAPBindingImpl defaultBinding;
    private String packageName;
    private String targetNamespace;
    private boolean isWrapped = true;
    private ClassLoader classLoader;
    private final WSDLPort binding;
    private QName serviceName;
    private QName portName;
    private Set<Class> classUsesWebMethod;
    private DatabindingConfig config;
    private MetadataReader metadataReader;
    public static final String PD_JAXWS_PACKAGE_PD = ".jaxws.";
    public static final String JAXWS_PACKAGE_PD = "jaxws.";
    public static final String RESPONSE = "Response";
    public static final String RETURN = "return";
    public static final String BEAN = "Bean";
    public static final String SERVICE = "Service";
    public static final String PORT = "Port";
    public static final Class HOLDER_CLASS;
    public static final Class<RemoteException> REMOTE_EXCEPTION_CLASS;
    public static final Class<RuntimeException> RUNTIME_EXCEPTION_CLASS;
    public static final Class<Exception> EXCEPTION_CLASS;
    public static final String DecapitalizeExceptionBeanProperties = "com.sun.xml.internal.ws.api.model.DecapitalizeExceptionBeanProperties";
    public static final String SuppressDocLitWrapperGeneration = "com.sun.xml.internal.ws.api.model.SuppressDocLitWrapperGeneration";
    public static final String DocWrappeeNamespapceQualified = "com.sun.xml.internal.ws.api.model.DocWrappeeNamespapceQualified";
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RuntimeModeler.class.desiredAssertionStatus();
        HOLDER_CLASS = Holder.class;
        REMOTE_EXCEPTION_CLASS = RemoteException.class;
        RUNTIME_EXCEPTION_CLASS = RuntimeException.class;
        EXCEPTION_CLASS = Exception.class;
        logger = Logger.getLogger("com.sun.xml.internal.ws.server");
    }

    public RuntimeModeler(@NotNull DatabindingConfig config) {
        EnvelopeStyle es;
        MTOM mtomAn;
        this.portClass = config.getEndpointClass() != null ? config.getEndpointClass() : config.getContractClass();
        this.serviceName = config.getMappingInfo().getServiceName();
        this.binding = config.getWsdlPort();
        this.classLoader = config.getClassLoader();
        this.portName = config.getMappingInfo().getPortName();
        this.config = config;
        this.wsBinding = config.getWSBinding();
        this.metadataReader = config.getMetadataReader();
        this.targetNamespace = config.getMappingInfo().getTargetNamespace();
        if (this.metadataReader == null) {
            this.metadataReader = new ReflectAnnotationReader();
        }
        if (this.wsBinding != null) {
            this.bindingId = this.wsBinding.getBindingId();
            if (config.getFeatures() != null) {
                this.wsBinding.getFeatures().mergeFeatures(config.getFeatures(), false);
            }
            if (this.binding != null) {
                this.wsBinding.getFeatures().mergeFeatures((Iterable<WebServiceFeature>) this.binding.getFeatures(), false);
            }
            this.features = WebServiceFeatureList.toList(this.wsBinding.getFeatures());
            return;
        }
        this.bindingId = config.getMappingInfo().getBindingID();
        this.features = WebServiceFeatureList.toList(config.getFeatures());
        if (this.binding != null) {
            this.bindingId = this.binding.getBinding().getBindingId();
        }
        if (this.bindingId == null) {
            this.bindingId = getDefaultBindingID();
        }
        if (!this.features.contains(MTOMFeature.class) && (mtomAn = (MTOM) getAnnotation(this.portClass, MTOM.class)) != null) {
            this.features.add(WebServiceFeatureList.getFeature(mtomAn));
        }
        if (!this.features.contains(EnvelopeStyleFeature.class) && (es = (EnvelopeStyle) getAnnotation(this.portClass, EnvelopeStyle.class)) != null) {
            this.features.add(WebServiceFeatureList.getFeature(es));
        }
        this.wsBinding = this.bindingId.createBinding(this.features);
    }

    private BindingID getDefaultBindingID() {
        BindingType bt2 = (BindingType) getAnnotation(this.portClass, BindingType.class);
        if (bt2 != null) {
            return BindingID.parse(bt2.value());
        }
        SOAPVersion ver = WebServiceFeatureList.getSoapVersion(this.features);
        boolean mtomEnabled = this.features.isEnabled(MTOMFeature.class);
        return SOAPVersion.SOAP_12.equals(ver) ? mtomEnabled ? BindingID.SOAP12_HTTP_MTOM : BindingID.SOAP12_HTTP : mtomEnabled ? BindingID.SOAP11_HTTP_MTOM : BindingID.SOAP11_HTTP;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setPortName(QName portName) {
        this.portName = portName;
    }

    private <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> cls2) {
        return (T) this.metadataReader.getAnnotation(cls2, cls);
    }

    private <T extends Annotation> T getAnnotation(Method method, Class<T> cls) {
        return (T) this.metadataReader.getAnnotation(cls, method);
    }

    private Annotation[] getAnnotations(Method method) {
        return this.metadataReader.getAnnotations(method);
    }

    private Annotation[] getAnnotations(Class<?> c2) {
        return this.metadataReader.getAnnotations(c2);
    }

    private Annotation[][] getParamAnnotations(Method method) {
        return this.metadataReader.getParameterAnnotations(method);
    }

    public AbstractSEIModelImpl buildRuntimeModel() throws SecurityException {
        this.model = new SOAPSEIModel(this.features);
        this.model.contractClass = this.config.getContractClass();
        this.model.endpointClass = this.config.getEndpointClass();
        this.model.classLoader = this.classLoader;
        this.model.wsBinding = this.wsBinding;
        this.model.databindingInfo.setWsdlURL(this.config.getWsdlURL());
        this.model.databindingInfo.properties().putAll(this.config.properties());
        if (this.model.contractClass == null) {
            this.model.contractClass = this.portClass;
        }
        if (this.model.endpointClass == null && !this.portClass.isInterface()) {
            this.model.endpointClass = this.portClass;
        }
        Class<?> seiClass = this.portClass;
        this.metadataReader.getProperties(this.model.databindingInfo.properties(), this.portClass);
        WebService webService = (WebService) getAnnotation(this.portClass, WebService.class);
        if (webService == null) {
            throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", this.portClass.getCanonicalName());
        }
        Class<?> seiFromConfig = configEndpointInterface();
        if (webService.endpointInterface().length() > 0 || seiFromConfig != null) {
            if (seiFromConfig != null) {
                seiClass = seiFromConfig;
            } else {
                seiClass = getClass(webService.endpointInterface(), ModelerMessages.localizableRUNTIME_MODELER_CLASS_NOT_FOUND(webService.endpointInterface()));
            }
            this.model.contractClass = seiClass;
            this.model.endpointClass = this.portClass;
            WebService seiService = (WebService) getAnnotation(seiClass, WebService.class);
            if (seiService == null) {
                throw new RuntimeModelerException("runtime.modeler.endpoint.interface.no.webservice", webService.endpointInterface());
            }
            SOAPBinding sbPortClass = (SOAPBinding) getAnnotation(this.portClass, SOAPBinding.class);
            SOAPBinding sbSei = (SOAPBinding) getAnnotation(seiClass, SOAPBinding.class);
            if (sbPortClass != null && (sbSei == null || sbSei.style() != sbPortClass.style() || sbSei.use() != sbPortClass.use())) {
                logger.warning(ServerMessages.RUNTIMEMODELER_INVALIDANNOTATION_ON_IMPL("@SOAPBinding", this.portClass.getName(), seiClass.getName()));
            }
        }
        if (this.serviceName == null) {
            this.serviceName = getServiceName((Class<?>) this.portClass, this.metadataReader);
        }
        this.model.setServiceQName(this.serviceName);
        if (this.portName == null) {
            this.portName = getPortName((Class<?>) this.portClass, this.metadataReader, this.serviceName.getNamespaceURI());
        }
        this.model.setPortName(this.portName);
        DatabindingMode dbm2 = (DatabindingMode) getAnnotation(this.portClass, DatabindingMode.class);
        if (dbm2 != null) {
            this.model.databindingInfo.setDatabindingMode(dbm2.value());
        }
        processClass(seiClass);
        if (this.model.getJavaMethods().size() == 0) {
            throw new RuntimeModelerException("runtime.modeler.no.operations", this.portClass.getName());
        }
        this.model.postProcess();
        this.config.properties().put(BindingContext.class.getName(), this.model.bindingContext);
        if (this.binding != null) {
            this.model.freeze(this.binding);
        }
        return this.model;
    }

    private Class configEndpointInterface() {
        if (this.config.getEndpointClass() == null || this.config.getEndpointClass().isInterface()) {
            return null;
        }
        return this.config.getContractClass();
    }

    private Class getClass(String className, Localizable errorMessage) {
        try {
            if (this.classLoader == null) {
                return Thread.currentThread().getContextClassLoader().loadClass(className);
            }
            return this.classLoader.loadClass(className);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeModelerException(errorMessage);
        }
    }

    private boolean noWrapperGen() {
        Object o2 = this.config.properties().get(SuppressDocLitWrapperGeneration);
        if (o2 == null || !(o2 instanceof Boolean)) {
            return false;
        }
        return ((Boolean) o2).booleanValue();
    }

    private Class getRequestWrapperClass(String className, Method method, QName reqElemName) {
        ClassLoader loader = this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e2) {
            if (noWrapperGen()) {
                return WrapperComposite.class;
            }
            logger.fine("Dynamically creating request wrapper Class " + className);
            return WrapperBeanGenerator.createRequestWrapperBean(className, method, reqElemName, loader);
        }
    }

    private Class getResponseWrapperClass(String className, Method method, QName resElemName) {
        ClassLoader loader = this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e2) {
            if (noWrapperGen()) {
                return WrapperComposite.class;
            }
            logger.fine("Dynamically creating response wrapper bean Class " + className);
            return WrapperBeanGenerator.createResponseWrapperBean(className, method, resElemName, loader);
        }
    }

    private Class getExceptionBeanClass(String className, Class exception, String name, String namespace) {
        boolean decapitalizeExceptionBeanProperties = true;
        Object o2 = this.config.properties().get(DecapitalizeExceptionBeanProperties);
        if (o2 != null && (o2 instanceof Boolean)) {
            decapitalizeExceptionBeanProperties = ((Boolean) o2).booleanValue();
        }
        ClassLoader loader = this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e2) {
            logger.fine("Dynamically creating exception bean Class " + className);
            return WrapperBeanGenerator.createExceptionBean(className, exception, this.targetNamespace, name, namespace, loader, decapitalizeExceptionBeanProperties);
        }
    }

    protected void determineWebMethodUse(Class clazz) throws SecurityException {
        WebMethod webMethod;
        if (clazz == null) {
            return;
        }
        if (!clazz.isInterface()) {
            if (clazz == Object.class) {
                return;
            }
            Method[] methods = clazz.getMethods();
            int length = methods.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                Method method = methods[i2];
                if (method.getDeclaringClass() != clazz || (webMethod = (WebMethod) getAnnotation(method, WebMethod.class)) == null || webMethod.exclude()) {
                    i2++;
                } else {
                    this.classUsesWebMethod.add(clazz);
                    break;
                }
            }
        }
        determineWebMethodUse(clazz.getSuperclass());
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x012f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void processClass(java.lang.Class r9) throws java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 347
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.model.RuntimeModeler.processClass(java.lang.Class):void");
    }

    private boolean isWebMethodBySpec(Method method, Class clazz) {
        int modifiers = method.getModifiers();
        boolean staticFinal = Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers);
        if (!$assertionsDisabled && !Modifier.isPublic(modifiers)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && clazz.isInterface()) {
            throw new AssertionError();
        }
        WebMethod webMethod = (WebMethod) getAnnotation(method, WebMethod.class);
        if (webMethod != null) {
            if (webMethod.exclude()) {
                return false;
            }
            if (staticFinal) {
                throw new RuntimeModelerException(ModelerMessages.localizableRUNTIME_MODELER_WEBMETHOD_MUST_BE_NONSTATICFINAL(method));
            }
            return true;
        }
        if (staticFinal) {
            return false;
        }
        Class declClass = method.getDeclaringClass();
        return getAnnotation((Class<?>) declClass, WebService.class) != null;
    }

    private boolean isWebMethod(Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
            return false;
        }
        Class clazz = method.getDeclaringClass();
        boolean declHasWebService = getAnnotation((Class<?>) clazz, WebService.class) != null;
        WebMethod webMethod = (WebMethod) getAnnotation(method, WebMethod.class);
        if (webMethod == null || webMethod.exclude() || !declHasWebService) {
            return declHasWebService && !this.classUsesWebMethod.contains(clazz);
        }
        return true;
    }

    protected SOAPBindingImpl createBinding(SOAPBinding soapBinding) {
        SOAPBindingImpl rtSOAPBinding = new SOAPBindingImpl();
        SOAPBinding.Style style = soapBinding != null ? soapBinding.style() : SOAPBinding.Style.DOCUMENT;
        rtSOAPBinding.setStyle(style);
        if (!$assertionsDisabled && this.bindingId == null) {
            throw new AssertionError();
        }
        this.model.bindingId = this.bindingId;
        SOAPVersion soapVersion = this.bindingId.getSOAPVersion();
        rtSOAPBinding.setSOAPVersion(soapVersion);
        return rtSOAPBinding;
    }

    public static String getNamespace(@NotNull String packageName) {
        String[] tokens;
        if (packageName.length() == 0) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(packageName, ".");
        if (tokenizer.countTokens() == 0) {
            tokens = new String[0];
        } else {
            tokens = new String[tokenizer.countTokens()];
            for (int i2 = tokenizer.countTokens() - 1; i2 >= 0; i2--) {
                tokens[i2] = tokenizer.nextToken();
            }
        }
        StringBuilder namespace = new StringBuilder("http://");
        for (int i3 = 0; i3 < tokens.length; i3++) {
            if (i3 != 0) {
                namespace.append('.');
            }
            namespace.append(tokens[i3]);
        }
        namespace.append('/');
        return namespace.toString();
    }

    private boolean isServiceException(Class<?> exception) {
        return (!EXCEPTION_CLASS.isAssignableFrom(exception) || RUNTIME_EXCEPTION_CLASS.isAssignableFrom(exception) || REMOTE_EXCEPTION_CLASS.isAssignableFrom(exception)) ? false : true;
    }

    private void processMethod(Method method) throws SecurityException {
        JavaMethodImpl javaMethod;
        WSDLBoundOperation bo2;
        WebMethod webMethod = (WebMethod) getAnnotation(method, WebMethod.class);
        if (webMethod == null || !webMethod.exclude()) {
            String methodName = method.getName();
            boolean isOneway = getAnnotation(method, Oneway.class) != null;
            if (isOneway) {
                for (Class<?> exception : method.getExceptionTypes()) {
                    if (isServiceException(exception)) {
                        throw new RuntimeModelerException("runtime.modeler.oneway.operation.no.checked.exceptions", this.portClass.getCanonicalName(), methodName, exception.getName());
                    }
                }
            }
            if (method.getDeclaringClass() == this.portClass) {
                javaMethod = new JavaMethodImpl(this.model, method, method, this.metadataReader);
            } else {
                try {
                    Method tmpMethod = this.portClass.getMethod(method.getName(), method.getParameterTypes());
                    javaMethod = new JavaMethodImpl(this.model, tmpMethod, method, this.metadataReader);
                } catch (NoSuchMethodException e2) {
                    throw new RuntimeModelerException("runtime.modeler.method.not.found", method.getName(), this.portClass.getName());
                }
            }
            MEP mep = getMEP(method);
            javaMethod.setMEP(mep);
            String action = null;
            String operationName = method.getName();
            if (webMethod != null) {
                action = webMethod.action();
                operationName = webMethod.operationName().length() > 0 ? webMethod.operationName() : operationName;
            }
            if (this.binding != null && (bo2 = this.binding.getBinding().get(new QName(this.targetNamespace, operationName))) != null) {
                WSDLInput wsdlInput = bo2.getOperation().getInput();
                String wsaAction = wsdlInput.getAction();
                if (wsaAction != null && !wsdlInput.isDefaultAction()) {
                    action = wsaAction;
                } else {
                    action = bo2.getSOAPAction();
                }
            }
            javaMethod.setOperationQName(new QName(this.targetNamespace, operationName));
            SOAPBinding methodBinding = (SOAPBinding) getAnnotation(method, SOAPBinding.class);
            if (methodBinding != null && methodBinding.style() == SOAPBinding.Style.RPC) {
                logger.warning(ModelerMessages.RUNTIMEMODELER_INVALID_SOAPBINDING_ON_METHOD(methodBinding, method.getName(), method.getDeclaringClass().getName()));
            } else if (methodBinding == null && !method.getDeclaringClass().equals(this.portClass)) {
                methodBinding = (SOAPBinding) getAnnotation(method.getDeclaringClass(), SOAPBinding.class);
                if (methodBinding != null && methodBinding.style() == SOAPBinding.Style.RPC && methodBinding.parameterStyle() == SOAPBinding.ParameterStyle.BARE) {
                    throw new RuntimeModelerException("runtime.modeler.invalid.soapbinding.parameterstyle", methodBinding, method.getDeclaringClass());
                }
            }
            if (methodBinding != null && this.defaultBinding.getStyle() != methodBinding.style()) {
                throw new RuntimeModelerException("runtime.modeler.soapbinding.conflict", methodBinding.style(), method.getName(), this.defaultBinding.getStyle());
            }
            boolean methodIsWrapped = this.isWrapped;
            SOAPBinding.Style style = this.defaultBinding.getStyle();
            if (methodBinding != null) {
                SOAPBindingImpl mySOAPBinding = createBinding(methodBinding);
                style = mySOAPBinding.getStyle();
                if (action != null) {
                    mySOAPBinding.setSOAPAction(action);
                }
                methodIsWrapped = methodBinding.parameterStyle().equals(SOAPBinding.ParameterStyle.WRAPPED);
                javaMethod.setBinding(mySOAPBinding);
            } else {
                SOAPBindingImpl sb = new SOAPBindingImpl(this.defaultBinding);
                if (action != null) {
                    sb.setSOAPAction(action);
                } else {
                    String defaults = SOAPVersion.SOAP_11 == sb.getSOAPVersion() ? "" : null;
                    sb.setSOAPAction(defaults);
                }
                javaMethod.setBinding(sb);
            }
            if (!methodIsWrapped) {
                processDocBareMethod(javaMethod, operationName, method);
            } else if (style.equals(SOAPBinding.Style.DOCUMENT)) {
                processDocWrappedMethod(javaMethod, methodName, operationName, method);
            } else {
                processRpcMethod(javaMethod, methodName, operationName, method);
            }
            this.model.addJavaMethod(javaMethod);
        }
    }

    private MEP getMEP(Method m2) {
        if (getAnnotation(m2, Oneway.class) != null) {
            return MEP.ONE_WAY;
        }
        if (Response.class.isAssignableFrom(m2.getReturnType())) {
            return MEP.ASYNC_POLL;
        }
        if (Future.class.isAssignableFrom(m2.getReturnType())) {
            return MEP.ASYNC_CALLBACK;
        }
        return MEP.REQUEST_RESPONSE;
    }

    protected void processDocWrappedMethod(JavaMethodImpl javaMethod, String methodName, String operationName, Method method) throws SecurityException {
        String requestClassName;
        String responseClassName;
        boolean methodHasHeaderParams = false;
        boolean isOneway = getAnnotation(method, Oneway.class) != null;
        RequestWrapper reqWrapper = (RequestWrapper) getAnnotation(method, RequestWrapper.class);
        ResponseWrapper resWrapper = (ResponseWrapper) getAnnotation(method, ResponseWrapper.class);
        String beanPackage = this.packageName + PD_JAXWS_PACKAGE_PD;
        if (this.packageName == null || this.packageName.length() == 0) {
            beanPackage = JAXWS_PACKAGE_PD;
        }
        if (reqWrapper != null && reqWrapper.className().length() > 0) {
            requestClassName = reqWrapper.className();
        } else {
            requestClassName = beanPackage + capitalize(method.getName());
        }
        if (resWrapper != null && resWrapper.className().length() > 0) {
            responseClassName = resWrapper.className();
        } else {
            responseClassName = beanPackage + capitalize(method.getName()) + RESPONSE;
        }
        String reqName = operationName;
        String reqNamespace = this.targetNamespace;
        String reqPartName = "parameters";
        if (reqWrapper != null) {
            if (reqWrapper.targetNamespace().length() > 0) {
                reqNamespace = reqWrapper.targetNamespace();
            }
            if (reqWrapper.localName().length() > 0) {
                reqName = reqWrapper.localName();
            }
            try {
                if (reqWrapper.partName().length() > 0) {
                    reqPartName = reqWrapper.partName();
                }
            } catch (LinkageError e2) {
            }
        }
        QName reqElementName = new QName(reqNamespace, reqName);
        javaMethod.setRequestPayloadName(reqElementName);
        Class requestClass = getRequestWrapperClass(requestClassName, method, reqElementName);
        Class responseClass = null;
        String resName = operationName + RESPONSE;
        String resNamespace = this.targetNamespace;
        QName resElementName = null;
        String resPartName = "parameters";
        if (!isOneway) {
            if (resWrapper != null) {
                if (resWrapper.targetNamespace().length() > 0) {
                    resNamespace = resWrapper.targetNamespace();
                }
                if (resWrapper.localName().length() > 0) {
                    resName = resWrapper.localName();
                }
                try {
                    if (resWrapper.partName().length() > 0) {
                        resPartName = resWrapper.partName();
                    }
                } catch (LinkageError e3) {
                }
            }
            resElementName = new QName(resNamespace, resName);
            responseClass = getResponseWrapperClass(responseClassName, method, resElementName);
        }
        TypeInfo typeRef = new TypeInfo(reqElementName, requestClass, new Annotation[0]);
        typeRef.setNillable(false);
        WrapperParameter requestWrapper = new WrapperParameter(javaMethod, typeRef, WebParam.Mode.IN, 0);
        requestWrapper.setPartName(reqPartName);
        requestWrapper.setBinding(ParameterBinding.BODY);
        javaMethod.addParameter(requestWrapper);
        WrapperParameter responseWrapper = null;
        if (!isOneway) {
            TypeInfo typeRef2 = new TypeInfo(resElementName, responseClass, new Annotation[0]);
            typeRef2.setNillable(false);
            responseWrapper = new WrapperParameter(javaMethod, typeRef2, WebParam.Mode.OUT, -1);
            javaMethod.addParameter(responseWrapper);
            responseWrapper.setBinding(ParameterBinding.BODY);
        }
        WebResult webResult = (WebResult) getAnnotation(method, WebResult.class);
        XmlElement xmlElem = (XmlElement) getAnnotation(method, XmlElement.class);
        QName resultQName = getReturnQName(method, webResult, xmlElem);
        Class returnType = method.getReturnType();
        boolean isResultHeader = false;
        if (webResult != null) {
            isResultHeader = webResult.header();
            methodHasHeaderParams = isResultHeader || 0 != 0;
            if (isResultHeader && xmlElem != null) {
                throw new RuntimeModelerException("@XmlElement cannot be specified on method " + ((Object) method) + " as the return value is bound to header", new Object[0]);
            }
            if (resultQName.getNamespaceURI().length() == 0 && webResult.header()) {
                resultQName = new QName(this.targetNamespace, resultQName.getLocalPart());
            }
        }
        if (javaMethod.isAsync()) {
            returnType = getAsyncReturnType(method, returnType);
            resultQName = new QName(RETURN);
        }
        QName resultQName2 = qualifyWrappeeIfNeeded(resultQName, resNamespace);
        if (!isOneway && returnType != null && !returnType.getName().equals("void")) {
            Annotation[] rann = getAnnotations(method);
            if (resultQName2.getLocalPart() != null) {
                TypeInfo rTypeReference = new TypeInfo(resultQName2, returnType, rann);
                this.metadataReader.getProperties(rTypeReference.properties(), method);
                rTypeReference.setGenericType(method.getGenericReturnType());
                ParameterImpl returnParameter = new ParameterImpl(javaMethod, rTypeReference, WebParam.Mode.OUT, -1);
                if (isResultHeader) {
                    returnParameter.setBinding(ParameterBinding.HEADER);
                    javaMethod.addParameter(returnParameter);
                } else {
                    returnParameter.setBinding(ParameterBinding.BODY);
                    responseWrapper.addWrapperChild(returnParameter);
                }
            }
        }
        Class[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] pannotations = getParamAnnotations(method);
        int pos = 0;
        int length = parameterTypes.length;
        for (int i2 = 0; i2 < length; i2++) {
            Class clazzType = parameterTypes[i2];
            String partName = null;
            String paramName = Constants.ELEMNAME_ARG_STRING + pos;
            boolean isHeader = false;
            if (!javaMethod.isAsync() || !AsyncHandler.class.isAssignableFrom(clazzType)) {
                boolean isHolder = HOLDER_CLASS.isAssignableFrom(clazzType);
                if (isHolder && clazzType == Holder.class) {
                    clazzType = (Class) Utils.REFLECTION_NAVIGATOR.erasure(((ParameterizedType) genericParameterTypes[pos]).getActualTypeArguments()[0]);
                }
                WebParam.Mode paramMode = isHolder ? WebParam.Mode.INOUT : WebParam.Mode.IN;
                WebParam webParam = null;
                XmlElement xmlElem2 = null;
                for (Annotation annotation : pannotations[pos]) {
                    if (annotation.annotationType() == WebParam.class) {
                        webParam = (WebParam) annotation;
                    } else if (annotation.annotationType() == XmlElement.class) {
                        xmlElem2 = (XmlElement) annotation;
                    }
                }
                QName paramQName = getParameterQName(method, webParam, xmlElem2, paramName);
                if (webParam != null) {
                    isHeader = webParam.header();
                    methodHasHeaderParams = isHeader || methodHasHeaderParams;
                    if (isHeader && xmlElem2 != null) {
                        throw new RuntimeModelerException("@XmlElement cannot be specified on method " + ((Object) method) + " parameter that is bound to header", new Object[0]);
                    }
                    if (webParam.partName().length() > 0) {
                        partName = webParam.partName();
                    } else {
                        partName = paramQName.getLocalPart();
                    }
                    if (isHeader && paramQName.getNamespaceURI().equals("")) {
                        paramQName = new QName(this.targetNamespace, paramQName.getLocalPart());
                    }
                    paramMode = webParam.mode();
                    if (isHolder && paramMode == WebParam.Mode.IN) {
                        paramMode = WebParam.Mode.INOUT;
                    }
                }
                TypeInfo typeRef3 = new TypeInfo(qualifyWrappeeIfNeeded(paramQName, reqNamespace), clazzType, pannotations[pos]);
                this.metadataReader.getProperties(typeRef3.properties(), method, pos);
                typeRef3.setGenericType(genericParameterTypes[pos]);
                int i3 = pos;
                pos++;
                ParameterImpl param = new ParameterImpl(javaMethod, typeRef3, paramMode, i3);
                if (isHeader) {
                    param.setBinding(ParameterBinding.HEADER);
                    javaMethod.addParameter(param);
                    param.setPartName(partName);
                } else {
                    param.setBinding(ParameterBinding.BODY);
                    if (paramMode != WebParam.Mode.OUT) {
                        requestWrapper.addWrapperChild(param);
                    }
                    if (paramMode == WebParam.Mode.IN) {
                        continue;
                    } else {
                        if (isOneway) {
                            throw new RuntimeModelerException("runtime.modeler.oneway.operation.no.out.parameters", this.portClass.getCanonicalName(), methodName);
                        }
                        responseWrapper.addWrapperChild(param);
                    }
                }
            }
        }
        if (methodHasHeaderParams) {
            resPartName = "result";
        }
        if (responseWrapper != null) {
            responseWrapper.setPartName(resPartName);
        }
        processExceptions(javaMethod, method);
    }

    private QName qualifyWrappeeIfNeeded(QName resultQName, String ns) {
        Object o2 = this.config.properties().get(DocWrappeeNamespapceQualified);
        boolean qualified = (o2 == null || !(o2 instanceof Boolean)) ? false : ((Boolean) o2).booleanValue();
        if (qualified && (resultQName.getNamespaceURI() == null || "".equals(resultQName.getNamespaceURI()))) {
            return new QName(ns, resultQName.getLocalPart());
        }
        return resultQName;
    }

    protected void processRpcMethod(JavaMethodImpl javaMethod, String methodName, String operationName, Method method) throws SecurityException {
        QName resultQName;
        QName paramQName;
        boolean isOneway = getAnnotation(method, Oneway.class) != null;
        Map<Integer, ParameterImpl> resRpcParams = new TreeMap<>();
        Map<Integer, ParameterImpl> reqRpcParams = new TreeMap<>();
        String reqNamespace = this.targetNamespace;
        String respNamespace = this.targetNamespace;
        if (this.binding != null && SOAPBinding.Style.RPC.equals(this.binding.getBinding().getStyle())) {
            QName opQName = new QName(this.binding.getBinding().getPortTypeName().getNamespaceURI(), operationName);
            WSDLBoundOperation op = this.binding.getBinding().get(opQName);
            if (op != null) {
                if (op.getRequestNamespace() != null) {
                    reqNamespace = op.getRequestNamespace();
                }
                if (op.getResponseNamespace() != null) {
                    respNamespace = op.getResponseNamespace();
                }
            }
        }
        QName reqElementName = new QName(reqNamespace, operationName);
        javaMethod.setRequestPayloadName(reqElementName);
        QName resElementName = null;
        if (!isOneway) {
            resElementName = new QName(respNamespace, operationName + RESPONSE);
        }
        WrapperParameter requestWrapper = new WrapperParameter(javaMethod, new TypeInfo(reqElementName, WrapperComposite.class, new Annotation[0]), WebParam.Mode.IN, 0);
        requestWrapper.setInBinding(ParameterBinding.BODY);
        javaMethod.addParameter(requestWrapper);
        WrapperParameter responseWrapper = null;
        if (!isOneway) {
            responseWrapper = new WrapperParameter(javaMethod, new TypeInfo(resElementName, WrapperComposite.class, new Annotation[0]), WebParam.Mode.OUT, -1);
            responseWrapper.setOutBinding(ParameterBinding.BODY);
            javaMethod.addParameter(responseWrapper);
        }
        Class returnType = method.getReturnType();
        String resultName = RETURN;
        String resultTNS = this.targetNamespace;
        String resultPartName = resultName;
        boolean isResultHeader = false;
        WebResult webResult = (WebResult) getAnnotation(method, WebResult.class);
        if (webResult != null) {
            boolean isResultHeader2 = webResult.header();
            if (webResult.name().length() > 0) {
                resultName = webResult.name();
            }
            if (webResult.partName().length() > 0) {
                resultPartName = webResult.partName();
                if (!isResultHeader2) {
                    resultName = resultPartName;
                }
            } else {
                resultPartName = resultName;
            }
            if (webResult.targetNamespace().length() > 0) {
                resultTNS = webResult.targetNamespace();
            }
            isResultHeader = webResult.header();
        }
        if (isResultHeader) {
            resultQName = new QName(resultTNS, resultName);
        } else {
            resultQName = new QName(resultName);
        }
        if (javaMethod.isAsync()) {
            returnType = getAsyncReturnType(method, returnType);
        }
        if (!isOneway && returnType != null && returnType != Void.TYPE) {
            Annotation[] rann = getAnnotations(method);
            TypeInfo rTypeReference = new TypeInfo(resultQName, returnType, rann);
            this.metadataReader.getProperties(rTypeReference.properties(), method);
            rTypeReference.setGenericType(method.getGenericReturnType());
            ParameterImpl returnParameter = new ParameterImpl(javaMethod, rTypeReference, WebParam.Mode.OUT, -1);
            returnParameter.setPartName(resultPartName);
            if (isResultHeader) {
                returnParameter.setBinding(ParameterBinding.HEADER);
                javaMethod.addParameter(returnParameter);
                rTypeReference.setGlobalElement(true);
            } else {
                ParameterBinding rb = getBinding(operationName, resultPartName, false, WebParam.Mode.OUT);
                returnParameter.setBinding(rb);
                if (rb.isBody()) {
                    rTypeReference.setGlobalElement(false);
                    WSDLPart p2 = getPart(new QName(this.targetNamespace, operationName), resultPartName, WebParam.Mode.OUT);
                    if (p2 == null) {
                        resRpcParams.put(Integer.valueOf(resRpcParams.size() + 10000), returnParameter);
                    } else {
                        resRpcParams.put(Integer.valueOf(p2.getIndex()), returnParameter);
                    }
                } else {
                    javaMethod.addParameter(returnParameter);
                }
            }
        }
        Class[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] pannotations = getParamAnnotations(method);
        int pos = 0;
        int length = parameterTypes.length;
        for (int i2 = 0; i2 < length; i2++) {
            Class clazzType = parameterTypes[i2];
            String paramName = "";
            String paramNamespace = "";
            String partName = "";
            boolean isHeader = false;
            if (!javaMethod.isAsync() || !AsyncHandler.class.isAssignableFrom(clazzType)) {
                boolean isHolder = HOLDER_CLASS.isAssignableFrom(clazzType);
                if (isHolder && clazzType == Holder.class) {
                    clazzType = (Class) Utils.REFLECTION_NAVIGATOR.erasure(((ParameterizedType) genericParameterTypes[pos]).getActualTypeArguments()[0]);
                }
                WebParam.Mode paramMode = isHolder ? WebParam.Mode.INOUT : WebParam.Mode.IN;
                Annotation[] annotationArr = pannotations[pos];
                int length2 = annotationArr.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length2) {
                        break;
                    }
                    Annotation annotation = annotationArr[i3];
                    if (annotation.annotationType() != WebParam.class) {
                        i3++;
                    } else {
                        WebParam webParam = (WebParam) annotation;
                        paramName = webParam.name();
                        partName = webParam.partName();
                        isHeader = webParam.header();
                        WebParam.Mode mode = webParam.mode();
                        paramNamespace = webParam.targetNamespace();
                        if (isHolder && mode == WebParam.Mode.IN) {
                            mode = WebParam.Mode.INOUT;
                        }
                        paramMode = mode;
                    }
                }
                if (paramName.length() == 0) {
                    paramName = Constants.ELEMNAME_ARG_STRING + pos;
                }
                if (partName.length() == 0) {
                    partName = paramName;
                } else if (!isHeader) {
                    paramName = partName;
                }
                if (partName.length() == 0) {
                    partName = paramName;
                }
                if (!isHeader) {
                    paramQName = new QName("", paramName);
                } else {
                    if (paramNamespace.length() == 0) {
                        paramNamespace = this.targetNamespace;
                    }
                    paramQName = new QName(paramNamespace, paramName);
                }
                TypeInfo typeRef = new TypeInfo(paramQName, clazzType, pannotations[pos]);
                this.metadataReader.getProperties(typeRef.properties(), method, pos);
                typeRef.setGenericType(genericParameterTypes[pos]);
                int i4 = pos;
                pos++;
                ParameterImpl param = new ParameterImpl(javaMethod, typeRef, paramMode, i4);
                param.setPartName(partName);
                if (paramMode == WebParam.Mode.INOUT) {
                    ParameterBinding pb = getBinding(operationName, partName, isHeader, WebParam.Mode.IN);
                    param.setInBinding(pb);
                    ParameterBinding pb2 = getBinding(operationName, partName, isHeader, WebParam.Mode.OUT);
                    param.setOutBinding(pb2);
                } else if (isHeader) {
                    typeRef.setGlobalElement(true);
                    param.setBinding(ParameterBinding.HEADER);
                } else {
                    ParameterBinding pb3 = getBinding(operationName, partName, false, paramMode);
                    param.setBinding(pb3);
                }
                if (param.getInBinding().isBody()) {
                    typeRef.setGlobalElement(false);
                    if (!param.isOUT()) {
                        WSDLPart p3 = getPart(new QName(this.targetNamespace, operationName), partName, WebParam.Mode.IN);
                        if (p3 == null) {
                            reqRpcParams.put(Integer.valueOf(reqRpcParams.size() + 10000), param);
                        } else {
                            reqRpcParams.put(Integer.valueOf(p3.getIndex()), param);
                        }
                    }
                    if (param.isIN()) {
                        continue;
                    } else {
                        if (isOneway) {
                            throw new RuntimeModelerException("runtime.modeler.oneway.operation.no.out.parameters", this.portClass.getCanonicalName(), methodName);
                        }
                        WSDLPart p4 = getPart(new QName(this.targetNamespace, operationName), partName, WebParam.Mode.OUT);
                        if (p4 == null) {
                            resRpcParams.put(Integer.valueOf(resRpcParams.size() + 10000), param);
                        } else {
                            resRpcParams.put(Integer.valueOf(p4.getIndex()), param);
                        }
                    }
                } else {
                    javaMethod.addParameter(param);
                }
            }
        }
        for (ParameterImpl p5 : reqRpcParams.values()) {
            requestWrapper.addWrapperChild(p5);
        }
        for (ParameterImpl p6 : resRpcParams.values()) {
            responseWrapper.addWrapperChild(p6);
        }
        processExceptions(javaMethod, method);
    }

    protected void processExceptions(JavaMethodImpl javaMethod, Method method) throws SecurityException {
        Class exceptionBean;
        Annotation[] anns;
        Action actionAnn = (Action) getAnnotation(method, Action.class);
        FaultAction[] faultActions = new FaultAction[0];
        if (actionAnn != null) {
            faultActions = actionAnn.fault();
        }
        for (Class<?> exception : method.getExceptionTypes()) {
            if (EXCEPTION_CLASS.isAssignableFrom(exception) && !RUNTIME_EXCEPTION_CLASS.isAssignableFrom(exception) && !REMOTE_EXCEPTION_CLASS.isAssignableFrom(exception)) {
                WebFault webFault = (WebFault) getAnnotation(exception, WebFault.class);
                Method faultInfoMethod = getWSDLExceptionFaultInfo(exception);
                ExceptionType exceptionType = ExceptionType.WSDLException;
                String namespace = this.targetNamespace;
                String name = exception.getSimpleName();
                String beanPackage = this.packageName + PD_JAXWS_PACKAGE_PD;
                if (this.packageName.length() == 0) {
                    beanPackage = JAXWS_PACKAGE_PD;
                }
                String className = beanPackage + name + BEAN;
                String messageName = exception.getSimpleName();
                if (webFault != null) {
                    if (webFault.faultBean().length() > 0) {
                        className = webFault.faultBean();
                    }
                    if (webFault.name().length() > 0) {
                        name = webFault.name();
                    }
                    if (webFault.targetNamespace().length() > 0) {
                        namespace = webFault.targetNamespace();
                    }
                    if (webFault.messageName().length() > 0) {
                        messageName = webFault.messageName();
                    }
                }
                if (faultInfoMethod == null) {
                    exceptionBean = getExceptionBeanClass(className, exception, name, namespace);
                    exceptionType = ExceptionType.UserDefined;
                    anns = getAnnotations((Class<?>) exceptionBean);
                } else {
                    exceptionBean = faultInfoMethod.getReturnType();
                    anns = getAnnotations(faultInfoMethod);
                }
                QName faultName = new QName(namespace, name);
                TypeInfo typeRef = new TypeInfo(faultName, exceptionBean, anns);
                CheckedExceptionImpl checkedException = new CheckedExceptionImpl(javaMethod, exception, typeRef, exceptionType);
                checkedException.setMessageName(messageName);
                FaultAction[] faultActionArr = faultActions;
                int length = faultActionArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    FaultAction fa = faultActionArr[i2];
                    if (!fa.className().equals(exception) || fa.value().equals("")) {
                        i2++;
                    } else {
                        checkedException.setFaultAction(fa.value());
                        break;
                    }
                }
                javaMethod.addException(checkedException);
            }
        }
    }

    protected Method getWSDLExceptionFaultInfo(Class exception) {
        if (getAnnotation((Class<?>) exception, WebFault.class) == null) {
            return null;
        }
        try {
            return exception.getMethod("getFaultInfo", new Class[0]);
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    protected void processDocBareMethod(JavaMethodImpl javaMethod, String operationName, Method method) throws SecurityException {
        String resultName = operationName + RESPONSE;
        String resultTNS = this.targetNamespace;
        String resultPartName = null;
        boolean isResultHeader = false;
        WebResult webResult = (WebResult) getAnnotation(method, WebResult.class);
        if (webResult != null) {
            if (webResult.name().length() > 0) {
                resultName = webResult.name();
            }
            if (webResult.targetNamespace().length() > 0) {
                resultTNS = webResult.targetNamespace();
            }
            resultPartName = webResult.partName();
            isResultHeader = webResult.header();
        }
        Class returnType = method.getReturnType();
        Type gReturnType = method.getGenericReturnType();
        if (javaMethod.isAsync()) {
            returnType = getAsyncReturnType(method, returnType);
        }
        if (returnType != null && !returnType.getName().equals("void")) {
            Annotation[] rann = getAnnotations(method);
            if (resultName != null) {
                QName responseQName = new QName(resultTNS, resultName);
                TypeInfo rTypeReference = new TypeInfo(responseQName, returnType, rann);
                rTypeReference.setGenericType(gReturnType);
                this.metadataReader.getProperties(rTypeReference.properties(), method);
                ParameterImpl returnParameter = new ParameterImpl(javaMethod, rTypeReference, WebParam.Mode.OUT, -1);
                if (resultPartName == null || resultPartName.length() == 0) {
                    resultPartName = resultName;
                }
                returnParameter.setPartName(resultPartName);
                if (isResultHeader) {
                    returnParameter.setBinding(ParameterBinding.HEADER);
                } else {
                    ParameterBinding rb = getBinding(operationName, resultPartName, false, WebParam.Mode.OUT);
                    returnParameter.setBinding(rb);
                }
                javaMethod.addParameter(returnParameter);
            }
        }
        Class[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] pannotations = getParamAnnotations(method);
        int pos = 0;
        int length = parameterTypes.length;
        for (int i2 = 0; i2 < length; i2++) {
            Class clazzType = parameterTypes[i2];
            String paramName = operationName;
            String partName = null;
            String requestNamespace = this.targetNamespace;
            boolean isHeader = false;
            if (!javaMethod.isAsync() || !AsyncHandler.class.isAssignableFrom(clazzType)) {
                boolean isHolder = HOLDER_CLASS.isAssignableFrom(clazzType);
                if (isHolder && clazzType == Holder.class) {
                    clazzType = (Class) Utils.REFLECTION_NAVIGATOR.erasure(((ParameterizedType) genericParameterTypes[pos]).getActualTypeArguments()[0]);
                }
                WebParam.Mode paramMode = isHolder ? WebParam.Mode.INOUT : WebParam.Mode.IN;
                Annotation[] annotationArr = pannotations[pos];
                int length2 = annotationArr.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length2) {
                        break;
                    }
                    Annotation annotation = annotationArr[i3];
                    if (annotation.annotationType() != WebParam.class) {
                        i3++;
                    } else {
                        WebParam webParam = (WebParam) annotation;
                        paramMode = webParam.mode();
                        if (isHolder && paramMode == WebParam.Mode.IN) {
                            paramMode = WebParam.Mode.INOUT;
                        }
                        isHeader = webParam.header();
                        if (isHeader) {
                            paramName = Constants.ELEMNAME_ARG_STRING + pos;
                        }
                        if (paramMode == WebParam.Mode.OUT && !isHeader) {
                            paramName = operationName + RESPONSE;
                        }
                        if (webParam.name().length() > 0) {
                            paramName = webParam.name();
                        }
                        partName = webParam.partName();
                        if (!webParam.targetNamespace().equals("")) {
                            requestNamespace = webParam.targetNamespace();
                        }
                    }
                }
                QName requestQName = new QName(requestNamespace, paramName);
                if (!isHeader && paramMode != WebParam.Mode.OUT) {
                    javaMethod.setRequestPayloadName(requestQName);
                }
                TypeInfo typeRef = new TypeInfo(requestQName, clazzType, pannotations[pos]);
                this.metadataReader.getProperties(typeRef.properties(), method, pos);
                typeRef.setGenericType(genericParameterTypes[pos]);
                int i4 = pos;
                pos++;
                ParameterImpl param = new ParameterImpl(javaMethod, typeRef, paramMode, i4);
                if (partName == null || partName.length() == 0) {
                    partName = paramName;
                }
                param.setPartName(partName);
                if (paramMode == WebParam.Mode.INOUT) {
                    ParameterBinding pb = getBinding(operationName, partName, isHeader, WebParam.Mode.IN);
                    param.setInBinding(pb);
                    ParameterBinding pb2 = getBinding(operationName, partName, isHeader, WebParam.Mode.OUT);
                    param.setOutBinding(pb2);
                } else if (isHeader) {
                    param.setBinding(ParameterBinding.HEADER);
                } else {
                    ParameterBinding pb3 = getBinding(operationName, partName, false, paramMode);
                    param.setBinding(pb3);
                }
                javaMethod.addParameter(param);
            }
        }
        validateDocBare(javaMethod);
        processExceptions(javaMethod, method);
    }

    private void validateDocBare(JavaMethodImpl javaMethod) {
        int numInBodyBindings = 0;
        for (Parameter param : javaMethod.getRequestParameters()) {
            if (param.getBinding().equals(ParameterBinding.BODY) && param.isIN()) {
                numInBodyBindings++;
            }
            if (numInBodyBindings > 1) {
                throw new RuntimeModelerException(ModelerMessages.localizableNOT_A_VALID_BARE_METHOD(this.portClass.getName(), javaMethod.getMethod().getName()));
            }
        }
        int numOutBodyBindings = 0;
        for (Parameter param2 : javaMethod.getResponseParameters()) {
            if (param2.getBinding().equals(ParameterBinding.BODY) && param2.isOUT()) {
                numOutBodyBindings++;
            }
            if (numOutBodyBindings > 1) {
                throw new RuntimeModelerException(ModelerMessages.localizableNOT_A_VALID_BARE_METHOD(this.portClass.getName(), javaMethod.getMethod().getName()));
            }
        }
    }

    private Class getAsyncReturnType(Method method, Class returnType) {
        if (Response.class.isAssignableFrom(returnType)) {
            Type ret = method.getGenericReturnType();
            return (Class) Utils.REFLECTION_NAVIGATOR.erasure(((ParameterizedType) ret).getActualTypeArguments()[0]);
        }
        Type[] types = method.getGenericParameterTypes();
        Class[] params = method.getParameterTypes();
        int i2 = 0;
        for (Class cls : params) {
            if (AsyncHandler.class.isAssignableFrom(cls)) {
                return (Class) Utils.REFLECTION_NAVIGATOR.erasure(((ParameterizedType) types[i2]).getActualTypeArguments()[0]);
            }
            i2++;
        }
        return returnType;
    }

    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static QName getServiceName(Class<?> implClass) {
        return getServiceName(implClass, (MetadataReader) null);
    }

    public static QName getServiceName(Class<?> implClass, boolean isStandard) {
        return getServiceName(implClass, null, isStandard);
    }

    public static QName getServiceName(Class<?> implClass, MetadataReader reader) {
        return getServiceName(implClass, reader, true);
    }

    public static QName getServiceName(Class<?> implClass, MetadataReader reader, boolean isStandard) {
        if (implClass.isInterface()) {
            throw new RuntimeModelerException("runtime.modeler.cannot.get.serviceName.from.interface", implClass.getCanonicalName());
        }
        String name = implClass.getSimpleName() + SERVICE;
        String packageName = "";
        if (implClass.getPackage() != null) {
            packageName = implClass.getPackage().getName();
        }
        WebService webService = (WebService) getAnnotation(WebService.class, implClass, reader);
        if (isStandard && webService == null) {
            throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", implClass.getCanonicalName());
        }
        if (webService != null && webService.serviceName().length() > 0) {
            name = webService.serviceName();
        }
        String targetNamespace = getNamespace(packageName);
        if (webService != null && webService.targetNamespace().length() > 0) {
            targetNamespace = webService.targetNamespace();
        } else if (targetNamespace == null) {
            throw new RuntimeModelerException("runtime.modeler.no.package", implClass.getName());
        }
        return new QName(targetNamespace, name);
    }

    public static QName getPortName(Class<?> implClass, String targetNamespace) {
        return getPortName(implClass, (MetadataReader) null, targetNamespace);
    }

    public static QName getPortName(Class<?> implClass, String targetNamespace, boolean isStandard) {
        return getPortName(implClass, null, targetNamespace, isStandard);
    }

    public static QName getPortName(Class<?> implClass, MetadataReader reader, String targetNamespace) {
        return getPortName(implClass, reader, targetNamespace, true);
    }

    public static QName getPortName(Class<?> implClass, MetadataReader reader, String targetNamespace, boolean isStandard) {
        String name;
        WebService webService = (WebService) getAnnotation(WebService.class, implClass, reader);
        if (isStandard && webService == null) {
            throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", implClass.getCanonicalName());
        }
        if (webService != null && webService.portName().length() > 0) {
            name = webService.portName();
        } else if (webService != null && webService.name().length() > 0) {
            name = webService.name() + PORT;
        } else {
            name = implClass.getSimpleName() + PORT;
        }
        if (targetNamespace == null) {
            if (webService != null && webService.targetNamespace().length() > 0) {
                targetNamespace = webService.targetNamespace();
            } else {
                String packageName = null;
                if (implClass.getPackage() != null) {
                    packageName = implClass.getPackage().getName();
                }
                if (packageName != null) {
                    targetNamespace = getNamespace(packageName);
                }
                if (targetNamespace == null) {
                    throw new RuntimeModelerException("runtime.modeler.no.package", implClass.getName());
                }
            }
        }
        return new QName(targetNamespace, name);
    }

    static <A extends Annotation> A getAnnotation(Class<A> cls, Class<?> cls2, MetadataReader metadataReader) {
        return metadataReader == null ? (A) cls2.getAnnotation(cls) : (A) metadataReader.getAnnotation(cls, cls2);
    }

    public static QName getPortTypeName(Class<?> implOrSeiClass) {
        return getPortTypeName(implOrSeiClass, null, null);
    }

    public static QName getPortTypeName(Class<?> implOrSeiClass, MetadataReader metadataReader) {
        return getPortTypeName(implOrSeiClass, null, metadataReader);
    }

    public static QName getPortTypeName(Class<?> implOrSeiClass, String tns, MetadataReader reader) throws SecurityException {
        if (!$assertionsDisabled && implOrSeiClass == null) {
            throw new AssertionError();
        }
        WebService webService = (WebService) getAnnotation(WebService.class, implOrSeiClass, reader);
        Class<?> clazz = implOrSeiClass;
        if (webService == null) {
            throw new RuntimeModelerException("runtime.modeler.no.webservice.annotation", implOrSeiClass.getCanonicalName());
        }
        if (!implOrSeiClass.isInterface()) {
            String epi = webService.endpointInterface();
            if (epi.length() > 0) {
                try {
                    clazz = Thread.currentThread().getContextClassLoader().loadClass(epi);
                    WebService ws = (WebService) getAnnotation(WebService.class, clazz, reader);
                    if (ws == null) {
                        throw new RuntimeModelerException("runtime.modeler.endpoint.interface.no.webservice", webService.endpointInterface());
                    }
                } catch (ClassNotFoundException e2) {
                    throw new RuntimeModelerException("runtime.modeler.class.not.found", epi);
                }
            }
        }
        WebService webService2 = (WebService) getAnnotation(WebService.class, clazz, reader);
        String name = webService2.name();
        if (name.length() == 0) {
            name = clazz.getSimpleName();
        }
        if (tns == null || "".equals(tns.trim())) {
            tns = webService2.targetNamespace();
        }
        if (tns.length() == 0) {
            tns = getNamespace(clazz.getPackage().getName());
        }
        if (tns == null) {
            throw new RuntimeModelerException("runtime.modeler.no.package", clazz.getName());
        }
        return new QName(tns, name);
    }

    private ParameterBinding getBinding(String operation, String part, boolean isHeader, WebParam.Mode mode) {
        if (this.binding == null) {
            if (isHeader) {
                return ParameterBinding.HEADER;
            }
            return ParameterBinding.BODY;
        }
        QName opName = new QName(this.binding.getBinding().getPortType().getName().getNamespaceURI(), operation);
        return this.binding.getBinding().getBinding(opName, part, mode);
    }

    private WSDLPart getPart(QName opName, String partName, WebParam.Mode mode) {
        WSDLBoundOperation bo2;
        if (this.binding != null && (bo2 = this.binding.getBinding().get(opName)) != null) {
            return bo2.getPart(partName, mode);
        }
        return null;
    }

    private static Boolean getBooleanSystemProperty(final String prop) {
        return (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.sun.xml.internal.ws.model.RuntimeModeler.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                String value = System.getProperty(prop);
                return value != null ? Boolean.valueOf(value) : Boolean.FALSE;
            }
        });
    }

    private static QName getReturnQName(Method method, WebResult webResult, XmlElement xmlElem) {
        String webResultName = null;
        if (webResult != null && webResult.name().length() > 0) {
            webResultName = webResult.name();
        }
        String xmlElemName = null;
        if (xmlElem != null && !xmlElem.name().equals("##default")) {
            xmlElemName = xmlElem.name();
        }
        if (xmlElemName != null && webResultName != null && !xmlElemName.equals(webResultName)) {
            throw new RuntimeModelerException("@XmlElement(name)=" + xmlElemName + " and @WebResult(name)=" + webResultName + " are different for method " + ((Object) method), new Object[0]);
        }
        String localPart = RETURN;
        if (webResultName != null) {
            localPart = webResultName;
        } else if (xmlElemName != null) {
            localPart = xmlElemName;
        }
        String webResultNS = null;
        if (webResult != null && webResult.targetNamespace().length() > 0) {
            webResultNS = webResult.targetNamespace();
        }
        String xmlElemNS = null;
        if (xmlElem != null && !xmlElem.namespace().equals("##default")) {
            xmlElemNS = xmlElem.namespace();
        }
        if (xmlElemNS != null && webResultNS != null && !xmlElemNS.equals(webResultNS)) {
            throw new RuntimeModelerException("@XmlElement(namespace)=" + xmlElemNS + " and @WebResult(targetNamespace)=" + webResultNS + " are different for method " + ((Object) method), new Object[0]);
        }
        String ns = "";
        if (webResultNS != null) {
            ns = webResultNS;
        } else if (xmlElemNS != null) {
            ns = xmlElemNS;
        }
        return new QName(ns, localPart);
    }

    private static QName getParameterQName(Method method, WebParam webParam, XmlElement xmlElem, String paramDefault) {
        String webParamName = null;
        if (webParam != null && webParam.name().length() > 0) {
            webParamName = webParam.name();
        }
        String xmlElemName = null;
        if (xmlElem != null && !xmlElem.name().equals("##default")) {
            xmlElemName = xmlElem.name();
        }
        if (xmlElemName != null && webParamName != null && !xmlElemName.equals(webParamName)) {
            throw new RuntimeModelerException("@XmlElement(name)=" + xmlElemName + " and @WebParam(name)=" + webParamName + " are different for method " + ((Object) method), new Object[0]);
        }
        String localPart = paramDefault;
        if (webParamName != null) {
            localPart = webParamName;
        } else if (xmlElemName != null) {
            localPart = xmlElemName;
        }
        String webParamNS = null;
        if (webParam != null && webParam.targetNamespace().length() > 0) {
            webParamNS = webParam.targetNamespace();
        }
        String xmlElemNS = null;
        if (xmlElem != null && !xmlElem.namespace().equals("##default")) {
            xmlElemNS = xmlElem.namespace();
        }
        if (xmlElemNS != null && webParamNS != null && !xmlElemNS.equals(webParamNS)) {
            throw new RuntimeModelerException("@XmlElement(namespace)=" + xmlElemNS + " and @WebParam(targetNamespace)=" + webParamNS + " are different for method " + ((Object) method), new Object[0]);
        }
        String ns = "";
        if (webParamNS != null) {
            ns = webParamNS;
        } else if (xmlElemNS != null) {
            ns = xmlElemNS;
        }
        return new QName(ns, localPart);
    }
}
