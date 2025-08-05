package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/SimpleXMLSchema.class */
final class SimpleXMLSchema extends AbstractXMLSchema implements XMLGrammarPool {
    private static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
    private Grammar fGrammar;
    private Grammar[] fGrammars;
    private XMLGrammarDescription fGrammarDescription;

    public SimpleXMLSchema(Grammar grammar) {
        this.fGrammar = grammar;
        this.fGrammars = new Grammar[]{grammar};
        this.fGrammarDescription = grammar.getGrammarDescription();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public Grammar[] retrieveInitialGrammarSet(String grammarType) {
        return "http://www.w3.org/2001/XMLSchema".equals(grammarType) ? (Grammar[]) this.fGrammars.clone() : ZERO_LENGTH_GRAMMAR_ARRAY;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void cacheGrammars(String grammarType, Grammar[] grammars) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public Grammar retrieveGrammar(XMLGrammarDescription desc) {
        if (this.fGrammarDescription.equals(desc)) {
            return this.fGrammar;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void lockPool() {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void unlockPool() {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void clear() {
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public XMLGrammarPool getGrammarPool() {
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public boolean isFullyComposed() {
        return true;
    }
}
