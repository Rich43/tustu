package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import java.lang.ref.WeakReference;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/WeakReferenceXMLSchema.class */
final class WeakReferenceXMLSchema extends AbstractXMLSchema {
    private WeakReference fGrammarPool = new WeakReference(null);

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public synchronized XMLGrammarPool getGrammarPool() {
        XMLGrammarPool grammarPool = (XMLGrammarPool) this.fGrammarPool.get();
        if (grammarPool == null) {
            grammarPool = new SoftReferenceGrammarPool();
            this.fGrammarPool = new WeakReference(grammarPool);
        }
        return grammarPool;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public boolean isFullyComposed() {
        return false;
    }
}
