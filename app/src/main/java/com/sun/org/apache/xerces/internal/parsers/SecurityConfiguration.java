package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/SecurityConfiguration.class */
public class SecurityConfiguration extends XIncludeAwareParserConfiguration {
    protected static final String SECURITY_MANAGER_PROPERTY = "http://apache.org/xml/properties/security-manager";

    public SecurityConfiguration() {
        this(null, null, null);
    }

    public SecurityConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    }

    public SecurityConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    }

    public SecurityConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings) {
        super(symbolTable, grammarPool, parentSettings);
        setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager(true));
    }
}
