package com.sun.xml.internal.ws.commons.xmlutil;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/commons/xmlutil/Converter.class */
public final class Converter {
    public static final String UTF_8 = "UTF-8";
    private static final Logger LOGGER = Logger.getLogger(Converter.class);
    private static final ContextClassloaderLocal<XMLOutputFactory> xmlOutputFactory = new ContextClassloaderLocal<XMLOutputFactory>() { // from class: com.sun.xml.internal.ws.commons.xmlutil.Converter.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.commons.xmlutil.ContextClassloaderLocal
        public XMLOutputFactory initialValue() throws Exception {
            return XMLOutputFactory.newInstance();
        }
    };
    private static final AtomicBoolean logMissingStaxUtilsWarning = new AtomicBoolean(false);

    private Converter() {
    }

    public static String toString(Throwable throwable) {
        if (throwable == null) {
            return "[ No exception ]";
        }
        StringWriter stringOut = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringOut));
        return stringOut.toString();
    }

    public static String toString(Packet packet) {
        if (packet == null) {
            return "[ Null packet ]";
        }
        if (packet.getMessage() == null) {
            return "[ Empty packet ]";
        }
        return toString(packet.getMessage());
    }

    public static String toStringNoIndent(Packet packet) {
        if (packet == null) {
            return "[ Null packet ]";
        }
        if (packet.getMessage() == null) {
            return "[ Empty packet ]";
        }
        return toStringNoIndent(packet.getMessage());
    }

    public static String toString(Message message) {
        return toString(message, true);
    }

    public static String toStringNoIndent(Message message) {
        return toString(message, false);
    }

    private static String toString(Message message, boolean createIndenter) {
        if (message == null) {
            return "[ Null message ]";
        }
        StringWriter stringOut = null;
        try {
            stringOut = new StringWriter();
            XMLStreamWriter writer = null;
            try {
                try {
                    writer = xmlOutputFactory.get().createXMLStreamWriter(stringOut);
                    if (createIndenter) {
                        writer = createIndenter(writer);
                    }
                    message.copy().writeTo(writer);
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (XMLStreamException ignored) {
                            LOGGER.fine("Unexpected exception occured while closing XMLStreamWriter", ignored);
                        }
                    }
                } finally {
                }
            } catch (Exception e2) {
                LOGGER.log(Level.WARNING, "Unexpected exception occured while dumping message", (Throwable) e2);
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (XMLStreamException ignored2) {
                        LOGGER.fine("Unexpected exception occured while closing XMLStreamWriter", ignored2);
                    }
                }
            }
            String string = stringOut.toString();
            if (stringOut != null) {
                try {
                    stringOut.close();
                } catch (IOException ex) {
                    LOGGER.finest("An exception occured when trying to close StringWriter", ex);
                }
            }
            return string;
        } catch (Throwable th) {
            if (stringOut != null) {
                try {
                    stringOut.close();
                } catch (IOException ex2) {
                    LOGGER.finest("An exception occured when trying to close StringWriter", ex2);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    public static byte[] toBytes(Message message, String encoding) throws XMLStreamException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (message != null) {
            try {
                XMLStreamWriter xsw = xmlOutputFactory.get().createXMLStreamWriter(baos, encoding);
                try {
                    message.writeTo(xsw);
                    try {
                        xsw.close();
                    } catch (XMLStreamException ex) {
                        LOGGER.warning("Unexpected exception occured while closing XMLStreamWriter", ex);
                    }
                } catch (Throwable th) {
                    try {
                        xsw.close();
                    } catch (XMLStreamException ex2) {
                        LOGGER.warning("Unexpected exception occured while closing XMLStreamWriter", ex2);
                    }
                    throw th;
                }
            } finally {
                try {
                    baos.close();
                } catch (IOException ex3) {
                    LOGGER.warning("Unexpected exception occured while closing ByteArrayOutputStream", ex3);
                }
            }
        }
        return baos.toByteArray();
    }

    public static Message toMessage(@NotNull InputStream dataStream, String encoding) throws XMLStreamException {
        XMLStreamReader xsr = XmlUtil.newXMLInputFactory(true).createXMLStreamReader(dataStream, encoding);
        return Messages.create(xsr);
    }

    public static String messageDataToString(byte[] data, String encoding) {
        try {
            return toString(toMessage(new ByteArrayInputStream(data), encoding));
        } catch (XMLStreamException ex) {
            LOGGER.warning("Unexpected exception occured while converting message data to string", ex);
            return "[ Message Data Conversion Failed ]";
        }
    }

    private static XMLStreamWriter createIndenter(XMLStreamWriter writer) {
        try {
            Class<?> clazz = Converter.class.getClassLoader().loadClass("javanet.staxutils.IndentingXMLStreamWriter");
            Constructor<?> c2 = clazz.getConstructor(XMLStreamWriter.class);
            writer = (XMLStreamWriter) XMLStreamWriter.class.cast(c2.newInstance(writer));
        } catch (Exception ex) {
            if (logMissingStaxUtilsWarning.compareAndSet(false, true)) {
                LOGGER.log(Level.WARNING, "Put stax-utils.jar to the classpath to indent the dump output", (Throwable) ex);
            }
        }
        return writer;
    }
}
