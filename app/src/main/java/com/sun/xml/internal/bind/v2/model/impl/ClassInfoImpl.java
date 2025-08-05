package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.istack.internal.FinalArrayList;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.annotation.MethodLocatable;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.ValuePropertyInfo;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.Location;
import com.sun.xml.internal.bind.v2.util.EditDistance;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
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
import javax.swing.text.AbstractDocument;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ClassInfoImpl.class */
public class ClassInfoImpl<T, C, F, M> extends TypeInfoImpl<T, C, F, M> implements ClassInfo<T, C>, Element<T, C> {
    protected final C clazz;
    private final QName elementName;
    private final QName typeName;
    private FinalArrayList<PropertyInfoImpl<T, C, F, M>> properties;
    private String[] propOrder;
    private ClassInfoImpl<T, C, F, M> baseClass;
    private boolean baseClassComputed;
    private boolean hasSubClasses;
    protected PropertySeed<T, C, F, M> attributeWildcard;
    private M factoryMethod;
    private static final SecondaryAnnotation[] SECONDARY_ANNOTATIONS;
    private static final Annotation[] EMPTY_ANNOTATIONS;
    private static final HashMap<Class, Integer> ANNOTATION_NUMBER_MAP;
    private static final String[] DEFAULT_ORDER;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoImpl, com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public /* bridge */ /* synthetic */ Locatable getUpstream() {
        return super.getUpstream();
    }

    static {
        $assertionsDisabled = !ClassInfoImpl.class.desiredAssertionStatus();
        SECONDARY_ANNOTATIONS = SecondaryAnnotation.values();
        EMPTY_ANNOTATIONS = new Annotation[0];
        ANNOTATION_NUMBER_MAP = new HashMap<>();
        Class[] annotations = {XmlTransient.class, XmlAnyAttribute.class, XmlAttribute.class, XmlValue.class, XmlElement.class, XmlElements.class, XmlElementRef.class, XmlElementRefs.class, XmlAnyElement.class, XmlMixed.class, OverrideAnnotationOf.class};
        HashMap<Class, Integer> m2 = ANNOTATION_NUMBER_MAP;
        for (Class c2 : annotations) {
            m2.put(c2, Integer.valueOf(m2.size()));
        }
        int index = 20;
        for (SecondaryAnnotation sa : SECONDARY_ANNOTATIONS) {
            for (Class member : sa.members) {
                m2.put(member, Integer.valueOf(index));
            }
            index++;
        }
        DEFAULT_ORDER = new String[0];
    }

    ClassInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C clazz) {
        super(builder, upstream);
        this.baseClassComputed = false;
        this.hasSubClasses = false;
        this.factoryMethod = null;
        this.clazz = clazz;
        if (!$assertionsDisabled && clazz == null) {
            throw new AssertionError();
        }
        this.elementName = parseElementName(clazz);
        XmlType t2 = (XmlType) reader().getClassAnnotation(XmlType.class, clazz, this);
        this.typeName = parseTypeName(clazz, t2);
        if (t2 != null) {
            String[] propOrder = t2.propOrder();
            if (propOrder.length == 0) {
                this.propOrder = null;
            } else if (propOrder[0].length() == 0) {
                this.propOrder = DEFAULT_ORDER;
            } else {
                this.propOrder = propOrder;
            }
        } else {
            this.propOrder = DEFAULT_ORDER;
        }
        XmlAccessorOrder xao = (XmlAccessorOrder) reader().getPackageAnnotation(XmlAccessorOrder.class, clazz, this);
        if (xao != null && xao.value() == XmlAccessOrder.UNDEFINED) {
            this.propOrder = null;
        }
        XmlAccessorOrder xao2 = (XmlAccessorOrder) reader().getClassAnnotation(XmlAccessorOrder.class, clazz, this);
        if (xao2 != null && xao2.value() == XmlAccessOrder.UNDEFINED) {
            this.propOrder = null;
        }
        if (nav().isInterface(clazz)) {
            builder.reportError(new IllegalAnnotationException(Messages.CANT_HANDLE_INTERFACE.format(nav().getClassName(clazz)), this));
        }
        if (!hasFactoryConstructor(t2) && !nav().hasDefaultConstructor(clazz)) {
            if (nav().isInnerClass(clazz)) {
                builder.reportError(new IllegalAnnotationException(Messages.CANT_HANDLE_INNER_CLASS.format(nav().getClassName(clazz)), this));
            } else if (this.elementName != null) {
                builder.reportError(new IllegalAnnotationException(Messages.NO_DEFAULT_CONSTRUCTOR.format(nav().getClassName(clazz)), this));
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    /* renamed from: getBaseClass */
    public ClassInfoImpl<T, C, F, M> getBaseClass2() {
        if (!this.baseClassComputed) {
            C s2 = nav().getSuperClass(this.clazz);
            if (s2 == null || s2 == nav().asDecl(Object.class)) {
                this.baseClass = null;
            } else {
                NonElement<T, C> b2 = this.builder.getClassInfo(s2, true, this);
                if (b2 instanceof ClassInfoImpl) {
                    this.baseClass = (ClassInfoImpl) b2;
                    this.baseClass.hasSubClasses = true;
                } else {
                    this.baseClass = null;
                }
            }
            this.baseClassComputed = true;
        }
        return this.baseClass;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.Element
    public final Element<T, C> getSubstitutionHead() {
        ClassInfoImpl<T, C, F, M> c2;
        ClassInfoImpl<T, C, F, M> baseClass = getBaseClass2();
        while (true) {
            c2 = baseClass;
            if (c2 == null || c2.isElement()) {
                break;
            }
            baseClass = c2.getBaseClass2();
        }
        return c2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final C getClazz() {
        return this.clazz;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.Element
    public ClassInfoImpl<T, C, F, M> getScope() {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    /* renamed from: getType */
    public final T getType2() {
        return nav().use(this.clazz);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    public boolean canBeReferencedByIDREF() {
        for (PropertyInfo<T, C> p2 : getProperties()) {
            if (p2.id() == ID.ID) {
                return true;
            }
        }
        ClassInfoImpl<T, C, F, M> base = getBaseClass2();
        if (base != null) {
            return base.canBeReferencedByIDREF();
        }
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final String getName() {
        return nav().getClassName(this.clazz);
    }

    public <A extends Annotation> A readAnnotation(Class<A> cls) {
        return (A) reader().getClassAnnotation(cls, this.clazz, this);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MaybeElement
    public Element<T, C> asElement() {
        if (isElement()) {
            return this;
        }
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public List<? extends PropertyInfo<T, C>> getProperties() throws ConflictException {
        if (this.properties != null) {
            return this.properties;
        }
        XmlAccessType at2 = getAccessType();
        this.properties = new FinalArrayList<>();
        findFieldProperties(this.clazz, at2);
        findGetterSetterProperties(at2);
        if (this.propOrder == DEFAULT_ORDER || this.propOrder == null) {
            XmlAccessOrder ao2 = getAccessorOrder();
            if (ao2 == XmlAccessOrder.ALPHABETICAL) {
                Collections.sort(this.properties);
            }
        } else {
            ClassInfoImpl<T, C, F, M>.PropertySorter sorter = new PropertySorter();
            Iterator<PropertyInfoImpl<T, C, F, M>> it = this.properties.iterator();
            while (it.hasNext()) {
                sorter.checkedGet(it.next());
            }
            Collections.sort(this.properties, sorter);
            sorter.checkUnusedProperties();
        }
        PropertyInfoImpl vp = null;
        PropertyInfoImpl ep = null;
        Iterator<PropertyInfoImpl<T, C, F, M>> it2 = this.properties.iterator();
        while (it2.hasNext()) {
            PropertyInfoImpl p2 = it2.next();
            switch (p2.kind()) {
                case ELEMENT:
                case REFERENCE:
                case MAP:
                    ep = p2;
                    break;
                case VALUE:
                    if (vp != null) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.MULTIPLE_VALUE_PROPERTY.format(new Object[0]), vp, p2));
                    }
                    if (getBaseClass2() != null) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.XMLVALUE_IN_DERIVED_TYPE.format(new Object[0]), p2));
                    }
                    vp = p2;
                    break;
                case ATTRIBUTE:
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
        }
        if (ep != null && vp != null) {
            this.builder.reportError(new IllegalAnnotationException(Messages.ELEMENT_AND_VALUE_PROPERTY.format(new Object[0]), vp, ep));
        }
        return this.properties;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void findFieldProperties(C c2, XmlAccessType at2) throws ConflictException {
        ClassInfo<T, C> top;
        Object superClass = nav().getSuperClass(c2);
        if (shouldRecurseSuperClass(superClass)) {
            findFieldProperties(superClass, at2);
        }
        for (F f2 : nav().getDeclaredFields(c2)) {
            Annotation[] annotations = reader().getAllFieldAnnotations(f2, this);
            boolean isDummy = reader().hasFieldAnnotation(OverrideAnnotationOf.class, f2);
            if (nav().isTransient(f2)) {
                if (hasJAXBAnnotation(annotations)) {
                    this.builder.reportError(new IllegalAnnotationException(Messages.TRANSIENT_FIELD_NOT_BINDABLE.format(nav().getFieldName(f2)), getSomeJAXBAnnotation(annotations)));
                }
            } else if (nav().isStaticField(f2)) {
                if (hasJAXBAnnotation(annotations)) {
                    addProperty(createFieldSeed(f2), annotations, false);
                }
            } else {
                if (at2 == XmlAccessType.FIELD || ((at2 == XmlAccessType.PUBLIC_MEMBER && nav().isPublicField(f2)) || hasJAXBAnnotation(annotations))) {
                    if (isDummy) {
                        ClassInfo<T, C> baseClass = getBaseClass2();
                        while (true) {
                            top = baseClass;
                            if (top == null || top.getProperty2(AbstractDocument.ContentElementName) != null) {
                                break;
                            } else {
                                baseClass = top.getBaseClass2();
                            }
                        }
                        DummyPropertyInfo prop = (DummyPropertyInfo) top.getProperty2(AbstractDocument.ContentElementName);
                        PropertySeed seed = createFieldSeed(f2);
                        prop.addType(createReferenceProperty(seed));
                    } else {
                        addProperty(createFieldSeed(f2), annotations, false);
                    }
                }
                checkFieldXmlLocation(f2);
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final boolean hasValueProperty() {
        ClassInfoImpl<T, C, F, M> bc2 = getBaseClass2();
        if (bc2 != null && bc2.hasValueProperty()) {
            return true;
        }
        for (PropertyInfo p2 : getProperties()) {
            if (p2 instanceof ValuePropertyInfo) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    /* renamed from: getProperty */
    public PropertyInfo<T, C> getProperty2(String name) {
        for (PropertyInfo<T, C> p2 : getProperties()) {
            if (p2.getName().equals(name)) {
                return p2;
            }
        }
        return null;
    }

    protected void checkFieldXmlLocation(F f2) {
    }

    /* JADX WARN: Incorrect return type in method signature: <T::Ljava/lang/annotation/Annotation;>(Ljava/lang/Class<TT;>;)TT; */
    private Annotation getClassOrPackageAnnotation(Class cls) {
        Annotation classAnnotation = reader().getClassAnnotation(cls, this.clazz, this);
        if (classAnnotation != null) {
            return classAnnotation;
        }
        return reader().getPackageAnnotation(cls, this.clazz, this);
    }

    private XmlAccessType getAccessType() {
        XmlAccessorType xat = (XmlAccessorType) getClassOrPackageAnnotation(XmlAccessorType.class);
        if (xat != null) {
            return xat.value();
        }
        return XmlAccessType.PUBLIC_MEMBER;
    }

    private XmlAccessOrder getAccessorOrder() {
        XmlAccessorOrder xao = (XmlAccessorOrder) getClassOrPackageAnnotation(XmlAccessorOrder.class);
        if (xao != null) {
            return xao.value();
        }
        return XmlAccessOrder.UNDEFINED;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ClassInfoImpl$PropertySorter.class */
    private final class PropertySorter extends HashMap<String, Integer> implements Comparator<PropertyInfoImpl> {
        PropertyInfoImpl[] used;
        private Set<String> collidedNames;

        PropertySorter() {
            super(ClassInfoImpl.this.propOrder.length);
            this.used = new PropertyInfoImpl[ClassInfoImpl.this.propOrder.length];
            for (String name : ClassInfoImpl.this.propOrder) {
                if (put(name, Integer.valueOf(size())) != null) {
                    ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_ENTRY_IN_PROP_ORDER.format(name), ClassInfoImpl.this));
                }
            }
        }

        @Override // java.util.Comparator
        public int compare(PropertyInfoImpl o1, PropertyInfoImpl o2) {
            int lhs = checkedGet(o1);
            int rhs = checkedGet(o2);
            return lhs - rhs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int checkedGet(PropertyInfoImpl p2) {
            Integer i2 = get(p2.getName());
            if (i2 == null) {
                if (p2.kind().isOrdered) {
                    ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_MISSING_FROM_ORDER.format(p2.getName()), p2));
                }
                i2 = Integer.valueOf(size());
                put(p2.getName(), i2);
            }
            int ii = i2.intValue();
            if (ii < this.used.length) {
                if (this.used[ii] != null && this.used[ii] != p2) {
                    if (this.collidedNames == null) {
                        this.collidedNames = new HashSet();
                    }
                    if (this.collidedNames.add(p2.getName())) {
                        ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_PROPERTIES.format(p2.getName()), p2, this.used[ii]));
                    }
                }
                this.used[ii] = p2;
            }
            return i2.intValue();
        }

        public void checkUnusedProperties() {
            int i2 = 0;
            while (i2 < this.used.length) {
                if (this.used[i2] == null) {
                    String str = ClassInfoImpl.this.propOrder[i2];
                    String strFindNearest = EditDistance.findNearest(str, new AbstractList<String>() { // from class: com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl.PropertySorter.1
                        @Override // java.util.AbstractList, java.util.List
                        public String get(int i3) {
                            return ((PropertyInfoImpl) ClassInfoImpl.this.properties.get(i3)).getName();
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                        public int size() {
                            return ClassInfoImpl.this.properties.size();
                        }
                    });
                    if (!(i2 > ClassInfoImpl.this.properties.size() - 1 ? false : ((PropertyInfoImpl) ClassInfoImpl.this.properties.get(i2)).hasAnnotation(OverrideAnnotationOf.class))) {
                        ClassInfoImpl.this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_ORDER_CONTAINS_UNUSED_ENTRY.format(str, strFindNearest), ClassInfoImpl.this));
                    }
                }
                i2++;
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public boolean hasProperties() {
        return !this.properties.isEmpty();
    }

    private static <T> T pickOne(T... args) {
        for (T arg : args) {
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }

    private static <T> List<T> makeSet(T... args) {
        List<T> l2 = new FinalArrayList<>();
        for (T arg : args) {
            if (arg != null) {
                l2.add(arg);
            }
        }
        return l2;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ClassInfoImpl$ConflictException.class */
    private static final class ConflictException extends Exception {
        final List<Annotation> annotations;

        public ConflictException(List<Annotation> one) {
            this.annotations = one;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ClassInfoImpl$DuplicateException.class */
    private static final class DuplicateException extends Exception {
        final Annotation a1;
        final Annotation a2;

        public DuplicateException(Annotation a1, Annotation a2) {
            this.a1 = a1;
            this.a2 = a2;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ClassInfoImpl$SecondaryAnnotation.class */
    private enum SecondaryAnnotation {
        JAVA_TYPE(1, XmlJavaTypeAdapter.class),
        ID_IDREF(2, XmlID.class, XmlIDREF.class),
        BINARY(4, XmlInlineBinaryData.class, XmlMimeType.class, XmlAttachmentRef.class),
        ELEMENT_WRAPPER(8, XmlElementWrapper.class),
        LIST(16, XmlList.class),
        SCHEMA_TYPE(32, XmlSchemaType.class);

        final int bitMask;
        final Class<? extends Annotation>[] members;

        SecondaryAnnotation(int bitMask, Class... clsArr) {
            this.bitMask = bitMask;
            this.members = clsArr;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ClassInfoImpl$PropertyGroup.class */
    private enum PropertyGroup {
        TRANSIENT(false, false, false, false, false, false),
        ANY_ATTRIBUTE(true, false, false, false, false, false),
        ATTRIBUTE(true, true, true, false, true, true),
        VALUE(true, true, true, false, true, true),
        ELEMENT(true, true, true, true, true, true),
        ELEMENT_REF(true, false, false, true, false, false),
        MAP(false, false, false, true, false, false);

        final int allowedsecondaryAnnotations;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ClassInfoImpl.class.desiredAssertionStatus();
        }

        PropertyGroup(boolean... bits) {
            int mask = 0;
            if (!$assertionsDisabled && bits.length != ClassInfoImpl.SECONDARY_ANNOTATIONS.length) {
                throw new AssertionError();
            }
            for (int i2 = 0; i2 < bits.length; i2++) {
                if (bits[i2]) {
                    mask |= ClassInfoImpl.SECONDARY_ANNOTATIONS[i2].bitMask;
                }
            }
            this.allowedsecondaryAnnotations = mask ^ (-1);
        }

        boolean allows(SecondaryAnnotation a2) {
            return (this.allowedsecondaryAnnotations & a2.bitMask) == 0;
        }
    }

    private void checkConflict(Annotation a2, Annotation b2) throws DuplicateException {
        if (!$assertionsDisabled && b2 == null) {
            throw new AssertionError();
        }
        if (a2 != null) {
            throw new DuplicateException(a2, b2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addProperty(PropertySeed<T, C, F, M> propertySeed, Annotation[] annotationArr, boolean z2) throws ConflictException {
        XmlTransient xmlTransient = null;
        XmlAnyAttribute xmlAnyAttribute = null;
        XmlAttribute xmlAttribute = null;
        XmlValue xmlValue = null;
        XmlElement xmlElement = null;
        XmlElements xmlElements = null;
        XmlElementRef xmlElementRef = null;
        XmlElementRefs xmlElementRefs = null;
        XmlAnyElement xmlAnyElement = null;
        XmlMixed xmlMixed = null;
        OverrideAnnotationOf overrideAnnotationOf = null;
        int iIntValue = 0;
        try {
            for (Annotation annotation : annotationArr) {
                Integer num = ANNOTATION_NUMBER_MAP.get(annotation.annotationType());
                if (num != null) {
                    switch (num.intValue()) {
                        case 0:
                            checkConflict(xmlTransient, annotation);
                            xmlTransient = (XmlTransient) annotation;
                            break;
                        case 1:
                            checkConflict(xmlAnyAttribute, annotation);
                            xmlAnyAttribute = (XmlAnyAttribute) annotation;
                            break;
                        case 2:
                            checkConflict(xmlAttribute, annotation);
                            xmlAttribute = (XmlAttribute) annotation;
                            break;
                        case 3:
                            checkConflict(xmlValue, annotation);
                            xmlValue = (XmlValue) annotation;
                            break;
                        case 4:
                            checkConflict(xmlElement, annotation);
                            xmlElement = (XmlElement) annotation;
                            break;
                        case 5:
                            checkConflict(xmlElements, annotation);
                            xmlElements = (XmlElements) annotation;
                            break;
                        case 6:
                            checkConflict(xmlElementRef, annotation);
                            xmlElementRef = (XmlElementRef) annotation;
                            break;
                        case 7:
                            checkConflict(xmlElementRefs, annotation);
                            xmlElementRefs = (XmlElementRefs) annotation;
                            break;
                        case 8:
                            checkConflict(xmlAnyElement, annotation);
                            xmlAnyElement = (XmlAnyElement) annotation;
                            break;
                        case 9:
                            checkConflict(xmlMixed, annotation);
                            xmlMixed = (XmlMixed) annotation;
                            break;
                        case 10:
                            checkConflict(overrideAnnotationOf, annotation);
                            overrideAnnotationOf = (OverrideAnnotationOf) annotation;
                            break;
                        default:
                            iIntValue |= 1 << (num.intValue() - 20);
                            break;
                    }
                }
            }
            PropertyGroup propertyGroup = null;
            int i2 = 0;
            if (xmlTransient != null) {
                propertyGroup = PropertyGroup.TRANSIENT;
                i2 = 0 + 1;
            }
            if (xmlAnyAttribute != null) {
                propertyGroup = PropertyGroup.ANY_ATTRIBUTE;
                i2++;
            }
            if (xmlAttribute != null) {
                propertyGroup = PropertyGroup.ATTRIBUTE;
                i2++;
            }
            if (xmlValue != null) {
                propertyGroup = PropertyGroup.VALUE;
                i2++;
            }
            if (xmlElement != null || xmlElements != null) {
                propertyGroup = PropertyGroup.ELEMENT;
                i2++;
            }
            if (xmlElementRef != null || xmlElementRefs != null || xmlAnyElement != null || xmlMixed != null || overrideAnnotationOf != null) {
                propertyGroup = PropertyGroup.ELEMENT_REF;
                i2++;
            }
            if (i2 > 1) {
                throw new ConflictException(makeSet(xmlTransient, xmlAnyAttribute, xmlAttribute, xmlValue, (Annotation) pickOne(xmlElement, xmlElements), (Annotation) pickOne(xmlElementRef, xmlElementRefs, xmlAnyElement)));
            }
            if (propertyGroup == null) {
                if (!$assertionsDisabled && i2 != 0) {
                    throw new AssertionError();
                }
                if (nav().isSubClassOf(propertySeed.getRawType(), nav().ref(Map.class)) && !propertySeed.hasAnnotation(XmlJavaTypeAdapter.class)) {
                    propertyGroup = PropertyGroup.MAP;
                } else {
                    propertyGroup = PropertyGroup.ELEMENT;
                }
            } else if (propertyGroup.equals(PropertyGroup.ELEMENT) && nav().isSubClassOf(propertySeed.getRawType(), nav().ref(Map.class)) && !propertySeed.hasAnnotation(XmlJavaTypeAdapter.class)) {
                propertyGroup = PropertyGroup.MAP;
            }
            if ((iIntValue & propertyGroup.allowedsecondaryAnnotations) != 0) {
                for (SecondaryAnnotation secondaryAnnotation : SECONDARY_ANNOTATIONS) {
                    if (!propertyGroup.allows(secondaryAnnotation)) {
                        for (Class<A> cls : secondaryAnnotation.members) {
                            Annotation annotation2 = propertySeed.readAnnotation(cls);
                            if (annotation2 != null) {
                                this.builder.reportError(new IllegalAnnotationException(Messages.ANNOTATION_NOT_ALLOWED.format(cls.getSimpleName()), annotation2));
                                return;
                            }
                        }
                    }
                }
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            switch (propertyGroup) {
                case TRANSIENT:
                    return;
                case ANY_ATTRIBUTE:
                    if (this.attributeWildcard != null) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.TWO_ATTRIBUTE_WILDCARDS.format(nav().getClassName(getClazz())), xmlAnyAttribute, this.attributeWildcard));
                        return;
                    }
                    this.attributeWildcard = propertySeed;
                    if (inheritsAttributeWildcard()) {
                        this.builder.reportError(new IllegalAnnotationException(Messages.SUPER_CLASS_HAS_WILDCARD.format(new Object[0]), xmlAnyAttribute, getInheritedAttributeWildcard()));
                        return;
                    } else {
                        if (!nav().isSubClassOf(propertySeed.getRawType(), nav().ref(Map.class))) {
                            this.builder.reportError(new IllegalAnnotationException(Messages.INVALID_ATTRIBUTE_WILDCARD_TYPE.format(nav().getTypeName(propertySeed.getRawType())), xmlAnyAttribute, getInheritedAttributeWildcard()));
                            return;
                        }
                        return;
                    }
                case ATTRIBUTE:
                    this.properties.add(createAttributeProperty(propertySeed));
                    return;
                case VALUE:
                    this.properties.add(createValueProperty(propertySeed));
                    return;
                case ELEMENT:
                    this.properties.add(createElementProperty(propertySeed));
                    return;
                case ELEMENT_REF:
                    this.properties.add(createReferenceProperty(propertySeed));
                    return;
                case MAP:
                    this.properties.add(createMapProperty(propertySeed));
                    return;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    return;
            }
        } catch (ConflictException e2) {
            List<Annotation> list = e2.annotations;
            this.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(getClazz()) + '#' + propertySeed.getName(), list.get(0).annotationType().getName(), list.get(1).annotationType().getName()), list.get(0), list.get(1)));
        } catch (DuplicateException e3) {
            this.builder.reportError(new IllegalAnnotationException(Messages.DUPLICATE_ANNOTATIONS.format(e3.a1.annotationType().getName()), e3.a1, e3.a2));
        }
    }

    protected ReferencePropertyInfoImpl<T, C, F, M> createReferenceProperty(PropertySeed<T, C, F, M> seed) {
        return new ReferencePropertyInfoImpl<>(this, seed);
    }

    protected AttributePropertyInfoImpl<T, C, F, M> createAttributeProperty(PropertySeed<T, C, F, M> seed) {
        return new AttributePropertyInfoImpl<>(this, seed);
    }

    protected ValuePropertyInfoImpl<T, C, F, M> createValueProperty(PropertySeed<T, C, F, M> seed) {
        return new ValuePropertyInfoImpl<>(this, seed);
    }

    protected ElementPropertyInfoImpl<T, C, F, M> createElementProperty(PropertySeed<T, C, F, M> seed) {
        return new ElementPropertyInfoImpl<>(this, seed);
    }

    protected MapPropertyInfoImpl<T, C, F, M> createMapProperty(PropertySeed<T, C, F, M> seed) {
        return new MapPropertyInfoImpl<>(this, seed);
    }

    private void findGetterSetterProperties(XmlAccessType xmlAccessType) throws ConflictException {
        Annotation[] annotationArr;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        C superClass = this.clazz;
        do {
            collectGetterSetters(this.clazz, linkedHashMap, linkedHashMap2);
            superClass = nav().getSuperClass(superClass);
        } while (shouldRecurseSuperClass(superClass));
        TreeSet treeSet = new TreeSet(linkedHashMap.keySet());
        treeSet.retainAll(linkedHashMap2.keySet());
        resurrect(linkedHashMap, treeSet);
        resurrect(linkedHashMap2, treeSet);
        for (String str : treeSet) {
            M m2 = linkedHashMap.get(str);
            M m3 = linkedHashMap2.get(str);
            Annotation[] allMethodAnnotations = m2 != null ? reader().getAllMethodAnnotations(m2, new MethodLocatable(this, m2, nav())) : EMPTY_ANNOTATIONS;
            Annotation[] allMethodAnnotations2 = m3 != null ? reader().getAllMethodAnnotations(m3, new MethodLocatable(this, m3, nav())) : EMPTY_ANNOTATIONS;
            boolean z2 = hasJAXBAnnotation(allMethodAnnotations) || hasJAXBAnnotation(allMethodAnnotations2);
            boolean z3 = false;
            if (!z2) {
                z3 = m2 != null && nav().isOverriding(m2, superClass) && m3 != null && nav().isOverriding(m3, superClass);
            }
            if ((xmlAccessType == XmlAccessType.PROPERTY && !z3) || ((xmlAccessType == XmlAccessType.PUBLIC_MEMBER && isConsideredPublic(m2) && isConsideredPublic(m3) && !z3) || z2)) {
                if (m2 != null && m3 != null && !nav().isSameType(nav().getReturnType(m2), nav().getMethodParameters(m3)[0])) {
                    this.builder.reportError(new IllegalAnnotationException(Messages.GETTER_SETTER_INCOMPATIBLE_TYPE.format(nav().getTypeName(nav().getReturnType(m2)), nav().getTypeName(nav().getMethodParameters(m3)[0])), new MethodLocatable(this, m2, nav()), new MethodLocatable(this, m3, nav())));
                } else {
                    if (allMethodAnnotations.length == 0) {
                        annotationArr = allMethodAnnotations2;
                    } else if (allMethodAnnotations2.length == 0) {
                        annotationArr = allMethodAnnotations;
                    } else {
                        annotationArr = new Annotation[allMethodAnnotations.length + allMethodAnnotations2.length];
                        System.arraycopy(allMethodAnnotations, 0, annotationArr, 0, allMethodAnnotations.length);
                        System.arraycopy(allMethodAnnotations2, 0, annotationArr, allMethodAnnotations.length, allMethodAnnotations2.length);
                    }
                    addProperty(createAccessorSeed(m2, m3), annotationArr, false);
                }
            }
        }
        linkedHashMap.keySet().removeAll(treeSet);
        linkedHashMap2.keySet().removeAll(treeSet);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void collectGetterSetters(C c2, Map<String, M> getters, Map<String, M> map) {
        Object superClass = nav().getSuperClass(c2);
        if (shouldRecurseSuperClass(superClass)) {
            collectGetterSetters(superClass, getters, map);
        }
        Collection<? extends M> methods = nav().getDeclaredMethods(c2);
        Map<String, List<M>> allSetters = new LinkedHashMap<>();
        for (M method : methods) {
            boolean used = false;
            if (!nav().isBridgeMethod(method)) {
                String name = nav().getMethodName(method);
                int arity = nav().getMethodParameters(method).length;
                if (nav().isStaticMethod(method)) {
                    ensureNoAnnotation(method);
                } else {
                    String propName = getPropertyNameFromGetMethod(name);
                    if (propName != null && arity == 0) {
                        getters.put(propName, method);
                        used = true;
                    }
                    String propName2 = getPropertyNameFromSetMethod(name);
                    if (propName2 != null && arity == 1) {
                        List<M> propSetters = allSetters.get(propName2);
                        if (null == propSetters) {
                            propSetters = new ArrayList<>();
                            allSetters.put(propName2, propSetters);
                        }
                        propSetters.add(method);
                        used = true;
                    }
                    if (!used) {
                        ensureNoAnnotation(method);
                    }
                }
            }
        }
        for (Map.Entry<String, M> entry : getters.entrySet()) {
            String propName3 = entry.getKey();
            M getter = entry.getValue();
            List<M> propSetters2 = allSetters.remove(propName3);
            if (null != propSetters2) {
                Object returnType = nav().getReturnType(getter);
                Iterator<M> it = propSetters2.iterator();
                while (true) {
                    if (it.hasNext()) {
                        M setter = it.next();
                        if (nav().isSameType(nav().getMethodParameters(setter)[0], returnType)) {
                            map.put(propName3, setter);
                            break;
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, List<M>> e2 : allSetters.entrySet()) {
            map.put(e2.getKey(), e2.getValue().get(0));
        }
    }

    private boolean shouldRecurseSuperClass(C sc) {
        return sc != null && (this.builder.isReplaced(sc) || reader().hasClassAnnotation(sc, XmlTransient.class));
    }

    private boolean isConsideredPublic(M m2) {
        return m2 == null || nav().isPublicMethod(m2);
    }

    private void resurrect(Map<String, M> methods, Set<String> complete) {
        for (Map.Entry<String, M> e2 : methods.entrySet()) {
            if (!complete.contains(e2.getKey()) && hasJAXBAnnotation(reader().getAllMethodAnnotations(e2.getValue(), this))) {
                complete.add(e2.getKey());
            }
        }
    }

    private void ensureNoAnnotation(M method) {
        Annotation[] annotations = reader().getAllMethodAnnotations(method, this);
        for (Annotation a2 : annotations) {
            if (isJAXBAnnotation(a2)) {
                this.builder.reportError(new IllegalAnnotationException(Messages.ANNOTATION_ON_WRONG_METHOD.format(new Object[0]), a2));
                return;
            }
        }
    }

    private static boolean isJAXBAnnotation(Annotation a2) {
        return ANNOTATION_NUMBER_MAP.containsKey(a2.annotationType());
    }

    private static boolean hasJAXBAnnotation(Annotation[] annotations) {
        return getSomeJAXBAnnotation(annotations) != null;
    }

    private static Annotation getSomeJAXBAnnotation(Annotation[] annotations) {
        for (Annotation a2 : annotations) {
            if (isJAXBAnnotation(a2)) {
                return a2;
            }
        }
        return null;
    }

    private static String getPropertyNameFromGetMethod(String name) {
        if (name.startsWith("get") && name.length() > 3) {
            return name.substring(3);
        }
        if (name.startsWith(BeanAdapter.IS_PREFIX) && name.length() > 2) {
            return name.substring(2);
        }
        return null;
    }

    private static String getPropertyNameFromSetMethod(String name) {
        if (name.startsWith("set") && name.length() > 3) {
            return name.substring(3);
        }
        return null;
    }

    protected PropertySeed<T, C, F, M> createFieldSeed(F f2) {
        return new FieldPropertySeed(this, f2);
    }

    protected PropertySeed<T, C, F, M> createAccessorSeed(M getter, M setter) {
        return new GetterSetterPropertySeed(this, getter, setter);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MaybeElement
    public final boolean isElement() {
        return this.elementName != null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public boolean isAbstract() {
        return nav().isAbstract(this.clazz);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public boolean isOrdered() {
        return this.propOrder != null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final boolean isFinal() {
        return nav().isFinal(this.clazz);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final boolean hasSubClasses() {
        return this.hasSubClasses;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final boolean hasAttributeWildcard() {
        return declaresAttributeWildcard() || inheritsAttributeWildcard();
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final boolean inheritsAttributeWildcard() {
        return getInheritedAttributeWildcard() != null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public final boolean declaresAttributeWildcard() {
        return this.attributeWildcard != null;
    }

    private PropertySeed<T, C, F, M> getInheritedAttributeWildcard() {
        ClassInfoImpl<T, C, F, M> baseClass = getBaseClass2();
        while (true) {
            ClassInfoImpl<T, C, F, M> c2 = baseClass;
            if (c2 != null) {
                if (c2.attributeWildcard == null) {
                    baseClass = c2.getBaseClass2();
                } else {
                    return c2.attributeWildcard;
                }
            } else {
                return null;
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MaybeElement
    public final QName getElementName() {
        return this.elementName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElement
    public final QName getTypeName() {
        return this.typeName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElement
    public final boolean isSimpleType() throws ConflictException {
        List<? extends PropertyInfo> props = getProperties();
        return props.size() == 1 && props.get(0).kind() == PropertyKind.VALUE;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoImpl
    void link() throws ConflictException {
        getProperties();
        Map<String, PropertyInfoImpl> names = new HashMap<>();
        Iterator<PropertyInfoImpl<T, C, F, M>> it = this.properties.iterator();
        while (it.hasNext()) {
            PropertyInfoImpl<T, C, F, M> p2 = it.next();
            p2.link();
            PropertyInfoImpl old = names.put(p2.getName(), p2);
            if (old != null) {
                this.builder.reportError(new IllegalAnnotationException(Messages.PROPERTY_COLLISION.format(p2.getName()), p2, old));
            }
        }
        super.link();
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return nav().getClassLocation(this.clazz);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean hasFactoryConstructor(XmlType xmlType) {
        if (xmlType == null) {
            return false;
        }
        String strFactoryMethod = xmlType.factoryMethod();
        T classValue = reader().getClassValue(xmlType, "factoryClass");
        if (strFactoryMethod.length() > 0) {
            if (nav().isSameType(classValue, nav().ref(XmlType.DEFAULT.class))) {
                classValue = nav().use(this.clazz);
            }
            Iterator<? extends M> it = nav().getDeclaredMethods(nav().asDecl((Navigator<T, C, F, M>) classValue)).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                M m2 = (M) it.next();
                if (nav().getMethodName(m2).equals(strFactoryMethod) && nav().isSameType(nav().getReturnType(m2), nav().use(this.clazz)) && nav().getMethodParameters(m2).length == 0 && nav().isStaticMethod(m2)) {
                    this.factoryMethod = m2;
                    break;
                }
            }
            if (this.factoryMethod == null) {
                this.builder.reportError(new IllegalAnnotationException(Messages.NO_FACTORY_METHOD.format(nav().getClassName(nav().asDecl((Navigator<T, C, F, M>) classValue)), strFactoryMethod), this));
            }
        } else if (!nav().isSameType(classValue, nav().ref(XmlType.DEFAULT.class))) {
            this.builder.reportError(new IllegalAnnotationException(Messages.FACTORY_CLASS_NEEDS_FACTORY_METHOD.format(nav().getClassName(nav().asDecl((Navigator<T, C, F, M>) classValue))), this));
        }
        return this.factoryMethod != null;
    }

    public Method getFactoryMethod() {
        return (Method) this.factoryMethod;
    }

    public String toString() {
        return "ClassInfo(" + ((Object) this.clazz) + ')';
    }
}
