package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import com.sun.xml.internal.bind.util.Which;
import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.annotation.ClassLocatable;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.Ref;
import com.sun.xml.internal.bind.v2.model.core.RegistryInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/ModelBuilder.class */
public class ModelBuilder<T, C, F, M> implements ModelBuilderI<T, C, F, M> {
    private static final Logger logger;
    final TypeInfoSetImpl<T, C, F, M> typeInfoSet;
    public final AnnotationReader<T, C, F, M> reader;
    public final Navigator<T, C, F, M> nav;
    public final String defaultNsUri;
    private final Map<C, C> subclassReplacements;
    private ErrorHandler errorHandler;
    private boolean hadError;
    public boolean hasSwaRef;
    private boolean linked;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Map<QName, TypeInfo> typeNames = new HashMap();
    final Map<String, RegistryInfoImpl<T, C, F, M>> registries = new HashMap();
    private final ErrorHandler proxyErrorHandler = new ErrorHandler() { // from class: com.sun.xml.internal.bind.v2.model.impl.ModelBuilder.1
        @Override // com.sun.xml.internal.bind.v2.model.core.ErrorHandler
        public void error(IllegalAnnotationException e2) {
            ModelBuilder.this.reportError(e2);
        }
    };

    static {
        Messages res;
        $assertionsDisabled = !ModelBuilder.class.desiredAssertionStatus();
        try {
            XmlSchema s2 = null;
            s2.location();
        } catch (NoSuchMethodError e2) {
            if (SecureLoader.getClassClassLoader(XmlSchema.class) == null) {
                res = Messages.INCOMPATIBLE_API_VERSION_MUSTANG;
            } else {
                res = Messages.INCOMPATIBLE_API_VERSION;
            }
            throw new LinkageError(res.format(Which.which(XmlSchema.class), Which.which(ModelBuilder.class)));
        } catch (NullPointerException e3) {
        }
        try {
            WhiteSpaceProcessor.isWhiteSpace("xyz");
            logger = Logger.getLogger(ModelBuilder.class.getName());
        } catch (NoSuchMethodError e4) {
            throw new LinkageError(Messages.RUNNING_WITH_1_0_RUNTIME.format(Which.which(WhiteSpaceProcessor.class), Which.which(ModelBuilder.class)));
        }
    }

    public ModelBuilder(AnnotationReader<T, C, F, M> reader, Navigator<T, C, F, M> navigator, Map<C, C> subclassReplacements, String defaultNamespaceRemap) {
        this.reader = reader;
        this.nav = navigator;
        this.subclassReplacements = subclassReplacements;
        this.defaultNsUri = defaultNamespaceRemap == null ? "" : defaultNamespaceRemap;
        reader.setErrorHandler(this.proxyErrorHandler);
        this.typeInfoSet = createTypeInfoSet2();
    }

    /* renamed from: createTypeInfoSet */
    protected TypeInfoSetImpl<T, C, F, M> createTypeInfoSet2() {
        return new TypeInfoSetImpl<>(this.nav, this.reader, BuiltinLeafInfoImpl.createLeaves(this.nav));
    }

    public NonElement<T, C> getClassInfo(C clazz, Locatable upstream) {
        return getClassInfo(clazz, false, upstream);
    }

    public NonElement<T, C> getClassInfo(C clazz, boolean searchForSuperClass, Locatable upstream) {
        NonElement<T, C> r2;
        if (!$assertionsDisabled && clazz == null) {
            throw new AssertionError();
        }
        NonElement<T, C> r3 = this.typeInfoSet.getClassInfo(clazz);
        if (r3 != null) {
            return r3;
        }
        if (this.nav.isEnum(clazz)) {
            EnumLeafInfoImpl<T, C, F, M> li = createEnumLeafInfo(clazz, upstream);
            this.typeInfoSet.add(li);
            r2 = li;
            addTypeName(r2);
        } else {
            boolean isReplaced = this.subclassReplacements.containsKey(clazz);
            if (isReplaced && !searchForSuperClass) {
                r2 = getClassInfo(this.subclassReplacements.get(clazz), upstream);
            } else if (this.reader.hasClassAnnotation(clazz, XmlTransient.class) || isReplaced) {
                r2 = getClassInfo(this.nav.getSuperClass(clazz), searchForSuperClass, new ClassLocatable(upstream, clazz, this.nav));
            } else {
                ClassInfoImpl<T, C, F, M> ci = createClassInfo(clazz, upstream);
                this.typeInfoSet.add(ci);
                for (PropertyInfo<T, C> p2 : ci.getProperties()) {
                    if (p2.kind() == PropertyKind.REFERENCE) {
                        addToRegistry(clazz, (Locatable) p2);
                        Class[] prmzdClasses = getParametrizedTypes(p2);
                        if (prmzdClasses != null) {
                            for (Class prmzdClass : prmzdClasses) {
                                if (prmzdClass != clazz) {
                                    addToRegistry(prmzdClass, (Locatable) p2);
                                }
                            }
                        }
                    }
                    for (TypeInfo<T, C> typeInfo : p2.ref()) {
                    }
                }
                ci.getBaseClass2();
                r2 = ci;
                addTypeName(r2);
            }
        }
        XmlSeeAlso sa = (XmlSeeAlso) this.reader.getClassAnnotation(XmlSeeAlso.class, clazz, upstream);
        if (sa != null) {
            for (T t2 : this.reader.getClassArrayValue(sa, "value")) {
                getTypeInfo(t2, (Locatable) sa);
            }
        }
        return r2;
    }

    private void addToRegistry(C clazz, Locatable p2) {
        C c2;
        String pkg = this.nav.getPackageName(clazz);
        if (!this.registries.containsKey(pkg) && (c2 = this.nav.loadObjectFactory(clazz, pkg)) != null) {
            addRegistry(c2, p2);
        }
    }

    private Class[] getParametrizedTypes(PropertyInfo p2) {
        try {
            Type pType = ((RuntimePropertyInfo) p2).getIndividualType();
            if (pType instanceof ParameterizedType) {
                ParameterizedType prmzdType = (ParameterizedType) pType;
                if (prmzdType.getRawType() == JAXBElement.class) {
                    Type[] actualTypes = prmzdType.getActualTypeArguments();
                    Class[] result = new Class[actualTypes.length];
                    for (int i2 = 0; i2 < actualTypes.length; i2++) {
                        result[i2] = (Class) actualTypes[i2];
                    }
                    return result;
                }
                return null;
            }
            return null;
        } catch (Exception e2) {
            logger.log(Level.FINE, "Error in ModelBuilder.getParametrizedTypes. " + e2.getMessage());
            return null;
        }
    }

    private void addTypeName(NonElement<T, C> r2) {
        TypeInfo old;
        QName t2 = r2.getTypeName();
        if (t2 != null && (old = this.typeNames.put(t2, r2)) != null) {
            reportError(new IllegalAnnotationException(Messages.CONFLICTING_XML_TYPE_MAPPING.format(r2.getTypeName()), old, r2));
        }
    }

    public NonElement<T, C> getTypeInfo(T t2, Locatable upstream) {
        NonElement<T, C> r2 = this.typeInfoSet.getTypeInfo((TypeInfoSetImpl<T, C, F, M>) t2);
        if (r2 != null) {
            return r2;
        }
        if (this.nav.isArray(t2)) {
            ArrayInfoImpl<T, C, F, M> ai2 = createArrayInfo(upstream, t2);
            addTypeName(ai2);
            this.typeInfoSet.add(ai2);
            return ai2;
        }
        C c2 = this.nav.asDecl((Navigator<T, C, F, M>) t2);
        if ($assertionsDisabled || c2 != null) {
            return getClassInfo(c2, upstream);
        }
        throw new AssertionError((Object) (t2.toString() + " must be a leaf, but we failed to recognize it."));
    }

    public NonElement<T, C> getTypeInfo(Ref<T, C> ref) {
        if (!$assertionsDisabled && ref.valueList) {
            throw new AssertionError();
        }
        C c2 = this.nav.asDecl((Navigator<T, C, F, M>) ref.type);
        if (c2 != null && this.reader.getClassAnnotation(XmlRegistry.class, c2, null) != null) {
            if (!this.registries.containsKey(this.nav.getPackageName(c2))) {
                addRegistry(c2, null);
                return null;
            }
            return null;
        }
        return getTypeInfo(ref.type, null);
    }

    protected EnumLeafInfoImpl<T, C, F, M> createEnumLeafInfo(C clazz, Locatable upstream) {
        return new EnumLeafInfoImpl<>(this, upstream, clazz, this.nav.use(clazz));
    }

    protected ClassInfoImpl<T, C, F, M> createClassInfo(C clazz, Locatable upstream) {
        return new ClassInfoImpl<>(this, upstream, clazz);
    }

    protected ElementInfoImpl<T, C, F, M> createElementInfo(RegistryInfoImpl<T, C, F, M> registryInfo, M m2) throws IllegalAnnotationException {
        return new ElementInfoImpl<>(this, registryInfo, m2);
    }

    protected ArrayInfoImpl<T, C, F, M> createArrayInfo(Locatable upstream, T arrayType) {
        return new ArrayInfoImpl<>(this, upstream, arrayType);
    }

    public RegistryInfo<T, C> addRegistry(C registryClass, Locatable upstream) {
        return new RegistryInfoImpl(this, upstream, registryClass);
    }

    public RegistryInfo<T, C> getRegistry(String packageName) {
        return this.registries.get(packageName);
    }

    /* renamed from: link */
    public TypeInfoSet<T, C, F, M> link2() throws ClassInfoImpl.ConflictException {
        if (!$assertionsDisabled && this.linked) {
            throw new AssertionError();
        }
        this.linked = true;
        for (ElementInfoImpl ei : this.typeInfoSet.getAllElements()) {
            ei.link();
        }
        for (ClassInfoImpl ci : this.typeInfoSet.beans().values()) {
            ci.link();
        }
        for (EnumLeafInfoImpl li : this.typeInfoSet.enums().values()) {
            li.link();
        }
        if (this.hadError) {
            return null;
        }
        return this.typeInfoSet;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public final void reportError(IllegalAnnotationException e2) {
        this.hadError = true;
        if (this.errorHandler != null) {
            this.errorHandler.error(e2);
        }
    }

    public boolean isReplaced(C sc) {
        return this.subclassReplacements.containsKey(sc);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilderI
    public Navigator<T, C, F, M> getNavigator() {
        return this.nav;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilderI
    public AnnotationReader<T, C, F, M> getReader() {
        return this.reader;
    }
}
