package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/XMLGrammarPreparser.class */
public class XMLGrammarPreparser {
    private static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private static final Map<String, String> KNOWN_LOADERS;
    private static final String[] RECOGNIZED_PROPERTIES;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityResolver fEntityResolver;
    protected XMLGrammarPool fGrammarPool;
    protected Locale fLocale;
    private Map<String, XMLGrammarLoader> fLoaders;

    static {
        Map<String, String> loaders = new HashMap<>();
        loaders.put("http://www.w3.org/2001/XMLSchema", "com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader");
        loaders.put("http://www.w3.org/TR/REC-xml", "com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDLoader");
        KNOWN_LOADERS = Collections.unmodifiableMap(loaders);
        RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", ERROR_HANDLER, "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool"};
    }

    public XMLGrammarPreparser() {
        this(new SymbolTable());
    }

    public XMLGrammarPreparser(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
        this.fLoaders = new HashMap();
        this.fErrorReporter = new XMLErrorReporter();
        setLocale(Locale.getDefault());
        this.fEntityResolver = new XMLEntityManager();
    }

    public boolean registerPreparser(String grammarType, XMLGrammarLoader loader) {
        if (loader == null) {
            if (KNOWN_LOADERS.containsKey(grammarType)) {
                String loaderName = KNOWN_LOADERS.get(grammarType);
                try {
                    XMLGrammarLoader gl = (XMLGrammarLoader) ObjectFactory.newInstance(loaderName, true);
                    this.fLoaders.put(grammarType, gl);
                    return true;
                } catch (Exception e2) {
                    return false;
                }
            }
            return false;
        }
        this.fLoaders.put(grammarType, loader);
        return true;
    }

    public Grammar preparseGrammar(String type, XMLInputSource is) throws IOException, XNIException {
        if (this.fLoaders.containsKey(type)) {
            XMLGrammarLoader gl = this.fLoaders.get(type);
            gl.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
            gl.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fEntityResolver);
            gl.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
            if (this.fGrammarPool != null) {
                try {
                    gl.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
                } catch (Exception e2) {
                }
            }
            return gl.loadGrammar(is);
        }
        return null;
    }

    public void setLocale(Locale locale) {
        this.fLocale = locale;
        this.fErrorReporter.setLocale(locale);
    }

    public Locale getLocale() {
        return this.fLocale;
    }

    public void setErrorHandler(XMLErrorHandler errorHandler) throws XMLConfigurationException {
        this.fErrorReporter.setProperty(ERROR_HANDLER, errorHandler);
    }

    public XMLErrorHandler getErrorHandler() {
        return this.fErrorReporter.getErrorHandler();
    }

    public void setEntityResolver(XMLEntityResolver entityResolver) {
        this.fEntityResolver = entityResolver;
    }

    public XMLEntityResolver getEntityResolver() {
        return this.fEntityResolver;
    }

    public void setGrammarPool(XMLGrammarPool grammarPool) {
        this.fGrammarPool = grammarPool;
    }

    public XMLGrammarPool getGrammarPool() {
        return this.fGrammarPool;
    }

    public XMLGrammarLoader getLoader(String type) {
        return this.fLoaders.get(type);
    }

    public void setFeature(String featureId, boolean value) {
        for (Map.Entry<String, XMLGrammarLoader> entry : this.fLoaders.entrySet()) {
            try {
                XMLGrammarLoader gl = entry.getValue();
                gl.setFeature(featureId, value);
            } catch (Exception e2) {
            }
        }
        if (featureId.equals(CONTINUE_AFTER_FATAL_ERROR)) {
            this.fErrorReporter.setFeature(CONTINUE_AFTER_FATAL_ERROR, value);
        }
    }

    public void setProperty(String propId, Object value) {
        for (Map.Entry<String, XMLGrammarLoader> entry : this.fLoaders.entrySet()) {
            try {
                XMLGrammarLoader gl = entry.getValue();
                gl.setProperty(propId, value);
            } catch (Exception e2) {
            }
        }
    }

    public boolean getFeature(String type, String featureId) {
        XMLGrammarLoader gl = this.fLoaders.get(type);
        return gl.getFeature(featureId);
    }

    public Object getProperty(String type, String propertyId) {
        XMLGrammarLoader gl = this.fLoaders.get(type);
        return gl.getProperty(propertyId);
    }
}
