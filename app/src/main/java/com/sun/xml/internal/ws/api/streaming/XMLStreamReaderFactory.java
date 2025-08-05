package com.sun.xml.internal.ws.api.streaming;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.xml.internal.ws.resources.StreamingMessages;
import com.sun.xml.internal.ws.streaming.XMLReaderException;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.InputSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamReaderFactory.class */
public abstract class XMLStreamReaderFactory {
    private static final String CLASS_NAME_OF_WSTXINPUTFACTORY = "com.ctc.wstx.stax.WstxInputFactory";
    private static final Logger LOGGER = Logger.getLogger(XMLStreamReaderFactory.class.getName());
    private static volatile ContextClassloaderLocal<XMLStreamReaderFactory> streamReader = new ContextClassloaderLocal<XMLStreamReaderFactory>() { // from class: com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.api.streaming.ContextClassloaderLocal
        public XMLStreamReaderFactory initialValue() throws IllegalArgumentException, FactoryConfigurationError {
            XMLInputFactory xif = XMLStreamReaderFactory.getXMLInputFactory();
            XMLStreamReaderFactory f2 = null;
            if (!XMLStreamReaderFactory.getProperty(XMLStreamReaderFactory.class.getName() + ".noPool").booleanValue()) {
                f2 = Zephyr.newInstance(xif);
            }
            if (f2 == null && xif.getClass().getName().equals(XMLStreamReaderFactory.CLASS_NAME_OF_WSTXINPUTFACTORY)) {
                f2 = new Woodstox(xif);
            }
            if (f2 == null) {
                f2 = new Default();
            }
            if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                XMLStreamReaderFactory.LOGGER.log(Level.FINE, "XMLStreamReaderFactory instance is = {0}", f2);
            }
            return f2;
        }
    };

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamReaderFactory$RecycleAware.class */
    public interface RecycleAware {
        void onRecycled();
    }

    public abstract XMLStreamReader doCreate(String str, InputStream inputStream, boolean z2);

    public abstract XMLStreamReader doCreate(String str, Reader reader, boolean z2);

    public abstract void doRecycle(XMLStreamReader xMLStreamReader);

    /* JADX INFO: Access modifiers changed from: private */
    public static XMLInputFactory getXMLInputFactory() throws IllegalArgumentException, FactoryConfigurationError {
        XMLInputFactory xif = null;
        if (getProperty(XMLStreamReaderFactory.class.getName() + ".woodstox").booleanValue()) {
            try {
                xif = (XMLInputFactory) Class.forName(CLASS_NAME_OF_WSTXINPUTFACTORY).newInstance();
            } catch (Exception e2) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, StreamingMessages.WOODSTOX_CANT_LOAD(CLASS_NAME_OF_WSTXINPUTFACTORY), (Throwable) e2);
                }
            }
        }
        if (xif == null) {
            xif = XmlUtil.newXMLInputFactory(true);
        }
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xif.setProperty(XMLInputFactory.IS_COALESCING, true);
        return xif;
    }

    public static void set(XMLStreamReaderFactory f2) {
        if (f2 == null) {
            throw new IllegalArgumentException();
        }
        streamReader.set(f2);
    }

    public static XMLStreamReaderFactory get() {
        return streamReader.get();
    }

    public static XMLStreamReader create(InputSource source, boolean rejectDTDs) {
        try {
            if (source.getCharacterStream() != null) {
                return get().doCreate(source.getSystemId(), source.getCharacterStream(), rejectDTDs);
            }
            if (source.getByteStream() != null) {
                return get().doCreate(source.getSystemId(), source.getByteStream(), rejectDTDs);
            }
            return get().doCreate(source.getSystemId(), new URL(source.getSystemId()).openStream(), rejectDTDs);
        } catch (IOException e2) {
            throw new XMLReaderException("stax.cantCreate", e2);
        }
    }

    public static XMLStreamReader create(@Nullable String systemId, InputStream in, boolean rejectDTDs) {
        return get().doCreate(systemId, in, rejectDTDs);
    }

    public static XMLStreamReader create(@Nullable String systemId, InputStream in, @Nullable String encoding, boolean rejectDTDs) {
        if (encoding == null) {
            return create(systemId, in, rejectDTDs);
        }
        return get().doCreate(systemId, in, encoding, rejectDTDs);
    }

    public static XMLStreamReader create(@Nullable String systemId, Reader reader, boolean rejectDTDs) {
        return get().doCreate(systemId, reader, rejectDTDs);
    }

    public static void recycle(XMLStreamReader r2) {
        get().doRecycle(r2);
        if (r2 instanceof RecycleAware) {
            ((RecycleAware) r2).onRecycled();
        }
    }

    private XMLStreamReader doCreate(String systemId, InputStream in, @NotNull String encoding, boolean rejectDTDs) {
        try {
            Reader reader = new InputStreamReader(in, encoding);
            return doCreate(systemId, reader, rejectDTDs);
        } catch (UnsupportedEncodingException ue) {
            throw new XMLReaderException("stax.cantCreate", ue);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamReaderFactory$Zephyr.class */
    private static final class Zephyr extends XMLStreamReaderFactory {
        private final XMLInputFactory xif;
        private final ThreadLocal<XMLStreamReader> pool = new ThreadLocal<>();
        private final Method setInputSourceMethod;
        private final Method resetMethod;
        private final Class zephyrClass;

        @Nullable
        public static XMLStreamReaderFactory newInstance(XMLInputFactory xif) {
            try {
                Class<?> clazz = xif.createXMLStreamReader(new StringReader("<foo/>")).getClass();
                if (!clazz.getName().startsWith("com.sun.xml.internal.stream.")) {
                    return null;
                }
                return new Zephyr(xif, clazz);
            } catch (NoSuchMethodException e2) {
                return null;
            } catch (XMLStreamException e3) {
                return null;
            }
        }

        public Zephyr(XMLInputFactory xif, Class clazz) throws NoSuchMethodException {
            this.zephyrClass = clazz;
            this.setInputSourceMethod = clazz.getMethod("setInputSource", InputSource.class);
            this.resetMethod = clazz.getMethod(Constants.RESET, new Class[0]);
            try {
                xif.setProperty(com.sun.org.apache.xerces.internal.impl.Constants.REUSE_INSTANCE, false);
            } catch (IllegalArgumentException e2) {
            }
            this.xif = xif;
        }

        @Nullable
        private XMLStreamReader fetch() {
            XMLStreamReader sr = this.pool.get();
            if (sr == null) {
                return null;
            }
            this.pool.set(null);
            return sr;
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public void doRecycle(XMLStreamReader r2) {
            if (this.zephyrClass.isInstance(r2)) {
                this.pool.set(r2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, InputStream in, boolean rejectDTDs) throws IllegalArgumentException {
            try {
                XMLStreamReader xsr = fetch();
                if (xsr == null) {
                    return this.xif.createXMLStreamReader(systemId, in);
                }
                InputSource is = new InputSource(systemId);
                is.setByteStream(in);
                reuse(xsr, is);
                return xsr;
            } catch (IllegalAccessException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            } catch (InvocationTargetException e3) {
                throw new XMLReaderException("stax.cantCreate", e3);
            } catch (XMLStreamException e4) {
                throw new XMLReaderException("stax.cantCreate", e4);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, Reader in, boolean rejectDTDs) throws IllegalArgumentException {
            try {
                XMLStreamReader xsr = fetch();
                if (xsr == null) {
                    return this.xif.createXMLStreamReader(systemId, in);
                }
                InputSource is = new InputSource(systemId);
                is.setCharacterStream(in);
                reuse(xsr, is);
                return xsr;
            } catch (IllegalAccessException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause == null) {
                    cause = e3;
                }
                throw new XMLReaderException("stax.cantCreate", cause);
            } catch (XMLStreamException e4) {
                throw new XMLReaderException("stax.cantCreate", e4);
            }
        }

        private void reuse(XMLStreamReader xsr, InputSource in) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            this.resetMethod.invoke(xsr, new Object[0]);
            this.setInputSourceMethod.invoke(xsr, in);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamReaderFactory$Default.class */
    public static final class Default extends XMLStreamReaderFactory {
        private final ThreadLocal<XMLInputFactory> xif = new ThreadLocal<XMLInputFactory>() { // from class: com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public XMLInputFactory initialValue() {
                return XMLStreamReaderFactory.getXMLInputFactory();
            }
        };

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, InputStream in, boolean rejectDTDs) {
            try {
                return this.xif.get().createXMLStreamReader(systemId, in);
            } catch (XMLStreamException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, Reader in, boolean rejectDTDs) {
            try {
                return this.xif.get().createXMLStreamReader(systemId, in);
            } catch (XMLStreamException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public void doRecycle(XMLStreamReader r2) {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamReaderFactory$NoLock.class */
    public static class NoLock extends XMLStreamReaderFactory {
        private final XMLInputFactory xif;

        public NoLock(XMLInputFactory xif) {
            this.xif = xif;
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, InputStream in, boolean rejectDTDs) {
            try {
                return this.xif.createXMLStreamReader(systemId, in);
            } catch (XMLStreamException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, Reader in, boolean rejectDTDs) {
            try {
                return this.xif.createXMLStreamReader(systemId, in);
            } catch (XMLStreamException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public void doRecycle(XMLStreamReader r2) {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamReaderFactory$Woodstox.class */
    public static final class Woodstox extends NoLock {
        public static final String PROPERTY_MAX_ATTRIBUTES_PER_ELEMENT = "xml.ws.maximum.AttributesPerElement";
        public static final String PROPERTY_MAX_ATTRIBUTE_SIZE = "xml.ws.maximum.AttributeSize";
        public static final String PROPERTY_MAX_CHILDREN_PER_ELEMENT = "xml.ws.maximum.ChildrenPerElement";
        public static final String PROPERTY_MAX_ELEMENT_COUNT = "xml.ws.maximum.ElementCount";
        public static final String PROPERTY_MAX_ELEMENT_DEPTH = "xml.ws.maximum.ElementDepth";
        public static final String PROPERTY_MAX_CHARACTERS = "xml.ws.maximum.Characters";
        private static final int DEFAULT_MAX_ATTRIBUTES_PER_ELEMENT = 500;
        private static final int DEFAULT_MAX_ATTRIBUTE_SIZE = 524288;
        private static final int DEFAULT_MAX_CHILDREN_PER_ELEMENT = Integer.MAX_VALUE;
        private static final int DEFAULT_MAX_ELEMENT_DEPTH = 500;
        private static final long DEFAULT_MAX_ELEMENT_COUNT = 2147483647L;
        private static final long DEFAULT_MAX_CHARACTERS = Long.MAX_VALUE;
        private int maxAttributesPerElement;
        private int maxAttributeSize;
        private int maxChildrenPerElement;
        private int maxElementDepth;
        private long maxElementCount;
        private long maxCharacters;
        private static final String P_MAX_ATTRIBUTES_PER_ELEMENT = "com.ctc.wstx.maxAttributesPerElement";
        private static final String P_MAX_ATTRIBUTE_SIZE = "com.ctc.wstx.maxAttributeSize";
        private static final String P_MAX_CHILDREN_PER_ELEMENT = "com.ctc.wstx.maxChildrenPerElement";
        private static final String P_MAX_ELEMENT_COUNT = "com.ctc.wstx.maxElementCount";
        private static final String P_MAX_ELEMENT_DEPTH = "com.ctc.wstx.maxElementDepth";
        private static final String P_MAX_CHARACTERS = "com.ctc.wstx.maxCharacters";
        private static final String P_INTERN_NSURIS = "org.codehaus.stax2.internNsUris";

        public Woodstox(XMLInputFactory xif) throws IllegalArgumentException {
            super(xif);
            this.maxAttributesPerElement = 500;
            this.maxAttributeSize = 524288;
            this.maxChildrenPerElement = Integer.MAX_VALUE;
            this.maxElementDepth = 500;
            this.maxElementCount = DEFAULT_MAX_ELEMENT_COUNT;
            this.maxCharacters = Long.MAX_VALUE;
            if (xif.isPropertySupported(P_INTERN_NSURIS)) {
                xif.setProperty(P_INTERN_NSURIS, true);
                if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                    XMLStreamReaderFactory.LOGGER.log(Level.FINE, "org.codehaus.stax2.internNsUris is {0}", (Object) true);
                }
            }
            if (xif.isPropertySupported(P_MAX_ATTRIBUTES_PER_ELEMENT)) {
                this.maxAttributesPerElement = Integer.valueOf(XMLStreamReaderFactory.buildIntegerValue(PROPERTY_MAX_ATTRIBUTES_PER_ELEMENT, 500)).intValue();
                xif.setProperty(P_MAX_ATTRIBUTES_PER_ELEMENT, Integer.valueOf(this.maxAttributesPerElement));
                if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                    XMLStreamReaderFactory.LOGGER.log(Level.FINE, "com.ctc.wstx.maxAttributesPerElement is {0}", Integer.valueOf(this.maxAttributesPerElement));
                }
            }
            if (xif.isPropertySupported(P_MAX_ATTRIBUTE_SIZE)) {
                this.maxAttributeSize = Integer.valueOf(XMLStreamReaderFactory.buildIntegerValue(PROPERTY_MAX_ATTRIBUTE_SIZE, 524288)).intValue();
                xif.setProperty(P_MAX_ATTRIBUTE_SIZE, Integer.valueOf(this.maxAttributeSize));
                if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                    XMLStreamReaderFactory.LOGGER.log(Level.FINE, "com.ctc.wstx.maxAttributeSize is {0}", Integer.valueOf(this.maxAttributeSize));
                }
            }
            if (xif.isPropertySupported(P_MAX_CHILDREN_PER_ELEMENT)) {
                this.maxChildrenPerElement = Integer.valueOf(XMLStreamReaderFactory.buildIntegerValue(PROPERTY_MAX_CHILDREN_PER_ELEMENT, Integer.MAX_VALUE)).intValue();
                xif.setProperty(P_MAX_CHILDREN_PER_ELEMENT, Integer.valueOf(this.maxChildrenPerElement));
                if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                    XMLStreamReaderFactory.LOGGER.log(Level.FINE, "com.ctc.wstx.maxChildrenPerElement is {0}", Integer.valueOf(this.maxChildrenPerElement));
                }
            }
            if (xif.isPropertySupported(P_MAX_ELEMENT_DEPTH)) {
                this.maxElementDepth = Integer.valueOf(XMLStreamReaderFactory.buildIntegerValue(PROPERTY_MAX_ELEMENT_DEPTH, 500)).intValue();
                xif.setProperty(P_MAX_ELEMENT_DEPTH, Integer.valueOf(this.maxElementDepth));
                if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                    XMLStreamReaderFactory.LOGGER.log(Level.FINE, "com.ctc.wstx.maxElementDepth is {0}", Integer.valueOf(this.maxElementDepth));
                }
            }
            if (xif.isPropertySupported(P_MAX_ELEMENT_COUNT)) {
                this.maxElementCount = Long.valueOf(XMLStreamReaderFactory.buildLongValue(PROPERTY_MAX_ELEMENT_COUNT, DEFAULT_MAX_ELEMENT_COUNT)).longValue();
                xif.setProperty(P_MAX_ELEMENT_COUNT, Long.valueOf(this.maxElementCount));
                if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                    XMLStreamReaderFactory.LOGGER.log(Level.FINE, "com.ctc.wstx.maxElementCount is {0}", Long.valueOf(this.maxElementCount));
                }
            }
            if (xif.isPropertySupported(P_MAX_CHARACTERS)) {
                this.maxCharacters = Long.valueOf(XMLStreamReaderFactory.buildLongValue(PROPERTY_MAX_CHARACTERS, Long.MAX_VALUE)).longValue();
                xif.setProperty(P_MAX_CHARACTERS, Long.valueOf(this.maxCharacters));
                if (XMLStreamReaderFactory.LOGGER.isLoggable(Level.FINE)) {
                    XMLStreamReaderFactory.LOGGER.log(Level.FINE, "com.ctc.wstx.maxCharacters is {0}", Long.valueOf(this.maxCharacters));
                }
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.NoLock, com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, InputStream in, boolean rejectDTDs) {
            return super.doCreate(systemId, in, rejectDTDs);
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.NoLock, com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
        public XMLStreamReader doCreate(String systemId, Reader in, boolean rejectDTDs) {
            return super.doCreate(systemId, in, rejectDTDs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int buildIntegerValue(String propertyName, int defaultValue) {
        String propVal = System.getProperty(propertyName);
        if (propVal != null && propVal.length() > 0) {
            try {
                Integer value = Integer.valueOf(Integer.parseInt(propVal));
                if (value.intValue() > 0) {
                    return value.intValue();
                }
            } catch (NumberFormatException nfe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, StreamingMessages.INVALID_PROPERTY_VALUE_INTEGER(propertyName, propVal, Integer.toString(defaultValue)), (Throwable) nfe);
                }
            }
        }
        return defaultValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long buildLongValue(String propertyName, long defaultValue) {
        String propVal = System.getProperty(propertyName);
        if (propVal != null && propVal.length() > 0) {
            try {
                long value = Long.parseLong(propVal);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, StreamingMessages.INVALID_PROPERTY_VALUE_LONG(propertyName, propVal, Long.toString(defaultValue)), (Throwable) nfe);
                }
            }
        }
        return defaultValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Boolean getProperty(final String prop) {
        return (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() {
                String value = System.getProperty(prop);
                return value != null ? Boolean.valueOf(value) : Boolean.FALSE;
            }
        });
    }
}
