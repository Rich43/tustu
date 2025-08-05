package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xerces.internal.util.EncodingMap;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/EncodingInfo.class */
public class EncodingInfo {
    String ianaName;
    String javaName;
    int lastPrintable;
    private Object[] fArgsForMethod = null;
    Object fCharsetEncoder = null;
    Object fCharToByteConverter = null;
    boolean fHaveTriedCToB = false;
    boolean fHaveTriedCharsetEncoder = false;

    public EncodingInfo(String ianaName, String javaName, int lastPrintable) {
        this.ianaName = ianaName;
        this.javaName = EncodingMap.getIANA2JavaMapping(ianaName);
        this.lastPrintable = lastPrintable;
    }

    public String getIANAName() {
        return this.ianaName;
    }

    public Writer getWriter(OutputStream output) throws UnsupportedEncodingException {
        if (this.javaName != null) {
            return new OutputStreamWriter(output, this.javaName);
        }
        this.javaName = EncodingMap.getIANA2JavaMapping(this.ianaName);
        if (this.javaName == null) {
            return new OutputStreamWriter(output, InternalZipConstants.CHARSET_UTF8);
        }
        return new OutputStreamWriter(output, this.javaName);
    }

    public boolean isPrintable(char ch) {
        if (ch <= this.lastPrintable) {
            return true;
        }
        return isPrintable0(ch);
    }

    private boolean isPrintable0(char ch) {
        if (this.fCharsetEncoder == null && CharsetMethods.fgNIOCharsetAvailable && !this.fHaveTriedCharsetEncoder) {
            if (this.fArgsForMethod == null) {
                this.fArgsForMethod = new Object[1];
            }
            try {
                this.fArgsForMethod[0] = this.javaName;
                Object charset = CharsetMethods.fgCharsetForNameMethod.invoke(null, this.fArgsForMethod);
                if (((Boolean) CharsetMethods.fgCharsetCanEncodeMethod.invoke(charset, (Object[]) null)).booleanValue()) {
                    this.fCharsetEncoder = CharsetMethods.fgCharsetNewEncoderMethod.invoke(charset, (Object[]) null);
                } else {
                    this.fHaveTriedCharsetEncoder = true;
                }
            } catch (Exception e2) {
                this.fHaveTriedCharsetEncoder = true;
            }
        }
        if (this.fCharsetEncoder != null) {
            try {
                this.fArgsForMethod[0] = new Character(ch);
                return ((Boolean) CharsetMethods.fgCharsetEncoderCanEncodeMethod.invoke(this.fCharsetEncoder, this.fArgsForMethod)).booleanValue();
            } catch (Exception e3) {
                this.fCharsetEncoder = null;
                this.fHaveTriedCharsetEncoder = false;
            }
        }
        if (this.fCharToByteConverter == null) {
            if (this.fHaveTriedCToB || !CharToByteConverterMethods.fgConvertersAvailable) {
                return false;
            }
            if (this.fArgsForMethod == null) {
                this.fArgsForMethod = new Object[1];
            }
            try {
                this.fArgsForMethod[0] = this.javaName;
                this.fCharToByteConverter = CharToByteConverterMethods.fgGetConverterMethod.invoke(null, this.fArgsForMethod);
            } catch (Exception e4) {
                this.fHaveTriedCToB = true;
                return false;
            }
        }
        try {
            this.fArgsForMethod[0] = new Character(ch);
            return ((Boolean) CharToByteConverterMethods.fgCanConvertMethod.invoke(this.fCharToByteConverter, this.fArgsForMethod)).booleanValue();
        } catch (Exception e5) {
            this.fCharToByteConverter = null;
            this.fHaveTriedCToB = false;
            return false;
        }
    }

    public static void testJavaEncodingName(String name) throws UnsupportedEncodingException {
        byte[] bTest = {118, 97, 108, 105, 100};
        new String(bTest, name);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/EncodingInfo$CharsetMethods.class */
    static class CharsetMethods {
        private static java.lang.reflect.Method fgCharsetForNameMethod;
        private static java.lang.reflect.Method fgCharsetCanEncodeMethod;
        private static java.lang.reflect.Method fgCharsetNewEncoderMethod;
        private static java.lang.reflect.Method fgCharsetEncoderCanEncodeMethod;
        private static boolean fgNIOCharsetAvailable;

        static {
            fgCharsetForNameMethod = null;
            fgCharsetCanEncodeMethod = null;
            fgCharsetNewEncoderMethod = null;
            fgCharsetEncoderCanEncodeMethod = null;
            fgNIOCharsetAvailable = false;
            try {
                Class charsetClass = Class.forName("java.nio.charset.Charset");
                Class charsetEncoderClass = Class.forName("java.nio.charset.CharsetEncoder");
                fgCharsetForNameMethod = charsetClass.getMethod("forName", String.class);
                fgCharsetCanEncodeMethod = charsetClass.getMethod("canEncode", new Class[0]);
                fgCharsetNewEncoderMethod = charsetClass.getMethod("newEncoder", new Class[0]);
                fgCharsetEncoderCanEncodeMethod = charsetEncoderClass.getMethod("canEncode", Character.TYPE);
                fgNIOCharsetAvailable = true;
            } catch (Exception e2) {
                fgCharsetForNameMethod = null;
                fgCharsetCanEncodeMethod = null;
                fgCharsetEncoderCanEncodeMethod = null;
                fgCharsetNewEncoderMethod = null;
                fgNIOCharsetAvailable = false;
            }
        }

        private CharsetMethods() {
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/EncodingInfo$CharToByteConverterMethods.class */
    static class CharToByteConverterMethods {
        private static java.lang.reflect.Method fgGetConverterMethod;
        private static java.lang.reflect.Method fgCanConvertMethod;
        private static boolean fgConvertersAvailable;

        static {
            fgGetConverterMethod = null;
            fgCanConvertMethod = null;
            fgConvertersAvailable = false;
            try {
                Class clazz = Class.forName("sun.io.CharToByteConverter");
                fgGetConverterMethod = clazz.getMethod("getConverter", String.class);
                fgCanConvertMethod = clazz.getMethod("canConvert", Character.TYPE);
                fgConvertersAvailable = true;
            } catch (Exception e2) {
                fgGetConverterMethod = null;
                fgCanConvertMethod = null;
                fgConvertersAvailable = false;
            }
        }

        private CharToByteConverterMethods() {
        }
    }
}
