package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.istack.internal.FinalArrayList;
import com.sun.istack.internal.localization.Localizable;
import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.Adapter;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.Location;
import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.activation.MimeType;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ElementInfoImpl.class */
class ElementInfoImpl<T, C, F, M> extends TypeInfoImpl<T, C, F, M> implements ElementInfo<T, C> {
    private final QName tagName;
    private final NonElement<T, C> contentType;
    private final T tOfJAXBElementT;
    private final T elementType;
    private final ClassInfo<T, C> scope;
    private final XmlElementDecl anno;
    private ElementInfoImpl<T, C, F, M> substitutionHead;
    private FinalArrayList<ElementInfoImpl<T, C, F, M>> substitutionMembers;
    private final M method;
    private final Adapter<T, C> adapter;
    private final boolean isCollection;
    private final ID id;
    private final ElementInfoImpl<T, C, F, M>.PropertyImpl property;
    private final MimeType expectedMimeType;
    private final boolean inlineBinary;
    private final QName schemaType;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ElementInfoImpl.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ElementInfoImpl$PropertyImpl.class */
    protected class PropertyImpl implements ElementPropertyInfo<T, C>, TypeRef<T, C>, AnnotationSource {
        protected PropertyImpl() {
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
        /* renamed from: getTarget */
        public NonElement<T, C> getTarget2() {
            return ElementInfoImpl.this.contentType;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.TypeRef
        public QName getTagName() {
            return ElementInfoImpl.this.tagName;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
        public List<? extends TypeRef<T, C>> getTypes() {
            return Collections.singletonList(this);
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        /* renamed from: ref */
        public List<? extends NonElement<T, C>> ref2() {
            return Collections.singletonList(ElementInfoImpl.this.contentType);
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
        public QName getXmlName() {
            return ElementInfoImpl.this.tagName;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
        public boolean isCollectionRequired() {
            return false;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
        public boolean isCollectionNillable() {
            return true;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.TypeRef
        public boolean isNillable() {
            return true;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.TypeRef
        public String getDefaultValue() {
            String v2 = ElementInfoImpl.this.anno.defaultValue();
            if (v2.equals(Localizable.NOT_LOCALIZABLE)) {
                return null;
            }
            return v2;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public ElementInfoImpl<T, C, F, M> parent() {
            return ElementInfoImpl.this;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public String getName() {
            return "value";
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public String displayName() {
            return "JAXBElement#value";
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public boolean isCollection() {
            return ElementInfoImpl.this.isCollection;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
        public boolean isValueList() {
            return ElementInfoImpl.this.isCollection;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
        public boolean isRequired() {
            return true;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public PropertyKind kind() {
            return PropertyKind.ELEMENT;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo, com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public Adapter<T, C> getAdapter() {
            return ElementInfoImpl.this.adapter;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public ID id() {
            return ElementInfoImpl.this.id;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public MimeType getExpectedMimeType() {
            return ElementInfoImpl.this.expectedMimeType;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public QName getSchemaType() {
            return ElementInfoImpl.this.schemaType;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        public boolean inlineBinaryData() {
            return ElementInfoImpl.this.inlineBinary;
        }

        @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
        /* renamed from: getSource */
        public PropertyInfo<T, C> getSource2() {
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
        public <A extends Annotation> A readAnnotation(Class<A> cls) {
            return (A) ElementInfoImpl.this.reader().getMethodAnnotation(cls, ElementInfoImpl.this.method, ElementInfoImpl.this);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
        public boolean hasAnnotation(Class<? extends Annotation> cls) {
            return ElementInfoImpl.this.reader().hasMethodAnnotation(cls, ElementInfoImpl.this.method);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ElementInfoImpl(ModelBuilder<T, C, F, M> modelBuilder, RegistryInfoImpl<T, C, F, M> registryInfoImpl, M m2) throws IllegalAnnotationException {
        super(modelBuilder, registryInfoImpl);
        this.method = m2;
        this.anno = (XmlElementDecl) reader().getMethodAnnotation(XmlElementDecl.class, m2, this);
        if (!$assertionsDisabled && this.anno == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !(this.anno instanceof Locatable)) {
            throw new AssertionError();
        }
        this.elementType = nav().getReturnType(m2);
        Object baseClass = nav().getBaseClass(this.elementType, nav().asDecl(JAXBElement.class));
        if (baseClass == null) {
            throw new IllegalAnnotationException(Messages.XML_ELEMENT_MAPPING_ON_NON_IXMLELEMENT_METHOD.format(nav().getMethodName(m2)), this.anno);
        }
        this.tagName = parseElementName(this.anno);
        T[] methodParameters = nav().getMethodParameters(m2);
        Adapter<T, C> adapter = null;
        if (methodParameters.length > 0) {
            XmlJavaTypeAdapter xmlJavaTypeAdapter = (XmlJavaTypeAdapter) reader().getMethodAnnotation(XmlJavaTypeAdapter.class, m2, this);
            if (xmlJavaTypeAdapter != null) {
                adapter = new Adapter<>(xmlJavaTypeAdapter, reader(), nav());
            } else if (((XmlAttachmentRef) reader().getMethodAnnotation(XmlAttachmentRef.class, m2, this)) != null) {
                TODO.prototype("in Annotation Processing swaRefAdapter isn't avaialble, so this returns null");
                adapter = new Adapter<>(this.owner.nav.asDecl(SwaRefAdapter.class), this.owner.nav);
            }
        }
        this.adapter = adapter;
        this.tOfJAXBElementT = methodParameters.length > 0 ? methodParameters[0] : (T) nav().getTypeArgument(baseClass, 0);
        if (this.adapter == null) {
            Object baseClass2 = nav().getBaseClass(this.tOfJAXBElementT, nav().asDecl(List.class));
            if (baseClass2 == null) {
                this.isCollection = false;
                this.contentType = modelBuilder.getTypeInfo(this.tOfJAXBElementT, this);
            } else {
                this.isCollection = true;
                this.contentType = modelBuilder.getTypeInfo(nav().getTypeArgument(baseClass2, 0), this);
            }
        } else {
            this.contentType = modelBuilder.getTypeInfo(this.adapter.defaultType, this);
            this.isCollection = false;
        }
        T classValue = reader().getClassValue2(this.anno, "scope");
        if (nav().isSameType(classValue, nav().ref(XmlElementDecl.GLOBAL.class))) {
            this.scope = null;
        } else {
            NonElement classInfo = modelBuilder.getClassInfo(nav().asDecl((Navigator<T, C, F, M>) classValue), this);
            if (!(classInfo instanceof ClassInfo)) {
                throw new IllegalAnnotationException(Messages.SCOPE_IS_NOT_COMPLEXTYPE.format(nav().getTypeName(classValue)), this.anno);
            }
            this.scope = (ClassInfo) classInfo;
        }
        this.id = calcId();
        this.property = createPropertyImpl();
        this.expectedMimeType = Util.calcExpectedMediaType(this.property, modelBuilder);
        this.inlineBinary = reader().hasMethodAnnotation(XmlInlineBinaryData.class, this.method);
        this.schemaType = Util.calcSchemaType(reader(), this.property, registryInfoImpl.registryClass, getContentInMemoryType(), this);
    }

    final QName parseElementName(XmlElementDecl xmlElementDecl) {
        String strName = xmlElementDecl.name();
        String strNamespace = xmlElementDecl.namespace();
        if (strNamespace.equals("##default")) {
            XmlSchema xmlSchema = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, nav().getDeclaringClassForMethod(this.method), this);
            if (xmlSchema != null) {
                strNamespace = xmlSchema.namespace();
            } else {
                strNamespace = this.builder.defaultNsUri;
            }
        }
        return new QName(strNamespace.intern(), strName.intern());
    }

    protected ElementInfoImpl<T, C, F, M>.PropertyImpl createPropertyImpl() {
        return new PropertyImpl();
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo
    /* renamed from: getProperty */
    public ElementPropertyInfo<T, C> getProperty2() {
        return this.property;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo
    /* renamed from: getContentType */
    public NonElement<T, C> getContentType2() {
        return this.contentType;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo
    public T getContentInMemoryType() {
        if (this.adapter == null) {
            return this.tOfJAXBElementT;
        }
        return this.adapter.customType;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.Element
    public QName getElementName() {
        return this.tagName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    /* renamed from: getType */
    public T getType2() {
        return this.elementType;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    public final boolean canBeReferencedByIDREF() {
        return false;
    }

    private ID calcId() {
        if (reader().hasMethodAnnotation(XmlID.class, this.method)) {
            return ID.ID;
        }
        if (reader().hasMethodAnnotation(XmlIDREF.class, this.method)) {
            return ID.IDREF;
        }
        return ID.NONE;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.Element
    /* renamed from: getScope */
    public ClassInfo<T, C> getScope2() {
        return this.scope;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo, com.sun.xml.internal.bind.v2.model.core.Element
    public ElementInfo<T, C> getSubstitutionHead() {
        return this.substitutionHead;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo
    public Collection<? extends ElementInfoImpl<T, C, F, M>> getSubstitutionMembers() {
        if (this.substitutionMembers == null) {
            return Collections.emptyList();
        }
        return this.substitutionMembers;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoImpl
    void link() {
        if (this.anno.substitutionHeadName().length() != 0) {
            QName name = new QName(this.anno.substitutionHeadNamespace(), this.anno.substitutionHeadName());
            this.substitutionHead = this.owner.getElementInfo((TypeInfoSet) null, name);
            if (this.substitutionHead == null) {
                this.builder.reportError(new IllegalAnnotationException(Messages.NON_EXISTENT_ELEMENT_MAPPING.format(name.getNamespaceURI(), name.getLocalPart()), this.anno));
            } else {
                this.substitutionHead.addSubstitutionMember(this);
            }
        } else {
            this.substitutionHead = null;
        }
        super.link();
    }

    private void addSubstitutionMember(ElementInfoImpl<T, C, F, M> child) {
        if (this.substitutionMembers == null) {
            this.substitutionMembers = new FinalArrayList<>();
        }
        this.substitutionMembers.add(child);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return nav().getMethodLocation(this.method);
    }
}
