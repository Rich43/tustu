package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.XalanConstants;
import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.FeaturePropertyBase;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xalan.internal.xsltc.compiler.SourceLoader;
import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
import com.sun.org.apache.xml.internal.utils.StopParseException;
import com.sun.org.apache.xml.internal.utils.StylesheetPIHandler;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javafx.fxml.FXMLLoader;
import javax.xml.XMLConstants;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jdk.xml.internal.JdkXmlFeatures;
import jdk.xml.internal.JdkXmlUtils;
import jdk.xml.internal.XMLSecurityManager;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TransformerFactoryImpl.class */
public class TransformerFactoryImpl extends SAXTransformerFactory implements SourceLoader, ErrorListener {
    public static final String TRANSLET_NAME = "translet-name";
    public static final String DESTINATION_DIRECTORY = "destination-directory";
    public static final String PACKAGE_NAME = "package-name";
    public static final String JAR_NAME = "jar-name";
    public static final String GENERATE_TRANSLET = "generate-translet";
    public static final String AUTO_TRANSLET = "auto-translet";
    public static final String USE_CLASSPATH = "use-classpath";
    public static final String DEBUG = "debug";
    public static final String ENABLE_INLINING = "enable-inlining";
    public static final String INDENT_NUMBER = "indent-number";
    protected static final String DEFAULT_TRANSLET_NAME = "GregorSamsa";
    private boolean _isNotSecureProcessing;
    private boolean _isSecureMode;
    private boolean _overrideDefaultParser;
    private String _accessExternalStylesheet;
    private String _accessExternalDTD;
    private XMLSecurityPropertyManager _xmlSecurityPropertyMgr;
    private XMLSecurityManager _xmlSecurityManager;
    private final JdkXmlFeatures _xmlFeatures;
    private Map<String, Class> _xsltcExtensionFunctions;
    private ErrorListener _errorListener = this;
    private URIResolver _uriResolver = null;
    private String _transletName = DEFAULT_TRANSLET_NAME;
    private String _destinationDirectory = null;
    private String _packageName = null;
    private String _jarFileName = null;
    private Map<Source, PIParamWrapper> _piParams = null;
    private boolean _debug = false;
    private boolean _enableInlining = false;
    private boolean _generateTranslet = false;
    private boolean _autoTranslet = false;
    private boolean _useClasspath = false;
    private int _indentNumber = -1;
    private ClassLoader _extensionClassLoader = null;

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TransformerFactoryImpl$PIParamWrapper.class */
    private static class PIParamWrapper {
        public String _media;
        public String _title;
        public String _charset;

        public PIParamWrapper(String media, String title, String charset) {
            this._media = null;
            this._title = null;
            this._charset = null;
            this._media = media;
            this._title = title;
            this._charset = charset;
        }
    }

    public TransformerFactoryImpl() {
        this._isNotSecureProcessing = true;
        this._isSecureMode = false;
        this._accessExternalStylesheet = "all";
        this._accessExternalDTD = "all";
        if (System.getSecurityManager() != null) {
            this._isSecureMode = true;
            this._isNotSecureProcessing = false;
        }
        this._xmlFeatures = new JdkXmlFeatures(!this._isNotSecureProcessing);
        this._overrideDefaultParser = this._xmlFeatures.getFeature(JdkXmlFeatures.XmlFeature.JDK_OVERRIDE_PARSER);
        this._xmlSecurityPropertyMgr = new XMLSecurityPropertyManager();
        this._accessExternalDTD = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
        this._accessExternalStylesheet = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET);
        this._xmlSecurityManager = new XMLSecurityManager(true);
        this._xsltcExtensionFunctions = null;
    }

    public Map<String, Class> getExternalExtensionsMap() {
        return this._xsltcExtensionFunctions;
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
        if (listener == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.ERROR_LISTENER_NULL_ERR, "TransformerFactory");
            throw new IllegalArgumentException(err.toString());
        }
        this._errorListener = listener;
    }

    @Override // javax.xml.transform.TransformerFactory
    public ErrorListener getErrorListener() {
        return this._errorListener;
    }

    @Override // javax.xml.transform.TransformerFactory
    public Object getAttribute(String name) throws IllegalArgumentException {
        if (name.equals(TRANSLET_NAME)) {
            return this._transletName;
        }
        if (name.equals(GENERATE_TRANSLET)) {
            return new Boolean(this._generateTranslet);
        }
        if (name.equals(AUTO_TRANSLET)) {
            return new Boolean(this._autoTranslet);
        }
        if (name.equals(ENABLE_INLINING)) {
            if (this._enableInlining) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        if (name.equals("http://apache.org/xml/properties/security-manager")) {
            return this._xmlSecurityManager;
        }
        if (name.equals(XalanConstants.JDK_EXTENSION_CLASSLOADER)) {
            return this._extensionClassLoader;
        }
        String propertyValue = this._xmlSecurityManager != null ? this._xmlSecurityManager.getLimitAsString(name) : null;
        if (propertyValue != null) {
            return propertyValue;
        }
        String propertyValue2 = this._xmlSecurityPropertyMgr != null ? this._xmlSecurityPropertyMgr.getValue(name) : null;
        if (propertyValue2 != null) {
            return propertyValue2;
        }
        ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_INVALID_ATTR_ERR, name);
        throw new IllegalArgumentException(err.toString());
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        if (name.equals(TRANSLET_NAME) && (value instanceof String)) {
            this._transletName = (String) value;
            return;
        }
        if (name.equals(DESTINATION_DIRECTORY) && (value instanceof String)) {
            this._destinationDirectory = (String) value;
            return;
        }
        if (name.equals(PACKAGE_NAME) && (value instanceof String)) {
            this._packageName = (String) value;
            return;
        }
        if (name.equals(JAR_NAME) && (value instanceof String)) {
            this._jarFileName = (String) value;
            return;
        }
        if (name.equals(GENERATE_TRANSLET)) {
            if (value instanceof Boolean) {
                this._generateTranslet = ((Boolean) value).booleanValue();
                return;
            } else if (value instanceof String) {
                this._generateTranslet = ((String) value).equalsIgnoreCase("true");
                return;
            }
        } else if (name.equals(AUTO_TRANSLET)) {
            if (value instanceof Boolean) {
                this._autoTranslet = ((Boolean) value).booleanValue();
                return;
            } else if (value instanceof String) {
                this._autoTranslet = ((String) value).equalsIgnoreCase("true");
                return;
            }
        } else if (name.equals(USE_CLASSPATH)) {
            if (value instanceof Boolean) {
                this._useClasspath = ((Boolean) value).booleanValue();
                return;
            } else if (value instanceof String) {
                this._useClasspath = ((String) value).equalsIgnoreCase("true");
                return;
            }
        } else if (name.equals(DEBUG)) {
            if (value instanceof Boolean) {
                this._debug = ((Boolean) value).booleanValue();
                return;
            } else if (value instanceof String) {
                this._debug = ((String) value).equalsIgnoreCase("true");
                return;
            }
        } else if (name.equals(ENABLE_INLINING)) {
            if (value instanceof Boolean) {
                this._enableInlining = ((Boolean) value).booleanValue();
                return;
            } else if (value instanceof String) {
                this._enableInlining = ((String) value).equalsIgnoreCase("true");
                return;
            }
        } else if (name.equals(INDENT_NUMBER)) {
            if (value instanceof String) {
                try {
                    this._indentNumber = Integer.parseInt((String) value);
                    return;
                } catch (NumberFormatException e2) {
                }
            } else if (value instanceof Integer) {
                this._indentNumber = ((Integer) value).intValue();
                return;
            }
        } else if (name.equals(XalanConstants.JDK_EXTENSION_CLASSLOADER)) {
            if (value instanceof ClassLoader) {
                this._extensionClassLoader = (ClassLoader) value;
                return;
            } else {
                ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_INVALID_ATTR_VALUE_ERR, "Extension Functions ClassLoader");
                throw new IllegalArgumentException(err.toString());
            }
        }
        if (this._xmlSecurityManager != null && this._xmlSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, value)) {
            return;
        }
        if (this._xmlSecurityPropertyMgr != null && this._xmlSecurityPropertyMgr.setValue(name, FeaturePropertyBase.State.APIPROPERTY, value)) {
            this._accessExternalDTD = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
            this._accessExternalStylesheet = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET);
        } else {
            ErrorMsg err2 = new ErrorMsg(ErrorMsg.JAXP_INVALID_ATTR_ERR, name);
            throw new IllegalArgumentException(err2.toString());
        }
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setFeature(String name, boolean value) throws TransformerConfigurationException {
        if (name == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_SET_FEATURE_NULL_NAME);
            throw new NullPointerException(err.toString());
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            if (this._isSecureMode && !value) {
                ErrorMsg err2 = new ErrorMsg(ErrorMsg.JAXP_SECUREPROCESSING_FEATURE);
                throw new TransformerConfigurationException(err2.toString());
            }
            this._isNotSecureProcessing = !value;
            this._xmlSecurityManager.setSecureProcessing(value);
            if (value && XalanConstants.IS_JDK8_OR_ABOVE) {
                this._xmlSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, FeaturePropertyBase.State.FSP, "");
                this._xmlSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET, FeaturePropertyBase.State.FSP, "");
                this._accessExternalDTD = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
                this._accessExternalStylesheet = this._xmlSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_STYLESHEET);
            }
            if (value && this._xmlFeatures != null) {
                this._xmlFeatures.setFeature(JdkXmlFeatures.XmlFeature.ENABLE_EXTENSION_FUNCTION, JdkXmlFeatures.State.FSP, false);
                return;
            }
            return;
        }
        if (name.equals("http://www.oracle.com/feature/use-service-mechanism") && this._isSecureMode) {
            return;
        }
        if (this._xmlFeatures != null && this._xmlFeatures.setFeature(name, JdkXmlFeatures.State.APIPROPERTY, Boolean.valueOf(value))) {
            if (name.equals(JdkXmlUtils.OVERRIDE_PARSER) || name.equals("http://www.oracle.com/feature/use-service-mechanism")) {
                this._overrideDefaultParser = this._xmlFeatures.getFeature(JdkXmlFeatures.XmlFeature.JDK_OVERRIDE_PARSER);
                return;
            }
            return;
        }
        ErrorMsg err3 = new ErrorMsg(ErrorMsg.JAXP_UNSUPPORTED_FEATURE, name);
        throw new TransformerConfigurationException(err3.toString());
    }

    @Override // javax.xml.transform.TransformerFactory
    public boolean getFeature(String name) {
        String[] features = {DOMSource.FEATURE, DOMResult.FEATURE, SAXSource.FEATURE, SAXResult.FEATURE, StAXSource.FEATURE, StAXResult.FEATURE, StreamSource.FEATURE, StreamResult.FEATURE, SAXTransformerFactory.FEATURE, SAXTransformerFactory.FEATURE_XMLFILTER, "http://www.oracle.com/feature/use-service-mechanism"};
        if (name == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_GET_FEATURE_NULL_NAME);
            throw new NullPointerException(err.toString());
        }
        for (String str : features) {
            if (name.equals(str)) {
                return true;
            }
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return !this._isNotSecureProcessing;
        }
        int index = this._xmlFeatures.getIndex(name);
        if (index > -1) {
            return this._xmlFeatures.getFeature(index);
        }
        return false;
    }

    public boolean overrideDefaultParser() {
        return this._overrideDefaultParser;
    }

    public JdkXmlFeatures getJdkXmlFeatures() {
        return this._xmlFeatures;
    }

    @Override // javax.xml.transform.TransformerFactory
    public URIResolver getURIResolver() {
        return this._uriResolver;
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setURIResolver(URIResolver resolver) {
        this._uriResolver = resolver;
    }

    @Override // javax.xml.transform.TransformerFactory
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException, NullPointerException {
        XMLReader reader = null;
        StylesheetPIHandler _stylesheetPIHandler = new StylesheetPIHandler(null, media, title, charset);
        try {
            if (source instanceof DOMSource) {
                DOMSource domsrc = (DOMSource) source;
                String baseId = domsrc.getSystemId();
                Node node = domsrc.getNode();
                DOM2SAX dom2sax = new DOM2SAX(node);
                _stylesheetPIHandler.setBaseId(baseId);
                dom2sax.setContentHandler(_stylesheetPIHandler);
                dom2sax.parse();
            } else {
                if (source instanceof SAXSource) {
                    reader = ((SAXSource) source).getXMLReader();
                }
                InputSource isource = SAXSource.sourceToInputSource(source);
                String baseId2 = isource.getSystemId();
                if (reader == null) {
                    reader = JdkXmlUtils.getXMLReader(this._overrideDefaultParser, !this._isNotSecureProcessing);
                }
                _stylesheetPIHandler.setBaseId(baseId2);
                reader.setContentHandler(_stylesheetPIHandler);
                reader.parse(isource);
            }
            if (this._uriResolver != null) {
                _stylesheetPIHandler.setURIResolver(this._uriResolver);
            }
        } catch (StopParseException e2) {
        } catch (IOException ioe) {
            throw new TransformerConfigurationException("getAssociatedStylesheets failed", ioe);
        } catch (SAXException se) {
            throw new TransformerConfigurationException("getAssociatedStylesheets failed", se);
        }
        return _stylesheetPIHandler.getAssociatedStylesheet();
    }

    @Override // javax.xml.transform.TransformerFactory
    public Transformer newTransformer() throws TransformerConfigurationException {
        TransformerImpl result = new TransformerImpl(new Properties(), this._indentNumber, this);
        if (this._uriResolver != null) {
            result.setURIResolver(this._uriResolver);
        }
        if (!this._isNotSecureProcessing) {
            result.setSecureProcessing(true);
        }
        return result;
    }

    @Override // javax.xml.transform.TransformerFactory
    public Transformer newTransformer(Source source) throws TransformerConfigurationException, ConfigurationError {
        Templates templates = newTemplates(source);
        Transformer transformer = templates.newTransformer();
        if (this._uriResolver != null) {
            transformer.setURIResolver(this._uriResolver);
        }
        return transformer;
    }

    private void passWarningsToListener(ArrayList<ErrorMsg> messages) throws TransformerException {
        if (this._errorListener == null || messages == null) {
            return;
        }
        int count = messages.size();
        for (int pos = 0; pos < count; pos++) {
            ErrorMsg msg = messages.get(pos);
            if (msg.isWarningError()) {
                this._errorListener.error(new TransformerConfigurationException(msg.toString()));
            } else {
                this._errorListener.warning(new TransformerConfigurationException(msg.toString()));
            }
        }
    }

    private void passErrorsToListener(ArrayList<ErrorMsg> messages) {
        try {
            if (this._errorListener == null || messages == null) {
                return;
            }
            int count = messages.size();
            for (int pos = 0; pos < count; pos++) {
                String message = messages.get(pos).toString();
                this._errorListener.error(new TransformerException(message));
            }
        } catch (TransformerException e2) {
        }
    }

    @Override // javax.xml.transform.TransformerFactory
    public Templates newTemplates(Source source) throws TransformerConfigurationException, ConfigurationError {
        ErrorMsg err;
        TransformerConfigurationException exc;
        PIParamWrapper p2;
        byte[][] bytecodes;
        if (this._useClasspath) {
            String transletName = getTransletBaseName(source);
            if (this._packageName != null) {
                transletName = this._packageName + "." + transletName;
            }
            try {
                Class clazz = ObjectFactory.findProviderClass(transletName, true);
                resetTransientAttributes();
                return new TemplatesImpl(new Class[]{clazz}, transletName, (Properties) null, this._indentNumber, this);
            } catch (ClassNotFoundException e2) {
                ErrorMsg err2 = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, transletName);
                throw new TransformerConfigurationException(err2.toString());
            } catch (Exception e3) {
                ErrorMsg err3 = new ErrorMsg(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + e3.getMessage());
                throw new TransformerConfigurationException(err3.toString());
            }
        }
        if (this._autoTranslet) {
            String transletClassName = getTransletBaseName(source);
            if (this._packageName != null) {
                transletClassName = this._packageName + "." + transletClassName;
            }
            if (this._jarFileName != null) {
                bytecodes = getBytecodesFromJar(source, transletClassName);
            } else {
                bytecodes = getBytecodesFromClasses(source, transletClassName);
            }
            if (bytecodes != null) {
                if (this._debug) {
                    if (this._jarFileName != null) {
                        System.err.println(new ErrorMsg(ErrorMsg.TRANSFORM_WITH_JAR_STR, transletClassName, this._jarFileName));
                    } else {
                        System.err.println(new ErrorMsg(ErrorMsg.TRANSFORM_WITH_TRANSLET_STR, transletClassName));
                    }
                }
                resetTransientAttributes();
                return new TemplatesImpl(bytecodes, transletClassName, (Properties) null, this._indentNumber, this);
            }
        }
        XSLTC xsltc = new XSLTC(this._xmlFeatures);
        if (this._debug) {
            xsltc.setDebug(true);
        }
        if (this._enableInlining) {
            xsltc.setTemplateInlining(true);
        } else {
            xsltc.setTemplateInlining(false);
        }
        if (!this._isNotSecureProcessing) {
            xsltc.setSecureProcessing(true);
        }
        xsltc.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, this._accessExternalStylesheet);
        xsltc.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this._accessExternalDTD);
        xsltc.setProperty("http://apache.org/xml/properties/security-manager", this._xmlSecurityManager);
        xsltc.setProperty(XalanConstants.JDK_EXTENSION_CLASSLOADER, this._extensionClassLoader);
        xsltc.init();
        if (!this._isNotSecureProcessing) {
            this._xsltcExtensionFunctions = xsltc.getExternalExtensionFunctions();
        }
        if (this._uriResolver != null) {
            xsltc.setSourceLoader(this);
        }
        if (this._piParams != null && this._piParams.get(source) != null && (p2 = this._piParams.get(source)) != null) {
            xsltc.setPIParameters(p2._media, p2._title, p2._charset);
        }
        int outputType = 2;
        if (this._generateTranslet || this._autoTranslet) {
            xsltc.setClassName(getTransletBaseName(source));
            if (this._destinationDirectory != null) {
                xsltc.setDestDirectory(this._destinationDirectory);
            } else {
                String xslName = getStylesheetFileName(source);
                if (xslName != null) {
                    File xslFile = new File(xslName);
                    String xslDir = xslFile.getParent();
                    if (xslDir != null) {
                        xsltc.setDestDirectory(xslDir);
                    }
                }
            }
            if (this._packageName != null) {
                xsltc.setPackageName(this._packageName);
            }
            if (this._jarFileName != null) {
                xsltc.setJarFileName(this._jarFileName);
                outputType = 5;
            } else {
                outputType = 4;
            }
        }
        InputSource input = Util.getInputSource(xsltc, source);
        byte[][] bytecodes2 = xsltc.compile(null, input, outputType);
        String transletName2 = xsltc.getClassName();
        if ((this._generateTranslet || this._autoTranslet) && bytecodes2 != null && this._jarFileName != null) {
            try {
                xsltc.outputToJar();
            } catch (IOException e4) {
            }
        }
        resetTransientAttributes();
        if (this._errorListener != this) {
            try {
                passWarningsToListener(xsltc.getWarnings());
            } catch (TransformerException e5) {
                throw new TransformerConfigurationException(e5);
            }
        } else {
            xsltc.printWarnings();
        }
        if (bytecodes2 == null) {
            ArrayList<ErrorMsg> errs = xsltc.getErrors();
            if (errs != null) {
                err = errs.get(errs.size() - 1);
            } else {
                err = new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR);
            }
            Throwable cause = err.getCause();
            if (cause != null) {
                exc = new TransformerConfigurationException(cause.getMessage(), cause);
            } else {
                exc = new TransformerConfigurationException(err.toString());
            }
            if (this._errorListener != null) {
                passErrorsToListener(xsltc.getErrors());
                try {
                    this._errorListener.fatalError(exc);
                } catch (TransformerException e6) {
                }
            } else {
                xsltc.printErrors();
            }
            throw exc;
        }
        return new TemplatesImpl(bytecodes2, transletName2, xsltc.getOutputProperties(), this._indentNumber, this);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        TemplatesHandlerImpl handler = new TemplatesHandlerImpl(this._indentNumber, this);
        if (this._uriResolver != null) {
            handler.setURIResolver(this._uriResolver);
        }
        return handler;
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
        Transformer transformer = newTransformer();
        if (this._uriResolver != null) {
            transformer.setURIResolver(this._uriResolver);
        }
        return new TransformerHandlerImpl((TransformerImpl) transformer);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException, ConfigurationError {
        Transformer transformer = newTransformer(src);
        if (this._uriResolver != null) {
            transformer.setURIResolver(this._uriResolver);
        }
        return new TransformerHandlerImpl((TransformerImpl) transformer);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        Transformer transformer = templates.newTransformer();
        TransformerImpl internal = (TransformerImpl) transformer;
        return new TransformerHandlerImpl(internal);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException, ConfigurationError {
        Templates templates = newTemplates(src);
        if (templates == null) {
            return null;
        }
        return newXMLFilter(templates);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        try {
            return new TrAXFilter(templates);
        } catch (TransformerConfigurationException e1) {
            if (this._errorListener != null) {
                try {
                    this._errorListener.fatalError(e1);
                    return null;
                } catch (TransformerException e2) {
                    new TransformerConfigurationException(e2);
                    throw e1;
                }
            }
            throw e1;
        }
    }

    @Override // javax.xml.transform.ErrorListener
    public void error(TransformerException e2) throws TransformerException {
        Throwable wrapped = e2.getException();
        if (wrapped != null) {
            System.err.println(new ErrorMsg(ErrorMsg.ERROR_PLUS_WRAPPED_MSG, e2.getMessageAndLocation(), wrapped.getMessage()));
        } else {
            System.err.println(new ErrorMsg(ErrorMsg.ERROR_MSG, e2.getMessageAndLocation()));
        }
        throw e2;
    }

    @Override // javax.xml.transform.ErrorListener
    public void fatalError(TransformerException e2) throws TransformerException {
        Throwable wrapped = e2.getException();
        if (wrapped != null) {
            System.err.println(new ErrorMsg(ErrorMsg.FATAL_ERR_PLUS_WRAPPED_MSG, e2.getMessageAndLocation(), wrapped.getMessage()));
        } else {
            System.err.println(new ErrorMsg(ErrorMsg.FATAL_ERR_MSG, e2.getMessageAndLocation()));
        }
        throw e2;
    }

    @Override // javax.xml.transform.ErrorListener
    public void warning(TransformerException e2) throws TransformerException {
        Throwable wrapped = e2.getException();
        if (wrapped != null) {
            System.err.println(new ErrorMsg(ErrorMsg.WARNING_PLUS_WRAPPED_MSG, e2.getMessageAndLocation(), wrapped.getMessage()));
        } else {
            System.err.println(new ErrorMsg(ErrorMsg.WARNING_MSG, e2.getMessageAndLocation()));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SourceLoader
    public InputSource loadSource(String href, String context, XSLTC xsltc) {
        Source source;
        try {
            if (this._uriResolver != null && (source = this._uriResolver.resolve(href, context)) != null) {
                return Util.getInputSource(xsltc, source);
            }
            return null;
        } catch (TransformerException e2) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.INVALID_URI_ERR, href + "\n" + e2.getMessage(), this);
            xsltc.getParser().reportError(2, msg);
            return null;
        }
    }

    private void resetTransientAttributes() {
        this._transletName = DEFAULT_TRANSLET_NAME;
        this._destinationDirectory = null;
        this._packageName = null;
        this._jarFileName = null;
    }

    private byte[][] getBytecodesFromClasses(Source source, String fullClassName) {
        String transletName;
        String transletPath;
        if (fullClassName == null) {
            return (byte[][]) null;
        }
        String xslFileName = getStylesheetFileName(source);
        File xslFile = null;
        if (xslFileName != null) {
            xslFile = new File(xslFileName);
        }
        int lastDotIndex = fullClassName.lastIndexOf(46);
        if (lastDotIndex > 0) {
            transletName = fullClassName.substring(lastDotIndex + 1);
        } else {
            transletName = fullClassName;
        }
        String transletPath2 = fullClassName.replace('.', '/');
        if (this._destinationDirectory != null) {
            transletPath = this._destinationDirectory + "/" + transletPath2 + ".class";
        } else if (xslFile != null && xslFile.getParent() != null) {
            transletPath = xslFile.getParent() + "/" + transletPath2 + ".class";
        } else {
            transletPath = transletPath2 + ".class";
        }
        File transletFile = new File(transletPath);
        if (!transletFile.exists()) {
            return (byte[][]) null;
        }
        if (xslFile != null && xslFile.exists()) {
            long xslTimestamp = xslFile.lastModified();
            long transletTimestamp = transletFile.lastModified();
            if (transletTimestamp < xslTimestamp) {
                return (byte[][]) null;
            }
        }
        Vector bytecodes = new Vector();
        int fileLength = (int) transletFile.length();
        if (fileLength > 0) {
            try {
                FileInputStream input = new FileInputStream(transletFile);
                byte[] bytes = new byte[fileLength];
                try {
                    readFromInputStream(bytes, input, fileLength);
                    input.close();
                    bytecodes.addElement(bytes);
                    String transletParentDir = transletFile.getParent();
                    if (transletParentDir == null) {
                        transletParentDir = SecuritySupport.getSystemProperty("user.dir");
                    }
                    File transletParentFile = new File(transletParentDir);
                    final String transletAuxPrefix = transletName + FXMLLoader.EXPRESSION_PREFIX;
                    File[] auxfiles = transletParentFile.listFiles(new FilenameFilter() { // from class: com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl.1
                        @Override // java.io.FilenameFilter
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".class") && name.startsWith(transletAuxPrefix);
                        }
                    });
                    for (File auxfile : auxfiles) {
                        int auxlength = (int) auxfile.length();
                        if (auxlength > 0) {
                            try {
                                FileInputStream auxinput = new FileInputStream(auxfile);
                                byte[] bytes2 = new byte[auxlength];
                                try {
                                    readFromInputStream(bytes2, auxinput, auxlength);
                                    auxinput.close();
                                    bytecodes.addElement(bytes2);
                                } catch (IOException e2) {
                                }
                            } catch (FileNotFoundException e3) {
                            }
                        }
                    }
                    int count = bytecodes.size();
                    if (count > 0) {
                        byte[][] result = new byte[count][1];
                        for (int i2 = 0; i2 < count; i2++) {
                            result[i2] = (byte[]) bytecodes.elementAt(i2);
                        }
                        return result;
                    }
                    return (byte[][]) null;
                } catch (IOException e4) {
                    return (byte[][]) null;
                }
            } catch (FileNotFoundException e5) {
                return (byte[][]) null;
            }
        }
        return (byte[][]) null;
    }

    private byte[][] getBytecodesFromJar(Source source, String fullClassName) {
        String jarPath;
        String xslFileName = getStylesheetFileName(source);
        File xslFile = null;
        if (xslFileName != null) {
            xslFile = new File(xslFileName);
        }
        if (this._destinationDirectory != null) {
            jarPath = this._destinationDirectory + "/" + this._jarFileName;
        } else if (xslFile != null && xslFile.getParent() != null) {
            jarPath = xslFile.getParent() + "/" + this._jarFileName;
        } else {
            jarPath = this._jarFileName;
        }
        File file = new File(jarPath);
        if (!file.exists()) {
            return (byte[][]) null;
        }
        if (xslFile != null && xslFile.exists()) {
            long xslTimestamp = xslFile.lastModified();
            long transletTimestamp = file.lastModified();
            if (transletTimestamp < xslTimestamp) {
                return (byte[][]) null;
            }
        }
        try {
            ZipFile jarFile = new ZipFile(file);
            String transletPath = fullClassName.replace('.', '/');
            String transletAuxPrefix = transletPath + FXMLLoader.EXPRESSION_PREFIX;
            String transletFullName = transletPath + ".class";
            Vector bytecodes = new Vector();
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement2();
                String entryName = entry.getName();
                if (entry.getSize() > 0 && (entryName.equals(transletFullName) || (entryName.endsWith(".class") && entryName.startsWith(transletAuxPrefix)))) {
                    try {
                        InputStream input = jarFile.getInputStream(entry);
                        int size = (int) entry.getSize();
                        byte[] bytes = new byte[size];
                        readFromInputStream(bytes, input, size);
                        input.close();
                        bytecodes.addElement(bytes);
                    } catch (IOException e2) {
                        return (byte[][]) null;
                    }
                }
            }
            int count = bytecodes.size();
            if (count > 0) {
                byte[][] result = new byte[count][1];
                for (int i2 = 0; i2 < count; i2++) {
                    result[i2] = (byte[]) bytecodes.elementAt(i2);
                }
                return result;
            }
            return (byte[][]) null;
        } catch (IOException e3) {
            return (byte[][]) null;
        }
    }

    private void readFromInputStream(byte[] bytes, InputStream input, int size) throws IOException {
        int n2;
        int offset = 0;
        int i2 = size;
        while (true) {
            int length = i2;
            if (length > 0 && (n2 = input.read(bytes, offset, length)) > 0) {
                offset += n2;
                i2 = length - n2;
            } else {
                return;
            }
        }
    }

    private String getTransletBaseName(Source source) {
        String baseName;
        String transletBaseName = null;
        if (!this._transletName.equals(DEFAULT_TRANSLET_NAME)) {
            return this._transletName;
        }
        String systemId = source.getSystemId();
        if (systemId != null && (baseName = Util.baseName(systemId)) != null) {
            transletBaseName = Util.toJavaName(Util.noExtName(baseName));
        }
        return transletBaseName != null ? transletBaseName : DEFAULT_TRANSLET_NAME;
    }

    private String getStylesheetFileName(Source source) {
        String systemId = source.getSystemId();
        if (systemId != null) {
            File file = new File(systemId);
            if (file.exists()) {
                return systemId;
            }
            try {
                URL url = new URL(systemId);
                if (DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol())) {
                    return url.getFile();
                }
                return null;
            } catch (MalformedURLException e2) {
                return null;
            }
        }
        return null;
    }

    protected final XSLTCDTMManager createNewDTMManagerInstance() {
        return XSLTCDTMManager.createNewDTMManagerInstance();
    }
}
