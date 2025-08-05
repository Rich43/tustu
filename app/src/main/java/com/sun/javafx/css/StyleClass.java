package com.sun.javafx.css;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StyleClass.class */
final class StyleClass {
    private final String styleClassName;
    private final int index;

    StyleClass(String styleClassName, int index) {
        this.styleClassName = styleClassName;
        this.index = index;
    }

    public String getStyleClassName() {
        return this.styleClassName;
    }

    public String toString() {
        return this.styleClassName;
    }

    public int getIndex() {
        return this.index;
    }
}
