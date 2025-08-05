package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchema.class */
final class XMLSchema extends AbstractXMLSchema {
    private final XMLGrammarPool fGrammarPool;

    public XMLSchema(XMLGrammarPool grammarPool) {
        this.fGrammarPool = grammarPool;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public XMLGrammarPool getGrammarPool() {
        return this.fGrammarPool;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public boolean isFullyComposed() {
        return true;
    }
}
