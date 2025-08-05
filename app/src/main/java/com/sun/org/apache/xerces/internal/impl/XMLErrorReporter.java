package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
import com.sun.org.apache.xerces.internal.util.ErrorHandlerProxy;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import org.xml.sax.ErrorHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLErrorReporter.class */
public class XMLErrorReporter implements XMLComponent {
    public static final short SEVERITY_WARNING = 0;
    public static final short SEVERITY_ERROR = 1;
    public static final short SEVERITY_FATAL_ERROR = 2;
    protected Locale fLocale;
    protected XMLErrorHandler fErrorHandler;
    protected XMLLocator fLocator;
    protected boolean fContinueAfterFatalError;
    protected XMLErrorHandler fDefaultErrorHandler;
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    private static final String[] RECOGNIZED_FEATURES = {CONTINUE_AFTER_FATAL_ERROR};
    private static final Boolean[] FEATURE_DEFAULTS = {null};
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    private static final String[] RECOGNIZED_PROPERTIES = {ERROR_HANDLER};
    private static final Object[] PROPERTY_DEFAULTS = {null};
    private ErrorHandler fSaxProxy = null;
    protected Map<String, MessageFormatter> fMessageFormatters = new HashMap();

    public void setLocale(Locale locale) {
        this.fLocale = locale;
    }

    public Locale getLocale() {
        return this.fLocale;
    }

    public void setDocumentLocator(XMLLocator locator) {
        this.fLocator = locator;
    }

    public void putMessageFormatter(String domain, MessageFormatter messageFormatter) {
        this.fMessageFormatters.put(domain, messageFormatter);
    }

    public MessageFormatter getMessageFormatter(String domain) {
        return this.fMessageFormatters.get(domain);
    }

    public MessageFormatter removeMessageFormatter(String domain) {
        return this.fMessageFormatters.remove(domain);
    }

    public String reportError(String domain, String key, Object[] arguments, short severity) throws XNIException {
        return reportError(this.fLocator, domain, key, arguments, severity);
    }

    public String reportError(String domain, String key, Object[] arguments, short severity, Exception exception) throws XNIException {
        return reportError(this.fLocator, domain, key, arguments, severity, exception);
    }

    public String reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity) throws XNIException {
        return reportError(location, domain, key, arguments, severity, null);
    }

    public String reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity, Exception exception) throws MissingResourceException, XNIException {
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
        XMLParseException parseException = exception != null ? new XMLParseException(location, message, exception) : new XMLParseException(location, message);
        XMLErrorHandler errorHandler = this.fErrorHandler;
        if (errorHandler == null) {
            if (this.fDefaultErrorHandler == null) {
                this.fDefaultErrorHandler = new DefaultErrorHandler();
            }
            errorHandler = this.fDefaultErrorHandler;
        }
        switch (severity) {
            case 0:
                errorHandler.warning(domain, key, parseException);
                break;
            case 1:
                errorHandler.error(domain, key, parseException);
                break;
            case 2:
                errorHandler.fatalError(domain, key, parseException);
                if (!this.fContinueAfterFatalError) {
                    throw parseException;
                }
                break;
        }
        return message;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XNIException {
        this.fContinueAfterFatalError = componentManager.getFeature(CONTINUE_AFTER_FATAL_ERROR, false);
        this.fErrorHandler = (XMLErrorHandler) componentManager.getProperty(ERROR_HANDLER);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            int suffixLength = featureId.length() - Constants.XERCES_FEATURE_PREFIX.length();
            if (suffixLength == Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE.length() && featureId.endsWith(Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE)) {
                this.fContinueAfterFatalError = state;
            }
        }
    }

    public boolean getFeature(String featureId) throws XMLConfigurationException {
        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            int suffixLength = featureId.length() - Constants.XERCES_FEATURE_PREFIX.length();
            if (suffixLength == Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE.length() && featureId.endsWith(Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE)) {
                return this.fContinueAfterFatalError;
            }
            return false;
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.ERROR_HANDLER_PROPERTY.length() && propertyId.endsWith(Constants.ERROR_HANDLER_PROPERTY)) {
                this.fErrorHandler = (XMLErrorHandler) value;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        for (int i2 = 0; i2 < RECOGNIZED_FEATURES.length; i2++) {
            if (RECOGNIZED_FEATURES[i2].equals(featureId)) {
                return FEATURE_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        for (int i2 = 0; i2 < RECOGNIZED_PROPERTIES.length; i2++) {
            if (RECOGNIZED_PROPERTIES[i2].equals(propertyId)) {
                return PROPERTY_DEFAULTS[i2];
            }
        }
        return null;
    }

    public XMLErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    public ErrorHandler getSAXErrorHandler() {
        if (this.fSaxProxy == null) {
            this.fSaxProxy = new ErrorHandlerProxy() { // from class: com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.1
                @Override // com.sun.org.apache.xerces.internal.util.ErrorHandlerProxy
                protected XMLErrorHandler getErrorHandler() {
                    return XMLErrorReporter.this.fErrorHandler;
                }
            };
        }
        return this.fSaxProxy;
    }
}
