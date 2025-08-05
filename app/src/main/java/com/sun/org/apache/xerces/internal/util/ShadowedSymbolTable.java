package com.sun.org.apache.xerces.internal.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/ShadowedSymbolTable.class */
public final class ShadowedSymbolTable extends SymbolTable {
    protected SymbolTable fSymbolTable;

    public ShadowedSymbolTable(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public String addSymbol(String symbol) {
        if (this.fSymbolTable.containsSymbol(symbol)) {
            return this.fSymbolTable.addSymbol(symbol);
        }
        return super.addSymbol(symbol);
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public String addSymbol(char[] buffer, int offset, int length) {
        if (this.fSymbolTable.containsSymbol(buffer, offset, length)) {
            return this.fSymbolTable.addSymbol(buffer, offset, length);
        }
        return super.addSymbol(buffer, offset, length);
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public int hash(String symbol) {
        return this.fSymbolTable.hash(symbol);
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public int hash(char[] buffer, int offset, int length) {
        return this.fSymbolTable.hash(buffer, offset, length);
    }
}
