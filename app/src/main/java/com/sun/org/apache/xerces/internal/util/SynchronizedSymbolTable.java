package com.sun.org.apache.xerces.internal.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SynchronizedSymbolTable.class */
public final class SynchronizedSymbolTable extends SymbolTable {
    protected SymbolTable fSymbolTable;

    public SynchronizedSymbolTable(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public SynchronizedSymbolTable() {
        this.fSymbolTable = new SymbolTable();
    }

    public SynchronizedSymbolTable(int size) {
        this.fSymbolTable = new SymbolTable(size);
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public String addSymbol(String symbol) {
        String strAddSymbol;
        synchronized (this.fSymbolTable) {
            strAddSymbol = this.fSymbolTable.addSymbol(symbol);
        }
        return strAddSymbol;
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public String addSymbol(char[] buffer, int offset, int length) {
        String strAddSymbol;
        synchronized (this.fSymbolTable) {
            strAddSymbol = this.fSymbolTable.addSymbol(buffer, offset, length);
        }
        return strAddSymbol;
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public boolean containsSymbol(String symbol) {
        boolean zContainsSymbol;
        synchronized (this.fSymbolTable) {
            zContainsSymbol = this.fSymbolTable.containsSymbol(symbol);
        }
        return zContainsSymbol;
    }

    @Override // com.sun.org.apache.xerces.internal.util.SymbolTable
    public boolean containsSymbol(char[] buffer, int offset, int length) {
        boolean zContainsSymbol;
        synchronized (this.fSymbolTable) {
            zContainsSymbol = this.fSymbolTable.containsSymbol(buffer, offset, length);
        }
        return zContainsSymbol;
    }
}
