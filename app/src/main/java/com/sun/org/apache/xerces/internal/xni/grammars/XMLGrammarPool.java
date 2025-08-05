package com.sun.org.apache.xerces.internal.xni.grammars;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/grammars/XMLGrammarPool.class */
public interface XMLGrammarPool {
    Grammar[] retrieveInitialGrammarSet(String str);

    void cacheGrammars(String str, Grammar[] grammarArr);

    Grammar retrieveGrammar(XMLGrammarDescription xMLGrammarDescription);

    void lockPool();

    void unlockPool();

    void clear();
}
