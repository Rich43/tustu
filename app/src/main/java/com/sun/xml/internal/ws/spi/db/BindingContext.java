package com.sun.xml.internal.ws.spi.db;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/BindingContext.class */
public interface BindingContext {
    public static final String DEFAULT_NAMESPACE_REMAP = "com.sun.xml.internal.bind.defaultNamespaceRemap";
    public static final String TYPE_REFERENCES = "com.sun.xml.internal.bind.typeReferences";
    public static final String CANONICALIZATION_SUPPORT = "com.sun.xml.internal.bind.c14n";
    public static final String TREAT_EVERYTHING_NILLABLE = "com.sun.xml.internal.bind.treatEverythingNillable";
    public static final String ENABLE_XOP = "com.sun.xml.internal.bind.XOP";
    public static final String SUBCLASS_REPLACEMENTS = "com.sun.xml.internal.bind.subclassReplacements";
    public static final String XMLACCESSORFACTORY_SUPPORT = "com.sun.xml.internal.bind.XmlAccessorFactory";
    public static final String RETAIN_REFERENCE_TO_INFO = "retainReferenceToInfo";

    Marshaller createMarshaller() throws JAXBException;

    Unmarshaller createUnmarshaller() throws JAXBException;

    JAXBContext getJAXBContext();

    Object newWrapperInstace(Class<?> cls) throws IllegalAccessException, InstantiationException;

    boolean hasSwaRef();

    @Nullable
    QName getElementName(@NotNull Object obj) throws JAXBException;

    @Nullable
    QName getElementName(@NotNull Class cls) throws JAXBException;

    XMLBridge createBridge(@NotNull TypeInfo typeInfo);

    XMLBridge createFragmentBridge();

    <B, V> PropertyAccessor<B, V> getElementPropertyAccessor(Class<B> cls, String str, String str2) throws JAXBException;

    @NotNull
    List<String> getKnownNamespaceURIs();

    void generateSchema(@NotNull SchemaOutputResolver schemaOutputResolver) throws IOException;

    QName getTypeName(@NotNull TypeInfo typeInfo);

    @NotNull
    String getBuildId();
}
