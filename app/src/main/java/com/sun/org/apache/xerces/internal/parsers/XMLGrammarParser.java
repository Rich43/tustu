package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/XMLGrammarParser.class */
public abstract class XMLGrammarParser extends XMLParser {
    protected DTDDVFactory fDatatypeValidatorFactory;

    protected XMLGrammarParser(SymbolTable symbolTable) throws XMLConfigurationException {
        super(new XIncludeAwareParserConfiguration());
        this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
    }
}
