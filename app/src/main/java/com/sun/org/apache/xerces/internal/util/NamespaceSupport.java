package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/NamespaceSupport.class */
public class NamespaceSupport implements NamespaceContext {
    protected String[] fNamespace;
    protected int fNamespaceSize;
    protected int[] fContext;
    protected int fCurrentContext;
    protected String[] fPrefixes;

    public NamespaceSupport() {
        this.fNamespace = new String[32];
        this.fContext = new int[8];
        this.fPrefixes = new String[16];
    }

    public NamespaceSupport(NamespaceContext context) {
        this.fNamespace = new String[32];
        this.fContext = new int[8];
        this.fPrefixes = new String[16];
        pushContext();
        Enumeration prefixes = context.getAllPrefixes();
        while (prefixes.hasMoreElements()) {
            String prefix = (String) prefixes.nextElement2();
            String uri = context.getURI(prefix);
            declarePrefix(prefix, uri);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public void reset() {
        this.fNamespaceSize = 0;
        this.fCurrentContext = 0;
        String[] strArr = this.fNamespace;
        int i2 = this.fNamespaceSize;
        this.fNamespaceSize = i2 + 1;
        strArr[i2] = XMLSymbols.PREFIX_XML;
        String[] strArr2 = this.fNamespace;
        int i3 = this.fNamespaceSize;
        this.fNamespaceSize = i3 + 1;
        strArr2[i3] = NamespaceContext.XML_URI;
        String[] strArr3 = this.fNamespace;
        int i4 = this.fNamespaceSize;
        this.fNamespaceSize = i4 + 1;
        strArr3[i4] = XMLSymbols.PREFIX_XMLNS;
        String[] strArr4 = this.fNamespace;
        int i5 = this.fNamespaceSize;
        this.fNamespaceSize = i5 + 1;
        strArr4[i5] = NamespaceContext.XMLNS_URI;
        this.fContext[this.fCurrentContext] = this.fNamespaceSize;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public void pushContext() {
        if (this.fCurrentContext + 1 == this.fContext.length) {
            int[] contextarray = new int[this.fContext.length * 2];
            System.arraycopy(this.fContext, 0, contextarray, 0, this.fContext.length);
            this.fContext = contextarray;
        }
        int[] iArr = this.fContext;
        int i2 = this.fCurrentContext + 1;
        this.fCurrentContext = i2;
        iArr[i2] = this.fNamespaceSize;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public void popContext() {
        int[] iArr = this.fContext;
        int i2 = this.fCurrentContext;
        this.fCurrentContext = i2 - 1;
        this.fNamespaceSize = iArr[i2];
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public boolean declarePrefix(String prefix, String uri) {
        if (prefix == XMLSymbols.PREFIX_XML || prefix == XMLSymbols.PREFIX_XMLNS) {
            return false;
        }
        for (int i2 = this.fNamespaceSize; i2 > this.fContext[this.fCurrentContext]; i2 -= 2) {
            if (this.fNamespace[i2 - 2] == prefix) {
                this.fNamespace[i2 - 1] = uri;
                return true;
            }
        }
        if (this.fNamespaceSize == this.fNamespace.length) {
            String[] namespacearray = new String[this.fNamespaceSize * 2];
            System.arraycopy(this.fNamespace, 0, namespacearray, 0, this.fNamespaceSize);
            this.fNamespace = namespacearray;
        }
        String[] strArr = this.fNamespace;
        int i3 = this.fNamespaceSize;
        this.fNamespaceSize = i3 + 1;
        strArr[i3] = prefix;
        String[] strArr2 = this.fNamespace;
        int i4 = this.fNamespaceSize;
        this.fNamespaceSize = i4 + 1;
        strArr2[i4] = uri;
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getURI(String prefix) {
        for (int i2 = this.fNamespaceSize; i2 > 0; i2 -= 2) {
            if (this.fNamespace[i2 - 2] == prefix) {
                return this.fNamespace[i2 - 1];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getPrefix(String uri) {
        for (int i2 = this.fNamespaceSize; i2 > 0; i2 -= 2) {
            if (this.fNamespace[i2 - 1] == uri && getURI(this.fNamespace[i2 - 2]) == uri) {
                return this.fNamespace[i2 - 2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public int getDeclaredPrefixCount() {
        return (this.fNamespaceSize - this.fContext[this.fCurrentContext]) / 2;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getDeclaredPrefixAt(int index) {
        return this.fNamespace[this.fContext[this.fCurrentContext] + (index * 2)];
    }

    public Iterator getPrefixes() {
        int count = 0;
        if (this.fPrefixes.length < this.fNamespace.length / 2) {
            String[] prefixes = new String[this.fNamespaceSize];
            this.fPrefixes = prefixes;
        }
        boolean unique = true;
        for (int i2 = 2; i2 < this.fNamespaceSize - 2; i2 += 2) {
            String prefix = this.fNamespace[i2 + 2];
            int k2 = 0;
            while (true) {
                if (k2 >= count) {
                    break;
                }
                if (this.fPrefixes[k2] != prefix) {
                    k2++;
                } else {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                int i3 = count;
                count++;
                this.fPrefixes[i3] = prefix;
            }
            unique = true;
        }
        return new IteratorPrefixes(this.fPrefixes, count);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public Enumeration getAllPrefixes() {
        int count = 0;
        if (this.fPrefixes.length < this.fNamespace.length / 2) {
            String[] prefixes = new String[this.fNamespaceSize];
            this.fPrefixes = prefixes;
        }
        boolean unique = true;
        for (int i2 = 2; i2 < this.fNamespaceSize - 2; i2 += 2) {
            String prefix = this.fNamespace[i2 + 2];
            int k2 = 0;
            while (true) {
                if (k2 >= count) {
                    break;
                }
                if (this.fPrefixes[k2] != prefix) {
                    k2++;
                } else {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                int i3 = count;
                count++;
                this.fPrefixes[i3] = prefix;
            }
            unique = true;
        }
        return new Prefixes(this.fPrefixes, count);
    }

    public Vector getPrefixes(String uri) {
        Vector prefixList = new Vector();
        for (int i2 = this.fNamespaceSize; i2 > 0; i2 -= 2) {
            if (this.fNamespace[i2 - 1] == uri && !prefixList.contains(this.fNamespace[i2 - 2])) {
                prefixList.add(this.fNamespace[i2 - 2]);
            }
        }
        return prefixList;
    }

    public boolean containsPrefix(String prefix) {
        for (int i2 = this.fNamespaceSize; i2 > 0; i2 -= 2) {
            if (this.fNamespace[i2 - 2] == prefix) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPrefixInCurrentContext(String prefix) {
        for (int i2 = this.fContext[this.fCurrentContext]; i2 < this.fNamespaceSize; i2 += 2) {
            if (this.fNamespace[i2] == prefix) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/NamespaceSupport$IteratorPrefixes.class */
    protected final class IteratorPrefixes implements Iterator {
        private String[] prefixes;
        private int counter = 0;
        private int size;

        public IteratorPrefixes(String[] prefixes, int size) {
            this.size = 0;
            this.prefixes = prefixes;
            this.size = size;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.counter < this.size;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.counter < this.size) {
                String[] strArr = NamespaceSupport.this.fPrefixes;
                int i2 = this.counter;
                this.counter = i2 + 1;
                return strArr[i2];
            }
            throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            for (int i2 = 0; i2 < this.size; i2++) {
                buf.append(this.prefixes[i2]);
                buf.append(" ");
            }
            return buf.toString();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/NamespaceSupport$Prefixes.class */
    protected final class Prefixes implements Enumeration {
        private String[] prefixes;
        private int counter = 0;
        private int size;

        public Prefixes(String[] prefixes, int size) {
            this.size = 0;
            this.prefixes = prefixes;
            this.size = size;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.counter < this.size;
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public Object nextElement2() {
            if (this.counter < this.size) {
                String[] strArr = NamespaceSupport.this.fPrefixes;
                int i2 = this.counter;
                this.counter = i2 + 1;
                return strArr[i2];
            }
            throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            for (int i2 = 0; i2 < this.size; i2++) {
                buf.append(this.prefixes[i2]);
                buf.append(" ");
            }
            return buf.toString();
        }
    }
}
