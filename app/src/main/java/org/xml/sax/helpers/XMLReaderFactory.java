package org.xml.sax.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.lingala.zip4j.util.InternalZipConstants;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:org/xml/sax/helpers/XMLReaderFactory.class */
public final class XMLReaderFactory {
    private static final String property = "org.xml.sax.driver";
    private static SecuritySupport ss = new SecuritySupport();
    private static String _clsFromJar = null;
    private static boolean _jarread = false;

    private XMLReaderFactory() {
    }

    public static XMLReader createXMLReader() throws SAXException {
        InputStream in;
        String className = null;
        ClassLoader cl = ss.getContextClassLoader();
        try {
            className = ss.getSystemProperty(property);
        } catch (RuntimeException e2) {
        }
        if (className == null) {
            if (!_jarread) {
                _jarread = true;
                try {
                    if (cl != null) {
                        in = ss.getResourceAsStream(cl, "META-INF/services/org.xml.sax.driver");
                        if (in == null) {
                            cl = null;
                            in = ss.getResourceAsStream(null, "META-INF/services/org.xml.sax.driver");
                        }
                    } else {
                        in = ss.getResourceAsStream(cl, "META-INF/services/org.xml.sax.driver");
                    }
                    if (in != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, InternalZipConstants.CHARSET_UTF8));
                        _clsFromJar = reader.readLine();
                        in.close();
                    }
                } catch (Exception e3) {
                }
            }
            className = _clsFromJar;
        }
        if (className == null) {
            className = "com.sun.org.apache.xerces.internal.parsers.SAXParser";
        }
        if (className != null) {
            return loadClass(cl, className);
        }
        try {
            return new ParserAdapter(ParserFactory.makeParser());
        } catch (Exception e4) {
            throw new SAXException("Can't create default XMLReader; is system property org.xml.sax.driver set?");
        }
    }

    public static XMLReader createXMLReader(String className) throws SAXException {
        return loadClass(ss.getContextClassLoader(), className);
    }

    private static XMLReader loadClass(ClassLoader loader, String className) throws SAXException {
        try {
            return (XMLReader) NewInstance.newInstance(loader, className);
        } catch (ClassCastException e4) {
            throw new SAXException("SAX2 driver class " + className + " does not implement XMLReader", e4);
        } catch (ClassNotFoundException e1) {
            throw new SAXException("SAX2 driver class " + className + " not found", e1);
        } catch (IllegalAccessException e2) {
            throw new SAXException("SAX2 driver class " + className + " found but cannot be loaded", e2);
        } catch (InstantiationException e3) {
            throw new SAXException("SAX2 driver class " + className + " loaded but cannot be instantiated (no empty public constructor?)", e3);
        }
    }
}
