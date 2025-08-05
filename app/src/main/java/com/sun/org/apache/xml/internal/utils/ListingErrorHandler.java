package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xml.internal.res.XMLMessages;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javafx.fxml.FXMLLoader;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/ListingErrorHandler.class */
public class ListingErrorHandler implements ErrorHandler, ErrorListener {
    protected PrintWriter m_pw;
    protected boolean throwOnWarning = false;
    protected boolean throwOnError = true;
    protected boolean throwOnFatalError = true;

    public ListingErrorHandler(PrintWriter pw) {
        this.m_pw = null;
        if (null == pw) {
            throw new NullPointerException(XMLMessages.createXMLMessage("ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", null));
        }
        this.m_pw = pw;
    }

    public ListingErrorHandler() {
        this.m_pw = null;
        this.m_pw = new PrintWriter((OutputStream) System.err, true);
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException exception) throws SAXException {
        logExceptionLocation(this.m_pw, exception);
        this.m_pw.println("warning: " + exception.getMessage());
        this.m_pw.flush();
        if (getThrowOnWarning()) {
            throw exception;
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException exception) throws SAXException {
        logExceptionLocation(this.m_pw, exception);
        this.m_pw.println("error: " + exception.getMessage());
        this.m_pw.flush();
        if (getThrowOnError()) {
            throw exception;
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException exception) throws SAXException {
        logExceptionLocation(this.m_pw, exception);
        this.m_pw.println("fatalError: " + exception.getMessage());
        this.m_pw.flush();
        if (getThrowOnFatalError()) {
            throw exception;
        }
    }

    @Override // javax.xml.transform.ErrorListener
    public void warning(TransformerException exception) throws TransformerException {
        logExceptionLocation(this.m_pw, exception);
        this.m_pw.println("warning: " + exception.getMessage());
        this.m_pw.flush();
        if (getThrowOnWarning()) {
            throw exception;
        }
    }

    @Override // javax.xml.transform.ErrorListener
    public void error(TransformerException exception) throws TransformerException {
        logExceptionLocation(this.m_pw, exception);
        this.m_pw.println("error: " + exception.getMessage());
        this.m_pw.flush();
        if (getThrowOnError()) {
            throw exception;
        }
    }

    @Override // javax.xml.transform.ErrorListener
    public void fatalError(TransformerException exception) throws TransformerException {
        logExceptionLocation(this.m_pw, exception);
        this.m_pw.println("error: " + exception.getMessage());
        this.m_pw.flush();
        if (getThrowOnError()) {
            throw exception;
        }
    }

    public static void logExceptionLocation(PrintWriter pw, Throwable exception) {
        SourceLocator causeLocator;
        String systemId;
        if (null == pw) {
            pw = new PrintWriter((OutputStream) System.err, true);
        }
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
            if (locator.getPublicId() != locator.getPublicId()) {
                systemId = locator.getPublicId();
            } else {
                systemId = null != locator.getSystemId() ? locator.getSystemId() : "SystemId-Unknown";
            }
            String id = systemId;
            pw.print(id + ":Line=" + locator.getLineNumber() + ";Column=" + locator.getColumnNumber() + ": ");
            pw.println("exception:" + exception.getMessage());
            pw.println("root-cause:" + (null != cause ? cause.getMessage() : FXMLLoader.NULL_KEYWORD));
            logSourceLine(pw, locator);
            return;
        }
        pw.print("SystemId-Unknown:locator-unavailable: ");
        pw.println("exception:" + exception.getMessage());
        pw.println("root-cause:" + (null != cause ? cause.getMessage() : FXMLLoader.NULL_KEYWORD));
    }

    public static void logSourceLine(PrintWriter pw, SourceLocator locator) {
        if (null == locator) {
            return;
        }
        if (null == pw) {
            pw = new PrintWriter((OutputStream) System.err, true);
        }
        String url = locator.getSystemId();
        if (null == url) {
            pw.println("line: (No systemId; cannot read file)");
            pw.println();
            return;
        }
        try {
            int line = locator.getLineNumber();
            int column = locator.getColumnNumber();
            pw.println("line: " + getSourceLine(url, line));
            StringBuffer buf = new StringBuffer("line: ");
            for (int i2 = 1; i2 < column; i2++) {
                buf.append(' ');
            }
            buf.append('^');
            pw.println(buf.toString());
        } catch (Exception e2) {
            pw.println("line: logSourceLine unavailable due to: " + e2.getMessage());
            pw.println();
        }
    }

    protected static String getSourceLine(String sourceUrl, int lineNum) throws Exception {
        URL url;
        try {
            url = new URL(sourceUrl);
        } catch (MalformedURLException mue) {
            int indexOfColon = sourceUrl.indexOf(58);
            int indexOfSlash = sourceUrl.indexOf(47);
            if (indexOfColon != -1 && indexOfSlash != -1 && indexOfColon < indexOfSlash) {
                throw mue;
            }
            url = new URL(SystemIDResolver.getAbsoluteURI(sourceUrl));
        }
        String line = null;
        InputStream is = null;
        BufferedReader br2 = null;
        try {
            URLConnection uc = url.openConnection();
            is = uc.getInputStream();
            br2 = new BufferedReader(new InputStreamReader(is));
            for (int i2 = 1; i2 <= lineNum; i2++) {
                line = br2.readLine();
            }
            br2.close();
            is.close();
            return line;
        } catch (Throwable th) {
            br2.close();
            is.close();
            throw th;
        }
    }

    public void setThrowOnWarning(boolean b2) {
        this.throwOnWarning = b2;
    }

    public boolean getThrowOnWarning() {
        return this.throwOnWarning;
    }

    public void setThrowOnError(boolean b2) {
        this.throwOnError = b2;
    }

    public boolean getThrowOnError() {
        return this.throwOnError;
    }

    public void setThrowOnFatalError(boolean b2) {
        this.throwOnFatalError = b2;
    }

    public boolean getThrowOnFatalError() {
        return this.throwOnFatalError;
    }
}
