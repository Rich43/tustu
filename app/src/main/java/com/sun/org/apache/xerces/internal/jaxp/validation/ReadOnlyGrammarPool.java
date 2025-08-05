package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/ReadOnlyGrammarPool.class */
final class ReadOnlyGrammarPool implements XMLGrammarPool {
    private final XMLGrammarPool core;

    public ReadOnlyGrammarPool(XMLGrammarPool pool) {
        this.core = pool;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void cacheGrammars(String grammarType, Grammar[] grammars) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void clear() {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void lockPool() {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public Grammar retrieveGrammar(XMLGrammarDescription desc) {
        return this.core.retrieveGrammar(desc);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public Grammar[] retrieveInitialGrammarSet(String grammarType) {
        return this.core.retrieveInitialGrammarSet(grammarType);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void unlockPool() {
    }
}
