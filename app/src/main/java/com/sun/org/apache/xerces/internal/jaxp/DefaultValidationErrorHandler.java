package com.sun.org.apache.xerces.internal.jaxp;

import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/DefaultValidationErrorHandler.class */
class DefaultValidationErrorHandler extends DefaultHandler {
    private static int ERROR_COUNT_LIMIT = 10;
    private int errorCount = 0;
    private Locale locale;

    public DefaultValidationErrorHandler(Locale locale) {
        this.locale = Locale.getDefault();
        this.locale = locale;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void error(SAXParseException e2) throws SAXException {
        if (this.errorCount >= ERROR_COUNT_LIMIT) {
            return;
        }
        if (this.errorCount == 0) {
            System.err.println(SAXMessageFormatter.formatMessage(this.locale, "errorHandlerNotSet", new Object[]{Integer.valueOf(this.errorCount)}));
        }
        String systemId = e2.getSystemId();
        if (systemId == null) {
            systemId = FXMLLoader.NULL_KEYWORD;
        }
        String message = "Error: URI=" + systemId + " Line=" + e2.getLineNumber() + ": " + e2.getMessage();
        System.err.println(message);
        this.errorCount++;
    }
}
