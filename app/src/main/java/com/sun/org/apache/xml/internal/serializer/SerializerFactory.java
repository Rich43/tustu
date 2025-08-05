package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xml.internal.serializer.utils.MsgKey;
import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
import java.util.Properties;
import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/SerializerFactory.class */
public final class SerializerFactory {
    private SerializerFactory() {
    }

    public static Serializer getSerializer(Properties format) throws Exception {
        Serializer ser;
        try {
            String method = format.getProperty("method");
            if (method == null) {
                String msg = Utils.messages.createMessage(MsgKey.ER_FACTORY_PROPERTY_MISSING, new Object[]{"method"});
                throw new IllegalArgumentException(msg);
            }
            String className = format.getProperty(OutputPropertiesFactory.S_KEY_CONTENT_HANDLER);
            if (null == className) {
                Properties methodDefaults = OutputPropertiesFactory.getDefaultMethodProperties(method);
                className = methodDefaults.getProperty(OutputPropertiesFactory.S_KEY_CONTENT_HANDLER);
                if (null == className) {
                    String msg2 = Utils.messages.createMessage(MsgKey.ER_FACTORY_PROPERTY_MISSING, new Object[]{OutputPropertiesFactory.S_KEY_CONTENT_HANDLER});
                    throw new IllegalArgumentException(msg2);
                }
            }
            Class cls = ObjectFactory.findProviderClass(className, true);
            Object obj = cls.newInstance();
            if (obj instanceof SerializationHandler) {
                ser = (Serializer) cls.newInstance();
                ser.setOutputFormat(format);
            } else if (obj instanceof ContentHandler) {
                SerializationHandler sh = (SerializationHandler) ObjectFactory.findProviderClass(SerializerConstants.DEFAULT_SAX_SERIALIZER, true).newInstance();
                sh.setContentHandler((ContentHandler) obj);
                sh.setOutputFormat(format);
                ser = sh;
            } else {
                throw new Exception(Utils.messages.createMessage("ER_SERIALIZER_NOT_CONTENTHANDLER", new Object[]{className}));
            }
            return ser;
        } catch (Exception e2) {
            throw new WrappedRuntimeException(e2);
        }
    }
}
