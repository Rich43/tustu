package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.util.ShadowedSymbolTable;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/CachingParserPool.class */
public class CachingParserPool {
    public static final boolean DEFAULT_SHADOW_SYMBOL_TABLE = false;
    public static final boolean DEFAULT_SHADOW_GRAMMAR_POOL = false;
    protected SymbolTable fSynchronizedSymbolTable;
    protected XMLGrammarPool fSynchronizedGrammarPool;
    protected boolean fShadowSymbolTable;
    protected boolean fShadowGrammarPool;

    public CachingParserPool() {
        this(new SymbolTable(), new XMLGrammarPoolImpl());
    }

    public CachingParserPool(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this.fShadowSymbolTable = false;
        this.fShadowGrammarPool = false;
        this.fSynchronizedSymbolTable = new SynchronizedSymbolTable(symbolTable);
        this.fSynchronizedGrammarPool = new SynchronizedGrammarPool(grammarPool);
    }

    public SymbolTable getSymbolTable() {
        return this.fSynchronizedSymbolTable;
    }

    public XMLGrammarPool getXMLGrammarPool() {
        return this.fSynchronizedGrammarPool;
    }

    public void setShadowSymbolTable(boolean shadow) {
        this.fShadowSymbolTable = shadow;
    }

    public DOMParser createDOMParser() {
        SymbolTable symbolTable = this.fShadowSymbolTable ? new ShadowedSymbolTable(this.fSynchronizedSymbolTable) : this.fSynchronizedSymbolTable;
        XMLGrammarPool grammarPool = this.fShadowGrammarPool ? new ShadowedGrammarPool(this.fSynchronizedGrammarPool) : this.fSynchronizedGrammarPool;
        return new DOMParser(symbolTable, grammarPool);
    }

    public SAXParser createSAXParser() {
        SymbolTable symbolTable = this.fShadowSymbolTable ? new ShadowedSymbolTable(this.fSynchronizedSymbolTable) : this.fSynchronizedSymbolTable;
        XMLGrammarPool grammarPool = this.fShadowGrammarPool ? new ShadowedGrammarPool(this.fSynchronizedGrammarPool) : this.fSynchronizedGrammarPool;
        return new SAXParser(symbolTable, grammarPool);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/CachingParserPool$SynchronizedGrammarPool.class */
    public static final class SynchronizedGrammarPool implements XMLGrammarPool {
        private XMLGrammarPool fGrammarPool;

        public SynchronizedGrammarPool(XMLGrammarPool grammarPool) {
            this.fGrammarPool = grammarPool;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar[] retrieveInitialGrammarSet(String grammarType) {
            Grammar[] grammarArrRetrieveInitialGrammarSet;
            synchronized (this.fGrammarPool) {
                grammarArrRetrieveInitialGrammarSet = this.fGrammarPool.retrieveInitialGrammarSet(grammarType);
            }
            return grammarArrRetrieveInitialGrammarSet;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar retrieveGrammar(XMLGrammarDescription gDesc) {
            Grammar grammarRetrieveGrammar;
            synchronized (this.fGrammarPool) {
                grammarRetrieveGrammar = this.fGrammarPool.retrieveGrammar(gDesc);
            }
            return grammarRetrieveGrammar;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void cacheGrammars(String grammarType, Grammar[] grammars) {
            synchronized (this.fGrammarPool) {
                this.fGrammarPool.cacheGrammars(grammarType, grammars);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void lockPool() {
            synchronized (this.fGrammarPool) {
                this.fGrammarPool.lockPool();
            }
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void clear() {
            synchronized (this.fGrammarPool) {
                this.fGrammarPool.clear();
            }
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void unlockPool() {
            synchronized (this.fGrammarPool) {
                this.fGrammarPool.unlockPool();
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/CachingParserPool$ShadowedGrammarPool.class */
    public static final class ShadowedGrammarPool extends XMLGrammarPoolImpl {
        private XMLGrammarPool fGrammarPool;

        public ShadowedGrammarPool(XMLGrammarPool grammarPool) {
            this.fGrammarPool = grammarPool;
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl, com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar[] retrieveInitialGrammarSet(String grammarType) {
            Grammar[] grammars = super.retrieveInitialGrammarSet(grammarType);
            return grammars != null ? grammars : this.fGrammarPool.retrieveInitialGrammarSet(grammarType);
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl, com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar retrieveGrammar(XMLGrammarDescription gDesc) {
            Grammar g2 = super.retrieveGrammar(gDesc);
            return g2 != null ? g2 : this.fGrammarPool.retrieveGrammar(gDesc);
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl, com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void cacheGrammars(String grammarType, Grammar[] grammars) {
            super.cacheGrammars(grammarType, grammars);
            this.fGrammarPool.cacheGrammars(grammarType, grammars);
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl
        public Grammar getGrammar(XMLGrammarDescription desc) {
            if (super.containsGrammar(desc)) {
                return super.getGrammar(desc);
            }
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl
        public boolean containsGrammar(XMLGrammarDescription desc) {
            return super.containsGrammar(desc);
        }
    }
}
