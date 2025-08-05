package com.sun.xml.internal.bind.v2;

import com.sun.istack.internal.FinalArrayList;
import com.sun.xml.internal.bind.Util;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.util.TypeCast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/ContextFactory.class */
public class ContextFactory {
    public static final String USE_JAXB_PROPERTIES = "_useJAXBProperties";

    public static JAXBContext createContext(Class[] classes, Map<String, Object> properties) throws JAXBException {
        Map<String, Object> properties2;
        if (properties == null) {
            properties2 = Collections.emptyMap();
        } else {
            properties2 = new HashMap<>(properties);
        }
        String defaultNsUri = (String) getPropertyValue(properties2, "com.sun.xml.internal.bind.defaultNamespaceRemap", String.class);
        Boolean c14nSupport = (Boolean) getPropertyValue(properties2, "com.sun.xml.internal.bind.c14n", Boolean.class);
        if (c14nSupport == null) {
            c14nSupport = false;
        }
        Boolean disablesecurityProcessing = (Boolean) getPropertyValue(properties2, JAXBRIContext.DISABLE_XML_SECURITY, Boolean.class);
        if (disablesecurityProcessing == null) {
            disablesecurityProcessing = false;
        }
        Boolean allNillable = (Boolean) getPropertyValue(properties2, "com.sun.xml.internal.bind.treatEverythingNillable", Boolean.class);
        if (allNillable == null) {
            allNillable = false;
        }
        Boolean retainPropertyInfo = (Boolean) getPropertyValue(properties2, "retainReferenceToInfo", Boolean.class);
        if (retainPropertyInfo == null) {
            retainPropertyInfo = false;
        }
        Boolean supressAccessorWarnings = (Boolean) getPropertyValue(properties2, JAXBRIContext.SUPRESS_ACCESSOR_WARNINGS, Boolean.class);
        if (supressAccessorWarnings == null) {
            supressAccessorWarnings = false;
        }
        Boolean improvedXsiTypeHandling = (Boolean) getPropertyValue(properties2, JAXBRIContext.IMPROVED_XSI_TYPE_HANDLING, Boolean.class);
        if (improvedXsiTypeHandling == null) {
            String improvedXsiSystemProperty = Util.getSystemProperty(JAXBRIContext.IMPROVED_XSI_TYPE_HANDLING);
            if (improvedXsiSystemProperty == null) {
                improvedXsiTypeHandling = true;
            } else {
                improvedXsiTypeHandling = Boolean.valueOf(improvedXsiSystemProperty);
            }
        }
        Boolean xmlAccessorFactorySupport = (Boolean) getPropertyValue(properties2, "com.sun.xml.internal.bind.XmlAccessorFactory", Boolean.class);
        if (xmlAccessorFactorySupport == null) {
            xmlAccessorFactorySupport = false;
            Util.getClassLogger().log(Level.FINE, "Property com.sun.xml.internal.bind.XmlAccessorFactoryis not active.  Using JAXB's implementation");
        }
        RuntimeAnnotationReader ar2 = (RuntimeAnnotationReader) getPropertyValue(properties2, JAXBRIContext.ANNOTATION_READER, RuntimeAnnotationReader.class);
        Collection<TypeReference> tr = (Collection) getPropertyValue(properties2, "com.sun.xml.internal.bind.typeReferences", Collection.class);
        if (tr == null) {
            tr = Collections.emptyList();
        }
        try {
            Map<Class, Class> subclassReplacements = TypeCast.checkedCast((Map) getPropertyValue(properties2, "com.sun.xml.internal.bind.subclassReplacements", Map.class), Class.class, Class.class);
            if (!properties2.isEmpty()) {
                throw new JAXBException(Messages.UNSUPPORTED_PROPERTY.format(properties2.keySet().iterator().next()));
            }
            JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
            builder.setClasses(classes);
            builder.setTypeRefs(tr);
            builder.setSubclassReplacements(subclassReplacements);
            builder.setDefaultNsUri(defaultNsUri);
            builder.setC14NSupport(c14nSupport.booleanValue());
            builder.setAnnotationReader(ar2);
            builder.setXmlAccessorFactorySupport(xmlAccessorFactorySupport.booleanValue());
            builder.setAllNillable(allNillable.booleanValue());
            builder.setRetainPropertyInfo(retainPropertyInfo.booleanValue());
            builder.setSupressAccessorWarnings(supressAccessorWarnings.booleanValue());
            builder.setImprovedXsiTypeHandling(improvedXsiTypeHandling.booleanValue());
            builder.setDisableSecurityProcessing(disablesecurityProcessing.booleanValue());
            return builder.build();
        } catch (ClassCastException e2) {
            throw new JAXBException(Messages.INVALID_TYPE_IN_MAP.format(new Object[0]), e2);
        }
    }

    private static <T> T getPropertyValue(Map<String, Object> properties, String keyName, Class<T> type) throws JAXBException {
        Object o2 = properties.get(keyName);
        if (o2 == null) {
            return null;
        }
        properties.remove(keyName);
        if (!type.isInstance(o2)) {
            throw new JAXBException(Messages.INVALID_PROPERTY_VALUE.format(keyName, o2));
        }
        return type.cast(o2);
    }

    @Deprecated
    public static JAXBRIContext createContext(Class[] classes, Collection<TypeReference> typeRefs, Map<Class, Class> subclassReplacements, String defaultNsUri, boolean c14nSupport, RuntimeAnnotationReader ar2, boolean xmlAccessorFactorySupport, boolean allNillable, boolean retainPropertyInfo) throws JAXBException {
        return createContext(classes, typeRefs, subclassReplacements, defaultNsUri, c14nSupport, ar2, xmlAccessorFactorySupport, allNillable, retainPropertyInfo, false);
    }

    @Deprecated
    public static JAXBRIContext createContext(Class[] classes, Collection<TypeReference> typeRefs, Map<Class, Class> subclassReplacements, String defaultNsUri, boolean c14nSupport, RuntimeAnnotationReader ar2, boolean xmlAccessorFactorySupport, boolean allNillable, boolean retainPropertyInfo, boolean improvedXsiTypeHandling) throws JAXBException {
        JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
        builder.setClasses(classes);
        builder.setTypeRefs(typeRefs);
        builder.setSubclassReplacements(subclassReplacements);
        builder.setDefaultNsUri(defaultNsUri);
        builder.setC14NSupport(c14nSupport);
        builder.setAnnotationReader(ar2);
        builder.setXmlAccessorFactorySupport(xmlAccessorFactorySupport);
        builder.setAllNillable(allNillable);
        builder.setRetainPropertyInfo(retainPropertyInfo);
        builder.setImprovedXsiTypeHandling(improvedXsiTypeHandling);
        return builder.build();
    }

    public static JAXBContext createContext(String contextPath, ClassLoader classLoader, Map<String, Object> properties) throws JAXBException {
        FinalArrayList<Class> classes = new FinalArrayList<>();
        StringTokenizer tokens = new StringTokenizer(contextPath, CallSiteDescriptor.TOKEN_DELIMITER);
        while (tokens.hasMoreTokens()) {
            boolean foundJaxbIndex = false;
            boolean foundObjectFactory = false;
            String pkg = tokens.nextToken();
            try {
                Class<?> o2 = classLoader.loadClass(pkg + ".ObjectFactory");
                classes.add(o2);
                foundObjectFactory = true;
            } catch (ClassNotFoundException e2) {
            }
            try {
                List<Class> indexedClasses = loadIndexedClasses(pkg, classLoader);
                if (indexedClasses != null) {
                    classes.addAll(indexedClasses);
                    foundJaxbIndex = true;
                }
                if (!foundObjectFactory && !foundJaxbIndex) {
                    throw new JAXBException(Messages.BROKEN_CONTEXTPATH.format(pkg));
                }
            } catch (IOException e3) {
                throw new JAXBException(e3);
            }
        }
        return createContext((Class[]) classes.toArray(new Class[classes.size()]), properties);
    }

    private static List<Class> loadIndexedClasses(String pkg, ClassLoader classLoader) throws IOException, JAXBException {
        String resource = pkg.replace('.', '/') + "/jaxb.index";
        InputStream resourceAsStream = classLoader.getResourceAsStream(resource);
        if (resourceAsStream == null) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
        try {
            FinalArrayList<Class> classes = new FinalArrayList<>();
            String className = in.readLine();
            while (className != null) {
                String className2 = className.trim();
                if (className2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) || className2.length() == 0) {
                    className = in.readLine();
                } else {
                    if (className2.endsWith(".class")) {
                        throw new JAXBException(Messages.ILLEGAL_ENTRY.format(className2));
                    }
                    try {
                        classes.add(classLoader.loadClass(pkg + '.' + className2));
                        className = in.readLine();
                    } catch (ClassNotFoundException e2) {
                        throw new JAXBException(Messages.ERROR_LOADING_CLASS.format(className2, resource), e2);
                    }
                }
            }
            return classes;
        } finally {
            in.close();
        }
    }
}
