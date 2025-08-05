package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/JAXPNamespaceContextWrapper.class */
public final class JAXPNamespaceContextWrapper implements NamespaceContext {
    private javax.xml.namespace.NamespaceContext fNamespaceContext;
    private SymbolTable fSymbolTable;
    private List fPrefixes;
    private final Vector fAllPrefixes = new Vector();
    private int[] fContext = new int[8];
    private int fCurrentContext;

    public JAXPNamespaceContextWrapper(SymbolTable symbolTable) {
        setSymbolTable(symbolTable);
    }

    public void setNamespaceContext(javax.xml.namespace.NamespaceContext context) {
        this.fNamespaceContext = context;
    }

    public javax.xml.namespace.NamespaceContext getNamespaceContext() {
        return this.fNamespaceContext;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() {
        return this.fSymbolTable;
    }

    public void setDeclaredPrefixes(List prefixes) {
        this.fPrefixes = prefixes;
    }

    public List getDeclaredPrefixes() {
        return this.fPrefixes;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getURI(String prefix) {
        String uri;
        if (this.fNamespaceContext == null || (uri = this.fNamespaceContext.getNamespaceURI(prefix)) == null || "".equals(uri)) {
            return null;
        }
        return this.fSymbolTable != null ? this.fSymbolTable.addSymbol(uri) : uri.intern();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getPrefix(String uri) {
        if (this.fNamespaceContext != null) {
            if (uri == null) {
                uri = "";
            }
            String prefix = this.fNamespaceContext.getPrefix(uri);
            if (prefix == null) {
                prefix = "";
            }
            return this.fSymbolTable != null ? this.fSymbolTable.addSymbol(prefix) : prefix.intern();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public Enumeration getAllPrefixes() {
        return Collections.enumeration(new TreeSet(this.fAllPrefixes));
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
        iArr[i2] = this.fAllPrefixes.size();
        if (this.fPrefixes != null) {
            this.fAllPrefixes.addAll(this.fPrefixes);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public void popContext() {
        Vector vector = this.fAllPrefixes;
        int[] iArr = this.fContext;
        int i2 = this.fCurrentContext;
        this.fCurrentContext = i2 - 1;
        vector.setSize(iArr[i2]);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public boolean declarePrefix(String prefix, String uri) {
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public int getDeclaredPrefixCount() {
        if (this.fPrefixes != null) {
            return this.fPrefixes.size();
        }
        return 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public String getDeclaredPrefixAt(int index) {
        return (String) this.fPrefixes.get(index);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
    public void reset() {
        this.fCurrentContext = 0;
        this.fContext[this.fCurrentContext] = 0;
        this.fAllPrefixes.clear();
    }
}
