package com.sun.xml.internal.ws.model;

import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.JavaMethod;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.JavaParam;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.JavaWsdlMappingType;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.ObjectFactory;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/ExternalMetadataReader.class */
public class ExternalMetadataReader extends ReflectAnnotationReader {
    private static final String NAMESPACE_WEBLOGIC_WSEE_DATABINDING = "http://xmlns.oracle.com/weblogic/weblogic-wsee-databinding";
    private static final String NAMESPACE_JAXWS_RI_EXTERNAL_METADATA = "http://xmlns.oracle.com/webservices/jaxws-databinding";
    private Map<String, JavaWsdlMappingType> readers = new HashMap();

    public ExternalMetadataReader(Collection<File> files, Collection<String> resourcePaths, ClassLoader classLoader, boolean xsdValidation, boolean disableXmlSecurity) throws FactoryConfigurationError {
        if (files != null) {
            for (File file : files) {
                try {
                    String namespace = Util.documentRootNamespace(newSource(file), disableXmlSecurity);
                    JavaWsdlMappingType externalMapping = parseMetadata(xsdValidation, newSource(file), namespace, disableXmlSecurity);
                    this.readers.put(externalMapping.getJavaTypeName(), externalMapping);
                } catch (Exception e2) {
                    throw new RuntimeModelerException("runtime.modeler.external.metadata.unable.to.read", file.getAbsolutePath());
                }
            }
        }
        if (resourcePaths != null) {
            for (String resourcePath : resourcePaths) {
                try {
                    String namespace2 = Util.documentRootNamespace(newSource(resourcePath, classLoader), disableXmlSecurity);
                    JavaWsdlMappingType externalMapping2 = parseMetadata(xsdValidation, newSource(resourcePath, classLoader), namespace2, disableXmlSecurity);
                    this.readers.put(externalMapping2.getJavaTypeName(), externalMapping2);
                } catch (Exception e3) {
                    throw new RuntimeModelerException("runtime.modeler.external.metadata.unable.to.read", resourcePath);
                }
            }
        }
    }

    private StreamSource newSource(String resourcePath, ClassLoader classLoader) {
        InputStream is = classLoader.getResourceAsStream(resourcePath);
        return new StreamSource(is);
    }

    private JavaWsdlMappingType parseMetadata(boolean xsdValidation, StreamSource source, String namespace, boolean disableXmlSecurity) throws TransformerException, IOException, JAXBException {
        if (NAMESPACE_WEBLOGIC_WSEE_DATABINDING.equals(namespace)) {
            return Util.transformAndRead(source, disableXmlSecurity);
        }
        if (NAMESPACE_JAXWS_RI_EXTERNAL_METADATA.equals(namespace)) {
            return Util.read(source, xsdValidation, disableXmlSecurity);
        }
        throw new RuntimeModelerException("runtime.modeler.external.metadata.unsupported.schema", namespace, Arrays.asList(NAMESPACE_WEBLOGIC_WSEE_DATABINDING, NAMESPACE_JAXWS_RI_EXTERNAL_METADATA).toString());
    }

    private StreamSource newSource(File file) {
        try {
            return new StreamSource(new FileInputStream(file));
        } catch (FileNotFoundException e2) {
            throw new RuntimeModelerException("runtime.modeler.external.metadata.unable.to.read", file.getAbsolutePath());
        }
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public <A extends Annotation> A getAnnotation(Class<A> cls, Class<?> cls2) {
        JavaWsdlMappingType erVar = reader(cls2);
        return erVar == null ? (A) super.getAnnotation(cls, cls2) : (A) Util.annotation(erVar, cls);
    }

    private JavaWsdlMappingType reader(Class<?> cls) {
        return this.readers.get(cls.getName());
    }

    Annotation[] getAnnotations(List<Object> objects) {
        ArrayList arrayList = new ArrayList();
        for (Object a2 : objects) {
            if (Annotation.class.isInstance(a2)) {
                arrayList.add(Annotation.class.cast(a2));
            }
        }
        return (Annotation[]) arrayList.toArray(new Annotation[arrayList.size()]);
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public Annotation[] getAnnotations(final Class<?> c2) {
        Merger<Annotation[]> merger = new Merger<Annotation[]>(reader(c2)) { // from class: com.sun.xml.internal.ws.model.ExternalMetadataReader.1
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation[] reflection() {
                return ExternalMetadataReader.super.getAnnotations((Class<?>) c2);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation[] external() {
                return ExternalMetadataReader.this.getAnnotations(this.reader.getClassAnnotation());
            }
        };
        return merger.merge();
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public Annotation[] getAnnotations(final Method m2) {
        Merger<Annotation[]> merger = new Merger<Annotation[]>(reader(m2.getDeclaringClass())) { // from class: com.sun.xml.internal.ws.model.ExternalMetadataReader.2
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation[] reflection() {
                return ExternalMetadataReader.super.getAnnotations(m2);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation[] external() {
                JavaMethod jm = ExternalMetadataReader.this.getJavaMethod(m2, this.reader);
                return jm == null ? new Annotation[0] : ExternalMetadataReader.this.getAnnotations(jm.getMethodAnnotation());
            }
        };
        return merger.merge();
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public <A extends Annotation> A getAnnotation(final Class<A> annType, final Method m2) {
        Merger<Annotation> merger = new Merger<Annotation>(reader(m2.getDeclaringClass())) { // from class: com.sun.xml.internal.ws.model.ExternalMetadataReader.3
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation reflection() {
                return ExternalMetadataReader.super.getAnnotation(annType, m2);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation external() {
                JavaMethod jm = ExternalMetadataReader.this.getJavaMethod(m2, this.reader);
                return (Annotation) Util.annotation(jm, annType);
            }
        };
        return (A) merger.merge();
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public Annotation[][] getParameterAnnotations(final Method m2) {
        Merger<Annotation[][]> merger = new Merger<Annotation[][]>(reader(m2.getDeclaringClass())) { // from class: com.sun.xml.internal.ws.model.ExternalMetadataReader.4
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation[][] reflection() {
                return ExternalMetadataReader.super.getParameterAnnotations(m2);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.ws.model.ExternalMetadataReader.Merger
            public Annotation[][] external() {
                JavaMethod jm = ExternalMetadataReader.this.getJavaMethod(m2, this.reader);
                Annotation[][] a2 = m2.getParameterAnnotations();
                for (int i2 = 0; i2 < m2.getParameterTypes().length; i2++) {
                    if (jm != null) {
                        JavaParam jp = jm.getJavaParams().getJavaParam().get(i2);
                        a2[i2] = ExternalMetadataReader.this.getAnnotations(jp.getParamAnnotation());
                    }
                }
                return a2;
            }
        };
        return merger.merge();
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public void getProperties(Map<String, Object> prop, Class<?> cls) {
        JavaWsdlMappingType r2 = reader(cls);
        if (r2 == null || ExistingAnnotationsType.MERGE.equals(r2.getExistingAnnotations())) {
            super.getProperties(prop, cls);
        }
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public void getProperties(Map<String, Object> prop, Method m2) {
        JavaWsdlMappingType r2 = reader(m2.getDeclaringClass());
        if (r2 == null || ExistingAnnotationsType.MERGE.equals(r2.getExistingAnnotations())) {
            super.getProperties(prop, m2);
        }
        if (r2 != null) {
            JavaMethod jm = getJavaMethod(m2, r2);
            Element[] e2 = Util.annotation(jm);
            prop.put("eclipselink-oxm-xml.xml-element", findXmlElement(e2));
        }
    }

    @Override // com.sun.xml.internal.ws.model.ReflectAnnotationReader, com.sun.xml.internal.ws.api.databinding.MetadataReader
    public void getProperties(Map<String, Object> prop, Method m2, int pos) {
        JavaMethod jm;
        JavaWsdlMappingType r2 = reader(m2.getDeclaringClass());
        if (r2 == null || ExistingAnnotationsType.MERGE.equals(r2.getExistingAnnotations())) {
            super.getProperties(prop, m2, pos);
        }
        if (r2 == null || (jm = getJavaMethod(m2, r2)) == null) {
            return;
        }
        JavaParam jp = jm.getJavaParams().getJavaParam().get(pos);
        Element[] e2 = Util.annotation(jp);
        prop.put("eclipselink-oxm-xml.xml-element", findXmlElement(e2));
    }

    JavaMethod getJavaMethod(Method method, JavaWsdlMappingType r2) {
        JavaWsdlMappingType.JavaMethods javaMethods = r2.getJavaMethods();
        if (javaMethods == null) {
            return null;
        }
        List<JavaMethod> sameName = new ArrayList<>();
        for (JavaMethod jm : javaMethods.getJavaMethod()) {
            if (method.getName().equals(jm.getName())) {
                sameName.add(jm);
            }
        }
        if (sameName.isEmpty()) {
            return null;
        }
        if (sameName.size() == 1) {
            return sameName.get(0);
        }
        Class<?>[] argCls = method.getParameterTypes();
        for (JavaMethod jm2 : sameName) {
            JavaMethod.JavaParams params = jm2.getJavaParams();
            if (params != null && params.getJavaParam() != null && params.getJavaParam().size() == argCls.length) {
                int count = 0;
                for (int i2 = 0; i2 < argCls.length; i2++) {
                    JavaParam jp = params.getJavaParam().get(i2);
                    if (argCls[i2].getName().equals(jp.getJavaType())) {
                        count++;
                    }
                }
                if (count == argCls.length) {
                    return jm2;
                }
            }
        }
        return null;
    }

    Element findXmlElement(Element[] xa) {
        int i2;
        if (xa == null) {
            return null;
        }
        int length = xa.length;
        for (0; i2 < length; i2 + 1) {
            Element e2 = xa[i2];
            i2 = (e2.getLocalName().equals("java-type") || e2.getLocalName().equals("xml-element")) ? 0 : i2 + 1;
            return e2;
        }
        return null;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/ExternalMetadataReader$Merger.class */
    static abstract class Merger<T> {
        JavaWsdlMappingType reader;

        abstract T reflection();

        abstract T external();

        Merger(JavaWsdlMappingType r2) {
            this.reader = r2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        T merge() {
            T tReflection = reflection();
            if (this.reader == null) {
                return tReflection;
            }
            T tExternal = external();
            if (!ExistingAnnotationsType.MERGE.equals(this.reader.getExistingAnnotations())) {
                return tExternal;
            }
            if (tReflection instanceof Annotation) {
                return (T) doMerge((Annotation) tReflection, (Annotation) tExternal);
            }
            if (tReflection instanceof Annotation[][]) {
                return (T) doMerge((Annotation[][]) tReflection, (Annotation[][]) tExternal);
            }
            return (T) doMerge((Annotation[]) tReflection, (Annotation[]) tExternal);
        }

        private Annotation doMerge(Annotation reflection, Annotation external) {
            return external != null ? external : reflection;
        }

        private Annotation[][] doMerge(Annotation[][] reflection, Annotation[][] external) {
            int i2 = 0;
            while (i2 < reflection.length) {
                reflection[i2] = doMerge(reflection[i2], external.length > i2 ? external[i2] : null);
                i2++;
            }
            return reflection;
        }

        private Annotation[] doMerge(Annotation[] annotations, Annotation[] externalAnnotations) {
            HashMap<String, Annotation> mergeMap = new HashMap<>();
            if (annotations != null) {
                for (Annotation reflectionAnnotation : annotations) {
                    mergeMap.put(reflectionAnnotation.annotationType().getName(), reflectionAnnotation);
                }
            }
            if (externalAnnotations != null) {
                for (Annotation externalAnnotation : externalAnnotations) {
                    mergeMap.put(externalAnnotation.annotationType().getName(), externalAnnotation);
                }
            }
            Collection<Annotation> values = mergeMap.values();
            int size = values.size();
            if (size == 0) {
                return null;
            }
            return (Annotation[]) values.toArray(new Annotation[size]);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/ExternalMetadataReader$Util.class */
    static class Util {
        private static final String DATABINDING_XSD = "jaxws-databinding.xsd";
        private static final String TRANSLATE_NAMESPACES_XSL = "jaxws-databinding-translate-namespaces.xml";
        static Schema schema;
        static JAXBContext jaxbContext;

        Util() {
        }

        static {
            SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            try {
                URL xsdUrl = getResource();
                if (xsdUrl != null) {
                    schema = sf.newSchema(xsdUrl);
                }
            } catch (SAXException e2) {
            }
            jaxbContext = createJaxbContext(false);
        }

        private static URL getResource() {
            ClassLoader classLoader = Util.class.getClassLoader();
            return classLoader != null ? classLoader.getResource(DATABINDING_XSD) : ClassLoader.getSystemResource(DATABINDING_XSD);
        }

        private static JAXBContext createJaxbContext(boolean disableXmlSecurity) {
            Class[] cls = {ObjectFactory.class};
            try {
                if (disableXmlSecurity) {
                    Map<String, Object> properties = new HashMap<>();
                    properties.put(JAXBRIContext.DISABLE_XML_SECURITY, Boolean.valueOf(disableXmlSecurity));
                    return JAXBContext.newInstance(cls, (Map<String, ?>) properties);
                }
                return JAXBContext.newInstance(cls);
            } catch (JAXBException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        public static JavaWsdlMappingType read(Source src, boolean xsdValidation, boolean disableXmlSecurity) throws IOException, JAXBException {
            JAXBContext ctx = jaxbContext(disableXmlSecurity);
            try {
                Unmarshaller um = ctx.createUnmarshaller();
                if (xsdValidation) {
                    if (schema == null) {
                    }
                    um.setSchema(schema);
                }
                Object o2 = um.unmarshal(src);
                return getJavaWsdlMapping(o2);
            } catch (JAXBException e2) {
                URL url = new URL(src.getSystemId());
                Source s2 = new StreamSource(url.openStream());
                Unmarshaller um2 = ctx.createUnmarshaller();
                if (xsdValidation) {
                    if (schema == null) {
                    }
                    um2.setSchema(schema);
                }
                Object o3 = um2.unmarshal(s2);
                return getJavaWsdlMapping(o3);
            }
        }

        private static JAXBContext jaxbContext(boolean disableXmlSecurity) {
            return disableXmlSecurity ? createJaxbContext(true) : jaxbContext;
        }

        public static JavaWsdlMappingType transformAndRead(Source src, boolean disableXmlSecurity) throws TransformerException, TransformerFactoryConfigurationError, JAXBException {
            Source xsl = new StreamSource(Util.class.getResourceAsStream(TRANSLATE_NAMESPACES_XSL));
            JAXBResult result = new JAXBResult(jaxbContext(disableXmlSecurity));
            TransformerFactory tf = XmlUtil.newTransformerFactory(!disableXmlSecurity);
            Transformer transformer = tf.newTemplates(xsl).newTransformer();
            transformer.transform(src, result);
            return getJavaWsdlMapping(result.getResult());
        }

        static JavaWsdlMappingType getJavaWsdlMapping(Object o2) {
            Object val = o2 instanceof JAXBElement ? ((JAXBElement) o2).getValue() : o2;
            if (val instanceof JavaWsdlMappingType) {
                return (JavaWsdlMappingType) val;
            }
            return null;
        }

        static <T> T findInstanceOf(Class<T> type, List<Object> objects) {
            for (Object o2 : objects) {
                if (type.isInstance(o2)) {
                    return type.cast(o2);
                }
            }
            return null;
        }

        public static <T> T annotation(JavaWsdlMappingType javaWsdlMappingType, Class<T> cls) {
            if (javaWsdlMappingType == null || javaWsdlMappingType.getClassAnnotation() == null) {
                return null;
            }
            return (T) findInstanceOf(cls, javaWsdlMappingType.getClassAnnotation());
        }

        public static <T> T annotation(JavaMethod javaMethod, Class<T> cls) {
            if (javaMethod == null || javaMethod.getMethodAnnotation() == null) {
                return null;
            }
            return (T) findInstanceOf(cls, javaMethod.getMethodAnnotation());
        }

        public static <T> T annotation(JavaParam javaParam, Class<T> cls) {
            if (javaParam == null || javaParam.getParamAnnotation() == null) {
                return null;
            }
            return (T) findInstanceOf(cls, javaParam.getParamAnnotation());
        }

        public static Element[] annotation(JavaMethod jm) {
            if (jm == null || jm.getMethodAnnotation() == null) {
                return null;
            }
            return findElements(jm.getMethodAnnotation());
        }

        public static Element[] annotation(JavaParam jp) {
            if (jp == null || jp.getParamAnnotation() == null) {
                return null;
            }
            return findElements(jp.getParamAnnotation());
        }

        private static Element[] findElements(List<Object> objects) {
            List<Element> elems = new ArrayList<>();
            for (Object o2 : objects) {
                if (o2 instanceof Element) {
                    elems.add((Element) o2);
                }
            }
            return (Element[]) elems.toArray(new Element[elems.size()]);
        }

        static String documentRootNamespace(Source src, boolean disableXmlSecurity) throws XMLStreamException, IllegalArgumentException, FactoryConfigurationError {
            XMLInputFactory factory = XmlUtil.newXMLInputFactory(!disableXmlSecurity);
            XMLStreamReader streamReader = factory.createXMLStreamReader(src);
            XMLStreamReaderUtil.nextElementContent(streamReader);
            String namespaceURI = streamReader.getName().getNamespaceURI();
            XMLStreamReaderUtil.close(streamReader);
            return namespaceURI;
        }
    }
}
