package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar;
import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDLoader;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/XMLGrammarCachingConfiguration.class */
public class XMLGrammarCachingConfiguration extends XIncludeAwareParserConfiguration {
    public static final int BIG_PRIME = 2039;
    protected static final SynchronizedSymbolTable fStaticSymbolTable = new SynchronizedSymbolTable(BIG_PRIME);
    protected static final XMLGrammarPoolImpl fStaticGrammarPool = new XMLGrammarPoolImpl();
    protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    protected XMLSchemaLoader fSchemaLoader;
    protected XMLDTDLoader fDTDLoader;

    public XMLGrammarCachingConfiguration() {
        this(fStaticSymbolTable, fStaticGrammarPool, null);
    }

    public XMLGrammarCachingConfiguration(SymbolTable symbolTable) {
        this(symbolTable, fStaticGrammarPool, null);
    }

    public XMLGrammarCachingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    }

    public XMLGrammarCachingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings) throws XMLConfigurationException {
        super(symbolTable, grammarPool, parentSettings);
        this.fSchemaLoader = new XMLSchemaLoader(this.fSymbolTable);
        this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
        this.fDTDLoader = new XMLDTDLoader(this.fSymbolTable, this.fGrammarPool);
    }

    public void lockGrammarPool() {
        this.fGrammarPool.lockPool();
    }

    public void clearGrammarPool() {
        this.fGrammarPool.clear();
    }

    public void unlockGrammarPool() {
        this.fGrammarPool.unlockPool();
    }

    public Grammar parseGrammar(String type, String uri) throws IOException, XNIException {
        XMLInputSource source = new XMLInputSource(null, uri, null);
        return parseGrammar(type, source);
    }

    public Grammar parseGrammar(String type, XMLInputSource is) throws IOException, XNIException {
        if (type.equals("http://www.w3.org/2001/XMLSchema")) {
            return parseXMLSchema(is);
        }
        if (type.equals("http://www.w3.org/TR/REC-xml")) {
            return parseDTD(is);
        }
        return null;
    }

    SchemaGrammar parseXMLSchema(XMLInputSource is) throws IOException, XMLConfigurationException {
        XMLEntityResolver resolver = getEntityResolver();
        if (resolver != null) {
            this.fSchemaLoader.setEntityResolver(resolver);
        }
        if (this.fErrorReporter.getMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN) == null) {
            this.fErrorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, new XSMessageFormatter());
        }
        this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        String propName = Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_LOCATION;
        this.fSchemaLoader.setProperty(propName, getProperty(propName));
        String propName2 = Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_NONS_LOCATION;
        this.fSchemaLoader.setProperty(propName2, getProperty(propName2));
        this.fSchemaLoader.setProperty(JAXPConstants.JAXP_SCHEMA_SOURCE, getProperty(JAXPConstants.JAXP_SCHEMA_SOURCE));
        this.fSchemaLoader.setFeature(SCHEMA_FULL_CHECKING, getFeature(SCHEMA_FULL_CHECKING));
        SchemaGrammar grammar = (SchemaGrammar) this.fSchemaLoader.loadGrammar(is);
        if (grammar != null) {
            this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", new Grammar[]{grammar});
        }
        return grammar;
    }

    DTDGrammar parseDTD(XMLInputSource is) throws IOException, XMLConfigurationException {
        XMLEntityResolver resolver = getEntityResolver();
        if (resolver != null) {
            this.fDTDLoader.setEntityResolver(resolver);
        }
        this.fDTDLoader.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        DTDGrammar grammar = (DTDGrammar) this.fDTDLoader.loadGrammar(is);
        if (grammar != null) {
            this.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[]{grammar});
        }
        return grammar;
    }
}
