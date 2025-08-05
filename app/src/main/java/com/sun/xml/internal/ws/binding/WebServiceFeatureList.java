package com.sun.xml.internal.ws.binding;

import com.oracle.webservices.internal.api.EnvelopeStyleFeature;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.util.Which;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import com.sun.xml.internal.ws.api.FeatureListValidator;
import com.sun.xml.internal.ws.api.FeatureListValidatorAnnotation;
import com.sun.xml.internal.ws.api.ImpliesWebServiceFeature;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.model.RuntimeModelerException;
import com.sun.xml.internal.ws.resources.ModelerMessages;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import javax.xml.ws.RespectBinding;
import javax.xml.ws.RespectBindingFeature;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

/* loaded from: rt.jar:com/sun/xml/internal/ws/binding/WebServiceFeatureList.class */
public final class WebServiceFeatureList extends AbstractMap<Class<? extends WebServiceFeature>, WebServiceFeature> implements WSFeatureList {
    private Map<Class<? extends WebServiceFeature>, WebServiceFeature> wsfeatures;
    private boolean isValidating;

    @Nullable
    private WSDLFeaturedObject parent;
    private static final Logger LOGGER = Logger.getLogger(WebServiceFeatureList.class.getName());

    public static WebServiceFeatureList toList(Iterable<WebServiceFeature> features) {
        if (features instanceof WebServiceFeatureList) {
            return (WebServiceFeatureList) features;
        }
        WebServiceFeatureList w2 = new WebServiceFeatureList();
        if (features != null) {
            w2.addAll(features);
        }
        return w2;
    }

    public WebServiceFeatureList() {
        this.wsfeatures = new HashMap();
        this.isValidating = false;
    }

    public WebServiceFeatureList(@NotNull WebServiceFeature... features) {
        this.wsfeatures = new HashMap();
        this.isValidating = false;
        if (features != null) {
            for (WebServiceFeature f2 : features) {
                addNoValidate(f2);
            }
        }
    }

    public void validate() {
        if (!this.isValidating) {
            this.isValidating = true;
            Iterator<WebServiceFeature> it = iterator();
            while (it.hasNext()) {
                WebServiceFeature ff = it.next();
                validate(ff);
            }
        }
    }

    private void validate(WebServiceFeature feature) {
        FeatureListValidatorAnnotation fva = (FeatureListValidatorAnnotation) feature.getClass().getAnnotation(FeatureListValidatorAnnotation.class);
        if (fva != null) {
            Class<? extends FeatureListValidator> beanClass = fva.bean();
            try {
                FeatureListValidator validator = beanClass.newInstance();
                validator.validate(this);
            } catch (IllegalAccessException e2) {
                throw new WebServiceException(e2);
            } catch (InstantiationException e3) {
                throw new WebServiceException(e3);
            }
        }
    }

    public WebServiceFeatureList(WebServiceFeatureList features) {
        this.wsfeatures = new HashMap();
        this.isValidating = false;
        if (features != null) {
            this.wsfeatures.putAll(features.wsfeatures);
            this.parent = features.parent;
            this.isValidating = features.isValidating;
        }
    }

    public WebServiceFeatureList(@NotNull Class<?> endpointClass) {
        this.wsfeatures = new HashMap();
        this.isValidating = false;
        parseAnnotations(endpointClass);
    }

    public void parseAnnotations(Iterable<Annotation> annIt) throws SecurityException, IllegalArgumentException {
        for (Annotation ann : annIt) {
            WebServiceFeature feature = getFeature(ann);
            if (feature != null) {
                add(feature);
            }
        }
    }

    public static WebServiceFeature getFeature(Annotation a2) throws SecurityException, IllegalArgumentException {
        WebServiceFeature ftr;
        if (!a2.annotationType().isAnnotationPresent(WebServiceFeatureAnnotation.class)) {
            ftr = null;
        } else if (a2 instanceof Addressing) {
            Addressing addAnn = (Addressing) a2;
            try {
                ftr = new AddressingFeature(addAnn.enabled(), addAnn.required(), addAnn.responses());
            } catch (NoSuchMethodError e2) {
                throw new RuntimeModelerException(ModelerMessages.RUNTIME_MODELER_ADDRESSING_RESPONSES_NOSUCHMETHOD(toJar(Which.which(Addressing.class))), new Object[0]);
            }
        } else if (a2 instanceof MTOM) {
            MTOM mtomAnn = (MTOM) a2;
            ftr = new MTOMFeature(mtomAnn.enabled(), mtomAnn.threshold());
        } else if (a2 instanceof RespectBinding) {
            RespectBinding rbAnn = (RespectBinding) a2;
            ftr = new RespectBindingFeature(rbAnn.enabled());
        } else {
            ftr = getWebServiceFeatureBean(a2);
        }
        return ftr;
    }

    public void parseAnnotations(Class<?> endpointClass) {
        for (Annotation a2 : endpointClass.getAnnotations()) {
            WebServiceFeature ftr = getFeature(a2);
            if (ftr != null) {
                if (ftr instanceof MTOMFeature) {
                    BindingID bindingID = BindingID.parse(endpointClass);
                    MTOMFeature bindingMtomSetting = (MTOMFeature) bindingID.createBuiltinFeatureList().get(MTOMFeature.class);
                    if (bindingMtomSetting != null && (bindingMtomSetting.isEnabled() ^ ftr.isEnabled())) {
                        throw new RuntimeModelerException(ModelerMessages.RUNTIME_MODELER_MTOM_CONFLICT(bindingID, Boolean.valueOf(ftr.isEnabled())), new Object[0]);
                    }
                }
                add(ftr);
            }
        }
    }

    private static String toJar(String url) {
        if (!url.startsWith("jar:")) {
            return url;
        }
        String url2 = url.substring(4);
        return url2.substring(0, url2.lastIndexOf(33));
    }

    private static WebServiceFeature getWebServiceFeatureBean(Annotation a2) throws SecurityException, IllegalArgumentException {
        WebServiceFeatureAnnotation wsfa = (WebServiceFeatureAnnotation) a2.annotationType().getAnnotation(WebServiceFeatureAnnotation.class);
        Class<? extends WebServiceFeature> beanClass = wsfa.bean();
        Constructor ftrCtr = null;
        String[] paramNames = null;
        for (Constructor con : beanClass.getConstructors()) {
            FeatureConstructor ftrCtrAnn = (FeatureConstructor) con.getAnnotation(FeatureConstructor.class);
            if (ftrCtrAnn != null) {
                if (ftrCtr == null) {
                    ftrCtr = con;
                    paramNames = ftrCtrAnn.value();
                } else {
                    throw new WebServiceException(ModelerMessages.RUNTIME_MODELER_WSFEATURE_MORETHANONE_FTRCONSTRUCTOR(a2, beanClass));
                }
            }
        }
        if (ftrCtr == null) {
            WebServiceFeature bean = getWebServiceFeatureBeanViaBuilder(a2, beanClass);
            if (bean != null) {
                return bean;
            }
            throw new WebServiceException(ModelerMessages.RUNTIME_MODELER_WSFEATURE_NO_FTRCONSTRUCTOR(a2, beanClass));
        }
        if (ftrCtr.getParameterTypes().length != paramNames.length) {
            throw new WebServiceException(ModelerMessages.RUNTIME_MODELER_WSFEATURE_ILLEGAL_FTRCONSTRUCTOR(a2, beanClass));
        }
        try {
            Object[] params = new Object[paramNames.length];
            for (int i2 = 0; i2 < paramNames.length; i2++) {
                Method m2 = a2.annotationType().getDeclaredMethod(paramNames[i2], new Class[0]);
                params[i2] = m2.invoke(a2, new Object[0]);
            }
            return (WebServiceFeature) ftrCtr.newInstance(params);
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    private static WebServiceFeature getWebServiceFeatureBeanViaBuilder(Annotation annotation, Class<? extends WebServiceFeature> beanClass) throws SecurityException, IllegalArgumentException {
        try {
            Method featureBuilderMethod = beanClass.getDeclaredMethod("builder", new Class[0]);
            Object builder = featureBuilderMethod.invoke(beanClass, new Object[0]);
            Method buildMethod = builder.getClass().getDeclaredMethod("build", new Class[0]);
            for (Method builderMethod : builder.getClass().getDeclaredMethods()) {
                if (!builderMethod.equals(buildMethod)) {
                    String methodName = builderMethod.getName();
                    Method annotationMethod = annotation.annotationType().getDeclaredMethod(methodName, new Class[0]);
                    Object annotationFieldValue = annotationMethod.invoke(annotation, new Object[0]);
                    Object[] arg = {annotationFieldValue};
                    if (!skipDuringOrgJvnetWsToComOracleWebservicesPackageMove(builderMethod, annotationFieldValue)) {
                        builderMethod.invoke(builder, arg);
                    }
                }
            }
            Object result = buildMethod.invoke(builder, new Object[0]);
            if (result instanceof WebServiceFeature) {
                return (WebServiceFeature) result;
            }
            throw new WebServiceException("Not a WebServiceFeature: " + result);
        } catch (IllegalAccessException e2) {
            throw new WebServiceException(e2);
        } catch (NoSuchMethodException e3) {
            return null;
        } catch (InvocationTargetException e4) {
            throw new WebServiceException(e4);
        }
    }

    private static boolean skipDuringOrgJvnetWsToComOracleWebservicesPackageMove(Method builderMethod, Object annotationFieldValue) {
        Class<?> annotationFieldValueClass = annotationFieldValue.getClass();
        if (!annotationFieldValueClass.isEnum()) {
            return false;
        }
        Class<?>[] builderMethodParameterTypes = builderMethod.getParameterTypes();
        if (builderMethodParameterTypes.length != 1) {
            throw new WebServiceException("expected only 1 parameter");
        }
        String builderParameterTypeName = builderMethodParameterTypes[0].getName();
        if (builderParameterTypeName.startsWith("com.oracle.webservices.internal.test.features_annotations_enums.apinew") || !builderParameterTypeName.startsWith("com.oracle.webservices.internal.api")) {
            return false;
        }
        return false;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<WebServiceFeature> iterator() {
        if (this.parent != null) {
            return new MergedFeatures(this.parent.getFeatures());
        }
        return this.wsfeatures.values().iterator();
    }

    @Override // com.sun.xml.internal.ws.api.WSFeatureList
    @NotNull
    public WebServiceFeature[] toArray() {
        if (this.parent != null) {
            return new MergedFeatures(this.parent.getFeatures()).toArray();
        }
        return (WebServiceFeature[]) this.wsfeatures.values().toArray(new WebServiceFeature[0]);
    }

    @Override // com.sun.xml.internal.ws.api.WSFeatureList
    public boolean isEnabled(@NotNull Class<? extends WebServiceFeature> feature) {
        WebServiceFeature ftr = get((Class<WebServiceFeature>) feature);
        return ftr != null && ftr.isEnabled();
    }

    public boolean contains(@NotNull Class<? extends WebServiceFeature> feature) {
        WebServiceFeature ftr = get((Class<WebServiceFeature>) feature);
        return ftr != null;
    }

    @Override // com.sun.xml.internal.ws.api.WSFeatureList
    @Nullable
    public <F extends WebServiceFeature> F get(@NotNull Class<F> cls) {
        F fCast = cls.cast(this.wsfeatures.get(cls));
        if (fCast == null && this.parent != null) {
            return (F) this.parent.getFeatures().get(cls);
        }
        return fCast;
    }

    public void add(@NotNull WebServiceFeature f2) {
        if (addNoValidate(f2) && this.isValidating) {
            validate(f2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean addNoValidate(@NotNull WebServiceFeature webServiceFeature) {
        if (!this.wsfeatures.containsKey(webServiceFeature.getClass())) {
            this.wsfeatures.put(webServiceFeature.getClass(), webServiceFeature);
            if (webServiceFeature instanceof ImpliesWebServiceFeature) {
                ((ImpliesWebServiceFeature) webServiceFeature).implyFeatures(this);
                return true;
            }
            return true;
        }
        return false;
    }

    public void addAll(@NotNull Iterable<WebServiceFeature> list) {
        for (WebServiceFeature f2 : list) {
            add(f2);
        }
    }

    void setMTOMEnabled(boolean b2) {
        this.wsfeatures.put(MTOMFeature.class, new MTOMFeature(b2));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object other) {
        if (!(other instanceof WebServiceFeatureList)) {
            return false;
        }
        WebServiceFeatureList w2 = (WebServiceFeatureList) other;
        return this.wsfeatures.equals(w2.wsfeatures) && this.parent == w2.parent;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        return this.wsfeatures.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.ws.api.WSFeatureList
    public void mergeFeatures(@NotNull Iterable<WebServiceFeature> features, boolean reportConflicts) {
        for (WebServiceFeature wsdlFtr : features) {
            if (get((Class) wsdlFtr.getClass()) == null) {
                add(wsdlFtr);
            } else if (reportConflicts && isEnabled(wsdlFtr.getClass()) != wsdlFtr.isEnabled()) {
                LOGGER.warning(ModelerMessages.RUNTIME_MODELER_FEATURE_CONFLICT(get((Class) wsdlFtr.getClass()), wsdlFtr));
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.WSFeatureList
    public void mergeFeatures(WebServiceFeature[] features, boolean reportConflicts) {
        for (WebServiceFeature wsdlFtr : features) {
            if (get((Class) wsdlFtr.getClass()) == null) {
                add(wsdlFtr);
            } else if (reportConflicts && isEnabled(wsdlFtr.getClass()) != wsdlFtr.isEnabled()) {
                LOGGER.warning(ModelerMessages.RUNTIME_MODELER_FEATURE_CONFLICT(get((Class) wsdlFtr.getClass()), wsdlFtr));
            }
        }
    }

    public void mergeFeatures(@NotNull WSDLPort wsdlPort, boolean honorWsdlRequired, boolean reportConflicts) throws SecurityException {
        if (honorWsdlRequired && !isEnabled(RespectBindingFeature.class)) {
            return;
        }
        if (!honorWsdlRequired) {
            addAll(wsdlPort.getFeatures());
            return;
        }
        for (WebServiceFeature wsdlFtr : wsdlPort.getFeatures()) {
            if (get((Class) wsdlFtr.getClass()) == null) {
                try {
                    Method m2 = wsdlFtr.getClass().getMethod("isRequired", new Class[0]);
                    try {
                        try {
                            boolean required = ((Boolean) m2.invoke(wsdlFtr, new Object[0])).booleanValue();
                            if (required) {
                                add(wsdlFtr);
                            }
                        } catch (InvocationTargetException e2) {
                            throw new WebServiceException(e2);
                        }
                    } catch (IllegalAccessException e3) {
                        throw new WebServiceException(e3);
                    }
                } catch (NoSuchMethodException e4) {
                    add(wsdlFtr);
                }
            } else if (reportConflicts && isEnabled(wsdlFtr.getClass()) != wsdlFtr.isEnabled()) {
                LOGGER.warning(ModelerMessages.RUNTIME_MODELER_FEATURE_CONFLICT(get((Class) wsdlFtr.getClass()), wsdlFtr));
            }
        }
    }

    public void setParentFeaturedObject(@NotNull WSDLFeaturedObject parent) {
        this.parent = parent;
    }

    @Nullable
    public static <F extends WebServiceFeature> F getFeature(@NotNull WebServiceFeature[] webServiceFeatureArr, @NotNull Class<F> cls) {
        for (WebServiceFeature webServiceFeature : webServiceFeatureArr) {
            F f2 = (F) webServiceFeature;
            if (f2.getClass() == cls) {
                return f2;
            }
        }
        return null;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/binding/WebServiceFeatureList$MergedFeatures.class */
    private final class MergedFeatures implements Iterator<WebServiceFeature> {
        private final Stack<WebServiceFeature> features = new Stack<>();

        public MergedFeatures(@NotNull WSFeatureList parent) {
            Iterator it = WebServiceFeatureList.this.wsfeatures.values().iterator();
            while (it.hasNext()) {
                this.features.push((WebServiceFeature) it.next());
            }
            for (WebServiceFeature f2 : parent) {
                if (!WebServiceFeatureList.this.wsfeatures.containsKey(f2.getClass())) {
                    this.features.push(f2);
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.features.empty();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public WebServiceFeature next() {
            if (!this.features.empty()) {
                return this.features.pop();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.features.empty()) {
                this.features.pop();
            }
        }

        public WebServiceFeature[] toArray() {
            return (WebServiceFeature[]) this.features.toArray(new WebServiceFeature[0]);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<Class<? extends WebServiceFeature>, WebServiceFeature>> entrySet() {
        return this.wsfeatures.entrySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public WebServiceFeature put(Class<? extends WebServiceFeature> key, WebServiceFeature value) {
        return this.wsfeatures.put(key, value);
    }

    public static SOAPVersion getSoapVersion(WSFeatureList features) {
        EnvelopeStyleFeature env = (EnvelopeStyleFeature) features.get(EnvelopeStyleFeature.class);
        if (env != null) {
            return SOAPVersion.from(env);
        }
        EnvelopeStyleFeature env2 = (EnvelopeStyleFeature) features.get(EnvelopeStyleFeature.class);
        if (env2 != null) {
            return SOAPVersion.from(env2);
        }
        return null;
    }

    public static boolean isFeatureEnabled(Class<? extends WebServiceFeature> type, WebServiceFeature[] features) {
        WebServiceFeature ftr = getFeature(features, type);
        return ftr != null && ftr.isEnabled();
    }

    public static WebServiceFeature[] toFeatureArray(WSBinding binding) {
        if (!binding.isFeatureEnabled(EnvelopeStyleFeature.class)) {
            WebServiceFeature[] f2 = {binding.getSOAPVersion().toFeature()};
            binding.getFeatures().mergeFeatures(f2, false);
        }
        return binding.getFeatures().toArray();
    }
}
