package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ReferencePropertyInfoImpl.class */
class ReferencePropertyInfoImpl<T, C, F, M> extends ERPropertyInfoImpl<T, C, F, M> implements ReferencePropertyInfo<T, C>, DummyPropertyInfo<T, C, F, M> {
    private Set<Element<T, C>> types;
    private Set<ReferencePropertyInfoImpl<T, C, F, M>> subTypes;
    private final boolean isMixed;
    private final WildcardMode wildcard;
    private final C domHandler;
    private Boolean isRequired;
    private static boolean is2_2;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReferencePropertyInfoImpl.class.desiredAssertionStatus();
        is2_2 = true;
    }

    public ReferencePropertyInfoImpl(ClassInfoImpl<T, C, F, M> classInfo, PropertySeed<T, C, F, M> seed) {
        super(classInfo, seed);
        this.subTypes = new LinkedHashSet();
        this.isMixed = seed.readAnnotation(XmlMixed.class) != null;
        XmlAnyElement xae = (XmlAnyElement) seed.readAnnotation(XmlAnyElement.class);
        if (xae == null) {
            this.wildcard = null;
            this.domHandler = null;
        } else {
            this.wildcard = xae.lax() ? WildcardMode.LAX : WildcardMode.SKIP;
            this.domHandler = nav().asDecl((Navigator<T, C, F, M>) reader().getClassValue2(xae, "value"));
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public Set<? extends Element<T, C>> ref2() {
        return getElements();
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    public PropertyKind kind() {
        return PropertyKind.REFERENCE;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
    public Set<? extends Element<T, C>> getElements() {
        if (this.types == null) {
            calcTypes(false);
        }
        if ($assertionsDisabled || this.types != null) {
            return this.types;
        }
        throw new AssertionError();
    }

    private void calcTypes(boolean z2) {
        XmlElementRef[] xmlElementRefArrValue;
        XmlElementRef[] xmlElementRefArrValue2;
        boolean zAddAllSubtypes;
        boolean zAddAllSubtypes2;
        this.types = new LinkedHashSet();
        XmlElementRefs xmlElementRefs = (XmlElementRefs) this.seed.readAnnotation(XmlElementRefs.class);
        XmlElementRef xmlElementRef = (XmlElementRef) this.seed.readAnnotation(XmlElementRef.class);
        if (xmlElementRefs != null && xmlElementRef != null) {
            this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), xmlElementRef.annotationType().getName(), xmlElementRefs.annotationType().getName()), xmlElementRef, xmlElementRefs));
        }
        if (xmlElementRefs != null) {
            xmlElementRefArrValue = xmlElementRefs.value();
        } else if (xmlElementRef != null) {
            xmlElementRefArrValue = new XmlElementRef[]{xmlElementRef};
        } else {
            xmlElementRefArrValue = null;
        }
        this.isRequired = Boolean.valueOf(!isCollection());
        if (xmlElementRefArrValue != null) {
            Navigator<T, C, F, M> navigatorNav = nav();
            AnnotationReader<T, C, F, M> erVar = reader();
            T tRef = navigatorNav.ref(XmlElementRef.DEFAULT.class);
            C cAsDecl = navigatorNav.asDecl(JAXBElement.class);
            for (XmlElementRef xmlElementRef2 : xmlElementRefArrValue) {
                T classValue = erVar.getClassValue2(xmlElementRef2, "type");
                if (nav().isSameType(classValue, tRef)) {
                    classValue = navigatorNav.erasure(getIndividualType());
                }
                if (navigatorNav.getBaseClass(classValue, cAsDecl) != null) {
                    zAddAllSubtypes2 = addGenericElement(xmlElementRef2);
                } else {
                    zAddAllSubtypes2 = addAllSubtypes(classValue);
                }
                if (this.isRequired.booleanValue() && !isRequired(xmlElementRef2)) {
                    this.isRequired = false;
                }
                if (z2 && !zAddAllSubtypes2) {
                    if (nav().isSameType(classValue, navigatorNav.ref(JAXBElement.class))) {
                        this.parent.builder.reportError(new IllegalAnnotationException(Messages.NO_XML_ELEMENT_DECL.format(getEffectiveNamespaceFor(xmlElementRef2), xmlElementRef2.name()), this));
                        return;
                    } else {
                        this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ELEMENT_REF.format(classValue), this));
                        return;
                    }
                }
            }
        }
        for (ReferencePropertyInfoImpl<T, C, F, M> referencePropertyInfoImpl : this.subTypes) {
            PropertySeed<T, C, F, M> propertySeed = referencePropertyInfoImpl.seed;
            XmlElementRefs xmlElementRefs2 = (XmlElementRefs) propertySeed.readAnnotation(XmlElementRefs.class);
            XmlElementRef xmlElementRef3 = (XmlElementRef) propertySeed.readAnnotation(XmlElementRef.class);
            if (xmlElementRefs2 != null && xmlElementRef3 != null) {
                this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), xmlElementRef3.annotationType().getName(), xmlElementRefs2.annotationType().getName()), xmlElementRef3, xmlElementRefs2));
            }
            if (xmlElementRefs2 != null) {
                xmlElementRefArrValue2 = xmlElementRefs2.value();
            } else if (xmlElementRef3 != null) {
                xmlElementRefArrValue2 = new XmlElementRef[]{xmlElementRef3};
            } else {
                xmlElementRefArrValue2 = null;
            }
            if (xmlElementRefArrValue2 != null) {
                Navigator<T, C, F, M> navigatorNav2 = nav();
                AnnotationReader<T, C, F, M> erVar2 = reader();
                T tRef2 = navigatorNav2.ref(XmlElementRef.DEFAULT.class);
                C cAsDecl2 = navigatorNav2.asDecl(JAXBElement.class);
                for (XmlElementRef xmlElementRef4 : xmlElementRefArrValue2) {
                    T classValue2 = erVar2.getClassValue2(xmlElementRef4, "type");
                    if (nav().isSameType(classValue2, tRef2)) {
                        classValue2 = navigatorNav2.erasure(getIndividualType());
                    }
                    if (navigatorNav2.getBaseClass(classValue2, cAsDecl2) != null) {
                        zAddAllSubtypes = addGenericElement(xmlElementRef4, referencePropertyInfoImpl);
                    } else {
                        zAddAllSubtypes = addAllSubtypes(classValue2);
                    }
                    if (z2 && !zAddAllSubtypes) {
                        if (nav().isSameType(classValue2, navigatorNav2.ref(JAXBElement.class))) {
                            this.parent.builder.reportError(new IllegalAnnotationException(Messages.NO_XML_ELEMENT_DECL.format(getEffectiveNamespaceFor(xmlElementRef4), xmlElementRef4.name()), this));
                            return;
                        } else {
                            this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ELEMENT_REF.format(new Object[0]), this));
                            return;
                        }
                    }
                }
            }
        }
        this.types = Collections.unmodifiableSet(this.types);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
    public boolean isRequired() {
        if (this.isRequired == null) {
            calcTypes(false);
        }
        return this.isRequired.booleanValue();
    }

    private boolean isRequired(XmlElementRef ref) {
        if (!is2_2) {
            return true;
        }
        try {
            return ref.required();
        } catch (LinkageError e2) {
            is2_2 = false;
            return true;
        }
    }

    private boolean addGenericElement(XmlElementRef r2) {
        String nsUri = getEffectiveNamespaceFor(r2);
        return addGenericElement(this.parent.owner.getElementInfo((TypeInfoSet) this.parent.getClazz(), new QName(nsUri, r2.name())));
    }

    private boolean addGenericElement(XmlElementRef r2, ReferencePropertyInfoImpl<T, C, F, M> info) {
        String nsUri = info.getEffectiveNamespaceFor(r2);
        ElementInfoImpl<T, C, F, M> ei = this.parent.owner.getElementInfo((TypeInfoSet) info.parent.getClazz(), new QName(nsUri, r2.name()));
        this.types.add(ei);
        return true;
    }

    private String getEffectiveNamespaceFor(XmlElementRef xmlElementRef) {
        String strNamespace = xmlElementRef.namespace();
        XmlSchema xmlSchema = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
        if (xmlSchema != null && xmlSchema.attributeFormDefault() == XmlNsForm.QUALIFIED && strNamespace.length() == 0) {
            strNamespace = this.parent.builder.defaultNsUri;
        }
        return strNamespace;
    }

    private boolean addGenericElement(ElementInfo<T, C> ei) {
        if (ei == null) {
            return false;
        }
        this.types.add(ei);
        for (ElementInfo<T, C> subst : ei.getSubstitutionMembers()) {
            addGenericElement(subst);
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean addAllSubtypes(T t2) {
        Navigator<T, C, F, M> navigatorNav = nav();
        NonElement classInfo = this.parent.builder.getClassInfo(navigatorNav.asDecl((Navigator<T, C, F, M>) t2), this);
        if (!(classInfo instanceof ClassInfo)) {
            return false;
        }
        boolean z2 = false;
        ClassInfo classInfo2 = (ClassInfo) classInfo;
        if (classInfo2.isElement()) {
            this.types.add(classInfo2.asElement());
            z2 = true;
        }
        for (ClassInfo classInfo3 : this.parent.owner.beans().values()) {
            if (classInfo3.isElement() && navigatorNav.isSubClassOf(classInfo3.getType2(), t2)) {
                this.types.add(classInfo3.asElement());
                z2 = true;
            }
        }
        for (ElementInfo elementInfo : this.parent.owner.getElementMappings(null).values()) {
            if (navigatorNav.isSubClassOf(elementInfo.getType2(), t2)) {
                this.types.add(elementInfo);
                z2 = true;
            }
        }
        return z2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl
    protected void link() {
        super.link();
        calcTypes(true);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.DummyPropertyInfo
    public final void addType(PropertyInfoImpl<T, C, F, M> info) {
        this.subTypes.add((ReferencePropertyInfoImpl) info);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
    public final boolean isMixed() {
        return this.isMixed;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
    public final WildcardMode getWildcard() {
        return this.wildcard;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
    public final C getDOMHandler() {
        return this.domHandler;
    }
}
