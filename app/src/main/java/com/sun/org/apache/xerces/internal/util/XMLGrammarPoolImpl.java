package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLGrammarPoolImpl.class */
public class XMLGrammarPoolImpl implements XMLGrammarPool {
    protected static final int TABLE_SIZE = 11;
    protected Entry[] fGrammars;
    private static final boolean DEBUG = false;
    protected int fGrammarCount = 0;
    protected boolean fPoolIsLocked = false;

    public XMLGrammarPoolImpl() {
        this.fGrammars = null;
        this.fGrammars = new Entry[11];
    }

    public XMLGrammarPoolImpl(int initialCapacity) {
        this.fGrammars = null;
        this.fGrammars = new Entry[initialCapacity];
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public Grammar[] retrieveInitialGrammarSet(String grammarType) {
        Grammar[] toReturn;
        synchronized (this.fGrammars) {
            int grammarSize = this.fGrammars.length;
            Grammar[] tempGrammars = new Grammar[this.fGrammarCount];
            int pos = 0;
            for (int i2 = 0; i2 < grammarSize; i2++) {
                for (Entry e2 = this.fGrammars[i2]; e2 != null; e2 = e2.next) {
                    if (e2.desc.getGrammarType().equals(grammarType)) {
                        int i3 = pos;
                        pos++;
                        tempGrammars[i3] = e2.grammar;
                    }
                }
            }
            toReturn = new Grammar[pos];
            System.arraycopy(tempGrammars, 0, toReturn, 0, pos);
        }
        return toReturn;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void cacheGrammars(String grammarType, Grammar[] grammars) {
        if (!this.fPoolIsLocked) {
            for (Grammar grammar : grammars) {
                putGrammar(grammar);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public Grammar retrieveGrammar(XMLGrammarDescription desc) {
        return getGrammar(desc);
    }

    public void putGrammar(Grammar grammar) {
        if (!this.fPoolIsLocked) {
            synchronized (this.fGrammars) {
                XMLGrammarDescription desc = grammar.getGrammarDescription();
                int hash = hashCode(desc);
                int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
                for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                    if (entry.hash == hash && equals(entry.desc, desc)) {
                        entry.grammar = grammar;
                        return;
                    }
                }
                Entry entry2 = new Entry(hash, desc, grammar, this.fGrammars[index]);
                this.fGrammars[index] = entry2;
                this.fGrammarCount++;
            }
        }
    }

    public Grammar getGrammar(XMLGrammarDescription desc) {
        synchronized (this.fGrammars) {
            int hash = hashCode(desc);
            int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
            for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                if (entry.hash == hash && equals(entry.desc, desc)) {
                    return entry.grammar;
                }
            }
            return null;
        }
    }

    public Grammar removeGrammar(XMLGrammarDescription desc) {
        synchronized (this.fGrammars) {
            int hash = hashCode(desc);
            int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
            Entry prev = null;
            for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                if (entry.hash != hash || !equals(entry.desc, desc)) {
                    prev = entry;
                } else {
                    if (prev != null) {
                        prev.next = entry.next;
                    } else {
                        this.fGrammars[index] = entry.next;
                    }
                    Grammar tempGrammar = entry.grammar;
                    entry.grammar = null;
                    this.fGrammarCount--;
                    return tempGrammar;
                }
            }
            return null;
        }
    }

    public boolean containsGrammar(XMLGrammarDescription desc) {
        synchronized (this.fGrammars) {
            int hash = hashCode(desc);
            int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
            for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                if (entry.hash == hash && equals(entry.desc, desc)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void lockPool() {
        this.fPoolIsLocked = true;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void unlockPool() {
        this.fPoolIsLocked = false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public void clear() {
        for (int i2 = 0; i2 < this.fGrammars.length; i2++) {
            if (this.fGrammars[i2] != null) {
                this.fGrammars[i2].clear();
                this.fGrammars[i2] = null;
            }
        }
        this.fGrammarCount = 0;
    }

    public boolean equals(XMLGrammarDescription desc1, XMLGrammarDescription desc2) {
        return desc1.equals(desc2);
    }

    public int hashCode(XMLGrammarDescription desc) {
        return desc.hashCode();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLGrammarPoolImpl$Entry.class */
    protected static final class Entry {
        public int hash;
        public XMLGrammarDescription desc;
        public Grammar grammar;
        public Entry next;

        protected Entry(int hash, XMLGrammarDescription desc, Grammar grammar, Entry next) {
            this.hash = hash;
            this.desc = desc;
            this.grammar = grammar;
            this.next = next;
        }

        protected void clear() {
            this.desc = null;
            this.grammar = null;
            if (this.next != null) {
                this.next.clear();
                this.next = null;
            }
        }
    }
}
