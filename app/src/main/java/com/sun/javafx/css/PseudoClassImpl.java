package com.sun.javafx.css;

import javafx.css.PseudoClass;

/* loaded from: jfxrt.jar:com/sun/javafx/css/PseudoClassImpl.class */
final class PseudoClassImpl extends PseudoClass {
    private final String pseudoClassName;
    private final int index;

    PseudoClassImpl(String pseudoClassName, int index) {
        this.pseudoClassName = pseudoClassName;
        this.index = index;
    }

    @Override // javafx.css.PseudoClass
    public String getPseudoClassName() {
        return this.pseudoClassName;
    }

    public String toString() {
        return this.pseudoClassName;
    }

    public int getIndex() {
        return this.index;
    }
}
