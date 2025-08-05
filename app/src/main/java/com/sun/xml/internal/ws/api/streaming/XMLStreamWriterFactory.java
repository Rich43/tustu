package com.sun.xml.internal.ws.api.streaming;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.xml.internal.ws.encoding.HasEncoding;
import com.sun.xml.internal.ws.streaming.XMLReaderException;
import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamWriterFactory.class */
public abstract class XMLStreamWriterFactory {
    private static final Logger LOGGER = Logger.getLogger(XMLStreamWriterFactory.class.getName());
    private static volatile ContextClassloaderLocal<XMLStreamWriterFactory> writerFactory = new ContextClassloaderLocal<XMLStreamWriterFactory>() { // from class: com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.api.streaming.ContextClassloaderLocal
        public XMLStreamWriterFactory initialValue() throws FactoryConfigurationError {
            XMLOutputFactory xof = null;
            if (Boolean.getBoolean(XMLStreamWriterFactory.class.getName() + ".woodstox")) {
                try {
                    xof = (XMLOutputFactory) Class.forName("com.ctc.wstx.stax.WstxOutputFactory").newInstance();
                } catch (Exception e2) {
                }
            }
            if (xof == null) {
                xof = XMLOutputFactory.newInstance();
            }
            XMLStreamWriterFactory f2 = null;
            if (!Boolean.getBoolean(XMLStreamWriterFactory.class.getName() + ".noPool")) {
                try {
                    Class<?> clazz = xof.createXMLStreamWriter(new StringWriter()).getClass();
                    if (clazz.getName().startsWith("com.sun.xml.internal.stream.")) {
                        f2 = new Zephyr(xof, clazz);
                    }
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(XMLStreamWriterFactory.class.getName()).log(Level.INFO, (String) null, (Throwable) ex);
                } catch (XMLStreamException ex2) {
                    Logger.getLogger(XMLStreamWriterFactory.class.getName()).log(Level.INFO, (String) null, (Throwable) ex2);
                }
            }
            if (f2 == null && xof.getClass().getName().equals("com.ctc.wstx.stax.WstxOutputFactory")) {
                f2 = new NoLock(xof);
            }
            if (f2 == null) {
                f2 = new Default(xof);
            }
            if (XMLStreamWriterFactory.LOGGER.isLoggable(Level.FINE)) {
                XMLStreamWriterFactory.LOGGER.log(Level.FINE, "XMLStreamWriterFactory instance is = {0}", f2);
            }
            return f2;
        }
    };

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamWriterFactory$RecycleAware.class */
    public interface RecycleAware {
        void onRecycled();
    }

    public abstract XMLStreamWriter doCreate(OutputStream outputStream);

    public abstract XMLStreamWriter doCreate(OutputStream outputStream, String str);

    public abstract void doRecycle(XMLStreamWriter xMLStreamWriter);

    public static void recycle(XMLStreamWriter r2) {
        get().doRecycle(r2);
    }

    @NotNull
    public static XMLStreamWriterFactory get() {
        return writerFactory.get();
    }

    public static void set(@NotNull XMLStreamWriterFactory f2) {
        if (f2 == null) {
            throw new IllegalArgumentException();
        }
        writerFactory.set(f2);
    }

    public static XMLStreamWriter create(OutputStream out) {
        return get().doCreate(out);
    }

    public static XMLStreamWriter create(OutputStream out, String encoding) {
        return get().doCreate(out, encoding);
    }

    public static XMLStreamWriter createXMLStreamWriter(OutputStream out) {
        return create(out);
    }

    public static XMLStreamWriter createXMLStreamWriter(OutputStream out, String encoding) {
        return create(out, encoding);
    }

    public static XMLStreamWriter createXMLStreamWriter(OutputStream out, String encoding, boolean declare) {
        return create(out, encoding);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamWriterFactory$Default.class */
    public static final class Default extends XMLStreamWriterFactory {
        private final XMLOutputFactory xof;

        public Default(XMLOutputFactory xof) {
            this.xof = xof;
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public XMLStreamWriter doCreate(OutputStream out) {
            return doCreate(out, "UTF-8");
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public synchronized XMLStreamWriter doCreate(OutputStream out, String encoding) {
            try {
                XMLStreamWriter writer = this.xof.createXMLStreamWriter(out, encoding);
                return new HasEncodingWriter(writer, encoding);
            } catch (XMLStreamException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public void doRecycle(XMLStreamWriter r2) {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamWriterFactory$Zephyr.class */
    public static final class Zephyr extends XMLStreamWriterFactory {
        private final XMLOutputFactory xof;
        private final ThreadLocal<XMLStreamWriter> pool;
        private final Method resetMethod;
        private final Method setOutputMethod;
        private final Class zephyrClass;

        public static XMLStreamWriterFactory newInstance(XMLOutputFactory xof) {
            try {
                Class<?> clazz = xof.createXMLStreamWriter(new StringWriter()).getClass();
                if (!clazz.getName().startsWith("com.sun.xml.internal.stream.")) {
                    return null;
                }
                return new Zephyr(xof, clazz);
            } catch (NoSuchMethodException e2) {
                return null;
            } catch (XMLStreamException e3) {
                return null;
            }
        }

        private Zephyr(XMLOutputFactory xof, Class clazz) throws NoSuchMethodException {
            this.pool = new ThreadLocal<>();
            this.xof = xof;
            this.zephyrClass = clazz;
            this.setOutputMethod = clazz.getMethod("setOutput", StreamResult.class, String.class);
            this.resetMethod = clazz.getMethod(Constants.RESET, new Class[0]);
        }

        @Nullable
        private XMLStreamWriter fetch() {
            XMLStreamWriter sr = this.pool.get();
            if (sr == null) {
                return null;
            }
            this.pool.set(null);
            return sr;
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public XMLStreamWriter doCreate(OutputStream out) {
            return doCreate(out, "UTF-8");
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public XMLStreamWriter doCreate(OutputStream out, String encoding) throws IllegalArgumentException {
            XMLStreamWriter xsw = fetch();
            if (xsw != null) {
                try {
                    this.resetMethod.invoke(xsw, new Object[0]);
                    this.setOutputMethod.invoke(xsw, new StreamResult(out), encoding);
                } catch (IllegalAccessException e2) {
                    throw new XMLReaderException("stax.cantCreate", e2);
                } catch (InvocationTargetException e3) {
                    throw new XMLReaderException("stax.cantCreate", e3);
                }
            } else {
                try {
                    xsw = this.xof.createXMLStreamWriter(out, encoding);
                } catch (XMLStreamException e4) {
                    throw new XMLReaderException("stax.cantCreate", e4);
                }
            }
            return new HasEncodingWriter(xsw, encoding);
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public void doRecycle(XMLStreamWriter r2) {
            if (r2 instanceof HasEncodingWriter) {
                r2 = ((HasEncodingWriter) r2).getWriter();
            }
            if (this.zephyrClass.isInstance(r2)) {
                try {
                    r2.close();
                    this.pool.set(r2);
                } catch (XMLStreamException e2) {
                    throw new WebServiceException(e2);
                }
            }
            if (r2 instanceof RecycleAware) {
                ((RecycleAware) r2).onRecycled();
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamWriterFactory$NoLock.class */
    public static final class NoLock extends XMLStreamWriterFactory {
        private final XMLOutputFactory xof;

        public NoLock(XMLOutputFactory xof) {
            this.xof = xof;
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public XMLStreamWriter doCreate(OutputStream out) {
            return doCreate(out, "utf-8");
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public XMLStreamWriter doCreate(OutputStream out, String encoding) {
            try {
                XMLStreamWriter writer = this.xof.createXMLStreamWriter(out, encoding);
                return new HasEncodingWriter(writer, encoding);
            } catch (XMLStreamException e2) {
                throw new XMLReaderException("stax.cantCreate", e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory
        public void doRecycle(XMLStreamWriter r2) {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/streaming/XMLStreamWriterFactory$HasEncodingWriter.class */
    public static class HasEncodingWriter extends XMLStreamWriterFilter implements HasEncoding {
        private final String encoding;

        HasEncodingWriter(XMLStreamWriter writer, String encoding) {
            super(writer);
            this.encoding = encoding;
        }

        @Override // com.sun.xml.internal.ws.encoding.HasEncoding
        public String getEncoding() {
            return this.encoding;
        }

        public XMLStreamWriter getWriter() {
            return this.writer;
        }
    }
}
