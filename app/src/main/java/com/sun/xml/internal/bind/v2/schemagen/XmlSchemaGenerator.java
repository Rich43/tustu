package com.sun.xml.internal.bind.v2.schemagen;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.api.CompositeStructure;
import com.sun.xml.internal.bind.api.ErrorListener;
import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.bind.v2.WellKnownNamespace;
import com.sun.xml.internal.bind.v2.model.core.Adapter;
import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
import com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.EnumConstant;
import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.MaybeElement;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.NonElementRef;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import com.sun.xml.internal.bind.v2.model.core.ValuePropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
import com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapter;
import com.sun.xml.internal.bind.v2.schemagen.Tree;
import com.sun.xml.internal.bind.v2.schemagen.episode.Bindings;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Any;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttrDecls;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttributeType;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexExtension;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexType;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexTypeHost;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ContentModelContainer;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ExplicitGroup;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalAttribute;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalElement;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Schema;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleExtension;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleRestrictionModel;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleType;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleTypeHost;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TopLevelAttribute;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TopLevelElement;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeDefParticle;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeHost;
import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;
import com.sun.xml.internal.bind.v2.util.StackRecorder;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.txw2.TXW;
import com.sun.xml.internal.txw2.TxwException;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.output.ResultFactory;
import com.sun.xml.internal.txw2.output.XmlSerializer;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimeType;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/XmlSchemaGenerator.class */
public final class XmlSchemaGenerator<T, C, F, M> {
    private static final Logger logger;
    private ErrorListener errorListener;
    private Navigator<T, C, F, M> navigator;
    private final TypeInfoSet<T, C, F, M> types;
    private final NonElement<T, C> stringType;
    private final NonElement<T, C> anyType;
    private static final Comparator<String> NAMESPACE_COMPARATOR;
    private static final String newline = "\n";
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Map<String, XmlSchemaGenerator<T, C, F, M>.Namespace> namespaces = new TreeMap(NAMESPACE_COMPARATOR);
    private final CollisionCheckStack<ClassInfo<T, C>> collisionChecker = new CollisionCheckStack<>();

    static {
        $assertionsDisabled = !XmlSchemaGenerator.class.desiredAssertionStatus();
        logger = com.sun.xml.internal.bind.Util.getClassLogger();
        NAMESPACE_COMPARATOR = new Comparator<String>() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.1
            @Override // java.util.Comparator
            public int compare(String lhs, String rhs) {
                return -lhs.compareTo(rhs);
            }
        };
    }

    public XmlSchemaGenerator(Navigator<T, C, F, M> navigator, TypeInfoSet<T, C, F, M> types) {
        this.navigator = navigator;
        this.types = types;
        this.stringType = types.getTypeInfo((TypeInfoSet<T, C, F, M>) navigator.ref2(String.class));
        this.anyType = types.getAnyTypeInfo2();
        for (ClassInfo<T, C> ci : types.beans().values()) {
            add(ci);
        }
        for (ElementInfo<T, C> ei1 : types.getElementMappings(null).values()) {
            add(ei1);
        }
        for (EnumLeafInfo<T, C> ei : types.enums().values()) {
            add(ei);
        }
        for (ArrayInfo<T, C> a2 : types.arrays().values()) {
            add(a2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public XmlSchemaGenerator<T, C, F, M>.Namespace getNamespace(String uri) {
        XmlSchemaGenerator<T, C, F, M>.Namespace n2 = this.namespaces.get(uri);
        if (n2 == null) {
            Map<String, XmlSchemaGenerator<T, C, F, M>.Namespace> map = this.namespaces;
            XmlSchemaGenerator<T, C, F, M>.Namespace namespace = new Namespace(uri);
            n2 = namespace;
            map.put(uri, namespace);
        }
        return n2;
    }

    public void add(ClassInfo<T, C> clazz) {
        if (!$assertionsDisabled && clazz == null) {
            throw new AssertionError();
        }
        String nsUri = null;
        if (clazz.getClazz() == this.navigator.asDecl(CompositeStructure.class)) {
            return;
        }
        if (clazz.isElement()) {
            nsUri = clazz.getElementName().getNamespaceURI();
            XmlSchemaGenerator<T, C, F, M>.Namespace ns = getNamespace(nsUri);
            ((Namespace) ns).classes.add(clazz);
            ns.addDependencyTo(clazz.getTypeName());
            add(clazz.getElementName(), false, clazz);
        }
        QName tn = clazz.getTypeName();
        if (tn != null) {
            nsUri = tn.getNamespaceURI();
        } else if (nsUri == null) {
            return;
        }
        XmlSchemaGenerator<T, C, F, M>.Namespace n2 = getNamespace(nsUri);
        ((Namespace) n2).classes.add(clazz);
        for (PropertyInfo<T, C> p2 : clazz.getProperties()) {
            n2.processForeignNamespaces(p2, 1);
            if (p2 instanceof AttributePropertyInfo) {
                AttributePropertyInfo<T, C> ap2 = (AttributePropertyInfo) p2;
                String aUri = ap2.getXmlName().getNamespaceURI();
                if (aUri.length() > 0) {
                    getNamespace(aUri).addGlobalAttribute(ap2);
                    n2.addDependencyTo(ap2.getXmlName());
                }
            }
            if (p2 instanceof ElementPropertyInfo) {
                ElementPropertyInfo<T, C> ep = (ElementPropertyInfo) p2;
                for (TypeRef<T, C> tref : ep.getTypes()) {
                    String eUri = tref.getTagName().getNamespaceURI();
                    if (eUri.length() > 0 && !eUri.equals(n2.uri)) {
                        getNamespace(eUri).addGlobalElement(tref);
                        n2.addDependencyTo(tref.getTagName());
                    }
                }
            }
            if (generateSwaRefAdapter(p2)) {
                ((Namespace) n2).useSwaRef = true;
            }
            MimeType mimeType = p2.getExpectedMimeType();
            if (mimeType != null) {
                ((Namespace) n2).useMimeNs = true;
            }
        }
        ClassInfo<T, C> bc2 = clazz.getBaseClass2();
        if (bc2 != null) {
            add(bc2);
            n2.addDependencyTo(bc2.getTypeName());
        }
    }

    public void add(ElementInfo<T, C> elem) {
        ElementInfo ei;
        boolean nillable;
        if (!$assertionsDisabled && elem == null) {
            throw new AssertionError();
        }
        QName name = elem.getElementName();
        XmlSchemaGenerator<T, C, F, M>.Namespace n2 = getNamespace(name.getNamespaceURI());
        if (elem.getScope2() != null) {
            ei = this.types.getElementInfo(elem.getScope2().getClazz(), name);
        } else {
            ei = this.types.getElementInfo(null, name);
        }
        XmlElement xmlElem = (XmlElement) ei.getProperty2().readAnnotation(XmlElement.class);
        if (xmlElem == null) {
            nillable = false;
        } else {
            nillable = xmlElem.nillable();
        }
        MultiMap multiMap = ((Namespace) n2).elementDecls;
        String localPart = name.getLocalPart();
        n2.getClass();
        multiMap.put((MultiMap) localPart, (String) new Namespace.ElementWithType(nillable, elem.getContentType2()));
        n2.processForeignNamespaces(elem.getProperty2(), 1);
    }

    public void add(EnumLeafInfo<T, C> envm) {
        if (!$assertionsDisabled && envm == null) {
            throw new AssertionError();
        }
        String nsUri = null;
        if (envm.isElement()) {
            nsUri = envm.getElementName().getNamespaceURI();
            XmlSchemaGenerator<T, C, F, M>.Namespace ns = getNamespace(nsUri);
            ((Namespace) ns).enums.add(envm);
            ns.addDependencyTo(envm.getTypeName());
            add(envm.getElementName(), false, envm);
        }
        QName typeName = envm.getTypeName();
        if (typeName != null) {
            nsUri = typeName.getNamespaceURI();
        } else if (nsUri == null) {
            return;
        }
        XmlSchemaGenerator<T, C, F, M>.Namespace n2 = getNamespace(nsUri);
        ((Namespace) n2).enums.add(envm);
        n2.addDependencyTo(envm.getBaseType().getTypeName());
    }

    public void add(ArrayInfo<T, C> a2) {
        if (!$assertionsDisabled && a2 == null) {
            throw new AssertionError();
        }
        String namespaceURI = a2.getTypeName().getNamespaceURI();
        XmlSchemaGenerator<T, C, F, M>.Namespace n2 = getNamespace(namespaceURI);
        ((Namespace) n2).arrays.add(a2);
        n2.addDependencyTo(a2.getItemType2().getTypeName());
    }

    public void add(QName tagName, boolean isNillable, NonElement<T, C> type) {
        if (type != null && type.getType2() == this.navigator.ref2(CompositeStructure.class)) {
            return;
        }
        XmlSchemaGenerator<T, C, F, M>.Namespace n2 = getNamespace(tagName.getNamespaceURI());
        MultiMap multiMap = ((Namespace) n2).elementDecls;
        String localPart = tagName.getLocalPart();
        n2.getClass();
        multiMap.put((MultiMap) localPart, (String) new Namespace.ElementWithType(isNillable, type));
        if (type == null) {
            return;
        }
        n2.addDependencyTo(type.getTypeName());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void writeEpisodeFile(XmlSerializer xmlSerializer) {
        String str;
        Bindings bindings = (Bindings) TXW.create(Bindings.class, xmlSerializer);
        if (this.namespaces.containsKey("")) {
            bindings._namespace(WellKnownNamespace.JAXB, "jaxb");
        }
        bindings.version("2.1");
        for (Map.Entry<String, XmlSchemaGenerator<T, C, F, M>.Namespace> entry : this.namespaces.entrySet()) {
            Bindings bindings2 = bindings.bindings();
            String key = entry.getKey();
            if (!key.equals("")) {
                bindings2._namespace(key, "tns");
                str = "tns:";
            } else {
                str = "";
            }
            bindings2.scd("x-schema::" + (key.equals("") ? "" : "tns"));
            bindings2.schemaBindings().map(false);
            for (ClassInfo classInfo : ((Namespace) entry.getValue()).classes) {
                if (classInfo.getTypeName() != null) {
                    if (classInfo.getTypeName().getNamespaceURI().equals(key)) {
                        Bindings bindings3 = bindings2.bindings();
                        bindings3.scd('~' + str + classInfo.getTypeName().getLocalPart());
                        bindings3.klass().ref(classInfo.getName());
                    }
                    if (classInfo.isElement() && classInfo.getElementName().getNamespaceURI().equals(key)) {
                        Bindings bindings4 = bindings2.bindings();
                        bindings4.scd(str + classInfo.getElementName().getLocalPart());
                        bindings4.klass().ref(classInfo.getName());
                    }
                }
            }
            for (EnumLeafInfo enumLeafInfo : ((Namespace) entry.getValue()).enums) {
                if (enumLeafInfo.getTypeName() != null) {
                    Bindings bindings5 = bindings2.bindings();
                    bindings5.scd('~' + str + enumLeafInfo.getTypeName().getLocalPart());
                    bindings5.klass().ref(this.navigator.getClassName(enumLeafInfo.getClazz()));
                }
            }
            bindings2.commit(true);
        }
        bindings.commit();
    }

    public void write(SchemaOutputResolver resolver, ErrorListener errorListener) throws IOException {
        if (resolver == null) {
            throw new IllegalArgumentException();
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Writing XML Schema for " + toString(), (Throwable) new StackRecorder());
        }
        SchemaOutputResolver resolver2 = new FoolProofResolver(resolver);
        this.errorListener = errorListener;
        Map<String, String> schemaLocations = this.types.getSchemaLocations();
        Map<XmlSchemaGenerator<T, C, F, M>.Namespace, Result> out = new HashMap<>();
        Map<XmlSchemaGenerator<T, C, F, M>.Namespace, String> systemIds = new HashMap<>();
        this.namespaces.remove("http://www.w3.org/2001/XMLSchema");
        for (XmlSchemaGenerator<T, C, F, M>.Namespace n2 : this.namespaces.values()) {
            String schemaLocation = schemaLocations.get(n2.uri);
            if (schemaLocation != null) {
                systemIds.put(n2, schemaLocation);
            } else {
                Result output = resolver2.createOutput(n2.uri, "schema" + (out.size() + 1) + ".xsd");
                if (output != null) {
                    out.put(n2, output);
                    systemIds.put(n2, output.getSystemId());
                }
            }
            n2.resetWritten();
        }
        for (Map.Entry<XmlSchemaGenerator<T, C, F, M>.Namespace, Result> e2 : out.entrySet()) {
            Result result = e2.getValue();
            e2.getKey().writeTo(result, systemIds);
            if (result instanceof StreamResult) {
                OutputStream outputStream = ((StreamResult) result).getOutputStream();
                if (outputStream != null) {
                    outputStream.close();
                } else {
                    Writer writer = ((StreamResult) result).getWriter();
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/XmlSchemaGenerator$Namespace.class */
    private class Namespace {

        @NotNull
        final String uri;
        private boolean selfReference;
        private final MultiMap<String, XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration> elementDecls;
        private Form attributeFormDefault;
        private Form elementFormDefault;
        private boolean useSwaRef;
        private boolean useMimeNs;
        static final /* synthetic */ boolean $assertionsDisabled;
        private final Set<XmlSchemaGenerator<T, C, F, M>.Namespace> depends = new LinkedHashSet();
        private final Set<ClassInfo<T, C>> classes = new LinkedHashSet();
        private final Set<EnumLeafInfo<T, C>> enums = new LinkedHashSet();
        private final Set<ArrayInfo<T, C>> arrays = new LinkedHashSet();
        private final MultiMap<String, AttributePropertyInfo<T, C>> attributeDecls = new MultiMap<>(null);
        private final Set<ClassInfo> written = new HashSet();

        static {
            $assertionsDisabled = !XmlSchemaGenerator.class.desiredAssertionStatus();
        }

        public Namespace(String uri) {
            this.elementDecls = new MultiMap<>(new ElementWithType(true, XmlSchemaGenerator.this.anyType));
            this.uri = uri;
            if (!$assertionsDisabled && XmlSchemaGenerator.this.namespaces.containsKey(uri)) {
                throw new AssertionError();
            }
            XmlSchemaGenerator.this.namespaces.put(uri, this);
        }

        void resetWritten() {
            this.written.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processForeignNamespaces(PropertyInfo<T, C> p2, int processingDepth) {
            for (TypeInfo<T, C> t2 : p2.ref2()) {
                if ((t2 instanceof ClassInfo) && processingDepth > 0) {
                    List<PropertyInfo> l2 = ((ClassInfo) t2).getProperties();
                    for (PropertyInfo subp : l2) {
                        processingDepth--;
                        processForeignNamespaces(subp, processingDepth);
                    }
                }
                if (t2 instanceof Element) {
                    addDependencyTo(((Element) t2).getElementName());
                }
                if (t2 instanceof NonElement) {
                    addDependencyTo(((NonElement) t2).getTypeName());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addDependencyTo(@Nullable QName qname) {
            if (qname == null) {
                return;
            }
            String nsUri = qname.getNamespaceURI();
            if (nsUri.equals("http://www.w3.org/2001/XMLSchema")) {
                return;
            }
            if (!nsUri.equals(this.uri)) {
                this.depends.add(XmlSchemaGenerator.this.getNamespace(nsUri));
            } else {
                this.selfReference = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTo(Result result, Map<XmlSchemaGenerator<T, C, F, M>.Namespace, String> systemIds) throws IOException {
            try {
                Schema schema = (Schema) TXW.create(Schema.class, ResultFactory.createSerializer(result));
                Map<String, String> xmlNs = XmlSchemaGenerator.this.types.getXmlNs(this.uri);
                for (Map.Entry<String, String> e2 : xmlNs.entrySet()) {
                    schema._namespace(e2.getValue(), e2.getKey());
                }
                if (this.useSwaRef) {
                    schema._namespace(WellKnownNamespace.SWA_URI, "swaRef");
                }
                if (this.useMimeNs) {
                    schema._namespace(WellKnownNamespace.XML_MIME_URI, "xmime");
                }
                this.attributeFormDefault = Form.get(XmlSchemaGenerator.this.types.getAttributeFormDefault(this.uri));
                this.attributeFormDefault.declare("attributeFormDefault", schema);
                this.elementFormDefault = Form.get(XmlSchemaGenerator.this.types.getElementFormDefault(this.uri));
                this.elementFormDefault.declare("elementFormDefault", schema);
                if (!xmlNs.containsValue("http://www.w3.org/2001/XMLSchema") && !xmlNs.containsKey(NameImpl.XML_SCHEMA_NAMESPACE_PREFIX)) {
                    schema._namespace("http://www.w3.org/2001/XMLSchema", NameImpl.XML_SCHEMA_NAMESPACE_PREFIX);
                }
                schema.version("1.0");
                if (this.uri.length() != 0) {
                    schema.targetNamespace(this.uri);
                }
                for (XmlSchemaGenerator<T, C, F, M>.Namespace ns : this.depends) {
                    schema._namespace(ns.uri);
                }
                if (this.selfReference && this.uri.length() != 0) {
                    schema._namespace(this.uri, "tns");
                }
                schema._pcdata("\n");
                for (XmlSchemaGenerator<T, C, F, M>.Namespace n2 : this.depends) {
                    Import imp = schema._import();
                    if (n2.uri.length() != 0) {
                        imp.namespace(n2.uri);
                    }
                    String refSystemId = systemIds.get(n2);
                    if (refSystemId != null && !refSystemId.equals("")) {
                        imp.schemaLocation(XmlSchemaGenerator.relativize(refSystemId, result.getSystemId()));
                    }
                    schema._pcdata("\n");
                }
                if (this.useSwaRef) {
                    schema._import().namespace(WellKnownNamespace.SWA_URI).schemaLocation("http://ws-i.org/profiles/basic/1.1/swaref.xsd");
                }
                if (this.useMimeNs) {
                    schema._import().namespace(WellKnownNamespace.XML_MIME_URI).schemaLocation(WellKnownNamespace.XML_MIME_URI);
                }
                for (Map.Entry<String, XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration> e3 : this.elementDecls.entrySet()) {
                    e3.getValue().writeTo(e3.getKey(), schema);
                    schema._pcdata("\n");
                }
                for (ClassInfo<T, C> c2 : this.classes) {
                    if (c2.getTypeName() != null) {
                        if (this.uri.equals(c2.getTypeName().getNamespaceURI())) {
                            writeClass(c2, schema);
                        }
                        schema._pcdata("\n");
                    }
                }
                for (EnumLeafInfo<T, C> e4 : this.enums) {
                    if (e4.getTypeName() != null) {
                        if (this.uri.equals(e4.getTypeName().getNamespaceURI())) {
                            writeEnum(e4, schema);
                        }
                        schema._pcdata("\n");
                    }
                }
                Iterator<ArrayInfo<T, C>> it = this.arrays.iterator();
                while (it.hasNext()) {
                    writeArray(it.next(), schema);
                    schema._pcdata("\n");
                }
                for (Map.Entry<String, AttributePropertyInfo<T, C>> e5 : this.attributeDecls.entrySet()) {
                    TopLevelAttribute a2 = schema.attribute();
                    a2.name(e5.getKey());
                    if (e5.getValue() == null) {
                        writeTypeRef(a2, XmlSchemaGenerator.this.stringType, "type");
                    } else {
                        writeAttributeTypeRef(e5.getValue(), a2);
                    }
                    schema._pcdata("\n");
                }
                schema.commit();
            } catch (TxwException e6) {
                XmlSchemaGenerator.logger.log(Level.INFO, e6.getMessage(), (Throwable) e6);
                throw new IOException(e6.getMessage());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTypeRef(TypeHost th, NonElementRef<T, C> typeRef, String refAttName) {
            switch (typeRef.getSource2().id()) {
                case ID:
                    th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", "ID"));
                    return;
                case IDREF:
                    th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_IDREF));
                    return;
                case NONE:
                    MimeType mimeType = typeRef.getSource2().getExpectedMimeType();
                    if (mimeType != null) {
                        th._attribute(new QName(WellKnownNamespace.XML_MIME_URI, "expectedContentTypes", "xmime"), mimeType.toString());
                    }
                    if (XmlSchemaGenerator.this.generateSwaRefAdapter(typeRef)) {
                        th._attribute(refAttName, new QName(WellKnownNamespace.SWA_URI, "swaRef", "ref"));
                        return;
                    } else if (typeRef.getSource2().getSchemaType() != null) {
                        th._attribute(refAttName, typeRef.getSource2().getSchemaType());
                        return;
                    } else {
                        writeTypeRef(th, typeRef.getTarget2(), refAttName);
                        return;
                    }
                default:
                    throw new IllegalStateException();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTypeRef(TypeHost th, NonElement<T, C> type, String refAttName) {
            Element e2 = null;
            if (type instanceof MaybeElement) {
                MaybeElement me = (MaybeElement) type;
                boolean isElement = me.isElement();
                if (isElement) {
                    e2 = me.asElement();
                }
            }
            if (type instanceof Element) {
                e2 = (Element) type;
            }
            if (type.getTypeName() == null) {
                if (e2 != null && e2.getElementName() != null) {
                    th.block();
                    if (type instanceof ClassInfo) {
                        writeClass((ClassInfo) type, th);
                        return;
                    } else {
                        writeEnum((EnumLeafInfo) type, (SimpleTypeHost) th);
                        return;
                    }
                }
                th.block();
                if (type instanceof ClassInfo) {
                    if (XmlSchemaGenerator.this.collisionChecker.push((ClassInfo) type)) {
                        XmlSchemaGenerator.this.errorListener.warning(new SAXParseException(Messages.ANONYMOUS_TYPE_CYCLE.format(XmlSchemaGenerator.this.collisionChecker.getCycleString()), null));
                    } else {
                        writeClass((ClassInfo) type, th);
                    }
                    XmlSchemaGenerator.this.collisionChecker.pop();
                    return;
                }
                writeEnum((EnumLeafInfo) type, (SimpleTypeHost) th);
                return;
            }
            th._attribute(refAttName, type.getTypeName());
        }

        private void writeArray(ArrayInfo<T, C> a2, Schema schema) {
            ComplexType ct = schema.complexType().name(a2.getTypeName().getLocalPart());
            ct._final(SchemaSymbols.ATTVAL_POUNDALL);
            LocalElement le = ct.sequence().element().name("item");
            le.type(a2.getItemType2().getTypeName());
            le.minOccurs(0).maxOccurs(SchemaSymbols.ATTVAL_UNBOUNDED);
            le.nillable(true);
            ct.commit();
        }

        private void writeEnum(EnumLeafInfo<T, C> e2, SimpleTypeHost th) {
            SimpleType st = th.simpleType();
            writeName(e2, st);
            SimpleRestrictionModel base = st.restriction();
            writeTypeRef(base, e2.getBaseType(), "base");
            for (EnumConstant c2 : e2.getConstants()) {
                base.enumeration().value(c2.getLexicalValue());
            }
            st.commit();
        }

        private void writeClass(ClassInfo<T, C> c2, TypeHost parent) {
            if (this.written.contains(c2)) {
                return;
            }
            this.written.add(c2);
            if (containsValueProp(c2)) {
                if (c2.getProperties().size() == 1) {
                    ValuePropertyInfo<T, C> vp = (ValuePropertyInfo) c2.getProperties().get(0);
                    SimpleType st = ((SimpleTypeHost) parent).simpleType();
                    writeName(c2, st);
                    if (vp.isCollection()) {
                        writeTypeRef(st.list(), vp.getTarget2(), "itemType");
                        return;
                    } else {
                        writeTypeRef(st.restriction(), vp.getTarget2(), "base");
                        return;
                    }
                }
                ComplexType ct = ((ComplexTypeHost) parent).complexType();
                writeName(c2, ct);
                if (c2.isFinal()) {
                    ct._final("extension restriction");
                }
                SimpleExtension se = ct.simpleContent().extension();
                se.block();
                for (PropertyInfo<T, C> p2 : c2.getProperties()) {
                    switch (p2.kind()) {
                        case ATTRIBUTE:
                            handleAttributeProp((AttributePropertyInfo) p2, se);
                            break;
                        case VALUE:
                            TODO.checkSpec("what if vp.isCollection() == true?");
                            se.base(((ValuePropertyInfo) p2).getTarget2().getTypeName());
                            break;
                        case ELEMENT:
                        case REFERENCE:
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError();
                            }
                            throw new IllegalStateException();
                    }
                }
                se.commit();
                TODO.schemaGenerator("figure out what to do if bc != null");
                TODO.checkSpec("handle sec 8.9.5.2, bullet #4");
                return;
            }
            ComplexType ct2 = ((ComplexTypeHost) parent).complexType();
            writeName(c2, ct2);
            if (c2.isFinal()) {
                ct2._final("extension restriction");
            }
            if (c2.isAbstract()) {
                ct2._abstract(true);
            }
            AttrDecls contentModel = ct2;
            TypeDefParticle contentModelOwner = ct2;
            ClassInfo<T, C> bc2 = c2.getBaseClass2();
            if (bc2 != null) {
                if (bc2.hasValueProperty()) {
                    SimpleExtension se2 = ct2.simpleContent().extension();
                    contentModel = se2;
                    contentModelOwner = null;
                    se2.base(bc2.getTypeName());
                } else {
                    ComplexExtension ce = ct2.complexContent().extension();
                    contentModel = ce;
                    contentModelOwner = ce;
                    ce.base(bc2.getTypeName());
                }
            }
            if (contentModelOwner != null) {
                ArrayList<Tree> children = new ArrayList<>();
                for (PropertyInfo<T, C> p3 : c2.getProperties()) {
                    if ((p3 instanceof ReferencePropertyInfo) && ((ReferencePropertyInfo) p3).isMixed()) {
                        ct2.mixed(true);
                    }
                    Tree t2 = buildPropertyContentModel(p3);
                    if (t2 != null) {
                        children.add(t2);
                    }
                }
                Tree top = Tree.makeGroup(c2.isOrdered() ? GroupKind.SEQUENCE : GroupKind.ALL, children);
                top.write(contentModelOwner);
            }
            for (PropertyInfo<T, C> p4 : c2.getProperties()) {
                if (p4 instanceof AttributePropertyInfo) {
                    handleAttributeProp((AttributePropertyInfo) p4, contentModel);
                }
            }
            if (c2.hasAttributeWildcard()) {
                contentModel.anyAttribute().namespace(SchemaSymbols.ATTVAL_TWOPOUNDOTHER).processContents(SchemaSymbols.ATTVAL_SKIP);
            }
            ct2.commit();
        }

        private void writeName(NonElement<T, C> c2, TypedXmlWriter xw) {
            QName tn = c2.getTypeName();
            if (tn != null) {
                xw._attribute("name", tn.getLocalPart());
            }
        }

        private boolean containsValueProp(ClassInfo<T, C> c2) {
            for (PropertyInfo p2 : c2.getProperties()) {
                if (p2 instanceof ValuePropertyInfo) {
                    return true;
                }
            }
            return false;
        }

        private Tree buildPropertyContentModel(PropertyInfo<T, C> p2) {
            switch (p2.kind()) {
                case ATTRIBUTE:
                    return null;
                case VALUE:
                    if ($assertionsDisabled) {
                        throw new IllegalStateException();
                    }
                    throw new AssertionError();
                case ELEMENT:
                    return handleElementProp((ElementPropertyInfo) p2);
                case REFERENCE:
                    return handleReferenceProp((ReferencePropertyInfo) p2);
                case MAP:
                    return handleMapProp((MapPropertyInfo) p2);
                default:
                    if ($assertionsDisabled) {
                        throw new IllegalStateException();
                    }
                    throw new AssertionError();
            }
        }

        private Tree handleElementProp(final ElementPropertyInfo<T, C> ep) {
            if (ep.isValueList()) {
                return new Tree.Term() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.1
                    @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        TypeRef<T, C> t2 = ep.getTypes().get(0);
                        LocalElement e2 = parent.element();
                        e2.block();
                        QName tn = t2.getTagName();
                        e2.name(tn.getLocalPart());
                        com.sun.xml.internal.bind.v2.schemagen.xmlschema.List lst = e2.simpleType().list();
                        Namespace.this.writeTypeRef(lst, t2, "itemType");
                        Namespace.this.elementFormDefault.writeForm(e2, tn);
                        writeOccurs(e2, isOptional || !ep.isRequired(), repeated);
                    }
                };
            }
            ArrayList<Tree> children = new ArrayList<>();
            for (final TypeRef<T, C> t2 : ep.getTypes()) {
                children.add(new Tree.Term() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.2
                    @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        TypeInfo ti;
                        LocalElement e2 = parent.element();
                        QName tn = t2.getTagName();
                        PropertyInfo propInfo = t2.getSource2();
                        TypeInfo parentInfo = propInfo == null ? null : propInfo.parent();
                        if (Namespace.this.canBeDirectElementRef(t2, tn, parentInfo)) {
                            if (!t2.getTarget2().isSimpleType() && (t2.getTarget2() instanceof ClassInfo) && XmlSchemaGenerator.this.collisionChecker.findDuplicate((ClassInfo) t2.getTarget2())) {
                                e2.ref(new QName(Namespace.this.uri, tn.getLocalPart()));
                            } else {
                                QName elemName = null;
                                if (t2.getTarget2() instanceof Element) {
                                    Element te = (Element) t2.getTarget2();
                                    elemName = te.getElementName();
                                }
                                Collection<TypeInfo> refs = propInfo.ref2();
                                if (refs != null && !refs.isEmpty() && elemName != null && ((ti = refs.iterator().next()) == null || (ti instanceof ClassInfoImpl))) {
                                    ClassInfoImpl cImpl = (ClassInfoImpl) ti;
                                    if (cImpl != null && cImpl.getElementName() != null) {
                                        e2.ref(new QName(cImpl.getElementName().getNamespaceURI(), tn.getLocalPart()));
                                    } else {
                                        e2.ref(new QName("", tn.getLocalPart()));
                                    }
                                } else {
                                    e2.ref(tn);
                                }
                            }
                        } else {
                            e2.name(tn.getLocalPart());
                            Namespace.this.writeTypeRef(e2, t2, "type");
                            Namespace.this.elementFormDefault.writeForm(e2, tn);
                        }
                        if (t2.isNillable()) {
                            e2.nillable(true);
                        }
                        if (t2.getDefaultValue() != null) {
                            e2._default(t2.getDefaultValue());
                        }
                        writeOccurs(e2, isOptional, repeated);
                    }
                });
            }
            final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeOptional(!ep.isRequired()).makeRepeated(ep.isCollection());
            final QName ename = ep.getXmlName();
            if (ename != null) {
                return new Tree.Term() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.3
                    @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        LocalElement e2 = parent.element();
                        if (ename.getNamespaceURI().length() > 0 && !ename.getNamespaceURI().equals(Namespace.this.uri)) {
                            e2.ref(new QName(ename.getNamespaceURI(), ename.getLocalPart()));
                            return;
                        }
                        e2.name(ename.getLocalPart());
                        Namespace.this.elementFormDefault.writeForm(e2, ename);
                        if (ep.isCollectionNillable()) {
                            e2.nillable(true);
                        }
                        writeOccurs(e2, !ep.isCollectionRequired(), repeated);
                        ComplexType p2 = e2.complexType();
                        choice.write(p2);
                    }
                };
            }
            return choice;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean canBeDirectElementRef(TypeRef<T, C> t2, QName tn, TypeInfo parentInfo) {
            Element te = null;
            ClassInfo ci = null;
            QName targetTagName = null;
            if (t2.isNillable() || t2.getDefaultValue() != null) {
                return false;
            }
            if (t2.getTarget2() instanceof Element) {
                te = (Element) t2.getTarget2();
                targetTagName = te.getElementName();
                if (te instanceof ClassInfo) {
                    ci = (ClassInfo) te;
                }
            }
            String nsUri = tn.getNamespaceURI();
            if (!nsUri.equals(this.uri) && nsUri.length() > 0 && (!(parentInfo instanceof ClassInfo) || ((ClassInfo) parentInfo).getTypeName() != null)) {
                return true;
            }
            if (ci != null && targetTagName != null && te.getScope2() == null && targetTagName.getNamespaceURI() == null && targetTagName.equals(tn)) {
                return true;
            }
            return (te == null || targetTagName == null || !targetTagName.equals(tn)) ? false : true;
        }

        private void handleAttributeProp(AttributePropertyInfo<T, C> ap2, AttrDecls attr) {
            LocalAttribute localAttribute = attr.attribute();
            String attrURI = ap2.getXmlName().getNamespaceURI();
            if (attrURI.equals("")) {
                localAttribute.name(ap2.getXmlName().getLocalPart());
                writeAttributeTypeRef(ap2, localAttribute);
                this.attributeFormDefault.writeForm(localAttribute, ap2.getXmlName());
            } else {
                localAttribute.ref(ap2.getXmlName());
            }
            if (ap2.isRequired()) {
                localAttribute.use(SchemaSymbols.ATTVAL_REQUIRED);
            }
        }

        private void writeAttributeTypeRef(AttributePropertyInfo<T, C> ap2, AttributeType a2) {
            if (ap2.isCollection()) {
                writeTypeRef(a2.simpleType().list(), ap2, "itemType");
            } else {
                writeTypeRef(a2, ap2, "type");
            }
        }

        private Tree handleReferenceProp(final ReferencePropertyInfo<T, C> rp) {
            ArrayList<Tree> children = new ArrayList<>();
            for (final Element<T, C> e2 : rp.getElements()) {
                children.add(new Tree.Term() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.4
                    @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        LocalElement eref = parent.element();
                        boolean local = false;
                        QName en = e2.getElementName();
                        if (e2.getScope2() != null) {
                            boolean qualified = en.getNamespaceURI().equals(Namespace.this.uri);
                            boolean unqualified = en.getNamespaceURI().equals("");
                            if (qualified || unqualified) {
                                if (unqualified) {
                                    if (Namespace.this.elementFormDefault.isEffectivelyQualified) {
                                        eref.form(SchemaSymbols.ATTVAL_UNQUALIFIED);
                                    }
                                } else if (!Namespace.this.elementFormDefault.isEffectivelyQualified) {
                                    eref.form(SchemaSymbols.ATTVAL_QUALIFIED);
                                }
                                local = true;
                                eref.name(en.getLocalPart());
                                if (e2 instanceof ClassInfo) {
                                    Namespace.this.writeTypeRef(eref, (ClassInfo) e2, "type");
                                } else {
                                    Namespace.this.writeTypeRef(eref, ((ElementInfo) e2).getContentType2(), "type");
                                }
                            }
                        }
                        if (!local) {
                            eref.ref(en);
                        }
                        writeOccurs(eref, isOptional, repeated);
                    }
                });
            }
            final WildcardMode wc = rp.getWildcard();
            if (wc != null) {
                children.add(new Tree.Term() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.5
                    @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        Any any = parent.any();
                        String pcmode = XmlSchemaGenerator.getProcessContentsModeName(wc);
                        if (pcmode != null) {
                            any.processContents(pcmode);
                        }
                        any.namespace(SchemaSymbols.ATTVAL_TWOPOUNDOTHER);
                        writeOccurs(any, isOptional, repeated);
                    }
                });
            }
            final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeRepeated(rp.isCollection()).makeOptional(!rp.isRequired());
            final QName ename = rp.getXmlName();
            if (ename != null) {
                return new Tree.Term() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.6
                    @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        LocalElement e3 = parent.element().name(ename.getLocalPart());
                        Namespace.this.elementFormDefault.writeForm(e3, ename);
                        if (rp.isCollectionNillable()) {
                            e3.nillable(true);
                        }
                        writeOccurs(e3, true, repeated);
                        ComplexType p2 = e3.complexType();
                        choice.write(p2);
                    }
                };
            }
            return choice;
        }

        private Tree handleMapProp(final MapPropertyInfo<T, C> mp) {
            return new Tree.Term() { // from class: com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.7
                @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
                protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                    QName ename = mp.getXmlName();
                    LocalElement e2 = parent.element();
                    Namespace.this.elementFormDefault.writeForm(e2, ename);
                    if (mp.isCollectionNillable()) {
                        e2.nillable(true);
                    }
                    LocalElement e3 = e2.name(ename.getLocalPart());
                    writeOccurs(e3, isOptional, repeated);
                    ComplexType p2 = e3.complexType();
                    LocalElement e4 = p2.sequence().element();
                    e4.name("entry").minOccurs(0).maxOccurs(SchemaSymbols.ATTVAL_UNBOUNDED);
                    ExplicitGroup seq = e4.complexType().sequence();
                    Namespace.this.writeKeyOrValue(seq, "key", mp.getKeyType2());
                    Namespace.this.writeKeyOrValue(seq, "value", mp.getValueType2());
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeKeyOrValue(ExplicitGroup seq, String tagName, NonElement<T, C> typeRef) {
            LocalElement key = seq.element().name(tagName);
            key.minOccurs(0);
            writeTypeRef(key, typeRef, "type");
        }

        public void addGlobalAttribute(AttributePropertyInfo<T, C> ap2) {
            this.attributeDecls.put((MultiMap<String, AttributePropertyInfo<T, C>>) ap2.getXmlName().getLocalPart(), (String) ap2);
            addDependencyTo(ap2.getTarget2().getTypeName());
        }

        public void addGlobalElement(TypeRef<T, C> tref) {
            this.elementDecls.put((MultiMap<String, XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration>) tref.getTagName().getLocalPart(), (String) new ElementWithType(false, tref.getTarget2()));
            addDependencyTo(tref.getTarget2().getTypeName());
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("[classes=").append((Object) this.classes);
            buf.append(",elementDecls=").append((Object) this.elementDecls);
            buf.append(",enums=").append((Object) this.enums);
            buf.append("]");
            return super.toString();
        }

        /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/XmlSchemaGenerator$Namespace$ElementDeclaration.class */
        abstract class ElementDeclaration {
            public abstract boolean equals(Object obj);

            public abstract int hashCode();

            public abstract void writeTo(String str, Schema schema);

            ElementDeclaration() {
            }
        }

        /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/XmlSchemaGenerator$Namespace$ElementWithType.class */
        class ElementWithType extends XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration {
            private final boolean nillable;
            private final NonElement<T, C> type;

            public ElementWithType(boolean nillable, NonElement<T, C> type) {
                super();
                this.type = type;
                this.nillable = nillable;
            }

            @Override // com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.ElementDeclaration
            public void writeTo(String localName, Schema schema) {
                TopLevelElement e2 = schema.element().name(localName);
                if (this.nillable) {
                    e2.nillable(true);
                }
                if (this.type != null) {
                    Namespace.this.writeTypeRef(e2, this.type, "type");
                } else {
                    e2.complexType();
                }
                e2.commit();
            }

            @Override // com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.ElementDeclaration
            public boolean equals(Object o2) {
                if (this == o2) {
                    return true;
                }
                if (o2 == null || getClass() != o2.getClass()) {
                    return false;
                }
                XmlSchemaGenerator<T, C, F, M>.Namespace.ElementWithType that = (ElementWithType) o2;
                return this.type.equals(that.type);
            }

            @Override // com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator.Namespace.ElementDeclaration
            public int hashCode() {
                return this.type.hashCode();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean generateSwaRefAdapter(NonElementRef<T, C> typeRef) {
        return generateSwaRefAdapter(typeRef.getSource2());
    }

    private boolean generateSwaRefAdapter(PropertyInfo<T, C> prop) {
        Object o2;
        Adapter<T, C> adapter = prop.getAdapter();
        if (adapter == null || (o2 = this.navigator.asDecl(SwaRefAdapter.class)) == null) {
            return false;
        }
        return o2.equals(adapter.adapterType);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (XmlSchemaGenerator<T, C, F, M>.Namespace ns : this.namespaces.values()) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append(ns.uri).append('=').append((Object) ns);
        }
        return super.toString() + '[' + ((Object) buf) + ']';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getProcessContentsModeName(WildcardMode wc) {
        switch (wc) {
            case LAX:
            case SKIP:
                return wc.name().toLowerCase();
            case STRICT:
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    protected static String relativize(String uri, String baseUri) {
        try {
            if (!$assertionsDisabled && uri == null) {
                throw new AssertionError();
            }
            if (baseUri == null) {
                return uri;
            }
            URI theUri = new URI(Util.escapeURI(uri));
            URI theBaseUri = new URI(Util.escapeURI(baseUri));
            if (theUri.isOpaque() || theBaseUri.isOpaque()) {
                return uri;
            }
            if (!Util.equalsIgnoreCase(theUri.getScheme(), theBaseUri.getScheme()) || !Util.equal(theUri.getAuthority(), theBaseUri.getAuthority())) {
                return uri;
            }
            String uriPath = theUri.getPath();
            String basePath = theBaseUri.getPath();
            if (!basePath.endsWith("/")) {
                basePath = Util.normalizeUriPath(basePath);
            }
            if (uriPath.equals(basePath)) {
                return ".";
            }
            String relPath = calculateRelativePath(uriPath, basePath, fixNull(theUri.getScheme()).equals(DeploymentDescriptorParser.ATTR_FILE));
            if (relPath == null) {
                return uri;
            }
            StringBuilder relUri = new StringBuilder();
            relUri.append(relPath);
            if (theUri.getQuery() != null) {
                relUri.append('?').append(theUri.getQuery());
            }
            if (theUri.getFragment() != null) {
                relUri.append('#').append(theUri.getFragment());
            }
            return relUri.toString();
        } catch (URISyntaxException e2) {
            throw new InternalError("Error escaping one of these uris:\n\t" + uri + "\n\t" + baseUri);
        }
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    private static String calculateRelativePath(String uri, String base, boolean fileUrl) {
        boolean onWindows = File.pathSeparatorChar == ';';
        if (base == null) {
            return null;
        }
        if ((fileUrl && onWindows && startsWithIgnoreCase(uri, base)) || uri.startsWith(base)) {
            return uri.substring(base.length());
        }
        return "../" + calculateRelativePath(uri, Util.getParentUriPath(base), fileUrl);
    }

    private static boolean startsWithIgnoreCase(String s2, String t2) {
        return s2.toUpperCase().startsWith(t2.toUpperCase());
    }
}
