package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Pool;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.BridgeContext;
import com.sun.xml.internal.bind.api.CompositeStructure;
import com.sun.xml.internal.bind.api.ErrorListener;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.RawAccessor;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
import com.sun.xml.internal.bind.util.Which;
import com.sun.xml.internal.bind.v2.WellKnownNamespace;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
import com.sun.xml.internal.bind.v2.model.core.Adapter;
import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.Ref;
import com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl;
import com.sun.xml.internal.bind.v2.model.impl.RuntimeModelBuilder;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeBuiltinLeafInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeEnumLeafInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationsException;
import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
import com.sun.xml.internal.bind.v2.runtime.property.AttributeProperty;
import com.sun.xml.internal.bind.v2.runtime.property.Property;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator;
import com.sun.xml.internal.bind.v2.util.EditDistance;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import com.sun.xml.internal.bind.v2.util.XmlFactory;
import com.sun.xml.internal.txw2.output.ResultFactory;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/JAXBContextImpl.class */
public final class JAXBContextImpl extends JAXBRIContext {
    private final Map<TypeReference, Bridge> bridges;
    private static DocumentBuilder db;
    private final QNameMap<JaxBeanInfo> rootMap;
    private final HashMap<QName, JaxBeanInfo> typeMap;
    private final Map<Class, JaxBeanInfo> beanInfoMap;
    protected Map<RuntimeTypeInfo, JaxBeanInfo> beanInfos;
    private final Map<Class, Map<QName, ElementBeanInfoImpl>> elements;
    public final Pool<Marshaller> marshallerPool;
    public final Pool<Unmarshaller> unmarshallerPool;
    public NameBuilder nameBuilder;
    public final NameList nameList;
    private final String defaultNsUri;
    private final Class[] classes;
    protected final boolean c14nSupport;
    public final boolean xmlAccessorFactorySupport;
    public final boolean allNillable;
    public final boolean retainPropertyInfo;
    public final boolean supressAccessorWarnings;
    public final boolean improvedXsiTypeHandling;
    public final boolean disableSecurityProcessing;
    private WeakReference<RuntimeTypeInfoSet> typeInfoSetCache;

    @NotNull
    private RuntimeAnnotationReader annotationReader;
    private boolean hasSwaRef;

    @NotNull
    private final Map<Class, Class> subclassReplacements;
    public final boolean fastBoot;
    private Set<XmlNs> xmlNsSet;
    private Encoded[] utf8nameTable;
    private static final Comparator<QName> QNAME_COMPARATOR;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JAXBContextImpl.class.desiredAssertionStatus();
        QNAME_COMPARATOR = new Comparator<QName>() { // from class: com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.6
            @Override // java.util.Comparator
            public int compare(QName lhs, QName rhs) {
                int r2 = lhs.getLocalPart().compareTo(rhs.getLocalPart());
                return r2 != 0 ? r2 : lhs.getNamespaceURI().compareTo(rhs.getNamespaceURI());
            }
        };
    }

    public Set<XmlNs> getXmlNsSet() {
        return this.xmlNsSet;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v37, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo] */
    private JAXBContextImpl(JAXBContextBuilder builder) throws JAXBException {
        boolean fastB;
        InternalBridge bridge;
        this.bridges = new LinkedHashMap();
        this.rootMap = new QNameMap<>();
        this.typeMap = new HashMap<>();
        this.beanInfoMap = new LinkedHashMap();
        this.beanInfos = new LinkedHashMap();
        this.elements = new LinkedHashMap();
        this.marshallerPool = new Pool.Impl<Marshaller>() { // from class: com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.istack.internal.Pool.Impl
            @NotNull
            public Marshaller create() {
                return JAXBContextImpl.this.createMarshaller();
            }
        };
        this.unmarshallerPool = new Pool.Impl<Unmarshaller>() { // from class: com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.istack.internal.Pool.Impl
            @NotNull
            public Unmarshaller create() {
                return JAXBContextImpl.this.createUnmarshaller();
            }
        };
        this.nameBuilder = new NameBuilder();
        this.xmlNsSet = null;
        this.defaultNsUri = builder.defaultNsUri;
        this.retainPropertyInfo = builder.retainPropertyInfo;
        this.annotationReader = builder.annotationReader;
        this.subclassReplacements = builder.subclassReplacements;
        this.c14nSupport = builder.c14nSupport;
        this.classes = builder.classes;
        this.xmlAccessorFactorySupport = builder.xmlAccessorFactorySupport;
        this.allNillable = builder.allNillable;
        this.supressAccessorWarnings = builder.supressAccessorWarnings;
        this.improvedXsiTypeHandling = builder.improvedXsiTypeHandling;
        this.disableSecurityProcessing = builder.disableSecurityProcessing;
        Collection<TypeReference> typeRefs = builder.typeRefs;
        try {
            fastB = Boolean.getBoolean(JAXBContextImpl.class.getName() + ".fastBoot");
        } catch (SecurityException e2) {
            fastB = false;
        }
        this.fastBoot = fastB;
        RuntimeTypeInfoSet typeSet = getTypeInfoSet();
        this.elements.put(null, new LinkedHashMap());
        for (RuntimeBuiltinLeafInfo leaf : RuntimeBuiltinLeafInfoImpl.builtinBeanInfos) {
            LeafBeanInfoImpl<?> bi2 = new LeafBeanInfoImpl<>(this, leaf);
            this.beanInfoMap.put(leaf.getClazz(), bi2);
            for (QName t2 : bi2.getTypeNames()) {
                this.typeMap.put(t2, bi2);
            }
        }
        Iterator<? extends EnumLeafInfo<Type, Class>> it = typeSet.enums().values().iterator();
        while (it.hasNext()) {
            RuntimeEnumLeafInfo e3 = (RuntimeEnumLeafInfo) it.next();
            JaxBeanInfo<?> bi3 = getOrCreate(e3);
            for (QName qn : bi3.getTypeNames()) {
                this.typeMap.put(qn, bi3);
            }
            if (e3.isElement()) {
                this.rootMap.put(e3.getElementName(), (QName) bi3);
            }
        }
        Iterator<? extends ArrayInfo<Type, Class>> it2 = typeSet.arrays().values().iterator();
        while (it2.hasNext()) {
            RuntimeArrayInfo a2 = (RuntimeArrayInfo) it2.next();
            JaxBeanInfo<?> ai2 = getOrCreate(a2);
            for (QName qn2 : ai2.getTypeNames()) {
                this.typeMap.put(qn2, ai2);
            }
        }
        for (Map.Entry<Class, ? extends ClassInfo<Type, Class>> entry : typeSet.beans().entrySet()) {
            ClassBeanInfoImpl<?> bi4 = getOrCreate((RuntimeClassInfo) entry.getValue());
            XmlSchema xs = (XmlSchema) this.annotationReader.getPackageAnnotation(XmlSchema.class, entry.getKey(), null);
            if (xs != null && xs.xmlns() != null && xs.xmlns().length > 0) {
                if (this.xmlNsSet == null) {
                    this.xmlNsSet = new HashSet();
                }
                this.xmlNsSet.addAll(Arrays.asList(xs.xmlns()));
            }
            if (bi4.isElement()) {
                this.rootMap.put(((RuntimeClassInfo) entry.getValue()).getElementName(), (QName) bi4);
            }
            for (QName qn3 : bi4.getTypeNames()) {
                this.typeMap.put(qn3, bi4);
            }
        }
        Iterator<? extends ElementInfo<Type, Class>> it3 = typeSet.getAllElements().iterator();
        while (it3.hasNext()) {
            RuntimeElementInfo n2 = (RuntimeElementInfo) it3.next();
            ElementBeanInfoImpl bi5 = getOrCreate(n2);
            if (n2.getScope2() == null) {
                this.rootMap.put(n2.getElementName(), (QName) bi5);
            }
            ClassInfo<Type, Class> scope = n2.getScope2();
            Class scopeClazz = scope == null ? null : scope.getClazz();
            Map<QName, ElementBeanInfoImpl> m2 = this.elements.get(scopeClazz);
            if (m2 == null) {
                m2 = new LinkedHashMap();
                this.elements.put(scopeClazz, m2);
            }
            m2.put(n2.getElementName(), bi5);
        }
        this.beanInfoMap.put(JAXBElement.class, new ElementBeanInfoImpl(this));
        this.beanInfoMap.put(CompositeStructure.class, new CompositeStructureBeanInfo(this));
        getOrCreate((RuntimeTypeInfo) typeSet.getAnyTypeInfo2());
        for (JaxBeanInfo bi6 : this.beanInfos.values()) {
            bi6.link(this);
        }
        for (Map.Entry<Class, Class> e4 : RuntimeUtil.primitiveToBox.entrySet()) {
            this.beanInfoMap.put(e4.getKey(), this.beanInfoMap.get(e4.getValue()));
        }
        Navigator<Type, Class, Field, Method> navigator = typeSet.getNavigator();
        for (TypeReference tr : typeRefs) {
            XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter) tr.get(XmlJavaTypeAdapter.class);
            Adapter<Type, Class> a3 = null;
            XmlList xl = (XmlList) tr.get(XmlList.class);
            Class erasedType = (Class) navigator.erasure(tr.type);
            a3 = xjta != null ? new Adapter<>(xjta.value(), navigator) : a3;
            if (tr.get(XmlAttachmentRef.class) != null) {
                a3 = new Adapter<>(SwaRefAdapter.class, navigator);
                this.hasSwaRef = true;
            }
            erasedType = a3 != null ? (Class) navigator.erasure(a3.defaultType) : erasedType;
            Name name = this.nameBuilder.createElementName(tr.tagName);
            if (xl == null) {
                bridge = new BridgeImpl(this, name, getBeanInfo(erasedType, true), tr);
            } else {
                bridge = new BridgeImpl(this, name, new ValueListBeanInfoImpl(this, erasedType), tr);
            }
            if (a3 != null) {
                bridge = new BridgeAdapter(bridge, a3.adapterType);
            }
            this.bridges.put(tr, bridge);
        }
        this.nameList = this.nameBuilder.conclude();
        for (JaxBeanInfo bi7 : this.beanInfos.values()) {
            bi7.wrapUp();
        }
        this.nameBuilder = null;
        this.beanInfos = null;
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public boolean hasSwaRef() {
        return this.hasSwaRef;
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public RuntimeTypeInfoSet getRuntimeTypeInfoSet() {
        try {
            return getTypeInfoSet();
        } catch (IllegalAnnotationsException e2) {
            throw new AssertionError(e2);
        }
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet, java.lang.Object] */
    public RuntimeTypeInfoSet getTypeInfoSet() throws IllegalAnnotationsException {
        RuntimeTypeInfoSet r2;
        if (this.typeInfoSetCache != null && (r2 = this.typeInfoSetCache.get()) != null) {
            return r2;
        }
        RuntimeModelBuilder builder = new RuntimeModelBuilder(this, this.annotationReader, this.subclassReplacements, this.defaultNsUri);
        IllegalAnnotationsException.Builder errorHandler = new IllegalAnnotationsException.Builder();
        builder.setErrorHandler(errorHandler);
        for (Class c2 : this.classes) {
            if (c2 != CompositeStructure.class) {
                builder.getTypeInfo(new Ref(c2));
            }
        }
        this.hasSwaRef |= builder.hasSwaRef;
        ?? Link = builder.link2();
        errorHandler.check();
        if (!$assertionsDisabled && Link == 0) {
            throw new AssertionError((Object) "if no error was reported, the link must be a success");
        }
        this.typeInfoSetCache = new WeakReference<>(Link);
        return Link;
    }

    public ElementBeanInfoImpl getElement(Class scope, QName name) {
        ElementBeanInfoImpl bi2;
        Map<QName, ElementBeanInfoImpl> m2 = this.elements.get(scope);
        if (m2 != null && (bi2 = m2.get(name)) != null) {
            return bi2;
        }
        return this.elements.get(null).get(name);
    }

    private ElementBeanInfoImpl getOrCreate(RuntimeElementInfo rei) {
        JaxBeanInfo bi2 = this.beanInfos.get(rei);
        return bi2 != null ? (ElementBeanInfoImpl) bi2 : new ElementBeanInfoImpl(this, rei);
    }

    protected JaxBeanInfo getOrCreate(RuntimeEnumLeafInfo eli) {
        JaxBeanInfo bi2 = this.beanInfos.get(eli);
        if (bi2 != null) {
            return bi2;
        }
        JaxBeanInfo bi3 = new LeafBeanInfoImpl(this, eli);
        this.beanInfoMap.put(bi3.jaxbType, bi3);
        return bi3;
    }

    protected ClassBeanInfoImpl getOrCreate(RuntimeClassInfo ci) {
        ClassBeanInfoImpl bi2 = (ClassBeanInfoImpl) this.beanInfos.get(ci);
        if (bi2 != null) {
            return bi2;
        }
        ClassBeanInfoImpl bi3 = new ClassBeanInfoImpl(this, ci);
        this.beanInfoMap.put(bi3.jaxbType, bi3);
        return bi3;
    }

    protected JaxBeanInfo getOrCreate(RuntimeArrayInfo ai2) {
        JaxBeanInfo abi = this.beanInfos.get(ai2);
        if (abi != null) {
            return abi;
        }
        JaxBeanInfo abi2 = new ArrayBeanInfoImpl(this, ai2);
        this.beanInfoMap.put(ai2.getType2(), abi2);
        return abi2;
    }

    public JaxBeanInfo getOrCreate(RuntimeTypeInfo e2) {
        if (e2 instanceof RuntimeElementInfo) {
            return getOrCreate((RuntimeElementInfo) e2);
        }
        if (e2 instanceof RuntimeClassInfo) {
            return getOrCreate((RuntimeClassInfo) e2);
        }
        if (e2 instanceof RuntimeLeafInfo) {
            JaxBeanInfo bi2 = this.beanInfos.get(e2);
            if ($assertionsDisabled || bi2 != null) {
                return bi2;
            }
            throw new AssertionError();
        }
        if (e2 instanceof RuntimeArrayInfo) {
            return getOrCreate((RuntimeArrayInfo) e2);
        }
        if (e2.getType2() == Object.class) {
            JaxBeanInfo bi3 = this.beanInfoMap.get(Object.class);
            if (bi3 == null) {
                bi3 = new AnyTypeBeanInfo(this, e2);
                this.beanInfoMap.put(Object.class, bi3);
            }
            return bi3;
        }
        throw new IllegalArgumentException();
    }

    public final JaxBeanInfo getBeanInfo(Object o2) {
        Class superclass = o2.getClass();
        while (true) {
            Class c2 = superclass;
            if (c2 != Object.class) {
                JaxBeanInfo bi2 = this.beanInfoMap.get(c2);
                if (bi2 != null) {
                    return bi2;
                }
                superclass = c2.getSuperclass();
            } else {
                if (o2 instanceof Element) {
                    return this.beanInfoMap.get(Object.class);
                }
                for (Class cls : o2.getClass().getInterfaces()) {
                    JaxBeanInfo bi3 = this.beanInfoMap.get(cls);
                    if (bi3 != null) {
                        return bi3;
                    }
                }
                return null;
            }
        }
    }

    public final JaxBeanInfo getBeanInfo(Object o2, boolean fatal) throws JAXBException {
        JaxBeanInfo bi2 = getBeanInfo(o2);
        if (bi2 != null) {
            return bi2;
        }
        if (fatal) {
            if (o2 instanceof Document) {
                throw new JAXBException(Messages.ELEMENT_NEEDED_BUT_FOUND_DOCUMENT.format(o2.getClass()));
            }
            throw new JAXBException(Messages.UNKNOWN_CLASS.format(o2.getClass()));
        }
        return null;
    }

    public final <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz) {
        return this.beanInfoMap.get(clazz);
    }

    public final <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz, boolean fatal) throws JAXBException {
        JaxBeanInfo<T> bi2 = getBeanInfo((Class) clazz);
        if (bi2 != null) {
            return bi2;
        }
        if (fatal) {
            throw new JAXBException(clazz.getName() + " is not known to this context");
        }
        return null;
    }

    public final Loader selectRootLoader(UnmarshallingContext.State state, TagName tag) {
        JaxBeanInfo beanInfo = this.rootMap.get(tag.uri, tag.local);
        if (beanInfo == null) {
            return null;
        }
        return beanInfo.getLoader(this, true);
    }

    public JaxBeanInfo getGlobalType(QName name) {
        return this.typeMap.get(name);
    }

    public String getNearestTypeName(QName name) {
        String[] all = new String[this.typeMap.size()];
        int i2 = 0;
        for (QName qn : this.typeMap.keySet()) {
            if (qn.getLocalPart().equals(name.getLocalPart())) {
                return qn.toString();
            }
            int i3 = i2;
            i2++;
            all[i3] = qn.toString();
        }
        String nearest = EditDistance.findNearest(name.toString(), all);
        if (EditDistance.editDistance(nearest, name.toString()) > 10) {
            return null;
        }
        return nearest;
    }

    public Set<QName> getValidRootNames() {
        Set<QName> r2 = new TreeSet<>(QNAME_COMPARATOR);
        for (QNameMap.Entry e2 : this.rootMap.entrySet()) {
            r2.add(e2.createQName());
        }
        return r2;
    }

    public synchronized Encoded[] getUTF8NameTable() {
        if (this.utf8nameTable == null) {
            Encoded[] x2 = new Encoded[this.nameList.localNames.length];
            for (int i2 = 0; i2 < x2.length; i2++) {
                Encoded e2 = new Encoded(this.nameList.localNames[i2]);
                e2.compact();
                x2[i2] = e2;
            }
            this.utf8nameTable = x2;
        }
        return this.utf8nameTable;
    }

    public int getNumberOfLocalNames() {
        return this.nameList.localNames.length;
    }

    public int getNumberOfElementNames() {
        return this.nameList.numberOfElementNames;
    }

    public int getNumberOfAttributeNames() {
        return this.nameList.numberOfAttributeNames;
    }

    static Transformer createTransformer(boolean disableSecureProcessing) {
        try {
            SAXTransformerFactory tf = (SAXTransformerFactory) XmlFactory.createTransformerFactory(disableSecureProcessing);
            return tf.newTransformer();
        } catch (TransformerConfigurationException e2) {
            throw new Error(e2);
        }
    }

    public static TransformerHandler createTransformerHandler(boolean disableSecureProcessing) {
        try {
            SAXTransformerFactory tf = (SAXTransformerFactory) XmlFactory.createTransformerFactory(disableSecureProcessing);
            return tf.newTransformerHandler();
        } catch (TransformerConfigurationException e2) {
            throw new Error(e2);
        }
    }

    static Document createDom(boolean disableSecurityProcessing) {
        Document documentNewDocument;
        synchronized (JAXBContextImpl.class) {
            if (db == null) {
                try {
                    DocumentBuilderFactory dbf = XmlFactory.createDocumentBuilderFactory(disableSecurityProcessing);
                    db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException e2) {
                    throw new FactoryConfigurationError(e2);
                }
            }
            documentNewDocument = db.newDocument();
        }
        return documentNewDocument;
    }

    @Override // javax.xml.bind.JAXBContext
    public MarshallerImpl createMarshaller() {
        return new MarshallerImpl(this, null);
    }

    @Override // javax.xml.bind.JAXBContext
    public UnmarshallerImpl createUnmarshaller() {
        return new UnmarshallerImpl(this, null);
    }

    @Override // javax.xml.bind.JAXBContext
    public Validator createValidator() {
        throw new UnsupportedOperationException(Messages.NOT_IMPLEMENTED_IN_2_0.format(new Object[0]));
    }

    @Override // javax.xml.bind.JAXBContext
    public JAXBIntrospector createJAXBIntrospector() {
        return new JAXBIntrospector() { // from class: com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.3
            @Override // javax.xml.bind.JAXBIntrospector
            public boolean isElement(Object object) {
                return getElementName(object) != null;
            }

            @Override // javax.xml.bind.JAXBIntrospector
            public QName getElementName(Object jaxbElement) {
                try {
                    return JAXBContextImpl.this.getElementName(jaxbElement);
                } catch (JAXBException e2) {
                    return null;
                }
            }
        };
    }

    private NonElement<Type, Class> getXmlType(RuntimeTypeInfoSet tis, TypeReference tr) {
        if (tr == null) {
            throw new IllegalArgumentException();
        }
        XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter) tr.get(XmlJavaTypeAdapter.class);
        XmlList xl = (XmlList) tr.get(XmlList.class);
        Ref<Type, Class> ref = new Ref<>(this.annotationReader, tis.getNavigator(), tr.type, xjta, xl);
        return tis.getTypeInfo((Ref) ref);
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public void generateEpisode(Result output) {
        if (output == null) {
            throw new IllegalArgumentException();
        }
        createSchemaGenerator().writeEpisodeFile(ResultFactory.createSerializer(output));
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext, javax.xml.bind.JAXBContext
    public void generateSchema(SchemaOutputResolver outputResolver) throws IOException {
        if (outputResolver == null) {
            throw new IOException(Messages.NULL_OUTPUT_RESOLVER.format(new Object[0]));
        }
        final SAXParseException[] e2 = new SAXParseException[1];
        final SAXParseException[] w2 = new SAXParseException[1];
        createSchemaGenerator().write(outputResolver, new ErrorListener() { // from class: com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.4
            @Override // com.sun.xml.internal.bind.api.ErrorListener, org.xml.sax.ErrorHandler
            public void error(SAXParseException exception) {
                e2[0] = exception;
            }

            @Override // com.sun.xml.internal.bind.api.ErrorListener, org.xml.sax.ErrorHandler
            public void fatalError(SAXParseException exception) {
                e2[0] = exception;
            }

            @Override // com.sun.xml.internal.bind.api.ErrorListener, org.xml.sax.ErrorHandler
            public void warning(SAXParseException exception) {
                w2[0] = exception;
            }

            @Override // com.sun.xml.internal.bind.api.ErrorListener
            public void info(SAXParseException exception) {
            }
        });
        if (e2[0] != null) {
            IOException x2 = new IOException(Messages.FAILED_TO_GENERATE_SCHEMA.format(new Object[0]));
            x2.initCause(e2[0]);
            throw x2;
        }
        if (w2[0] != null) {
            IOException x3 = new IOException(Messages.ERROR_PROCESSING_SCHEMA.format(new Object[0]));
            x3.initCause(w2[0]);
            throw x3;
        }
    }

    private XmlSchemaGenerator<Type, Class, Field, Method> createSchemaGenerator() {
        try {
            RuntimeTypeInfoSet tis = getTypeInfoSet();
            XmlSchemaGenerator<Type, Class, Field, Method> xsdgen = new XmlSchemaGenerator<>(tis.getNavigator(), tis);
            Set<QName> rootTagNames = new HashSet<>();
            Iterator<? extends ElementInfo<Type, Class>> it = tis.getAllElements().iterator();
            while (it.hasNext()) {
                RuntimeElementInfo ei = (RuntimeElementInfo) it.next();
                rootTagNames.add(ei.getElementName());
            }
            Iterator<? extends ClassInfo<Type, Class>> it2 = tis.beans().values().iterator();
            while (it2.hasNext()) {
                RuntimeClassInfo ci = (RuntimeClassInfo) it2.next();
                if (ci.isElement()) {
                    rootTagNames.add(ci.asElement().getElementName());
                }
            }
            for (TypeReference tr : this.bridges.keySet()) {
                if (!rootTagNames.contains(tr.tagName)) {
                    if (tr.type == Void.TYPE || tr.type == Void.class) {
                        xsdgen.add(tr.tagName, false, null);
                    } else if (tr.type != CompositeStructure.class) {
                        NonElement<Type, Class> typeInfo = getXmlType(tis, tr);
                        xsdgen.add(tr.tagName, !tis.getNavigator().isPrimitive(tr.type), typeInfo);
                    }
                }
            }
            return xsdgen;
        } catch (IllegalAnnotationsException e2) {
            throw new AssertionError(e2);
        }
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public QName getTypeName(TypeReference tr) {
        try {
            NonElement<Type, Class> xt = getXmlType(getTypeInfoSet(), tr);
            if (xt == null) {
                throw new IllegalArgumentException();
            }
            return xt.getTypeName();
        } catch (IllegalAnnotationsException e2) {
            throw new AssertionError(e2);
        }
    }

    @Override // javax.xml.bind.JAXBContext
    public <T> Binder<T> createBinder(Class<T> cls) {
        if (cls == Node.class) {
            return (Binder<T>) createBinder();
        }
        return super.createBinder(cls);
    }

    @Override // javax.xml.bind.JAXBContext
    public Binder<Node> createBinder() {
        return new BinderImpl(this, new DOMScanner());
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public QName getElementName(Object o2) throws JAXBException {
        JaxBeanInfo bi2 = getBeanInfo(o2, true);
        if (!bi2.isElement()) {
            return null;
        }
        return new QName(bi2.getElementNamespaceURI(o2), bi2.getElementLocalName(o2));
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public QName getElementName(Class o2) throws JAXBException {
        JaxBeanInfo bi2 = getBeanInfo(o2, true);
        if (!bi2.isElement()) {
            return null;
        }
        return new QName(bi2.getElementNamespaceURI(o2), bi2.getElementLocalName(o2));
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public Bridge createBridge(TypeReference ref) {
        return this.bridges.get(ref);
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    @NotNull
    public BridgeContext createBridgeContext() {
        return new BridgeContextImpl(this);
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public RawAccessor getElementPropertyAccessor(Class wrapperBean, String nsUri, String localName) throws JAXBException {
        JaxBeanInfo bi2 = getBeanInfo(wrapperBean, true);
        if (!(bi2 instanceof ClassBeanInfoImpl)) {
            throw new JAXBException(((Object) wrapperBean) + " is not a bean");
        }
        ClassBeanInfoImpl classBeanInfoImpl = (ClassBeanInfoImpl) bi2;
        while (true) {
            ClassBeanInfoImpl cb = classBeanInfoImpl;
            if (cb != null) {
                for (Property p2 : cb.properties) {
                    final Accessor acc = p2.getElementPropertyAccessor(nsUri, localName);
                    if (acc != null) {
                        return new RawAccessor() { // from class: com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.5
                            @Override // com.sun.xml.internal.bind.api.RawAccessor
                            public Object get(Object bean) throws AccessorException {
                                return acc.getUnadapted(bean);
                            }

                            @Override // com.sun.xml.internal.bind.api.RawAccessor
                            public void set(Object bean, Object value) throws AccessorException {
                                acc.setUnadapted(bean, value);
                            }
                        };
                    }
                }
                classBeanInfoImpl = cb.superClazz;
            } else {
                throw new JAXBException(((Object) new QName(nsUri, localName)) + " is not a valid property on " + ((Object) wrapperBean));
            }
        }
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public List<String> getKnownNamespaceURIs() {
        return Arrays.asList(this.nameList.namespaceURIs);
    }

    @Override // com.sun.xml.internal.bind.api.JAXBRIContext
    public String getBuildId() {
        Package pkg = getClass().getPackage();
        if (pkg == null) {
            return null;
        }
        return pkg.getImplementationVersion();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(Which.which(getClass()) + " Build-Id: " + getBuildId());
        buf.append("\nClasses known to this context:\n");
        Set<String> names = new TreeSet<>();
        for (Class key : this.beanInfoMap.keySet()) {
            names.add(key.getName());
        }
        for (String name : names) {
            buf.append(Constants.INDENT).append(name).append('\n');
        }
        return buf.toString();
    }

    public String getXMIMEContentType(Object o2) {
        JaxBeanInfo bi2 = getBeanInfo(o2);
        if (!(bi2 instanceof ClassBeanInfoImpl)) {
            return null;
        }
        ClassBeanInfoImpl cb = (ClassBeanInfoImpl) bi2;
        for (Property p2 : cb.properties) {
            if (p2 instanceof AttributeProperty) {
                AttributeProperty ap2 = (AttributeProperty) p2;
                if (ap2.attName.equals(WellKnownNamespace.XML_MIME_URI, "contentType")) {
                    try {
                        return (String) ap2.xacc.print(o2);
                    } catch (AccessorException e2) {
                        return null;
                    } catch (ClassCastException e3) {
                        return null;
                    } catch (SAXException e4) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public JAXBContextImpl createAugmented(Class<?> clazz) throws JAXBException {
        Class[] newList = new Class[this.classes.length + 1];
        System.arraycopy(this.classes, 0, newList, 0, this.classes.length);
        newList[this.classes.length] = clazz;
        JAXBContextBuilder builder = new JAXBContextBuilder(this);
        builder.setClasses(newList);
        return builder.build();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/JAXBContextImpl$JAXBContextBuilder.class */
    public static class JAXBContextBuilder {
        private boolean retainPropertyInfo;
        private boolean supressAccessorWarnings;
        private String defaultNsUri;

        @NotNull
        private RuntimeAnnotationReader annotationReader;

        @NotNull
        private Map<Class, Class> subclassReplacements;
        private boolean c14nSupport;
        private Class[] classes;
        private Collection<TypeReference> typeRefs;
        private boolean xmlAccessorFactorySupport;
        private boolean allNillable;
        private boolean improvedXsiTypeHandling;
        private boolean disableSecurityProcessing;

        public JAXBContextBuilder() {
            this.retainPropertyInfo = false;
            this.supressAccessorWarnings = false;
            this.defaultNsUri = "";
            this.annotationReader = new RuntimeInlineAnnotationReader();
            this.subclassReplacements = Collections.emptyMap();
            this.c14nSupport = false;
            this.xmlAccessorFactorySupport = false;
            this.improvedXsiTypeHandling = true;
            this.disableSecurityProcessing = true;
        }

        public JAXBContextBuilder(JAXBContextImpl baseImpl) {
            this.retainPropertyInfo = false;
            this.supressAccessorWarnings = false;
            this.defaultNsUri = "";
            this.annotationReader = new RuntimeInlineAnnotationReader();
            this.subclassReplacements = Collections.emptyMap();
            this.c14nSupport = false;
            this.xmlAccessorFactorySupport = false;
            this.improvedXsiTypeHandling = true;
            this.disableSecurityProcessing = true;
            this.supressAccessorWarnings = baseImpl.supressAccessorWarnings;
            this.retainPropertyInfo = baseImpl.retainPropertyInfo;
            this.defaultNsUri = baseImpl.defaultNsUri;
            this.annotationReader = baseImpl.annotationReader;
            this.subclassReplacements = baseImpl.subclassReplacements;
            this.c14nSupport = baseImpl.c14nSupport;
            this.classes = baseImpl.classes;
            this.typeRefs = baseImpl.bridges.keySet();
            this.xmlAccessorFactorySupport = baseImpl.xmlAccessorFactorySupport;
            this.allNillable = baseImpl.allNillable;
            this.disableSecurityProcessing = baseImpl.disableSecurityProcessing;
        }

        public JAXBContextBuilder setRetainPropertyInfo(boolean val) {
            this.retainPropertyInfo = val;
            return this;
        }

        public JAXBContextBuilder setSupressAccessorWarnings(boolean val) {
            this.supressAccessorWarnings = val;
            return this;
        }

        public JAXBContextBuilder setC14NSupport(boolean val) {
            this.c14nSupport = val;
            return this;
        }

        public JAXBContextBuilder setXmlAccessorFactorySupport(boolean val) {
            this.xmlAccessorFactorySupport = val;
            return this;
        }

        public JAXBContextBuilder setDefaultNsUri(String val) {
            this.defaultNsUri = val;
            return this;
        }

        public JAXBContextBuilder setAllNillable(boolean val) {
            this.allNillable = val;
            return this;
        }

        public JAXBContextBuilder setClasses(Class[] val) {
            this.classes = val;
            return this;
        }

        public JAXBContextBuilder setAnnotationReader(RuntimeAnnotationReader val) {
            this.annotationReader = val;
            return this;
        }

        public JAXBContextBuilder setSubclassReplacements(Map<Class, Class> val) {
            this.subclassReplacements = val;
            return this;
        }

        public JAXBContextBuilder setTypeRefs(Collection<TypeReference> val) {
            this.typeRefs = val;
            return this;
        }

        public JAXBContextBuilder setImprovedXsiTypeHandling(boolean val) {
            this.improvedXsiTypeHandling = val;
            return this;
        }

        public JAXBContextBuilder setDisableSecurityProcessing(boolean val) {
            this.disableSecurityProcessing = val;
            return this;
        }

        public JAXBContextImpl build() throws JAXBException {
            if (this.defaultNsUri == null) {
                this.defaultNsUri = "";
            }
            if (this.subclassReplacements == null) {
                this.subclassReplacements = Collections.emptyMap();
            }
            if (this.annotationReader == null) {
                this.annotationReader = new RuntimeInlineAnnotationReader();
            }
            if (this.typeRefs == null) {
                this.typeRefs = Collections.emptyList();
            }
            return new JAXBContextImpl(this);
        }
    }
}
