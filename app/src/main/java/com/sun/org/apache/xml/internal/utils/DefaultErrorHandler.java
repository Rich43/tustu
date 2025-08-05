package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xml.internal.res.XMLMessages;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.commons.math3.geometry.VectorFormat;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/DefaultErrorHandler.class */
public class DefaultErrorHandler implements ErrorHandler, ErrorListener {
    PrintWriter m_pw;
    boolean m_throwExceptionOnError;

    public DefaultErrorHandler(PrintWriter pw) {
        this.m_throwExceptionOnError = true;
        this.m_pw = pw;
    }

    public DefaultErrorHandler(PrintStream pw) {
        this.m_throwExceptionOnError = true;
        this.m_pw = new PrintWriter((OutputStream) pw, true);
    }

    public DefaultErrorHandler() {
        this(true);
    }

    public DefaultErrorHandler(boolean throwExceptionOnError) {
        this.m_throwExceptionOnError = true;
        this.m_pw = new PrintWriter((OutputStream) System.err, true);
        this.m_throwExceptionOnError = throwExceptionOnError;
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException exception) throws SAXException {
        printLocation(this.m_pw, exception);
        this.m_pw.println("Parser warning: " + exception.getMessage());
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override // javax.xml.transform.ErrorListener
    public void warning(TransformerException exception) throws TransformerException {
        printLocation(this.m_pw, exception);
        this.m_pw.println(exception.getMessage());
    }

    @Override // javax.xml.transform.ErrorListener
    public void error(TransformerException exception) throws TransformerException {
        if (this.m_throwExceptionOnError) {
            throw exception;
        }
        printLocation(this.m_pw, exception);
        this.m_pw.println(exception.getMessage());
    }

    @Override // javax.xml.transform.ErrorListener
    public void fatalError(TransformerException exception) throws TransformerException {
        if (this.m_throwExceptionOnError) {
            throw exception;
        }
        printLocation(this.m_pw, exception);
        this.m_pw.println(exception.getMessage());
    }

    public static void ensureLocationSet(TransformerException exception) {
        SourceLocator causeLocator;
        SourceLocator locator = null;
        Throwable cause = exception;
        do {
            if (cause instanceof SAXParseException) {
                locator = new SAXSourceLocator((SAXParseException) cause);
            } else if ((cause instanceof TransformerException) && null != (causeLocator = ((TransformerException) cause).getLocator())) {
                locator = causeLocator;
            }
            if (cause instanceof TransformerException) {
                cause = ((TransformerException) cause).getCause();
            } else if (cause instanceof SAXException) {
                cause = ((SAXException) cause).getException();
            } else {
                cause = null;
            }
        } while (null != cause);
        exception.setLocator(locator);
    }

    public static void printLocation(PrintStream pw, TransformerException exception) {
        printLocation(new PrintWriter(pw), exception);
    }

    public static void printLocation(PrintStream pw, SAXParseException exception) {
        printLocation(new PrintWriter(pw), exception);
    }

    public static void printLocation(PrintWriter pw, Throwable exception) {
        SourceLocator causeLocator;
        String systemId;
        SourceLocator locator = null;
        Throwable cause = exception;
        do {
            if (cause instanceof SAXParseException) {
                locator = new SAXSourceLocator((SAXParseException) cause);
            } else if ((cause instanceof TransformerException) && null != (causeLocator = ((TransformerException) cause).getLocator())) {
                locator = causeLocator;
            }
            if (cause instanceof TransformerException) {
                cause = ((TransformerException) cause).getCause();
            } else if (cause instanceof WrappedRuntimeException) {
                cause = ((WrappedRuntimeException) cause).getException();
            } else if (cause instanceof SAXException) {
                cause = ((SAXException) cause).getException();
            } else {
                cause = null;
            }
        } while (null != cause);
        if (null != locator) {
            if (null != locator.getPublicId()) {
                systemId = locator.getPublicId();
            } else {
                systemId = null != locator.getSystemId() ? locator.getSystemId() : XMLMessages.createXMLMessage("ER_SYSTEMID_UNKNOWN", null);
            }
            String id = systemId;
            pw.print(id + VectorFormat.DEFAULT_SEPARATOR + XMLMessages.createXMLMessage("line", null) + locator.getLineNumber() + VectorFormat.DEFAULT_SEPARATOR + XMLMessages.createXMLMessage("column", null) + locator.getColumnNumber() + VectorFormat.DEFAULT_SEPARATOR);
            return;
        }
        pw.print("(" + XMLMessages.createXMLMessage("ER_LOCATION_UNKNOWN", null) + ")");
    }
}
