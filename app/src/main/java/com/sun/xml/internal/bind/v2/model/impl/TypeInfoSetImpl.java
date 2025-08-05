package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.core.BuiltinLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.LeafInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.Ref;
import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
import com.sun.xml.internal.bind.v2.util.FlattenIterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/TypeInfoSetImpl.class */
class TypeInfoSetImpl<T, C, F, M> implements TypeInfoSet<T, C, F, M> {

    @XmlTransient
    public final Navigator<T, C, F, M> nav;

    @XmlTransient
    public final AnnotationReader<T, C, F, M> reader;
    private final Map<T, BuiltinLeafInfo<T, C>> builtins = new LinkedHashMap();
    private final Map<C, EnumLeafInfoImpl<T, C, F, M>> enums = new LinkedHashMap();
    private final Map<T, ArrayInfoImpl<T, C, F, M>> arrays = new LinkedHashMap();

    @XmlJavaTypeAdapter(RuntimeUtil.ToStringAdapter.class)
    private final Map<C, ClassInfoImpl<T, C, F, M>> beans = new LinkedHashMap();

    @XmlTransient
    private final Map<C, ClassInfoImpl<T, C, F, M>> beansView = Collections.unmodifiableMap(this.beans);
    private final Map<C, Map<QName, ElementInfoImpl<T, C, F, M>>> elementMappings = new LinkedHashMap();
    private final Iterable<? extends ElementInfoImpl<T, C, F, M>> allElements = new Iterable<ElementInfoImpl<T, C, F, M>>() { // from class: com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl.1
        @Override // java.lang.Iterable, java.util.List
        public Iterator<ElementInfoImpl<T, C, F, M>> iterator() {
            return new FlattenIterator(TypeInfoSetImpl.this.elementMappings.values());
        }
    };
    private final NonElement<T, C> anyType;
    private Map<String, Map<String, String>> xmlNsCache;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public /* bridge */ /* synthetic */ ElementInfo getElementInfo(Object obj, QName qName) {
        return getElementInfo((TypeInfoSetImpl<T, C, F, M>) obj, qName);
    }

    static {
        $assertionsDisabled = !TypeInfoSetImpl.class.desiredAssertionStatus();
    }

    public TypeInfoSetImpl(Navigator<T, C, F, M> nav, AnnotationReader<T, C, F, M> reader, Map<T, ? extends BuiltinLeafInfoImpl<T, C>> leaves) {
        this.nav = nav;
        this.reader = reader;
        this.builtins.putAll(leaves);
        this.anyType = createAnyType2();
        for (Map.Entry<Class, Class> e2 : RuntimeUtil.primitiveToBox.entrySet()) {
            this.builtins.put(nav.getPrimitive(e2.getKey()), leaves.get(nav.ref(e2.getValue())));
        }
        this.elementMappings.put(null, new LinkedHashMap());
    }

    /* renamed from: createAnyType */
    protected NonElement<T, C> createAnyType2() {
        return new AnyTypeImpl(this.nav);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
    public Navigator<T, C, F, M> getNavigator() {
        return this.nav;
    }

    public void add(ClassInfoImpl<T, C, F, M> ci) {
        this.beans.put(ci.getClazz(), ci);
    }

    public void add(EnumLeafInfoImpl<T, C, F, M> li) {
        this.enums.put(li.clazz, li);
    }

    public void add(ArrayInfoImpl<T, C, F, M> ai2) {
        this.arrays.put(ai2.getType2(), ai2);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public NonElement<T, C> getTypeInfo(T type) {
        T type2 = this.nav.erasure(type);
        LeafInfo<T, C> l2 = this.builtins.get(type2);
        if (l2 != null) {
            return l2;
        }
        if (this.nav.isArray(type2)) {
            return this.arrays.get(type2);
        }
        C d2 = this.nav.asDecl((Navigator<T, C, F, M>) type2);
        if (d2 == null) {
            return null;
        }
        return getClassInfo(d2);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    /* renamed from: getAnyTypeInfo */
    public NonElement<T, C> getAnyTypeInfo2() {
        return this.anyType;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
    public NonElement<T, C> getTypeInfo(Ref<T, C> ref) {
        if (!$assertionsDisabled && ref.valueList) {
            throw new AssertionError();
        }
        C c2 = this.nav.asDecl((Navigator<T, C, F, M>) ref.type);
        if (c2 != null && this.reader.getClassAnnotation(XmlRegistry.class, c2, null) != null) {
            return null;
        }
        return getTypeInfo((TypeInfoSetImpl<T, C, F, M>) ref.type);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<C, ? extends ClassInfoImpl<T, C, F, M>> beans() {
        return this.beansView;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<T, ? extends BuiltinLeafInfo<T, C>> builtins() {
        return this.builtins;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<C, ? extends EnumLeafInfoImpl<T, C, F, M>> enums() {
        return this.enums;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<? extends T, ? extends ArrayInfoImpl<T, C, F, M>> arrays() {
        return this.arrays;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public NonElement<T, C> getClassInfo(C type) {
        LeafInfo<T, C> l2 = this.builtins.get(this.nav.use(type));
        if (l2 != null) {
            return l2;
        }
        LeafInfo<T, C> l3 = this.enums.get(type);
        if (l3 != null) {
            return l3;
        }
        if (this.nav.asDecl(Object.class).equals(type)) {
            return this.anyType;
        }
        return this.beans.get(type);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public ElementInfoImpl<T, C, F, M> getElementInfo(C scope, QName name) {
        ElementInfoImpl<T, C, F, M> r2;
        while (scope != null) {
            Map<QName, ElementInfoImpl<T, C, F, M>> m2 = this.elementMappings.get(scope);
            if (m2 != null && (r2 = m2.get(name)) != null) {
                return r2;
            }
            scope = this.nav.getSuperClass(scope);
        }
        return this.elementMappings.get(null).get(name);
    }

    public final void add(ElementInfoImpl<T, C, F, M> ei, ModelBuilder<T, C, F, M> builder) {
        C scope = null;
        if (ei.getScope2() != null) {
            scope = ei.getScope2().getClazz();
        }
        Map<QName, ElementInfoImpl<T, C, F, M>> m2 = this.elementMappings.get(scope);
        if (m2 == null) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            m2 = linkedHashMap;
            this.elementMappings.put(scope, linkedHashMap);
        }
        ElementInfoImpl<T, C, F, M> existing = m2.put(ei.getElementName(), ei);
        if (existing != null) {
            QName en = ei.getElementName();
            builder.reportError(new IllegalAnnotationException(Messages.CONFLICTING_XML_ELEMENT_MAPPING.format(en.getNamespaceURI(), en.getLocalPart()), ei, existing));
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<QName, ? extends ElementInfoImpl<T, C, F, M>> getElementMappings(C scope) {
        return this.elementMappings.get(scope);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Iterable<? extends ElementInfoImpl<T, C, F, M>> getAllElements() {
        return this.allElements;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
    public Map<String, String> getXmlNs(String namespaceUri) {
        if (this.xmlNsCache == null) {
            this.xmlNsCache = new HashMap();
            for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
                XmlSchema xs = (XmlSchema) this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
                if (xs != null) {
                    String uri = xs.namespace();
                    Map<String, String> m2 = this.xmlNsCache.get(uri);
                    if (m2 == null) {
                        Map<String, Map<String, String>> map = this.xmlNsCache;
                        HashMap map2 = new HashMap();
                        m2 = map2;
                        map.put(uri, map2);
                    }
                    for (XmlNs xns : xs.xmlns()) {
                        m2.put(xns.prefix(), xns.namespaceURI());
                    }
                }
            }
        }
        Map<String, String> r2 = this.xmlNsCache.get(namespaceUri);
        return r2 != null ? r2 : Collections.emptyMap();
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
    public Map<String, String> getSchemaLocations() {
        Map<String, String> r2 = new HashMap<>();
        for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
            XmlSchema xs = (XmlSchema) this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
            if (xs != null) {
                String loc = xs.location();
                if (!loc.equals(XmlSchema.NO_LOCATION)) {
                    r2.put(xs.namespace(), loc);
                }
            }
        }
        return r2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
    public final XmlNsForm getElementFormDefault(String nsUri) {
        XmlNsForm xnf;
        for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
            XmlSchema xs = (XmlSchema) this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
            if (xs != null && xs.namespace().equals(nsUri) && (xnf = xs.elementFormDefault()) != XmlNsForm.UNSET) {
                return xnf;
            }
        }
        return XmlNsForm.UNSET;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
    public final XmlNsForm getAttributeFormDefault(String nsUri) {
        XmlNsForm xnf;
        for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
            XmlSchema xs = (XmlSchema) this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
            if (xs != null && xs.namespace().equals(nsUri) && (xnf = xs.attributeFormDefault()) != XmlNsForm.UNSET) {
                return xnf;
            }
        }
        return XmlNsForm.UNSET;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet
    public void dump(Result out) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(getClass());
        Marshaller m2 = context.createMarshaller();
        m2.marshal(this, out);
    }
}
