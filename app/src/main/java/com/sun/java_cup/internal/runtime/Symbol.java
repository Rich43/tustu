package com.sun.java_cup.internal.runtime;

import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/java_cup/internal/runtime/Symbol.class */
public class Symbol {
    public int sym;
    public int parse_state;
    boolean used_by_parser;
    public int left;
    public int right;
    public Object value;

    public Symbol(int id, int l2, int r2, Object o2) {
        this(id);
        this.left = l2;
        this.right = r2;
        this.value = o2;
    }

    public Symbol(int id, Object o2) {
        this(id);
        this.left = -1;
        this.right = -1;
        this.value = o2;
    }

    public Symbol(int sym_num, int l2, int r2) {
        this.used_by_parser = false;
        this.sym = sym_num;
        this.left = l2;
        this.right = r2;
        this.value = null;
    }

    public Symbol(int sym_num) {
        this(sym_num, -1);
        this.left = -1;
        this.right = -1;
        this.value = null;
    }

    public Symbol(int sym_num, int state) {
        this.used_by_parser = false;
        this.sym = sym_num;
        this.parse_state = state;
    }

    public String toString() {
        return FXMLLoader.CONTROLLER_METHOD_PREFIX + this.sym;
    }
}
