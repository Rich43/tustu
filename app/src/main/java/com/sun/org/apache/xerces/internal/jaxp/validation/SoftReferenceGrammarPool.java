package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLSchemaDescription;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/SoftReferenceGrammarPool.class */
final class SoftReferenceGrammarPool implements XMLGrammarPool {
    protected static final int TABLE_SIZE = 11;
    protected static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
    protected Entry[] fGrammars;
    protected int fGrammarCount = 0;
    protected final ReferenceQueue fReferenceQueue = new ReferenceQueue();
    protected boolean fPoolIsLocked = false;

    public SoftReferenceGrammarPool() {
        this.fGrammars = null;
        this.fGrammars = new Entry[11];
    }

    public SoftReferenceGrammarPool(int initialCapacity) {
        this.fGrammars = null;
        this.fGrammars = new Entry[initialCapacity];
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
    public Grammar[] retrieveInitialGrammarSet(String grammarType) {
        Grammar[] grammarArr;
        synchronized (this.fGrammars) {
            clean();
            grammarArr = ZERO_LENGTH_GRAMMAR_ARRAY;
        }
        return grammarArr;
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
                clean();
                XMLGrammarDescription desc = grammar.getGrammarDescription();
                int hash = hashCode(desc);
                int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
                for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                    if (entry.hash == hash && equals(entry.desc, desc)) {
                        if (entry.grammar.get() != grammar) {
                            entry.grammar = new SoftGrammarReference(entry, grammar, this.fReferenceQueue);
                        }
                        return;
                    }
                }
                Entry entry2 = new Entry(hash, index, desc, grammar, this.fGrammars[index], this.fReferenceQueue);
                this.fGrammars[index] = entry2;
                this.fGrammarCount++;
            }
        }
    }

    public Grammar getGrammar(XMLGrammarDescription desc) {
        synchronized (this.fGrammars) {
            clean();
            int hash = hashCode(desc);
            int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
            for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                Grammar tempGrammar = (Grammar) entry.grammar.get();
                if (tempGrammar == null) {
                    removeEntry(entry);
                } else if (entry.hash == hash && equals(entry.desc, desc)) {
                    return tempGrammar;
                }
            }
            return null;
        }
    }

    public Grammar removeGrammar(XMLGrammarDescription desc) {
        synchronized (this.fGrammars) {
            clean();
            int hash = hashCode(desc);
            int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
            for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                if (entry.hash == hash && equals(entry.desc, desc)) {
                    return removeEntry(entry);
                }
            }
            return null;
        }
    }

    public boolean containsGrammar(XMLGrammarDescription desc) {
        synchronized (this.fGrammars) {
            clean();
            int hash = hashCode(desc);
            int index = (hash & Integer.MAX_VALUE) % this.fGrammars.length;
            for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
                Grammar tempGrammar = (Grammar) entry.grammar.get();
                if (tempGrammar == null) {
                    removeEntry(entry);
                } else if (entry.hash == hash && equals(entry.desc, desc)) {
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
        if (desc1 instanceof XMLSchemaDescription) {
            if (!(desc2 instanceof XMLSchemaDescription)) {
                return false;
            }
            XMLSchemaDescription sd1 = (XMLSchemaDescription) desc1;
            XMLSchemaDescription sd2 = (XMLSchemaDescription) desc2;
            String targetNamespace = sd1.getTargetNamespace();
            if (targetNamespace != null) {
                if (!targetNamespace.equals(sd2.getTargetNamespace())) {
                    return false;
                }
            } else if (sd2.getTargetNamespace() != null) {
                return false;
            }
            String expandedSystemId = sd1.getExpandedSystemId();
            if (expandedSystemId != null) {
                if (!expandedSystemId.equals(sd2.getExpandedSystemId())) {
                    return false;
                }
                return true;
            }
            if (sd2.getExpandedSystemId() != null) {
                return false;
            }
            return true;
        }
        return desc1.equals(desc2);
    }

    public int hashCode(XMLGrammarDescription desc) {
        if (desc instanceof XMLSchemaDescription) {
            XMLSchemaDescription sd = (XMLSchemaDescription) desc;
            String targetNamespace = sd.getTargetNamespace();
            String expandedSystemId = sd.getExpandedSystemId();
            int hash = targetNamespace != null ? targetNamespace.hashCode() : 0;
            return hash ^ (expandedSystemId != null ? expandedSystemId.hashCode() : 0);
        }
        return desc.hashCode();
    }

    private Grammar removeEntry(Entry entry) {
        if (entry.prev != null) {
            entry.prev.next = entry.next;
        } else {
            this.fGrammars[entry.bucket] = entry.next;
        }
        if (entry.next != null) {
            entry.next.prev = entry.prev;
        }
        this.fGrammarCount--;
        entry.grammar.entry = null;
        return (Grammar) entry.grammar.get();
    }

    private void clean() {
        Reference referencePoll = this.fReferenceQueue.poll();
        while (true) {
            Reference ref = referencePoll;
            if (ref != null) {
                Entry entry = ((SoftGrammarReference) ref).entry;
                if (entry != null) {
                    removeEntry(entry);
                }
                referencePoll = this.fReferenceQueue.poll();
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/SoftReferenceGrammarPool$Entry.class */
    static final class Entry {
        public int hash;
        public int bucket;
        public Entry prev;
        public Entry next;
        public XMLGrammarDescription desc;
        public SoftGrammarReference grammar;

        protected Entry(int hash, int bucket, XMLGrammarDescription desc, Grammar grammar, Entry next, ReferenceQueue queue) {
            this.hash = hash;
            this.bucket = bucket;
            this.prev = null;
            this.next = next;
            if (next != null) {
                next.prev = this;
            }
            this.desc = desc;
            this.grammar = new SoftGrammarReference(this, grammar, queue);
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

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/SoftReferenceGrammarPool$SoftGrammarReference.class */
    static final class SoftGrammarReference extends SoftReference {
        public Entry entry;

        protected SoftGrammarReference(Entry entry, Grammar grammar, ReferenceQueue queue) {
            super(grammar, queue);
            this.entry = entry;
        }
    }
}
