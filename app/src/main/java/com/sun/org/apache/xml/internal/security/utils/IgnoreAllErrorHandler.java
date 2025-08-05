package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.AccessController;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/IgnoreAllErrorHandler.class */
public class IgnoreAllErrorHandler implements ErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(IgnoreAllErrorHandler.class);
    private static final boolean warnOnExceptions = getProperty("com.sun.org.apache.xml.internal.security.test.warn.on.exceptions");
    private static final boolean throwExceptions = getProperty("com.sun.org.apache.xml.internal.security.test.throw.exceptions");

    private static boolean getProperty(String str) {
        return ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean(str));
        })).booleanValue();
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException sAXParseException) throws SAXException {
        if (warnOnExceptions) {
            LOG.warn("", sAXParseException);
        }
        if (throwExceptions) {
            throw sAXParseException;
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException sAXParseException) throws SAXException {
        if (warnOnExceptions) {
            LOG.error("", sAXParseException);
        }
        if (throwExceptions) {
            throw sAXParseException;
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        if (warnOnExceptions) {
            LOG.warn("", sAXParseException);
        }
        if (throwExceptions) {
            throw sAXParseException;
        }
    }
}
