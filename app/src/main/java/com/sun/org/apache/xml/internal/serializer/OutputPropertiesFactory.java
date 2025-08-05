package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Properties;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/OutputPropertiesFactory.class */
public final class OutputPropertiesFactory {
    private static final String S_BUILTIN_EXTENSIONS_URL = "http://xml.apache.org/xalan";
    private static final String S_BUILTIN_OLD_EXTENSIONS_URL = "http://xml.apache.org/xslt";
    public static final String S_BUILTIN_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xalan}";
    public static final String S_KEY_INDENT_AMOUNT = "{http://xml.apache.org/xalan}indent-amount";
    public static final String S_KEY_LINE_SEPARATOR = "{http://xml.apache.org/xalan}line-separator";
    public static final String S_KEY_CONTENT_HANDLER = "{http://xml.apache.org/xalan}content-handler";
    public static final String S_KEY_ENTITIES = "{http://xml.apache.org/xalan}entities";
    public static final String S_USE_URL_ESCAPING = "{http://xml.apache.org/xalan}use-url-escaping";
    public static final String S_OMIT_META_TAG = "{http://xml.apache.org/xalan}omit-meta-tag";
    public static final String ORACLE_IS_STANDALONE = "http://www.oracle.com/xml/is-standalone";
    private static final String PROP_DIR = "com/sun/org/apache/xml/internal/serializer/";
    private static final String PROP_FILE_XML = "output_xml.properties";
    private static final String PROP_FILE_TEXT = "output_text.properties";
    private static final String PROP_FILE_HTML = "output_html.properties";
    private static final String PROP_FILE_UNKNOWN = "output_unknown.properties";
    public static final String S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xslt}";
    public static final int S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN = S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL.length();
    private static final String S_XSLT_PREFIX = "xslt.output.";
    private static final int S_XSLT_PREFIX_LEN = S_XSLT_PREFIX.length();
    private static final String S_XALAN_PREFIX = "org.apache.xslt.";
    private static final int S_XALAN_PREFIX_LEN = S_XALAN_PREFIX.length();
    private static Integer m_synch_object = new Integer(1);
    private static Properties m_xml_properties = null;
    private static Properties m_html_properties = null;
    private static Properties m_text_properties = null;
    private static Properties m_unknown_properties = null;
    private static final Class ACCESS_CONTROLLER_CLASS = findAccessControllerClass();

    private static Class findAccessControllerClass() {
        try {
            return Class.forName("java.security.AccessController");
        } catch (Exception e2) {
            return null;
        }
    }

    public static final Properties getDefaultMethodProperties(String method) {
        Properties defaultProperties;
        String fileName = null;
        try {
            synchronized (m_synch_object) {
                if (null == m_xml_properties) {
                    fileName = PROP_FILE_XML;
                    m_xml_properties = loadPropertiesFile(fileName, null);
                }
            }
            if (method.equals("xml")) {
                defaultProperties = m_xml_properties;
            } else if (method.equals("html")) {
                if (null == m_html_properties) {
                    m_html_properties = loadPropertiesFile(PROP_FILE_HTML, m_xml_properties);
                }
                defaultProperties = m_html_properties;
            } else if (method.equals("text")) {
                if (null == m_text_properties) {
                    m_text_properties = loadPropertiesFile(PROP_FILE_TEXT, m_xml_properties);
                    if (null == m_text_properties.getProperty("encoding")) {
                        String mimeEncoding = Encodings.getMimeEncoding(null);
                        m_text_properties.put("encoding", mimeEncoding);
                    }
                }
                defaultProperties = m_text_properties;
            } else if (method.equals("")) {
                if (null == m_unknown_properties) {
                    m_unknown_properties = loadPropertiesFile(PROP_FILE_UNKNOWN, m_xml_properties);
                }
                defaultProperties = m_unknown_properties;
            } else {
                defaultProperties = m_xml_properties;
            }
            return new Properties(defaultProperties);
        } catch (IOException ioe) {
            throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_METHOD_PROPERTY", new Object[]{fileName, method}), ioe);
        }
    }

    /* JADX WARN: Finally extract failed */
    private static Properties loadPropertiesFile(final String resourceName, Properties defaults) throws IOException {
        String newValue;
        Properties props = new Properties(defaults);
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            try {
                if (ACCESS_CONTROLLER_CLASS != null) {
                    is = (InputStream) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.1
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Object run2() {
                            return OutputPropertiesFactory.class.getResourceAsStream(resourceName);
                        }
                    });
                } else {
                    is = OutputPropertiesFactory.class.getResourceAsStream(resourceName);
                }
                bis = new BufferedInputStream(is);
                props.load(bis);
                if (bis != null) {
                    bis.close();
                }
                if (is != null) {
                    is.close();
                }
                Enumeration keys = ((Properties) props.clone()).keys();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement2();
                    String value = null;
                    try {
                        value = SecuritySupport.getSystemProperty(key);
                    } catch (SecurityException e2) {
                    }
                    if (value == null) {
                        value = (String) props.get(key);
                    }
                    String newKey = fixupPropertyString(key, true);
                    String newValue2 = null;
                    try {
                        newValue2 = SecuritySupport.getSystemProperty(newKey);
                    } catch (SecurityException e3) {
                    }
                    if (newValue2 == null) {
                        newValue = fixupPropertyString(value, false);
                    } else {
                        newValue = fixupPropertyString(newValue2, false);
                    }
                    if (key != newKey || value != newValue) {
                        props.remove(key);
                        props.put(newKey, newValue);
                    }
                }
                return props;
            } catch (IOException ioe) {
                if (defaults == null) {
                    throw ioe;
                }
                throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[]{resourceName}), ioe);
            } catch (SecurityException se) {
                if (defaults == null) {
                    throw se;
                }
                throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[]{resourceName}), se);
            }
        } catch (Throwable th) {
            if (bis != null) {
                bis.close();
            }
            if (is != null) {
                is.close();
            }
            throw th;
        }
    }

    private static String fixupPropertyString(String s2, boolean doClipping) {
        if (doClipping && s2.startsWith(S_XSLT_PREFIX)) {
            s2 = s2.substring(S_XSLT_PREFIX_LEN);
        }
        if (s2.startsWith(S_XALAN_PREFIX)) {
            s2 = S_BUILTIN_EXTENSIONS_UNIVERSAL + s2.substring(S_XALAN_PREFIX_LEN);
        }
        int index = s2.indexOf("\\u003a");
        if (index > 0) {
            String temp = s2.substring(index + 6);
            s2 = s2.substring(0, index) + CallSiteDescriptor.TOKEN_DELIMITER + temp;
        }
        return s2;
    }
}
