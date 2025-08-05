package com.sun.xml.internal.messaging.saaj.util;

import com.sun.xml.internal.messaging.saaj.soap.FastInfosetDataContentHandler;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/FastInfosetReflection.class */
public class FastInfosetReflection {
    static Constructor fiDOMDocumentParser_new;
    static Method fiDOMDocumentParser_parse;
    static Constructor fiDOMDocumentSerializer_new;
    static Method fiDOMDocumentSerializer_serialize;
    static Method fiDOMDocumentSerializer_setOutputStream;
    static Class fiFastInfosetSource_class;
    static Constructor fiFastInfosetSource_new;
    static Method fiFastInfosetSource_getInputStream;
    static Method fiFastInfosetSource_setInputStream;
    static Constructor fiFastInfosetResult_new;
    static Method fiFastInfosetResult_getOutputStream;

    static {
        try {
            Class clazz = Class.forName("com.sun.xml.internal.fastinfoset.dom.DOMDocumentParser");
            fiDOMDocumentParser_new = clazz.getConstructor((Class[]) null);
            fiDOMDocumentParser_parse = clazz.getMethod("parse", Document.class, InputStream.class);
            Class clazz2 = Class.forName("com.sun.xml.internal.fastinfoset.dom.DOMDocumentSerializer");
            fiDOMDocumentSerializer_new = clazz2.getConstructor((Class[]) null);
            fiDOMDocumentSerializer_serialize = clazz2.getMethod("serialize", Node.class);
            fiDOMDocumentSerializer_setOutputStream = clazz2.getMethod("setOutputStream", OutputStream.class);
            Class clazz3 = Class.forName(FastInfosetDataContentHandler.STR_SRC);
            fiFastInfosetSource_class = clazz3;
            fiFastInfosetSource_new = clazz3.getConstructor(InputStream.class);
            fiFastInfosetSource_getInputStream = clazz3.getMethod("getInputStream", (Class[]) null);
            fiFastInfosetSource_setInputStream = clazz3.getMethod("setInputStream", InputStream.class);
            Class clazz4 = Class.forName("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetResult");
            fiFastInfosetResult_new = clazz4.getConstructor(OutputStream.class);
            fiFastInfosetResult_getOutputStream = clazz4.getMethod("getOutputStream", (Class[]) null);
        } catch (Exception e2) {
        }
    }

    public static Object DOMDocumentParser_new() throws Exception {
        if (fiDOMDocumentParser_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return fiDOMDocumentParser_new.newInstance((Object[]) null);
    }

    public static void DOMDocumentParser_parse(Object parser, Document d2, InputStream s2) throws Exception {
        if (fiDOMDocumentParser_parse == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiDOMDocumentParser_parse.invoke(parser, d2, s2);
    }

    public static Object DOMDocumentSerializer_new() throws Exception {
        if (fiDOMDocumentSerializer_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return fiDOMDocumentSerializer_new.newInstance((Object[]) null);
    }

    public static void DOMDocumentSerializer_serialize(Object serializer, Node node) throws Exception {
        if (fiDOMDocumentSerializer_serialize == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiDOMDocumentSerializer_serialize.invoke(serializer, node);
    }

    public static void DOMDocumentSerializer_setOutputStream(Object serializer, OutputStream os) throws Exception {
        if (fiDOMDocumentSerializer_setOutputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiDOMDocumentSerializer_setOutputStream.invoke(serializer, os);
    }

    public static boolean isFastInfosetSource(Source source) {
        return source.getClass().getName().equals(FastInfosetDataContentHandler.STR_SRC);
    }

    public static Class getFastInfosetSource_class() {
        if (fiFastInfosetSource_class == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return fiFastInfosetSource_class;
    }

    public static Source FastInfosetSource_new(InputStream is) throws Exception {
        if (fiFastInfosetSource_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (Source) fiFastInfosetSource_new.newInstance(is);
    }

    public static InputStream FastInfosetSource_getInputStream(Source source) throws Exception {
        if (fiFastInfosetSource_getInputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (InputStream) fiFastInfosetSource_getInputStream.invoke(source, (Object[]) null);
    }

    public static void FastInfosetSource_setInputStream(Source source, InputStream is) throws Exception {
        if (fiFastInfosetSource_setInputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        fiFastInfosetSource_setInputStream.invoke(source, is);
    }

    public static boolean isFastInfosetResult(Result result) {
        return result.getClass().getName().equals("com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetResult");
    }

    public static Result FastInfosetResult_new(OutputStream os) throws Exception {
        if (fiFastInfosetResult_new == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (Result) fiFastInfosetResult_new.newInstance(os);
    }

    public static OutputStream FastInfosetResult_getOutputStream(Result result) throws Exception {
        if (fiFastInfosetResult_getOutputStream == null) {
            throw new RuntimeException("Unable to locate Fast Infoset implementation");
        }
        return (OutputStream) fiFastInfosetResult_getOutputStream.invoke(result, (Object[]) null);
    }
}
