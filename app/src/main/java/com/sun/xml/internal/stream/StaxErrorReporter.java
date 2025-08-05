package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.util.MissingResourceException;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/stream/StaxErrorReporter.class */
public class StaxErrorReporter extends XMLErrorReporter {
    protected XMLReporter fXMLReporter = null;

    public StaxErrorReporter(PropertyManager propertyManager) {
        putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", new XMLMessageFormatter());
        reset(propertyManager);
    }

    public StaxErrorReporter() {
        putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", new XMLMessageFormatter());
    }

    public void reset(PropertyManager propertyManager) {
        this.fXMLReporter = (XMLReporter) propertyManager.getProperty(XMLInputFactory.REPORTER);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLErrorReporter
    public String reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity) throws MissingResourceException, XNIException {
        String message;
        MessageFormatter messageFormatter = getMessageFormatter(domain);
        if (messageFormatter != null) {
            message = messageFormatter.formatMessage(this.fLocale, key, arguments);
        } else {
            StringBuffer str = new StringBuffer();
            str.append(domain);
            str.append('#');
            str.append(key);
            int argCount = arguments != null ? arguments.length : 0;
            if (argCount > 0) {
                str.append('?');
                for (int i2 = 0; i2 < argCount; i2++) {
                    str.append(arguments[i2]);
                    if (i2 < argCount - 1) {
                        str.append('&');
                    }
                }
            }
            message = str.toString();
        }
        switch (severity) {
            case 0:
                try {
                    if (this.fXMLReporter != null) {
                        this.fXMLReporter.report(message, "WARNING", null, convertToStaxLocation(location));
                    }
                    break;
                } catch (XMLStreamException ex) {
                    throw new XNIException(ex);
                }
            case 1:
                try {
                    if (this.fXMLReporter != null) {
                        this.fXMLReporter.report(message, "ERROR", null, convertToStaxLocation(location));
                    }
                    break;
                } catch (XMLStreamException ex2) {
                    throw new XNIException(ex2);
                }
            case 2:
                if (!this.fContinueAfterFatalError) {
                    throw new XNIException(message);
                }
                break;
        }
        return message;
    }

    Location convertToStaxLocation(final XMLLocator location) {
        return new Location() { // from class: com.sun.xml.internal.stream.StaxErrorReporter.1
            @Override // javax.xml.stream.Location
            public int getColumnNumber() {
                return location.getColumnNumber();
            }

            @Override // javax.xml.stream.Location
            public int getLineNumber() {
                return location.getLineNumber();
            }

            @Override // javax.xml.stream.Location
            public String getPublicId() {
                return location.getPublicId();
            }

            @Override // javax.xml.stream.Location
            public String getSystemId() {
                return location.getLiteralSystemId();
            }

            @Override // javax.xml.stream.Location
            public int getCharacterOffset() {
                return location.getCharacterOffset();
            }

            public String getLocationURI() {
                return "";
            }
        };
    }
}
