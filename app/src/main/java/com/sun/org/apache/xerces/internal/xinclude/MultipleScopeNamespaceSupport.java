package com.sun.org.apache.xerces.internal.xinclude;

import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import java.util.Enumeration;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/MultipleScopeNamespaceSupport.class */
public class MultipleScopeNamespaceSupport extends NamespaceSupport {
    protected int[] fScope;
    protected int fCurrentScope;

    public MultipleScopeNamespaceSupport() {
        this.fScope = new int[8];
        this.fCurrentScope = 0;
        this.fScope[0] = 0;
    }

    public MultipleScopeNamespaceSupport(NamespaceContext context) {
        super(context);
        this.fScope = new int[8];
        this.fCurrentScope = 0;
        this.fScope[0] = 0;
    }

    @Override // com.sun.org.apache.xerces.internal.util.NamespaceSupport, com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public Enumeration getAllPrefixes() {
        int count = 0;
        if (this.fPrefixes.length < this.fNamespace.length / 2) {
            String[] prefixes = new String[this.fNamespaceSize];
            this.fPrefixes = prefixes;
        }
        boolean unique = true;
        for (int i2 = this.fContext[this.fScope[this.fCurrentScope]]; i2 <= this.fNamespaceSize - 2; i2 += 2) {
            String prefix = this.fNamespace[i2];
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
        return new NamespaceSupport.Prefixes(this.fPrefixes, count);
    }

    public int getScopeForContext(int context) {
        int scope = this.fCurrentScope;
        while (context < this.fScope[scope]) {
            scope--;
        }
        return scope;
    }

    @Override // com.sun.org.apache.xerces.internal.util.NamespaceSupport, com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getPrefix(String uri) {
        return getPrefix(uri, this.fNamespaceSize, this.fContext[this.fScope[this.fCurrentScope]]);
    }

    @Override // com.sun.org.apache.xerces.internal.util.NamespaceSupport, com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getURI(String prefix) {
        return getURI(prefix, this.fNamespaceSize, this.fContext[this.fScope[this.fCurrentScope]]);
    }

    public String getPrefix(String uri, int context) {
        return getPrefix(uri, this.fContext[context + 1], this.fContext[this.fScope[getScopeForContext(context)]]);
    }

    public String getURI(String prefix, int context) {
        return getURI(prefix, this.fContext[context + 1], this.fContext[this.fScope[getScopeForContext(context)]]);
    }

    public String getPrefix(String uri, int start, int end) {
        if (uri == NamespaceContext.XML_URI) {
            return XMLSymbols.PREFIX_XML;
        }
        if (uri == NamespaceContext.XMLNS_URI) {
            return XMLSymbols.PREFIX_XMLNS;
        }
        for (int i2 = start; i2 > end; i2 -= 2) {
            if (this.fNamespace[i2 - 1] == uri && getURI(this.fNamespace[i2 - 2]) == uri) {
                return this.fNamespace[i2 - 2];
            }
        }
        return null;
    }

    public String getURI(String prefix, int start, int end) {
        if (prefix == XMLSymbols.PREFIX_XML) {
            return NamespaceContext.XML_URI;
        }
        if (prefix == XMLSymbols.PREFIX_XMLNS) {
            return NamespaceContext.XMLNS_URI;
        }
        for (int i2 = start; i2 > end; i2 -= 2) {
            if (this.fNamespace[i2 - 2] == prefix) {
                return this.fNamespace[i2 - 1];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.util.NamespaceSupport, com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public void reset() {
        this.fCurrentContext = this.fScope[this.fCurrentScope];
        this.fNamespaceSize = this.fContext[this.fCurrentContext];
    }

    public void pushScope() {
        if (this.fCurrentScope + 1 == this.fScope.length) {
            int[] contextarray = new int[this.fScope.length * 2];
            System.arraycopy(this.fScope, 0, contextarray, 0, this.fScope.length);
            this.fScope = contextarray;
        }
        pushContext();
        int[] iArr = this.fScope;
        int i2 = this.fCurrentScope + 1;
        this.fCurrentScope = i2;
        iArr[i2] = this.fCurrentContext;
    }

    public void popScope() {
        int[] iArr = this.fScope;
        int i2 = this.fCurrentScope;
        this.fCurrentScope = i2 - 1;
        this.fCurrentContext = iArr[i2];
        popContext();
    }
}
